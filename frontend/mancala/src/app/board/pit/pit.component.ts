import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pit',
  templateUrl: './pit.component.html',
  styleUrls: ['./pit.component.css']
})
export class PitComponent implements OnInit {


  @Input() pit: PitDto;
  @Input() game: GameDto;
  @Output() onPitClicked: EventEmitter<any> = new EventEmitter<any>();


  constructor() {
  }

  ngOnInit() {
  }

  moveStone() {
    if ((!this.game.singlePlayer) || (this.game.singlePlayer && this.pit.player === "P1"))
      this.onPitClicked.emit(this.pit)
  }
}
