function chooseAll() {
	var oCheckBoxLeader = document.getElementById("checkboxLeader");
	var oCheckbox = document.getElementsByName('checkbox_strength');
	if (oCheckBoxLeader.checked == true) {
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

function outAll_equipmentdetail() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=exportSingleForm&pagename=equipmentdetail",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentdetail&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

function outAll_equipmentcollective() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=exportSingleForm&pagename=equipmentcollective",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentcollective&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材实力汇总导出成word
 * */
function equipmentCollectiveToWord(){
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=equipmentCollectiveToWord",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentCollectiveToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材实力汇总导出成word
 * */
function allEquipmentCollectiveToWord(){
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=allEquipmentCollectiveToWord",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=allEquipmentCollectiveToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材明细统计导出成word
 * */
function equipmentDetailToWord(){
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=equipmentDetailToWord",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentDetailToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材明细统计导出成word
 * */
function allEquipmentDetailToWord(){
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=allEquipmentDetailToWord",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=allEquipmentDetailToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材明细账导出成word
 * */
function equipmentDetailAccountToWord(){
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 2; j < oCurrentTd.length - 1; j++) {
				//if(j==10||j==11||j==12)
				if(j==9||j==10||j==11){
					oArray[m][j-2] =oCurrentTd[j].getElementsByTagName('input')[0].value;					
				}else{
					oArray[m][j-2] = oCurrentTd[j].innerHTML;					
				}
			}
			m++;
		}
	}
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=equipmentDetailAccountToWord",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				alert("datayayay:"+data);
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentDetailAccountToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

/**
 * 器材明细账导出成word
 * */
function allEquipmentDetailToWord(){
	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=allEquipmentDetailToWord",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename" +
						"=allEquipmentDetailToWord&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

function outAll_contractstatistic() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet?operate=exportSingleForm&pagename=contract_statistics",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "StatisticsQueryServlet?operate=download&pagename=contract_statistics&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

function outAll_productstatistic() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet?operate=exportSingleForm&pagename=product_statistics",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "StatisticsQueryServlet?operate=download&pagename=product_statistics&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

function outAll_updatequery() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "UpdateQueryServlet?operate=exportSingleForm",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "UpdateQueryServlet?operate=download&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}

function outAll_equipmentdetailAccount() {
	var oCurrentCheckBox = document.getElementsByName("checkbox_strength");
	//先获取选中了多少的checkbox
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
	//创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "EquipmentServlet?operate=exportSingleForm&pagename=equipmentdetailAccount",
		data : {
			'outData' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			if (data == 0) {
//				alert("导出失败!");
				window.wxc.xcConfirm("导出失败", "error");
			} else {
				window.location.href = "EquipmentServlet?operate=download&pagename=equipmentdetailAccount&absolutePath="
						+ data;
			}
		},
		error : function() {
//			alert("请求失败");
			window.wxc.xcConfirm("请求失败", "error");
		}
	});
}