package bol.mancala.expectedResults;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpGame {


    public static Game createNewGame() {

        List<Pit> pitsPlayer1 = List.of(
                Pit.builder().stones(6).bigPit(false).position(0).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(1).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(2).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(3).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(4).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(5).player(PlayerEnum.P1).build(),
                Pit.builder().stones(0).bigPit(true).position(6).player(PlayerEnum.P1).build()
        );

      /*  List<Pit> pitsPlayer2 =   List.copyOf(pitsPlayer1);

        pitsPlayer2.forEach(pit -> {
            pit.setPlayer(PlayerEnum.P2);
            pit.setPosition(Integer.sum(pit.getPosition(), GameService.POSITION_TO_ADD));
        });
        */
        List<Pit> pitsPlayer2 = List.of(
                Pit.builder().stones(6).bigPit(false).position(7).player(PlayerEnum.P2).build(),
                Pit.builder().stones(6).bigPit(false).position(8).player(PlayerEnum.P2).build(),
                Pit.builder().stones(6).bigPit(false).position(9).player(PlayerEnum.P2).build(),
                Pit.builder().stones(6).bigPit(false).position(10).player(PlayerEnum.P2).build(),
                Pit.builder().stones(6).bigPit(false).position(11).player(PlayerEnum.P2).build(),
                Pit.builder().stones(6).bigPit(false).position(12).player(PlayerEnum.P2).build(),
                Pit.builder().stones(0).bigPit(true).position(13).player(PlayerEnum.P2).build()
        );

        Game game = Game.builder().playerAmount(2).gameId(1L)
                .pits(Stream.of(pitsPlayer1, pitsPlayer2).flatMap(Collection::stream).collect(Collectors.toList()))
                .build();

        game.getPits().forEach(pit -> pit.setGame(game));

        return game;
    }
}
