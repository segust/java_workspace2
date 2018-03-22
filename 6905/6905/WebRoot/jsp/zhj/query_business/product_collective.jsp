<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>设备汇总查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/query_business/queryBusiness.css">
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script type="text/javascript" src="js/query_business/product_operate.js"></script>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/zhjleft.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
      			<span>当前位置：</span>
      			<a href="ProductQueryServlet?operate=productDetailQuery">业务查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">设备信息</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">设备汇总查询</a>
			</div>
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
      			<div class="search_box">
				<form action="ProductQueryServlet?operate=productCollectiveQuery" method="post">
					<p>
						<span>产品型号</span> 
			            <span><input class="setTem" type="text" name="productModel"/></span>
			            <span>产品单元</span>
			            <span><input class="setTem" type="text" name="productUnit"/></span>
			            <span>
			           		<input class="scan-btn" type="submit" value="查询">
			           		<input type="button" value="导出" class="scan-btn" onclick="outAll_productcollective();">
			           	</span>
      				</p>
      			</form>
				</div>
				<div class="research_box">
						<%-- <label>当前页产品总金额：</label><%=request.getAttribute("price") %>
						&nbsp;&nbsp; --%>
						<label>产品总金额：</label><%=request.getAttribute("totalPrice") %>
				</div>
				<div id="fare-table-box">
					<table class="fare-table" id="fare-table">
						<thead>
							<tr>
								<th rowspan="2">
									<input type="checkbox" id="checkbox_product" onclick="chooseAll();">
								</th>
								<th rowspan="2">序号</th>
								<th rowspan="2">产品型号</th>
								<th colspan="2">产品名称</th>
								<th rowspan="2">器材代码</th>
								<th rowspan="2">计量单位</th>
								<th rowspan="2">单价</th>
								<th rowspan="2">数量</th>
								<th rowspan="2">金额</th>
							</tr>
							<tr><th>整机名称</th><th>单元名称</th></tr>
						</thead>
						<% 
						   	List<HashMap<String, Object>> T = (List<HashMap<String, Object>>)request.getAttribute("message"); 
						   	for(int i = 0;i<T.size(); i++) {
				   		%>
						<tbody>
						<tr class="contract-add-table-body">
						<td><input class="setTem" type="checkbox" name="checkbox_product"/></td>
						<td><%=i+(Integer)request.getAttribute("pageSize")*((Integer)request.getAttribute("curPageNum")-1)+1 %></td>
						<td><%=T.get(i).get("productModel") %></td>
						<td><%=T.get(i).get("wholeName") %></td>
						<td><%=T.get(i).get("productUnit") %></td>
						<td><%=T.get(i).get("QCBM") %></td>
						<td><%=T.get(i).get("measureUnit") %></td>
						<td><%=T.get(i).get("productPrice") %></td>
						<td><%=T.get(i).get("num") %></td>
						<td><%=T.get(i).get("totalPrice") %></td>
						</tbody>
						<%} %>
					</table>
				</div>
			</div>
			<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
					//condition
					String productModel=(String)request.getAttribute("productModel");
					String productUnit=(String)request.getAttribute("productUnit");
					String keeper=(String)request.getAttribute("keeper");
					//curPageNum 当前页
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					//pageSize 按多少条分页
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					//userSum 全部设备信息的个数
					int productSum=(Integer)request.getAttribute("sum");
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
				%>
			    <span>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=1&pageSize=<%=pageSize%>&productModel=<%=productModel%>&productUnit=<%=productUnit %>&keeper=<%=keeper %>" >首页</a>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>&keeper=<%=keeper %>">&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>&keeper=<%=keeper %>">&gt;&gt;</a>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>&keeper=<%=keeper %>">尾页</a>
			    </span>
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage()' >确定</a>
		   		<span>每页显示</span>
			    <select name="selectPageSize" onchange='selectPageSize(this.value)' id="selectPageSize">
			        <option value ="10">10</option>
			        <option value ="15">15</option>
			        <option value="20">20</option>
			    </select>
		    	<span>条记录，共</span>
		    	<label><%=totalPageNum %></label>
		    	<span>页</span>
			</div>
		</div>
      <%@include file="../../../ConstantHTML/zhjfoot.html" %>
      <!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	var productModel="<%=productModel%>";
			 	var productUnit="<%=productUnit%>";
			 	var keeper="<%=keeper%>";
			 	if(skipPageNum<=0)
			 		//alert("请输入有效页面");
			 		window.wxc.xcConfirm("请输入有效页面","error");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		//alert("您输入的页面大于总页数");
			 		window.wxc.xcConfirm("您输入的页面大于总页数","error");
			 	else if(!/^(-|\+)?\d+$/.test(skipPageNum))
			 		//alert("输入的 ' "+skipPageNum+" '非法，请重新输入 ！ ");
			 		window.wxc.xcConfirm("输入的 ' "+skipPageNum+" '非法，请重新输入 ！ ","error");
			 	else
					window.location.href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel="+productModel+"&productUnit="+productUnit+"&keeper="+keeper;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var productModel="<%=productModel%>";
			 	var productUnit="<%=productUnit%>";
			 	var keeper="<%=keeper%>";
				window.location.href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=1&pageSize="+value+"&productModel="+productModel+"&productUnit="+productUnit+"&keeper="+keeper;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
			var pageSize=<%=pageSize %>;
			if(pageSize=="null")
				document.getElementById("selectPageSize").value="10";
			else
				document.getElementById("selectPageSize").value=pageSize;
		</script>
  </body>
</html>
