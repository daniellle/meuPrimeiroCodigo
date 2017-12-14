import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { IndicadorQuestionarioService } from './../../../servico/indicador-questionario.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { IndicadorQuestionario } from './../../../modelo/indicador-questionario.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cadastro-indicador-questionario',
  templateUrl: './cadastro-indicador-questionario.component.html',
  styleUrls: ['./cadastro-indicador-questionario.component.scss'],
})
export class CadastroIndicadorQuestionarioComponent extends BaseComponent implements OnInit {

  model: IndicadorQuestionario;
  id: number;
  indicadorQuestionarioForm: FormGroup;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private indicadorQuestionarioService: IndicadorQuestionarioService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new IndicadorQuestionario();
    this.emModoConsulta();
    this.createForm();
    this.setGrupoPergunta();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/indicadorquestionario`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.indicadorQuestionarioService.salvar(this.prepareSave()).subscribe((response: IndicadorQuestionario) => {
        this.model = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.router.navigate([`${environment.path_raiz_cadastro}/indicadorquestionario`]);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }
  private createForm() {
    this.indicadorQuestionarioForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      orientacao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(250),
        ]),
      ],
    });
  }

  private setGrupoPergunta() {
    this.id = this.activatedRoute.snapshot.params['id'];
    if (this.id) {
      this.carregarIndicadorQuestionario();
    }
  }

  private carregarIndicadorQuestionario(): void {
    this.indicadorQuestionarioService.buscarPorId(this.id).subscribe((grupoPergunta: IndicadorQuestionario) => {
      if (grupoPergunta && grupoPergunta.id) {
        this.model = grupoPergunta;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private converterModelParaForm() {
    this.indicadorQuestionarioForm.patchValue({
      descricao: this.model.descricao,
      orientacao: this.model.orientacao,
    });
  }

  private prepareSave(): IndicadorQuestionario {
    const formModel = this.indicadorQuestionarioForm.controls;
    const saveProdutoServico: IndicadorQuestionario = {
      id: this.model.id,
      descricao: formModel.descricao.value as string,
      orientacao: formModel.orientacao.value as string,
    };
    return saveProdutoServico;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.indicadorQuestionarioForm.controls['descricao'].invalid) {
      if (this.indicadorQuestionarioForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.indicadorQuestionarioForm.controls['descricao'],
          MensagemProperties.app_rst_labels_descricao);
        isValido = false;
      }
    }

    return isValido;
  }

}
