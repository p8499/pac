package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

import java.io.*;

/**
 * Created by Administrator on 6/13/2018.
 */
@JsonSerialize(using = Project.Serializer.class)
public class Project implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public final StringProperty mName;
    public final ObjectProperty<EnvJtee> mEnvJtee;
    public final ObjectProperty<EnvAndroid> mEnvAndroid;
    public final ObjectProperty<Modules> mModules;

    public Project() {
        mName = new SimpleStringProperty();
        mEnvJtee = new SimpleObjectProperty<>(new EnvJtee());
        mEnvAndroid = new SimpleObjectProperty<>(new EnvAndroid());
        mModules = new SimpleObjectProperty<>(new Modules());
    }

    public Project(Object map) {
        DocumentContext projectContext = JsonPath.parse(map);
        mName = new SimpleStringProperty(projectContext.read("$.name"));
        mEnvJtee = new SimpleObjectProperty<>(new EnvJtee(projectContext.read("$.envJtee")));
        mEnvJtee.get().mParent = this;
        mEnvAndroid = new SimpleObjectProperty<>(new EnvAndroid(projectContext.read("$.envAndroid")));
        mEnvAndroid.get().mParent = this;
        mModules = new SimpleObjectProperty<>(new Modules(projectContext.read("$.modules")));
        mModules.get().mParent = this;
    }

    public TreeItem<String> getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mTreeItem.getChildren().addAll(mEnvJtee.get().getTreeItem(), mEnvAndroid.get().getTreeItem(), mModules.get().getTreeItem());
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Object project = Configuration.defaultConfiguration().jsonProvider().parse(new FileInputStream(new File("C:/IdeaProjects/paca/build/libs/project.json")), "UTF-8");
        Project bean = new Project(project);
        System.out.println(bean);
    }

    @Override
    public String toString() {
        return mName.get();
    }

    static class Serializer extends JsonSerializer<Project> {
        @Override
        public void serialize(Project value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("name");
            gen.writeString(value.mName.get());
            gen.writeFieldName("envJtee");
            gen.writeObject(value.mEnvJtee.get());
            gen.writeFieldName("envAndroid");
            gen.writeObject(value.mEnvAndroid.get());
            gen.writeFieldName("modules");
            gen.writeObject(value.mModules.get());
            gen.writeEndObject();
        }
    }
}
