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

import br.com.ezvida.rst.dao.EmailDepartamentoRegionalDAO;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.EmailDepartamentoRegional;
import fw.core.service.BaseService;

@Stateless
public class EmailDepartamentoRegionalService extends BaseService {

	private static final long serialVersionUID = 2341998655035365127L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailDepartamentoRegionalService.class);

	@Inject
	private EmailDepartamentoRegionalDAO emailDepartamentoRegionalDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailDepartamentoRegional> emailsDepartamentoRegional,
			DepartamentoRegional departamentoRegional) {
		LOGGER.debug("Salvando Emails Departamento Regional");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsDepartamentoRegional)) {
			for (EmailDepartamentoRegional emailDepartamentoRegional : emailsDepartamentoRegional) {
				emailDepartamentoRegional.setDepartamentoRegional(departamentoRegional);
				emailService.salvar(emailDepartamentoRegional.getEmail());
				emailDepartamentoRegionalDAO.salvar(emailDepartamentoRegional);
			}

			ids = emailsDepartamentoRegional.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(departamentoRegional.getId(), ids, EmailDepartamentoRegional.class, "departamentoRegional");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailDepartamentoRegional emailDepartamentoRegional) {
		LOGGER.debug("Salvando EmailDepartamentoRegional");
		emailDepartamentoRegionalDAO.salvar(emailDepartamentoRegional);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmailDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Listando todos os EmailDepartamentoRegional por departamento regional");
		return emailDepartamentoRegionalDAO.pesquisarPorDepartamentoRegional(idDepartamentoRegional);
	}

}
