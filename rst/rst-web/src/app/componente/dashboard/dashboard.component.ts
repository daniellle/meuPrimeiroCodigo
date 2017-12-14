import {Component, OnInit} from '@angular/core';
import {ToastyService} from 'ng2-toasty';
import {BloqueioService} from '../../servico/bloqueio.service';
import {DashboardService} from '../../servico/dashboard.service';
import {TemplateComponent} from '../../compartilhado/template/template.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent extends TemplateComponent implements OnInit {

  constructor(protected service: DashboardService, protected bloqueio: BloqueioService, protected dialogo: ToastyService) {
    super(bloqueio, dialogo);
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

}
