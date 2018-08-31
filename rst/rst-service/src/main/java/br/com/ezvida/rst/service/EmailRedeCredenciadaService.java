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

import br.com.ezvida.rst.dao.EmailRedeCredenciadaDAO;
import br.com.ezvida.rst.model.EmailRedeCredenciada;
import br.com.ezvida.rst.model.RedeCredenciada;
import fw.core.service.BaseService;

@Stateless
public class EmailRedeCredenciadaService extends BaseService {

	private static final long serialVersionUID = -3226342501897987283L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRedeCredenciadaService.class);

	@Inject
	private EmailRedeCredenciadaDAO emailRedeCredenciadaDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailRedeCredenciada> emailsRedeCredenciada, RedeCredenciada redeCredenciada) {
		LOGGER.debug("Salvando Emails Rede Credenciada");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsRedeCredenciada)) {
			for (EmailRedeCredenciada emailRedeCredenciada : emailsRedeCredenciada) {
				emailRedeCredenciada.setRedeCredenciada(redeCredenciada);
				emailService.salvar(emailRedeCredenciada.getEmail());
				salvar(emailRedeCredenciada);
			}

			ids = emailsRedeCredenciada.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(redeCredenciada.getId(), ids, EmailRedeCredenciada.class, "redeCredenciada");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailRedeCredenciada emailRedeCredenciada) {
		LOGGER.debug("Salvando EmailRedeCredenciada");
		emailRedeCredenciadaDAO.salvar(emailRedeCredenciada);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmailRedeCredenciada> pesquisarPorRedeCredeciada(Long idRedeCredenciada) {
		LOGGER.debug("Listando todos os EmailRedeCredenciada por rede credenciada");
		return emailRedeCredenciadaDAO.pesquisarPorRedeCredenciada(idRedeCredenciada);
	}

}
