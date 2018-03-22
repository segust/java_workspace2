<%@page
	import="cn.edu.cqupt.service.qualification_management.InfoService"%>
<%@page import="cn.edu.cqupt.dao.InfoDAO"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>按设备统计</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/query_business/queryBusiness.css">
</head>

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> 
			<a href="ProductQueryServlet?operate=productDetailQuery">业务查询</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a href="StatisticsQueryServlet?operate=contractStatistic">统计查询</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp; 
			 <a href="StatisticsQueryServlet?operate=productStatistic">按设备统计</a>
		</div>
		<!-- 内容区 -->
		<div class="fare-data" id="fare-data">
			<div class="search_box">
				<form action="StatisticsQueryServlet?operate=productStatistic"
					method="post">
					<span>产品型号</span> <span><input type="text"
						name="productModel" /> </span> <span>产品单元</span> <span><input
						type="text" name="productUnit" /> </span> <span>军代室</span> <select
						name="keeper" id="keeper">
						<option value="all">请选择</option>
						<%
									String ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
									List<String> jdsList= new InfoService().getJDSNameList(ownedUnit);
									for(int i=0;i<jdsList.size();i++){
								 %>
						<option value="<%=jdsList.get(i) %>"><%=jdsList.get(i) %></option>
						<%
								} 
								%>
					</select> <input type="submit" value="查询"> <input type="button"
						value="导出" onclick="outAll_productstatistic();">
				</form>
			</div>
			<div id="whole"></div>
			<div id="detail_by_product">
				<table id="detail_by_product_table">
					<thead>
						<tr>
							<th>合同编号</th>
							<th>产品名称</th>
							<th>产品型号</th>
							<th>产品单元</th>
							<th>产品单价</th>
							<th>设备总数</th>
							<th>在库数量</th>
							<th>出库数量</th>
							<th>在库设备金额</th>
							<th>出库设备金额</th>
							<th>设备金额</th>
							<th>承制单位</th>
							<th>代储单位</th>
							<th>合同签订日期</th>
							<th>订购数量</th>
							<th>合同金额</th>
						</tr>
					</thead>
				</table>
				<div>
					<p></p>
					<input type="button" value="确定" class="scan-btn"
						style="float:right;" id="sure_product">
				</div>
			</div>
			<div class="research_box">
				<!-- <label>库存总量：</label> <label>出库总量：</label> <label>合同总金额：</label> <label>当前页合同总金额：</label>
				<label>库存总金额：</label> <label>当前页库存总金额：</label> -->
			</div>
			<div id="fare-table-box">
				<table class="fare-table" id="fare-table">
					<thead>
						<tr>
							<th><input type="checkbox" onclick="chooseAll();"
								id="checkbox_product">
							</th>
							<th>序号</th>
							<th>产品名称</th>
							<th>产品型号</th>
							<th>产品单元</th>
							<th>单价</th>
							<th>设备数量</th>
							<th>承制单位</th>
							<th>代储单位</th>
							<th>操作</th>
						</tr>
					</thead>
					<% 
							List<Map<String,String>> T=(ArrayList<Map<String,String>>)request.getAttribute("message");
							int curPageNum=(Integer)request.getAttribute("curPageNum");
							int pageSize=(Integer)request.getAttribute("pageSize");
							int orderNumber=(curPageNum-1)*pageSize;
							for(int i=0;i<T.size();i++){
					%>
					<tbody>
						<tr class="contract-add-table-body">
							<td><input class="setTem" type="checkbox"
								name="checkbox_product"></td>
							<td><%=i+orderNumber+1%></td>
							<td><%=T.get(i).get("productName") %></td>
							<td><%=T.get(i).get("productModel") %></td>
							<td><%=T.get(i).get("productUnit") %></td>
							<td><%=T.get(i).get("productPrice") %></td>
							<td><%=T.get(i).get("productNum") %></td>
							<td><%=T.get(i).get("manufacturer") %></td>
							<td><%=T.get(i).get("keeper") %></td>
							<td><input type="button" value="查看合同信息"
								onclick="out_detail_product(this)"></td>
							<%}%>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 分页的盒子 -->
		<div class="page-box">
			<% 
				String productModel=(String)request.getAttribute("productModel");
				String productUnit=(String)request.getAttribute("productUnit");
				//Product_StatisticSum 按设备统计结果的个数
				int Product_StatisticSum=(Integer)request.getAttribute("sum");
				//根据总数和每页条数计算分多少页
				int totalPageNum = Product_StatisticSum%pageSize==0?Product_StatisticSum/pageSize:(Product_StatisticSum/pageSize+1);
				if(totalPageNum==0){
					totalPageNum=1;
					curPageNum=1;
				}
				//上一页的页码
				int prePageNum = curPageNum-1<1?1:curPageNum-1;
				//下一页的页码
				int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
			<span> <a
				href="StatisticsQueryServlet?operate=productStatistic&curPageNum=1&pageSize=<%=pageSize%>&productModel=<%=productModel%>&productUnit=<%=productUnit%>">首页</a>
				<a
				href="StatisticsQueryServlet?operate=productStatistic&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit%> ">&lt;&lt;</a>
				<span><%=curPageNum %></span> <a
				href="StatisticsQueryServlet?operate=productStatistic&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit%>">&gt;&gt;</a>
				<a
				href="StatisticsQueryServlet?operate=productStatistic&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit%>">尾页</a>
			</span> <span>跳到第</span> <input type="text" id="skipPageNum" /> <span>页</span>
			<a onclick="skipPage()">确定</a> <span>每页显示</span> <select
				name="selectPageSize" onchange='selectPageSize(this.value)'
				id="selectPageSize">
				<option value="10">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
			</select> <span>条记录，共</span> <label><%=totalPageNum %></label> <span>页</span>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="js/query_business/tools.js"></script>
	<script type="text/javascript"
		src="js/query_business/product_operate.js"></script>
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
					window.location.href="StatisticsQueryServlet?operate=productStatistic&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel=<%=productModel%>&productUnit=<%=productUnit%>";
			}
		</script>

	<!-- 用户选择每页显示的条数后提交到Servlet -->
	<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="StatisticsQueryServlet?operate=productStatistic&curPageNum=1&pageSize="+pageSize+"&productModel=<%=productModel%>&productUnit=<%=productUnit%>";
			}
		</script>

	<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
	<script type="text/javascript">
			var pageSize="<%=request.getParameter("pageSize")%>";
			if(pageSize=="null")
				document.getElementById("selectPageSize").value="10";
			else
				document.getElementById("selectPageSize").value=pageSize;
		</script>
</body>
</html>
