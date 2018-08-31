package br.com.ezvida.rst.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Provider
public class SystemResponseFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemResponseFilter.class);
    private static Properties properties = new Properties();

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        responseContext.getHeaders().add("System-Info", getInfo());
    }


    private String getInfo() {
        if (properties.isEmpty()) {
            InputStream resourceAsStream =
                SystemResponseFilter.class.getResourceAsStream(
                    "/META-INF/maven/br.com.ezvida.rst/rst-web/pom.properties");
            try {
                properties.load(resourceAsStream);
            } catch (Exception e) {
                LOGGER.debug("Não foi possível obter as informações do sistema {}", e);
            }
        }
        return properties.getProperty("artifactId", "rst") + " " +
            properties.getProperty("version", "default");
    }

}
