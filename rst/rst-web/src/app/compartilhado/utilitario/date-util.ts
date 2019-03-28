import * as moment from 'moment';

export class DateUtil {

    public static isBetween(dataInicio: Date, dataFim: Date): boolean {
        const dataAtual = moment(new Date());
        const momentInicio = moment(new Date(dataInicio));
        const momentFim = moment(new Date(dataFim));
        return dataAtual.isBetween(momentInicio, momentFim);
    }

}
