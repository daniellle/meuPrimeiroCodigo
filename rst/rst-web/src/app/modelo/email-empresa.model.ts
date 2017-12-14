import { Email } from 'app/modelo/email.model';

export class EmailEmpresa {
    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
