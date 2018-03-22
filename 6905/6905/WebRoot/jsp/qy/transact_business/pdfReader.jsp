<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>PDF onlineReader</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
	.div{ 
		margin:0 auto; 
		width:800px; 
		height:100%; 
	}
</style>
</head>

<body>
	<%
    	String pdfPath = request.getParameter("path");
    	pdfPath = "upload/" + pdfPath;
  	%>
	<%-- <%=pdfPath %> --%>
	<script type="text/javascript" src="js/transact_business/pdfobject.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery.media.js"></script>
	<script type="text/javascript">
 			window.onload = function (){
  				var success = new PDFObject({ url: "<%=pdfPath %>" }).embed();
 				if(!success){
           			var opts = {
             			width:800,
              			height:$(document).height(),
              			autoplay:true
         			};  
          			$('a.media').media(opts);
      			}
      		};
	</script>
	<div class="div">
     	<a style="display:none;" class="media" href="<%=pdfPath %>"></a>
	</div>
</body>
</html>
