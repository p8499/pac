package com.p8499.paca.generator.database.module;

import com.google.googlejavaformat.java.Formatter;
import com.jayway.jsonpath.JsonPath;
import com.p8499.paca.Generator1;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.para.styleenums.TCaseOption;
import gudusoft.gsqlparser.pp.para.styleenums.TCompactMode;
import gudusoft.gsqlparser.pp.stmtformattor.FormattorFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 6/27/2018.
 */
public class BaseCreateViewGenerator extends Generator1 {
    public BaseCreateViewGenerator(Map project) {
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
    public File getPath(File folder, int index) throws Exception {
        Map project = (Map) getContext().get("project");
        String id = (String) ((Map) ((List) project.get("modules")).get(index)).get("id");
        return new File(folder, String.format("base_create_view_%s.sql", id));
    }

    @Override
    public String getContent(int index) throws Exception {
        Map project = (Map) getContext().get("project");
        String datasource = (String) ((Map) ((List) project.get("modules")).get(index)).get("datasource");
        String databaseType = (String) ((Map) ((List) JsonPath.parse(project).read(String.format("$.envJtee.datasources[?(@.id=='%s')]", datasource))).get(0)).get("databaseType");
        //
        getContext().put("index", index);
        Template template = getVelocityEngine().getTemplate("com/p8499/paca/templates/database/module/base_create_view.vm");
        Writer bufferWriter = new StringWriter();
        template.merge(getContext(), bufferWriter);
        bufferWriter.flush();
        bufferWriter.close();
        TGSqlParser tgSqlParser = new TGSqlParser("oracle".equals(databaseType) ? EDbVendor.dbvoracle : "postgresql".equals(databaseType) ? EDbVendor.dbvpostgresql : EDbVendor.dbvgeneric);
        tgSqlParser.sqltext = bufferWriter.toString();
        tgSqlParser.parse();
        return FormattorFactory.pp(tgSqlParser, getGFmtOpt()).trim();
    }
}
