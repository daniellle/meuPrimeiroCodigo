package br.com.ezvida.rst.model.dto;

import java.util.List;
import java.util.Set;

import br.com.ezvida.rst.model.EnderecoUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.TelefoneUnidadeAtendimentoTrabalhador;

public class PesquisaSesiDTO {

	private Long idUat;

	private String razaoSocialUat;

	private Set<EnderecoUnidadeAtendimentoTrabalhador> endereco;

	private Set<TelefoneUnidadeAtendimentoTrabalhador> telefone;

	private List<LinhaDTO> linhas;

	public PesquisaSesiDTO() {

	}

	public PesquisaSesiDTO(Long idUat, String razaoSocialUat, Set<EnderecoUnidadeAtendimentoTrabalhador> endereco,
			Set<TelefoneUnidadeAtendimentoTrabalhador> telefone, List<LinhaDTO> linhas) {
		this.idUat = idUat;
		this.razaoSocialUat = razaoSocialUat;
		this.endereco = endereco;
		this.telefone = telefone;
		this.linhas = linhas;
	}

	public Long getIdUat() {
		return idUat;
	}

	public void setIdUat(Long idUat) {
		this.idUat = idUat;
	}

	public String getRazaoSocialUat() {
		return razaoSocialUat;
	}

	public void setRazaoSocialUat(String razaoSocialUat) {
		this.razaoSocialUat = razaoSocialUat;
	}

	public Set<EnderecoUnidadeAtendimentoTrabalhador> getEndereco() {
		return endereco;
	}

	public void setEndereco(Set<EnderecoUnidadeAtendimentoTrabalhador> endereco) {
		this.endereco = endereco;
	}

	public Set<TelefoneUnidadeAtendimentoTrabalhador> getTelefone() {
		return telefone;
	}

	public void setTelefone(Set<TelefoneUnidadeAtendimentoTrabalhador> telefone) {
		this.telefone = telefone;
	}

	public List<LinhaDTO> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<LinhaDTO> linhas) {
		this.linhas = linhas;
	}
}
