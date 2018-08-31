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
import br.com.ezvida.rst.dao.CertificadoDAO;
import br.com.ezvida.rst.dao.filter.CertificadoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Certificado;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;


@Stateless
public class CertificadoService  extends BaseService {

	private static final long serialVersionUID = 6177984194310109115L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificadoService.class);

	private static final String CERTIFICADOS = "Certificado";
	
	@Inject
	private CertificadoDAO certificadoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> listarTodos() {	
		return certificadoDAO.pesquisarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado buscarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Certificado cert = certificadoDAO.pesquisarPorId(id);
		if (cert == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de certificados por id: " + id);
		
		return cert;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Certificado> pesquisarPaginado(CertificadoFilter filter) {
		return certificadoDAO.pesquisarPaginado(filter);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Certificado salvar(Certificado certificado, ClienteAuditoria auditoria) {

		if (certificado == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		
		String descricaoAuditoria = "Cadastro de " + CERTIFICADOS + ": ";
		if (certificado.getId() != null) {
			certificado.setDataExclusao(new Date());
			descricaoAuditoria = "Remoção de " + CERTIFICADOS + ": ";
		}

		certificadoDAO.salvar(certificado);
		
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, certificado);
		return certificado;
	}
}
