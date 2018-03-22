<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@page import="cn.edu.cqupt.beans.OutList"%>
<%@ page language="java" import="java.util.*,cn.edu.cqupt.beans.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
  <base href="<%=basePath%>">

  <title>出库管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/transact_business/transact.js"></script>
<script src="js/public/format-upload.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/transact_business/transact.css" />
<style>
.outtable-import-table tr:hover{background-color:white;}

.outtable-import-table [value="选择文件"]{
  border: 0px;
  color: white;
  height: 25px;
  width: 80px;
  font-weight: bold;
  background: #3CB371;
  cursor: pointer;
}
.outtable-import-table [value="选择文件"]:hover{
  background-color:green;
}

</style>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
  <%@include file="../../../ConstantHTML/top.html" %>
  <%@include file="../../../ConstantHTML/left.html" %>
  <div id="right">
   <!-- 二级标题 -->
   <div class="subName">
    <span>当前位置：</span>
    <a href="OutWarehouseServlet?operate=addOutList">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
    <a href="OutWarehouseServlet?operate=addOutList">出库管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
    导入发料单
  </div>
  <div id="content">
   <!--发料单文件word导入 -->
   <div id="diaobochukuguanli">
        <!-- <div class="toptitle">
      				<title>出库管理</title>
      			</div> -->
           <%
           boolean isCheck = (Boolean)request.getAttribute("ischeck");
           if(!isCheck) {
           %>
           <div id="daorufaliaodantable" style="margin-top: 5px;">
             <form action="OutWarehouseServlet?operate=importOutlist" method="post" enctype="multipart/form-data" onsubmit="return format()">
               <center>
                <table class="outtable-import-table">
                  <tbody>
                   <tr>
                    <td>导入发料单：</td>
                    <td>
                      <!-- <input type="file" > -->
                      <input type="text" size="20" name="upfile" id="upfile" style="padding:5px 0;border:1px dotted black">  
                      <a class="file-wrap" href="#">
                      浏览
                      <input id="fileToUpload" class="fileToUpload" type="file" name="attachment"  accept=".whms" onchange="upfile.value=this.value">
                      </a>
                    </td>
                  </tr>
                  <tr>
                    <td>提示：</td>
                    <td id="msg"><p class="error-info"></p></td>
                  </tr>
                  <tr>
                     <td>
                      <!-- <input type="button" value="提交" onclick="uploadOutList();"> -->
                      <input type="submit" value="提交">
                    </td>
                    <td><input type="reset" value="重置"></td>
                  </tr>

                </tbody>
              </table>
            </form>
            <%} %>

            <%
            if(isCheck) {
            %>
            导入成功！
            <div id="xinzengfaliaodan" class="outlist-box">
            	<%
              List<OutList> lists = (List<OutList>)request.getAttribute("queryLists");
              Date date = lists.get(0).getDate();
              %>
              <table class="add-outlist-table">
               <tr>
                <td></td>
                <th colspan="4">
                 <span>通信装备代储维修器材出库发料单</span>
               </th>
               <td></td>
               <td rowspan="6"><img alt="" src="img/transact_business/addlist.JPG"></td>
             </tr>
             <tr>
              <td>军代表室(章)：</td><td colspan="2"></td>
              <td>发料单位(章)：</td><td colspan="2"></td>
            </tr>
            <tr>
              <td>案由文号：</td>
              <td colspan="2"><input id="fileNo" value="<%=lists.get(0).getFileNo() %>" readonly="true"/></td>
              <td>发料单号：</td>
              <td><input id="listId" value="<%=lists.get(0).getListId() %>" readonly="true"/></td>
            </tr>
            <tr>
              <td>运输方式：</td>
              <td colspan="2"><input id="diliverMean" value="<%=lists.get(0).getDiliverMean() %>" readonly="true"/></td>
              <td>运单编号：</td>
              <td><input id="deliverNo" value="<%=lists.get(0).getDiliverMean() %>" readonly="true"/></td>
            </tr>
            <tr>
              <td>收料单位(章)：</td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <!-- 	<td>共<input id="totalNum"/>页 第<input id="curNum"/>页</td> -->
            </tr>
          </table>
          <table class="outlist-content-table" id="list-content">
            <tr>
              <th>序号</th>
              <th>品名代码</th>
              <th>名称型号</th>
              <th>单位</th>
              <th>品级</th>
              <th>通知数</th>
              <th>实发数</th>
              <th>件数</th>
              <th>单价(元)</th>
              <th>金额(元)</th>
              <th>备注</th>
            </tr>
            <%
            if(lists.size() > 0) {
            for(int i=0;i<lists.size();i++) {
            %>
            <tr>
             <td><%=i+1 %></td>
             <td><%=lists.get(i).getPMNM()%></td>
             <td><%=lists.get(i).getProductModel()%></td>
             <td><%=lists.get(i).getUnit() %></td>
             <td><%=lists.get(i).getQuanlity() %></td>
             <td><%=lists.get(i).getAskCount() %></td>
             <td><%=lists.get(i).getRealCount() %></td>
             <td><%=lists.get(i).getNum()%></td>
             <td><%=lists.get(i).getPrice()%></td>
             <td><%=lists.get(i).getMoney() %></td>
             <td><%=lists.get(i).getRemark()%></td>
           </tr>
           <%} 
         }
       }
       %>
     </table>
   </div>
 </div>
</div>
</div>
</div>
<%@include file="../../../ConstantHTML/foot.html" %>
<script src="js/transact_business/ajaxfileupload.js"></script>
<script  src="js/transact_business/ajaxUpload.js"></script>

</body>
</html>
