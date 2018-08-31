import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { ClassificacaoPontuacaoService } from './../../../servico/classificacao-pontuacao.service';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ClassificacaoPontuacao } from './../../../modelo/classificacao-pontuacao.model';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { isNumeric } from 'rxjs/util/isNumeric';

@Component({
  selector: 'app-cadastro-classificacao-pontuacao',
  templateUrl: './cadastro-classificacao-pontuacao.component.html',
  styleUrls: ['./cadastro-classificacao-pontuacao.component.scss'],
})
export class CadastroClassificacaoPontuacaoComponent extends BaseComponent implements OnInit {

  model: ClassificacaoPontuacao;
  id: number;
  classificacaoPontuacaoForm: FormGroup;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private classificacaoPontuacaoService: ClassificacaoPontuacaoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new ClassificacaoPontuacao();
    this.emModoConsulta();
    this.createForm();
    this.setGrupoPergunta();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.CLASSIFICACAO_PONTUACAO,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/classificacaopontuacao`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.classificacaoPontuacaoService.salvar(this.prepareSave()).subscribe((response: ClassificacaoPontuacao) => {
        this.model = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.router.navigate([`${environment.path_raiz_cadastro}/classificacaopontuacao`]);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  excluir(): void {
    this.classificacaoPontuacaoService.desativar(this.prepareSave()).subscribe((response: ClassificacaoPontuacao) => {
      this.model = response;
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.router.navigate([`${environment.path_raiz_cadastro}/classificacaopontuacao`]);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createForm() {
    this.classificacaoPontuacaoForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      mensagem: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      recomendacao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      valorMinimo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      valorMaximo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
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
    this.classificacaoPontuacaoService.buscarPorId(this.id).subscribe((grupoPergunta: ClassificacaoPontuacao) => {
      if (grupoPergunta && grupoPergunta.id) {
        this.model = grupoPergunta;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private converterModelParaForm() {
    this.classificacaoPontuacaoForm.patchValue({
      descricao: this.model.descricao,
      recomendacao: this.model.recomendacao,
      mensagem: this.model.mensagem,
      valorMaximo: this.model.valorMaximo,
      valorMinimo: this.model.valorMinimo,
    });
  }

  private prepareSave(): ClassificacaoPontuacao {
    const formModel = this.classificacaoPontuacaoForm.controls;
    const saveProdutoServico: ClassificacaoPontuacao = {
      id: this.model.id,
      recomendacao: formModel.recomendacao.value as string,
      mensagem: formModel.mensagem.value as string,
      valorMaximo: formModel.valorMaximo.value as number,
      valorMinimo: formModel.valorMinimo.value as number,
      descricao: formModel.descricao.value as string,
    };
    return saveProdutoServico;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.classificacaoPontuacaoForm.controls['descricao'].invalid) {
      if (this.classificacaoPontuacaoForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.classificacaoPontuacaoForm.controls['descricao'],
          MensagemProperties.app_rst_labels_descricao);
        isValido = false;
      }
    }

    if (this.classificacaoPontuacaoForm.controls['valorMinimo'].value === null) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.classificacaoPontuacaoForm.controls['valorMinimo'],
        MensagemProperties.app_rst_labels_pontuacao_minima);
      isValido = false;
    }

    if (this.classificacaoPontuacaoForm.controls['valorMaximo'].value === null) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.classificacaoPontuacaoForm.controls['valorMaximo'],
        MensagemProperties.app_rst_labels_pontuacao_maxima);
      isValido = false;
    }

    if (this.classificacaoPontuacaoForm.controls['valorMaximo'].value) {
      if (!isNumeric(this.classificacaoPontuacaoForm.controls['valorMaximo'].value)) {
        this.mensagemErroComParametros('app_rst_campo_inteiro', this.classificacaoPontuacaoForm.controls['valorMaximo'],
          MensagemProperties.app_rst_labels_pontuacao_maxima);
        isValido = false;
      }
    }

    if (this.classificacaoPontuacaoForm.controls['valorMinimo'].value) {
      if (!isNumeric(this.classificacaoPontuacaoForm.controls['valorMinimo'].value)) {
        this.mensagemErroComParametros('app_rst_campo_inteiro', this.classificacaoPontuacaoForm.controls['valorMinimo'],
          MensagemProperties.app_rst_labels_pontuacao_minima);
        isValido = false;
      }
    }

    if ((isNumeric(this.classificacaoPontuacaoForm.controls['valorMinimo'].value) &&
      isNumeric(this.classificacaoPontuacaoForm.controls['valorMaximo'].value)) &&
      Number(this.classificacaoPontuacaoForm.controls['valorMinimo'].value) >
      Number(this.classificacaoPontuacaoForm.controls['valorMaximo'].value)) {
      this.mensagemError(MensagemProperties.app_rst_mensagem_pontuacao_maxima_maior_que_pontuacao_minima);
      isValido = false;
    }

    return isValido;
  }

}
