<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>添加角色</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/user_management/usermanageindex.css" />
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 当前位置 -->
		<div id="subName" class="subName">
			<span>当前位置：</span> <a href="UserServlet?operation=peruserinfo">用户管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a
				href="UserServlet?operation=manager&subOperation=searchAllUser&curPageNum=1&pageSize=10">管理员用户</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a href="UserServlet?operation=manager&subOperation=roleInfo">角色管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			添加角色
		</div>
		<!-- 内容区 -->
		<div class="content">
			<!--添加角色子页面-->
			<div class="toptitle"></div>
			<form action="UserServlet?operation=manager&subOperation=addRole"
				method="post" class="addrole-form">
				<h3>添加角色信息</h3>
				<p>
					<span> 角色名 <input type="text" name="roleName"> </span>
				</p>
				<p>
					<span> 业务办理<input type="checkbox" name="contractManage">
					</span> <span> 业务查询<input type="checkbox" name="queryBusiness">
					</span> <span> 轮换更新<input type="checkbox" name="borrowUpdate">
					</span>
				</p>
				<p>
					<span> 存储维护<input type="checkbox" name="storeMantain">
					</span> <span> 库房管理<input type="checkbox" name="warehouseManage">
					</span>
				</p>
				<p>
					<span> 实力统计<input type="checkbox" name="statistics">
					</span> <span> 经费管理<input type="checkbox" name="fareManage">
					</span>
				</p>
				<p>
					<span> 资质管理<input type="checkbox" name="qualificationManage">
					</span> <span> 系统管理<input type="checkbox" name="systemManage">
					</span> <span> 用户管理<input type="checkbox" name="userManage">
					</span>
				</p>
				<p>
					<input type="submit" value="添加" class="return-btn"> <input
						type="reset" value="重置" class="return-btn"> <a
						href="UserServlet?operation=manager&subOperation=roleInfo"
						class="return-btn">返回</a>
				</p>
			</form>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
</body>
</html>
