import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { Component, OnInit } from '@angular/core';
import { environment } from './../../../../environments/environment';
import { Router, ActivatedRoute } from '@angular/router';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-questionario-intermediario',
  templateUrl: './questionario-intermediario.component.html',
  styleUrls: ['./questionario-intermediario.component.scss']
})
export class QuestionarioIntermediarioComponent implements OnInit {

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit() {
  
  }

  mudarPagina(tipo) {
    if (tipo === 'pesquisar') {
      this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${tipo}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/${tipo}`]);
    }
  }

  hasPermissaoResposta() {
    return Seguranca.isPermitido([PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_CADASTRAR,
      PermissoesEnum.RESPOSTA_ALTERAR,
      PermissoesEnum.RESPOSTA_CONSULTAR,
      PermissoesEnum.RESPOSTA_DESATIVAR]);
  }

  hasPermissaoQuestionario() {
    return Seguranca.isPermitido([PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR]);
  }

  hasPermissaoGrupoPergunta() {
    return Seguranca.isPermitido([PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
      PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
      PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
      PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR]);
  }

  hasPermissaoIndicador() {
    return Seguranca.isPermitido([PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR]);
  }

  hasPermissaoPergunta() {
    return Seguranca.isPermitido([PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_CADASTRAR,
      PermissoesEnum.PERGUNTA_ALTERAR,
      PermissoesEnum.PERGUNTA_CONSULTAR,
      PermissoesEnum.PERGUNTA_DESATIVAR]);
  }

  hasPermissaoClassificacao() {
    return Seguranca.isPermitido([PermissoesEnum.CLASSIFICACAO_PONTUACAO,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR]);
  }

}
