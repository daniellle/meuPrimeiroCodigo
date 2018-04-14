import { SolicitarEmailService } from './../servico/solicitar-email.service';
import { TrabalhadorService } from './../servico/trabalhador.service';
import { AcessoNegadoComponent } from './erro/acesso-negado/acesso-negado.component';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { TemplateModule } from './../compartilhado/template/template.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { ToastyService, ToastyModule } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from './../servico/bloqueio.service';
import { AlterarSenhaComponent } from './alterar-senha/alterar-senha.component';
import { RecuperarSenhaComponent } from './recuperar-senha/recuperar-senha.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SolicitarEmailComponent } from './solicitar-email/solicitar-email.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'primeiroacesso', loadChildren: './primeiro-acesso/primeiro-acesso.module#PrimeiroAcessoModule',
      },
      {
        path: 'acessonegado',
        component: AcessoNegadoComponent,
        data: {title: MensagemProperties.app_rst_acesso_negado_titulo},
      },
      {
        path: 'recuperarsenha',
        component: RecuperarSenhaComponent,
        data: {title: MensagemProperties.app_rst_recupera_senha_titulo},
      },
      {
        path: 'alterarsenha/:hash',
        component: AlterarSenhaComponent,
        data: {title: MensagemProperties.app_rst_alterar_senha_titulo},
      },
      {
        path: 'solicitaremail/:id',
        component: SolicitarEmailComponent,
        data: {title: MensagemProperties.app_rst_alterar_senha_titulo},
      },
    ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    TemplateModule,
    CompartilhadoModule,
    ToastyModule,
  ],
  declarations: [RecuperarSenhaComponent, AcessoNegadoComponent, AlterarSenhaComponent, SolicitarEmailComponent],
  providers: [
    BloqueioService,
    DialogService,
    ToastyService,
    AutenticacaoService,
    TrabalhadorService,
    SolicitarEmailService,
  ],
})
export class PublicoModule { }
