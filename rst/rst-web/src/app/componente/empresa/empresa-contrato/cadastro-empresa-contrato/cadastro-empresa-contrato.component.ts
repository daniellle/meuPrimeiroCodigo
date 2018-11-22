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
    unidadeObra: UnidadeObra;
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
    ) {
        super(bloqueioService, dialogo);
        this.delayerUndObra.debounceTime(500).distinctUntilChanged().switchMap(
            (text) => this.unidadesObra = this.pesquisarUnidadeObrasPorNome(text)).subscribe();
        this.delayerUndSesi.debounceTime(500).distinctUntilChanged().switchMap(
            (text) => this.unidadesAT = this.pesquisarUnidadeSesi(text)).subscribe();
    }

    ngOnInit() {
        this.setEmpresa();
        this.usuarioLogado = Seguranca.getUsuario();
        this.contrato = new Contrato();
        this.empresa = new Empresa();
        this.filtro = new FiltroEmpresaContrato();
        this.model = new Contrato();
        this.drs = new Array<DepartamentoRegional>();
        this.listaContratos = new Array<Contrato>();
        // this.unidadesObra = new Array<UnidadeObra>();
        this.carregarCombo();
        this.title = MensagemProperties.app_rst_empresa_contrato_cadastrar_title;
        this.createForm();

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
                    })
                }
            }), (error) => {
                this.mensagemError(error);
            }
    }

    setEmpresa() {
        this.idEmpresa = this.activatedRoute.snapshot.params['id'];
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
        if (this.contratoForm.controls['dr'].errors) {
            if (this.contratoForm.controls['dr'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dr'],
                    MensagemProperties.app_rst_labels_departamento_regional);
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
        if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors) {
            if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['unidadeAtendimentoTrabalhador'],
                    MensagemProperties.app_rst_labels_unidade_sesi);
            }
        }
        if(this.contratoForm.errors.intervaloDeDatas){
            this.mensagemError("A data final deve ser maior que a data de início");
        }
    }

    salvar() {
        if (this.contratoForm.valid) {
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
            console.log(this.contratoForm.errors);
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
        console.log(this.unidadeATService.pesquisarTodos());
        return this.unidadeATService.pesquisarTodos();
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
                    Validators.required
                ],
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
