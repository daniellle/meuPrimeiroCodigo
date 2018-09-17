import {SistemaPerfil} from "./sistema-perfil.model";

export class Sistema {

    id: number;
    nome: String;
    codigo: String;
    sistemaPerfis: SistemaPerfil[];

    constructor(id?: number, nome?: String, codigo?: String, sistemaPerfis?: SistemaPerfil[]) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.sistemaPerfis = sistemaPerfis;
    }
}
