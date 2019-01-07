package br.com.ezvida.rst.service;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.utils.RelatorioUtils;
import fw.core.service.BaseService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

@Stateless
public class RelatorioService extends BaseService implements Serializable {

    @Inject
    @Preferencial
    UsuarioService service;
    public byte[] gerarRelatorioPDF(UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria) {
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"reportPDF.jrxml");
        return RelatorioUtils.gerarPdf(service.pesquisarPaginadoRelatorio(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    public byte[] gerarRelatorioCSV(UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria){
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"reportCSV.jrxml");
        return RelatorioUtils.gerarCsv(service.pesquisarPaginadoRelatorio(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    public Object pesquisarPaginado(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, ClienteAuditoria clienteInfos) {
       return null;
    }
}
