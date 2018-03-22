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

		<title>轮换更新查询</title>

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
			href="css/query_business/queryBusiness.css">
		<script type="text/javascript"
			src="js/query_business/product_operate.js"></script>
	</head>

	<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/jdsleft.html"%>
		<div id="right">
			<!-- 二级标题开始 -->
			<div class="subName">
				<span>当前位置：</span>
      			业务查询&nbsp;&nbsp;>&nbsp;&nbsp;
      			轮换更新查询
			</div>
			<!-- 二级标题结束 -->

			<!-- 内容区开始 -->
			<div class="fare-data" id="fare-data">
			<!--显示子页面-->
				
			<!-- 表格开始 -->
				<div id="fare-table-box">
					<table class="fare-table" id="fare-table">
						<thead>
							<tr>
								<th>
									产品编码
								</th>
								<th>
									操作类型
								</th>
								<th>
									操作时间
								</th>
							</tr>
						</thead>
						<tbody>
							<%
					//得到当前查询条件
					String message = (String) request.getAttribute("message");
					if (message != null) {
								%>
							<td><%=message%></td>
							<%
									} else {
										List<Map<String, String>> recordList = (List<Map<String, String>>) request.getAttribute("recordList");
										for (int i = 0 ; i < recordList.size(); i++) {
										
								%>
							<tr class="contract-add-table-body">
								<td><%=recordList.get(i).get("productCode")%></td>
								<td><%=recordList.get(i).get("operateType")%></td>
								<td><%=recordList.get(i).get("operateTime")%></td>
							</tr>
							<%
									}
									}
								%>
						</tbody>
					</table>
				</div>
				<!-- 表格结束 "UpdateQueryServlet?operate=updateQueryByContractId&contractId="+contractId+"";>-->
			</div>
			<!-- 内容区结束 -->
			</div>
	</body>
</html>
