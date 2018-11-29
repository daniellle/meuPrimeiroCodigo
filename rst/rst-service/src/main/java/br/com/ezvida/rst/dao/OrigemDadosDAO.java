package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.model.OrigemDados;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;

public class OrigemDadosDAO extends BaseDAO<OrigemDados, Long> {

    @Inject
    public OrigemDadosDAO(EntityManager em) {
        super(em, OrigemDados.class);
    }

    public BigInteger countByDescricao(String descricao) {
        String sql = " select count(1) from origem_dados where ds_origem_dados = :descricao ";
        Query query = this.getEm().createNativeQuery(sql);
        query.setParameter("descricao", descricao);
        return DAOUtil.getSingleResult(query);
    }

}
