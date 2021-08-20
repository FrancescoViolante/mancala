import {Component} from '@angular/core';
import {BoardService} from '../board.service';
import {delay, expand, tap} from "rxjs/operators";
import {of} from "rxjs";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent {

  public game: GameDto;
  public winner: string = null;

  constructor(private boardService: BoardService) {

    this.boardService.createNewGame(boardService.getModel()).subscribe((game) => {
      this.game = game
    });
  }


  pitClicked(event: PitDto) {
    if (event.player != this.game.playerWhoMove || event.bigPit || event.stones === 0) {
      return;
    }
    const model: MovePitRequestModel = {
      gameId: this.game.gameId,
      playerWhoMoved: event.player,
      positionClicked: event.position
    };
    if (!this.game.singlePlayer) {
      this.boardService.moveStones(model).subscribe((game) => {
        this.checkEndgame(game);
        this.game = game;
      });
    } else {
      const sub = this.boardService.moveStones(model)
        .pipe(
          tap((game: GameDto) => {
            this.checkEndgame(game);
            this.game = game;
          }),
          expand(game => {
            const movePitReqModSinglePlayer: MovePitRequestModel = {
              gameId: this.game.gameId,
              playerWhoMoved: "P2",
              positionClicked: this.findElementToClick(game)
            };
            this.checkEndgame(game);
            if (game.playerWhoMove === "P2" && !game.finished) {
              return this.boardService.moveStones(movePitReqModSinglePlayer).pipe(
                delay(2000)
              );
            } else return of(game);
          }),
        );

      sub.subscribe((game) => {
        this.checkEndgame(game);
        this.game = game;
      });
    }
  }

  checkEndgame(game: GameDto) {
    if (game.finished) {
      const totalPlayer1 = game.pitP1.find(pit => pit.bigPit).stones;
      const totalPlayer2 = game.pitP2.find(pit => pit.bigPit).stones;
      this.winner = totalPlayer1 === totalPlayer2 ? 'We have a Draw' : totalPlayer1 > totalPlayer2 ? 'Winner is player 1' : 'Winner is player 2';
    }
  }

  findElementToClick(game: GameDto) {
    const clickablePits = game.pitP2.filter(pit => !pit.bigPit).filter(pit => pit.stones !== 0);
    return clickablePits[Math.floor(Math.random() * clickablePits.length)].position;
  }
}
