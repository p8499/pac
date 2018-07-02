# PAC Workbench
A tool for modeling and code generating.

## 啓動本程序

宕落本項目，定位到build/libs，雙擊運行pac-1.0-SNAPSHOT.jar  
或宕落本項目後，cd至build/libs，然後運行java -jar pac-1.0-SNAPSHOT.jar

## 生成項目代碼

在界面上選擇菜單Source->New->Sample Project以打開示例項目。也直接通過撳快捷鍵Ctrl+P來打開  
示例項目是一個簡易的Sales題材的項目

然後依次通過以下菜單生成3種最終項目代碼

* Project->Generate->Database Scripts（數據庫腳本）
* Project->Generate->J2EE Project（運行於Tomcat的Web項目基類）
* Project->Generate->Android Module（Android項目的模塊）

## 示例項目概覽

Sales項目一共包含三個業務對象，分別是

* Employee（銷售人員）
* Product（產品）
* Record（銷售記錄）

### 視圖字段

通過界面左側樹結點找到Employee對象的Fields，留意右側列表，它有一個字段的Source是view，即emamount。這代表emamount不屬於表字段，而是一個可通過邏輯計算出其值的視圖字段  
視圖字段可以用於對象的select/count等讀操作，但不可用於insert/update等寫操作  
然而具體如何計算emamount，則需要程序員在建模後通過sql來實現  
同樣的，Product對象也有一個view字段，它是imamount

此例中，Employee.emamount、Product.imamount分別代表單一銷售員的銷售總額和單一產品的銷售總額

### 字段前綴

建議爲每個Module設定一個前綴，用於其每個Field的開頭  
此舉可以避免Java端編寫業務邏輯使用exists條件時的列名衝突

此例中，Employee、Product和Record的字段前綴分別爲em、im和re

## 運行生成的代碼

要運行生成的代碼，需要按以下步驟執行

### 準備數據庫

在樹結點的Project->J2EE Environment->Datasources中可以看到項目使用的所有數據庫鏈接  
PAC支持多數據源，但是目前狀態下PAC只支持Oracle和Postgresql兩種數據庫產品  

此例中，你需要保證db01數據庫安裝正常、數據庫服務通暢以及username用戶擁有CREATE TABLE、CREATE VIEW、CREATE SEQUENCE和連接權限

### 創建Tables

PAC會爲每一個數據源創建一系列sql文件

* create_all
    * create_tables
        * create_table_Xxx（每個Module）
    * create_views
        * create_view_Xxx (每個Module，.sql或.txt)
* drop_all
    * drop_tables
        * drop_table_Xxx（每個Module）
    * drop_views
        * drop_view_Xxx（每個Module）

因爲視圖有可能需要你手動編輯，所以先運行create_tables而不是create_all

此例中，cd至Sales_scripts，使用sqlplus連接到數據庫db01，然後運行@db01_create_tables.sql

### 實現視圖字段

在產生的數據庫腳本中如果存在txt文件，則代表其中的sql有需要你手動修改的部分

此例中，將db01_create_view_Employee.txt、db01_create_view_Product.txt分別複製成文件
db01_create_view_Employee.sql、db01_create_view_Product.sql，區別只是其擴展名

然後修改文件如下
| db01_create_view_Employee.txt | db01_create_view_Employee.sql |
| ---------- | -----------|
| ```Sql
/**
     * ID: Employee
     * Description: Employee master table
     */

CREATE VIEW F0101View AS SELECT 
/*Employee ID*/ t0.EMID EMID , 
/*Employee Status*/ t0.EMSTATUS EMSTATUS , 
/*Employee Gender*/ t0.EMGENDER EMGENDER , 
/*Employee Name*/ t0.EMNAME EMNAME , 
/*Total Sales Amount*/ nvl(t1.AMOUNT,0) EMAMOUNT FROM F0101 t0
LEFT JOIN (SELECT REEMID EMID, sum(IMPRICE*REQTY) AMOUNT FROM F4211 LEFT JOIN F4101 ON REIMID = IMID GROUP BY REEMID) t1 ON t1.EMID=t0.EMID;
``` | ```Sql
/**
     * ID: Product
     * Description: Product master table
     */

CREATE VIEW F4101View AS SELECT 
/*Product ID*/ t0.IMID IMID , 
/*Product Name*/ t0.IMNAME IMNAME , 
/*Product Price*/ t0.IMPRICE IMPRICE , 
/*Total Sales Amount*/ t0.IMPRICE*nvl(t1.REQTY,0) IMAMOUNT FROM F4101 t0
LEFT JOIN (SELECT REIMID REIMID, sum(REQTY) REQTY FROM F4211 GROUP BY REIMID) t1 ON t1.REIMID=t0.IMID;
``` |


