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
import br.com.ezvida.rst.dao.SindicatoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SindicatoFilter;
import br.com.ezvida.rst.model.Sindicato;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class SindicatoService extends BaseService {

	private static final long serialVersionUID = 7996558601128390932L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoService.class);

	@Inject
	private SindicatoDAO sindicatoDAO;
	
	@Inject
	private EmailSindicatoService emailSindicatoService;

	@Inject
	private EnderecoSindicatoService enderecoSindicatoService;

	@Inject
	private TelefoneSindicatoService telefoneSindicatoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Sindicato pesquisarPorId(Long id, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}

		Sindicato sindicato = sindicatoDAO.pesquisarPorId(id,dadosFilter);

		if (sindicato == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		if (sindicato != null) {
			sindicato.setEmail(Sets.newHashSet(emailSindicatoService.pesquisarPorIdSindicato(sindicato.getId())));
			sindicato.setTelefone(Sets.newHashSet(telefoneSindicatoService.pesquisarPorIdSindicato(sindicato.getId())));
			sindicato.setEndereco(Sets.newHashSet(enderecoSindicatoService.pesquisarPorIdSindicato(sindicato.getId())));
		}
		LogAuditoria.registrar(LOGGER, auditoria,  "pesquisa de sindicato por id: " + id);
		return sindicato;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Sindicato> listarTodos() {
		LOGGER.debug("Listando todos os sindicatos");
		return sindicatoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Sindicato> pesquisarPaginado(SindicatoFilter sindicatoFilter, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de sindicato por filtro: ", sindicatoFilter);
		return sindicatoDAO.pesquisarPaginado(sindicatoFilter, dados);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Sindicato salvar(Sindicato sindicato, ClienteAuditoria auditoria) {		
		validar(sindicato);
		sindicatoDAO.salvar(sindicato);
		enderecoSindicatoService.salvar(sindicato.getEndereco(), sindicato);
		emailSindicatoService.salvar(sindicato.getEmail(), sindicato);
		telefoneSindicatoService.salvar(sindicato.getTelefone(), sindicato);
		
		String descricaoAuditoria = "Cadastro de Sindicato: ";
		if(sindicato.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro do Sindicato: ";
		}
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, sindicato);

		return sindicato;
	}

	private void validar(Sindicato sindicato) {		
		
		if (StringUtils.isNotEmpty(sindicato.getCnpj()) && !ValidadorUtils.isValidCNPJ(sindicato.getCnpj())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
		}
				
		Sindicato sindicatoRetorno = sindicatoDAO.pesquisarPorCNPJ(sindicato.getCnpj());
		if (sindicatoRetorno != null && !sindicatoRetorno.getId().equals(sindicato.getId())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_sindicato"), getMensagem("app_rst_label_cnpj")));
		}
	}
	
	public Sindicato pesquisarPorCNPJ(String cnpj, ClienteAuditoria auditoria){
		LogAuditoria.registrar(LOGGER, auditoria,"Pesquisar sindicato por CNPJ: " + cnpj);
		return sindicatoDAO.pesquisarPorCNPJ(cnpj);
	}
	
}
