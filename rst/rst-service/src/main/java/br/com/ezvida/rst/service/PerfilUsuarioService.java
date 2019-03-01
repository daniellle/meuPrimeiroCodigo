package br.com.ezvida.rst.service;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import br.com.ezvida.rst.utils.RelatorioUtils;
import fw.core.service.BaseService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

@Stateless
public class PerfilUsuarioService extends BaseService implements Serializable {

    @Inject
    @Preferencial
    UsuarioService service;
    public byte[] gerarRelatorioPDF(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario
            , ClienteAuditoria auditoria) {
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"reportPDF.jrxml");
        return RelatorioUtils.gerarPdf(service.pesquisarPaginadoRelatorio(usuarioFilter, dados, usuario, auditoria), is, new HashMap<>() );
    }

    public byte[] gerarRelatorioXLS(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario
            , ClienteAuditoria auditoria){
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"report1.jrxml");
        return RelatorioUtils.gerarCsv(service.pesquisarPaginadoRelatorio(usuarioFilter, dados, usuario, auditoria), is, new HashMap<>() );
    }

    public ListaPaginada<PerfilUsuarioDTO> pesquisarPaginado(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario, ClienteAuditoria auditoria) {

       return service.pesquisarListaPaginadaPerfilUsuario(usuarioFilter, dadosFilter, usuario, auditoria);
    }
}
