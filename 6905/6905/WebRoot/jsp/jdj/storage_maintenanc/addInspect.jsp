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

<title>设备检查</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="js/storage_maintenanc/inspectRecord.js"></script>
<script type="text/javascript" src="js/transact_business/transact.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/storage_maintenanc/storage.css" />
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
      	<!-- 二级标题 -->
      	<div class="subName">
      		<span>当前位置：</span>
      		<a href="Maintain?operateType=maintainQuery&curPageNum=1&pageSize=10">存储维护</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      		<a href="InspectServlet?operate=inspect&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=">检查管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      		新增检查
		</div>

		<!-- 内容区 -->
		<div class="content">
			<div class="search-box" style="height: 50px;background-color: #2E8B57;">
				<form class="add-storage" 
					action="InspectServlet?operate=inspect&mark=search&curPageNum=1&pageSize=10"
					method="post">
					<span>产品型号:</span> <span><input class="setTem"
						name="productModel" type="text" />
					</span> <span>产品单元:</span> <span><input class="setTem"
						name="unitName" type="text" />
					</span> <span>生产单位:</span> <span><input class="setTem"
						name="manufacturer" type="text" />
					</span> <span>机号:</span> <span><input class="setTem"
						name="deviceNo" type="text" />
					<button type="submit" class="search-btn"style="background-color: #3CB371;">查询</button>
				</form>
			</div>
			<div id="contract-table-box">
			<table id="contract-table">
				<thead>
					<tr>
						<td style="background-color: #669966;"></td>
						<td style="background-color: #669966;">序号</td>
						<td style="background-color: #669966;">产品型号</td>
						<td style="background-color: #669966;">单元名称</td>
						<td style="background-color: #669966;">批次</td>
						<td style="background-color: #669966;">机号</td>
						<td style="background-color: #669966;">单价</td>
						<td style="background-color: #669966;">数量</td>
						<td style="background-color: #669966;">器材类型</td>
						<td style="background-color: #669966;">存储期限</td>
						<td style="background-color: #669966;">剩余存放时间</td>
						<td style="background-color: #669966;">承制单位</td>
						<td style="background-color: #669966;">代储单位</td>
						<td style="background-color: #669966;">产品备注</td>
						<td style="background-color: #669966;">检查备注</td>
					</tr>
				</thead>
				<tbody>
					<%
						Map<String,String> condition = (Map<String,String>)request.getAttribute("condition");
						request.setAttribute("condition", condition);
						int curPageNum=0;
						int pageSize=0;
						try{
							curPageNum=Integer.parseInt(condition.get("curPageNum"));
							pageSize=Integer.parseInt(condition.get("pageSize"));
							}catch(Exception e){}
						Map<String, String> map = new HashMap<String, String>();
						List<Map<String, String>> list = (ArrayList<Map<String, String>>) pageContext
								.getAttribute("list", PageContext.REQUEST_SCOPE);
						if (list != null) {
							for (int i = 0; i < list.size(); i++) {
								map = list.get(i);
					%>
					<tr>
					<td ><input type="checkbox" name="mycheck"><input type="hidden" name="id" value="<%=map.get("productId")%>"></td>
					<td><%=i+pageSize*(curPageNum-1)+1%></td>
					<td><%=map.get("productModel")%></td>
					<td><%=map.get("unitName")%></td>
					<td><%=map.get("batch")%></td>
					<td><%=map.get("deviceNo")%></td>
					<td><%=map.get("newPrice")%></td>
					<td><%=map.get("num")%></td>
					<td><%=map.get("productType")%></td>
					<td><%=map.get("StorageTime")%></td>
					<td><%=map.get("restKeepTime")%></td>
					<td><%=map.get("manufacturer")%></td>
					<td><%=map.get("keeper")%></td>
					<td><%=map.get("remark")%></td>
					<td><input class="setTem" type="text">
					</td>
					</tr>
					<%
						}
						}
					%>
					
					
				</tbody>
			</table>
			<div style="float: right;">
			<p style="width:100%;text-align:center;">
			<input type="button" class="search-btn" value="全选" onclick="getAll();">
			<input type="button" class="search-btn" value="军代局检查" onclick="jdjCheckInspect();" style="width: 85px;">
			</p>
			</div>
			</div>
			<div class="page-box" style="color: black;">
				<% 
				//得到当前查询条件
				long sum=0;
				long totalPageNum=0;
				try{
					curPageNum=Integer.parseInt(condition.get("curPageNum"));
					pageSize=Integer.parseInt(condition.get("pageSize"));
					sum=(Long)request.getAttribute("sum");
					totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
					}catch(Exception e){	
					}
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
					}
					long prePageNum = curPageNum-1<1?1:curPageNum-1;
					long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
				<span style="color: black;">
			    	<a href="InspectServlet?operate=inspect&curPageNum=1&pageSize=<%=pageSize%>&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName") %>&manufacturer=<%=condition.get("manufacturer") %>&deviceNo=<%=condition.get("deviceNo") %>">首页</a>
			    	<a href="InspectServlet?operate=inspect&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName") %>&manufacturer=<%=condition.get("manufacturer") %>&deviceNo=<%=condition.get("deviceNo") %>" >&lt;&lt;</a>
			   	<span style="color: black;"><%=curPageNum %></span>
			   		<a href="InspectServlet?operate=inspect&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName")  %>&manufacturer=<%=condition.get("manufacturer")%>&deviceNo=<%=condition.get("deviceNo") %>">&gt;&gt;</a>
			    	<a href="InspectServlet?operate=inspect&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName")  %>&manufacturer=<%=condition.get("manufacturer")%>&deviceNo=<%=condition.get("deviceNo") %>">尾页</a>
			    </span>
		    	<span style="color: black;">跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span style="color: black;">页</span>
		    	<a onclick='skipPage()'>确定</a>
		   		<span style="color: black;">每页显示</span>
			    <select name="selectPageSize" onchange='selectPageSize(this.value)' id="selectPageSize">
			        <option value ="10">10</option>
			        <option value ="15">15</option>
			        <option value="20">20</option>
			    </select>
		    	<span style="color: black;">条记录，共</span>
		    	<label style="color: black;"><%=totalPageNum %></label>
		    	<span style="color: black;">页</span>
			
        </div>
	</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
	      	<!-- 跳转到多少页时 js的skipPage()函数先判断输入的页码是否有效-->
      	<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="InspectServlet?operate=inspect&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName") %>&manufacturer=<%=condition.get("manufacturer") %>&deviceNo=<%=condition.get("deviceNo") %>";
			}
		</script>	
			
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="InspectServlet?operate=inspect&curPageNum=1&pageSize="+value+"&productModel=<%=condition.get("productModel") %>&unitName=<%=condition.get("unitName") %>&manufacturer=<%=condition.get("manufacturer") %>&deviceNo=<%=condition.get("deviceNo") %>";
			}
		</script>
		
		<script type="text/javascript">
			var pageSize="<%=request.getParameter("pageSize")%>";
			if(pageSize=="null")
				document.getElementById("selectPageSize").value="10";
			else
				document.getElementById("selectPageSize").value=pageSize;
		</script>
</body>
</html>
