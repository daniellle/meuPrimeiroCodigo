package br.com.ezvida.rst.email.velocity;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import com.google.common.io.LineReader;

public final class VelocityUtil {

    private VelocityUtil() {
        // Utility Class
    }

    /**
     * Remove comentários e formatação de macros, métodos e cláusulas do Velocity
     */
    public static StringBuilder stripTemplateFormatting(InputStreamReader reader) throws IOException {

        StringBuilder template = new StringBuilder();

        LineReader lineReader = new LineReader(reader);

        String line;

        while ((line = lineReader.readLine()) != null) {

            if (line.startsWith("##")) {
                continue;
            }

            String modifiedLine = line;

            modifiedLine = StringUtils.stripStart(modifiedLine, " ");
            modifiedLine = StringUtils.stripStart(modifiedLine, "\t");

            if (modifiedLine.length() == 0 || modifiedLine.charAt(0) == '#') {
                line = modifiedLine;
            }

            line = StringUtils.stripEnd(line, " ");
            line = StringUtils.stripEnd(line, "\t");

            template.append(line);
            template.append('\n');

        }

        return template;

    }

}