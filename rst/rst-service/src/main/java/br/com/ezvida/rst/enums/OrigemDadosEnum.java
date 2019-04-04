package br.com.ezvida.rst.enums;

public enum OrigemDadosEnum {

    DEGUSTACAO("Degustação"), ENTRADA_MANUAL("Entrada Manual");

    private String descricao;

    OrigemDadosEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
