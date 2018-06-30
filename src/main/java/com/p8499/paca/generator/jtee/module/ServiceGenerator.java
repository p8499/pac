package com.p8499.paca.generator.jtee.module;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.jayway.jsonpath.JsonPath;
import com.p8499.paca.Generator1;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 6/6/2018.
 */
public class ServiceGenerator extends Generator1 {
    public ServiceGenerator(Map project) {
        super(project);
        getContext().put("Integer", Integer.class);
        getContext().put("String", String.class);
        getContext().put("Math", Math.class);
        getContext().put("Calendar", Calendar.class);
        getContext().put("JsonPath", JsonPath.class);
        getContext().put("StringUtils", StringUtils.class);
        getContext().put("project", project);
    }

    @Override
    public File getPath(File folder, int index) {
        Map project = (Map) getContext().get("project");
        String packageName = (String) ((Map) project.get("envJtee")).get("packageService");
        String className = (String) ((Map) ((List) project.get("modules")).get(index)).get("jteeServiceAlias");
        return new File(folder, "src/main/java/" + packageName.replace(".", "/") + "/" + className + ".java");
    }

    @Override
    public String getContent(int index) throws IOException, FormatterException {
        getContext().put("index", index);
        Template template = getVelocityEngine().getTemplate("com/p8499/paca/templates/jtee/module/service.vm");
        Writer bufferWriter = new StringWriter();
        template.merge(getContext(), bufferWriter);
        bufferWriter.flush();
        bufferWriter.close();
        return new Formatter().formatSource(bufferWriter.toString()).trim();
    }
}
