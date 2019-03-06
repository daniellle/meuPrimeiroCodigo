package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalTipoServicoDAO extends BaseDAO<UatQuadroPessoalTipoServico, Long> {

    @Inject
    public UatQuadroPessoalTipoServicoDAO(EntityManager em) {
        super(em, UatQuadroPessoalTipoServico.class, "descricao");
    }

    public List<UatQuadroPessoalTipoServico> findByArea(Long idArea) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select s UatQuadroPessoalTipoServico s inner join fetch s.uatQuadroPessoalArea a");
        jpql.append(" where a.id = :idArea ");
        TypedQuery<UatQuadroPessoalTipoServico> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("idArea", idArea);
        return query.getResultList();
    }
}
