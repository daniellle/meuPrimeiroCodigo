export class ProximaDoseVacina{
    id: number;
    dtProximaDoseVacina: Date;
    remover: boolean;

    constructor(init?: Partial<ProximaDoseVacina>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}