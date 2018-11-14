package br.com.ezvida.rst.model;

import fw.core.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tipo_programa", uniqueConstraints = @UniqueConstraint(name = "pk_tipo_programa", columnNames = {
        "id_tipo_programa" }))
public class TipoPrograma extends BaseEntity<Long> {

    @Id
    @Column(name = "id_tipo_programa")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipo_programa_id_tipo_prog")
    @SequenceGenerator(name = "seq_tipo_programa_id_tipo_prog", sequenceName = "seq_tipo_programa_id_tipo_prog", allocationSize = 1)
    private Long id;

    @Column(name = "ds_sigla")
    private String sigla;

    @Column(name = "ds_tipo_programa")
    private String descricao;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
