// JavaScript Document
function clear9831() {
	$.ajax({
		type : "post",
		data : {
			'data' : ""
		},
		url : "ServiceOf9831Servlet?operate=deleteAll9831",
		dataType : "text",
		success : function() {
			window.location.href = "SystemManagementServlet?operate=9831";
		},
		error : function() {
			// alert("请求失败");
			window.wxc.xcConfirm("请求失败 ", "error");
		}
	});
}

// 勾选全部
function chooseAll() {
	var oCheckBoxLeader = document.getElementById("check_9831_leader");
	var oInput = document.getElementsByName('check_9831');
	if (oCheckBoxLeader.checked) {
		for ( var i = 0; i < oInput.length; i++) {
			oInput[i].checked = true;
		}
	} else {
		for ( var i = 0; i < oInput.length; i++) {
			oInput[i].checked = false;
		}
	}
}

// 批量加入基础数据库
function getchoose() {
	var oCurrentCheckBox = document.getElementsByName("check_9831");
	// 先获取选中了多少的checkbox
	var m = 0;
	var oArray = new Array();
	for ( var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			for ( var j = 1; j < oCurrentTd.length; j++) {
				oArray[m][j - 1] = oCurrentTd[j].innerHTML;
			}
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
				url : "ServiceOf9831Servlet?operate=intoBasedata",
				dataType : "text",
				success : function(data) {
					// alert("添加成功");
					window.wxc
							.xcConfirm(
									"添加成功 ",
									"success",
									{
										onOk : function() {
											window.location.href = "SystemManagementServlet?operate=basedata";
										}
									});
				},
				error : function() {
					// alert("请求失败");
					window.wxc.xcConfirm("请求失败", "error");
				}
			});
}

// 点击查询返回后展示页面
function slt9831ByCdt(nowpage) {
	//nowpage==-1表示是跳转页面
	var totalPageNum = Number(document.getElementById("totalPageNum").innerHTML);
	if (nowpage == -1) {
		var skipPageNum = Number(document.getElementById("skipPageNum").value);
		if (isNaN(skipPageNum) || skipPageNum <= 0) {
			window.wxc.xcConfirm("请输入有效页面", "warning");
			return;
		} else if (skipPageNum > totalPageNum) {
			window.wxc.xcConfirm("您输入的页面大于总页数", "warning");
			return;
		}
		nowpage=skipPageNum;
	}
	//-2表示是选择每页显示多少条
	var oSltForm = document.getElementById("9832-Slt-form"), oInputs = oSltForm
			.getElementsByTagName("input"), PMNM = oInputs[0].value, PMBM = oInputs[1].value, PMCS = oInputs[2].value, LBQF = oInputs[3].value, 
		pagesize = document
			.getElementById("selectPageSize").value;
	$
			.ajax({
				type : "post",
				url : "ServiceOf9831Servlet",
				data : {
					operate : "select",
					nowpage : nowpage,
					pagesize : pagesize,
					PMNM : PMNM,
					PMBM : PMBM,
					PMCS : PMCS,
					LBQF : LBQF
				},
				dataType : "json",
				success : function(data) {
					// console.log(data);
					var o9831Table = document.getElementById("9831-info-table");
					if (data.items == null) {
						$(o9831Table).html("");
						window.wxc.xcConfirm("没有符合条件的数据", "info", {
							onOk : function() {
								return;
							}
						});
					} else {
						var items = data.items, trlen = items.length;
						$(o9831Table).html("");
						if (trlen <= 0) {
							window.wxc.xcConfirm("没有符合条件的数据", "info", {
								onOk : function() {
									return;
								}
							});
						} else {
							var tdlen = items[0].length;
							for ( var i = 0; i < trlen; i++) {
								var tr = document.createElement("tr");
								tr.setAttribute("class",
										"contract-add-table-body");
								// 添加checkbox
								var oInputTd = document.createElement("td"), oInput = document
										.createElement("input");
								oInput.setAttribute("type", "checkbox");
								oInput.setAttribute("name", "check_9831");
								oInputTd.appendChild(oInput);
								tr.appendChild(oInputTd);
								// 添加返回的内容
								for ( var j = 0; j < tdlen - 1; j++) {
									var td = document.createElement("td");
									td.innerHTML = items[i][j];
									tr.appendChild(td);
								}
								// 设置隔行变色
								if (i % 2 != 0)
									tr.setAttribute("class", "addTrColor");
								o9831Table.appendChild(tr);
							}
						}
					}
				},
				error : function() {
					window.wxc.xcConfirm("查询错误", "warning");
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
			for ( var i = 0; i < o.length; i++) {
				r.push(arrayToJson(o[i]));
			}
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
}

/*
 * //清空9831库弹提示输入密码框 输入正确进入Servlet function clear9831() { confirm('确定清空9831库吗？') {
 * var ex = prompt("请输入用户密码"); jQuery.ajax({ type : "post", url :
 * "ServiceOf9831Servlet?operate=checkpwd", data : { 'pwd' : ex }, dataType :
 * "text", success : function(data) { alert(data);
 * 
 * if (data == "删除成功") { alert("成功清除9831库"); } }, error : function() {
 * alert("请求失败"); } }); } }
 */