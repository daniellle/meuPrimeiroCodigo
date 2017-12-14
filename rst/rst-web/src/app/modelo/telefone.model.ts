import { TipoTelefone } from 'app/modelo/enum/enum-tipo-telefone.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';

export class Telefone {
    id: number;
    tipo: TipoTelefone;
    contato: SimNao;
    numero: string;
    dtDesativacao: Date;

    constructor(init?: Partial<Telefone>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
