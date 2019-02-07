package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.girst.apiclient.model.Usuario;
import br.com.ezvida.girst.apiclient.model.UsuarioPerfilSistema;
import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.UsuarioEntidadeDAO;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.UsuarioEntidade;
import br.com.ezvida.rst.utils.ValidadorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaTrabalhadorLotacaoDAO;
import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorLotacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaTrabalhadorLotacaoService extends BaseService {

	private static final long serialVersionUID = 4356739461801828538L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorLotacaoService.class);

	@Inject
	private EmpresaTrabalhadorLotacaoDAO empresaTrabalhadorLotacaoDAO;

	@Inject
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@Inject
	private TrabalhadorDAO trabalhadorDAO;

	@Inject
	private UsuarioEntidadeDAO usuarioEntidadeDAO;

	@Inject
	@Preferencial
	private UsuarioService usuarioService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaTrabalhadorLotacao> pesquisarPaginado(
			EmpresaTrabalhadorLotacaoFilter empresaTrabalhadorLotacaoFilter, ClienteAuditoria auditoria) {
		LOGGER.debug("Pesquisando Trabalhador por filtro paginado");


		if (empresaTrabalhadorService.pesquisarPorIdEIdEmpresa(empresaTrabalhadorLotacaoFilter.getIdEmpresaTrabalhador(),
				empresaTrabalhadorLotacaoFilter.getIdEmpresa()) == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de empresa trabalhador lotação por filtro: ",
				empresaTrabalhadorLotacaoFilter);
		return empresaTrabalhadorLotacaoDAO.pesquisarPaginado(empresaTrabalhadorLotacaoFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaTrabalhadorLotacao pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando EmpresaTrabalhadorLotacao por id");
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return empresaTrabalhadorLotacaoDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhadorLotacao salvar(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			ClienteAuditoria auditoria) {
		String descricaoAuditoria = "Cadastro de empresa trabalhador lotação: ";
		if (empresaTrabalhadorLotacao.getId() != null) {
			descricaoAuditoria = "Alteração de empresa trabalhador lotação: ";
		}
		validar(empresaTrabalhadorLotacao);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresaTrabalhadorLotacao);
		empresaTrabalhadorLotacaoDAO.salvar(empresaTrabalhadorLotacao);
		return empresaTrabalhadorLotacao;
	}

	private void validar(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao) {

		if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataAdmissao() != null) {
			if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataAdmissao()
					.after(empresaTrabalhadorLotacao.getDataAssociacao())) {
				throw new BusinessErrorException(getMensagem("app_rst_emp_tra_lot_fora_periodo"));
			}
		}

		 if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataDemissao() != null) {
		
			 if (empresaTrabalhadorLotacao.getDataDesligamento() != null) {
//				 throw new BusinessErrorException(getMensagem("app_rst_data_desligamento_obrigatoria"));				 
				 if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataDemissao()
							.before(empresaTrabalhadorLotacao.getDataDesligamento())) {
						throw new BusinessErrorException(getMensagem("app_rst_emp_tra_lot_fora_periodo"));					
				}
			 }
		 }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhadorLotacao remover(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			ClienteAuditoria auditoria) {
		String descricaoAuditoria = "Removendo Empresa Trabalhador Lotação: ";
		EmpresaTrabalhadorLotacao empresaTrabalhadorRetorno = null;
		if (empresaTrabalhadorLotacao != null && empresaTrabalhadorLotacao.getId() != null) {
			empresaTrabalhadorRetorno = pesquisarPorId(empresaTrabalhadorLotacao.getId());
			if (empresaTrabalhadorRetorno.getDataExclusao() == null) {
				empresaTrabalhadorRetorno.setDataExclusao(new Date());
			}
			LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresaTrabalhadorLotacao);
			empresaTrabalhadorLotacaoDAO.salvar(empresaTrabalhadorRetorno);
		}
		return empresaTrabalhadorRetorno;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(EmpresaTrabalhador empresaTrabalhador) {
		return CollectionUtils.isNotEmpty(empresaTrabalhadorLotacaoDAO
				.verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(empresaTrabalhador));
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarTrabalhadorPrimeiroAcesso(String cpf){
		boolean validarPrimeiroAcesso;

				validarPrimeiroAcesso = validarTrabalhadorVidaAtiva(cpf);
				if (!validarPrimeiroAcesso){
					throw new BusinessErrorException(getMensagem("app_rst_empregado_invalido"));
				}

		return validarPrimeiroAcesso;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarTrabalhador(String cpf){
		LOGGER.debug("Validando usuário");
		if (validarCpf(cpf)){
			Usuario usuario = buscarUsuarioGirst(cpf);
			if(usuarioSotemPerfisDeEmpresa(usuario)){
				Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpf(cpf);
				List<UsuarioEntidade> usuarioEntidade = usuarioEntidadeDAO.pesquisarPorCPF(cpf, true);
				Boolean validarGestorOuTrabalhador = false;
				if(trabalhador == null && usuarioEntidade != null){
					validarGestorOuTrabalhador = empresaTrabalhadorLotacaoDAO.validarGestor(cpf);
				}else{
					validarGestorOuTrabalhador = empresaTrabalhadorLotacaoDAO.validarTrabalhador(cpf);
				}
				if(!validarGestorOuTrabalhador){
					throw new BusinessErrorException(getMensagem("app_rst_empregado_invalido"));
				}
			}
		} else{
			throw new BusinessErrorException(getMensagem("app_rst_empregado_cpf_invalido"));
		}
		return true;
	}

	public Usuario buscarUsuarioGirst(String cpf){
		LOGGER.debug("Pesquisando Usuario no GIRST");
		Usuario usuario = new Usuario();
		try{
			usuario = usuarioService.buscarPorLogin(cpf);
		} catch (Exception e) {
			throw new BusinessErrorException(getMensagem("app_rst_empregado_invalido"));
		}
		return usuario;
	}

	public boolean usuarioSotemPerfisDeEmpresa(Usuario usuario){
		boolean validar = true;
		for (UsuarioPerfilSistema perfilSistema : usuario.getPerfisSistema()) {
			if (!perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("GEEM")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("GEEMM")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("PFS")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("TRA")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("GCOI")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("RH")
					&& !perfilSistema.getPerfil().getCodigo().trim().toUpperCase().equals("ST")) {
				validar = false;
				break;
			}
		}
		return validar;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarTrabalhadorVidaAtiva(String cpf) {
		Boolean validarTrabalhadorComVidaAtiva = false;

			if(validarCpf(cpf)) {
				validarTrabalhadorComVidaAtiva = empresaTrabalhadorLotacaoDAO.validarTrabalhador(cpf);
			}
		return validarTrabalhadorComVidaAtiva;
	}

	public boolean validarCpf(String cpf){
		if( !cpf.isEmpty() ) {
			cpf = cpf.replace(".","").replace("-","");
		}else{
			throw new BusinessErrorException(getMensagem("app_rst_empregado_cpf_invalido"));
		}
		return (ValidadorUtils.isValidCPF(cpf));
	}

}
