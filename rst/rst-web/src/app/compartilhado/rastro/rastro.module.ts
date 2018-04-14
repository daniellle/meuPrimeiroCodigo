import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RastroComponent} from './rastro.component';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [RouterModule, CommonModule],
  declarations: [RastroComponent],
  exports: [RastroComponent],
})
export class RastroModule {
}
