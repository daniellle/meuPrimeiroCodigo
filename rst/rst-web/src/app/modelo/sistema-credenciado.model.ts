export class SistemaCredenciado {

    constructor(
        public id?: number,
        public clientId?: string,
        public clientSecret?: string,
        public cnpj?: string,
        public nomeResponsavel?: string,
        public emailResponsavel?: string,
        public telefoneResponsavel?: string,
        public entidade?: string,
        public sistema?: string,
        public dataCriacao?: Date,
        public dataDesativacao?: Date,
        public dataAtualizacao?: Date,
    ) { }
}
