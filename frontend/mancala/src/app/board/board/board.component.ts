import {Component} from '@angular/core';
import {BoardService} from '../board.service'

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent {

  public game: GameDto;
  public winner: string;

  constructor(private boardService: BoardService) {
    this.boardService.createNewGame().subscribe((game) => {
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


    this.boardService.moveStones(model).subscribe((game) => {

      if (game.finished) {
        const totalPlayer1 = game.pitP1.find(pit => pit.bigPit).stones;
        const totalPlayer2 = game.pitP2.find(pit => pit.bigPit).stones;
        this.winner = totalPlayer1 === totalPlayer2 ? 'Draw' : totalPlayer1 > totalPlayer2 ? 'Winner is player 1' : 'Winner is player 2';
      }
      this.game = game
    });
  }


}
