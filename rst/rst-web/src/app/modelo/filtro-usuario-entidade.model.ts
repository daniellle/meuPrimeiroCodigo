export class FiltroUsuarioEntidade {
    cpf: string;
    razaoSocial: string;
    cnpj: string;
    idEstado: string;
    nomeFantasia: string;
    perfil: string;

    constructor(init?: Partial<FiltroUsuarioEntidade>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
