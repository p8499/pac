package com.p8499.paca;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.para.styleenums.*;
import gudusoft.gsqlparser.pp.stmtformattor.FormattorFactory;
import net.minidev.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.*;
import java.util.logging.XMLFormatter;

/**
 * Created by Administrator on 5/24/2018.
 */
public class Test {
    public static void main(String[] args) throws IOException, FormatterException {
        test2();
        //we have the project
        VelocityContext ctx = new VelocityContext();
        Object project = Configuration.defaultConfiguration().jsonProvider().parse(new FileInputStream(new File("C:\\Users\\jdeuser\\Desktop\\ss\\Sales.json")), "UTF-8");
        ctx.put("Class", Class.class);
        ctx.put("Integer", Integer.class);
        ctx.put("String", String.class);
        ctx.put("Math", Math.class);
        ctx.put("Calendar", Calendar.class);
        ctx.put("JsonPath", JsonPath.class);
        ctx.put("StringUtils", StringUtils.class);
        ctx.put("project", project);
        ctx.put("index", 0);
        ctx.put("ArrayUtils", ArrayUtils.class);
        ctx.put("out", System.out);

        //use this statement to test a json path
        Object x = JsonPath.parse(project).read("$.modules[2].uniques[?(@.serial==true)]");
//        List modules = (List) ((Map) project).get("modules");
//        System.out.println(modules);
//        Collections.reverse(modules);
//        System.out.println((List) ((Map) project).get("modules"));
//        Collections.reverse((List)x);
        //we initialize the velocity engine
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath." + RuntimeConstants.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());
        ve.init();

        //we get the template and parse the project
        Template t = ve.getTemplate("com/p8499/paca/templates/test/reset_data.vm");
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);
//        System.out.println(sw.toString());
//        System.out.println(new Formatter().formatSource(sw.toString()));
        StringBuilder sb = new StringBuilder();
        Generator.getPrettyPrinter().process(sw.toString(), sb);
        System.out.print(sb.toString());

//        TGSqlParser tgSqlParser = new TGSqlParser(EDbVendor.dbvoracle);
//        tgSqlParser.sqltext = sw.toString();
//        tgSqlParser.parse();
//        GFmtOpt gFmtOpt = GFmtOptFactory.newInstance();
//        gFmtOpt.caseKeywords = TCaseOption.CoNoChange;
//        gFmtOpt.caseIdentifier = TCaseOption.CoNoChange;
//        gFmtOpt.caseQuotedIdentifier = TCaseOption.CoNoChange;
//        gFmtOpt.caseFuncname = TCaseOption.CoNoChange;
//        gFmtOpt.caseDatatype = TCaseOption.CoNoChange;
//        gFmtOpt.compactMode = TCompactMode.Cpmugly;
//        gFmtOpt.lineWidth = 999;
//        System.out.print(FormattorFactory.pp(tgSqlParser, gFmtOpt).trim());

    }

    public static void test2() {
        //json is simulated as a referenceGroup
        String json = "[{'id':0, 'domestics':['a','b'], 'foreignModul':'t1', 'foreigns':['a','b']}, {'id':1, 'domestics':['b','f'], 'foreignModule':'t1', 'foreigns':['b','f']}, {'id':2, 'domestics':['e','f'], 'foreignModule':'t1', 'foreigns':['e','f']}]";
        Object project = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Object x = JsonPath.parse(project).read("$.[?(@.id>0 && @.domestics contains 'b')]");
//        Object x = JsonPath.parse(project).read("$.[?(@.id>0 && @.domestics contains 'b')]");
        //JsonPath.parse(project).read("$.domestics[?(@ in ['b','f','g'])]");
        //{domestic:b, foreigns:[{groupId:0, foreignModule: t1, foreign: b}, {groupId:1, foreignModule: t1, foreign: b}]}

//        HashSet set = new HashSet();
//        set.addAll(JsonPath.parse(project).read("$..domestics.*"));
//        JsonPath.parse(set).read("$[?(@=='a')]");

        List list = new ArrayList<>();
        list.addAll(JsonPath.parse(project).read("$..domestics.*"));
        JsonPath.parse(list).read("$[?(@=='a')]");

//        List list = new ArrayList<>();
//        list.add("a");
//        list.add("b");
//        System.out.println(JsonPath.parse(list).read("$").toString());
//        list.add("c");
//        System.out.println(JsonPath.parse(list).read("$").toString());
//        HashSet s = new HashSet();
    }
}
