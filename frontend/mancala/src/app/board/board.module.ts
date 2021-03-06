import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BoardComponent } from './board/board.component';
import { PitComponent } from './pit/pit.component';
import {NewGameComponent} from "./newgame/new-game.component";



@NgModule({
  declarations: [BoardComponent, PitComponent, NewGameComponent],
  exports: [
    BoardComponent
  ],
  imports: [
    CommonModule
  ]
})
export class BoardModule { }
