import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LinhaService } from './../../../servico/linha.service';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { Linha } from './../../../modelo/linha.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-cadastro-produto-servico',
  templateUrl: './cadastro-produto-servico.component.html',
  styleUrls: ['./cadastro-produto-servico.component.scss'],
})
export class CadastroProdutoServicoComponent extends BaseComponent implements OnInit {

  model: ProdutoServico;
  id: number;
  produtoServicoForm: FormGroup;
  listaLinha: Linha[];
  novaLinha: Linha;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private linhaService: LinhaService,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private produoServicoService: ProdutoServicoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.model = new ProdutoServico();
    this.listaLinha = new Array<Linha>();
    this.novaLinha = new Linha();
    this.emModoConsulta();
    this.createForm();
    this.setProdutoServico();
    this.carregarListaLinha();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.PRODUTO_SERVICO,
      PermissoesEnum.PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.PRODUTO_SERVICO_DESATIVAR]);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/produtoservico`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.produoServicoService.salvar(this.prepareSave()).subscribe((response: ProdutoServico) => {
        this.model = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  adicionarLinha(modalAdicionarLinha) {
    const linha = this.novaLinha;
    this.modalService.open(modalAdicionarLinha).result
      .then((result) => {
        if (this.isLinhaValida(linha)) {
          this.salvarLinha(linha);
        }
      }, (reason) => {
        this.limparNovaLinha();
      });
  }

  private limparNovaLinha(): void {
    this.novaLinha = new Linha();
  }

  private salvarLinha(linha: Linha): void {
    this.linhaService.salvar(linha).subscribe((response: Linha) => {
      this.carregarListaLinha();
      this.setarLinhaCadastrada(response);
      this.limparNovaLinha();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private setarLinhaCadastrada(linha: Linha): void {
    this.produtoServicoForm.patchValue({
      linha: linha.id,
    });
  }

  private isLinhaValida(linha: Linha): boolean {
    return linha.descricao ? true : false;
  }

  private setProdutoServico() {
    this.id = this.activatedRoute.snapshot.params['id'];
    if (this.id) {
      this.carregarProdutoServico();
    }
  }

  private carregarProdutoServico(): void {
    this.produoServicoService.buscarPorId(this.id).subscribe((produtoServico: ProdutoServico) => {
      if (produtoServico && produtoServico.id) {
        this.model = produtoServico;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createForm() {
    this.produtoServicoForm = this.formBuilder.group({
      nome: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      descricao: [
        { value: null, disabled: this.modoConsulta },
      ],
      linha: [
        { value: '', disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

  private converterModelParaForm() {
    this.produtoServicoForm.patchValue({
      nome: this.model.nome,
      descricao: this.model.descricao,
      linha: this.model.linha.id,
    });
  }

  private prepareSave(): ProdutoServico {

    const formModel = this.produtoServicoForm.controls;
    const saveProdutoServico: ProdutoServico = {
      id: this.model.id,
      dataCriacao: this.model.dataCriacao,
      dataAlteracao: this.model.dataAlteracao,
      dataExclusao: this.model.dataExclusao,
      nome: formModel.nome.value as string,
      descricao: formModel.descricao.value as string,
      linha: { id: formModel.linha.value } as Linha,
    };
    return saveProdutoServico;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.produtoServicoForm.controls['nome'].invalid) {
      if (this.produtoServicoForm.controls['nome'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.produtoServicoForm.controls['nome'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }
    }

    if (!this.produtoServicoForm.controls['linha'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.produtoServicoForm.controls['linha'],
        MensagemProperties.app_rst_labels_linha);
      isValido = false;
    }

    return isValido;
  }

  private carregarListaLinha(): void {
    this.linhaService.buscarTodas().subscribe((listaLinha: Linha[]) => {
      this.listaLinha = listaLinha;
    }, (error) => {
      this.mensagemError(error);
    });
  }

}
