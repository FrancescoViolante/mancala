package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import bol.mancala.repositories.GameRepo;
import bol.mancala.utils.GameUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;


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
        closeGameIfLastTurn(game);

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
        LinkedList<Pit> orderedPits = recreatePitListStartingFromPositionInInput(game.getPits(), pitClickedPredicate);

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
            if (isLastPitToUpdated(clickedPit, i)) {
                calculateNextPlayerWhoMove(game, playerWhoMoved, pitToUpdate);
                checkIfOpponentStonesCanBeStealed(game, playerWhoMoved, pitToUpdate);
            }

        }
    }

    private void checkIfOpponentStonesCanBeStealed(Game game, PlayerEnum playerWhoMoved, Pit pitToUpdate) {
        if (hasPitSamePlayerWhoMoved(playerWhoMoved, pitToUpdate)
                && isNotABigPit(pitToUpdate) && isAPitWithOneStone(pitToUpdate)) {
            createPitsForPlayerWithoutBigPits(game, pitToUpdate);
        }

    }

    private boolean isNotABigPit(Pit pitToUpdate) {
        return !pitToUpdate.isBigPit();
    }

    private void createPitsForPlayerWithoutBigPits(Game game, Pit pitToUpdate) {

        List<Pit> pitsWithoutBigPits = game.getPits().stream().filter(this::isNotABigPit).collect(toList());
        List<Pit> playerP1Pits = pitsWithoutBigPits.stream().filter(pit -> pit.getPlayer().equals(PlayerEnum.P1)).collect(toList());
        List<Pit> playerP2Pits = pitsWithoutBigPits.stream().filter(pit -> pit.getPlayer().equals(PlayerEnum.P2))
                .sorted(Comparator.comparing(Pit::getPosition).reversed())
                .collect(toList());

        stealStonesOfOpponent(game, pitToUpdate,  calculateOppositePitOfClickedPit(pitToUpdate, playerP1Pits, playerP2Pits));
    }

    private Pit calculateOppositePitOfClickedPit(Pit pitToUpdate, List<Pit> p1List, List<Pit> p2List) {


        switch (pitToUpdate.getPlayer()) {
            case P1: {
               return p2List.get(p1List.indexOf(pitToUpdate));
            }
            case P2: {
                return  p1List.get(p2List.indexOf(pitToUpdate));
            }
            default:
                throw new IllegalArgumentException(String.format("Invalid player %s", pitToUpdate.getPlayer()));
        }
    }

    private void stealStonesOfOpponent(Game game, Pit stealerPit, Pit oppositePit) {

        if (!isAPitWithoutStones(oppositePit)) {
            int stonesToAddToOpponentBigPit = oppositePit.getStones() + stealerPit.getStones();

            Pit bigPitToUpdate = game.getPits().stream().filter(Pit::isBigPit)
                    .filter(pit -> pit.getPlayer().equals(stealerPit.getPlayer())).findFirst().orElseThrow();
            bigPitToUpdate.setStones(sumStonesToPit(stonesToAddToOpponentBigPit, bigPitToUpdate));
            updateClickedOrStealedPitStonesToZero(oppositePit);
            updateClickedOrStealedPitStonesToZero(stealerPit);
        }
    }

    private int sumStonesToPit(int stonesToAddToOpponentBigPit, Pit bigPitToUpdate) {
        return Integer.sum(bigPitToUpdate.getStones(), stonesToAddToOpponentBigPit);
    }


    private void resetStones(List<Pit> pits) {
        pits.forEach( pit -> pit.setStones(INITIAL_STONES_BIGPIT));
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
        return recreatePitListStartingFromPositionInInput(orderedPits, nextPositionPredicate)
                .stream().filter(Pit::getUpdatablePit)
                .findFirst().get().getPosition();
    }

    private void calculateNextPlayerWhoMove(Game game, PlayerEnum playerWhoMoved, Pit pitToUpdate) {
        if (hasPitSamePlayerWhoMoved(playerWhoMoved, pitToUpdate)
                && pitToUpdate.isBigPit()) {
            game.setPlayerWhoMove(playerWhoMoved);
        } else {
            game.setPlayerWhoMove(PlayerEnum.getNextPlayerEnum(game.getPlayerAmount(), playerWhoMoved));
        }
    }

    private boolean hasPitSamePlayerWhoMoved(PlayerEnum playerWhoMoved, Pit pitToCheck) {
        return pitToCheck.getPlayer() == playerWhoMoved;
    }

    private boolean isLastPitToUpdated(Pit clickedPit, int i) {
        return i == clickedPit.getStones();
    }

    private void incrementStoneNextPitByOne(Pit pitToUpdate) {
        pitToUpdate.setStones(sumStonesToPit(INCREMENT_STONE, pitToUpdate));
    }

    private Pit findPitClickedInPitList(Game game, Predicate<Pit> pitClickedPredicate) {
        return game.getPits().stream()
                .filter(pitClickedPredicate).findFirst().orElseThrow();
    }

    private void updateClickedOrStealedPitStonesToZero(Pit clickedPit) {
        clickedPit.setStones(0);
    }

    private LinkedList<Pit> recreatePitListStartingFromPositionInInput(List<Pit> pits, Predicate<Pit> searchPitPredicate) {
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
        int randomPlayerEnumValue = new Random().ints(FIRST_PLAYER, playerAmount + 1)
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


    private void closeGameIfLastTurn(Game game) {
        Map<PlayerEnum, List<Pit>> mapOfPitsByPlayerEnum = game.getPits().stream().filter(pit -> !pit.isBigPit())
                .collect(groupingBy(Pit::getPlayer, mapping(Function.identity(), toList())));
        AtomicBoolean stonesOfOnePlayerAllZero= new AtomicBoolean(false);

        mapOfPitsByPlayerEnum.forEach((k, v) -> {
            if(!stonesOfOnePlayerAllZero.get()){
                stonesOfOnePlayerAllZero.set(checkIfAllStonesOfPlayerAreZero(v));
            }
        });
        sumRemainingStonesToBigPits(game, mapOfPitsByPlayerEnum, stonesOfOnePlayerAllZero);
    }

    private void sumRemainingStonesToBigPits(Game game, Map<PlayerEnum, List<Pit>> mapOfPitsByPlayerEnum, AtomicBoolean stonesOfOnePlayerAllZero) {
        if(stonesOfOnePlayerAllZero.get()){

            mapOfPitsByPlayerEnum.forEach((k,v) -> {
                Pit bigPitByPlayerEnum = retrieveBigPitByPlayerEnum(game, k);
                int stonesToAdd = calculateRemainingStones(v);
                bigPitByPlayerEnum.setStones(sumStonesToPit(stonesToAdd, bigPitByPlayerEnum));
            });
            resetStones(game.getPits().stream()
                    .filter(pit -> !pit.isBigPit()).collect(toList()));
        }
    }

    private Pit retrieveBigPitByPlayerEnum(Game game, PlayerEnum k) {
        return game.getPits().stream().filter(Pit::isBigPit).filter(pit -> pit.getPlayer() == k).findFirst().get();
    }

    private int calculateRemainingStones(List<Pit> v) {
        int stonesToAdd = v.stream().mapToInt(Pit::getStones).sum();
        return stonesToAdd;
    }


    private boolean checkIfAllStonesOfPlayerAreZero(List<Pit> v) {
        return v.stream().map(Pit::getStones).allMatch(value -> value == 0);
    }


    public GameServiceImpl(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }
}
