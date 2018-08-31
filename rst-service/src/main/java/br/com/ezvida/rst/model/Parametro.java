package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;

import fw.core.model.BaseEntity;

//@formatter:off
@Entity
@Table(name = "parametro")
//@formatter:on
public class Parametro extends BaseEntity<Long> {

    private static final long serialVersionUID = 7571251383231429177L;

    @Id
	@Column(name = "id_parametro")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_parametro")
    @SequenceGenerator(name = "sequence_parametro", sequenceName = "seq_parametro_id", allocationSize = 1, initialValue = 1)
    private Long id;

	@Column(name = "ds_parametro")
    private String nome;

	@Column(name = "vl_parametro")
    private String valor;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("Id", getId()).add("Nome", getNome()).toString();
    }


}