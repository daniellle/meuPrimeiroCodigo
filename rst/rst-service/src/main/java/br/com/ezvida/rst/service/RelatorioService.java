package br.com.ezvida.rst.service;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.model.dto.RelatorioUsuarioDTO;
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
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"report1.jrxml");
        return RelatorioUtils.gerarPdf(service.pesquisarPaginadoGirstPDF(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    public byte[] gerarRelatorioCSV(UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria){
        InputStream is =  getClass().getResourceAsStream(RelatorioUtils.REPORT_PATH+"report1.jrxml");
        return RelatorioUtils.gerarCsv(service.pesquisarPaginadoGirstPDF(usuarioFilter, dados, auditoria), is, new HashMap<>() );
    }

    private List<RelatorioUsuarioDTO> getLista(){

        RelatorioUsuarioDTO dto1 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        RelatorioUsuarioDTO dto2 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        RelatorioUsuarioDTO dto3 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        RelatorioUsuarioDTO dto4 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        RelatorioUsuarioDTO dto5 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");
        RelatorioUsuarioDTO dto6 = new RelatorioUsuarioDTO("ABC", "02393802398", "A", "B", "C", "Solutis");

        List<RelatorioUsuarioDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
        list.add(dto5);
        list.add(dto6);

        return list;
    }
}
