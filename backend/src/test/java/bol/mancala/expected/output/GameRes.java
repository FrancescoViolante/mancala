package bol.mancala.expected.output;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
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
                    Pit pit = Pit.builder().stones(6)
                            .bigPit(false).updatablePit(true)
                            .position(position)
                            .build();
                    calculatePlayer(pit);
                    adaptBigPitValues(pit, 0);
                    pits.add(pit);
                });

        Game game = Game.builder().playerAmount(2)
                .playerWhoMove(PlayerEnum.P1)
                .gameId(1L)
                .pits(pits)
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));
        GameUtil.setNextPitPosition(new LinkedList<>(game.getPits()));
        return game;
    }

    private static void adaptBigPitValues(Pit pit, int bigPitStones) {
        if (pit.getPosition() == 6 || pit.getPosition() == 13) {
            pit.setBigPit(Boolean.TRUE);
            pit.setStones(bigPitStones);
        }
    }

    private static void calculatePlayer(Pit pit) {
        if (pit.getPosition() < 7)
            pit.setPlayer(PlayerEnum.P1);
        else
            pit.setPlayer(PlayerEnum.P2);
    }

    public static Game gameWithTwoPlayersClickOnPositionFourFirstTurnExpected() {
        Game gameWithTwoPlayers = createNewGameWithTwoPlayers();
        gameWithTwoPlayers.getPits().get(4).setStones(0);
        setClickedPitAndBigPitOpponentNonUpdatable(gameWithTwoPlayers, List.of(4, 13));
        List<Integer> updatedPits = List.of(5, 7, 8, 9, 10);

        gameWithTwoPlayers.getPits().stream().filter(pit -> updatedPits.contains(pit.getPosition())).forEach(
                pit -> pit.setStones(7)
        );
        gameWithTwoPlayers.getPits().get(6).setStones(1);

        gameWithTwoPlayers.setPlayerWhoMove(PlayerEnum.P2);
        return gameWithTwoPlayers;
    }


    public static Game createAdvancedGameWithTwoPlayers() {


        List<Pit> pits = new ArrayList<>();
        IntStream.range(0, 14)
                .forEach(position -> {
                    Pit pit = Pit.builder().stones(0).bigPit(false).updatablePit(true).position(position).build();
                    calculatePlayer(pit);
                    adaptBigPitValues(pit, 30);
                    pits.add(pit);
                });

        Game game = Game.builder().playerAmount(2).gameId(1L)
                .playerWhoMove(PlayerEnum.P2)
                .pits(pits)
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));
        GameUtil.setNextPitPosition(new LinkedList<>(game.getPits()));

        game.getPits().get(0).setStones(1);
        game.getPits().get(11).setStones(7);

        game.getPits().get(7).setStones(1);
        game.getPits().get(4).setStones(3);
        return game;
    }


    public static Game gameWithTwoPlayersP2StealsStonesExpected() {
        Game gameWithTwoPlayers = createAdvancedGameWithTwoPlayers();
        setClickedPitAndBigPitOpponentNonUpdatable(gameWithTwoPlayers, List.of(6, 7));

        gameWithTwoPlayers.getPits().get(7).setStones(0);
        gameWithTwoPlayers.getPits().get(4).setStones(0);
        gameWithTwoPlayers.getPits().get(13).setStones(34);

        gameWithTwoPlayers.setPlayerWhoMove(PlayerEnum.P1);
        return gameWithTwoPlayers;
    }

    public static Game gameWithTwoPlayersP1StealsStonesExpected() {
        Game gameWithTwoPlayers = createAdvancedGameWithTwoPlayers();
        setClickedPitAndBigPitOpponentNonUpdatable(gameWithTwoPlayers, List.of(0, 13));

        gameWithTwoPlayers.getPits().get(0).setStones(0);
        gameWithTwoPlayers.getPits().get(11).setStones(0);
        gameWithTwoPlayers.getPits().get(6).setStones(38);

        gameWithTwoPlayers.setPlayerWhoMove(PlayerEnum.P2);
        return gameWithTwoPlayers;
    }

    private static void setClickedPitAndBigPitOpponentNonUpdatable(Game game, List<Integer> i) {
        game.getPits().stream().filter(pit -> i.contains(pit.getPosition()))
                .forEach(pit -> pit.setUpdatablePit(false));
    }

    public static LinkedList<Pit> createPitListFourPlayers() {


        LinkedList<Pit> pits = new LinkedList<>();
        IntStream.range(0, 28)
                .forEach(position -> {
                    Pit pit = Pit.builder().stones(6).position(position).build();
                    pits.add(pit);
                });

        return pits;
    }


    public static Game createEndGameWithTwoPlayersP2LastTurn() {


        List<Pit> pits = new ArrayList<>();
        IntStream.range(0, 14)
                .forEach(position -> {
                    Pit pit = Pit.builder().stones(0).bigPit(false).updatablePit(true).position(position).build();
                    calculatePlayer(pit);
                    adaptBigPitValues(pit, 30);
                    pits.add(pit);
                });

        Game game = Game.builder().playerAmount(2).gameId(1L)
                .playerWhoMove(PlayerEnum.P2)
                .pits(pits)
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));
        GameUtil.setNextPitPosition(new LinkedList<>(game.getPits()));

        game.getPits().get(0).setStones(1);
        game.getPits().get(7).setStones(1);

        game.getPits().get(2).setStones(1);
        game.getPits().get(3).setStones(3);
        game.getPits().get(4).setStones(3);
        game.getPits().get(5).setStones(3);
        return game;
    }

    public static Game gameWithTwoPlayersP2LostFinalResultExpected() {
        Game gameWithTwoPlayers = createEndGameWithTwoPlayersP2LastTurn();
        setClickedPitAndBigPitOpponentNonUpdatable(gameWithTwoPlayers, List.of(6, 7));

        gameWithTwoPlayers.getPits().stream().filter(pit -> !pit.isBigPit())
                .forEach(pit -> pit.setStones(0));

        gameWithTwoPlayers.getPits().get(6).setStones(38);
        gameWithTwoPlayers.getPits().get(13).setStones(34);

        gameWithTwoPlayers.setPlayerWhoMove(PlayerEnum.P1);
        return gameWithTwoPlayers;
    }

    public static Game createEndGameWithTwoPlayersP1LastTurn() {

        Game game = createAdvancedGameWithTwoPlayers();

        game.getPits().get(7).setStones(0);
        game.getPits().get(5).setStones(1);
        return game;
    }

    public static Game gameWithTwoPlayersP1WinFinalResultExpected() {
        Game gameExpected = createEndGameWithTwoPlayersP1LastTurn();
        setClickedPitAndBigPitOpponentNonUpdatable(gameExpected, List.of(0, 13));

        gameExpected.getPits().stream().filter(pit -> !pit.isBigPit())
                .forEach(pit -> pit.setStones(0));

        gameExpected.getPits().get(6).setStones(42);
        gameExpected.getPits().get(13).setStones(30);

        gameExpected.setPlayerWhoMove(PlayerEnum.P2);
        return gameExpected;
    }
}
