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

### 插入測試數據

插入測試數據有助於調試view、controller等各種功能

### 實現視圖字段

在產生的數據庫腳本中如果存在txt文件，則代表其中的sql有需要你手動修改的部分

此例中，將db01_create_view_Employee.txt、db01_create_view_Product.txt分別複製成文件
db01_create_view_Employee.sql、db01_create_view_Product.sql，區別只是其擴展名

然後修改文件如下

原始文件db01_create_view_Employee.txt

```Sql
/**
     * ID: Employee
     * Description: Employee master table
     */

CREATE VIEW F0101View AS SELECT 
/*Employee ID*/ t0.EMID EMID , 
/*Employee Status*/ t0.EMSTATUS EMSTATUS , 
/*Employee Gender*/ t0.EMGENDER EMGENDER , 
/*Employee Name*/ t0.EMNAME EMNAME , 
/*Total Sales Amount*/ ? EMAMOUNT FROM F0101 t0;
```

複製成db01_create_view_Employee.sql，並編輯爲

```Sql
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
LEFT JOIN (SELECT REEMID EMID, sum(IMPRICE*REQTY) AMOUNT FROM F4211 LEFT JOIN F4101 ON REIMID = IMID GROUP BY REEMID) t1 ON t1.EMID = t0.EMID;
```

原始文件db01_create_view_Product.txt

```Sql
/**
     * ID: Product
     * Description: Product master table
     */

CREATE VIEW F4101View AS SELECT 
/*Product ID*/ t0.IMID IMID , 
/*Product Name*/ t0.IMNAME IMNAME , 
/*Product Price*/ t0.IMPRICE IMPRICE , 
/*Total Sales Amount*/ ? IMAMOUNT FROM F4101 t0;
```

複製成db01_create_view_Employee.sql，並編輯爲

```Sql
/**
     * ID: Product
     * Description: Product master table
     */

CREATE VIEW F4101View AS SELECT 
/*Product ID*/ t0.IMID IMID , 
/*Product Name*/ t0.IMNAME IMNAME , 
/*Product Price*/ t0.IMPRICE IMPRICE , 
/*Total Sales Amount*/ t0.IMPRICE*nvl(t1.REQTY,0) IMAMOUNT FROM F4101 t0
LEFT JOIN (SELECT REIMID REIMID, sum(REQTY) REQTY FROM F4211 GROUP BY REIMID) t1 ON t1.REIMID = t0.IMID;
```

### 創建Views

當視圖全部實現後，即可運行create_views腳本  
當然如果沒有任何視圖字段需要手動實現，那麼可以直接跳過上一步而直接運行create_views  
在這種情況下，更簡便的方式則是直接運行create_all來取代create_tables和create_views

此例中，cd至Sales_scripts，使用sqlplus連接到數據庫db01，然後運行@db01_create_views.sql

### 實現Controller

scripts部分告一段落，接下去看Web項目  
PAC生成的J2EE項目，可以使用支持gradle的IDE（例如Idea）直接打開  
在這裏，PAC生成的是符合MVC設計規範的Web項目代碼  
大致分成以下幾種Class

* Bean（對象）
* Mask（對象掩碼，在只讀取、更新一個對象的某些字段而非全部字段的場景下使用）
* Mapper（mybatis的sql映射文件）
* Service（數據庫service類）
* ControllerBase（Spring的controller基類，需要繼承之來編寫實際controller）
* 其他輔助class（如日期轉換、查詢語句串行化、反串行化等class）

對於Controller來講，PAC生成的基類代碼已經使得業務邏輯層面的編程變得極爲簡單，但是各接口開放與不開放、具體邏輯仍需程序員自行定義  
故這裏設計爲程序員創建Controller繼承ControllerBase的形式

此例中，進入Sales/src/main/java/test/sales/controller，添加如下三個controller類

EmployeeController

```Java
package test.sales.controller;

//import ...

@RestController
public class EmployeeController extends EmployeeControllerBase {
    @Override
    protected Employee onGet(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer emid, EmployeeMask mask) throws Exception {
        //just fetch the record and return
        return employeeService.get(emid, mask);
    }

    @Override
    protected Employee onAdd(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer emid, Employee bean) throws Exception {
        //add and return the bean itself
        return employeeService.add(bean);
    }

    @Override
    protected Employee onUpdate(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer emid, Employee bean, EmployeeMask mask) throws Exception {
        //update and return the bean itself
        return employeeService.update(bean, mask);
    }

    @Override
    protected void onDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer emid) throws Exception {
        //here we simulate an HTTP-409 error code
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected Long onCount(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter) throws Exception {
        //count and return
        return employeeService.count(filter);
    }

    @Override
    protected List<Employee> onQuery(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter, OrderByListExpr orderByList, long start, long count, EmployeeMask mask) throws Exception {
        //query and return the list
        return employeeService.query(filter, orderByList, start, count, mask);
    }
   //inherit other methods
}
```

ProductController

```Java
package test.sales.controller;

//import ...

@RestController
public class ProductController extends ProductControllerBase {
    @Override
    protected Product onGet(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer imid, ProductMask mask) throws Exception {
        return productService.get(imid, mask);
    }

    @Override
    protected Product onAdd(HttpSession session, HttpServletRequest request, HttpServletResponse response, Product bean) throws Exception {
        return productService.add(bean);
    }

    @Override
    protected Product onUpdate(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer imid, Product bean, ProductMask mask) throws Exception {
        return productService.update(bean, mask);
    }

    @Override
    protected void onDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer imid) throws Exception {
        productService.delete(imid);
    }

    @Override
    protected Long onCount(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter) throws Exception {
        return productService.count(filter);
    }

    @Override
    protected List<Product> onQuery(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter, OrderByListExpr orderByList, long start, long count, ProductMask mask) throws Exception {
        return productService.query(filter, orderByList, start, count, mask);
    }
    //inherit other methods
}
```

RecordController

```Java
package test.sales.controller;

//import ...

@RestController
public class RecordController extends RecordControllerBase {
    @Override
    protected Record onGet(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer reid, RecordMask mask) throws Exception {
        return recordService.get(reid, mask);
    }

    @Override
    protected Record onAdd(HttpSession session, HttpServletRequest request, HttpServletResponse response, Record bean) throws Exception {
        return recordService.add(bean);
    }

    @Override
    protected Record onUpdate(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer reid, Record bean, RecordMask mask) throws Exception {
        return recordService.update(bean, mask);
    }

    @Override
    protected void onDelete(HttpSession session, HttpServletRequest request, HttpServletResponse response, Integer reid) throws Exception {
        recordService.delete(reid);
    }

    @Override
    protected Long onCount(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter) throws Exception {
        return recordService.count(filter);
    }

    @Override
    protected List<Record> onQuery(HttpSession session, HttpServletRequest request, HttpServletResponse response, FilterExpr filter, OrderByListExpr orderByList, long start, long count, RecordMask mask) throws Exception {
        return recordService.query(filter, orderByList, start, count, mask);
    }
    //inherit other methods
}
```

### 編譯J2EE項目

在生成的J2EE文件夾下有build.gradle文件，代表這是一個gradle項目  
如果你的項目中使用Oracle的數據庫，則需要自行前往oracle.com下載其最新JDBC驅動ojdbc6.jar，然後擺到J2EE根目錄下，與build.gradle平級

此例中，複製ojdbc6.jar至Sales  
再cd至Sales文件夾下，運行gradle build，然後可直接將build/libs/Sales.war部署到Tomcat的webapp文件夾

### 測試J2EE服務

使用Idea自帶REST Client或Postman或其他Web服務測試工具進行測試

測試add服務

| 參數或返回 | 值 |
| ---------- | -----------|
| HTTP method | POST |
| Host/port | http://127.0.0.1:80 |
| Path | Sales/api/Employee/1001 |
| Headers | Content-Type=application/json;charset=UTF-8 |
| Request Parameters |  |
| Request Body | {"emid":1001,"emstatus":0,"emgender":"M","emname":"Jovi Phoe"} |
| Response | {"emid":1001,"emstatus":0,"emgender":"M","emname":"Jovi Phoe"} |
| Response Code | 200 |

測試update服務

| 參數或返回 | 值 |
| ---------- | -----------|
| HTTP method | PUT |
| Host/port | http://127.0.0.1:80 |
| Path | Sales/api/Employee/1001 |
| Headers | Content-Type=application/json;charset=UTF-8 |
| Request Parameters | mask={"emstatus":true} |
| Request Body | {"emid":1001,"emstatus":1} |
| Response | {"emid":1001,"emstatus":1} |
| Response Code | 200 |

測試get服務

| 參數或返回 | 值 |
| ---------- | -----------|
| HTTP method | GET |
| Host/port | http://127.0.0.1:80 |
| Path | Sales/api/Employee/1001 |
| Headers | Content-Type=application/json;charset=UTF-8 |
| Request Parameters |  |
| Request Body | |
| Response | {"emid":1001,"emstatus":1,"emgender":"M","emname":"Jovi Phoe","emamount":0.0} |
| Response Code | 200 |

測試delete服務

| 參數或返回 | 值 |
| ---------- | -----------|
| HTTP method | DELETE |
| Host/port | http://127.0.0.1:80 |
| Path | Sales/api/Employee/1001 |
| Headers | Content-Type=application/json;charset=UTF-8 |
| Request Parameters |  |
| Request Body | |
| Response |  |
| Response Code | 403 |

See also
Android Client: https://github.com/p8499/Sales
