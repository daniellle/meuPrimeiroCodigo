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

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getQtdHoras() {
		return qtdHoras;
	}

	public void setQtdHoras(String qtdHoras) {
		this.qtdHoras = qtdHoras;
	}

	public String getIdJornadas() {
		return idJornadas;
	}

	public void setIdJornadas(String idJornadas) {
		this.idJornadas = idJornadas;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	
	
	

}
