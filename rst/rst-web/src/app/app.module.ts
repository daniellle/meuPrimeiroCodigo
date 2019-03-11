import {PublicoModule} from './publico/publico.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LocationStrategy, PathLocationStrategy, APP_BASE_HREF} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {AppRoutes} from './app.routes';
import {AppComponent} from './app.component';
import {MomentModule} from 'angular2-moment';
import {BloqueioService} from './servico/bloqueio.service';
import {SegurancaModule} from './seguranca/seguranca.module';
import {BloqueioModule} from './compartilhado/bloqueio/bloqueio.module';
import {AutenticacaoInterceptor} from './servico/autenticacao.interceptor';
import {AutenticacaoModule} from './componente/autenticacao/autenticacao.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ModalModule} from 'ngx-bootstrap';
import { environment } from 'environments/environment';


@NgModule({
              bootstrap: [AppComponent],
              declarations: [AppComponent],
              providers: [
                  BloqueioService,
                  {
                      provide: LocationStrategy,
                      useClass: PathLocationStrategy
                  },
                  {
                      provide: HTTP_INTERCEPTORS,
                      useClass: AutenticacaoInterceptor,
                      multi: true
                  },
                //   {
                //     provide: APP_BASE_HREF,
                //     useValue: environment.baseHref
                //   }
              ],
              imports: [
                  AutenticacaoModule,
                  SegurancaModule,
                  BloqueioModule,
                  BrowserModule,
                  HttpClientModule,
                  FormsModule,
                  AppRoutes,
                  BrowserAnimationsModule,
                  NgbModule.forRoot(),
                  ModalModule.forRoot(),
                  MomentModule,
                  BsDropdownModule.forRoot(),
                  TabsModule.forRoot(),
                  PublicoModule
              ],
          })
export class AppModule {

}
