package com.p8499.paca.generator.database;

import com.jayway.jsonpath.JsonPath;
import com.p8499.paca.Generator0;
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
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Administrator on 6/27/2018.
 */
public class InstallOracleGenerator extends Generator0 {
    public InstallOracleGenerator(Map project) {
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
    public File getPath(File folder) {
        return new File(folder, "install_oracle.sql");
    }

    @Override
    public String getContent() throws IOException {
        Template template = getVelocityEngine().getTemplate("com/p8499/paca/templates/database/install_oracle.vm");
        Writer bufferWriter = new StringWriter();
        template.merge(getContext(), bufferWriter);
        bufferWriter.flush();
        bufferWriter.close();
        TGSqlParser tgSqlParser = new TGSqlParser(EDbVendor.dbvoracle);
        tgSqlParser.sqltext = bufferWriter.toString();
        tgSqlParser.parse();
        return FormattorFactory.pp(tgSqlParser, getGFmtOpt()).trim();
    }
}
