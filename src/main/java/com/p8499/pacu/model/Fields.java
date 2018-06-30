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
@JsonSerialize(using = Fields.Serializer.class)
public class Fields implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Module mParent;
    public final ListProperty<Field> mFields;

    public Fields() {
        mFields = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public Fields(Object list) {
        DocumentContext fieldsContext = JsonPath.parse(list);
        List<Field> fieldList = new ArrayList<>();
        fieldsContext.<List>read("$").forEach(fieldMap -> {
            Field field = new Field(fieldMap);
            field.mParent = this;
            fieldList.add(field);
        });
        mFields = new SimpleListProperty<>(FXCollections.observableList(fieldList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mFields.forEach(field -> mTreeItem.getChildren().add(field.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "Fields";
    }

    static class Serializer extends JsonSerializer<Fields> {
        @Override
        public void serialize(Fields value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Field field : value.mFields)
                gen.writeObject(field);
            gen.writeEndArray();
        }
    }
}
