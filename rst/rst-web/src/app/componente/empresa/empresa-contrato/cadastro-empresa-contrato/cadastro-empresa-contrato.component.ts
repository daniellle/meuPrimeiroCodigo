import {Component, OnInit} from '@angular/core';
import {Empresa} from "../../../../modelo/empresa.model";
import {Contrato} from "../../../../modelo/contrato.model";
import {FiltroEmpresaContrato} from "../../../../modelo/filtro-empresa-contrato.model";
import {Paginacao} from "../../../../modelo/paginacao.model";
import {TrabalhadorService} from "../../../../servico/trabalhador.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EmpresaService} from "../../../../servico/empresa.service";
import {BloqueioService} from "../../../../servico/bloqueio.service";
import {ToastyService} from "ng2-toasty";
import {EmpresaContratoService} from "../../../../servico/empresa-contrato.service";
import {BaseComponent} from "../../../base.component";
import {UnidadeObraService} from "../../../../servico/unidade-obra.service";
import {UnidadeAtendimentoTrabalhador} from "../../../../modelo/unid-atend-trabalhador.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {environment} from "../../../../../environments/environment";
import {UnidadeObra} from "../../../../modelo/unidade-obra.model";
import {ValidateData} from "../../../../compartilhado/validators/data.validator";
import {MensagemProperties} from "../../../../compartilhado/utilitario/recurso.pipe";
import {UatService} from "../../../../servico/uat.service";
import {Usuario} from "../../../../modelo/usuario.model";
import {Seguranca} from "../../../../compartilhado/utilitario/seguranca.model";
import {TipoProgramaService} from "../../../../servico/tipo-programa.service";
import {TipoPrograma} from "../../../../modelo/tipo-programa.model";
import { datasContratoValidator } from './datas-contrato.validator';
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";
import {DepartamentoRegional} from "../../../../modelo/departamento-regional.model";
import {UsuarioEntidadeService} from "../../../../servico/usuario-entidade.service";
import {FiltroUsuarioEntidade} from "../../../../modelo/filtro-usuario-entidade.model";
import {PerfilEnum} from "../../../../modelo/enum/enum-perfil";
import {ListaPaginada} from "../../../../modelo/lista-paginada.model";
import {UsuarioEntidade} from "../../../../modelo/usuario-entidade.model";
import {Uat} from "../../../../modelo/uat.model";
import {DepartamentoRegionalProdutoServicoService} from "../../../../servico/departamento-regional-produto-servico.service";
import {DepartRegionalService} from "../../../../servico/depart-regional.service";
import {FiltroDepartRegional} from "../../../../modelo/filtro-depart-regional.model";
import {MdOptionSelectionChange} from "@angular/material";

@Component({
    selector: 'app-cadastro-empresa-contrato',
    templateUrl: './cadastro-empresa-contrato.component.html',
    styleUrls: ['./cadastro-empresa-contrato.component.scss']
})
export class CadastroEmpresaContratoComponent extends BaseComponent implements OnInit {

    idEmpresa: number;
    empresa: Empresa;
    contrato: Contrato;
    model: Contrato;
    filtro: FiltroEmpresaContrato;
    listaContratos: Contrato[];
    bloqueado: boolean;
    paginacao: Paginacao = new Paginacao(1, 10);
    inconsistencia: boolean;
    mensagemInconsistencia: string;
    contratoForm: FormGroup;
    unidadesObra: Observable<UnidadeObra[]>;
    unidadeObra: UnidadeObra = new UnidadeObra();
    unidadesAT: Observable<UnidadeAtendimentoTrabalhador[]>;
    unidadeSesi: UnidadeAtendimentoTrabalhador;
    usuarioLogado: Usuario;
    flagUsuario: string;
    drs: DepartamentoRegional[];
    uats: Uat[];
    tiposPrograma: TipoPrograma[];
    public delayerUndObra = new Subject<string>();
    public delayerUndSesi = new Subject<string>();
    isDr: boolean;
    isUnidadeSesi: boolean;
    filtroUsuarioEntidade: FiltroUsuarioEntidade;
    listaUsuarioEntidade: UsuarioEntidade[];
    filtroDepartRegional: FiltroDepartRegional;
    drSelecionado: DepartamentoRegional;


    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private contratoService: EmpresaContratoService,
        private unidadeObraService: UnidadeObraService,
        private unidadeATService: UatService,
        private tipoProgramaService: TipoProgramaService,
        private usuarioEntidadeService: UsuarioEntidadeService,
        private drService: DepartRegionalService,
    ) {
        super(bloqueioService, dialogo);
        this.delayerUndObra.debounceTime(500).distinctUntilChanged().switchMap(
            (text) => this.unidadesObra = this.pesquisarUnidadeObrasPorNome(text)).subscribe();
        this.delayerUndSesi.debounceTime(500).distinctUntilChanged().switchMap(
            (text) => this.unidadesAT = this.pesquisarUnidadeSesi(text)).subscribe();
    }

    ngOnInit() {
        this.drs = new Array<DepartamentoRegional>();
        this.uats = new Array<Uat>();
        this.filtroUsuarioEntidade = new FiltroUsuarioEntidade();
        this.setEmpresa();
        this.usuarioLogado = Seguranca.getUsuario();
        this.verificarPerfil();
        if(!this.isDr && !this.isUnidadeSesi){
            this.trazerDrs();
        }
        this.contrato = new Contrato();
        this.empresa = new Empresa();
        this.filtro = new FiltroEmpresaContrato();
        this.filtroDepartRegional = new FiltroDepartRegional();
        this.model = new Contrato();
        this.listaContratos = new Array<Contrato>();
        this.carregarCombo();
        this.title = MensagemProperties.app_rst_empresa_contrato_cadastrar_title;
        this.createForm();

    }

    displayFn(unidadeObra?: UnidadeObra): String | undefined {

        return unidadeObra ? unidadeObra.descricao : undefined;
    }

    displayFnSesi(unidadeSesi?: UnidadeAtendimentoTrabalhador): String | undefined {
        return unidadeSesi ? unidadeSesi.razaoSocial : undefined;
    }


    verificarPerfil(){
        this.filtroUsuarioEntidade.cpf = this.usuarioLogado.sub;
        this.usuarioLogado.papeis.forEach(perfil => {
            if (perfil === PerfilEnum.SUDR || perfil === PerfilEnum.DIDR || perfil === PerfilEnum.GDRA
                || perfil === PerfilEnum.GDRM) {
                this.isDr = true;
                this.pegaDrsDoUsuario()
            }
            else if(perfil === PerfilEnum.GUS) {
                this.isUnidadeSesi = true;
                this.pegaUatsDoUsuario()
            }
        });
    }

    pegaDrsDoUsuario(){
        this.filtroUsuarioEntidade.cpf = this.usuarioLogado.sub;
        if(this.isUndefined(this.filtroUsuarioEntidade.idEstado)){
            this.filtroUsuarioEntidade.idEstado = '0';
        }
        this.usuarioEntidadeService.pesquisarPaginado(this.filtroUsuarioEntidade, this.paginacao, 'Departamento Regional')
            .subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
                if(this.filtroUsuarioEntidade.idEstado == '0'){
                    this.filtroUsuarioEntidade.idEstado = undefined;
                }
                if(retorno.quantidade > 0){
                    this.listaUsuarioEntidade = retorno.list;
                    this.listaUsuarioEntidade.forEach( usuarioEntidade => {
                        this.drs.push(usuarioEntidade.departamentoRegional);
                    })
                }
        }, (error) => {
                this.mensagemError(error);
            })
    }

    pegaUatsDoUsuario(){
        this.filtroUsuarioEntidade.cpf = this.usuarioLogado.sub;
        if(this.isUndefined(this.filtroUsuarioEntidade.idEstado)){
            this.filtroUsuarioEntidade.idEstado = '0';
        }
        this.usuarioEntidadeService.pesquisarPaginado(this.filtroUsuarioEntidade, this.paginacao, 'Unidade SESI')
            .subscribe( (retorno: ListaPaginada<UsuarioEntidade>) => {
                if(this.filtroUsuarioEntidade.idEstado == '0'){
                    this.filtroUsuarioEntidade.idEstado = undefined;
                }
                if(retorno.quantidade > 0){
                    this.listaUsuarioEntidade = retorno.list;
                    this.listaUsuarioEntidade.forEach( usuarioEntidade => {
                        this.uats.push(usuarioEntidade.uat);
                        this.unidadeATService.pesquisarPorId(usuarioEntidade.uat.id.toString()).subscribe(response =>{
                            let uat
                            uat = response;
                            if(this.drs == null || this.drs == [] || this.drs == undefined || this.drs.length == 0){
                                this.drs.push(uat.departamentoRegional);
                            }
                            this.drs.forEach( dr =>{
                                if(dr.id != uat.departamentRegional.id){
                                    this.drs.push(uat.departamentoRegional);
                                }
                            })
                        })
                    })
                }
            }), (error) => {
                this.mensagemError(error);
            }
    }

    setEmpresa() {
        this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    }

    trazerDrs(){
        this.drService.listarTodos().subscribe( response => {
            this.drs = response;
        },(error) => {
            this.mensagemError(error);
        });
    }

    alteraDr(){
        this.filtroDepartRegional.razaoSocial = this.contratoForm.controls['dr'].value;
        if(this.filtroDepartRegional.idEstado == undefined){
            this.filtroDepartRegional.idEstado = '0';
        }
        if(this.contratoForm.controls['dr'].value != "" ) {
            this.drService.pesquisar(this.filtroDepartRegional, new Paginacao(1, 10))
                .subscribe(response => {
                    this.drSelecionado = response.list[0];
                }), (error) => {
                this.mensagemError(error);
            }
        }
    }

    verificarCampos() {
        if (this.contratoForm.controls['unidadeObra'].errors) {
            if (this.contratoForm.controls['unidadeObra'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['unidadeObra'],
                    MensagemProperties.app_rst_labels_unidade_obra);
            }
        }
        if (this.contratoForm.controls['dataContratoInicio'].errors) {
                if (this.contratoForm.controls['dataContratoInicio'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dataContratoInicio'],
                    MensagemProperties.app_rst_labels_data_inicio);
            }
        }
        if (this.contratoForm.controls['dataContratoFim'].errors) {
            if (this.contratoForm.controls['dataContratoFim'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dataContratoFim'],
                    MensagemProperties.app_rst_labels_data_fim);
            }
        }
        if (this.contratoForm.controls['tipoPrograma'].errors) {
            if (this.contratoForm.controls['tipoPrograma'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['tipoPrograma'],
                    MensagemProperties.app_rst_labels_tipo_programa);
            }
        }

        if (this.contratoForm.controls['anoVigencia'].errors) {
            if (this.contratoForm.controls['anoVigencia'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['anoVigencia'],
                    MensagemProperties.app_rst_labels_ano_vigencia);
            }
           else{
                this.mensagemError("Inserir um valor de ano entre 1900 e 3000");
                return;
            }
        }
        if(!this.isUnidadeSesi) {
            this.contratoForm.controls['unidadeAtendimentoTrabalhador'].setValidators(Validators.required);
            if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors) {
                if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors.required) {
                    this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['unidadeAtendimentoTrabalhador'],
                        MensagemProperties.app_rst_labels_unidade_sesi);
                }
            }
            if (this.contratoForm.controls['dr'].errors) {
                if (this.contratoForm.controls['dr'].errors.required) {
                    this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dr'],
                        MensagemProperties.app_rst_labels_departamento_regional);
                }
            }
        }
        if(this.isUnidadeSesi){
            this.contratoForm.controls['unidadeAtendimentoTrabalhadorSelect'].setValidators(Validators.required);
            if (this.contratoForm.controls['unidadeAtendimentoTrabalhadorSelect'].errors) {
                if (this.contratoForm.controls['unidadeAtendimentoTrabalhadorSelect'].errors.required) {
                    this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['unidadeAtendimentoTrabalhadorSelect'],
                        MensagemProperties.app_rst_labels_unidade_sesi);
                }
            }
        }
        if(this.contratoForm.errors != null) {
            if (this.contratoForm.errors.intervaloDeDatas) {
                this.mensagemError("A data final deve ser maior que a data de início");
            }
        }
    }

    salvar() {
        if (this.contratoForm.valid) {
            this.contratoForm.removeControl('dr');
            if(this.isUnidadeSesi){
                this.contratoForm.removeControl('unidadeAtendimentoTrabalhador')
            }
            else{
                this.contratoForm.removeControl('unidadeAtendimentoTrabalhadorSelect')
            }
            this.contrato = this.contratoForm.getRawValue() as Contrato
            this.contrato.dataContratoFim = this.contrato.dataContratoFim.formatted;
            this.contrato.dataContratoInicio = this.contrato.dataContratoInicio.formatted;
            this.contratoService.salvar(this.contrato)
                .subscribe(() => {
                    this.mensagemSucesso("Contrato criado com sucesso");
                    this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/contrato`]);
                })
        }
        else {
            this.contratoForm.updateValueAndValidity()
            this.verificarCampos();
        }

    }

    carregarCombo() {
        this.tipoProgramaService.pesquisarTodos().subscribe(response => {
            this.tiposPrograma = response;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    pesquisarUnidadeObrasPorNome(text: string): Observable<UnidadeObra[]> {
        return this.unidadeObraService.pesquisarPorNome(text, this.idEmpresa);
    }

    pesquisarUnidadeSesi(text: string): Observable<UnidadeAtendimentoTrabalhador[]> {
        return this.unidadeATService.pesquisarPorNome(text, this.drSelecionado.id);
    }


    createForm() {
        this.contratoForm = this.formBuilder.group({
            dataContratoInicio: [
                '',
                [
                    Validators.required, ValidateData,
                ],
            ],
            dataContratoFim: [
                '',
                [
                    Validators.required,
                    ValidateData
                ],
            ],
            unidadeObra: [
                '',
                [
                    Validators.required,
                    Validators.maxLength(160),
                ],
            ],
            dr:[
                '',
                [
                    Validators.required,
                ]
            ],
            anoVigencia: [
                '',
                [
                    Validators.maxLength(4),
                    Validators.required,
                    Validators.minLength(4),
                    Validators.min(1900),
                    Validators.max(2600),

                ],
            ],
            unidadeAtendimentoTrabalhador: [
                '',
                [
                    Validators.maxLength(100),
                ],
            ],
            unidadeAtendimentoTrabalhadorSelect: [
                '',
                [

                ]
            ],
            tipoPrograma: [
                '',
                [
                    Validators.maxLength(100),
                    Validators.required
                ],
            ]
        }, {
            validator: datasContratoValidator
        });
    }

    voltar() {
        if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/contrato`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/contrato`]);
        }
    }


}
