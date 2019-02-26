package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.TipoPrograma;

public class TipoProgramaDAO extends BaseRstDAO<TipoPrograma, Long> {

    @Inject
    public TipoProgramaDAO(EntityManager em) {
        super(em, TipoPrograma.class, "descricao");
    }

}
