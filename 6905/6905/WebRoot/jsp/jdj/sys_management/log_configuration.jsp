<%@page import="cn.edu.cqupt.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>日志管理</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/sys_management/systemmanage.css" />
		<script type="text/javascript" src="js/statistics/statistics.js"></script>
		<script type="text/javascript" src="js/sys_management/tools.js"></script>
	</head>

	<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/jdjleft.html"%>
		<div id="right">
			<!-- 二级标题 -->
			<div class="subName">
				<span>当前位置：</span>
      			<a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="SystemManagementServlet?operate=logConfiguration&curPageNum=1&pageSize=10">日志管理</a>
			</div>

			<!-- 内容区开始 -->
			<div class="fare-data" id="fare-data">
				<!--日志管理子页面-->
				<!--搜索框开始-->
				<div class="search_box">
					<form
						action="SystemManagementServlet?operate=logConfiguration&curPageNum=1&pageSize=10"
						method="post">
						<span>用户名</span>
						<span>
							<input class="setTem" name="username" type="text" />
						</span>
						<span>操作类型</span>
						<span>
							<input class="setTem" name="operateType" type="text" />
						</span>
						<span>时间</span>
						<span>
							<input type="text" class="sang_Calender" name="fromdate" />
						</span>
						<span>到</span>
						<span>
							<input type="text" class="sang_Calender" name="todate" />
						</span>
						<span>
							<input class="scan-btn" type="submit" value="查询">
							<input type="button" value="导出" class="scan-btn">
						</span>
					</form>
				</div>
				<!--搜索框结束-->

				<!-- 表格开始 -->
				<div id="log_configuration-box">
					<table  id="log_configuration">
						<thead>
							<tr>
								<th>
									<input type="checkbox" id="checkboxLeader" onclick="chooseAll();">
								</th>
								<th>
									序号
								</th>
								<th>
									用户名
								</th>
								<th>
									操作类型
								</th>
								<th style="width: 200px;">
									操作时间
								</th>
								<th>
									备注
								</th>
							</tr>
						</thead>
						<%
						//得到当前查询条件
						HashMap<String, Object> condition = (HashMap<String, Object>)request.getAttribute("condition");
						//request.setAttribute("condition", condition);
						//curPageNum 当前页
						int curPageNum=Integer.parseInt((String)condition.get("curPageNum"));
						//pageSize 按多少条分页
						int pageSize=Integer.parseInt((String)condition.get("pageSize"));
						//logSum 全部设备信息的个数
						int logSum=(Integer)condition.get("sum");
						//根据总数和每页条数计算分多少页
						int totalPageNum = logSum%pageSize==0?logSum/pageSize:(logSum/pageSize+1);
						if(totalPageNum==0){
							totalPageNum=1;
							curPageNum=1;
							}
						//上一页的页码
						int prePageNum = curPageNum-1<1?1:curPageNum-1;
						//下一页的页码
						int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
						String username = (String)condition.get("username");
						String fromdate = (String)condition.get("fromdate");
						String todate = (String)condition.get("todate");
						String operateType = (String)condition.get("operateType");
										
                   		String message=(String)request.getAttribute("message");
                   		if(StringUtil.isNotEmpty(message)){
                   	%>
						<span><%=message %></span>
						<tbody>
						<%
                   		}else{
	                   		List<Map<String, String>> logList=(List<Map<String, String>>)request.getAttribute("logList");
	                   		for(int i=0;i<logList.size();i++){
                    	%>
						
							<tr class="contract-add-table-body">
								<td>
									<input class="setTem" type="checkbox" name="checkbox_strength" />
								</td>
								<td><%=(curPageNum-1)*pageSize + 1 + i %></td>
								<td><%=logList.get(i).get("userName")%></td>
								<td><%=logList.get(i).get("operateType")%></td>
								<td><%=logList.get(i).get("operateTime")%></td>
								<%
									String remark=logList.get(i).get("remark");
									String subremark="";
									if(remark.length()>=30){
										subremark=remark.substring(0, 30)+"...";
								 %>
									<td style="position:relative">
										<%=subremark%>
										<input type="hidden" value="<%=remark%>">
										<a onclick="showMoreRemark(this)" class="get-logmore-a">
											查看更多
											<div id="show-logremark-box" class="show-logremark-box">
											</div>
										</a>
									</td>
								<%
									}else{
								 %>
								 	<td>
										<%=remark%>
									</td>
								<%
									}
								 %>
							</tr>
							<%	                   		
                       		}
                   		} 
                   		%>
						</tbody>
					</table>
				</div>
				<!-- 表格结束 -->
			</div>
			<!-- 内容区结束-->

			<!-- 分页的盒子开始 -->
			<div class="page-box">
				<span> <a
					href="SystemManagementServlet?operate=logConfiguration&curPageNum=1&pageSize=<%=pageSize%>&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>">首页</a>
					<a
					href="SystemManagementServlet?operate=logConfiguration&curPageNum=<%=prePageNum%>&pageSize=<%=pageSize%>&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>">&lt;&lt;</a>
					<span><%=curPageNum%></span> <a
					href="SystemManagementServlet?operate=logConfiguration&curPageNum=<%=nextPageNum%>&pageSize=<%=pageSize%>&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>">&gt;&gt;</a>
					<a
					href="SystemManagementServlet?operate=logConfiguration&curPageNum=<%=totalPageNum%>&pageSize=<%=pageSize%>&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>">尾页</a>
				</span>
				<span>跳到第</span>
				<input type="text" id="skipPageNum" />
				<span>页</span>
				<a onclick='skipPage()'>确定</a>
				<span>每页显示</span>
				<select name="selectPageSize" onchange='selectPageSize(this.value)'
					id="selectPageSize">
					<option value="10">
						10
					</option>
					<option value="15">
						15
					</option>
					<option value="20">
						20
					</option>
				</select>
				<span>条记录，共</span>
				<label><%=totalPageNum %></label>
				<span>页</span>
			</div>
			<!-- 分页的盒子结束 -->

		</div>
		<%@include file="../../../ConstantHTML/jdjfoot.html"%>
		
		<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=document.getElementById('skipPageNum').value;
			 	var pageSize=<%=pageSize %>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="SystemManagementServlet?operate=logConfiguration&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>";	
			}
		</script>

		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="SystemManagementServlet?operate=logConfiguration&curPageNum=1&pageSize="+value+"&username=<%=username%>&fromdate=<%=fromdate%>&todate=<%=todate%>&operateType=<%=operateType%>";
			}
		</script>

		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
			var pageSize=<%=pageSize %>;
			if(pageSize=="null")
				document.getElementById("selectPageSize").value="10";
			else
				document.getElementById("selectPageSize").value=pageSize;
		</script>
	</body>
	<script type="text/javascript" src="ConstantHTML/js/dateTime.js"></script>
</html>
