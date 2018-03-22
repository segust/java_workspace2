<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

<title>合同管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/transact_business/transact.css" >
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<link rel="stylesheet" href="css/transact_business/jquery-ui.min.css" >
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script src="js/transact_business/transact.js"></script>
<script src="js/qualification_management/ajaxfileupload.js"></script>
<script src="js/transact_business/ajaxUpload.js"></script>

<style>
td [value="选择文件"] {
	border: 0px;
	color: white;
	height: 25px;
	width: 80px;
	font-weight: bold;
	background: #3CB371;
	cursor: pointer;
}

td [value="选择文件"]:hover {
	background-color: green;
}
</style>
</head>

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span>
			<c:if test="${isAdd }">
				<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;新增合同</c:if>
			<c:if test="${isUpdate }">
				<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
				<a href="ContractHandleServlet?operate=querycontract">查询合同</a>&nbsp;&nbsp;>&nbsp;&nbsp;编辑合同</c:if>
		</div>
		<!-- 内容区 -->
		<div class="content">
			<div id="xinzenghetong">
				<div id="contract-table-box">
					<c:if test="${isAdd }">
						<form action="ContractHandleServlet?operate=addcontract"
							method="post" enctype="multipart/form-data"
							onsubmit="return submitContractValidate();">
					</c:if>
					<c:if test="${isUpdate }">
						<form action="ContractHandleServlet?operate=updateform"
							method="post" onsubmit="return submitContractValidate()">
					</c:if>
					<table id="contract-table">
						<tbody>
							<c:if test="${isAdd }">
								<tr>
									<td style="border: 0px;">合同编号:</td>
									<td style="border: 0px;"><input type="text"
										name="contractid" id="contractid" onblur="iscIdExist(this);">
									</td>
									<td style="border: 0px;">订购数量:</td>
									<td style="border: 0px;"><input type="text"
										name="totalnumber" id="totalnumber">
									</td>
								</tr>
							</c:if>
							<c:if test="${isUpdate }">
								<tr>
									<td style="border: 0px;">合同编号:</td>
									<td style="border: 0px;"><input type="text"
										name="contractid" id="contractid" readonly="readonly"
										value="${contract.contractId }">
									</td>
									<td style="border: 0px;">订购数量:</td>
									<td style="border: 0px;"><input type="text"
										name="totalnumber" id="totalnumber"
										value="${contract.totalNumber }" />
									</td>
								</tr>
							</c:if>
							<tr>
								<td style="border: 0px;">合同金额:</td>
								<td style="border: 0px;"><input type="text"
									name="contractprice" id="contractprice"
									value="${contract.contractPrice }">
								</td>
								<td style="border: 0px;">军代室:</td>
								<td style="border: 0px;"><input type="text" name="jds"
									id="jds" value="${contract.JDS }">
								</td>
							</tr>
							<tr>
								<td style="border: 0px;">签订日期:</td>
								<td style="border: 0px;"><input type="text"
									class="sang_Calender" id="signdate" name="signdate"
									value="${contract.signDate }"> <script
										type="text/javascript" src="ConstantHTML/js/spdateTime.js"></script>
								</td>
								<td style="border: 0px;">采购单位:</td>
								<td style="border: 0px;"><input type="text" name="buyer"
									id="buyer" value="${contract.buyer }" />
								</td>
							</tr>
							<%-- <c:if test="${isAdd }">
								<tr>
									<td style="border: 0px;">上传附件:</td>
									<td colspan="2" style="border: 0px;">
										<!-- <input  type="file" name="attachment" /> --> <input
										type="text" size="20" name="upfile" id="upfile"
										style="padding:5px 0;border:1px dotted black"> <input
										type="button" value="选择文件" onclick="path.click()"> <input
										id="path" type="file" style="display:none" name="attachment"
										onchange="validateContractFile(this)">
									</td>
								</tr>
							</c:if> --%>
							<tr>
								<td style="border: 0px;"></td>
								<td style="border: 0px;"><c:if test="${isAdd }">
										<input class="submit" type="button" value="添加"
											onclick="uploadForm();">
									</c:if> <c:if test="${isUpdate }">
										<input class="submit" type="button" value="修改"
											onclick="updateContract()">
									</c:if>
								</td>
								<td style="border: 0px;"><input type="reset" value="重置" />
								</td>
							</tr>
						</tbody>
					</table>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
</body>
</html>
