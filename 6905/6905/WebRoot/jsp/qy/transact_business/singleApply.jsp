<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.edu.cqupt.beans.Product"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>批量新入库申请</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >
	<link rel="stylesheet" href="css/transact_business/jquery.combobox.css" >
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<link rel="stylesheet" href="css/paging.css">
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<!-- <script type="text/javascript">
	$(function() {
		var sugestions = new Array();
		sugestions = "${dNos}".replace('[','').replace(']','').split(',');
		$('.deviceNo').combobox(sugestions);
	});
	</script> -->
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<jsp:useBean id="product" class="cn.edu.cqupt.beans.Product" scope="request"/>
	<%@include file="../../../ConstantHTML/top.html" %>
	<%@include file="../../../ConstantHTML/left.html" %>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span>
			业务办理&nbsp;&nbsp;>&nbsp;&nbsp;
			查询合同&nbsp;&nbsp;>&nbsp;&nbsp;
			申请入库
		</div>
		<!-- 内容区 -->
		<div id="content">
			<div id="topcontent">
				<div id="rukuguanli" style="margin-top: 10px;">
					<div id="contract-table-box">
						<table id="contract-table" class="contract-table">
							<thead>
								<tr>
									<th rowspan="2"></th>
									<th rowspan="2">合同编号</th>
									<th rowspan="2">产品型号</th>
									<th rowspan="2">机号</th>
									<th colspan="2">产品名称</th>
									<th rowspan="2">品名内码</th>
									<th rowspan="2">单价</th>
									<th rowspan="2">单位</th>
									<th rowspan="2">数量</th>
									<th rowspan="2">交付时限</th>
									<th rowspan="2">承制单位</th>
									<th rowspan="2">代储单位</th>
									<th rowspan="2">产品状态</th>
									<th rowspan="2">操作</th>
								</tr>
    							<tr><th>整机名称</th><th>单元名称</th></tr>
							</thead>
							<tbody>
								<% 
								List<HashMap<String, String>> products = (List<HashMap<String, String>>)request.getAttribute("products"); 
								%>

								<%
								for(int i = 0;i<products.size(); i++) {
								%>
								<tr>
									<td><input type="radio" name="applyinproduct" onclick="radioSelect(this);"/>
										<input type="hidden" value="<%=products.get(i).get("productId")%>"/></td>
										<% String contractId = products.get(i).get("contractid"); %>
										<td><%=contractId%></td>
										<% String model = products.get(i).get("productModel"); %>
										<td><%=model%></td>
										<% String deviceNo = products.get(i).get("deviceNo"); %>
										<td><%=deviceNo%></td>
										<% 
    										String name = products.get(i).get("wholeName");
    				    				%>
    									<td><%=name%></td>
    									<% 
    										String unitName = products.get(i).get("productUnit");
    				   					 %>
    									<td><%=unitName%></td>
										<td><%=products.get(i).get("PMNM")%></td>
										<td><%=products.get(i).get("productPrice") %></td>
										<td><%=products.get(i).get("measureUnit")%></td>
										<% double sum = Double.parseDouble(products.get(i).get("productPrice"))*Double.parseDouble(products.get(i).get("count")); %>
										<td><%=products.get(i).get("count") %></td>
										<td><%=products.get(i).get("deliveryTime") %></td>
										<td><%=products.get(i).get("manufacturer") %></td>
										<td><%=products.get(i).get("keeper") %></td>
										<td><%=products.get(i).get("proStatus") %></td>
										<td><input type="button" value="修改" onclick="editProduct(this);"/><input type="button" value="删除" onclick="deleteProduct(this);"/></td>
										<%-- <td>
										<span>
											<%if(counts.get(i) >1) {%>
											<input type="button" value="批量入库" onclick="window.location.href='ProductHandleServlet?operate=batchInApply&contractId=<%=contractId%>&model=<%=model%>&unit=<%=unit%>&price=<%=products.get(i).getPrice() %>&measure=<%=products.get(i).getMeasureUnit()%>&manufacture=<%=products.get(i).getManufacturer()%>&keeper=<%=products.get(i).getKeeper()%>'">
											<%} else {%>
											<input type="button" value="单个入库" onclick="window.location.href='ProductHandleServlet?operate=singleInApply&productId=<%=id%>'">
											<%} %>
										</span>
									</td> --%>
									<%} %>
								</tbody>
							</table>
						</div>
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
						<% int sum = (Integer)request.getAttribute("sum"); %>
						<div class="total">共<span><%=sum%10 == 0? sum/10:sum/10+1 %></span>页</div>

						<div class="go-page">
							<span class="text">到第</span>
							<input class="page_Input" type="text" value="">
							<span class="text">页</span>
							<a class="sure-btn" href="javascript:;" role="button" tabindex="0">确定</a>
						</div>
					</div>
				</div>
				<div id="bottom_content">
					<div id="xinzenghetong"  class="addproduct_box">
						<div id="xinzenghetongtable"  class="addproduct_table">
							<form action="<%=path %>/AddInApplyServlet?operate=singleInApply" method="post" id="singleForm">
								<table class="contract-add-table" >
									<tbody>
										<tr>
											<td>合同编号：</td>
											<td id="contractid">${contractId}</td>
											<td>入库方式：</td>
											<td>
												<label id="inMeans">新入库</label>
											</td>
										</tr>
										<tr>
											<td>入库批次：</td>
											<td>
												<input type="text" name="batch" id="batch"/>
											</td>
											<td>机号：</td>
											<td>
												<input type="text" name="deviceNo" id="deviceNo" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>产品名称：</td>
											<td >
												<input id="productname" disabled="true"/>
											</td>
											<td >产品型号：</td>
											<td >
												<input id="productmodel" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>单元名称：</td>
											<td>
												<input id="productunit" disabled="true"/>
											</td>
											<td>品名内码</td>
											<td>
												<input name="PMNM" id="pmnm" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>整机名称：</td>
											<td>
												<input name="wholeName" id="wholename" disabled="true"/>
											</td>
											<td>单价：</td>
											<td>
												<input type="text" name="productPrice" value="" id="price" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>数量：</td>
											<td><label>1</label></td>
											<td>计量单位：</td>
											<td>
												<input type="text" name="measure" value="" id="measure" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>承制单位：</td>
											<td>
												<input name="manufacturer" value="" id="manufacturer" disabled="true"/>
											</td>
											<td>代储单位：</td>
											<td>
												<input name="keeper" value="" id="keeper" disabled="true"/>
											</td>
										</tr>
										<tr>
											<td>摆放位置</td>
											<td>
												<input name="location" id="location"/>
											</td>
											<td>维护周期</td>
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
											<td>生产时间</td>
											<td>
												<input name="producedDate" class="sang_Calender" id="signdate"/>
												<script type="text/javascript" src="ConstantHTML/js/spdateTime.js"></script>
											</td>
											<td>产品编号</td>
											<td>
												<input name="productCode" id="productcode"/>
											</td>
										</tr>
										<tr>
											<td>存储期限(年)</td>
											<td>
												<input name="savePeriod" id="storagetime"/>
											</td>
											<td>备注</td>
											<td>
												<input name="remark" id="remark"/>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
							<div style="float:right; margin-top: 10px;">
								<input class="search-btn" type="button" value="添加" onclick="addApply();" id="addApply" style="display: none;">
								<input class="search-btn" type="reset" value="重置" onclick="clearForm('singleForm')"/>
							</div>
						</div> 
					</div>
					<div id="addprolist" class="addproduct_list" style="width: 27%">
						<p id="start-msg">您还没有添加数据！</p>
						<br/>
					</div>
					<div style="float: right;margin-right: 6%"><span id="submit" style="margin-top: 5px; display: none;"><input type="button" class="search-btn" value="批量提交" style="width: 70px;" onclick="upObjs();"></span></div>
					<!-- 测试浮动显示框 -->
					<div id="view-table">
						<table class="contract-add-table">
							<tbody>
								<tr>
									<td>合同编号:</td>
									<td id="contract-view"></td>
									<td >入库方式:</td>
									<td  id="mean-view"></td>
								</tr>
								<tr>
									<td >入库批次:</td>
									<td  id="batch-view"></td>
									<td >品名内码:</td>
									<td  id="pmnm-view"></td>
								</tr>
								<tr>
									<td >产品名称:</td>
									<td  id="pname-view"></td>
									<td >产品型号:</td>
									<td  id="model-view"></td>
								</tr>		
								<tr>
									<td >单元名称:</td>
									<td id="unit-view"></td>
									<td >机号:</td>
									<td  id="device-view"></td>
								</tr>
								<tr>
									<td >整机名称:</td>
									<td  id="name-view"></td>
									<td >单价:</td>
									<td  id="price-view"></td>
								</tr>
								<tr>
									<td >数量:</td>
									<td  id="num">1</td>
									<td >计量单位:</td>
									<td  id="measure-view"></td>
								</tr>
								<tr>
									<td >承制单位:</td>
									<td  id="manu-view"></td>
									<td >代储单位:</td>
									<td  id="keeper-view"></td>
								</tr>
								<tr>
									<td >摆放位置:</td>
									<td  id="location-view"></td>
									<td >维护周期:</td>
									<td  id="maintain-view"></td>
								</tr>
								<tr>
									<td >生产日期:</td>
									<td  id="pdate-view"></td>
									<td >产品编号:</td>
									<td  id="code-view"></td>
								</tr>
								<tr>
									<td >存储期限:</td>
									<td  id="storage-view"></td>
									<td >备注:</td>
									<td  id="remark-view"></td>
								</tr>
							</tbody>
						</table>
						<div style="float: right;margin-top: 10px;"><input class="search-btn" type="button" onclick="hide();" value="确定"/></div>
					</div>
					<!-- 覆盖的第二层 -->
					<div id="cover-table"></div> 
				</div>
			</div>
		</div>
		<%@include file="../../../ConstantHTML/foot.html" %>
	</body>


	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script src="js/transact_business/jquery.js"></script> 
	<script src="js/transact_business/jquery.bgiframe.pack.js"></script>
	<script src="js/transact_business/jquery.combobox.newin.js"></script>
	<script src="js/transact_business/paging.js"></script>
	<script src="js/transact_business/addSingleInApply.js"></script>
	</html>
