<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="cn.edu.cqupt.beans.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>新增产品</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" type="text/css"
	href="css/transact_business/transact.css" />
<link rel="stylesheet" type="text/css"
	href="css/transact_business/jquery.combobox.css" />
<link rel="stylesheet" type="text/css"
	href="css/qualification_management/qualifymanagement.css">
<link rel="stylesheet" href="css/paging.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
</head>

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<script type="text/javascript">
		$(function() {
			var sugestions = new Array();
			sugestions = "${uname}".replace('[', '').replace(']', '').split(',');
			var namesugest = new Array();
			namesugest = "${pname}".replace('[', '').replace(']', '')
					.split(',');
			var pmnmsugest = new Array();
			pmnmsugest = "${pmnm}".replace('[', '').replace(']', '')
					.split(',');
			$('.pmnm').combobox(pmnmsugest);
			$('.pname').combobox(namesugest);
			$('.unit').combobox(sugestions);
		});
	</script>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a
				href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="ContractHandleServlet?operate=querycontract">查询合同</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			新增产品
		</div>
		<!-- 内容区 -->
		<div id="content">
			<div id="topcontent">
				<div id="rukuguanli" style="margin-top: 10px;">
					<div id="contract-table-box">
				<table id="contract-table" class="contract-table">
							<thead>
								<tr>
									<th rowspan="2">合同编号</th>
									<th rowspan="2">产品型号</th>
									<th colspan="2">产品名称</th>
									<th rowspan="2">单价</th>
									<th rowspan="2">单位</th>
									<th rowspan="2">数量</th>
									<th rowspan="2">金额</th>
									<th rowspan="2">交付时限</th>
									<th rowspan="2">承制单位</th>
									<th rowspan="2">代储单位</th>
									<th rowspan="2">产品状态</th>
								</tr>
								<tr><th>整机名称</th><th>单元名称</th></tr>
							</thead>
							<tbody>
							<%
								List<HashMap<String, String>> products = (List<HashMap<String, String>>) request
										.getAttribute("products");
							%>
							
								<%
									for (int i = 0; i < products.size(); i++) {
								%>
								<tr>
									<%
										String contractId = products.get(i).get("contractid");
									%>
									<td><%=contractId%>
									</td>
									<%
										String model = products.get(i).get("productModel");
									%>
									<td><%=model%>
									</td>
									<%
										String wholeName = products.get(i).get("wholeName");
									%>
									<td><%=wholeName%></td>
									<%
										String unitName = products.get(i).get("productUnit");
									%>
									<td><%=unitName%></td>
									<td><%=products.get(i).get("productPrice")%>
									</td>
									<td><%=products.get(i).get("measureUnit")%>
									</td>
									<%
										double sum = Double.parseDouble(products.get(i).get(
													"productPrice"))
													* Double.parseDouble(products.get(i).get("count"));
									%>
									<td><%=products.get(i).get("count")%>
									</td>
									<td><%=sum%>
									</td>
									<td><%=products.get(i).get("deliveryTime")%>
									</td>
									<td><%=products.get(i).get("manufacturer")%>
									</td>
									<td><%=products.get(i).get("keeper")%>
									</td>
									<td><%=products.get(i).get("proStatus")%>
									</td>
									<%-- <td>
    						<span>
    						<%if(counts.get(i) >1) {%>
    							<input type="button" value="批量入库" onclick="window.location.href='ProductHandleServlet?operate=batchInApply&contractId=<%=contractId%>&model=<%=model%>&unit=<%=unit%>&price=<%=products.get(i).getPrice() %>&measure=<%=products.get(i).getMeasureUnit()%>&manufacture=<%=products.get(i).getManufacturer()%>&keeper=<%=products.get(i).getKeeper()%>'">
    						<%} else {%>
    						<input type="button" value="单个入库" onclick="window.location.href='ProductHandleServlet?operate=singleInApply&productId=<%=id%>'">
    						<%} %>
    						</span>
    					</td> --%>
							
							<%
								}
							%>
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

                    <div class="total">共<span>100</span>页</div>

                    <div class="go-page">
                        <span class="text">到第</span>
                        <input class="page_Input" type="text" value="">
                        <span class="text">页</span>
                        <a class="sure-btn" href="javascript:;" role="button" tabindex="0">确定</a>
                    </div>

				</div>
			</div>
			<div id="bottom_content">
				<div id="xinzengchanpin" class="addproduct_box">
					<div id="xinzengchanpintable" class="addproduct_table">
						<form action="ProductHandleServlet?operate=addproduct"
							method="post" id="myForm">
							<table class="contract-add-table">
								<tbody>
									<tr>
										<td style="border: 0px;">合同编号:</td>
										<td style="border: 0px;"><label id="contractid">${contract.contractId
												}</label></td>
										<td style="border: 0px;">整机名称:</td>
										<td style="border: 0px;"><input name="productname"
											id="productname" class="pname" /></td>
									</tr>
									<tr>
										<td style="border: 0px;">单元名称:</td>
										<td style="border: 0px;"><input name="productunit"
											id="productunit" class="unit"></td>
										<td style="border: 0px;">数量:</td>
										<td style="border: 0px;"><input id="productcount">
										</td>
									</tr>
									<tr>
										<td style="border: 0px;">品名内码：</td>
										<td style="border: 0px;"><input type="text" name="PMNM"
											id="PMNM" class="pmnm" value="" /></td>
										<td style="border: 0px;">产品型号:</td>
										<td style="border: 0px;"><input name="productmodel"
											id="productmodel" disabled="true" /></td>
									</tr>
									<tr>
										<td style="border: 0px;">单位:</td>
										<td style="border: 0px;"><input type="text"
											name="measureunit" id="measureunit" disabled="true">
										</td>
										<td style="border: 0px;">单价:</td>
										<td style="border: 0px;"><input type="text"
											name="productprice" id="productprice" value=""
											disabled="true" /></td>
										<!--  <td style="border: 0px;">机号：</td>
        <td style="border: 0px;">
         <input type="text" name="deviceNo" id="deviceNo" onblur="checkDeviceNo(this);"/><br/>
       	<span id="error">*机号重复请重新填写</span>
       	<span id="ok">*机号唯一，可以使用</span> 
       </td>-->
									</tr>
									<tr>
										<td style="border: 0px;">交付时限:</td>
										<td style="border: 0px;"><input class="sang_Calender"
											type="text" name="deliverytime" id="deliverytime" /> <script
												type="text/javascript"
												src="ConstantHTML/js/spdateTime.js"></script></td>
										<td style="border: 0px;">承制单位:</td>
										<td style="border: 0px;"><input type="text"
											name="manufacturer" id="manufacturer" /></td>
									</tr>
									<tr>
										<td style="border: 0px;">代储单位:</td>
										<td style="border: 0px;"><input type="text" name="keeper"
											id="keeper" /></td>
										<td style="border: 0px;">签订时间:</td>
										<td style="border: 0px;"><label id="signtime">${contract.signDate
												}</label></td>
									</tr>
									<tr>
										<td style="border: 0px;">采购单位:</td>
										<td style="border: 0px;"><label id="buyer">${contract.buyer
												}</label></td>
									</tr>
								</tbody>
							</table>
							<div style="float: right;margin-top: 10px;">
								<input class="search-btn" type="button" value="添加"
									onclick="addProduct();">
								<!-- <input class="search-btn" style="margin-right: 10px;" type="submit" value="提交" >-->
								<input class="search-btn" type="reset" value="重置" />
							</div>
						</form>
					</div>
					<div id="addprolist" class="addproduct_list">
						<p id="start-msg">您还没有添加数据！</p>
					</div>
					<div style="float: left;">
						<span id="submit" style="margin-top: 5px; display: none;"><input
							type="button" class="search-btn" value="批量提交"
							style="width: 80px;" onclick="upObjs();">
						</span>
					</div>
					<!-- 测试浮动显示框 -->
					<div id="view-table">
						<table class="contract-add-table">
							<tbody>
								<tr>
									<td style="border: 0px;">合同编号:</td>
									<td style="border: 0px;" id="cid-view"></td>
									<td style="border: 0px;">品名内码:</td>
									<td style="border: 0px;" id="pmnm-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">整机名称:</td>
									<td style="border: 0px;" id="name-view"></td>
									<td style="border: 0px;">单元名称:</td>
									<td style="border: 0px;" id="unit-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">机号:</td>
									<td style="border: 0px;" id="device-view"></td>
									<td style="border: 0px;">产品型号:</td>
									<td style="border: 0px;" id="model-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">单位:</td>
									<td style="border: 0px;" id="measure-view"></td>
									<td style="border: 0px;">单价:</td>
									<td style="border: 0px;" id="price-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">数量:</td>
									<td style="border: 0px;" id="num-view"></td>
									<td style="border: 0px;">交付时限:</td>
									<td style="border: 0px;" id="time-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">承制单位:</td>
									<td style="border: 0px;" id="manu-view"></td>
									<td style="border: 0px;">代储单位:</td>
									<td style="border: 0px;" id="keeper-view"></td>
								</tr>
								<tr>
									<td style="border: 0px;">签订时间:</td>
									<td style="border: 0px;" id="sign-view"></td>
									<td style="border: 0px;">采购单位:</td>
									<td style="border: 0px;" id="buyer-view"></td>
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
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
</body>
<script type="text/javascript" src="js/transact_business/jquery.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript" src="js/transact_business/jquery.bgiframe.pack.js"></script>
<script type="text/javascript" src="js/transact_business/jquery.combobox.js"></script>
<script type="text/javascript" src="js/transact_business/addBacthPro.js"></script>
<script src="js/transact_business/paging.js"></script>

</html>

