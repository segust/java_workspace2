<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

		<title>批量入库</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
		<script type="text/javascript" src="js/transact_business/addBacthPro.js"></script>
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/transact_business/transact.css" />
		<link rel="stylesheet" type="text/css"
			href="css/qualification_management/qualifymanagement.css">
		<script type="text/javascript">
		   var addFlag="<%=request.getAttribute("flag")%>";
			if(addFlag=="1") {
				alert("添加入库申请成功，请导出进行审核");
				window.location.href="InWarehouseServlet?operate=queryApply";
			}
			else if(addFlag=="0") {
				alert("添加入库申请不成功，请及时与管理员联系");
				window.location.href="ProductHandleServlet?operate=gotoproduct&contractid=<%=request.getAttribute("contractid")%>";
			}
			else
				;
		</script>
	</head>

	<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

	<body>
		<%
			List<String> result = (List<String>) request.getAttribute("result");
		%>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/left.html"%>
		<div id="right">
			<!-- 二级标题 -->
			<div class="subName">
				<span>当前位置：</span>
      			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ContractHandleServlet?operate=querycontract">查询合同</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<c:if test="${isAddBatch }"><a href="ProductHandleServlet?operate=queryproduct&contractid=<%=result.get(11)%>">申请入库</a>&nbsp;&nbsp;>&nbsp;&nbsp;</c:if>
      			批量入库
			</div>
			<!-- 内容区 -->
			<div id="content">
				<div id="xinzenghetong" style="margin-top: 10px;">
					<div id="xinzenghetongtable">
						<form action="AddBatchInApplyServlet?operate=addBatchInApply"
							method="post" onsubmit="return isEqualProductCount();">
							<table class="contract-add-table" style="text-align: center;">
								<tbody style="text-align: center;">
									<%
										if (result != null) {
									%>
									<tr>
										<td style="border: 0px;">
											合同编号：
										</td>
										<td style="border: 0px; text-align: center;">
											<input style="text-align: center;" name="contractId"
												value="<%=result.get(11)%>" />
										</td>
										<td style="border: 0px;">
											入库方式：
										</td>
										<td style="border: 0px;">
											<label>
												新入库
											</label>
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											产品名称：
										</td>
										<td style="border: 0px;">
											<input name="productName" style="text-align: center;"
												value="<%=result.get(0)%>" />
										</td>
										<td style="border: 0px;">
											产品型号：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="productModel"
												value="<%=result.get(1)%>" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											入库批次：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="batch" />
										</td>
										<td style="border: 0px;">
											机号：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="deviceNo" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											整机名称：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="wholeName" />
										</td>
										<td style="border: 0px;">
											单元名称：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="productUnit"
												value="<%=result.get(2)%>" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											计量单位：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="measureUnit"
												value="<%=result.get(3)%>" />
										</td>
										<td style="border: 0px;">
											单价：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="productPrice" value="<%=result.get(4)%>">
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											数量：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="num"
												value="<%=result.get(10)%>" id="num"/>
										</td>
										<td style="border: 0px;">
											品名内码
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="PMNM" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											承制单位：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="manufacturer" value="<%=result.get(6)%>"/>
										</td>
										<td style="border: 0px;">
											代储单位：
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="keeper" value="<%=result.get(7)%>"/>
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											摆放位置
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="location" />
										</td>
										<td style="border: 0px;">
											维护周期
										</td>
										<td style="border: 0px;">
											<select name="maintainCycle">
												<option value="30d">
													一个月
												</option>
												<option value="90d">
													三个月
												</option>
												<option value="180d">
													六个月
												</option>
												<option value="365d">
													一年
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											生产时间
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" class="sang_Calender"
												name="producedDate" />
										</td>
										<td style="border: 0px;">
											产品编号
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="productCode1" id="productCode1"
												value="<%=result.get(8)%>" />
											——
											<input style="text-align: center;" name="productCode2" id="productCode2"/>
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											存储期限
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="storageTime" />
										</td>
										<td style="border: 0px;">
											备注
										</td>
										<td style="border: 0px;">
											<input style="text-align: center;" name="remark" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											<input type="submit" class="search-btn" value="提交">
										</td>
										<td style="border: 0px;">
											<input type="reset" class="search-btn" value="重置" />
										</td>
									</tr>
									<%
										} else {
									%>
									<tr>
										<td>
											合同编号：
										</td>
										<td>
											<input name="contractId" />
										</td>
										<td>
											入库方式：
										</td>
										<td>
											<label>
												新入库
											</label>
										</td>
									</tr>
									<tr>
										<td>
											产品名称：
										</td>
										<td>
											<input name="productName" />
										</td>
										<td>
											产品型号：
										</td>
										<td>
											<input name="productModel" />
										</td>
									</tr>
									<tr>
										<td>
											整机名称：
										</td>
										<td>
											<input name="wholeName" />
										</td>
										<td>
											单元名称：
										</td>
										<td>
											<input name="productUnit" />
										</td>
									</tr>
									<tr>
										<td>
											计量单位：
										</td>
										<td>
											<input name="measureUnit" />
										</td>
										<td>
											单价：
										</td>
										<td>
											<input name="productPrice" />
										</td>
									</tr>
									<tr>
										<td>
											数量：
										</td>
										<td>
											<input name="num" id="num"/>
										</td>
										<td>
											品名内码
										</td>
										<td>
											<input name="PMNM" />
										</td>
									</tr>
									<tr>
										<td>
											承制单位：
										</td>
										<td>
											<input name="manufacturer" />
										</td>
										<td>
											代储单位：
										</td>
										<td>
											<input name="keeper" />
										</td>
									</tr>
									<tr>
										<td>
											摆放位置
										</td>
										<td>
											<input name="location" />
										</td>
										<td>
											维护周期
										</td>
										<td>
											<select name="maintainCycle">
												<option value="30d">
													一个月
												</option>
												<option value="90d">
													三个月
												</option>
												<option value="180d">
													六个月
												</option>
												<option value="365d">
													一年
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											生产时间
										</td>
										<td>
											<input name="producedDate" />
										</td>
										<td>
											产品编号
										</td>
										<td>
											<input name="productCode1" id="productCode1" />
											——
											<input name="productCode2" id="productCode2" />
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">
											<input type="submit" class="search-btn" value="提交">
										</td>
										<td>
											<input type="reset" class="search-btn" value="重置" />
										</td>
									</tr>
									<%
										}
									%>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
		<%@include file="../../../ConstantHTML/foot.html"%>
	</body>
	<script type="text/javascript" src="js/transact_business/dateTime.js"></script>
</html>
