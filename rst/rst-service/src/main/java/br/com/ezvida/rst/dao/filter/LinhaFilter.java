package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class LinhaFilter extends FilterBase {

	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

}
