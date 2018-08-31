import { LinhaDTO } from './linha-dto.model';
import { TelefoneUnidAtendTrabalhador } from 'app/modelo/telefone-unid-atend-trabalhador.model';
import { EnderecoUnidAtendTrabalhador } from 'app/modelo/endereco-unid-atend-trabalhador.model';

export class PesquisaSesiDTO {
    idUat: number;
    razaoSocialUat: string;
    endereco: EnderecoUnidAtendTrabalhador[];
    telefone: TelefoneUnidAtendTrabalhador[];
    linhas: LinhaDTO[];

    constructor() {

    }
}
