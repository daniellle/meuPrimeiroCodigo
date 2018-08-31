import { environment } from './../../../../environments/environment';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { BaseComponent } from 'app/componente/base.component';
import { GrupoPerguntaService } from './../../../servico/grupo-pergunta.service';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { GrupoPergunta } from './../../../modelo/grupo-pergunta.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cadastro-grupo-pergunta',
  templateUrl: './cadastro-grupo-pergunta.component.html',
  styleUrls: ['./cadastro-grupo-pergunta.component.scss']
})
export class CadastroGrupoPerguntaComponent extends BaseComponent implements OnInit {

  model: GrupoPergunta;
  id: number;
  grupoPerguntaForm: FormGroup;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private grupoPerguntaService: GrupoPerguntaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new GrupoPergunta();
    this.emModoConsulta();
    this.createForm();
    this.setGrupoPergunta();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
      PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
      PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/grupopergunta`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.grupoPerguntaService.salvar(this.prepareSave()).subscribe((response: GrupoPergunta) => {
        this.model = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.router.navigate([`${environment.path_raiz_cadastro}/grupopergunta`]);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  excluir(): void {
    this.grupoPerguntaService.desativar(this.prepareSave()).subscribe((response: GrupoPergunta) => {
      this.model = response;
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.router.navigate([`${environment.path_raiz_cadastro}/grupopergunta`]);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createForm() {
    this.grupoPerguntaForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
    });
  }

  private setGrupoPergunta() {
    this.id = this.activatedRoute.snapshot.params['id'];
    if (this.id) {
      this.carregarGrupoPergunta();
    }
  }

  private carregarGrupoPergunta(): void {
    this.grupoPerguntaService.buscarPorId(this.id).subscribe((grupoPergunta: GrupoPergunta) => {
      if (grupoPergunta && grupoPergunta.id) {
        this.model = grupoPergunta;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private converterModelParaForm() {
    this.grupoPerguntaForm.patchValue({
      descricao: this.model.descricao,
    });
  }

  private prepareSave(): GrupoPergunta {
    const formModel = this.grupoPerguntaForm.controls;
    const saveProdutoServico: GrupoPergunta = {
      id: this.model.id,
      dataCriacao: this.model.dataCriacao,
      dataAlteracao: this.model.dataAlteracao,
      dataExclusao: this.model.dataExclusao,
      descricao: formModel.descricao.value as string,
    };
    return saveProdutoServico;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.grupoPerguntaForm.controls['descricao'].invalid) {
      if (this.grupoPerguntaForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.grupoPerguntaForm.controls['descricao'],
          MensagemProperties.app_rst_labels_descricao);
        isValido = false;
      }
    }

    return isValido;
  }

}
