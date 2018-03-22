<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Fare"%>
<%@page import="cn.edu.cqupt.beans.FareDetail"%>
<%@page import="cn.edu.cqupt.service.fare_management.FareService"%>
<%@page import="cn.edu.cqupt.service.fare_management.FareDetailService"%>
<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>查看经费详情</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/fare_management/addFare.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdjleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			查看经费明细
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!--编辑经费子页面-->
			<div class="add-box" id="fare-add-box">
				<%
						   //获得当前页
							int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
							//pageSize 按多少条分页
							int pageSize=Integer.parseInt(request.getParameter("pageSize"));
							String builtType=(String)request.getParameter("builtType");
	                        String startTime=(String)request.getParameter("startTime");
	                        String endTime = (String)request.getParameter("endTime");
	                        String storeCompany = (String)request.getParameter("storeCompany");
				%>
				<%
						  //根据ID查找需要修改的费用记录
						  int fareId =Integer.parseInt(request.getParameter("fareId")) ;
						  FareService fareService = new FareService();
						  Fare fare = fareService.getOneFareById(fareId);
						  String type=fare.getFareType();
					%>
				<!--   <form action = "/6905/FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%= pageSize%>&operate=edit" method="post">-->
				<form id="edit-form"
					action="FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%= pageSize%>&builtType=<%=builtType %>&startTime=<%=startTime %>&endTime=<%=endTime %>&storeCompany=<%=storeCompany %>&operate=edit"
					method="post">
					<input type="hidden" name="fareId" value=<%=fareId%>>
					<div class="add-mainfare-box" id="add-mainfare-box">
						<p>
							<label for="edit-type">费用类型:</label> 
							<select name="type" id="edit-type">
								<%-- <option value="购置费" <%="购置费".equals(type)?"selected":"" %>>
									器材购置费</option> --%>
								<option value="运杂费" <%="运杂费".equals(type)?"selected":"" %>>
									运杂费</option>
								<option value="轮换费" <%="轮换费".equals(type)?"selected":"" %>>
									轮换费</option>
								<option value="维护保养费" <%="维护保养费".equals(type)?"selected":"" %>>
									维护保养费</option>
								<option value="其他" <%="其他".equals(type)?"selected":"" %>>
									其他</option>
							</select>
						</p>
						<p>
							<label for="edit-qy">代储企业:</label> <input id="edit-qy"
								class="edit-box-input" name="company"
								value="<%=fare.getStoreCompany()%>" type="text" />
						</p>
						<p>
							<label for="edit-JDS">军代室:</label> <input
								id="edit-JDS" class="edit-box-input" name="JdRoom"
								value="<%=fare.getJdRoom()%>" type="text" />
						</p>
						<p>
							<label for="edit-price">总金额:</label> 
							<input
								id="add-allmoney" class="edit-box-input" name="amount"
								value="<%=fare.getFareAmount()%>" type="text" readonly />
							<input type="hidden" id="add-allmoney-hidden" value="">
						</p>
						<p>
							<label for="edit-remark">
								备注: </label> <input
								id="edit-remark" class="edit-box-input" name="remark"
								value="<%=fare.getRemark()%>" type="text" />
						</p>
						<p>
							<input type="button" class="addDetail-btn"
								value="返回"
								onclick="window.location.href='<%=basePath%>FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>'">
						</p>
					</div>
					<%
                      			FareDetailService detailService=new FareDetailService();
					 	 		List<FareDetail> allDetail = new ArrayList<FareDetail>();
					  			allDetail=detailService.getAllFareDetail(fareId); 
	               				out=pageContext.getOut();
	               				out.write("<div class='add-detailfare-box' id='add-detailfare-box'>");
	               				for( int j=0;j<allDetail.size();j++){
                   					out.write("<div>");
                   					out.write("<p>");
                   					//out.write("<span class=\"delete-icon\" onclick=\"deleteFareDetail(this)\"></span>");
                   					out.write("<span>项目：<input type='text' class='small-text-input' value='"+allDetail.get(j).getDetailName()+"'></span>");
                   					out.write("<span>时间：<input type='text' class='small-text-input' value='"+allDetail.get(j).getDetailTime()+"'></span>");
                   					out.write("<span>凭证号：<input type='text' class='small-text-input' value='"+allDetail.get(j).getVoucherNo()+"'></span>");
                   					out.write("<span>金额：<input type='text' class='small-text-input' value='"+allDetail.get(j).getDetailAmount()+"' onfocus='minusMoney(this)' onblur='getAllMoney(this)'></span>");
                   					out.write("</p>");
                   					out.write("<p>");
                   					out.write("<span>摘要：<input type='text' class='big-text-input' value='"+allDetail.get(j).getDetailAbstract()+"'></span>");
                   					out.write("<span>备注：<input type='text' class='big-text-input' value='"+allDetail.get(j).getRemark()+"'></span>");
                   					out.write("</p>");
                   					out.write("</div>");
                   					out.write("<p class='add-detail-bottom-p'></p>");
                    			}
                    			out.write("</div>");
                     %>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
</body>
</html>