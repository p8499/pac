package com.p8499.paca.generator.test;

import com.jayway.jsonpath.JsonPath;
import com.p8499.paca.Generator0;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by jdeuser on 2018/7/19.
 */
public class ResetDataGenerator extends Generator0 {
    public ResetDataGenerator(Map project) {
        super(project);
        getContext().put("Class", Integer.class);
        getContext().put("Integer", Integer.class);
        getContext().put("String", String.class);
        getContext().put("Math", Math.class);
        getContext().put("Calendar", Calendar.class);
        getContext().put("JsonPath", JsonPath.class);
        getContext().put("StringUtils", StringUtils.class);
        getContext().put("ArrayUtils", ArrayUtils.class);
        getContext().put("project", project);
    }

    @Override
    public File getPath(File folder) {
        return new File(folder, "reset_data.jmx");
    }

    @Override
    public String getContent() throws IOException {
        Template template = getVelocityEngine().getTemplate("com/p8499/paca/templates/test/reset_data.vm");
        Writer bufferWriter = new StringWriter();
        template.merge(getContext(), bufferWriter);
        bufferWriter.flush();
        bufferWriter.close();
        StringBuilder result = new StringBuilder();
        getPrettyPrinter().process(bufferWriter.toString(), result);
        return result.toString().trim();
    }
}
