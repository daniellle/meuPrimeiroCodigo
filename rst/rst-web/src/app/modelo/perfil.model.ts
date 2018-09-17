export class Perfil {

    id: number;
    nome: String;
    codigo: String;
    hierarquia: number;

    constructor(id?: number, nome?: String, codigo?: String, hierarquia?: number) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.hierarquia = hierarquia;
    }

}
