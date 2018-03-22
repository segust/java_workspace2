<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Showrecord</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="css/welcome.css">
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
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">业务查询</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ProductQueryServlet?operate=productCollectiveQuery">设备信息</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			出入库记录查询				
			   </div>
						      		
      		<!-- 内容区 -->
      		<div class="content">      			
      			<!-- 这里写你的模块的html代码 -->
           <div id="Showrecord">
               <table class="fare-table" id="fare-table">
               <thead>
                 <tr><th>序号</th>
                     <th>机号</th>
                     <th>操作类型</th>
                     <th>操作时间</th>
                     <th>状态</th></tr>    
               </thead>
               <tbody>
               <%
               		List<HashMap<String,Object>> T = (List<HashMap<String,Object>>) request.getAttribute("message");
               		HashMap<String,Object> map = new HashMap<String,Object>();
          	   		int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
			
                 	String type=(String)request.getParameter("type");
					String Means = null;
					
          	   		 for(int i=0;i<T.size();i++){
              			map=T.get(i);
              			if(type.equalsIgnoreCase("inApply"))  Means = map.get("inMeans").toString();
              		
              			if(type.equalsIgnoreCase("outApply")) Means = map.get("outMeans").toString();
              	%>
              			
                 <tr>
                     <td><%=i+pageSize*(curPageNum-1)+1 %></td>
                     <td><%=map.get("deviceNo") %></td>
                     <td><%=Means%></td>
                     <td><%=map.get("insertTime")%></td>
                     <td><%=map.get("chStatus")%></td>
                 </tr>
                 <%} %>
               </tbody>
               </table>
           </div>
 
      	</div>	
      	      		<!-- 分页的盒子 -->
			<div class="page-box">
				<% 
				int sum=0;
				int totalPageNum=0;
				try{
					curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					pageSize=Integer.parseInt(request.getParameter("pageSize"));
					sum=(Integer)request.getAttribute("sum");
					totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
					}catch(Exception e){}
					type = (String)request.getAttribute("type");
					String deviceNo = (String)request.getAttribute("deviceNo");
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
				<span>
			    	<a href="ProductQueryServlet?curPageNum=1&pageSize=<%=pageSize %>&operate=inoutInfo&type=<%=type%>&deviceNo=<%=deviceNo%>">首页</a>
			    	<a href="ProductQueryServlet?curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&operate=inoutInfo&type=<%=type%>&deviceNo=<%=deviceNo%>" >&lt;&lt;</a>
			   		<span><%=curPageNum %></span>
			   		<a href="ProductQueryServlet?curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&operate=inoutInfo&type=<%=type%>&deviceNo=<%=deviceNo%>">&gt;&gt;</a>
			    	<a href="ProductQueryServlet?curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&operate=inoutInfo&type=<%=type%>&deviceNo=<%=deviceNo%>">尾页</a>
			    </span>
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage();'>确定</a>
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
            	<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	var type="<%=type%>";
				var deviceNo="<%=deviceNo%>";
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="ProductQueryServlet?&operate=inoutInfo&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&type="+type+"&deviceNo="+deviceNo+"";
			}
		</script>
		
		<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var type="<%=type%>";
				var deviceNo="<%=deviceNo%>";
				window.location.href="ProductQueryServlet?&operate=inoutInfo&curPageNum=1&pageSize="+value+"&type="+type+"&deviceNo="+deviceNo+"";
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
      <script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
      <script src="ConstantHTML/js/homepage.js"></script>
      <script src="js/welcome.js"></script>
      
  </body>
</html>
