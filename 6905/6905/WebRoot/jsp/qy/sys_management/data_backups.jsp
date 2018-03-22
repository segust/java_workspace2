<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
  <base href="<%=basePath%>">

  <title>数据备份</title>

  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">    
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
  <meta http-equiv="description" content="This is my page">
  <script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
  <script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
  <script type="text/javascript" src="js/sys_management/data_backup.js"></script>
  <link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="css/sys_management/systemmanage.css"/>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
  <%@include file="../../../ConstantHTML/top.html" %>
  <%@include file="../../../ConstantHTML/left.html" %>
  <div id="right">
    <!-- 位置 -->
    <div class="subName">
      <span>当前位置：</span>
      <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      <a href="SystemManagementServlet?operate=doDataBackup">数据备份</a>
    </div>
    <!-- 内容区 -->
    <div class="content">
      <h1>数据备份</h1>
      <p class="reminder">您可以在右方选择相应的操作</p>
    <div class="form-wrap">
  <!--<table class="tem-table" style="margin-top:100px;margin-left:150px;">
       <tr>
         <td style="width:80px;">文件名</td>
         <td><input class="setTem" type="text" name="filename"></td>
       </tr>
       <tr>
         <td></td>
         <td><input class="scan-btn" type="submit" value="备份"></td>
       </tr>
     </table> -->
     <ul>
      <li>

        <input type="button" value="数据备份" onclick="outData()">

      </li>
      <li><a href="#">数据浏览</a></li>

      <li>
      <form action="${pageContext.request.contextPath}/SystemManagementServlet?operate=LoadIn" enctype="multipart/form-data" method="post">
        <input type="text" size="20" name="upfile" id="upfile" style="padding:5px 0;border:1px dotted black;float: none;">  
        <input type="button" value="浏览" onclick="path.click()">  
        <input type="file" id="path" style="display:none" name="attachment" onchange="upfile.value=this.value">
        <input type="submit" value="数据恢复">
     </form>
      </li>
     </ul>
     <!--  <form action="${pageContext.request.contextPath}/SystemManagementServlet?operate=LoadIn" method="post" enctype="multipart/form-data">
     		<input type="file" name="attachment">
    		<input type="submit"  value="数据导入">
     </form> -->
 </div>

</div>

</div>
<%@include file="../../../ConstantHTML/foot.html" %>
</body>
</html>
