package br.com.ezvida.rst.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

import br.com.ezvida.rst.model.Parametro;

public class ParametroDAO extends BaseDAO<Parametro, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametroDAO.class);

    @Inject
    public ParametroDAO(EntityManager em) {
        super(em, Parametro.class);
    }

    public Parametro carregarPorNome(String nome) {

        LOGGER.debug("Carregando parametro por nome: (#{})", nome);

        //@formatter:off
        String jpql = "select"
            .concat("   parametro ")
            .concat("from")
            .concat("   Parametro parametro ")
            .concat("where")
            .concat("   parametro.nome = :nome");
        //@formatter:on

        return DAOUtil.getSingleResult(criarConsultaPorTipo(jpql)
                .setParameter("nome", nome));

    }

}