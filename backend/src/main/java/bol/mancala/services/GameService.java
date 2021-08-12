package bol.mancala.services;

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

@Log4j2
@Service
public class GameService {

    public static final int POSITION_TO_ADD = 7;
    public static final int STONES = 6;
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

    private LinkedList<Pit> createPits(int playerAmount) {

        int pitNumber = calculatePitNumber(playerAmount);
        LinkedList<Pit> pits = new LinkedList<>();
        for (int i = 0; i < pitNumber; i++) {
            pits.add(Pit.builder()
                    .stones(STONES)
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
                .forEach(pit -> pit.setBigPit(true));
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
