package br.com.ezvida.rst.service;

import java.util.Date;
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
import br.com.ezvida.rst.dao.ProfissionalDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProfissionalFilter;
import br.com.ezvida.rst.model.Profissional;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class ProfissionalService extends BaseService {

	private static final long serialVersionUID = -6520802686037153388L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfissionalService.class);

	@Inject
	private ProfissionalDAO profissionalDAO;

	@Inject
	private EmailProfissionalService emailProfissionalService;

	@Inject
	private TelefoneProfissionalService telefoneProfissionalService;

	@Inject
	private EnderecoProfissionalService enderecoProfissionalService;

	@Inject
	private UnidadeAtendimentoTrabalhadorProfissionalService unidadeAtendimentoTrabalhadorProfissionalService;

	@Inject
	private ProfissionalEspecialidadeService profissionalEspecialidadeService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Profissional pesquisarPorId(Long id, ClienteAuditoria auditoria) {	

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Profissional profissional = profissionalDAO.pesquisarPorId(id);
		
		if (profissional == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		if (profissional != null) {
			profissional.setListaEmailProfissional(Sets.newLinkedHashSet(emailProfissionalService.pesquisarPorProfissional(profissional.getId())));
			profissional.setListaTelefoneProfissional(Sets.newLinkedHashSet(telefoneProfissionalService.pesquisarPorProfissional(profissional.getId())));
			profissional.setListaEnderecoProfissional(Sets.newLinkedHashSet(enderecoProfissionalService.pesquisarPorProfissional(profissional.getId())));
			profissional.setListaUnidadeAtendimentoTrabalhadorProfissional(Sets.newLinkedHashSet(unidadeAtendimentoTrabalhadorProfissionalService.pesquisarPorProfissional(profissional.getId())));
			profissional.setListaProfissionalEspecialidade(Sets.newLinkedHashSet(profissionalEspecialidadeService.pesquisarPorProfissional(profissional.getId())));
		}
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de profissional por id: " + id);
		return profissional;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Profissional> pesquisarPaginado(ProfissionalFilter profissionalFilter, ClienteAuditoria auditoria, DadosFilter dados) {
		
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de profissional por filtro: ", profissionalFilter);
		return profissionalDAO.pesquisarPaginado(profissionalFilter, dados);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Profissional> listarTodos() {
		return profissionalDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Profissional salvar(Profissional profissional, ClienteAuditoria auditoria) {	
		
		String descricaoAuditoria = "Cadastro de Profissional: ";
		if(profissional.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de profissional: ";
		}
		
		validar(profissional);		
		profissionalDAO.salvar(profissional);				
		emailProfissionalService.salvar(profissional.getListaEmailProfissional(), profissional);
		telefoneProfissionalService.salvar(profissional.getListaTelefoneProfissional(), profissional);
		enderecoProfissionalService.salvar(profissional.getListaEnderecoProfissional(), profissional);
		unidadeAtendimentoTrabalhadorProfissionalService.salvar(profissional.getListaUnidadeAtendimentoTrabalhadorProfissional(), profissional);
		profissionalEspecialidadeService.salvar(profissional.getListaProfissionalEspecialidade(), profissional);		
		
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, profissional);
		return profissional;
	}

	private void validar(Profissional profissional) {
		
		if (StringUtils.isNotEmpty(profissional.getCpf()) && !ValidadorUtils.isValidCPF(profissional.getCpf())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
		}

		if (StringUtils.isNotEmpty(profissional.getNit()) && !ValidadorUtils.validarNit(profissional.getNit())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
		}
		
		Profissional profissionalRetorno = profissionalDAO.pesquisarPorConselhoRegional(profissional);
		if (profissionalRetorno != null && !profissionalRetorno.getId().equals(profissional.getId())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_profissional") 
							,getMensagem("app_rst_label_Conselho")));
		}

		if (profissional.getDataNascimento() != null && profissional.getDataNascimento().after(new Date())) {
			throw new BusinessErrorException(getMensagem("app_rst_data_maior_que_atual"
					, getMensagem("app_rst_label_data_nascimento")));
		}
	}
}
