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
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {


    private GameService gameService;


    @GetMapping("/new-game")
    public ResponseEntity<Game> createGame() {

        Game gameToSave = gameService.initializeBoard(2);
        return  ResponseEntity.ok(gameService.saveOrUpdateGameInDataBase(gameToSave));
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
