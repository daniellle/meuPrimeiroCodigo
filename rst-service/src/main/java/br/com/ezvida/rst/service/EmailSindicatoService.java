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

import br.com.ezvida.rst.dao.EmailSindicatoDAO;
import br.com.ezvida.rst.model.EmailSindicato;
import br.com.ezvida.rst.model.Sindicato;
import fw.core.service.BaseService;

@Stateless
public class EmailSindicatoService extends BaseService {

	private static final long serialVersionUID = 1767419036175366950L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSindicatoService.class);

	@Inject
	private EmailSindicatoDAO emailSindicatoDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailSindicato> emailsSindicato, Sindicato sindicato) {
		LOGGER.debug("Salvando Emails Sindicato ...");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsSindicato)) {
			for (EmailSindicato emailSindicato : emailsSindicato) {
				emailService.salvar(emailSindicato.getEmail());
				emailSindicato.setSindicato(sindicato);
				emailSindicatoDAO.salvar(emailSindicato);
			}

			ids = emailsSindicato.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(sindicato.getId(), ids, EmailSindicato.class, "sindicato");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailSindicato emailSindicato) {
		LOGGER.debug("Salvando Email Sindicato ...");
		emailSindicatoDAO.salvar(emailSindicato);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmailSindicato> pesquisarPorIdSindicato(Long id) {
		return emailSindicatoDAO.pesquisarPorIdSindicato(id);
	}
}
