package br.com.ezvida.rst.model.dto;

import java.math.BigDecimal;

public class UatInstalacaoFisicaDTO {
    private Long idInstalacaoFisica;
    private String descricaoCategoria;
    private String descricaoAmbiente;
    private Integer quantidade;
    private BigDecimal area;

    public UatInstalacaoFisicaDTO(Long idInstalacaoFisica, String descricaoCategoria, String descricaoAmbiente, Integer quantidade, BigDecimal area) {
        this.idInstalacaoFisica = idInstalacaoFisica;
        this.descricaoCategoria = descricaoCategoria;
        this.descricaoAmbiente = descricaoAmbiente;
        this.quantidade = quantidade;
        this.area = area;
    }

    public Long getIdInstalacaoFisica() {
        return idInstalacaoFisica;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public String getDescricaoAmbiente() {
        return descricaoAmbiente;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getArea() {
        return area;
    }
}
