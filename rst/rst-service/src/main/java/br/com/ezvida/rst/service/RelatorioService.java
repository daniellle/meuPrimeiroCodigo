package br.com.ezvida.rst.service;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import br.com.ezvida.rst.utils.RelatorioUtils;
import fw.core.service.BaseService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class RelatorioService extends BaseService implements Serializable {

    @Inject
    @Preferencial
    UsuarioService service;
    public byte[] gerarRelatorioPDF(UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria) {
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"reportPDF.jrxml");
        return RelatorioUtils.gerarPdf(service.pesquisarPaginadoGirstPDF(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    public byte[] gerarRelatorioCSV(UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria){
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"reportCSV.jrxml");
        return RelatorioUtils.gerarCsv(service.pesquisarPaginadoGirstPDF(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    private List<PerfilUsuarioDTO> getLista(){

        PerfilUsuarioDTO dto1 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        PerfilUsuarioDTO dto2 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        PerfilUsuarioDTO dto3 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        PerfilUsuarioDTO dto4 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        PerfilUsuarioDTO dto5 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        PerfilUsuarioDTO dto6 = new PerfilUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");

        List<PerfilUsuarioDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
        list.add(dto5);
        list.add(dto6);

        return list;
    }
}
