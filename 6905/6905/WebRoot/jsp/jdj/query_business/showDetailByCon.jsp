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

		<title>轮换更新查询操作明细</title>

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
	<%
								HashMap<String, Object> condition = (HashMap<String, Object>)request.getAttribute("condition");			
								int curPageNum=Integer.parseInt((String)condition.get("curPageNum"));
								int pageSize=Integer.parseInt((String)condition.get("pageSize"));
								//String sumStr = (String)request.getAttribute("sum");
								int sum=(Integer)request.getAttribute("sum");
								int totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
								if(totalPageNum==0){
									totalPageNum=1;
									curPageNum=1;
								}
								String productId = (String)request.getAttribute("productId");
								//上一页的页码
								int prePageNum = curPageNum-1<1?1:curPageNum-1;
								//下一页的页码
								int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
	 %>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/jdjleft.html"%>
		<div id="right">
			<!-- 二级标题开始 -->
			<div class="subName">
				<span>当前位置：</span>
      			<a href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10">业务查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10">轮换更新查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			操作明细
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
								<td></td>
								<td colspan="5">出库</td>
								<td></td>
								<td colspan="5">入库</td>
							</tr>
							<tr>
								<th>
									合同编号
								</th>
								<th>
									产品型号
								</th>
								<th>
									产品机号
								</th>
								<th>
									操作时间
								</th>
								<th>
									轮换更新状态
								</th>
								<th>
									
								</th>
								<th>
									合同编号
								</th>
								<th>
									产品型号
								</th>
								<th>
									产品机号
								</th>
								<th>
									操作时间
								</th>
								<th>
									轮换更新状态
								</th>
								
							</tr>
						</thead>
						<tbody>
							<%	
								List<Map<String, String>> recordList = (List<Map<String, String>>) request.getAttribute("recordList");
								if(recordList != null){
								for (int i = recordList.size() - 1 ; i >= 0; i--) {
										
							%>
							<tr class="contract-add-table-body">
							<%	
							if(recordList.get(i).size() < 10){
							%>
								<td><%=recordList.get(i).get("contractId")%></td>
								<td><%=recordList.get(i).get("productModel") %></td>
								<td><%=recordList.get(i).get("deviceNo")%></td>
								<td><%=recordList.get(i).get("operateTime")%></td>
								<td><%=recordList.get(i).get("proStatus")%></td>
								<td><img src=" img/arrow2.png"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								
							<%	
							}else{
							%>
								<td><%=recordList.get(i).get("contractId1")%></td>
								<td><%=recordList.get(i).get("productModel1") %></td>
								<td><%=recordList.get(i).get("deviceNo1")%></td>
								<td><%=recordList.get(i).get("operateTime1")%></td>
								<td><%=recordList.get(i).get("proStatus1")%></td>
								<td><img src=" img/arrow2.png"></td>
								<td><%=recordList.get(i).get("contractId2")%></td>
								<td><%=recordList.get(i).get("productModel2") %></td>
								<td><%=recordList.get(i).get("deviceNo2")%></td>
								<td><%=recordList.get(i).get("operateTime2")%></td>
								<td><%=recordList.get(i).get("proStatus2")%></td>
							<%	
							}
							%>
							</tr>
							<%
									}
									}
								%>
						</tbody>
					</table>
				</div>
				<!-- 表格结束 >-->
				
			</div>
			<!-- 分页的盒子开始 -->
				<div class="page-box">
					<span> <a
						href="UpdateQueryServlet?operate=updateQueryDetailByProductId&curPageNum=1&pageSize=<%=pageSize %>&productId=<%=productId %>">首页</a>
						<a
						href="UpdateQueryServlet?operate=updateQueryDetailByProductId&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productId=<%=productId %>">&lt;&lt;</a>
						<span><font color="black"><%=curPageNum %></font>
					</span> <a
						href="UpdateQueryServlet?operate=updateQueryDetailByProductId&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productId=<%=productId %>">&gt;&gt;</a>
						<a
						href="UpdateQueryServlet?operate=updateQueryDetailByProductId&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productId=<%=productId %>">尾页</a>
					</span>
					<span><font color="black">跳到第</font>
					</span>
					<input type="text" id="skipPageNum" />
					<span><font color="black">页</font>
					</span>
					<a onclick='skipPage()'>确定</a>
					<span><font color="black">每页显示</font>
					</span>
					<select name="selectPageSize" onchange='selectPageSize(this.value)'
						id="selectPageSize">
						<option value="10">
							10
						</option>
						<option value="15">
							15
						</option>
						<option value="20">
							20
						</option>
					</select>
					<span><font color="black">条记录，共</font>
					</span>
					<label>
						<font color="black">1</font>
					</label>
					<span><font color="black">页</font>
					</span>
				</div>
			<!-- 分页的盒子结束 -->
			<!-- 内容区结束 -->
			</div>
			<%@include file="../../../ConstantHTML/jdjfoot.html" %>
	</body>
</html>
