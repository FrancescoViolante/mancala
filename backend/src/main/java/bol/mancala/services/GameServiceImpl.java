package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import bol.mancala.repositories.GameRepo;
import bol.mancala.repositories.PitRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class GameServiceImpl implements GameService {

    public static final int POSITION_TO_ADD = 7;
    public static final int INITIAL_STONES = 6;
    public static final int INITIAL_STONES_BIGPIT = 0;
    private GameRepo gameRepo;
    private PitRepo pitRepo;

    public Game initializeBoard(int playerAmount) {


        List<Pit> pits = createPits(playerAmount);
        Game game = Game.builder().playerAmount(playerAmount).pits(pits).build();
        pits.forEach(pit -> pit.setGame(game));
        Game game2 = gameRepo.save(game);

        log.info(pitRepo.findAllByGame_GameId(game2.getGameId()));
        log.info(game2);
        return game2;
    }

    public Game moveStones(MovePitRequestModel movePitRequestModel) {

      /*  Optional<Game> retrivedGame = gameRepo.findById(movePitRequestModel.getGameId());

        checkGameIdProvided(retrivedGame);
        Game game = retrivedGame.isPresent() : retrivedGame.get();
        checkClickedPosition();*/
        return null;
    }

    private void checkClickedPosition(Optional<Game> retrivedGame) {

     /*   if(retrivedGame.get()){
            throw  new IllegalArgumentException("Game not present in DB.");
        }*/
    }

    private void checkGameIdProvided(Optional<Game> retrivedGame) {
/*
        retrivedGame.ifPresent(
                (game) ->   game)
        ()-> throw  new IllegalArgumentException("Game not present in DB."));


        ); ? throw  new IllegalArgumentException("Game not present in DB.") :
        if(retrivedGame.isEmpty()){
            throw  new IllegalArgumentException("Game not present in DB.");
        }*/

    }

    private LinkedList<Pit> createPits(int playerAmount) {

        int pitNumber = calculatePitNumber(playerAmount);
        LinkedList<Pit> pits = new LinkedList<>();
        for (int i = 0; i < pitNumber; i++) {
            pits.add(Pit.builder()
                    .stones(INITIAL_STONES)
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

        List<Integer> bigPitPositions = new java.util.ArrayList<>(Collections.singletonList(6));
        int j = 6;
        while (j < pitNumber - 1) {
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

    @Autowired
    public void setGameRepo(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    @Autowired
    public void setPitRepo(bol.mancala.repositories.PitRepo pitRepo) {
        this.pitRepo = pitRepo;
    }
}
