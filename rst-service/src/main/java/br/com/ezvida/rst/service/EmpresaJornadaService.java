package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaJornadaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaJornada;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaJornadaService extends BaseService {

	private static final long serialVersionUID = -219674651005262766L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaJornadaService.class);

	@Inject
	private EmpresaJornadaDAO empresaJornadaDAO;
	
	@Inject
	private EmpresaService empresaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaJornada> retornarPorEmpresa(EmpresaFilter empresaFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de jornada por empresa: " + empresaFilter.getId());
		
		if (empresaFilter.getId() == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		
		Empresa empresa = empresaService.pesquisarPorId(empresaFilter.getId(), auditoria, dadosFilter);
		
		if (empresa == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresaJornadaDAO.retornarPorEmpresa(empresaFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaJornada pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de jornada por id: " + id);
		return empresaJornadaDAO.pesquisarPorId(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaJornada pesquisarPorId(Long id) {
		return empresaJornadaDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<EmpresaJornada> salvar(Long id ,Set<EmpresaJornada> emJonadas, ClienteAuditoria auditoria) {
		LOGGER.debug("Salvando Jornada Empresa");
		Set<EmpresaJornada> jornadaSalvos = Sets.newHashSet();
		boolean sucess = false;
		Empresa em = new Empresa(id);
		if (CollectionUtils.isNotEmpty(emJonadas)) {			
			for (EmpresaJornada empresaJornada : emJonadas) {
				if(empresaJornada.getId() == null) {
					if(empresaJornadaDAO.verificandoExistenciaJornada(id,empresaJornada.getJornada().getId()) == null) {
						empresaJornada.setEmpresa(em);
						jornadaSalvos.add(empresaJornada);
						salvar(empresaJornada);
						sucess = true;
					}
				}
			}
			
		}
		if (!jornadaSalvos.isEmpty()) {
			auditoria.setDescricao("Associar jornada para empresa " + id);
			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), jornadaSalvos);
		}
		
		if(!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		}else {
			return emJonadas;
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaJornada empresaJornada) {
		empresaJornadaDAO.salvar(empresaJornada);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaJornada desativarEmpresaJornada(EmpresaJornada emJonadas, ClienteAuditoria auditoria) {
		Set<EmpresaJornada> jornadaSalvos = Sets.newHashSet();
		if(emJonadas.getId() != null) {
			emJonadas = pesquisarPorId(emJonadas.getId());
			if(emJonadas.getDataExclusao() == null) {
				emJonadas.setDataExclusao(new Date());
				jornadaSalvos.add(emJonadas);
				salvar(emJonadas);
			}
		}

		if (!jornadaSalvos.isEmpty()) {
			auditoria.setDescricao("Desassociar jornada para empresa " + emJonadas.getEmpresa().getId());
			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), jornadaSalvos);
		}
		return emJonadas;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaJornada> pesquisarPorEmpresa(Long idEmpresa, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de jornada por empresa: " + idEmpresa);
		return empresaJornadaDAO.pesquisarPorEmpresa(idEmpresa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaJornada> pesquisarPorEmpresa(Long idEmpresa) {
		return empresaJornadaDAO.pesquisarPorEmpresa(idEmpresa);
	}
}
