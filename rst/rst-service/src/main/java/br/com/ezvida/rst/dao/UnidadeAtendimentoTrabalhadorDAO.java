package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EnderecoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidAtendTrabalhadorFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import com.google.common.collect.Maps;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UnidadeAtendimentoTrabalhadorDAO extends BaseDAO<UnidadeAtendimentoTrabalhador, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorDAO.class);

    @Inject
    public UnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
        super(em, UnidadeAtendimentoTrabalhador.class, "razaoSocial");
    }

    public List<UnidadeAtendimentoTrabalhador> pesquisarTodos() {
        LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador");
        StringBuilder jpql = new StringBuilder();
        jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
        jpql.append(" order by uat.razaoSocial ");
        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        return query.getResultList();
    }


    public List<UnidadeAtendimentoTrabalhador> buscarPorNome(String nome, Long dr){
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select uat from UnidadeAtendimentoTrabalhador uat ");
        jpql.append(" left join fetch uat.departamentoRegional dr");
        jpql.append(" where upper(uat.nomeFantasia) like :nome and dr.id = :dr");
        jpql.append(" order by uat.nomeFantasia");

        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("nome", "%" + nome.toUpperCase() + "%");
        query.setParameter("dr", dr);
        return query.getResultList();
    }

    public UnidadeAtendimentoTrabalhador pesquisarPorId(Long id, DadosFilter segurancaFilter) {
        LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador por Id");
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();

        jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
        montarJoinPesquisarPorId(segurancaFilter, jpql);

        jpql.append(" where uat.id = :id");

        if (segurancaFilter != null && !segurancaFilter.isAdministrador()) {
            montarFiltroPesquisarPorId(id, segurancaFilter, jpql, parametros);
        }

        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("id", id);
        DAOUtil.setParameterMap(query, parametros);
        return DAOUtil.getSingleResult(query);
    }

    public UnidadeAtendimentoTrabalhador pesquisarPorId(Long id){
        LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador por Id");
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select uat from UnidadeAtendimentoTrabalhador uat ");
        jpql.append(" where uat.id = :id ");
        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("id", id);
        return DAOUtil.getSingleResult(query);
    }

    private void montarFiltroPesquisarPorId(Long id, DadosFilter segurancaFilter, StringBuilder jpql,
                                            Map<String, Object> parametros) {
        if (segurancaFilter.temIdsEmpresa()) {
            jpql.append(" and empresa.id IN (:idsEmpresa) ");
            parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
        }

        if (segurancaFilter.temIdsDepRegional()) {
            if (id != null || segurancaFilter.temIdsEmpresa()) {
                jpql.append(" and ");
            }

            jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
            parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
        }

        if (segurancaFilter.temIdsUnidadeSESI()) {
            if (id != null || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
                jpql.append(" and ");
            }
            jpql.append(" uat.id IN (:idsUnidadeSESI) ");
            parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
        }

        if (segurancaFilter.temIdsTrabalhador()) {
            if (id != null || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsUnidadeSESI()) {
                jpql.append(" and ");
            }
            jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
            parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
        }
    }

    private void montarJoinPesquisarPorId(DadosFilter segurancaFilter, StringBuilder jpql) {
        jpql.append("left join fetch uat.departamentoRegional departamentoRegional ");

        if (segurancaFilter != null && !segurancaFilter.isAdministrador() && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
            jpql.append("left join uat.empresaUats empresaUat ");
            jpql.append("left join empresaUat.empresa empresa ");

            if (segurancaFilter.temIdsTrabalhador()) {
                jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
                jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
            }
        }
    }

    public ListaPaginada<UnidadeAtendimentoTrabalhador> pesquisarPaginado(
            UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, DadosFilter segurancaFilter) {

        LOGGER.debug("Pesquisando paginado Unidade de Atendimento ao Trabalhador por filtro");

        ListaPaginada<UnidadeAtendimentoTrabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();

        getQueryPaginado(jpql, parametros, unidAtendTrabalhadorFilter, false, segurancaFilter);

        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());

        DAOUtil.setParameterMap(query, parametros);

        listaPaginada.setQuantidade(getCountQueryPaginado(unidAtendTrabalhadorFilter, segurancaFilter));

        if (unidAtendTrabalhadorFilter != null) {
            query.setFirstResult(
                    (unidAtendTrabalhadorFilter.getPagina() - 1) * unidAtendTrabalhadorFilter.getQuantidadeRegistro());
            query.setMaxResults(unidAtendTrabalhadorFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(query.getResultList());

        return listaPaginada;
    }

    private long getCountQueryPaginado(UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter,
                                       DadosFilter segurancaFilter) {
        Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();
        getQueryPaginado(jpql, parametros, unidAtendTrabalhadorFilter, true, segurancaFilter);
        Query query = criarConsulta(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        return DAOUtil.getSingleResult(query);
    }

    private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                  UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, boolean count, DadosFilter segurancaFilter) {

        boolean cnpj = false;
        boolean razaoSocial = false;
        boolean depRegional = false;
        boolean status = false;

        if (unidAtendTrabalhadorFilter != null) {
            cnpj = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getCnpj());
            razaoSocial = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getRazaoSocial());
            depRegional = unidAtendTrabalhadorFilter.getIdDepRegional() != null
                    && unidAtendTrabalhadorFilter.getIdDepRegional().intValue() > 0;
            status = StringUtils.isNotBlank(unidAtendTrabalhadorFilter.getStatusCat());
        }


        montarJoinPaginado(jpql, count, segurancaFilter, depRegional);

        if (cnpj || razaoSocial || depRegional || status) {
            jpql.append(" where ");
            montarFiltroPaginado(jpql, parametros, unidAtendTrabalhadorFilter, cnpj, razaoSocial, depRegional);
            addAnd(jpql, cnpj, razaoSocial, depRegional, status);
            montarFiltroSituacaoPaginado(jpql, unidAtendTrabalhadorFilter);
        }

        addFiltroIds(jpql, parametros, segurancaFilter, cnpj, razaoSocial, depRegional, status);

        if (!count) {
            jpql.append(" order by uat.razaoSocial ");
        }
    }

    private void addFiltroIds(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
                              boolean cnpj, boolean razaoSocial, boolean depRegional, boolean status) {
        if (segurancaFilter != null && !segurancaFilter.isAdministrador()) {
            if (cnpj || razaoSocial || depRegional || status
                    && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsUnidadeSESI()) {
                //jpql.append(" and ");
                montarFiltroIdsPaginado(jpql, parametros, segurancaFilter);
            }
        }
    }

    private void montarFiltroIdsPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                         DadosFilter segurancaFilter) {
        if (segurancaFilter.temIdsEmpresa()) {
            //testar se " empresaUat.empresa.id IN (:idsEmpresa) " funciona
            jpql.append(" and ");
            jpql.append(" empresa.id IN (:idsEmpresa) ");
            parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
        }

        if (segurancaFilter.temIdsEmpresa() && segurancaFilter.temIdsDepRegional()) {
            jpql.append(" and ");
        }

        if (segurancaFilter.temIdsDepRegional()) {
            jpql.append(" uat.departamentoRegional.id IN (:idsDepRegional) ");
            parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
        }

        if (segurancaFilter.temIdsUnidadeSESI()) {
            if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
                jpql.append(" and ");
            }

            jpql.append(" uat.id IN (:idsUnidadeSESI) ");
            parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
        }

        if (segurancaFilter.temIdsTrabalhador()) {

            if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsUnidadeSESI()) {
                jpql.append(" and ");
            }

            jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
            parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
        }
    }

    private void montarFiltroSituacaoPaginado(StringBuilder jpql,
                                              UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter) {
        if (Situacao.ATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
            jpql.append(" uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
        } else if (Situacao.INATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
            jpql.append(" uat.dataDesativacao ").append(Situacao.INATIVO.getQuery());
        }
    }

    private void addAnd(StringBuilder jpql, boolean cnpj, boolean razaoSocial, boolean depRegional, boolean status) {
        if (status) {
            if (cnpj || razaoSocial || depRegional) {
                jpql.append(" and");
            }
        }
    }

    private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                      UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, boolean cnpj, boolean razaoSocial,
                                      boolean depRegional) {
        if (cnpj) {
            jpql.append(" uat.cnpj like :cnpj escape :sc ");
            parametros.put("sc", "\\");
            parametros.put("cnpj", "%" + unidAtendTrabalhadorFilter.getCnpj().replace("%", "\\%") + "%");
        }

        if (razaoSocial) {
            if (cnpj) {
                jpql.append(" and");
            }
            //jpql.append(" UPPER(uat.razaoSocial) like :razaoSocial escape :sc");
            //tem q ver com Dan pq isso não está funcionando...
            jpql.append(" set_simple_name(UPPER(uat.razaoSocial)) like set_simple_name(:razaoSocial) ");
            parametros.put("razaoSocial", "%" + unidAtendTrabalhadorFilter.getRazaoSocial().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
        }

        if (depRegional) {
            if (cnpj || razaoSocial) {
                jpql.append(" and");
            }
            jpql.append(" uat.departamentoRegional.id = :idDepRegional");
            parametros.put("idDepRegional", unidAtendTrabalhadorFilter.getIdDepRegional());
        }
    }

    private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter, boolean depRegional) {
        if (count) {
            jpql.append("select count(distinct uat.id) from UnidadeAtendimentoTrabalhador uat ");
            //jpql.append("left join uat.empresaUats empresaUat ");
            jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
        } else {
            jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
            jpql.append("left join fetch uat.endereco endUat ");
            //jpql.append("left join uat.empresaUats empresaUat ");
            //jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
            jpql.append("left join fetch endUat.endereco endereco ");
            jpql.append("left join fetch endereco.municipio municipio ");
        }

        if (depRegional) {
            //ver se é necessário, já que ele pesquisa direto em uat.departamentoRegional.id
            jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
        }

        if (segurancaFilter != null) {

            if (!segurancaFilter.isAdministrador()) {

                if (segurancaFilter.temIdsEmpresa()) {
                    jpql.append("left join uat.empresaUats empresaUat ");
                    jpql.append("left join empresaUat.empresa empresa ");
                }

                if (segurancaFilter.temIdsTrabalhador()) {
                    jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
                    jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
                }

                if (segurancaFilter.temIdsDepRegional()) {
                    jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
                }
            }
        }
    }

    public UnidadeAtendimentoTrabalhador pesquisarPorCNPJ(String cnpj) {
        LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador por CNPJ");
        StringBuilder jpql = new StringBuilder();
        jpql.append("select uat from UnidadeAtendimentoTrabalhador uat where uat.cnpj = :cnpj");
        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("cnpj", cnpj);
        return DAOUtil.getSingleResult(query);
    }

    public List<UnidadeAtendimentoTrabalhador> pesquisarPorDepartamento(Long departamentoId) {
        LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador Ativas por Departamento");
        StringBuilder jpql = new StringBuilder();
        jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
        jpql.append("left join fetch uat.departamentoRegional departamentoRegional ");
        jpql.append("where uat.departamentoRegional.id = :departamentoRegional ");
        jpql.append("and uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("departamentoRegional", departamentoId);
        return query.getResultList();
    }

    public List<UnidadeAtendimentoTrabalhador> pesquisarPorEndereco(EnderecoFilter enderecoFilter, DadosFilter segurancaFilter) {
        LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador por Id");
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();

        boolean idEstado = false;
        boolean idMunicipio = false;
        boolean bairro = false;

        montarJoinPesquisarPorEndereco(segurancaFilter, jpql);

        if (enderecoFilter != null) {
            jpql.append(" where ");

            idEstado = enderecoFilter.getIdEstado() != null
                    && enderecoFilter.getIdEstado().intValue() > 0;
            idMunicipio = enderecoFilter.getIdMunicipio() != null
                    && enderecoFilter.getIdMunicipio().intValue() > 0;
            bairro = StringUtils.isNotEmpty(enderecoFilter.getBairro());

            montarFiltroPesquisarPorEndereco(enderecoFilter, jpql, parametros, idEstado, idMunicipio, bairro);

        }

        if("0".equals(enderecoFilter.getFiltrarDepRegEmp())){
            addFiltroDepRegEmpPesquisarPorEndereco(segurancaFilter, jpql, parametros, idEstado, idMunicipio, bairro);
        }

        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        return query.getResultList();
    }

    private void addFiltroDepRegEmpPesquisarPorEndereco(DadosFilter segurancaFilter, StringBuilder jpql,
                                                        Map<String, Object> parametros, boolean idEstado, boolean idMunicipio, boolean bairro) {
        if (segurancaFilter != null) {
            if ((idEstado || idMunicipio || bairro)
                    && (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {

                montarFiltroIdsPaginado(jpql, parametros, segurancaFilter);
            }
        }
    }

    private void montarFiltroPesquisarPorEndereco(EnderecoFilter enderecoFilter, StringBuilder jpql,
                                                  Map<String, Object> parametros, boolean idEstado, boolean idMunicipio, boolean bairro) {
        if (idEstado) {
            jpql.append(" estado.id = :idEstado");
            parametros.put("idEstado", enderecoFilter.getIdEstado());
        }

        if (idMunicipio) {
            if (idEstado) {
                jpql.append(" and");
            }
            jpql.append(" municipio.id = :idMunicipio");
            parametros.put("idMunicipio", enderecoFilter.getIdMunicipio());
        }

        if (bairro) {
            if (idEstado || idMunicipio) {
                jpql.append(" and");
            }
            jpql.append(" UPPER(endereco.bairro) like :bairro escape :sc");
            parametros.put("sc", "\\");
            parametros.put("bairro", "%" + enderecoFilter.getBairro().replace("%", "\\%").toUpperCase() + "%");
        }
    }

    private void montarJoinPesquisarPorEndereco(DadosFilter segurancaFilter, StringBuilder jpql) {
        jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
        jpql.append("left join fetch uat.endereco endUat ");
        jpql.append("left join uat.empresaUats empresaUat ");
        jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
        jpql.append("left join fetch endUat.endereco endereco ");
        jpql.append("left join fetch endereco.municipio municipio ");
        jpql.append("left join fetch municipio.estado estado ");

        if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
            jpql.append("left join uat.empresaUats empresaUat ");
            jpql.append("left join empresaUat.empresa empresa ");

            if (segurancaFilter.temIdsTrabalhador()) {
                jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
                jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
            }
        }
    }

    public List<UnidadeAtendimentoTrabalhador> buscarPorEmpresaEAtivas(Long id) {
        LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador Ativas por Empresa");
        StringBuilder jpql = new StringBuilder();
        jpql.append("select new UnidadeAtendimentoTrabalhador (uat.cnpj, uat.razaoSocial, ");
        jpql.append("  departamentoRegionalUat.cnpj, departamentoRegionalUat.razaoSocial, departamentoRegionalUat.siglaDR, estado.siglaUF) ");
        jpql.append(" from EmpresaUnidadeAtendimentoTrabalhador empresaUnidadeAtendimentoTrabalhador ");
        jpql.append("	left join empresaUnidadeAtendimentoTrabalhador.unidadeAtendimentoTrabalhador uat ");
        jpql.append("	left join uat.departamentoRegional departamentoRegionalUat ");
        jpql.append("	left join departamentoRegionalUat.listaEndDepRegional enderecos ");
        jpql.append("	left join enderecos.endereco endereco ");
        jpql.append("	left join endereco.municipio municipio ");
        jpql.append("	left join municipio.estado estado ");
        jpql.append("	where empresaUnidadeAtendimentoTrabalhador.empresa.id = :id ");
        jpql.append("		and empresaUnidadeAtendimentoTrabalhador.dataExclusao is null ");

        TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("id", id);
        return query.getResultList();
    }

    public BigInteger countByListIdAndCNPJEmpresa(Collection<Long> listId, String CNPJ) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("	count(1) ");
        sql.append("from ");
        sql.append("	und_atd_trabalhador unidade ");
        sql.append("inner join empresa_uat uat on ");
        sql.append("	uat.id_und_atd_trabalhador_fk = unidade.id_und_atd_trabalhador ");
        sql.append("inner join empresa on ");
        sql.append("	uat.id_empresa_fk = empresa.id_empresa ");
        sql.append("where ");
        sql.append("	empresa.no_cnpj = :cnpj ");
        if (listId != null && !listId.isEmpty()) {
            sql.append("	and unidade.id_und_atd_trabalhador in (:listId) ");
        }

        Query query = this.getEm().createNativeQuery(sql.toString());
        query.setParameter("cnpj", CNPJ);
        if (listId != null && !listId.isEmpty()) {
            query.setParameter("listId", listId);
        }
        return  DAOUtil.getSingleResult(query);
    }
}