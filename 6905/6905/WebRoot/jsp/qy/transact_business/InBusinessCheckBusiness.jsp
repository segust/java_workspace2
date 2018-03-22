<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>入库审核管理</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" />
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<style>
	.singlerow_form [value="确定导入"]{
		border: 0px;
        color: white;
        height: 25px;
        width: 80px;
        font-weight: bold;
        background: #3CB371;
        cursor: pointer;
	}

	.singlerow_form [value="确定导入"]:hover{
		background-color:green;
	}
	</style>
</head>

<body>
	<%@include file="../../../ConstantHTML/top.html" %>
	<%@include file="../../../ConstantHTML/left.html" %>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span>
			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="InWarehouseServlet?operate=queryApply">入库管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			入库审核管理
		</div>
		<div id="content">
			<!--发料单文件excel导入 -->
			<div id="daorufaliaodantable" class="singlerow_form">
				<form method="post" action="${pageContext.request.contextPath}/InWarehouseServlet?operate=importExcelForm" enctype="multipart/form-data" onsubmit="return format()">
					<p class="tips">导入审核文件：</p>
					<%-- <a class="search-btn" style="width:120px;" href="${pageContext.request.contextPath}/InWarehouseServlet?operate=importExcelForm">导入审核文件</a> --%>
					<!-- <input type="file" name="checkInApplyFile" accept=".xls"> -->
					
					<input id="upfile" type="text" size="20" name="upfile"  autocomplete="off" style="padding:5px 0;border:1px dotted black">

					<!-- <input type="button" value="选择文件"  onclick="path.click()">   -->
					<a class="file-wrap" href="#">
					浏览
					<input id="fileToUpload" type="file" name="checkInApplyFile" accept=".whms" onchange="upfile.value=this.value">
					</a>

					<input type="submit" value="确定导入">
					<p class="error-info"></p>
				</form>
			</div>  
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html" %>
	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="js/public/format-upload.js"></script>
	<script>
	    var addFlag="<%=request.getAttribute("msg")%>";
	    if(addFlag != "null")
	    	//alert(addFlag);
	    	window.wxc.xcConfirm(addFlag,"info",{
	    		onOK: function() {
	    			window.location.href = "InWarehouseServlet?operate=queryApply";
	    		}
	    	});
	</script>
</body>
</html>