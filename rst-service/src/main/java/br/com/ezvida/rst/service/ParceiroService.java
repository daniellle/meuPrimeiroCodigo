package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ParceiroDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.model.Parceiro;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class ParceiroService extends BaseService {

	private static final long serialVersionUID = 7990146043589789883L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroService.class);

	@Inject
	private ParceiroDAO parceiroDAO;

	@Inject
	private ParceiroEspecialidadeService parceiroEspecialidadeService;

	@Inject
	private EmailParceiroService emailParceiroService;

	@Inject
	private EnderecoParceiroService enderecoParceiroService;

	@Inject
	private TelefoneParceiroService telefoneParceiroService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Parceiro pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Parceiro parceiro = parceiroDAO.pesquisarPorId(id);
		
		if (parceiro == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		if (parceiro != null) {
			parceiro.setParceiroEspecialidades(
					Sets.newLinkedHashSet(parceiroEspecialidadeService.pesquisarPorParceiro(parceiro.getId())));
			parceiro.setTelefonesParceiro(
					Sets.newLinkedHashSet(telefoneParceiroService.pesquisarPorParceiro(parceiro.getId())));
			parceiro.setEnderecosParceiro(
					Sets.newLinkedHashSet(enderecoParceiroService.pesquisarPorParceiro(parceiro.getId())));
			parceiro.setEmailsParceiro(
					Sets.newLinkedHashSet(emailParceiroService.pesquisarPorParceiro(parceiro.getId())));
		}
		
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de parceiro por id: " + id);

		return parceiro;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Parceiro> listarTodos(ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, 
				"Listando todas os Parceiros Credenciados.");
		return parceiroDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Parceiro> pesquisarPaginado(ParceiroFilter parceiroFilter, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Unidades de Parceiros Credenciados por filtro: ", parceiroFilter);
		return parceiroDAO.pesquisarPaginado(parceiroFilter, dados);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Parceiro salvar(Parceiro parceiro, ClienteAuditoria auditoria) {
		
		String descricaoAuditoria = "Cadastro de parceiro: ";
		if(parceiro.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de parceiro: ";
		}

		validar(parceiro);
		parceiroDAO.salvar(parceiro);

		parceiroEspecialidadeService.salvar(parceiro.getParceiroEspecialidades(), parceiro);
		emailParceiroService.salvar(parceiro.getEmailsParceiro(), parceiro);
		enderecoParceiroService.salvar(parceiro.getEnderecosParceiro(), parceiro);
		telefoneParceiroService.salvar(parceiro.getTelefonesParceiro(), parceiro);
		
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, parceiro);
		
		return parceiro;
	}

	private void validar(Parceiro parceiro) {
		LOGGER.debug("Validando paricero... ");

		String labelCnpjCpf = parceiro.getNumeroCnpjCpf().length() == 11 ? "app_rst_label_cpf" : "app_rst_label_cnpj";
		if (StringUtils.isNotEmpty(parceiro.getNumeroCnpjCpf())
				&& !ValidadorUtils.isValidCNPJ(parceiro.getNumeroCnpjCpf())
				&& !ValidadorUtils.isValidCPF(parceiro.getNumeroCnpjCpf())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_campo_invalido", getMensagem(labelCnpjCpf)));
		}

		Parceiro parceiroRetorno = parceiroDAO.pesquisarPorCNPJ(parceiro.getNumeroCnpjCpf());
		if (parceiroRetorno != null && !parceiroRetorno.getId().equals(parceiro.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_parceiro_credenciado"), getMensagem(labelCnpjCpf)));
		}

		if (StringUtils.isNotBlank(parceiro.getNumeroNit()) && !ValidadorUtils.validarNit(parceiro.getNumeroNit())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
		}

		if (StringUtils.isNotBlank(parceiro.getNumeroNitResponsavel())
				&& !ValidadorUtils.validarNit(parceiro.getNumeroNitResponsavel())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
		}
	}

}
