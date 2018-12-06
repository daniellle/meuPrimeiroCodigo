package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.model.TipoPrograma;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class TipoProgramaDAO extends BaseRstDAO<TipoPrograma, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoPrograma.class);

    @Inject
    public TipoProgramaDAO(EntityManager em) {
        super(em, TipoPrograma.class, "descricao");
    }

}
