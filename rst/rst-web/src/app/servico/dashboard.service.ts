import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BaseService} from './base.service';
import {BloqueioService} from './bloqueio.service';
import {Dashboard} from '../modelo/dashboard.model';

@Injectable()
export class DashboardService extends BaseService<Dashboard> {

  constructor(protected http: HttpClient, protected bloqueioService: BloqueioService) {
    super(http, bloqueioService);
  }

}
