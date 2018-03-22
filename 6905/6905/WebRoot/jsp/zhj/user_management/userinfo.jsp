<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>用户信息列表</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="ConstantHTML/js/homepage.js"></script>
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="js/user_management/user_management.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="css/user_management/usermanageindex.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
</head>
<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 当前位置-->
		<div class="subName">
			<span>当前位置：</span> <a href="UserServlet?operation=peruserinfo">用户管理</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			<a
				href="UserServlet?operation=manager&subOperation=searchAllUser&curPageNum=1&pageSize=10">管理员用户</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
			用户管理
		</div>

		<!-- 内容区 -->
		<%
			//subOperation 全部查询(searchAllUser)还是模糊查询(searchLikeUser)，因为查询结果是返回的是一个页面，所以要判定
					String subOperation=(String)request.getParameter("subOperation");
					String searchStr="";
					String searchType="";
					if(subOperation.equals("searchLikeUser")){
						searchStr=(String)request.getParameter("searchStr");
						searchType=(String)request.getParameter("searchType");
					}
					//curPageNum 当前页
					int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
					//pageSize 按多少条分页
					int pageSize=Integer.parseInt(request.getParameter("pageSize"));
					//userSum 全部用户的个数
					long userSum=(Long)request.getAttribute("userSum");
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
		<div class="content">
			<div class="toptitle">
				<div class="userinfo-op">
					<form
						action="UserServlet?operation=manager&subOperation=searchLikeUser&curPageNum=1&pageSize=10"
						method="post">
						<select name="searchType" id="searchType">
							<option value="role">角色</option>
							<option value="name">姓名</option>
						</select> <input type="text" name="searchStr" id="searchStr"> <input
							type="submit" value="查询" class="search-btn"> <input
							type="button" value="添加用户" class="add-btn"
							onclick="window.location.href='jsp/zhj/user_management/adduser.jsp?curPageNum=<%=curPageNum%>&pageSize=<%=pageSize%>'">
					</form>
				</div>
			</div>
		</div>
		<div class="manage-users-info">
			<table class="user-table">
				<thead>
					<tr class="user-table-head">
						<td>编号</td>
						<td>账号</td>
						<td>姓名</td>
						<td>角色</td>
						<td>职责</td>
						<td>所属单位</td>
						<td>所属职权单位</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody class="user-table-body">
					<%
						ArrayList<User> curUserList=(ArrayList<User>)request.getAttribute("curUserList");
									 	int listSize=curUserList.size();
									 	int id=(curPageNum-1)*pageSize;
									 	for(int i=0;i<listSize;i++){
									 		id++;
					%>
					<tr>
						<td><%=id%></td>
						<td><%=curUserList.get(i).getIdentifyNum()%></td>
						<td><%=curUserList.get(i).getName()%></td>
						<td><%=curUserList.get(i).getRole()%></td>
						<td><%=curUserList.get(i).getDuty()%></td>
						<td><%=curUserList.get(i).getOwnedUnit()%></td>
						<td><%=curUserList.get(i).getAuthorityUnit()%></td>
						<td><input type="button" value="修改"
							onclick="updateUser(this)"> <input type="button"
							value="删除" onclick="deleteUser(this)">
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
		</div>
		<!-- 分页的盒子 -->
		<div class="page-box">
			<span> <a
				href="UserServlet?operation=manager&curPageNum=1&pageSize=<%=pageSize%>&subOperation=<%=subOperation%>&searchType=<%=searchType%>&searchStr=<%=searchStr%>">首页</a>
				<a
				href="UserServlet?operation=manager&curPageNum=<%=prePageNum%>&pageSize=<%=pageSize%>&subOperation=<%=subOperation%>&searchType=<%=searchType%>&searchStr=<%=searchStr%>">&lt;&lt;</a>
				<span><%=curPageNum%></span> <a
				href="UserServlet?operation=manager&curPageNum=<%=nextPageNum%>&pageSize=<%=pageSize%>&subOperation=<%=subOperation%>&searchType=<%=searchType%>&searchStr=<%=searchStr%>">&gt;&gt;</a>
				<a
				href="UserServlet?operation=manager&curPageNum=<%=totalPageNum%>&pageSize=<%=pageSize%>&subOperation=<%=subOperation%>&searchType=<%=searchType%>&searchStr=<%=searchStr%>">尾页</a>
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
		if(repeatFlag==1){
			window.wxc.xcConfirm("请勿重复提交","warning");
		}
		if(addFlag==1){
			window.wxc.xcConfirm("添加成功","success");
		}
		else if(addFlag==0){
			window.wxc.xcConfirm("添加失败","error");
		}
	</script>
	<script type="text/javascript">
		var updateFlag=<%=request.getAttribute("updateFlag")%>;
		if(updateFlag==1)
			window.wxc.xcConfirm("修改成功","success");
		else if(updateFlag==0)
			window.wxc.xcConfirm("修改失败","error");
	</script>

	<!-- 跳转页面的有效性判断和跳转 -->
	<script type="text/javascript">
			function skipPage(){
			 	var skipPageNum=Number(document.getElementById('skipPageNum').value);
			 	var pageSize="<%=pageSize%>";
			 	var subOperation="<%=subOperation%>";
			 	var searchStr="<%=searchStr%>";
			 	var searchType="<%=searchType%>";
			 	if(isNaN(skipPageNum)||skipPageNum<=0){
			 		window.wxc.xcConfirm("请输入有效页面","warning");
			 		return;
			 	}
			 	else if(skipPageNum><%=totalPageNum%>){
			 		window.wxc.xcConfirm("您输入的页面大于总页数","warning");
			 		return;
			 	}
			 	else
					window.location.href="UserServlet?operation=manager&curPageNum="+skipPageNum+"&pageSize="+pageSize+"&subOperation="+subOperation+"&searchStr="+searchStr+"&searchType="+searchType;
			}
		</script>

	<!-- 用户选择每页显示的条数后提交到Servlet -->
	<script type="text/javascript">
			function selectPageSize(value){
				var pageSize=value;
				var searchStr="<%=searchStr%>";
			 	var searchType="<%=searchType%>";
			 	var subOperation="<%=subOperation%>";
				window.location.href="UserServlet?operation=manager&curPageNum=1&pageSize="+pageSize+"&subOperation="+subOperation+"&searchStr="+searchStr+"&searchType="+searchType;
			}
		</script>

	<!-- 控制下拉条在页面跳转后保持和原来选中的一致 -->
	<script type="text/javascript">
				var searchType="<%=request.getParameter("searchType")%>";
				var pageSize="<%=request.getParameter("pageSize")%>";
				if(searchType=="null"||searchType=="")
					document.getElementById("searchType").value="role";
				else
					document.getElementById("searchType").value=searchType;
				if(pageSize=="null")
					document.getElementById("selectPageSize").value="10";
				else
					document.getElementById("selectPageSize").value=pageSize;
		</script>

	<!-- 删除用户时的弹窗和跳转 -->
	<script type="text/javascript">
			function deleteUser(ev) {
				var curPageNum="<%=curPageNum%>";
				var pageSize="<%=pageSize%>";
				window.wxc.xcConfirm("确认删除当前用户？","confirm",{onOk:function(){ 
					var oCurrentTd=ev.parentNode.parentNode.getElementsByTagName('td');
					var url="";
					var subOperation="<%=subOperation%>";
					if(subOperation=="searchLikeUser"){
						var searchStr="<%=searchStr%>";
						var searchType="<%=searchType%>";
						url="UserServlet?operation=manager&subOperation=deleteUser&curPageNum="+curPageNum+"&pageSize="+pageSize+"&identifyNum="+oCurrentTd[1].innerHTML+"&searchStr="+searchStr+"&searchType="+searchType;
					}
					else{ 
						url="UserServlet?operation=manager&subOperation=deleteUser&curPageNum="+curPageNum+"&pageSize="+pageSize+"&identifyNum="+oCurrentTd[1].innerHTML;
					}
					window.location.href=url;
				}});
			}
		</script>

	<!-- 修改用户时的跳转和传参 -->
	<script type="text/javascript">
			function updateUser(ev){
				var subOperation="<%=subOperation%>";
				var curPageNum="<%=curPageNum%>";
				var pageSize="<%=pageSize%>";
				var oCurrentTd=ev.parentNode.parentNode.getElementsByTagName('td');
				var identifyNum=oCurrentTd[1].innerHTML;
				var username=oCurrentTd[2].innerHTML;
				var role=oCurrentTd[3].innerHTML;
				var duty=oCurrentTd[4].innerHTML;
				var ownedUnit=oCurrentTd[5].innerHTML;
				var authorityUnit=oCurrentTd[6].innerHTML;
				var url="";
				if(subOperation=="searchLikeUser"){
					searchStr="<%=searchStr%>";
					searchType="<%=searchType%>";
				url = "jsp/zhj/user_management/updateuser.jsp?identifyNum="
						+ identifyNum + "&username=" + username + "&role="
						+ role + "&duty=" + duty + "&ownedUnit=" + ownedUnit
						+ "&authorityUnit=" + authorityUnit + "&curPageNum="
						+ curPageNum + "&pageSize=" + pageSize
						+ "&subOperation=" + subOperation + "&searchStr="
						+ searchStr + "&searchType=" + searchType;
			} else {
				url = "jsp/zhj/user_management/updateuser.jsp?identifyNum="
						+ identifyNum + "&username=" + username + "&role="
						+ role + "&duty=" + duty + "&ownedUnit=" + ownedUnit
						+ "&authorityUnit=" + authorityUnit + "&curPageNum="
						+ curPageNum + "&pageSize=" + pageSize
						+ "&subOperation=" + subOperation;
			}
			window.location.href = url;
		}
	</script>
</body>
</html>
