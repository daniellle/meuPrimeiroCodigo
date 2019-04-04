package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidadeObraContratoUatFilter;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import fw.core.jpa.DAOUtil;

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

    @SuppressWarnings("unchecked")
	public ListaPaginada<UnidadeObraContratoUat> pesquisarPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, Long empresaId) {

        Long quantidade = getCountQueryPaginado(unidadeObraContratoUatFilter, empresaId);

        ListaPaginada<UnidadeObraContratoUat> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, unidadeObraContratoUatFilter, false);

        Query query = criarConsulta(jpql.toString());

        query.setParameter("idEmpresa", empresaId);

        if(!unidadeObraContratoUatFilter.getDrs().isEmpty()){
            query.setParameter("drs", unidadeObraContratoUatFilter.getDrs());

        }
        if(!unidadeObraContratoUatFilter.getUats().isEmpty()){
            query.setParameter("uats", unidadeObraContratoUatFilter.getUats());
        }

        listaPaginada.setQuantidade(quantidade);

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
        query.setParameter("idEmpresa", empresaId);//unidadeObraContratoUatFilter.getIdEmpresa());
        if(!unidadeObraContratoUatFilter.getDrs().isEmpty()){
            query.setParameter("drs", unidadeObraContratoUatFilter.getDrs());

        }
        if(!unidadeObraContratoUatFilter.getUats().isEmpty()){
            query.setParameter("uats", unidadeObraContratoUatFilter.getUats());
        }


        //DAOUtil.setParameterMap(query, parametros);

        return DAOUtil.getSingleResult(query);
    }

    private void getQueryPaginado(StringBuilder jpql, UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, boolean count) {

        if (count) {
            jpql.append(" select count(unidadeObraContratoUat) ");

            jpql.append(" from UnidadeObraContratoUat unidadeObraContratoUat");
            jpql.append(" join unidadeObraContratoUat.unidadeObra unidadeObra");
            jpql.append("  join unidadeObraContratoUat.tipoPrograma tipoPrograma");
            jpql.append("  join unidadeObraContratoUat.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
            jpql.append(" join unidadeAtendimentoTrabalhador.departamentoRegional departamentoRegional");
            jpql.append(" join unidadeObra.empresa empresa ");


        } else {
            jpql.append(" select unidadeObraContratoUat ");

            jpql.append(" from UnidadeObraContratoUat unidadeObraContratoUat");

            jpql.append(" left join fetch unidadeObraContratoUat.unidadeObra unidadeObra");
            jpql.append(" left join fetch unidadeObraContratoUat.tipoPrograma tipoPrograma");
            jpql.append(" left join fetch unidadeObraContratoUat.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
            jpql.append(" left join fetch unidadeAtendimentoTrabalhador.departamentoRegional departamentoRegional");
            jpql.append(" left join fetch unidadeObra.empresa empresa ");
        }

        jpql.append(" where empresa.id = :idEmpresa ");

        if(!unidadeObraContratoUatFilter.getDrs().isEmpty()){
            jpql.append(" and departamentoRegional.id in :drs");
        }

        if(!unidadeObraContratoUatFilter.getUats().isEmpty()){
            jpql.append(" and unidadeAtendimentoTrabalhador.id in :uats");
        }


        if(!count){
            jpql.append(" order by case when unidadeObraContratoUat.flagInativo is null then 0  else 1  end, unidadeObraContratoUat.dataContratoInicio desc");
        }
    }

    public Boolean existsByDRs(List<Long> listDRs, Long idContrato) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("	exists( ");
        sql.append("	select ");
        sql.append("		c.id_und_obra_contrato_uat ");
        sql.append("	from ");
        sql.append("		und_obra_contrato_uat c ");
        sql.append("	inner join und_atd_trabalhador uat on ");
        sql.append("		c.id_und_atd_trabalhador_fk = uat.id_und_atd_trabalhador ");
        sql.append("	where c.id_und_obra_contrato_uat = :idContrato ");
        sql.append("		and uat.id_departamento_regional_fk in (:drs)) ");
        Query query = this.getEm().createNativeQuery(sql.toString());
        query.setParameter("drs", listDRs);
        query.setParameter("idContrato", idContrato);
        return (Boolean) query.getSingleResult();
    }

    public Boolean existsByUnidade(List<Long> listUnidades, Long idContrato) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("	exists( ");
        sql.append("	select ");
        sql.append("		c.id_und_obra_contrato_uat ");
        sql.append("	from ");
        sql.append("		und_obra_contrato_uat c ");
        sql.append("	where c.id_und_obra_contrato_uat = :idContrato ");
        sql.append("		and c.id_und_atd_trabalhador_fk in (:unidades)) ");
        Query query = this.getEm().createNativeQuery(sql.toString());
        query.setParameter("unidades", listUnidades);
        query.setParameter("idContrato", idContrato);
        return (Boolean) query.getSingleResult();
    }

}
