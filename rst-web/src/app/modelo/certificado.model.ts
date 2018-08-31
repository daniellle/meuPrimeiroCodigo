
import { TipoCurso } from './tipo-curso.model';
export class Certificado {

    descricao: string;
    modalidade: string;
    tipoCurso = new TipoCurso();
    trabalhador = {};
    cargaHoraria: string;
    dataValidade: any;
    dataConclusao: any;
    arquivo: ByteString;
    tipoArquivo: string;
    nomeArquivo: string;
    inclusaoTrabalhador: string;
}
