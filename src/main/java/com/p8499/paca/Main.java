package com.p8499.paca;

import com.jayway.jsonpath.Configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 6/6/2018.
 */
public class Main {
    public static void main(String[] args) {
        Options options = new Options()
                .addOption("database", false, "generate database scripts")
                .addOption("j2ee", false, "generate j2ee project")
                .addOption("android", false, "generate android module")
                .addOption("test", false, "generate test scripts")
                .addOption("i", true, "input project file")
                .addOption("o", true, "output folder");
        try {
            CommandLine commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption("i") && commandLine.hasOption("o")
                    && (commandLine.hasOption("database") || commandLine.hasOption("j2ee") || commandLine.hasOption("android") || commandLine.hasOption("test"))) {
                //has i and o, and has at least one of database/j2ee/android/test
                File inputFile = new File(commandLine.getOptionValue("i"));
                File outputFolder = new File(commandLine.getOptionValue("o"));
                FileInputStream fis = new FileInputStream(inputFile);
                Map project = (Map) Configuration.defaultConfiguration().jsonProvider().parse(fis, "UTF-8");
                fis.close();
                if (commandLine.hasOption("database"))
                    generateDatabase(project, new File(outputFolder, "database"));
                if (commandLine.hasOption("j2ee"))
                    generateJtee(project, new File(outputFolder, "j2ee"));
                if (commandLine.hasOption("android"))
                    generateAndroid(project, new File(outputFolder, "android"));
                if (commandLine.hasOption("test"))
                    generateAndroid(project, new File(outputFolder, "test"));
            } else
                verbose();
        } catch (Exception e) {
            verbose();
        }
    }

    public static void verbose() {
        System.out.println("Usage: java-jar paca.jar -database -j2ee -android -test -i input_file -o output_folder");
    }

    public static void generateDatabase(Map project, File outputDir) throws Exception {
        com.p8499.paca.generator.database.module.CreateTableGenerator createTableGenerator = new com.p8499.paca.generator.database.module.CreateTableGenerator(project);
        com.p8499.paca.generator.database.module.DropTableGenerator dropTableGenerator = new com.p8499.paca.generator.database.module.DropTableGenerator(project);
        com.p8499.paca.generator.database.module.CreateViewGenerator createViewGenerator = new com.p8499.paca.generator.database.module.CreateViewGenerator(project);
        com.p8499.paca.generator.database.module.CreateViewBaseGenerator createViewBaseGenerator = new com.p8499.paca.generator.database.module.CreateViewBaseGenerator(project);
        com.p8499.paca.generator.database.module.DropViewGenerator dropViewGenerator = new com.p8499.paca.generator.database.module.DropViewGenerator(project);
        List modules = (List) project.get("modules");
        for (int i = 0; i < modules.size(); i++) {
            createTableGenerator.writeTo(outputDir, i);
            dropTableGenerator.writeTo(outputDir, i);
            createViewGenerator.writeTo(outputDir, i);
            createViewBaseGenerator.writeTo(outputDir, i);
            dropViewGenerator.writeTo(outputDir, i);
        }
        com.p8499.paca.generator.database.datasource.CreateAllGenerator createAllGenerator = new com.p8499.paca.generator.database.datasource.CreateAllGenerator(project);
        com.p8499.paca.generator.database.datasource.DropAllGenerator dropAllGenerator = new com.p8499.paca.generator.database.datasource.DropAllGenerator(project);
        com.p8499.paca.generator.database.datasource.CreateTablesGenerator createTablesGenerator = new com.p8499.paca.generator.database.datasource.CreateTablesGenerator(project);
        com.p8499.paca.generator.database.datasource.DropTablesGenerator dropTablesGenerator = new com.p8499.paca.generator.database.datasource.DropTablesGenerator(project);
        com.p8499.paca.generator.database.datasource.CreateViewsGenerator createViewsGenerator = new com.p8499.paca.generator.database.datasource.CreateViewsGenerator(project);
        com.p8499.paca.generator.database.datasource.DropViewsGenerator dropViewsGenerator = new com.p8499.paca.generator.database.datasource.DropViewsGenerator(project);
        List datasources = (List) ((Map) project.get("envJtee")).get("datasources");
        for (int i = 0; i < datasources.size(); i++) {
            createAllGenerator.writeTo(outputDir, i);
            dropAllGenerator.writeTo(outputDir, i);
            createTablesGenerator.writeTo(outputDir, i);
            dropTablesGenerator.writeTo(outputDir, i);
            createViewsGenerator.writeTo(outputDir, i);
            dropViewsGenerator.writeTo(outputDir, i);
        }
    }

    public static void generateJtee(Map project, File outputDir) throws Exception {
        new com.p8499.paca.generator.jtee.BuildGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.WebGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.SpringContextGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.Log4j2Generator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.MybatisConfigGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.DatabaseGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.DefaultDateFormatterGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterLogicExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterConditionExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterOperandExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterSerializerGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.FilterDeserializerGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.OrderByExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.OrderByListExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.RangeExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.jtee.base.RangeListExprGenerator(project).writeTo(outputDir);
        com.p8499.paca.generator.jtee.module.BeanGenerator beanGenerator = new com.p8499.paca.generator.jtee.module.BeanGenerator(project);
        com.p8499.paca.generator.jtee.module.MaskGenerator maskGenerator = new com.p8499.paca.generator.jtee.module.MaskGenerator(project);
        com.p8499.paca.generator.jtee.module.MapperGenerator mapperGenerator = new com.p8499.paca.generator.jtee.module.MapperGenerator(project);
        com.p8499.paca.generator.jtee.module.ServiceGenerator serviceGenerator = new com.p8499.paca.generator.jtee.module.ServiceGenerator(project);
        com.p8499.paca.generator.jtee.module.ControllerBaseGenerator controllerBaseGenerator = new com.p8499.paca.generator.jtee.module.ControllerBaseGenerator(project);
        List modules = (List) project.get("modules");
        for (int i = 0; i < modules.size(); i++) {
            beanGenerator.writeTo(outputDir, i);
            maskGenerator.writeTo(outputDir, i);
            mapperGenerator.writeTo(outputDir, i);
            serviceGenerator.writeTo(outputDir, i);
            controllerBaseGenerator.writeTo(outputDir, i);
        }
    }

    public static void generateAndroid(Map project, File outputDir) throws Exception {
        new com.p8499.paca.generator.android.BuildGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.AndroidManifestGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.StringsGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.ConstantsGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.DefaultDateFormatterGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterLogicExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterConditionExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterOperandExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterSerializerGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.FilterDeserializerGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.OrderByExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.OrderByListExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.RangeExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.RangeListExprGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.RetrofitFactoryGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.CookieManagerGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.SerializableCookieGenerator(project).writeTo(outputDir);
        new com.p8499.paca.generator.android.base.PersistentCookieStoreGenerator(project).writeTo(outputDir);
        com.p8499.paca.generator.android.module.BeanGenerator beanGenerator = new com.p8499.paca.generator.android.module.BeanGenerator(project);
        com.p8499.paca.generator.android.module.MaskGenerator maskGenerator = new com.p8499.paca.generator.android.module.MaskGenerator(project);
        com.p8499.paca.generator.android.module.StubGenerator stubGenerator = new com.p8499.paca.generator.android.module.StubGenerator(project);
        com.p8499.paca.generator.android.module.ViewGenerator viewGenerator = new com.p8499.paca.generator.android.module.ViewGenerator(project);
        com.p8499.paca.generator.android.module.ListViewGenerator listViewGenerator = new com.p8499.paca.generator.android.module.ListViewGenerator(project);
        List modules = (List) project.get("modules");
        for (int i = 0; i < modules.size(); i++) {
            beanGenerator.writeTo(outputDir, i);
            maskGenerator.writeTo(outputDir, i);
            stubGenerator.writeTo(outputDir, i);
            viewGenerator.writeTo(outputDir, i);
            listViewGenerator.writeTo(outputDir, i);
        }
    }

    public static void generateTest(Map project, File outputDir) throws Exception {
        new com.p8499.paca.generator.test.ResetDataGenerator(project).writeTo(outputDir);
    }
}
