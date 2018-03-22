<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + 
request.getServerPort()
+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
	<base href="<%=basePath%>">
	<title>出库管理</title>
	<link rel="stylesheet" href="css/transact_business/jquery.combobox.css">
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="ConstantHTML/css/global.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >
	<link rel="stylesheet" href="css/paging.css">

	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	
	<script type="text/javascript">
			/* $(function() {
				var sugestions = new Array();
    			sugestions = "${pmnm}".replace('[','').replace(']','').split(',');
    			$('.pmnm').combobox(sugestions);
    		}); */
</script>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span>
			<a href="OutWarehouseServlet?operate=addOutList">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="OutWarehouseServlet?operate=addOutList">出库管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			填写发料单
		</div>
		<div id="content">
			<div id="top_content">
				<!--选择代储企业 -->
				<div id="bottom_content">
					<div id="keeper-box">
						<div id="keepers" class="select-keeper-box">
							<span style="color: red;">请选择代储企业：</span>
							<select name="ownedUnit" id="ownedUnit">
								<option value="all">请选择</option>
								<c:forEach var="company" items="${companys }">
								<option value="${company }">${company }</option>
							</c:forEach>
						</select>
						<!-- <input type="submit" value="查询"   >  -->
					</div>
					<div>
						<table class="fare-table" id="fare-table">
							<thead>
								<tr>
									<th>
										<!-- <input type="checkbox" onclick="chooseAll();" id="checkbox_product"> -->
									</th>
									<th>序号</th>
									<th>产品名称</th>
									<th>品名内码</th>
									<th>产品型号</th>
									<th>产品单元</th>
									<th>机号</th>
									<th>单价</th>
									<th>计量单位</th>
									<th>数量</th>
									<th>存储期限</th>
									<th>承制单位</th>
									<th>代储单位</th>
									<th>备注</th>
								</tr>
							</thead>
							<tbody>
                           <%-- 
							<%
								 List<HashMap<String,Object>> queryResult = (List<HashMap<String,Object>>)request.getAttribute("queryResult");
								 int i=0;
								 for(i=0;i<queryResult.size();i++) {
							%>
								<tr class="contract-add-table-body">
								<td><input class="setTem" type="checkbox" name="checkbox_product"><input type="hidden" value=“<%=queryResult.get(i).get("productId")%>”></td>
								<td><%=i+1 %></td>
								<td><%=queryResult.get(i).get("productModel")%></td>
								<td><%=queryResult.get(i).get("productUnit")%></td>
								<td><%=queryResult.get(i).get("batch")%></td>
								<td><%=queryResult.get(i).get("deviceNo")%></td>
								<td><%=queryResult.get(i).get("productPrice")%></td>
								<td>1</td>
								<td><%=MyDateFormat.changeDateToString((java.util.Date)queryResult.get(i).get("execDate"))%></td>
								<td><%=queryResult.get(i).get("restKeepTime")%></td>
								<td><%=queryResult.get(i).get("restMaintainTime")%></td>
								<td><%=queryResult.get(i).get("Means")%></td>
								<td><%=queryResult.get(i).get("storageTime")%></td>
								<td><%=queryResult.get(i).get("nextMaintainTime")%></td>
								<td><%=queryResult.get(i).get("manufacturer")%></td>
								<td><%=queryResult.get(i).get("keeper")%></td>
								<td><%=queryResult.get(i).get("remark")%></td>
								</tr>
								<%} %>  --%>
							</tbody>
						</table>
					</div>
                    <div  class="paging-wrape">

						<ul class="pagination">
							<li>
								<a href="javascript:;" aria-label="Previous">
									&laquo;
								</a>
							</li>
							<li><a class="active" href="javascript:;">1</a></li>

							<li>
								<a href="javascript:;" aria-label="Next">
									&raquo;
								</a>
							</li>

						</ul>

						<div class="total">共<span></span>页</div>

						<div class="go-page">
							<span class="text">到第</span>
							<input class="page_Input" type="text" value="">
							<span class="text">页</span>
							<a class="sure-btn" href="javascript:;" role="button" tabindex="0">确定</a>
						</div>

					</div>

				</div>


				<!-- 新增发料单表单 -->
				<div id="xinzengfaliaodan" class="outlist-box">
					<table class="add-outlist-table" id="list-header">
						<tr>
							<td></td>
							<th colspan="4">
								<span>通信装备代储维修器材出库发料单</span>
							</th>
							<td></td>
							<td rowspan="6"><img alt="" src="img/transact_business/addlist.JPG"></td>
						</tr>
						<tr>
							<td>军代表室(章)：</td><td colspan="2"></td>
							<td>发料单位(章)：</td><td colspan="2"></td>
						</tr>
						<tr>
							<td>案由文号：</td>
							<td colspan="2"><input id="fileNo"/></td>
							<td>发料单号：</td>
							<td><input id="listId"/></td>
						</tr>
						<tr>
							<td>运输方式：</td>
							<td colspan="2"><input id="diliverMean"/></td>
							<td>运单编号：</td>
							<td><input id="deliverNo"/></td>
						</tr>
						<tr>
							<td>收料单位(章)：</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<!-- 	<td>共<input id="totalNum"/>页 第<input id="curNum"/>页</td> -->
						</tr>
					</table>
					<table class="outlist-content-table" id="list-content">
						<tr>
							<th>序号</th>
							<th>品名代码</th>
							<th>名称型号</th>
							<th>单位</th>
							<th>品级</th>
							<th>通知数</th>
							<th>实发数</th>
							<th>件数</th>
							<th>单价(元)</th>
							<th>金额(元)</th>
							<th>备注</th>
						</tr>
						<tr>
							<td>1</td>
							<td><input type="hidden" value=""/><input type="text" disabled="true"/></td>
							<td><input type="text" disabled="true"/></td>
							<td><input type="text" disabled="true"/></td>
							<td><input type="text"/></td>
							<td><input type="text"/></td>
							<td><input type="hidden" value=""/><input type="text" onblur="validNum(this);"/></td>
							<td><input type="text"/></td>
							<td><input type="text" disabled="true"/></td>
							<td><input type="text" disabled="true"/></td>
							<td><input type="hidden" value=""/><input type="text"/></td>
							<!-- <td style="border: 0px;"><span><img src="img/ok.jpg"></span></td> -->
						</tr>
					</table>
					<div id="operation">
						<!-- <input id="addRow" type="button" class="search-btn" value="新增一行" onclick="addNewRow();">
						<input id="removeRow" type="button" class="search-btn" value="删除一行" onclick="removeRow();"> -->
						<input type="button" class="search-btn" value="导出表单" onclick="makeOutList(1);">
						<input type="hidden" id="pmnm-value" value="">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="../../../ConstantHTML/zhjfoot.html"%>

<script src="js/transact_business/jquery.js"></script>
<script src="js/transact_business/jquery.bgiframe.pack.js"></script>
<script src="js/transact_business/jquery.combobox.outlist.js"></script>
<script src="js/transact_business/transact.js"></script>
<script src="js/transact_business/jds-paging.js"></script>
<script src="ConstantHTML/js/homepage.js"></script>
</body>
</html>


