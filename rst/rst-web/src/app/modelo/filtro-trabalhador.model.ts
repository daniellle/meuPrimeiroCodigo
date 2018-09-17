export class FiltroTrabalhador {
    id: string;
    cpf: string;
    nome: string;
    nit: string;
    situacao = '';
    falecidos = true;
    aplicarDadosFilter = true;
    idEstado: '';
    fromMinhaConta: boolean;
}
