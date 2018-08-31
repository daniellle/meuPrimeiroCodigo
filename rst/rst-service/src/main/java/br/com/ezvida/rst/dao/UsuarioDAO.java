package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Usuario;

import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class UsuarioDAO extends BaseDAO<Usuario, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioDAO.class);

    @Inject
    public UsuarioDAO(EntityManager em) {
        super(em, Usuario.class);
    }

    public Usuario carregarPorLogin(String login) {

        LOGGER.debug("Carregando usuário por login: (#{})", login);

        //@formatter:off
        String jpql = "select"
            .concat("   usuario ")
            .concat("from")
            .concat("   Usuario usuario ")
            .concat("where")
            .concat("   usuario.login = :login");
        //@formatter:on

        TypedQuery<Usuario> query = criarConsultaPorTipo(jpql);
        query.setParameter("login", login);

        return DAOUtil.getSingleResult(query);

    }

}
