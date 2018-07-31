package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Trabalhador;
import com.google.common.collect.Maps;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.*;

public class TrabalhadorDAO extends BaseDAO<Trabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorDAO.class);

	static final int TOTAL_RECORD_PER_QUERY = 5;

	@Inject
	public TrabalhadorDAO(EntityManager em) {
		super(em, Trabalhador.class);
	}

	public Trabalhador pesquisarPorId(TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Buscando Trabalhador por Id");
		
		Long id = trabalhadorFilter.getId();

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		montarJoinPesquisarPorId(segurancaFilter, jpql);

		jpql.append(" where trabalhador.id = :id");

		if (segurancaFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			filtroIdsPesquisarPorId(segurancaFilter, id, jpql, parametros);
		}

		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		parametros.put("id", id);
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	private void filtroIdsPesquisarPorId(DadosFilter segurancaFilter, Long id, StringBuilder jpql,
			Map<String, Object> parametros) {
		if (segurancaFilter.temIdsEmpresa() && !segurancaFilter.isAdministrador()) {
			jpql.append(" and empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
			if (id != null || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" depRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		if (segurancaFilter.temIdsTrabalhador() && !segurancaFilter.isAdministrador()) {
			if (id != null || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarJoinPesquisarPorId(DadosFilter segurancaFilter, StringBuilder jpql) {
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" left join fetch trabalhador.profissao p ");
		jpql.append(" left join fetch trabalhador.pais a ");
		jpql.append(" left join fetch trabalhador.municipio m ");
		jpql.append(" left join fetch m.estado e ");

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" left join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" left join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" left join empresa.empresaUats empresaUats ");
				jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
		}
	}

	public Trabalhador pesquisarPorCpfDataNascimento(String cpf, Date dataNascimento) {
		LOGGER.debug("Buscando Trabalhador por cpf e data de nascimento");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" left join fetch trabalhador.profissao p ");
		jpql.append(" left join fetch trabalhador.pais a ");
		jpql.append(" left join fetch trabalhador.municipio m ");
		jpql.append(" left join fetch m.estado e ");
		jpql.append(" where trabalhador.cpf = :cpf");
		jpql.append(" and trabalhador.dataNascimento = :dataNascimento");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		query.setParameter("dataNascimento", dataNascimento);

		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Trabalhador> pesquisarPaginado(TrabalhadorFilter trabalhadorFilter,
			DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Trabalhador por filtro");

		ListaPaginada<Trabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, trabalhadorFilter, false, segurancaFilter);
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(trabalhadorFilter, segurancaFilter));

		if (trabalhadorFilter != null && trabalhadorFilter.getPagina() != null
				&& trabalhadorFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((trabalhadorFilter.getPagina() - 1) * trabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(trabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, trabalhadorFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, boolean count, DadosFilter segurancaFilter) {

		boolean situacao = false;
		boolean cpf = false;
		boolean nome = false;
		boolean nit = false;
		boolean falecidos = false;
		boolean estado = false;
		
		if (trabalhadorFilter != null)
			estado = trabalhadorFilter.getIdEstado() != null && trabalhadorFilter.getIdEstado().intValue() > 0;

		montarJoinPaginado(jpql, count, segurancaFilter, estado);

		if (trabalhadorFilter != null || segurancaFilter != null) {
			jpql.append(" where ");
		}

		if (trabalhadorFilter != null) {
			situacao = StringUtils.isNotBlank(trabalhadorFilter.getSituacao());
			cpf = StringUtils.isNotEmpty(trabalhadorFilter.getCpf());
			nome = StringUtils.isNotEmpty(trabalhadorFilter.getNome());
			nit = StringUtils.isNotEmpty(trabalhadorFilter.getNit());
			falecidos = trabalhadorFilter.isFalecidos();			

			situacao = montarFiltroSituacaoPaginado(jpql, trabalhadorFilter, situacao);

			montarFiltroPaginado(jpql, parametros, trabalhadorFilter, situacao, cpf, nome, nit, estado);

			montarFiltroFalecidoPaginado(jpql, trabalhadorFilter, situacao, cpf, nome, nit, estado);
		}

		aplicarDadosFilter(jpql, parametros, trabalhadorFilter, segurancaFilter, situacao, cpf, nome, nit, falecidos, estado);

		if (!count) {
			jpql.append(" order by trabalhador.nome");
		}

	}

	private void aplicarDadosFilter(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter, boolean situacao, boolean cpf,
			boolean nome, boolean nit, boolean falecidos, boolean estado) {
		if (segurancaFilter != null && trabalhadorFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			boolean hasFilters = cpf || nome || nit || situacao || !falecidos || estado;
			addFiltroIds(jpql, parametros, segurancaFilter, hasFilters);
		}
	}

	private void addFiltroIds(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
			boolean hasFilters) {
		if (segurancaFilter.temIdsEmpresa() && !segurancaFilter.isAdministrador()) {
			if (hasFilters) {
				jpql.append(" and ");
			}

			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
			if (hasFilters || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" depRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		if (segurancaFilter.temIdsTrabalhador() && !segurancaFilter.isAdministrador()) {
			if (hasFilters || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarFiltroFalecidoPaginado(StringBuilder jpql, TrabalhadorFilter trabalhadorFilter, boolean situacao,
			boolean cpf, boolean nome, boolean nit, boolean estado) {
		if (trabalhadorFilter.isFalecidos()) {
			if (situacao || cpf || nome || nit || estado) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.dataFalecimento is null ");
		}
	}

	private boolean montarFiltroSituacaoPaginado(StringBuilder jpql, TrabalhadorFilter trabalhadorFilter,
			boolean situacao) {
		if (situacao) {
			boolean verificarSituacao = false;
			if (Situacao.ATIVO.getCodigo().equals(trabalhadorFilter.getSituacao())) {
				jpql.append(" trabalhador.dataFalecimento ").append(Situacao.ATIVO.getQuery());
				verificarSituacao = true;
			}

			if (Situacao.INATIVO.getCodigo().equals(trabalhadorFilter.getSituacao())) {
				jpql.append(" trabalhador.dataFalecimento ").append(Situacao.INATIVO.getQuery());
				verificarSituacao = true;
			}
			situacao = verificarSituacao;
		}
		return situacao;
	}

	private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, boolean situacao, boolean cpf, boolean nome, boolean nit, boolean estado) {
		if (cpf) {
			if (situacao) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.cpf = :cpf ");
			parametros.put("cpf", trabalhadorFilter.getCpf());
		}
		if (nome) {
			if (situacao || cpf) {
				jpql.append(" and ");
			}
			jpql.append(" set_simple_name(UPPER(trabalhador.nome)) like set_simple_name(:nome) escape :sc ");
			parametros.put("sc", "\\");
			//parametros.put("nome", "%" + trabalhadorFilter.getNome().replaceAll("%", "\\%").toUpperCase() + "%");
			parametros.put("nome", "%" + trabalhadorFilter.getNome().replaceAll("%", "\\%").toUpperCase().replace(" ", "%") + "%");
		}
		if (nit) {
			if (situacao || cpf || nome) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.nit = :nit ");
			parametros.put("nit", trabalhadorFilter.getNit());
		}
		
		if (estado) {
			if (situacao || cpf || nome || nit) 
				jpql.append(" and ");
			jpql.append(" e.id = :idEstado ");
			parametros.put("idEstado", trabalhadorFilter.getIdEstado());
		}
	}

	private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter, boolean estado) {
		if (count) {
			jpql.append("select count( DISTINCT trabalhador.id) from Trabalhador trabalhador ");
		} else {
			jpql.append("select DISTINCT trabalhador from Trabalhador trabalhador ");
		}
		
		if (estado) {
			jpql.append(" left join trabalhador.municipio m ");
			jpql.append(" left join m.estado e ");			
		}

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" inner join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" inner join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" inner join empresa.empresaUats empresaUats ");
				jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
		}
	}

	public List<Trabalhador> listarTodos() {
		LOGGER.debug("Listando todos os trabalhadores");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" order by trabalhador.nome");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	public List<Trabalhador> pesquisarPorCPF(List<String> cpf) {
		LOGGER.debug("Pesquisando Trabalhador por CPF");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select t from Trabalhador t where t.cpf in (:cpf)");
		jpql.append(" order by t.nome");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public Trabalhador pesquisarPorCpf(String cpf) {
		LOGGER.debug("Pesquisando Trabalhador por CPF");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select t from Trabalhador t where t.cpf = :cpf");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return DAOUtil.getSingleResult(query);
	}

	public Trabalhador buscarVacinasAlergiasMedicamentosAutoDeclarados(String cpf) {
		LOGGER.debug("Pesquisando dados auto-declarados do trabalhador por cpf.");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo("select new Trabalhador(t.id, t.descricaoMedicamentos, t.descricaoAlergias, t.descricaoVacinas) from Trabalhador t where t.cpf = :cpf");
		query.setParameter("cpf", cpf);
		return DAOUtil.getSingleResult(query);
	}
	
	public Long buscarVidaAtiva(String id) {
		LOGGER.debug("Pesquisando Vida Ativa do trabalhador por id.");
		Query query = criarConsulta("select count(t.id) from EmpresaTrabalhador t where t.trabalhador.id = :id and t.dataFimContrato >= current_date ");
		query.setParameter("id", Long.parseLong(id));
		return DAOUtil.getSingleResult(query);
	}	


	public Map<String, List<Object>> buscarTrabalhadoresByEmpresasDoUsuario(List<Long> empresas, String nome, String cpf, String page) {
		LOGGER.debug("Pesquisando trabalhadores pelas empresas do usuário");
		Integer pagina = 0;

		if (StringUtils.isNotEmpty(page)) {
			pagina = Integer.valueOf(page);
		}

		StringBuilder jpql2 = new StringBuilder();
		StringBuilder jpql = new StringBuilder();

		jpql.append(" SELECT COUNT(DISTINCT t2.ID_EMP_TRABALHADOR) from trabalhador t ");
		jpql2.append(" SELECT DISTINCT t.id_trabalhador, t.dt_nascimento, t.fl_genero, t.no_cpf, t.nm_trabalhador, em.nm_fantasia, f2.ds_funcao, cbo.ds_cbo, t.imagem from trabalhador t ");

		this.joinTrabalhadorUsuario(jpql);
		this.joinTrabalhadorUsuario(jpql2);

		this.checkFiltroTrabalhadorUsuario(jpql, nome, cpf, empresas);
		this.checkFiltroTrabalhadorUsuario(jpql2, nome, cpf, empresas);

		jpql2.append(" order by nm_trabalhador asc limit :items offset :pagina");

		Query query = criarConsultaNativa(jpql.toString());
		Query query2 = criarConsultaNativa(jpql2.toString());

		this.setParametros(query, nome, cpf, empresas);
		this.setParametros(query2, nome, cpf, empresas);

		query2.setParameter("items", TOTAL_RECORD_PER_QUERY);
		query2.setParameter("pagina", TOTAL_RECORD_PER_QUERY * pagina );



		BigInteger count = DAOUtil.getSingleResult(query);
		List<Object> trabalhadores = query2.getResultList();
        Map<String, List<Object>> hashMap = new HashMap<>();
        List<Object> countL = new ArrayList<>();
        countL.add(count);
        hashMap.put("count", countL);
        hashMap.put("trabalhadores", trabalhadores);
		return hashMap;
	}

    private void joinTrabalhadorUsuario(StringBuilder jpql) {
        jpql.append(" INNER JOIN emp_trabalhador t2 ");
        jpql.append(" ON t.id_trabalhador = t2.id_trabalhador_fk ");
        jpql.append(" AND t2.dt_demissao IS NULL ");
        jpql.append(" AND t2.dt_admissao = (SELECT MAX(t3.dt_admissao) FROM emp_trabalhador t3 WHERE t.id_trabalhador = t3.id_trabalhador_fk) ");
        jpql.append(" INNER JOIN  empresa em ");
        jpql.append(" ON t2.id_empresa_fk = em.id_empresa ");
        jpql.append(" LEFT JOIN emp_trab_lotacao etl ");
        jpql.append(" ON t2.id_emp_trabalhador = etl.id_empr_trabalhador_fk ");
        jpql.append(" AND etl.dt_associacao = (SELECT MAX(etl2.dt_associacao) FROM emp_trab_lotacao etl2 WHERE etl2.id_empr_trabalhador_fk = t2.id_emp_trabalhador) ");
        jpql.append(" LEFT JOIN emp_lotacao el ");
        jpql.append(" ON etl.id_emp_lotacao_fk = el.id_empresa_lotacao ");
        jpql.append(" LEFT JOIN emp_cbo ec ");
        jpql.append(" ON el.id_emp_cbo_fk = ec.id_emp_cbo ");
        jpql.append(" LEFT JOIN emp_funcao ef2 ");
        jpql.append(" ON el.id_emp_funcao_fk = ef2.id_emp_funcao ");
        jpql.append(" LEFT JOIN funcao f2 ");
        jpql.append(" ON ef2.id_funcao_fk = f2.id_funcao ");
        jpql.append(" LEFT JOIN cbo cbo ");
        jpql.append(" ON ec.id_cbo_fk = cbo.id_cbo ");
    }

	public void checkFiltroTrabalhadorUsuario(StringBuilder jpql, String nome, String cpf, List<Long> empresas){
		boolean hasNome = StringUtils.isNotEmpty(nome);
		boolean hasCpf = StringUtils.isNotEmpty(cpf);
		boolean hasEmpresas = CollectionUtils.isNotEmpty(empresas);

		if (hasNome || hasCpf || hasEmpresas) {
			jpql.append(" where ");
		}

		if(hasNome) {
			jpql.append(" (upper(nm_trabalhador) like upper(:nome)) ");
		}

		if (hasNome && hasCpf){
			jpql.append(" AND ");
		}

		if(hasCpf){
			jpql.append("(no_cpf like :cpf) ");
		}

		if ((hasNome || hasCpf) & hasEmpresas){
			jpql.append(" AND ");
		}

		if (hasEmpresas){
			jpql.append(" em.id_empresa IN (:idEmpresa) ");
		}
	}

	public void setParametros(Query query, String nome, String cpf, List<Long> empresas){
		if(StringUtils.isNotEmpty(nome)) {
			query.setParameter("nome", "%"+nome.trim().replace(" ", "%")+"%");
		}
		if(StringUtils.isNotEmpty(cpf)) {
			query.setParameter("cpf", "%"+cpf+"%");
		}

		if (empresas!=null && !empresas.isEmpty()) {
			query.setParameter("idEmpresa", empresas);
		}
	}
}
