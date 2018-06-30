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
 * Created by Administrator on 6/14/2018.
 */
@JsonSerialize(using = Datasources.Serializer.class)
public class Datasources implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public EnvJtee mParent;
    public final ListProperty<Datasource> mDatasources;

    public Datasources() {
        mDatasources = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public Datasources(Object list) {
        DocumentContext datasourcesContext = JsonPath.parse(list);
        List<Datasource> datasourceList = new ArrayList<>();
        datasourcesContext.<List>read("$").forEach(datasourceMap -> {
            Datasource datasource = new Datasource(datasourceMap);
            datasource.mParent = this;
            datasourceList.add(datasource);
        });
        mDatasources = new SimpleListProperty<>(FXCollections.observableList(datasourceList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mDatasources.forEach(datasource -> mTreeItem.getChildren().add(datasource.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "Data Sources";
    }

    static class Serializer extends JsonSerializer<Datasources> {
        @Override
        public void serialize(Datasources value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Datasource datasource : value.mDatasources)
                gen.writeObject(datasource);
            gen.writeEndArray();
        }
    }
}
