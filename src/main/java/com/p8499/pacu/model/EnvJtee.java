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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 6/13/2018.
 */
@JsonSerialize(using = EnvJtee.Serializer.class)
public class EnvJtee implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Project mParent;
    public final StringProperty mApp;
    public final StringProperty mBaseUrl;
    public final StringProperty mPackageBase;
    public final StringProperty mPackageBean;
    public final StringProperty mPackageMask;
    public final StringProperty mPackageMapper;
    public final StringProperty mPackageService;
    public final StringProperty mPackageControllerBase;
    public final ObjectProperty<Datasources> mDatasources;

    public EnvJtee() {
        mApp = new SimpleStringProperty();
        mBaseUrl = new SimpleStringProperty();
        mPackageBase = new SimpleStringProperty();
        mPackageBean = new SimpleStringProperty();
        mPackageMask = new SimpleStringProperty();
        mPackageMapper = new SimpleStringProperty();
        mPackageService = new SimpleStringProperty();
        mPackageControllerBase = new SimpleStringProperty();
        mDatasources = new SimpleObjectProperty<>(new Datasources());
        mDatasources.get().mParent = this;
    }

    public EnvJtee(Object map) {
        DocumentContext envJteeContext = JsonPath.parse(map);
        mApp = new SimpleStringProperty(envJteeContext.read("$.app"));
        mBaseUrl = new SimpleStringProperty(envJteeContext.read("$.baseUrl"));
        mPackageBase = new SimpleStringProperty(envJteeContext.read("$.packageBase"));
        mPackageBean = new SimpleStringProperty(envJteeContext.read("$.packageBean"));
        mPackageMask = new SimpleStringProperty(envJteeContext.read("$.packageMask"));
        mPackageMapper = new SimpleStringProperty(envJteeContext.read("$.packageMapper"));
        mPackageService = new SimpleStringProperty(envJteeContext.read("$.packageService"));
        mPackageControllerBase = new SimpleStringProperty(envJteeContext.read("$.packageControllerBase"));
        mDatasources = new SimpleObjectProperty<>(new Datasources(envJteeContext.read("$.datasources")));
        mDatasources.get().mParent = this;
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mTreeItem.getChildren().add(mDatasources.get().getTreeItem());
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "J2EE Environment";
    }

    static class Serializer extends JsonSerializer<EnvJtee> {
        @Override
        public void serialize(EnvJtee value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("app");
            gen.writeString(value.mApp.get());
            gen.writeFieldName("baseUrl");
            gen.writeString(value.mBaseUrl.get());
            gen.writeFieldName("packageBase");
            gen.writeString(value.mPackageBase.get());
            gen.writeFieldName("packageBean");
            gen.writeString(value.mPackageBean.get());
            gen.writeFieldName("packageMask");
            gen.writeString(value.mPackageMask.get());
            gen.writeFieldName("packageMapper");
            gen.writeString(value.mPackageMapper.get());
            gen.writeFieldName("packageService");
            gen.writeString(value.mPackageService.get());
            gen.writeFieldName("packageControllerBase");
            gen.writeString(value.mPackageControllerBase.get());
            gen.writeFieldName("datasources");
            gen.writeObject(value.mDatasources.get());
            gen.writeEndObject();
        }
    }
}
