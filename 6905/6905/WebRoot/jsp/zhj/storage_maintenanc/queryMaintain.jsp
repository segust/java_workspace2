<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>设备维护</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="js/public/format-upload.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/storage_maintenanc/storage.css" />
	<script src="js/storage_maintenanc/getmaintaininfo.js"></script>
	<style>
	[value="选择文件"],[value="导入"]{
		width:80px;
		height:25px;
		background-color:#3CB371;
		border:0;
		color:white;
		font-weight:bold;
	}
	[value="选择文件"]:hover,[value="导入"]:hover{
		cursor:pointer;
		background-color:#009933;
	}
	</style>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html" %>
	<%@include file="../../../ConstantHTML/zhjleft.html" %>
	<div id="right">
		<%
		ArrayList<HashMap<String, String>> result = null;
		result = (ArrayList<HashMap<String, String>>)request.getAttribute("result");
		%>
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span>
			<a href="Maintain?operateType=maintainQuery&curPageNum=1&pageSize=10">存储维护</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="Maintain?operateType=maintainQuery&curPageNum=1&pageSize=10">维护管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			维护记录查询
		</div>

		<!-- 内容区 -->
		<div class="content">
			<div class="search-box" style="height: 50px;">
				<form class="add-storage" action="<%=path %>/Maintain?operateType=maintainInfoQuery&curPageNum=1&pageSize=10" method="post">
					<span>产品型号:</span>
					<span><input class="setTem" name="productModel" type="text"/></span>
					<span>产品单元:</span>
					<span><input class="setTem" name="productUnit" type="text"/></span>
					<span>承制单位:</span>
					<span><input class="setTem" name="manufacturer" type="text"/></span>
					<span>维护类别</span>
					<span><input class="setTem" name="maintainType" type="text"/></span>
					<span>机号</span>
					<span><input class="setTem" name="deviceNo" type="text"/></span>
					<button  type="submit" class="search-btn">查询</button>
				</form>
			</div>
			<form action="${pageContext.request.contextPath}/MaintainServlet?operate=importLog" enctype="multipart/form-data" method="post" onsubmit="return format()">
				<input type="text" size="20" name="upfile" id="upfile" style="padding:5px 0;border:1px dotted black;margin-left: 10px;">  

				<a class="file-wrap" href="#">
					浏览
					<input type="file" id="fileToUpload" accept=".whms" onchange="upfile.value=this.value">
				</a>

				<input type="submit" value="导入">
				<p class="error-info"></p>
			</form>
			<div id="contract-table-box">
				<table id="contract-table">
					<thead>
						<tr>
							<td></td>
							<td>序号</td>
							<td>产品型号</td>
							<td>单元名称</td>
							<td>批次</td>
							<td>机号</td>
							<td>单价</td>
							<td>数量</td>
							<td>器材类型</td>
							<td>存储期限</td>
							<td>承制单位</td>
							<td>代储单位</td>
							<td>维护类别</td>
							<td>维护时间</td>
							<td>备注</td>
						</tr>
					</thead>
					<tbody>
						<%
						List<String> condition =(ArrayList<String>)request.getAttribute("condition");
						request.setAttribute("condition", condition);
						int curPageNum=0;
						int pageSize=0;
						try{
						curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
						pageSize=Integer.parseInt(request.getParameter("pageSize"));
					}catch(Exception e){}
					if(request.getAttribute("result") != null){
					for(int i=0; i<result.size(); i++)
					{
					HashMap<String, String> map = 
					result.get(i);
					%>
					<tr>
						<td><input type="checkbox" name="checkbox_maintaininfo">
							<input type="hidden" value=<%=map.get("logId") %>></td>
							<td><%=map.get("productId") %></td>
							<td><%=map.get("productModel") %></td>
							<td><%=map.get("productUnit") %></td>
							<td><%=map.get("batch") %></td>
							<td><%=map.get("deviceNo") %></td>
							<td><%=map.get("price") %></td>
							<td><%=map.get("num") %></td>
							<td><%=map.get("productType") %></td>
							<td><%=map.get("storageTime") %></td>
							<td><%=map.get("manufacturer") %>位</td>
							<td><%=map.get("keeper") %></td>
							<td><%=map.get("maintainType") %></td>
							<td><%=map.get("operateTime") %></td>
							<td><%=map.get("remark") %></td>
						</tr>
						<%
					}
				}
				else
					{
				%>
				<tr><td>没有结果返回</td></tr>
				<%
			}
			%>
		</tbody>
	</table>
	<input type="button" value="导出" class="scan-btn" onclick="OutMaintainInfo()";>
</div>     			
<!-- 分页的盒子 -->
<div class="page-box">
	<% 	
	long sum = 0;
	long totalPageNum=0;
	try{
	curPageNum=Integer.parseInt(request.getParameter("curPageNum"));			
	pageSize=Integer.parseInt(request.getParameter("pageSize"));
	sum=(Long)request.getAttribute("sum");
	totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
}catch(Exception e){}

if(totalPageNum==0){
totalPageNum=1;
curPageNum=1;
}
long prePageNum = curPageNum-1<1?1:curPageNum-1;
long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
%>
<span>
	<a href="Maintain?operateType=maintainInfoQuery&curPageNum=1&pageSize=<%=pageSize %>&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>">首页</a>
	<a href="Maintain?operateType=maintainInfoQuery&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>" >&lt;&lt;</a>
	<span style="color: black;"><%=curPageNum %></span>
	<a href="Maintain?operateType=maintainInfoQuery&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>">&gt;&gt;</a>
	<a href="Maintain?operateType=maintainInfoQuery&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>">尾页</a>
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
<%@include file="../../../ConstantHTML/zhjfoot.html" %>
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
		window.location.href="Maintain?operateType=maintainInfoQuery&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>";
}
</script>

<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
<script type="text/javascript">
function selectPageSize(value){
	var pageSize=value;
	window.location.href="Maintain?operateType=maintainInfoQuery&curPageNum=1&pageSize="+value+"&productModel=<%=condition.get(0) %>&productUnit=<%=condition.get(1) %>&manufacturer=<%=condition.get(2) %>&maintainTye=<%=condition.get(3) %>&deviceNo=<%=condition.get(4) %>";
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
