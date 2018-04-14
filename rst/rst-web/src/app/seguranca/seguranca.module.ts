import { AutenticacaoService } from './../servico/autenticacao.service';
import { SessaoGuard } from './sessao.guard';
import { NgModule } from '@angular/core';
import { AutenticacaoGuard } from './autenticacao.guard';
import { AutorizacaoGuard } from './autorizacao.guard';

@NgModule({
  providers: [AutenticacaoService, SessaoGuard, AutenticacaoGuard, AutorizacaoGuard],
})
export class SegurancaModule {

}
