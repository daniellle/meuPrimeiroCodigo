package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.GrupoPerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.PerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.QuestionarioDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PerguntaQuestionarioFilter;
import br.com.ezvida.rst.dao.filter.QuestionarioFilter;
import br.com.ezvida.rst.enums.StatusQuestionario;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.model.RespostaQuestionario;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class QuestionarioService extends BaseService {

	private static final long serialVersionUID = 6853839745993194464L;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionarioService.class);

	@Inject
	private QuestionarioDAO questionarioDAO;

	@Inject
	private PerguntaQuestionarioService perguntaQuestionarioService;
	
	@Inject
	private PerguntaQuestionarioDAO perguntaQuestionarioDAO;
	
	@Inject
	private GrupoPerguntaQuestionarioDAO grupoPerguntaQuestionarioDAO;
	
	@Inject
	private RespostaQuestionarioService respostaQuestionarioService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Questionario pesquisarPorId(Long id, ClienteAuditoria auditoria) {

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Questionario questionario = questionarioDAO.pesquisarPorId(id);

		if (questionario == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		if (auditoria != null) {
			LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de questionario por id: " + id);
		}
		return questionario;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Questionario> pesquisarPaginado(QuestionarioFilter questionarioFilter,
			ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de questionario por filtro: ", questionarioFilter);
		return questionarioDAO.pesquisarPaginado(questionarioFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Questionario> listarTodos() {
		return questionarioDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Questionario salvar(Questionario questionario, ClienteAuditoria auditoria) {
		validar(questionario);
		String descricaoAuditoria = "Alteração no cadastro de questionario: ";
		if (questionario.getId() == null) {
			questionario.setStatus(StatusQuestionario.EDICAO);
			descricaoAuditoria = "Cadastro de questionario: ";
		}

		if (questionario.getId() != null && (questionario.getStatus().equals(StatusQuestionario.PUBLICADO)
				|| questionario.getStatus().equals(StatusQuestionario.DESATIVADO))) {
			questionario.setStatus(StatusQuestionario.EDICAO);
			descricaoAuditoria = "Cadastro de questionario: ";
			try {
				Questionario novoQuestionario = inserirNovoQuestionario(questionario);
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, novoQuestionario);
				return novoQuestionario;
			} catch (CloneNotSupportedException e) {
				LOGGER.error(e.getMessage());
			}
		}
		questionarioDAO.salvar(questionario);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionario);
		return questionario;
	}
	
	private	Questionario inserirNovoQuestionario(Questionario questionario) throws CloneNotSupportedException {
		Questionario novoQuestionario  = criarCopiaQuestionario(questionario);
		questionarioDAO.salvar(novoQuestionario);
		for (PerguntaQuestionario pergunta : novoQuestionario.getListaPerguntaQuestionario()) {
			grupoPerguntaQuestionarioDAO.salvar(pergunta.getGrupoPergunta());
			perguntaQuestionarioDAO.salvar(pergunta);
			respostaQuestionarioService.salvarRespostaQuestionarioEmLote(pergunta.getRespostaQuestionarios(),  pergunta);
		}
		novoQuestionario.setListaPerguntaQuestionario(null);
		return novoQuestionario;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Questionario publicar(Questionario questionario, ClienteAuditoria auditoria) {

		validar(questionario);
		
		questionario.setStatus(StatusQuestionario.PUBLICADO);
		Questionario publicado = questionarioDAO.pesquisarPorPublicado(questionario.getTipoQuestionario());
		
		if (publicado != null) {
			questionario.setVersao(publicado.getVersao() + 1);
			publicado.setStatus(StatusQuestionario.DESATIVADO);
			questionarioDAO.salvar(publicado);
		} else {
			questionario.setVersao(1);
		}
		
		String descricaoAuditoria = "";
		if (questionario.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de questionario: ";
		}

		questionarioDAO.salvar(questionario);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionario);
		return questionario;
	}

	private void validar(Questionario questionario) {
//		List<Questionario> questionarioRetorno = questionarioDAO.pesquisarPorNome(questionario);
//		
//		if (CollectionUtils.isNotEmpty(questionarioRetorno) && !questionarioRetorno.iterator().next().getId().equals(questionario.getId())) {
//			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
//					getMensagem("app_rst_label_questionario"), getMensagem("app_rst_label_titulo")));
//		}
		
		List<PerguntaQuestionario> perguntasQuestionario = perguntaQuestionarioDAO.buscarPerguntasQuestionarioPorIdQuestionario(questionario.getId());
		if (CollectionUtils.isEmpty(perguntasQuestionario)) {
			throw new BusinessErrorException(getMensagem("app_rst_questionario_sem_pergunta_resposta"));
		}
		
	}

	private Questionario criarCopiaQuestionario(Questionario questionario)
			throws CloneNotSupportedException {
		Questionario questionarioClone = (Questionario) ObjectUtils.clone(questionario);
		questionarioClone.setId(null);
		questionarioClone.setVersao(null);
		questionarioClone.setStatus(StatusQuestionario.EDICAO);

		Set<PerguntaQuestionario> clonePerguntas = Sets.newHashSet();

		PerguntaQuestionarioFilter perguntaFilter = new PerguntaQuestionarioFilter();
		perguntaFilter.setIdQuestionario(questionario.getId());
		questionario.setListaPerguntaQuestionario(new HashSet<PerguntaQuestionario>(
				perguntaQuestionarioService.buscarPerguntaQuestionario(perguntaFilter, null)));
		for (PerguntaQuestionario pergunta : questionario.getListaPerguntaQuestionario()) {
			PerguntaQuestionario perguntaClone = (PerguntaQuestionario) pergunta.clone();
			perguntaClone.setId(null);
			perguntaClone.setQuestionario(questionarioClone);

			Set<RespostaQuestionario> cloneResposta = Sets.newHashSet();
			for (RespostaQuestionario resposta : pergunta.getRespostaQuestionarios()) {
				RespostaQuestionario respostaClone = (RespostaQuestionario) resposta.clone();
				respostaClone.setId(null);
				respostaClone.setPerguntaQuestionario(pergunta);
				cloneResposta.add(respostaClone);
			}

			perguntaClone.setRespostaQuestionarios(cloneResposta);
			clonePerguntas.add(perguntaClone);
		}

		questionarioClone.setListaPerguntaQuestionario(clonePerguntas);

		return questionarioClone;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> buscarVersoes() {
		LOGGER.debug(" Buscando versoes Questionário ");
		return questionarioDAO.buscarVersoes();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Questionario desativar(Questionario questionario, ClienteAuditoria auditoria) {
		if (questionario != null && questionario.getId() != null) {
			questionario = pesquisarPorId(questionario.getId(), auditoria);
			if (questionario.getDataExclusao() == null) {
				questionario.setDataExclusao(new Date());
				questionarioDAO.salvar(questionario);
				String descricaoAuditoria = "Desativação de questionario: ";
				if (auditoria != null) {
					LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionario);
				}
			}
		}
		return questionario;
	}
}
