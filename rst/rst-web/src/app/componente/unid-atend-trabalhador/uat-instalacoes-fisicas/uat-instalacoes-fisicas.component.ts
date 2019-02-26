import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from "app/componente/base.component";
import { OnInit, Component, Input } from "@angular/core";
import { FormGroup, FormArray, FormBuilder } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastyService } from 'ng2-toasty';

@Component({
    selector: 'app-uat-instalacoes-fisicas',
    templateUrl: './uat-instalacoes-fisicas.component.html',
    styleUrls: ['./uat-instalacoes-fisicas.component.scss'],
})
export class UatInstalacoesFisicasComponent extends BaseComponent implements OnInit {

    @Input()
    private idUnidade: Number;

    private form: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService) {
        super(bloqueioService, dialogo);
    }

    ngOnInit(): void {
        this.initForm();
    }

    private initForm() {
        this.form = this.formBuilder.group({
            categoria: null,
        });
    }

}
