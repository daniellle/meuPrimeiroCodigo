import {SistemaPerfil} from "./sistema-perfil.model";

export class Sistema {

    id: number;
    nome: string;
    codigo: string;
    sistemaPerfis?: SistemaPerfil[];

    constructor(id?: number, nome?: string, codigo?: string, sistemaPerfis?: SistemaPerfil[]) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.sistemaPerfis = sistemaPerfis;
    }
}
