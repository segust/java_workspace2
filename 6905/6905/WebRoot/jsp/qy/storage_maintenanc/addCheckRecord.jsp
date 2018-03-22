<%@page import="cn.edu.cqupt.util.MyDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="cn.edu.cqupt.beans.User"%>
<%@page import="cn.edu.cqupt.beans.Fare"%>
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
        <title>新增检查记录</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" href="ConstantHTML/css/homepage.css">
        <link rel="stylesheet" href="ConstantHTML/css/global.css">
        <link rel="stylesheet" href="css/addCheckRecord.css" >
        
    </head>
    <body>
    	<%@include file="../../../ConstantHTML/top.html"%>
    	<%@include file="../../../ConstantHTML/left.html"%>
    
    	<div id="right">
    		<!-- 二级标题 -->
    		<div class="subName">
    			<span>当前位置：</span> 新增检查记录
    		</div>
    		<!-- 内容区 -->
    		<div id="content">
    			<!-- 信息填写开始 -->
    			<div class="check-info-wrap clearfix">
    			    <p class="tips">请填写检查相关信息</p>
    		        <ul class="check-info-wrap1">
                        <li class="clearfix"><label for="unit">检查单位:</label><input id="unit" type="text"></li>
                        <li class="clearfix"><label for="date">检查日期:</label>
                        	<input id="date" class="sang_Calender" type="text">
                        	<script	type="text/javascript" src="ConstantHTML/js/spdateTime.js"></script>
                        </li>
                        <li class="clearfix"><label for="site">检查地点:</label><input id="site" type="text"></li>
                        <li class="clearfix"><label for="item">检查事项:</label><textarea id="item"></textarea></li>
                    </ul>
                    <ul class="check-info-wrap2"> 
                        
                        <li class="clearfix"><label for="suggest">检查意见:</label><textarea id="suggest"></textarea></li    >
                        <li class="clearfix"><label for="feedback">反馈结果:</label><textarea id="feedback"></textarea></    li>
                        <li class="clearfix"><label for="remark">备注:</label><textarea id="remark"></textarea></li>
    		        </ul>
    		    </div>
    		    <!-- 信息填写结束 -->
    
    		    <!-- 按钮开始 -->
    			<div class="btn-wrap">
                    <p class="tips error-tips hide">还没有记录，不能查看！</p>
                    <a class="showTable btn" data-fn="view-record" href="javascript:;">查看记录</a>
                    <a class="submit-btn btn" data-fn="add-record" href="javascript:;">确认添加</a>
    		    </div>
                <!-- 按钮结束 -->
    
                <!-- 添加成功开始 -->
                <div class="add-success hide">
    					<img class="successimg" src="img/right.png" alt="success">
    					<h1 class="successinfo">添加成功</h1>
    					<h3 class="successinfo">运气不错哟！记录添加成功了</h3>
    					<a class="add-again btn" data-fn="add-again" href="javascript:;">继续添加</a>
                        <a class="showTable btn" data-fn="view-record" href="javascript:;">查看记录</a>
    			</div>
    			<!-- 添加成功结束 -->
                <div style="text-align:center">
    			    <div class="all-record-wrap" style="display:none;">
    			    	<h3>检查记录</h3>
                        <table>
                        <thead>
                            <tr>
                                <th>检查单位</th>
                                <th>检查日期</th>
                                <th>检查地点</th>
                                <th>检查事项</th>
                                <th>检查意见</th>
                                <th>反馈结果</th>
                                <th>备注</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                        </table>
                        <a class="add-again btn" data-fn="add-again" href="javascript:;">继续添加</a>
    			    </div>
    			</div>
    		</div>
    
    	</div>
    	<%@include file="../../../ConstantHTML/foot.html"%>
    	<script src="ConstantHTML/js/homepage.js"></script>
    	<script src="ConstantHTML/js/jquery-1.9.1.min.js"></script>
    	<script src="js/addCheckRecord.js"></script>
    </body>
</html>
