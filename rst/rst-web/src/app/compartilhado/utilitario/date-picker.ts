import { IMyDpOptions } from 'mydatepicker';
import * as moment from 'moment';

const constDatePickerOptions: IMyDpOptions = {
    openSelectorOnInputClick: false,
    indicateInvalidDate: false,
    editableDateField: false,
    dateFormat: 'dd/mm/yyyy',
    showClearDateBtn: true,
    inline: false,
};

export class DatePicker {

    static datePickerOptions = constDatePickerOptions;

    static convertDateForMyDatePicker(data: string): any {
        if (data) {
            const dataFormatada: Date = new Date(moment(data, 'DD/MM/YYYY').format('YYYY-MM-DD'));
            const modelData = {
                jsdate: dataFormatada,
                date: {
                    year: dataFormatada.getUTCFullYear(),
                    month: dataFormatada.getUTCMonth() + 1,
                    day: dataFormatada.getUTCDate(),
                },
            };
            return modelData;
        }
    }
}
