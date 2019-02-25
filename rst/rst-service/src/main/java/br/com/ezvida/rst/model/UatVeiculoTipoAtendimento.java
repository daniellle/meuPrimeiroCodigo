package br.com.ezvida.rst.model;

import fw.core.model.BaseEntity;
import fw.core.model.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "uat_veiculo_tipo_atendimento", uniqueConstraints = @UniqueConstraint(name = "pk_uat_veiculo_tipo_atendimento", columnNames = {
        "id_uat_veiculo_tipo_atendimento"}))
public class UatVeiculoTipoAtendimento extends BaseEntity<Long> {

    private static final long serialVersionUID = 6511311756196719447L;

    @Id
    @Column(name = "id_uat_veiculo_tipo_atendimento")
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uat_veiculo_tipo_fk", referencedColumnName = "id_uat_veiculo_tipo")
    @NotNull
    private UatVeiculoTipo uatVeiculoTipo;


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UatVeiculoTipo getUatVeiculoTipo() {
        return uatVeiculoTipo;
    }

    public void setUatVeiculoTipo(UatVeiculoTipo uatVeiculoTipo) {
        this.uatVeiculoTipo = uatVeiculoTipo;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
