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

import br.com.ezvida.rst.dao.EmailProfissionalDAO;
import br.com.ezvida.rst.model.EmailProfissional;
import br.com.ezvida.rst.model.Profissional;
import fw.core.service.BaseService;

@Stateless
public class EmailProfissionalService extends BaseService {

	private static final long serialVersionUID = -2448076993512958091L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailProfissionalService.class);

	@Inject
	private EmailProfissionalDAO emailProfissionalDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmailProfissional> pesquisarPorProfissional(Long idProfissional) {
		LOGGER.debug("Listando todos os EmailProfissional por profissional");
		return emailProfissionalDAO.pesquisarPorProfissional(idProfissional);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailProfissional> emailsProfissional, Profissional profissional) {
		LOGGER.debug("Salvando EmailProfissional");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(emailsProfissional)) {
			for (EmailProfissional emailProfissional : emailsProfissional) {
				emailProfissional.setProfissional(profissional);
				emailService.salvar(emailProfissional.getEmail());
				emailProfissionalDAO.salvar(emailProfissional);
			}

			ids = emailsProfissional.stream().map(t -> t.getId()).collect(Collectors.toList());
		}

		LOGGER.debug("Foram desativados ");

		emailService.desativarEmail(profissional.getId(), ids, EmailProfissional.class, "profissional");
	}

}
