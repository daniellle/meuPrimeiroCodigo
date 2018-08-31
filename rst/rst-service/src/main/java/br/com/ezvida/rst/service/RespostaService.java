package br.com.ezvida.rst.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RespostaDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.RespostaFilter;
import br.com.ezvida.rst.model.Resposta;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class RespostaService extends BaseService {

	private static final long serialVersionUID = -5656586347041179510L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassificacaoPontuacaoService.class);

	private static final String RESPOSTA = "Resposta";

	@Inject
	private RespostaDAO respostaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Resposta buscarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		Resposta resposta = respostaDAO.pesquisarPorId(id);
		if (resposta == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de Resposta por id: " + id);

		return resposta;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Resposta> pesquisaPaginada(RespostaFilter respostaFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Respsta por filtro.", respostaFilter);
		return respostaDAO.pesquisarPaginado(respostaFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Resposta salvar(Resposta resposta, ClienteAuditoria auditoria) {

		if (resposta == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		validar(resposta);

		String descricaoAuditoria = "Cadastro de " + RESPOSTA + ": ";
		if (resposta.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de " + RESPOSTA + ": ";
		}

		respostaDAO.salvar(resposta);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, resposta);
		return resposta;
	}

	private void validar(Resposta resposta) {
		Resposta respostaCadastrada = buscarPorDescricao(resposta.getDescricao());
		if (respostaCadastrada != null && !respostaCadastrada.getId().equals(resposta.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_label_resposta_ja_cadastrada"));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Resposta buscarPorDescricao(String descricao) {
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Resposta resposta = respostaDAO.pesquisarPorDescricao(descricao);
		return resposta;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Resposta desativarResposta(Resposta resposta, ClienteAuditoria auditoria) {
		if(resposta != null && resposta.getId() != null) {
			resposta = pesquisarPorId(resposta.getId());
			if(resposta.getDataExclusao() == null) {
				this.validarExclusao(resposta.getId());
				resposta.setDataExclusao(new Date());
				respostaDAO.salvar(resposta);
				String descricaoAuditoria = "Desativação de resposta: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, resposta);
			}
		}
		return resposta;
	}
	
	private void validarExclusao(Long idResposta) {
		if (respostaDAO.respostaEmUso(idResposta)) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_resposta_associada"));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Resposta pesquisarPorId(Long id) {
		return respostaDAO.pesquisarPorId(id);
	}

}
