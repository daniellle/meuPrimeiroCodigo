import {MascaraUtil} from './../compartilhado/utilitario/mascara.util';
import {Seguranca} from './../compartilhado/utilitario/seguranca.model';
import {Usuario} from './../modelo/usuario.model';
import {DatePicker} from './../compartilhado/utilitario/date-picker';
import {Situacao} from 'app/modelo/enum/enum-situacao.model';

import {ListaPaginada} from './../modelo/lista-paginada.model';
import {Paginacao} from 'app/modelo/paginacao.model';
import {RecursoPipe} from './../compartilhado/utilitario/recurso.pipe';
import {Optional} from '@angular/core';
import {AbstractControl, FormGroup} from '@angular/forms';
import {BloqueioService} from '../servico/bloqueio.service';
import {ToastOptions, ToastyService} from 'ng2-toasty';
import {EstadoService} from 'app/servico/estado.service';
import {IOption} from 'ng-select';
import * as moment from 'moment';
import {ValidarData} from 'app/compartilhado/validators/data.validator';
import {PerfilEnum} from "../modelo/enum/enum-perfil";

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

    isNotVazia(valor: any): boolean {
        return !this.isVazia(valor);
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

    objToStrMap(obj) {
        let strMap = new Map();
        for (let k of Object.keys(obj)) {
            strMap.set(k, obj[k]);
        }
        return strMap;
    };

    strMapToObj(strMap) {
        let obj = Object.create(null);
        strMap.forEach((v, k) => {
            obj[k] = v;
        });
        return obj;
    }

    // validacao de data

    // validarData(data: any): boolean {
    //   const RegExPattern = '^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}$';

    //   if (!((data.match(RegExPattern)) && (data !== ''))) {
    //     return false;
    //   }
    //   return true;
    // }

    retiraLetra($event, str) {
        if ((str !== '0' && str !== '' && !Number(str)) || ($event.target.value.length > 10)) {
            $event.target.value = $event.target.value.substring(0, $event.target.value.length - 1);
            str = str.substring(0, str - 1);
            this.retiraLetra($event, str);
        }
        return $event.target.value;
    }

    maskDate($event, form?: FormGroup, campoForm?: string, verificaDataModel ?: string) {
        let str: string = $event.target.value;
        str = str.replace(new RegExp('/', 'g'), '');
        $event.target.value = this.retiraLetra($event, str);
        if ($event.target.value.length < 10 && $event.keyCode !== 8) {
            if ($event.target.value.length === 2 || $event.target.value.length === 5) {
                $event.target.value = $event.target.value += '/';
            }
        }
        if (verificaDataModel && $event.target.value !== '') {
            this.validacaoDataParaNgModel($event, verificaDataModel);
        }
        const existeFiltro = form && campoForm && $event.target.value.length === 10;
        if (form && campoForm) {
            form.controls[campoForm].patchValue(
                $event.target.value,
            );
        }

        if (existeFiltro) {
            if (ValidarData(moment($event.target.value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
                form.controls[campoForm].patchValue(
                    DatePicker.convertDateForMyDatePicker($event.target.value),
                );
            }
        }
    }

    validacaoDataParaNgModel($event, verificaDataModel: string) {
        verificaDataModel = ValidarData(moment($event.target.value, 'DD/MM/YYYY').format('YYYY-MM-DD')) ? 'yes' : '';
    }

    temPapel(...papeis: PerfilEnum[]): boolean {
        if (this.isNotEmpty(papeis)) {
            return this.contemPapel(papeis);
        }
    }

    naoTemPapel(...papeis: PerfilEnum[]): boolean {
        if (this.isNotEmpty(papeis)) {
            console.log(papeis);
            return !this.contemPapel(papeis);
        }
    }

    private contemPapel(papeis: PerfilEnum[]): boolean {
        let set = new Set<boolean>();
        papeis.forEach(papel => {
            if (this.usuarioLogado.papeis.indexOf(papel) > -1) {
                set.add(true);
            } else {
                set.add(false);
            }
        });
        return set.has(true);
    }

    contemPerfil(perfis: PerfilEnum[], usuario: Usuario): boolean {
        let set = new Set<boolean>();
        perfis.forEach(perfil => {
            if (usuario.perfisSistema.find((element) => element.perfil.codigo === perfil)) {
                set.add(true);
            } else {
                set.add(false);
            }
        });
        return set.has(true);
    }

}
