import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pit',
  templateUrl: './pit.component.html',
  styleUrls: ['./pit.component.css']
})
export class PitComponent implements OnInit {


  @Input() pit: PitDto;
  @Output() onPitClicked : EventEmitter<any> = new EventEmitter<any>();


  constructor() { }

  ngOnInit() {
  }

  moveStone() {
    this.onPitClicked.emit(this.pit)
  }
}
