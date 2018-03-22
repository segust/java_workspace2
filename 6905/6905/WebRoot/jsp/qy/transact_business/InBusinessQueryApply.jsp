<%@ page language="java"
import="java.util.*,cn.edu.cqupt.beans.*,cn.edu.cqupt.util.*"
pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">

	<title>入库申请管理</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script src="js/transact_business/transact.js"></script>
	<script src="ConstantHTML/js/crossPageCheck.js"></script>
    <link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transactQueryProduct.css">
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">

	<%
	boolean isAll = (Boolean)request.getAttribute("allIn");
	%>
</head>

<%-- <body onload="isAllIn(<%=isAll %>);"> --%>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>

	<%
	List<HashMap<String,Object>> inApplyList = (List<HashMap<String,Object>>) request.getAttribute("inApplyList");
	%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span> <a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="InWarehouseServlet?operate=queryApply">入库管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			入库申请管理
		</div>
		<div id="content">
			<!--产品查询 -->
			<div id="lunhuanchurukuguanli" style="margin-top: 5px;">
				<div class="search-box">
					<form method="post" id="searchInApplyForm"
					action="${pageContext.request.contextPath}/InWarehouseServlet?operate=searchInApply">
					<span>合同号</span> <span><input class="setTem"
					name="contractId" type="text" /> </span> <span>产品型号</span> <span><input
					class="setTem" name="productType" type="text" /> </span> <span>单元名称</span>
					<span><input class="setTem" name="unitName" type="text" />
					</span>
					<br>
					<br>
					<span>操作类型</span> 
					<span>
						<select name="operateType" id="operateType" onchange="searchViaOpType()">
							<option value="allIn" selected="selected">所有入库</option>
							<option value="newIn">新入库</option>
							<option value="circleIn">轮换入库</option>
							<option value="circleOut">轮换出库</option>
							<option value="renowIn">更新入库</option>
							<option value="renewOut">更新出库</option>
						</select> 
					</span> 
					<span>时间段</span> <input name="fromDate" type="text"
					class="sang_Calender" /> -<input class="sang_Calender"
					name="toDate" type="text" />
					<script type="text/javascript"
					src="ConstantHTML/js/spdateTime.js"></script>

					<span>状态</span> <span><input class="setTem" name="status"
					type="text" /><!-- <select name="status">
					<option value="已通过">已通过</option>
					<option value="待审核">待审核</option>
					<option value="不通过">不通过</option>
					</select> --></span> <span><input class="search-btn"
					type="submit" value="查询申请" style="background-color: #3CB371;">
				</span>
			</form>
		</div>
		<div id="whole"></div>
		<div id="borrow_In">
			<table class="contract-add-table">
				<thead>
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
				</thead>
				<tbody id="borrowin_Table">
				</tbody>
			</table>
			<input type="button" value="确定" id="sure_borrow_In">
		</div>
		<div id="borrow_Out">
			<table class="contract-add-table">
				<thead>
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
		<div id="contract-table-box">
			<table id="contract-table">
				<thead>
					<tr class="Apply-head">
						<td>
							<input type="checkbox" id="checkLeader" onclick="getAll()">
						</td>
						<td>序号</td>
						<td>操作类型</td>
						<td>批次</td>
						<td>操作时间</td>
						<td>状态</td>
						<td>数量</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody>
					<%
							//存储bool值
					boolean isUpdateOutApply = (Boolean)request.getAttribute("isUpdateOutApply");
					boolean isBorrowOutApply = (Boolean)request.getAttribute("isBorrowOutApply");
							//curPageNum 当前页
					int curPageNum=(Integer)request.getAttribute("curPageNum");
							//pageSize 按多少条分页
					int pageSize=(Integer)request.getAttribute("pageSize");
					HashMap<String,Object> inApplyTemp = new HashMap<String,Object>();
					if (inApplyList!=null) {
					int inApplyListLen = inApplyList.size();

					for (int i = 0; i < inApplyListLen; i++) {
					inApplyTemp = inApplyList.get(i);
									//格式化产品生产日期和操作日期时间
									boolean isIn = (Boolean)request.getAttribute("isIn");
									String formatProducedDate = "";
									String formatexecDate = "";
									if(isIn) {
									InApply temp = (InApply)inApplyTemp.get("apply");
									java.util.Date producedDateTemp = temp.getProducedDate();
									java.util.Date execDateTemp = temp.getExecDate();
									formatProducedDate = MyDateFormat.changeDateToString(producedDateTemp);
									formatexecDate = MyDateFormat.changeDateToString(execDateTemp);
									pageContext.setAttribute("inApply", temp);
								}else {
								OutApply temp = (OutApply)inApplyTemp.get("apply");
								java.util.Date producedDateTemp = temp.getProducedDate();
								java.util.Date execDateTemp = temp.getExecDate();
								formatProducedDate = MyDateFormat.changeDateToString(producedDateTemp);
								formatexecDate = MyDateFormat.changeDateToString(execDateTemp);
								pageContext.setAttribute("inApply", temp);
							}
							Product pro = (Product)inApplyTemp.get("product");
							
									//将inApplyTemp对象放到page中，目的是下面的表达式语言的使用
									//使用表达式语言的好处是，当值为null的时候，页面不显示“null”
									pageContext.setAttribute("isUpdateOutApply", isUpdateOutApply);
									pageContext.setAttribute("isBorrowOutApply", isBorrowOutApply);
									pageContext.setAttribute("pro", pro);
									int count = i+1+(curPageNum-1) * pageSize;
									%>
									<tr class="contract-add-table-body">
										<td>
											<c:if test="${isIn==true }">
											<input type="checkbox" name="mycheck" onclick="checkChange(this)"/><input type="hidden" value="${inApply.inId}"/> 
										</c:if> 
										<c:if test="${isIn==false }">
										<input type="checkbox" name="mycheck" onclick="checkChange(this)"/><input type="hidden" value="${inApply.outId}"/> 
									</c:if>
								</td>
								<td><%=count%></td>
								<c:if test="${isIn==true }">
								<td id="means">${inApply.inMeans}</td>
							</c:if>
							<c:if test="${isIn==false }">
							<td id="means">${inApply.outMeans}</td>
						</c:if>
						<td>${inApply.batch}</td>
						<td><%=formatexecDate %></td>
						<td>${inApply.chStatus}</td>
						<td>${inApply.num}</td>
						<td><input input type="button" value="查看"
							name="operate_detail" onclick="detail_Apply(this);"></td>
							<!-- 	</form> -->
						</tr>

						<%
					}
				}
				%>
			</tbody>
		</table>
		<div style="float: right;margin-top: 10px;">
						<%-- <input type="button" class="search-btn" value="全选"
							onclick="getAll();">
							&nbsp;&nbsp;  --%>
							<input type="button"
							class="search-btn" value="导出"
							onclick="checktransact('<%=request.getSession().getAttribute("ownedUnit") %>',1);">
						</div>
					</div>
				</div>
			</div>
			<form method="post" id="checkedIdForm">
				<input type="hidden" id="checkedIdStr" name="checkedIdStr"
				value="<%=request.getAttribute("checkedIdStr") %>"> <input
				type="hidden" id="unCheckedIdStr" name="unCheckedIdStr" value="">
			</form>
			<div class="page-box">
				<% 
					//userSum 全部用户的个数
				int userSum=(Integer)request.getAttribute("sum");
					//根据总数和每页条数计算分多少页
				long totalPageNum = userSum%pageSize==0?userSum/pageSize:(userSum/pageSize+1);
				if(totalPageNum==0){
				totalPageNum=1;
				curPageNum=1;
			}
					//上一页的页码
			long prePageNum = curPageNum-1<1?1:curPageNum-1;
					//下一页的页码
			long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
			%>
			<%
			String flag = request.getAttribute("query").toString();
			if("1".equals(flag)) { 
			%>
			<span> 
				<a
				onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=queryApply&curPageNum=1&pageSize=<%=pageSize %>')">首页</a>
				<a
				onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=queryApply&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>')">&lt;&lt;</a>
				<span><%=curPageNum %></span> <a
				onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=queryApply&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>')">&gt;&gt;</a>
				<a
				onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=queryApply&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>')">尾页</a>
			</span>
			<%
		}else if("2".equals(flag)){
		String contractId=request.getParameter("contractid");
		String productType=request.getParameter("productType");
		String unitName=request.getParameter("unitName");
		String operateType=request.getParameter("operateType");
		String fromDate=request.getParameter("fromDate");
		String toDate=request.getParameter("toDate");
		String status=request.getParameter("status");
		%>
		<span> 
			<a
			onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=searchInApply&contractId=<%=contractId%>&productType=<%=productType%>&unitName=<%=unitName%>&operateType=<%=operateType%>&fromDate=<%=fromDate%>&toDate=<%=toDate%>&status=<%=status%>&curPageNum=1&pageSize=<%=pageSize %>','mycheck')">首页</a>
			<a
			onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=searchInApply&contractId=<%=contractId%>&productType=<%=productType%>&unitName=<%=unitName%>&operateType=<%=operateType%>&fromDate=<%=fromDate%>&toDate=<%=toDate%>&status=<%=status%>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>','mycheck')">&lt;&lt;</a>
			<span><%=curPageNum %></span> 
			<a
			onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=searchInApply&contractId=<%=contractId%>&productType=<%=productType%>&unitName=<%=unitName%>&operateType=<%=operateType%>&fromDate=<%=fromDate%>&toDate=<%=toDate%>&status=<%=status%>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>','mycheck')">&gt;&gt;</a>
			<a
			onclick="turnToPageWithCheckedbox('InWarehouseServlet?operate=searchInApply&contractId=<%=contractId%>&productType=<%=productType%>&unitName=<%=unitName%>&operateType=<%=operateType%>&fromDate=<%=fromDate%>&toDate=<%=toDate%>&status=<%=status%>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>','mycheck')">尾页</a>
		</span>
		<%} %>
		<span>跳到第</span> <input type="text" id="skipPageNum" /> <span>页</span>
		<a onclick="turnToPageWithCheckedbox(skipPage(),'mycheck')">确定</a> 
		<span>每页显示</span> 
		<select
		name="selectPageSize" onchange='selectPageSize(this.value)'
		id="selectPageSize">
		<option value="10">10</option>
		<option value="15">15</option>
		<option value="20">20</option>
	</select> 
	<span>条记录，共</span> 
	<label><%=totalPageNum %></label> <span>页</span>
</div>
</div>
<!-- 跳转页面的有效性判断和跳转 -->
<script src="ConstantHTML/js/changeTrColor.js"></script>
<script>
function skipPage(){
	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
	var pageSize=<%=pageSize %>;
	var flag = <%=flag%>;
	if(skipPageNum<=0)
		alert("请输入有效页面");
	else if(skipPageNum><%=totalPageNum%>)
		alert("您输入的页面大于总页数");
	else if(flag =="1")
		window.location.href="InWarehouseServlet?operate=queryApply&curPageNum="+skipPageNum+"&pageSize="+pageSize;
	else if(flag =="2")
		window.location.href="InWarehouseServlet?operate=searchInApply&contractId=<%=request.getParameter("contractid") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&status=<%=request.getParameter("status") %>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
}
</script>

<!-- 用户选择每页显示的条数后提交到Servlet -->
<script>
function selectPageSize(value){
	var pageSize=value;
	var flag = <%=flag%>;
	if(flag =="1") 
		window.location.href="InWarehouseServlet?operate=queryApply&curPageNum=1&pageSize="+value;
	else if(flag =="2")
		window.location.href="InWarehouseServlet?operate=searchInApply&contractId=<%=request.getParameter("contractid") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&status=<%=request.getParameter("status") %>&curPageNum=1&pageSize="+value;
}
</script>

<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
<script>
var pageSize="<%=request.getAttribute("pageSize")%>";
if(pageSize=="null")
	document.getElementById("selectPageSize").value="10";
else
	document.getElementById("selectPageSize").value=pageSize;
</script>

<!-- 控制操作类型页面跳转后保持一致 -->
<script>
var operateType="<%=request.getAttribute("operateType")%>";
if(operateType=="null")
	document.getElementById("operateType").value="allIn";
else
	document.getElementById("operateType").value=operateType;
</script>

<!-- 显示是否选择了当前页面的checkbox -->
<script>
var checkedIdFlagStr="<%=request.getAttribute("checkedIdFlagStr")%>",
checkedIdFlagArray=[],
oCheckboxBody=document.getElementsByName("mycheck");
if(checkedIdFlagStr!="null"&&checkedIdFlagStr!=""){
	checkedIdFlagArray=checkedIdFlagStr.split(",");
}
for (var i = 0; i < checkedIdFlagArray.length; i++) {
	if(checkedIdFlagArray[i]==1){
		oCheckboxBody[i].checked=true;
	}
}
</script>
<%@include file="../../../ConstantHTML/foot.html"%>
</body>
</html>
