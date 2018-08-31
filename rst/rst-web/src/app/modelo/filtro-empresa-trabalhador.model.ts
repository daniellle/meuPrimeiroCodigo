export class FiltroEmpresaTrabalhador {
    cpf: string;
    nome: string;

    constructor(init?: Partial<FiltroEmpresaTrabalhador>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
