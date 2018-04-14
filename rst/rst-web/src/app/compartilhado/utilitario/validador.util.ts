import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';

export class ValidadorUtil {

    private static numerosIguais(cpf: String): boolean {
        for (let i = 0; i < 10; i++) {
            if (cpf[i] !== cpf[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static getFirstDigit(v): number {
        const matriz = [10, 9, 8, 7, 6, 5, 4, 3, 2];
        let total = 0;
        let    verifc;
        for (let i = 0; i < 9; i++) {
            total += v[i] * matriz[i];
        }
        verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
        return verifc;
    }

    private static getSecondDigit(v): number {
        const matriz = [11, 10, 9, 8, 7, 6, 5, 4, 3, 2];
        let total = 0;
        let    verifc;
        for (let i = 0; i < 10; i++) {
            total += v[i] * matriz[i];
        }
        verifc = ((total % 11) < 2) ? 0 : (11 - (total % 11));
        return verifc;
    }

    public static validarCPF(strCPF: string): Boolean {

        if (strCPF != null) {
            strCPF = MascaraUtil.removerMascara(strCPF);

            if (strCPF.toString().length === 11 && !this.numerosIguais(strCPF)) {
                const dig1 = this.getFirstDigit(strCPF.substr(0, 9));
                const dig2 = this.getSecondDigit(strCPF.substr(0, 10));
                const final = strCPF.substr(9, 2);
                const val = '' + dig1 + dig2;
                if (final === val) {
                    return true;
                }
            }
        }
        return false;
    }
}
