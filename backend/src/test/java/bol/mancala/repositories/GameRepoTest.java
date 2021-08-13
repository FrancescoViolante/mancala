package bol.mancala.repositories;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class GameRepoTest {

    @Autowired
    private GameRepo gameRepo;

    @BeforeEach
    void cleanUpDb() {
        gameRepo.deleteAll();
    }

    @Test
    public void createNewGame() {

        Game item = gameRepo.save(Game.builder().playerAmount(2).playerWhoMove(PlayerEnum.P1).build());
        assertAll(
                () -> assertThat(item.getPlayerAmount()).isEqualTo(2)
        );

    }


}
