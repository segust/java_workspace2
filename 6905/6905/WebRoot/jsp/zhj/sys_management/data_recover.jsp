<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据恢复</title>
    <meta charset="UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/sys_management/systemmanage.css"/>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/zhjleft.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="SystemManagementServlet?operate=dataRecover">数据恢复</a>
			</div>
      		<!-- 内容区 -->
      		<div class="content">
      			<div class="toptitle">
      				<title>数据恢复</title>
      			</div>
      			
      			   <div id="data-recover-box">
      			   	<form>
      			   		<table  class="data-recover-table" id="data-recover-table">
      			   			<thead>
      			   				<tr>
      			   					<th>文件名</th>
      			   					<th>备份时间</th>
      			   					<th>操作</th>
      			   				</tr>
      			   			</thead>
      			   			<tbody>
      			   				<%
				      			List<HashMap<String, Object>> T=(List<HashMap<String, Object>>)request.getAttribute("message");
				      			for(int i=0;i<T.size();i++){
	      		 				%>
      			   				<tr class="contract-add-table-body">
      			   					<td><%=T.get(i).get("filename") %></td>
      			   					<td><%=T.get(i).get("time") %></td>
      			   					<td style="width:50px;"><input type="button" onclick="window.location.href='../6905/SystemManagementServlet?operate=doDataRecover&filename=<%=T.get(i).get("filename") %>'"value="导入"></td>
      			   				</tr>
      			   			</tbody>
			      			   <%
			      			   	}
			      			    %>		
      			   		</table>
      			   	</form>
      			   </div>
      		</div>
      		<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
				    //得到当前查询条件,后续再增添查询条件
					//HashMap<String, Object> condition = (HashMap<String, Object>)request.getAttribute("condition");
					
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
			    <a href="SystemManagementServlet?operate=dataRecover&curPageNum=1&pageSize=<%=pageSize%>" >首页</a>
			    <a href="SystemManagementServlet?operate=dataRecover&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>">&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="SystemManagementServlet?operate=dataRecover&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="SystemManagementServlet?operate=dataRecover&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
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
			 	var pageSize="<%=pageSize %>";
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="SystemManagementServlet?operate=dataRecover&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="SystemManagementServlet?operate=dataRecover&curPageNum=1&pageSize="+value;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>
  </body>
</html>
