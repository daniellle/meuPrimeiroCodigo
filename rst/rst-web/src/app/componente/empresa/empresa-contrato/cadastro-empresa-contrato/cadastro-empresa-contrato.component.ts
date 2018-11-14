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
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.setEmpresa();
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


    carregarUnidadesObra(id: number) {

    }

    private prepareSave(model: Contrato): Contrato {
        const contrato = {
            id: model.id,
            dataContratoInicio: model.dataContratoInicio,
            dataContratoFim: model.dataContratoFim,
            unidadeObra: model.unidadeObra,
            anoVigencia: model.anoVigencia,
            unidadeAtendimentoTrabalhador: model.unidadeAtendimentoTrabalhador,
            fl_inativo: model.fl_inativo,
            tipoPrograma: model.tipoPrograma
        };
        return contrato as Contrato;
    }


    setEmpresa() {
        this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    }

    salvar() {

    }

    validar() {

    }

    carregarCombo(){
        this.unidadeObraService.pesquisarPorEmpresa(this.idEmpresa).subscribe(response => {
            this.unidadesObra = response;
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
                    Validators.required
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
