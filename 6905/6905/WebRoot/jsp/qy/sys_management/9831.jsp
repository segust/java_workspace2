<%@page import="cn.edu.cqupt.beans.Common9831"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName

()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>9831库</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/sys_management/systemmanage.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script src="js/sys_management/tools.js"></script>
<script src="js/sys_management/9831.js"></script>
<style>
.impAndDel [type="button"] {
	border: 0px;
	color: white;
	height: 25px;
	padding: 0 5px;
	font-weight: bold;
	background: #3CB371;
}

.impAndDel [type="button"]:hover {
	background-color: #009933;
}
</style>
</head>


<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/left.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="ServiceOf9831Servlet?operate=select">9831库</a>
		</div>

		<!-- 内容区 -->
		<div class="fare-data" id="fare-data">
			<div class="search_box">
				<form id="9832-Slt-form">
					<span>PMNM(内码)</span> <span><input name="PMNM" type="text">
					</span> <span>PMBM(编码)</span> <span><input name="PMBM" type="text">
					</span> <span>PMCS(名称)</span> <span><input name="PMCS" type="text">
					</span> <span>LBQF(类型)</span> <span><input name="LBQF" type="text">
					</span> <span> <input name="codeSubmit" class="scan-btn"
						type="button" value="查询" class="scan_btn"
						onclick="slt9831ByCdt(1);"> <input type="button"
						class="scan-btn"
						onclick="window.location.href='../6905/jsp/qy/sys_management/add_9831.jsp'"
						value="加入新纪录" id="add_new"> <input type="button"
						class="scan-btn" style="width: 120px;" value="批量加入待选库"
						onclick="getchoose();"> </span>
				</form>
			</div>
			<div class="impAndDel">
				<form
					action="${pageContext.request.contextPath}/ServiceOf9831Servlet?operate=LoadIn9831"
					method="post" enctype="multipart/form-data" style="margin:20px 0;"
					onsubmit="return format()">
					<!-- <input type="file" name="attachment"> -->
					<input type="text" size="20" name="upfile" id="upfile"
						style="padding:5px 0;border:1px dotted black"> <a
						class="file-wrap" href="#"> 浏览 <input id="fileToUpload"
						type="file" name="attachment" accept=".xls,.xlsx"
						onchange="upfile.value=this.value"> </a> <input type="submit"
						value="导入">
					<p class="error-info"></p>
				</form>
			</div>
			<div id="fare-table-box">
				<table id="fare-table">
					<thead>
						<tr>
							<th><input type="checkbox" id="check_9831_leader"
								onclick="chooseAll();">
							</th>
							<th>序号</th>
							<th>PMNM</th>
							<th>PMBM</th>
							<th>QCBM</th>
							<th>PMCS</th>
							<th>XHTH</th>
							<th>JLDW</th>
							<th>CKJG</th>
							<th>BZTJ</th>
							<th>BZJS</th>
							<th>BZZL</th>
							<th>QCXS</th>
							<th>MJYL</th>
							<th>XHDE</th>
							<th>XLDJ</th>
							<th>SCCJNM</th>
							<th>GHDWNM</th>
							<th>ZBSX</th>
							<th>SCDXNF</th>
							<th>LBQF</th>
							<th>YJDBZ</th>
							<th>SYBZ</th>
							<th>SCBZ</th>
							<th>ZBBDSJ</th>
						</tr>
					</thead>
					<tbody id="9831-info-table">
						<%
							ArrayList<Common9831> message=(ArrayList<Common9831>)request.getAttribute("message");
						 	for(int i=0;i<message.size();i++){
						%>
						<tr class="contract-add-table-body">
							<td><input type="checkbox" name="check_9831" /></td>
							<td><%=i+(Integer)request.getAttribute("pageSize")*((Integer)request.getAttribute("curPageNum")-1)+1%></td>
							<td><%=message.get(i).getPMNM()%></td>
							<td><%=message.get(i).getPMBM()%></td>
							<td><%=message.get(i).getQCBM()%></td>
							<td><%=message.get(i).getPMCS()%></td>
							<td><%=message.get(i).getXHTH()%></td>
							<td><%=message.get(i).getJLDW()%></td>
							<td><%=message.get(i).getCKDJ()%></td>
							<td><%=message.get(i).getBZTJ()%></td>
							<td><%=message.get(i).getBZJS()%></td>
							<td><%=message.get(i).getBZZL()%></td>
							<td><%=message.get(i).getQCXS()%></td>
							<td><%=message.get(i).getMJYL()%></td>
							<td><%=message.get(i).getXHDE()%></td>
							<td><%=message.get(i).getXLDJ()%></td>
							<td><%=message.get(i).getSCCJNM()%></td>
							<td><%=message.get(i).getGHDWNM()%></td>
							<td><%=message.get(i).getZBSX()%></td>
							<td><%=message.get(i).getSCDXNF()%></td>
							<td><%=message.get(i).getLBQF()%></td>
							<td><%=message.get(i).getYJDBZ()%></td>
							<td><%=message.get(i).getSYBZ()%></td>
							<td><%=message.get(i).getSCBZ()%></td>
							<td><%=message.get(i).getZBBDSJ()%></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
		<!-- 分页的盒子 -->
		<div class="page-box">
			<%
			 //得到当前查询条件
			 HashMap<String, Object> condition = (HashMap<String, Object>)request.getAttribute("condition");
			 String PMNM=(String)condition.get("PMNM");
			 String PMBM=(String)condition.get("PMBM");
			 String PMCS=(String)condition.get("PMCS");
			 String LBQF=(String)condition.get("LBQF");
						//curPageNum 当前页
			 int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
						//pageSize 按多少条分页
			 int pageSize=Integer.parseInt(request.getParameter("pageSize"));
						//userSum 全部明细信息的个数
			 int DetailSum=(Integer)request.getAttribute("Detailsum");
						//根据总数和每页条数计算分多少页
			 int totalPageNum = DetailSum%pageSize==0?DetailSum/pageSize:(DetailSum/pageSize+1);
			 if(totalPageNum==0){
			 	totalPageNum=1;
			 	curPageNum=1;
			}
						//上一页的页码
			int prePageNum = curPageNum-1<1?1:curPageNum-1;
						//下一页的页码
			int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
			%>
			<span> <a onclick="slt9831ByCdt(1)">首页</a> <a
				onclick="slt9831ByCdt(<%=prePageNum%>)">&lt;&lt;</a> <span><%=curPageNum%></span>
				<a onclick="slt9831ByCdt(<%=nextPageNum%>)">&gt;&gt;</a> <a
				onclick="slt9831ByCdt(<%=totalPageNum%>)">尾页</a> </span> <span>跳到第</span> <input
				type="text" id="skipPageNum" /> <span>页</span> <a
				onclick="slt9831ByCdt(-1);">确定</a> <span>每页显示</span> 
				<select name="selectPageSize" onchange="slt9831ByCdt(1);" id="selectPageSize">
				<option value="10">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
			</select> <span>条记录，共</span> <label id="totalPageNum"><%=totalPageNum%></label>
			<span>页</span>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/foot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
	<script src="js/public/format-upload.js"></script>
</body>
</html>
