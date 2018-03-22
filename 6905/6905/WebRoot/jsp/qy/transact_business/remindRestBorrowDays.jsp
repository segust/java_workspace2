<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>轮换提醒查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/query_business/queryBusiness.css">
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
      			<span>当前位置：</span>
      			业务查询&nbsp;&nbsp;>&nbsp;&nbsp;
      			设备信息&nbsp;&nbsp;>&nbsp;&nbsp;
      			设备汇总查询
			</div>	
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
      		<div class="search_box">
				<form action="ProductQueryServlet?operate=productCollectiveQuery" method="post">
					<span>剩余轮换天数</span><span><input type="text" name="restborrowdays"/></span>
			        <input type="submit" value="查询">
      			</form>
			</div>
			<div id="fare-table-box">
						<table class="fare-table" id="fare-table">
							<thead>
								<tr>
								<th>
									<input type="checkbox" onclick="chooseAll();" id="checkbox_product">
								</th>
								<th>序号</th>
								<th>产品型号</th>
								<th>产品单元</th>
								<th>批次</th>
								<th>机号</th>
								<th>单价</th>
								<th>数量</th>
								<th>操作日期</th>
								<th>剩余存放天数</th>
								<th>企业剩于维护天数</th>
								<th>操作类型</th>
								<th>存储期限</th>
								<th>企业下次维护日期</th>
								<th>承制单位</th>
								<th>代储单位</th>
								<th>备注</th>
								<th>操作</th>
								</tr>
							</thead>
							<tbody>
							
								<tr class="contract-add-table-body">
								<td><input class="setTem" type="checkbox" name="checkbox_product"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								</tr>
								


							</tbody>
						</table>
				</div>
			</div>
			<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
					//condition
					String productModel=request.getParameter("productModel");
					String productUnit=request.getParameter("productUnit");
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
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=1&pageSize=<%=pageSize%>&productModel=<%=productModel%>&productUnit=<%=productUnit %>" >首页</a>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>">&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>">&gt;&gt;</a>
			    <a href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=productModel%>&productUnit=<%=productUnit %>">尾页</a>
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
      <%@include file="../../../ConstantHTML/foot.html" %>
      <script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	  <script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	  <script type="text/javascript" src="js/query_business/product_operate.js"></script>
      <!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	var productModel="<%=productModel%>";
			 	var productUnit="<%=productUnit%>";
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel="+productModel+"&productUnit="+productUnit;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var productModel="<%=productModel%>";
			 	var productUnit="<%=productUnit%>";
				window.location.href="ProductQueryServlet?operate=productCollectiveQuery&curPageNum=1&pageSize="+value+"&productModel="+productModel+"&productUnit="+productUnit;
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
