import {Component, Input} from '@angular/core';
import {Credencial} from '../../modelo/credencial.model';
import {Router} from '@angular/router';
import {AutenticacaoService} from '../../servico/autenticacao.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MensagemProperties} from '../../compartilhado/utilitario/recurso.pipe';
import {BaseComponent} from '../base.component';
import {BloqueioService} from '../../servico/bloqueio.service';
import {ToastyService} from 'ng2-toasty';

@Component({
  selector: 'app-autenticacao',
  templateUrl: './autenticacao.component.html',
  styleUrls: ['./autenticacao.component.css'],
})
export class AutenticacaoComponent extends BaseComponent {

  @Input() credencial: Credencial = new Credencial();

  formulario: FormGroup;

  constructor(private router: Router, private service: AutenticacaoService, formulario: FormBuilder,
              protected bloqueio: BloqueioService, protected dialogo: ToastyService) {
    super(bloqueio, dialogo);
    this.formulario = formulario.group({
      usuario: new FormControl(null, [Validators.required]),
      senha: new FormControl(null, Validators.required),
    });
  }

  autenticar() {

    this.service.autenticar().subscribe((autenticado: boolean) => {
      if (autenticado) {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }
    }, () => {
      this.mensagemError(MensagemProperties.app_rst_usuario_credencias_invalida);
    }, () => this.router.navigate(['/']));

  }

}
