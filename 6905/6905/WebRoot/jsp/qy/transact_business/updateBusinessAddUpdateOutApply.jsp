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

	<title>更新出库申请管理</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/transact_business/jquery.combobox.css">
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transact.css" />
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>

</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<div></div>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span>
			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="BorrowServlet?operate=updateInOut">更新管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			新增更新出库申请
		</div>

		<div id="content">
		  <div id="top_content">
				     <table class="fare-table" id="fare-table">
							<thead>
								<tr>
								<th>
									<!-- <input type="checkbox" onclick="chooseAll();" id="checkbox_product"> -->
								</th>
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
								<th>操作</th>
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

         <div id="bottom_content">
         <div id="lunhuanchurukuguanli" class="addproduct_box">
					<div id="xinzenglunhuanrukutable" class="addproduct_table" style="margin-left: 20%">
            <table class="contract-add-table">
								<tbody>
									<tr>
										<td>
											出库方式：
										</td>
										<td>
											<label name="outMeans" id="outMeans">更新出库</label>
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
											原数量：
										</td>
										<td>
											<input name="oldnum" id="oldnum" disabled="true"/>
										</td>
										<td>
											数量：
										</td>
										<td>
											<input name="num" id="num"/>
										</td>
									</tr>
									<tr>
									<td>
											原价格：
										</td>
										<td>
											<input name="oldprice" id="oldprice" disabled="true"/>
										</td>
										<td>
											价格：
										</td>
										<td>
											<input name="price" id="price"/>
										</td>
									</tr>
									<!-- <tr>
										<td>
											出库时间(月)：
										</td>
										<td>
											<input name="borrowLength" id="borrowLength"/>
										</td>
									</tr> -->
									<tr>
										<td>
											备注：
										</td>
										<td>
											<input name="remark" id="remark"/>
										</td>
											<td>
											操作时间：
										</td>
										<td>
											<label name="operateTime" id="operateTime"><%=MyDateFormat.changeDateToLongString(new Date())%></label>
										</td>
									</tr>
								</tbody>
							</table>
							<div style="float:right; margin-top: 10px;">
						<input class="search-btn" type="button" value="添加" onclick="addApply('<%=basePath%>');">
						<input class="search-btn" type="reset" value="重置"/>
					</div>
					</div>
					</div>
         </div>
			</div>
		</div>
		<%@include file="../../../ConstantHTML/foot.html"%>
		<script type="text/javascript" src="js/transact_business/transact.js"></script>
		<script type="text/javascript" src="js/transact_business/addUpdateOutApply.js"></script>
		<script type="text/javascript" src="js/transact_business/jquery.js"></script>
		<script type="text/javascript" src="js/transact_business/jquery.bgiframe.pack.js"></script>
		<script type="text/javascript" src="js/transact_business/jquery.combobox.update.js"></script>
		<script src="js/transact_business/loadTable.js"></script>
	</body>
	</html>
