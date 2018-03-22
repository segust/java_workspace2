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

	<title>新增轮换出库申请</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" href="css/transact_business/jquery.combobox.css" >
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >
	<!-- <script type="text/javascript">
	$(function() {
		var sugestions = new Array();
		var names = new Array();
		sugestions = "${pmnm}".replace('[','').replace(']','').split(',');
		names = "${pname}".replace('[','').replace(']','').split(',');
		$('.pmnm').combobox(sugestions);
		$('.pname').combobox(names);
	});
	</script> -->
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span>
			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="BorrowServlet?operate=borrowCheck">轮换管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			新增轮换出库申请
		</div>
		<div id="content">
		    <div id="top_content">
		         <table class="fare-table" id="fare-table">
							<thead>
								<tr>
								<th></th>
								<th>序号</th>
								<th>产品型号</th>
								<th>产品单元</th>
								<th>批次</th>
								<th>机号</th>
								<th>单价</th>
								<th>数量</th>
								<th>操作日期</th>
								<th>剩余存放天数</th>
								<th>企业剩于维护天数</th>
								<th>操作类型</th>
								<th>存储期限</th>
								<th>企业下次维护日期</th>
								<th>承制单位</th>
								<th>代储单位</th>
								<th>备注</th>
								</tr>
							</thead>
							 
							<tbody>
								
							</tbody>
						</table>
		    </div>

		    <!-- 分页 -->
            <div class="paging-wrape" id="paging-wrape">
			    <ul class="pagination">
			    	<!-- <li>
			    		<a href="#" aria-label="Previous">
			    			<span aria-hidden="true">&laquo;</span>
			    		</a>
			    	</li> -->
			    	<!-- <li><a href="javascript:;">1</a></li> -->
			    	<!-- <li>
			    		<a href="#" aria-label="Next">
			    			<span aria-hidden="true">&raquo;</span>
			    		</a>
			    	</li> -->
			    </ul>
                <span class="total-page" id="total-page"></span>
            </div>
			<!--产品查询 -->
			<div id="bottom_content">
				<div id="lunhuanchurukuguanli" class="addproduct_box">
					<div id="xinzenglunhuanrukutable" class="addproduct_table" style="margin-left: 20%">
						<form>
							<table class="contract-add-table">
								<tbody>
									<tr>
										<td>
											出库方式：
										</td>
										<td>
											<label name="outMeans" id="outMeans">轮换出库</label>
										</td>
									</td>
									<td>
										出库批次：
									</td>
									<td>
										<input name="Batch" id="batch"/>
									</td>
								</tr>
								<tr>
									<td>
										数量：
									</td>
									<td>
										<input name="num" id="oldnum" disabled="true"/>
									</td>
									<td>
										操作时间：
									</td>
									<td>
										<label name="operateTime" id="operateTime"><%=MyDateFormat.changeDateToLongString(new Date())%></label>
									</td>
								</tr>
								<tr>
									<td>
										出库时间(月)：
									</td>
									<td>
										<input name="borrowLength" id="borrowLength"/>
									</td>
									<td>
										出库原因：
									</td>
									<td>
										<input name="borrowReason" id="borrowReason"/>
									</td>
								</tr>
								<tr>
									<td>
										备注：
									</td>
									<td>
										<input name="remark" id="remark"/>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
					<div style="float:right; margin-top: 10px;">
						<input class="search-btn" type="button" value="添加" onclick="addApply('<%=basePath%>');">
						<input class="search-btn" type="reset" value="重置"/>
					</div>
				</div>
			</div>
			<input type="hidden" id="oldprice"/>
		<!-- 	<div id="addprolist" class="addproduct_list" style="width: 28%">
				<span id="start-msg">您还没有添加数据！</span>
				<br/>
			</div> -->
		<!-- 	<div style="float: right;margin-right: 6%"><span id="submit" style="margin-top: 5px; display: none;"><input type="button" class="search-btn" value="批量提交" style="width: 70px;" onclick="upObjs();"></span></div> -->
			<!-- 测试浮动显示框 -->
			<!-- <div id="view-table" style="position:absolute;z-index:100;padding: 1px;border-width: 1px 1px 1px medium;border-style:solid;border-color:#E1E1E1;margin-left: 7%;min-width: 40%;display: none;">
				<table class="contract-add-table">
					<tbody>
						<tr>
							<td style="border: 0px;">出库方式:</td><td style="border: 0px;" id="mean-view"></td><td style="border: 0px;">出库批次:</td><td style="border: 0px;" id="batch-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">整机名称:</td><td style="border: 0px;" id="name-view"></td><td style="border: 0px;">单元名称:</td><td style="border: 0px;" id="unit-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">机号:</td><td style="border: 0px;" id="device-view"></td><td style="border: 0px;">品名内码:</td><td style="border: 0px;" id="pmnm-view"></td>
						</tr>		
						<tr>
							<td style="border: 0px;">数量:</td><td style="border: 0px;">1</td><td style="border: 0px;">计量单位:</td><td style="border: 0px;" id="measure-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">承制单位:</td><td style="border: 0px;" id="manu-view"></td><td style="border: 0px;">代储单位:</td><td style="border: 0px;" id="keeper-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">单价:</td><td style="border: 0px;" id="price-view"></td><td style="border: 0px;">摆放位置:</td><td style="border: 0px;" id="location-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">生产时间:</td><td style="border: 0px;" id="pdate-view"></td><td style="border: 0px;">维护周期:</td><td style="border: 0px;" id="maintain-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">出库时间:</td><td style="border: 0px;" id="outtime-view"></td><td style="border: 0px;">出库原因:</td><td style="border: 0px;" id="reason-view"></td>
						</tr>
						<tr>
							<td style="border: 0px;">备注:</td><td style="border: 0px;" id="remark-view"></td><td style="border: 0px;">操作时间:</td><td style="border: 0px;" id="operate-view"></td>
						</tr>
					</tbody>
				</table>
				<div style="float: right;margin-top: 10px;"><input class="search-btn" type="button" onclick="hide();" value="确定"/></div>
			</div>
			覆盖的第二层
			<div id="cover-table" style="position:absolute;z-index:99;margin-left:3%;width:50%;height:70%;background-color: white;display: none;"></div> 
		</div>
	<div id="submit"> <input class="search-btn" type="button" value="提交"/></div> -->
	</div>
		</div>	
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script src="js/transact_business/addBorrowOutApply.js"></script>
	<script type="text/javascript" src="js/transact_business/jquery.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script type="text/javascript" src="js/transact_business/jquery.bgiframe.pack.js"></script>
	<script type="text/javascript" src="js/transact_business/jquery.combobox.borrow.js"></script>
	<script src="js/transact_business/loadTable.js"></script>
</body>
</html>
