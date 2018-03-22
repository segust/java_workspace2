<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>轮换出入库管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transactQueryProduct.css" />
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
      			<a href="BorrowServlet?operate=borrowInOut">轮换管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			轮换出入库管理
			</div>
       <div id="content">
       	     <!--产品查询 -->
        <div id="lunhuanchurukuguanli">
      
            <div class="search-box" >
            <form method="post" action="BorrowServlet?operate=borrowInOut">
				<span>合同号</span> 
                <span><input class="setTem" name="P.contractId" type="text"/></span>
                <span>产品型号</span> 
                <span><input class="setTem" name="productmodel" type="text"/></span>
                <span>单元名称</span> 
                <span><input class="setTem" name="unitname" type="text"/></span>
                <span>产品名称</span> 
                <span><input class="setTem" name="productName" type="text"/></span>
				<span>签订日期</span> 
                <span><input class="sang_Calender" name="signdate" type="text"/></span>
                <span><input class="search-btn" type="submit" value="查询产品"></span>
				
				</form>
            </div>
            <div id="contract-table-box">
			  <!--   <form action="BorrowServlet" method="post"> -->
			
				   <table  id="contract-table">
				   <thead>
					<tr class="contract-add-table-head">
    					<th style="background-color: #009966; ">序号</th>
    					<th style="background-color: #009966; ">产品型号</th>
    					<th style="background-color: #009966; ">单元名称</th>
    					<th style="background-color: #009966; ">机号</th>
    					<th style="background-color: #009966; ">单价</th>
    					<th style="background-color: #009966; ">数量</th>
    					<th style="background-color: #009966; ">计量单位</th>
    					<th style="background-color: #009966; ">类型</th>
    					<th style="background-color: #009966; ">生产日期</th>
    					<th style="background-color: #009966; ">存储期限</th>
    					<th style="background-color: #009966; ">企业定期维护周期</th>
    					<th style="background-color: #009966; ">承制单位</th>
    					<th style="background-color: #009966; ">代储单位</th>
    					<th style="background-color: #009966; ">合同编号</th>
    					<th style="background-color: #009966; ">状态</th>
    					<th style="background-color: #009966; ">操作</th>
    				</tr>
				   </thead>
				        <tbody>
                     	 <% 
						   		List<HashMap<String, Object>> T = (List<HashMap<String, Object>>)request.getAttribute("message"); 
						   		//curPageNum 当前页
								int curPageNum=(Integer)request.getAttribute("curPageNum");
								//pageSize 按多少条分页
								int pageSize=(Integer)request.getAttribute("pageSize");
						   		for(int i = 0;i<T.size(); i++) {
						   			int count = i+1+(curPageNum-1) * pageSize;
				   			%>
							
								<tr class="contract-add-table-body">
								<!--<td><input class="setTem" type="checkbox"/></td>  -->
								<td><%=count %></td>
								<td><%=T.get(i).get("productModel")==null?" ": T.get(i).get("productModel")%></td>
								<td><%=T.get(i).get("productUnit")==null?" ":T.get(i).get("productUnit") %></td>
								<td><%=T.get(i).get("deviceNo")==null?" ": T.get(i).get("deviceNo")%></td>
								<td><%=T.get(i).get("productPrice") ==null?" ": T.get(i).get("productPrice") %></td>
								<td><%=T.get(i).get("count")==null?" ":T.get(i).get("count") %></td>
								<td><%=T.get(i).get("measureUnit")==null?" ":T.get(i).get("measureUnit") %></td>
								<td>这是啥！！</td>
								<td><%=T.get(i).get("producedDate")==null?" ":T.get(i).get("producedDate") %></td>
								<td><%=T.get(i).get("restKeepTime")==null?" ":T.get(i).get("restKeepTime") %></td>
								<td><%=T.get(i).get("maintainCycle")==null?" ":T.get(i).get("maintainCycle") %></td>
								<td><%=T.get(i).get("manufacturer")==null?" ":T.get(i).get("manufacturer") %></td>
								<td ><%=T.get(i).get("keeper")==null?" ":T.get(i).get("keeper") %></td>
								<td><%=T.get(i).get("contractId") ==null?" ":T.get(i).get("contractId")%></td>
								<td><%=T.get(i).get("proStatus")==null?" ":T.get(i).get("proStatus") %></td>
								<td style="width:100px; "><!--<a href="BorrowServlet?operate=applyBorrowInWarehouse&nn=<%=i%>&N1=<%=T.get(i).get("deviceNo")==null?" ": T.get(i).get("deviceNo")%>&N2=<%=T.get(i).get("productPrice") %>&N3=<%=T.get(i).get("measureUnit") %>&N4=<%=T.get(i).get("manufacturer") %>&N5=<%=T.get(i).get("keeper") %>&N6=<%=T.get(i).get("maintainCycle") %>&N7=<%=T.get(i).get("producedDate") %>&N8=<%=T.get(i).get("restKeepTime")%>&N9=<%=T.get(i).get("contractId") %>&N10=<%=T.get(i).get("productModel") %>&N11=<%=T.get(i).get("productUnit") %>">申请<br>入库</a><br>-->
								<!--  <a href="BorrowServlet?operate=applyBorrowOutWarehouse&nn=<%=i%>&N1=<%=T.get(i).get("deviceNo") %>&N2=<%=T.get(i).get("productPrice") %>&N3=<%=T.get(i).get("measureUnit") %>&N4=<%=T.get(i).get("manufacturer") %>&N5=<%=T.get(i).get("keeper") %>&N6=<%=T.get(i).get("maintainCycle") %>&N7=<%=T.get(i).get("producedDate") %>&N8=<%=T.get(i).get("restKeepTime")%>&N9=<%=T.get(i).get("contractId") %>&N10=<%=T.get(i).get("productModel") %>&N11=<%=T.get(i).get("productUnit") %>">申请<br>出库</a>-->
								<input type="button"  value="申请入库 " onclick="window.location.href='/6905/BorrowServlet?operate=applyBorrowInWarehouse&nn=<%=i%>&N1=<%=T.get(i).get("deviceNo")==null?" ": T.get(i).get("deviceNo")%>&N2=<%=T.get(i).get("productPrice") ==null?" ": T.get(i).get("productPrice")%>&N3=<%=T.get(i).get("measureUnit")==null?" ":T.get(i).get("measureUnit")%>&N4=<%=T.get(i).get("manufacturer")==null?" ":T.get(i).get("manufacturer") %>&N5=<%=T.get(i).get("keeper")==null?" ":T.get(i).get("keeper") %>&N6=<%=T.get(i).get("maintainCycle")==null?" ":T.get(i).get("maintainCycle") %>&N7=<%=T.get(i).get("producedDate")==null?" ":T.get(i).get("producedDate")  %>&N8=<%=T.get(i).get("restKeepTime")==null?" ":T.get(i).get("restKeepTime") %>&N9=<%=T.get(i).get("contractId") ==null?" ":T.get(i).get("contractId") %>&N10=<%=T.get(i).get("productModel")==null?" ": T.get(i).get("productModel") %>&N11=<%=T.get(i).get("productUnit")==null?" ":T.get(i).get("productUnit")%>'" >
								
								<input type="button"  value="申请出库 " onclick="window.location.href='/6905/BorrowServlet?operate=applyBorrowOutWarehouse&nn=<%=i%>&N1=<%=T.get(i).get("deviceNo")==null?" ": T.get(i).get("deviceNo") %>&N2=<%=T.get(i).get("productPrice") ==null?" ": T.get(i).get("productPrice")%>&N3=<%=T.get(i).get("measureUnit")==null?" ":T.get(i).get("measureUnit") %>&N4=<%=T.get(i).get("manufacturer")==null?" ":T.get(i).get("manufacturer")  %>&N5=<%=T.get(i).get("keeper")==null?" ":T.get(i).get("keeper") %>&N6=<%=T.get(i).get("maintainCycle")==null?" ":T.get(i).get("maintainCycle") %>&N7=<%=T.get(i).get("producedDate")==null?" ":T.get(i).get("producedDate")  %>&N8=<%=T.get(i).get("restKeepTime")==null?" ":T.get(i).get("restKeepTime") %>&N9=<%=T.get(i).get("contractId") ==null?" ":T.get(i).get("contractId") %>&N10=<%=T.get(i).get("productModel")==null?" ": T.get(i).get("productModel")%>&N11=<%=T.get(i).get("productUnit")==null?" ":T.get(i).get("productUnit") %>'" ></td>
								
								</tr>
						
							<%} %>
								</tbody>
                </table>
				
            </div> 
       </div>
       	<div class="page-box">
				<% 
					//得到当前查询条件
					Map<String,String> condition = (Map<String,String>)request.getAttribute("condition");
					request.setAttribute("condition", condition);
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
			    <a href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
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
		</div>
		<%@include file="../../../ConstantHTML/foot.html" %>
  </body>
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
					window.location.href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="BorrowServlet?operate=borrowInOut&P.contractid=<%=condition.get("P.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&productName=<%=condition.get("productName") %>&curPageNum=1&pageSize="+value;
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
  <script type="text/javascript" src="js/transact_business/dateTime.js"></script>
</html>
