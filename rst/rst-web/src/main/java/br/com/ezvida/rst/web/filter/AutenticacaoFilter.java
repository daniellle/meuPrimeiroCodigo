package br.com.ezvida.rst.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import fw.core.common.util.ResourceUtil;
import fw.web.ProvedorServico;
import fw.web.request.ValidandoXSSRequestWrapper;

@Provider
public class AutenticacaoFilter extends ProvedorServico implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoFilter.class);

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.trace(ResourceUtil.getMensagem("app_seguranca_removendo_xss"));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!Strings.isNullOrEmpty(((HttpServletRequest) request).getSession().getId())) {

			Enumeration<String> attributeNames = ((HttpServletRequest) request).getSession().getAttributeNames();

			if (attributeNames != null) {

				while (attributeNames.hasMoreElements()) {
					String atributo = attributeNames.nextElement();
					if (!atributo.equals("Shib-Session-ID") || !atributo.equals("Shib-Session-Login")) {
						((HttpServletRequest) request).getSession().removeAttribute(atributo);
					}
				}

			}

		}

		HttpServletRequest req = (HttpServletRequest) request;

		if (!isAllowed(req)) { // html5Mode - forward every other request to index page
			req.getRequestDispatcher("/index.html").forward(request, response);
		}

		HttpSession session = ((HttpServletRequest) req).getSession(true);

		if (session != null && !Strings.isNullOrEmpty(session.getId())) {
			LOGGER.debug("Nova sessÃ£o ({}) gerada com sucesso!");
		}

		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers",
				"origin, content-type, accept, authorization, content-disposition, content-version, content-error, cache-control, pragma, expires");
		((HttpServletResponse) response).addHeader("cache-control", "no-cache, no-store, must-revalidate");
		((HttpServletResponse) response).addHeader("pragma", "no-cache");
		((HttpServletResponse) response).addHeader("expires", "0");

		chain.doFilter(new ValidandoXSSRequestWrapper(req), response);

	}

	private boolean isAllowed(HttpServletRequest req) {
		List<String> allowedStart = Arrays.asList("/index.html", "/api");
		for (String path : allowedStart) {
			if (req.getServletPath().startsWith(path)) {
				return true;
			}
		}

		List<String> allowedEnds = Arrays.asList(".js", ".css", ".scss", ".ico", ".svg", ".jpg", ".png", ".txt", ".eot", ".otf", ".woff2", ".woff",
				".ttf");
		for (String path : allowedEnds) {
			if (req.getServletPath().endsWith(path)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		LOGGER.trace(ResourceUtil.getMensagem("app_seguranca_removendo_xss"));
	}

}
