<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Attach" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>合同管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="js/fare_management/uploadAttach.js"></script>
	<link rel="stylesheet" type="text/css" href="css/fare_management/fareManagement.css"/>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<script>  
	function delayURL(url) {  
	   var delay=document.getElementById("time").innerHTML;  
	   if(delay>0){  
	   delay--;  
	   document.getElementById("time").innerHTML=delay;  
	   }else{  
	   window.top.location.href=url;  
	   }  
	setTimeout("delayURL('" + url + "')", 1000);  
	//此处1000毫秒即每一秒跳转一次  
	}  
	</script>  
  </head>
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      <div id="right">
<!-- 二级标题 -->
      		<div class="subName">
				
			</div> 			
			<!-- 内容区 -->
		    <div id="content">
		    <!-- 费用统计信息界面 -->
            <div class="fare-data" id="fare-data">
		         <div class="upload">
            <table class="upload-table">
                <tbody>
                <tr>
                    <td>
		                <img src="img/fare_management/ok.png"  width="100" height="100">
		                上传成功<br>
		                <span id="time">3</span>
		                秒钟之后自动跳转<br>
		                如果不跳转，请点击下面链接<br>
		                <a href="/6905/ContractHandleServlet?operate=querycontract">点我回到合同列表</a>
                    </td>
                </tr>
                </tbody>
            </table>
            </div>
        </div>
 		</div>
 </div>
 <script type="text/javascript">  
	delayURL("/6905/ContractHandleServlet?operate=querycontract");  
</script>
   <%@include file="../../../ConstantHTML/foot.html" %>
</body>
</html>
                
				
           
