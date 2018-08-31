package br.com.ezvida.rst.model;

public class MensagemErro {

	private String codigo;
	
	private String mensagem;
	
	private String descricao;
	
	
	public MensagemErro(String codigo, String mensagem, String descricao) {
		this.codigo = codigo;
		this.mensagem = mensagem;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}