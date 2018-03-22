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
    
    <title>查询合同</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" type="text/css" href="css/transact_business/transact.css" />
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
    <script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
    <script src="ConstantHTML/js/xcConfirm.js"></script>
	<script src="js/transact_business/transact.js"></script>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
		<!-- 二级标题 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="ContractHandleServlet?operate=addcontract">业务办理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			查询合同
			</div>
       <div id="content">
       <div class="search-box" >
            	<form method="post" action="ContractHandleServlet?operate=querycontract">
					<span>合同号</span> 
	                <span><input class="setTem" name="contractid" type="text"/></span>
	                <span>产品型号</span> 
	                <span><input class="setTem" name="productmodel" type="text"/></span>
	                <span>单元名称</span> 
	                <span><input class="setTem" name="unitname" type="text"/></span>
					<span>签订日期</span> 
	                	<input class="sang_Calender" class="setTem" name="signdate" type="text"/>
	                	<script  src="ConstantHTML/js/spdateTime.js"></script>
	                <span><input class="search-btn" type="submit" value="查询合同" id="rukushenqing"></span>
				</form>
				<br>
				<label>当前页合同总金额：</label><%=request.getAttribute("curPageContractPrice") %>
				&nbsp;&nbsp;<label>合同总金额：</label><%=request.getAttribute("contractPriceSum") %>
         </div>
       	     <!--合同查询 -->
            <div id="contract-table-box">
				   <table id="contract-table">
				   <thead>
						<tr>
    						<td>合同编号</td>
    						<td>订购数量</td>
    						<td>合同金额</td>
    						<td>军代室</td>
    						<td>签订日期</td>
    						<td>采购单位</td>
    						<td>附件</td>
    						<td>操作</td>
    					</tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${contracts }" var="contract">
					<tr>
    					<td>${contract.contractId }</td>
    					<td><span>${contract.totalNumber }</span></td>
    					<td><span>${contract.contractPrice }</span></td>
    					<td><span>${contract.JDS }</span></td>
    					<td><span>${contract.signDate }</span></td>
    					<td><span>${contract.buyer }</span></td>
    					<td>
    					<c:if test="${contract.attachment !=null}">
    						<input type="button" onclick="window.open('jsp/qy/transact_business/pdfReader.jsp?path=${contract.attachment}')" value="查看"/> 
    						<input type="button" onclick="deleteAttach(this);" value="删除"/>
    					</c:if>
    					<c:if test="${contract.attachment ==null}">
    						<input type="button" value="上传" onclick="window.location.href='ContractHandleServlet?operate=gotoUpload&contractid=${contract.contractId}'">
    					</c:if>
    					</td>
    					<td style=" width:350px;">
    						<input type="button" value="增加产品" onclick="window.location.href='ProductHandleServlet?operate=gotoproduct&contractid=${contract.contractId }'"/>
    						<input type="button" value="编辑合同" onclick="window.location.href='ContractHandleServlet?operate=updatecontract&contractId=${contract.contractId }'">
    						<input type="button" onclick="delContract(this);" value="删除合同">
    						<input type="button" value="产品到库" onclick="window.location.href='ProductHandleServlet?operate=queryproduct&contractid=${contract.contractId }'">
    						<input type="button" value="申请入库" onclick="window.location.href='AddInApplyServlet?operate=goToInApply&contractid=${contract.contractId }'">
    					</td>
                  	</c:forEach>
                   </tbody>
                </table>
            </div>
           <div class="page-box">
				<% 
					//得到当前查询条件
					Map<String,String> condition = (Map<String,String>)request.getAttribute("condition");
					request.setAttribute("condition", condition);
					//curPageNum 当前页
					int curPageNum=(Integer)request.getAttribute("curPageNum");
					//pageSize 按多少条分页
					int pageSize=(Integer)request.getAttribute("pageSize");
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
			    <span>
			    <a href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum=1&pageSize=<%=pageSize %>" >首页</a>
			    <a href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>">&gt;&gt;</a>
			    <a href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>">尾页</a>
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
		</div>
		<%@include file="../../../ConstantHTML/foot.html" %>
		<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
       	<!-- 跳转页面的有效性判断和跳转 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=eval(document.getElementById('skipPageNum')).value;
			 	var pageSize=<%=pageSize %>;
			 	if(skipPageNum<=0)
			 		alert("请输入有效页面");
			 	else if(skipPageNum><%=totalPageNum%>)
			 		alert("您输入的页面大于总页数");
			 	else
					window.location.href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum="+skipPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				window.location.href="ContractHandleServlet?operate=querycontract&contractid=<%=condition.get("c.contractId") %>&productmodel=<%=condition.get("productModel") %>&unitname=<%=condition.get("productUnit") %>&signdate=<%=condition.get("signDate") %>&curPageNum=1&pageSize="+value;
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
  </body>
</html>
