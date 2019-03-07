import { PermissoesEnum } from './../../../modelo/enum/enum-permissoes';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { Component, Input, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UatQuadroPessoalService } from 'app/servico/uat-quadro-pessoal.service';
import { UatQuadroPessoalAreaService } from 'app/servico/uat-quadro-pessoal-area.service';
import { UatQuadroPessoalTipoProfissionalService } from 'app/servico/uat-quadro-pessoal-tipo-profissional.service';
import { UatQuadroPessoaTipoServicoService } from 'app/servico/uat-quadro-pessoal-tipo-servico.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { UatQuadroPessoalArea } from 'app/modelo/uat-quadro-pessoal-area';
import { UatQuadroPessoalTipoServico } from 'app/modelo/uat-quadro-pessoal-tipo-servico';
import { UatQuadroPessoal } from 'app/modelo/uat-quadro-pessoal';
import { UatQuadroPessoalTipoProfissional } from 'app/modelo/uat-quadro-pessoal-tipo-profissional';
import { UnidadeAtendimentoTrabalhador } from 'app/modelo/unid-atend-trabalhador.model';

@Component({
    selector: 'app-uat-quadro-pessoal',
    templateUrl: './uat-quadro-pessoal.component.html',
    styleUrls: ['./uat-quadro-pessoal.component.scss'],
})
export class UatQuadroPessoalComponent extends BaseComponent implements OnInit {

    @Input()
    private idUnidade: Number;

    private form: FormGroup;

    objectKeys = Object.keys;

    modal;

    modalRef;

    idQuadroPessoalExcluir: Number;

    quadrosPessoaisAgg: any = null;

    listArea: UatQuadroPessoalArea[] = [];

    listTipoServico: UatQuadroPessoalTipoServico[] = [];

    listTipoProfissional: UatQuadroPessoalTipoProfissional[] = [];

    listQuadroPessoalSave: UatQuadroPessoal[] = [];

    /*
    areaSelecionada: any = null;

    tipoServicoSelecionado: any = null;

    tipoProfissionalSelecionado: any = null;
*/

    constructor(
        private formBuilder: FormBuilder,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private uatQuadroPessoalService: UatQuadroPessoalService,
        private uatQuadroPessoalAreaService: UatQuadroPessoalAreaService,
        private uatQuadroPessoalTipoServicoService: UatQuadroPessoaTipoServicoService,
        private uatQuadroPessoalTipoProfissionalService: UatQuadroPessoalTipoProfissionalService,
        public modalAtivo: NgbActiveModal,
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
    ) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_cadastro_quadro_pessoal_title;
        this.emModoConsulta();
    }

    ngOnInit(): void {
        this.initForm();
        this.loadAreas();
        this.findQuadroPessoalAgg();
    }

    private initForm() {
        this.form = this.formBuilder.group({
            area: [{ value: null, disabled: this.modoConsulta }, Validators.required],
            tipoServico: [{ value: null, disabled: this.modoConsulta }, Validators.required],
        });
    }

    private emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.CAT_ESTRUTURA_CADASTRAR]);
    }

    openModal(modal: any) {
        this.modalRef = this.modalService.open(modal, { size: 'sm' });
    }

    selecionarRemover(idQuadroPessoalExcluir: Number) {
        this.idQuadroPessoalExcluir = idQuadroPessoalExcluir;
    }

    private loadAreas() {
        this.uatQuadroPessoalAreaService.findAll().subscribe((areas) => {
            this.listArea = areas;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private loadTipoServico(idArea: Number) {
        this.uatQuadroPessoalTipoServicoService.findByArea(idArea).subscribe((tiposServicos) => {
            this.listTipoServico = tiposServicos;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private loadTipoProfissional(idTipoServico: Number) {
        this.uatQuadroPessoalTipoProfissionalService.findByTipoServico(idTipoServico).subscribe((tiposProfissionais) => {
            this.listTipoProfissional = tiposProfissionais;
            this.changeForm();
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private changeForm() {
        this.listQuadroPessoalSave = [];
        for (const prof of this.listTipoProfissional) {
            this.listQuadroPessoalSave.push(
                new UatQuadroPessoal(null,
                    new UatQuadroPessoalTipoProfissional(prof.id, null, prof.descricao),
                    new UnidadeAtendimentoTrabalhador(this.idUnidade)));
        }
    }

    salvar() {
        this.uatQuadroPessoalService.salvar(this.listQuadroPessoalSave).subscribe((data) => {
            this.form.reset();
            this.listQuadroPessoalSave = [];
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.findQuadroPessoalAgg();
        },
            (error) => {
                this.mensagemError(error);
            });
    }

    limpar() {
        this.form.reset();
        this.listQuadroPessoalSave = [];
        this.findQuadroPessoalAgg();
    }

    changeCombo(name: string) {
        if (name === 'area') {
            const idArea = this.form.get('area').value;
            if (idArea !== null && idArea !== undefined) {
                this.loadTipoServico(idArea);
            }
            this.form.patchValue({ tipoServico: null });
        } else if (name === 'tipoServico') {
            const idTipoServico = this.form.get('tipoServico').value;
            if (idTipoServico !== null && idTipoServico !== undefined) {
                this.loadTipoProfissional(idTipoServico);
            }
        }
    }

    findQuadroPessoalAgg() {
        if (this.idUnidade !== null && this.idUnidade !== undefined) {
            this.uatQuadroPessoalService.findByUnidadeAgg(this.idUnidade).subscribe(
                (data) => {
                    this.quadrosPessoaisAgg = data;
                }, (error) => {
                    this.mensagemError(error);
                },
            );
        }
    }

    showComboTipoServico() {
        const idArea = this.form.get('area').value;
        return idArea !== null && idArea !== undefined;
    }

    showFormQuadroPessoal() {
        const tipoServico = this.form.get('tipoServico').value;
        const idArea = this.form.get('area').value;
        return tipoServico != null && tipoServico !== undefined &&
            idArea !== null && idArea !== undefined &&
            this.listQuadroPessoalSave !== null &&
            this.listQuadroPessoalSave !== undefined &&
            this.listQuadroPessoalSave.length > 0;
    }

    showGridQuadroPessoal() {
        return this.quadrosPessoaisAgg !== null &&
            this.quadrosPessoaisAgg !== undefined &&
            this.quadrosPessoaisAgg !== {};
    }

    hasPermissaoDesativar() {
        return Seguranca.isPermitido(
            [PermissoesEnum.CAT_ESTRUTURA_DESATIVAR]);
    }

    desativar() {
        this.uatQuadroPessoalService.desativar(this.idQuadroPessoalExcluir, this.idUnidade).subscribe(
            (data) => {
                this.mensagemSucesso(data['content']);
                this.findQuadroPessoalAgg();
            }, (error) => {
                this.mensagemError(MensagemProperties.app_rst_erro_geral);
            });
    }

}
