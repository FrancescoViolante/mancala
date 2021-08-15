package bol.mancala.expected.results;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import bol.mancala.utils.GameUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class GameRes {


    public static Game createNewGameWithTwoPlayers() {


        List<Pit> pits = new ArrayList<>();
        IntStream.range(0, 14)
                .forEach(position -> {
                    Pit pit = Pit.builder().stones(6).bigPit(false).updatablePit(true).position(position).build();
                    calculatePlayer(pit);
                    adaptBigPitValues(pit);
                    pits.add(pit);
                });

        Game game = Game.builder().playerAmount(2).gameId(1L)
                .playerWhoMove(PlayerEnum.P1)
                .pits(pits)
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));
        GameUtil.setAfterPitId(new LinkedList<>(game.getPits()));
        return game;
    }

    private static void adaptBigPitValues(Pit pit) {
        if (pit.getPosition() == 6 || pit.getPosition() == 13) {
            pit.setBigPit(Boolean.TRUE);
            pit.setStones(0);
        }
    }

    private static void calculatePlayer(Pit pit) {
        if (pit.getPosition() < 7)
            pit.setPlayer(PlayerEnum.P1);
        else
            pit.setPlayer(PlayerEnum.P2);
    }

    public static Game gameWithTwoPlayersClickOnPositionFourFirstTournExpected() {
        Game gameWithTwoPlayers = createNewGameWithTwoPlayers();
        gameWithTwoPlayers.getPits().get(4).setStones(0);
        gameWithTwoPlayers.getPits().get(4).setUpdatablePit(false);
        gameWithTwoPlayers.getPits().get(13).setUpdatablePit(false);
        List<Integer> updatedPits = List.of(5,7,8,9,10);

        gameWithTwoPlayers.getPits().stream().filter(pit -> updatedPits.contains(pit.getPosition())).forEach(
                pit -> pit.setStones(7)
        );
        gameWithTwoPlayers.getPits().get(6).setStones(1);

        gameWithTwoPlayers.setPlayerWhoMove(PlayerEnum.P2);
        return gameWithTwoPlayers;
    }


}
