<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>用户个人信息</title>

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
<link rel="stylesheet" href="css/user_management/usermanageindex.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 当前位置 -->
		<div class="subName">
			<span>当前位置：</span> <a href="UserServlet?operation=peruserinfo">用户管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			个人用户
		</div>
		<!-- 内容区 -->
		<%
				User user = (User) request.getAttribute("user");
			%>
		<div class="content">
			<div class="toptitle">
				<div class="userinfo-op">
					<%String identifyNum=user.getIdentifyNum(); %>
				</div>
			</div>
			<div class="manage-users-info">
				<div class="peruser-info">
					<div class="peruser-info-title">用户基本信息</div>
					<div class="peruser-info-eachrow">
						<div class="peruser-info-columntitle">账号</div>
						<div class="peruser-info-columncontent"><%=user.getIdentifyNum()%></div>
						<div class="peruser-info-columntitle" style="padding-left: 40px;">用户姓名</div>
						<div class="peruser-info-columncontent"><%=user.getName()%></div>
					</div>
					<div class="peruser-info-eachrow">
						<div class="peruser-info-columntitle">角色</div>
						<div class="peruser-info-columncontent"><%=user.getRole()%></div>
						<div class="peruser-info-columntitle" style="padding-left: 40px;">职责</div>
						<div class="peruser-info-columncontent"><%=user.getDuty()%></div>
					</div>
					<div class="peruser-info-eachrow">
						<div class="peruser-info-columntitle">单位</div>
						<div class="peruser-info-columncontent" style="width: 580px;"><%=user.getOwnedUnit()%></div>
					</div>
					<div class="peruser-info-eachrow">
						<div class="peruser-info-columntitle">所属职权单位</div>
						<div class="peruser-info-columncontent" style="width: 580px;"><%=user.getAuthorityUnit()%></div>
					</div>
					<div class="peruser-info-eachrow">
						<input type="button" value="修改密码" class="add-btn"
							onclick="window.location.href='jsp/zhj/user_management/updatePwd.jsp?identifyNum=<%=identifyNum %>'">
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
	<script type="text/javascript">
		var updateFlag=<%=request.getAttribute("updateFlag")%>;
		if(updateFlag==1)
			window.wxc.xcConfirm("修改成功","success",{onOk:function(){
				userLoginOut("<%=basePath%>");
			}});
		else if(updateFlag==0)
			window.wxc.xcConfirm("修改失败","error");
		else;
	</script>
</body>
</html>
