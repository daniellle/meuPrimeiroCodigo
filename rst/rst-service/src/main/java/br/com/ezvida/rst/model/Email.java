package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.converter.TipoEmailConverter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.enums.TipoEmail;

@Entity
@Table(name = "EMAIL", uniqueConstraints = @UniqueConstraint(name = "PK_EMAIL", columnNames = { "ID_EMAIL" }))
public class Email extends AbstractData {

	private static final long serialVersionUID = -8591930033223488189L;

	public static final String REGEX_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-zàáâãéêíóôõúüç0-9]+[A-Za-zàáâãéêíóôõúüç0-9-]*([A-Za-zàáâãéêíóôõúüç0-9])(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";

	@Id
	@Column(name = "ID_EMAIL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL")
	@SequenceGenerator(name = "SEQUENCE_EMAIL", sequenceName = "SEQ_EMAIL_ID_EMAIL", allocationSize = 1)
	private Long id;

	@Column(name = "DS_EMAIL")
	private String descricao;

	@Column(name = "FL_TIPO")
	@Convert(converter = TipoEmailConverter.class)
	private TipoEmail tipo;

	@Column(name = "FL_NOTIFICACAO")
	@Convert(converter = SimNaoConverter.class)
	private SimNao notificacao;

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoEmail getTipo() {
		return tipo;
	}

	public void setTipo(TipoEmail tipo) {
		this.tipo = tipo;
	}

	public SimNao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(SimNao notificacao) {
		this.notificacao = notificacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((notificacao == null) ? 0 : notificacao.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
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
		if (notificacao != other.notificacao)
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
}
