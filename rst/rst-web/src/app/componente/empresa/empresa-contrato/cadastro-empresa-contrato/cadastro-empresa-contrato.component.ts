import {Component, OnInit} from '@angular/core';
import {Empresa} from "../../../../modelo/empresa.model";
import {Contrato} from "../../../../modelo/contrato.model";
import {FiltroEmpresaContrato} from "../../../../modelo/filtro-empresa-contrato.model";
import {Paginacao} from "../../../../modelo/paginacao.model";
import {TrabalhadorService} from "../../../../servico/trabalhador.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EmpresaTrabalhadorService} from "../../../../servico/empresa-trabalhador.service";
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
import {EmpresaTrabalhador} from "../../../../modelo/empresa-trabalhador.model";
import {ValidateData} from "../../../../compartilhado/validators/data.validator";
import {ValidateCNPJ} from "../../../../compartilhado/validators/cnpj.validator";
import {ValidarNit} from "../../../../compartilhado/validators/nit.validator";
import {ValidateEmail} from "../../../../compartilhado/validators/email.validator";
import {MensagemProperties} from "../../../../compartilhado/utilitario/recurso.pipe";
import {UatService} from "../../../../servico/uat.service";
import {Usuario} from "../../../../modelo/usuario.model";
import {Seguranca} from "../../../../compartilhado/utilitario/seguranca.model";
import {Perfil} from "../../../../modelo/perfil.model";
import {PerfilEnum} from "../../../../modelo/enum/enum-perfil";
import {TipoProgramaService} from "../../../../servico/tipo-programa.service";
import {TipoPrograma} from "../../../../modelo/tipo-programa.model";
import * as moment from 'moment';

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
    unidadesObra: UnidadeObra[];
    unidadeObra: UnidadeObra;
    unidadesAT: UnidadeAtendimentoTrabalhador[];
    usuarioLogado: Usuario;
    flagUsuario: string;
    tiposPrograma: TipoPrograma[];


    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private empresaService: EmpresaService,
        private activatedRoute: ActivatedRoute,
        private trabalhadorService: TrabalhadorService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private contratoService: EmpresaContratoService,
        private unidadeObraService: UnidadeObraService,
        private unidadeATService: UatService,
        private tipoProgramaService: TipoProgramaService,
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.setEmpresa();
        this.usuarioLogado = Seguranca.getUsuario();
        this.contrato = new Contrato();
        this.empresa = new Empresa();
        this.filtro = new FiltroEmpresaContrato();
        this.model = new Contrato();
        this.listaContratos = new Array<Contrato>();
        this.unidadesObra = new Array<UnidadeObra>();
        this.createForm();
        this.carregarCombo();
        this.title = MensagemProperties.app_rst_empresa_contrato_cadastrar_title;

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
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dataInicio'],
                    MensagemProperties.app_rst_labels_data_inicio);
            }
        }
        if (this.contratoForm.controls['dataContratoFim'].errors) {
            if (this.contratoForm.controls['dataContratoFim'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['dataFim'],
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
        }
        if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors) {
            if (this.contratoForm.controls['unidadeAtendimentoTrabalhador'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.contratoForm.controls['unidadeAtendimentoTrabalhador'],
                    MensagemProperties.app_rst_labels_unidade_sesi);
            }
        }
    }

    salvar() {
        if (this.contratoForm.valid) {
            this.contrato = this.contratoForm.getRawValue() as Contrato
            this.contrato.dataContratoFim = moment( this.contrato.dataContratoFim).format('YYYY-MM-DD')
            this.contrato.dataContratoInicio = moment( this.contrato.dataContratoInicio).format('YYYY-MM-DD')
            console.log(this.contrato);
            this.contratoService.salvar(this.contrato)
                .subscribe(() => {
                    this.mensagemSucesso("Contrato criado com sucesso");
                    this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/contrato`]);
                })
        }
        else {
            this.verificarCampos();
        }

    }

    carregarCombo() {
        this.unidadeObraService.pesquisarPorEmpresa(this.idEmpresa).subscribe(response => {
            this.unidadesObra = response;
        }, (error) => {
            this.mensagemError(error);
        });

        this.unidadeATService.pesquisarTodos().subscribe(response => {
            this.unidadesAT = response;
        }, (error) => {
            this.mensagemError(error);
        });

        this.tipoProgramaService.pesquisarTodos().subscribe(response => {
            this.tiposPrograma = response;
        }, (error) => {
            this.mensagemError(error);
        });

    }


    createForm() {
        this.contratoForm = this.formBuilder.group({
            dataContratoInicio: [
                {value: null},
                Validators.compose([
                    Validators.required, ValidateData,
                ]),
            ],
            dataContratoFim: [
                {value: null},
                Validators.compose([
                    Validators.required,
                    ValidateData
                ]),
            ],
            unidadeObra: [
                {value: undefined},
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            anoVigencia: [
                {value: null},
                Validators.compose([
                    Validators.maxLength(4),
                    Validators.required,
                    Validators.minLength(4),

                ]),
            ],
            unidadeAtendimentoTrabalhador: [
                {value: null},
                Validators.compose([
                    Validators.maxLength(100),
                    Validators.required
                ]),
            ],
            tipoPrograma: [
                {value: null},
                Validators.compose([
                    Validators.maxLength(100),
                    Validators.required
                ]),
            ]
        });
    }

    voltar() {
        if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/contrato`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/`]);
        }
    }


}
