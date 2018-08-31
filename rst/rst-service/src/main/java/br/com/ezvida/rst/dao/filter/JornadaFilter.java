package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class JornadaFilter extends FilterBase {

	@QueryParam("turno")
	private String turno;

	@QueryParam("qtdHoras")
	private String qtdHoras;

	@QueryParam("idJornadas")
	private String idJornadas;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public String getTurno() {
		return turno;
	}

	public String getQtdHoras() {
		return qtdHoras;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

}
