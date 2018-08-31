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

import br.com.ezvida.rst.dao.EmailParceiroDAO;
import br.com.ezvida.rst.model.EmailParceiro;
import br.com.ezvida.rst.model.Parceiro;
import fw.core.service.BaseService;

@Stateless
public class EmailParceiroService extends BaseService {

	private static final long serialVersionUID = -4423260931857862634L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailParceiroService.class);

	@Inject
	private EmailParceiroDAO emailParceiroDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailParceiro> emailsParceiro, Parceiro parceiro) {
		LOGGER.debug("Salvando Emails Parceiro");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsParceiro)) {
			for (EmailParceiro emailParceiro : emailsParceiro) {
				emailParceiro.setParceiro(parceiro);
				emailService.salvar(emailParceiro.getEmail());
				salvar(emailParceiro);
			}

			ids = emailsParceiro.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(parceiro.getId(), ids, EmailParceiro.class, "parceiro");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailParceiro emailParceiro) {
		LOGGER.debug("Salvando EmailParceiro");
		emailParceiroDAO.salvar(emailParceiro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmailParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Listando todos os EmailParceiro por parceiro ");
		return emailParceiroDAO.pesquisarPorParceiro(idParceiro);
	}

}
