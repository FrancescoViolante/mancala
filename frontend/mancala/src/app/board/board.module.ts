import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BoardComponent } from './board/board.component';
import { PitComponent } from './pit/pit.component';



@NgModule({
  declarations: [BoardComponent, PitComponent],
  exports: [
    BoardComponent
  ],
  imports: [
    CommonModule
  ]
})
export class BoardModule { }
