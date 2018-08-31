package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EmpresaTrabalhadorLotacaoFilter extends FilterBase {

	@QueryParam("id")
	private Long idEmpresaTrabalhador;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public Long getIdEmpresaTrabalhador() {
		return idEmpresaTrabalhador;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

}
