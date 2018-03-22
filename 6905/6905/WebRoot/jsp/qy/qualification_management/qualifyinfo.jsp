<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.Qualify"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>资质文件信息</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/crossPageCheck.js"></script>
		<script type="text/javascript" src="js/qualification_management/qualification_management.js"></script>
		<script type="text/javascript" src="ConstantHTML/js/changeTrColor.js"></script>
		<script src="ConstantHTML/js/xcConfirm.js"></script>
		<link rel="stylesheet" type="text/css"
			href="ConstantHTML/css/homepage.css">
		<link rel="stylesheet" type="text/css"
			href="css/qualification_management/qualifymanagement.css">
		<link href="ConstantHTML/css/xcConfirm.css" rel="stylesheet">
		<script type="text/javascript">
			var deleteFlag=<%=request.getAttribute("deleteFlag")%>;
			if(deleteFlag==1)
				window.wxc.xcConfirm("删除成功","success");
			else if(deleteFlag==0)
				window.wxc.xcConfirm("删除失败","error");
		</script>
		<script type="text/javascript">
			var addFlag=<%=request.getAttribute("addFlag")%>;
			var repeatFlag=<%=request.getAttribute("repeatFlag")%>;
			var str="";
			if(repeatFlag==1)
				str+="不允许重复提交\n";
			if(addFlag==1)
				str+="添加成功";
			else if(addFlag==0)
				str+="添加失败";
			if(str!="")
				window.wxc.xcConfirm(str,"info");
		</script>
		<script type="text/javascript">
			var updateFlag=<%=request.getAttribute("updateFlag")%>;
			if(updateFlag==1)
				window.wxc.xcConfirm("修改成功","success");
			else if(updateFlag==0)
				window.wxc.xcConfirm("修改失败","error");
		</script>
	</head>

	<body>
		<%@include file="../../../ConstantHTML/top.html"%>
		<%@include file="../../../ConstantHTML/left.html"%>
		<div id="right">
			<!-- 当前位置 -->
			<div class="subName">
				<span>当前位置：</span>
      			资质管理
			</div>

			<!-- 内容区 -->
				<% 
					//operate 全部查询=all，条件查询=searchQualify
					String operate=(String)request.getParameter("operate");
					String searchStr="";
					String searchType="";
					String year="";
					String searchAttr="";
					if(operate.equals("searchQualify")){
						searchStr=(String)request.getParameter("searchStr");
						searchType=(String)request.getParameter("searchType");
						year=(String)request.getParameter("year");
						searchAttr=(String)request.getParameter("searchAttr");
					}
					//curPageNum 当前页
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					//pageSize 按多少条分页
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					//userSum 全部用户的个数
					long qualifySum=(Long)request.getAttribute("qualifySum");
					//根据总数和每页条数计算分多少页
					long totalPageNum = qualifySum%pageSize==0?qualifySum/pageSize:(qualifySum/pageSize+1);
					if(totalPageNum==0){
						totalPageNum=1;
						curPageNum=1;
					}
					//上一页的页码
					long prePageNum = curPageNum-1<1?1:curPageNum-1;
					//下一页的页码
					long nextPageNum = curPageNum+1>totalPageNum?totalPageNum:curPageNum+1;
				%>
			<div class="content">
					<div class="qualifyinfo-op">
						<form action="QualifyServlet?operate=searchQualify&curPageNum=1&pageSize=10" method="post">
							文件名：<input type="text" name="searchStr" value="请输入查询的文件名" onclick="if(this.value=='请输入查询的文件名'){this.value='';}" onblur="if(this.value=='') {this.value='请输入查询的文件名';}">
							年份：<input type="text" name="year" value="请输入查询的年份" onclick="if(this.value=='请输入查询的年份'){this.value='';}" onblur="if(this.value=='') {this.value='请输入查询的年份';}">
							文件属性：<select name="searchAttr" id="searchAttr">
								<option value="所有">
								所有
								</option>
								<option value="自查">
									自查
								</option>
								<option value="上报">
									上报
								</option>
							</select>
							文件类型：<select name="searchType" id="searchType">
								<option value="所有类型">
									所有类型
								</option>
								<option value="资质申报文件">
									资质申报文件
								</option>
								<option value="资质审查报告">
									资质审查报告
								</option>
								<option value="代储合同">
									代储合同
								</option>
							</select>
							<input type="submit" value="查询" class="search-btn">
							<input type="button" value="导出资质文档" class="add-btn" style="margin-left: 50px;" onclick="exportChooseQualify()">
							<input type="button" value="上传资质文档" class="add-btn"
								onclick="window.location.href = 'jsp/qy/qualification_management/addqualify.jsp?curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>'">
						</form>
					</div>
				<!-- 资质文件信息table开始 -->
				<div class="qualification-info">
					<table class="qualification-table">
						<thead>
							<tr class="qualification-table-head">
								<td>
									<input type="checkbox" onclick="chooseAll();" id="checkboxhead">
								</td>
								<td>
									文件编号
								</td>
								<td style="width: 600px;">
									文件名
								</td>
								<td>
									文件类型
								</td>
								<td>
									年份
								</td>
								<td>
									文件属性
								</td>
								<td>
									操作
								</td>
							</tr>
						</thead>
						<tbody class="qualification-table-body">
							<% 
								ArrayList<Qualify> qualifyList=new ArrayList<Qualify>();
								qualifyList=(ArrayList<Qualify>)request.getAttribute("curQualifyList");
								long qualifySize=qualifyList.size();
								int id=(curPageNum-1)*pageSize;
								for(int i=0;i<qualifySize;i++){
									id++;
						 	%>
							<tr>
								<td>
									<input type="checkbox" name="checkboxbody" value="<%=qualifyList.get(i).getQualifyId() %>">
								</td>
								<td><%=id%></td>
								<td style="width: 600px;"><%=qualifyList.get(i).getQualifyTitle() %></td>
								<td><%=qualifyList.get(i).getQualifyType() %></td>
								<td><%=qualifyList.get(i).getYear() %></td>
								<td><%=qualifyList.get(i).getQualifyAttr() %></td>
								<td>
									<input type="button" name="download" value="下载" onclick="downloadQualify(<%=qualifyList.get(i).getQualifyId() %>)">
									<input type="button" name="delete" value="删除" onclick="deleteQualify(<%=qualifyList.get(i).getQualifyId() %>)">
									<input type="button" name="update" value="修改" onclick="updateQualify(this,<%=qualifyList.get(i).getQualifyId() %>)">
								</td>
							</tr>
							<%
							}
						 	%>
						</tbody>
					</table>
				</div>
			</div>
			<form method="post" id="checkedIdForm">
				<input type="hidden" id="checkedIdStr" name="checkedIdStr" value="<%=request.getAttribute("checkedIdStr") %>">
				<input type="hidden" id="unCheckedIdStr" name="unCheckedIdStr" value="">
			</form>
			
			<!-- 分页的盒子 -->
			<div class="page-box">
			    <span>
			    <a onclick="turnToPageWithCheckedbox('QualifyServlet?operate=<%=operate %>&curPageNum=1&pageSize=<%=pageSize %>&searchType=<%=searchType %>&searchStr=<%=searchStr %>&year=<%=year %>&searchAttr=<%=searchAttr %>','checkboxbody')">首页</a>
			    <a onclick="turnToPageWithCheckedbox('QualifyServlet?operate=<%=operate %>&curPageNum=<%=prePageNum %>&pageSize=<%=pageSize %>&searchType=<%=searchType %>&searchStr=<%=searchStr %>&year=<%=year %>&searchAttr=<%=searchAttr %>','checkboxbody')" >&lt;&lt;</a>
			   	<span><%=curPageNum %></span>
			    <a onclick="turnToPageWithCheckedbox('QualifyServlet?operate=<%=operate %>&curPageNum=<%=nextPageNum %>&pageSize=<%=pageSize %>&searchType=<%=searchType %>&searchStr=<%=searchStr %>&year=<%=year %>&searchAttr=<%=searchAttr %>','checkboxbody')">&gt;&gt;</a>
			    <a onclick="turnToPageWithCheckedbox('QualifyServlet?operate=<%=operate %>&curPageNum=<%=totalPageNum %>&pageSize=<%=pageSize %>&searchType=<%=searchType %>&searchStr=<%=searchStr %>&year=<%=year %>&searchAttr=<%=searchAttr %>','checkboxbody')">尾页</a>
			    </span>
		    	<span>跳到第</span>
		    	<input type="text" id="skipPageNum"/>
		    	<span>页</span>
		    	<a onclick="turnToPageWithCheckedbox(skipPage(),'checkboxbody')" >确定</a>
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
		<%@include file="../../../ConstantHTML/foot.html"%>
		<!-- 页面跳转时判定 -->
		<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=document.getElementById('skipPageNum').value;
			 	var pageSize=<%=pageSize %>;
			 	var operate="<%=operate %>";
			 	var searchStr="<%=searchStr %>";
			 	var searchType="<%=searchType %>";
			 	var year="<%=year%>";
			 	var searchAttr="<%=searchAttr%>";
			 	if(skipPageNum<=0){
			 		alert("请输入有效页面");
			 		return window.location.href;
			 	}
			 	else if(skipPageNum><%=totalPageNum%>){
			 		alert("您输入的页面大于总页数");
			 		return window.location.href;
			 	}
			 	else{
			 		return "QualifyServlet?operate=all&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&searchStr="+searchStr+"&searchType="+searchType+"&year="+year+"&searchAttr="+searchAttr;
					//window.location.href="QualifyServlet?operate=all&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&searchStr="+searchStr+"&searchType="+searchType;
				}
			}
		</script>
		
		<!-- 用户选择每页显示的条数后提交到Servlet -->
		<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var searchStr="<%=searchStr %>";
			 	var searchType="<%=searchType %>";
			 	var operate="<%=operate %>";
				window.location.href="QualifyServlet?operate="+operate+"&curPageNum=1&pageSize="+pageSize+"&searchStr="+searchStr+"&searchType="+searchType;
			}
		</script>
		
		<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
		<script type="text/javascript">
				var searchType="<%=request.getParameter("searchType") %>";
				var pageSize="<%=request.getParameter("pageSize")%>";
				var searchAttr="<%=request.getParameter("searchAttr")%>";
				if(searchType=="null"||searchType=="")
					document.getElementById("searchType").value="所有类型";
				else
					document.getElementById("searchType").value=searchType;
				if(searchAttr=="null"||searchAttr=="")
					document.getElementById("searchAttr").value="所有";
				else
					document.getElementById("searchAttr").value=searchAttr;
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>

		<!-- 点击删除按钮的函数 -->
		<script type="text/javascript">
			function deleteQualify(qualifyId) {
				window.wxc.xcConfirm("确认删除当前资质文件？","confirm",{onOk:function(){
				var curPageNum="<%=curPageNum%>";
					var pageSize="<%=pageSize%>";
					window.location.href="QualifyServlet?operate=deleteQualify&curPageNum="+curPageNum+"&pageSize="+pageSize+"&qualifyId="+qualifyId;
				}});
			}
		</script>
		
		<!-- 点击修改按钮的函数 -->
		<script type="text/javascript">
			function updateQualify(ev,qualifyId){
				var oCurrentTd=ev.parentNode.parentNode.getElementsByTagName('td');
				var curPageNum=<%=curPageNum%>;
				var pageSize=<%=pageSize%>;
				var qualifyTitle=oCurrentTd[2].innerHTML,
					qualifyType=oCurrentTd[3].innerHTML,
					year=oCurrentTd[4].innerHTML,
					qualifyAttr=oCurrentTd[5].innerHTML;
				window.location.href="jsp/qy/qualification_management/updatequalify.jsp?qualifyId="+qualifyId+"&qualifyTitle="+qualifyTitle+"&year="+year+"&qualifyAttr="+qualifyAttr+"&qualifyType="+qualifyType+"&curPageNum="+curPageNum+"&pageSize="+pageSize;
			}
		</script>
		
		<!-- 显示是否选择了当前页面的checkbox -->
		<script type="text/javascript">
			var checkedIdFlagStr="<%=request.getAttribute("checkedIdFlagStr")%>",
				checkedIdFlagArray=[],
				oCheckboxBody=document.getElementsByName("checkboxbody");
			if(checkedIdFlagStr!="null"&&checkedIdFlagStr!=""){
				checkedIdFlagArray=checkedIdFlagStr.split(",");
			}
			for (var i = 0; i < checkedIdFlagArray.length; i++) {
				if(checkedIdFlagArray[i]==1){
					oCheckboxBody[i].checked=true;
				}
			}
		</script>
		
		<script type="text/javascript">
			var searchFlag=<%=request.getAttribute("searchFlag")%>;
			if(searchFlag==0){
				var txt="没有资质文件或未查询到符合条件的资质文件";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
			}
		</script>
	</body>
</html>
