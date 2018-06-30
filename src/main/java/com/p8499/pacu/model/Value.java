package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 6/13/2018.
 */
@JsonSerialize(using = Value.Serializer.class)
public class Value implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Values mParent;
    public final StringProperty mValue;
    public final StringProperty mCode;
    public final StringProperty mLabel;

    public Value() {
        mValue = new SimpleStringProperty();
        mCode = new SimpleStringProperty();
        mLabel = new SimpleStringProperty();
    }

    public Value(Object valueMap) {
        DocumentContext valueContext = JsonPath.parse(valueMap);
        mValue = new SimpleStringProperty(valueContext.read("$.value"));
        mCode = new SimpleStringProperty(valueContext.read("$.code"));
        mLabel = new SimpleStringProperty(valueContext.read("$.label"));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return mCode.get();
    }

    static class Serializer extends JsonSerializer<Value> {
        @Override
        public void serialize(Value value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("value");
            gen.writeString(value.mValue.get());
            gen.writeFieldName("code");
            gen.writeString(value.mCode.get());
            gen.writeFieldName("label");
            gen.writeString(value.mLabel.get());
            gen.writeEndObject();
        }
    }
}
