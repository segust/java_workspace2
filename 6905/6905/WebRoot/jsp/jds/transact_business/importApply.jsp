<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>导入申请</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css">
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<style>
	.check-import-table tr:hover {
		background-color: white;
	}
	</style>
	<script type="text/javascript">
	function init() {
		var flag = ${isImport };
		if(flag) {
			var runStatus = ${runStatus };
			if(!runStatus) {
				window.wxc.xcConfirm("文件损坏或被篡改，请发出单位重新导出！","info");
			}
		}
	}
	window.onload = init();
	</script>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdsleft.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span> <a href="InWarehouseServlet?operate=importApply">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			申请管理
		</div>
		<div id="content">
			<!--审核文件excel导入 -->
			<div id="lunhuanshenheguanli">

				<div id="daorushenhewenjiantable">
					<form
					action="${pageContext.request.contextPath}/InWarehouseServlet?operate=importExcelFormForJDS"
					method="post" enctype="multipart/form-data" onsubmit="return format()">
					<table class="check-import-table">
						<tbody>
							<tr>
								<td>导入申请文件：</td>
								<td>
									<input type="text" size="20" name="upfile" id="upfile"style="padding:5px 0;border:1px dotted black" onchange="getType(this);"> 
									<a class="file-wrap" href="#">浏览
									<input id="fileToUpload" type="file" accept=".whms" name="attachment"	onchange="upfile.value=this.value" >
                                    </a>
									<input type="hidden" name="applyType" value="">
								</td>
							</tr>

							<tr>
								<td><input type="submit" value="导入"></td>
								<td><input type="reset" value="重置" /></td>
							</tr>

                           
						</tbody>
					</table>
					<p class="error-info"></p>
				</form>
			</div>
		</div>

	</div>
</div>
<%@include file="../../../ConstantHTML/jdsfoot.html"%>
<script src="js/transact_business/jdscheck.js"></script>
<script src="js/public/format-upload.js"></script>
</body>
</html>
