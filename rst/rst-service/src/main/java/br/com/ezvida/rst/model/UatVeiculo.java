package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "UAT_VEICULO", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_VEICULO", columnNames = {
        "ID_UAT_VEICULO"}))
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
    @JoinColumn(name = "ID_UAT_VEICULO_TIPO_FK", referencedColumnName = "ID_UAT_VEICULO_TIPO")
    private UatVeiculoTipo uatVeiculoTipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UAT_VEICULO_TIPO_ATENDIMENTO_FK", referencedColumnName = "ID_UAT_VEICULO_TIPO_ATENDIMENTO")
    private UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataAlteracao(new Date());
		setDataCriacao(new Date());
	}
	
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

	public UatVeiculoTipo getUatVeiculoTipo() {
		return uatVeiculoTipo;
	}

	public void setUatVeiculoTipo(UatVeiculoTipo uatVeiculoTipo) {
		this.uatVeiculoTipo = uatVeiculoTipo;
	}
}
