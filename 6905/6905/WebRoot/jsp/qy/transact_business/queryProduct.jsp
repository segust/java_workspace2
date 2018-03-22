<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>产品入库申请</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transact.css" />
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
  </head>
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
            		<!-- 二级标题 -->
      		<div class="subName">
			<span>当前位置：</span>
      			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ContractHandleServlet?operate=querycontract">查询合同</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			产品入库申请
			</div>
       <div id="content">
        <div class="search-box" >
            <form method="post" action="ContractHandleServlet?operate=querycontract">
                <span>产品型号</span> 
                <span><input class="setTem" name="productmodel" type="text"/></span>
                <span>单元名称</span> 
                <span><input class="setTem" name="unitname" type="text"/></span>
                <span><input class="search-btn" type="submit" value="查询产品" id="rukushenqing"></span>
			</form>
         </div>
       	     <!--入库管理 -->
        <div id="rukuguanli" style="margin-top: 10px;">
            <div id="contract-table-box">
				<table id="contract-table" class="contract-table">
				   <thead>
						<tr>
							<th rowspan="2"><input type="checkbox" name="mycheckbox"/></th>
							<th rowspan="2">合同编号</th>
							<th rowspan="2">产品型号</th>
							<th colspan="2">产品名称</th>
							<th rowspan="2">单价</th>
							<th rowspan="2">单位</th>
							<th rowspan="2">数量</th>
							<th rowspan="2">金额</th>
							<th rowspan="2">交付时限</th>
							<th rowspan="2">承制单位</th>
							<th rowspan="2">代储单位</th>
							<th rowspan="2">产品状态</th>
							<th rowspan="2">起始机号</th>
							<th rowspan="2">结束机号</th>
    					</tr>
    					<tr><th>整机名称</th><th>单元名称</th></tr>
				   </thead>
				   <% 
				   		List<HashMap<String, String>> products = (List<HashMap<String, String>>)request.getAttribute("products"); 
				   %>
				   <tbody>
				   <%
				   		for(int i = 0;i<products.size(); i++) {
				   %>
					<tr>
						<td><input type="checkbox" name="mycheck"/></td>
						<% String contractId = products.get(i).get("contractid"); %>
    					<td><%=contractId%></td>
    					<% String model = products.get(i).get("productModel"); %>
    					<td><%=model%></td>
    					<% 
    						String name = products.get(i).get("wholeName");
    				    %>
    					<td><%=name%></td>
    					<% 
    						String unitName = products.get(i).get("productUnit");
    				    %>
    					<td><%=unitName%></td>
    					<td><%=products.get(i).get("productPrice") %></td>
    					<td><%=products.get(i).get("measureUnit")%></td>
    					<% double sum = Double.parseDouble(products.get(i).get("productPrice"))*Double.parseDouble(products.get(i).get("count")); %>
    					<td><%=products.get(i).get("count") %></td>
    					<td><%=sum %></td>
    					<td><%=products.get(i).get("deliveryTime") %></td>
    					<td><%=products.get(i).get("manufacturer") %></td>
    					<td><%=products.get(i).get("keeper") %></td>
    					<td><%=products.get(i).get("proStatus") %></td>
    					<td><input type="text" onblur="checkNext(this);" value=""/></td>
    					<% if(Integer.parseInt(products.get(i).get("count")) >1) { %>
    					<td><input type="text"/></td>
    					<% }else { %>
    					<td><input type="text" value="不可用" disabled="true"/></td>
    					<%} %>
    					<%-- <td>
    						<span>
    						<%if(counts.get(i) >1) {%>
    							<input type="button" value="批量入库" onclick="window.location.href='ProductHandleServlet?operate=batchInApply&contractId=<%=contractId%>&model=<%=model%>&unit=<%=unit%>&price=<%=products.get(i).getPrice() %>&measure=<%=products.get(i).getMeasureUnit()%>&manufacture=<%=products.get(i).getManufacturer()%>&keeper=<%=products.get(i).getKeeper()%>'">
    						<%} else {%>
    						<input type="button" value="单个入库" onclick="window.location.href='ProductHandleServlet?operate=singleInApply&productId=<%=id%>'">
    						<%} %>
    						</span>
    					</td> --%>
                   </tbody>
                   <%} %>
                </table>
                <div style="float: right;margin-top: 10px;">
					<input type="button" class="search-btn" value="添加机号" onclick="addDeviceNo();">
					<input type="hidden" id="returnId" value="<%=request.getAttribute("contractid")%>">
				</div>
            </div> 
       </div>
       </div>
       	<div class="page-box">
				<% 
					//得到当前查询条件
					Map<String,String> condition = (Map<String,String>)request.getAttribute("condition");
					request.setAttribute("condition", condition);
					String cid = (String)request.getAttribute("contractid");
					//curPageNum 当前页
					int curPageNum=(Integer)request.getAttribute("curPageNum");
					//pageSize 按多少条分页
					int pageSize=(Integer)request.getAttribute("pageSize");
					//userSum 全部用户的个数
					int userSum=(Integer)request.getAttribute("sum");
					//根据总数和每页条数计算分多少页
					long totalPageNum = userSum%pageSize==0?userSum/pageSize:(userSum/pageSize+1);
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
						}
					//上一页的页码
					long prePageNum = curPageNum-1<1?1:curPageNum-1;
					//下一页的页码
					long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
			    <span>
			    <a href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>>&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
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
					window.location.href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="ProductHandleServlet?operate=queryproduct&contractid=<%=cid %>&curPageNum=1&pageSize="+value;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
				var pageSize="<%=request.getAttribute("pageSize")%>";
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>
		<script type="text/javascript" src="js/transact_business/transact.js"></script>
      <%@include file="../../../ConstantHTML/foot.html" %>
  </body>
</html>