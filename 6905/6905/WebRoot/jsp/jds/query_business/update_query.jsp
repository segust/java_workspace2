<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
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
				<a
					href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10">业务查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
				<a
					href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10">轮换更新查询</a>
			</div>
			<!-- 二级标题结束 -->

			<!-- 内容区开始 -->
			<div class="fare-data" id="fare-data">
				<!--轮换更新查询子页面-->
				<!--搜索框开始-->
				<div class="search_box">
					<form
						action="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10"
						method="post">
						<span>产品型号</span>
						<span><input class="setTem" name="productModel" type="text" />
						</span>
						<span>轮换更新时间</span>
						<input type="text" class="sang_Calender"
								name="fromdate" tabindex="-1"/>
						<span>到</span>
						<input type="text" class="sang_Calender" name="todate" tabindex="-1"/>
						<span>查询类型</span>
						<span><select name="queryType">
								<option value="2"> 
									轮换
								</option>
								<option value="1">
									更新
								</option>
								</select>
						</span>
						<span>代储企业</span>
							<select name="keeper" id="keeper" >
								<option value="all">请选择</option>
								<c:forEach var="company" items="${companys }">
								<option value="${company }">${company }</option>
							</c:forEach>
						</select>
						     <input class="scan-btn" type="submit" value="查询">
					</form>
				</div>
				<!--搜索框结束-->

				<!-- 表格开始 -->
				<div class="">
					<div id="fare-table-box">
						<table class="fare-table" id="fare-table">
							<thead>
								<tr>
									<th>
										<input type="checkbox" onclick="chooseAll();"
											id="checkbox_product">
									</th>
									<th>
										序号
									</th>		
									<th>
										设备名称
									</th>
									<th>
										产品型号
									</th>
									<th>机号</th>
									<th>
										单元名称
									</th>
									<th>
										规格
									</th>
									<th>
										单价
									</th>
									<th>
										状态
									</th>
									<th>
										承制单位
									</th>
									<th>
										代储单位
									</th>
									<th>
										操作时间
									</th>
									<th>
										操作明细
									</th>
								</tr>
							</thead>
							<tbody>
								<%
					//得到当前查询条件
					HashMap<String, Object> condition = (HashMap<String, Object>)request.getAttribute("condition");
					//request.setAttribute("condition", condition);
					//curPageNum 当前页
					int curPageNum=Integer.parseInt((String)condition.get("curPageNum"));
					//pageSize 按多少条分页
					int pageSize=Integer.parseInt((String)condition.get("pageSize"));
					//productSum 全部设备信息的个数
					int productSum=(Integer)condition.get("sum");
					//根据总数和每页条数计算分多少页
					int totalPageNum = productSum%pageSize==0?productSum/pageSize:(productSum/pageSize+1);
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
						}
					//上一页的页码
					int prePageNum = curPageNum-1<1?1:curPageNum-1;
					//下一页的页码
					int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
					String productModel = (String)condition.get("productModel");
					String fromdate = (String)condition.get("fromdate");
					String todate = (String)condition.get("todate");
					String queryType = (String)condition.get("queryType");									
					List<Map<String, Object>> list = (List<Map<String, Object>>) request.getAttribute("list");
					if(list != null){										
					for (int i = 0 ; i < list.size(); i++) {
					String productId = (String)list.get(i).get("productId");
					String contractId = (String)list.get(i).get("contractId");		
					String productName = (String)list.get(i).get("productName");
					String productUnit = (String)list.get(i).get("productUnit");		
					String measureUnit = (String)list.get(i).get("measureUnit");
					String productPrice = (String)list.get(i).get("productPrice");		
					String manufacturer = (String)list.get(i).get("manufacturer");
					String deviceNo=(String)list.get(i).get("deviceNo");
					String keeper = (String)list.get(i).get("keeper");
					String status = (String)list.get(i).get("status");
					String productModelThisPage = (String)list.get(i).get("productModel");	
					String operateTime = (String)list.get(i).get("operateTime");
								%>
								<tr class="contract-add-table-body">
									<td>
										<input class="setTem" type="checkbox" name="checkbox_product" />
										<input class="setTem" type="hidden" <%=productId %> />
									</td>
									<td><%=(curPageNum-1)*pageSize + 1 + i%></td>
									<td><%=productName%></td>
									<td><%=productModelThisPage %></td>
									<td><%=deviceNo %></td>
									<td><%=productUnit%></td>
									<td><%=measureUnit%></td>
									<td><%=productPrice%></td>
									<td><%=status%></td>
									<td><%=manufacturer%></td>
									<td><%=keeper%></td>
									<td><%=operateTime %></td>			
									<td>
										<input type="button" value="按合同统计查看"
											onclick="{window.location.href='/6905/UpdateQueryServlet?operate=updateQueryDetailByProductId&curPageNum=1&pageSize=10&productId=<%=productId%>'}">
										<input type="button" value="按产品统计查看"
											onclick="{window.location.href='/6905/UpdateQueryServlet?operate=updateQueryDetailByProductModel&curPageNum=1&pageSize=10&productModel=<%=productModelThisPage%>'}">
									</td>
								</tr>
								<%
									}
									}
								%>
							</tbody>
						</table>
					</div>
				</div>
				<!-- 表格结束 --"UpdateQueryServlet?operate=updateQueryByContractId&contractId="+contractId+"";>
				

			<!-- 分页的盒子开始 -->
				<div class="page-box">
					<span> <a
						href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=<%=pageSize%>&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>">首页</a>
						<a
						href="UpdateQueryServlet?operate=updateQuery&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>">&lt;&lt;</a>
						<span><font color="black"><%=curPageNum%></font>
					</span> <a
						href="UpdateQueryServlet?operate=updateQuery&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>">&gt;&gt;</a>
						<a
						href="UpdateQueryServlet?operate=updateQuery&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>">尾页</a>
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
						<font color="black"><%=totalPageNum %></font>
					</label>
					<span><font color="black">页</font>
					</span>
				</div>
				<!-- 分页的盒子结束 -->
			</div>
		</div>
		<%@include file="../../../ConstantHTML/jdsfoot.html"%>
		<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
		<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="UpdateQueryServlet?operate=updateQuery&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>";		
			}
		</script>

		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize="+value+"&productModel=<%=productModel%>&fromdate=<%=fromdate%>&todate=<%=todate%>";
	}
</script>

		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
		var pageSize=<%=pageSize %>;
	if (pageSize == "null")
		document.getElementById("selectPageSize").value = "10";
	else
		document.getElementById("selectPageSize").value = pageSize;
</script>

	</body>
	<script type="text/javascript" src="ConstantHTML/js/dateTime.js"></script>
</html>
