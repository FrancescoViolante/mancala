import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pit',
  templateUrl: './pit.component.html',
  styleUrls: ['./pit.component.css']
})
export class PitComponent implements OnInit {


  @Input() pit; //todo quando c'è il dto dal Back  pit: PITDTO
  @Output() pitClicked : EventEmitter<any> = new EventEmitter<any>();


  constructor() { }

  ngOnInit() {
  }

}
