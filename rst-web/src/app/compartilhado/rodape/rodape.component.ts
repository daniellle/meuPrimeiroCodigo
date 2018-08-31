import {Component} from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'app-rodape',
  templateUrl: './rodape.component.html',
  styleUrls: ['./rodape.component.css'],
})
export class RodapeComponent {
  data: Date = new Date();
}
