<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
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

<title>列表查询细节展示</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript" src="js/transact_business/transact.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/transact_business/transact.css" />
<link rel="stylesheet" type="text/css"
	href="css/transact_business/jquery.combobox.css" />
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/xcConfirm.css" />
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<script type="text/javascript">
		$(function() {
			var sugestions = new Array();
			sugestions = "${deviceNo}".replace('[', '').replace(']', '').split(
					',');
			$('.deviceNo').combobox(sugestions);
		});
	</script>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span> <a
				href="InWarehouseServlet?operate=listQueryApply">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="InWarehouseServlet?operate=listQueryApply">列表查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			列表详情
		</div>
		<div class="toptitle">
			<input type="button" class="search-btn" value="返回"
				onclick="window.location.href='<%=basePath%>InWarehouseServlet?operate=listQueryApply'"
				style="float: right;margin-top: 15px;margin-right: 10px;">
		</div>
		<div id="content">
			<!--产品查询 -->
			<div id="whole"></div>
			<div id="lunhuanchurukuguanli">
				<div id="deviceno_box">
					<form>
						<span>机号：</span> <span><input name="deviceNo" id="deviceNo"
							class="deviceNo"></span>
					</form>
				</div>
				<div id="xinzenglunhuanrukutable" style="margin-top:25px;">
					<form id="addApplyform">
						<table class="contract-add-table">
							<tbody>
								<tr>
									<td>产品型号：</td>
									<td><input name="productModel" id="productModel"
										readonly="true" class="unit" /></td>
									<td>设备类型：</td>
									<td><input name="productType" id="productType"
										class="unit" /></td>
								</tr>
								<tr>
									<td>单元名称：</td>
									<td><input name="unitName" id="unitName" class="unit" /></td>
									<td>批次：</td>
									<td><input name="Batch" id="Batch" class="unit" /></td>
								</tr>
								<tr>
									<!--  <td>机号：</td>
									<td><input name="deviceNo" id="deviceNo" class="deviceNo"></td>-->
									<td>单价：</td>
									<td><input name="Price" id="Price" class="unit" /></td>
									<td>数量：</td>
									<td><input name="Num" id="Num" class="unit" /></td>
								</tr>
								<tr>
									<td>计量单位：</td>
									<td><input name="measure" id="measure" class="unit" /></td>
									<td>品名内码：</td>
									<td><input name="PMNM" id="PMNM" class="unit" /></td>
								</tr>
								<tr>
									<td>承制单位：</td>
									<td><input name="manufacturer" id="manufacturer"
										class="unit" /></td>
									<td>代储单位：</td>
									<td><input name="keeper" id="keeper" class="unit" /></td>
								</tr>
								<tr>

									<td>摆放位置：</td>
									<td><input name="location" id="location" class="unit" /></td>
									<td>存储期限</td>
									<td><input name="storageTime" id="storageTime"
										class="unit" /></td>
								</tr>
								<!-- <%=request.getParameter("N4")%> -->
								<tr>

									<td>企业定期维护时间</td>
									<td><input name="maintainCycle" id="maintainCycle"
										class="unit" /></td>
									<td>生产时间：</td>
									<td><input name="producedDate" id="producedDate"
										class="unit" /></td>
								</tr>

								<tr>

									<td>操作日期：</td>
									<td><input name="execDate" id="execDate" class="unit" /></td>
									<td>操作类型：</td>
									<td><input name="Means" id="Means" class="unit" /></td>
								</tr>
								<tr>

									<td>合同编号：</td>
									<td><input name="contractId" id="contractId" class="unit" /></td>
									<td>采购单位：</td>
									<td><input name="buyer" id="buyer" class="unit" /></td>
								</tr>
								<tr>

									<td>JD室：</td>
									<td><input name="JDS" id="JDS" class="unit" /></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>更新历史：</td>
									<td><input type="button1" class="search-btn" value="查看"
										onclick="searchupdatehistory()"></td>
									<td>维修历史：</td>
									<td><input type="button1" class="search-btn" value="查看"
										onclick="searchmaintainhistory('<%=basePath%>')"></td>
								</tr>
							</tbody>
						</table>
					</form>

					<div id="showupdatehistory">
						<table>
							<tr>
								<td colspan="2">出库产品</td>
								<td></td>
								<td colspan="2">替代入库产品</td>
							</tr>
							<tr>
								<td>产品型号</td>
								<td>机号</td>
								<td></td>
								<td>产品型号</td>
								<td>机号</td>
							</tr>
							<tr>
								<td><input id="newproductmodel" value=""
									class="inputborder"></td>
								<td><input id="newdeviceno" value="" class="inputborder"></td>
								<td>——></td>
								<td><input id="oldproductmodel" value=""
									class="inputborder"></td>
								<td><input id="olddeviceno" value="" class="inputborder"></td>
							</tr>
							<tr>
								<td colspan="3"></td>
								<td colspan="2"><input type="button1" value="确定"
									id="sure_updatehistory" class="search-btn"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<input type="hidden" id="inId"
				value="<%=request.getParameter("inId")%>"> <input
				type="hidden" id="outId" value="<%=request.getParameter("outId")%>">
			<input type="hidden" id="status"
				value="<%=request.getParameter("status")%>">
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
</body>
<script type="text/javascript" src="js/transact_business/jquery.js"></script>
<script type="text/javascript" src="js/transact_business/transact.js"></script>
<script type="text/javascript"
	src="js/transact_business/jquery.bgiframe.pack.js"></script>
<script type="text/javascript"
	src="js/transact_business/jquery.combobox_product.js"></script>
</html>
