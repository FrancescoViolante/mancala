package bol.mancala.controllers;


import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.model.Game;
import bol.mancala.services.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Log4j2
@RestController
@RequestMapping("actions")
public class GameController {


    private GameService gameService;


    @GetMapping("/new-game")
    public Game createGame() {

        return gameService.initializeBoard(2);

    }

    @PostMapping("/move-stones")
    public ResponseEntity<Game> moveStones(@Valid @RequestBody MovePitRequestModel movePitRequestModel) {

        return ResponseEntity.ok(gameService.moveStones(movePitRequestModel));

    }

    @Autowired
    public void setGameService(GameService gameServiceImpl) {
        this.gameService = gameServiceImpl;
    }
}
