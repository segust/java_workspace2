/*------ 复选框选择 ------ */
// 表头的复选框是否选中,用来选择每条经费记录
function selectAll() {
	var checkbox = document.getElementsByName("selectFare");// 表格中每个子复选框
	var allCheckbox = document.getElementById("allCheckbox");// 表头的总复选框
	var btn = document.getElementById("export-btn");// 导出按钮
	// 如果表格中第一个复选框选中，再点击总复选框，则全部选不中
	if (checkbox[0].checked) {
		allCheckbox.checked = false;
		for ( var i = 0; i < checkbox.length; i++) {
			if (checkbox[i].type == "checkbox")
				checkbox[i].checked = false;
			btn.disabled = true;// 导出按钮为不可点击状态
			btn.className = "unable-export-btn";// 导出按钮变灰
		}
	}
	// 如果其他复选框选中，再点击总复选框，则全部选中
	else {
		for ( var i = 0; i < checkbox.length; i++) {
			if (checkbox[i].type == "checkbox")
				checkbox[i].checked = true;
			btn.disabled = false;// 导出按钮为可点击状态
			btn.className = "export-btn";// 添加导出按钮样式
		}
	}
}

// 判断表格中单个复选框是否选中
function ifSelect() {
	var checkbox = document.getElementsByName("selectFare");
	var btn = document.getElementById("export-btn");
	if (checkbox[0].checked) {
		btn.disabled = false;
		btn.className = "export-btn";
	} else {
		for ( var i = 1; i < checkbox.length; i++) {
			if (checkbox[i].checked) {
				btn.disabled = false;
				btn.className = "export-btn";
				break;
			} else {
				btn.disabled = true;
				btn.className = "unable-export-btn";
			}
		}
	}

}

/*------ 导出经费按钮跳转------*/
function exportExcel() {
	var allCheckbox = document.getElementById("allCheckbox");// 表头的总复选框
	var theads = allCheckbox.parentNode.parentNode.getElementsByTagName("th");
	var excelArray = new Array();
	for ( var i = 0; i < theads.length; i++) {
		excelArray[i] = theads[i].innerHTML;
	}
	document.forms.exportForm.action = "/6905/FareServlet?operate=export&excelHead="
			+ excelArray;
	document.forms.exportForm.submit();

}

/*------ 查询的复选框选择 ------ */
// -------查询条件-----的复选框是否选中
function allSelect() {// 全部选择，全部不选
	var mainbox = document.getElementById("box"), childboxes = document
			.getElementsByName("fareType"),
	// startDate = document.getElementById("startDate").value,
	// endDate = document.getElementById("endDate").value,
	flag = true;
	// if (startDate == "" || endDate == "") {
	// document.getElementById("startDate").value = "";
	// document.getElementById("endDate").value = "";
	// }
	if (!mainbox.checked)
		flag = false;
	for ( var i = 0; i < childboxes.length; i++) {
		childboxes[i].checked = flag;
	}
}

// 用于初始化页面的方法
var yg = {};
yg.app = {};
yg.app.aa = function() {
	var oSt111 = document.getElementById("St111"), str, oIdStr = document
			.getElementById("idStr");
	if (oSt111 != null && oSt111 != "") {
		str = oSt111.value;
		var thisSelect = "";// 本页被选中的
		if (oIdStr != null && oIdStr != "")
			oIdStr.value = oIdStr.value + str;// 把之前已经能够选取的赋给idStr
		var array1 = new Array();
		array1 = str.split(";");
		var flag = false;// 用于判断是否改变导出按钮可用
		var checkbox = document.getElementsByName("selectFare");
		for ( var i = 0; i < checkbox.length; i++) {// 若已经选取则设为checked=true
			for ( var j = 0; j < array1.length - 1; j++) {
				if (checkbox[i].value == array1[j]) {
					checkbox[i].checked = true;
					thisSelect = thisSelect + checkbox[i].value + ";";// 这里
					flag = true;
				}
			}
		}
		// document.getElementById("thisSelect").value = thisSelect;
		if (flag) {
			var btn = document.getElementById("export-btn");
			btn.disabled = false;
			btn.className = "export-btn";
		}
		// // 改变刚刚添加，修改的记录的style的方法
		// 注释原因：添加修改后时间会自动记录为当前时间，自动出现在表的第一行
		// if (document.getElementById("checkfare-tbody")) {
		// var oCheckFareTrs = document.getElementById("checkfare-tbody")
		// .getElementsByTagName("tr");
		// var oLastFareId = document.getElementById("lastFareId").value;
		// if (oLastFareId != "" && oLastFareId != null) {
		// oCheckFareTrs[0].style.color = "red";
		// }
		// }
	}
};

/*------ 统计记录导出的按钮------*/
function exportStatisticsFare() {
	var id = document.getElementById("fare-table");
	var theads = id.parentNode.getElementsByTagName("th");
	var excelArray = new Array();
	for ( var i = 0; i < theads.length; i++) {
		excelArray[i] = theads[i].innerHTML;
	}
	document.forms.exportForm.action = "FareServlet?operate=exportStatisticsFare&excelHead="
			+ excelArray;
	document.forms.exportForm.submit();
}
// 改变刚刚添加，编辑的记录的style的方法
// function getTd() {
// var tr = document.getElementsByName("fare");
// var fareId = eval(document.getElementById("lastFareId").value);
// alert("fareId=" + fareId);
// for (var i = 0; i < tr.length; i++) {
// if (tr[i].cells[0].childNodes[0].value == fareId) {
// alert("dddd");
// tr[i].style.color = "red";
// alert("if里面的是=" + tr[i].cells[0].value);
// }
// }
// alert("第一个是=" + tr[0].cells[0].childNodes[0].value);
// }
