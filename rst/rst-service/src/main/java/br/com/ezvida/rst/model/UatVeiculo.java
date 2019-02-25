package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "UAT_VEICULO")
public class UatVeiculo extends AbstractData {

    private static final long serialVersionUID = 8672002241253994157L;

    @Id
    @Column(name = "ID_UAT_VEICULO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_VEICULO")
    @SequenceGenerator(name = "SEQUENCE_UAT_VEICULO", sequenceName = "SEQ_UAT_VEICULO", allocationSize = 1)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR", nullable = false)
    private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UAT_VEICULO_TIPO_ATENDIMENTO_FK", referencedColumnName = "ID_UAT_VEICULO_TIPO_ATENDIMENTO", nullable = false)
    private UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }


    public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
        return unidadeAtendimentoTrabalhador;
    }

    public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
        this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
    }

    public UatVeiculoTipoAtendimento getUnidadeVeiculoTipoAtendimento() {
        return unidadeVeiculoTipoAtendimento;
    }

    public void setUnidadeVeiculoTipoAtendimento(UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento) {
        this.unidadeVeiculoTipoAtendimento = unidadeVeiculoTipoAtendimento;
    }
}
