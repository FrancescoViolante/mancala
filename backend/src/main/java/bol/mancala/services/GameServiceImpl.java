package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import bol.mancala.repositories.GameRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Log4j2
@Service
public class GameServiceImpl implements GameService {

    private GameRepo gameRepo;

    public Game initializeBoard(int playerAmount) {


        List<Pit> pits = createPits(playerAmount);
        Game game = Game.builder().playerAmount(playerAmount)
                .playerWhoMove(calculatePlayerWhoStart(playerAmount))
                .pits(pits).build();

        pits.forEach(pit -> pit.setGame(game));
        return gameRepo.save(game);

    }


    public Game moveStones(MovePitRequestModel movePitRequestModel) {

        Optional<Game> retrievedGame = gameRepo.findById(movePitRequestModel.getGameId());
        checkGameIdProvided(retrievedGame);

        Game game = retrievedGame.get();
        checkClickedPosition(game, movePitRequestModel.getPositionClicked());
        setAfterPitId(new LinkedList<>(game.getPits()));

        //0 is not possible because is not clickable in FE
        Predicate<Pit> pitClickedPredicate = pit -> pit.getPosition() == movePitRequestModel.getPositionClicked();

        setNotUpdatablePits(new LinkedList<>(game.getPits()), movePitRequestModel, pitClickedPredicate);
        updateNextPositionStones(game, movePitRequestModel, pitClickedPredicate);
        return null;
    }

    private void setNotUpdatablePits(LinkedList<Pit> pits, MovePitRequestModel movePitRequestModel, Predicate<Pit> pitClickedPredicate) {


        Predicate<Pit> bigPitsOfOtherPlayerPredicate = pit ->
                (!pit.getPlayer().equals(movePitRequestModel.getPlayerWhoMoved())
                        && pit.isBigPit());


        pits.forEach(pit -> {
            if (pitClickedPredicate.test(pit) || bigPitsOfOtherPlayerPredicate.test(pit)) {
                pit.setUpdatablePit(false);
            } else
                pit.setUpdatablePit(true);
        });
    }

    private void setAfterPitId(LinkedList<Pit> pits) {
        Pit lastPit = pits.peekLast();

        pits.forEach(
                (element) -> {
                    if (!element.equals(lastPit))
                        element.setPositionNextElement(pits.indexOf(element) + 1);
                    else
                        element.setPositionNextElement(0);
                }
        );
    }

    private void updateNextPositionStones(Game game, MovePitRequestModel movePitRequestModel, Predicate<Pit> pitClickedPredicate) {


        Pit clickedPit =game.getPits().stream()
                .filter(pitClickedPredicate).findFirst().orElseThrow();

        int valueOfStonesPresentInClickedPit = clickedPit.getStones();

        LinkedList<Pit> orderedPits = ricreatePitListStartingFromPositionInInput(game.getPits(), movePitRequestModel.getPositionClicked(), pitClickedPredicate);

        //SIMULIAMO SIANO 20
        //valueOfStonesPresentInClickedPit = 21;

        int i = 0;
        int positionNextElementToUpdate = orderedPits.element().getPositionNextElement();
        while (i < valueOfStonesPresentInClickedPit) {

            int finalPositionNextElementToUpdate = positionNextElementToUpdate;
            Pit pitToUpdate = orderedPits.stream().filter(e -> e.getPosition() == finalPositionNextElementToUpdate).findFirst().get();

            pitToUpdate.setStones(Integer.sum(pitToUpdate.getStones(), INCREMENT_STONE));

            Predicate<Pit> nextPositionPredicate = pit -> pit.getPosition() == pitToUpdate.getPositionNextElement();

            positionNextElementToUpdate = ricreatePitListStartingFromPositionInInput(orderedPits, pitToUpdate.getPositionNextElement(), nextPositionPredicate)
                                        .stream().filter(Pit::getUpdatablePit)
                                        .findFirst().get().getPosition();

            i++;
        }

        updateClickedStonesToZero(clickedPit);

    }

    private void updateClickedStonesToZero(Pit clickedPit) {
        clickedPit.setStones(0);
    }

    private LinkedList<Pit> ricreatePitListStartingFromPositionInInput(List<Pit> pits, Integer positionClicked, Predicate<Pit> pitClickedPredicate) {
        Pit positionOfElementClicked = pits.stream().filter(pitClickedPredicate).findFirst().get();
        LinkedList<Pit> orderedListByPositionInInput = new LinkedList<>();
        List<Pit> pitWithPositionMinorOfClickedPosition = pits.subList(INITIAL_POSITION, pits.indexOf(positionOfElementClicked));
        ListIterator<Pit> listIterator = pits.listIterator(pits.indexOf(positionOfElementClicked));
        while (listIterator.hasNext()) {
            orderedListByPositionInInput.add(listIterator.next());
        }
        orderedListByPositionInInput.addAll(pitWithPositionMinorOfClickedPosition);

        return orderedListByPositionInInput;
    }


    private PlayerEnum calculatePlayerWhoStart(int playerAmount) {
        int randomPlayerEnumValue = new Random().ints(FIRST_PLAYER, playerAmount)
                .findFirst().orElse(1);

        return PlayerEnum.getPlayerEnumByValue(randomPlayerEnumValue);
    }

    private void checkClickedPosition(Game game, Integer positionClicked) {

        long positionProvidedOccurrenciesInGame = game.getPits().stream()
                .map(Pit::getPosition)
                .filter(position -> position.equals(positionClicked))
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

    @Autowired
    public void setGameRepo(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

}
