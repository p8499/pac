package com.p8499.pacu.model;

import javafx.scene.control.TreeItem;

/**
 * Created by Administrator on 6/19/2018.
 */
public interface TreeItemContainer {
    TreeItem<String> getTreeItem();

    void refreshTreeItem();
}
