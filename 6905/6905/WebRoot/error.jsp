<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>错误页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="css/user_management/usermanageindex.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript">
		var limitVisitFlag="<%=request.getAttribute("limitVisitFlag")%>",
			lastPage="<%=request.getAttribute("lastPage")%>",
			lastPageArray=lastPage.split("/"),
			lastPageIndex=lastPageArray[lastPageArray.length-1],
			version="<%=config.getInitParameter("version")%>",
			href = "<%=basePath%>";
	
	if (limitVisitFlag == "1") {
		alert("您当前没有访问权限");
		if (lastPageIndex == "UserLoginServlet")
					window.location.href = href;
				else
					window.location.href = lastPage;
		/* window.wxc.xcConfirm("您当前没有访问权限", "warning", {
			onOk : function() {
				switch (version) {
				case 1:
					href += "jsp/qy/welcome.jsp";
					break;
				case 2:
					href += "jsp/jds/welcome.jsp";
					break;
				case 3:
					href += "jsp/jdj/welcome.jsp";
					break;
				case 4:
					href += "jsp/zhj/welcome.jsp";
					break;
				default:
					break;
				}
				if (lastPageIndex == "UserLoginServlet")
					window.location.href = href;
				else
					window.location.href = lastPage;
			}
		}); */
	}
</script>
</head>

<body>
</body>

</html>
