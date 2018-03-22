<%@page import="cn.edu.cqupt.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Parameter_Configuration"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>参数配置</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/sys_management/systemmanage.css" />
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
		<!-- 当前盒子 -->
		<div class="subName">
			<span>当前位置：</span> <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			参数配置
		</div>

		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle">
				<h1>参数配置</h1>
			</div>
			<%Parameter_Configuration T=(Parameter_Configuration)request.getAttribute("message"); %>
			<div id="canshupeizhi_table">
				<form
					action="ParameterConfigurationServlet?operate=parameter_configuration"
					method="post">
					<table class="tem-table" style="margin-top:50px;margin-left:50px;">
						<tr>
							<td>军代局定期抽查周期</td>
							<td><select name="alarm_cycle">
									<%-- <option class="add-box-option"><%=T.getAlarm_cycle() %></option> --%>
									<option value="1月">一个月</option>
									<option value="3月">三个月</option>
									<option value="6月">六个月</option>
									<option value="1年">十二个月</option>
							</select></td>
						</tr>
						<tr>
							<td>军代局定期抽查提前提示天数</td>
							<td><input class="setTem" type="text"
								name="alarm_ahead_days" value="<%=T.getAlarm_ahead_days() %>" />
							</td>
						</tr>
						<tr>
							<td>装备存放到期提前提示启动天数</td>
							<td><input class="setTem" type="text"
								name="store_ahead_days" value="<%=T.getStore_ahead_days() %>" />
							</td>
						</tr>
						<tr>
							<td>装备轮换更新出库到期提前提示启动天数</td>
							<td><input class="setTem" type="text" name="out_ahead_days"
								value="<%=T.getOut_ahead_days() %>" />
							</td>
						</tr>
					</table>
					<input class="save-btn" type="submit" value="保存"
						style="margin-left:435px;">
				</form>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
</body>
</html>
