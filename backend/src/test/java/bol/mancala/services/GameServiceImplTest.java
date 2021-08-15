package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.expected.inputs.MovePitRequestModelImp;
import bol.mancala.expected.results.GameRes;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import bol.mancala.repositories.GameRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


        Game game = gameServiceImpl.initializeBoard(2);
        assertAll(
                () -> assertThat(game.getPits().size()).isEqualTo(14),
                () -> assertThat(game.getPlayerAmount()).isEqualTo(2)

        );
    }



    @Test
    void moveStonesGameNotPresentInDb() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameServiceImpl.moveStones(MovePitRequestModelImp.createMovePitRequestModelFirstMove()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Game not present.");
    }

    @Test
    void moveStonesInvalidPositionProvided() {

        Game game = GameRes.createNewGameWithTwoPlayers();
        MovePitRequestModel movePitRequestModel = MovePitRequestModelImp.createMovePitRequestModelFirstMove();
        game.getPits().removeIf(pit -> pit.getPosition() == movePitRequestModel.getPositionClicked());

        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameServiceImpl.moveStones(movePitRequestModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position clicked is not present.");
    }

    @Test
    void moveStonesFirstTurnClickOnPosition4() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createNewGameWithTwoPlayers()));


        Game game = gameServiceImpl.moveStones(MovePitRequestModelImp.createMovePitRequestModelFirstMove());

        assertEquals(GameRes.gameWithTwoPlayersClickOnPositionFourFirstTurnExpected(), game);
    }

    @Test
    void stealStonesOfOpponent() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createEndGameWithTwoPlayers()));
        when(gameRepo.save(any(Game.class))).thenReturn(GameRes.gameWithTwoPlayersClickLastTurnExpected());

        Game game = gameServiceImpl.moveStones(MovePitRequestModelImp.createMovePitRequestModelLastMove());

        assertEquals(GameRes.gameWithTwoPlayersClickLastTurnExpected(), game);
    }


}
