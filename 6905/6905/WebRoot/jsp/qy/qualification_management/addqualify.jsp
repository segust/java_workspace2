<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

		<title>添加资质文件</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="js/qualification_management/qualification_management.js"></script>
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/qualification_management/qualifymanagement.css">
	</head>

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/left.html"%>
		<div id="right">
			<!-- 当前位置 -->
			<div class="subName">
			<span>当前位置：</span>
      			<a href="QualifyServlet?operate=all&curPageNum=1&pageSize=10">资质管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			添加资质文件
			</div>
			<!-- 内容区 -->
			<div class="content">
				<div class="toptitle">
				</div>
				<%
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
				 %>
				<form action="QualifyServlet?operate=addQualify&curPageNum=<%=curPageNum %>&pageSize=<%=pageSize %>" method="post" enctype="multipart/form-data" class="addqualify-form">
					<p>
						文档：<span><input type="file" name="file" onchange="showTitle(this);"></span>
					</p>
					<p>
						文件名：<span><input type="text" name="qualifyTitle" id="qualifyTitle" placeholder="根据上传文件自动确定文件名" readonly="readonly"></span>
					</p>
					<p>
						文件类型：
						<span>
							<select name="qualifyType">
								<option value="资质申报文件">
									资质申报文件
								</option>
								<option value="资质审查报告">
									资质审查报告
								</option>
								<option value="代储合同">
									代储合同
								</option>
							</select>
						</span>
					</p>
					<p>
						年份：
						<span>
							<%
								Calendar c=Calendar.getInstance();
								int year=c.get(Calendar.YEAR);
							%>
							<input type="text" value="<%=year%>" name="year">
						</span>
					</p>
					<p>
						文件属性：
						<span>
							<select name="qualifyAttr">
								<option value="自查">自查</option>
								<option value="上报">上报</option>
							</select>
						</span>
					</p>
					<p>
						<input type="submit" value="上传" class="addqualify-btn">
						<input type="reset" value="重置" class="resetqualify-btn">
						<a href="QualifyServlet?operate=all&curPageNum=1&pageSize=10" class="resetqualify-btn">返回</a>
					</p>
				</form>
			</div>

		</div>
		<%@include file="../../../ConstantHTML/foot.html"%>
	</body>
</html>
