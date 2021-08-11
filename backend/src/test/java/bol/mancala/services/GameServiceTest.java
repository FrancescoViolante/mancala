package bol.mancala.services;

import bol.mancala.dto.Board;
import bol.mancala.model.Game;
import bol.mancala.repositories.GameRepo;
import bol.mancala.repositories.PitRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private PitRepo pitRepo;
    @Mock
    private GameRepo gameRepo;

    @Test
    void initializeBoard() {

        Game g = Game.builder().playerAmount(2).gameId(1L).build();
        when(gameRepo.save(any(Game.class))).thenReturn(g);

        Game game = gameService.initializeBoard(2);
        System.out.println(game);
    }
}
