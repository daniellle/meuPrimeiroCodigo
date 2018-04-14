import { Email } from './email.model';
export class EmailSindicato {

    id: number;
    email: Email;

    constructor() {
        this.email = new Email();
    }
}
