package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;

import java.io.*;

/**
 * Created by Administrator on 6/13/2018.
 */
@JsonSerialize(using = Datasource.Serializer.class)
public class Datasource implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Datasources mParent;
    public final StringProperty mId;
    public final StringProperty mDatabaseType;
    public final StringProperty mUrl;
    public final StringProperty mUsername;
    public final StringProperty mPassword;

    public Datasource() {
        mId = new SimpleStringProperty();
        mDatabaseType = new SimpleStringProperty();
        mUrl = new SimpleStringProperty();
        mUsername = new SimpleStringProperty();
        mPassword = new SimpleStringProperty();
    }

    public Datasource(Object datasourceMap) {
        DocumentContext datasourceContext = JsonPath.parse(datasourceMap);
        mId = new SimpleStringProperty(datasourceContext.read("$.id"));
        mDatabaseType = new SimpleStringProperty(datasourceContext.read("$.databaseType"));
        mUrl = new SimpleStringProperty(datasourceContext.read("$.url"));
        mUsername = new SimpleStringProperty(datasourceContext.read("$.username"));
        mPassword = new SimpleStringProperty(datasourceContext.read("$.password"));
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
        return mId.get();
    }

    static class Serializer extends JsonSerializer<Datasource> {
        @Override
        public void serialize(Datasource value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("id");
            gen.writeString(value.mId.get());
            gen.writeFieldName("databaseType");
            gen.writeString(value.mDatabaseType.get());
            gen.writeFieldName("url");
            gen.writeString(value.mUrl.get());
            gen.writeFieldName("username");
            gen.writeString(value.mUsername.get());
            gen.writeFieldName("password");
            gen.writeString(value.mPassword.get());
            gen.writeEndObject();
        }
    }
}
