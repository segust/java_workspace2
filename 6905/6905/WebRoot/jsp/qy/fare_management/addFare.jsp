<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Fare"%>
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
<title>经费管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/transact_business/jquery.combobox.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			添加经费
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!--添加经费子页面-->
			<%
				//获得当前的页码
				int curPageNum = Integer.parseInt(request
						.getParameter("curPageNum"));
				//获得最后的页码
				int totalPageNum = Integer.parseInt(request
						.getParameter("totalPageNum"));
				//pageSize 按多少条分页
				int pageSize = Integer.parseInt(request.getParameter("pageSize"));
				//后台用 select count(*)... 得到查询的全部数据的个数
				long sum = Long.parseLong(request.getParameter("sum"));
			%>
			<div class="add-box" id="fare-add-box">
				<form id="add-form"
					action="FareServlet?sum=<%=sum%>&curPageNum=<%=totalPageNum%>&pageSize=<%=pageSize%>&operate=add"
					method="post">
					<div class="add-mainfare-box" id="add-mainfare-box">
						<p>
							<label for="add-type">费用类型:</label> <select id="add-type"
								name="type" onchange="changeFare(this)">
								<!-- <option value="器材购置费">器材购置费</option> -->
								<option value="运杂费">运杂费</option>
								<option value="轮换费">轮换费</option>
								<option value="维护保养费">维护保养费</option>
								<option value="其他">其他</option>
							</select>
						</p>
						<p>
							<label for="add-qy">代储企业:</label> <input id="add-qy"
								class="add-box-input" name="company" placeholder="请输入代储企业"
								type="text" />
						</p>
						<p>
							<label for="add-JDS">军 &nbsp;代 &nbsp;室:</label> <input
								id="add-JDS" class="add-box-input" name="JdRoom"
								placeholder="请输入军代室" type="text" />
						</p>
						<p>
							<input id="add-date" type="hidden" name="date"
								value="<%=MyDateFormat.changeDateToString(new Date())%>" />
						</p>
						<p>
							<label for="add-allmoney">总 &nbsp;金 &nbsp;额:</label> <input
								id="add-allmoney" type="text" name="allMoney"
								class="add-box-input"
								onfocus="window.wxc.xcConfirm('总金额通过明细金额加和得出','info')"
								readonly="readonly" /> <input type="hidden"
								id="add-allmoney-hidden" value="">
						</p>
						<p>
							<label for="add-remark">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
							<input id="add-remark" class="add-box-input" name="remark"
								placeholder="请输入备注" type="text" />
						</p>
						<p id="w">
							<input id="Hidden1" name="Hidden1" type="hidden" value="" /> <input
								id="Hidden2" name="Hidden2" type="hidden" value="" />
						</p>
						<p>
							<!-- <input type="button" class="addDetail-btn" name="tianjia2" id="add-faredetail-input"
								onclick="addDetailBox('器材购置费')" value="添加明细" /> -->
							<input type="button" class="addDetail-btn" name="tianjia2"
								id="add-faredetail-input" onclick="addDetailBox('运杂费')"
								value="添加明细" /> <input class="faredetail-submit-btn"
								type="button"
								onclick="addFare(<%=sum%>,<%=pageSize%>,'add','<%=basePath%>');"
								value="添加" /> <a
								href="FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>"
								class="faredetail-return-btn">返回</a>
						</p>
					</div>
					<div class="add-detailfare-box" id="add-detailfare-box">
						<div>
							<p style="position: relative;">
								<span class="delete-icon" onclick="deleteFareDetail(this)">
								</span> <span> 项目： <input type="text"
									class="small-text-input project"> </span> <span> 时间： <select
									style="width: 65px;margin-left: 0px;"
									onchange="changeDay(this,'1')">
										<%
										Calendar c=Calendar.getInstance();
										int year=c.get(Calendar.YEAR);
										int month=c.get(Calendar.MONTH)+1;
										int day=0;
										if(month==1)
											day=c.get(Calendar.DATE);
										else
											day=31;
										for(int i=year;i>=year-5;i--){
									%>
										<option value="<%=i%>"><%=i%></option>
										<%
									} 
									%>
								</select> <label>-</label> <select style="width: 45px;margin-left: 0px;"
									onchange="changeDay(this,'2')">
										<%
											for(int i=1;i<=month;i++){ 
										%>
										<option value="<%=i %>"><%=i %></option>
										<%
										}
										 %>
								</select> <label>-</label> <select style="width: 45px;margin-left: 0px;">
										<%
											for(int i=1;i<=day;i++){ 
										%>
										<option value="<%=i %>"><%=i %></option>
										<%
										}
										 %>
								</select> <!-- <input type="text" class="sang_Calender" style="width: 150px;background: none;">
									<script	type="text/javascript" src="js/transact_business/dateTime.js"></script> -->
								</span> <span> 凭证号： <input type="text" class="small-text-input">
								</span> <span> 金额： <input type="text" class="small-text-input"
									onfocus="minusMoney(this)" onblur="getAllMoney(this)" /> </span>
							</p>
							<p>
								<span> 摘要： <input type="text" class="big-text-input">
								</span> <span> 备注： <input type="text" class="big-text-input">
								</span>
							</p>
							<p class="add-detail-bottom-p"></p>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="js/fare_management/addFare.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script>
	$(function() {
		$('.project').combobox(['运输', '装卸', '保险']);//这行combobox函数内的参数应为sugestions
	});
	</script>
	<script src="js/transact_business/jquery.js"></script>
	<script src="js/transact_business/jquery.bgiframe.pack.js"></script>
	<script src="js/transact_business/jquery.combobox.newin.js"></script>
</body>
</html>
