import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BoardService} from "../board.service";

@Component({
  selector: 'app-newgame',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css']
})
export class NewGameComponent implements OnInit {

  isSinglePlayer: boolean = false;
  ngOnInit() {
  }

  constructor(private router: Router, private boardService :BoardService) {
  }

  newGame() {

    this.boardService.setModel({
      playerAmount : 2,
      singlePlayer : this.isSinglePlayer
    });
    this.router.navigate(["/board"]);
  }
}
