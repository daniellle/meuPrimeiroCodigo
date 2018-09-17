import {Perfil} from "./perfil.model";

export class SistemaPerfil {

    id: number;
    perfil: Perfil;

    constructor(id?: number, perfil?: Perfil) {
        this.id = id;
        this.perfil = perfil;
    }

}
