import { AbstractControl } from '@angular/forms';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';

export function ValidateCPF(control: AbstractControl) {

  let strCPF = control.value;
  let isValid = null;

  function getFirstDigit(v) {
    const matriz = [10, 9, 8, 7, 6, 5, 4, 3, 2];
    let total = 0;
    let  verifc;
    for (let i = 0; i < 9; i++) {
      total += v[i] * matriz[i];
    }
    verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
    return verifc;
  }

  function getSecondDigit(v) {
    const matriz = [11, 10, 9, 8, 7, 6, 5, 4, 3, 2];
    let total = 0;
    let verifc;
    for (let i = 0; i < 10; i++) {
      total += v[i] * matriz[i];
    }
    verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
    return verifc;
  }

  function numerosIguais(cpf: String) {
    for (let i = 0; i < 10; i++) {
      if (cpf[i] !== cpf[i + 1]) {
        return false;
      }
    }
    return true;
  }

  if (strCPF != null) {
    strCPF = MascaraUtil.removerMascara(strCPF);

    if (strCPF.toString().length === 11 && !numerosIguais(strCPF)) {
      const dig1 = getFirstDigit(strCPF.substr(0, 9));
      const dig2 = getSecondDigit(strCPF.substr(0, 10));
      const final = strCPF.substr(9, 2);
      const val = '' + dig1 + dig2;
      if (final === val) {
        isValid = true;
      }
    }
  }

  if (!isValid) {
    return { validCPF: true };
  }
  return null;
}
