# PAC Workbench
A tool for modeling and code generating.

### 啓動本程序
宕落本項目，定位到build/libs，雙擊運行pac-1.0-SNAPSHOT.jar  
或宕落本項目後，cd至build/libs，然後運行java -jar pac-1.0-SNAPSHOT.jar

### 生成項目代碼
在界面上選擇菜單Source->New->Sample Project以打開示例項目。也直接通過撳快捷鍵Ctrl+P來打開  
示例項目是一個簡易的Sales題材的項目  
然後依次通過菜單Project->Generate->Database Scripts、Project->Generate->J2EE Project、Project->Generate->Android Module生成3種最終項目代碼，並將牠們擺在同一個目標文件夾內

### 示例項目概覽
Sales項目一共包含三個業務對象，分別是
* Employee（銷售人員）、
* Product（產品）
* Record（銷售記錄）

### 運行生成的代碼
首先你要將
