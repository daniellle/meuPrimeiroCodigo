import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import { ValidateCPF } from 'app/compartilhado/validators/cpf.validator';
import { Usuario } from 'app/modelo/usuario.model';

@Component({
  selector: 'app-dados-gerais',
  templateUrl: './dados-gerais.component.html'
})
export class DadosGeraisComponent implements OnInit, OnChanges {

  usuarioForm: FormGroup;

  @Input() modoAlterar: boolean;
  @Input() modoConsulta: boolean;
  @Input() usuario: Usuario;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.criarForm();
    if(this.usuario) {
      this.preencheForm();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['usuario']) {
      this.criarForm();
    if(this.usuario){
        this.preencheForm();
      }
    }
  }

  private preencheForm() {
    this.usuarioForm.controls['nome'].setValue(this.usuario.nome);
    this.usuarioForm.controls['login'].setValue(this.usuario.login);
    this.usuarioForm.controls['email'].setValue(this.usuario.email);
  }

  private criarForm(): void {
    this.usuarioForm = this.formBuilder.group({
      nome: [
          { value: '', disabled: this.modoConsulta },
          [ Validators.required, Validators.maxLength(160)]
      ],
      login: [
          { value: '', disabled: this.modoAlterar || this.modoConsulta },
          [ Validators.required, ValidateCPF]
      ],
      email: [
          { value: '', disabled: this.modoConsulta },
          [ Validators.required, Validators.maxLength(255), Validators.email ]
      ]
    });
  }

  getFormValue() {
    return this.usuarioForm.getRawValue();
  }

}
