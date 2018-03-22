<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>更新审核管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<style>
		.check-import-table tr:hover{background-color:white;}
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
      			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="BorrowServlet?operate=updateInOut">更新管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			更新审核管理
			</div>
       <div id="content">
       	     <!--审核文件excel导入 -->
        <div id="lunhuanshenheguanli">
       
          <div id="daorushenhewenjiantable">
			<center>
			<form action="${pageContext.request.contextPath}/UpdateServlet?operate=updateInOutCheck" method="post" enctype="multipart/form-data" onsubmit="return format()">
              <table class="check-import-table">
		<tbody>
			<tr>
				<td>导入审核文件：</td>
				<td>
					<!-- <input type="file" name="attachment"> -->
					 <input type="text" size="20" name="upfile" id="upfile" style="padding:5px 0;border:1px dotted black">  
        			 <a class="file-wrap" href="#">
                      浏览
					<input id="fileToUpload" type="file" name="attachment" accept=".whms" onchange="upfile.value=this.value">
				    </a>
				</td>
			</tr>
			
			<tr>
				<td><input type="submit" value="导入"></td>
				<td><input type="reset" value="重置"/></td>
			</tr>
			
		  
			<%-- <tr>
				<td>提示：</td><td>${requestScope.message}</td>
			</tr> --%>
			
		 	</tbody>
		 </table>
		 <p class="error-info"></p>
				</form>
            </div>  
            </div>
     
       </div>
       </div>
      <%@include file="../../../ConstantHTML/foot.html" %>
	  <script src="ConstantHTML/js/xcConfirm.js"></script>
      <script src="js/public/format-upload.js"></script>
      <script>
	    var addFlag="<%=request.getAttribute("msg")%>";
	    if(addFlag != "null")
	    	//alert(addFlag);
	    	window.wxc.xcConfirm(addFlag,"info",{
	    		onOK: function() {
	    			window.location.href = "InWarehouseServlet?operate=queryApply";
	    		}
	    	});
	</script>
  </body>
</html>
