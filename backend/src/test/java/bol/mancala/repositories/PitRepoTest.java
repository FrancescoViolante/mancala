package bol.mancala.repositories;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import bol.mancala.model.Pit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class PitRepoTest {

    @Autowired
    private PitRepo pitRepo;

    @Autowired
    private GameRepo gameRepo;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createNewGame() {


        Pit pit = pitRepo.save(Pit.builder().stones(6).bigPit(false).position(0).player(PlayerEnum.P1).build());

        assertAll(
                () -> assertThat(pit.getPitId()).isEqualTo(1L),
                () -> assertThat(pit.getStones()).isEqualTo(6),
                () -> assertThat(pit.getPlayer()).usingRecursiveComparison().isEqualTo(PlayerEnum.P1),
                () -> assertThat(pit.getPosition()).isEqualTo(0),
                () -> assertFalse(pit.isBigPit()));

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createNewGameWithPits() {

        List<Pit> pits = List.of(Pit.builder().stones(6).bigPit(false).position(0).player(PlayerEnum.P1).build(),
                Pit.builder().stones(6).bigPit(false).position(1).player(PlayerEnum.P2).build());
        Game game =  Game.builder().playerAmount(2).pits(pits).build();

        pits.forEach(pit-> pit.setGame(game));
        Game gameSaved = gameRepo.save(game) ;

        assertThat(game.getPits().size()).isEqualTo(2);


        assertThat(pitRepo.findAllByGame_GameId(gameSaved.getGameId()).size()).isEqualTo(2);
    }

}
