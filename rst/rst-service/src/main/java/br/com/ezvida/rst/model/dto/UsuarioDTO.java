package br.com.ezvida.rst.model.dto;

import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.Empresa;

import java.util.List;

public class UsuarioDTO {

    private String login;

    private List<DepartamentoRegional> departamentosRegionais;

    private List<Empresa> empresas;

    private String tipoImagem;

    private byte[] imagem;

    private String apelido;

    private Boolean exibirApelido;

    private String fotoPerfil;

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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Boolean getExibirApelido() {
        return exibirApelido;
    }

    public void setExibirApelido(Boolean exibirApelido) {
        this.exibirApelido = exibirApelido;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
