package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Empresa;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class EmpresaDAO extends BaseDAO<Empresa, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaDAO.class);

	private boolean filtroAplicado;

	@Inject
	public EmpresaDAO(EntityManager em) {
		super(em, Empresa.class);
	}

	public Empresa pesquisarPorId(Long id, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando Empresa por Id");

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select empresa from Empresa empresa ");
		jpql.append(" left join fetch empresa.porteEmpresa porteEmpresa ");
		jpql.append(" left join fetch empresa.tipoEmpresa tipoEmpresa ");
		jpql.append(" left join fetch empresa.segmento segmento ");
		jpql.append(" left join fetch empresa.ramoEmpresa ramoEmpresa ");

		if (segurancaFilter != null && temIdsDepRegionalOuTemIdsEmpresa(segurancaFilter) && !segurancaFilter.isAdministrador()) {
			jpql.append(" inner join empresa.empresaUats empresaUats ");
			jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
			jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional departamentoRegional ");
		}

		jpql.append(" where empresa.id = :id");

		if (segurancaFilter != null && !segurancaFilter.isAdministrador()) {
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
				if (id != null || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional()) {
					jpql.append(" and ");
				}
				jpql.append(" unidadeAtendimentoTrabalhador.id IN (:idsUnidadeSESI) ");
				parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
			}
		}

		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Empresa> pesquisarPaginado(EmpresaFilter empresaFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Empresas por filtro");

		ListaPaginada<Empresa> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoPesquisar(jpql, parametros, empresaFilter, false, segurancaFilter);
		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(empresaFilter, segurancaFilter));

		if (empresaFilter != null) {
			query.setFirstResult((empresaFilter.getPagina() - 1) * empresaFilter.getQuantidadeRegistro());
			query.setMaxResults(empresaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(EmpresaFilter empresaFilter, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
        getQueryPaginadoPesquisar(jpql, parametros, empresaFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

    private void getQueryPaginadoPesquisar(StringBuilder jpql, Map<String, Object> parametros, EmpresaFilter empresaFilter,
                                  boolean count, DadosFilter segurancaFilter) {

        boolean cpf = false;
        boolean razaoSocial = false;
        boolean nomeFantasia = false;
        boolean status = false;
        boolean porte = false;
        boolean estado = true;
        boolean cnae = false;

        setFiltroAplicado(false);

        if (count) {
            jpql.append("select count(DISTINCT empresa.id) from Empresa empresa ");
            jpql.append(" left join empresa.porteEmpresa porteEmpresa ");
            jpql.append(" left join empresa.enderecosEmpresa enderecosEmpresa ");
            jpql.append(" left join enderecosEmpresa.endereco endereco ");
            jpql.append(" left join endereco.municipio municipio ");
            jpql.append(" left join municipio.estado estado ");
            jpql.append(" left join empresa.empresaCnaes empresaCnaes ");
            jpql.append(" left join empresaCnaes.cnae cnae ");



        } else {
            jpql.append("select DISTINCT empresa from Empresa empresa ");
            jpql.append(" left join empresa.porteEmpresa porteEmpresa ");
            jpql.append(" left join empresa.enderecosEmpresa enderecosEmpresa ");
            jpql.append(" left join enderecosEmpresa.endereco endereco ");
            jpql.append(" left join endereco.municipio municipio ");
            jpql.append(" left join municipio.estado estado ");
            jpql.append(" left join empresa.empresaCnaes empresaCnaes ");
            jpql.append(" left join empresaCnaes.cnae cnae ");
        }

        this.montaJoinFiltroDados(jpql, segurancaFilter);

        jpql.append(" where empresa.dataExclusao is null ");
        setFiltroAplicado(true);

        if (empresaFilter != null) {

            cpf = StringUtils.isNotEmpty(empresaFilter.getCnpj());
            razaoSocial = StringUtils.isNotEmpty(empresaFilter.getRazaoSocial());
            nomeFantasia = StringUtils.isNotEmpty(empresaFilter.getNomeFantasia());
            status = StringUtils.isNotBlank(empresaFilter.getStatusCat());
            porte = empresaFilter.getIdPorte() != null && empresaFilter.getIdPorte().intValue() > 0;
            estado = empresaFilter.getIdEstado() != null && empresaFilter.getIdEstado().intValue() > 0;
            cnae = StringUtils.isNotEmpty(empresaFilter.getCodCnae());

            if (cpf) {
                adicionarAnd(jpql);
                jpql.append(" empresa.cnpj = :cnpj ");
                parametros.put("cnpj", empresaFilter.getCnpj());
                setFiltroAplicado(true);
            }

            if (razaoSocial) {
                adicionarAnd(jpql);
                jpql.append(" set_simple_name(UPPER(empresa.razaoSocial)) like set_simple_name(:razaoSocial) escape :sc ");
                parametros.put("sc", "\\");
                parametros.put("razaoSocial", "%" + empresaFilter.getRazaoSocial().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
                setFiltroAplicado(true);
            }

            if (nomeFantasia) {
                adicionarAnd(jpql);
                jpql.append(" set_simple_name(UPPER(empresa.nomeFantasia)) like set_simple_name(:nomeFantasia) escape :sc ");
                parametros.put("sc", "\\");
                parametros.put("nomeFantasia", "%" + empresaFilter.getNomeFantasia().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
                setFiltroAplicado(true);
            }

            if (status) {
                this.filtrarDadosSituacao(jpql, empresaFilter);
            }

            if(porte)
            {
                adicionarAnd(jpql);
                jpql.append(" porteEmpresa.id = :idPorte ");
                parametros.put("idPorte", empresaFilter.getIdPorte());
                setFiltroAplicado(true);
            }

            if (estado) {
                adicionarAnd(jpql);
                jpql.append(" estado.id = :idEstado ");
                parametros.put("idEstado", empresaFilter.getIdEstado());
                setFiltroAplicado(true);
            }

            if (cnae) {
                adicionarAnd(jpql);
                jpql.append(" UPPER(cnae.codigo) like :codigo escape :sc ");
                parametros.put("sc", "\\");
                parametros.put("codigo", "%" + empresaFilter.getCodCnae().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
                setFiltroAplicado(true);
            }
        }

        this.filtrarDadosSeguranca(segurancaFilter, jpql, parametros);

        if (!count) {
            jpql.append(" order by empresa.razaoSocial ");
        }
    }

	
	private void montaJoinFiltroDados(StringBuilder jpql, DadosFilter segurancaFilter) {
		if (segurancaFilter != null && temIdsDepRegionalOuTemIdsEmpresa(segurancaFilter) && !segurancaFilter.isAdministrador()) {
			jpql.append(" inner join empresa.empresaUats empresaUats ");
			jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
			jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional departamentoRegional ");
		}
	}
	
	private void filtrarDadosSituacao(StringBuilder jpql, EmpresaFilter empresaFilter) {
		adicionarAnd(jpql);
		if (Situacao.ATIVO.getCodigo().equals(empresaFilter.getStatusCat())) {
			jpql.append(" empresa.dataDesativacao ").append(Situacao.ATIVO.getQuery());
		} else if (Situacao.INATIVO.getCodigo().equals(empresaFilter.getStatusCat())) {
			jpql.append(" empresa.dataDesativacao ").append(Situacao.INATIVO.getQuery());
		}
	}
	
	private void filtrarDadosSeguranca(DadosFilter segurancaFilter, StringBuilder jpql, Map<String, Object> parametros) {
		if (segurancaFilter != null && temIdsDepRegionalOuTemIdsEmpresa(segurancaFilter) && !segurancaFilter.isAdministrador()) {
			if (segurancaFilter.temIdsEmpresa()) {
				adicionarAnd(jpql);
				jpql.append(" empresa.id IN (:idsEmpresa) ");
				parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
				setFiltroAplicado(true);
			}
			if (segurancaFilter.temIdsDepRegional()) {
				adicionarAnd(jpql);
				jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
				setFiltroAplicado(true);
			}

			if (segurancaFilter.temIdsUnidadeSESI()) {
				adicionarAnd(jpql);
				jpql.append(" unidadeAtendimentoTrabalhador.id IN (:idsUnidadeSESI) ");
				parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
				setFiltroAplicado(true);
			}
		}
	}

	public List<Empresa> listarTodos() {
		LOGGER.debug("Listando todos os Empresas");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select c from Empresa c ");
		jpql.append(" left join c.porteEmpresa p ");
		jpql.append(" left join c.tipoEmpresa t ");
		jpql.append(" left join c.unidadeObra unidadeObra ");
		jpql.append(" left join c.telefoneEmpresa telefoneEmpresa ");
		jpql.append(" left join telefoneEmpresa.telefone telefone ");
		jpql.append(" left join c.enderecosEmpresa enderecosEmpresa ");
		jpql.append(" left join enderecosEmpresa.endereco endereco ");
		jpql.append(" order by c.cnpj ");
		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	public Empresa pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando Empresa por CNPJ");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select c from Empresa c where c.cnpj = :cnpj");

		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cnpj", cnpj);

		return DAOUtil.getSingleResult(query);
	}

	private boolean isFiltroAplicado() {
		return filtroAplicado;
	}

	private void setFiltroAplicado(boolean filtroAplicado) {
		this.filtroAplicado = filtroAplicado;
	}

	private void adicionarAnd(StringBuilder jpql) {
		if (isFiltroAplicado()) {
			jpql.append(" and ");
		}
	}

	private boolean temIdsDepRegionalOuTemIdsEmpresa(DadosFilter segurancaFilter) {
		return segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsUnidadeSESI();
	}

	public List<Empresa> buscarEmpresasPorIds(Set<Long> ids) {
		LOGGER.debug("Pesquisando Empresa por Ids");

		StringBuilder jpql = new StringBuilder();

		jpql.append("select new Empresa( ");
		jpql.append(" empresa.id, empresa.cnpj, empresa.razaoSocial ) ");
		jpql.append(" from Empresa empresa ");
		jpql.append(" where empresa.id in (:ids) ");
		jpql.append(" order by empresa.razaoSocial");

		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("ids", ids);

		return query.getResultList();
	}

	public List<Empresa> buscarEmpresasUatsDrsPorCpf(String cpf) {
		LOGGER.debug("Pesquisando Empresa por cpf");

		StringBuilder jpql = new StringBuilder();

		jpql.append("select new Empresa( ");
		jpql.append(" empresa.id, empresa.cnpj, empresa.razaoSocial ) ");
		jpql.append("from Empresa empresa ");
		jpql.append("inner join empresa.empresaTrabalhadores empresaTrabalhador ");
		jpql.append("inner join empresaTrabalhador.trabalhador trabalhador ");
		jpql.append(" where empresaTrabalhador.dataExclusao is null and empresaTrabalhador.dataDemissao is null ");
		jpql.append(" and trabalhador.cpf = :cpf ");

		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public Empresa buscarEmpresaCadastroPorCnpj(String cnpj){
		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresa from Empresa empresa ");
		jpql.append(" left join fetch empresa.porteEmpresa porteEmpresa ");
		jpql.append(" left join fetch empresa.tipoEmpresa tipoEmpresa ");

		jpql.append(" left join fetch empresa.empresaCnaes empresaCnaes ");
		jpql.append(" left join fetch empresaCnaes.cnae cnae ");

		jpql.append(" left join fetch empresa.segmento segmento ");
		jpql.append(" left join fetch empresa.ramoEmpresa ramoEmpresa ");

		jpql.append(" where empresa.cnpj = :cnpj");
		jpql.append(" and empresa.dataExclusao is null ");

		TypedQuery<Empresa> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cnpj", cnpj);

		return DAOUtil.getSingleResult(query);


	}

	@SuppressWarnings("unchecked")
	public List<String> findCNPJByIdsDepartamentoRegional(Collection<Long> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("	empresa.no_cnpj as cnpj ");
		sql.append("from ");
		sql.append("	departamento_regional dr ");
		sql.append("inner join und_atd_trabalhador unidade on ");
		sql.append("	dr.id_departamento_regional = unidade.id_departamento_regional_fk ");
		sql.append("inner join empresa_uat uat on ");
		sql.append("	unidade.id_und_atd_trabalhador = uat.id_und_atd_trabalhador_fk ");
		sql.append("inner join empresa empresa on ");
		sql.append("	uat.id_empresa_fk = empresa.id_empresa ");
		sql.append("where ");
		sql.append("	dr.id_departamento_regional in (:listIds) ");
		Query query = getEm().createNativeQuery(sql.toString());
		query.setParameter("listIds", ids);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findCNPJByIdsUnidadeSesi(Collection<Long> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("	empresa.no_cnpj as cnpj ");
		sql.append("from ");
		sql.append("	und_atd_trabalhador unidade ");
		sql.append("inner join empresa_uat uat on ");
		sql.append("	uat.id_und_atd_trabalhador_fk = unidade.id_und_atd_trabalhador ");
		sql.append("inner join empresa on ");
		sql.append("	uat.id_empresa_fk = empresa.id_empresa ");
		sql.append("where ");
		sql.append("	unidade.id_und_atd_trabalhador in (:listIds) ");
		Query query = getEm().createNativeQuery(sql.toString());
		query.setParameter("listIds", ids);
		return query.getResultList();
	}
}
