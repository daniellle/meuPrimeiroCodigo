package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class ImunizacaoFilter extends FilterBase {

    @QueryParam("periodo")
    private String periodo;

	@QueryParam("nome")
	private String nome;

    public String getPeriodo(){
        return periodo;
    }

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return "ImunizacaoFilter [periodo=" + periodo + ", nome=" + nome + "]";
	}

}
