<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Demo</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="css/welcome.css">
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/left.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
      			<span>当前位置：</span>
      			<a href="#">欢迎页面</a>
			   </div>
						      		
      		<!-- 内容区 -->
      		<div class="content">      			
      			<!-- 这里写你的模块的html代码 -->
            <div class="welcome-wrap" id="ini-style">
              <p><span>xxx</span>,您好！</p>
              <p>欢迎进入企业版</p>
            </div>
      			
      		</div>
      		
        </div>
      <%@include file="../../../ConstantHTML/foot.html" %>
      <script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
      <script src="ConstantHTML/js/homepage.js"></script>
      <script src="js/welcome.js"></script>
  </body>
</html>
