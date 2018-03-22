var obj = new Array();
var obj1 = new Array();
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
			for ( var i = 0; i < o.length; i++) {
				r.push(arrayToJson(o[i]));
			}
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
}

// 按设备统计 内层 向后台发送请求
function out_detail_product(targ) {
	var tr = targ.parentNode.parentNode, tds = tr.getElementsByTagName("td"), oProductModel = tds[3].innerHTML, oProductUnit = tds[4].innerHTML;
	// 把获取到的数据传给后台
	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet",
		data : {
			operate : 'productOprateDetail',
			productModel : oProductModel,
			productUnit : oProductUnit
		},
		dataType : "json",
		success : function(data) {
			createdetail_by_productTable(data);
		}
	});
	$(document).ready(function() {
		$("#whole").show();
		$("#detail_by_product").show();
	});
	$(document).ready(function() {
		$("#sure_product").click(function() {
			$("#whole").hide();
			$("#detail_by_product").hide();
			deletedetail_by_productTable();
		});
	});
}

// 根据返回数据 构造 按设备统计 内层页面
function createdetail_by_productTable(detail) {
	var t = document.getElementById("detail_by_product_table"), tbody = document
			.createElement("tbody"), oInSum = 0, oOutSum = 0, oContractPrice = 0, oDevicePrice = 0, oInPrice = 0, oOutPrice = 0, oInPriceSum = 0, oOutPriceSum = 0;
	if (detail.length > 0) {
		for ( var i = 0; i < detail.length; i++) {
			var tr = document.createElement("tr"); // 创建新的行
			if (i % 2 != 0)
				tr.className = "addTrColor";
			for ( var j = 0; j < 16; j++) {
				var td = document.createElement("td"); // 创建新的列
				switch (j) {
				case 0:
					td.innerHTML = detail[i].contractId;
					break;
				case 1:
					td.innerHTML = detail[i].productName;
					break;
				case 2:
					td.innerHTML = detail[i].productModel;
					break;
				case 3:
					td.innerHTML = detail[i].productUnit;
					break;
				case 4:
					td.innerHTML = detail[i].productPrice;
					break;
				case 5:
					td.innerHTML = detail[i].productNum;
					break;
				case 6:
					td.innerHTML = detail[i].productInNum;
					oInSum += Number(detail[i].productInNum);
					break;
				case 7:
					td.innerHTML = detail[i].productOutNum;
					oOutSum += Number(detail[i].productOutNum);
					break;
				case 8:
					oInPrice = (Number(detail[i].productInNum) * Number(detail[i].productPrice))
							.toFixed(2);
					td.innerHTML = oInPrice;
					oInPriceSum += Number(oInPrice);
					break;
				case 9:
					oOutPrice = (Number(detail[i].productOutNum) * Number(detail[i].productPrice))
							.toFixed(2);
					td.innerHTML = oOutPrice;
					oOutPriceSum += Number(oOutPrice);
					break;
				case 10:
					td.innerHTML = (Number(detail[i].productNum) * Number(detail[i].productPrice))
							.toFixed(2);
					oDevicePrice += Number(detail[i].productNum)
							* Number(detail[i].productPrice);
					break;
				case 11:
					td.innerHTML = detail[i].manufacturer;
					break;
				case 12:
					td.innerHTML = detail[i].keeper;
					break;
				case 13:
					td.innerHTML = detail[i].signDate;
					break;
				case 14:
					td.innerHTML = detail[i].totalNumber;
					break;
				case 15:
					td.innerHTML = Number(detail[i].contractPrice).toFixed(2);
					oContractPrice += Number(detail[i].contractPrice);
					break;
				default:
					break;
				}
				tr.appendChild(td);
			}
			tbody.appendChild(tr);
		}
		oContractPrice = oContractPrice.toFixed(2);
		oDevicePrice = oDevicePrice.toFixed(2);
		oInPriceSum = oInPriceSum.toFixed(2);
		oOutPriceSum = oOutPriceSum.toFixed(2);
		t.appendChild(tbody);
		var p = t.parentNode.getElementsByTagName("p")[0];
		$(p).html(
				"在库总量：" + oInSum + "&nbsp;&nbsp;&nbsp;&nbsp;出库总量：" + oOutSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;合同总金额：" + oContractPrice
						+ "<br>在库设备总金额：" + oInPriceSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;出库设备总金额：" + oOutPriceSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;设备总金额：" + oDevicePrice
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（金额=单价×数量）");
	} else {
		alert("没有数据");
		return;
	}
}

// 在 按设备统计 内层 点击确定后清除数据
function deletedetail_by_productTable() {
	var t = document.getElementById("detail_by_product_table"), length = t.rows.length, p = t.parentNode
			.getElementsByTagName("p")[0];
	for ( var i = 1; i < length; i++) {
		t.deleteRow(1);
	}
	$(p).html("");
}

// 按合同统计 内层 向后台发送请求
function out_detail_contract(targ) {
	var tr = targ.parentNode.parentNode, tds = tr.getElementsByTagName("td"), oContractId = tds[2].innerHTML;
	// 把获取到的数据传给后台
	jQuery.ajax({
		type : "post",
		url : "StatisticsQueryServlet",
		data : {
			operate : 'contractOprateDetail',
			contractId : oContractId
		},
		dataType : "json",
		success : function(data) {
			createdetail_by_contractTable(data);
		}
	});
	$(document).ready(function() {
		$("#whole").show();
		$("#detail_by_contract").show();
	});
	$(document).ready(function() {
		$("#sure_contract").click(function() {
			$("#whole").hide();
			$("#detail_by_contract").hide();
			deletedetail_by_contractTable();
		});
	});
}

// 按合同统计，查看产品信息
function createdetail_by_contractTable(detail) {
	var t = document.getElementById("detail_by_contract_table"), tbody = document
			.createElement("tbody"), oInSum = 0, oOutSum = 0, oInPriceSum = 0, oOutPriceSum = 0, oPriceSum = 0, oInPrice = 0, oOutPrice = 0, oPrice = 0, oOutInfoOperButton = "<input type='button' onclick='outInfoByProd(this)' value='查看'>";
	if (detail.length > 0) {
		for ( var i = 0; i < detail.length; i++) {
			var tr = document.createElement("tr"); // 创建行
			if (i % 2 != 0)
				tr.className = "addTrColor";
			for ( var j = 0; j < 14; j++) {
				var td = document.createElement("td"); // 创建列
				switch (j) {
				case 0:
					td.innerHTML = detail[i].productName;
					break;
				case 1:
					td.innerHTML = detail[i].productModel;
					break;
				case 2:
					td.innerHTML = detail[i].productUnit;
					break;
				case 3:
					td.innerHTML = detail[i].productPrice;
					break;
				case 4:
					td.innerHTML = detail[i].productNum;
					break;
				case 5:
					td.innerHTML = detail[i].productInNum;
					oInSum += Number(detail[i].productInNum);
					break;
				case 6:
					td.innerHTML = detail[i].productOutNum;
					oOutSum += Number(detail[i].productOutNum);
					break;
				case 7:
					oInPrice = (Number(detail[i].productPrice) * Number(detail[i].productInNum))
							.toFixed(2);
					td.innerHTML = oInPrice;
					oInPriceSum += Number(oInPrice);
					break;
				case 8:
					oOutPrice = (Number(detail[i].productPrice) * Number(detail[i].productOutNum))
							.toFixed(2);
					td.innerHTML = oOutPrice;
					oOutPriceSum += Number(oOutPrice);
					break;
				case 9:
					oPrice = (Number(detail[i].productPrice) * Number(detail[i].productNum))
							.toFixed(2);
					td.innerHTML = oPrice;
					oPriceSum += Number(oPrice);
					break;
				case 10:
					td.innerHTML = detail[i].operateTime;
					break;
				case 11:
					td.innerHTML = oOutInfoOperButton;
					break;
				case 12:
					td.innerHTML = detail[i].manufacturer;
					break;
				case 13:
					td.innerHTML = detail[i].keeper;
					break;
				default:
					break;
				}
				tr.appendChild(td);
			}
			tbody.appendChild(tr);
		}
		oPriceSum = oPriceSum.toFixed(2);
		oInPriceSum = oInPriceSum.toFixed(2);
		oOutPriceSum = oOutPriceSum.toFixed(2);
		t.appendChild(tbody);
		var p = t.parentNode.getElementsByTagName("p")[0];
		$(p).html(
				"在库总量：" + oInSum + "&nbsp;&nbsp;&nbsp;&nbsp;出库总量：" + oOutSum
						+ "<br>在库设备总金额：" + oInPriceSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;出库设备总金额：" + oOutPriceSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;设备总金额：" + oPriceSum
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（金额=单价×数量）");
	} else {
		alert("没有数据");
		return;
	}
}

// 在 按合同统计 内层 查看当前合同的产品 点击确定后清除数据
function deletedetail_by_contractTable() {
	var t = document.getElementById("detail_by_contract_table"), length = t.rows.length, p = t.parentNode
			.getElementsByTagName("p")[0];
	for ( var i = 1; i < length; i++) {
		t.deleteRow(1);
	}
	$(p).html("");
}

// 在 查看当前合同下的产品 点击查看出库时间
function outInfoByProd(targ) {
	var oTds = targ.parentNode.parentNode.getElementsByTagName("td"), oProdModel = oTds[1].innerHTML, oProdUnit = oTds[2].innerHTML, oOperTime = oTds[10].innerHTML;
	$.ajax({
		url : "StatisticsQueryServlet",
		type : "POST",
		data : {
			operate : "outInfoByProd",
			productModel : oProdModel,
			productUnit : oProdUnit,
			operateTime : oOperTime
		},
		dataType : "json",
		success : function(data) {
			var oProdInfo="产品名称："+oTds[0].innerHTML+"&nbsp;&nbsp;&nbsp;&nbsp;产品型号："+oProdModel+"&nbsp;&nbsp;&nbsp;&nbsp;产品单元："+oProdUnit+"<br><br>统一入库时间："+oOperTime;
			create_outinfo_table(data,oProdInfo);
		}
	});
}

// 拼接产品的出库信息
function create_outinfo_table(oOutInfo,oProdInfo) {
	var oTable = document.getElementById("outinfo_by_product_table"), oTbody = document
			.createElement("tbody");
	if (oOutInfo.length > 0) {
		for ( var i = 0; i < oOutInfo.length; i++) {
			var oTr = document.createElement("tr"); // 创建新的行
			if (i % 2 != 0)
				oTr.className = "addTrColor";
			for ( var j = 0; j < 2; j++) {
				var oTd = document.createElement("td"); // 创建新的列
				switch (j) {
				case 0:
					oTd.innerHTML = oOutInfo[i].outTime;
					break;
				case 1:
					oTd.innerHTML = oOutInfo[i].outNum;
					break;
				default:
					break;
				}
				oTr.appendChild(oTd);
			}
			oTbody.appendChild(oTr);
		}
		oTable.appendChild(oTbody);
		var p = oTable.parentNode.getElementsByTagName("p")[0];
		$(p).html(oProdInfo);
	} else {
		window.wxc.xcConfirm("没有出库信息","info");
		//alert("没有数据");
		return;
	}
	$(document).ready(function() {
		$("#outinfo_by_product").show();
	});
	$(document).ready(function() {
		$("#sure_outinfo").click(function() {
			$("#outinfo_by_product").hide();
			deleteOutInfo();
		});
	});
}

// 点击出库信息页面确定后 清除出库信息
function deleteOutInfo() {
	var t = document.getElementById("outinfo_by_product_table"), 
		length = t.rows.length, 
		p = t.parentNode.getElementsByTagName("p")[0];
	for ( var i = 1; i < length; i++) {
		t.deleteRow(1);
	}
	$(p).html("");
}