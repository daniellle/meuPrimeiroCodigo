import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Parceiro } from './../../../modelo/parceiro.model';
import { ParceiroService } from './../../../servico/parceiro.service';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { FiltroParceiro } from './../../../modelo/filtro-parceiro.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
import { FormBuilder } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../base.component';
import { Router, ActivatedRoute } from '@angular/router';
import { BloqueioService } from '../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { Especialidade } from 'app/modelo/especialidade.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
    selector: 'app-pesquisar-parceiro-credenciado',
    templateUrl: './pesquisar-parceiro-credenciado.component.html',
    styleUrls: ['./pesquisar-parceiro-credenciado.component.scss'],
})
export class PesquisarParceiroCredenciadoComponent extends BaseComponent implements OnInit {

    situacoes = Situacao;
    keysSituacao: string[];
    especialidades: Especialidade[];
    filtro: FiltroParceiro;
    parceiros: Parceiro[];
    inputCnpjCpf: string;

    constructor(
        private router: Router, private fb: FormBuilder,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService, private service: ParceiroService,
        protected dialogo: ToastyService) {
        super(bloqueioService, dialogo);
    }

    private getEspecialidades() {
        this.service.getEspecialidades().subscribe((retorno: Especialidade[]) => {
            this.especialidades = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    ngOnInit() {
        this.keysSituacao = Object.keys(this.situacoes);
        this.filtro = new FiltroParceiro();
        this.especialidades = new Array<Especialidade>();
        this.parceiros = new Array<Parceiro>();
        this.getEspecialidades();
        this.isConsultar();
    }

    isConsultar() {
        this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.PARCEIRO_CREDENCIADA,
        PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR,
        PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
        PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR]);
    }

    pesquisar() {
        this.parceiros = new Array<Parceiro>();
        if (this.validarCampos()) {
            this.paginacao.pagina = 1;
            this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Parceiro>) => {
                this.parceiros = retorno.list;
                this.paginacao = this.getPaginacao(this.paginacao, retorno);
                if (retorno.quantidade === 0) {
                    this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
                }
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    mudarMascara(event: any) {
        if (this.inputCnpjCpf) {
            if (this.inputCnpjCpf.length <= 14) {
                this.inputCnpjCpf = MascaraUtil.removerMascara(this.inputCnpjCpf);
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/\W/g, '');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/(\d{3})(\d)/, '$1.$2');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/(\d{3})(\d)/, '$1.$2');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            } else {
                this.inputCnpjCpf = MascaraUtil.removerMascara(this.inputCnpjCpf);
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/\W/g, '');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/^(\d{2})(\d)/, '$1.$2');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/\.(\d{3})(\d)/, '.$1/$2');
                this.inputCnpjCpf = this.inputCnpjCpf.replace(/(\d{4})(\d)/, '$1-$2');
            }
        }
    }

    validarCampos(): Boolean {
        let isValido: Boolean = true;

        if (!this.inputCnpjCpf
            && !this.filtro.razaoSocialNome
            && !this.filtro.especialidade) {
            this.mensagemError(MensagemProperties.app_rst_msg_pesquisar_todos_vazios);
            isValido = false;
        } else {
            if (this.inputCnpjCpf) {
                if (this.inputCnpjCpf.length <= 14) {
                    if (this.inputCnpjCpf.length < 14) {
                        this.mensagemError(MensagemProperties.app_rst_cpf_invalido);
                        isValido = false;
                    }
                } else {
                    if (this.inputCnpjCpf.length < 18) {
                        this.mensagemError(MensagemProperties.app_rst_cnpj_invalido);
                        isValido = false;
                    }
                }
            }
            this.filtro.cpfCnpj = MascaraUtil.removerMascara(this.inputCnpjCpf);
        }
        return isValido;
    }

    pageChanged(event: any): void {
        this.paginacao.pagina = event.page;
        this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Parceiro>) => {
            this.parceiros = retorno.list;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    selecionar(model: any) {
        if (model && model.id) {
            this.router.navigate([model.id], { relativeTo: this.activatedRoute },
            );
        }
    }

    incluir() {
        this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
    }

    formatarCpfCnpj(item: Parceiro) {
        return this.getCnpjCpfFormatado(item.numeroCnpjCpf);
    }

    orderByNome(list: any) {
        list.sort((left, right): number => {
            if (left.nome > right.nome) {
                return 1;
            }
            if (left.nome < right.nome) {
                return -1;
            }
            return 0;
        });
    }

    hasPermissaoCadastro() {
        return Seguranca.isPermitido([PermissoesEnum.PARCEIRO_CREDENCIADA,
        PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR]);
    }
}
