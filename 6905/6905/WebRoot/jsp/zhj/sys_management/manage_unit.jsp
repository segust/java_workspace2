<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.edu.cqupt.beans.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>添加数据</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="css/sys_management/systemmanage.css">
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript" src="js/sys_management/basedata.js"></script>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="ServiceOfBaseDataServlet?operate=select">基础数据库</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			管理单元
		</div>
		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle"></div>
			<div class="show_table">
				<form action="ServiceOfBaseDataServlet?operate=selectUnit"
					method="post">
					<table class="modify_basedata">
						<tr>
							<th>序号</th>
							<th>产品内码</th>
							<th>器材编码</th>
							<th>单价</th>
							<th>计量单位</th>
							<th>操作</th>
						</tr>
						<%
            ArrayList<Unit> unitList=(ArrayList<Unit>)request.getAttribute("UnitList");
            String FKPMNM = (String)request.getAttribute("FKPMNM");
            for(int i=0;i<unitList.size();i++){
            %>
						<tr>
							<td>
							<%=i+1%>
							<input type="hidden" value=<%=unitList.get(i).getId()%>>
							<input type="hidden" value=<%=FKPMNM%>>
							</td>
							<td><%=unitList.get(i).getPMNM()%></td>
							<td><%=unitList.get(i).getQCBM()%></td>
							<td><%=unitList.get(i).getCKDJ()%></td>
							<td><%=unitList.get(i).getJLDW()%></td>
							<td> <input value="删除"
								type="button" onclick="deleteUnit(this)"></td>
						</tr>
						<% 
             }
             %>
					</table>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
</body>
</html>
