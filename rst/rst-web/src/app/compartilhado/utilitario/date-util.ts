import * as moment from 'moment';

export class DateUtil {

    public static isBetween(dataInicio: any, dataFim: any): boolean {
        let d1 = dataInicio.split("/");
        let d2 = dataFim.split("/");
        let dataInicioFormat = new Date(d1[2], parseInt(d1[1])-1, d1[0]);
        let dataFimFormat = new Date(d2[2], parseInt(d2[1])-1, d2[0]);
        const dataAtual = new Date();
        return (dataAtual > dataInicioFormat && dataAtual < dataFimFormat);
    }

}
