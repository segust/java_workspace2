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

<title>修改用户</title>

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
			<a
				href="UserServlet?operation=manager&subOperation=searchAllUser&curPageNum=1&pageSize=10">用户管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			修改用户
		</div>
		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle"></div>
			<form
				action="UserServlet?operation=manager&subOperation=updateUser&curPageNum=<%=request.getParameter("curPageNum")%>&pageSize=<%=request.getParameter("pageSize")%>"
				method="post">
				<%
					String subOperation=request.getParameter("subOperation");
									String searchStr="";
									String searchType="";
									if(subOperation.equals("searchLikeUser")){
										searchStr=(String)request.getParameter("searchStr");
										searchType=(String)request.getParameter("searchType");
									}
				%>
				<input type="hidden" name="subOperation" value="<%=subOperation%>">
				<input type="hidden" name="searchStr" value="<%=searchStr%>">
				<input type="hidden" name="searchType" value="<%=searchType%>">
				<div class="manage-users-info">
					<div class="adduser-info">
						<div class="adduser-info-title">修改用户</div>
						<div class="adduser-info-eachrow">
							<div class="adduser-info-columntitle">账号</div>
							<div class="adduser-info-columncontent">
								<input type="text" name="identifyNum"
									value="<%=request.getParameter("identifyNum")%>">
							</div>
							<div class="adduser-info-columntitle" style="padding-left: 60px;">
								用户姓名</div>
							<div class="adduser-info-columncontent">
								<input type="text" name="username"
									value="<%=request.getParameter("username")%>">
							</div>
						</div>
						<div class="adduser-info-eachrow">
							<div class="adduser-info-columntitle">角色</div>
							<div class="adduser-info-columncontent">
								<select name="role" id="role">
									<%
										ArrayList<String> allRoleName=new RoleService().searchAllRoleName(); 
																				for(int i=0;i<allRoleName.size();i++){
									%>
									<option value="<%=allRoleName.get(i)%>"><%=allRoleName.get(i)%>
										<%
											}
										%>
									
								</select>
							</div>
							<div class="adduser-info-columntitle" style="padding-left: 60px;">
								职责</div>
							<div class="adduser-info-columncontent">
								<input type="text" name="duty"
									value="<%=request.getParameter("duty")%>">
							</div>
						</div>
						<div class="adduser-info-eachrow">
							<div class="adduser-info-columntitle">所属单位</div>
							<div class="adduser-info-columncontent" style="width: 580px;">
								<input type="text" name="ownedUnit"
									value="<%=request.getParameter("ownedUnit")%>">
							</div>
						</div>
						<div class="adduser-info-eachrow">
							<div class="adduser-info-columntitle">所属职权单位</div>
							<div class="adduser-info-columncontent" style="width: 580px;">
								<input type="text" name="authorityUnit"
									value="<%=request.getParameter("authorityUnit")%>">
							</div>
						</div>
						<div class="adduser-info-eachrow">
							<input type="submit" value="修改" class="return-btn"> <input
								type="reset" value="重置" class="return-btn"> <a
								href="UserServlet?operation=manager&subOperation=searchAllUser&curPageNum=<%=request.getParameter("curPageNum")%>&pageSize=<%=request.getParameter("pageSize")%>"
								class="return-btn">返回</a>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<!-- 控制角色下拉条和修改前用户的角色一致 -->
	<script type="text/javascript">
		var curRole="<%=request.getParameter("role")%>";
		document.getElementById("role").value = curRole;
	</script>
</body>
</html>
