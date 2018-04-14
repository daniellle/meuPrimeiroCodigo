import { Linha } from './linha.model';

export class PesquisaSesiProdutoServico {
    idUnidade: number;
    mapsProdutoServico: Array<Map<Linha, string[]>>;
}
