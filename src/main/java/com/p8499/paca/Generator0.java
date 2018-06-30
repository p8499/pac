package com.p8499.paca;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * Created by Administrator on 6/6/2018.
 */
public abstract class Generator0 extends Generator {

    public Generator0(Map project) {
        super(project);
    }

    public Generator0 writeTo(File folder) throws Exception {
        String content = getContent();
        if (content != null && content.length() > 0) {
            File path = getPath(folder);
            path.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(path);
            writer.write(getContent());
            writer.flush();
            writer.close();
        }
        return this;
    }

    public abstract File getPath(File folder) throws Exception;

    public abstract String getContent() throws Exception;
}
