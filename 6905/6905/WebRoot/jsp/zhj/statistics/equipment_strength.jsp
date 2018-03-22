<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>器材实力汇总统计</title>

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
			href="css/sys_management/systemmanage.css" />
			<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script src="ConstantHTML/js/xcConfirm.js"></script>
		<script type="text/javascript" src="js/statistics/statistics.js"></script>
	</head>

	<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/zhjleft.html"%>
		<div id="right">
			<!-- 二级标题开始 -->
			<div class="subName">
				<span>当前位置：</span>
      			<a href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=10">实力统计</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=10">器材实力汇总统计</a>
			</div>
			<!-- 二级标题结束 -->
			
			<!-- 内容区开始 -->
			<div class="fare-data" id="fare-data">
				<!--轮换更新查询子页面-->
				<!--搜索框开始-->
				<div class="search_box">
					<form
						action="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=10"
						method="post" onsubmit="return judgeInputTime('inYearId')">
						<span style="margin-left: 10px;">在库年份</span>
						<span><input class="setTem" name="inYear" type="text" id="inYearId"/> </span>
						<span>器材代码</span>
						<span><input class="setTem" name="QCBM" type="text" /> </span>
						<span>产品名称</span>
						<span><input class="setTem" name="productName" type="text" />
						<span>产品型号</span>
						<span><input class="setTem" name="productModel" type="text" />
						</span>
						</span>
						<span>
							<input class="scan-btn" type="submit" value="查询">
							<input type="button" value="导出" class="scan-btn" onclick="equipmentCollectiveToWord();">
							<input type="button" value="全部导出" class="scan-btn" onclick="allEquipmentCollectiveToWord();">
						</span>
					</form>
				</div>
				<!--搜索框结束-->
				<p style="margin-left: 20px;">
					<label>总金额：</label><%=request.getAttribute("totalPrice") %>
					&nbsp;&nbsp;<label>本页总金额：</label><%=request.getAttribute("price") %>
				</p>

				<!-- 表格开始 -->
				<div class="">
					<!-- content -->
					<div class="">
						<!-- show_table -->
						<div id="equipment_strength-box">
						<h1 class="title">通信装备代储维修器材实力汇总表</h1>
							<!-- show_content -->
							<table id="equipment_strength">
								<thead>
									<tr>
										<th>
											<input type="checkbox" id="checkboxLeader" onclick="chooseAll();">
										</th>
										<th>
											序号
										</th>				
										<th>
											器材代码
										</th>
										<th>
											名称型号
										</th>
										<th>
											计量单位
										</th>
										<th>
											数量
										</th>
										<th>
											单价(元)
										</th>
										<th>
											金额(元)
										</th>
										<th>
											代储企业
										</th>
										<th>
											备注
										</th>
									</tr>
								</thead>
								<tbody>
									<tr class="contract-add-table-body">
										<%
											//得到当前查询条件
											//curPageNum 当前页
											int curPageNum=(Integer)request.getAttribute("curPageNum");
											//pageSize 按多少条分页
											int pageSize=(Integer)request.getAttribute("pageSize");
											//productSum 全部设备信息的个数
											int productSum=(Integer)request.getAttribute("sum");
											//根据总数和每页条数计算分多少页
											int totalPageNum = productSum%pageSize==0?productSum/pageSize:(productSum/pageSize+1);
											if(totalPageNum==0){
												totalPageNum=1;
												curPageNum=1;
												}
											//上一页的页码
											int prePageNum = curPageNum-1<1?1:curPageNum-1;
											//下一页的页码
											int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
											
											String inYear = (String)request.getAttribute("inYear");
											String QCBM = (String)request.getAttribute("QCBM");
											String productName = (String)request.getAttribute("productName");
											String productModel = (String)request.getAttribute("productModel");
											String keeper = (String)request.getAttribute("keeper");
											
										String message=(String)request.getAttribute("message");
											if(message!=null){
										%>
										<td><%=message %></td>
										<%
										}else{
											List<Map<String, Object>> list=(List<Map<String, Object>>)request.getAttribute("list");
											for(int i = 0 ; i<list.size() ; i++){
									 	%>
										<tr class="contract-add-table-body">
											<td>
												<input class="setTem" type="checkbox"
													name="checkbox_strength" />
											</td>
											<td><%=(curPageNum-1)*pageSize + 1 + i%></td>
											<td><%=list.get(i).get("QCBM") %></td>
											<td><%=list.get(i).get("productName") %>+<%=list.get(i).get("productModel") %></td>
											<td><%=list.get(i).get("measureUnit") %></td>
											<td><%=list.get(i).get("num") %></td>
											<td><%=list.get(i).get("productPrice") %></td>
											<td><%=list.get(i).get("totalPrice") %></td>
											<td><%=list.get(i).get("ownedUnit") %></td>
											<td><%=list.get(i).get("remark") %></td>
										</tr>
										<%		
											}
										} 
										%>									
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- 内容区结束 -->
			
			<!-- 分页的盒子开始 -->
			<div class="page-box">
				<span> <a
					href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=<%=pageSize%>&inYear=<%=inYear%>&QCBM=<%=QCBM%>&productName=<%=productName%>&productModel=<%=productModel%>&keeper=<%=keeper%>">首页</a>
					<a
					href="EquipmentServlet?operate=equipmentCollective&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear%>&QCBM=<%=QCBM%>&productName=<%=productName%>&productModel=<%=productModel%>&keeper=<%=keeper%>">&lt;&lt;</a>
					<span><%=curPageNum%></span> <a
					href="EquipmentServlet?operate=equipmentCollective&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear%>&QCBM=<%=QCBM%>&productName=<%=productName%>&productModel=<%=productModel%>&keeper=<%=keeper%>">&gt;&gt;</a>
					<a
					href="EquipmentServlet?operate=equipmentCollective&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear%>&QCBM=<%=QCBM%>&productName=<%=productName%>&productModel=<%=productModel%>&keeper=<%=keeper%>">尾页</a>
				</span>
				<span>跳到第</span>
				<input type="text" id="skipPageNum" />
				<span>页</span>
				<a onclick='skipPage()'>确定</a>
				<span>每页显示</span>
				<select name="selectPageSize" onchange='selectPageSize(this.value)'
					id="selectPageSize">
					<option value="10">
						10
					</option>
					<option value="15">
						15
					</option>
					<option value="20">
						20
					</option>
				</select>
				<span>条记录，共</span>
				<label><%=totalPageNum %></label>
				<span>页</span>
			</div>
			<!-- 分页的盒子结束 -->
		</div>
		<%@include file="../../../ConstantHTML/zhjfoot.html"%>
		
		<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=document.getElementById('skipPageNum').value;
			 	var pageSize="<%=pageSize%>";
			 	var inYear="<%=inYear%>";
			 	var QCBM="<%=QCBM%>";
			 	var productModel="<%=productModel%>";
			 	var productName="<%=productName%>";
			 	var keeper="<%=keeper%>";
			 	if(skipPageNum<=0)
			 		//alert("请输入有效页面");
			 		window.wxc.xcConfirm("请输入有效页面","error");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		//alert("您输入的页面大于总页数");
			 		window.wxc.xcConfirm("您输入的页面大于总页数","error");
			 	else if(!/^(-|\+)?\d+$/.test(skipPageNum))
			 		//alert("输入的 ' "+skipPageNum+" '非法，请重新输入 ！ ");
			 		window.wxc.xcConfirm("输入的 ' "+skipPageNum+" '非法，请重新输入 ！ ","error");
			 	else																				
					window.location.href="EquipmentServlet?operate=equipmentCollective&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&inYear="+inYear+"&QCBM="+QCBM+"&productName="+productName+"&productModel="+productModel+"&keeper="+keeper;		
			}
			//用于实力统计在库年份的判断
		      function judgeInYearId(){
		                var flag=false;
		               var inYearId=document.getElementById('inYearId').value;  
		                      var     newPar=/^(-|\+)?\d+$/ ;        //用来判断是不是整数 
		                   var   b=newPar.test(inYearId);
		             if(b==false) {
		             alert("输入的 ' "+inYearId+" '非法，请重新输入 ！ ");
		             document.getElementById('inYearId').value=""; 
		                flag=false; 
		        } else {//是整数的情况下，判断他是否大于0
							  if(inYearId>0){
							  flag=true;
								  } 
								  else{//若不大于0，则错误
								  alert("输入的 ' "+inYearId+" '非法，请重新输入 ！ ");
		                    document.getElementById('inYearId').value=""; 
		                         flag=false; 
								  }
		    } 
		    return   flag;
		}
		</script>

		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var inYear="<%=inYear%>";
			 	var QCBM="<%=QCBM%>";
			 	var productModel="<%=productModel%>";
			 	var productName="<%=productName%>";
			 	var keeper="<%=keeper%>";
				window.location.href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize="+value+"&inYear="+inYear+"&QCBM="+QCBM+"&productName="+productName+"&productModel="+productModel+"&keeper="+keeper;		
			}
		</script>

		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
			var pageSize="<%=request.getParameter("pageSize")%>";
			if(pageSize=="null")
				document.getElementById("selectPageSize").value="10";
			else
				document.getElementById("selectPageSize").value=pageSize;
		</script>
		
	</body>
	
</html>
