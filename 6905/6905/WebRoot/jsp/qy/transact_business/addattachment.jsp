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

<title>添加合同附件</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/qualification_management/qualifymanagement.css">
</head>


<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a
				href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="ContractHandleServlet?operate=querycontract">查询合同</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			添加附件
		</div>
		<!-- 内容区 -->
		<div class="content">
			<form action="ContractHandleServlet?operate=uploadAttach"
				method="post" enctype="multipart/form-data">
				<table class="addqualify-table">
					<tbody>
						<tr>
							<td style="text-align: right">附件：</td>
							<td><input type="hidden" name="contractid"
								value="${contractid }"> <input type="file" name="file"
								accept=".pdf" onchange="validateContractFile(this)" accept="application/pdf"></td>
						</tr>
						<tr>
							<td colspan="2" style="padding-left: 60px;"><input type="submit"
								value="上传" class="addqualify-btn"> <input type="reset"
								value="重置" class="resetqualify-btn"></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script>
			//验证上传后文件是否是pdf
function validateContractFile(targ){
	var oFileName=targ.value,
		oStartIndex=oFileName.lastIndexOf(".")+1,
		oEndIndex=oFileName.length,
		oFileType=oFileName.substring(oStartIndex,oEndIndex);
	if(oFileType!="pdf"){
		var tempStr='<input type="hidden" name="contractid" value="${contractid }"> <input type="file" name="file" accept=".pdf" onchange="validateContractFile(this)">';
		$(targ.parentNode).html(tempStr);
		alert("请上传pdf文件");
	}
}
		</script>
</body>
</html>
