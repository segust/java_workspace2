window.onload = function() {
	header();
};


function header() {
	$(".header").animate({
		top : '0'
	}, 1000);
	$(".content").fadeIn(2000);
	other();
}

function other() {
	login();
}

function login() {

	$(".login-wrap").animate({
		left : '50%'
	}, 700);
}

function submitBtn() {
	$(".submit-btn").css(display, "block");
}

//扩展的getClass函数
function getClass(tagname, className) {
	//判断浏览器是否支持getElementsByClassName，如果支持就直接的用
	if (document.getElementsByClassName) {
		return document.getElementsByClassName(className);
	} else { //当浏览器不支持getElementsByClassName的时候用下面的方法
		var tagname = document.getElementsByTagName_r(tagname); //获取指定元素
		var tagnameAll = []; //这个数组用于存储所有符合条件的元素
		for ( var i = 0; i < tagname.length; i++) { //遍历获得的元素
			if (tagname[i].className == className) { //如果获得的元素中的class的值等于指定的类名，就赋值给tagnameAll
				tagnameAll[tagnameAll.length] = tagname[i];
			}
		}
		return tagnameAll;
	}
}

// 验证用户输入信息的有效性
function validateUser() {
	var oIdentifyNum, oPassword;
	oIdentifyNum = document.getElementById("name").value;
	oPassword = document.getElementById("password").value;
	$.ajax({
		url : '../../UserLoginServlet',
		type : 'POST',
		data : {
			identifyNum : oIdentifyNum,
			password : oPassword
		},
		dataType : "text",
		success : function(oResponseInfo) {
			var oResFlag, oResUrl, oResArray;
			oResArray = oResponseInfo.split(",");
			oResFlag = oResArray[0];
			oResUrl = oResArray[1];
			if (oResFlag == "1") {
				window.location.href = oResUrl;
			} else if (oResFlag == "0") {
				window.wxc.xcConfirm("用户名或密码错误", "error");
			} else if (oResFlag == "-1"){
				window.wxc.xcConfirm("网络或数据库连接错误", "error");
			}
		}
	});
}

//按回车的响应
function keydownEvent() {
    var e = window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13 ) {
        this.validateUser();
    }
}
