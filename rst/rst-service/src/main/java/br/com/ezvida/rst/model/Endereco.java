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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.HashCodeBuilder;

import br.com.ezvida.rst.converter.TipoEnderecoConverter;
import br.com.ezvida.rst.enums.TipoEndereco;

@Entity
@Table(name = "ENDERECO", uniqueConstraints = @UniqueConstraint(name = "PK_ENDERECO", columnNames = { "ID_ENDERECO" }))
public class Endereco extends AbstractData {

	private static final long serialVersionUID = 3277701293800151197L;

	@Id
	@Column(name = "ID_ENDERECO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ENDERECO")
	@SequenceGenerator(name = "SEQUENCE_ENDERECO", sequenceName = "SEQ_ENDERECO_ID_ENDERECO", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_FK", referencedColumnName = "ID_MUNICIPIO")
	private Municipio municipio;

	@Column(name = "DS_ENDERECO")
	private String descricao;

	@Column(name = "NO_ENDERECO")
	private String numero;

	@Column(name = "DS_COMPLEMENTO")
	private String complemento;

	@Column(name = "NO_CEP")
	private String cep;

	@Column(name = "DS_BAIRRO")
	private String bairro;

	@Column(name = "FL_TIPO")
	@Convert(converter = TipoEnderecoConverter.class)
	private TipoEndereco tipoEndereco;

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getNumero() {
		return numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getCep() {
		return cep;
	}

	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public Municipio getMunicipio() {
		return this.municipio;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(bairro).append(cep).append(complemento)
				.append(getDataAlteracao()).append(getDataCriacao()).append(getDataExclusao()).append(descricao).append(descricao)
				.append(numero).append(tipoEndereco).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
			return false;
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null)
				return false;
		} else if (!getDataCriacao().equals(other.getDataCriacao()))
			return false;
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null)
				return false;
		} else if (!getDataExclusao().equals(other.getDataExclusao()))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (municipio == null) {
			if (other.municipio != null)
				return false;
		} else if (!municipio.equals(other.municipio))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (tipoEndereco != other.tipoEndereco)
			return false;
		return true;
	}
}
