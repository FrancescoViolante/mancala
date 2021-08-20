import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RemovePPipe } from './remove-p.pipe';



@NgModule({
  declarations: [RemovePPipe],
  exports: [
    RemovePPipe
  ],
  imports: [
    CommonModule
  ]
})
export class RemovePModule { }
