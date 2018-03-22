function chooseAll() {
	var oCheckboxLeader=document.getElementById("checkbox_product");
	var oCheckbox = document.getElementsByName("checkbox_product");
	if (oCheckboxLeader.checked == true) {
		for (var i = 0; i < oCheckbox.length; i++) {
			oCheckbox[i].checked = true;
		}
	} else {
		for (var i = 0; i < oCheckbox.length; i++) {
			oCheckbox[i].checked = false;
		}
	}
}

function arrayToJson(o) {
	var r = [];
	if (typeof o == "string")
		return "\""
	+ o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n")
	.replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for (var i in o)
				r.push(i + ":" + arrayToJson(o[i]));
			if (!!document.all
				&& !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/
				.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
		}
		r = "{" + r.join() + "}";
	} else {
		for (var i = 0; i < o.length; i++) {
			r.push(arrayToJson(o[i]));
		}
		r = "[" + r.join() + "]";
	}
	return r;
}
return o.toString();
}

function outAll_productcollective() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_product");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
			.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length; j++) {
				oArray[m][j - 1] = oCurrentTd[j].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "ProductQueryServlet?operate=exportSingleForm&pagename=product_collective",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "ProductQueryServlet?operate=download&pagename=product_collective&absolutePath="
				+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}


function getBorrowItem() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_product");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {
				oArray[m][j - 1] = oCurrentTd[j + 1].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "BorrowServlet?operate=gotoBorrowOut",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(){
				window.location.href = "jsp/qy/transact_business/borrowBusinessAddBorrowOutApply.jsp";
		},
		error : function() {
			alert("请求失败");
		}
	});
}

function getUpdateItem() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_product");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {
				oArray[m][j - 1] = oCurrentTd[j + 1].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "UpdateServlet?operate=gotoUpdateOut",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success:function(){
			window.location.href = "jsp/qy/transact_business/updateBusinessAddUpdateOutApply.jsp";
		}
	});
}
function outAll_productdetail() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_product");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
			.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {

				oArray[m][j - 1] = oCurrentTd[j].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "ProductQueryServlet?operate=exportSingleForm&pagename=product_detail",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "ProductQueryServlet?operate=download&pagename=product_detail&absolutePath="
				+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}

function outAll_contractstatistic() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
			.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {

				oArray[m][j - 1] = oCurrentTd[j + 1].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet?operate=exportSingleForm&pagename=contract_statistics",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "StatisticsQueryServlet?operate=download&pagename=contract_statistics&absolutePath="
				+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}

function outAll_productstatistic() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
			.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {

				oArray[m][j - 1] = oCurrentTd[j + 1].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet?operate=exportSingleForm&pagename=product_statistics",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "StatisticsQueryServlet?operate=download&pagename=product_statistics&absolutePath="
				+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}

function outAll_updatequery() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_product");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
			.getElementsByTagName('td');
			for (var j = 1; j < oCurrentTd.length - 1; j++) {

				oArray[m][j - 1] = oCurrentTd[j + 1].innerHTML;
			}
			m++;
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "UpdateQueryServlet?operate=exportSingleForm",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "UpdateQueryServlet?operate=download&absolutePath="
				+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}
function selectdeviceNo(provinces){
	var sel = document.getElementById('selectdeviceno');
	var detail = eval(provinces); 
	for (var i = 0; i < detail.length; i++){
		var opt = document.createElement ("option"); 
		opt.value = detail[i];
	}
}

/**
 * 判断输入的天数是否合法
 * */
 function judgeInputTime(restKeepTime,restMaintainTime){
 	var flag = true;
 	if(document.getElementById(restKeepTime)==null||document.getElementById(restMaintainTime)==null)
 		return;
 	var time1 = document.getElementById(restKeepTime).value;
 	var time2 = document.getElementById(restMaintainTime).value;
	var reg = /^(-|\+)?\d+$/;//判断是否为整数
	
	if(time1!=""){
		var a = reg.test(time1);
		if(a==false){
			//alert("输入的 ' "+time1+" '非法，请重新输入 ！ ");
			window.wxc.xcConfirm("输入的 ' "+time1+" '非法，请重新输入 ！ ","error");
			document.getElementById(restKeepTime).value="";
			flag = false;
		}else{
			if(time1<0){
				//alert("输入的 ' "+time1+" '非法，请重新输入 ！ ");
				window.wxc.xcConfirm("输入的 ' "+time1+" '非法，请重新输入 ！ ","error");
				document.getElementById(restKeepTime).value="";
				flag = false;
			}
		}
	}
	if(time2!=""){
		var b = reg.test(time2);
		if(b==false){
			//alert("输入的 ' "+time2+" '非法，请重新输入 ！ ");
			window.wxc.xcConfirm("输入的 ' "+time2+" '非法，请重新输入 ！ ","error");
			document.getElementById(restMaintainTime).value="";
			flag = false;
		}else{
			if(time2<0){
				//alert("输入的 ' "+time2+" '非法，请重新输入 ！ ");
				window.wxc.xcConfirm("输入的 ' "+time2+" '非法，请重新输入 ！ ","error");
				document.getElementById(restMaintainTime).value="";
				flag = false;
			}
		}
	}
	
	return flag;
}
