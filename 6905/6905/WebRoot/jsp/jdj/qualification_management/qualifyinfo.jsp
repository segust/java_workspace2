<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Qualify"%>
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

		<title>部门信息</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" href="css/qualification_management/qualifymanagement.css">
		<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	</head>

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/jdjleft.html"%>
		<div id="right">
			<!-- 当前位置 -->
			<div class="subName">
				<span>当前位置：</span> 资质管理
			</div>
			<%
				String allJdsContentStr = (String) request
						.getAttribute("allRootLevelContentStr");
				String version = (String) request.getAttribute("version");
			%>
			<input type="hidden" value="<%=allJdsContentStr%>"
				id="allJdjContentStr">
			<input type="hidden" value="<%=version%>" id="version">
			<div class="content">
				<!-- 左边的树状目录盒子 -->
				<div class="treecontent-box" id="treecontent-box">


				</div>

				<!-- 右边的盒子 -->
				<div id="department-box">
					<div id="department-info-title">
						部门基本信息
					</div>
					<!-- 部门具体信息的盒子 -->
					<div id="department-info-box">
						<div class="department-info-eachrow">
							<div class="department-info-columntitle">
								部门负责人
							</div>
							<div class="department-info-columncontent" style="width: 80px;"
								id="departmentManager">
							</div>
							<div class="department-info-columntitle">
								分管领导
							</div>
							<div class="department-info-columncontent" style="width: 80px;"
								id="leader">
							</div>
						</div>
						<div class="department-info-eachrow">
							<div class="department-info-columntitle">
								部门名称
							</div>
							<div class="department-info-columncontent" id="departmentName">
							</div>
						</div>
						<div class="department-info-eachrow">
							<div class="department-info-columntitle"
								id="ownedDepartmentTitle">
								上级部门名称
							</div>
							<div class="department-info-columncontent"
								id="ownedDepartmentName">
							</div>
						</div>
						<div class="department-info-oprow">
							<input type="button" value="添加下一级部门" class="dpment-btn"
								id="addNextDepartButton" onclick="showAddForm();">
							<input type="button" value="删除当前部门" class="dpment-btn"
								id="deleteNextDepartButton" onclick="deleteDepartment();">
							<input type="button" value="修改当前部门" class="dpment-btn"
								id="updateNextDepartButton" onclick="showUpdateForm();">
							<input type="button" value="查看资质文件" class="dpment-btn"
								id="searchQualifyButton" onclick="showQualifyForm();">
							<input type="button" value="导入资质文件" class="dpment-btn"
								id="importQualifyButton" onclick="showImportQualifyForm();">
						</div>
					</div>
					<!-- 添加部门的盒子 -->
					<div id="add-department-box">
						<form>
							<div class="department-info-addeachrow">
							部门负责人：
							<input type="text" id="addDepartmentManager">
							</div>
							<div class="department-info-addeachrow">
							分管领导：
							<input type="text" id="addLeader">
							</div>
							<div class="department-info-addeachrow">
							部门名称：
							<input type="text" id="addLevelName">
							</div>
							<div class="department-info-addeachrow">
							上级部门名称：
							<input type="text" id="ownedLevelName"
								readonly="readonly">
							</div>
							<div class="department-info-addeachrow">
								<input type="button" value="添加" class="dpment-input-btn" onclick="addDepartment();">
								<input type="reset" value="重置" class="dpment-input-btn">
								<input type="button" value="返回" class="dpment-input-btn" onclick="returnToInfo();">
							</div>
						</form>
					</div>
					<!-- 修改部门的盒子 -->
					<div id="update-department-box">
						<form>
							<div class="department-info-addeachrow">
							部门负责人：
							<input type="text" id="updateDepartmentManager">
							</div>
							<div class="department-info-addeachrow">
							分管领导：
							<input type="text" id="updateLeader">
							</div>
							<div class="department-info-addeachrow">
							部门名称：
							<input type="text" id="updateLevelName">
							</div>
							<div class="department-info-addeachrow">
							上级部门名称：
							<input type="text" id="updateOwnedLevelName" >
							</div>
							<div class="department-info-addeachrow">
								<input type="button" value="修改" class="dpment-input-btn" onclick="updateDepartment();">
								<input type="reset" value="重置" class="dpment-input-btn">
								<input type="button" value="返回" class="dpment-input-btn" onclick="returnToInfo();">
							</div>
						</form>
					</div>
					<!-- 导入资质文件的盒子 -->
					<div id="import-qualify-box">
						<form action="" method="POST" enctype="multipart/form-data" class="import-qualify-form">
							<div class="department-info-addeachrow" id="importFile-input">
								请选择导入的压缩包：<input type="file" id="importFile" name="importFile" accept="application/zip " onchange="validateFileType(this);">
							</div>
							<div class="department-info-addeachrow">
								<input type="button" value="导入" class="dpment-input-btn" onclick="uploadQualifyZip();">
								<input type="reset" value="重置" class="dpment-input-btn">
								<input type="button" value="返回" class="dpment-input-btn" onclick="returnToInfo();">
							</div>
						</form>
					</div>
				</div>
				<!-- 查看资质文件的盒子 -->
				<div class="qualify-info-box" id="qualify-box-info">
					<!-- 上面操作和标题的盒子 -->
					<span id="curCompanyName"></span>
					<div class="up-qualifyinfo-op">
						<form action="QualifyServlet?operate=searchQualify&curPageNum=1&pageSize=10" method="post">
							文件名：<input type="text" name="searchStr" id="searchStr" value="请输入查询的文件名" onclick="if(this.value=='请输入查询的文件名'){this.value='';}" onblur="if(this.value=='') {this.value='请输入查询的文件名';}">
							年份：<input type="text" name="year" id="year" value="请输入查询的年份" onclick="if(this.value=='请输入查询的年份'){this.value='';}" onblur="if(this.value=='') {this.value='请输入查询的年份';}">
							文件类型：<select name="searchType" id="searchType">
								<option value="所有类型">
									所有类型
								</option>
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
							<input type="button" value="查询" class="search-btn" onclick="searchThroughButton();">
							<input type="button" value="返回" class="search-btn" onclick="returnToContentAndInfo();">
						</form>
					</div>
					<!-- 资质文件信息table开始 -->
					<div class="qualification-info">
						<table class="qualification-table">
							<thead>
								<tr class="qualification-table-head">
									<td>
										文件编号
									</td>
									<td style="width: 600px;">
										文件名
									</td>
									<td style="width: 100px;">
										文件类型
									</td>
									<td>
										年份
									</td>
									<td>
										操作
									</td>
								</tr>
							</thead>
							<tbody class="qualification-table-body" id="qualification-table-body">
							</tbody>
						</table>
					</div>
					<!--下面分页的盒子 -->
					<div class="page-box" id="page-box">
					    <span>
					    <a onclick="searchThroughPage('firstPage');">首页</a>
					    <a onclick="searchThroughPage('prePage');">&lt;&lt;</a>
					   	<span id="curPageNum"></span>
					    <a onclick="searchThroughPage('nextPage');">&gt;&gt;</a>
					    <a onclick="searchThroughPage('finalPage');">尾页</a>
					    </span>
				    	<span>跳到第</span>
				    	<input type="text" id="skipPageNum"/>
				    	<span>页</span>
				    	<a onclick="searchThroughPage('turnToPage');" >确定</a>
				   		<span>每页显示</span>
					    <select name="selectPageSize" onchange="searchThroughButton();" id="selectPageSize">
					        <option value ="10">10</option>
					        <option value ="15">15</option>
					        <option value="20">20</option>
					    </select>
				    	<span>条记录，共</span>
				    	<label id="pageSum"></label>
				    	<span>页</span>
					</div>
				</div>
			</div>
		</div>
		<%@include file="../../../ConstantHTML/jdjfoot.html"%>
		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
		<script src="ConstantHTML/js/xcConfirm.js"></script>
		<script type="text/javascript" src="js/qualification_management/ajaxfileupload.js"></script>
		<script type="text/javascript"
			src="js/qualification_management/createTreeContentAndShowInfo.js"></script>
		<!-- 树状目录显示所有指挥局 -->
		<script type="text/javascript">
			showStartContentByVersion();
		</script>
	</body>
</html>
