<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.edu.cqupt.beans.Page"%>
<%@page import="cn.edu.cqupt.beans.Humidity"%>
<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
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

		<title>库房湿度管理</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/user_management/usermanageindex.css" />
		<link rel="stylesheet" type="text/css"
			href="css/warehouse_management/content.css" />
	</head>
	<body>
		<%@include file="../../../../ConstantHTML/top.html"%>
		<%@include file="../../../../ConstantHTML/left.html"%>
		<div id="right">
			<!-- 二级标题开始 -->
			<div id="subName" class="subName">
				<span>当前位置：</span>
   				库房管理&nbsp;&nbsp;>&nbsp;&nbsp;
   				湿度管理
			</div>
			<!-- 二级标题结束 -->
			<!-- 内容区开始 -->
			<%
					String startDate=(request.getAttribute("startDate")==null)?"":(String)request.getAttribute("startDate");
					String endDate=(request.getAttribute("endDate")==null)?"":(String)request.getAttribute("endDate");
					String startHumidity=(request.getAttribute("startHumidity")==null)?"":(String)request.getAttribute("startHumidity");
					String endHumidity=(request.getAttribute("endHumidity")==null)?"":(String)request.getAttribute("endHumidity");
			%>
			<div class="content">
				<!--温度管理列表子页面-->
				<!-- 搜索框开始 -->
				<div class="toptitle" id="search-condition-box">
					<div class="toptitle-op">
						<form method="post">
							<div class="tem-box">
								<label>
									库房标准湿度：
								</label>
								<input type="text" id="startHumidity" value="<%=startHumidity %>"/>
								<label>
									到
								</label>
								<input type="text" id="endHumidity" value="<%=endHumidity %>"/>
							</div>
							<div class="op-box">
								<input type="button" class="smallbtn" value="设定" onclick="searchHumidityByCondition();">
							</div>
							<div class="tim-box">
								<label>
									时间段：
								</label>
								<input class="sang_Calender" type="text" id="startDate" value="<%=startDate %>"/>
								<label>
									到
								</label>
								<input class="sang_Calender" type="text" id="endDate" value="<%=endDate %>"/>
							</div>
							<div class="op-box">
								<%
									Page pp=(Page)request.getAttribute("page"); 
									int currentPageNum = pp.getCurrentPageNum();
									int pageSize = pp.getPageSize();
									int totalPage = pp.getTotalPage();
									String url = pp.getUrl();
									int prePageNum = currentPageNum-1<1?1:currentPageNum-1;
									int nextPageNum = currentPageNum+1>totalPage?totalPage:currentPageNum+1;
								%>
								<input type="button" class="btn" onclick="searchHumidityByCondition();" value="查询">
								<input class="btn" type="button" value="清空查询条件" onclick="clearSearchCondition();">
								<%-- <input class="btn" type="button" value="添加记录" 
									onclick="window.location.href='jsp/qy/warehouse_management/humidity/addHumidity.jsp?currentPageNum=<%=currentPageNum %>&pageSize=<%=pageSize %>'"> --%>
							</div>
						</form>
					</div>
				</div>
				<!-- 搜索框结束 -->

				<!-- 表格开始 -->
				<div class="temp-info" id="search-result-box">
					<div id="table-box">
					    <table class="temp-table">
					    	<thead>
					    		<tr class="temp-table-head">
					    			<td>
					    				湿度
					    			</td>
					    			<td style="width:200px;">
					    				记录湿度的时间
					    			</td>
					    			<td>
					    				位置
					    			</td>
					    		</tr>
					    	</thead>
					    	<tbody class="temp-table-body" id="humidity-table">
					    		<%
					    			ArrayList<Humidity> humidityList = pp.getHumodityRecords();
					    			for(int i=0;i<humidityList.size();i++){
					    		%>
					    		<tr>
					    		<td><%=humidityList.get(i).getHumidity()%></td>
					    		<td><%=humidityList.get(i).getCurRecordDate()%></td>
					    		<td><%=humidityList.get(i).getPosition()%></td>
					    		</tr>
					    		<%
					    		}
					    		%>
					    	</tbody>
					    </table>
					</div>
					<div id="humi-chart-container"></div>
					<div class="getrecord-way-box">
						<span>符合标准的记录数：<span id="matchNum"></span>条</span>
						<span>不符合标准的记录数：<span id="unmatchNum"></span>条</span>
						<button class="btn" onclick="searchHumidityByCondition();">以表格展示</button>
						<button class="btn" id="showChart">以折线图展示</button>
					</div>
				</div>
				<!-- 表格结束 -->
				<!-- 查看修改记录盒子 -->
				<div id="search-update-box" class="search-update-box">
					<div class="preupdate-info-box"id="oldRecord">
					</div>
					<div class="nowupdate-info-box" id="updateRecord">
					</div>
					<p>
						<input type="button" value="返回" class="smallbtn" onclick="returnToRecords();">
					</p>
				</div>
				<!-- 查看修改记录盒子结束 -->
			</div>
			<!-- 内容区结束 -->
			
			<!-- 分页的盒子开始 -->
			<div class="page-box" id="page-box">
				<span>
			    <a href="<%=url %>&num=1&pageSize=<%=pageSize%>&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>" >首页</a>
			    <a href="<%=url %>&num=<%=prePageNum%>&pageSize=<%=pageSize%>&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>" >&lt;&lt;</a>
			   	<span><%=currentPageNum %></span>
			    <a href="<%=url %>&num=<%=nextPageNum%>&pageSize=<%=pageSize%>&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>">&gt;&gt;</a>
			    <a href="<%=url %>&num=<%=totalPage%>&pageSize=<%=pageSize%>&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>">尾页</a>
			    </span>
			    <span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage()'>确定</a>
		   		<span>每页显示</span>
			    <select name="selectPageSize" onchange='selectPageSize(this.value)' id="selectPageSize">
			        <option value ="10">10</option>
			        <option value ="15">15</option>
			        <option value="20">20</option>
			    </select>
		    	<span>条记录，共</span>
		    	<label><%=totalPage %></label>
		    	<span>页</span>
			</div>
			<!-- 分页的盒子结束 -->
		</div>
		<%@include file="../../../../ConstantHTML/foot.html"%>
		<!-- 跳转到页面控制 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPage%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="<%=url %>&num="+skipPageNum+"&pageSize=<%=pageSize%>&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>";
			}
		</script>
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="<%=url %>&num=1&pageSize="+pageSize+"&startDate=<%=startDate%>&endDate=<%=endDate%>&startHumidity=<%=startHumidity%>&endHumidity=<%=endHumidity%>";
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
		<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/dateTime.js"></script>
	    <script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	    <script src="js/warehouse_management/highcharts.js"></script>
	    <script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	    <script type="text/javascript" src="js/warehouse_management/warehouse_management.js"></script>
	</body>
	
</html>
