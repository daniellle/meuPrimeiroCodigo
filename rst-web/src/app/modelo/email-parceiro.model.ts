import { Email } from 'app/modelo/email.model';

export class EmailParceiro {
    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
