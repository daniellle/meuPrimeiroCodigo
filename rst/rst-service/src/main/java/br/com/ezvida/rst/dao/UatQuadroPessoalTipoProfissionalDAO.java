package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import fw.core.jpa.BaseDAO;

import java.util.List;

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
