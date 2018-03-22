<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>更新角色</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
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
			<a
				href="UserServlet?operation=manager&subOperation=searchAllUser&curPageNum=1&pageSize=10">管理员用户</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a href="UserServlet?operation=manager&subOperation=roleInfo">角色管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			修改角色
		</div>

		<!-- 内容区 -->
		<div class="content">
			<!--添加角色子页面-->
			<div class="toptitle"></div>
			<form action="UserServlet?operation=manager&subOperation=updateRole"
				method="post" class="addrole-form">
				<input type="hidden" name="roleId"
					value="<%=request.getParameter("roleId") %>">
				<h3>添加角色信息</h3>
				<p>
					<span> 角色名 <input type="text" name="roleName" id="roleName">
					</span>
				</p>
				<p>
					<span> 合同管理<input type="checkbox" name="contractManage">
					</span> <span> 业务查询<input type="checkbox" name="queryBusiness">
					</span> <span> 轮换更新<input type="checkbox" name="borrowUpdate">
					</span>
				</p>
				<p>
					<span> 存储维护<input type="checkbox" name="storeMantain">
					</span> <span> 实力统计<input type="checkbox" name="statistics">
					</span>
				</p>
				<p>
					<span> 资质管理<input type="checkbox" name="qualificationManage">
					</span> <span> 系统管理<input type="checkbox" name="systemManage">
					</span> <span> 用户管理<input type="checkbox" name="userManage">
					</span>
				</p>
				<p>
					<input type="submit" value="修改" class="return-btn"> <input
						type="reset" value="重置" class="return-btn"> <a
						href="UserServlet?operation=manager&subOperation=roleInfo"
						class="return-btn">返回</a>
				</p>
			</form>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>

	<!-- 给当前角色元素附初值 -->
	<script type="text/javascript">
		//获取传来的当前角色信息
		var curRoleInfo="<%=request.getParameter("curRoleInfo")%>";
		var curRoleInfoArray = new Array();
		curRoleInfoArray = curRoleInfo.split(",");
		document.getElementById("roleName").value = curRoleInfoArray[0];
		//获取checkbox元素
		var oInputs = document.getElementsByTagName("input");
		var oCheckboxes = [];
		for ( var i = 0; i < oInputs.length; i++) {
			if (oInputs[i].type == 'checkbox') {
				oCheckboxes.push(oInputs[i]);
			}
		}
		//根据传来的1,0参数设置checkbox选中与否
		var i = 0;
		for ( var j = 1; j < curRoleInfoArray.length; j++) {
			if (curRoleInfoArray[j] == "1") {
				oCheckboxes[i].checked = "checked";
			}
			i++;
		}
	</script>
</body>
</html>