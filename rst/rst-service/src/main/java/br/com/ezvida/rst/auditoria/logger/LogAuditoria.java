package br.com.ezvida.rst.auditoria.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.MDC;

import br.com.ezvida.girst.apiclient.model.Usuario;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.FilterBase;
import fw.core.common.encode.JacksonMapper;
import fw.core.common.util.ResourceUtil;
import fw.core.exception.BusinessErrorException;
import fw.core.model.BaseEntity;

public class LogAuditoria {

	private static final String TIPO_OPERACAO = "tipo_operacao";
	private static final String NAVEGADOR = "navegador";
	private static final String FUNCIONALIDADE = "funcionalidade";
	private static final String RST = "rst";
	private static final String APP_NAME = "appName";
	private static final String USUARIO = "usuario";
	private static final String PATTERN = "dd-MM-yyyy HH:mm:ss";

	private LogAuditoria() {
		throw new IllegalStateException("Utility class");
	}
	
	public static <T extends BaseEntity<Long>> void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, Collection<T> objects) {
		createMDC(auditoria);
		try {
			for (T object : objects) {
				logger.info(mensagem + new String(createMapper().writeValueAsBytes(object), "UTF-8"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
			
		}

	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, BaseEntity<Long> obj) {
		createMDC(auditoria);

		JacksonMapper mapper = createMapper();
		mapper.addMixIn(byte[].class, PropertyFilter.class);
		
		try {
			logger.info(mensagem + new String(mapper.writeValueAsBytes(obj), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, FilterBase obj) {
		createMDC(auditoria);

		try {
			logger.info(mensagem + "= {}", new String(createMapper().writeValueAsBytes(obj), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem) {
		createMDC(auditoria);
		logger.info(mensagem);
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, Usuario usuario) {
		createMDC(auditoria);
		try {
			logger.info(mensagem + new String(createMapper().writeValueAsBytes(usuario), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
	}

	private static JacksonMapper createMapper() {
		JacksonMapper mapper = new JacksonMapper();
		DateFormat df = new SimpleDateFormat(PATTERN);
		mapper.setDateFormat(df);

		return mapper;
	}

	private static void createMDC(ClienteAuditoria auditoria) {
		MDC.clear();
		MDC.put(APP_NAME, RST);
		MDC.put(USUARIO, auditoria.getUsuario());
		MDC.put(FUNCIONALIDADE, auditoria.getFuncionalidade().getCodigoJson());
		MDC.put(NAVEGADOR, auditoria.getNavegador());
		MDC.put(TIPO_OPERACAO, auditoria.getTipoOperacao().getCodigoJson());
	}
}
