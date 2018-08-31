package br.com.ezvida.rst.model.dto;

import java.util.List;
import java.util.Map;

public class QuestionarioTrabalhadorDTO {

	private Long idTrabalhador;
	private Long idEmpresa;
	private Long idQuestionarioTrabalhador;
	private Map<Integer, List<Integer>> respostas;
	
	public Long getIdTrabalhador() {
		return idTrabalhador;
	}
	public void setIdTrabalhador(Long idTrabalhador) {
		this.idTrabalhador = idTrabalhador;
	}
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Long getIdQuestionarioTrabalhador() {
		return idQuestionarioTrabalhador;
	}
	public void setIdQuestionarioTrabalhador(Long idQuestionarioTrabalhador) {
		this.idQuestionarioTrabalhador = idQuestionarioTrabalhador;
	}
	public Map<Integer, List<Integer>> getRespostas() {
		return respostas;
	}
	public void setRespostas(Map<Integer, List<Integer>> respostas) {
		this.respostas = respostas;
	}
}
