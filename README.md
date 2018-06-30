# PAC Workbench
A tool for modeling and code generating.

## 啓動本程序

宕落本項目，定位到build/libs，雙擊運行pac-1.0-SNAPSHOT.jar  
或宕落本項目後，cd至build/libs，然後運行java -jar pac-1.0-SNAPSHOT.jar

## 生成項目代碼

在界面上選擇菜單Source->New->Sample Project以打開示例項目。也直接通過撳快捷鍵Ctrl+P來打開  
示例項目是一個簡易的Sales題材的項目

然後依次通過以下菜單生成3種最終項目代碼，並將它們擺在同一個目標文件夾內

* Project->Generate->Database Scripts
* Project->Generate->J2EE Project
* Project->Generate->Android Module

## 示例項目概覽

Sales項目一共包含三個業務對象，分別是

* Employee（銷售人員）
* Product（產品）
* Record（銷售記錄）

### 視圖字段
通過界面左側樹結點找到Employee對象的Fields，留意右側列表，它有一個字段的Source是view，即emamount。這代表emamount不屬於表字段，而是一個可通過邏輯計算出的視圖字段  
視圖字段可以用於對象的select/count等讀操作，但不可用於insert/update等寫操作  
然而具體如何計算emamount，則需要程序員在建模後通過sql來實現
同樣的，Product對象也有一個view字段，它是imamount

### 字段前綴
建議爲每個Module設定一個前綴，用於其每個Field的開頭  
此舉可以避免Java端編寫業務邏輯使用exists條件時的列名衝突

## 運行生成的代碼

首先你要將
