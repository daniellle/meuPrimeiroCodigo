package br.com.ezvida.rst.email.velocity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityResourceLoader extends ResourceLoader {

    private static final String DEFAULT_ENCODING = "UTF8";

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityResourceLoader.class);

    private String path;

    public void init(ExtendedProperties configuration) {
        path = (String) configuration.getProperty("path");
    }

    public InputStream getResourceStream(String name) throws ResourceNotFoundException {

        InputStream result = null;

        if (StringUtils.isEmpty(name)) {
            throw new ResourceNotFoundException("No template name provided");
        }

        File templateFile = new File(name);

        try {

            if (templateFile.exists()) {
                LOGGER.info("Template externalizado encontrado: {}", name);
                result = new FileInputStream(templateFile);
            } else {
                LOGGER.info("Template externalizado não encontrado: {}, buscando template interno", name);
				result = ClassUtils.getResourceAsStream(getClass(), name);
            }

            if (result != null) {

                LOGGER.info("Template encontrado: {}", templateFile.getAbsolutePath());

                StringBuilder template = VelocityUtil.stripTemplateFormatting(new InputStreamReader(result, DEFAULT_ENCODING));
                result.close();

                result = new ByteArrayInputStream(template.toString().getBytes(DEFAULT_ENCODING));

            }

            if (result == null) {

                LOGGER.info("Nenhum template com o nome especificado foi encontrado: {}", name);
                throw new ResourceNotFoundException("SabespResourceLoader Error: cannot find resource: " + name);
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("Template não encontrado", e);
            return null;
        } catch (IOException e) {
            LOGGER.error("Erro ao ler template", e);
            return null;
        }

        return result;
    }

    public String getPath() {
        return path;
    }

    public boolean isSourceModified(Resource resource) {
        return false;
    }

    public long getLastModified(Resource resource) {
        return 0;
    }

}