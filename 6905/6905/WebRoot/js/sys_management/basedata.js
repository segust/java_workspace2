// JavaScript Document
/*
 * function check9831() { var oCurrentCheckBox =
 * document.getElementsByName("checkbox9831"); // 创建二维数组将选中的信息保存 var oArray =
 * new Array(); //var str=""; for (var i = 0; i < oCurrentCheckBox.length; i++) {
 * oArray[i] = new Array(); if (oCurrentCheckBox[i].checked){ var oCurrentTd =
 * oCurrentCheckBox[i].parentNode.parentNode .getElementsByTagName('td'); //j<oCurrentTd-1表示不存入checkbox那一列
 * for (var j = 0; j < oCurrentTd.length - 1; j++) { oArray[i][j] = oCurrentTd[j +
 * 1].innerHTML; //str+=oArray[i][j]; } } else oArray[i]=null; } }
 * 
 * 
 * window.onload=function(){ var
 * oChooseAll=document.getElementById('delete_all'); addOnclick(oChooseAll); }
 */
var oArray = new Array();

function addOnclick(obj) {
	obj.onclick = chooseAll;
}

function chooseAll() {
	var oCheckBoxLeader = document.getElementById("check_basedata_leader");
	var oInput = document.getElementsByName('check_basedata');
	if (oCheckBoxLeader.checked) {
		for (var i = 0; i < oInput.length; i++) {
			oInput[i].checked = true;
		}
	} else {
		for (var i = 0; i < oInput.length; i++) {
			oInput[i].checked = false;
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
function getchoose() {
	var oCurrentCheckBox = document.getElementsByName("check_basedata");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			var val = oCurrentTd[0].getElementsByTagName("input")[1].value;
			oArray[m] = val;
			m++;
		}
	}

	// 创建二维数组将选中的信息保存
	// alert(arrayToJson(oArray));

	$
			.ajax({
				type : "post",
				data : {
					'data' : arrayToJson(oArray)
				},
				url : "ServiceOfBaseDataServlet?operate=delete",
				dataType : "text",
				success : function(data) {
					if (data == "1") {
						// alert("删除成功！");
						window.wxc
								.xcConfirm(
										"删除成功 ",
										"success",
										{
											onOk : function() {
												for (var i = 0; i < oCurrentCheckBox.length; i++) {
													if (oCurrentCheckBox[i].checked) {
														var oCurrentTr = oCurrentCheckBox[i].parentNode.parentNode;
														oCurrentTr.style.display = "none";
														oCurrentCheckBox[i].checked = false;
														// $(oCurrentTr).remove();
													}
												}
											}
										});
					} else if (data == "0") {
						// alert("删除失败");
						window.wxc.xcConfirm("删除失败", "warning");
					}
				},
				error : function() {
					// alert("请求失败");
					window.wxc.xcConfirm("请求失败", "warning");
				}
			});
}
/*
 * function updateUnit(event){ var tr = event.parentNode.parentNode; var tds =
 * tr.getElementsByTagName("td"); for (var i = 0; i < tds.length; i++){
 * if(i==1){ oArray.PMNM=tds[i+1].innerHTML; } if(i==2){
 * obj.UnitName=tds[i+1].innerHTML; } if(i==3){ obj.price=tds[i+1].innerHTML; }
 * if(i==4){ obj.measure=tds[i+1].innerHTML; } } //把获取到的数据传给后台 jQuery.ajax({
 * type:"post", url:"ServiceOfBaseDataServlet?operate=updateUnit",
 * data:{'data':arrayToJson(obj)}, dataType:"text" }); }
 */

function updateUnit(event) {
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName("td");
	for (var i = 0; i < tds.length; i++) {
		if (i == 0) {
			oArray.unitId = tds[i].getElementsByTagName("input")[1].value;
		}
		if (i == 2) {
			oArray.UnitName = tds[i].getElementsByTagName("input")[0].value;
		}
		if (i == 3) {
			oArray.price = tds[i].getElementsByTagName("input")[0].value;
		}
		if (i == 4) {
			oArray.measure = tds[i].getElementsByTagName("input")[0].value;
		}
	}

	// 把获取到的数据传给后台
	jQuery
			.ajax({
				type : "post",
				url : "ServiceOfBaseDataServlet?operate=updateUnit&unitId="
						+ oArray.unitId + "&unitname=" + oArray.UnitName
						+ "&measure=" + oArray.measure + "&price="
						+ oArray.price,
				success : function(msg) {
					if (msg == "1") {
						// alert("修改成功");
						window.wxc
								.xcConfirm(
										"修改成功 ",
										"success",
										{
											onOk : function() {
												window.location.href = "ServiceOfBaseDataServlet?operate=selectUnit";
											}
										});
					} else if (msg == "0") {
						// alert("修改失败");
						window.wxc.xcConfirm("修改失败 ", "error");
					}

				},
				error : function() {
					window.wxc.xcConfirm("修改失败","warning");
				}
			});

}

function deleteUnit(event) {
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName("td");

	oArray.unitId = tds[0].getElementsByTagName("input")[0].value;
	oArray.FKPMNM = tds[0].getElementsByTagName("input")[1].value;

	window.wxc
			.xcConfirm(
					"是否确定删除？ ",
					"confirm",
					{
						onOk : function() {
							// 把获取到的数据传给后台
							jQuery
									.ajax({
										type : "post",
										url : "ServiceOfBaseDataServlet?operate=deleteUnit&unitId="
												+ oArray.unitId,
										success : function(msg) {
											if (msg == "1") {
												// alert("删除成功");
												window.wxc
														.xcConfirm(
																"删除成功",
																"success",
																{
																	onOk : function() {
																		window.location.href = "ServiceOfBaseDataServlet?operate=selectUnit&PMNM="
																				+ oArray.FKPMNM;
																	}
																});

												// $(tr).remove();
											} else if (msg == "0") {
												// alert("删除失败");
												window.wxc.xcConfirm("删除失败 ",
														"error");
											}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败","warning");
				}
			});
						}});
}

function outdatabase() {
	jQuery
			.ajax({
				type : "post",
				url : "ServiceOfBaseDataServlet?operate=exportBaseDataAndUnit",
				success : function(data) {
					if (data == 0) {
						window.wxc.xcConfirm("导出失败","warning");		
					} else {
						window.location.href = "ServiceOfBaseDataServlet?operate=download&path="
								+ data;
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败","warning");	
				}
			});
}

function deletebasedata(event) {
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName("td");
	var id = tds[0].getElementsByTagName("input")[1].value;
	window.wxc
			.xcConfirm(
					"是否确定删除？ ",
					"confirm",
					{
						onOk : function() {
							// 把获取到的数据传给后台
							jQuery
									.ajax({
										type : "post",
										url : "ServiceOfBaseDataServlet?operate=del&id="
												+ id,
										success : function(data) {
											if (data == "1") {
												// alert("删除成功");
												window.wxc
														.xcConfirm(
																"删除成功 ",
																"success",
																{
																	onOk : function() {
																		window.location.href = "SystemManagementServlet?operate=basedata";
																	}
																});
											} else if (data == "0") {
												// alert("删除失败");
												window.wxc.xcConfirm("删除失败 ",
														"error");
											}

										},
										error : function() {
											// alert("请求失败");
											window.wxc.xcConfirm("请求失败 ",
													"error");
										}
									});
						}
					});
}

function addOneUnit(basePath) {
	var FKPMNM = document.getElementById("FKPMNM").value;
	var PMNM = document.getElementById("PMNM").value;
	var PMBM = document.getElementById("PMBM").value;
	var QCBM = document.getElementById("QCBM").value;
	var PMCS = document.getElementById("PMCS").value;
	var XHTH = document.getElementById("XHTH").value;
	var JLDW = document.getElementById("JLDW").value;
	var CKDJ = document.getElementById("CKDJ").value;
	var BZTJ = document.getElementById("BZTJ").value;
	var BZJS = document.getElementById("BZJS").value;
	var BZZL = document.getElementById("BZZL").value;
	var QCXS = document.getElementById("QCXS").value;
	var MJYL = document.getElementById("MJYL").value;
	var XHDE = document.getElementById("XHDE").value;
	var XLDJ = document.getElementById("XLDJ").value;
	var SCCJNM = document.getElementById("SCCJNM").value;
	var GHDWNM = document.getElementById("GHDWNM").value;
	var ZBSX = document.getElementById("ZBSX").value;
	var SCDXNF = document.getElementById("SCDXNF").value;
	var LBQF = document.getElementById("LBQF").value;
	var YJDBZ = document.getElementById("YJDBZ").value;
	var SYBZ = document.getElementById("SYBZ").value;
	var SCBZ = document.getElementById("SCBZ").value;
	jQuery
			.ajax({
				type : "post",
				data:{
					"operate":'addOneUnit',
					"FKPMNM":FKPMNM,
					"PMNM":PMNM,
					"PMBM":PMBM,
					"QCBM":QCBM,
					"PMCS":PMCS,
					"XHTH":XHTH,
					"JLDW":JLDW,
					"CKDJ":CKDJ,
					"BZTJ":BZTJ,
					"BZJS":BZJS,
					"BZZL":BZZL,
					"QCXS":QCXS,
					"MJYL":MJYL,
					"XHDE":XHDE,
					"XLDJ":XLDJ,
					"SCCJNM":SCCJNM,
					"GHDWNM":GHDWNM,
					"ZBSX":ZBSX,
					"SCDXNF":SCDXNF,
					"LBQF":LBQF,
					"YJDBZ":YJDBZ,
					"SYBZ":SYBZ,
					"SCBZ":SCBZ
				},
				url : "ServiceOfBaseDataServlet",
				success : function(data) {
					if (data == 0) {
						window.wxc.xcConfirm("添加失败 ！ ", "error");
					} else {
						window.wxc
								.xcConfirm(
										"添加成功 ！ ",
										"success",
										{
											onOk : function() {
												window.location.href = basePath+"ServiceOfBaseDataServlet?operate=selectUnit&PMNM="
														+FKPMNM;
											}
										});
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败 ", "error");
//	if (confirm('确定删除该条记录吗？')) {
//		// 把获取到的数据传给后台
//		jQuery.ajax({
//			type : "post",
//			url : "ServiceOfBaseDataServlet",
//			data : {
//				operate : 'del',
//				id : id
//			},
//			success : function(data) {
//				if (data == "1") {
//					window.wxc.xcConfirm("删除成功","success");
//					tr.style.display = "none";
//				} else if (data == "0") {
//					window.wxc.xcConfirm("删除失败","warning");
//				}
			}});
//	}
}

function updateBasedata() {
	var id = document.getElementById("id").value;
	var PMNM = document.getElementById("PMNM").value;
	var PMBM = document.getElementById("PMBM").value;
	var QCBM = document.getElementById("QCBM").value;
	var PMCS = document.getElementById("PMCS").value;
	var XHTH = document.getElementById("XHTH").value;
	var JLDW = document.getElementById("JLDW").value;
	var CKDJ = document.getElementById("CKDJ").value;
	var BZTJ = document.getElementById("BZTJ").value;
	var BZJS = document.getElementById("BZJS").value;
	var BZZL = document.getElementById("BZZL").value;
	var QCXS = document.getElementById("QCXS").value;
	var MJYL = document.getElementById("MJYL").value;
	var XHDE = document.getElementById("XHDE").value;
	var XLDJ = document.getElementById("XLDJ").value;
	var SCCJNM = document.getElementById("SCCJNM").value;
	var GHDWNM = document.getElementById("GHDWNM").value;
	var ZBSX = document.getElementById("ZBSX").value;
	var SCDXNF = document.getElementById("SCDXNF").value;
	var LBQF = document.getElementById("LBQF").value;
	var YJDBZ = document.getElementById("YJDBZ").value;
	var SYBZ = document.getElementById("SYBZ").value;
	var SCBZ = document.getElementById("SCBZ").value;
	var ZBBDSJ = document.getElementById("ZBBDSJ").value;
	
	window.wxc
	.xcConfirm(
			"是否提交修改？ ",
			"confirm",
			{
				onOk : function() {	
				jQuery
				.ajax({
					type : "post",
					data:{
						"operate":'save',
						"id":id,
						"PMNM":PMNM,
						"PMBM":PMBM,
						"QCBM":QCBM,
						"PMCS":PMCS,
						"XHTH":XHTH,
						"JLDW":JLDW,
						"CKDJ":CKDJ,
						"BZTJ":BZTJ,
						"BZJS":BZJS,
						"BZZL":BZZL,
						"QCXS":QCXS,
						"MJYL":MJYL,
						"XHDE":XHDE,
						"XLDJ":XLDJ,
						"SCCJNM":SCCJNM,
						"GHDWNM":GHDWNM,
						"ZBSX":ZBSX,
						"SCDXNF":SCDXNF,
						"LBQF":LBQF,
						"YJDBZ":YJDBZ,
						"SYBZ":SYBZ,
						"SCBZ":SCBZ,
						"ZBBDSJ":ZBBDSJ
					},
					url : "ServiceOfBaseDataServlet",
					success : function(data) {
						if (data == 0) {
							window.wxc.xcConfirm("修改失败 ！ ", "error");
						} else {
							window.wxc
									.xcConfirm(
											"修改成功 ！ ",
											"success",
											{
												onOk : function() {
													window.location.href = "SystemManagementServlet?operate=basedata";
												}
											});
						}
					},
					error : function() {
						window.wxc.xcConfirm("请求失败 ", "warning");
					}
				});}});
}