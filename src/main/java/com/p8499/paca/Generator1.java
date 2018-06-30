package com.p8499.paca;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * Created by Administrator on 6/6/2018.
 */
public abstract class Generator1 extends Generator {
    public Generator1(Map project) {
        super(project);
    }

    public Generator1 writeTo(File folder, Integer index) throws Exception {
        String content = getContent(index);
        if (content != null && content.length() > 0) {
            File path = getPath(folder, index);
            path.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(path);
            writer.write(getContent(index));
            writer.flush();
            writer.close();
        }
        return this;
    }

    public abstract File getPath(File folder, int index) throws Exception;

    public abstract String getContent(int index) throws Exception;
}
