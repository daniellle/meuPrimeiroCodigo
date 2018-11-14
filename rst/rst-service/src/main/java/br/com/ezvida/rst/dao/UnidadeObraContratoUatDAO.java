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
        query.append(" select * from unidadeObraContratoUat")
            .append(" join und_obra o on und_obra_contrato_uat.id_und_obra_fk = o.id_und_obra ")
            .append(" join und_atd_trabalhador u on und_obra_contrato_uat.id_und_atd_trabalhador_fk = u.id_und_atd_trabalhador ")
            .append(" where o.id_empresa_fk = :idEmpresa ")
            .append(" order by und_obra_contrato_uat.fl_inativo ");
        TypedQuery<UnidadeObraContratoUat> typedQuery = criarConsultaPorTipo(query.toString());
        typedQuery.setParameter("idEmpresa", idEmpresa);

        return typedQuery.getResultList();
    }

    public ListaPaginada<UnidadeObraContratoUat> pesquisarPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter) {

            ListaPaginada<UnidadeObraContratoUat> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder jpql = new StringBuilder();
        //Map<String, Object> parametros = Maps.newHashMap();

        getQueryPaginado(jpql, unidadeObraContratoUatFilter, false);

        Query query = criarConsultaNativa(jpql.toString());

        query.setParameter("idEmpresa", 1038); //unidadeObraContratoUatFilter.getIdEmpresa());
        //DAOUtil.setParameterMap(query, parametros);

        listaPaginada.setQuantidade(getCountQueryPaginado(unidadeObraContratoUatFilter));

        if (unidadeObraContratoUatFilter != null) {
            query.setFirstResult((unidadeObraContratoUatFilter.getPagina() - 1) * unidadeObraContratoUatFilter.getQuantidadeRegistro());
            query.setMaxResults(unidadeObraContratoUatFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(query.getResultList());

        return listaPaginada;
    }

    private Long getCountQueryPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter) {
        //Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, unidadeObraContratoUatFilter, true);

        Query query = criarConsultaNativa(jpql.toString());
        query.setParameter("idEmpresa", 1038);//unidadeObraContratoUatFilter.getIdEmpresa());

        //DAOUtil.setParameterMap(query, parametros);

        return DAOUtil.getSingleResult(query);
    }

    private void getQueryPaginado(StringBuilder jpql, UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, boolean count) {

        if (count) {
            jpql.append(" select count(id_und_obra_contrato_uat) ");
            jpql.append("  from und_obra_contrato_uat ");
            jpql.append("  join und_obra o on und_obra_contrato_uat.id_und_obra_fk = o.id_und_obra ");
            jpql.append(" join und_atd_trabalhador u on und_obra_contrato_uat.id_und_atd_trabalhador_fk = u.id_und_atd_trabalhador ");
        } else {
            jpql.append(" select * ");
            jpql.append(" from und_obra_contrato_uat ");
            jpql.append(" join und_obra o on und_obra_contrato_uat.id_und_obra_fk = o.id_und_obra ");
            jpql.append(" join und_atd_trabalhador u on und_obra_contrato_uat.id_und_atd_trabalhador_fk = u.id_und_atd_trabalhador ");
        }


        jpql.append(" where o.id_empresa_fk = :idEmpresa ");

      /*  if (unidadeObraContratoUatFilter != null) {

            boolean codigo = StringUtils.isNotBlank(unidadeObraContratoUatFilter.getCodigo());
            boolean descricao = StringUtils.isNotBlank(funcaoFilter.getDescricao());
            boolean id = funcaoFilter.getIdEmpresa() != null;

            if (id) {
                jpql.append(" and empresa.id = :idEmpresa ");
                parametros.put("idEmpresa", funcaoFilter.getIdEmpresa());
            }

            if (descricao) {
                jpql.append(" and upper(funcao.descricao) like :descricao escape :sc ");
                parametros.put("sc", "\\");
                parametros.put("descricao", "%" + funcaoFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
            }

            if (codigo) {
                jpql.append(" and upper(funcao.codigo) like :codigo escape :sc ");
                funcaoFilter.setCodigo(funcaoFilter.getCodigo().replace("%", "\\%").toUpperCase());
                parametros.put("sc", "\\");
                parametros.put("codigo", "%" + funcaoFilter.getCodigo().concat("%").toUpperCase());
            }

            if (!count) {
                jpql.append(" order by funcao.descricao ");
            }
        }*/

    }

    public int ativarContrato(UnidadeObraContratoUat unidadeObraContratoUat){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("update ").
                append(getTipoClasse().getSimpleName()).
                append(" set flagInativo = :flagInativo, ")
                .append(" dataInativo = :dataInativo ");

        sqlBuilder.append(" where id = :id");

        Query query = criarConsulta(sqlBuilder.toString());

        query.setParameter("id", unidadeObraContratoUat.getId());
        query.setParameter("flagInativo", unidadeObraContratoUat.getFlagInativo() );
        query.setParameter("dataInativo", new Date() );

        return query.executeUpdate();
    }

    public int desativarContrato(UnidadeObraContratoUat unidadeObraContratoUat){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("update ").
                append(getTipoClasse().getSimpleName()).
                append(" set flagInativo = :flagInativo, ")
                .append(" dataInativo = :dataInativo ");

        sqlBuilder.append(" where id = :id");

        Query query = criarConsulta(sqlBuilder.toString());

        query.setParameter("id", unidadeObraContratoUat.getId());
        query.setParameter("flagInativo", null );
        query.setParameter("dataInativo", null );

        return query.executeUpdate();
    }


}
