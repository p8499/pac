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
@JsonSerialize(using = References.Serializer.class)
public class References implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Module mParent;
    public final ListProperty<Reference> mReferences;

    public References() {
        mReferences = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public References(Object list) {
        DocumentContext referencesContext = JsonPath.parse(list);
        List<Reference> referenceList = new ArrayList<>();
        referencesContext.<List>read("$").forEach(referenceMap -> {
            Reference reference = new Reference(referenceMap);
            reference.mParent = this;
            referenceList.add(reference);
        });
        mReferences = new SimpleListProperty<>(FXCollections.observableList(referenceList));
    }

    public TreeItem getTreeItem() {
        if (mTreeItem == null) {
            mTreeItem = new TreeItem(this);
        }
        mTreeItem.getChildren().clear();
        mReferences.forEach(reference -> mTreeItem.getChildren().add(reference.getTreeItem()));
        return mTreeItem;
    }

    public void refreshTreeItem() {
        Event.fireEvent(getTreeItem(), new TreeItem.TreeModificationEvent(TreeItem.valueChangedEvent(), getTreeItem(), getTreeItem().getValue()));
    }

    @Override
    public String toString() {
        return "References";
    }

    static class Serializer extends JsonSerializer<References> {
        @Override
        public void serialize(References value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartArray();
            for (Reference reference : value.mReferences)
                gen.writeObject(reference);
            gen.writeEndArray();
        }
    }
}
