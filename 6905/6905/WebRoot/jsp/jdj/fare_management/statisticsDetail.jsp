<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Fare"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>经费管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="js/fare_management/export.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" type="text/css"
	href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			统计经费
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!-- 费用查询信息界面 -->
			<%
				int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
						//pageSize 按多少条分页
						//首次进入页面时记得在路径上加上pageSize=10参数,Servlet跳转是也记得加上pageSize参数
					  int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					  long totalPageNum=0;
					  long sum=(Long)request.getAttribute("fareSum");
			%>
			<div class="fare-data" id="fare-data">
				<form name="checkForm" method="post">
					<div id="search-box">
						<p>
							费用类型： <input type="hidden" name="builtType"> <input
								type="checkbox" id="box" onclick="allSelect()"> 
								<!-- <label
								for="1">器材购置费</label><input id="1" name="fareType"
								type="checkbox" value="器材购置费">&nbsp;&nbsp;  -->
								<label
								for="2">运杂费</label><input id="2" name="fareType" type="checkbox"
								value="运杂费">&nbsp;&nbsp; <label for="3">轮换费</label><input
								id="3" name="fareType" type="checkbox" value="轮换费">&nbsp;&nbsp;
							<label for="6">维护保养费</label><input id="6" name="fareType"
								type="checkbox" value="维护保养费">&nbsp;&nbsp; <label
								for="8">其他</label><input id="8" name="fareType" type="checkbox"
								value="其他"> <input class="search-btn" type="button"
								onclick="checkInput('statistics')" value="费用统计"> <input
								class="search-btn" type="button"
								onclick="checkInput('statisticsDetail')" value="明细统计">
						</p>
						<p>
							时 间 段： <input type="text" id="startDate" class="sang_Calender"
								tabindex="-1" name="startTime" /> 到 <input type="text"
								id="endDate" class="sang_Calender" tabindex="-1" name="endTime" />
							<script	src="ConstantHTML/js/spdateTime.js"></script>
							代储企业： <input type="text" name="storeCompany"> <span
								class="btn"> <input class="search-btn" type="button"
								onclick="checkInput('search')" value="费用信息查询"> </span>
						</p>
						<p>
							查询的费用类型： <input type="text"
								value="<%=request.getAttribute("type")%>"
								class="choose-fareType-text">
						</p>
					</div>
				</form>
				<div id="fare-table-box">
					<form name="exportForm" method="post">
						<table id="fare-table" class="fare-table">
							<thead>
								<tr>
									<th class="fare-table-num-th">序号</th>
									<th class="fare-table-other-th">明细类型</th>
									<th class="fare-table-other-th">开始时间</th>
									<th class="fare-table-other-th">结束时间</th>
									<th class="fare-table-other-th">费用金额</th>
								</tr>
							</thead>
							<tbody>
								<%
									HashMap<String,Double> sumMap=new HashMap<String,Double>();
									                        	String startTime=(String)request.getAttribute("startTime");
									                        	String endTime = (String)request.getAttribute("endTime");
									                        	//每一个记录的金额 
									                        	sumMap=(HashMap)request.getAttribute("sumMap");
															Fare fare;
															fare=null;
															int id=0;
															Iterator iter = sumMap.entrySet().iterator();
								                            while (iter.hasNext()) {
								                           Map.Entry entry = (Map.Entry) iter.next();
								                           String  key = String.valueOf(entry.getKey());
								                           double  val = (Double)entry.getValue();
								                           id++;
								%>
								<tr>
									<td><%=id%></td>
									<td><%=key%></td>
									<td><%=startTime%></td>
									<td><%=endTime%></td>
									<td><%=val%></td>
								</tr>
								<%
									}
								%>
								<%-- <%
							 session.setAttribute("statisticsFareMap",sumMap);
	                         session.setAttribute("statisticsStartTime",startTime);
	                         session.setAttribute("statisticsEndTime",endTime);
							%> --%>
							</tbody>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
	<!-- 点击查询时，先判断输入框是否合法-->
	<script type="text/javascript"> 
		function checkInput(text){
			var startDate=document.getElementById("startDate").value;
			var endDate=document.getElementById("endDate").value;  
		    if(startDate!=""&&endDate==""){
		       	window.wxc.xcConfirm("结束时间不可为空","warning");
				return;
			}
			if(startDate==""&&endDate!=""){
			 	window.wxc.xcConfirm("起始时间不可为空","warning");
				return;
			}
			if(startDate>endDate){
				window.wxc.xcConfirm("起始时间大于结束时间","warning");
				return;
			}
			switch (text) {
			case "search":
				document.forms.checkForm.action="FareServlet?&operate=check&curPageNum=1&pageSize=<%= pageSize%>";  
				break;
			case "statistics":
			    document.forms.checkForm.action="FareServlet?&operate=statistics&curPageNum=1&pageSize=<%= pageSize%>";  
				break;
			case "statisticsDetail":
			    document.forms.checkForm.action="FareServlet?&operate=statisticsDetail&curPageNum=1&pageSize=<%= pageSize%>";  
				break;
			default:
				break;
			}
			document.forms.checkForm.submit();
		} 
	</script>
	<script>
	function allSelect() {// 全部选择，全部不选
		var mainbox = document.getElementById("box"), childboxes = document
			.getElementsByName("fareType"),
		flag = true;
		if (!mainbox.checked)
			flag = false;
		for ( var i = 0; i < childboxes.length; i++) {
			childboxes[i].checked = flag;
		}
	}
	</script>
</body>
</html>