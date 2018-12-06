import { FormGroup, ValidatorFn } from '@angular/forms';

import * as moment from 'moment';

export const datasContratoValidator: ValidatorFn = (formGroup: FormGroup) => {
  const dataDeInicio = formGroup.controls['dataContratoInicio'].value;
  const dataDeTermino = formGroup.controls['dataContratoFim'].value;

  if(!dataDeInicio || !dataDeTermino)
    return null;

  if(!dataDeInicio.formatted || !dataDeTermino.formatted)
    return null;

  const dataDeInicioMoment = moment(dataDeInicio.formatted, 'DD/MM/YYYY');
  const dataDeTerminoMoment = moment(dataDeTermino.formatted, 'DD/MM/YYYY');

  return !dataDeInicioMoment.isSameOrAfter(dataDeTerminoMoment)  
    ? null 
    : { intervaloDeDatas: true };
};
