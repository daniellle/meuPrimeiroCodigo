package br.com.ezvida.rst.model;

import br.com.ezvida.girst.apiclient.model.Usuario;
public class PrimeiroAcesso {
	private Trabalhador trabalhador;
	private Usuario usuario;
	private SolicitacaoEmail solicitacaoEmail;

	PrimeiroAcesso() {
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public SolicitacaoEmail getSolicitacaoEmail() {
		return solicitacaoEmail;
	}

	public void setSolicitacaoEmail(SolicitacaoEmail solicitacaoEmail) {
		this.solicitacaoEmail = solicitacaoEmail;
	}
}
