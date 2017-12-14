import { environment } from './../../../../environments/environment';
import { RespostaQuestionarioTrabalhadorService } from './../../../servico/resposta-questionario-trabalhador.service';
import { ResultadoQuestionarioDTO } from './../../../modelo/resultado-questionario-dto.model';
import { BaseComponent } from 'app/componente/base.component';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-resultado-questionario',
  templateUrl: './resultado-questionario.component.html',
  styleUrls: ['./resultado-questionario.component.scss']
})
export class ResultadoQuestionarioComponent extends BaseComponent implements OnInit {

  @ViewChild('modalResultado') modalResultado: NgbModal;

  public resultadoQuestionario: ResultadoQuestionarioDTO;
  public idQuestionarioTrabalhador: number;
  public idTrabalhador: number;
  public orientacaoModal: string;
  public meusdados: boolean;
  public rastroIgev: boolean;

  constructor(
    private router: Router, private formBuilder: FormBuilder, private route: ActivatedRoute,
    protected bloqueioService: BloqueioService, private service: RespostaQuestionarioTrabalhadorService,
    protected dialogo: ToastyService, private dialogService: DialogService,
    private activatedRoute: ActivatedRoute, private modalService: NgbModal,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.resultadoQuestionario = new ResultadoQuestionarioDTO();
    this.meusdados = this.activatedRoute.snapshot.url[0].toString() === 'meusdados';
    this.rastroIgev = this.activatedRoute.snapshot.url[0].toString() === 'igev';
    this.idQuestionarioTrabalhador = this.activatedRoute.snapshot.params['idQuest'];
    this.idTrabalhador = this.activatedRoute.snapshot.params['id'];
    this.buscarResultadoQuestionario();
  }

  buscarResultadoQuestionario() {
    this.service.buscarPorId(this.idQuestionarioTrabalhador).subscribe((retorno: ResultadoQuestionarioDTO) => {
      this.resultadoQuestionario = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  voltar() {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/historico`]);
    } else if (this.rastroIgev) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.idTrabalhador}/historico`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/historico`]);
    }
  }

  openModalResultado(orientacao: any) {
    this.orientacaoModal = orientacao;
    this.modalService.open(this.modalResultado, { size: 'lg' }).result
      .then((result) => {
        this.orientacaoModal = orientacao;
      }, (reason) => {
      });
  }

  adjustHeight(el){
    el.style.height = (el.scrollHeight > el.clientHeight) ? (el.scrollHeight)+"px" : "60px";
}

}
