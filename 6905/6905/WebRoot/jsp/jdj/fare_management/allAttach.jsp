<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Attach"%>
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
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/fare_management/uploadAttach.js"></script>
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
			查看附件
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!--查看附件子页面-->
			<div class="add-box" id="fare-add-box">
				<div class="add-top">
					<%
						//获得当前页
						//pageSize 按多少条分页
						int pageSize = 0;
						int curPageNum = 0;
						int fareId = Integer.parseInt(request.getParameter("fareId"));
						String builtType = (String) request.getAttribute("builtType");
						String startTime = (String) request.getAttribute("startTime");
						String endTime = (String) request.getAttribute("endTime");
						String storeCompany = (String) request.getAttribute("storeCompany");
						if (request.getAttribute("pageSize") != null) {
							pageSize = (Integer) request.getAttribute("pageSize");
						} else {
							pageSize = Integer.parseInt(request.getParameter("pageSize"));
						}
						if (request.getAttribute("curPageNum") != null) {
							curPageNum = (Integer) request.getAttribute("curPageNum");
						} else {
							curPageNum = Integer.parseInt(request
									.getParameter("curPageNum"));
						}
					%>
					<div class="op"></div>
				</div>
				<div class="file-list-box">
					<div class="file-table-box">
						<table>
							<thead>
								<tr>
									<th>附件</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<%
									ArrayList<Attach> allAttach = new ArrayList<Attach>();
									if (request.getAttribute("allAttach") != null) {
										allAttach = (ArrayList) request.getAttribute("allAttach");
										Attach attach;
										for (int i = 0; i < allAttach.size(); i++) {
											attach = allAttach.get(i);
											String realName = attach.getAttachTitle().substring(
													attach.getAttachTitle().indexOf("_") + 1);
								%>
								<tr>
									<td><%=realName%></td>
									<td class="file-list-box-td-small">
										<a class="download-btn" href="FareServlet?operate=download&curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&realName=<%=realName%>&path=<%=attach.getAttachPath()%>&fareId=<%=attach.getAttachId()%>">
											下载
										</a> 
										<input type="button" value="删除" onclick="deleteAttach('FareServlet?curPageNum=<%=curPageNum %>&pageSize=<%=pageSize %>&builtType=<%=builtType %>&startTime=<%=startTime %>&endTime=<%=endTime %>&storeCompany=<%=storeCompany %>&operate=deleteAttach&attachId=<%=attach.getAttachId()%>&fareId=<%=fareId%>')">
									</td>
								</tr>
								<%
									}
									}
								%>
							</tbody>
						</table>
						<div>
							<form id="fileForm"
								action="FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=upload&fareId=<%=fareId%>"
								enctype="multipart/form-data" name="files"
								onsubmit="return checkUpload()" method="post">
								<div class="file-btn-box">
									选择附件： <input id="up1" type="file" name="pic" multiple /> <input
										type="button" class="add-btn" id="upload1" value="上传"
										onclick="judgeFile()"> <input type="button"
										class="add-btn" value="返回"
										onclick="window.location.href='<%=basePath%>FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=check'">
								</div>
								<table id="file">
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdjfoot.html"%>
</body>
</html>
