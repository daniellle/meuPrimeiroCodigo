package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TipoCursoDAO;
import br.com.ezvida.rst.model.TipoCurso;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class TipoCursoService extends BaseService{

	private static final long serialVersionUID = -4206289824783658682L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TipoCursoService.class);
	
	@Inject
	private TipoCursoDAO tipoCursoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCurso> listarTodos() {	
		return tipoCursoDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoCurso buscarPorId(Long id) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		TipoCurso produtoServico = tipoCursoDAO.pesquisarPorId(id);
		if (produtoServico == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return produtoServico;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipoCurso salvar(TipoCurso tipoCurso, ClienteAuditoria auditoria) {

		if (tipoCurso == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		
		String descricaoAuditoria = "Cadastro de tipo de curso: ";
		if (tipoCurso.getId() != null) {
			descricaoAuditoria = "Remoção de tipo de curso: ";
		}
		validar(tipoCurso);
		tipoCursoDAO.salvar(tipoCurso);
		
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, tipoCurso);
		return tipoCurso;
	}
	
	private void validar(TipoCurso tipoCurso) {
		TipoCurso tipoCursoCadastrado = buscarPorDescricao(tipoCurso.getDescricao());
		if (tipoCursoCadastrado != null) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_tipo_curso"), getMensagem("app_rst_label_descricao")));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoCurso buscarPorDescricao(String descricao) {
		LOGGER.debug("Buscando Tipo Curso por Descrição");
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		return tipoCursoDAO.pesquisarPorDescricao(descricao);
	}

}
