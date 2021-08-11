package bol.mancala.controllers;


import bol.mancala.model.Game;
import bol.mancala.services.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("actions")
public class GameController {


    private GameService gameService;


    @GetMapping("/hello")
    public void hello() {

        log.info("Hi");

    }


    @GetMapping("/new-game")
    public Game createGame() {

        return gameService.initializeBoard(2);

    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
