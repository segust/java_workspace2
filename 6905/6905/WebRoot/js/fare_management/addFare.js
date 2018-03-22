// 页面点击添加明细按钮
function addDetailBox(fareType) {
	var oDetailBox = document.getElementById("add-detailfare-box"), oDetailBoxContent = "", div = document
			.createElement("div"), oCurYear = new Date().getFullYear(), oCurMonth = new Date()
			.getMonth() + 1, oCurDay;
	// 初始化
	if (oCurMonth == 1)
		oCurDay = new Date().getDate();
	else
		oCurDay = 31;
	oDetailBoxContent += "<p style=\"position: relative;\">";
	oDetailBoxContent += "<span class='delete-icon' onclick='deleteFareDetail(this)'></span>";
	oDetailBoxContent += "<span>项目：<input type='text' class='small-text-input project'></span>";
	oDetailBoxContent += "<span>时间：";
	oDetailBoxContent += '<select style="width: 65px;margin-left: 0px;" onchange="changeDay(this,1)">';
	for ( var i = oCurYear; i >= oCurYear - 5; i--) {
		oDetailBoxContent += "<option value='" + i + "'>" + i + "</option>";
	}
	oDetailBoxContent += "</select>";
	oDetailBoxContent += "<label>-</label>";
	oDetailBoxContent += '<select style="width: 45px;margin-left: 0px;" onchange="changeDay(this,2)">';
	for ( var i = 1; i <= oCurMonth; i++) {
		oDetailBoxContent += "<option value='" + i + "'>" + i + "</option>";
	}
	oDetailBoxContent += "</select>";
	oDetailBoxContent += "<label>-</label>";
	oDetailBoxContent += '<select style="width: 45px;margin-left: 0px;">';
	for ( var i = 1; i <= oCurDay; i++) {
		oDetailBoxContent += "<option value='" + i + "'>" + i + "</option>";
	}
	oDetailBoxContent += "</select>";
	oDetailBoxContent += "</span>";
	oDetailBoxContent += "<span>凭证号：<input type='text' class='small-text-input'></span>";
	oDetailBoxContent += "<span>金额：<input type='text' class='small-text-input' onfocus='minusMoney(this);' onblur='getAllMoney(this);'></span>";
	oDetailBoxContent += "</p><p>";
	oDetailBoxContent += "<span>摘要：<input type='text' class='big-text-input'></span>";
	oDetailBoxContent += "<span>备注：<input type='text' class='big-text-input'></span>";
	oDetailBoxContent += "</p><p class='add-detail-bottom-p'></p>";
	div.innerHTML = oDetailBoxContent;
	oDetailBox.appendChild(div);

	// var oScript=document.createElement("script");
	// oScript.src="js/transact_business/dateTime.js";
	// var oTempP=div.getElementsByTagName("p")[0];
	// oTempP.insertBefore(oScript,oTempP.getElementsByTagName("span")[2]);
	// div.getElementsByTagName("script")[0].src="js/transact_business/dateTime.js";

	var sugestions = new Array();
	switch (fareType) {
	case "器材购置费":
		$('.project').combobox([ '' ]);// 这行combobox函数内的参数应为sugestions
		break;
	case "运杂费":
		$('.project').combobox([ '运输', '装卸', '保险' ]);// 这行combobox函数内的参数应为sugestions
		break;
	case "轮换费":
		$('.project').combobox([ '搬运', '检测', '包装' ]);
		break;
	case "维护保养费":
		$('.project').combobox([ '' ]);
		break;
	case "其他":
		$('.project').combobox([ '' ]);
		break;
	default:
		break;
	}

}

// 删除一条明细
function deleteFareDetail(targ) {
	var oCurDiv = targ.parentNode.parentNode;
	window.wxc.xcConfirm("确认删除吗？", "confirm", {
		onOk : function() {
			$(oCurDiv).remove();
		}
	});
}

// 获得焦点，预先减总金额
function minusMoney(event) {
	var oAllMoneyObj = document.getElementById("add-allmoney"), oAllHiddenMoneyObj = document
			.getElementById("add-allmoney-hidden"), oAllMoney = oAllMoneyObj.value, oCurMoney = event.value;
	if (oAllMoney == "")
		oAllMoney = 0;
	if (oCurMoney == "")
		oCurMoney = 0;
	oAllHiddenMoneyObj.value = Math.abs(oAllMoney) - Math.abs(oCurMoney);
}

// 失去焦点，计算并显示总金额
function getAllMoney(event) {
	var oAllHiddenMoneyObj = document.getElementById("add-allmoney-hidden"), oAllMoneyObj = document
			.getElementById("add-allmoney"), oAllHiddenMoney = oAllHiddenMoneyObj.value, oCurMoney = event.value;
	if (oAllHiddenMoney == "")
		oAllHiddenMoney = 0;
	if (oCurMoney == "")
		oCurMoney = 0;
	oAllMoneyObj.value = Math.abs(oAllHiddenMoney) + Math.abs(oCurMoney);
}

// 点击添加经费，提交到后台
function addFare(sum, pageSize, operate, basePath) {
	var oFareInputs = document.getElementById("add-mainfare-box")
			.getElementsByTagName("input"), oFareType = document
			.getElementById("add-type").value, oFareDetailBox = document
			.getElementById("add-detailfare-box"), oFareDetailDivs = oFareDetailBox
			.getElementsByTagName("div"), oFareDetailDivInputs, oFareDetailTimeSelect, oFareDetails = "[";

	if (oFareInputs[3].value == "") {
		window.wxc.xcConfirm("请勿添加空的经费", "warning");
		return;
	}

	for ( var i = 0; i < oFareDetailDivs.length; i++) {
		if (i % 2 != 0)
			continue;
		oFareDetailDivInputs = oFareDetailDivs[i].getElementsByTagName("input");
		oFareDetailTimeSelect = oFareDetailDivs[i]
				.getElementsByTagName("select");
		oFareDetails += "{'detailName':'" + oFareDetailDivInputs[0].value
				+ "',";
		oFareDetails += "'detailTime':'";
		for ( var j = 0; j < oFareDetailTimeSelect.length; j++) {
			oFareDetails += oFareDetailTimeSelect[j].value;
			if (j != oFareDetailTimeSelect.length - 1)
				oFareDetails += "-";
		}
		oFareDetails += "',";
		oFareDetails += "'voucherNo':'" + oFareDetailDivInputs[1].value + "',";
		oFareDetails += "'detailAmount':'" + oFareDetailDivInputs[2].value
				+ "',";
		oFareDetails += "'abstract':'" + oFareDetailDivInputs[3].value + "',";
		oFareDetails += "'remark':'" + oFareDetailDivInputs[4].value + "'},";
	}
	oFareDetails += "]";
	oFareDetails = oFareDetails.replace(",]", "]");

	$.ajax({
		url : 'FareServlet',
		type : "POST",
		data : {
			operate : "add",
			sum : sum,
			type : oFareType,
			company : oFareInputs[0].value,
			JdRoom : oFareInputs[1].value,
			date : oFareInputs[2].value,
			allMoney : oFareInputs[3].value,
			remark : oFareInputs[5].value,
			fareDetails : oFareDetails
		},
		success : function(data) {
			if (data == "1")
				window.wxc.xcConfirm("添加经费成功", "success", {
					onOk : function() {
						window.location.href = basePath
								+ "FareServlet?curPageNum=1&pageSize="
								+ pageSize + "&fareId=0";
					}
				});
			else if (data == "0")
				window.wxc.xcConfirm("添加经费失败", "error", {
					onOk : function() {
						window.location.href = basePath
								+ "FareServlet?curPageNum=1&pageSize="
								+ pageSize + "&fareId=0";
					}
				});
		}
	});
}

function editFare(curPageNum, pageSize, startTime, endTime, storeCompany,
		builtType, fareId, basePath) {
	var oFareInputs = document.getElementById("add-mainfare-box")
			.getElementsByTagName("input"), oFareType = document
			.getElementById("edit-type").value, oFareDetailBox = document
			.getElementById("add-detailfare-box"), oFareDetailDivs = oFareDetailBox
			.getElementsByTagName("div"), oFareDetailDivInputs, oFareDetailTimeSelect, oFareDetails = "[";

	if (oFareInputs[2].value == "") {
		window.wxc.xcConfirm("请勿添加空的经费", "warning");
		return;
	}

	for ( var i = 0; i < oFareDetailDivs.length; i++) {
		if (i % 2 != 0)
			continue;
		oFareDetailDivInputs = oFareDetailDivs[i].getElementsByTagName("input");
		oFareDetails += "{'detailName':'" + oFareDetailDivInputs[0].value
				+ "',";
		oFareDetailTimeSelect = oFareDetailDivs[i]
				.getElementsByTagName("select");
		oFareDetails += "'detailTime':'";
		for ( var j = 0; j < oFareDetailTimeSelect.length; j++) {
			oFareDetails += oFareDetailTimeSelect[j].value;
			if (j != oFareDetailTimeSelect.length - 1)
				oFareDetails += "-";
		}
		oFareDetails += "',";
		oFareDetails += "'voucherNo':'" + oFareDetailDivInputs[1].value + "',";
		oFareDetails += "'detailAmount':'" + oFareDetailDivInputs[2].value
				+ "',";
		oFareDetails += "'abstract':'" + oFareDetailDivInputs[3].value + "',";
		oFareDetails += "'remark':'" + oFareDetailDivInputs[4].value + "'},";
	}
	oFareDetails += "]";
	oFareDetails = oFareDetails.replace(",]", "]");
	// alert(oFareDetails);

	$.ajax({
		url : 'FareServlet',
		type : "POST",
		data : {
			operate : "edit",
			pageSize : pageSize,
			builtType : builtType,
			fareId : fareId,
			type : oFareType,
			company : oFareInputs[0].value,
			JdRoom : oFareInputs[1].value,
			amount : oFareInputs[2].value,
			remark : oFareInputs[4].value,
			fareDetails : oFareDetails
		},
		success : function(data) {
			if (data == "1")
				window.wxc.xcConfirm("修改经费成功", "success", {
					onOk : function() {
						window.location.href = basePath
								+ "FareServlet?curPageNum=1&pageSize="
								+ pageSize + "&fareId=" + fareId;
					}
				});
			else if (data == "0")
				window.wxc.xcConfirm("修改经费失败", "error", {
					onOk : function() {
						window.location.href = basePath
								+ "FareServlet?curPageNum=1&pageSize="
								+ pageSize + "&fareId=" + fareId;
					}
				});
		}
	});
}

// 根据费用类型不同，下拉框显示不同
function changeFare(event) {
	var sugestions = new Array(), oAddFareDetailButton = document
			.getElementById("add-faredetail-input"), oAddFareDetailBox = document
			.getElementById("add-detailfare-box"), oTempInputs = oAddFareDetailBox
			.getElementsByTagName("input");
	for ( var i = 0; i < oTempInputs.length; i++) {
		oTempInputs[i].value = "";
	}

	switch (event.value) {
	case "器材购置费":
		$('.project').combobox([ '' ]);// 这行combobox函数内的参数应为sugestions
		oAddFareDetailButton.setAttribute("onclick", "addDetailBox('器材购置费')");
		break;
	case "运杂费":
		$('.project').combobox([ '运输', '装卸', '保险' ]);// 这行combobox函数内的参数应为sugestions
		oAddFareDetailButton.setAttribute("onclick", "addDetailBox('运杂费')");
		break;
	case "轮换费":
		$('.project').combobox([ '搬运', '检测', '包装' ]);
		oAddFareDetailButton.setAttribute("onclick", "addDetailBox('轮换费')");
		break;
	case "维护保养费":
		$('.project').combobox([ '' ]);
		oAddFareDetailButton.setAttribute("onclick", "addDetailBox('维护保养费')");
		break;
	case "其他":
		$('.project').combobox([ '' ]);
		oAddFareDetailButton.setAttribute("onclick", "addDetailBox('其他')");
		break;
	default:
		break;
	}
}

/*// ///用来增加明细框
 function addinput() {// 控制格式就可以了
 var div = document.getElementById("w");
 var newNode = document.createElement("div");
 newNode.innerHTML = ";";
 var input = document.createElement("input");
 input.type = "text";
 input.name = "St1";
 input.className = 'addDetail-input';
 var span = document.createElement("span");
 span.innerHTML = ":&nbsp";
 var input1 = document.createElement("input");
 input1.type = "text";
 input1.name = "St2";
 input1.className = 'add-box-input';
 var span1 = document.createElement("span");
 span1.innerHTML = "&nbsp";
 var del = document.createElement("input");
 del.type = "button";
 del.value = "删除";
 del.className = 'return-btn';
 del.onclick = function d() {
 if (confirm('确定删除吗？')) {
 alert('删除成功！');
 this.parentNode.parentNode.removeChild(this.parentNode);
 return;
 } else {
 alert('删除失败！');
 return;
 }
 };
 var innerdiv = document.createElement("p");
 innerdiv.appendChild(input);
 innerdiv.appendChild(span);
 innerdiv.appendChild(input1);
 innerdiv.appendChild(span1);
 innerdiv.appendChild(del);
 div.appendChild(innerdiv);
 }

 // ------用来删除单个的明细-------
 function deleteitem(obj) {

 if (confirm('确定删除吗？')) {
 // alert('删除成功！');
 obj.parentNode.parentNode.removeChild(obj.parentNode);
 return;
 } else {
 // alert('删除失败！'); //一个明细框没必要提示那么多
 return;
 }
 }

 // ///// 判断输入框是否为空或者格式不合适
 var count = 0;// 只增加不减少
 var realNum = 0;// 输入框所存在的实际数目
 // /////////判断是否为空
 function Judge1(text) {
 var amount = document.getElementById(text + "-price").value;
 var storeCompany = document.getElementById(text + "-qy").value;
 var jds = document.getElementById(text + "-JDS").value;
 var date = document.getElementById(text + "-date").value;
 flag = true;
 if (amount == "") {
 alert("金额为空，请输入！  ");
 flag = false;
 return;
 }
 if (amount != "" && flag == true) { // 不为空则判断是否是数字
 var num = document.getElementById(text + '-price').value;
 if (isNaN(num)) {
 alert("输入的 ' " + num + " ' 不是数字，请输入数字 ！ ");
 document.getElementById(text + '-price').value = "";
 flag = false;
 return;
 }
 }
 if (storeCompany == "" && flag == true) {
 flag = false;
 alert("代储企业为空，请输入！");
 return;
 }
 if (jds == "" && flag == true) {
 flag = false;
 alert("军代室为空， 请输入！");
 return;
 }
 if (date == "" && flag == true) {
 flag = false;
 alert("操作日期为空， 请选择！ ");
 return;
 }
 var selectTime = document.getElementById(text + "-date").value;
 var selectTime1 = selectTime.replace("-", "/");// 替换字符，变成标准格式
 var today = new Date();// 取今天的日期
 var d1 = new Date(Date.parse(selectTime1)); //
 if (d1 > today && flag == true) {
 flag = false;
 alert("日期选择错误," + selectTime + " 大于当前时间！");
 return;
 }

 var re1 = "";// 一个用来记录st1
 var re2 = "";// 这个一个用来记录st2
 realNum = 0;
 var name1 = document.getElementsByTagName('input');
 var k = 0;
 for ( var i = 0; i < name1.length; i++) {
 if (name1[i].name == 'St1')// 只有输入框的名字是St1的时候才记录
 { // ///////////
 realNum++;// 记录的是总的明细的条数
 k = i + 1;// 下面是判断明细框的是否相同，遍历一遍
 for (; k < name1.length; k++) {
 if ((name1[i].value == name1[k].value)
 && (name1[k].name == 'St1')) {
 alert("明细框内容重复：" + name1[i].value);
 return;
 }
 }

 }
 }
 for ( var i = 0; i < realNum; i++) {
 if (document.getElementsByName("St1")[i].value == ""
 || document.getElementsByName("St2")[i].value == "") {
 alert("明细框不能为空！");
 return;
 }
 if (document.getElementsByName("St1")[i].value.indexOf(";") > 0) {
 alert("含有非法字符，请删除！ ");
 return;
 }
 if (isNaN(document.getElementsByName("St2")[i].value)
 || (document.getElementsByName("St2")[i].value < 0)) {// 不可小于0
 alert("输入的 ' " + document.getElementsByName("St2")[i].value
 + " ' 是非法字符，请重新输入 ！ ");
 document.getElementsByName("St2")[i].value = "";
 return;
 } else {

 re1 += document.getElementsByName("St1")[i].value + ";";
 re2 += document.getElementsByName("St2")[i].value + ";";
 document.getElementById("Hidden1").value = re1;
 document.getElementById("Hidden2").value = re2;

 }
 }

 if (flag) {
 if (text == "add")
 alert('添加成功！');
 else if (text == "edit")
 alert('修改成功！');
 document.getElementById(text + "-form").submit();
 }
 }*/

function changeDay(targ, targFlag) {
	var oCalenderSelects = targ.parentNode.getElementsByTagName("select"), oChooseYear, oChooseMonth, oEndMonth, oEndDay, oCurMonthSelect = oCalenderSelects[1], oCurDaySelect = oCalenderSelects[2], oCurYear = new Date()
			.getFullYear(), oCurMonth = new Date().getMonth() + 1, oCurDay = new Date()
			.getDate();
	if (targFlag == "1") {
		oChooseYear = Number(targ.value);
		oChooseMonth = Number(oCalenderSelects[1].value);
	} else if (targFlag == "2") {
		oChooseMonth = Number(targ.value);
		oChooseYear = Number(oCalenderSelects[0].value);
	} else if (targFlag == "3") {
		oChooseMonth = Number(oCalenderSelects[1].value);
		oChooseYear = Number(oCalenderSelects[0].value);
	}

	// 先判断选择的年份是否是小于当前年份
	if (oChooseYear < oCurYear) {
		oEndDay = getEndDay(oChooseMonth, oChooseYear);
		oEndMonth = 12;
	} else {
		// 等于当前年份，继续判断月份
		if (oChooseMonth < oCurMonth) {
			// 小于当前月份，日期根据大小平月判定
			oEndDay = getEndDay(oChooseMonth, oChooseYear);
		} else {
			// 等于当前月份，日期则只能到今天
			oEndDay = oCurDay;
		}
		oEndMonth = oCurMonth;
	}

	// 创建monthOption到oCurMonthSelect
	if (targFlag == "1") {
		// 只有改变年份才可以动态改月份
		$(oCurMonthSelect).html("");
		for ( var i = 1; i <= oEndMonth; i++) {
			var oMonthEachOption = document.createElement("option");
			oMonthEachOption.value = i;
			oMonthEachOption.innerHTML = i;
			oCurMonthSelect.appendChild(oMonthEachOption);
		}
	}

	// 创建dayOption到oCurDaySelect
	$(oCurDaySelect).html("");
	for ( var i = 1; i <= oEndDay; i++) {
		var oDayEachOption = document.createElement("option");
		oDayEachOption.value = i;
		oDayEachOption.innerHTML = i;
		oCurDaySelect.appendChild(oDayEachOption);
	}
}

function getEndDay(oChooseMonth, oChooseYear) {
	var oEndDay;
	if (oChooseMonth == 1 || oChooseMonth == 3 || oChooseMonth == 5
			|| oChooseMonth == 7 || oChooseMonth == 8 || oChooseMonth == 10
			|| oChooseMonth == 12)
		oEndDay = 31;
	else if (oChooseMonth == 4 || oChooseMonth == 6 || oChooseMonth == 9
			|| oChooseMonth == 11)
		oEndDay = 30;
	else {
		if ((oChooseYear % 4 == 0 && oChooseYear % 100 != 0)
				|| (oChooseYear % 400 == 0))
			oEndDay = 29;
		else
			oEndDay = 28;
	}
	return oEndDay;
}
