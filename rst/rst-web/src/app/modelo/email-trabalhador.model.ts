import { Email } from 'app/modelo/email.model';

export class EmailTrabalhador {
    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
