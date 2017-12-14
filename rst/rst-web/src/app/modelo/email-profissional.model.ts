import { Email } from 'app/modelo/email.model';

export class EmailProfissional {
    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
