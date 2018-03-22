<%@page import="cn.edu.cqupt.beans.Temperature"%>
<%@page import="cn.edu.cqupt.service.warehouse_management.TemperatureService"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>修改温度</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/user_management/usermanageindex.css" />
		<link rel="stylesheet" type="text/css"
			href="css/warehouse_management/content.css" />
	</head>
	<body>
		<%@include file="../../../../ConstantHTML/top.html"%>
		<%@include file="../../../../ConstantHTML/left.html"%>
		<div id="right">
			<div id="subName" class="subName">
				<span>当前位置：</span>
   				库房管理&nbsp;&nbsp;>&nbsp;&nbsp;
   				温度管理&nbsp;&nbsp;>&nbsp;&nbsp;
   				修改温度
			</div>
			<!-- 内容区 -->
			<%
				int currentPageNum = Integer.parseInt(request.getParameter("currentPageNum"));
				int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			%>
			<div class="content">
				<!--温度管理列表子页面开始-->
					<!-- 搜索框开始 -->
					<div class="toptitle">
							<div class="toptitle-op">
								<div class="op-box">
								</div>
							</div>
						</div>
					</div>
					<!-- 搜索框结束 -->
					 <%
					  	//根据id查找该记录
					  	Long temperatureId = Long.parseLong(request.getParameter("temperatureId"));
					  	TemperatureService s = new TemperatureService();
					  	Temperature temperature = s.getOneTemperatureById(temperatureId);
					 %>
					<!-- 修改温度开始 -->
					<div class="add-box" id="tem-add-box">
						<form action="TemperatureServlet?operate=updateTemperature" method="post">
							<input type="hidden" name="temperatureId" value="<%=temperature.getTemperatureId() %>">
							<p>
								<label for="add-tem">温度:</label>
								<input id="add-tem" name="temperature" type="text" value="<%=temperature.getTemperature()%>"/>
							</p>
							<p>
								<label for="add-tim">时间:</label>
								<input id="add-tim" class="sang_Calender" name="curRecordDate" type="text" value="<%=temperature.getCurRecordDate()%>"/>
							</p>
							<p>
								<label for="add-cite">位置:</label> 
								<input id="add-cite" name="position" type="text" value="<%=temperature.getPosition()%>"/>
							</p>
							<p>
								<input class="submit" type="submit" value="修改" /> 
								<input class="reset" type="reset" value="重置" />
								<input type="button" value="返回" onclick="window.location.href='<%=basePath%>TemperatureServlet?operate=getAllTemperature&num=<%=currentPageNum %>&pageSize=<%=pageSize %>'">
							</p>
						</form>
					</div>
					<!-- 修改温度结束 -->
  				</div>
  		<%@include file="../../../../ConstantHTML/foot.html"%>
  		<script type="text/javascript" src="ConstantHTML/js/dateTime.js"></script>
  		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
  </body>
</html>
