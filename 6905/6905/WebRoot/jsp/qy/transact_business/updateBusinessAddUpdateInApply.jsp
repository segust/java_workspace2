<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<title>更新入库申请管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" href="css/transact_business/jquery.combobox.css">
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/transact_business/transact.css">
<link rel="stylesheet" href="css/paging.css">
<script>
	$(function() {
		var sugestions = new Array();
		sugestions = "${pname}".replace('[','').replace(']','').split(',');
		$('.pname').combobox(sugestions);
		sugestions = "${pmnm}".replace('[','').replace(']','').split(',');
		$('.pmnm').combobox(sugestions);
	});
	</script>
</head>


<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span> <a
				href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="BorrowServlet?operate=updateInOut">更新管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			新增更新入库申请
		</div>
		<div id="content">
			<div class="toptitle-op">
		        <ul class="in-apply clearfix">
                    <li>
                    	<label>操作日期</label><input class="sang_Calender" type="text">
                        <script src="js/transact_business/dateTime.js"></script>
                    </li>
                    <li><label>产品型号</label><input type="text"></li>
                    <li><a class="btn submit-btn">查询</a></li>
                </ul>
	        </div>

			<div id="top_content">
				<table class="fare-table" id="fare-table">
					<thead>
						<tr>
							<th></th>
							<th>序号</th>
							<th>批次</th>
							<th>产品名称</th>
							<th>产品型号</th>
							<th>产品单元</th>
							<th>机号</th>
							<th>单价</th>
							<th>数量</th>
							<th>操作类型</th>
							<th>操作日期</th>
							<th>品名内码</th>
							<th>计量单位</th>
							<th>存储期限</th>
							<th>摆放位置</th>
							<th>维护周期</th>
							<th>承制单位</th>
							<th>代储单位</th>
							<th>备注</th>
						</tr>
					</thead>

					<tbody>
						<% 
						List<Map<String,String>> resultList = (List<Map<String,String>>)request.getAttribute("products");
						int i;
						for(i=0;i<resultList.size();i++) {
						%>
						<tr>
							<td><input type="radio" class="setTem" name="selectPro"
								onchange="radioSelect(this);" /><input type="hidden"
								value="<%=resultList.get(i).get("productId") %>" />
							</td>
							<td><%=i+1 %></td>
							<td><%=resultList.get(i).get("batch") %></td>
							<td><%=resultList.get(i).get("productName") %></td>
							<td><%=resultList.get(i).get("productModel") %></td>
							<td><%=resultList.get(i).get("productUnit") %></td>
							<td><%=resultList.get(i).get("deviceNo") %></td>
							<td><%=resultList.get(i).get("productPrice") %></td>
							<td>1</td>
							<td>更新出库</td>
							<td><%=resultList.get(i).get("insertTime") %></td>
							<td><%=resultList.get(i).get("PMNM") %></td>
							<td><%=resultList.get(i).get("measureUnit") %></td>
							<td><%=resultList.get(i).get("storageTime") %></td>
							<td><%=resultList.get(i).get("location") %></td>
							<td><%=resultList.get(i).get("maintainCycle") %></td>
							<td><%=resultList.get(i).get("manufacturer") %></td>
							<td><%=resultList.get(i).get("keeper") %></td>
							<td><%=resultList.get(i).get("remark") %></td>
						</tr>
						<%} %>
					</tbody>
				</table>
			</div>
			<!-- 分页开始 -->
			<div class="paging-wrape">

				<ul class="pagination">
					<li><a href="javascript:;" aria-label="Previous"> &laquo;
					</a></li>
					<li><a class="active" href="javascript:;">1</a>
					</li>

					<li><a href="javascript:;" aria-label="Next"> &raquo; </a></li>

				</ul>

				<div class="total">
					共<span>100</span>页
				</div>

				<div class="go-page">
					<span class="text">到第</span> <input class="page_Input" type="text"
						value=""> <span class="text">页</span> <a class="sure-btn"
						href="javascript:;" role="button" tabindex="0">确定</a>
				</div>

			</div>
			<!-- 分页结束 -->
		</div>

		<!--产品查询 -->
		<div id="bottom_content">
			<div id="lunhuanchurukuguanli" class="addproduct_box">
				<div id="xinzenglunhuanrukutable" class="addproduct_table">
					<form id="addApplyform">
						<table class="contract-add-table">
							<tbody>
								<tr>
									<td>入库方式：</td>
									<td><label name="inMeans" id="inMeans">更新入库</label></td>
									<td>入库批次：</td>
									<td><input name="Batch" id="batch" /></td>
								</tr>
								<tr>
									<td>机号：</td>
									<td><input name="DeviceNo" id="deviceNo" value=""
										class="dNo" onblur="checkdNo(this);"/></td>
									<td>品名内码：</td>
									<td><input name="PMNM" id="pmnm" class="pmnm" /></td>
								</tr>
								<tr>
									<td>整机名称：</td>
									<td><input name="WholeName" id="wholename" class="pname" />
									</td>
									<td>单元名称：</td>
									<td><input name="UnitName" id="productunit" class="unit">
									</td>

								</tr>
								<tr>
									<td>数量：</td>
									<td><label id="num">1</label></td>
									<td>计量单位：</td>
									<td><input name="measure" id="measure" value="" disabled="true"/></td>
								</tr>
								<tr>
									<td>承制单位：</td>
									<td><input name="manufacturer" id="manufacturer" value="" />
									</td>
									<td>代储单位：</td>
									<td><input name="keeper" value="" id="keeper" /></td>
								</tr>
								<tr>
									<td>单价：</td>
									<td><input name="Price" value="" id="price" disabled="true"/></td>
									<td>摆放位置：</td>
									<td><input name="location" id="location" /></td>
								</tr>
								<tr>
									<td>生产时间：</td>
									<td id="calender-wrap"><input class="sang_Calender"
										id="signdate" name="producedDate" value="" /> <script
											type="text/javascript" src="js/transact_business/dateTime.js"></script>
									</td>
									<td>企业维护周期</td>
									<td>
												<select name="maintainCycle" id="maintain">
													<option value="一个月" selected="selected">一个月</option>
													<option value="三个月">三个月</option>
													<option value="六个月">六个月</option>
													<option value="一年">一年</option>
												</select>
											</td>
								</tr>
								<tr>
									<td>备注：</td>
									<td><input name="remark" id="remark" /></td>
									<td>操作时间：</td>
									<td><label name="operateTime" id="operateTime"><%=MyDateFormat.changeDateToLongString(new Date())%></label>
									</td>
								</tr>

							</tbody>
						</table>
					</form>
					<div style="float:right; margin-top: 10px;">
						<input class="search-btn" type="button" value="添加"
							onclick="addApply('<%=basePath %>');" id="addApply" style="display: none;"> <input class="search-btn"
							type="reset" value="重置"  onclick="clearForm('addApplyform')"/>
					</div>
				</div>
			</div>
		</div>
		<div id="bottom_right">
			<div id="addprolist" class="addproduct_list" style="width: 27%">
				<p id="start-msg">您还没有添加数据！</p>
				<br />
			</div>
			<div style="float: right;margin-right: 6%">
				<span id="submit" style="margin-top: 5px; display: none;"><input
					type="button" class="search-btn" value="批量提交" style="width: 70px;"
					onclick="upObjs('<%=basePath %>');">
				</span>
			</div>
			<!-- 测试浮动显示框 -->
			<div id="view-table">
				<table class="contract-add-table">
					<tbody>
						<tr>
							<td>入库方式:</td>
							<td id="mean-view"></td>
							<td>入库批次:</td>
							<td id="batch-view"></td>
						</tr>
						<tr>
							<td>品名内码:</td>
							<td id="pmnm-view"></td>
							<td>整机名称:</td>
							<td id="name-view"></td>
						</tr>
						<tr>
							<td>单元名称:</td>
							<td id="unit-view"></td>
							<td>机号:</td>
							<td id="device-view"></td>
						</tr>
						<tr>
							<td>数量:</td>
							<td>1</td>
							<td>计量单位:</td>
							<td id="measure-view"></td>
						</tr>
						<tr>
							<td>承制单位:</td>
							<td id="manu-view"></td>
							<td>代储单位:</td>
							<td id="keeper-view"></td>
						</tr>
						<tr>
							<td>单价:</td>
							<td id="price-view"></td>
							<td>摆放位置:</td>
							<td id="location-view"></td>
						</tr>
						<tr>
							<td>生产时间:</td>
							<td id="pdate-view"></td>
							<td>维护周期:</td>
							<td id="maintain-view"></td>
						</tr>
						<tr>
							<td>备注:</td>
							<td id="remark-view"></td>
							<td>操作时间:</td>
							<td id="operate-view"></td>
						</tr>
					</tbody>
				</table>
				<div style="float: right;margin-top: 10px;">
					<input class="search-btn" type="button" onclick="hide();"
						value="确定" />
				</div>
			</div>
			<!-- 覆盖的第二层 -->
			<div id="cover-table"></div>
			<input type="hidden" id="pmnm-value" value="${pmnm }" /> <input
				type="hidden" id="pname-value" value="${pname }">
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="js/transact_business/addUpdateInApply.js"></script>
	<script src="js/transact_business/jquery.js"></script>
	<script src="js/transact_business/jquery.bgiframe.pack.js"></script>
	<script src="js/transact_business/jquery.combobox.borrow.js"></script>
	<script src="js/transact_business/go-to-paging.js"></script>
</body>
</html>
