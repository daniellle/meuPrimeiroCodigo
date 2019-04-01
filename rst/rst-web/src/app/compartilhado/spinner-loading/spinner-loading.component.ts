import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-spinner-loading',
  templateUrl: './spinner-loading.component.html',
  styleUrls: ['./spinner-loading.component.scss'],
})
export class SpinnerLoadingComponent {

  @Input() loading: boolean;
}
