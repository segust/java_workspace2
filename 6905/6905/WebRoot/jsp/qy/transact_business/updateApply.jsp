<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>更新出库</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/query_business/queryBusiness.css">
    <link rel="stylesheet" href="css/paging.css">
    <link rel="stylesheet" href="css/transact_business/transact.css">
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
      			<a href="ProductQueryServlet?operate=gotoBorrowIn">轮换更新</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			更新出库申请				
			</div>
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
	      		<div class="search_box">
					<form action="ProductQueryServlet?operate=updateApply" method="post" onsubmit="return judgeInputTime('restKeepTime','restMaintainTime')">
					<span >产品型号</span>
	                <span><input type="text" name="productModel"/></span>
	                <span>产品单元</span>
	                <span><input type="text" name="productUnit"/></span>
	                <span>承制单位</span>
	                <span><input type="text" name="manufacturer"/></span>
	                <span>操作类型</span>
	                <span>
	                <select name ="Means" class="setTem">
                    <option value="">所有入库</option>
                    <option value="新入库">新入库</option>
                    <option value="更新入库">更新入库</option>
                    <option value="轮换入库">轮换入库</option>
                    </select>
                    </span>
                    <br/>
	                <span>剩余存放天数&lt;=</span>
	                <span><input type="text" name="restKeepTime" id="restKeepTime"/></span>
	                <span>企业维护剩余天数&lt;=</span>
	                <span><input type="text" name="restMaintainTime" id="restMaintainTime"/></span>
	                <span>机号</span>
	                <span><input type="text" name="deviceNo"/></span>
	                <input class="scan-btn" type="submit" value="查询">
	                <input type="button" value="导出" class="scan-btn" onclick="outAll_productdetail();">
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
								<!-- <th>操作</th> -->
								</tr>
							</thead>
							<tbody>
							 <% 
						   		List<HashMap<String, Object>> T = (List<HashMap<String, Object>>)request.getAttribute("message"); 
						   		for(int i = 0;i<T.size(); i++) {
				   			%>
								<tr class="contract-add-table-body">
								<td><input class="setTem" type="checkbox" name="checkbox_product"></td>
								<td><%=i+1 %></td>
								<td><%=T.get(i).get("productModel") %></td>
								<td><%=T.get(i).get("productUnit") %></td>
								<td><%=T.get(i).get("batch") %></td>
								<td><%=T.get(i).get("deviceNo") %></td>
								<td><%=T.get(i).get("productPrice") %></td>
								<td><%=T.get(i).get("num") %></td>
								<td><%=T.get(i).get("execDate") %></td>
								<td><%=T.get(i).get("restKeepTime") %></td>
								<td><%=T.get(i).get("restMaintainTime") %></td>
								<td><%=T.get(i).get("Means") %></td>
								<td><%=T.get(i).get("storageTime") %></td>
								<td><%=T.get(i).get("nextMaintainTime") %></td>
								<td><%=T.get(i).get("manufacturer") %></td>
								<td><%=T.get(i).get("keeper") %></td>
								<td><%=T.get(i).get("remark") %></td>
								<td><input type="button" value="入库记录查询"
								onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&type=inApply&productId=111&deviceNo=<%=T.get(i).get("deviceNo") %>'}">
									<input type="button" value="出库记录查询"
											onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&type=outApply&productId=111&deviceNo=<%=T.get(i).get("deviceNo") %>'}"></td>
								</tr>
							<%} %>
							</tbody>
						</table>
				</div>
				<%-- <div id="operate">
				   <c:if test="${type=='borrowOut' }"><span><input class="scan-btn" type="button" value="轮换出库"  id="borrow_Out" onclick="getBorrowItem();"></span></c:if>
				   <c:if test="${type=='updateOut' }"><span><input class="scan-btn" type="button" value="更新出库"  id="update_Out" onclick="getUpdateItem();"></span></c:if>
				</div> --%>
			</div>

      		<!-- 分页开始 -->
			<div class="paging-wrape">

				<ul class="pagination">
					<li><a href="javascript:;" aria-label="Previous"> &laquo;
					</a>
					</li>
					<li><a class="active" href="javascript:;">1</a>
					</li>

					<li><a href="javascript:;" aria-label="Next"> &raquo; </a>
					</li>

				</ul>

				<div class="total">
					共<span>0</span>页
				</div>

				<div class="go-page">
					<span class="text">到第</span> <input class="page_Input" type="text"
						value=""> <span class="text">页</span> <a class="sure-btn"
						href="javascript:;" role="button" tabindex="0">确定</a>
				</div>

			</div>
			<!-- 分页结束 -->
			<ul class="relative-link" id="relative-link">
			    <li><a href="UpdateServlet?operate=gotoUpdateOut">更新出库申请</a></li>
			</ul>
		</div>	  
     	<%@include file="../../../ConstantHTML/foot.html" %>
      	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	  	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
	  	<script src="ConstantHTML/js/xcConfirm.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
		<script src="js/query_business/product_operate.js"></script>
		<script src="js/query_business/product_save.js"></script>
		<script src="js/transact_business/lhgxck-paging.js"></script>
	</body>
</html>
