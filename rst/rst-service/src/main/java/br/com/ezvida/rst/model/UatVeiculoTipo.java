package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UAT_VEICULO_TIPO")
public class UatVeiculoTipo extends AbstractData {

    private static final long serialVersionUID = -9074566396669587122L;

    @Id
    @Column(name = "ID_UAT_VEICULO_TIPO")
    private Long id;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
