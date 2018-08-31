package br.com.ezvida.rst.service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.alibaba.fastjson.JSON;

import br.com.ezvida.rst.auditoria.model.Auditoria;
import br.com.ezvida.rst.dao.filter.AuditoriaFilter;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Parametro;
import br.com.ezvida.rst.model.Usuario;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class AuditoriaService extends BaseService {

	private static final String D_M_YYYY = "d/M/yyyy";
	private static final String INDEX_LOGSTASH = "logstash-*";
	private static final String LIKE = "*";
	private static final String INFO = "INFO";
	private static final String LEVEL = "level";
	private static final String RST = "rst";
	private static final String TIMESTAMP = "@timestamp";

	private static final String MDC_TIPO_OPERACAO = "mdc.tipo_operacao";
	private static final String MDC_FUNCIONALIDADE = "mdc.funcionalidade";
	private static final String MDC_USUARIO = "mdc.usuario";
	private static final String MDC_APP_NAME = "mdc.appName";


	private static final long serialVersionUID = 3688920689919191792L;

	@Inject
	private UsuarioEntidadeService usuarioEntidadeService;

	@Inject
	private ParametroService parametroService;

	public ListaPaginada<Auditoria> pesquisarPorFiltro(AuditoriaFilter auditoriaFilter, DadosFilter dadosFilter, Usuario usuario) {
		ListaPaginada<Auditoria> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Parametro host = parametroService.carregarParametroPorNome(ParametroService.HOST_ELASTICSEARCH);

		if (host == null) {
			throw new BusinessErrorException("Houve um erro ao obter paramêtros de auditoria.");
		}


		try {
			String[] ipPorta = host.getValor().split(":");

			RestHighLevelClient client = new RestHighLevelClient(
					RestClient.builder(new HttpHost(ipPorta[1], Integer.valueOf(ipPorta[2]), ipPorta[0])));

			SearchHits searchHits = client.search(getSearchRequest(auditoriaFilter, dadosFilter, usuario)).getHits();

			listaPaginada.setQuantidade(searchHits.getTotalHits());

			searchHits.forEach(hit -> listaPaginada.getList().add(JSON.parseObject(hit.getSourceAsString(), Auditoria.class)));

			client.close();
		} catch (DateTimeParseException e) {
			throw new BusinessErrorException("Houve erro ao fazer parse na data.", e);
		} catch (Exception e) {
			throw new BusinessErrorException("Houve falha ao tentar conectar com o elasticsearch.", e);
		}

		return listaPaginada;
	}

	private SearchSourceBuilder getSearchSourceBuilder(AuditoriaFilter auditoriaFilter, DadosFilter dadosFilter, Usuario usuario) {
		return new SearchSourceBuilder().query(getBoolQuery(auditoriaFilter, dadosFilter, usuario)).sort(TIMESTAMP, SortOrder.DESC)
				.from((auditoriaFilter.getPagina() - 1) * auditoriaFilter.getQuantidadeRegistro()).size(auditoriaFilter.getQuantidadeRegistro());
	}

	private BoolQueryBuilder getBoolQuery(AuditoriaFilter auditoriaFilter, DadosFilter dadosFilter, Usuario usuario) {
		BoolQueryBuilder bool = new BoolQueryBuilder().must(QueryBuilders.matchQuery(MDC_APP_NAME, RST))
				.must(QueryBuilders.matchQuery(LEVEL, INFO))
				.must(QueryBuilders.rangeQuery(TIMESTAMP)
						.from(DateTimeFormatter.ISO_DATE.format(DateTimeFormatter.ofPattern(D_M_YYYY)
								.parse(auditoriaFilter.getDataInicial())))
						.to(DateTimeFormatter.ISO_DATE.format(DateTimeFormatter.ofPattern(D_M_YYYY)
								.parse(auditoriaFilter.getDataFinal()))));

		if (StringUtils.isNotBlank(auditoriaFilter.getUsuario())) {
			bool.must(QueryBuilders.matchQuery(MDC_USUARIO, LIKE.concat(auditoriaFilter.getUsuario()).concat(LIKE)));
		}

		if (!auditoriaFilter.getFuncionalidade().equals(Funcionalidade.TODOS.getCodigoJson())) {
			bool.must(QueryBuilders.matchQuery(MDC_FUNCIONALIDADE, auditoriaFilter.getFuncionalidade()));
		}

		if (!auditoriaFilter.getTipoOperacaoAuditoria().equals(TipoOperacaoAuditoria.TODOS.getCodigoJson())) {
			bool.must(QueryBuilders.matchQuery(MDC_TIPO_OPERACAO, auditoriaFilter.getTipoOperacaoAuditoria()));
		}

		if (dadosFilter.temIdsDepRegional() || dadosFilter.temIdsEmpresa()) {
			Collection<String> logins = usuarioEntidadeService.buscarUsuarioEntidadedoUsuarioLogado(usuario.getLogin(), dadosFilter);
			bool.must(QueryBuilders.termsQuery(MDC_USUARIO, logins));
		}

		return bool;
	}

	private SearchRequest getSearchRequest(AuditoriaFilter auditoriaFilter, DadosFilter dadosFilter, Usuario usuario) {
		return new SearchRequest(INDEX_LOGSTASH).source(getSearchSourceBuilder(auditoriaFilter, dadosFilter, usuario));
	}
}
