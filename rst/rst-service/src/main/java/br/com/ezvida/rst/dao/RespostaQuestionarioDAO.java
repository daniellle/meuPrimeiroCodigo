package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.RespostaQuestionario;
import fw.core.jpa.DAOUtil;

public class RespostaQuestionarioDAO extends BaseRstDAO<RespostaQuestionario, Long> {

	@Inject
	public RespostaQuestionarioDAO(EntityManager em) {
		super(em, RespostaQuestionario.class);
	}

	public RespostaQuestionario pesquisarCompletoPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where respostaQuestionario.id = :id ");
		TypedQuery<RespostaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	public List<RespostaQuestionario> pesquisarPorPerguntaQuestionario(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select respostaQuestionario from RespostaQuestionario respostaQuestionario ");
		jpql.append(" left join fetch respostaQuestionario.resposta ");
		jpql.append(" left join fetch respostaQuestionario.perguntaQuestionario perguntaQuestionario ");
		jpql.append(" where perguntaQuestionario.id = :id ");
		jpql.append(" and respostaQuestionario.dataExclusao is null ");
		jpql.append(" order by respostaQuestionario.ordemResposta ");
		TypedQuery<RespostaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select respostaQuestionario from RespostaQuestionario respostaQuestionario ");
		jpql.append(" left join fetch respostaQuestionario.perguntaQuestionario perguntaQuestionario ");
		jpql.append(" left join fetch perguntaQuestionario.indicadorQuestionario indicadorQuestionario ");
	}
	
	public Integer calcularPontuacao(Long id) {
		StringBuilder jpql = new StringBuilder();		
		jpql.append(" select max(s.pontuacao) from ( ");
		jpql.append(" select distinct rq.id_pergunta_quest_fk, rq.pontuacao, pq.id_indicador_quest_fk from resposta_quest rq ");
		jpql.append(" left join pergunta_quest pq on (rq.id_pergunta_quest_fk = pq.id_pergunta_quest)) s ");
		jpql.append(" where s.id_pergunta_quest_fk = :id ");
		jpql.append(" group by s.id_pergunta_quest_fk ");
		Query query = criarConsultaNativa(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}

}
