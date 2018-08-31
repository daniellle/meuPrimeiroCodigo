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
import br.com.ezvida.rst.dao.IndicadorQuestionarioDAO;
import br.com.ezvida.rst.dao.filter.IndicadorQuestionarioFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.IndicadorQuestionario;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class IndicadorQuestionarioService extends BaseService {

	private static final long serialVersionUID = 8687660962318244863L;

	private static final Logger LOGGER = LoggerFactory.getLogger(IndicadorQuestionarioService.class);

	private static final String INDICADOR_QUESTIONARIO = "Indicador Questionário";

	@Inject
	private IndicadorQuestionarioDAO indicadorQuestionarioDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public IndicadorQuestionario buscarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		IndicadorQuestionario indicadorQuestionario = indicadorQuestionarioDAO.pesquisarPorId(id);
		if (indicadorQuestionario == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de IndicadorQuestionario por id: " + id);

		return indicadorQuestionario;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<IndicadorQuestionario> pesquisaPaginada(
			IndicadorQuestionarioFilter indicadorQuestionarioFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Grupo de IndicadorQuestionario por filtro.",
				indicadorQuestionarioFilter);
		return indicadorQuestionarioDAO.pesquisarPaginado(indicadorQuestionarioFilter);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IndicadorQuestionario salvar(IndicadorQuestionario indicadorQuestionario, ClienteAuditoria auditoria) {

		if (indicadorQuestionario == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		validar(indicadorQuestionario);

		String descricaoAuditoria = "Cadastro de " + INDICADOR_QUESTIONARIO + ": ";
		if (indicadorQuestionario.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de " + INDICADOR_QUESTIONARIO + ": ";
		}

		indicadorQuestionarioDAO.salvar(indicadorQuestionario);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, indicadorQuestionario);
		return indicadorQuestionario;
	}

	private void validar(IndicadorQuestionario indicadorQuestionario) {
		IndicadorQuestionario indicadorQuestionarioCadastrado = buscarPorDescricao(
				indicadorQuestionario.getDescricao());
		if (indicadorQuestionarioCadastrado != null
				&& !indicadorQuestionarioCadastrado.getId().equals(indicadorQuestionario.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_indicador_questionario"), getMensagem("app_rst_label_descricao")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public IndicadorQuestionario buscarPorDescricao(String descricao) {
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		IndicadorQuestionario indicadorQuestionario = indicadorQuestionarioDAO.pesquisarPorDescricao(descricao);
		return indicadorQuestionario;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IndicadorQuestionario desativar(IndicadorQuestionario indicadorQuestionario, ClienteAuditoria auditoria) {
		if(indicadorQuestionario != null && indicadorQuestionario.getId() != null) {
			indicadorQuestionario = pesquisarPorId(indicadorQuestionario.getId());
			if(indicadorQuestionario.getDataExclusao() == null) {
				this.validarExclusao(indicadorQuestionario.getId());
				indicadorQuestionario.setDataExclusao(new Date());
				indicadorQuestionarioDAO.salvar(indicadorQuestionario);
				String descricaoAuditoria = "Desativação de indicador: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, indicadorQuestionario);
			}
		}
		return indicadorQuestionario;
	}
	
	private void validarExclusao(Long idIndicadorQuestionario) {
		if (indicadorQuestionarioDAO.emUso(idIndicadorQuestionario)) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_indicadorQuestionario_associada"));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public IndicadorQuestionario pesquisarPorId(Long id) {
		return indicadorQuestionarioDAO.pesquisarPorId(id);
	}

}
