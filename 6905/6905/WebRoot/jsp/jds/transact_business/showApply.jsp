<%@ page language="java" import="java.util.*,cn.edu.cqupt.beans.*,cn.edu.cqupt.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>查看申请</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="js/transact_business/transact.js"></script>
	<script type="text/javascript" src="jquery.json-2.4.min.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transactQueryProduct.css" />
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/jdsleft.html" %>
      
      <%
		List<HashMap<String,Object>> inApplyList = (List<HashMap<String,Object>>) request.getAttribute("inApplyList");
		%>
      
      	<div id="right">
      	<div class="subName">

			</div>
       <div id="content">
       	     <!--产品查询 -->
        <div id="lunhuanchurukuguanli" style="margin-top: 5px;">
        <!-- 
            <div class="search-box"  >
				
            </div> -->
            	<div id="contract-table-box">
				   <table id="contract-table">
						<thead>
							<tr class="Apply-head">
								<td style="background-color: #669966; "></td>
								<td style="background-color: #669966; ">序号</td>
								<td style="background-color: #669966; ">产品型号</td>
								<td style="background-color: #669966; ">单元名称</td>
								<td style="background-color: #669966; ">机号</td>
								<td style="background-color: #669966; ">单价</td>
								<td style="background-color: #669966; ">数量</td>
								<td style="background-color: #669966; ">计量单位</td>
								<td style="background-color: #669966; ">类型</td>
								<td style="background-color: #669966; ">操作类型</td>
								<td style="background-color: #669966; ">操作日期</td>
								<td style="background-color: #669966; ">生产日期</td>
								<td style="background-color: #669966; ">存储期限</td>
								<td style="background-color: #669966; ">企业定期维护周期</td>
								<td style="background-color: #669966; ">承制单位</td>
								<td style="background-color: #669966; ">代储单位</td>
								<td style="background-color: #669966; ">合同编号</td>
								<td style="background-color: #669966; ">状态</td>
								<td style="background-color: #669966; ">JD室</td>
								<td style="background-color: #669966; ">审核人</td>
								<td style="background-color: #669966; ">操作</td>
							</tr>
						</thead>
						<tbody>
						<%
							//curPageNum 当前页
							int curPageNum=(Integer)request.getAttribute("curPageNum");
							//pageSize 按多少条分页
							int pageSize=(Integer)request.getAttribute("pageSize");
						    HashMap<String,Object> inApplyTemp = new HashMap<String,Object>();
							if (inApplyList != null) {
								int inApplyListLen = inApplyList.size();
								
								for (int i = 0; i < inApplyListLen; i++) {
									inApplyTemp = inApplyList.get(i);
									//格式化产品生产日期和操作日期时间
									InApply temp = (InApply)inApplyTemp.get("apply");
									Product pro = (Product)inApplyTemp.get("product");
									java.util.Date producedDateTemp = temp.getProducedDate();
									java.util.Date execDateTemp = temp.getExecDate();
									
									String formatProducedDate = MyDateFormat.changeDateToString(producedDateTemp);
									String formatexecDate = MyDateFormat.changeDateToString(execDateTemp);
									//将inApplyTemp对象放到page中，目的是下面的表达式语言的使用
									//使用表达式语言的好处是，当值为null的时候，页面不显示“null”
									pageContext.setAttribute("inApply", temp);
									pageContext.setAttribute("pro", pro);
									int count = i+1+(curPageNum-1) * pageSize;
									//request.setAttribute("inApply", temp);
									
						%>
							<tr class="contract-add-table-body">
									<td><input type="checkbox" name="mycheck"/>
									<input type="hidden" name="id" value="${inApply.inId}"></td>
									<td><%=count%></td>
									<td>${pro.productModel}</td>
									<td>${inApply.unitName}</td>
									<td>${inApply.deviceNo}</td>
									<td>${inApply.newPrice}</td>
									<td>${inApply.num}</td>
									<td>${inApply.measure}</td>
									<td>数据库里不知道</td>
									<td>${inApply.inMeans}</td>
									<td><%=formatexecDate %></td>
									<td><%=formatProducedDate %></td>
									<td>${inApply.storageTime}</td>
									<td>${inApply.maintainCycle}</td>
									<td>${inApply.manufacturer}</td>
									<td>${inApply.keeper}</td>
									<td>${inApply.contractId}</td>
									<td>${inApply.chStatus}</td>
									<td>本军代室</td>
									<td>${inApply.PMNM}</td>
							<!-- 	</form> -->
							</tr>
						
						<%
							}
							}
						%>
						</tbody>
					</table>
					<div style="float: right;">
					<input type="button" class="search-btn" value="全选" onclick="getAll();">
					<input type="button" class="search-btn" value="导出" onclick="checktransact();"><input type="button" class="search-btn" value="打印" onclick="checktransact();"><input type="button" class="search-btn" value="审核通过" onclick="jdsCheck();">  <input type="button" class="search-btn" value="审核不通过" onclick="jdsNoCheck();"></div>		
					</div>		
				</div>
			</div>
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
			    <a href="InWarehouseServlet?operate=listQueryApply&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="InWarehouseServlet?operate=listQueryApply&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="InWarehouseServlet?operate=listQueryApply&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="InWarehouseServlet?operate=listQueryApply&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
			    </span>
			    <%}else if("2".equals(flag)){%>
			    	<span>
			    <a href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper")%>&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper")%>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper")%>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper")%>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
			    </span>
			    <%} %>
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage()' >确定</a>
		   		<span>每页显示</span>
			    <select name="selectPageSize" onchange='selectPageSize(this.value)' id="selectPageSize">
			        <option value ="10">10</option>
			        <option value ="15">15</option>
			        <option value="20">20</option>
			    </select>
		    	<span>条记录，共</span>
		    	<label><%=totalPageNum %></label>
		    	<span>页</span>
			</div>
		</div>
		<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	var flag = <%=flag%>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else if(flag =="1")
					window.location.href="InWarehouseServlet?operate=listQueryApply&curPageNum="+skipPageNum+"&pageSize="+pageSize;
				else if(flag =="2")
					window.location.href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper") %>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var flag = <%=flag%>;
				if(flag =="1") 
					window.location.href="InWarehouseServlet?operate=listQueryApply&curPageNum=1&pageSize="+value;
				else if(flag =="2")
					window.location.href="InWarehouseServlet?operate=listQuery&manufacturer=<%=request.getParameter("manufacturer") %>&productType=<%=request.getParameter("productType") %>&unitName=<%=request.getParameter("unitName") %>&operateType=<%=request.getParameter("operateType") %>&fromDate=<%=request.getParameter("fromDate") %>&toDate=<%=request.getParameter("toDate") %>&keeper=<%=request.getParameter("keeper") %>&curPageNum=1&pageSize="+value;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
				var pageSize="<%=request.getAttribute("pageSize")%>";
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>
      <%@include file="../../../ConstantHTML/jdsfoot.html" %>
  </body>
    <script type="text/javascript" src="js/transact_business/dateTime.js"></script>
</html>
