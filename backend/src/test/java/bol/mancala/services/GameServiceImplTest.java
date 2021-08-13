package bol.mancala.services;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.expectedResults.ExpGame;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import bol.mancala.repositories.GameRepo;
import bol.mancala.repositories.PitRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    @Mock
    private PitRepo pitRepo;
    @Mock
    private GameRepo gameRepo;

    @Test
    void initializeBoard() {


        when(gameRepo.save(any(Game.class))).thenReturn(ExpGame.createNewGame());

        Game game = gameServiceImpl.initializeBoard(2);
        assertAll(
                () -> assertThat(game.getPits().size()).isEqualTo(14),
                () -> assertThat(game.getGameId().longValue()).isEqualTo(1L),
                () -> assertThat(game.getPlayerAmount()).isEqualTo(2),

                allPitsHaveSameGameId(game),
                firstPositionInTwoPlayerGameIsZero(game),
                lastPositionInTwoPlayerGameIs13(game),
                playerPresentAreP1AndP2(game),
                stoneInitialValueIs6(game)
        );
    }

    @Test
    void moveStones() {


    }

    private Executable allPitsHaveSameGameId(Game game) {
        return () -> assertThat(game.getPits().stream()
                .mapToLong(pit -> pit.getGame().getGameId())
                .allMatch(e -> e == 1));
    }

    private Executable lastPositionInTwoPlayerGameIs13(Game game) {
        return () -> assertThat(game.getPits().stream().mapToInt(Pit::getPosition).max().stream()
                .findFirst().orElse(Integer.MAX_VALUE)).isEqualTo(13);
    }

    private Executable firstPositionInTwoPlayerGameIsZero(Game game) {
        return () -> assertThat(game.getPits().stream().mapToInt(Pit::getPosition).min().stream()
                .findFirst().orElse(Integer.MIN_VALUE)).isEqualTo(0);
    }

    private Executable playerPresentAreP1AndP2(Game game) {
        return () ->
                assertThat(game.getPits().stream().map(Pit::getPlayer).distinct().collect(Collectors.toList())
                        .containsAll(List.of(PlayerEnum.P1, PlayerEnum.P2)));
    }

    private Executable stoneInitialValueIs6(Game game) {
        return () ->
                assertThat(game.getPits().stream().map(Pit::getStones).allMatch(stones -> stones == 6));
    }


}
