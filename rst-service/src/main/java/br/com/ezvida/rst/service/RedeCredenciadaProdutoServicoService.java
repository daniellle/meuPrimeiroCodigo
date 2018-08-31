package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RedeCredenciadaProdutoServicoDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.model.RedeCredenciada;
import br.com.ezvida.rst.model.RedeCredenciadaProdutoServico;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;
@Stateless
public class RedeCredenciadaProdutoServicoService extends BaseService {

	private static final long serialVersionUID = -7687345133044758058L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaProdutoServico.class);

	@Inject
	private RedeCredenciadaProdutoServicoDAO redeCredenciadaProdutoServicoDAO;


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<RedeCredenciadaProdutoServico> retornarPorRedeCredenciada(
			RedeCredenciadaFilter redeCredenciadaFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,"Pesquisando Produtos e Serviços por Rede Credenciada");
		return redeCredenciadaProdutoServicoDAO.retornarPorRedeCredenciadal(redeCredenciadaFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<RedeCredenciadaProdutoServico> salvar(Long id,
			Set<RedeCredenciadaProdutoServico> redeCredenciadaProdutoServicos, ClienteAuditoria auditoria) {		

		boolean sucess = false;
		if (CollectionUtils.isNotEmpty(redeCredenciadaProdutoServicos)) {
			RedeCredenciada rede = new RedeCredenciada(id);
			for (RedeCredenciadaProdutoServico redeCredenciadaProdutoServico : redeCredenciadaProdutoServicos) {
				if (redeCredenciadaProdutoServico.getId() == null) {
					if (redeCredenciadaProdutoServicoDAO.verificandoExistenciaProdutoServico(id,
						redeCredenciadaProdutoServico.getProdutoServico().getId()) == null) {
						redeCredenciadaProdutoServico.setRedeCredenciada(rede);
						auditoria.setDescricao("Salvar rede credenciada");
						salvar(redeCredenciadaProdutoServico,auditoria);
						sucess = true;
					}
				}
			}

		}

		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {
			return redeCredenciadaProdutoServicos;
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(RedeCredenciadaProdutoServico redeCredenciadaProdutoServico, ClienteAuditoria auditoria) {
		redeCredenciadaProdutoServicoDAO.salvar(redeCredenciadaProdutoServico);
		LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(),redeCredenciadaProdutoServico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RedeCredenciadaProdutoServico desativarRedeCredenciadaProdutoServico(
			RedeCredenciadaProdutoServico redeCredenciadaProdutoServico, ClienteAuditoria auditoria) {
		if (redeCredenciadaProdutoServico.getId() != null) {
			redeCredenciadaProdutoServico = redeCredenciadaProdutoServicoDAO
					.pesquisarPorId(redeCredenciadaProdutoServico.getId());
			if (redeCredenciadaProdutoServico.getDataExclusao() == null) {
				redeCredenciadaProdutoServico.setDataExclusao(new Date());
				auditoria.setDescricao("Desativar rede credenciada");
				salvar(redeCredenciadaProdutoServico, auditoria);
			}
		}
		return redeCredenciadaProdutoServico;
	}
	
	
}
