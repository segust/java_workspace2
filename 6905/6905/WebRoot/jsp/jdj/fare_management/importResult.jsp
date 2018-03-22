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
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/fare_management/uploadAttach.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			导入结果
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!-- 费用统计信息界面 -->
			<%
			  //获得当前页
				int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
				//pageSize 按多少条分页
				int pageSize=Integer.parseInt(request.getParameter("pageSize"));
				int all=(Integer)request.getAttribute("all");
				int addNum=(Integer)request.getAttribute("addNum");
				int updateNum=(Integer)request.getAttribute("updateNum");
		    %>
			<div class="fare-data" id="fare-data">
				<div class="upload">
					<table class="upload-table">
						<tbody>
							<tr>
								<td>
									<img src="img/fare_management/ok.png" width="100"
									height="100"> 尝试导入<%=all %>条记录
									<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									其中，<span style="color:red;"><%=addNum %></span>条添加成功
									<span style="color:red;"><%=updateNum %></span>条更新成功
									<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=all-addNum-updateNum %>条已存在，未导入！
									<br>
								<br> <a href="FareServlet?curPageNum=1&pageSize=10 ">查看经费列表</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
</body>
</html>