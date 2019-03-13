import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from "app/componente/base.component";
import { OnInit, Component, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { ToastyService } from 'ng2-toasty';
import { UatInstalacaoFisicaService } from 'app/servico/uat-instalacao-fisica.service';
import { UatInstalacaoFisicaCategoriaService } from 'app/servico/uat-instalacao-fisica-categoria.service';
import { UatInstalacaoFisicaCategoriaAmbienteService } from 'app/servico/uat-instalacao-fisica-ambiente.service';
import { UatInstalacaoFisicaCategoria } from 'app/modelo/uat-instalacai-fisica-categoria';
import { UatInstalacaoFisicaAmbiente } from 'app/modelo/uat-instalacao-fisica-ambiente';
import { UatInstalacaoFisica } from 'app/modelo/uat-instalacao-fisica';
import { UnidadeAtendimentoTrabalhador } from 'app/modelo/unid-atend-trabalhador.model';
import { ModalConfirmarService } from 'app/compartilhado/modal-confirmar/modal-confirmar.service';

@Component({
    selector: 'app-uat-instalacoes-fisicas',
    templateUrl: './uat-instalacoes-fisicas.component.html',
    styleUrls: ['./uat-instalacoes-fisicas.component.scss'],
})
export class UatInstalacoesFisicasComponent extends BaseComponent implements OnInit {

    @Input() idUnidade: Number;
    @Input() modoConsulta: boolean;
    @Input() hasPermissaoCadastrarAlterar: boolean;
    @Input() hasPermissaoDesativar: boolean;

    private form: FormGroup;
    listCategorias: UatInstalacaoFisicaCategoria[] = [];
    listAmbientes: UatInstalacaoFisicaAmbiente[] = [];
    listInstalacoesFisicasSave: UatInstalacaoFisica[] = [];
    instalacoesAgg: any = null;
    objectKeys = Object.keys;

    constructor(
        private formBuilder: FormBuilder,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private uatInstalacaoFisicasService: UatInstalacaoFisicaService,
        private uatInstalacaoFisicasCategoriaService: UatInstalacaoFisicaCategoriaService,
        private uatInstalacaoFisicasAmbienteService: UatInstalacaoFisicaCategoriaAmbienteService,
        private modalConfirmarSerivce: ModalConfirmarService,
    ) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_cadastro_instalacoes_fisicas_title;
    }

    ngOnInit(): void {
        this.initForm();
        this.loadCategorias();
        this.findInstalacoesAgg();
    }

    onChangeCategoria() {
        let categoria = this.form.get('categoria').value;
        if (categoria !== null && categoria !== undefined) {
            this.loadAmbientes(categoria);
        }
    }

    showFormInstalacoesFisicas() {
        let categoria = this.form.get('categoria').value;
        return categoria != null && categoria !== undefined &&
            this.listInstalacoesFisicasSave !== null &&
            this.listInstalacoesFisicasSave !== undefined &&
            this.listInstalacoesFisicasSave.length > 0;
    }

    salvar() {
        this.uatInstalacaoFisicasService.salvar(this.listInstalacoesFisicasSave).subscribe((data) => {
            this.form.reset();
            this.listInstalacoesFisicasSave = [];
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.findInstalacoesAgg();
        },
            (error) => {
                this.mensagemError(error);
            });
    }

    limpar() {
        this.form.reset();
        this.listInstalacoesFisicasSave = [];
        this.findInstalacoesAgg();
    }

    openConfirmationDialog(uatInstalacaoFisica: any) {
        this.modalConfirmarSerivce.confirm('Excluir Instalação Física', 'Tem certeza que deseja excluir esta Instalação Física?')
        .then((confirmed) => {
          if(confirmed) {
            this.desativar(uatInstalacaoFisica.idInstalacaoFisica, this.idUnidade);
          }
        });
    }

    duplicarCampo(index: number) {
        const amb = this.listInstalacoesFisicasSave[index].uatInstalacaoFisicaAmbiente;
        this.listInstalacoesFisicasSave.splice(index + 1, 0,
            new UatInstalacaoFisica(null, null, null, amb,
                new UnidadeAtendimentoTrabalhador(this.idUnidade), true));
    }

    showGridInstalacoes() {
        return this.instalacoesAgg !== null &&
            this.instalacoesAgg !== undefined &&
            this.instalacoesAgg !== {};
    }

    private findInstalacoesAgg() {
        if (this.idUnidade !== null && this.idUnidade !== undefined) {
            this.uatInstalacaoFisicasService.findByUnidadeAgg(this.idUnidade).subscribe(
                (data) => {
                    this.instalacoesAgg = data;
                }, (error) => {
                    this.mensagemError(error);
                },
            );
        }
    }

    private desativar(idInstalacaoFisica: Number, idUnidade: Number) {
        this.uatInstalacaoFisicasService.desativar(idInstalacaoFisica, idUnidade).subscribe(
            (data) => {
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.findInstalacoesAgg();
            }, (error) => {
                this.mensagemError(error);
            });
    }

    private initForm() {
        this.form = this.formBuilder.group({
            categoria: [{ value: null, disabled: this.modoConsulta }, Validators.required],
        });
    }

    private loadCategorias() {
        this.uatInstalacaoFisicasCategoriaService.findAll().subscribe((categorias) => {
            this.listCategorias = categorias;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private loadAmbientes(idCategoria: Number) {
        this.uatInstalacaoFisicasAmbienteService.findByCategoria(idCategoria).subscribe((ambientes) => {
            this.listAmbientes = ambientes;
            this.changeForm();
        },
            (error) => {
                this.mensagemError(error);
            });
    }

    private changeForm() {
        this.listInstalacoesFisicasSave = [];
        for (const amb of this.listAmbientes) {
            this.listInstalacoesFisicasSave.push(
                new UatInstalacaoFisica(null, null, null, amb,
                    new UnidadeAtendimentoTrabalhador(this.idUnidade), false));
        }
    }
}
