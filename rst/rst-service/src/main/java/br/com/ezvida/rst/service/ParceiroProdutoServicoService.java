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
import br.com.ezvida.rst.dao.ParceiroProdutoServicoDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.model.Parceiro;
import br.com.ezvida.rst.model.ParceiroProdutoServico;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class ParceiroProdutoServicoService extends BaseService{

	
	private static final long serialVersionUID = -5152642127218096099L;
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroProdutoServico.class);

	@Inject
	private ParceiroProdutoServicoDAO parceiroProdutoServicoDAO;


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<ParceiroProdutoServico> retornarPorParceiroProdutoServico(
			ParceiroFilter ParceiroFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,"Pesquisando Produtos e Serviços por Parceirio");
		return parceiroProdutoServicoDAO.retornarPorParceiro(ParceiroFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<ParceiroProdutoServico> salvar(Long id,
			Set<ParceiroProdutoServico> parceiroProdutoServicos, ClienteAuditoria auditoria) {
		LOGGER.debug("Salvando Jornada Empresa");

		boolean sucess = false;
		if (CollectionUtils.isNotEmpty(parceiroProdutoServicos)) {
			Parceiro parceiro = new Parceiro(id);
			for (ParceiroProdutoServico parceiroProdutoServico : parceiroProdutoServicos) {
				if (parceiroProdutoServico.getId() == null) {
					if (parceiroProdutoServicoDAO.verificandoExistenciaProdutoServico(id,
						parceiroProdutoServico.getProdutoServico().getId()) == null) {
						parceiroProdutoServico.setParceiro(parceiro);
						auditoria.setDescricao("Salvar parceiro produto/servico para o parceiro " 
								+ parceiroProdutoServico.getParceiro().getId());	
						salvar(parceiroProdutoServico,auditoria);
						sucess = true;
					}
				}
			}

		}

		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {
			return parceiroProdutoServicos;
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ParceiroProdutoServico ParceiroProdutoServico, ClienteAuditoria auditoria) {
		parceiroProdutoServicoDAO.salvar(ParceiroProdutoServico);
		LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), ParceiroProdutoServico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ParceiroProdutoServico desativarParceiroProdutoServico(
			ParceiroProdutoServico ParceiroProdutoServico, ClienteAuditoria auditoria) {
		if (ParceiroProdutoServico.getId() != null) {
			ParceiroProdutoServico = parceiroProdutoServicoDAO
					.pesquisarPorId(ParceiroProdutoServico.getId());
			if (ParceiroProdutoServico.getDataExclusao() == null) {
				ParceiroProdutoServico.setDataExclusao(new Date());
				auditoria.setDescricao("Desativar parceiro produto/servico para o parceiro " 
				+ ParceiroProdutoServico.getParceiro().getId());				
				salvar(ParceiroProdutoServico,auditoria);
			}
		}
		return ParceiroProdutoServico;
	}

}
