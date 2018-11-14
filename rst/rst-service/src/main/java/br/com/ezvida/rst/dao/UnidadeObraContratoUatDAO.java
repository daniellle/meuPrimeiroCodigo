package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidadeObraContratoUatFilter;
import br.com.ezvida.rst.model.UnidadeObra;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import com.google.common.collect.Maps;
import fw.core.jpa.DAOUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public List<UnidadeObraContratoUat> listarTodos(Long idEmpresa){
        LOGGER.debug("Listando todos os contratos");

        StringBuilder query = new StringBuilder();
        query.append(" select unidadeObraContratoUat from UnidadeObraContratoUat unidadeObraContratoUat")
//              .append(" inner join fetch unidadeObraContratoUat unidadeObra")
//              .append(" inner join fetch unidadeObra.empresa empresa ")
//            .append(" join und_obra o on und_obra_contrato_uat.id_und_obra_fk = o.id_und_obra ")
//            .append(" join und_atd_trabalhador u on und_obra_contrato_uat.id_und_atd_trabalhador_fk = u.id_und_atd_trabalhador ")
            .append(" where empresa.id = :idEmpresa ")
            .append(" order by unidadeObraContratoUat.flagInativo ");
        TypedQuery<UnidadeObraContratoUat> typedQuery = criarConsultaPorTipo(query.toString());
//        typedQuery.setParameter("idEmpresa", idEmpresa);

        return typedQuery.getResultList();
    }

    public ListaPaginada<UnidadeObraContratoUat> pesquisarPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, Long empresaId) {

            ListaPaginada<UnidadeObraContratoUat> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, unidadeObraContratoUatFilter, false);

        Query query = criarConsulta(jpql.toString());

        query.setParameter("idEmpresa", empresaId);

        listaPaginada.setQuantidade(getCountQueryPaginado(unidadeObraContratoUatFilter, empresaId));

        if (unidadeObraContratoUatFilter != null) {
            query.setFirstResult((unidadeObraContratoUatFilter.getPagina() - 1) * unidadeObraContratoUatFilter.getQuantidadeRegistro());
            query.setMaxResults(unidadeObraContratoUatFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(query.getResultList());

        return listaPaginada;
    }

    private Long getCountQueryPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, Long empresaId) {
        //Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, unidadeObraContratoUatFilter, true);

        Query query = criarConsulta(jpql.toString());
//        query.setParameter("idEmpresa", empresaId);//unidadeObraContratoUatFilter.getIdEmpresa());

        //DAOUtil.setParameterMap(query, parametros);

        return DAOUtil.getSingleResult(query);
    }

    private void getQueryPaginado(StringBuilder jpql, UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, boolean count) {

        if (count) {
            jpql.append(" select count(unidadeObraContratoUat) ");
        } else {
            jpql.append(" select unidadeObraContratoUat ");
        }
        jpql.append(" from UnidadeObraContratoUat unidadeObraContratoUat");

        jpql.append(" left join fetch unidadeObraContratoUat.unidadeObra unidadeObra");
        jpql.append(" left join fetch unidadeObra.empresa empresa ");

        jpql.append(" where empresa.id = :idEmpresa ");

    }

}
