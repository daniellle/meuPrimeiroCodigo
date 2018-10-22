package br.com.ezvida.rst.dao.filter;


import java.io.Serializable;

public class SistemaCredenciadoFilter implements Serializable {

    private static final long serialVersionUID = 1150395423447694646L;

    private String cnpj;

    private String nomeResponsavel;

    private String sistema;

    private Boolean bloqueado;

    private Integer pagina;

    private Integer quantidadeRegistro;

    public SistemaCredenciadoFilter() {

    }

    public SistemaCredenciadoFilter(String cnpj, String nomeResponsavel, String sistema, Boolean bloqueado, Integer pagina, Integer quantidadeRegistro) {
        this.cnpj = cnpj;
        this.nomeResponsavel = nomeResponsavel;
        this.sistema = sistema;
        this.bloqueado = bloqueado;
        this.pagina = pagina;
        this.quantidadeRegistro = quantidadeRegistro;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getQuantidadeRegistro() {
        return quantidadeRegistro;
    }

    public void setQuantidadeRegistro(Integer quantidadeRegistro) {
        this.quantidadeRegistro = quantidadeRegistro;
    }
}
