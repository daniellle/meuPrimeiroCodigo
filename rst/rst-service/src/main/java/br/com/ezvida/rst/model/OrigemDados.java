package br.com.ezvida.rst.model;

import fw.core.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "origem_dados")
public class OrigemDados extends BaseEntity<Long> {

    private static final long serialVersionUID = 7992787033298999021L;

    @Id
    @Column(name = "id_origem_dados")
    private Long id;

    @Column(name = "ds_origem_dados", nullable = false, length = 20)
    private String descricao;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrigemDados that = (OrigemDados) o;

        return id.equals(that.id);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
