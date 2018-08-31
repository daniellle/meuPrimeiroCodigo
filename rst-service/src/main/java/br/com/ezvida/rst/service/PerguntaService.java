package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.PerguntaDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PerguntaFilter;
import br.com.ezvida.rst.model.Pergunta;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class PerguntaService extends BaseService {

	private static final long serialVersionUID = 8886199506238877707L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaService.class);

	@Inject
	private PerguntaDAO perguntaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pergunta pesquisarPorId(Long id, ClienteAuditoria auditoria) {

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Pergunta pergunta = perguntaDAO.pesquisarPorId(id);

		if (pergunta == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de pergunta por id: " + id);
		return pergunta;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Pergunta> pesquisarPaginado(PerguntaFilter perguntaFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de pergunta por filtro: ", perguntaFilter);
		return perguntaDAO.pesquisarPaginado(perguntaFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pergunta> listarTodos() {
		return perguntaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Pergunta salvar(Pergunta pergunta, ClienteAuditoria auditoria) {

		String descricaoAuditoria = "Cadastro de Pergunta: ";
		if (pergunta.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de pergunta: ";
		}

		validar(pergunta);
		perguntaDAO.salvar(pergunta);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, pergunta);
		return pergunta;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Pergunta desativarPergunta(Pergunta pergunta, ClienteAuditoria auditoria) {
		if(pergunta != null && pergunta.getId() != null) {
			pergunta = pesquisarPorId(pergunta.getId());
			if(pergunta.getDataExclusao() == null) {
				this.validarExclusao(pergunta.getId());
				pergunta.setDataExclusao(new Date());
				perguntaDAO.salvar(pergunta);
				String descricaoAuditoria = "Desativação de pergunta: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, pergunta);
			}
		}
		return pergunta;
	}
	
	private void validar(Pergunta pergunta) {
		Pergunta perguntaRetorno = perguntaDAO.pesquisarPorDescricao(pergunta);
		if (perguntaRetorno != null) {
			throw new BusinessErrorException(getMensagem("app_rst_label_pergunta_ja_cadastrada"));
		}
	}
	
	private void validarExclusao(Long idPergunta) {
		if (perguntaDAO.perguntaEmUso(idPergunta)) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_pergunta_associada"));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pergunta pesquisarPorId(Long id) {
		return perguntaDAO.pesquisarPorId(id);
	}

}
