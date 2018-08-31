package br.com.ezvida.rst.model.dto;

import java.util.Date;

public class DoseDTO {

    private String nome;
    private String dataVacinacao;


    public String getDataVacinacao() {
        return dataVacinacao;
    }

    public void setDataVacinacao(String dataVacinacao) {
        this.dataVacinacao = dataVacinacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
