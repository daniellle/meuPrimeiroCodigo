import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { MdSelect } from '@angular/material';

import { Sistema, UsuarioPerfilSistema, Usuario, Perfil } from '../../../../modelo/index';
import { SistemaService } from 'app/servico/sistema.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { SistemaPerfil } from 'app/modelo/sistema-perfil.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';

const COD_SISTEMAS_RELACIONADOS = ['dw', 'indigev', 'portal'];
const COD_SISTEMA_CADASTRO = 'cadastro';

@Component({
  selector: 'app-associa-perfil-barramento',
  templateUrl: './associa-perfil-barramento.component.html',
  styleUrls: ['./associa-perfil-barramento.component.scss']
})
export class AssociaPerfilBarramentoComponent extends BaseComponent implements OnInit, OnChanges   {
    @ViewChild('sistemasSelect') sistemasSelect: MdSelect;

    @Input() modoConsulta: boolean;
    @Input() usuario: Usuario;

    @Output() associaPerfilEvent = new EventEmitter<any>();

    perfisSistemas: UsuarioPerfilSistema[];
    sistemaSelecionado: Sistema;
    sistemas: Sistema[] = [];
    perfisDoSistema: SistemaPerfil[] = [];
    temCadastro: boolean;
    associandoEpidemiologia: boolean;

    constructor(private sistemaService: SistemaService,
                protected bloqueioService: BloqueioService,
                protected dialogo: ToastyService) {
        super(bloqueioService, dialogo);

    }

    ngOnInit() {
        if(this.usuario) {
            this.perfisSistemas = [].concat(this.usuario.perfisSistema);
        }
        this.sistemaService.buscarSistemasPermitidos(Seguranca.getUsuario())
            .subscribe(sistemas => setTimeout(() => this.sistemas = sistemas));
    }

    ngOnChanges(changes: SimpleChanges): void {
        if(changes['usuario'])
            if(this.usuario != undefined)
            this.perfisSistemas = this.usuario.perfisSistema;
    }

    changeSistema(sistema: Sistema) {
        if(sistema) {
            if(sistema.codigo == "cadastro"){
                this.perfisDoSistema = sistema.sistemaPerfis.sort((a,b) => {//Organiza baseado no nome dos Perfis se a pesquisa for do sistema Cadastro
                    if(a.perfil.nome < b.perfil.nome){ return -1 };
                    if(a.perfil.nome > b.perfil.nome){ return 1 };
                    return 0;
                });
            } else{
                this.perfisDoSistema = sistema.sistemaPerfis;
            }
            this.sistemaSelecionado = sistema;
        } else {
            this.perfisDoSistema = [];
            this.sistemaSelecionado = undefined;
        }
    }

    selecionaSistema(sistemaNome: string) {
        if(sistemaNome) {
            const sistema = this.sistemas.find(s => s.nome == sistemaNome);
            this.changeSistema(sistema);
            this.sistemasSelect.writeValue(sistema);
            this.sistemasSelect.focus();
        }
    }

    usuarioTemPerfilSistema(codigoPerfil: string): boolean {
        if(this.sistemaSelecionado) {
            const codigoSistema = this.sistemaSelecionado.codigo;
            return this.perfisSistemas.some(ps =>
                ps.sistema.codigo === codigoSistema && ps.perfil.codigo === codigoPerfil);
        }
        return false;
    }

    atualizaPerfilSistema(event: any, sistemaPerfil: any) {
        const sistema = { id: this.sistemaSelecionado.id, nome: this.sistemaSelecionado.nome, codigo:  this.sistemaSelecionado.codigo };
        if(event.checked) {
            this.addUsuarioPerfil(sistemaPerfil.perfil, sistema);
        } else {
            this.removeUsuarioPerfil(sistemaPerfil.perfil, sistema);
        }
    }

    associarPerfil() {
        this.usuario.perfisSistema.forEach(sistemaPerfil => {
            if(sistemaPerfil.sistema.codigo == "cadastro"){
                this.temCadastro = true;
            }
        });
        this.perfisSistemas.forEach(sistemaPerfil => {
            if(sistemaPerfil.sistema.codigo == "epidemiologia"){
                this.associandoEpidemiologia = true;
            }
        });
        if(!this.temCadastro && this.associandoEpidemiologia){
            this.perfisSistemas = [];
            this.usuario.perfisSistema.pop();
            this.temCadastro = undefined;
            this.associandoEpidemiologia = undefined;
            this.mensagemError("É necessário associar ter uma associação com o sistema Cadastro para criar associação com o sistema Epidemiologia");
            return;
        }
        this.usuario.perfisSistema = [].concat(this.perfisSistemas);
        this.changeSistema(undefined);
        this.sistemasSelect.writeValue('');
        this.temCadastro = undefined;
        this.associandoEpidemiologia = undefined;
    }

    isPerfilTrabalhador(perfil: Perfil): boolean {
        return perfil.codigo == PerfilEnum.TRA;
    }

    private addUsuarioPerfil(perfil: Perfil, sistema: Sistema) {
        if(this.ehSistemaCadastroOuRelacionado(sistema)) {
            this.sistemas.filter(s => this.ehSistemaCadastroOuRelacionado(s))
                .forEach(s => {
                    if(this.perfilTahAssociadoAoSistema(s, perfil)) {
                        this.perfisSistemas.push({perfil, sistema: s})
                    }
                });
        } else {
            this.perfisSistemas.push({perfil, sistema});
        }
    }

    private removeUsuarioPerfil(perfil: Perfil, sistema: Sistema) {
        if(this.ehSistemaCadastroOuRelacionado(sistema)) {
            this.sistemas.filter(s => this.ehSistemaCadastroOuRelacionado(s))
                .forEach(s => {
                    if(this.perfilTahAssociadoAoSistema(s, perfil)) {
                        const index = this.perfisSistemas
                            .findIndex(ps => ps.perfil.codigo === perfil.codigo && ps.sistema.codigo === s.codigo);

                        if(index > -1) this.perfisSistemas.splice(index, 1);
                    }
                });
        } else {
            const index = this.perfisSistemas.findIndex(ps => ps.perfil.codigo === perfil.codigo && ps.sistema.codigo === sistema.codigo);
            this.perfisSistemas.splice(index, 1);
        }
    }

    private ehSistemaCadastroOuRelacionado(sistema: Sistema): boolean {
        return sistema.codigo === COD_SISTEMA_CADASTRO || COD_SISTEMAS_RELACIONADOS.includes(sistema.codigo);
    }

    private perfilTahAssociadoAoSistema(sistema: Sistema, perfil: Perfil): boolean {
        return sistema.sistemaPerfis.some(sistemaPerfil => sistemaPerfil.perfil.codigo === perfil.codigo);
    }

}
