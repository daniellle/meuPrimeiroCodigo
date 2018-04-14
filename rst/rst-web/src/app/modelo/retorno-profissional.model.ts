import { Profissional } from './profissional.model';
export class RetornoProfissional {
    quantidade: number;
    list: Profissional[];

    constructor( quantidade: number, list: Profissional[]) {
    this.list = list;
    this.quantidade = quantidade;
    }
}
