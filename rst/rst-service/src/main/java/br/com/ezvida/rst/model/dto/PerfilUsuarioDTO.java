package br.com.ezvida.rst.model.dto;

import java.io.Serializable;
import java.util.List;

public class PerfilUsuarioDTO implements Serializable {


    private String nome;
    private String login;
    private String perfil;
    private String departamento;
    private String unidade;
    private String empresa;

    public PerfilUsuarioDTO(String nome, String login, String perfil, String departamento, String unidade, String empresa){
        this.nome = nome;
        this.login = login;
        this.perfil = perfil;
        this.departamento = departamento;
        this.unidade = unidade;
        this.empresa = empresa;
    }

    public PerfilUsuarioDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "PerfilUsuarioDTO{" +
                "nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", perfil='" + perfil + '\'' +
                ", departamento='" + departamento + '\'' +
                ", unidade='" + unidade + '\'' +
                ", empresa='" + empresa + '\'' +
                '}';
    }
}
