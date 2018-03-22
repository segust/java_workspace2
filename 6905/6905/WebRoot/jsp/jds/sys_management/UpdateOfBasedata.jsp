<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="cn.edu.cqupt.beans.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
Basedata b=new Basedata();
b=(Basedata)request.getAttribute("message") ;%>
<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>编辑数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
	<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<link rel="stylesheet" type="text/css" href="css/sys_management/systemmanage.css">
	<link rel="stylesheet" type="text/css" href="ConstantHTML/css/homepage.css">
	<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
	<script src="ConstantHTML/js/xcConfirm.js"></script>
	<script type="text/javascript" src="js/sys_management/basedata.js"></script>
  </head>
  
  <!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->
  
  <body>
      <%@include file="../../../ConstantHTML/top.html" %>
      <%@include file="../../../ConstantHTML/jdsleft.html" %>
      	<div id="right">
      		<!-- 二级标题 -->
      		<div class="subName">
				<span>当前位置：</span>
      			<a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			<a href="ServiceOfBaseDataServlet?operate=select">基础数据库</a>&nbsp;&nbsp;>&nbsp;&nbsp;
      			编辑基础数据库数据
			</div>
			    		
      		<!-- 内容区 -->
      		<div class="content">
      			<div class="toptitle">
      				<title>编辑基础数据库数据</title>
      			</div>
      			
      			 <div class="show_table">
      			 <form action="ServiceOfBaseDataServlet?operate=save&id=<%=b.getId() %>"    method="post" >
                  <table class="modify_basedata">
                      <tr> <td>PMNM(内码)</td>
                           <td><input type="hidden" value=<%=b.getId()%> id="id">
                           <input class="setbasedata" id="PMNM" type="text" value="<%=b.getPMNM()%>"/></td>
                      </tr>
                     
                      <tr>
                          <td>PMBM(编码)</td>
                           <td><input class="setbasedata" id="PMBM" type="text" value="<%=b.getPMBM()%>"/></td>
                       </tr>
                       <tr>
                          <td>QCBM</td>
                        <td><input class="setbasedata" id="QCBM" type="text" value="<%=b.getQCBM()%>"/></td>
                        </tr>
                        <tr>
                          <td>PMCS(名称)</td>
                          <td><input class="setbasedata" id="PMCS" type="text" value="<%=b.getPMCS()%>"/></td>
                        </tr>
                        <tr>
                          <td>XHTH</td>
                          <td><input class="setbasedata" id="XHTH" type="text" value="<%=b.getXHTH()%>"/></td>
                        </tr>
                        <tr>
                          <td>XLDJ</td>
                         <td><input class="setbasedata" id="XLDJ" type="text" value="<%=b.getXLDJ()%>"/></td>
                        </tr>
                        <tr>
                          <td>XHDE</td>
                          <td><input class="setbasedata" id="XHDE" type="text" value="<%=b.getXHDE()%>"/></td>
                        </tr>
                        <tr>
                          <td>JLDW</td>
                           <td><input class="setbasedata" id="JLDW" type="text" value="<%=b.getJLDW()%>"/></td>
                        </tr>
                        <tr>
                          <td>MJYL</td>
                           <td><input class="setbasedata" id="MJYL" type="text" value="<%=b.getMJYL()%>"/></td>
                        </tr>
                        <tr>
                          <td>QCXS</td>
                         <td><input class="setbasedata" id="QCXS" type="text" value="<%=b.getQCXS()%>"/></td>
                        </tr>
                        <tr>
                          <td>BZZL</td>
                          <td><input class="setbasedata" id="BZZL" type="text" value="<%=b.getBZZL()%>"/></td>
                        </tr>
                        <tr>
                          <td>BZJS</td>
                         <td><input class="setbasedata" id="BZJS" type="text" value="<%=b.getBZJS()%>"/></td>
                        </tr>
                        <tr>
                          <td>BZTJ</td>
                         <td><input class="setbasedata" id="BZTJ" type="text" value="<%=b.getBZTJ()%>"/></td>
                        </tr>
                        <tr>
                          <td>CKDJ(单价)</td>
                        <td><input class="setbasedata" id="CKDJ" type="text" value="<%=b.getCKDJ()%>"/></td>
                        </tr>
                        <tr>
                          <td>SCCJNM</td>
                         <td><input class="setbasedata" id="SCCJNM" type="text" value="<%=b.getSCCJNM()%>"/></td>
                        </tr>
                        <tr>
                          <td>GHDWNM</td>
                           <td><input class="setbasedata" id="GHDWNM" type="text" value="<%=b.getGHDWNM()%>"/></td>
                        </tr>
                        <tr> 
                          <td>ZBSX</td>
                          <td><input class="setbasedata" id="ZBSX" type="text" value="<%=b.getZBSX()%>"/></td>
                        </tr>
                        <tr>
                          <td>LBQF(类型)</td>
                         <td><input class="setbasedata" id="LBQF" type="text" value="<%=b.getLBQF()%>"/></td>
                         </tr>
                         <tr> 
                          <td>ZBBDSJ</td>
                           <td><input class="setbasedata" id="ZBBDSJ" type="text" value="<%=b.getZBBDSJ()%>"/></td>
                         </tr>
                         <tr>
                          <td>SYBZ</td>
                           <td><input class="setbasedata" id="SYBZ" type="text" value="<%=b.getSYBZ()%>"/></td>
                         </tr>
                         <tr>
                          <td>YJDBZ</td>
                            <td><input class="setbasedata" id="YJDBZ" type="text" value="<%=b.getYJDBZ()%>"/></td>
                         </tr>
                         <tr>
                          <td>SCBZ</td>
                          <td><input class="setbasedata" id="SCBZ" type="text" value="<%=b.getSCBZ()%>"/></td>
                      	</tr>
                      	<tr>
                      		<td>SCDXNF</td>
                      		<td><input class="setbasedata" id="SCDXNF" type="text" value="<%=b.getSCDXNF()%>"/></td>
                      	</tr>
                      <tr>
                      <td></td>
                      <td><input type="button" value="修改" onclick="updateBasedata()" style="margin-left:130px;"></td>
                    </tr>
                </table>
                 </form>
              </div>
              
</body>
      			
      		</div>
      		
        </div>
      <%@include file="../../../ConstantHTML/jdsfoot.html" %>
  </body>
</html>
