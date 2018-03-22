<%@page import="cn.edu.cqupt.service.qualification_management.InfoService"%>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>器材明细查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/sys_management/systemmanage.css"/>
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script src="js/statistics/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="js/statistics/tools.js"></script>
	<script type="text/javascript" src="js/statistics/statistics.js"></script>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/jdjleft.html" %>
      	<div id="right">
      		<!-- 当前盒子 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=10">实力统计</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="EquipmentServlet?operate=equipmentDetail">器材明细查询</a>
			</div>
			
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
      		<div class="search_box">
      			<form action="EquipmentServlet?operate=equipmentDetail" method="post" onsubmit="return judgeInputTime('inYearId')">
		        	<span style="margin-left:10px;">在库年份</span>
		            <span><input class="setTem" type="text" name="inYear" id="inYearId"/></span>
		            <span>器材代码</span> 
		            <span><input class="setTem" type="text" name="QCBM"/></span>
		            <span>器材名称</span>
		            <span><input class="setTem" type="text" name="productName"/></span>
		            <span>军代室</span>
							<select name="keeper" id="keeper" >
								<option value="all">请选择</option>
								<%
									String ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
									List<String> jdsList= new InfoService().getJDSNameList(ownedUnit);
									for(int i=0;i<jdsList.size();i++){
								 %>
								<option value="<%=jdsList.get(i) %>"><%=jdsList.get(i) %></option>
								<%
								} 
								%>
						</select>
		            <span>
		            	<input class="scan-btn" type="submit" value="查询">
		            	<input type="button" value="导出" class="scan-btn" onclick="equipmentDetailToWord();">
		            	<input type="button" value="全部导出" class="scan-btn" onclick="allEquipmentDetailToWord();">
		            </span>
             	</form>
        	</div>
      		  <div id="equipment_detail-box">
      		  <h1 class="title">通信装备代储维修器材明细表</h1>
                  <table id="equipment_detail">
                   	<thead>
                      <tr>
                          <th>
                          	<input type="checkbox" id="checkboxLeader" onclick="chooseAll();">
                          </th>
                          <th>序号</th>
                          <th>器材代码</th>
                          <th>器材名称</th>
                          <th>生产单位</th>
                          <th>计量单位</th>
                          <th>单价(元)</th>
                          <th>序列号</th>
                          <th>生产时间</th>
                          <th>入库时间</th>
                          <th>质量等级</th>
                          <th>储存期限(月)</th>
                          <th>代储企业</th>
                          <th>备注</th>
                      </tr>
                   </thead>
                    <% 
						   	List<HashMap<String, Object>> T = (List<HashMap<String, Object>>)request.getAttribute("message"); 
						   	//首次进入页面时记得在路径上加上curPageNum=1参数,Servlet跳转是也记得加上curPageNum参数
					int curPageNum=(Integer)request.getAttribute("curPageNum");
					
					//pageSize 按多少条分页
					//首次进入页面时记得在路径上加上pageSize=10参数,Servlet跳转是也记得加上pageSize参数
					int pageSize=(Integer)request.getAttribute("pageSize");
					int orderNumber=(curPageNum-1)*pageSize;
						   	for(int i = 0;i<T.size(); i++) {
				   	%>
                   <tbody>
                       <tr class="contract-add-table-body">
                          <td><input class="setTem" type="checkbox" name="checkbox_strength"/></td>
                          <td><%=i+orderNumber+1 %></td>
                          <td><%=T.get(i).get("QCBM") %></td>
                          <td><%=T.get(i).get("productName") %></td>
                          <td><%=T.get(i).get("manufacturer") %></td>
                          <td><%=T.get(i).get("measureUnit") %></td>
                          <td><%=T.get(i).get("productPrice") %></td>
                          <td></td>
                          <td><%=T.get(i).get("producedDate") %></td>
                          <td><%=T.get(i).get("operateTime") %></td>
                          <td></td>
                          <td><%=T.get(i).get("storageTime") %></td>
                          <td><%=T.get(i).get("keeper") %></td>
                          <td><%=T.get(i).get("remark") %></td>
                       </tr>
                   </tbody>
                   <%} %>
                </table>
              </div>
         </div>
      	 <!-- 分页的盒子 -->
			<div class="page-box">
				<% 
					String inYear = (String)request.getAttribute("inYear");
					String QCBM = (String)request.getAttribute("QCBM");
					String productName = (String)request.getAttribute("productName");
					String keeper=(String)request.getAttribute("keeper");
					
					//后台用 select count(*)... 得到的你查询的全部数据的个数
					int sum=(Integer)request.getAttribute("sum");
					
					//根据总数和每页条数计算分多少页
					int totalPageNum = sum%pageSize==0?sum/pageSize:(sum/pageSize+1);
					
					//没有数据则当前页=1,总页数=1
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
					}
					
					//上一页的页码
					int prePageNum = curPageNum-1<1?1:curPageNum-1;
					
					//下一页的页码
					int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
				<!-- 显示页码的html代码，样式暂时先这样 -->
				<!-- 将curPageNum和pageSize传给你的Servlet，
						用于DAO的SQL语句中limit ?,? 的两个参数赋予 (curPageNum-1)*pageSize,pageSize 两值-->
				<span>
			    	<a href="EquipmentServlet?operate=equipmentDetail&curPageNum=1&pageSize=<%=pageSize %>&productName=<%=productName %>&QCBM=<%=QCBM%>&inYear=<%=inYear%>&keeper=<%=keeper%>">首页</a>
			    	<a href="EquipmentServlet?operate=equipmentDetail&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&productName=<%=productName %>&QCBM=<%=QCBM%>&inYear=<%=inYear%>&keeper=<%=keeper%>" >&lt;&lt;</a>
			   		<span><%=curPageNum %></span>
			   		<a href="EquipmentServlet?operate=equipmentDetail&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&productName=<%=productName %>&QCBM=<%=QCBM%>&inYear=<%=inYear%>&keeper=<%=keeper%>">&gt;&gt;</a>
			    	<a href="EquipmentServlet?operate=equipmentDetail&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&productName=<%=productName %>&QCBM=<%=QCBM%>&inYear=<%=inYear%>&keeper=<%=keeper%>">尾页</a>
			    </span>
			    
			    <!-- 跳转到多少页时 js的skipPage()函数先判断输入的页码是否有效-->
				<!-- 函数体就在下面，把里面的路径改成你的  -->
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick='skipPage()'>确定</a>
		    	
		    	<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
				<!-- 函数体就在下面，把里面的路径改成你的  -->
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
    
      <%@include file="../../../ConstantHTML/jdjfoot.html" %>
      	<!-- 跳转到多少页时 js的skipPage()函数先判断输入的页码是否有效-->
	<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=document.getElementById('skipPageNum').value;
			 	var pageSize="<%=pageSize %>";
			 	var productName="<%=productName%>";
			 	var QCBM="<%=QCBM%>";
			 	var inYear="<%=inYear%>";
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
					window.location.href="EquipmentServlet?operate=equipmentDetail&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&productName="+productName+"&QCBM="+QCBM+"&inYear="+inYear+"&keeper="+keeper;
			}
		</script>

	<!-- 用户选择每页显示的条数后 js执行selectPageSize(this.value)函数，pageSize重新赋值-->
	<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var productName="<%=productName%>";
			 	var QCBM="<%=QCBM%>";
			 	var inYear="<%=inYear%>";
			 	var keeper="<%=keeper%>";
				window.location.href="EquipmentServlet?operate=equipmentDetail&curPageNum=1&pageSize="+value+"&productName="+productName+"&QCBM="+QCBM+"&inYear="+inYear+"&keeper="+keeper;
			}
		</script>
		
		<script type="text/javascript">
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
