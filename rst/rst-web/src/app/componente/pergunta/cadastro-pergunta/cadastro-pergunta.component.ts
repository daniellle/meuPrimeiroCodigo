import { EnumTipoResposta } from './../../../modelo/tipo-resposta-enum.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PerguntaService } from './../../../servico/pergunta.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Pergunta } from './../../../modelo/pergunta.model';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';


@Component({
  selector: 'app-cadastro-pergunta',
  templateUrl: './cadastro-pergunta.component.html',
  styleUrls: ['./cadastro-pergunta.component.scss'],
})
export class CadastroPerguntaComponent extends BaseComponent implements OnInit {

  model: Pergunta;
  id: number;
  perguntaForm: FormGroup;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private perguntaService: PerguntaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new Pergunta();
    this.emModoConsulta();
    this.createForm();
    this.setPergunta();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_CADASTRAR,
      PermissoesEnum.PERGUNTA_ALTERAR,
      PermissoesEnum.PERGUNTA_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/pergunta`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.perguntaService.salvar(this.prepareSave()).subscribe((response: Pergunta) => {
        this.model = response;
        this.router.navigate([`${environment.path_raiz_cadastro}/pergunta`]);
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  private setPergunta() {
    this.id = this.activatedRoute.snapshot.params['id'];
    if (this.id) {
      this.carregarPergunta();
    }
  }

  private carregarPergunta(): void {
    this.perguntaService.buscarPorId(this.id).subscribe((pergunta: Pergunta) => {
      if (pergunta && pergunta.id) {
        this.model = pergunta;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createForm() {
    this.perguntaForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
      ],
    });
  }

  private converterModelParaForm() {
    this.perguntaForm.patchValue({
      descricao: this.model.descricao,
    });
  }

  private prepareSave(): Pergunta {
    const formModel = this.perguntaForm.controls;
    const savePergunta: Pergunta = {
      id: this.model.id,
      descricao: formModel.descricao.value as string,
    };
    return savePergunta;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.perguntaForm.controls['descricao'].invalid) {
      if (this.perguntaForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaForm.controls['descricao'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }
    }
    return isValido;
  }

  excluir() {
    this.perguntaService.desativar(this.prepareSave()).subscribe((response: Pergunta) => {
      this.model = response;
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.router.navigate([`${environment.path_raiz_cadastro}/pergunta`]);
    }, (error) => {
      this.mensagemError(error);
    });
  }

}
