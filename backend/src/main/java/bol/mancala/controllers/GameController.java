package bol.mancala.controllers;


import bol.mancala.dto.GameDto;
import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.entities.Game;
import bol.mancala.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game saved."),
            @ApiResponse(responseCode = "500", description = "Error creating a new game.")
    })
    @GetMapping("/new-game")
    public ResponseEntity<GameDto> createGame() throws IllegalArgumentException{

        Game gameToSave = gameService.initializeBoard(2);
        return  ResponseEntity.ok(gameService.saveOrUpdateGameInDataBase(gameToSave));
    }

    @Operation(summary = "Move stones in pits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stones moved."),
            @ApiResponse(responseCode = "500", description = "Error moving stones.")
    })
    @PostMapping("/move-stones")
    public ResponseEntity<GameDto> moveStones(@Valid @RequestBody MovePitRequestModel movePitRequestModel) {

        Game gameToUpdate = gameService.moveStones(movePitRequestModel);
        return ResponseEntity.ok(gameService.saveOrUpdateGameInDataBase(gameToUpdate));

    }

    @Autowired
    public void setGameService(GameService gameServiceImpl) {
        this.gameService = gameServiceImpl;
    }
}
