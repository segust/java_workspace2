<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Role"%>
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

<title>角色信息列表</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/user_management/user_management.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/user_management/usermanageindex.css" />
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
			角色管理
		</div>

		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle">
				<!--
						<title>角色管理列表</title>
					-->
				<div class="userinfo-op">
					<form
						action="UserServlet?operation=manager&subOperation=searchLikeRole"
						method="post">
						<select name="searchType">
							<option value="role">角色</option>
						</select> <input type="text" name="searchStr"> <input type="submit"
							value="查询" class="search-btn"></input> <input type="button"
							value="添加角色" class="add-btn"
							onclick="window.location.href='jsp/zhj/user_management/addrole.jsp'"></input>
					</form>
				</div>
			</div>
			<div class="manage-users-info" id="manage-roles-info">
				<table id="role-table" class="user-table">
					<thead>
						<tr class="user-table-head">
							<td>编号</td>
							<td>角色名称</td>
							<td>业务办理</td>
							<td>业务查询</td>
							<td>轮换更新</td>
							<td>存储维护</td>
							<td>实力统计</td>
							<td>经费管理</td>
							<td>资质管理</td>
							<td>系统管理</td>
							<td>用户管理</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody class="user-table-body">
						<%
							ArrayList<Role> allRoleList = (ArrayList<Role>)request.getAttribute("allRoleList");
												int curRoleLength=allRoleList.size();
												int id=0;
												for (int i = 0; i < curRoleLength; i++) {
													id++;
						%>
						<tr>
							<td><%=id%></td>
							<td><%=allRoleList.get(i).getRole()%></td>
							<td>
								<%
									if (allRoleList.get(i).getContractManage() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getQueryBusiness() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getBorrowUpdate() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getStoreMantain() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getStatistics() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getFareManage() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getQualificationManage() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getSystemManage() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td>
								<%
									if (allRoleList.get(i).getUserManage() == 1) {
								%> <%="是"%> <%
 	} else {
 %> <%="否"%> <%
 	}
 %>
							</td>
							<td><input type="button" value="修改"
								onclick="updateRole(this,<%=allRoleList.get(i).getRoleId()%>,<%=this.getServletContext().getInitParameter("version")%>)">
								<input type="button" value="删除"
								onclick="deleteRole(this,<%=allRoleList.get(i).getRoleId()%>)">
							</td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
	<script type="text/javascript">
		var deleteFlag=<%=request.getAttribute("deleteFlag")%>;
		if(deleteFlag==1)
			window.wxc.xcConfirm("删除成功","success");
		else if(deleteFlag==0)
			window.wxc.xcConfirm("删除失败","error");
	</script>
	<script type="text/javascript">
		var addFlag=<%=request.getAttribute("addFlag")%>;
		var repeatFlag=<%=request.getAttribute("repeatFlag")%>;
		if(repeatFlag==1){
			window.wxc.xcConfirm("请勿重复提交","warning");
		}
		if(addFlag==1){
			window.wxc.xcConfirm("添加成功","success");
		}
		else if(addFlag==0){
			window.wxc.xcConfirm("添加失败","error");
		}
	</script>
	<script type="text/javascript">
		var updateFlag=<%=request.getAttribute("updateFlag")%>;
		if(updateFlag==1)
			window.wxc.xcConfirm("修改成功","success");
		else if(updateFlag==0)
			window.wxc.xcConfirm("修改失败","error");
	</script>
</body>
</html>