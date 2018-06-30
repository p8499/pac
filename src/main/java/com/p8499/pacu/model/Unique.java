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
@JsonSerialize(using = Unique.Serializer.class)
public class Unique implements Serializable, TreeItemContainer {
    private TreeItem mTreeItem;
    public Uniques mParent;
    public final BooleanProperty mKey;
    public final BooleanProperty mSerial;
    public final ListProperty<String> mItems;

    public Unique() {
        mKey = new SimpleBooleanProperty();
        mSerial = new SimpleBooleanProperty();
        mItems = new SimpleListProperty(FXCollections.<String>observableArrayList());
    }

    public Unique(Object uniqueMap) {
        DocumentContext uniqueContext = JsonPath.parse(uniqueMap);
        mKey = new SimpleBooleanProperty(uniqueContext.read("$.key"));
        mSerial = new SimpleBooleanProperty(uniqueContext.read("$.serial"));
        List<String> itemList = new ArrayList<>();
        uniqueContext.<List<String>>read("$.items").forEach(item -> itemList.add(item));
        mItems = new SimpleListProperty<>(FXCollections.observableList(itemList));
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
        return StringUtils.join(mItems, ", ");
    }

    static class Serializer extends JsonSerializer<Unique> {
        @Override
        public void serialize(Unique value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("key");
            gen.writeBoolean(value.mKey.get());
            gen.writeFieldName("serial");
            gen.writeBoolean(value.mSerial.get());
            gen.writeFieldName("items");
            gen.writeObject(value.mItems.get());
            gen.writeEndObject();
        }
    }
}
