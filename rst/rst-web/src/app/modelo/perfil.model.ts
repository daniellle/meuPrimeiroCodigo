export class Perfil {

    id: number;
    nome: string;
    codigo: string;
    hierarquia: number;

    constructor(id?: number, nome?: string, codigo?: string, hierarquia?: number) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.hierarquia = hierarquia;
    }

}
