package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.expected.inputs.MovePitRequestModelImp;
import bol.mancala.expected.results.GameRes;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import bol.mancala.repositories.GameRepo;
import bol.mancala.utils.GameUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    @Mock
    private GameRepo gameRepo;

    @Test
    void initializeBoard() {


        when(gameRepo.save(any(Game.class))).thenReturn(GameRes.createNewGameWithTwoPlayers());

        Game game = gameServiceImpl.initializeBoard(2);
        assertAll(
                () -> assertThat(game.getPits().size()).isEqualTo(14),
                () -> assertThat(game.getGameId().longValue()).isEqualTo(1L),
                () -> assertThat(game.getPlayerAmount()).isEqualTo(2),
                () -> assertThat(game.getPlayerWhoMove()).isEqualTo(PlayerEnum.P1),

                numberOfStonesInGame(game),
                allPitsHaveSameGameId(game),
                firstPositionInTwoPlayerGameIsZero(game),
                lastPositionInTwoPlayerGameIs13(game),
                playerPresentAreP1AndP2(game),
                stoneInitialValueIs6(game)
        );
    }


    @Test
    void moveStonesFirstTurnClickOnPosition4() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createNewGameWithTwoPlayers()));
        when(gameRepo.save(any(Game.class))).thenReturn(GameRes.gameWithTwoPlayersClickOnPositionFourFirstTournExpected());

        Game game = gameServiceImpl.moveStones(MovePitRequestModelImp.createMovePitRequestModel());

        assertEquals(game, GameRes.gameWithTwoPlayersClickOnPositionFourFirstTournExpected());
    }

    @Test
    void moveStonesGameNotPresentInDb() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameServiceImpl.moveStones(MovePitRequestModelImp.createMovePitRequestModel()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Game not present.");
    }

    @Test
    void moveStonesInvalidPositionProvided() {

        Game game = GameRes.createNewGameWithTwoPlayers();
        MovePitRequestModel movePitRequestModel = MovePitRequestModelImp.createMovePitRequestModel();
        game.getPits().removeIf(pit -> pit.getPosition() == movePitRequestModel.getPositionClicked());

        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameServiceImpl.moveStones(movePitRequestModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position clicked is not present.");
    }

    private Executable numberOfStonesInGame(Game game) {
        return () -> assertThat(game.getPits().stream()
                .mapToInt(Pit::getStones)
                .reduce(Integer::sum).getAsInt()).isEqualTo(72);
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
