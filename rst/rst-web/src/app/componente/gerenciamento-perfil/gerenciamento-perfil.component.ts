import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { EstadoService } from 'app/servico/estado.service';
import { environment } from './../../../environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { UsuarioService } from 'app/servico/usuario.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';


@Component({
  selector: 'app-gerenciamento-perfil',
  templateUrl: './gerenciamento-perfil.component.html',
  styleUrls: ['./gerenciamento-perfil.component.scss']
})
export class GerenciamentoPerfilComponent extends BaseComponent implements OnInit {

  public usurioSenhaForm: FormGroup;
  
  constructor(
    private servico: AutenticacaoService,   
    private route: ActivatedRoute,   
    private router: Router,
    private formBuilder: FormBuilder,
    protected estadoService: EstadoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService) {
      super(bloqueioService, dialogo, estadoService);
    }

  ngOnInit() {
    this.createForm();    
  }
  
  passwordMatchValidator(g: FormGroup) {
    if (!(g.get('nova').value === g.get('confirmacao').value)){
      g.get('confirmacao').setErrors( {MatchPassword: true} )
    }
    return g.get('nova').value === g.get('confirmacao').value
       ? null : {'mismatch': true};
  }

  createForm() {
    this.usurioSenhaForm = this.formBuilder.group({
      atual:[''],       
      nova: ['',Validators.compose([Validators.required, Validators.pattern('(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$')])],
      confirmacao: ['', Validators.required],
    }, { validator: this.passwordMatchValidator});
  }



  validarCampos(): Boolean {
    let isValido = true;   
    if (this.usurioSenhaForm.controls['atual'].invalid) {
        if (this.usurioSenhaForm.controls['atual'].errors.required) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usurioSenhaForm.controls['atual'],
                "Errro ao Trocar Senha");
            isValido = false;
        }

    }
    return isValido;
}

private prepareSave() {
  const formModel = this.usurioSenhaForm.controls;
  var atual = formModel.atual.value;
  var nova = formModel.nova.value;
  var confirmacao = formModel.confirmacao.value;
}

  salvar(): void {
    if (this.validarCampos()) {
        this.prepareSave();
        this.servico.alterarSenhaRST(this.usurioSenhaForm.controls.atual.value, this.usurioSenhaForm.controls.nova.value)
        .subscribe((retorno: any) => {
          this.mensagemSucesso(MensagemProperties.app_rst_alterar_senha_sucesso);
          window.location.href = environment.url_portal;
        }, (error) => {
          console.log("Falha no Envio de Senha Trocada.");
          this.mensagemError(error);
        });
    }
}  

voltar(): void {
  this.router.navigate([`${environment.path_raiz_cadastro}`]);
}

}
