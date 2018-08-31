package br.com.ezvida.rst.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.GrupoPerguntaDAO;
import br.com.ezvida.rst.dao.filter.GrupoPerguntaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.GrupoPergunta;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class GrupoPerguntaService extends BaseService {

	private static final long serialVersionUID = 9040982681054747378L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GrupoPerguntaService.class);

	private static final String GRUPO_PERGUNTA = "Grupo de Pergunta";

	@Inject
	private GrupoPerguntaDAO grupoPerguntaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public GrupoPergunta buscarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		GrupoPergunta grupoPergunta = grupoPerguntaDAO.pesquisarPorId(id);
		if (grupoPergunta == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de Grupo de Pergunta por id: " + id);

		return grupoPergunta;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<GrupoPergunta> pesquisaPaginada(GrupoPerguntaFilter grupoPerguntaFilter,
			ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Grupo de Pergunta por filtro.", grupoPerguntaFilter);
		return grupoPerguntaDAO.pesquisarPaginado(grupoPerguntaFilter);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public GrupoPergunta salvar(GrupoPergunta grupoPergunta, ClienteAuditoria auditoria) {

		if (grupoPergunta == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		validar(grupoPergunta);

		String descricaoAuditoria = "Cadastro de " + GRUPO_PERGUNTA + ": ";
		if (grupoPergunta.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de " + GRUPO_PERGUNTA + ": ";
		}

		grupoPerguntaDAO.salvar(grupoPergunta);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, grupoPergunta);
		return grupoPergunta;
	}

	private void validar(GrupoPergunta grupoPergunta) {
		GrupoPergunta grupoPerguntaCadastrado = buscarPorDescricao(grupoPergunta.getDescricao());
		if (grupoPerguntaCadastrado != null) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_grupo"), getMensagem("app_rst_label_descricao")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public GrupoPergunta buscarPorDescricao(String descricao) {
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		GrupoPergunta grupoPergunta = grupoPerguntaDAO.pesquisarPorDescricao(descricao);
		return grupoPergunta;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public GrupoPergunta desativarGrupoPergunta(GrupoPergunta grupoPergunta, ClienteAuditoria auditoria) {
		if(grupoPergunta != null && grupoPergunta.getId() != null) {
			grupoPergunta = pesquisarPorId(grupoPergunta.getId());
			if(grupoPergunta.getDataExclusao() == null) {
				this.validarExclusao(grupoPergunta.getId());
				grupoPergunta.setDataExclusao(new Date());
				grupoPerguntaDAO.salvar(grupoPergunta);
				String descricaoAuditoria = "Desativação de grupoPergunta: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, grupoPergunta);
			}
		}
		return grupoPergunta;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public GrupoPergunta pesquisarPorId(Long id) {
		return grupoPerguntaDAO.pesquisarPorId(id);
	}
	
	private void validarExclusao(Long idGrupoPergunta) {
		if (grupoPerguntaDAO.validarUso(idGrupoPergunta)) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_grupo_pergunta_associada"));
		}
	}

}
