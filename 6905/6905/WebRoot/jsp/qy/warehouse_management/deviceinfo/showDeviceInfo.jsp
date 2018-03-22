<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.cqupt.beans.Device"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>库房设备信息</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css"
			href="css/warehouse_management/deviceinfo.css">
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	</head>

	<body>
		<%@include file="../../../../ConstantHTML/top.html"%>
		<%@include file="../../../../ConstantHTML/left.html"%>
		<div id="right">
			<!-- 当前位置 -->
			<div class="subName" id="subName">
				<span>当前位置：</span>库房设备信息
			</div>
			<div class="content" id="device-content-box">
				<div class="rightTop" id="device-search-box">
					<div class="deviceinfo-op">
						<p class="p1">
							<label>设备进库时间</label>
							<!--
							<span style="display: inline-block;">
								<input type="text" class="sang_Calender" id="deviceStartTime">
								<script type="text/javascript" src="js/transact_business/dateTime.js"></script>
							</span>
							<span>到</span>
							<span style="display: inline-block;">
								<input type="text" class="sang_Calender" id="deviceEndTime">
								<script type="text/javascript" src="js/transact_business/dateTime.js"></script>
							</span>
							-->
							<input type="text" class="sang_Calender" id="deviceStartTime">
							<label>到</label>
								<input type="text" class="sang_Calender" id="deviceEndTime">
							<% 
								ArrayList<String> deviceTypeList=(ArrayList<String>)request.getAttribute("deviceTypeList"); 
							%>
							<label>状态</label>
							<select id="chooseStatus">
								<option value="全部状态">全部状态</option>
								<option value="使用">使用</option>
								<option value="维修">维修</option>
								<option value="报废">报废</option>
							</select>
							<label>设备类型</label>
							<select id="searchDeviceType" class="select-box">
								<option value="所有设备">
									所有设备
								</option>
								<%for (String deviceType : deviceTypeList) {%>
								<option value="<%=deviceType %>">
									<%=deviceType %>
								</option>	
								<%}%>
							</select>
						</p>
						<p class="p2">
							<input type="button" value="查询" class="device-btn" onclick="searchThroughButton();">
							<input type="button" value="清空条件" class="device-btn" onclick="clearCondition();" >
							<input type="button" value="添加设备记录" class="device-btn"
								onclick="showAddDeviceForm();">
						</p>
					</div>
				</div>
				<!-- 设备信息表的盒子 -->
				<div class="device-info" id="device-info-box">
					<table class="device-table">
						<thead>
							<tr class="device-table-head">
								<td>
									设备名称
								</td>
								<td>
									设备编号
								</td>
								<td>
									所在位置
								</td>
								<td>
									启用时间
								</td>
								<td>
									状态
								</td>
								<td>
									操作
								</td>
							</tr>
						</thead>
						<tbody class="device-table-body" id="device-table-body">
							<%
								ArrayList<Device> firstDeviceList = new ArrayList<Device>();
								firstDeviceList = (ArrayList<Device>) request
										.getAttribute("firstDeviceList");
								for (int i = 0; i < firstDeviceList.size(); i++) {
							%>
							<tr>
								<td><%=firstDeviceList.get(i).getDeviceName()%></td>
								<td><%=firstDeviceList.get(i).getDeviceNo()%></td>
								<td><%=firstDeviceList.get(i).getPosition()%></td>
								<td><%=firstDeviceList.get(i).getDeviceInTime()%></td>
								<%
									String tempStr = firstDeviceList.get(i).getRepairTime();
								%>
								<%-- <%
										if (!tempStr.equals("") && tempStr != null) {
								%>
								<td><%=firstDeviceList.get(i).getRepairTime()%></td>
								<%
									} else {
								%>
								<td>
									无
								</td>
								<%
									}
								%> --%>
								<td><%=firstDeviceList.get(i).getStatus() %></td>
								<td>
									<%-- <input type="button" name="deleteDevice" value="删除设备"
										onclick="deleteDevice(this,<%=firstDeviceList.get(i).getDeviceId()%>);"> --%>
									<input type="button" name="updateDevice" value="修改设备信息"
										onclick="showUpdateDeviceForm(this,<%=firstDeviceList.get(i).getDeviceId()%>);">
									<%
										if(!firstDeviceList.get(i).getStatus().equals("报废")){
									%>
									<input type="button" name="addRepairRecord" value="添加维修记录"
										onclick="showRepairDeviceForm(this,<%=firstDeviceList.get(i).getDeviceId()%>)">
									<%
									}
									 %>	
									<%
										if (!tempStr.equals("") && tempStr != null) {
									%>
									<input type="button" name="getRepairRecord" value="查看维修记录"
										onclick="showCurDeviceRepairInfoForm(<%=firstDeviceList.get(i).getDeviceId()%>);">
									<%
									}
									%>
								</td>
							</tr>
							<%
								}
							%>
						</tbody>
					</table>
				</div>
				<!-- 添加设备的盒子 -->
				<div id="add-device-box" class="device-op-box">
					<form>
						<p>
							设备名称：
							<input type="text" id="addDeviceName" />
						</p>
						<p>
							设备编号：
							<input type="text" id="addDeviceNum" />
						</p>
						<p>
							所在位置：
							<input type="text" id="addPosition" />
						</p>
						<p>
							启用时间：
							<input type="text" id="addDeviceInTime" class="sang_Calender">
						</p>
						<p>
							状态：
							<input type="text" id="addstatus" value="使用" readonly="readonly">
						</p>
						<p>
							<input type="button" class="device-btn" value="添加"
								onclick="addDevice();">
							<input type="reset" class="device-btn" value="重置">
							<input type="button" class="device-btn" value="返回"
								onclick="returnToInfo();">
						</p>
					</form>
				</div>
				<!-- 修改设备的盒子 -->
				<div id="update-device-box" class="device-op-box">
					<form>
						<p>
							设备名称：
							<input type="text" />
						</p>
						<p>
							设备编号：
							<input type="text" />
						</p>
						<p>
							所在位置：
							<input type="text" />
						</p>
						<p>
							启用时间：
							<input type="text" class="sang_Calender"/>
						</p>
						<p>
							状态：
							<select id="updatestatus">
								<option value="使用">使用</option>
								<option value="维修">维修</option>
								<option value="报废">报废</option>
							</select>
						</p>
						<p>
							<input type="button" class="device-btn" value="修改"
								onclick="updateDevice();">
							<input type="reset" class="device-btn" value="重置">
							<input type="button" class="device-btn" value="返回"
								onclick="returnToInfo();">
						</p>
					</form>
					<input type="hidden" id="updateDeviceId">
				</div>
				<!-- 添加维修记录的盒子 -->
				<div id="add-repair-box" class="device-op-box">
					<form>
						<p>
							维修人：
							<input type="text" />
						</p>
						<p>
							维修设备：
							<input type="text" readonly="readonly" />
						</p>
						<p>
							设备编号：
							<input type="text" readonly="readonly" />
						</p>
						<p>
							维修时间：
							<input type="text" class="sang_Calender" />
						</p>
						<p>
							维修原因：
							<input type="text" />
						</p>
						<p>
							<%String referer=request.getHeader("Referer"); %>
							<input type="button" class="device-btn" value="添加维修记录"
								onclick="addRepairRecord('<%=referer%>')">
							<input type="button" class="device-btn" value="重置"
								onclick="resetRepairInfo(this);">
							<input type="button" class="device-btn" value="返回"
								onclick="returnToInfo();">
						</p>
					</form>
					<input type="hidden" id="repairDeviceId">
				</div>
				<!-- 分页盒子 -->
				<div class="page-box" id="page-box">
					<span> 
						<a onclick="searchThroughButton('firstPage')">首页</a> 
						<a onclick="searchThroughButton('prePage')">&lt;&lt;</a> 
						<span id="curPageNum">1</span>
						<a onclick="searchThroughButton('nextPage')">&gt;&gt;</a> 
						<a onclick="searchThroughButton('lastPage')">尾页</a> 
					</span>
					<span>跳到第</span>
					<input type="text" id="skipPageNum" />
					<span>页</span>
					<a onclick="searchThroughButton('skipPage')">确定</a>
					<span>每页显示</span>
					<select name="selectPageSize" onchange="searchThroughButton('firstPage')"
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
					<label id="pageSum"><%=request.getAttribute("pageSum") %></label>
					<span>页</span>
				</div>
			</div>
			
			<!-- 查看某一个设备维修信息内容的盒子 -->
			<div class="content" id="repair-content-box"  style="display: none;">
				<div class="toptitle" id="search-repair-box">
					<div class="deviceinfo-op">
						<form>
							<input type="button" value="返回" style="position: absolute;left: 1000px;top: 120px;" class="device-btn" onclick="returnToInfoFromRepair();">
						</form>
					</div>
				</div>
				<!-- 查看某一个设备维修信息表的盒子 -->
				<div class="device-info" id="repair-info-box">
					<table class="device-table">
						<thead>
							<tr class="device-table-head">
								<td>
									维修人
								</td>
								<td>
									维修时间
								</td>
								<td>
									维修原因
								</td>
								<td>
									操作
								</td>
							</tr>
						</thead>
						<tbody class="device-table-body" id="repair-info-tbody">

						</tbody>
					</table>
				</div>
				<!-- 设备维修信息分页的盒子 -->
				<!-- <div class="page-box" id="page-box">
					<span> 
						<a onclick="searchThroughButton('firstPage')">首页</a> 
						<a onclick="searchThroughButton('prePage')">&lt;&lt;</a> 
						<span id="curPageNum">1</span>
						<a onclick="searchThroughButton('nextPage')">&gt;&gt;</a> 
						<a onclick="searchThroughButton('lastPage')">尾页</a> 
					</span>
					<span>跳到第</span>
					<input type="text" id="skipPageNum" />
					<span>页</span>
					<a onclick="searchThroughButton('skipPage')">确定</a>
					<span>每页显示</span>
					<select name="selectPageSize" onchange="searchThroughButton('firstPage')"
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
					<label></label>
					<span>页</span>
				</div> -->
			</div>
		</div>
		<%@include file="../../../../ConstantHTML/foot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="js/warehouse_management/deviceinfo.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/dateTime.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script> 
	</body>
</html>
