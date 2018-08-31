import { Email } from 'app/modelo/email.model';

export class EmailRedeCredenciada {
    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
