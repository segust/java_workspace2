<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Demo</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
				<ul>
					<li style="background-color: white;">
						<a href="#" style="color: #009999;">二级标题1</a>
					</li>
					<li>
						<a href="#">二级标题2</a>
					</li>
					<li style="background-color: white;">
						<a href="#" style="color: #009999;">二级标题3</a>
					</li>
					<li>
						<a href="#">二级标题4</a>
					</li>
				</ul>
			</div>
      		<!-- 内容区 -->
      		<div class="content">
      			<div class="toptitle">
      				<title>这是你的模块各页面的标题</title>
      			</div>
      			
      			<!-- 这里写你的模块的html代码 -->
      			
      		</div>
      		
      		<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
					//curPageNum 当前页
					//首次进入页面时记得在路径上加上curPageNum=1参数,Servlet跳转是也记得加上curPageNum参数
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					
					//pageSize 按多少条分页
					//首次进入页面时记得在路径上加上pageSize=10参数,Servlet跳转是也记得加上pageSize参数
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					
					//后台用 select count(*)... 得到的你查询的全部数据的个数
					long sum=(Long)request.getAttribute("sum");
					
					//根据总数和每页条数计算分多少页
					long totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
					
					//没有数据则当前页=1,总页数=1
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
					}
					
					//上一页的页码
					long prePageNum = curPageNum-1<1?1:curPageNum-1;
					
					//下一页的页码
					long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
				<!-- 显示页码的html代码，样式暂时先这样 -->
				<!-- 将curPageNum和pageSize传给你的Servlet，
						用于DAO的SQL语句中limit ?,? 的两个参数赋予 (curPageNum-1)*pageSize,pageSize 两值-->
				<span>
			    	<a href="你的Servlet路径?curPageNum=1&pageSize=<%=pageSize %>&你的其他参数">首页</a>
			    	<a href="你的Servlet路径?curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&你的其他参数" >&lt;&lt;</a>
			   		<span><%=curPageNum %></span>
			   		<a href="你的Servlet路径?curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&你的其他参数">&gt;&gt;</a>
			    	<a href="你的Servlet路径?curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&你的其他参数">尾页</a>
			    </span>
			    
			    <!-- 跳转到多少页时 js的skipPage()函数先判断输入的页码是否有效-->
				<!-- 函数体就在下面，把里面的路径改成你的  -->
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage()'>确定</a>
		    	
		    	<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
				<!-- 函数体就在下面，把里面的路径改成你的  -->
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
      <%@include file="../../../ConstantHTML/foot.html" %>
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
					window.location.href="你的Servlet路径?&curPageNum="+skipPageNum+"&pageSize="+pageSize+"你的其他参数";
			}
		</script>
		
		<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="你的Servlet路径?curPageNum=1&pageSize="+value+"你的其他参数";
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
