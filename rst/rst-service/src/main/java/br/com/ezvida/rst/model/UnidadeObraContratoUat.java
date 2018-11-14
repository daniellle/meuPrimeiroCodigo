package br.com.ezvida.rst.model;

import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fw.core.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "und_obra_contrato_uat", uniqueConstraints = @UniqueConstraint(name = "pk_und_obra_contrato_uat", columnNames = {
        "id_und_obra_contrato_uat" }))
public class UnidadeObraContratoUat extends BaseEntity<Long> {

    @Id
    @Column(name = "id_und_obra_contrato_uat")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_und_obra_cont_id_und_obra_")
    @SequenceGenerator(name = "seq_und_obra_cont_id_und_obra_", sequenceName = "seq_und_obra_cont_id_und_obra_", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_und_obra_fk")
    private UnidadeObra unidadeObra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_und_atd_trabalhador_fk")
    private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_programa_fk", referencedColumnName = "id_tipo_programa", nullable = false)
    private TipoPrograma tipoPrograma;

    @JsonDeserialize(using = DateJsonDeserializer.class)
    @JsonSerialize(using = DateJsonSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_contrato_ini")
    private Date dataContratoInicio;

    @JsonDeserialize(using = DateJsonDeserializer.class)
    @JsonSerialize(using = DateJsonSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_contrato_fim")
    private Date dataContratoFim;

    @Column(name = "fl_inativo")
    private Character flagInativo;

    @Column(name = "ano_vigencia")
    private String anoVigencia;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UnidadeObra getUnidadeObra() {
        return unidadeObra;
    }

    public void setUnidadeObra(UnidadeObra unidadeObra) {
        this.unidadeObra = unidadeObra;
    }

    public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
        return unidadeAtendimentoTrabalhador;
    }

    public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
        this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
    }

    public TipoPrograma getTipoPrograma() {
        return tipoPrograma;
    }

    public void setTipoPrograma(TipoPrograma tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
    }

    public Date getDataContratoInicio() {
        return dataContratoInicio;
    }

    public void setDataContratoInicio(Date dataContratoInicio) {
        this.dataContratoInicio = dataContratoInicio;
    }

    public Date getDataContratoFim() {
        return dataContratoFim;
    }

    public void setDataContratoFim(Date dataContratoFim) {
        this.dataContratoFim = dataContratoFim;
    }

    public Character getFlagInativo() {
        return flagInativo;
    }

    public void setFlagInativo(Character flagInativo) {
        this.flagInativo = flagInativo;
    }

    public String getAnoVigencia() {
        return anoVigencia;
    }

    public void setAnoVigencia(String anoVigencia) {
        this.anoVigencia = anoVigencia;
    }
}
