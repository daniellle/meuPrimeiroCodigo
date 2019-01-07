package br.com.ezvida.rst.dao.filter;

import java.io.Serializable;

import javax.ws.rs.QueryParam;

public class UsuarioFilter extends FilterBase implements Serializable {

	private static final long serialVersionUID = -28954060495542884L;

	@QueryParam("id")
	private Long id;

	@QueryParam("login")
	private String login;

	@QueryParam("nome")
	private String nome;

	@QueryParam("email")
	private String email;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	@QueryParam("idDepartamentoRegional")
	private Long idDepartamentoRegional;

	@QueryParam("codigoPerfil")
	private String codigoPerfil;

	@QueryParam("idUnidadeSesi")
	private Long idUnidadeSesi;

	public UsuarioFilter() {
	}

	public UsuarioFilter(String login, String nome, Integer pagina, Integer quantidadeRegistro) {
		this.login = login;
		this.nome = nome;
		this.setPagina(pagina);
		this.setQuantidadeRegistro(quantidadeRegistro);
	}

	public String getLogin() {
		return login;
	}

	public String getNome() {
		return nome;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public Long getIdDepartamentoRegional() {
		return idDepartamentoRegional;
	}

	public String getCodigoPerfil() {
		return codigoPerfil;
	}

	public Long getIdUnidadeSesi() { return  idUnidadeSesi;}

}
