package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SindicatoFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Sindicato;
import br.com.ezvida.rst.utils.CollectionUtil;
import com.google.common.collect.Maps;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SindicatoDAO extends BaseDAO<Sindicato, Long> {

    private static final String LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA = "left join empresaSindicato.empresa empresa ";
    private static final String LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO = "left join c.empresaSindicato empresaSindicato ";
    private static final String SELECT_DISTINCT_C_FROM_SINDICATO_C = "select distinct c from Sindicato c ";
    private static final String AND = " and ";
    private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoDAO.class);

    @Inject
    public SindicatoDAO(EntityManager em) {
        super(em, Sindicato.class, "razaoSocial");
    }

    public Sindicato pesquisarPorId(Long id, DadosFilter segurancaFilter) {
        LOGGER.debug("Pesquisando todos os Sindicatos por Id");
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
        jpql.append(LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO);
        jpql.append(LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA);
        jpql.append(" left join empresa.empresaUats empresaUats ");
        jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
        jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
        jpql.append(" where c.id = :id");

        parametros.put("id", id);
        if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
            jpql.append(" and depRegional.id IN (:idsDepRegional) ");
            parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
        }

        if (segurancaFilter.temIdsUnidadeSESI() && !segurancaFilter.isAdministrador()) {
            jpql.append(" unidadeAtendimentoTrabalhador.id IN (:idsUnidadeSESI) ");
            parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
        }

        TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        return DAOUtil.getSingleResult(query);
    }

    public ListaPaginada<Sindicato> pesquisarPaginado(SindicatoFilter sindicatoFilter, DadosFilter segurancaFilter) {
        LOGGER.debug("Pesquisando paginado Sindicatos por filtro");

        ListaPaginada<Sindicato> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder jpql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginado(jpql, parametros, sindicatoFilter, false, segurancaFilter);
        TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        listaPaginada.setQuantidade(getCountQueryPaginado(sindicatoFilter, segurancaFilter));

        query.setFirstResult((sindicatoFilter.getPagina() - 1) * sindicatoFilter.getQuantidadeRegistro());
        query.setMaxResults(sindicatoFilter.getQuantidadeRegistro());

        listaPaginada.setList(query.getResultList());

        return listaPaginada;
    }

    public long getCountQueryPaginado(SindicatoFilter sindicatoFilter, DadosFilter segurancaFilter) {
        Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();
        getQueryPaginado(jpql, parametros, sindicatoFilter, true, segurancaFilter);
        Query query = criarConsulta(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        return DAOUtil.getSingleResult(query);
    }


    public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                 SindicatoFilter sindicatoFilter, boolean count, DadosFilter segurancaFilter) {

        boolean cnpj = false;
        boolean razaoSocial = false;
        boolean nomeFantasia = false;
        boolean situacao = false;
        boolean ids = false;

        montarJoinPaginado(jpql, count, segurancaFilter);

        if (sindicatoFilter != null) {

            cnpj = StringUtils.isNotEmpty(sindicatoFilter.getCnpj());
            razaoSocial = StringUtils.isNotEmpty(sindicatoFilter.getRazaoSocial());
            nomeFantasia = StringUtils.isNotEmpty(sindicatoFilter.getNomeFantasia());
            situacao = StringUtils.isNotBlank(sindicatoFilter.getSituacao());
            ids = StringUtils.isNotBlank(sindicatoFilter.getIds());

            montarJoinDepReg(jpql, segurancaFilter);

            addWhere(jpql, cnpj, razaoSocial, nomeFantasia, situacao, ids);

            montarFiltroPaginado(jpql, parametros, sindicatoFilter, cnpj, razaoSocial, nomeFantasia);
            montarFiltroSituacaoPaginado(jpql, sindicatoFilter, cnpj, razaoSocial, nomeFantasia, situacao);
            montarFiltroIds(jpql, parametros, sindicatoFilter, cnpj, razaoSocial, nomeFantasia, situacao, ids);
        }

        if (segurancaFilter != null && sindicatoFilter != null && sindicatoFilter.isAplicarDadosFilter()) {
            addFiltroDepRegional(jpql, parametros, segurancaFilter, cnpj, razaoSocial, nomeFantasia, situacao, ids);
        }

        if (!count) {
            jpql.append(" order by c.razaoSocial ");
        }

    }

    private void addFiltroDepRegional(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
                                      boolean cnpj, boolean razaoSocial, boolean nomeFantasia, boolean situacao, boolean ids) {
        boolean hasFilters = cnpj || razaoSocial || nomeFantasia || situacao || ids;

        if (hasFilters && !segurancaFilter.isAdministrador()) {
            if (segurancaFilter.temIdsDepRegional()) {
                jpql.append(AND);
                jpql.append(" depRegional.id IN (:idsDepRegional) ");
                parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
            }

            if(segurancaFilter.temIdsUnidadeSESI()){
                jpql.append(AND);
                jpql.append(" unidadeAtendimentoTrabalhador.id IN (:idsUnidadeSESI) ");
                parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
            }
        }
    }

    private void montarFiltroIds(StringBuilder jpql, Map<String, Object> parametros, SindicatoFilter sindicatoFilter,
                                 boolean cnpj, boolean razaoSocial, boolean nomeFantasia, boolean situacao, boolean ids) {
        if (ids) {
            if (cnpj || razaoSocial || nomeFantasia || situacao) {
                jpql.append(AND);
            }

            jpql.append(" c.id not in (:ids) ");
            parametros.put("ids", CollectionUtil.getIds(sindicatoFilter.getIds()));
        }
    }

    private void montarFiltroSituacaoPaginado(StringBuilder jpql, SindicatoFilter sindicatoFilter, boolean cnpj,
                                              boolean razaoSocial, boolean nomeFantasia, boolean situacao) {
        if (situacao) {
            if (cnpj || razaoSocial || nomeFantasia) {
                jpql.append(AND);
            }
            if (Situacao.ATIVO.getCodigo().equals(sindicatoFilter.getSituacao())) {
                jpql.append(" c.dataDesativacao ").append(Situacao.ATIVO.getQuery());
            } else if (Situacao.INATIVO.getCodigo().equals(sindicatoFilter.getSituacao())) {
                jpql.append(" c.dataDesativacao ").append(Situacao.INATIVO.getQuery());
            }
        }
    }

    private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                      SindicatoFilter sindicatoFilter, boolean cnpj, boolean razaoSocial, boolean nomeFantasia) {
        if (cnpj) {
            jpql.append(" c.cnpj = :cnpj ");
            parametros.put("cnpj", sindicatoFilter.getCnpj());
        }
        if (razaoSocial) {
            if (cnpj) {
                jpql.append(AND);
            }
            jpql.append(" set_simple_name(UPPER(c.razaoSocial)) like set_simple_name(:razaoSocial) escape :sc ");
            parametros.put("sc", "\\");
            parametros.put("razaoSocial", "%" + sindicatoFilter.getRazaoSocial().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");

        }
        if (nomeFantasia) {
            if (cnpj || razaoSocial) {
                jpql.append(AND);
            }
            jpql.append(" set_simple_name(UPPER(c.nomeFantasia)) like set_simple_name(:nomeFantasia) escape :sc ");
            parametros.put("sc", "\\");
            parametros.put("nomeFantasia", "%" + sindicatoFilter.getNomeFantasia().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
        }
    }

    private void addWhere(StringBuilder jpql, boolean cnpj, boolean razaoSocial, boolean nomeFantasia, boolean situacao,
                          boolean ids) {
        if (cnpj || razaoSocial || nomeFantasia || situacao || ids) {
            jpql.append(" where ");
        }
    }

    private void montarJoinDepReg(StringBuilder jpql, DadosFilter segurancaFilter) {
        if (segurancaFilter != null && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsUnidadeSESI() && !segurancaFilter.isAdministrador()) {
            jpql.append(" left join empresa.empresaUats empresaUats ");
            jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
            jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
        }
    }

    private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
        if (count) {
            jpql.append("select count(distinct c.id) from Sindicato c ");
        } else {
            jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
        }

        if (segurancaFilter != null && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsUnidadeSESI() && !segurancaFilter.isAdministrador()) {
            jpql.append(LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO);
            jpql.append(LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA);
        }
    }


    public List<Sindicato> listarTodos() {
        LOGGER.debug("Listando todos os sindicatos");

        StringBuilder jpql = new StringBuilder();
        montarSelecetComJoins(jpql);
        jpql.append(" order by c.razaoSocial ");
        TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());

        return query.getResultList();
    }

    private void montarSelecetComJoins(StringBuilder jpql) {
        jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
        jpql.append("left join fetch c.endereco endSind ");
        jpql.append("left join fetch endSind.endereco e ");
        jpql.append("left join fetch e.municipio municipio ");
        jpql.append("left join fetch municipio.estado estado ");
        jpql.append("left join fetch c.email emSind ");
        jpql.append("left join fetch emSind.email email ");
        jpql.append("left join fetch c.telefone telSind ");
        jpql.append("left join fetch telSind.telefone telefone ");
    }

    public Sindicato pesquisarPorCNPJ(String cnpj) {
        LOGGER.debug("Pesquisando Sindicato por CNPJ");
        StringBuilder jpql = new StringBuilder();
        jpql.append("select c from Sindicato c where c.cnpj = :cnpj");
        TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("cnpj", cnpj);
        return DAOUtil.getSingleResult(query);
    }

    public List<Sindicato> buscarPorEmpresaEAtivos(Long id) {
        LOGGER.debug("Buscando sindicatos ativos por empresa");

        StringBuilder jpql = new StringBuilder();
        jpql.append("select new Sindicato(sindicato.cnpj, sindicato.razaoSocial, sindicato.nomeFantasia) ");
        jpql.append(" from EmpresaSindicato empresaSindicato ");
        jpql.append(" inner join empresaSindicato.sindicato sindicato ");
        jpql.append(" where empresaSindicato.empresa.id = :id ");
        jpql.append(" 	and empresaSindicato.dataDesligamento is null and empresaSindicato.dataExclusao is null ");

        TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("id", id);

        return query.getResultList();
    }
}
