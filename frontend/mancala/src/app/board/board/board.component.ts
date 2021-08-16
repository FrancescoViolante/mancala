import {Component, OnInit} from '@angular/core';
import {BoardService} from '../board.service'

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent {

  public game: GameDto;


  constructor(private boardService: BoardService) {
    this.boardService.createNewGame().subscribe((game) => {this.game = game});
  }

  pitClicked(event: PitDto){
    if(event.player!=this.game.playerWhoMove || event.bigPit || event.stones === 0){
      return;
    }

    const model : MovePitRequestModel ={gameId:this.game.gameId, playerWhoMoved:event.player, positionClicked:event.position};
    this.boardService.moveStones(model).subscribe((game) => {this.game = game});
  }


}
