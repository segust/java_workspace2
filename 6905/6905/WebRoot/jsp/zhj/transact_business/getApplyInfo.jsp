<%@ page language="java" import="java.util.*,cn.edu.cqupt.beans.*"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

<title>审核申请</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css"
	href="css/transact_business/transactQueryProduct.css" />
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="js/transact_business/jdscheck.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/transact_business/content.css" />
<script type="text/javascript">
			var deleteFlag=<%=request.getAttribute("deleteFlag")%>;
			if(deleteFlag==1)
				alert("删除成功");
			else if(deleteFlag==0)
				alert("删除失败");
		</script>
<script type="text/javascript">
			var addFlag=<%=request.getAttribute("addFlag")%>;
			var repeatFlag=<%=request.getAttribute("repeatFlag")%>;
			var str="";
			if(repeatFlag==1)
				str+="不允许重复提交\n";
			if(addFlag==1)
				str+="添加成功";
			else if(addFlag==0)
				str+="添加失败";
			if(str!="")
				alert(str);
		</script>
<script type="text/javascript">
			var updateFlag=<%=request.getAttribute("updateFlag")%>;
			if(updateFlag==1)
				alert("修改成功");
			else if(updateFlag==0)
				alert("修改失败");
		</script>
</head>
<body>
	<%@include file="../../../../ConstantHTML/top.html"%>
	<%@include file="../../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 二级标题开始 -->
		<div id="subName" class="subName">
			<span>当前位置：</span> <a
				href="TemperatureServlet?operate=getAllTemperature">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			导入申请
		</div>
		<!-- 二级标题结束 -->

		<!-- 内容区开始 -->
		<div class="content">
			<div>
				<%
					List<ArrayList<String>> list = (List<ArrayList<String>>)request.getAttribute("dyadicArray");
					String applyType =(String)request.getAttribute("applyType");
					String chType = "";
					int idIndex=0,meansIndex=0,batchIndex=0,execDateIndex=0,numIndex=0,statusIndex=0,ownedUnitIndex=0,borrowLength=0,oldNumIndex=0;
					if("RK".equals(applyType)) {
						chType="入库审核";
					}else if("LHRK".equals(applyType)) {
						chType="轮换入库审核";
					}else if("LHCK".equals(applyType)) {
						chType="轮换出库审核";
					}else if("GXCK".equals(applyType)) {
						chType="更新出库审核";
					}else if("GXRK".equals(applyType)) {
						chType="更新入库审核";
					}
				 %>
				<span id="checkmsg"
					style="color: blue;width: 200px;margin-left: auto;margin-right: auto;"></span>
				<div style="float: left;margin-left: 10px;margin-top:20px;">
					<input type="button" class="search-btn" style="width: 100px;"
						value="一键通过"
						onclick="allcheck('<%=applyType %>','<%=chType %>已通过');" /> <input
						type="button" class="search-btn" style="width: 100px;"
						value="一键不通过"
						onclick="allcheck('<%=applyType %>','<%=chType %>不通过');" />
					<%-- 	<c:if test="${isIn}"><input type="button" class="search-btn" style="width: 80px;background-color: red; margin-left:500px;" value="同步数据库" onclick="synDatabase('<%=applyType %>');"/></c:if> --%>
					<%-- <c:if test="${isUpdateOutApply || isBorrowOutApply}"><input type="button" class="search-btn" style="width: 80px;background-color: red; margin-left:500px;" value="同步数据库" onclick="synUpdateDatabase('<%=applyType %>');"/></c:if> --%>
					<input type="button" class="search-btn" style="width: 80px;"
						value="审核通过"
						onclick="checkPass('<%=applyType %>','<%=chType %>已通过',this)" /> <input
						type="button" class="search-btn" style="width: 80px;"
						value="审核不通过"
						onclick="checkPass('<%=applyType %>','<%=chType %>不通过',this)" /> <input
						type="button" id="outTable" class="search-btn"
						style="width: 100px;background-color: grey;" disabled="true"
						value="导出审核结果" onclick="downloadAll('<%=applyType %>')" />
				</div>
				<div id="whole"></div>
				<div id="borrow_In">
					<table class="contract-add-table">
						<thead>
							<tr>
								<th>整机名称</th>
								<th>单元名称</th>
								<th>机号</th>
								<th>品名内码</th>
								<th>计量单位</th>
								<th>承制单位</th>
								<th>代储单位</th>
								<th>单价</th>
								<th>摆放位置</th>
								<th>生产时间</th>
								<th>企业定期维护时间</th>
								<th>产品状态</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="borrowin_Table">
						</tbody>
					</table>
					<input type="button" value="确定" id="sure_borrow_In">
				</div>
				<div id="borrow_Out">
					<table class="contract-add-table">
						<thead>
							<tr>
								<th>整机名称</th>
								<th>单元名称</th>
								<th>机号</th>
								<th>品名内码</th>
								<th>计量单位</th>
								<th>承制单位</th>
								<th>代储单位</th>
								<th>单价</th>
								<th>摆放位置</th>
								<th>生产时间</th>
								<th>维护周期</th>
								<th>产品状态</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="borrowout_Table">
						</tbody>
					</table>
					<input type="button" value="确定" id="sure_borrow_Out">
				</div>
				<div id="update">
					<table class="contract-add-table">
						<thead>
							<th>整机名称</th>
							<th>单元名称</th>
							<th>机号</th>
							<th>单价</th>
							<th>原产品单价</th>
							<th>计量单位</th>
							<th>承制单位</th>
							<th>代储单位</th>
							<th>品名内码</th>
							<th>摆放位置</th>
							<th>存储期限</th>
							<th>企业定期维护时间</th>
							<th>产品状态</th>
							<th>生产时间</th>
							<th>备注</th>
						</thead>
						<tbody id="update_table">
						</tbody>
					</table>
					<input type="button" value="确定" id="sure_update">
				</div>
				<div id="new_In">
					<table class="contract-add-table">
						<thead>
							<th>合同编号</th>
							<th>产品名称</th>
							<th>产品型号</th>
							<th>机号</th>
							<th>整机名称</th>
							<th>单元名称</th>
							<th>计量单位</th>
							<th>单价</th>
							<th>品名内码</th>
							<th>承制单位</th>
							<th>代储单位</th>
							<th>摆放位置</th>
							<th>维护周期</th>
							<th>生产时间</th>
							<th>产品编号</th>
							<th>产品状态</th>
							<th>存储期限</th>
							<th>备注</th>
						</thead>
						<tbody id="new_Table">
						</tbody>
					</table>
					<input type="button" value="确定" id="sure_new_In">
				</div>
				<div id="table-box"
					style="margin-left: auto;margin-right: auto; float:left;">
					<table class="temp-table" style="margin-top: 10px;" id="mytable">
						<thead>
							<tr class="temp-table-head">
								<td><input type="checkbox" name="checkall"
									onchange="getAll()" /></td>
								<td>序号</td>
								<td>操作类型</td>
								<td>批次</td>
								<td>操作时间</td>
								<td>数量</td>
								<c:if test="${isBorrowOutApply }">
								<td>轮换时间</td>
								</c:if>
								<c:if test="${isUpdateOutApply }">
								<td>原数量</td>
								</c:if>
								<td>代储企业</td>
								<td>状态</td>
								<td>审核人</td>
								<td>操作</td>
							</tr>
						</thead>
						<c:if test="${runStatus }">
							<tbody class="temp-table-body">
								<%
					    		boolean isIn = (Boolean)request.getAttribute("isIn");
					    		int i;
					    		if(isIn) {
					    			List<String> indexs = list.get(0);
					    			for(int j=0;j<indexs.size();j++) {
					    				String index = indexs.get(j);
					    				if("inId".equals(index)) {
					    					idIndex = j;
					    				}else if("inMeans".equals(index)) {
					    					meansIndex = j;
					    				}else if("batch".equals(index)) {
					    					batchIndex = j;
					    				}else if("num".equals(index)) {
					    					numIndex = j;
					    				}else if("execDate".equals(index)) {
					    					execDateIndex = j;
					    				}else if("chStatus".equals(index)) {
					    					statusIndex = j;
					    				}else if("ownedUnit".equals(index)) {
					    					ownedUnitIndex = j;
					    				}
					    			}
					    		if(list.size() > 0) {
					    			for(i=1;i<list.size();i++) {
					    	 %>
								<tr>
									<td><input type="checkbox" name="mycheck" /> <input
										type="hidden" value="<%=list.get(i).get(idIndex)%>" /> <input
										type="hidden" value="<%=list.get(i).get(ownedUnitIndex)%>" />
									</td>
									<td><%=i%></td>
									<td><%=list.get(i).get(meansIndex) %></td>
									<td><%=list.get(i).get(batchIndex) %></td>
									<td><%=list.get(i).get(execDateIndex) %></td>
									<td><%=list.get(i).get(numIndex)%></td>
									<td><%=list.get(i).get(ownedUnitIndex)%></td>
									<td><%=list.get(i).get(statusIndex) %></td>
									<td><p name="user" style="display: none;"
											class="info_user"><%=request.getAttribute("username")%></p></td>
									<td><input type="button" value="查看详情" class="search-btn"
										onclick="detail_Apply(this);" /></td>
								</tr>
								<%	}
					    	}
					    } else {
					    	List<String> indexs = list.get(0);
					    			for(int j=0;j<indexs.size();j++) {
					    				String index = indexs.get(j);
					    				if("outId".equals(index)) {
					    					idIndex = j;
					    				}else if("outMeans".equals(index)) {
					    					meansIndex = j;
					    				}else if("batch".equals(index)) {
					    					batchIndex = j;
					    				}else if("num".equals(index)) {
					    					numIndex = j;
					    				}else if("execDate".equals(index)) {
					    					execDateIndex = j;
					    				}else if("chStatus".equals(index)) {
					    					statusIndex = j;
					    				}else if("ownedUnit".equals(index)) {
					    					ownedUnitIndex = j;
					    				}else if("borrowLength".equals(index)) {
					    						borrowLength = j;
					    				}else if("oldNum".equals(index)) {
					    						oldNumIndex = j;
					    				}
					    			}
					    		if(list.size() > 0) {
					    			for(i=1;i<list.size();i++) {
					    	 %>
								<tr>
									<td><input type="checkbox" name="mycheck" /> <input
										type="hidden" value="<%=list.get(i).get(idIndex)%>" /> <input
										type="hidden" value="<%=list.get(i).get(ownedUnitIndex)%>" />
									</td>
									<td><%=i%></td>
									<td><%=list.get(i).get(meansIndex) %></td>
									<td><%=list.get(i).get(batchIndex) %></td>
									<td><%=list.get(i).get(execDateIndex) %></td>
									<td><%=list.get(i).get(numIndex)%></td>
									<c:if test="${isBorrowOutApply }">
										<td><%=list.get(i).get(borrowLength)%></td>
									</c:if>
									<c:if test="${isUpdateOutApply }">
										<td><%=list.get(i).get(oldNumIndex)%></td>
									</c:if>
									<td><%=list.get(i).get(ownedUnitIndex)%></td>
									<td><%=list.get(i).get(statusIndex) %></td>
									<td><span name="user" style="display: none;"
										class="info_user"><%=request.getAttribute("username")%></span></td>
									<td><input type="button" value="查看详情" class="search-btn"
										onclick="detail_Apply(this);" /></td>
								</tr>
								<%	}
					    	}
					    } %>
							</tbody>
						</c:if>
					</table>
				</div>
			</div>
		</div>
		<!-- 内容区结束 -->

		<!-- 分页的盒子开始 -->
		<!--<div class="page-box">
			
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
		    	<label></label>
		    	<span>页</span>
			</div>
			-->
		<!-- 分页的盒子结束 -->
	</div>
	<%@include file="../../../../ConstantHTML/zhjfoot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
</html>
