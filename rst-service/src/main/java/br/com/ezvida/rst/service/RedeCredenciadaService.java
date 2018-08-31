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
import br.com.ezvida.rst.dao.RedeCredenciadaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.model.RedeCredenciada;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class RedeCredenciadaService extends BaseService {

	private static final long serialVersionUID = -4295436949625301851L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaService.class);

	@Inject
	private RedeCredenciadaDAO redeCredenciadaDAO;

	@Inject
	private EmailRedeCredenciadaService emailRedeCredenciadaService;

	@Inject
	private EnderecoRedeCredenciadaService enderecoRedeCredenciadaService;

	@Inject
	private TelefoneRedeCredenciadaService telefoneRedeCredenciadaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RedeCredenciada pesquisarPorId(Long id, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		RedeCredenciada redeCredenciada = redeCredenciadaDAO.pesquisarPorId(id, dadosFilter);
		if (redeCredenciada == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		if (redeCredenciada != null) {
			redeCredenciada
					.setTelefonesRedeCredenciada(
							Sets.newLinkedHashSet(telefoneRedeCredenciadaService.pesquisarPorRedeCredenciada(redeCredenciada.getId())));
			redeCredenciada
					.setEnderecosRedeCredenciada(
							Sets.newLinkedHashSet(enderecoRedeCredenciadaService.pesquisarPorRedeCredenciada(redeCredenciada.getId())));
			redeCredenciada
					.setEmailsRedeCredenciada(Sets.newLinkedHashSet(emailRedeCredenciadaService.pesquisarPorRedeCredeciada(redeCredenciada.getId())));
		}
		
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de rede credenciada por id: " + id);

		return redeCredenciada;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RedeCredenciada> pesquisarTodos(ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Listando todas as Redes Credenciadas.");
		return redeCredenciadaDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<RedeCredenciada> pesquisarPaginado(RedeCredenciadaFilter redeCredenciadaFilter, ClienteAuditoria auditoria,
			DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Redes Credenciadas por filtro: ", redeCredenciadaFilter);
		return redeCredenciadaDAO.pesquisarPaginado(redeCredenciadaFilter, dados);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RedeCredenciada salvar(RedeCredenciada redeCredenciada, ClienteAuditoria auditoria) {
		
		String descricaoAuditoria = "Cadastro de rede credenciada: ";
		if(redeCredenciada.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de rede credenciada: ";
		}
		
		validar(redeCredenciada);
		redeCredenciadaDAO.salvar(redeCredenciada);

		emailRedeCredenciadaService.salvar(redeCredenciada.getEmailsRedeCredenciada(), redeCredenciada);
		enderecoRedeCredenciadaService.salvar(redeCredenciada.getEnderecosRedeCredenciada(), redeCredenciada);
		telefoneRedeCredenciadaService.salvar(redeCredenciada.getTelefonesRedeCredenciada(), redeCredenciada);
		
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, redeCredenciada);
		return redeCredenciada;
	}

	private void validar(RedeCredenciada redeCredenciada) {

		if (StringUtils.isNotEmpty(redeCredenciada.getNumeroCnpj()) && !ValidadorUtils.isValidCNPJ(redeCredenciada.getNumeroCnpj())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
		}

		RedeCredenciada redeCredenciadaRetorno = redeCredenciadaDAO.pesquisarPorCNPJ(redeCredenciada.getNumeroCnpj());
		if (redeCredenciadaRetorno != null && !redeCredenciadaRetorno.getId().equals(redeCredenciada.getId())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_rede_Credenciada"), getMensagem("app_rst_label_cnpj")));
		}

		if (StringUtils.isNotBlank(redeCredenciada.getNumeroNitResponsavel())
				&& !ValidadorUtils.validarNit(redeCredenciada.getNumeroNitResponsavel())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
		}

		if (StringUtils.isNotEmpty(redeCredenciada.getEmailResponsavel()) && !ValidadorUtils.isValidEmail(redeCredenciada.getEmailResponsavel())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_email")));
		}

	}

}
