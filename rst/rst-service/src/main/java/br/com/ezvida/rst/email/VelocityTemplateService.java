package br.com.ezvida.rst.email;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fw.core.exception.BusinessException;

public class VelocityTemplateService implements Serializable {

	private static final long serialVersionUID = -1951391347494274099L;

	private static final Logger LOGGER = LoggerFactory.getLogger(VelocityTemplateService.class);

    private static final String DEFAULT_PATH = "/velocity/templates/";

    private final VelocityEngine engine;

    public VelocityTemplateService() {
        this.engine = createEngine();
    }

    public <T> String mergeTemplate(String template, Map<String, T> params) {

        StringWriter result = new StringWriter();

        try {

            LOGGER.info("Load template");

            VelocityContext context = createNewContext();

            if (MapUtils.isNotEmpty(params)) {
                for (Entry<String, T> entry : params.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }

            mergeTemplate(context, template, result);

            LOGGER.info("Properties gerado com sucesso");

            return result.toString();

        } finally {
            close(result);
        }

    }

    public String mergeTemplateIntoString(String template, Map<String, Object> params) {
        return mergeTemplate(template, params);
    }

    private void close(StringWriter result) {

        try {

            result.close();

        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }

    }

    /**
     * Faz o merge dos templates.
     * 
     * @param context
     *            Contexto do velocity contendo todos os parametros para o template
     * @param templateFileName
     *            Template a ser utilizado
     * 
     * @return <code>StringWriter</code>
     */
    public void mergeTemplate(VelocityContext context, String templateFileName, Writer writer) {

        try {

            StringBuilder templatePathBuilder = normalizeTemplatePath(DEFAULT_PATH);

            templatePathBuilder.append(templateFileName);

            String templateFilePath = templatePathBuilder.toString();

            LOGGER.info("Merging template: {}", templateFilePath);

            getEngine().getTemplate(templateFilePath, "UTF-8").merge(context, writer);

            writer.flush();
            writer.close();

        } catch (IOException ioe) {
            LOGGER.error("Error trying to write output", ioe);
        }

        LOGGER.info("Template merging successful!");

    }

    /**
     * Metodo reponsável por inicializar um novo contexto de parametros para o velocity.
     * 
     * @return <code>VelocityContext</code>
     */
    private VelocityContext createNewContext() {

        ToolManager velocityToolManager = new ToolManager();
        velocityToolManager.configure("/velocity/velocity-tools.xml");
        VelocityContext context = new VelocityContext(velocityToolManager.createContext());

        return context;
    }

    private VelocityEngine createEngine() {

        LOGGER.info("Creating the Generator Velocity Engine");

        Properties properties = new Properties();

        InputStream stream = null;

        try {

            stream = this.getClass().getResourceAsStream("/velocity/velocity.properties");

            properties.load(stream);

            StringBuilder templatePathBuilder = normalizeTemplatePath(DEFAULT_PATH);

            properties.setProperty("velocity.resource.loader.path", templatePathBuilder.toString());
            properties.setProperty(RuntimeConstants.VM_LIBRARY, getMacroFilesPath(templatePathBuilder.toString()));
            properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");

        } catch (IOException e) {
            LOGGER.error("Error creating the Sabesp Generator Velocity Engine", e);
        } finally {
            closeStream(stream);
        }

        return new VelocityEngine(properties);

    }

    private void closeStream(InputStream stream) {

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                LOGGER.error("Could not close the file properties, Velocity Engine", e);
            }
        }

    }

    private StringBuilder normalizeTemplatePath(String path) {

        StringBuilder templatePathBuilder = new StringBuilder();

        if (!StringUtils.isBlank(path) && path.startsWith(".") && !path.endsWith(File.separator)) {
            templatePathBuilder.append(File.separator);
        } else {
            templatePathBuilder.append("/velocity/templates/");
        }

        return templatePathBuilder;

    }

    private String getMacroFilesPath(String basePath) {

        StringBuilder macroFilesStringBuilder = new StringBuilder();
        File macroDirectory = new File(basePath + "/velocity/macros");

        if (macroDirectory.exists() && macroDirectory.isDirectory()) {

            File[] macroFiles = macroDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".vm");
                }
            });

            if (macroFiles != null) {
                for (File macroVmFile : macroFiles) {
                    macroFilesStringBuilder.append(macroVmFile.getAbsolutePath());
                    macroFilesStringBuilder.append(',');
                }
            }
        }

        return macroFilesStringBuilder.toString();
    }

    public VelocityEngine getEngine() {
        return engine;
    }

}
