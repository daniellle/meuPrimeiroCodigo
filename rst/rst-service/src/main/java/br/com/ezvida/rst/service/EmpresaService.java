	package br.com.ezvida.rst.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaDAO;
import br.com.ezvida.rst.dao.EmpresaTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.UsuarioEntidade;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaService extends BaseService {

	private static final long serialVersionUID = 7996558601128390932L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaService.class);

	@Inject
	private EmpresaDAO empresaDAO;

	@Inject
	private EmpresaTrabalhadorDAO empresaTrabalhadorDAO;

	@Inject
	private EmailEmpresaService emailEmpresaService;

	@Inject
	private EnderecoEmpresaService enderecoEmpresaService;

	@Inject
	private TelefoneEmpresaService telefoneEmpresaService;

	@Inject
	private EmpresaUnidadeAtendimentoTrabalhadorService empresaUatService;

	@Inject
	private EmpresaCnaeService empresaCnaeService;

	@Inject
	private UnidadeObraService unidadeObraService;
	
	@Inject
	private UsuarioEntidadeService usuarioEntidade;

	@Inject
	private UnidadeAtendimentoTrabalhadorService uatService;

	@Inject
	private SindicatoService sindicatoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empresa pesquisarPorId(Long id, DadosFilter dadosFilter) {
		LOGGER.debug("Pesquisando Empresas por id");
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		return empresaDAO.pesquisarPorId(id, dadosFilter);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empresa pesquisarPorId(Long id, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LOGGER.debug("Pesquisando Empresas por id");
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Empresa empresa = empresaDAO.pesquisarPorId(id, dadosFilter);
		
		if (empresa == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		if (empresa != null) {
			empresa.setTelefoneEmpresa(Sets.newHashSet(telefoneEmpresaService.buscarPorEmpresa(empresa.getId())));
			empresa.setEnderecosEmpresa(Sets.newHashSet(enderecoEmpresaService.buscarPorEmpresa(empresa.getId())));
			empresa.setEmailsEmpresa(Sets.newHashSet(emailEmpresaService.buscarPorEmpresa(empresa.getId())));
			empresa.setEmpresaUats(Sets.newHashSet(empresaUatService.buscarPorEmpresa(empresa.getId())));
			empresa.setEmpresaCnaes(Sets.newHashSet(empresaCnaeService.buscarPorEmpresa(empresa.getId())));
			empresa.setUnidadeObra(Sets.newHashSet(unidadeObraService.buscarPorEmpresa(empresa.getId())));
		}
		LogAuditoria.registrar(LOGGER, auditoria,  "pesquisa de empresa por id: " + id);
		return empresa;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Empresa> listarTodos() {
		return empresaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Empresa> pesquisarPaginado(EmpresaFilter clienteFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de empresa por filtro: ", clienteFilter);
		return empresaDAO.pesquisarPaginado(clienteFilter, dadosFilter);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UsuarioEntidade> pesquisarMinhaEmpresa(ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de minhas empresas");
		
		List<UsuarioEntidade> empresas = usuarioEntidade.pesquisarTodasEmpresasAssociadas(auditoria.getUsuario());
		
		if (empresas == null || empresas.isEmpty()) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresas;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Empresa salvar(Empresa empresa, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		String descricaoAuditoria = "Cadastro de empresa: ";
		if (empresa.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de empresa: ";
		}
		validar(empresa);

		if (empresa.getId() != null && empresa.getDataDesativacao() != null) {
			empresaTrabalhadorDAO.inativar(empresa.getId());
		}

		if (!dadosFilter.isGestorEmpresa()) {
			empresaDAO.salvar(empresa);
			emailEmpresaService.salvar(empresa.getEmailsEmpresa(), empresa);
			enderecoEmpresaService.salvar(empresa.getEnderecosEmpresa(), empresa);
			telefoneEmpresaService.salvar(empresa.getTelefoneEmpresa(), empresa);
			empresaUatService.salvar(empresa.getEmpresaUats(), empresa);
			empresaCnaeService.salvar(empresa.getEmpresaCnaes(), empresa);
			unidadeObraService.salvar(empresa.getUnidadeObra(), empresa);
		} else if (empresa.getId() != null) {
			Empresa emp = empresaDAO.pesquisarPorId(empresa.getId(), dadosFilter);
			emp.setNomeContato(empresa.getNomeContato());
			emp.setNumeroNitContato(empresa.getNumeroNitContato());
			emp.setDescricaCargoContato(empresa.getDescricaCargoContato());
			emp.setNumeroTelefoneContato(empresa.getNumeroTelefoneContato());
			emp.setEmailContato(empresa.getEmailContato());
			emp.setNomeResponsavel(empresa.getNomeResponsavel());
			emp.setNumeroNitResponsavel(empresa.getNumeroNitResponsavel());
			emp.setEmailResponsavel(empresa.getEmailResponsavel());
			emp.setNumeroTelefone(empresa.getNumeroTelefone());
			empresaDAO.salvar(emp);
			emailEmpresaService.salvar(empresa.getEmailsEmpresa(), empresa);
			telefoneEmpresaService.salvar(empresa.getTelefoneEmpresa(), empresa);
		}


		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, empresa);
		return empresa;
	}

	private void validar(Empresa empresa) {

		if (StringUtils.isNotEmpty(empresa.getCnpj()) && !ValidadorUtils.isValidCNPJ(empresa.getCnpj())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
		}

		Empresa empresaRetorno = empresaDAO.pesquisarPorCNPJ(empresa.getCnpj());
		if (empresaRetorno != null && !empresaRetorno.getId().equals(empresa.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_empresa"), getMensagem("app_rst_label_cnpj")));
		}

		if (StringUtils.isNotBlank(empresa.getNumeroNitContato())
				&& !ValidadorUtils.validarNit(empresa.getNumeroNitContato())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_contato")));
		}

		if (StringUtils.isNotBlank(empresa.getNumeroNitResponsavel())
				&& !ValidadorUtils.validarNit(empresa.getNumeroNitResponsavel())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Empresa> buscarEmpresasUatsDrsPorIds(Set<Long> ids) {
		LOGGER.debug("Pesquisando Empresas por id");

		if (CollectionUtils.isNotEmpty(ids)) {
			List<Empresa> empresas = empresaDAO.buscarEmpresasPorIds(ids);

			for (Empresa empresa : empresas) {
                empresa.setUats(uatService.buscaPorIdEmpresa(empresa.getId()));
				empresa.setSindicatos(sindicatoService.buscarPorEmpresaEAtivos(empresa.getId()));
				empresa.setId(null);
			}

			return empresas;
		}

		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Empresa> buscarEmpresasUatsDrsPorCpf(String cpf) {
		LOGGER.debug("Pesquisando Empresas por cpf");
		List<Empresa> empresas = empresaDAO.buscarEmpresasUatsDrsPorCpf(cpf);

		for (Empresa empresa : empresas) {
			empresa.setUats(uatService.buscarPorEmpresaEAtivas(empresa.getId()));
			empresa.setSindicatos(sindicatoService.buscarPorEmpresaEAtivos(empresa.getId()));
			empresa.setId(null);
		}

		return empresas;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empresa buscarEmpresaCadastroPorCnpj(String cnpj) {
        LOGGER.debug("Pesquisando Empresas por cpf");
        if (cnpj == null) {
            throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
        }

        Empresa empresa = empresaDAO.buscarEmpresaCadastroPorCnpj(cnpj);

        if (empresa == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

		return empresa;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> findCNPJByIdsDepartamentoRegional(Collection<Long> ids) {
		return empresaDAO.findCNPJByIdsDepartamentoRegional(ids);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> findCNPJByIdsUnidadeSesi(Collection<Long> ids) {
		return empresaDAO.findCNPJByIdsUnidadeSesi(ids);
	}

}
