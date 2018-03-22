<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Fare"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>经费管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/fare_management/export.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<link rel="stylesheet" href="css/fare_management/fareManagement.css" />
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
</head>


<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/jdsleft.html"%>
	<div id="right">
		<!-- 二级标题 -->
		<div class="subName">
			<span>当前位置：</span> <a href="FareServlet?curPageNum=1&pageSize=10">经费管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;查询经费
		</div>
		<!-- 内容区 -->
		<div id="content">
			<!-- 费用查询信息界面 -->
			<%
				//curPageNum 当前页
						//首次进入页面时记得在路径上加上curPageNum=1参数,Servlet跳转是也记得加上curPageNum参数
						int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
						
						//pageSize 按多少条分页
						//首次进入页面时记得在路径上加上pageSize=10参数,Servlet跳转是也记得加上pageSize参数
						int pageSize=Integer.parseInt(request.getParameter("pageSize"));
						
						//后台用 select count(*)... 得到查询的全部数据的个数
						long sum=(Long)request.getAttribute("checkFareSum");
						
						//根据总数和每页条数计算分多少页
						long totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1); 
						//没有数据则当前页=1,总页数=1
						if(totalPageNum==0){
							totalPageNum=1;
							curPageNum=1;
						}
						//上一页的页码
						long prePageNum = curPageNum-1<1?1:curPageNum-1;
						
						//下一页的页码
						long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
			%>
			<div class="fare-data" id="fare-data">
				<!--    <form action="/6905/FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&operate=check"  method="post">-->
				<form name="checkForm" method="post">
					<div id="search-box">
						<p>
							费用类型： <input type="hidden" name="builtType"> <input
								type="checkbox" id="box" onclick="allSelect()">
							<!-- <label
								for="1">器材购置费</label> <input id="1" name="fareType"
								type="checkbox" value="器材购置费">  -->
							<label for="2">运杂费</label> <input id="2" name="fareType"
								type="checkbox" value="运杂费"> <label for="3">轮换费</label>
							<input id="3" name="fareType" type="checkbox" value="轮换费">
							<label for="6">维护保养费</label> <input id="6" name="fareType"
								type="checkbox" value="维护保养费"> <label for="8">其他</label><input
								id="8" name="fareType" type="checkbox" value="其他"> <input
								class="search-btn" type="button"
								onclick="checkInput('statistics')" value="费用统计"> <input
								class="search-btn" type="button"
								onclick="checkInput('statisticsDetail')" value="明细统计">
						</p>
						<p>
							时间段： <input type="text" id="startDate" class="sang_Calender"
								tabindex="-1" name="startTime" /> 到 <input type="text"
								id="endDate" class="sang_Calender" tabindex="-1" name="endTime" />
							<script type="text/javascript"
								src="ConstantHTML/js/spdateTime.js"></script>
							代储企业： <input type="text" name="storeCompany"> <span
								class="btn"> <input class="search-btn" type="button"
								onclick="checkInput('search')" value="费用信息查询"> <input
								class="add-btn" type="button" value="导入经费"
								onclick="window.location.href='jsp/jds/fare_management/importFare.jsp?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>'">
								<!-- <input class="unable-export-btn" id="export-btn" type="button"
								disabled="disabled" onclick='selectId("export")' value="导出经费"> -->
							</span>
						</p>
					</div>
				</form>
				<div id="fare-table-box">
					<form name="exportForm" method="post">
						<table id="fare-table" class="fare-table">
							<thead>
								<tr>
									<%-- <th class="fare-table-checkbox">
										<input id="allCheckbox"
										type="checkbox" onclick="selectAll()">
									</th> --%>
									<th class="fare-table-num-th">序号</th>
									<th class="fare-table-other-th">费用类型</th>
									<th class="fare-table-other-th">费用金额</th>
									<th class="fare-table-other-th">代储企业</th>
									<th class="fare-table-other-th">军代室</th>
									<th class="fare-table-other-th">记录经费的系统时间</th>
									<th class="fare-table-other-th">备注</th>
									<th class="fare-table-other-th">操作</th>
								</tr>
							</thead>
							<tbody>
								<%
									String startTime=(String)request.getAttribute("startTime");
									                        	String endTime = (String)request.getAttribute("endTime");
									                        	String storeCompany =(String)request.getAttribute("storeCompany");
									                        	String builtType=(String)request.getAttribute("builtType");
										                        String newStr = builtType.replaceAll("'",""); 
													  	ArrayList<Fare> checkFare = new ArrayList<Fare>();
														if(request.getAttribute("check")!=null){
															checkFare=(ArrayList)request.getAttribute("check");
															Fare fare;
															int id=(curPageNum-1)*pageSize;
															for(int i=0;i<checkFare.size();i++){
																fare = checkFare.get(i);
									                        			id++;
								%>
								<tr>
									<%-- <td><input type="checkbox" name="selectFare"
										value=<%=fare.getFareId() %> onclick="ifSelect()">
									</td> --%>
									<td><%=id%></td>
									<td><%=fare.getFareType()%></td>
									<td><%=fare.getFareAmount()%></td>
									<td><%=fare.getStoreCompany()%></td>
									<td><%=fare.getJdRoom()%></td>
									<td><%=fare.getOperateDate()%></td>
									<td><%=fare.getRemark()%></td>
									<td><input type="button" value="查看明细"
										onclick="window.location.href='jsp/qy/fare_management/checkFareDetail.jsp?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=newStr%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&fareId=<%=fare.getFareId()%>'">
										<!-- 
			                        	<input type="button" value="删除" onclick='{if(confirm("确定删除吗?")){window.location="/6905/FareServlet?operate=delete&curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=newStr%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&fareId=<%=fare.getFareId()%> ";alert("删除成功！");return true;}alert("删除失败！");return false;}' >
			                        --> <input type="button" value="附件"
										onclick="window.location.href='FareServlet?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>&builtType=<%=newStr%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=attach&fareId=<%=fare.getFareId()%>'">
									</td>
								</tr>
								<%
									} 
													    String ids=(String)request.getAttribute("idStr");
										                        out=pageContext.getOut(); 
													    out.write("<input id=\"St111\" type=\"hidden\"  name=\"St1\" value="+ids+">");
													    }
								%>
							</tbody>
						</table>
					</form>
					<form id="idForm" name="idForm" method="post">
						<input type="hidden" id="idStr" name="idStr1" value=""> <input
							type="hidden" id="noSelect" name="noSelect1" value=""> <input
							type="hidden" id="thisSelect" name="thisSelect1" value="">
					</form>
				</div>
				<!-- 分页的盒子 -->
				<div class="page-box">
					<!-- 将curPageNum和pageSize传给你的Servlet，
						用于DAO的SQL语句中limit ?,? 的两个参数赋予 (curPageNum-1)*pageSize,pageSize 两值-->
					<span> <a onclick="selectId('first');">首页</a> <a
						onclick="selectId('previous');">&lt;&lt;</a> <span><%=curPageNum%></span>
						<a onclick="selectId('next');">&gt;&gt;</a> <a
						onclick="selectId('end');">尾页</a> </span>
					<!-- 跳转到多少页时 js的skipPage()函数先判断输入的页码是否有效-->
					<span>跳到第</span> <input type="text" id="skipPageNum" /> <span>页</span>
					<a onclick="selectId('jump')">确定</a>
					<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
					<span>每页显示</span> <select name="selectPageSize"
						onchange='selectId("changeSize")' id="selectPageSize">
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
					</select> <span>条记录，共</span> <label><%=totalPageNum%></label> <span>页</span>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/jdsfoot.html"%>
	<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
	<!-- 点击查询时，先判断输入框是否合法-->
	<script type="text/javascript">
//用的是switch
function selectId(text){
	 var re="";
	 var noC="";//定义的没有被选择的checkbox
	 var str=document.getElementById("idStr").value;
	 var checkbox = document.getElementsByName("selectFare");
	 var flag=true;//
	 for(var i=0;i<checkbox.length;i++){//判断整个页面有没有被选中的选项
    	if(checkbox[i].checked){
	    	re=re+checkbox[i].value+";";
    	}else{
    		noC=noC+checkbox[i].value+";";
    		} 
    	}
     document.getElementById("noSelect").value=noC;
	 document.getElementById("idStr").value=str+re;//将本次选择的和上次选择的加到一起
	 switch(text){ 
	 case "first":  //如果是首页执行里面的action 
	 	document.forms.idForm.action="FareServlet?&operate=check&curPageNum=1&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%> ";
	  	document.forms.idForm.submit();
	  	break;
	  
	 case "previous":  //如果是上一页执行里面的action
	  	document.forms.idForm.action="FareServlet?curPageNum=<%=prePageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=check ";
	  	document.forms.idForm.submit();
	  	break;
	  
	 case "next": //如果是下一页执行里面的action
	 	document.forms.idForm.action="FareServlet?curPageNum=<%=nextPageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=check ";
	  	document.forms.idForm.submit();
	   	break;
	   
	 case "end": // 最后一页
	 	document.forms.idForm.action="FareServlet?curPageNum=<%=totalPageNum%>&pageSize=<%=pageSize%>&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&storeCompany=<%=storeCompany%>&operate=check" ;
	   	document.forms.idForm.submit();
	   	break;
	   
	 case "jump"://跳页 
	 	var skipPageNum=Number(document.getElementById('skipPageNum').value);
	 	var pageSize=<%=pageSize%>;
	 	if(isNaN(skipPageNum)||skipPageNum<=0){
	 		window.wxc.xcConfirm("请输入有效页面","warning");
			return;
		}
		else if(skipPageNum><%=totalPageNum%>){
			window.wxc.xcConfirm("您输入的页面大于总页数","warning");
			return;
		}
		document.forms.idForm.action="FareServlet?operate=check&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
	 	document.forms.idForm.submit();
	 	break;
	 	
	case "changeSize":// 改变每页的大小 <!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
	    var pageSize=document.getElementById('selectPageSize').value;
		document.forms.idForm.action="FareServlet?curPageNum=1&operate=check&builtType=<%=builtType%>&startTime=<%=startTime%>&endTime=<%=endTime%>&pageSize="+pageSize ;
	   	document.forms.idForm.submit();
	    break;
	    
	case "export":
	      var allCheckbox= document.getElementById("allCheckbox");//表头的总复选框
		  var theads=allCheckbox.parentNode.parentNode.getElementsByTagName("th");
		  var excelArray=new Array();
          for(var i=0;i<theads.length;i++){
        		excelArray[i]=theads[i].innerHTML;
          }
          document.forms.idForm.action="FareServlet?operate=export&excelHead="+excelArray; 
		  document.forms.idForm.submit();
		  break;
	} 
}

		//检查输入的条件是否合适,（离不开这个页面）
		function checkInput(text){
				var startDate=document.getElementById("startDate").value;
				var endDate=document.getElementById("endDate").value;
			    if(startDate!=""&&endDate==""){
			    	window.wxc.xcConfirm("结束时间段不可为空","warning");
					return;
				}
				if(startDate==""&&endDate!=""){
					window.wxc.xcConfirm("起始时间段不可为空","warning");
					return;
				}
				if(startDate>endDate){
					window.wxc.xcConfirm("起始时间大于结束时间","warning");
					return;
				}
				switch (text) {
				case "search":
					document.forms.checkForm.action="FareServlet?&operate=check&curPageNum=1&pageSize="+<%=pageSize%>;  
					break;
				case "statistics":
					document.forms.checkForm.action="FareServlet?&operate=statistics&curPageNum=1&pageSize="+<%=pageSize%>;   
					break;
				case "statisticsDetail":
					document.forms.checkForm.action="FareServlet?&operate=statisticsDetail&curPageNum=1&pageSize=<%=pageSize%>";
					break;
				default:
					break;
				}
				document.forms.checkForm.submit();
	    	}  
	    </script>
	<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
	<script type="text/javascript">
		var pageSize="<%=request.getParameter("pageSize")%>";
		if (pageSize == "null")
			document.getElementById("selectPageSize").value = "10";
		else
			document.getElementById("selectPageSize").value = pageSize;
	</script>
</body>
</html>