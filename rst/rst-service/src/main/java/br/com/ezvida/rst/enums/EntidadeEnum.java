package br.com.ezvida.rst.enums;

public enum EntidadeEnum {

    ADMIN("admin"),
    DN("dn"),
    DR("dr"),
    UNIDADE("unidade"),
    EMPRESA("empresa");

    private String value;

    EntidadeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

