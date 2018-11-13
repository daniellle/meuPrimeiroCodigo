package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class UnidadeObraContratoUatFilter extends FilterBase{

    @QueryParam("idEmpresa")
    private Long idEmpresa;

    public Long getIdEmpresa() {
        return idEmpresa;
    }


}