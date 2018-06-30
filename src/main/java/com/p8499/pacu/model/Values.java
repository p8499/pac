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
@JsonSerialize(using = Values.Serializer.class)
public class Values implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Field mParent;
    public final ListProperty<Value> mValues;

    public Values() {
        mValues = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public Values(Object list) {
        DocumentContext valuesContext = JsonPath.parse(list);
        List<Value> valueList = new ArrayList<>();
        valuesContext.<List>read("$").forEach(valueMap -> {
            Value value = new Value(valueMap);
            value.mParent = this;
            valueList.add(value);
        });
        mValues = new SimpleListProperty<>(FXCollections.observableList(valueList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mValues.forEach(value -> mTreeItem.getChildren().add(value.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "Values";
    }

    static class Serializer extends JsonSerializer<Values> {
        @Override
        public void serialize(Values value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Value val : value.mValues)
                gen.writeObject(val);
            gen.writeEndArray();
        }
    }
}
