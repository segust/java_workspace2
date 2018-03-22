
var objs = new Array();
var pros = new Array();
var index = 0;
//记录添加的申请信息
function addApply(basePath) {
	if (true) {
		var means = document.getElementById("outMeans").innerHTML;
		var batch = document.getElementById("batch").value;
//		var wholename = document.getElementById("wholename").value;
//		var unit = document.getElementById("productunit").value;
//		var dNo = document.getElementById("deviceNo").value;
//		var pmnm = document.getElementById("pmnm").value;
//		var measure = document.getElementById("measure").value;
//		var manuf = document.getElementById("manufacturer").value;
//		var keeper = document.getElementById("keeper").value;
//		var price = document.getElementById("price").value;
//		var location = document.getElementById("location").value;
//		var makeTime = document.getElementById("pDate").value;
//		var maintain = document.getElementById("maintain").value;
		var outTime = document.getElementById("borrowLength").value;
		var reason = document.getElementById("borrowReason").value;
		var remark = document.getElementById("remark").value;
		var execTime = document.getElementById("operateTime").innerHTML;
		var obj = new createApply(means, batch,outTime,reason,remark,execTime);
		objs.push(obj);

		/*var list = document.getElementById("addprolist");
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		msg.setAttribute("style", "display:none;");
		submit.setAttribute("style", "display:block;");
		var span = document.createElement("span");
		span.setAttribute("style", "margin-top:10px;");
		span.innerHTML = "新增申请:机号&nbsp;<a href='javascript:void(0)' onclick='setPro(objs,"
				+ index
				+ ")'>"
				+ obj.dNo
				+ "</a>&nbsp;&nbsp;<input class='search-btn' type='button' value='删除' onclick='delcur(this,"
				+ index + ")'/><br/>";
		list.appendChild(span);
		index++;*/
		addPros();
		upObjs(obj,basePath);
	}
}

function addPros() {
	var oTable = document.getElementById("fare-table");
	var trs = oTable.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	for(var i=0;i<trs.length;i++) {
		var oTr = trs[i];
		var tds = oTr.getElementsByTagName('td');
		pros[i] = new Array();
		pros[i][0] = tds[2].innerHTML;
		pros[i][1] = tds[5].innerHTML;
	}
}

function setPro(objs, index) {
	var curObj = objs[index];
	var means = document.getElementById("mean-view");
	var batch = document.getElementById("batch-view");
	/*var name = document.getElementById("name-view");
	var unit = document.getElementById("unit-view");
	var device = document.getElementById("device-view");
	var pmnm = document.getElementById("pmnm-view");
	var measure = document.getElementById("measure-view");
	var manuf = document.getElementById("manu-view");
	var keeper = document.getElementById("keeper-view");
	var price = document.getElementById("price-view");
	var location = document.getElementById("location-view");
	var makeTime = document.getElementById("pdate-view");
	var maintain = document.getElementById("maintain-view");*/
	var outTime = document.getElementById("outtime-view");
	var reason = document.getElementById("reason-view");
	var remark = document.getElementById("remark-view");
	var operate = document.getElementById("operate-view");
	means.innerHTML = curObj.means;
	batch.innerHTML = curObj.batch;
	/*name.innerHTML = curObj.wholename;
	unit.innerHTML = curObj.unit;
	device.innerHTML = curObj.dNo;
	pmnm.innerHTML = curObj.pmnm;
	measure.innerHTML = curObj.measure;
	manuf.innerHTML = curObj.manuf;
	keeper.innerHTML = curObj.keeper;
	price.innerHTML = curObj.price;
	location.innerHTML = curObj.location;
	makeTime.innerHTML = curObj.makeTime;
	maintain.innerHTML = curObj.maintain;*/
	outTime.innerHTML = curObj.outTime;
	reason.innerHTML = curObj.reason;
	remark.innerHTML = curObj.remark;
	operate.innerHTML = curObj.execTime;
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
	var span = event.parentNode;
	var list = document.getElementById("addprolist");
	list.removeChild(span);
	if (list.getElementsByTagName("span").length == 0) {
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		msg.setAttribute("style", "display:block;");
		submit.setAttribute("style", "display:none;");
	}
	objs.splice(ind, 1);
}
//js申请表对象
function createApply(means, batch, outTime,reason,remark,execTime) {
	this.means = means;
	this.batch = batch;
	/*this.wholename = wholename;
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
*/	this.outTime = outTime;
	this.reason = reason;
	this.remark = remark;
	this.execTime = execTime;
	return this;
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

function upObjs(obj,basePath) {
	$.ajax({
		type : "post",
		url : "BorrowServlet?operate=BorrowOutWarehouse",
		data : {
			'data' : arrayToJson(obj),
			'pros': arrayToJson(pros)
		},
		dataType : "text",
		success : function(data) {
			if (data == "1") {
				//alert("提交成功");
				window.wxc.xcConfirm("提交成功","info",{onOk:function(){
					window.location.href = basePath+"InWarehouseServlet?operate=queryApply";
				}});
			} else if (data == "0") {
				//alert("提交失败");
				window.wxc.xcConfirm("提交失败","warning");
			}
		},
		error : function() {
			//alert("请求失败");
			window.wxc.xcConfirm("请求失败","warning");
		}
	});
	objs.slice(0, objs.length-1);
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