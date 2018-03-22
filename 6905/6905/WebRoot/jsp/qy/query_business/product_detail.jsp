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
    
    <title>设备明细查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/query_business/queryBusiness.css">
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
   
  </head>
  
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">业务查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">设备信息</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ProductQueryServlet?operate=productDetailQuery">设备明细查询</a>				
			</div>
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
      			<form action="ProductQueryServlet?operate=productDetailQuery" method="post" onsubmit="return judgeInputTime('restKeepTime','restMaintainTime')">
		      		<div class="search_box">
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
		                    <option value="轮换入库">轮换入库</option>
			                <option value="更新入库">更新入库</option>
			                <option value="轮换出库">轮换出库</option>
			                <option value="更新出库">更新出库</option>
			                <option value="发料调拨出库">发料调拨出库</option>
			                <option value="发料轮换出库">发料轮换出库</option>
			                <option value="发料更新出库">发料更新出库</option>
		                    </select>
		                    </span>
		             </div>
		             <div class="search_box">
			                <!-- <span>剩余存放天数&lt;=</span>
			                <span><input type="text" name="restKeepTime" id="restKeepTime"/></span> -->
			                <span>企业维护剩余天数&lt;=</span>
			                <span><input type="text" name="restMaintainTime" id="restMaintainTime"/></span>
			                <span>机号</span>
			                <span><input type="text" name="deviceNo"/></span>
			                <input class="scan-btn" type="submit" value="查询">
			                <input type="button" value="导出" class="scan-btn" onclick="outAll_productdetail();">
					</div>
				</form>
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
								<th>器材类型</th>
								<th>操作日期</th>
								<!-- <th>剩余存放天数</th> -->
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
								<td></td>
								<td><%=T.get(i).get("execDate") %></td>
								<%-- <td><%=T.get(i).get("restKeepTime") %></td> --%>
								<td><%=T.get(i).get("restMaintainTime") %></td>
								<td><%=T.get(i).get("Means") %></td>
								<td><%=T.get(i).get("storageTime") %></td>
								<td><%=T.get(i).get("nextMaintainTime") %></td>
								<td><%=T.get(i).get("manufacturer") %></td>
								<td><%=T.get(i).get("ownedUnit") %></td>
								<td><%=T.get(i).get("remark") %></td>
								<td><input type="button" value="入库记录查询"
									onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&type=inApply&productId=<%=T.get(i).get("productId") %>&deviceNo=<%=T.get(i).get("deviceNo") %>'}">
									<input type="button" value="出库记录查询"
									onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&type=outApply&productId=<%=T.get(i).get("productId") %>&deviceNo=<%=T.get(i).get("deviceNo") %>'}">
								</td>
								</tr>
							<%} %>
							</tbody>
						</table>
				</div>
				<div id="operate">
				   <c:if test="${type=='borrowOut' }"><span><input class="scan-btn" type="button" value="轮换出库"  id="borrow_Out" onclick="getBorrowItem();"></span></c:if>
				   <c:if test="${type=='updateOut' }"><span><input class="scan-btn" type="button" value="更新出库"  id="update_Out" onclick="getUpdateItem();"></span></c:if>
				</div>
			</div>
			<!-- 此处不可进行轮换更新操作 -->
			<!-- <ul class="relative-link" id="relative-link">
			    <li><a href="BorrowServlet?operate=gotoBorrowOut">轮换出库申请</a></li>
			    <li><a href="UpdateServlet?operate=gotoUpdateOut">更新出库申请</a></li>
			</ul> -->
      		<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
				    //得到当前查询条件
					String productModel=(String)request.getAttribute("productModel");
					String productUnit=(String)request.getAttribute("productUnit");
					String manufacturer=(String)request.getAttribute("manufacturer");
					String Means=(String)request.getAttribute("Means");
					String restKeepTime=(String)request.getAttribute("restKeepTime");
					String restMaintainTime=(String)request.getAttribute("restMaintainTime");
					String deviceNo=(String)request.getAttribute("deviceNo");
					//curPageNum 当前页
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					//pageSize 按多少条分页
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					//userSum 全部明细信息的个数
					int DetailSum=(Integer)request.getAttribute("Detailsum");
					//根据总数和每页条数计算分多少页
					int totalPageNum = DetailSum%pageSize==0?DetailSum/pageSize:(DetailSum/pageSize+1);
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
			    <a href="ProductQueryServlet?operate=productDetailQuery&curPageNum=1&pageSize=<%=pageSize%>
			    		&productModel=<%=productModel %>&productUnit=<%=productUnit %>&manufacturer=<%=manufacturer %>
			    		&Means=<%=Means %>&restKeepTime=<%=restKeepTime %>&restMaintainTime=<%=restMaintainTime %>
			    		&deviceNo=<%=deviceNo %>" >首页</a>
			    <a href="ProductQueryServlet?operate=productDetailQuery&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>
			    		&productModel=<%=productModel %>&productUnit=<%=productUnit %>&manufacturer=<%=manufacturer %>
			    		&Means=<%=Means %>&restKeepTime=<%=restKeepTime %>&restMaintainTime=<%=restMaintainTime %>
			    		&deviceNo=<%=deviceNo %>">&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="ProductQueryServlet?operate=productDetailQuery&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>
			    		&productModel=<%=productModel %>&productUnit=<%=productUnit %>&manufacturer=<%=manufacturer %>
			    		&Means=<%=Means %>&restKeepTime=<%=restKeepTime %>&restMaintainTime=<%=restMaintainTime %>
			    		&deviceNo=<%=deviceNo %>">&gt;&gt;</a>
			    <a href="ProductQueryServlet?operate=productDetailQuery&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>
			    		&productModel=<%=productModel %>&productUnit=<%=productUnit %>&manufacturer=<%=manufacturer %>
			    		&Means=<%=Means %>&restKeepTime=<%=restKeepTime %>&restMaintainTime=<%=restMaintainTime %>
			    		&deviceNo=<%=deviceNo %>">尾页</a>
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
	  	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
     	<script src="ConstantHTML/js/xcConfirm.js"></script>
      	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
     	<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
       	<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize="<%=pageSize %>";
			 	var productModel="<%=productModel %>";
				var productUnit="<%=productUnit %>";
				var manufacturer="<%=manufacturer %>";
				var Means="<%=Means %>";
				var restKeepTime="<%=restKeepTime %>";
				var restMaintainTime="<%=restMaintainTime %>";
				var deviceNo="<%=deviceNo %>";
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
					window.location.href="ProductQueryServlet?operate=productDetailQuery&curPageNum="+skipPageNum+"&pageSize="+pageSize
											+"&productModel="+productModel+"&productUnit="+productUnit+"&manufacturer="+manufacturer
											+"&Means="+Means+"&restKeepTime="+restKeepTime+"&restMaintainTime="+restMaintainTime
											+"&deviceNo="+deviceNo;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var productModel="<%=productModel %>";
				var productUnit="<%=productUnit %>";
				var manufacturer="<%=manufacturer %>";
				var Means="<%=Means %>";
				var restKeepTime="<%=restKeepTime %>";
				var restMaintainTime="<%=restMaintainTime %>";
				var deviceNo="<%=deviceNo %>";
				window.location.href="ProductQueryServlet?operate=productDetailQuery&curPageNum=1&pageSize="+value
										+"&productModel="+productModel+"&productUnit="+productUnit+"&manufacturer="+manufacturer
										+"&Means="+Means+"&restKeepTime="+restKeepTime+"&restMaintainTime="+restMaintainTime
										+"&deviceNo="+deviceNo;
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
		<script src="js/query_business/product_operate.js"></script>
  </body>
</html>
