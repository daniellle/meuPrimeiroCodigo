import { AbstractControl } from '@angular/forms/src/model';

export function ValidateSenha(control: AbstractControl) {

    if (!ValidarSenha(control.value)) {
        return { validSenha: true };
    }
    return null;
}

export function ValidarSenha(strSenha: any): boolean {
    if (strSenha) {
        return new RegExp('(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$').test(strSenha);
    }
}
