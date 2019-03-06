package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalTipoProfissionalDAO extends BaseDAO<UatQuadroPessoalTipoProfissional, Long> {

    @Inject
    public UatQuadroPessoalTipoProfissionalDAO(EntityManager em) {
        super(em, UatQuadroPessoalTipoProfissional.class, "descricao");
    }

    public List<UatQuadroPessoalTipoProfissional> findByTipoServico(Long idTipoServico) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select s UatQuadroPessoalTipoProfissional p inner join fetch p.uatQuadroPessoalTipoServico s ");
        jpql.append(" where s.id = :idTipoServico ");
        TypedQuery<UatQuadroPessoalTipoProfissional> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("idTipoServico", idTipoServico);
        return query.getResultList();
    }
}
