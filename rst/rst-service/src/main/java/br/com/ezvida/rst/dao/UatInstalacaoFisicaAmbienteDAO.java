package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import fw.core.jpa.BaseDAO;

import java.util.List;

public class UatInstalacaoFisicaAmbienteDAO extends BaseDAO<UatInstalacaoFisicaAmbiente, Long> {

    @Inject
    public UatInstalacaoFisicaAmbienteDAO(EntityManager em) {
        super(em, UatInstalacaoFisicaAmbiente.class, "descricao");
    }

    public List<UatInstalacaoFisicaAmbiente> findByCategoria(Long idCategoria) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select uifa from UatInstalacaoFisicaAmbiente uifa join fetch ");
        jpql.append(" uifa.instalacaoFisicaCategoria cat  ");
        jpql.append(" where cat.id = :idCategoria ");
        TypedQuery<UatInstalacaoFisicaAmbiente> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("idCategoria", idCategoria);
        return query.getResultList();
    }
}
