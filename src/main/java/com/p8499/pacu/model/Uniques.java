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
@JsonSerialize(using = Uniques.Serializer.class)
public class Uniques implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Module mParent;
    public final ListProperty<Unique> mUniques;

    public Uniques() {
        mUniques = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public Uniques(Object list) {
        DocumentContext uniquesContext = JsonPath.parse(list);
        List<Unique> uniqueList = new ArrayList<>();
        uniquesContext.<List>read("$").forEach(uniqueMap -> {
            Unique unique = new Unique(uniqueMap);
            unique.mParent = this;
            uniqueList.add(unique);
        });
        mUniques = new SimpleListProperty<>(FXCollections.observableList(uniqueList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mUniques.forEach(unique -> mTreeItem.getChildren().add(unique.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "Uniques";
    }

    static class Serializer extends JsonSerializer<Uniques> {
        @Override
        public void serialize(Uniques value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Unique unique : value.mUniques)
                gen.writeObject(unique);
            gen.writeEndArray();
        }
    }
}
