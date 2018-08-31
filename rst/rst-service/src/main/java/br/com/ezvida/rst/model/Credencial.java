package br.com.ezvida.rst.model;

import java.io.Serializable;

public class Credencial implements Serializable {

    private static final long serialVersionUID = 5868547109601699338L;

    private String identificador;

    private String usuario;

    private String senha;

    public Credencial() {
        // Padrão
    }

    public Credencial(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}