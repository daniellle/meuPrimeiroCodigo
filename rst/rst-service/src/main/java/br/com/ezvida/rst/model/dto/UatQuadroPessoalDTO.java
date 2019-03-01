package br.com.ezvida.rst.model.dto;

public class UatQuadroPessoalDTO {
    private String descricaoArea;
    private String descricaoTipoServico;
    private String descricaoTipoProfissional;
    private Integer quantidade;
    private Long idQuadroPessoal;
    private Long idUat;

    public UatQuadroPessoalDTO(String descricaoArea, String descricaoTipoServico, String descricaoTipoProfissional, Integer quantidade, Long idQuadroPessoal, Long idUat) {
        this.descricaoArea = descricaoArea;
        this.descricaoTipoServico = descricaoTipoServico;
        this.descricaoTipoProfissional = descricaoTipoProfissional;
        this.quantidade = quantidade;
        this.idQuadroPessoal = idQuadroPessoal;
        this.idUat = idUat;
    }

    public String getDescricaoArea() {
        return descricaoArea;
    }

    public String getDescricaoTipoServico() {
        return descricaoTipoServico;
    }

    public String getDescricaoTipoProfissional() {
        return descricaoTipoProfissional;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Long getIdQuadroPessoal() {
        return idQuadroPessoal;
    }

    public Long getIdUat() {
        return idUat;
    }
}
