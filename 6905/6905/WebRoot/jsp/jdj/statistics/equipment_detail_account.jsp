<%@page import="cn.edu.cqupt.service.qualification_management.InfoService"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>器材明细账统计</title>
    
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
	<script type="text/javascript" src="js/statistics/statistics.js"></script>
	<script type="text/javascript" src="js/statistics/tools.js"></script>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/jdjleft.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="EquipmentServlet?operate=equipmentCollective&curPageNum=1&pageSize=10">实力统计</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="EquipmentServlet?operate=equipmentDetailAccount">器材明细账统计</a>
			</div>
      		
      		<!-- 内容区 -->
      		<div class="fare-data" id="fare-data">
      		<div class="search_box">
      		<form action="EquipmentServlet?operate=equipmentDetailAccount" method="post" onsubmit="return judgeInputTime('inYearId')">
        		<span style="margin-left:10px;">在库年份</span> 
                <span><input class="setTem" type="text" name="inYear" id="inYearId"/></span>
                <span>器材代码</span> 
                <span><input class="setTem" type="text" name="QCBM"/></span>
                <span>产品名称</span>
				<span><input class="setTem" type="text" name="productName" /></span>
				<span>产品型号</span>
				<span><input class="setTem" type="text" name="productModel" />
				</span>
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
                	<input type="button" value="导出" class="scan-btn" onclick="equipmentDetailAccountToWord();">
                </span>
             </form>
        </div>
       <div id="whole"></div>
				<div id="mingxitable">
					<table class="tem-table_mingxizhang" id="tem-table_mingxizhang">
						<thead>
							<tr>
								<td>
									年
								</td>
								<td>
									月
								</td>
								<td>
									日
								</td>
								<td>
									料单编号
								</td>
								<td>
									结存数量
								</td>
								<td>
									厂(所)或公司名称</td>
						                 <td>收入</td>
										<td>发出</td>
										<td>结存</td>
								<td>
									备注
								</td>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
					<input type="button" value="确定" class="sure_detail-btn"
						id="sure_detail_account">
				</div>
         <div id="equipment_detail_account-box">
         	<h1 class="title">通信装备代储维修器材明细账</h1>
                 <table  id="equipment_detail_account">
                   <thead>
                      <tr>
                          <th>
                          		<input type="checkbox" id="checkboxLeader" onclick="chooseAll();">
                          </th>
                          <th>序号</th>
                          <th>名称型号</th>
                          <th>包装件数</th>
                          <th>总体积(m3)</th>
                          <th>总重量</th>
                          <th>生产厂家</th>
                          <th>单价(万元)</th>
                          <th>器材代码</th>
                          <th>包装说明</th>
                          <th>配套说明</th>
                          <th>NO.</th>
                          <th>明细账</th>
                      </tr>
                   </thead>
                   <%
                     List<HashMap<String,Object>> T=new ArrayList<HashMap<String,Object>>();
                     T=(List<HashMap<String, Object>>)request.getAttribute("T");
                     int curPageNum=(Integer)request.getAttribute("curPageNum"); 
						//pageSize 按多少条分页
						int pageSize=(Integer)request.getAttribute("pageSize");
						int pageNumber=(curPageNum-1)*pageSize;
                     for(int i = 0;i<T.size(); i++) {
                    %>
                   <tbody>
                       <tr class="contract-add-table-body">
                          <td><input class="setTem" type="checkbox" name="checkbox_strength"/></td>
                          <td><%=i+pageNumber+1 %></td>
                          <td><%=T.get(i).get("productName").toString() %>+<%=T.get(i).get("productModel").toString() %></td>
                          <td><%=T.get(i).get("BZJS").toString() %></td>
                          <td><%=T.get(i).get("BZTJ").toString() %></td>
                          <td><%=T.get(i).get("BZZL").toString() %></td>
                          <td><%=T.get(i).get("manufacturer").toString() %></td>
                          <td><%=T.get(i).get("productPrice").toString() %></td>
                          <td><%=T.get(i).get("QCBM").toString() %></td>
                          <td><input type="text" ></td>
                          <td><input type="text" ></td>
                          <td><input type="text" ></td>
                          <td><input type="button" value="明细账" name="operate_detail"onclick="out_EquipmentDetailAccount(this)"></td>
                       </tr>
                </tbody>
                <%} %>
    			</table>
			</div>
		</div>
       <!-- 分页的盒子 -->
			<div class="page-box">
				<% 
					String inYear=(String)request.getAttribute("inYear");
					String QCBM=(String)request.getAttribute("QCBM");
					String productModel=(String)request.getAttribute("productModel");
		            String productName=(String)request.getAttribute("productName");
		            String keeper=(String)request.getAttribute("keeper");
					//equipment_StatisticSum 全部按器材明细统计结果的个数
					int equipment_StatisticSum=(Integer)request.getAttribute("sum");
					//根据总数和每页条数计算分多少页
					int totalPageNum = equipment_StatisticSum%pageSize==0?equipment_StatisticSum/pageSize:(equipment_StatisticSum/pageSize+1);
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
						}
					//上一页的页码
					int prePageNum = curPageNum-1<1?1:curPageNum-1;
					//下一页的页码
					int nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
			    <span>
			    <a href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum=1&pageSize=<%=pageSize%>&inYear=<%=inYear %>&QCBM=<%=QCBM%>&productName=<%=productName%>&productName=<%=productName%>&keeper=<%=keeper%>">首页</a>
			    <a href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear %>&QCBM=<%=QCBM%>&productName=<%=productName%>&productName=<%=productName%>&keeper=<%=keeper%>">&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear %>&QCBM=<%=QCBM%>&productName=<%=productName%>&productName=<%=productName%>&keeper=<%=keeper%>">&gt;&gt;</a>
			    <a href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&inYear=<%=inYear %>&QCBM=<%=QCBM%>&productName=<%=productName%>&productName=<%=productName%>&keeper=<%=keeper%>">尾页</a>
			    </span>
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
      <%@include file="../../../ConstantHTML/jdjfoot.html" %>
      	<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=document.getElementById('skipPageNum').value;
			 	var pageSize="<%=pageSize %>";
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
					window.location.href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&inYear="+inYear+"&QCBM="+QCBM+"&productModel="+productModel+"&productName="+productName+"&keeper="+keeper;
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
				window.location.href="EquipmentServlet?operate=equipmentDetailAccount&curPageNum=1&pageSize="+value+"&inYear="+inYear+"&QCBM="+QCBM+"&productModel="+productModel+"&productName="+productName+"&keeper="+keeper;
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
