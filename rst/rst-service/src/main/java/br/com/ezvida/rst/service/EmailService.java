package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.EmailDAO;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.Email;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmailService extends BaseService {

	private static final long serialVersionUID = 5066473596257241552L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Inject
	private EmailDAO emailDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Email email) {
		LOGGER.debug("Salvando Email...");
		validar(email);
		if (email.getNotificacao() == null) {
			email.setNotificacao(SimNao.NAO);
		}
		emailDAO.salvar(email);
	}

	private void validar(Email email) {
		LOGGER.debug("Validando Email...");
		if (StringUtils.isNotEmpty(email.getDescricao()) && !ValidadorUtils.isValidEmail(email.getDescricao())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_email")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativarEmail(Long id, List<Long> ids, Class<?> type, String fieldClass) {
		String[] fields = { fieldClass, "email" };
		int quantidade = emailDAO.desativar(id, ids, type, fields);
		LOGGER.debug("Foi desativado um total de {} em {}", quantidade, type.getSimpleName());
	}
}
