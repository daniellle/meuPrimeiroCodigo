
import { AbstractControl } from '@angular/forms/src/model';

export function ValidateEmail(control: AbstractControl) {

    if (!ValidarEmail(control.value)) {
        return { validEmail: true };
    }
    return null;
}

export function ValidarEmail(strEmail: any): boolean {
    // tslint:disable-next-line:max-line-length
    if (strEmail) {
        return new RegExp(/^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-zàáâãéêíóôõúüç0-9]+[A-Za-zàáâãéêíóôõúüç0-9-]*([A-Za-zàáâãéêíóôõúüç0-9])(\.[A-Za-z]+)*(\.[A-Za-z]{2,})$/).test(strEmail);
    }
}
