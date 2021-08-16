package bol.mancala.repositories;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import bol.mancala.expected.results.GameRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class GameRepoTest {

    public static final int INITIAL_STONE_VALUE = 6;

    @Autowired
    private GameRepo gameRepo;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createNewGame() {

        Game game = gameRepo.save(GameRes.createNewGameWithTwoPlayers());
        assertAll(
                () -> assertThat(game.getPlayerAmount()).isEqualTo(2),
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


    private Executable numberOfStonesInGame(Game game) {
        return () -> assertThat(game.getPits().stream()
                .mapToInt(Pit::getStones)
                .reduce(Integer::sum).getAsInt()).isEqualTo(72);
    }

    private Executable allPitsHaveSameGameId(Game game) {
        return () -> assertThat(game.getPits().stream()
                .mapToLong(pit -> pit.getGame().getGameId())
                .allMatch(e -> e == game.getGameId()));
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
                assertThat(game.getPits().stream().map(Pit::getStones).allMatch(stones -> stones == INITIAL_STONE_VALUE));
    }



}
