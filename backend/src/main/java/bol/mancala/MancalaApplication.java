package bol.mancala;

import bol.mancala.model.Player;
import bol.mancala.repositories.PlayerRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class MancalaApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MancalaApplication.class, args);
        PlayerRepo playerRepo = configurableApplicationContext.getBean(PlayerRepo.class);
        Player player1 = Player.builder().username("player1").password("pippo").build();
        Player player2 = Player.builder().username("player2").password("pluto").build();

        playerRepo.saveAll(Arrays.asList(player1, player2));
    }

}
