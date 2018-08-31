package br.com.ezvida.rst.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.PesquisaSesiDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PesquisaSesiFilter;
import br.com.ezvida.rst.model.Linha;
import br.com.ezvida.rst.model.ProdutoServico;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.LinhaDTO;
import br.com.ezvida.rst.model.dto.PesquisaSesiDTO;
import br.com.ezvida.rst.model.dto.ProdutoServicoDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class PesquisaSesiService extends BaseService {

	private static final long serialVersionUID = 6561480469370694141L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PesquisaSesiService.class);

	@Inject
	private PesquisaSesiDAO pesquisaSesiDAO;

	@Inject
	private LinhaService linhaService;

	@Inject
	private ProdutoServicoService produtoServicoService;

	@Inject
	private UnidadeAtendimentoTrabalhadorService unidadeAtendimentoTrabalhadorService;

	@Inject
	private TelefoneUnidadeAtendimentoTrabalhadorService telefoneUnidadeAtendimentoTrabalhadorService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeAtendimentoTrabalhador> buscarUnidadesSesi(ClienteAuditoria auditoria, DadosFilter dados) {
		LOGGER.debug("Buscando Unidades Sesi");
		return pesquisaSesiDAO.buscarUnidadesSesi(dados);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeAtendimentoTrabalhador buscarEnderecoUnidadeSesi(Long id, ClienteAuditoria auditoria,
			DadosFilter dados) {

		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		UnidadeAtendimentoTrabalhador uat = unidadeAtendimentoTrabalhadorService.pesquisarPorId(id, null, dados);

		return uat;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<PesquisaSesiDTO> pesquisarPaginado(PesquisaSesiFilter clienteFilter,
			ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Unidades Sesi Produto Serviço por filtro: ",
				clienteFilter);

		ListaPaginada<UnidadeAtendimentoTrabalhador> uats = pesquisaSesiDAO.pesquisarPaginado(clienteFilter,
				dadosFilter);

		List<PesquisaSesiDTO> listaPesquisaSesiDTO = new ArrayList<PesquisaSesiDTO>();

		for (UnidadeAtendimentoTrabalhador uat : uats.getList()) {
			PesquisaSesiDTO pesquisaSesiDTO = new PesquisaSesiDTO();
			pesquisaSesiDTO.setIdUat(uat.getId());
			pesquisaSesiDTO.setRazaoSocialUat(uat.getRazaoSocial());
			pesquisaSesiDTO.setEndereco(uat.getEndereco());
			pesquisaSesiDTO.setTelefone(
					Sets.newHashSet(telefoneUnidadeAtendimentoTrabalhadorService.pesquisarPorIdUat(uat.getId())));
			List<Linha> linhas = linhaService.buscarLinhasPorIdUat(uat.getId().toString(), dadosFilter);
			List<ProdutoServico> produtosServicos = produtoServicoService.buscarProdutosPorIdUat(uat.getId().toString(), dadosFilter);

			List<LinhaDTO> linhasDTO = new ArrayList<LinhaDTO>();
			for (Linha linha : linhas) {
				LinhaDTO linhaDTO = new LinhaDTO();
				linhaDTO.setDescricao(linha.getDescricao());

				List<ProdutoServicoDTO> produtosServicosDTO = new ArrayList<ProdutoServicoDTO>();
				for (ProdutoServico produtoServico : produtosServicos) {
					ProdutoServicoDTO produtoServicosDTO = new ProdutoServicoDTO();
					if (produtoServico.getLinha().getId().equals(linha.getId())) {
						produtoServicosDTO.setNome(produtoServico.getNome());
						produtosServicosDTO.add(produtoServicosDTO);
					}
				}

				linhaDTO.setProdutosServicos(produtosServicosDTO);

				linhasDTO.add(linhaDTO);
			}
			pesquisaSesiDTO.setLinhas(linhasDTO);

			listaPesquisaSesiDTO.add(pesquisaSesiDTO);
		}

		ListaPaginada<PesquisaSesiDTO> retorno = new ListaPaginada<PesquisaSesiDTO>(uats.getQuantidade(),
				listaPesquisaSesiDTO);
		return retorno;
	}

}
