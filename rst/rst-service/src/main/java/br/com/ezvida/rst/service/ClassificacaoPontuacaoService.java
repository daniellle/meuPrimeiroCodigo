package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ClassificacaoPontuacaoDAO;
import br.com.ezvida.rst.dao.filter.ClassificacaoPontuacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.ClassificacaoPontuacao;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class ClassificacaoPontuacaoService extends BaseService {

	private static final String APP_RST_PARAMETRO_NULO = "app_rst_parametro_nulo";

	private static final long serialVersionUID = -2787157930970702838L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassificacaoPontuacaoService.class);

	private static final String CLASSIFICACAO_PONTUACAO = "ClassificacaoPontuacao";

	@Inject
	private ClassificacaoPontuacaoDAO classificacaoPontuacaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClassificacaoPontuacao buscarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem(APP_RST_PARAMETRO_NULO));
		}

		ClassificacaoPontuacao classificacaoPontuacao = classificacaoPontuacaoDAO.pesquisarPorId(id);
		if (classificacaoPontuacao == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de ClassificacaoPontuacao por id: " + id);

		return classificacaoPontuacao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<ClassificacaoPontuacao> pesquisaPaginada(
			ClassificacaoPontuacaoFilter classificacaoPontuacaoFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando ClassificacaoPontuacao por filtro.",
				classificacaoPontuacaoFilter);
		return classificacaoPontuacaoDAO.pesquisarPaginado(classificacaoPontuacaoFilter);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ClassificacaoPontuacao salvar(ClassificacaoPontuacao classificacaoPontuacao, ClienteAuditoria auditoria) {

		if (classificacaoPontuacao == null) {
			throw new BusinessErrorException(getMensagem(APP_RST_PARAMETRO_NULO));
		}
		validar(classificacaoPontuacao);

		String descricaoAuditoria = "Cadastro de " + CLASSIFICACAO_PONTUACAO + ": ";
		if (classificacaoPontuacao.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de " + CLASSIFICACAO_PONTUACAO + ": ";
		}

		classificacaoPontuacaoDAO.salvar(classificacaoPontuacao);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, classificacaoPontuacao);
		return classificacaoPontuacao;
	}

	private void validar(ClassificacaoPontuacao classificacaoPontuacao) {
		ClassificacaoPontuacao classificacaoPontuacaoCadastrado = buscarPorDescricao(
				classificacaoPontuacao.getDescricao());
		if (classificacaoPontuacaoCadastrado != null) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_classificacao_ja_cadastrada"));
		}
		
		List<ClassificacaoPontuacao> listaClassificacaoEntcontrada = classificacaoPontuacaoDAO.buscarClassificacaoPontuacaoPorValorMinimoEMaximo(classificacaoPontuacao.getValorMinimo(), classificacaoPontuacao.getValorMaximo());
		if (CollectionUtils.isNotEmpty(listaClassificacaoEntcontrada)) {
			for (ClassificacaoPontuacao classificacaoPontuacao2 : listaClassificacaoEntcontrada) {
				if (!classificacaoPontuacao2.getId().equals(classificacaoPontuacao.getId())) {
					throw new BusinessErrorException(getMensagem("app_rst_erro_classificacao_ja_cadastrada_com_mesmo_valor_minimo_ou_maximo"));
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClassificacaoPontuacao buscarPorDescricao(String descricao) {
		if (descricao == null) {
			throw new BusinessErrorException(getMensagem(APP_RST_PARAMETRO_NULO));
		}
		return classificacaoPontuacaoDAO.pesquisarPorDescricao(descricao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ClassificacaoPontuacao desativar(ClassificacaoPontuacao classificacaoPontuacao, ClienteAuditoria auditoria) {
		if(classificacaoPontuacao != null && classificacaoPontuacao.getId() != null) {
			classificacaoPontuacao = buscarPorId(classificacaoPontuacao.getId());
			if(classificacaoPontuacao.getDataExclusao() == null) {
				this.validarExclusao(classificacaoPontuacao.getId());
				classificacaoPontuacao.setDataExclusao(new Date());
				classificacaoPontuacaoDAO.salvar(classificacaoPontuacao);
				String descricaoAuditoria = "Desativação de classificação/pontuação: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, classificacaoPontuacao);
			}
		}
		return classificacaoPontuacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClassificacaoPontuacao buscarPorId(Long id) {		
		return classificacaoPontuacaoDAO.pesquisarPorId(id);	
	}
	
	private void validarExclusao(Long idClassificacaoPontuacao) {
		if (classificacaoPontuacaoDAO.classificacaoPontuacaoEmUso(idClassificacaoPontuacao)) {
			throw new BusinessErrorException(getMensagem("app_rst_erro_classificacao_associada"));
		}
	}

}
