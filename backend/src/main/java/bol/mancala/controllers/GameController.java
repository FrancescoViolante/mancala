package bol.mancala.controllers;


import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.entities.Game;
import bol.mancala.services.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Log4j2
@RestController
@RequestMapping("mancala")
public class GameController {


    private GameService gameService;


    @GetMapping("/new-game")
    public ResponseEntity<Game> createGame() {

        Game gameToSave = gameService.initializeBoard(2);
        Game gameToSave2 =  gameService.saveOrUpdateGameInDataBase(gameToSave);
        return  ResponseEntity.ok(gameToSave2);
    }

    @PostMapping("/move-stones")
    public ResponseEntity<Game> moveStones(@Valid @RequestBody MovePitRequestModel movePitRequestModel) {

        Game gameToUpdate = gameService.moveStones(movePitRequestModel);
        return ResponseEntity.ok(gameService.saveOrUpdateGameInDataBase(gameToUpdate));

    }

    @Autowired
    public void setGameService(GameService gameServiceImpl) {
        this.gameService = gameServiceImpl;
    }
}
