function submitProductValidate() {
	var bacth = document.getElementById("batch").value;
	var flag = true;
	var resultStr = "";
	if(bacth == "") {
		resultStr +="请输入批次";
		flag= false;
	}
	if (resultStr != "")
		alert(resultStr);
	return flag;
}
var objs = new Array();
var index = 0;
var preId  = 0;
//记录添加的申请信息
function addApply(basePath) {
	if (submitProductValidate()) {
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
		var execTime = document.getElementById("operateTime").innerHTML;
		if(preId != 0) {
			var obj = new createApply(preId,means, batch, wholename, unit, dNo, pmnm, measure,
					 manuf, keeper, price, location, makeTime, maintain,remark,execTime);
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
			clearRadio();
			var add = document.getElementById("addApply");
			add.setAttribute("style", "display:none;");
		}else {
			alert("请选择对账产品！");
		}
		
	}
}

function setPro(objs, index) {
	var curObj = objs[index];
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
	var operate = document.getElementById("operate-view");
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
	$(operate).html(curObj.execTime);
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
	var p = event.parentNode;
	var list = document.getElementById("addprolist");
	if (list.getElementsByTagName("p").length == 2) {
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		msg.setAttribute("style", "display:block;");
		submit.setAttribute("style", "display:none;");
	}
	list.removeChild(p);
	objs.splice(ind, 1);
	index--;
}
//js申请表对象
function createApply(id,means, batch, wholename, unit, dNo, pmnm, measure,
		 manuf, keeper, price, location, makeTime, maintain,remark,execTime) {
	this.preId = id;
	this.means = means;
	this.batch = batch;
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
	this.execTime = execTime;
	preId = 0;
	return this;
}

//清空radio按钮
function clearRadio() {
	var radios = document.getElementsByName("applyinproduct");
	for(var i=0;i<radios.length;i++) {
		if(radios[i].checked) {
			radios[i].checked = false;
		}
	}
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

function upObjs(basePath) {
	$.ajax({
		type : "post",
		url : "UpdateServlet?operate=UpdateInWarehouse",
		data : {
			'data' : arrayToJson(objs)
		},
		dataType : "text",
		success : function(data) {
			if (data == "1") {
				alert("添加成功");
				window.location.href =basePath+ "InWarehouseServlet?operate=queryApply";
			} else if (data == "0") {
				alert("添加失败");
			}

		},
		error : function() {
			alert("请求失败");
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
		alert(str);
	return flag;
}

/*轮换入库申请-选择产品后自动写入
function radioSelect(event) {
	if(event.checked) {
		var oTr = event.parentNode.parentNode;
		var tds = oTr.getElementsByTagName('td');
		preId = tds[0].getElementsByTagName('input')[1].value;
		var wholename = document.getElementById("wholename");
		wholename.value = tds[3].innerHTML;
		var unit = document.getElementById("productunit")
		unit.value = tds[5].innerHTML;
		var dNo = document.getElementById("deviceNo");
		dNo.value = tds[6].innerHTML;
		var pmnm = document.getElementById("pmnm");
		pmnm.value = tds[11].innerHTML;
		var measure = document.getElementById("measure");
		measure.value = tds[12].innerHTML;
		var storageTime = document.getElementById("storageTime");
		storageTime.value = tds[13].innerHTML;
		var manuf = document.getElementById("manufacturer");
		manuf.value = tds[15].innerHTML;
		var keeper = document.getElementById("keeper");
		keeper.value = tds[1].innerHTML;
		var price = document.getElementById("price");
		price.value = tds[7].innerHTML;
		var productmodel = document.getElementById("productmodel");
		productmodel.value = tds[4].innerHTML;
	}
}*/

function radioSelect(event) {
	var add = document.getElementById("addApply");
	if(event.checked) {
		var oTr = event.parentNode.parentNode;
		var tds = oTr.getElementsByTagName('td');
		preId = tds[0].getElementsByTagName('input')[1].value;
		add.removeAttribute("style");
	}else {
		add.setAttribute("style", "display:none;");
	}
}

function checkdNo(event) {
	var dNo = event.value;
	if(dNo != "") {
		$.ajax({
			type : "post",
			url : "ProductHandleServlet?operate=checkDeviceNo",
			data : {
				'dNo' : dNo
			},
			dataType : "text",
			success : function(data) {
				if (data == "1") {
				} else if (data == "0") {
					alert("机号存在，请重新填写");
					event.value ="";
				}

			},
			error : function() {
				alert("请求失败");
			}
		});
	}
	
}