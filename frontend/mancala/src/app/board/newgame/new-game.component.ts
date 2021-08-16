import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-newgame',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css']
})
export class NewGameComponent implements OnInit {

  ngOnInit() {
  }

  constructor(private router: Router) {

  }

  newGame() {
    this.router.navigate(["/board"]);
  }
}
