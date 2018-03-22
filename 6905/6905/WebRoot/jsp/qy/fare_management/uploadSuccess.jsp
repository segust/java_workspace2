<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Attach"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>经费管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="js/fare_management/uploadAttach.js"></script>
<link rel="stylesheet" type="text/css"
	href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
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
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			上传成功
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!-- 费用统计信息界面 -->
			<%
				 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
				 int curPageNum=(Integer)request.getAttribute("curPageNum");
				 String builtType=(String)request.getAttribute("builtType");
	             String startTime=(String)request.getAttribute("startTime");
	             String endTime = (String)request.getAttribute("endTime");
	             String storeCompany = (String)request.getAttribute("storeCompany");
		    %>
			<div class="fare-data" id="fare-data">
				<div class="upload">
					<table class="upload-table">
						<tbody>
							<tr>
								<td><img src="img/fare_management/ok.png" width="100"
									height="100"> 上传成功<br> <span id="time">3</span>
									秒钟之后自动跳转<br> 如果不跳转，请点击下面链接<br> <a
									href="FareServlet?curPageNum=<%=curPageNum %>&pageSize=<%=pageSize %>&operate=attach&fareId=<%= request.getAttribute("fareId") %>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>">点我回到附件列表</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script type="text/javascript">  
		delayURL("FareServlet?curPageNum=<%=curPageNum %>&pageSize=<%=pageSize %>&builtType=<%=builtType %>&startTime=<%=startTime %>&endTime=<%=endTime %>&storeCompany=<%=storeCompany %>&operate=attach&fareId=<%= request.getAttribute("fareId") %>");  
	</script>
</body>
</html>



