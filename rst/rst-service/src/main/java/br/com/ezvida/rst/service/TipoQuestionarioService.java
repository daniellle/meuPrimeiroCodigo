package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TipoQuestionarioDAO;
import br.com.ezvida.rst.model.TipoQuestionario;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

public class TipoQuestionarioService extends BaseService {
	
	private static final long serialVersionUID = 685642312416177650L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TipoQuestionarioService.class);
	
	@Inject
	private TipoQuestionarioDAO tipoQuestionarioDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoQuestionario pesquisarPorId(Long id, ClienteAuditoria auditoria) {	

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		TipoQuestionario tipoQuestionario = tipoQuestionarioDAO.pesquisarPorId(id);
		
		if (tipoQuestionario == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de tipo de questionario por id: " + id);
		return tipoQuestionario;
	}

	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoQuestionario> listarTodos(ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,"listar todos os tipos de questionario");
		return tipoQuestionarioDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipoQuestionario salvar(TipoQuestionario tipoQuestionario, ClienteAuditoria auditoria) {	
		
		String descricaoAuditoria = "Cadastro de tipo de questionario: ";
		if(tipoQuestionario.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de tipo de questionario: ";
		}
		
		validar(tipoQuestionario);		
		tipoQuestionarioDAO.salvar(tipoQuestionario);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, tipoQuestionario);
		return tipoQuestionario;
	}

	private void validar(TipoQuestionario tipoQuestionario) {		
		TipoQuestionario tipoQuestionarioRetorno = tipoQuestionarioDAO.pesquisarPorDescricao(tipoQuestionario);
		if (tipoQuestionarioRetorno != null) {
			throw new BusinessErrorException(
					getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_tipoQuestionario") 
							,getMensagem("app_rst_label_Conselho")));
		}
	}

}
