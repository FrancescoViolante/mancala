package bol.mancala.expectedResults;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExpGame {


    public static Game createNewGameWithTwoPlayers() {


        List<Pit> pits = new ArrayList<>();
        IntStream.range(0, 14)
                .forEach(position -> {
                    Pit pit = Pit.builder().stones(6).bigPit(false).position(position).build();
                    calculatePlayer(pit);
                    adaptBigPitValues(pit);
                    pits.add(pit);
                });


        Game game = Game.builder().playerAmount(2).gameId(1L)
                .playerWhoMove(PlayerEnum.P1)
                .pits(pits)
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));

        return game;
    }

    private static void adaptBigPitValues(Pit pit) {
        if (pit.getPosition() == 7 || pit.getPosition() == 13)
            pit.setBigPit(Boolean.TRUE);

    }

    private static void calculatePlayer(Pit pit) {
        if (pit.getPosition() < 7)
            pit.setPlayer(PlayerEnum.P1);
        else
            pit.setPlayer(PlayerEnum.P2);
    }
}
