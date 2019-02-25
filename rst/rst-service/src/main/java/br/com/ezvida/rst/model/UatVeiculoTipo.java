package br.com.ezvida.rst.model;

import fw.core.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "UAT_VEICULO_TIPO", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_VEICULO_TIPO", columnNames = {
        "ID_UAT_VEICULO_TIPO"}))
public class UatVeiculoTipo extends BaseEntity<Long> {

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
