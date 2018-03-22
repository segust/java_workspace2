<%@page import="cn.edu.cqupt.beans.Contract"%>
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

<title>按合同统计</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/query_business/queryBusiness.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
</head>

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> 
			<a href="ProductQueryServlet?operate=productDetailQuery">业务查询</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a href="StatisticsQueryServlet?operate=contractStatistic">统计查询</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp; 
			<a href="StatisticsQueryServlet?operate=contractStatistic">按合同统计</a>
		</div>
		<!-- 内容区 -->
		<div class="fare-data" id="fare-data">
			<div class="search_box">
				<form action="StatisticsQueryServlet?operate=contractStatistic"
					method="post">
					<span>合同编号</span> <span><input type="text" name="contractId">
					</span> <span>合同年份</span> <span><input type="text" name="signDate"
						id="contractYear"> </span> <span>军代室</span> <span><input
						type="text" name="JDS"> </span> <input type="submit" value="查询">
					<input type="button" value="导出"
						onclick="outAll_contractstatistic();">
				</form>
			</div>
			<div id="whole"></div>
			<div id="detail_by_contract">
				<table id="detail_by_contract_table">
					<thead>
						<tr>
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
							<th>入库时间</th>
							<th>出库时间</th>
							<th>承制单位</th>
							<th>代储单位</th>
						</tr>
					</thead>
				</table>
				<div>
					<p></p>
					<input type="button" value="确定" class="scan-btn"
						style="float:right;" id="sure_contract">
				</div>
			</div>
			<div id="outinfo_by_product">
				<div>
					<p></p>
				</div>
				<table id="outinfo_by_product_table">
					<thead>
						<tr>
							<th>出库时间</th>
							<th>出库数量</th>
						</tr>
					</thead>
				</table>
				<div>
					<input type="button" value="确定" class="scan-btn" id="sure_outinfo">
				</div>
			</div>
			<div class="research_box">
				<%
 					%>
				<label> 合同总金额： </label><%=request.getAttribute("contractPriceSum") %>
				<label> 当前页合同总金额： </label><%=request.getAttribute("contractPricePageSum") %>
				<%
					%>
			</div>
			<div id="fare-table-box">
				<table class="fare-table" id="fare-table">
					<thead>
						<tr>
							<th style="width: 50px;"><input type="checkbox"
								onclick="chooseAll();" id="checkbox_product"></th>
							<th>序号</th>
							<th>合同编号</th>
							<th>订货数量</th>
							<th>合同金额</th>
							<th>军代室</th>
							<th>签订日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<% 
							List<Contract> contractList =(List<Contract>)request.getAttribute("contractList");
							//curPageNum 当前页
							int curPageNum=(Integer)request.getAttribute("curPageNum"); 
							//pageSize 按多少条分页
							int pageSize=(Integer)request.getAttribute("pageSize");
							int pageNumber=(curPageNum-1)*pageSize;
							for(int i = 0;i<contractList.size(); i++) {
						%>
						<tr class="contract-add-table-body">
							<td><input class="setTem" type="checkbox"
								name="checkbox_product"></td>
							<td><%=i+pageNumber+1 %></td>
							<td><%=contractList.get(i).getContractId() %></td>
							<td><%=contractList.get(i).getTotalNumber()%></td>
							<td><%=contractList.get(i).getContractPrice() %></td>
							<td><%=contractList.get(i).getJDS() %></td>
							<td><%=contractList.get(i).getSignDate() %></td>
							<td><input type="button" value="查看产品信息"
								name="operate_detail" onclick="out_detail_contract(this)">
							</td>
							<% }%>
						
					</tbody>
				</table>
			</div>
		</div>
		<!-- 分页的盒子 -->
		<div class="page-box">
			<% 
					String signDate=(String)request.getAttribute("signDate");
					String contractId=(String)request.getAttribute("contractId");
		            String JDS=(String)request.getAttribute("JDS");
					//Contract_StatisticSum 全部按合同统计结果的个数
					int Contract_StatisticSum=(Integer)request.getAttribute("sum");
					//根据总数和每页条数计算分多少页
					int totalPageNum = Contract_StatisticSum%pageSize==0?Contract_StatisticSum/pageSize:(Contract_StatisticSum/pageSize+1);
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
				href="StatisticsQueryServlet?operate=contractStatistic&curPageNum=1&pageSize=<%=pageSize%>&JDS=<%=JDS %>&contractId=<%=contractId%>&signDate=<%=signDate%>">首页</a>
				<a
				href="StatisticsQueryServlet?operate=contractStatistic&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&JDS=<%=JDS%>&contractId=<%=contractId%>&signDate=<%=signDate%>">&lt;&lt;</a>
				<span><%=curPageNum %></span> <a
				href="StatisticsQueryServlet?operate=contractStatistic&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&JDS=<%=JDS%>&contractId=<%=contractId%>&signDate=<%=signDate%>">&gt;&gt;</a>
				<a
				href="StatisticsQueryServlet?operate=contractStatistic&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&JDS=<%=JDS%>&contractId=<%=contractId%>&signDate=<%=signDate%>">尾页</a>
			</span> <span>跳到第</span> <input type="text" id="skipPageNum" /> <span>页</span>
			<a onclick='skipPage()'>确定</a> <span>每页显示</span> <select
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
	<script src="ConstantHTML/js/xcConfirm.js"></script>
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
					window.location.href = "StatisticsQueryServlet?operate=contractStatistic&curPageNum="
							+ skipPageNum
							+ "&pageSize="
							+ pageSize
							+ "&JDS=<%=JDS%>&contractId=<%=contractId%>&signDate=<%=signDate%>";
			} 
		</script>
	<!-- 用户选择每页显示的条数后提交到Servlet -->
	<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="StatisticsQueryServlet?operate=contractStatistic&curPageNum=1&pageSize="+value+"&JDS=<%=JDS%>&contractId=<%=contractId%>&signDate=<%=signDate%>";
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
