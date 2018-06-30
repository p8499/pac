package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 6/20/2018.
 */
@JsonSerialize(using = Field.Serializer.class)
public class Field implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Fields mParent;
    public final StringProperty mSource;
    public final StringProperty mDatabaseColumn;
    public final StringProperty mDescription;
    public final BooleanProperty mNotnull;
    public final StringProperty mJavaType;
    public final IntegerProperty mStringLength;
    public final IntegerProperty mIntegerLength;
    public final IntegerProperty mFractionLength;
    public final StringProperty mDefaultValue;
    public final ObjectProperty<Values> mValues;

    public Field() {
        mSource = new SimpleStringProperty();
        mDatabaseColumn = new SimpleStringProperty();
        mDescription = new SimpleStringProperty();
        mNotnull = new SimpleBooleanProperty();
        mJavaType = new SimpleStringProperty();
        mStringLength = new SimpleIntegerProperty();
        mIntegerLength = new SimpleIntegerProperty();
        mFractionLength = new SimpleIntegerProperty();
        mDefaultValue = new SimpleStringProperty();
        mValues = new SimpleObjectProperty<>(new Values());
        mValues.get().mParent = this;
    }

    public Field(Object fieldMap) {
        DocumentContext fieldContext = JsonPath.parse(fieldMap);
        mSource = new SimpleStringProperty(fieldContext.read("$.source"));
        mDatabaseColumn = new SimpleStringProperty(fieldContext.read("$.databaseColumn"));
        mDescription = new SimpleStringProperty(fieldContext.read("$.description"));
        mNotnull = new SimpleBooleanProperty(fieldContext.read("$.notnull"));
        mJavaType = new SimpleStringProperty(fieldContext.read("$.javaType"));
        mStringLength = new SimpleIntegerProperty(fieldContext.read("$.stringLength"));
        mIntegerLength = new SimpleIntegerProperty(fieldContext.read("$.integerLength"));
        mFractionLength = new SimpleIntegerProperty(fieldContext.read("$.fractionLength"));
        mDefaultValue = new SimpleStringProperty(fieldContext.read("$.defaultValue"));
        mValues = new SimpleObjectProperty<>(new Values(fieldContext.read("$.values")));
        mValues.get().mParent = this;
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mTreeItem.getChildren().addAll(mValues.get().getTreeItem());
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return mDatabaseColumn.get();
    }

    static class Serializer extends JsonSerializer<Field> {
        @Override
        public void serialize(Field value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("source");
            gen.writeString(value.mSource.get());
            gen.writeFieldName("databaseColumn");
            gen.writeString(value.mDatabaseColumn.get());
            gen.writeFieldName("description");
            gen.writeString(value.mDescription.get());
            gen.writeFieldName("notnull");
            gen.writeBoolean(value.mNotnull.get());
            gen.writeFieldName("javaType");
            gen.writeString(value.mJavaType.get());
            gen.writeFieldName("stringLength");
            gen.writeNumber(value.mStringLength.get());
            gen.writeFieldName("integerLength");
            gen.writeNumber(value.mIntegerLength.get());
            gen.writeFieldName("fractionLength");
            gen.writeNumber(value.mFractionLength.get());
            gen.writeFieldName("defaultValue");
            gen.writeObject(value.mDefaultValue.get());
            gen.writeFieldName("values");
            gen.writeObject(value.mValues.get());
            gen.writeEndObject();
        }
    }
}
