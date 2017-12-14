package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.RespostaQuestionarioTrabalhador;
import br.com.ezvida.rst.model.dto.IndicadorDTO;
import br.com.ezvida.rst.model.dto.ResultadoQuestionarioDTO;

public class RespostaQuestionarioTrabalhadorDAO extends BaseRstDAO<RespostaQuestionarioTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DependenteDAO.class);

	@Inject
	public RespostaQuestionarioTrabalhadorDAO(EntityManager em) {
		super(em, RespostaQuestionarioTrabalhador.class);
	}

	public Set<RespostaQuestionarioTrabalhador> pesquisarPorQuestionarioTrabalhador(Long idQuestionarioTrabalhador) {
		LOGGER.debug("Pesquisando Dependente por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where questionarioTrabalhador.id = :idQuestionarioTrabalhador ");
		TypedQuery<RespostaQuestionarioTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idQuestionarioTrabalhador", idQuestionarioTrabalhador);

		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append(
				"select respostaQuestionarioTrabalhador from RespostaQuestionarioTrabalhador respostaQuestionarioTrabalhador ");
		jpql.append(
				" left join fetch respostaQuestionarioTrabalhador.questionarioTrabalhador questionarioTrabalhador ");
		jpql.append(" left join fetch respostaQuestionarioTrabalhador.respostaQuestionario respostaQuestionario ");
		jpql.append(" left join fetch respostaQuestionario.perguntaQuestionario perguntaQuestionario ");
		jpql.append(" left join fetch perguntaQuestionario.indicadorQuestionario indicadorQuestionario ");
		jpql.append(" left join fetch perguntaQuestionario.questionario questionario ");
	}

	@SuppressWarnings("unchecked")
	public ResultadoQuestionarioDTO getResultadoQuestionario(Long idQuestionarioTrabalhador) {

		ResultadoQuestionarioDTO resultadoQuestionarioDTO = new ResultadoQuestionarioDTO();
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select indicador_quest.ds_indicador_quest,indicador_quest.ds_orientacao, ");
		jpql.append(" max(resposta_quest.pontuacao) from indicador_quest indicador_quest ");
		jpql.append(" left join pergunta_quest pergunta_quest ");
		jpql.append(" on (indicador_quest.id_indicador_quest=pergunta_quest.id_indicador_quest_fk) ");
		jpql.append(" left join resposta_quest resposta_quest ");
		jpql.append(" on (resposta_quest.id_pergunta_quest_fk=pergunta_quest.id_pergunta_quest) ");
		jpql.append(" left join resp_quest_trab resp_quest_trab");
		jpql.append(" on (resp_quest_trab.id_resposta_quest_fk=resposta_quest.id_resposta_quest) ");
		jpql.append(" where resp_quest_trab.id_quest_trab_fk = :idQuestionarioTrabalhador ");
		jpql.append(" group by indicador_quest.ds_indicador_quest,indicador_quest.ds_orientacao ");

		Query query = getEm().createNativeQuery(jpql.toString());
		query.setParameter("idQuestionarioTrabalhador", idQuestionarioTrabalhador);
		List<Object[]> listaIndicadores = query.getResultList();

		IndicadorDTO indicadorDTO;
		List<IndicadorDTO> listaIndicadorDTO = new ArrayList<IndicadorDTO>();
		for (Object[] objects : listaIndicadores) {
			boolean aprovado = true;
			if ((Integer) objects[2] > 0) {
				aprovado = false;
			}
			indicadorDTO = new IndicadorDTO((String) objects[0], (String) objects[1], aprovado);
			listaIndicadorDTO.add(indicadorDTO);
		}
		resultadoQuestionarioDTO.setListaIndicadores(listaIndicadorDTO);

		return resultadoQuestionarioDTO;

	}

	@SuppressWarnings("unchecked")
	public List<IndicadorDTO> getListaIndicadorePorIdPerguntaQuestionario(List<Long> idsRespostasQuestionario) {

		StringBuilder jpql = new StringBuilder();
		jpql.append(" select indicador_quest.ds_indicador_quest,indicador_quest.ds_orientacao, ");
		jpql.append(" max(resposta_quest.pontuacao) from indicador_quest indicador_quest ");
		jpql.append(" left join pergunta_quest pergunta_quest ");
		jpql.append(" on (indicador_quest.id_indicador_quest=pergunta_quest.id_indicador_quest_fk) ");
		jpql.append(" left join resposta_quest resposta_quest ");
		jpql.append(" on (resposta_quest.id_pergunta_quest_fk=pergunta_quest.id_pergunta_quest) ");
		jpql.append(" where resposta_quest.id_resposta_quest in (:idsRespostasQuestionario) ");
		jpql.append(" group by indicador_quest.ds_indicador_quest,indicador_quest.ds_orientacao ");

		Query query = getEm().createNativeQuery(jpql.toString());
		query.setParameter("idsRespostasQuestionario", idsRespostasQuestionario);
		List<Object[]> listaIndicadores = query.getResultList();

		IndicadorDTO indicadorDTO;
		List<IndicadorDTO> listaIndicadorDTO = new ArrayList<IndicadorDTO>();
		for (Object[] objects : listaIndicadores) {
			boolean aprovado = true;
			if ((Integer) objects[2] > 0) {
				aprovado = false;
			}
			indicadorDTO = new IndicadorDTO((String) objects[0], (String) objects[1], aprovado);
			listaIndicadorDTO.add(indicadorDTO);
		}

		return listaIndicadorDTO;

	}
}
