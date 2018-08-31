package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class TrabalhadorFilter extends FilterBase {
	
	@QueryParam("id")
	private Long id;
	
	@QueryParam("cpf")
	private String cpf;

	@QueryParam("nome")
	private String nome;

	@QueryParam("nit")
	private String nit;
	
	@QueryParam("situacao")
	private String situacao;
	
	@QueryParam("falecidos")
	private boolean falecidos;
	
	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;
	
	@QueryParam("estado")
	private Long idEstado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cPF) {
		cpf = cPF;
	}

	public String getNome() {
		return nome;
	}


	public String getNit() {
		return nit;
	}


	public String getSituacao() {
		return situacao;
	}


	public boolean isFalecidos() {
		return falecidos;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}
	
	
	public Long getIdEstado() {
		return idEstado;
	}
	
}
