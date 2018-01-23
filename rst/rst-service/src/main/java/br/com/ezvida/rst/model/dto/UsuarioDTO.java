package br.com.ezvida.rst.model.dto;

import java.util.List;

import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.Empresa;

public class UsuarioDTO {

	private String login;

	private List<DepartamentoRegional> departamentosRegionais;

	private List<Empresa> empresas;

	private String tipoImagem;

	private byte[] imagem;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<DepartamentoRegional> getDepartamentosRegionais() {
		return departamentosRegionais;
	}

	public void setDepartamentosRegionais(List<DepartamentoRegional> departamentosRegionais) {
		this.departamentosRegionais = departamentosRegionais;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String getTipoImagem() {
		return tipoImagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipoImagem = tipoImagem;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

}
