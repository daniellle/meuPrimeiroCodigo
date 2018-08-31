package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.dao.TelefoneRedeCredenciadaDAO;
import br.com.ezvida.rst.model.RedeCredenciada;
import br.com.ezvida.rst.model.TelefoneRedeCredenciada;
import fw.core.service.BaseService;

@Stateless
public class TelefoneRedeCredenciadaService extends BaseService {

	private static final long serialVersionUID = 4692487413508019593L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneRedeCredenciadaService.class);

	@Inject
	private TelefoneRedeCredenciadaDAO telefoneRedeCredenciadaDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneRedeCredenciada> telefonesRedeCredenciadas, RedeCredenciada redeCredenciada) {
		LOGGER.debug("Salvando TelefoneRedeCredenciada ");
		
		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesRedeCredenciadas)) {
			for (TelefoneRedeCredenciada telefoneRedeCredenciada : telefonesRedeCredenciadas) {
				telefoneRedeCredenciada.setRedeCredenciada(redeCredenciada);
				telefoneService.salvar(telefoneRedeCredenciada.getTelefone());
				salvar(telefoneRedeCredenciada);
			}

			ids = telefonesRedeCredenciadas.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(redeCredenciada.getId(), ids, TelefoneRedeCredenciada.class, "redeCredenciada");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneRedeCredenciada telefoneRedeCredenciada) {
		LOGGER.debug("Salvando TelefoneRedeCredenciada");
		telefoneRedeCredenciadaDAO.salvar(telefoneRedeCredenciada);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TelefoneRedeCredenciada> pesquisarPorRedeCredenciada(Long idRedeCredenciada) {
		LOGGER.debug("Listando todos os TelefoneRedeCredenciada por rede credenciada ");
		return telefoneRedeCredenciadaDAO.pesquisarPorRedeCredenciada(idRedeCredenciada);
	}

}
