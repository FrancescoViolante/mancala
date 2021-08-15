package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import bol.mancala.repositories.GameRepo;
import bol.mancala.utils.GameUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class GameServiceImpl implements GameService {

    private final GameRepo gameRepo;

    public Game initializeBoard(int playerAmount) {


        List<Pit> pits = createPits(playerAmount);
        Game game = Game.builder().playerAmount(playerAmount)
                .playerWhoMove(calculatePlayerWhoStart(playerAmount))
                .pits(pits).build();

        pits.forEach(pit -> pit.setGame(game));
        GameUtil.calculatePlayerEnum(new LinkedList<>(pits));
        return game;

    }


    public Game moveStones(MovePitRequestModel movePitRequestModel) {

        Optional<Game> retrievedGame = gameRepo.findById(movePitRequestModel.getGameId());
        checkGameIdProvided(retrievedGame);

        Game game = retrievedGame.get();
        //0 is not possible because is not clickable in FE
        Predicate<Pit> pitClickedPredicate = pit -> pit.getPosition() == movePitRequestModel.getPositionClicked();
        checkClickedPosition(game, pitClickedPredicate);
        GameUtil.setNextPitPosition(new LinkedList<>(game.getPits()));
        setNotUpdatablePits(new LinkedList<>(game.getPits()), movePitRequestModel, pitClickedPredicate);
        calculateNextTurnGame(game, movePitRequestModel, pitClickedPredicate);


        return game;
    }

    public Game saveOrUpdateGameInDataBase(Game game) {
        return gameRepo.save(game);
    }

    private void setNotUpdatablePits(LinkedList<Pit> pits, MovePitRequestModel movePitRequestModel, Predicate<Pit> pitClickedPredicate) {

        Predicate<Pit> bigPitsOfOtherPlayerPredicate = pit ->
                (!pit.getPlayer().equals(movePitRequestModel.getPlayerWhoMoved())
                        && pit.isBigPit());


        pits.forEach(pit -> {
            pit.setUpdatablePit(!pitClickedPredicate.test(pit) && !bigPitsOfOtherPlayerPredicate.test(pit));
        });
    }

    private void calculateNextTurnGame(Game game, MovePitRequestModel movePitRequestModel, Predicate<Pit> pitClickedPredicate) {


        Pit clickedPit = findPitClickedInPitList(game, pitClickedPredicate);
        LinkedList<Pit> orderedPits = ricreatePitListStartingFromPositionInInput(game.getPits(), pitClickedPredicate);

        updatePitStones(game, movePitRequestModel.getPlayerWhoMoved(), clickedPit, orderedPits);
        updateClickedOrStealedPitStonesToZero(clickedPit);
    }

    private void updatePitStones(Game game, PlayerEnum playerWhoMoved, Pit clickedPit, LinkedList<Pit> orderedPits) {
        int i = 0;

        int positionNextElementToUpdate = calculateFirstPositionNextPitToUpdate(orderedPits.element());
        while (i < clickedPit.getStones()) {

            int finalPositionNextElementToUpdate = positionNextElementToUpdate;
            Pit pitToUpdate = orderedPits.stream().filter(e -> e.getPosition() == finalPositionNextElementToUpdate).findFirst().get();

            incrementStoneNextPitByOne(pitToUpdate);

            Predicate<Pit> nextPositionPredicate = pit -> pit.getPosition() == calculateFirstPositionNextPitToUpdate(pitToUpdate);

            positionNextElementToUpdate = calculatePositionNextPitToUpdate(orderedPits, nextPositionPredicate);
            i++;
            if(isLastPitToUpdated(clickedPit, i)){
                calculateNextPlayerWhoMove(game, playerWhoMoved, pitToUpdate);
                stealPitsOpponent(game, playerWhoMoved, pitToUpdate);
            }

        }
    }

    private void stealPitsOpponent(Game game, PlayerEnum playerWhoMoved, Pit pitToUpdate) {
        if (hasPitSamePlayerWhoMoved(playerWhoMoved, pitToUpdate)
                && isNotABigPit(pitToUpdate) && isAPitWithOneStone(pitToUpdate)) {
            reversePits(game, pitToUpdate);
        }

    }

    private boolean isNotABigPit(Pit pitToUpdate) {
        return !pitToUpdate.isBigPit();
    }

    private void reversePits(Game game, Pit pitToUpdate) {
        List<Pit> pits = game.getPits();
        int indexOfPitStealer = pits.stream().filter(this::isNotABigPit).collect(Collectors.toList()).indexOf(pitToUpdate);

        Collections.reverse(pits);

        Pit oppositePit = game.getPits().stream().filter(this::isNotABigPit)
                .filter(pit ->  pits.indexOf(pit) - 2 == indexOfPitStealer).findFirst().orElseThrow();
        steal(game, pitToUpdate, oppositePit );
    }

    private void steal(Game game, Pit stealerPit, Pit oppositePit) {

        if(!isAPitWithoutStones(oppositePit)){
           int stonesToAddToOpponentBigPit =  oppositePit.getStones() + stealerPit.getStones();

            Collections.reverse(game.getPits());
            Pit bigPitToUpdate = game.getPits().stream().filter(Pit::isBigPit)
                    .filter(pit -> hasPitSamePlayerWhoMoved(pit.getPlayer(), stealerPit)).findFirst().orElseThrow();
            bigPitToUpdate.setStones(Integer.sum(bigPitToUpdate.getStones(),stonesToAddToOpponentBigPit));
            updateClickedOrStealedPitStonesToZero(oppositePit);
            updateClickedOrStealedPitStonesToZero(stealerPit);
        }
    }


    private boolean isAPitWithoutStones(Pit pitToUpdate) {
        return pitToUpdate.getStones() == 0;
    }
    private boolean isAPitWithOneStone(Pit pitToUpdate) {
        return pitToUpdate.getStones() == 1;
    }

    private int calculateFirstPositionNextPitToUpdate(Pit element) {
        return element.getPositionNextElement();
    }

    private int calculatePositionNextPitToUpdate(LinkedList<Pit> orderedPits, Predicate<Pit> nextPositionPredicate) {
        return ricreatePitListStartingFromPositionInInput(orderedPits, nextPositionPredicate)
                .stream().filter(Pit::getUpdatablePit)
                .findFirst().get().getPosition();
    }

    private void calculateNextPlayerWhoMove(Game game, PlayerEnum playerWhoMoved, Pit pitToUpdate) {
        if (hasPitSamePlayerWhoMoved(playerWhoMoved, pitToUpdate)
                && pitToUpdate.isBigPit()) {
            game.setPlayerWhoMove(playerWhoMoved);
        } else {
            game.setPlayerWhoMove(PlayerEnum.getNextPlayerEnum(game.getPlayerAmount(), game.getPlayerWhoMove()));
        }
    }

    private boolean hasPitSamePlayerWhoMoved(PlayerEnum playerWhoMoved, Pit pitToCheck) {
        return pitToCheck.getPlayer() == playerWhoMoved;
    }

    private boolean isLastPitToUpdated(Pit clickedPit, int i) {
        return i == clickedPit.getStones();
    }

    private void incrementStoneNextPitByOne(Pit pitToUpdate) {
        pitToUpdate.setStones(Integer.sum(pitToUpdate.getStones(), INCREMENT_STONE));
    }

    private Pit findPitClickedInPitList(Game game, Predicate<Pit> pitClickedPredicate) {
        return game.getPits().stream()
                .filter(pitClickedPredicate).findFirst().orElseThrow();
    }

    private void updateClickedOrStealedPitStonesToZero(Pit clickedPit) {
        clickedPit.setStones(0);
    }

    private LinkedList<Pit> ricreatePitListStartingFromPositionInInput(List<Pit> pits, Predicate<Pit> searchPitPredicate) {
        Pit positionOfPitSearched = pits.stream().filter(searchPitPredicate).findFirst().get();
        LinkedList<Pit> orderedListByPositionInInput = new LinkedList<>();
        List<Pit> pitsWithPositionMinorOfPitSearched = pits.subList(INITIAL_POSITION, pits.indexOf(positionOfPitSearched));
        ListIterator<Pit> listIterator = pits.listIterator(pits.indexOf(positionOfPitSearched));
        while (listIterator.hasNext()) {
            orderedListByPositionInInput.add(listIterator.next());
        }
        orderedListByPositionInInput.addAll(pitsWithPositionMinorOfPitSearched);

        return orderedListByPositionInInput;
    }


    private PlayerEnum calculatePlayerWhoStart(int playerAmount) {
        int randomPlayerEnumValue = new Random().ints(FIRST_PLAYER, playerAmount+1)
                .findFirst().orElse(1);

        return PlayerEnum.getPlayerEnumByValue(randomPlayerEnumValue);
    }

    private void checkClickedPosition(Game game, Predicate<Pit> positionClicked) {

        long positionProvidedOccurrenciesInGame = game.getPits().stream()
                .filter(positionClicked)
                .count();

        if (positionProvidedOccurrenciesInGame != POSITION_CLICKED_IS_PRESENT)
            throw new IllegalArgumentException("Position clicked is not present.");

    }


    private void checkGameIdProvided(Optional<Game> retrivedGame) {
        if (retrivedGame.isEmpty())
            throw new IllegalArgumentException("Game not present.");
    }


    private LinkedList<Pit> createPits(int playerAmount) {

        int pitNumber = calculatePitNumber(playerAmount);
        LinkedList<Pit> pits = new LinkedList<>();
        for (int i = 0; i < pitNumber; i++) {
            pits.add(Pit.builder()
                    .stones(INITIAL_STONES_PIT)
                    .position(i)
                    .build());
        }
        calculateBigPit(pits, pitNumber);
        return pits;
    }

    private int calculatePitNumber(int playerAmount) {
        return playerAmount * 7;
    }

    private void calculateBigPit(LinkedList<Pit> pits, int pitNumber) {

        List<Integer> bigPitPositions = new java.util.ArrayList<>(Collections.singletonList(FIRT_BIGPIT_POSITION));
        int j = 6;
        while (j < pitValueStartingFromZero(pitNumber)) {
            j += POSITION_TO_ADD;
            bigPitPositions.add(j);
        }

        pits.stream()
                .filter(pit -> bigPitPositions.contains(pit.getPosition()))
                .forEach(pit -> {
                    pit.setBigPit(true);
                    pit.setStones(INITIAL_STONES_BIGPIT);
                });
    }

    private int pitValueStartingFromZero(int pitNumber) {
        return pitNumber - 1;
    }


    public GameServiceImpl(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }
}
