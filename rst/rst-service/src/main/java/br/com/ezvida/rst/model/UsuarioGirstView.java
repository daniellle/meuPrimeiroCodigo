package br.com.ezvida.rst.model;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fw.core.model.BaseEntity;

@Entity
@Immutable
@Table(name = "vw_usuario_entidade")
public class UsuarioGirstView extends BaseEntity<Long> {

	private static final long serialVersionUID = 9129132196995010109L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "login")
	private String login;


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

	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;

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
}
