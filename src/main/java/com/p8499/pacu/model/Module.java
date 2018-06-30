package com.p8499.pacu.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 6/20/2018.
 */
@JsonSerialize(using = Module.Serializer.class)
public class Module implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Modules mParent;
    public final StringProperty mId;
    public final StringProperty mDescription;
    public final StringProperty mComment;
    public final StringProperty mDatasource;
    public final StringProperty mDatabaseTable;
    public final StringProperty mDatabaseView;
    public final StringProperty mJteeBeanAlias;
    public final StringProperty mJteeMaskAlias;
    public final StringProperty mJteeMapperAlias;
    public final StringProperty mJteeServiceAlias;
    public final StringProperty mJteeControllerBaseAlias;
    public final StringProperty mJteeControllerPath;
    public final StringProperty mJteeControllerAttachmentPath;
    public final StringProperty mAndroidBeanAlias;
    public final StringProperty mAndroidMaskAlias;
    public final StringProperty mAndroidStubAlias;
    public final StringProperty mAndroidViewAlias;
    public final StringProperty mAndroidListViewAlias;
    public final ObjectProperty<Fields> mFields;
    public final ObjectProperty<Uniques> mUniques;
    public final ObjectProperty<References> mReferences;

    public Module() {
        mId = new SimpleStringProperty();
        mDescription = new SimpleStringProperty();
        mComment = new SimpleStringProperty();
        mDatasource = new SimpleStringProperty();
        mDatabaseTable = new SimpleStringProperty();
        mDatabaseView = new SimpleStringProperty();
        mJteeBeanAlias = new SimpleStringProperty();
        mJteeMaskAlias = new SimpleStringProperty();
        mJteeMapperAlias = new SimpleStringProperty();
        mJteeServiceAlias = new SimpleStringProperty();
        mJteeControllerBaseAlias = new SimpleStringProperty();
        mJteeControllerPath = new SimpleStringProperty();
        mJteeControllerAttachmentPath = new SimpleStringProperty();
        mAndroidBeanAlias = new SimpleStringProperty();
        mAndroidMaskAlias = new SimpleStringProperty();
        mAndroidStubAlias = new SimpleStringProperty();
        mAndroidViewAlias = new SimpleStringProperty();
        mAndroidListViewAlias = new SimpleStringProperty();
        mFields = new SimpleObjectProperty<>(new Fields());
        mFields.get().mParent = this;
        mUniques = new SimpleObjectProperty<>(new Uniques());
        mUniques.get().mParent = this;
        mReferences = new SimpleObjectProperty<>(new References());
        mReferences.get().mParent = this;
    }

    public Module(Object moduleMap) {
        DocumentContext moduleContext = JsonPath.parse(moduleMap);
        mId = new SimpleStringProperty(moduleContext.read("$.id"));
        mDescription = new SimpleStringProperty(moduleContext.read("$.description"));
        mComment = new SimpleStringProperty(moduleContext.read("$.comment"));
        mDatasource = new SimpleStringProperty(moduleContext.read("$.datasource"));
        mDatabaseTable = new SimpleStringProperty(moduleContext.read("$.databaseTable"));
        mDatabaseView = new SimpleStringProperty(moduleContext.read("$.databaseView"));
        mJteeBeanAlias = new SimpleStringProperty(moduleContext.read("$.jteeBeanAlias"));
        mJteeMaskAlias = new SimpleStringProperty(moduleContext.read("$.jteeMaskAlias"));
        mJteeMapperAlias = new SimpleStringProperty(moduleContext.read("$.jteeMapperAlias"));
        mJteeServiceAlias = new SimpleStringProperty(moduleContext.read("$.jteeServiceAlias"));
        mJteeControllerBaseAlias = new SimpleStringProperty(moduleContext.read("$.jteeControllerBaseAlias"));
        mJteeControllerPath = new SimpleStringProperty(moduleContext.read("$.jteeControllerPath"));
        mJteeControllerAttachmentPath = new SimpleStringProperty(moduleContext.read("$.jteeControllerAttachmentPath"));
        mAndroidBeanAlias = new SimpleStringProperty(moduleContext.read("$.androidBeanAlias"));
        mAndroidMaskAlias = new SimpleStringProperty(moduleContext.read("$.androidMaskAlias"));
        mAndroidStubAlias = new SimpleStringProperty(moduleContext.read("$.androidStubAlias"));
        mAndroidViewAlias = new SimpleStringProperty(moduleContext.read("$.androidViewAlias"));
        mAndroidListViewAlias = new SimpleStringProperty(moduleContext.read("$.androidListViewAlias"));
        mFields = new SimpleObjectProperty<>(new Fields(moduleContext.read("$.fields")));
        mFields.get().mParent = this;
        mUniques = new SimpleObjectProperty<>(new Uniques(moduleContext.read("$.uniques")));
        mUniques.get().mParent = this;
        mReferences = new SimpleObjectProperty<>(new References(moduleContext.read("$.references")));
        mReferences.get().mParent = this;
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mTreeItem.getChildren().addAll(mFields.get().getTreeItem(), mUniques.get().getTreeItem(), mReferences.get().getTreeItem());
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return mId.get();
    }

    static class Serializer extends JsonSerializer<Module> {
        @Override
        public void serialize(Module value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("id");
            gen.writeString(value.mId.get());
            gen.writeFieldName("description");
            gen.writeString(value.mDescription.get());
            gen.writeFieldName("comment");
            gen.writeString(value.mComment.get());
            gen.writeFieldName("datasource");
            gen.writeString(value.mDatasource.get());
            gen.writeFieldName("databaseTable");
            gen.writeString(value.mDatabaseTable.get());
            gen.writeFieldName("databaseView");
            gen.writeString(value.mDatabaseView.get());
            gen.writeFieldName("jteeBeanAlias");
            gen.writeString(value.mJteeBeanAlias.get());
            gen.writeFieldName("jteeMaskAlias");
            gen.writeString(value.mJteeMaskAlias.get());
            gen.writeFieldName("jteeMapperAlias");
            gen.writeObject(value.mJteeMapperAlias.get());
            gen.writeFieldName("jteeServiceAlias");
            gen.writeObject(value.mJteeServiceAlias.get());
            gen.writeFieldName("jteeControllerBaseAlias");
            gen.writeString(value.mJteeControllerBaseAlias.get());
            gen.writeFieldName("jteeControllerPath");
            gen.writeObject(value.mJteeControllerPath.get());
            gen.writeFieldName("jteeControllerAttachmentPath");
            gen.writeObject(value.mJteeControllerAttachmentPath.get());
            gen.writeFieldName("androidBeanAlias");
            gen.writeString(value.mAndroidBeanAlias.get());
            gen.writeFieldName("androidMaskAlias");
            gen.writeObject(value.mAndroidMaskAlias.get());
            gen.writeFieldName("androidStubAlias");
            gen.writeObject(value.mAndroidStubAlias.get());
            gen.writeFieldName("androidViewAlias");
            gen.writeString(value.mAndroidViewAlias.get());
            gen.writeFieldName("androidListViewAlias");
            gen.writeString(value.mAndroidListViewAlias.get());
            gen.writeFieldName("fields");
            gen.writeObject(value.mFields.get());
            gen.writeFieldName("uniques");
            gen.writeObject(value.mUniques.get());
            gen.writeFieldName("references");
            gen.writeObject(value.mReferences.get());
            gen.writeEndObject();
        }
    }
}
