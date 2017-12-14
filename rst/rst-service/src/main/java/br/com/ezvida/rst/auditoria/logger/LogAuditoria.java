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

	private LogAuditoria() {
		throw new IllegalStateException("Utility class");
	}
	private static final String PATTERN = "dd-MM-yyyy HH:mm:ss";
	
	public static <T> void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, Collection<T> obj) {
		MDC.put("usuario", auditoria.getUsuario());
		MDC.put("funcionalidade", auditoria.getFuncionalidade().getCodigoJson());
		MDC.put("navegador", auditoria.getNavegador());
		MDC.put("tipo_operacao", auditoria.getTipoOperacao().getCodigoJson());
		JacksonMapper mapper = new JacksonMapper();
		DateFormat df  = new SimpleDateFormat(PATTERN);
		mapper.setDateFormat(df);
		try {
			for (T t : obj) {
				logger.info(mensagem + new String(mapper.writeValueAsBytes(t), "UTF-8"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
			
		}
		MDC.clear();

	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, BaseEntity<Long> obj) {
		MDC.put("usuario", auditoria.getUsuario());
		MDC.put("funcionalidade", auditoria.getFuncionalidade().getCodigoJson());
		MDC.put("navegador", auditoria.getNavegador());
		MDC.put("tipo_operacao", auditoria.getTipoOperacao().getCodigoJson());
		JacksonMapper mapper = new JacksonMapper();
		DateFormat df  = new SimpleDateFormat(PATTERN);
		mapper.setDateFormat(df);
		mapper.addMixIn(byte[].class, PropertyFilter.class);
		
		try {
			logger.info(mensagem + new String(mapper.writeValueAsBytes(obj), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
		MDC.clear();
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, FilterBase obj) {
		MDC.put("usuario", auditoria.getUsuario());
		MDC.put("funcionalidade", auditoria.getFuncionalidade().getCodigoJson());
		MDC.put("navegador", auditoria.getNavegador());
		MDC.put("tipo_operacao", auditoria.getTipoOperacao().getCodigoJson());
		JacksonMapper mapper = new JacksonMapper();
		DateFormat df  = new SimpleDateFormat(PATTERN);
		mapper.setDateFormat(df);
		try {
			logger.info(mensagem + "= {}", new String(mapper.writeValueAsBytes(obj), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
		MDC.clear();
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem) {
		MDC.put("usuario", auditoria.getUsuario());
		MDC.put("funcionalidade", auditoria.getFuncionalidade().getCodigoJson());
		MDC.put("navegador", auditoria.getNavegador());
		MDC.put("tipo_operacao", auditoria.getTipoOperacao().getCodigoJson());
		logger.info(mensagem);
		MDC.clear();
	}

	public static void registrar(Logger logger, ClienteAuditoria auditoria, String mensagem, Usuario usuario) {
		MDC.put("usuario", auditoria.getUsuario());
		MDC.put("funcionalidade", auditoria.getFuncionalidade().getCodigoJson());
		MDC.put("navegador", auditoria.getNavegador());
		MDC.put("tipo_operacao", auditoria.getTipoOperacao().getCodigoJson());
		JacksonMapper mapper = new JacksonMapper();
		DateFormat df  = new SimpleDateFormat(PATTERN);
		mapper.setDateFormat(df);
		try {
			logger.info(mensagem + new String(mapper.writeValueAsBytes(usuario), "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessErrorException(ResourceUtil.getMensagem("app_rst_falha_auditoria"), e);
		}
		MDC.clear();
	}
	
	
}
