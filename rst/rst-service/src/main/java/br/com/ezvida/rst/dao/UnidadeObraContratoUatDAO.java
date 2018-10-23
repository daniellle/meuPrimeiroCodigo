package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.model.UnidadeObra;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class UnidadeObraContratoUatDAO extends BaseRstDAO<UnidadeObraContratoUat, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraContratoUatDAO.class);

    @Inject
    public UnidadeObraContratoUatDAO(EntityManager em) {
        super(em, UnidadeObraContratoUat.class, "dataContratoInicio");
    }

    public List<UnidadeObraContratoUat> validar(String cnpj){
        LOGGER.debug("Validando unidade obra ativos pelo código da empresa...");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" select unidadeObraContratoUat from UnidadeObraContratoUat unidadeObraContratoUat");
        sqlBuilder.append(" inner join fetch unidadeObraContratoUat.unidadeObra unidadeObra");
        sqlBuilder.append(" inner join fetch unidadeObra.empresa empresa ");
        sqlBuilder.append(" where empresa.cnpj = :cnpj ");
        sqlBuilder.append(" and unidadeObraContratoUat.dataContratoInicio is not null and unidadeObraContratoUat.dataContratoInicio <= :dataHoje ");
        sqlBuilder.append(" and unidadeObraContratoUat.dataContratoFim is not null and unidadeObraContratoUat.dataContratoFim > :dataHoje ");
        sqlBuilder.append(" and unidadeObraContratoUat.flagInativo = :flagInativo");

        TypedQuery<UnidadeObraContratoUat> query = criarConsultaPorTipo(sqlBuilder.toString());
        query.setParameter("cnpj", cnpj);
        query.setParameter("dataHoje", new Date(), TemporalType.DATE);
        query.setParameter("flagInativo", "N".charAt(0) );

        return query.getResultList();
    }
}
