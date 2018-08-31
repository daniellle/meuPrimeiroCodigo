package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PerguntaQuestionarioFilter;
import br.com.ezvida.rst.enums.StatusQuestionario;
import br.com.ezvida.rst.model.Pergunta;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.model.dto.GrupoPerguntaQuestionarioDTO;
import br.com.ezvida.rst.model.dto.PerguntaQuestionarioDTO;
import br.com.ezvida.rst.model.dto.QuestionarioDTO;
import br.com.ezvida.rst.model.dto.RespostaQuestionarioDTO;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class PerguntaQuestionarioDAO extends BaseDAO<PerguntaQuestionario, Long> {

	@Inject
	public PerguntaQuestionarioDAO(EntityManager em) {
		super(em, PerguntaQuestionario.class);
	}

	public QuestionarioDTO obterQuestionario() {

		StringBuilder jpql = new StringBuilder();
		jpql.append("select new br.com.ezvida.rst.model.dto.QuestionarioDTO(questionario.id, questionario.nome, ");
		jpql.append(" questionario.descricao) from Questionario questionario ");
		jpql.append(" where questionario.status = :status ");

		TypedQuery<QuestionarioDTO> query = criarConsultaPorTipo(jpql.toString(), QuestionarioDTO.class);
		query.setParameter("status", StatusQuestionario.PUBLICADO);
		return DAOUtil.getSingleResult(query);
	}

	public List<GrupoPerguntaQuestionarioDTO> pesquisarGrupoPerguntaQuestionario(Long idQuestionario) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select distinct new br.com.ezvida.rst.model.dto.GrupoPerguntaQuestionarioDTO(grupoPergunta.id, grupoPergunta.descricao, ");
		jpql.append(" listaPerguntaQuestionario.ordemGrupo) from GrupoPergunta grupoPergunta ");
		jpql.append(" left join grupoPergunta.listaPerguntaQuestionario listaPerguntaQuestionario ");
		jpql.append(" left join listaPerguntaQuestionario.questionario questionario ");
		jpql.append(" where questionario.id = :idQuestionario ");
		jpql.append(" and listaPerguntaQuestionario.dataExclusao is null ");
		jpql.append(" order by listaPerguntaQuestionario.ordemGrupo ");

		TypedQuery<GrupoPerguntaQuestionarioDTO> query = criarConsultaPorTipo(jpql.toString(), GrupoPerguntaQuestionarioDTO.class);
		query.setParameter("idQuestionario", idQuestionario);

		return query.getResultList();
	}

	public List<PerguntaQuestionarioDTO> pesquisarPerguntaQuestionarioDTO(Long idGrupoPergunta, Long idQuestionario) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(
				"select new br.com.ezvida.rst.model.dto.PerguntaQuestionarioDTO(perguntaQuestionario.id, perguntaQuestionario.pergunta.descricao, ");
		jpql.append("perguntaQuestionario.ordemPergunta, perguntaQuestionario.tipoResposta) from PerguntaQuestionario perguntaQuestionario ");
		jpql.append("left join perguntaQuestionario.pergunta pergunta ");
		jpql.append("left join perguntaQuestionario.grupoPergunta grupoPergunta ");
		jpql.append(" where grupoPergunta.id = :idGrupoPergunta and perguntaQuestionario.questionario.id = :idQuestionario ");
		jpql.append(" and  perguntaQuestionario.dataExclusao is null ");
		jpql.append(" order by perguntaQuestionario.ordemPergunta ");

		TypedQuery<PerguntaQuestionarioDTO> query = criarConsultaPorTipo(jpql.toString(), PerguntaQuestionarioDTO.class);
		query.setParameter("idGrupoPergunta", idGrupoPergunta);
		query.setParameter("idQuestionario", idQuestionario);

		List<PerguntaQuestionarioDTO> listaPerguntaQuestionarioDTO = query.getResultList();

		for (PerguntaQuestionarioDTO perguntaQuestionarioDTO : listaPerguntaQuestionarioDTO) {
			perguntaQuestionarioDTO.setListaRespostaQuestionario(pesquisarRespostaQuestionarioDTO(perguntaQuestionarioDTO.getId()));
		}

		return listaPerguntaQuestionarioDTO;
	}

	public List<RespostaQuestionarioDTO> pesquisarRespostaQuestionarioDTO(Long idPerguntaQuestionario) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("select new br.com.ezvida.rst.model.dto.RespostaQuestionarioDTO(respostaQuestionario.id, resposta.descricao, ");
		jpql.append("respostaQuestionario.ordemResposta, respostaQuestionario.pontuacao) from RespostaQuestionario respostaQuestionario ");
		jpql.append("left join respostaQuestionario.perguntaQuestionario perguntaQuestionario ");
		jpql.append("left join respostaQuestionario.resposta resposta ");
		jpql.append(" where perguntaQuestionario.id = :idPerguntaQuestionario and respostaQuestionario.dataExclusao is null");
		jpql.append(" order by respostaQuestionario.ordemResposta ");

		TypedQuery<RespostaQuestionarioDTO> query = criarConsultaPorTipo(jpql.toString(), RespostaQuestionarioDTO.class);
		query.setParameter("idPerguntaQuestionario", idPerguntaQuestionario);

		return query.getResultList();
	}

	public ListaPaginada<PerguntaQuestionario> perguntaQuestionario(PerguntaQuestionarioFilter perguntaQuestionarioFilter) {
		ListaPaginada<PerguntaQuestionario> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, perguntaQuestionarioFilter, false);

		TypedQuery<PerguntaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(perguntaQuestionarioFilter));

		if (perguntaQuestionarioFilter != null && perguntaQuestionarioFilter.getPagina() != null
				&& perguntaQuestionarioFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((perguntaQuestionarioFilter.getPagina() - 1) * perguntaQuestionarioFilter.getQuantidadeRegistro());
			query.setMaxResults(perguntaQuestionarioFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, PerguntaQuestionarioFilter perguntaQuestionarioFilter,
			boolean count) {

		if (count) {
			jpql.append(" select DISTINCT count(p.id)from PerguntaQuestionario p ");
		} else {
			jpql.append(" select DISTINCT p from PerguntaQuestionario p ");
			jpql.append(" left join fetch p.pergunta pergunta ");
			jpql.append(" left join fetch p.grupoPergunta grupoPergunta ");
			jpql.append(" left join fetch p.pergunta pergunta ");
			jpql.append(" left join fetch p.indicadorQuestionario ");
			jpql.append(" left join fetch p.questionario ");

		}

		if (perguntaQuestionarioFilter != null && perguntaQuestionarioFilter.getIdQuestionario() != null) {
			parametros.put("idQuestionario", perguntaQuestionarioFilter.getIdQuestionario());
			jpql.append(" where p.questionario.id = :idQuestionario");
		}

		jpql.append(" and p.dataExclusao is null ");

		if (!count) {
			jpql.append(" order by p.ordemGrupo, p.ordemPergunta");
		}

	}

	public long getCountQueryPaginado(PerguntaQuestionarioFilter perguntaQuestionarioFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		parametros.put("idQuestionario", perguntaQuestionarioFilter.getIdQuestionario());
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, perguntaQuestionarioFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	public List<PerguntaQuestionario> buscarPerguntaQuestionario(PerguntaQuestionarioFilter perguntaQuestionarioFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		jpql.append(" select DISTINCT p from PerguntaQuestionario p ");
		jpql.append(" left join fetch p.pergunta pergunta ");
		jpql.append(" left join fetch p.grupoPergunta grupoPergunta ");
		jpql.append(" left join fetch p.respostaQuestionarios respostaQuestionarios ");
		jpql.append(" left join fetch respostaQuestionarios.resposta resposta ");

		if (perguntaQuestionarioFilter != null && perguntaQuestionarioFilter.getIdQuestionario() != null) {
			jpql.append(" where p.questionario.id = :idQuestionario");
			jpql.append(" and p.dataExclusao is null");
			parametros.put("idQuestionario", perguntaQuestionarioFilter.getIdQuestionario());
		}

		jpql.append(" order by p.ordemGrupo, p.ordemPergunta");

		TypedQuery<PerguntaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}

	public List<PerguntaQuestionario> buscarPerguntasQuestionarioPorIdQuestionario(Long idQuestionario) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		jpql.append(" select DISTINCT p from PerguntaQuestionario p ");
		jpql.append(" left join fetch p.pergunta pergunta ");
		jpql.append(" left join  p.grupoPergunta grupoPergunta ");
		jpql.append(" left join  p.respostaQuestionarios respostaQuestionarios ");
		jpql.append(" left join  respostaQuestionarios.resposta resposta ");

		if (idQuestionario != null) {
			jpql.append(" where p.questionario.id = :idQuestionario");
			parametros.put("idQuestionario", idQuestionario);
			jpql.append(" and p.dataExclusao is null");
		}
		TypedQuery<PerguntaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}

	public Pergunta verificarPerguntaEmUso(Pergunta pergunta) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select DISTINCT p from PerguntaQuestionario p ");
		jpql.append(" left join fetch p.pergunta pergunta ");

		if (pergunta != null && pergunta.getId() != null) {
			jpql.append(" where pergunta.id = :id");
			parametros.put("id", pergunta.getId());
		}

		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString(), Pergunta.class);
		DAOUtil.setParameterMap(query, parametros);
		return query.getSingleResult();
	}

	@Override
	public PerguntaQuestionario pesquisarPorId(Long id) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select DISTINCT p from PerguntaQuestionario p ");
		jpql.append(" left join fetch p.pergunta pergunta ");
		jpql.append(" left join fetch p.grupoPergunta grupoPergunta ");
		jpql.append(" left join fetch p.respostaQuestionarios respostaQuestionarios ");
		jpql.append(" left join fetch respostaQuestionarios.resposta resposta ");
		jpql.append(" left join fetch  p.questionario questionario ");

		if (id != null) {
			jpql.append(" where p.id = :id");
			parametros.put("id", id);
		}

		TypedQuery<PerguntaQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getSingleResult();
	}
}
