package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class SetorFilter extends FilterBase {
	
	@QueryParam("sigla")
	private String sigla;

	@QueryParam("descricao")
	private String descricao;
	
	@QueryParam("idSetor")
	private String idSetor;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdSetor() {
		return idSetor;
	}

	public void setIdSetor(String idSetor) {
		this.idSetor = idSetor;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}
