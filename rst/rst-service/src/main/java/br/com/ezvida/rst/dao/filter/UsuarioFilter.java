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

	public UsuarioFilter() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioFilter(String login, String nome, Integer pagina, Integer quantidadeRegistro) {
		this.login = login;
		this.nome = nome;
		this.setPagina(pagina);
		this.setQuantidadeRegistro(quantidadeRegistro);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdDepartamentoRegional() {
		return idDepartamentoRegional;
	}

	public void setIdDepartamentoRegional(Long idDepartamentoRegional) {
		this.idDepartamentoRegional = idDepartamentoRegional;
	}

	public String getCodigoPerfil() {
		return codigoPerfil;
	}

	public void setCodigoPerfil(String codigoPerfil) {
		this.codigoPerfil = codigoPerfil;
	}
}
