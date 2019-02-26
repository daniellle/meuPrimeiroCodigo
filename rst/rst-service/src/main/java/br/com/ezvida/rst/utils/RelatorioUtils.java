package br.com.ezvida.rst.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class RelatorioUtils {
	
	private RelatorioUtils() {
	    throw new IllegalStateException("Utility class");
	  }

    public static final String REPORT_PATH = "/META-INF/reports/";

    public static byte[] gerarPdf(List<PerfilUsuarioDTO> list, InputStream is, Map<String, Object> parameters){
        try
        {

            JasperDesign design = JRXmlLoader.load(is);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint  = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(list));

            ByteArrayOutputStream baos =new ByteArrayOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

            return baos.toByteArray();
        }
        catch(Exception ex)
        {
            System.out.println("EXCEPTION: "+ex);
        }

        return null;
    }

    public static byte[] gerarCsv(List<PerfilUsuarioDTO> list, InputStream is, Map<String, Object> parameters){

        try {
            JasperDesign design = JRXmlLoader.load(is);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(list));

            ByteArrayOutputStream baos =new ByteArrayOutputStream();

            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.exportReport();

//            JRCsvExporter exporter = new JRCsvExporter();

            return baos.toByteArray();
        }catch (Exception e){
            System.out.println("EXCEPTION: "+e);
        }
        return null;
    }
}
