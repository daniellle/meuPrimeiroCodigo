package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.converter.ModalidadeConverter;
import br.com.ezvida.rst.enums.Modalidade;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "CERTIFICADO", uniqueConstraints = @UniqueConstraint(name = "PK_CERTIFICADO"
, columnNames = { "ID_CERTIFICADO" }))
public class Certificado extends AbstractData  {

	private static final long serialVersionUID = -1664160860396616915L;

	@Id
	@Column(name = "ID_CERTIFICADO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CERTIFICADO")
	@SequenceGenerator(name = "SEQUENCE_CERTIFICADO", sequenceName = "SEQ_CERTIFICADO_ID_CERTIFICA", allocationSize = 1)
	private Long id;
	
	@Column(name = "DS_CERTIFICADO")
	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR", nullable = false)
	private Trabalhador trabalhador;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_CURSO_FK", referencedColumnName = "ID_TIPO_CURSO")
	private TipoCurso tipoCurso;
	
	@Column(name = "FL_MODALIDADE")
	@Convert(converter = ModalidadeConverter.class)
	private Modalidade modalidade;
	
	@Column(name = "QTD_HORAS")
	private String cargaHoraria;
	
	@Column(name = "arquivo_cert")
	@Type(type="org.hibernate.type.BinaryType")
	private byte[] arquivo;
	
	@Column(name = "tp_arquivo_cert")
	private String tipoArquivo;
	
	@Column(name = "nm_arquivo_cert")
	private String nomeArquivo;
	
	@Column(name = "dt_validade")
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataValidade;
	
	@Column(name = "DT_CONCLUSAO")
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConclusao;
	
	@Column(name = "FL_TRABALHADOR")
	private String inclusaoTrabalhador;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}

	public void setTipoCurso(TipoCurso tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public String getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
	
	public String getInclusaoTrabalhador() {
		return inclusaoTrabalhador;
	}

	public void setInclusaoTrabalhador(String inclusaoTrabalhador) {
		this.inclusaoTrabalhador = inclusaoTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Certificado other = (Certificado) obj;
		
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		
		return true;
	}
	

}
