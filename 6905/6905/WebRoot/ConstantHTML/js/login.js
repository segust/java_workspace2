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

// function loginClick(){
// var oLoginForm=getClass("fieldset","login-form"),
// oSubmitBtn=getClass("div","submit-btn");
// oSubmitBtn[0].onclick=function(){
// afterLogin();
// };
// oLoginForm[0].onsubmit=function(){
// // return setTimeout("afterLogin()",500);
// return false;
// };
// }

// 扩展的getClass函数
function getClass(tagname, className) {
	// 判断浏览器是否支持getElementsByClassName，如果支持就直接的用
	if (document.getElementsByClassName) {
		return document.getElementsByClassName(className);
	} else { // 当浏览器不支持getElementsByClassName的时候用下面的方法
		var tagname = document.getElementsByTagName_r(tagname); // 获取指定元素
		var tagnameAll = []; // 这个数组用于存储所有符合条件的元素
		for (var i = 0; i < tagname.length; i++) { // 遍历获得的元素
			if (tagname[i].className == className) { // 如果获得的元素中的class的值等于指定的类名，就赋值给tagnameAll
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
        url: '../../UserLoginServlet',
        type: 'POST',
        data: {
            identifyNum: oIdentifyNum,
            password: oPassword
        },
        dataType: "text",
        success: function(oResponseFlag) {
            if (oResponseFlag == "0") {
                alert("用户名或密码输入错误");
                window.location.href = "../../jsp/login/login.html";
            }
        }
    });
}
