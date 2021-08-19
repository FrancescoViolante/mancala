package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.expected.input.MovePitRequestModelInp;
import bol.mancala.expected.output.GameRes;
import bol.mancala.entities.Game;
import bol.mancala.repositories.GameRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

        assertThatThrownBy(() -> gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelFirstMove()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Game not present.");
    }

    @Test
    void moveStonesInvalidPositionProvided() {

        Game game = GameRes.createNewGameWithTwoPlayers();
        MovePitRequestModel movePitRequestModel = MovePitRequestModelInp.createMovePitRequestModelFirstMove();
        game.getPits().removeIf(pit -> pit.getPosition() == movePitRequestModel.getPositionClicked());

        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameServiceImpl.moveStones(movePitRequestModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid position clicked.");
    }

    @Test
    void moveStonesFirstTurnClickOnPosition4() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createNewGameWithTwoPlayers()));


        Game game = gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelFirstMove());

        assertEquals(GameRes.gameWithTwoPlayersClickOnPositionFourFirstTurnExpected(), game);
    }

    @Test
    void playerP1stealsStonesOfOpponent() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createAdvancedGameWithTwoPlayers()));

        Game game = gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelP1StealsStones());

        assertEquals(GameRes.gameWithTwoPlayersP1StealsStonesExpected(), game);
    }

    @Test
    void playerP2stealsStonesOfOpponent() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createAdvancedGameWithTwoPlayers()));

        Game game = gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelP2StealsStones());

        assertEquals(GameRes.gameWithTwoPlayersP2StealsStonesExpected(), game);
    }

    @Test
    void playerP2LastEndStealsStonesOfOpponentButLost() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createEndGameWithTwoPlayersP2LastTurn()));

        Game game = gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelP2StealsStones());

        assertEquals(GameRes.gameWithTwoPlayersP2LostFinalResultExpected(), game);
    }

    @Test
    void playerP1LastEndStealsStonesOfOpponentAndWin() {
        when(gameRepo.findById(any(Long.class))).thenReturn(Optional.of(GameRes.createEndGameWithTwoPlayersP1LastTurn()));

        Game game = gameServiceImpl.moveStones(MovePitRequestModelInp.createMovePitRequestModelP1StealsStones());

        assertEquals(GameRes.gameWithTwoPlayersP1WinFinalResultExpected(), game);
    }


}
