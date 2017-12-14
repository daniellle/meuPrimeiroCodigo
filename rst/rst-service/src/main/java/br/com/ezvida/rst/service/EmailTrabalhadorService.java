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

import br.com.ezvida.rst.dao.EmailTrabalhadorDAO;
import br.com.ezvida.rst.model.EmailTrabalhador;
import br.com.ezvida.rst.model.Trabalhador;
import fw.core.service.BaseService;

@Stateless
public class EmailTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 2487265128681331956L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTrabalhadorService.class);

	@Inject
	private EmailTrabalhadorDAO emailTrabalhadorDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmailTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Listando todos os EmailTrabalhador por trabalhador");
		return emailTrabalhadorDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailTrabalhador> emailsTrabalhador, Trabalhador trabalhador) {
		LOGGER.debug("Salvando EmailTrabalhador");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(emailsTrabalhador)) {
			for (EmailTrabalhador emailTrabalhador : emailsTrabalhador) {
				emailTrabalhador.setTrabalhador(trabalhador);
				emailService.salvar(emailTrabalhador.getEmail());
				emailTrabalhadorDAO.salvar(emailTrabalhador);
			}
			
			ids = emailsTrabalhador.stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		
		LOGGER.debug("Foram desativados ");
		
		emailService.desativarEmail(trabalhador.getId(), ids, EmailTrabalhador.class, "trabalhador");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmailTrabalhador> pesquisarPorEmail(String email) {
	    return emailTrabalhadorDAO.pesquisarPorEmail(email);
	}
}
