//军代室检查前台抓取
function jdsCheckInspect() {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 0; j < oCurrentTd.length - 1; j++) {
				if (j == 0) {
					var val = oCurrentTd[j].getElementsByTagName("input")[1].value;
					oArray[m][j] = val;
				} else if (j == 13) {
					var val = oCurrentTd[j + 1].getElementsByTagName("input")[0].value;
					oArray[m][j] = val;
				} else {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	jQuery
			.ajax({
				type : "post",
				url : "InspectServlet?operate=record&JDInspect=0",
				data : {
					'data' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					window.wxc
							.xcConfirm(
									data,
									"success",
									{
										onOk : function() {
											window.location.href = "InspectServlet?operate=inspectQuery&mark=search&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=&restKeepTime=&JDInspect=";
										}
									});
					// // alert(data);
					// if (data == "保存成功！") {
					// window.location.href =
					// "InspectServlet?operate=inspectQuery&mark=search&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=&restKeepTime=&JDInspect=";
					// }

				},
				error : function() {
					window.wxc.xcConfirm("请求失败",
							window.wxc.xcConfirm.typeEnum.error);
					// alert("请求失败");
				}
			});
}
// 军代局检查前台抓取
function jdjCheckInspect() {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 0; j < oCurrentTd.length - 1; j++) {
				if (j == 0) {
					var val = oCurrentTd[j].getElementsByTagName("input")[1].value;
					oArray[m][j] = val;
				} else if (j == 13) {
					var val = oCurrentTd[j + 1].getElementsByTagName("input")[0].value;
					oArray[m][j] = val;
				} else {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	jQuery
			.ajax({
				type : "post",
				url : "InspectServlet?operate=record&JDInspect=1",
				data : {
					'data' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					window.wxc
							.xcConfirm(
									data,
									"success",
									{
										onOk : function() {
											window.location.href = "InspectServlet?operate=inspectQuery&mark=search&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=&restKeepTime=&JDInspect=";
										}
									});
					// // alert(data);
					// if (data == "保存成功！") {
					// window.location.href =
					// "InspectServlet?operate=inspectQuery&mark=search&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=&restKeepTime=&JDInspect=";
					// }

				},
				error : function() {
					window.wxc.xcConfirm("请求失败",
							window.wxc.xcConfirm.typeEnum.error);
					// alert("请求失败");
				}
			});
}
// 维护确认前台抓取
function checkmaintain() {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 0; j < oCurrentTd.length - 1; j++) {
				if (j == 0) {
					var val = oCurrentTd[j].getElementsByTagName("input")[1].value;
					oArray[m][j] = val;
				} else if (j == 13 || j == 14) {
					var val = oCurrentTd[j + 1].getElementsByTagName("input")[0].value;
					oArray[m][j] = val;
				} else {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	jQuery
			.ajax({
				type : "post",
				url : "Maintain?operateType=saveMaintainLog",
				data : {
					'data' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					window.wxc
							.xcConfirm(
									"记录设备维护成功",
									"info",
									{
										onOk : function() {
											window.location.href = "Maintain?operateType=maintainInfoQuery&curPageNum=1&pageSize=10";
										}
									});
					// alert(data);
					// if (data == "保存成功！") {
					// window.location.href =
					// "Maintain?operateType=maintainInfoQuery&curPageNum=1&pageSize=10";
					// }
				},
				error : function() {
					window.wxc.xcConfirm("请求失败", "warning");
					// alert("请求失败");
				}
			});
}

// 申请管理前台抓取
function checktransact(ownedUnit, version) {
	var exportType = "none";
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	if (!isNoCheck(oCurrentCheckBox)) {
		if (version == 1) {
			for (var i = 0; i < oCurrentCheckBox.length; i++) {
				if (oCurrentCheckBox[i].checked) {
					oArray[m] = new Array();
					var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
							.getElementsByTagName('td');
					var length = oCurrentTd.length;
					for (var j = 0; j < oCurrentTd.length; j++) {
						if (j == 0) {
							var val = oCurrentTd[j]
									.getElementsByTagName("input")[1].value;
							oArray[m][j] = val;
						} else if (j == 1) {
							oArray[m][j] = oCurrentTd[j + 1].innerHTML;
							exportType = oCurrentTd[j + 1].innerHTML;
						} else if (j == oCurrentTd.length - 1) {
							oArray[m][j] = ownedUnit;
						} else {
							oArray[m][j] = oCurrentTd[j + 1].innerHTML;
						}
					}
					m++;
				}
			}
		} else if (version == 2) {
			for (var i = 0; i < oCurrentCheckBox.length; i++) {
				if (oCurrentCheckBox[i].checked) {
					oArray[m] = new Array();
					var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
							.getElementsByTagName('td');
					var length = oCurrentTd.length;
					var k = 0;
					for (var j = 0; j < oCurrentTd.length - 2; j++) {
						if (j == 0) {
							var val = oCurrentTd[j]
									.getElementsByTagName("input")[1].value;
							oArray[m][k++] = val;
						} else if (j == 1) {
							oArray[m][k++] = oCurrentTd[j + 1].innerHTML;
							exportType = oCurrentTd[j + 1].innerHTML;
						} else if (j == oCurrentTd.length - 3) {
							oArray[m][j] = oCurrentTd[1]
									.getElementsByTagName('input')[0].value;
							;
						} else {
							oArray[m][j] = oCurrentTd[j + 1].innerHTML;
						}
					}
					m++;
				}
			}
		}
		// alert(oArray);
		switch (exportType) {
		case "新入库":
			exportType = "newIn";
			break;
		case "轮换入库":
			exportType = "circleIn";
			break;
		case "轮换出库":
			exportType = "circleOut";
			break;
		case "更新入库":
			exportType = "renewIn";
			break;
		case "更新出库":
			exportType = "renewOut";
			break;
		default:
			break;
		}
		// 创建二维数组将选中的信息保存
		jQuery
				.ajax({
					type : "post",
					url : "InWarehouseServlet?operate=exportSingleForm&exportType="
							+ exportType,
					data : {
						'outData' : arrayToJson(oArray)
					},
					dataType : "text",
					success : function(data) {
						if (data == 0) {
							// alert();
							window.wxc.xcConfirm("导出失败!", "warning");
						} else {
							window.location.href = "InWarehouseServlet?operate=download&absolutePath="
									+ data + "&exportType=" + exportType;
						}
					},
					error : function() {
						// alert("请求失败");
						window.wxc.xcConfirm("请求失败!", "warning");
					}
				});
	} else {
		var txt = "请至少选择一项进行导出";
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
	}

}

// 判断复选框一个都没有选中
function isNoCheck(checks) {
	var flag = true;
	for (var i = 0; i < checks.length; i++) {
		if (checks[i].checked) {
			flag = false;
			break;
		}
	}
	return flag;
}

// 更新出库时抓取
function checkUpdateOut(ownedUnit) {
	var exportType = "none";
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 0; j < oCurrentTd.length; j++) {
				if (j == 0) {
					var val = oCurrentTd[j].getElementsByTagName("input")[1].value;
					oArray[m][j] = val;
				} else if (j == 9) {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
					exportType = oCurrentTd[j + 1].innerHTML;
				} else if (j == oCurrentTd.length - 1) {
					oArray[m][j] = ownedUnit;
				} else {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	if (exportType == "新入库") {
		exportType = "newIn";
	} else if (exportType == "轮换入库") {
		exportType = "circleIn";
	} else if (exportType == "轮换出库") {
		exportType = "circleOut";
	} else if (exportType == "更新入库") {
		exportType = "renewIn";
	} else if (exportType == "更新出库") {
		exportType = "renewOut";
	}
	// 创建二维数组将选中的信息保存
	jQuery
			.ajax({
				type : "post",
				url : "InWarehouseServlet?operate=exportSingleForm&exportType="
						+ exportType,
				data : {
					'outData' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					if (data == 0) {
						window.wxc.xcConfirm("导出失败!", "warning");
					} else {
						window.location.href = "InWarehouseServlet?operate=download&absolutePath="
								+ data + "&exportType=" + exportType;
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败!", "warning");
				}
			});
}

function arrayToJson1(o) {
	var r = []; // []里面是放数组 {}里面是对象
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

// 弹框输入出库数量
// changed by lyt 1107
function outProduct(event, listId) {
	var tr = event.parentNode.parentNode;
	var PMNM = tr.getElementsByTagName("td")[2].innerHTML;
	var outMeans = tr.getElementsByTagName("td")[1].innerHTML;
	var type = "调拨";
	var path = "allot";
	if (outMeans == "发料更新出库") {
		type = "更新";
		path = "update";
	} else if (outMeans == "发料轮换出库") {
		type = "轮换";
		path = "turn";
	}
	var strs = tr.getElementsByTagName("td")[3].innerHTML.split('+');
	var productModel = strs[1];
	var leftCount = parseInt(tr.getElementsByTagName("td")[13].innerHTML, 10);
	// var count = prompt("请输入出库数量", 0);
	window.wxc
			.xcConfirm(
					"请输入出库数量:\t",
					"input",
					{
						onOk : function() {
							var count = $(".inputBox").val();
							if (count >= 0 && count <= leftCount) {
								jQuery
										.ajax({
											type : "post",
											url : "OutListServlet?operate=selectDeviceNoAndUpdateProStatus",
											dataType : "json",
											data : {
												'PMNM' : PMNM,
												'productModel' : productModel,
												'listId' : listId,
												'count' : count,
												'outType' : type
											},
											success : function(data) {
												var info = "请出库以下产品:", title = "\t产品型号\t机号\t摆放位置";
												if (data.length > 0) {
													var str = info + "\n"
															+ title + "\n";
													for (var i = 0; i < data.length; i++) {
														str += "\t"
																+ data[i].productModel
																+ "\t"
																+ data[i].deviceNo
																+ "\t"
																+ data[i].location
																+ "\n";
													}
													window.wxc
															.xcConfirm(
																	str,
																	"info",
																	{
																		onOk : function() {
																			window.location.href = "OutListServlet?operate=selectDetail&type="
																					+ path
																					+ "&listId="
																					+ listId;
																		}
																	});
												}
											},
											error : function() {
												window.wxc.xcConfirm("请求失败!",
														"warning");
											}
										});
							} else {
								// alert("输入数量非法，请输入正确数量");
								window.wxc.xcConfirm("输入数量非法，请输入正确数量",
										"warning");
							}
						}
					});

}

function outProductTurn(event) {
	var tr = event.parentNode.parentNode;
	var PMNM = tr.getElementsByTagName("td")[1].innerHTML;
	var productModel = tr.getElementsByTagName("td")[2].innerHTML;
	var listId = tr.getElementsByTagName("td")[13].innerHTML;
	var ownedUnit = tr.getElementsByTagName("td")[14].innerHTML;
	var outMeans = tr.getElementsByTagName("td")[15].innerHTML;
	var leftCount = parseInt(tr.getElementsByTagName("td")[12].innerHTML, 10);
	// var count = prompt("请输入出库数量");
	window.wxc
			.xcConfirm(
					"请输入出库数量:\t",
					"input",
					{
						onOk : function() {
							var count = $(".inputBox").val();
							if (count >= 0 && count <= leftCount) {
								jQuery
										.ajax({
											type : "post",
											url : "OutListServlet?operate=selectDeviceNoAndUpdateProStatus&PMNM="
													+ PMNM
													+ "&productModel="
													+ productModel
													+ "&listId="
													+ listId
													+ "&ownedUnit="
													+ ownedUnit
													+ "&outMeans="
													+ outMeans
													+ "&count="
													+ count,
											dataType : "text",
											success : function(data) {
												window.wxc
														.xcConfirm(
																data,
																"info",
																{
																	onOk : function() {
																		window.location.href = "OutListServlet?operate=selectOutListTurn";
																	}
																});
											},
											error : function() {
												window.wxc.xcConfirm("请求失败！",
														"warning");
											}
										});
							} else {
								// alert("输入数量非法，请输入正确数量");
								window.wxc.xcConfirm("输入数量非法，请输入正确数量",
										"warning");
							}
						}
					});

}

function outProductUpdate(event) {
	var tr = event.parentNode.parentNode;
	var PMNM = tr.getElementsByTagName("td")[1].innerHTML;
	var productModel = tr.getElementsByTagName("td")[2].innerHTML;
	var listId = tr.getElementsByTagName("td")[13].innerHTML;
	var ownedUnit = tr.getElementsByTagName("td")[14].innerHTML;
	var outMeans = tr.getElementsByTagName("td")[15].innerHTML;
	var leftCount = parseInt(tr.getElementsByTagName("td")[12].innerHTML, 10);
	// var count = prompt("请输入出库数量");
	window.wxc
			.xcConfirm(
					"请输入出库数量:\t",
					"input",
					{
						onOk : function() {
							var count = $(".inputBox").val();
							if (count >= 0 && count <= leftCount) {
								jQuery
										.ajax({
											type : "post",
											url : "OutListServlet?operate=selectDeviceNoAndUpdateProStatus&PMNM="
													+ PMNM
													+ "&productModel="
													+ productModel
													+ "&listId="
													+ listId
													+ "&ownedUnit="
													+ ownedUnit
													+ "&outMeans="
													+ outMeans
													+ "&count="
													+ count,
											dataType : "text",
											success : function(data) {
												window.wxc
														.xcConfirm(
																data,
																"info",
																{
																	onOk : function() {
																		window.location.href = "OutListServlet?operate=selectOutListUpdate";
																	}
																});
											},
											error : function() {
												window.wxc.xcConfirm("请求失败！",
														"warning");
											}
										});
							} else {
								// alert("输入数量非法，请输入正确数量");
								window.wxc.xcConfirm("输入数量非法，请输入正确数量",
										"warning");
							}
						}
					});

}
// 删除合同弹提示输入密码框 输入正确进入Servlet

function delContract(event) {
	var tr = event.parentNode.parentNode, id = tr.getElementsByTagName("td")[0].innerHTML, ex = '', txt = "请输入密码";
	window.wxc
			.xcConfirm(
					txt,
					window.wxc.xcConfirm.typeEnum.input,
					{
						onOk : function(v) {
							ex = v;

							jQuery
									.ajax({
										type : "post",
										url : "ContractHandleServlet?operate=checkpwd&contractid="
												+ id,
										data : {
											'pwd' : ex
										},
										dataType : "text",
										success : function(data) {
											window.wxc.xcConfirm(data, "info");

											if (data == "删除成功") {
												tr.style.display = "none";
											}
										},
										error : function() {
											window.wxc.xcConfirm("请求失败！",
													"warning");
										}
									});

						}
					});
	$(".txtBox .inputBox").attr('type', 'password');
}

// 删除附件 弹提示框跳转到Servlet
function deleteAttach(event) {
	var tr = event.parentNode.parentNode;
	var id = tr.getElementsByTagName("td")[0].innerHTML;
	window.wxc
			.xcConfirm(
					"确定删除'" + id + "'的附件吗?",
					"confirm",
					{
						onOk : function() {
							jQuery
									.ajax({
										type : "post",
										url : "ContractHandleServlet?operate=deleteAttach&contractid="
												+ id,
										dataType : "text",
										success : function(data) {
											if (data == "1") {
												// alert("附件删除成功");
												window.wxc
														.xcConfirm(
																"附件删除成功",
																"info",
																{
																	onOk : function() {
																		window.location.href = "ContractHandleServlet?operate=querycontract";
																	}
																});
											} else if (data == "0") {
												// alert("附件删除失败");
												window.wxc.xcConfirm("附件删除失败",
														"info");
											} else if (data == "2") {
												// alert("文件被占用，请稍后再试");
												window.wxc.xcConfirm(
														"文件被占用，请稍后再试", "info");
											}
										},
										error : function() {
											window.wxc.xcConfirm("请求失败",
													"warning");
										}
									});
						}
					});
	/*
	 * if (confirm('确定删除\"' + id + '\"的附件吗?')) { jQuery .ajax({ type : "post",
	 * url : "ContractHandleServlet?operate=deleteAttach&contractid=" + id,
	 * dataType : "text", success : function(data) { if (data == "1") {
	 * alert("附件删除成功"); window.location.href =
	 * "ContractHandleServlet?operate=querycontract"; } else if (data == "0") {
	 * alert("附件删除失败"); } else if (data == "2") { alert("文件被占用，请稍后再试"); } },
	 * error : function() { alert("请求失败"); } }); return true; } return false;
	 */
}

// 轮换出入库的字段有效性验证
function applySubmitValidate() {
	var num = eval(document.getElementById("Num")).value;
	var storageTime = eval(document.getElementById("storageTime")).value;
	var reg = new RegExp(/\d+/);
	var str = "";
	var flag = true;
	if (!reg.exec(num)) {
		if (num == "") {
			str += "数量为空\n";
			flag = false;
		} else {
			str += "数量不为数字\n";
			flag = false;
		}
	}
	if (!reg.exec(storageTime)) {
		str += "存储期限不为数字\n";
		flag = false;
	}
	if (storageTime == "") {
		str += "存储期限为空\n";
		flag = false;
	}
	if (str != "")
		alert(str);
	return flag;
}

// 申请轮换出库弹提示框点确认后提交到Servlet
// function addApply() {
// if (applySubmitValidate()) {
// if (confirm('确定提交申请吗?')) {
// jQuery.ajax({
// cache : true,
// type : "post",
// url : "BorrowServlet?operate=addOutapply&type=LHCK",
// data : $('#addApplyform').serialize(),// 你的formid
// async : false,
// error : function() {
// alert("Connection error");
// window.location.href = "BorrowServlet?operate=borrowInOut";
// },
// success : function(data) {
// if (data == "1") {
// alert("添加成功");
// window.location.href = "InWarehouseServlet?operate=queryApply";
// } else if (data == "0") {
// alert("添加失败");
// }
// }
// });
// return true;
// }
// return false;
// }
// }

// 申请轮换入库弹提示框点确认后提交到Servlet
function addInApply() {
	if (applySubmitValidate()) {
		window.wxc
				.xcConfirm(
						"确定提交申请吗?",
						"confirm",
						{
							onOk : function() {
								jQuery
										.ajax({
											cache : true,
											type : "post",
											url : "BorrowServlet?operate=addInapply&type=LHRK",
											data : $('#addApplyform')
													.serialize(),// 你的formid
											async : false,
											success : function(data) {
												if (data == "1") {
													window.wxc
															.xcConfirm(
																	"添加成功",
																	"info",
																	{
																		onOk : function() {
																			window.location.href = "InWarehouseServlet?operate=queryApply";
																		}
																	});
												} else if (data == "0") {
													window.wxc.xcConfirm(
															"添加失败", "warning");
												}
											},
											error : function() {
												window.wxc.xcConfirm("请求失败",
														"warning");
											}
										});
							}
						});
	}
}

// 轮换出入库的字段有效性验证
function updateInApplySubmitValidate() {
	var num = eval(document.getElementById("Num")).value;
	var price = eval(document.getElementById("Price")).value;
	var oldPrice = eval(document.getElementById("oldPrice")).value;
	var oldNum = eval(document.getElementById("oldNum")).value;
	var storageTime = eval(document.getElementById("StorageTime")).value;
	var reg = new RegExp(/\d+/);
	var str = "";
	var flag = true;
	if (!reg.exec(price)) {
		str += "单价不为数字\n";
		flag = false;
	}
	if (!reg.exec(num)) {
		str += "数量不为数字\n";
		flag = false;
	}
	if (!reg.exec(oldPrice)) {
		str += "原产品单价不为数字\n";
		flag = false;
	}
	if (!reg.exec(oldNum)) {
		str += "原产品数量不为数字\n";
		flag = false;
	}
	if (!reg.exec(storageTime)) {
		str += "存储期限不为数字\n";
		flag = false;
	}
	if (price == "") {
		str += "单价为空\n";
		flag = false;
	}
	if (num == "") {
		str += "数量为空\n";
		flag = false;
	}
	if (oldPrice == "") {
		str += "原产品单价为空\n";
		flag = false;
	}
	if (oldNum == "") {
		str += "原产品数量为空\n";
		flag = false;
	}
	if (storageTime == "") {
		str += "存储期限为空\n";
		flag = false;
	}
	if (str != "")
		window.wxc.xcConfirm(str, "warning");
	return flag;
}

// delete by liuyutian
// 这是李梦馨的废弃的？
/*
 * // 轮换更新弹提示框点确认后提交到Servlet function addUpdateInApply() { if
 * (updateInApplySubmitValidate()) { if (confirm('确定提交更新申请吗?')) { jQuery .ajax({
 * cache : true, type : "post", url :
 * "BorrowServlet?operate=addInapply&type=GXRK", data :
 * $('#addApplyform').serialize(),// 你的formid async : false, error : function() {
 * alert("连接失败"); window.location.href = "BorrowServlet?operate=updateInOut"; },
 * success : function(data) { if (data == "1") { alert("添加成功");
 * window.location.href = "InWarehouseServlet?operate=queryApply"; } else if
 * (data == "0") { alert("添加失败"); } } }); return true; } return false; }
 *  }
 */

/*
 * // 轮换更新弹提示框点确认后提交到Servlet function addUpdateOutApply() { if
 * (updateInApplySubmitValidate()) { if (confirm('确定提交更新申请吗?')) { jQuery .ajax({
 * cache : true, type : "post", url :
 * "BorrowServlet?operate=addOutapply&type=GXCK", data :
 * $('#addApplyform').serialize(),// 你的formid async : false, error : function() {
 * alert("连接失败"); window.location.href = "BorrowServlet?operate=updateInOut"; },
 * success : function(data) { if (data == "1") { alert("添加成功");
 * window.location.href = "InWarehouseServlet?operate=queryApply"; } else if
 * (data == "0") { alert("添加失败"); } } }); return true; } return false; }
 *  }
 */

function jdsCheck() {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox

	var m = 0;
	var data1 = [];
	var type = "none";
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {

			var oCurrentTd = oCurrentCheckBox[i].parentNode
					.getElementsByTagName("input");
			// data1[m]=oCurrentTd[1].value+","+oCurrentTd[2].value;
			data1[m] = oCurrentTd[1].value;
			type = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName("td")[9].innerHTML;
			m++;
		}
	}

	var data1 = arrayToJson(data1); // 将数组转换成json格式
	window.wxc
			.xcConfirm(
					"确定将申请'" + data1 + "'审核通过吗?",
					"confirm",
					{
						onOk : function() {
							jQuery
									.ajax({
										type : "post",
										url : "InWarehouseServlet?operate=jdsCheck&opFlag=1&checkType="
												+ type,
										data : {
											'outData' : data1
										},
										dataType : "text",
										success : function(data) {
											window.wxc
													.xcConfirm(
															"审核通过操作已完成",
															"info",
															{
																onOk : function() {
																	window.location.href = "InWarehouseServlet?operate=listQueryApply";
																}
															});
										},
										error : function() {
											window.wxc.xcConfirm("请求失败",
													"warning");
										}
									});
						}
					});
}

function jdsNoCheck() {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox

	var m = 0;
	var data1 = [];
	var checkType = "none";
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {

			var oCurrentTd = oCurrentCheckBox[i].parentNode
					.getElementsByTagName("input");
			data1[m] = oCurrentTd[1].value;
			checkType = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName("td")[9].innerHTML;
			m++;
		}
	}

	var data1 = arrayToJson(data1); // 将数组转换成json格式
	window.wxc
			.xcConfirm(
					"确定将申请'" + data1 + "'审核不通过吗?",
					"confirm",
					{
						onOk : function() {
							jQuery
									.ajax({
										type : "post",
										url : "InWarehouseServlet?operate=jdsCheck&opFlag=2&checkType="
												+ checkType,
										data : {
											'outData' : data1
										},
										dataType : "text",
										success : function(data) {
											window.wxc
													.xcConfirm(
															"审核通过操作已完成",
															"info",
															{
																onOk : function() {
																	window.location.href = "InWarehouseServlet?operate=listQueryApply";
																}
															});
										},
										error : function() {
											window.wxc.xcConfirm("请求失败",
													"warning");
										}
									});
						}
					});
}

// 确认出库
function confirmOut() {
	var ids = document.getElementsByName("pid");
	var date = document.getElementById("mydate").value;
	var values = new Array();
	for (var i = 0; i < ids.length; i++) {
		values.push(ids[i].value);
	}
	jQuery.ajax({
		cache : true,
		type : "post",
		url : "OutWarehouseServlet?operate=confirmOut",
		async : false,
		data : {
			'data' : arrayToJson(values),
			'date' : date
		},
		error : function() {
			// alert("连接失败，请检查网络！");
			window.wxc.xcConfirm("连接失败，请检查网络！", "warning");
		},
		success : function(data) {
			if (data == "1") {
				window.wxc.xcConfirm("确认成功", "info");
				$("#confirm-btn").css("display", "none");
				$("#confirm-msg").css("display", "block");
				msg.display = "block";
			} else if (data == "0") {
				window.wxc.xcConfirm("确认失败", "warning");
			}
		}
	});

}

/* 添加一行 */
function addRow() {
	var oTr = document.createElement('tr');
	var oFragment = document.createDocumentFragment();
	for (var i = 0; i < 11; i++) {
		var oTd = document.createElement('td');
		var oInput = document.createElement('input');
		oInput.type = "text";
		oTd.appendChild(oInput);
		oFragment.appendChild(oTd);
	}
	oTr.appendChild(oFragment);
	var oTbody = document.getElementsByTagName('tbody');
	oTbody[0].appendChild(oTr);
}

/* 删除一行 */
function removeRow() {
	var oTable = document.getElementById("list-content");
	var trs = oTable.getElementsByTagName("tr");
	var oTr = trs[trs.length - 1];
	var oOrder = oTr.getElementsByTagName("td")[0].innerHTML;
	if (oOrder != "1") {
		oTable.removeChild(oTr);
	} else {
		// alert("不得删除第一行");
		window.wxc.xcConfirm("不得删除第一行", "warning");
	}
}

/* 封装表格数据 */
function createData() {

	var arrayOuter = [], arrayInner = [], oInput = document
			.getElementsByTagName('form')[0].getElementsByTagName('input'), oTr = document
			.getElementsByTagName('tr');

	for (var i = 0; i < 4; i++) {
		arrayInner.push(oInput[i].value);
	}

	arrayOuter.push(arrayInner);

	for (var i = 2; i < oTr.length; i++) {
		arrayInner = [];
		var oInputOftr = oTr[i].getElementsByTagName('input');
		for (var j = 0; j < 11; j++) {
			arrayInner.push(oInputOftr[j].value);
		}
		arrayOuter.push(arrayInner);
	}

	// for(var i=0;i<arrayOuter.length;i++){
	// console.log(arrayOuter[i]);
	// }
	var str = JSON.stringify(arrayOuter);
	console.log(str);
	oInput[oInput.length - 1].value = str;

}

function detail_Apply(event) {
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName("td");
	var means = document.getElementById("means").innerHTML;
	var type="",id="";
	for (var i = 0; i < 3; i++) {
		if (i == 0) {
			id = tds[i].getElementsByTagName("input")[1].value;
		}
		if (i == 2) {
			type = tds[i].innerHTML;
		}
	}
	// 把获取到的数据传给后台
	if (type == "新入库" || type == "轮换入库" || type == "更新入库") {
		jQuery.ajax({
			type : "post",
			url : "ApplyHandleServlet?operate=searchByInId&inId=" + id
					+ "&means=" + means,
			success : function(data) {
				if (type == "新入库") {
					createnewIndetail(data);
				} else if (type == "轮换入库") {
					createborrowIndetail(data);
				} else if (type == "更新入库") {
					createupdatedetail(data);
				}
			}
		});
	} else if (type == "轮换出库" || type == "更新出库") {
		jQuery.ajax({
			type : "post",
			url : "ApplyHandleServlet?operate=searchByOutId&outId=" + id,
			success : function(data) {
				if (type == "轮换出库") {
					createborrowOutdetail(data);
				} else if (type == "更新出库") {
					createupdatedetail(data);
				}
			}
		});
	}
	// 把获取到的数据传给后台
	// jQuery.ajax({
	// });
	if (type == "新入库") {
		$(document).ready(function() {
			$("#whole").show();
			$("#new_In").show();
		});
		$(document).ready(function() {
			$("#sure_new_In").click(function() {
				$("#whole").hide();
				$("#new_In").hide();
				$("#new_Table").html("");
			});
		});
	} else if (type == "轮换入库") {
		$(document).ready(function() {
			$("#whole").show();
			$("#borrow_In").show();
		});
		$(document).ready(function() {
			$("#sure_borrow_In").click(function() {
				$("#whole").hide();
				$("#borrow_In").hide();
				$("#borrowin_Table").html("");
			});
		});
	} else if (type == "轮换出库") {
		$(document).ready(function() {
			$("#whole").show();
			$("#borrow_Out").show();
		});
		$(document).ready(function() {
			$("#sure_borrow_Out").click(function() {
				$("#whole").hide();
				$("#borrow_Out").hide();
				$("#borrowout_Table").html("");
			});
		});
	} else if (type == "更新入库") {
		$(document).ready(function() {
			$("#whole").show();
			$("#update").show();
		});
		$(document).ready(function() {
			$("#sure_update").click(function() {
				$("#whole").hide();
				$("#update").hide();
				$("#update_table").html("");
			});
		});
	} else if (type == "更新出库") {
		$(document).ready(function() {
			$("#whole").show();
			$("#update").show();
		});
		$(document).ready(function() {
			$("#sure_update").click(function() {
				$("#whole").hide();
				$("#update").hide();
				$("#update_table").html("");
			});
		});
	}
}
function createnewIndetail(provinces) {
	var t = document.getElementById("new_Table"); // 获取Table
	// 通过eval() 函数可以将JSON字符串转化为对象
	var trs = t.getElementsByTagName("tr");// 获取行
	// for(var j=0;j<trs.length;j++){
	// t.deleteRow();
	// }
	var detail = eval(provinces);
	if (detail.length > 0) {

		for (var i = 0; i < detail.length; i++) {
			// tr
			// if(i%11==0){
			var r = t.insertRow(t.rows.length);// 创建新的行
			// }
			// td
			for (var j = 0; j < 16; j++) {
				var c = r.insertCell(); // 创建新的列
				if (j == 0) {
					c.innerHTML = detail[i].contractid;
				}
				if (j == 1) {
					// c.innerHTML = detail[i].productname;
					c.innerHTML = detail[i].wholeName + detail[i].productunit;
				}
				if (j == 2) {
					c.innerHTML = detail[i].productmodel;
				}
				if (j == 3) {
					c.innerHTML = detail[i].deviceNo;
				}
				// if(j==4){
				// c.innerHTML = detail[i].wholeName;
				// }
				// if(j==5){
				// c.innerHTML = detail[i].productunit;
				// }
				if (j == 4) {
					c.innerHTML = detail[i].measureunit;
				}
				if (j == 5) {
					c.innerHTML = detail[i].productprice;
				}
				if (j == 6) {
					c.innerHTML = detail[i].pmnm;
				}
				if (j == 7) {
					c.innerHTML = detail[i].manuf;
				}
				if (j == 8) {
					c.innerHTML = detail[i].keeper;
				}
				if (j == 9) {
					c.innerHTML = detail[i].location;
				}
				if (j == 10) {
					c.innerHTML = detail[i].maintainCycle;
				}
				if (j == 11) {
					c.innerHTML = detail[i].produced;
				}
				if (j == 12) {
					c.innerHTML = detail[i].productcode;
				}
				if (j == 13) {
					c.innerHTML = detail[i].status;
				}
				if (j == 14) {
					c.innerHTML = detail[i].storage;
				}
				if (j == 15) {
					c.innerHTML = detail[i].remark;
				}
			}
		}
	} else {
		var r = t.insertRow();
		var c = r.insertCell();
		c.innerHTML = "";
	}
	// alert(document.getElementById("new_Table"));
	// document.getElementById("new_Table").appendChild(t);
}
function createborrowIndetail(provinces) {
	var t = document.getElementById("borrowin_Table"); // 获取Table
	// 通过eval() 函数可以将JSON字符串转化为对象
	var trs = t.getElementsByTagName("tr");// 获取行
	for (var j = 0; j < trs.length; j++) {
		t.deleteRow();
	}
	var detail = eval(provinces);
	if (detail.length > 0) {

		for (var i = 0; i < detail.length; i++) {
			// tr
			// if(i%11==0){
			var r = t.insertRow(t.rows.length);// 创建新的行
			// }
			// td
			for (var j = 0; j < 13; j++) {
				var c = r.insertCell(); // 创建新的列
				if (j == 0) {
					c.innerHTML = detail[i].wholeName;
				}
				if (j == 1) {
					c.innerHTML = detail[i].productunit;
				}
				if (j == 2) {
					c.innerHTML = detail[i].deviceNo;
				}
				if (j == 3) {
					c.innerHTML = detail[i].pmnm;
				}
				if (j == 4) {
					c.innerHTML = detail[i].measureunit;
				}
				if (j == 5) {
					c.innerHTML = detail[i].manuf;
				}
				if (j == 6) {
					c.innerHTML = detail[i].keeper;
				}
				if (j == 7) {
					c.innerHTML = detail[i].productprice;
				}
				if (j == 8) {
					c.innerHTML = detail[i].location;
				}
				if (j == 9) {
					c.innerHTML = detail[i].produced;
				}
				if (j == 10) {
					c.innerHTML = detail[i].maintainCycle;
				}
				if (j == 11) {
					c.innerHTML = detail[i].status;
				}
				if (j == 12) {
					c.innerHTML = detail[i].remark;
				}
			}
		}
	} else {
		var r = t.insertRow();
		var c = r.insertCell();
		c.innerHTML = "";
	}
	// document.getElementById('borrowout_Table').appendChild(t);
}

function createborrowOutdetail(provinces) {
	var t = document.getElementById("borrowout_Table"); // 获取Table
	// 通过eval() 函数可以将JSON字符串转化为对象
	var trs = t.getElementsByTagName("tr");// 获取行
	var detail = eval("(" + provinces + ")");
	if (detail.length > 0) {
		for (var i = 0; i < detail.length; i++) {
			// if(i%11==0){
			var r = t.insertRow(t.rows.length);// 创建新的行
			// }
			// td
			for (var j = 0; j < 13; j++) {
				var c = r.insertCell(); // 创建新的列
				if (j == 0) {
					c.innerHTML = detail[i].wholeName;
				}
				if (j == 1) {
					c.innerHTML = detail[i].productunit;
				}
				if (j == 2) {
					c.innerHTML = detail[i].deviceNo;
				}
				if (j == 3) {
					c.innerHTML = detail[i].pmnm;
				}
				if (j == 4) {
					c.innerHTML = detail[i].measureunit;
				}
				if (j == 5) {
					c.innerHTML = detail[i].manuf;
				}
				if (j == 6) {
					c.innerHTML = detail[i].keeper;
				}
				if (j == 7) {
					c.innerHTML = detail[i].productprice;
				}
				if (j == 8) {
					c.innerHTML = detail[i].location;
				}
				if (j == 9) {
					c.innerHTML = detail[i].produced;
				}
				if (j == 10) {
					c.innerHTML = detail[i].maintainCycle;
				}
				if (j == 11) {
					c.innerHTML = detail[i].status;
				}
				if (j == 12) {
					c.innerHTML = detail[i].remark;
				}
			}
		}
	} else {
		var r = t.insertRow();
		var c = r.insertCell();
		c.innerHTML = "";
	}
	// document.getElementById('borrowin_Table').appendChild(t);
}
function createupdatedetail(provinces) {
	var t = document.getElementById("update_table"); // 获取Table
	// 通过eval() 函数可以将JSON字符串转化为对象
	var trs = t.getElementsByTagName("tr");// 获取行
	// for(var j=0;j<trs.length;j++){
	// t.deleteRow();
	// }
	var detail = eval(provinces);
	if (detail.length > 0) {

		for (var i = 0; i < detail.length; i++) {
			// tr
			// if(i%11==0){
			var r = t.insertRow(t.rows.length);// 创建新的行
			// }
			// td
			for (var j = 0; j < 15; j++) {
				var c = r.insertCell(); // 创建新的列
				if (j == 0) {
					c.innerHTML = detail[i].wholeName;
				}
				if (j == 1) {
					c.innerHTML = detail[i].productunit;
				}
				if (j == 2) {
					c.innerHTML = detail[i].deviceNo;
				}
				if (j == 3) {
					c.innerHTML = detail[i].productprice;
				}
				if (j == 4) {
					c.innerHTML = detail[i].oldprice;
				}
				if (j == 5) {
					c.innerHTML = detail[i].measureunit;
				}
				if (j == 6) {
					c.innerHTML = detail[i].manuf;
				}
				if (j == 7) {
					c.innerHTML = detail[i].keeper;
				}
				if (j == 8) {
					c.innerHTML = detail[i].pmnm;
				}
				if (j == 9) {
					c.innerHTML = detail[i].location;
				}
				if (j == 10) {
					c.innerHTML = detail[i].storage;
				}
				if (j == 11) {
					c.innerHTML = detail[i].maintainCycle;
				}
				if (j == 12) {
					c.innerHTML = detail[i].status;
				}
				if (j == 13) {
					c.innerHTML = detail[i].produced;
				}
				if (j == 14) {
					c.innerHTML = detail[i].remark;
				}
			}
		}
	} else {
		var r = t.insertRow();
		var c = r.insertCell();
		c.innerHTML = "";
	}
	// document.getElementById('update_table').appendChild(t);
}
function createupdatedetail(provinces) {
	var t = document.getElementById("update_table"); // 获取Table
	// 通过eval() 函数可以将JSON字符串转化为对象
	var trs = t.getElementsByTagName("tr");// 获取行
	for (var j = 0; j < trs.length; j++) {
		t.deleteRow();
	}
	var detail = eval(provinces);
	if (detail.length > 0) {

		for (var i = 0; i < detail.length; i++) {
			// tr
			// if(i%11==0){
			var r = t.insertRow(t.rows.length);// 创建新的行
			// }
			// td
			for (var j = 0; j < 15; j++) {
				var c = r.insertCell(); // 创建新的列
				if (j == 0) {
					c.innerHTML = detail[i].wholeName;
				}
				if (j == 1) {
					c.innerHTML = detail[i].productunit;
				}
				if (j == 2) {
					c.innerHTML = detail[i].deviceNo;
				}
				if (j == 3) {
					c.innerHTML = detail[i].productprice;
				}
				if (j == 4) {
					c.innerHTML = detail[i].oldprice;
				}
				if (j == 5) {
					c.innerHTML = detail[i].measureunit;
				}
				if (j == 6) {
					c.innerHTML = detail[i].manuf;
				}
				if (j == 7) {
					c.innerHTML = detail[i].keeper;
				}
				if (j == 8) {
					c.innerHTML = detail[i].pmnm;
				}
				if (j == 9) {
					c.innerHTML = detail[i].location;
				}
				if (j == 10) {
					c.innerHTML = detail[i].storage;
				}
				if (j == 11) {
					c.innerHTML = detail[i].maintainCycle;
				}
				if (j == 12) {
					c.innerHTML = detail[i].status;
				}
				if (j == 13) {
					c.innerHTML = detail[i].produced;
				}
				if (j == 14) {
					c.innerHTML = detail[i].remark;
				}
			}
		}
	} else {
		var r = t.insertRow();
		var c = r.insertCell();
		c.innerHTML = "";
	}
	// document.getElementById('update_table').appendChild(t);
}
// 军代室版js
function jdsListDownload() {
	var exportType = "none";
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			var length = oCurrentTd.length;
			for (var j = 0; j < 2; j++) {
				if (j == 0) {
					var val = oCurrentTd[j].getElementsByTagName("input")[1].value;
					oArray[m][j] = val;
				} else if (j == 1) {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
					exportType = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	if (exportType == "新入库") {
		exportType = "newIn";
	} else if (exportType == "轮换入库") {
		exportType = "circleIn";
	} else if (exportType == "轮换出库") {
		exportType = "circleOut";
	} else if (exportType == "更新入库") {
		exportType = "renewIn";
	} else if (exportType == "更新出库") {
		exportType = "renewOut";
	}
	// 创建二维数组将选中的信息保存
	jQuery
			.ajax({
				type : "post",
				url : "InWarehouseServlet?operate=exportSingleForm&exportType="
						+ exportType,
				data : {
					'outData' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					if (data != "") {
						window.location.href = "InWarehouseServlet?operate=download&absolutePath="
								+ data + "&exportType=" + exportType;
					}
				},
				error : function() {
					window.wxc.xcConfirm("请求失败","warning");
				}
			});
}

/* 封装数据1 */
function makeOutList(version) {
	idIndex = 0;
	var outListType = "";
	switch (version) {
	case 1:
		outListType = "direct";
		break;
	case 2:
		outListType = "borrow";
		break;
	case 3:
		outListType = "update";
		break;
	default:
		outListType = "direct";
		break;
	}
	// 得到表头
	var aywh = "";
	var fldh = "";
	var ysfs = "";
	var ydbh = "";
	if (document.getElementById("fileNo") != null) {
		aywh = document.getElementById("fileNo").value;
	}
	if (document.getElementById("listId") != null) {
		fldh = document.getElementById("listId").value;
	}
	if (document.getElementById("diliverMean") != null) {
		ysfs = document.getElementById("diliverMean").value;
	}
	if (document.getElementById("deliverNo") != null) {
		ydbh = document.getElementById("deliverNo").value;
	}
	var keeper = document.getElementById("ownedUnit").value;

	var list = new createList(aywh, fldh, ysfs, ydbh);
	// 得到每一行
	var oArray = new Array();
	var m = 0;
	var n = 0;
	var oTable = document.getElementById("list-content");
	var trs = oTable.getElementsByTagName("tr");
	for (var i = 1; i < trs.length; i++) {
		oArray[m] = new Array();
		var oTds = trs[i].getElementsByTagName("td");
		for (var j = 0; j < oTds.length; j++) {
			if (j == 0) {
				oArray[m][n++] = oTds[j].innerHTML;
			} else if (j == 1 || j == 6) {
				// outProIds[idIndex++] =
				// oTds[j].getElementsByTagName("input")[0].value;
				oArray[m][n++] = oTds[j].getElementsByTagName("input")[1].value;
			} else if (j == 10) {
				oArray[m][n++] = oTds[j].getElementsByTagName("input")[1].value;
				oArray[m][n++] = oTds[j].getElementsByTagName("input")[0].value;
			} else {
				oArray[m][n++] = oTds[j].getElementsByTagName("input")[0].value;
			}
		}
		m++;
	}

	jQuery
			.ajax({
				type : "post",
				url : "OutWarehouseServlet?operate=ExportOutListFileJDS&outListType="
						+ outListType,
				data : {
					'header' : arrayToJson(list),
					'content' : arrayToJson(oArray),
					'ids' : arrayToJson(lyt.vars.myIds),
					'keeper' : keeper
				},
				dataType : "text",
				success : function(data) {
					window.location.href = "OutWarehouseServlet?operate=downloadOutlistFile&outListType="
							+ outListType + "&path=" + data;
					lyt.vars.myIds.slice(0, lyt.vars.myIds.length - 1);
				},
				error : function() {
					window.wxc.xcConfirm("请求失败","warning");
					lyt.vars.myIds.slice(0, lyt.vars.myIds.length - 1);
				}
			});

}

/* 封装发料单抬头 */
function createList(AYWH, FLDH, YSFS, YDBH) {
	this.AYWH = AYWH;
	this.FLDH = FLDH;
	this.YSFS = YSFS;
	this.YDBH = YDBH;
	return this;
}
// 添加机号抓取
function addDeviceNo() {
	var contractId = document.getElementById("returnId").value;
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	var oUnchekedFlag = true;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (!oCurrentCheckBox[i].checked)
			continue;
		else {
			oUnchekedFlag = false;
			break;
		}
	}
	if (oUnchekedFlag) {
		//alert("请先勾选具体产品!");
		window.wxc.xcConfirm("请先勾选具体产品!","warning");
		return;
	}

	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for (var j = 0; j < oCurrentTd.length - 1; j++) {
				/*
				 * if (j == 0) { var val =
				 * oCurrentTd[j].getElementsByTagName("input")[1].value;
				 * oArray[m][j] = val; } else
				 */if (j == 12 || j == 13) {
					var val = oCurrentTd[j + 1].getElementsByTagName("input")[0].value;
					oArray[m][j] = val;
				} else {
					oArray[m][j] = oCurrentTd[j + 1].innerHTML;
				}
			}
			m++;
		}
	}
	jQuery
			.ajax({
				type : "post",
				url : "AddBatchInApplyServlet?operate=addBatchDeviceNo",
				data : {
					'data' : arrayToJson(oArray)
				},
				dataType : "text",
				success : function(data) {
					//alert("添加机号" + data);
					window.wxc.xcConfirm("添加机号"+data,"info",{onOk:function(){ 
						window.location.href = "ProductHandleServlet?operate=queryproduct&contractid="
							+ contractId;
					}});
					/*
					 * if (data == "保存成功！") { window.location.href =
					 * "InspectServlet?operate=inspectQuery&mark=search&curPageNum=1&pageSize=10&productModel=&unitName=&manufacturer=&deviceNo=&restKeepTime=&JDInspect="; }
					 */
				},
				error : function() {
					window.wxc.xcConfirm("请求失败","warning");
				}
			});
}

function searchupdatehistory() {
	var productModel = document.getElementById("productModel").value;
	var deviceNo = document.getElementById("deviceNo").value;
	var means = document.getElementById("Means").value;
	var newproductmodel = document.getElementById("newproductmodel");
	var newdeviceno = document.getElementById("newdeviceno");
	var oldproductmodel = document.getElementById("oldproductmodel");
	var olddeviceno = document.getElementById("olddeviceno");
	$.ajax({
		type : "post",
		url : "InWarehouseServlet?operate=queryUpdateHistory&deviceNo="
				+ deviceNo + "&productModel=" + productModel + "&means="
				+ means,
		dataType : "json",
		success : function(data) {
			if (data == 0) {
				//alert("该产品没有轮换更新！");
				window.wxc.xcConfirm("该产品没有轮换更新！","warning");
			} else {
				var product = data[0];
				$(document).ready(function() {
					$("#whole").show();
					$("#showupdatehistory").show();
				});
				newproductmodel.setAttribute("value", product.newproductModel);
				newdeviceno.setAttribute("value", product.newdeviceNo);
				oldproductmodel.setAttribute("value", product.oldproductModel);
				olddeviceno.setAttribute("value", product.olddeviceNo);
				$(document).ready(function() {
					$("#sure_updatehistory").click(function() {
						$("#whole").hide();
						$("#showupdatehistory").hide();
					});
				});
			}
		},

		error : function() {
			window.wxc.xcConfirm("请求失败","warning");
		}
	});
}
function searchmaintainhistory(basePath) {
	var productModel = document.getElementById("productModel").value, deviceNo = document
			.getElementById("deviceNo").value;
	window.location.href = basePath
			+ "InWarehouseServlet?operate=showMaintainHistory&deviceNo="
			+ deviceNo + "&productModel=" + productModel;
}

function checkNext(event) {
	var tds = event.parentNode.parentNode.getElementsByTagName("td"), val = event.value, td = tds[tds.length - 1], other = td
			.getElementsByTagName('input')[0];
	if (other.disabled) {
		other.value = val;
	}
}

// 入库申请管理 操作类型下拉onchange事件
function searchViaOpType() {
	// var oOperateType=document.getElementById("operateType").value;
	document.forms.searchInApplyForm.submit();
}

// 军代室导入申请一键导入js
function getType(event) {

}

// 企业版 合同申请入库 全选和全部选函数
function getAll() {
	var oOperateType = document.getElementById("operateType").value;
	if (oOperateType == "allIn") {
		//alert("请选择具体操作类型，再进行多选操作");
		window.wxc.xcConfirm("请选择具体操作类型，再进行多选操作","warning");
		return;
	} else {
		var oCheckLeader = document.getElementById("checkLeader"), checks = document
				.getElementsByName("mycheck"), flag = false, reg = new RegExp(
				'待审核');

		if (!oCheckLeader.checked) {
			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = false;
			}
		} else {
			for (var i = 0; i < checks.length; i++) {
				var oType = checks[i].parentNode.parentNode
						.getElementsByTagName("td")[5].innerHTML;
				if (reg.test(oType))
					checks[i].checked = true;
			}
		}
	}
	return;
}

// 判断是否一个都没选
function emptyCheck() {
	var checks = document.getElementsByName("mycheck");
	var flag = true;
	for (var i = 0; i < checks.length; i++) {
		if (checks[i].checked == true) {
			flag = false;
			break;
		}
	}
	return flag;
}

// 企业版 选择单个复选框函数
function checkChange(event) {
	var oOperateType = document.getElementById("operateType").value;
	if (oOperateType == "allIn") {
		event.checked = false;
		//alert("请选择具体操作类型，再进行多选操作");
		window.wxc.xcConfirm("请选择具体操作类型，再进行多选操作","warning");
		return;
	} else {
		if (!event.checked)
			event.checked = false;
		else {
			var oCurCheck = event.parentNode.parentNode
					.getElementsByTagName("td")[5].innerHTML, reg = new RegExp(
					'待审核');
			if (reg.test(oCurCheck))
				event.checked = true;
			else {
				event.checked = false;
				//alert("当前产品未处于待审核状态，不可选");
				window.wxc.xcConfirm("当前产品未处于待审核状态，不可选","warning");
			}
			return;
		}
	}
}

// // 所有入库，解决方法一：页面加载执行
// function isAllIn(flag) {
// var checkBoxs = document.getElementsByName("mycheck");
// if (flag) {
// for ( var i = 0; i < checkBoxs.length; i++) {
// checkBoxs[i].setAttribute("disabled", "true");
// }
// } else {
// for ( var i = 0; i < checkBoxs.length; i++) {
// checkBoxs[i].removeAttribute("disabled");
// }
// }
// }
//
// function recover() {
// var oCurrentCheckBox = document.getElementsByName("mycheck");
// // 恢复checkbox为可选状态
// for ( var k = 0; k < oCurrentCheckBox.length; k++) {
// oCurrentCheckBox[i].removeAttribute("disabled");
// }
// }
//
// function checkall() {
// var checks = document.getElementsByName("mycheck");
// for ( var j = 0; j < checks.length; j++) {
// if (checks[j].checked == true) {
// checks[j].checked = false;
// } else if (checks[j].checked == false) {
// checks[j].checked = true;
// }
// }
// }

// 军代室及以上 列表查询 全选和全不选
function listQuaryCheckAll() {
	var oOperateType = document.getElementById("operateType").value, oOwnedUnit = document
			.getElementById("ownedUnit").value;
	if (oOperateType == "allIn") {
		//alert("请选择具体的操作类型和代储企业，再进行多选操作");
		window.wxc.xcConfirm("请选择具体的操作类型和代储企业，再进行多选操作","warning");
		return;
	} else if (oOwnedUnit == "所有企业") {
		window.wxc.xcConfirm("请选择具体的操作类型和代储企业，再进行多选操作","warning");
		return;
	} else {
		var oCheckLeader = document.getElementById("checkLeader"), checks = document
				.getElementsByName("mycheck"), flag = false, reg = new RegExp(
				'待审核');

		if (!oCheckLeader.checked) {
			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = false;
			}
		} else {
			for (var i = 0; i < checks.length; i++) {
				var oType = checks[i].parentNode.parentNode
						.getElementsByTagName("td")[6].innerHTML;
				if (reg.test(oType))
					checks[i].checked = true;
			}
		}
	}
	return;
}

// 军代室及以上 列表查询 选择一个
function listQueryCheckEach(event) {

}
