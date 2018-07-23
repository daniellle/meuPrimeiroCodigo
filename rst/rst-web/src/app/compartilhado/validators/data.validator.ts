import { AbstractControl } from '@angular/forms/src/model';

export function ValidateData(control: AbstractControl) {

    const strData = control.value ? control.value.jsdate : null;
    if (!ValidarData(strData)) {
        return { validData: true };
    }
    return null;
}

export function ValidarData(strData: any) {
    if (strData) {
        return Date.parse(strData);
    }
    return false;
}

export function ValidarDataFutura(strData: any) {
    if (strData) {
        return new Date().getTime() < new Date(strData).getTime();
    }
    return false;
}

export function ValidarDataPassadaOuCorrente(strData: any) {
    if (strData) {
        return new Date().getTime() >= new Date(strData).getTime();
    }
    return false;
}

export function CompareDataBefore(date1: string, date2: string) {
    const data1 = Date.parse(date1);
    const data2 = Date.parse(date2);
    return data1 < data2;
}

export function ValidateDataFutura(control: AbstractControl) {
    const strData = control.value ? control.value.jsdate : null;
    if (ValidarDataFutura(strData)) {
        return { validDataFutura: true };
    }
    return null;
}

export function ValidateDataPassadaOuCorrente(control: AbstractControl) {
    const strData = control.value ? control.value.jsdate : null;
    if (ValidarDataPassadaOuCorrente(strData)) {
        return { validDataFutura: true };
    }
    return null;
}