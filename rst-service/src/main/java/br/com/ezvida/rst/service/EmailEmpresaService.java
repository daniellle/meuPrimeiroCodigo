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

import br.com.ezvida.rst.dao.EmailEmpresaDAO;
import br.com.ezvida.rst.model.EmailEmpresa;
import br.com.ezvida.rst.model.Empresa;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmailEmpresaService extends BaseService {

	private static final long serialVersionUID = 8218232567987160783L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailEmpresaService.class);

	@Inject
	private EmailEmpresaDAO emailEmpresaDAO;

	@Inject
	private EmailService emailService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmailEmpresa pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando EmailsEmpresa por id");
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return emailEmpresaDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmailEmpresa> listarTodos() {
		LOGGER.debug("Listando todos os emailEmpresa");
		return emailEmpresaDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmailEmpresa emailEmpresa) {
		LOGGER.debug("Salvando Email Empresa");
		emailEmpresaDAO.salvar(emailEmpresa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmailEmpresa> emailsEmpresa, Empresa empresa) {
		LOGGER.debug("Salvando Emails Empresa...");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(emailsEmpresa)) {
			for (EmailEmpresa emailEmpresa : emailsEmpresa) {
				emailEmpresa.setEmpresa(empresa);
				emailService.salvar(emailEmpresa.getEmail());
				salvar(emailEmpresa);
			}

			ids = emailsEmpresa.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		emailService.desativarEmail(empresa.getId(), ids, EmailEmpresa.class, "empresa");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmailEmpresa> buscarPorEmpresa(Long id) {
		return emailEmpresaDAO.buscarPorEmpresa(id);
	}
}
