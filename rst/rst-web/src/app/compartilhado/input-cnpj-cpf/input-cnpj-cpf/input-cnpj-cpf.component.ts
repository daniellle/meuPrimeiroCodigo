import { Component, OnInit, Input, Output } from '@angular/core';
import { MascaraUtil } from '../../utilitario/mascara.util';

@Component({
  selector: 'app-input-cnpj-cpf',
  templateUrl: './input-cnpj-cpf.component.html',
  styleUrls: ['./input-cnpj-cpf.component.scss'],
})
export class InputCnpjCpfComponent implements OnInit {

  @Input('valor') @Output('valor') public valor: string;

  @Input('id') @Output('id')  public id: string;

  @Input('classe') @Output('classe')  public classe: string;

  @Input('nome') @Output('nome')  public nome: string;

  @Input('placeholder') @Output('placeholder')  public placeholder: string;

  constructor() { }

  ngOnInit() {
  }

  mudarMascara(event: any) {
            if (this.valor.length <= 14) {
                this.valor = MascaraUtil.removerMascara(this.valor);
                this.valor = this.valor.replace(/(\d{3})(\d)/, '$1.$2');
                this.valor = this.valor.replace(/(\d{3})(\d)/, '$1.$2');
                this.valor = this.valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            } else {
                this.valor = MascaraUtil.removerMascara(this.valor);
                this.valor = this.valor.replace(/^(\d{2})(\d)/, '$1.$2');
                this.valor = this.valor.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
                this.valor = this.valor.replace(/\.(\d{3})(\d)/, '.$1/$2');
                this.valor = this.valor.replace(/(\d{4})(\d)/, '$1-$2');
            }

        }

}
