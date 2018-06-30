package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/13/2018.
 */
@JsonSerialize(using = Reference.Serializer.class)
public class Reference implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public References mParent;
    public final ListProperty<String> mDomestics;
    public final StringProperty mForeignModule;
    public final ListProperty<String> mForeigns;

    public Reference() {
        mDomestics = new SimpleListProperty(FXCollections.<String>observableArrayList());
        mForeignModule = new SimpleStringProperty();
        mForeigns = new SimpleListProperty(FXCollections.<String>observableArrayList());
    }

    public Reference(Object referenceMap) {
        DocumentContext referenceContext = JsonPath.parse(referenceMap);
        List<String> domesticList = new ArrayList<>();
        referenceContext.<List<String>>read("$.domestics").forEach(domestic -> domesticList.add(domestic));
        mDomestics = new SimpleListProperty<>(FXCollections.observableList(domesticList));
        mForeignModule = new SimpleStringProperty(referenceContext.read("$.foreignModule"));
        List<String> foreignList = new ArrayList<>();
        referenceContext.<List<String>>read("$.foreigns").forEach(foreign -> foreignList.add(foreign));
        mForeigns = new SimpleListProperty<>(FXCollections.observableList(foreignList));
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
        return StringUtils.join(mDomestics, ", ");
    }

    static class Serializer extends JsonSerializer<Reference> {
        @Override
        public void serialize(Reference value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("domestics");
            gen.writeObject(value.mDomestics.get());
            gen.writeFieldName("foreignModule");
            gen.writeString(value.mForeignModule.get());
            gen.writeFieldName("foreigns");
            gen.writeObject(value.mForeigns.get());
            gen.writeEndObject();
        }
    }
}
