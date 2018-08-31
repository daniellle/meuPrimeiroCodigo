package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class PerguntaQuestionarioFilter extends FilterBase {

	@QueryParam("idQuestionario")
	private Long idQuestionario;

	public Long getIdQuestionario() {
		return idQuestionario;
	}

	public void setIdQuestionario(Long idQuestionario) {
		this.idQuestionario = idQuestionario;
	}
}
