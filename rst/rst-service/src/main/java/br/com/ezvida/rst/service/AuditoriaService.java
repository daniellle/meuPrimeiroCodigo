package br.com.ezvida.rst.service;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTime;
import org.json.JSONObject;

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

	private static final String TIMESTAMP = "@timestamp";

	private static final long serialVersionUID = 3688920689919191792L;

	@Inject
	private UsuarioEntidadeService usuarioEntidadeService;

	@Inject
	private ParametroService parametroService;

	@SuppressWarnings("resource")
	public ListaPaginada<Auditoria> pesquisarPorFiltro(AuditoriaFilter auditoriaFilter, DadosFilter dadosFilter, Usuario usuario) {
		SearchResponse response = null;
		ListaPaginada<Auditoria> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Parametro host = parametroService.carregarParametroPorNome(ParametroService.HOST_ELASTICSEARCH);
		Parametro cluster = parametroService.carregarParametroPorNome(ParametroService.CLUSTER_ELASTICSEARCH);

		if (host == null || cluster == null) {
			throw new BusinessErrorException("Houve um erro ao obter paramêtros de auditoria.");
		}

		TransportClient client = null;
		try {

			Collection<String> logins = usuarioEntidadeService.buscarUsuarioEntidadedoUsuarioLogado(usuario.getLogin(), dadosFilter);

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			date = df.parse(auditoriaFilter.getDataInicial());
			df.applyPattern("yyyy-MM-dd");
			auditoriaFilter.setDataInicial(df.format(date));
			df = new SimpleDateFormat("dd/MM/yyyy");
			date = df.parse(auditoriaFilter.getDataFinal());
			df.applyPattern("yyyy-MM-dd");
			auditoriaFilter.setDataFinal(df.format(date));

			String paramFunc = auditoriaFilter.getFuncionalidade().equals(Funcionalidade.TODOS.getCodigoJson()) ? "*"
					: Funcionalidade.getTFuncionalidadeJson(auditoriaFilter.getFuncionalidade()).getCodigoJson();
			String paramTpOp = auditoriaFilter.getTipoOperacaoAuditoria().equals(TipoOperacaoAuditoria.TODOS.getCodigoJson()) ? "*"
					: auditoriaFilter.getTipoOperacaoAuditoria();

			String usuarioFiltro = StringUtils.isBlank(auditoriaFilter.getUsuario()) ? "*" : ("*" + auditoriaFilter.getUsuario() + "*");

			Settings settings = Settings.builder().put("cluster.name", cluster.getValor()).put("client.transport.sniff", true).build();

			String ipPorta[] = host.getValor().split(":");

			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(ipPorta[0]), Integer.valueOf(ipPorta[1])))
					.addTransportAddress(new TransportAddress(InetAddress.getByName(ipPorta[0]), Integer.valueOf(ipPorta[2])));

			if (client != null) {
				BoolQueryBuilder bool = new BoolQueryBuilder();
				bool.must(QueryBuilders.matchQuery("mdc.appName", "rst")).must(QueryBuilders.matchQuery("level", "INFO"))
						.must(QueryBuilders.rangeQuery(TIMESTAMP).from(auditoriaFilter.getDataInicial()).to(auditoriaFilter.getDataFinal()));

				if (StringUtils.isNotBlank(auditoriaFilter.getUsuario())) {
					bool.must(QueryBuilders.matchQuery("mdc.usuario", usuarioFiltro));
				}

				if (!auditoriaFilter.getFuncionalidade().equals(Funcionalidade.TODOS.getCodigoJson())) {
					bool.must(QueryBuilders.matchQuery("mdc.funcionalidade", paramFunc));
				}

				if (!auditoriaFilter.getTipoOperacaoAuditoria().equals(TipoOperacaoAuditoria.TODOS.getCodigoJson())) {
					bool.must(QueryBuilders.matchQuery("mdc.tipo_operacao", paramTpOp));
				}

				if (dadosFilter.temIdsDepRegional() || dadosFilter.temIdsEmpresa()) {
					bool.must(QueryBuilders.termsQuery("mdc.usuario", logins));
				}

				long count = client.prepareSearch().setIndices("logstash-*").setSize(0).setQuery(bool).execute().actionGet().getHits().getTotalHits();

				response = client.prepareSearch().setIndices("logstash-*").addSort(TIMESTAMP, SortOrder.DESC).setQuery(bool)
						.setFrom((auditoriaFilter.getPagina() - 1) * auditoriaFilter.getQuantidadeRegistro())
						.setSize(auditoriaFilter.getQuantidadeRegistro()).execute().actionGet();

				if (response != null && count > 0) {
					listaPaginada.setQuantidade(count);
					List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					for (SearchHit searchHit : searchHits) {

						Auditoria a = JSON.parseObject(searchHit.getSourceAsString(), Auditoria.class);
						JSONObject jsonObject = new JSONObject(searchHit.getSourceAsString());
						a.setTimestamp(formatter.format(DateTime.parse(jsonObject.getString(TIMESTAMP)).toDate()));
						listaPaginada.getList().add(a);
					}
				}

			}

		} catch (ParseException e) {
			throw new BusinessErrorException("Houve um erro na conversão da data para pesquisar.");
		} catch (Exception e) {
			throw new BusinessErrorException("Houve falha ao tentar conectar com o elasticsearch.", e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return listaPaginada;
	}
}
