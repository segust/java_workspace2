<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page
	import="cn.edu.cqupt.beans.*,java.sql.*,cn.edu.cqupt.dao.*, java.util.*"%>

<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>基础数据库</title>

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
	href="css/sys_management/systemmanage.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript" src="js/sys_management/basedata.js"></script>
<style>
.search_box_basedata tr:hover {
	background-color: white;
}
</style>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<div class="subName">
			<span>当前位置：</span> <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			基础数据库
		</div>

		<div class="fare-data" id="fare-data">
			<div class="search_box_basedata">
				<form action="ServiceOfBaseDataServlet?operate=select" method="post">
					<span>产品内码</span> <span><input class="setTem" type="text"
						name="PMNM" /> </span> <span>产品名称</span> <span><input
						class="setTem" type="text" name="PMCS" /> </span> <span> <input
						class="scan-btn" type="submit" value="查询"> <%-- <input
						class="scan-btn" type="button" onclick="getchoose();" value="批量删除">
						<input class="scan-btn" type="button"
						onclick="window.location.href='<%=basePath%>jsp/jdj/sys_management/add_base_data.jsp'"
						value="加入新纪录"> </span> --%>
				</form>
			</div>
			<div class="impAndDel">
				<form
					action="${pageContext.request.contextPath}/ServiceOfBaseDataServlet?operate=importBaseDataFile"
					method="post" enctype="multipart/form-data">
					<!-- <input type="file" name="attachment"> -->
					<input type="text" size="20" name="upfile" id="upfile"
						style="padding:5px 0;border:1px dotted black"> <input
						type="button" value="选择文件" onclick="path.click()"> <input
						id="path" type="file" style="display:none" name="attachment"
						onchange="upfile.value=this.value"> <input type="submit"
						value="导入" class="scan-btn">
				</form>
			</div>
			<div id="fare-table-box">
				<table class="fare-table" id="fare-table">
					<thead>
						<tr>
							<th><input type="checkbox" id="check_basedata_leader"
								onclick="chooseAll();"></th>
							<th>序号</th>
							<th>PMNM(内码)</th>
							<th>PMBM(编码)</th>
							<th>QCBM</th>
							<th>PMCS(名称)</th>
							<th>XHTH</th>
							<th>XLDJ</th>
							<th>XHDE</th>
							<th>JLDW</th>
							<th>MJYL</th>
							<th>QCXS</th>
							<th>BZZL</th>
							<th>BZJS</th>
							<th>BZTJ</th>
							<th>CKDJ(单价)</th>
							<th>SCCJNM</th>
							<th>GHDWNM</th>
							<th>ZBSX</th>
							<th>LBQF(类型)</th>
							<th>ZBBDSJ</th>
							<th>SYBZ</th>
							<th>YJDBZ</th>
							<th>SCBZ</th>
							<th>SCDXNF</th>
							<th>编辑</th>
							<th>删除</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<%
							ArrayList<Basedata> message=(ArrayList<Basedata>)request.getAttribute("message");
										                      for(int i=0;i<message.size();i++){
						%>
						<tr class="contract-add-table-body">
							<td><input type="checkbox" name="check_basedata" /> <input
								type="hidden" value=<%=message.get(i).getId()%>></td>
							<td><%=i+(Integer)request.getAttribute("pageSize")*((Integer)request.getAttribute("curPageNum")-1)+1%></td>
							<td><%=message.get(i).getPMNM()%></td>
							<td><%=message.get(i).getPMBM()%></td>
							<td><%=message.get(i).getQCBM()%></td>
							<td><%=message.get(i).getPMCS()%></td>
							<td><%=message.get(i).getXHTH()%></td>
							<td><%=message.get(i).getXLDJ()%></td>
							<td><%=message.get(i).getXHDE()%></td>
							<td><%=message.get(i).getJLDW()%></td>
							<td><%=message.get(i).getMJYL()%></td>
							<td><%=message.get(i).getQCXS()%></td>
							<td><%=message.get(i).getBZZL()%></td>
							<td><%=message.get(i).getBZJS()%></td>
							<td><%=message.get(i).getBZTJ()%></td>
							<td><%=message.get(i).getCKDJ()%></td>
							<td><%=message.get(i).getSCCJNM()%></td>
							<td><%=message.get(i).getGHDWNM()%></td>
							<td><%=message.get(i).getZBSX()%></td>
							<td><%=message.get(i).getLBQF()%></td>
							<td><%=message.get(i).getZBBDSJ()%></td>
							<td><%=message.get(i).getSYBZ()%></td>
							<td><%=message.get(i).getYJDBZ()%></td>
							<td><%=message.get(i).getSCBZ()%></td>
							<td><%=message.get(i).getSCBZ()%></td>
							<td><input type="button"
								onclick="window.location.href='<%=basePath%>ServiceOfBaseDataServlet?operate=edit&id=<%=message.get(i).getId()%>'"
								value="编辑"></td>
							<td><input type="button" value="删除"
								onclick="deletebasedata(this)"></td>
							<td>
								<%
									if((message.get(i).getQCBM()+"").equals("0")){
								%> <input
								type="button" value="增加单元" id="addUnit"
								onclick="window.location.href='jsp/zhj/sys_management/add_unit.jsp?pmnm=<%=message.get(i).getPMNM()%>'">
								<input type="button" id="manageUnit"
								onclick="window.location.href='<%=basePath%>ServiceOfBaseDataServlet?operate=selectUnit&PMNM=<%=message.get(i).getPMNM()%>'"
								value="管理单元">
							</td>
							<%
								}
							%>
							<%
								}
							%>
						</tr>
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
							String PMCS=(String)condition.get("PMCS");
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
			<span> <a
				href="SystemManagementServlet?operate=basedata&curPageNum=1&pageSize=<%=pageSize%>
			    		&PMNM=<%=PMNM%>&PMCS=<%=PMCS%>">首页</a>
				<a
				href="SystemManagementServlet?operate=basedata&curPageNum=<%=prePageNum%>&pageSize=<%=pageSize%>
			    		&PMNM=<%=PMNM%>&PMCS=<%=PMCS%>">&lt;&lt;</a>
				<span><%=curPageNum%></span> <a
				href="SystemManagementServlet?operate=basedata&curPageNum=<%=nextPageNum%>&pageSize=<%=pageSize%>
			    		&PMNM=<%=PMNM%>&PMCS=<%=PMCS%>">&gt;&gt;</a>
				<a
				href="SystemManagementServlet?operate=basedata&curPageNum=<%=totalPageNum%>&pageSize=<%=pageSize%>
			    		&PMNM=<%=PMNM%>&PMCS=<%=PMCS%>">尾页</a>
			</span> <span>跳到第</span> <input type="text" id="skipPageNum" /> <span>页</span>
			<a onclick='skipPage()'>确定</a> <span>每页显示</span> <select
				name="selectPageSize" onchange='selectPageSize(this.value)'
				id="selectPageSize">
				<option value="10">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
			</select> <span>条记录，共</span> <label><%=totalPageNum%></label> <span>页</span>
		</div>
	</div>


	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
	<!-- 跳转页面的有效性判断和跳转 -->
	<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize="<%=pageSize%>";
			 	var PMNM="<%=PMNM%>";
			 	var PMCS="<%=PMCS%>";
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="SystemManagementServlet?operate=basedata&curPageNum="+skipPageNum+"&pageSize="+pageSize
											+"&PMNM="+PMNM+"&PMCS="+PMCS;
			}
		</script>

	<!-- 用户选择每页显示的条数后提交到Servlet -->
	<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var PMNM="<%=PMNM%>";
			 	var PMCS="<%=PMCS%>";
				window.location.href="SystemManagementServlet?operate=basedata&curPageNum=1&pageSize="+value
										+"&PMNM="+PMNM+"&PMCS="+PMCS;
			}
		</script>

	<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
	<script type="text/javascript">
		var pageSize="<%=pageSize%>";
		if (pageSize == "null")
			document.getElementById("selectPageSize").value = "10";
		else
			document.getElementById("selectPageSize").value = pageSize;
	</script>
</body>
</html>
