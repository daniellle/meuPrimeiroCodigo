import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
];

@NgModule({
  providers: [],
  declarations: [HomeComponent],
  imports: [RouterModule.forChild(routes)],
})
export class HomeModule {
}
