package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;

public class UnidadeObraContratoUatFilter extends FilterBase{

    @QueryParam("idEmpresa")
    private Long idEmpresa;

    public Long getIdEmpresa() {
        return idEmpresa;
    }


    @QueryParam("drs")
    private ArrayList<Long> drs;

    public ArrayList<Long> getDrs() {
        return drs;
    }

    @QueryParam("uats")
    private ArrayList<Long> uats;

    public ArrayList<Long> getUats() {
        return uats;
    }

}