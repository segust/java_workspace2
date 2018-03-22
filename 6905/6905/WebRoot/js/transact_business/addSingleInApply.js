var items = [];
function submitProductValidate() {
	var bacth = document.getElementById("batch").value;
	var flag = true;
	var resultStr = "";
	if (bacth == "") {
		resultStr += "请输入批次";
		flag = false;
	}
	if (resultStr != "")
		window.wxc.xcConfirm(resultStr, "info");
	return flag;
}
var objs = new Array();
var index = 0;
var preData = new Array();
// 记录添加的申请信息
function addApply() {
	if (submitProductValidate()) {
		var contractid = document.getElementById("contractid").innerHTML;
		var means = document.getElementById("inMeans").innerHTML;
		var batch = document.getElementById("batch").value;
		var wholename = document.getElementById("wholename").value;
		var unit = document.getElementById("productunit").value;
		var dNo = document.getElementById("deviceNo").value;
		var pmnm = document.getElementById("pmnm").value;
		var measure = document.getElementById("measure").value;
		var manuf = document.getElementById("manufacturer").value;
		var keeper = document.getElementById("keeper").value;
		var price = document.getElementById("price").value;
		var location = document.getElementById("location").value;
		var makeTime = document.getElementById("signdate").value;
		var maintain = document.getElementById("maintain").value;
		var remark = document.getElementById("remark").value;
		var productname = document.getElementById("productname").value;
		var productmodel = document.getElementById("productmodel").value;
		var productcode = document.getElementById("productcode").value;
		var storagetime = document.getElementById("storagetime").value;
		var obj = new createApply(contractid, means, productname, productmodel,
				batch, wholename, unit, dNo, pmnm, measure, productcode,
				storagetime, manuf, keeper, price, location, makeTime,
				maintain, remark);
		objs.push(obj);

		var list = document.getElementById("addprolist");
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		msg.setAttribute("style", "display:none;");
		submit.setAttribute("style", "display:block;");
		var span = document.createElement("p");
		span.setAttribute("style", "margin-top:10px;");
		span.innerHTML = "新增申请:机号&nbsp;<a href='javascript:void(0)' onclick='setPro(objs,"
				+ index
				+ ")'>"
				+ obj.dNo
				+ "</a>&nbsp;&nbsp;<input class='search-btn' type='button' value='删除' onclick='delcur(this,"
				+ index + ")'/><br/>";
		list.appendChild(span);
		index++;
		var list = document.getElementById("addprolist");
		var bacthInput = document.getElementById("batch");
		if (list.getElementsByTagName("p").length > 1) {
			bacthInput.setAttribute("disabled", true);
		}

		var add = document.getElementById("addApply");
		add.setAttribute("style", "display:none;");

		addToArray();
		clearRadio();
	}

}

function addToArray() {
	var oTable = document.getElementById("contract-table"), oTbody = oTable
			.getElementsByTagName("tbody")[0], oTrs = oTbody
			.getElementsByTagName("tr");

	for (var i = 0; i < oTrs.length; i++) {
		if (oTrs[i].cells[0].getElementsByTagName("input")[0].checked) {
			oTrs[i].cells[0].getElementsByTagName("input")[0].disabled = true;
			items.push(oTrs[i].cells[3].innerHTML);
			break;
		}
	}
}

function setPro(objs, index) {
	var curObj = objs[index];
	var contractid = document.getElementById("contract-view");
	var means = document.getElementById("mean-view");
	var batch = document.getElementById("batch-view");
	var name = document.getElementById("name-view");
	var unit = document.getElementById("unit-view");
	var device = document.getElementById("device-view");
	var pmnm = document.getElementById("pmnm-view");
	var measure = document.getElementById("measure-view");
	var manuf = document.getElementById("manu-view");
	var keeper = document.getElementById("keeper-view");
	var price = document.getElementById("price-view");
	var location = document.getElementById("location-view");
	var makeTime = document.getElementById("pdate-view");
	var maintain = document.getElementById("maintain-view");
	var remark = document.getElementById("remark-view");
	var pname = document.getElementById("pname-view");
	var model = document.getElementById("model-view");
	var code = document.getElementById("code-view");
	var storage = document.getElementById("storage-view");
	$(contractid).html(curObj.contractid);
	$(pname).html(curObj.productname);
	$(model).html(curObj.productmodel);
	$(code).html(curObj.productcode);
	$(storage).html(curObj.storagetime);
	$(means).html(curObj.means);
	$(batch).html(curObj.batch);
	$(name).html(curObj.wholename);
	$(unit).html(curObj.unit);
	$(device).html(curObj.dNo);
	$(pmnm).html(curObj.pmnm);
	$(measure).html(curObj.measure);
	$(manuf).html(curObj.manuf);
	$(keeper).html(curObj.keeper);
	$(price).html(curObj.price);
	$(location).html(curObj.location);
	$(makeTime).html(curObj.makeTime);
	$(maintain).html(curObj.maintain);
	$(remark).html(curObj.remark);
	$(document).ready(function() {
		$("#view-table").slideToggle("slow");
		$("#cover-table").slideToggle("slow");
	});
}
function hide() {
	$(document).ready(function() {
		$("#view-table").slideToggle("slow");
		$("#cover-table").slideToggle("slow");
	});
}

function delcur(event, ind) {
	var p = event.parentNode, list = document.getElementById("addprolist");
	clearDisabled($(event));
	if (list.getElementsByTagName("p").length == 2) {
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		var batch = document.getElementById("batch");
		batch.removeAttribute("disabled");
		msg.setAttribute("style", "display:block;");
		submit.setAttribute("style", "display:none;");
		clearForm("singleForm");
	}
	list.removeChild(p);
	objs.splice(ind, 1);
	index--;
}

function clearDisabled(rem) {
	var oA = rem.prev("a"), jiHao = oA.html();

	console.log(items);
	for (var i = 0; i < items.length; i++) {
		if (items[i] == jiHao) {
			items.splice(i, 1);
		}
	}
	console.log(items);
	checkItems();
}

function checkItems() {
	var oTable = document.getElementById("contract-table"), oTbody = oTable
			.getElementsByTagName("tbody")[0], oTrs = oTbody
			.getElementsByTagName("tr"), flag;

	for (var i = 0; i < oTrs.length; i++) {
		flag = false;
		for (var j = 0; j < items.length; j++) {
			if (oTrs[i].cells[3].innerHTML == items[j]) {
				flag = true;
			}
		}

		if (flag == true) {
			oTrs[i].cells[0].getElementsByTagName('input')[0].disabled = true;
		} else {
			oTrs[i].cells[0].getElementsByTagName('input')[0].disabled = false;
		}
	}
}

// js申请表对象
function createApply(contractid, means, productname, productmodel, batch,
		wholename, unit, dNo, pmnm, measure, productcode, storagetime, manuf,
		keeper, price, location, makeTime, maintain, remark) {
	this.means = means;
	this.batch = batch;
	this.contractid = contractid;
	this.productname = productname;
	this.productmodel = productmodel;
	this.productcode = productcode;
	this.storagetime = storagetime;
	this.wholename = wholename;
	this.unit = unit;
	this.dNo = dNo;
	this.pmnm = pmnm;
	this.measure = measure;
	this.manuf = manuf;
	this.keeper = keeper;
	this.price = price;
	this.location = location;
	this.makeTime = makeTime;
	this.maintain = maintain;
	// date类型的需要转换
	this.remark = remark;
	return this;
}

// 清空radio按钮
function clearRadio() {
	var radios = document.getElementsByName("applyinproduct");
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			radios[i].checked = false;
		}
	}
}

// 清空form选择
function clearForm(id) {
	clearRadio();
	var formObj = document.getElementById(id);
	if (formObj == undefined) {
		return;
	}
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].type == "text"
				&& formObj.elements[i].name != "contractid"
				&& formObj.elements[i].name != "signtime"
				&& formObj.elements[i].name != "buyer") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "password") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "radio") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "checkbox") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "select-one") {
			formObj.elements[i].options[0].selected = true;
		} else if (formObj.elements[i].type == "select-multiple") {
			for (var j = 0; j < formObj.elements[i].options.length; j++) {
				formObj.elements[i].options[j].selected = false;
			}
		} else if (formObj.elements[i].type == "file") {
			// formObj.elements[i].select();
			// document.selection.clear();
			// for IE, Opera, Safari, Chrome
			var file = formObj.elements[i];
			if (file.outerHTML) {
				file.outerHTML = file.outerHTML;
			} else {
				file.value = ""; // FF(包括3.5)
			}
		} else if (formObj.elements[i].type == "textarea") {
			formObj.elements[i].value = "";
		}
	}
}

function upObjs() {
	$
			.ajax({
				type : "post",
				url : "AddInApplyServlet?operate=inApply",
				data : {
					'data' : arrayToJson(objs)
				},
				dataType : "text",
				success : function(data) {
					if (data == "1") {
						// alert("添加申请");
						window.wxc
								.xcConfirm(
										"添加成功！",
										"info",
										{
											onOk : function() {
												window.location.href = "InWarehouseServlet?operate=queryApply";
											}
										});
					} else if (data == "0") {
						window.wxc.xcConfirm("添加失败！", "warning");
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败！", "warning");
				}
			});
}

function arrayToJson(o) {
	var r = [];
	if (typeof o == "string")
		return "\""
				+ o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n")
						.replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for ( var i in o)
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

// 判断输入的产品编号的数字串之差是否等于产品数量
function isEqualProductCount() {
	var productCode1 = document.getElementById("productCode1").value;
	var productCode2 = document.getElementById("productCode2").value;
	var num = document.getElementById("num").value;
	var reg = new RegExp(/\d+/);
	var startCode = reg.exec(productCode1);
	var endCode = reg.exec(productCode2);
	var testNum = endCode - startCode + 1;
	var str = "";
	var flag = true;
	if (num == "") {
		str += "输入数量为空";
		flag = false;
	}
	if (testNum < 0) {
		str += "开始编号大于结束编号";
		flag = false;
	} else if (testNum != num) {
		str += "编号范围和所输数量不一致";
		flag = false;
	}
	if (str != "")
		// alert(str);
		window.wxc.xcConfirm(str, "warning");
	return flag;
}

/* 新入库申请-选择产品后自动写入 */
function radioSelect(event) {
	var add = document.getElementById("addApply");
	if (event.checked) {
		var oTr = event.parentNode.parentNode;
		var tds = oTr.getElementsByTagName('td');
		var wholename = document.getElementById("wholename");
		wholename.value = tds[4].innerHTML;
		var unit = document.getElementById("productunit");
		unit.value = tds[5].innerHTML;
		var dNo = document.getElementById("deviceNo");
		dNo.value = tds[3].innerHTML;
		var pmnm = document.getElementById("pmnm");
		pmnm.value = tds[6].innerHTML;
		var measure = document.getElementById("measure");
		measure.value = tds[8].innerHTML;
		var manuf = document.getElementById("manufacturer");
		manuf.value = tds[11].innerHTML;
		var keeper = document.getElementById("keeper");
		keeper.value = tds[12].innerHTML;
		var price = document.getElementById("price");
		price.value = tds[7].innerHTML;
		var productname = document.getElementById("productname");
		productname.value = tds[4].innerHTML;
		var productmodel = document.getElementById("productmodel");
		productmodel.value = tds[2].innerHTML;
		add.removeAttribute("style");
	} else {
		add.setAttribute("style", "display:none;");
	}

}

/* 新入库申请-修改产品 */
function editProduct(event) {
	var oTr = event.parentNode.parentNode;
	event.setAttribute("value", "保存");
	event.setAttribute("onclick", "saveProduct(this)");
	var tds = oTr.getElementsByTagName('td');
	for (var i = 0; i < tds.length; i++) {
		if (i == 2 || i == 5 || i == 6 || i == 7 || i == 10 || i == 11
				|| i == 12) {
			var val = tds[i].innerHTML;
			preData[i] = val;
			$(tds[i]).html(
					"<input type='text' value='" + val
							+ "' style='height:20px;font-size:16px;'/>");
		} else if (i == 4) {
			var val = tds[i].innerHTML;
			preData[i] = val;
			$(tds[i])
					.html(
							"<input type='text' value='"
									+ val
									+ "' style='height:20px;width:230px;font-size:16px;'/>");
		}
	}
}

/* 新入库申请-修改产品 */
function saveProduct(event) {
	event.setAttribute("value", "修改");
	event.setAttribute("onclick", "editProduct(this)");
	var oTr = event.parentNode.parentNode;
	var tds = oTr.getElementsByTagName('td');
	var array = new Array();
	for (var i = 0; i < tds.length - 1; i++) {
		if (i == 2 || i == 4 || i == 5 || i == 6 || i == 7 || i == 10
				|| i == 11 || i == 12) {
			var val = tds[i].getElementsByTagName('input')[0].value;
			array[i] = val;
			$(tds[i]).html(val);
		} else if (i == 0) {
			var val = tds[i].getElementsByTagName('input')[1].value;
			array[i] = val;
		} else {
			array[i] = tds[i].innerHTML;
		}
	}

	$.ajax({
		type : "post",
		url : "AddInApplyServlet?operate=updatePro",
		data : {
			'data' : arrayToJson(array)
		},
		dataType : "text",
		success : function(data) {
			// alert(data);
			window.wxc.xcConfirm(data, "info");
			if (data == "修改失败！") {
				for (var i = 0; i < tds.length - 1; i++) {
					if (i == 2 || i == 4 || i == 5 || i == 6 || i == 7
							|| i == 10 || i == 11 || i == 12) {
						var val = preData[i];
						$(tds[i]).html(val);
					}
				}
			} else {
				radioSelect(tds[0].getElementsByTagName('input')[0]);
			}
			preData.splice(0, preData.length);
		},
		error : function() {
			// alert("请求失败");
			window.wxc.xcConfirm("请求失败", "warning");
		}
	});
}

// 删除产品提示框
function deleteProduct(event) {
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName('td');
	var id = tds[0].getElementsByTagName('input')[1].value;
	var name = tds[4].innerHTML + tds[5].innerHTML;
	window.wxc.xcConfirm("确定删除'" + name + "'产品吗吗?", "confirm", {
		onOk : function() {
			jQuery.ajax({
				type : "post",
				url : "AddInApplyServlet?operate=deletePro&productId=" + id,
				dataType : "text",
				success : function(data) {
					if (data == "1") {
						window.wxc.xcConfirm("删除成功", "info");
						var oDeviceNo = tds[3].innerHTML;
						var oArray = retChoObjAndInd(oDeviceNo);
						if (oArray.length == 2)
							delcur(oArray[0], oArray[1]);
						// tr.style.display = "none";
						table = tr.parentNode;
						table.removeChild(tr);
						clearForm(singleForm);
					} else if (data == "0") {
						window.wxc.xcConfirm("删除失败", "warning");
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败", "warning");
				}
			});
		}
	});
}

// 清空form选择
function clearForm(id) {
	var formObj = document.getElementById(id);
	if (formObj == undefined) {
		return;
	}
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].type == "text"
				&& formObj.elements[i].name != "contractid"
				&& formObj.elements[i].name != "signtime"
				&& formObj.elements[i].name != "buyer") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "password") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "radio") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "checkbox") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "select-one") {
			formObj.elements[i].options[0].selected = true;
		} else if (formObj.elements[i].type == "select-multiple") {
			for (var j = 0; j < formObj.elements[i].options.length; j++) {
				formObj.elements[i].options[j].selected = false;
			}
		} else if (formObj.elements[i].type == "file") {
			// formObj.elements[i].select();
			// document.selection.clear();
			// for IE, Opera, Safari, Chrome
			var file = formObj.elements[i];
			if (file.outerHTML) {
				file.outerHTML = file.outerHTML;
			} else {
				file.value = ""; // FF(包括3.5)
			}
		} else if (formObj.elements[i].type == "textarea") {
			formObj.elements[i].value = "";
		}
	}
}

// 上面删除后看下面是否已选择该机号，选择了返回下面的元素对象和在list中的位置
function retChoObjAndInd(oDeviceNo) {
	var oList = document.getElementById("addprolist"), oATags = oList
			.getElementsByTagName("a"), oInputs = oList
			.getElementsByTagName("input"), oArray = new Array();
	if (oATags.length == 0)
		oArray[0] = -1;
	for (var i = 0; i < oATags.length; i++) {
		if (oDeviceNo == oATags[i].innerHTML) {
			oArray[0] = oInputs[i];
			oArray[1] = i;
			return oArray;
		}
	}
}
