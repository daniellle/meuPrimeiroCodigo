
import { AbstractControl } from '@angular/forms/src/model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';

export function ValidateCNPJ(control: AbstractControl) {

    let strCNPJ = control.value;
    let isValid = null;

    if (strCNPJ != null) {
        strCNPJ = MascaraUtil.removerMascara(strCNPJ);
        if ((strCNPJ.toString().length === 14 || strCNPJ.toString().length === 11) && !numerosIguais(strCNPJ)) {
            const dig1 = getFirstDigit(strCNPJ.substr(0, 12));
            const dig2 = getSecondDigit(strCNPJ.substr(0, 13));
            const final = strCNPJ.substr(12, 2);
            const val = '' + dig1 + dig2;
            if (final === val) {
                isValid = true;
            }
        }
    }

    if (!isValid) {
        return { validCNPJ: true };
    }
    return null;

    function getFirstDigit(v) {
        const matriz = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
        let total = 0;
        let verifc;
        for (let i = 0; i < 12; i++) {
            total += v[i] * matriz[i];
        }
        verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
        return verifc;
    }

    function getSecondDigit(v) {
        const matriz = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
        let total = 0;
        let  verifc;
        for (let i = 0; i < 13; i++) {
            total += v[i] * matriz[i];
        }
        verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
        return verifc;
    }

    function numerosIguais(cpf: string) {
        for (let i = 0; i < 10; i++) {
            if (cpf[i] !== cpf[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
