package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.LinhaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.LinhaFilter;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.Linha;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class LinhaService extends BaseService {

	private static final long serialVersionUID = -6957205194725998504L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LinhaService.class);

	@Inject
	private LinhaDAO linhaDAO;
	
	@Inject
	private UnidadeAtendimentoTrabalhadorService uatService;
	
	@Inject
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Linha> listarTodos(LinhaFilter linhaFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LOGGER.debug("Listando todas as Linha");
		
		if (dadosFilter.isTrabalhador()) {
			Set<EmpresaTrabalhador> empresas = empresaTrabalhadorService.pesquisarPorTrabalhadorCpf(auditoria.getUsuario());
			
			
			if (CollectionUtils.isNotEmpty(empresas)) {
				if (CollectionUtils.isEmpty(dadosFilter.getIdsEmpresa())) {
					dadosFilter.setIdsEmpresa(Sets.newHashSet());
				}
				for (EmpresaTrabalhador empresa : empresas) {
					dadosFilter.getIdsEmpresa().add(empresa.getEmpresa().getId());
				}
			}
		}
		return linhaDAO.pesquisarTodos(dadosFilter, linhaFilter, this.habilitaFiltro(dadosFilter));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Linha buscarPorId(Long id) {
		LOGGER.debug("Buscando Linha por Id");
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Linha linha = linhaDAO.pesquisarPorId(id);
		return linha;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Linha buscarPorDescricao(String descricao) {
		LOGGER.debug("Buscando Linha por Descrição");
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Linha linha = linhaDAO.pesquisarPorDescricao(descricao);
		return linha;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Linha salvar(Linha linha) {
		LOGGER.debug("Salvando Linha");
		if (linha == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		validar(linha);
		linhaDAO.salvar(linha);
		
		return linha;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Linha> buscarLinhasPorIdDepartamento(Long id) {
		LOGGER.debug("Listando todas as Linhas Por Departamento");
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		return linhaDAO.buscarLinhasPorIdDepartamento(id);
	}
	
	private void validar(Linha linha) {
		Linha linhaCadastrada = buscarPorDescricao(linha.getDescricao());
		if (linhaCadastrada != null) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_linha"), getMensagem("app_rst_label_descricao")));
		}
	}

	private boolean habilitaFiltro(DadosFilter filter) {
		return !filter.isGestorDr();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Linha> buscarLinhasPorIdDepartamentoUat(Long id, DadosFilter dados) {

		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		UnidadeAtendimentoTrabalhador uat = uatService.pesquisarPorId(id, null, dados);

		if (uat == null || uat.getDepartamentoRegional() == null || uat.getDepartamentoRegional().getId() == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		
		List<Linha> linhas = linhaDAO.buscarLinhasPorIdDepartamento(uat.getDepartamentoRegional().getId());

		return linhas;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Linha> buscarLinhasPorIdUat(String ids, DadosFilter dados) {

		if (ids == null || StringUtils.isBlank(ids)) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		List<Linha> linhas = linhaDAO.buscarLinhasPorIdUat(ids);

		return linhas;
	}

}
