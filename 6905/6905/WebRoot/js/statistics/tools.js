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

function out_EquipmentDetailAccount(event) {
	var tr = event.parentNode.parentNode,
		tds = tr.getElementsByTagName("td"),
		product = tds[2].innerHTML,
		manufacturer = tds[6].innerHTML,
		productPrice = tds[7].innerHTML,
		QCBM = tds[8].innerHTML;
		var strs= new Array();
		strs = product.split("+");
		productName = strs[0];
		productModel = strs[1];
	//把获取到的数据传给后台
	jQuery
			.ajax({
				type : "post",
				url : "EquipmentServlet",
				data:{
					operate:'findEquipmentDetailAccount',
					productName:productName,
					productModel:productModel,
					manufacturer:manufacturer,
					productPrice:productPrice,
					QCBM:QCBM
				},
				dataType:"json",
				success : function(data) {
					createprodectdetail(data);
				}
			});
	$(document).ready(function() {
		$("#whole").show();
		$("#mingxitable").show();
	});
	$(document).ready(function() {
		$("#sure_detail_account").click(function() {
			$("#whole").hide();
			$("#mingxitable").hide();
			deletedetail_by_mxzTable();
		});
	});
}
function createprodectdetail(detail) {
	var t = document.getElementById("tem-table_mingxizhang"),
		tbody = document.createElement("tbody");
	if (detail.length > 0) {
		for ( var i = 0; i < detail.length; i++) {
			var tr =  document.createElement("tr");		//创建新的行 
			for ( var j = 0; j < 10; j++) {
				var td = document.createElement("td");	 //创建新的列 
				switch (j) {
				case 0:
					td.innerHTML = detail[i].year;
					break;
				case 1:
					td.innerHTML = detail[i].month;
					break;
				case 2:
					td.innerHTML = detail[i].day;
					break;
				case 3:
					td.innerHTML = detail[i].listId;
					break;
				case 4:
					td.innerHTML = detail[i].num;
					break;
				case 5:
					td.innerHTML = detail[i].keeper;
					break;
				case 6:
					td.innerHTML = detail[i].income;
					break;
				case 7:
					td.innerHTML = detail[i].out;
					break;
				case 8:
					td.innerHTML = detail[i].rest;
					break;
				case 9:
					td.innerHTML = detail[i].remark;
					break;
				default:
					break;
				}
				tr.appendChild(td);
			}
			tbody.appendChild(tr);
		}
		t.appendChild(tbody);
	} else {
//		alert("没有数据");
		window.wxc.xcConfirm("没有数据", "warning");
	}
}

//在 器材明细账 内层 点击确定后清除数据
function deletedetail_by_mxzTable() {
	var t = document.getElementById("tem-table_mingxizhang"), 
		length = t.rows.length;
	for ( var i = 1; i < length; i++) {
		t.deleteRow(1);
	}
}