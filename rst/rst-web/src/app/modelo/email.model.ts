export class Email {

        id: number;
        descricao: string;
        tipo: string;
        notificacao: boolean;
        dataCriacao: Date;
        dataAlteracao: Date;
        dataExclusao: Date;

        constructor(init?: Partial<Email>) {
            if (init) {
                Object.assign(this, init);
            }
        }
}
