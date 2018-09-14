package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProfissionalFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Profissional;
import com.google.common.collect.Maps;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfissionalDAO extends BaseDAO<Profissional, Long> {

    @Inject
    public ProfissionalDAO(EntityManager em) {
        super(em, Profissional.class, "nome");
    }

    public Profissional pesquisarPorId(Long id) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select profissional from Profissional profissional ");
        jpql.append(" left join fetch profissional.estado estado ");
        jpql.append(" left join fetch profissional.conselhoRegional conselhoRegional ");
        jpql.append(" where profissional.id = :id");
        TypedQuery<Profissional> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("id", id);

        return DAOUtil.getSingleResult(query);
    }

    public List<Profissional> listarTodos() {
        StringBuilder jpql = new StringBuilder();
        montarSelecetComJoins(jpql);
        TypedQuery<Profissional> query = criarConsultaPorTipo(jpql.toString());

        return query.getResultList();
    }

    public ListaPaginada<Profissional> pesquisarPaginado(ProfissionalFilter profissionalFilter,
                                                         DadosFilter segurancaFilter) {
        ListaPaginada<Profissional> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, parametros, profissionalFilter, false, segurancaFilter);

        TypedQuery<Profissional> query = criarConsultaPorTipo(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);

        listaPaginada.setQuantidade(getCountQueryPaginado(profissionalFilter, segurancaFilter));

        if (profissionalFilter != null) {
            query.setFirstResult((profissionalFilter.getPagina() - 1) * profissionalFilter.getQuantidadeRegistro());
            query.setMaxResults(profissionalFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(query.getResultList());
        return listaPaginada;

    }

    public long getCountQueryPaginado(ProfissionalFilter profissionalFilter, DadosFilter segurancaFilter) {
        Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();

        getQueryPaginado(jpql, parametros, profissionalFilter, true, segurancaFilter);

        Query query = criarConsulta(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);

        return DAOUtil.getSingleResult(query);
    }

    public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                 ProfissionalFilter profissionalFilter, boolean count, DadosFilter segurancaFilter) {

        boolean cpf = false;
        boolean registro = false;
        boolean nome = false;
        boolean status = false;
        boolean estado = false;

        montarJoinPaginado(jpql, count, segurancaFilter);

        if (profissionalFilter != null) {

            cpf = StringUtils.isNotBlank(profissionalFilter.getCpf());
            registro = StringUtils.isNotBlank(profissionalFilter.getRegistro());
            nome = StringUtils.isNotBlank(profissionalFilter.getNome());
            status = StringUtils.isNotBlank(profissionalFilter.getStatusProfissional());
            estado = profissionalFilter.getIdEstado() != null && profissionalFilter.getIdEstado().intValue() > 0;

            if (cpf || registro || nome || status || estado) {
                jpql.append(" where ");
            }

            montarFiltroPaginado(jpql, parametros, profissionalFilter, cpf, registro, nome, estado);
            montarFiltroStatusPaginado(jpql, profissionalFilter, cpf, registro, nome, status, estado);
        }

        montarFiltroDepRegional(jpql, parametros, segurancaFilter, cpf, registro, nome, status, estado);

        if (!count) {
            jpql.append(" order by p.nome");
        }
    }

    private void montarFiltroDepRegional(StringBuilder jpql, Map<String, Object> parametros,
                                         DadosFilter segurancaFilter, boolean cpf, boolean registro, boolean nome, boolean status, boolean estado) {
        boolean aplicado = segurancaFilter != null && !segurancaFilter.isAdministrador() && (cpf || registro || nome || status || estado);
        boolean filtroIdDep = false;
        boolean filtroUnid = false;
        if (aplicado) {
            filtroIdDep = segurancaFilter.temIdsDepRegional();
            filtroUnid = segurancaFilter.temIdsUnidadeSESI();
        }
        if (filtroIdDep) {
            jpql.append(" and ");
            jpql.append("  uat.departamentoRegional.id IN (:idsDepRegional) ");
            parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
        }

        if (filtroUnid) {
            jpql.append(" and ");
            jpql.append(" uat.id IN (:idsUnidadeSESI) ");
            parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
        }
    }

    private void montarFiltroStatusPaginado(StringBuilder jpql, ProfissionalFilter profissionalFilter, boolean cpf,
                                            boolean registro, boolean nome, boolean status, boolean estado) {
        if (status) {
            if (cpf || registro || nome || estado) {
                jpql.append(" and ");
            }
            if (Situacao.ATIVO.getCodigo().equals(profissionalFilter.getStatusProfissional())) {
                jpql.append(" p.dataExclusao ").append(Situacao.ATIVO.getQuery());
            } else if (Situacao.INATIVO.getCodigo().equals(profissionalFilter.getStatusProfissional())) {
                jpql.append(" p.dataExclusao ").append(Situacao.INATIVO.getQuery());
            }
        }
    }

    private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
                                      ProfissionalFilter profissionalFilter, boolean cpf, boolean registro, boolean nome, boolean estado) {
        if (cpf) {
            jpql.append(" upper(p.cpf) like :cpf escape :sc ");
            parametros.put("sc", "\\");
            parametros.put("cpf", profissionalFilter.getCpf().replace("%", "\\%").toUpperCase());
        }

        if (registro) {
            if (cpf) {
                jpql.append(" and ");
            }
            jpql.append(" UPPER(p.registro) like :registro escape :sc ");
            parametros.put("sc", "\\");
            parametros.put("registro",
                    "%" + profissionalFilter.getRegistro().replace("%", "\\%").toUpperCase() + "%");
        }
        if (nome) {
            if (cpf || registro) {
                jpql.append(" and ");
            }
            jpql.append(" set_simple_name(UPPER(p.nome)) like set_simple_name(:nome) escape :sc  ");
            parametros.put("sc", "\\");
            parametros.put("nome", "%" + profissionalFilter.getNome().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
        }

        if (estado) {
            if (cpf || registro || nome)
                jpql.append(" and ");

            jpql.append(" estado.id = :idEstado ");
            parametros.put("idEstado", profissionalFilter.getIdEstado());
        }
    }

    private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
        if (count) {
            jpql.append(" select DISTINCT count(p.id)from Profissional p ");
        } else {
            jpql.append(" select DISTINCT p from Profissional p ");
        }

        if (segurancaFilter != null && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsUnidadeSESI()) {
            jpql.append(" left join p.listaUnidadeAtendimentoTrabalhadorProfissional profissionalUat ");
            jpql.append(" left join profissionalUat.unidadeAtendimentoTrabalhador uat ");
        }
    }

    private void montarSelecetComJoins(StringBuilder jpql) {
        jpql.append(" select p from Profissional p ");
        jpql.append(" left join fetch p.profissionalEspecialidade ps ");
        jpql.append(" left join fetch ps.especialidade pse ");
        jpql.append(" left join fetch p.endereco profEnd ");
        jpql.append(" left join fetch profEnd.endereco endereco ");
        jpql.append(" left join fetch endereco.municipio municipio ");
        jpql.append(" left join fetch municipio.estado estado ");
        jpql.append(" left join fetch p.telefone telefoneProfissional ");
        jpql.append(" left join fetch telefoneProfissional.telefone telefone ");
        jpql.append(" left join fetch p.email emailProfissional ");
        jpql.append(" left join fetch emailProfissional.email email ");

    }

    public Profissional pesquisarPorCPF(String cpf) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select p from Profissional p where p.cpf = :cpf");

        TypedQuery<Profissional> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("cpf", cpf);

        return DAOUtil.getSingleResult(query);
    }

    public Profissional pesquisarPorConselhoRegional(Profissional profissional) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select p from Profissional p  ");
        if (profissional.getEstado() != null) {
            jpql.append(" inner join fetch p.estado estado ");
        }
        jpql.append(" where p.registro = :registro ");
        if (profissional.getEstado() != null) {
            jpql.append(" and p.estado = :estado");
        } else {
            jpql.append(" and p.estado is null ");
        }
        TypedQuery<Profissional> query = criarConsultaPorTipo(jpql.toString());
        query.setParameter("registro", profissional.getRegistro());
        if (profissional.getEstado() != null) {
            query.setParameter("estado", profissional.getEstado());
        }

        return DAOUtil.getSingleResult(query);

    }
}
