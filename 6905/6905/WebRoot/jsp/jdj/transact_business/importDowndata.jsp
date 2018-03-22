<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>导入下发数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >
	<script src="js/public/format-upload.js"></script>

  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/jdjleft.html" %>
      	<div id="right">
      	<div class="subName">

			</div>
       <div id="content">
       	     <!--审核文件excel导入 -->
        <div id="lunhuanshenheguanli">
       
          <div id="daorushenhewenjiantable">
			<center>
			<form action="${pageContext.request.contextPath}/InWarehouseServlet?operate=importBackupDownFiles" method="post" enctype="multipart/form-data" onsubmit="return format()">
				<p class="error-info"></p>
              <table class="check-import-table">
		<tbody>
			<tr>
				<td>导入申请文件：</td>
				<td>
					<input id="upfile" type="text" size="20" name="upfile"  autocomplete="off" style="padding:5px 0;border:1px dotted black">
					<a class="file-wrap" href="javascript:;">
					浏览
					<input type="file" id="fileToUpload" accept=".whms" name="attachment">
				    </a>
				</td>
			</tr>
			<!-- <td>
					<select name="applyType">
						<option value="LHRK">轮换入库申请</option>
						<option value="LHCK">轮换出库申请</option>
						<option value="RK">入库申请</option>
						<option value="GXCK">更新出库申请</option>
					</select>
				</td> -->
			<tr>
				<td><input type="submit" value="导入" ></td>
				<td><input type="reset" value="重置"/></td>
			</tr>
			
		  
			<tr>
				<td>提示：</td><td id="msg">请导入军代室下发文件</td>
			</tr>
			
		 	</tbody>
		 </table>
				</form>
            </div>  
            </div>
     
       </div>
       </div>
      <%@include file="../../../ConstantHTML/jdjfoot.html" %>
      <script type="text/javascript" src="js/transact_business/ajaxfileupload.js"></script>
  	  <script type="text/javascript" src="js/transact_business/ajaxUpload.js"></script>
  	  <script type="text/javascript">
      	var val="${runStatus}";
      		var result = "";
      		if(val == "true") {
      			result = "导入成功";
      		} else if(val == "false") {
				result = "导入失败";
			}
			alert(result);   
      </script>
  </body>
</html>
