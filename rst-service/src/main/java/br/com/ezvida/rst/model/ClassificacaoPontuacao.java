package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.ezvida.rst.enums.Classificacao;

@Entity
@Table(name = "CLASSIF_PONTUACAO", uniqueConstraints = @UniqueConstraint(name = "PK_CLASSIF_PONT", columnNames = {
		"ID_CLASSIF_PONT" }))
public class ClassificacaoPontuacao extends AbstractData {

	private static final long serialVersionUID = 2769285464215425591L;

	@Id
	@Column(name = "ID_CLASSIF_PONT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CLASSIF_PONTUACAO")
	@SequenceGenerator(name = "SEQUENCE_CLASSIF_PONTUACAO", sequenceName = "SEQ_CLASSIF_PONTU_ID_CLASSIF_P", allocationSize = 1)
	private Long id;

	@Column(name = "DS_CLASSIF_PONT")
	private String descricao;

	@Column(name = "MENS_PONT")
	private String mensagem;

	@Column(name = "RECOMENDACAO")
	private String recomendacao;
	
	@Column(name = "VL_MINIMO")
	private Integer valorMinimo;
	
	@Column(name = "VL_MAXIMO")
	private Integer valorMaximo;

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
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getRecomendacao() {
		return recomendacao;
	}

	public void setRecomendacao(String recomendacao) {
		this.recomendacao = recomendacao;
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	public Integer getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(Integer valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public Integer getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(Integer valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((recomendacao == null) ? 0 : recomendacao.hashCode());
		result = prime * result + ((valorMaximo == null) ? 0 : valorMaximo.hashCode());
		result = prime * result + ((valorMinimo == null) ? 0 : valorMinimo.hashCode());
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
		
		ClassificacaoPontuacao other = (ClassificacaoPontuacao) obj;
		
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (mensagem == null) {
			if (other.mensagem != null) {
				return false;
			}
		} else if (!mensagem.equals(other.mensagem)) {
			return false;
		}
		if (recomendacao == null) {
			if (other.recomendacao != null) {
				return false;
			}
		} else if (!recomendacao.equals(other.recomendacao)) {
			return false;
		}
		if (valorMaximo == null) {
			if (other.valorMaximo != null) {
				return false;
			}
		} else if (!valorMaximo.equals(other.valorMaximo)) {
			return false;
		}
		if (valorMinimo == null) {
			if (other.valorMinimo != null) {
				return false;
			}
		} else if (!valorMinimo.equals(other.valorMinimo)) {
			return false;
		}
		return true;
	}

	public Classificacao getCodigoClassificacao(Integer pontuacao) {
		Classificacao classificacao = null;
		if (pontuacao >= 0 && pontuacao <= 2) {
			classificacao = Classificacao.BAIXO_RISCO;
		} else if (pontuacao >= 3 && pontuacao <= 5) {
			classificacao = Classificacao.MEDIO_RISCO;
		} else if (pontuacao >= 6 && pontuacao <= 7) {
			classificacao = Classificacao.MEDIO_ALTO;
		} else if (pontuacao >= 8 && pontuacao <= 10) {
			classificacao = Classificacao.ALTO_RISCO;
		}
		return classificacao;
	}
	
}
