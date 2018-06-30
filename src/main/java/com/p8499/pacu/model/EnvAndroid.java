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
@JsonSerialize(using = EnvAndroid.Serializer.class)
public class EnvAndroid implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Project mParent;
    public final StringProperty mApp;
    public final StringProperty mPackageBase;
    public final StringProperty mPackageBean;
    public final StringProperty mPackageMask;
    public final StringProperty mPackageStub;
    public final StringProperty mPackageView;

    public EnvAndroid() {
        mApp = new SimpleStringProperty();
        mPackageBase = new SimpleStringProperty();
        mPackageBean = new SimpleStringProperty();
        mPackageMask = new SimpleStringProperty();
        mPackageStub = new SimpleStringProperty();
        mPackageView = new SimpleStringProperty();
    }

    public EnvAndroid(Object envAndroidMap) {
        DocumentContext envAndroidContext = JsonPath.parse(envAndroidMap);
        mApp = new SimpleStringProperty(envAndroidContext.read("$.app"));
        mPackageBase = new SimpleStringProperty(envAndroidContext.read("$.packageBase"));
        mPackageBean = new SimpleStringProperty(envAndroidContext.read("$.packageBean"));
        mPackageMask = new SimpleStringProperty(envAndroidContext.read("$.packageMask"));
        mPackageStub = new SimpleStringProperty(envAndroidContext.read("$.packageStub"));
        mPackageView = new SimpleStringProperty(envAndroidContext.read("$.packageView"));
    }

    public TreeItem<String> getTreeItem() {
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
        return "Android Environment";
    }

    static class Serializer extends JsonSerializer<EnvAndroid> {
        @Override
        public void serialize(EnvAndroid value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("app");
            gen.writeString(value.mApp.get());
            gen.writeFieldName("packageBase");
            gen.writeString(value.mPackageBase.get());
            gen.writeFieldName("packageBean");
            gen.writeString(value.mPackageBean.get());
            gen.writeFieldName("packageMask");
            gen.writeString(value.mPackageMask.get());
            gen.writeFieldName("packageStub");
            gen.writeString(value.mPackageStub.get());
            gen.writeFieldName("packageView");
            gen.writeString(value.mPackageView.get());
            gen.writeEndObject();
        }
    }
}
