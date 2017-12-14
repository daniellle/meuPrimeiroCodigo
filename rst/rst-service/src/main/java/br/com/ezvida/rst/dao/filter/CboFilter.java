package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class CboFilter extends FilterBase {
	
	@QueryParam("codigo")
	private String codigo;

	@QueryParam("descricao")
	private String descricao;
	
	@QueryParam("idCbos")
	private String idCbos;
	
	@QueryParam("idEmpresa")
	private Long idEmpresa;
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdCbos() {
		return idCbos;
	}

	public void setIdCbos(String idCbos) {
		this.idCbos = idCbos;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}
