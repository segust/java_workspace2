// 删除角色事件
function deleteRole(ev, roleId) {
	window.wxc.xcConfirm("确认删除当前角色？","confirm",{onOk:function(){
		window.location.href = "UserServlet?operation=manager&subOperation=deleteRole&roleId="
			+ roleId;
	}});
}

// 修改角色和传参
function updateRole(ev, roleId,version) {
	var oCurrentTd = ev.parentNode.parentNode.getElementsByTagName('td');
	var folder="";
	var str = "";
	for (var i = 1; i < oCurrentTd.length - 1; i++) {
		var content = oCurrentTd[i].innerHTML;
		if ($.trim(content) == "是")
			str += ",1";
		else if ($.trim(content) == "否")
			str += ",0";
		else
			str += $.trim(content);
	}
	switch(version){
		case 1:
			folder="qy";
			break;
		case 2:
			folder="jds";
			break;
		case 3:
			folder="jdj";
			break;
		case 4:
			folder="zhj";
			break;
		default:
			break;
	}
	window.location.href = "jsp/"+folder+"/user_management/updaterole.jsp?curRoleInfo="
			+ str + "&roleId=" + roleId;
}

// 验证修改时输入的旧密码
function validateOldPwd() {
	var oInputOldPwd = document.getElementById("prepwd").value, oPrepwdText = document
			.getElementById("prepwdText");
	$.ajax({
				url : 'UserServlet',
				type : 'POST',
				data : {
					operation : 'validateOldPwd',
					inputOldPwd : oInputOldPwd
				},
				dataType : "text",
				success : function(data) {
					if (data == "1")
						oPrepwdText.innerHTML = "";
					else if (data == "0")
						oPrepwdText.innerHTML = "原密码不正确";
				}
			});
	// 决定按钮是否可用
	pwdButtonable();
}

// 验证输入新密码是否为空
function validateEmptyPwd() {
	var newpwd = $("#newpwd").val();
	var oInputOldPwd = document.getElementById("prepwd").value;
	var connewpwd = $("#connewpwd").val();
	if (connewpwd != "") {
		if (newpwd != connewpwd)
			$("#connewpwdText").html("两次密码不一致");
		else
			$("#connewpwdText").html("");
	}

	if (newpwd == null || newpwd == "")
		$("#newpwdText").html("密码不能为空");
	else if (newpwd == oInputOldPwd)
		$("#newpwdText").html("新旧密码一致");
	else
		$("#newpwdText").html("");
	// 决定按钮是否可用
	pwdButtonable();
}

// 验证两次输入密码是否一致
function validateConPwd() {
	var newpwd = $("#newpwd").val();
	var connewpwd = $("#connewpwd").val();
	if (newpwd != connewpwd)
		$("#connewpwdText").html("两次密码不一致");
	else {
		if (newpwd == null || newpwd == "")
			$("#newpwdText").html("密码不能为空");
		else
			$("#connewpwdText").html("");
	}
	// 决定按钮是否可用
	pwdButtonable();
}

// 清空输入的密码和消息
function clearPwdInfo() {
	document.getElementById("prepwd").value = "";
	document.getElementById("newpwd").value = "";
	document.getElementById("connewpwd").value = "";
	document.getElementById("prepwdText").innerHTML = "";
	document.getElementById("newpwdText").innerHTML = "";
	document.getElementById("connewpwdText").innerHTML = "";
}

// 按钮是否可用
function pwdButtonable() {
	var prepwd, newpwdText, connewpwdText, prepwdText,flag;
	prepwd = document.getElementById("prepwd").value;
	newpwdText = document.getElementById("newpwdText").innerHTML;
	connewpwdText = document.getElementById("connewpwdText").innerHTML;
	prepwdText = document.getElementById("prepwdText").innerHTML;
	if (newpwdText == "" && connewpwdText == "" && prepwdText == ""
			&& prepwd != ""){
		document.getElementById("updatePwdButton").disabled = false;
		showPwdButtonStyle(false);
	}
	else{
		document.getElementById("updatePwdButton").disabled = true;
		showPwdButtonStyle(true);
	}
}

// 控制修改按钮显示不同样式
function showPwdButtonStyle(flag) {
	var oUpdatePwdButton = document.getElementById("updatePwdButton");
	if(!flag)
		oUpdatePwdButton.className = "return-btn";
	else
		oUpdatePwdButton.className = "disabled-btn";
}