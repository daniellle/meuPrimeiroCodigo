package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import fw.core.jpa.BaseDAO;
import fw.core.jpa.H2Database;

import br.com.ezvida.rst.model.Token;

public class TokenDAO extends BaseDAO<Token, String> {

    @Inject
    public TokenDAO(@H2Database EntityManager em) {
        super(em, Token.class);
    }

}