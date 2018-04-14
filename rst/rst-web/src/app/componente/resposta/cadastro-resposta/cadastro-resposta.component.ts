import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { RespostaService } from './../../../servico/resposta.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Resposta } from './../../../modelo/resposta.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cadastro-resposta',
  templateUrl: './cadastro-resposta.component.html',
  styleUrls: ['./cadastro-resposta.component.scss']
})
export class CadastroRespostaComponent extends BaseComponent implements OnInit {

  model: Resposta;
  id: number;
  respostaForm: FormGroup;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private respostaService: RespostaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new Resposta();
    this.emModoConsulta();
    this.createForm();
    this.setGrupoPergunta();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_CADASTRAR,
      PermissoesEnum.RESPOSTA_ALTERAR,
      PermissoesEnum.RESPOSTA_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/resposta`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.respostaService.salvar(this.prepareSave()).subscribe((response: Resposta) => {
        this.model = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.router.navigate([`${environment.path_raiz_cadastro}/resposta`]);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  excluir(): void {
    this.respostaService.desativar(this.prepareSave()).subscribe((response: Resposta) => {
      this.model = response;
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.router.navigate([`${environment.path_raiz_cadastro}/resposta`]);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createForm() {
    this.respostaForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(250),
        ]),
      ],
    });
  }

  private setGrupoPergunta() {
    this.id = this.activatedRoute.snapshot.params['id'];
    if (this.id) {
      this.carregarResposta();
    }
  }

  private carregarResposta(): void {
    this.respostaService.buscarPorId(this.id).subscribe((resposta: Resposta) => {
      if (resposta && resposta.id) {
        this.model = resposta;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private converterModelParaForm() {
    this.respostaForm.patchValue({
      descricao: this.model.descricao,
    });
  }

  private prepareSave(): Resposta {
    const formModel = this.respostaForm.controls;
    const saveProdutoServico: Resposta = {
      id: this.model.id,
      descricao: formModel.descricao.value as string,
    };
    return saveProdutoServico;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.respostaForm.controls['descricao'].invalid) {
      if (this.respostaForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.respostaForm.controls['descricao'],
          MensagemProperties.app_rst_labels_descricao);
        isValido = false;
      }
    }

    return isValido;
  }
}
