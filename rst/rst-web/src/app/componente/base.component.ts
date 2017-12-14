import { MascaraUtil } from './../compartilhado/utilitario/mascara.util';
import { Seguranca } from './../compartilhado/utilitario/seguranca.model';
import { Usuario } from './../modelo/usuario.model';
import { DatePicker } from './../compartilhado/utilitario/date-picker';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';

import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { RecursoPipe } from './../compartilhado/utilitario/recurso.pipe';
import { Optional } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { BloqueioService } from '../servico/bloqueio.service';
import { ToastOptions, ToastyService } from 'ng2-toasty';
import { EstadoService } from 'app/servico/estado.service';
import { IOption } from 'ng-select';

export abstract class BaseComponent {

  municipios: IOption[];

  datePickerOptions = DatePicker.datePickerOptions;

  paginacao: Paginacao = new Paginacao();
  totalItens: number;
  processado = true;
  modoConsulta: boolean;
  modoAlterar: boolean;
  modoIncluir: boolean;
  title: string;
  usuarioLogado: Usuario;
  municipioVazio: boolean;
  mascaraCpf = MascaraUtil.mascaraCpf;
  qtdHoras = MascaraUtil.qtdHoras;
  mascaraCnpj = MascaraUtil.mascaraCnpj;
  mascaraTelefone = MascaraUtil.mascaraTelefone;
  mascaraTelefoneFixo = MascaraUtil.mascaraTelefoneFixo;
  mascaraCep = MascaraUtil.mascaraCep;
  mascaraData = MascaraUtil.mascaraNascimento;
  mascaraNit = MascaraUtil.mascaraNit;

  constructor(
    protected bloqueioService:
      BloqueioService,
    @Optional() protected dialogo?: ToastyService,
    protected estadoService?: EstadoService,
    protected recursoPipe?: RecursoPipe,
  ) {
    this.bloqueioService.evento.subscribe((evento) => this.setBloqueado(evento));
    this.recursoPipe = new RecursoPipe();
    this.municipios = [];
    this.usuarioLogado = Seguranca.getUsuario();
    this.municipioVazio = true;
  }

  private setBloqueado(processado: boolean) {
    this.processado = processado;
  }

  protected mensagemInformacao(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
      this.dialogo.info(this.getMensagem(mensagem, controle));
    }
  }

  protected mensagemSucesso(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
      this.dialogo.success(this.getMensagem(mensagem, controle));
    }
  }

  protected mensagemErroComParametros(mensagem: string, controle?: AbstractControl, ...parametros: any[]) {
    this.mensagemError(this.recursoPipe.transform(mensagem, parametros), controle);
  }

  protected mensagemErroComParametrosModel(mensagem: string, ...parametros: any[]) {
    this.mensagemError(this.recursoPipe.transform(mensagem, parametros));
  }

  protected mensagemAguardando(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
      this.dialogo.wait(this.getMensagem(mensagem, controle));
    }
  }

  protected mensagemError(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
      this.dialogo.error(this.getMensagem(mensagem, controle));
    }
  }

  protected mensagemAtencao(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
      this.dialogo.warning(this.getMensagem(mensagem, controle));
    }
  }

  private getMensagem(mensagem: string, controle?: AbstractControl): ToastOptions {

    const configuracoes: ToastOptions = {
      title: '',
      timeout: 5000,
      msg: mensagem,
      showClose: true,
      theme: 'bootstrap',
    };

    return configuracoes;

  }

  getCpfFormatado(cpf: string) {
    return MascaraUtil.formatarCpf(cpf);
  }
  getNitFormatado(nit: string) {
    return MascaraUtil.getNitFormatado(nit);
  }

  getCnpjFormatado(cnpj: string) {
    return MascaraUtil.formatarCnpj(cnpj);
  }

  pesquisarMunicipiosPorEstado(idEstado: number) {
    this.estadoService.pesquisarMunicipiosPorEstado(idEstado).subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      dados.forEach((element) => {
        const item = new Option();
        item.value = element.id;
        item.label = element.descricao;
        listaOption.push(item);
      });
      this.municipios = listaOption;
    },
      (erro) => {
        this.municipios = [];
      });
  }

  isVazia(valor: any): boolean {
    return valor === undefined || valor === null || valor === '';
  }

  listaUndefinedOuVazia(valor: any[]): boolean {
    if (!valor || (valor && valor.length === 0)) {
      return true;
    } else {
      return false;
    }
  }

  isNull(valor: any): boolean {
    return valor === null || valor === 'null';
  }

  isUndefined(valor: any): boolean {
    return valor === undefined || valor === 'undefined';
  }

  removeItemOfList(item: any, list: any[]) {
    const index: number = list.indexOf(item);
    if (index !== -1) {
      list.splice(index, 1);
    }
  }

  isEmpty(list: any[]): boolean {
    if (list && list.length > 0) {
      return false;
    }
    return true;
  }

  isNotEmpty(list: any[]): boolean {
    if (list && list.length > 0) {
      return true;
    }
    return false;
  }

  getPaginacao(paginacao: Paginacao, retorno: ListaPaginada<any>): Paginacao {
    if (retorno.list != null) {
      paginacao.totalItens = retorno.quantidade;
    }
    return paginacao;
  }

  getSituacaoPorData(data: any) {
    return data ? Situacao.I : Situacao.A;
  }

  orderByDescricao(list: any[]) {
    if (!this.listaUndefinedOuVazia(list)) {
      list.sort((left, right): number => {
        if (left.descricao > right.descricao) {
          return 1;
        }
        if (left.descricao < right.descricao) {
          return -1;
        }
        return 0;
      });
    }
  }

  convertDateToString(data: any): string {
    return data.day + '/' + data.month + '/' + data.year;
  }

  getCnpjCpfFormatado(valor: string): string {
    let string = undefined;
    if (valor.length <= 11) {
      string = MascaraUtil.formatarCpf(valor);
    } else {
      string = MascaraUtil.formatarCnpj(valor);
    }
    return string;
  }

  convertDateToStringUS(data: any): string {
    return `${data.date.year}/${data.date.month}/${data.date.day}`;
  }

  isSomenteTrabalhador(): boolean {
    return this.usuarioLogado.papeis.length === 1 &&
      this.usuarioLogado.papeis.indexOf('TRA') > -1;
  }

  getIdTrabalhadorLogado(): string {
    if (this.usuarioLogado.dados.idsTrabalhador
      && this.usuarioLogado.dados.idsTrabalhador.length > 0) {
      return this.usuarioLogado.dados.idsTrabalhador[0];
    }
  }

  hasPermissao(nomePermissao) {
    return Seguranca.isPermitido([nomePermissao]);
  }

}
