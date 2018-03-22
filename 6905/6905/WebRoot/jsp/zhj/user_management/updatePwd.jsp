<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.service.user_management.RoleService"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>修改密码</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script	src="js/user_management/user_management.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/user_management/usermanageindex.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 当前位置 -->
		<div id="subName" class="subName">
			<span>当前位置：</span> <a href="UserServlet?operation=peruserinfo">用户管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a href="UserServlet?operation=peruserinfo">个人用户</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			修改密码
		</div>
		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle"></div>
			<%String identifyNum=request.getParameter("identifyNum"); %>
			<form
				action="UserServlet?operation=updatePwd&identifyNum=<%=identifyNum %>"
				method="post" class="updatepwd-form">
				<h4>修改个人密码</h4>
				<p>
					<span class="left-span">原密码：</span> <span> <input
						type="password" id="prepwd" onblur="validateOldPwd();"
						onmouseout="validateOldPwd();"> </span> <span id="prepwdText"
						class="right-span"> </span>
				</p>
				<p>
					<span class="left-span">新密码：</span> <span> <input
						type="password" name="password" id="newpwd"
						onblur="validateEmptyPwd();" onmouseout="validateEmptyPwd();">
					</span> <span id="newpwdText" class="right-span"> </span>
				</p>
				<p>
					<span class="left-span">确认新密码：</span> <span> <input
						type="password" name="connewpwd" id="connewpwd"
						onblur="validateConPwd();" onmouseout="validateConPwd();">
					</span> <span id="connewpwdText" class="right-span"> </span>
				</p>
				<p>
					<input type="submit" value="修改" class="disabled-btn"
						id="updatePwdButton" disabled="disabled"> <input
						type="button" value="重置" class="return-btn"
						onclick="clearPwdInfo();"> <a
						href="UserServlet?operation=peruserinfo" class="return-btn">返回</a>
				</p>
			</form>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
</body>
</html>
