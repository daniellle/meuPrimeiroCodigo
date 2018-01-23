package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class CertificadoFilter extends FilterBase {

	@QueryParam("id")
	private Long id;

	@QueryParam("descricao")
	private String descricao;

	@QueryParam("idTipoCurso")
	private Long idTipoCurso;

	@QueryParam("idTrabalhador")
	private Long idTrabalhador;

	@QueryParam("inclusaoTrabalhador")
	private String inclusaoTrabalhador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public Long getIdTipoCurso() {
		return idTipoCurso;
	}

	public void setIdTipoCurso(Long idTipoCurso) {
		this.idTipoCurso = idTipoCurso;
	}

	public Long getIdTrabalhador() {
		return idTrabalhador;
	}

	public String getInclusaoTrabalhador() {
		return inclusaoTrabalhador;
	}

}
