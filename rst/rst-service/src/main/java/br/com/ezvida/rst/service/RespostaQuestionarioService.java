package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RespostaQuestionarioDAO;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.model.RespostaQuestionario;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class RespostaQuestionarioService extends BaseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5668627105791716843L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RespostaQuestionarioService.class);

	@Inject
	private RespostaQuestionarioDAO respostaQuestionarioDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RespostaQuestionario> pesquisarPorPerguntaQuestionario(Long idPerguntaQuestionario) {
		if (idPerguntaQuestionario == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return respostaQuestionarioDAO.pesquisarPorPerguntaQuestionario(idPerguntaQuestionario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RespostaQuestionario desativarRespostaQuestionario(RespostaQuestionario respostaQuestionario, ClienteAuditoria auditoria) {
		if (respostaQuestionario != null && respostaQuestionario.getId() != null) {
			respostaQuestionario = pesquisarPorId(respostaQuestionario.getId(), auditoria);
			if (respostaQuestionario.getDataExclusao() == null) {
				respostaQuestionario.setDataExclusao(new Date());
				respostaQuestionarioDAO.salvar(respostaQuestionario);
				String descricaoAuditoria = "Desativação de resposta no questionário: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, respostaQuestionario);
			}
		}
		return respostaQuestionario;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RespostaQuestionario pesquisarPorId(Long id, ClienteAuditoria auditoria) {

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		RespostaQuestionario respostaQuestionario = respostaQuestionarioDAO.pesquisarPorId(id);

		if (respostaQuestionario == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de resposta por id: " + id);
		return respostaQuestionario;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRespostaQuestionarioEmLote(Set<RespostaQuestionario> respostaQuestionarios, PerguntaQuestionario perguntaQuest) {
		LOGGER.debug("desativando respostas questionário");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(respostaQuestionarios)) {
		for (RespostaQuestionario respostaQuestionario : respostaQuestionarios) {
			respostaQuestionario.setPerguntaQuestionario(perguntaQuest);
			respostaQuestionarioDAO.salvar(respostaQuestionario);
		}
			ids = respostaQuestionarios.stream().map(t -> t.getId()).collect(Collectors.toList());
		}

		LOGGER.debug("Foram desativados ");

		respostaQuestionarioDAO.desativar(perguntaQuest.getId(), ids, "perguntaQuestionario");
	}
}
