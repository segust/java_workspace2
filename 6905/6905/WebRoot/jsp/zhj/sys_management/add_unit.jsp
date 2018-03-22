<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>添加数据</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="ConstantHTML/js/homepage.js"></script>
<script type="text/javascript" src="ConstantHTML/js/jquery-1.11.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="css/sys_management/systemmanage.css">
<link rel="stylesheet" type="text/css"
	href="ConstantHTML/css/homepage.css">
<link rel="stylesheet" href="ConstantHTML/css/xcConfirm.css">
<script src="ConstantHTML/js/xcConfirm.js"></script>
<script type="text/javascript" src="js/sys_management/basedata.js"></script>
</head>

<!-- 特别注意：大家记得把上面的<script>和<link>复制到自己jsp页面,和你的css、js在一起声明 -->

<body>
	<%@include file="../../../ConstantHTML/top.html"%>
	<%@include file="../../../ConstantHTML/zhjleft.html"%>
	<div id="right">
		<!-- 当前位置 -->
		<div class="subName">
			<span>当前位置：</span> <a href="ServiceOf9831Servlet?operate=select">系统管理</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			<a href="ServiceOfBaseDataServlet?operate=select">基础数据库</a>&nbsp;&nbsp;>&nbsp;&nbsp;
			增加单元
		</div>
		<!-- 内容区 -->
		<div class="content">
			<div class="toptitle"></div>
			<div class="show_table">
				<form>
					<table class="modify_basedata">
						<tr>
							<td>品名内码</td>
							<td>
							<input type="hidden" value=<%=request.getParameter("pmnm")%> id="FKPMNM">
							<input class="setbasedata" id="PMNM" type="text" value=""/>
							</td>
						</tr>

						<tr>
							<td>品名编码</td>
							<td><input class="setbasedata" id="PMBM" type="text" />
							</td>
						</tr>
						<tr>
							<td>器材编码</td>
							<td><input class="setbasedata" id="QCBM" type="text" />
							</td>
						</tr>
						<tr>
							<td>产品名称型号规格</td>
							<td><input class="setbasedata" id="PMCS" type="text" />
							</td>
						</tr>
						<tr>
							<td>图号</td>
							<td><input class="setbasedata" id="XHTH" type="text" />
							</td>
						</tr>
						<tr>
							<td>计量单位</td>
							<td><input class="setbasedata" id="JLDW" type="text" />
							</td>
						</tr>
						<tr>
							<td>参考价格</td>
							<td><input class="setbasedata" id="CKDJ" type="text" />
							</td>
						</tr>
						<tr>
							<td>包装体积</td>
							<td><input class="setbasedata" id="BZTJ" type="text" />
							</td>
						</tr>
						<tr>
							<td>包装件数</td>
							<td><input class="setbasedata" id="BZJS" type="text" />
							</td>
						</tr>
						<tr>
							<td>包装重量</td>
							<td><input class="setbasedata" id="BZZL" type="text" />
							</td>
						</tr>
						<tr>
							<td>器材项数</td>
							<td><input class="setbasedata" id="QCXS" type="text" />
							</td>
						</tr>
						<tr>
							<td>每机用量</td>
							<td><input class="setbasedata" id="MJYL" type="text" />
							</td>
						</tr>
						<tr>
							<td>消耗定额</td>
							<td><input class="setbasedata" id="XHDE" type="text" />
							</td>
						</tr>
						<tr>
							<td>修理等级</td>
							<td><input class="setbasedata" id="XLDJ" type="text" />
							</td>
						</tr>
						<tr>
							<td>生产单位内码</td>
							<td><input class="setbasedata" id="SCCJNM" type="text" />
							</td>
						</tr>
						<tr>
							<td>供货单位内码</td>
							<td><input class="setbasedata" id="GHDWNM" type="text" />
							</td>
						</tr>
						<tr>
							<td>装备属性</td>
							<td><input class="setbasedata" id="ZBSX" type="text" />
							</td>
						</tr>
						<tr>
							<td>生产定型</td>
							<td><input class="setbasedata" id="SCDXNF" type="text" />
							</td>
						</tr>
						<tr>
							<td>类别区分</td>
							<td><input class="setbasedata" id="LBQF" type="text" />
							</td>
						</tr>
						<tr>
							<td>叶节点标志</td>
							<td><input class="setbasedata" id="YJDBZ" type="text" />
							</td>
						</tr>
						<tr>
							<td>试验标志</td>
							<td><input class="setbasedata" id="SYBZ" type="text" />
							</td>
						</tr>
						<tr>
							<td>删除标志</td>
							<td><input class="setbasedata" id="SCBZ" type="text" />
							</td>
						</tr>
						<tr>
							<td></td><!-- submit -->
							<td><input type="button" value="提交" onclick="addOneUnit('<%=basePath%>')"
								style="margin-left:130px;">
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../../../ConstantHTML/zhjfoot.html"%>
</body>
</html>
