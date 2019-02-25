package br.com.ezvida.rst.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "uat_veiculo")
public class UatVeiculo extends AbstractData {

    private static final long serialVersionUID = 8672002241253994157L;

    @Id
    @Column(name = "id_uat_veiculo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_uat_veiculo")
    @SequenceGenerator(name = "sequence_uat_veiculo", sequenceName = "seq_uat_veiculo", allocationSize = 1)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_und_atd_trabalhador_fk", referencedColumnName = "id_und_atd_trabalhador")
    @NotNull
    private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uat_veiculo_tipo_atendimento_fk", referencedColumnName = "id_uat_veiculo_tipo_atendimento")
    @NotNull
    private UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento;

    @Column(name = "quantidade")
    @NotNull
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
