package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class QuestionarioTrabalhadorFilter extends FilterBase {

    @QueryParam("id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
