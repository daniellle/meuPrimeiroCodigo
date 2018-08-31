package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EmpresaLotacaoFilter extends FilterBase {

	@QueryParam("idUnidadeObra")
	private Long idUnidadeObra;

	@QueryParam("idSetor")
	private Long idSetor;

	@QueryParam("idFuncao")
	private Long idFuncao;

	@QueryParam("idJornada")
	private Long idJornada;

	@QueryParam("idCargo")
	private Long idCargo;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public Long getIdUnidadeObra() {
		return idUnidadeObra;
	}

	public Long getIdSetor() {
		return idSetor;
	}

	public Long getIdFuncao() {
		return idFuncao;
	}

	public Long getIdJornada() {
		return idJornada;
	}

	public Long getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Long idCargo) {
		this.idCargo = idCargo;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}
}
