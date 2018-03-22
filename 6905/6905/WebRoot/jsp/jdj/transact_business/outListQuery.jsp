<%@ page language="java" import="java.util.*,cn.edu.cqupt.beans.*,cn.edu.cqupt.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>调拨出库信息列表查询</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transactQueryProduct.css" />
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html" %>
	<%@include file="../../../ConstantHTML/jdjleft.html" %>

	<%
	ArrayList<HashMap<String,String>> outList = (ArrayList<HashMap<String,String>>) request.getAttribute("message");
	%>
	<% 
					//curPageNum 当前页
					String num=request.getParameter("curPageNum");
					int curPageNum=Integer.parseInt(num);
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

	<div id="right">
		<div class="subName">
			<span>当前位置：</span>
      		<a href="OutListServlet?operate=selectOutListAllot">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;调拨出库列表信息
		</div>
		<div id="content">
			<div class="table-wrap">
				<table>
					<thead>
						<tr>
							<th>序号</th>
							<th>发料单号</th>
							<th>运单编号</th>
							<th>案由文号</th>
							<th>运输方式</th>
							<th>操作时间</th>
							<th>出库方式</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<% for(int i = 0;i<outList.size();i++) { %>
						<tr>
							<td><%=(curPageNum-1)*pageSize+i+1 %></td>
							<td><%=outList.get(i).get("listId") %></td>
							<td><%=outList.get(i).get("fileNo") %></td>
							<td><%=outList.get(i).get("deliverNo") %></td>
							<td><%=outList.get(i).get("diliverMean") %></td>
							<td><%=outList.get(i).get("date") %></td>
							<td><%=outList.get(i).get("outMeans") %></td>
							<td><input type="button" value="查看" onclick="window.location.href='OutListServlet?operate=selectDetail&type=allot&listId=<%=outList.get(i).get("listId") %>'"></td>
						</tr>	
						<%} %>
					</tbody>
				</table>
			</div>
			<div id="type">allotOut</div>
		</div>
		<!-- 分页的盒子 -->
				<div class="page-box">
			    <span>
			    <a href="OutListServlet?operate=selectOutListAllot&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="OutListServlet?operate=selectOutListAllot&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="OutListServlet?operate=selectOutListAllot&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="OutListServlet?operate=selectOutListAllot&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
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
	
	<%@include file="../../../ConstantHTML/jdjfoot.html" %>
	<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize="<%=pageSize %>";
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="OutListServlet?operate=selectOutListAllot&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="OutListServlet?operate=selectOutListAllot&curPageNum=1&pageSize="+pageSize;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
				var pageSize="<%=pageSize %>";
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="js/transact_business/transact.js"></script>
	<script type="text/javascript" src="js/transact_business/changeColor.js"></script>
	<script type="text/javascript" src="js/transact_business/dateTime.js"></script>
</body>
</html>
