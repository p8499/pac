package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/20/2018.
 */
@JsonSerialize(using = Modules.Serializer.class)
public class Modules implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Project mParent;
    public final ListProperty<Module> mModules;

    public Modules() {
        mModules = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public Modules(Object list) {
        DocumentContext modulesContext = JsonPath.parse(list);
        List<Module> moduleList = new ArrayList<>();
        modulesContext.<List>read("$").forEach(moduleMap -> {
            Module module = new Module(moduleMap);
            module.mParent = this;
            moduleList.add(module);
        });
        mModules = new SimpleListProperty<>(FXCollections.observableList(moduleList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mModules.forEach(module -> mTreeItem.getChildren().add(module.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "Modules";
    }

    static class Serializer extends JsonSerializer<Modules> {
        @Override
        public void serialize(Modules value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Module module : value.mModules)
                gen.writeObject(module);
            gen.writeEndArray();
        }
    }
}
