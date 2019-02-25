package br.com.ezvida.rst.model;

import fw.core.model.BaseEntity;
import fw.core.model.BaseModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "uat_veiculo_tipo", uniqueConstraints = @UniqueConstraint(name = "pk_uat_veiculo_tipo", columnNames = {
        "id_uat_veiculo_tipo"}))
public class UatVeiculoTipo extends BaseEntity<Long> {

    private static final long serialVersionUID = -9074566396669587122L;

    @Id
    @Column(name = "id_uat_veiculo_tipo")
    private Long id;

    @Column(name = "descricao")
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
