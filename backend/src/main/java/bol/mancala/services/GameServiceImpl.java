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
        checkClickedPosition(retrievedGame.get(), movePitRequestModel.getPositionClicked());

        return null;
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
            throw new IllegalArgumentException("Invalid position clicked.");

    }


    private void checkGameIdProvided(Optional<Game> retrivedGame) {
        if (retrivedGame.isEmpty())
            throw new IllegalArgumentException("Game not present in DB.");
    }


    private LinkedList<Pit> createPits(int playerAmount) {

        int pitNumber = calculatePitNumber(playerAmount);
        LinkedList<Pit> pits = new LinkedList<>();
        for (int i = 0; i < pitNumber; i++) {
            pits.add(Pit.builder()
                    .stones(INITIAL_STONES_PIT)
                    .position(i)
                    //  .game(game)
                    .build());
        }
        calculateBigPit(pits, pitNumber);
        return pits;
    }

    private Game createGame(int playerAmount) {

        return Game.builder().playerAmount(playerAmount).build();
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
