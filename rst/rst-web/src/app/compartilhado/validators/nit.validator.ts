import { AbstractControl } from '@angular/forms/src/model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';

export function ValidarNit(control: AbstractControl) {

    let strNIT = control.value;
    let soma = 0;
    let resto = 0;
    let dv = 0;
    let multiplicador = 2;
    const numeros = [];
    let isValid = null;
    let quantidade = 0;

    if (strNIT != null) {
        strNIT = MascaraUtil.removerMascara(strNIT);
        quantidade = strNIT.toString().length;
        if (quantidade === 11) {
            for (let i = quantidade - 1; i > 0; i--) {
                if (multiplicador > 9) {
                    multiplicador = 2;
                }
                numeros[i] = strNIT.substring(i - 1, i) * multiplicador;
                multiplicador++;
            }
            for (let i = numeros.length; i > 0; i--) {
                if (numeros[i - 1] != null) {
                    soma += numeros[i - 1];
                }
            }
            resto = soma % 11;
            dv = 11 - resto;
            dv = dv !== 11 ? dv : 0;

            const ultimo = parseInt(strNIT.substring(quantidade - 1), 10);

            if (ultimo === dv) {
                isValid = true;
            }
        }
    }

    if (!isValid) {
        return { validNIT: true };
    }
    return null;
}
