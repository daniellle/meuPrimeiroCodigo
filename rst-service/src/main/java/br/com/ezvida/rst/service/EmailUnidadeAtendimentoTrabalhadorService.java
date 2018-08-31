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

import br.com.ezvida.rst.dao.EmailUnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.model.EmailUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.service.BaseService;

@Stateless
public class EmailUnidadeAtendimentoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 3028254380587599148L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUnidadeAtendimentoTrabalhadorService.class);

	@Inject
	private EmailUnidadeAtendimentoTrabalhadorDAO emailUnidadeAtendimentoTrabalhadorDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailUnidadeAtendimentoTrabalhador> emailsUnidadeAtendimentoTrabalhador, UnidadeAtendimentoTrabalhador unidadeEmailTrabalhador) {
		LOGGER.debug("Salvando Emails Unidade Atendimento Trabalhador...");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsUnidadeAtendimentoTrabalhador)) {
			for (EmailUnidadeAtendimentoTrabalhador emailUnidadeAtendimentoTrabalhador : emailsUnidadeAtendimentoTrabalhador) {
				emailUnidadeAtendimentoTrabalhador.setUnidadeAtendimentoTrabalhador(unidadeEmailTrabalhador);
				emailService.salvar(emailUnidadeAtendimentoTrabalhador.getEmail());
				salvar(emailUnidadeAtendimentoTrabalhador);
			}

			ids = emailsUnidadeAtendimentoTrabalhador.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(unidadeEmailTrabalhador.getId(), ids, EmailUnidadeAtendimentoTrabalhador.class, "unidadeAtendimentoTrabalhador");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailUnidadeAtendimentoTrabalhador emailUnidadeAtendimentoTrabalhador) {
		LOGGER.debug("Emails Unidade Atendimento Trabalhador");
		emailUnidadeAtendimentoTrabalhadorDAO.salvar(emailUnidadeAtendimentoTrabalhador);
	}

	public List<EmailUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		return emailUnidadeAtendimentoTrabalhadorDAO.pesquisarPorIdUat(id);
	}
}
