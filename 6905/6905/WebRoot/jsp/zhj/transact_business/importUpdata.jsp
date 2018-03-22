<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>导入上报备份数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script src="ConstantHTML/js/homepage.js"></script>
	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
	<script src="js/public/format-upload.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="css/transact_business/transact.css" >

  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/zhjleft.html" %>
      	<div id="right">
      	<div class="subName">

			</div>
       <div id="content">
       	     <!--审核文件excel导入 -->
        <div id="lunhuanshenheguanli">
       
          <div id="daorushenhewenjiantable">
			<center>
			<form action="${pageContext.request.contextPath}/InWarehouseServlet?operate=importBackupUpFiles" method="post" enctype="multipart/form-data" onsubmit="return format()">
              <table class="check-import-table">
		<tbody>
			<tr>
				<td>导入申请文件：</td>
				<td>
				<input type="text" size="20" name="upfile" id="upfile"style="padding:5px 0;border:1px dotted black" onchange="getType(this);"> 
					<a class="file-wrap" href="javascript:;">
                    浏览
					<input id="fileToUpload" type="file" accept=".whms" name="attachment"	onchange="upfile.value=this.value" >
				    </a>
				</td>
			</tr>
			<!-- <td>
					<select name="applyType" id="applyType">
						<option value="LHRK">轮换入库申请</option>
						<option value="LHCK">轮换出库申请</option>
						<option value="RK">入库申请</option>
						<option value="GXCK">更新出库申请</option>
					</select>
				</td> -->
             <tr>
                <td><p class="error-info"></p></td>
             </tr>
			<tr>
				
				<!-- <td><input type="button" value="导入" onclick="uploadjdjBackup(1);"></td> -->
				<td><input type="submit" value="确定导入"></td>
				<td><input type="reset" value="重置"></td>
				
			</tr>
			
		  
			<tr>
				<td>提示：</td><td id="msg">请导入军代局上报文件</td>
			</tr>
			
		 	</tbody>
		 </table>
				</form>
            </div>  
            </div>
     
       </div>
       </div>
      <%@include file="../../../ConstantHTML/zhjfoot.html" %>
      <!--<script type="text/javascript" src="js/qualification_management/ajaxfileupload.js"></script>-->
  	  <!--<script type="text/javascript" src="js/transact_business/ajaxUpload.js"></script>-->
	 <script type="text/javascript">
	 	var val="${runStatus}";
      		var result = "";
      		if(val != "") {
      			if(val == "true") {
      			result = "导入成功";
      		} else if(val == "false") {
				result = "导入失败";
			}
			alert(result); 
      		}
      		    
	 </script>
  </body>
</html>
