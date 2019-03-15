package br.com.ezvida.rst.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.PesquisaSesiDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PesquisaSesiFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.Linha;
import br.com.ezvida.rst.model.ProdutoServico;
import br.com.ezvida.rst.model.Telefone;
import br.com.ezvida.rst.model.TelefoneUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.PesquisaSesiDTO;

@RunWith(MockitoJUnitRunner.class)
public class PesquisaSesiServiceTest {

    @InjectMocks
    private PesquisaSesiService pesquisaSesiService;

    @Mock
    private PesquisaSesiDAO pesquisaSesiDAO;

    @Mock
    private LinhaService linhaService;

    @Mock
    private ProdutoServicoService produtoServicoService;

    @Mock
    private TelefoneUnidadeAtendimentoTrabalhadorService telefoneUnidadeAtendimentoTrabalhadorService;

    private ClienteAuditoria auditoria = new ClienteAuditoria();

    private ListaPaginada<UnidadeAtendimentoTrabalhador> listaPaginada;

    private UnidadeAtendimentoTrabalhador uat;

    private DepartamentoRegional departamentoRegional;

    private Linha linha;

    private List<Linha> linhas;

    private ProdutoServico produtoServico;

    private List<ProdutoServico> produtosServicos;

    private TelefoneUnidadeAtendimentoTrabalhador telefoneUnidadeAtendimentoTrabalhador;

    private List<TelefoneUnidadeAtendimentoTrabalhador> telefonesUnidadeAtendimentoTrabalhador;

    private Telefone telefone;

    @Before
    public void inicializar() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.auditoria.setNavegador("navegador_teste");
        this.auditoria.setUsuario("usuario_teste");
        this.auditoria.setFuncionalidade(Funcionalidade.PESQUISA_SESI);
        this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
        this.listaPaginada = new ListaPaginada<>(0l, new ArrayList<>());
        this.uat = new UnidadeAtendimentoTrabalhador();
        this.departamentoRegional = new DepartamentoRegional();
        this.departamentoRegional.setId(1l);
        this.linha = new Linha();
        this.linhas = new ArrayList<>();
        this.produtoServico = new ProdutoServico();
        this.produtosServicos = new ArrayList<>();
        this.telefoneUnidadeAtendimentoTrabalhador = new TelefoneUnidadeAtendimentoTrabalhador();
        this.telefonesUnidadeAtendimentoTrabalhador = new ArrayList<>();
        this.telefone = new Telefone();
    }

    @Test
    public void pesquisaSesi() {
        PesquisaSesiFilter pesquisaSesiFilter = new PesquisaSesiFilter();
        this.uat.setId(1l);
        this.uat.setNomeFantasia("Unidade Sesi do Trabalhador");
        this.uat.setCnpj("11813409000143");
        this.uat.setDepartamentoRegional(this.departamentoRegional);
        this.uat.setRazaoSocial("Unidade do trabalhador");
        this.uat.setDataCriacao(new Date());
        this.listaPaginada.setList(Arrays.asList(uat));
        this.listaPaginada.setQuantidade(1l);
        this.linha.setId(1l);
        this.linha.setDescricao("Linha da Unidade Sesi");
        this.produtoServico.setId(1l);
        this.produtoServico.setDescricao("Produto Da Unidade");
        this.produtoServico.setLinha(this.linha);
        this.produtosServicos.add(produtoServico);
        this.linhas.add(linha);
        this.telefone.setId(1l);
        this.telefone.setNumero("986940261");
        this.telefoneUnidadeAtendimentoTrabalhador.setId(1l);
        this.telefoneUnidadeAtendimentoTrabalhador.setUnidadeAtendimentoTrabalhador(uat);
        this.telefoneUnidadeAtendimentoTrabalhador.setTelefone(telefone);
        this.telefonesUnidadeAtendimentoTrabalhador.add(telefoneUnidadeAtendimentoTrabalhador);
        Mockito.when(this.pesquisaSesiDAO.pesquisarPaginado(pesquisaSesiFilter, null)).thenReturn(listaPaginada);
        Mockito.when(this.linhaService.buscarLinhasPorIdUat(uat.getId().toString(), null)).thenReturn(linhas);
        Mockito.when(this.produtoServicoService.buscarProdutosPorIdUat(uat.getId().toString(), null)).thenReturn(produtosServicos);
        Mockito.when(this.telefoneUnidadeAtendimentoTrabalhadorService.pesquisarPorIdUat(uat.getId())).thenReturn(telefonesUnidadeAtendimentoTrabalhador);
        ListaPaginada<PesquisaSesiDTO> pesquisaSesiResult = this.pesquisaSesiService.pesquisarPaginado(pesquisaSesiFilter, auditoria, null);
        PesquisaSesiDTO uatDTOResult = pesquisaSesiResult.getList().get(0);
        Assert.assertEquals(1l, pesquisaSesiResult.getQuantidade().longValue());
        Assert.assertEquals(1l, uatDTOResult.getIdUat().longValue());
        Assert.assertEquals("Linha da Unidade Sesi", uatDTOResult.getLinhas().get(0).getDescricao());
    }
}
