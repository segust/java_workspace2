<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Attach"%>
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

<title>经费管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/fare_management/uploadAttach.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdsleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			导入经费
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!--查看附件子页面-->
			<%
				int curPageNum = Integer.parseInt(request
						.getParameter("curPageNum"));
				//pageSize 按多少条分页
				//首次进入页面时记得在路径上加上pageSize=10参数,Servlet跳转是也记得加上pageSize参数
				int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			%>
			<div class="add-box" id="fare-add-box">
				<div class="add-top"></div>
				<div class="file-list-box">
					<div class="file-table-box">
						<table>
						</table>
						<div>
							<form
								action="FareServlet?operate=import&curPageNum=1&pageSize=10"
								id="myform" enctype="multipart/form-data" name="files"
								onsubmit="return checkUpload()" method="post">
								<div class="file-btn-box">
									<img src="img/fare_management/import.png" width="100"
										height="100"> 导入文件：<input type="file" name="file1"
										id="file1">
									<p style="text-align: right;margin-top: 20px;">
										<input type="button" class="submit-btn" id="upload"
											onclick="testFile()" value="导入"> <a
											href="<%=request.getHeader("Referer")%>"
											class="export-fare-a">返回</a>
									</p>
								</div>
								<table id="file">
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdsfoot.html"%>
	<script type="text/javascript">
		function testFile() {
			var name = document.getElementById("file1").value;
			var str = name.lastIndexOf(".");
			var result = name.substring(str + 1, name.length);
			if (("whms" == result)) {
				document.getElementById("myform").submit();
			} else {
				if ("" == result) {
					window.wxc.xcConfirm("请勿导入空文件！ ","warning");
				} else {
					window.wxc.xcConfirm("文件不是whms格式，请重新选择 ！","warning");
				}
			}
		}
	</script>
</body>
</html>


