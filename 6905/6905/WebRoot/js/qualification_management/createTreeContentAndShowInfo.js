// 获取版本号,控制从那一级开始显示
function showStartContentByVersion() {
	var version, oRootLevel, oRootContentStr;
	version = document.getElementById("version").value;
	oRootLevel = getRootLevel(version);
	oRootContentStr = getRootContentStr(version);
	showTreeRootContent(oRootContentStr, oRootLevel);
}

// 展示当前版本下开始的树状目录
function showTreeRootContent(oRootContentStr, oRootLevel) {
	var oRootLevelContentStr, oRootLevelContentStrArray, i, str;
	oRootLevelContentStr = document.getElementById(oRootContentStr).value;
	oRootLevelContentStrArray = oRootLevelContentStr.split(",");
	str = "<h3>部门目录</h3><ul id=\"rootLevel\">";
	for (i = 0; i < oRootLevelContentStrArray.length; i++) {
		str += "<li name=" + oRootLevel + ">";
		str += "<a onclick=\"showNextLevelContentAndCurLevelInfo('"
				+ oRootLevel + "','" + oRootLevelContentStrArray[i]
				+ "','logo',this)\" ></a>";
		str += "<a onclick=\"showNextLevelContentAndCurLevelInfo('"
				+ oRootLevel + "','" + oRootLevelContentStrArray[i]
				+ "','content',this)\" >";
		str += oRootLevelContentStrArray[i];
		str += "</a>";
		str += "</li>";
	}
	str += "</ul>";
	document.getElementById("treecontent-box").innerHTML = str;

	// 根据当前级别是否有下级，控制显示不同样式
	showContentLogo(oRootLevelContentStrArray, oRootLevel, 1, null);
	
	// 默认显示第一个部门信息
	showNextLevelContentAndCurLevelInfo(oRootLevel,oRootLevelContentStrArray[0],'content',this);
	
	var oRootA=document.getElementById("rootLevel").getElementsByTagName("a");
	// 默认显示下一级部门目录
	for (i = 0; i < oRootA.length; i++) {
		if(i/2!=0)
			continue;
		showNextLevelContentAndCurLevelInfo(oRootLevel,oRootLevelContentStrArray[i/2],'logo',oRootA[i]);
	};
	
}

// 展示当前选择级别对应部门的下级部门和当前选择部门的基本信息
function showNextLevelContentAndCurLevelInfo(oCurLevel, oCurLevelName,
		oALabeltype, ev) {
	var oNextLevel, oClassName, oCurLi, oContentFunction;
	var oResultJSONObj, oNextLevelUl, oNextLevelContentArray, oCurLevelInfoArray, str;
	var i;
	// 当点击部门前开关标志的a标签
	if (oALabeltype == "logo") {
		// 获取当前级别的下一级
		oNextLevel = getNextLevel(oCurLevel);
		// 获取当前a标签的class和它的直接父节点li
		oClassName = ev.className;
		oCurLi = ev.parentNode;

		oContentFunction = getContentFunction(oNextLevel);
	}

	$
			.ajax({
				url : 'InfoServlet',
				type : 'POST',
				data : {
					operate : 'searchContentAndInfo',
					nextLevel : oNextLevel,
					curLevel : oCurLevel,
					curLevelName : oCurLevelName
				},
				success : function(oResultStr) {
					oResultJSONObj = eval('(' + oResultStr + ')');
					if (oALabeltype == "logo") {
						if(ev.nextSibling==null)
							oNextLevelUl=null;
						else
							oNextLevelUl = ev.nextSibling.nextSibling;
						// 当前级别有下一级且处于关闭下级目录状态
						if (oClassName == "closelogo") {
							// 如果ul已经构建出来并在closelogo下处于不显示状态，那么点击后让其显示出来
							if (oNextLevelUl != null) {
								while (oNextLevelUl != null) {
									oNextLevelUl.style.display = "block";
									oNextLevelUl = oNextLevelUl.nextSibling;
								}
							}
							// 否则就动态构建树状目录
							else {
								oNextLevelContentArray = oResultJSONObj.nextLevelContentStr
										.split(",");
								str = oCurLi.innerHTML;
								for (i = 0; i < oNextLevelContentArray.length; i++) {
									str += "<ul>";
									str += "<li name=" + oNextLevel + ">";
									str += "<a onclick=\"" + oContentFunction
											+ "('" + oNextLevel + "','"
											+ oNextLevelContentArray[i]
											+ "','logo',this)\" ></a>";
									str += "<a onclick=\"" + oContentFunction
											+ "('" + oNextLevel + "','"
											+ oNextLevelContentArray[i]
											+ "','content',this)\" >";
									str += oNextLevelContentArray[i];
									str += "</a>";
									str += "</li>";
									str += "</ul>";
								}
								oCurLi.innerHTML = str;
								// 根据下一级别是否还有下级，控制显示不同样式
								// 如果下一级是company，不用判断
								if (oNextLevel != "company") {
									showContentLogo(oNextLevelContentArray,
											oNextLevel, 0, oCurLi);
								} else {
									for (i = 0; i < oNextLevelContentArray.length; i++) {
										oCurLi.getElementsByTagName("li")[i]
												.getElementsByTagName("a")[0].className = "stoplogo";
									}
								}
							}
							oCurLi.getElementsByTagName("a")[0].className = "openlogo";
						}
						// 当前级别有下一级且处于打开下级目录状态
						else if (oClassName == "openlogo") {
							// 关闭下级目录
							while (oNextLevelUl != null) {
								oNextLevelUl.style.display = "none";
								oNextLevelUl = oNextLevelUl.nextSibling;
							}
							oCurLi.getElementsByTagName("a")[0].className = "closelogo";
						}
					} else if (oALabeltype == "content") {
						// 当点击部门的a标签 将当前级别部门信息展示出来
						oCurLevelInfoArray = oResultJSONObj.curLevelInfoStr
								.split(",");
						showDepartmentInfo(oCurLevelInfoArray, oCurLevel);
						// 展示可操作的按钮
						showOperateButton(oCurLevel);
					}
				}
			});
}

// 根据版本获取根级别
function getRootLevel(version) {
	var oRootLevel = "";
	switch (version) {
	case "4":
		oRootLevel = "zhj";
		break;
	case "3":
		oRootLevel = "jdj";
		break;
	case "2":
		oRootLevel = "jds";
		break;
	default:
		break;
	}
	return oRootLevel;
}

// 根据版本获取要查询的根目录的键
function getRootContentStr(version) {
	var oRootContentStr = "";
	switch (version) {
	case "4":
		oRootContentStr = "allZhjContentStr";
		break;
	case "3":
		oRootContentStr = "allJdjContentStr";
		break;
	case "2":
		oRootContentStr = "allJdsContentStr";
		break;
	default:
		break;
	}
	return oRootContentStr;
}

// 根据当前部门级别获取下级部门级别
function getNextLevel(oCurLevel) {
	var nextLevel = "";
	switch (oCurLevel) {
	case "zhj":
		nextLevel = "jdj";
		break;
	case "jdj":
		nextLevel = "jds";
		break;
	case "jds":
		nextLevel = "company";
		break;
	default:
		break;
	}
	return nextLevel;
}

// 根据目录级别获取a标签后的onclick函数
function getContentFunction(oNextLevel) {
	oContentFunction = "";
	if (oNextLevel == "company")
		oContentFunction = "showCurCompanyInfo";
	else
		oContentFunction = "showNextLevelContentAndCurLevelInfo";
	return oContentFunction;
}

// 根据部门是否有下级部门,显示不同logo样式
function showContentLogo(oContentArray, oLevel, oRootFlag, oCurLi) {
	if (oRootFlag == 1) {
		var oCurUl = document.getElementById("rootLevel");
		for ( var i = 0; i < oContentArray.length; i++) {
			var oTempFlag = getNextLevelContent(oLevel, oContentArray[i]);
			if (oTempFlag != 0)
				oCurUl.getElementsByTagName("li")[i].getElementsByTagName("a")[0].className = "closelogo";
			else
				oCurUl.getElementsByTagName("li")[i].getElementsByTagName("a")[0].className = "stoplogo";
		}
	} else {
		for ( var i = 0; i < oContentArray.length; i++) {
			var oTempFlag = getNextLevelContent(oLevel, oContentArray[i]);
			if (oTempFlag != 0)
				oCurLi.getElementsByTagName("li")[i].getElementsByTagName("a")[0].className = "closelogo";
			else
				oCurLi.getElementsByTagName("li")[i].getElementsByTagName("a")[0].className = "stoplogo";
		}
	}
}

// 判断当前目录级别下是否有下级目录
function getNextLevelContent(curLevel, curLevelName) {
	var oTempFlag, nextLevel;
	nextLevel = getNextLevel(curLevel);
	$.ajax({
		url : 'InfoServlet',
		type : 'POST',
		data : {
			operate : 'searchContent',
			nextLevel : nextLevel,
			curLevel : curLevel,
			curLevelName : curLevelName
		},
		async : false,
		success : function(oResultFlag) {
			oTempFlag = oResultFlag;
		}
	});
	return oTempFlag;
}

// 展示当前选择的代储企业信息
function showCurCompanyInfo(curLevel, curCompanyName, oALabeltype, event) {
	// 点击了代储企业content
	if (oALabeltype == "content") {
		$.ajax({
			url : 'InfoServlet',
			type : 'POST',
			data : {
				operate : 'searchContentAndInfo',
				curLevel : curLevel,
				curLevelName : curCompanyName
			},
			success : function(oResultStr) {
				var oResultJSONObj = eval('(' + oResultStr + ')');
				// 动态将当前部门信息展示出来
				var oCurLevelInfoArray = oResultJSONObj.curLevelInfoStr
						.split(",");
				showDepartmentInfo(oCurLevelInfoArray, 0);
				showOperateButton(curLevel);
			}
		});
	}
}

// 点击添加、删除、修改时取得节点对象，部门名称，当前目录级别
function getUpdateParamArray(oUpdateType) {
	var oALabelArray, oDepartmentName, oCurLevel, oUpdateParamArray;
	oALabelArray = document.getElementById("rootLevel").getElementsByTagName(
			"a");
	oDepartmentName = document.getElementById("departmentName").innerHTML;
	// 查找oDepartmentName在oALabelArray哪一个位置出现
	for ( var i = 0; i < oALabelArray.length; i++) {
		if (oDepartmentName == oALabelArray[i].innerHTML) {
			oCurLevel = oALabelArray[i].parentNode.getAttribute("name");
			if (oUpdateType == "add") {
				oUpdateParamArray = [
						oALabelArray[i].parentNode.getElementsByTagName("a")[0],
						oDepartmentName, oCurLevel ];
			} else
				oUpdateParamArray = [ oALabelArray[i], oDepartmentName,
						oCurLevel ];
			return oUpdateParamArray;
		}
	}
}

// 根据选择的当前目录不同，动态将当前部门基本信息展示出来
function showDepartmentInfo(oCurLevelInfoArray, oCurLevel) {
	document.getElementById("department-info-box").style.display = "block";
	document.getElementById("add-department-box").style.display = "none";
	document.getElementById("update-department-box").style.display = "none";
	document.getElementById("import-qualify-box").style.display = "none";
	if (oCurLevel == "zhj") {
		document.getElementById("departmentName").innerHTML = oCurLevelInfoArray[0];
		document.getElementById("departmentManager").innerHTML = oCurLevelInfoArray[1];
		document.getElementById("leader").innerHTML = oCurLevelInfoArray[2];
		document.getElementById("ownedDepartmentName").style.display = "none";
		document.getElementById("ownedDepartmentTitle").style.display = "none";
	} else {
		document.getElementById("departmentName").innerHTML = oCurLevelInfoArray[0];
		document.getElementById("ownedDepartmentName").innerHTML = oCurLevelInfoArray[1];
		document.getElementById("departmentManager").innerHTML = oCurLevelInfoArray[2];
		document.getElementById("leader").innerHTML = oCurLevelInfoArray[3];
		document.getElementById("ownedDepartmentName").style.display = "block";
		document.getElementById("ownedDepartmentTitle").style.display = "block";
	}
}

// 根据当前的级别选择能操作的按钮
function showOperateButton(oCurLevel) {
	var version, oRootLevel, addNextDepartButton, deleteNextDepartButton, updateNextDepartButton, importQualifyButton, searchQualifyButton;
	// 获取根目录级别
	version = document.getElementById("version").value;
	oRootLevel = getRootLevel(version);

	// 获取按钮对象
	addNextDepartButton = document.getElementById("addNextDepartButton");
	deleteNextDepartButton = document.getElementById("deleteNextDepartButton");
	updateNextDepartButton = document.getElementById("updateNextDepartButton");
	importQualifyButton = document.getElementById("importQualifyButton");
	searchQualifyButton = document.getElementById("searchQualifyButton");

	// 删除部门后按钮全部消失
	if (oCurLevel == "") {
		addNextDepartButton.style.display = "none";
		deleteNextDepartButton.style.display = "none";
		updateNextDepartButton.style.display = "none";
		importQualifyButton.style.display = "none";
		searchQualifyButton.style.display = "none";
	}
	// 如果根目录级别和当前级别一致则只出现添加部门
	else if (oCurLevel == oRootLevel) {
		addNextDepartButton.style.display = "block";
		deleteNextDepartButton.style.display = "none";
		updateNextDepartButton.style.display = "none";
		importQualifyButton.style.display = "none";
		searchQualifyButton.style.display = "none";
	} else {
		// 企业不出现"添加下属部门"
		if (oCurLevel == "company") {
			addNextDepartButton.style.display = "none";
			deleteNextDepartButton.style.display = "block";
			updateNextDepartButton.style.display = "block";
			importQualifyButton.style.display = "block";
			searchQualifyButton.style.display = "block";
		}
		// 其他情况
		else {
			addNextDepartButton.style.display = "block";
			deleteNextDepartButton.style.display = "block";
			updateNextDepartButton.style.display = "block";
			importQualifyButton.style.display = "none";
			searchQualifyButton.style.display = "none";
		}
	}
}

// 显示添加部门的表单
function showAddForm() {
	var ownedDepartment = document.getElementById("departmentName").innerHTML;

	document.getElementById("addDepartmentManager").value = "";
	document.getElementById("addLeader").value = "";
	document.getElementById("addLevelName").value = "";
	document.getElementById("ownedLevelName").value = ownedDepartment;

	document.getElementById("department-info-box").style.display = "none";
	document.getElementById("update-department-box").style.display = "none";
	document.getElementById("add-department-box").style.display = "block";
	document.getElementById("import-qualify-box").style.display = "none";
}

// 添加部门
function addDepartment() {
	var oUpdateParamArray, oCurContentNode, oCurLevel, oNextLevel, oContentFunction, oAddLevelName, oOwnedLevelName, oManager, oLeader, str, oParentNode, oCurNodeClassName, oCurDepartmentInfoArray, flag;
	oUpdateParamArray = getUpdateParamArray("add");
	oCurContentNode = oUpdateParamArray[0];
	oCurLevel = oUpdateParamArray[2];
	oNextLevel = getNextLevel(oCurLevel);
	oContentFunction = getContentFunction(oNextLevel);

	oAddLevelName = document.getElementById("addLevelName").value;
	oOwnedLevelName = document.getElementById("ownedLevelName").value;
	oManager = document.getElementById("addDepartmentManager").value;
	oLeader = document.getElementById("addLeader").value;

	// 进行非空判断
	flag = ifEmptyStringWhenUpdate(oManager, oLeader, oAddLevelName,
			oOwnedLevelName);
	if (flag) {
		// 传入后台，添加部门
		$.ajax({
			url : 'InfoServlet',
			type : 'POST',
			data : {
				operate : 'add',
				curLevel : oCurLevel,
				addLevelName : oAddLevelName,
				ownedLevelName : oOwnedLevelName,
				manager : oManager,
				leader : oLeader
			},
			success : function(oResultStr) {
				if (oResultStr == "1") {
					// 修改当前logo标签的css，并立即显示出来
					oCurNodeClassName = oCurContentNode.className;
					if (oCurNodeClassName == "stoplogo") {
						oCurContentNode.className = "openlogo";
					}

					// 同步更新目录
					oParentNode = oCurContentNode.parentNode;
					str = oParentNode.innerHTML;
					str += "<ul>";
					str += "<li name=" + oNextLevel + ">";
					str += "<a onclick=\"" + oContentFunction + "('"
							+ oNextLevel + "','" + oAddLevelName
							+ "','logo',this)\" class=\"stoplogo\" >";
					str += "<a onclick=\"" + oContentFunction + "('"
							+ oNextLevel + "','" + oAddLevelName
							+ "','content',this)\">";
					str += oAddLevelName;
					str += "</a>";
					str += "</li>";
					str += "</ul>";
					oParentNode.innerHTML = str;

					// 回到部门基本信息页面
					oCurDepartmentInfoArray = [ oManager, oLeader,
							oAddLevelName, oOwnedLevelName ];
					returnToInfoThroughUpdate(oCurDepartmentInfoArray,
							"add-department-box");

					// 展示按钮
					showOperateButton(oNextLevel);

					window.wxc.xcConfirm("添加成功","success");
				} else
					window.wxc.xcConfirm("添加失败","error");
			}
		});
	} else
		return;
}

// 获取当前部门id
function getDepartmentId(departmentName, oCurLevel) {
	var oCurId;
	// 获取当前部门的id
	$.ajax({
		url : 'InfoServlet',
		type : 'POST',
		async : false,
		data : {
			operate : 'getId',
			curLevel : oCurLevel,
			curLevelName : departmentName
		},
		success : function(data) {
			oCurId = data;
		}
	});
	return oCurId;
}

// 显示修改部门的表单
function showUpdateForm() {
	var departmentManager, leader, departmentName, ownedDepartmentName;
	departmentManager = document.getElementById("departmentManager").innerHTML;
	leader = document.getElementById("leader").innerHTML;
	departmentName = document.getElementById("departmentName").innerHTML;
	ownedDepartmentName = document.getElementById("ownedDepartmentName").innerHTML;

	document.getElementById("updateDepartmentManager").value = departmentManager;
	document.getElementById("updateLeader").value = leader;
	document.getElementById("updateLevelName").value = departmentName;
	document.getElementById("updateOwnedLevelName").value = ownedDepartmentName;

	document.getElementById("department-info-box").style.display = "none";
	document.getElementById("update-department-box").style.display = "block";
	document.getElementById("add-department-box").style.display = "none";
	document.getElementById("import-qualify-box").style.display = "none";
}

// 修改部门
function updateDepartment() {
	var oCurContentNode, oUpdateParamArray, oCurLevel, oPreDepartmentName, oUpdateLevelName, oOwnedLevelName, oManager, oLeader, oCurId, oCurDepartmentInfoArray, flag;
	oUpdateParamArray = getUpdateParamArray("update");
	oCurContentNode = oUpdateParamArray[0];
	oPreDepartmentName = oUpdateParamArray[1];
	oCurLevel = oUpdateParamArray[2];

	oUpdateLevelName = document.getElementById("updateLevelName").value;
	oOwnedLevelName = document.getElementById("updateOwnedLevelName").value;
	oManager = document.getElementById("updateDepartmentManager").value;
	oLeader = document.getElementById("updateLeader").value;
	oCurId = getDepartmentId(oPreDepartmentName, oCurLevel);

	// 进行非空判断
	flag = ifEmptyStringWhenUpdate(oManager, oLeader, oUpdateLevelName,
			oOwnedLevelName);

	if (flag) {
		// 传入后台修改部门
		$.ajax({
			url : 'InfoServlet',
			type : 'POST',
			data : {
				operate : 'update',
				curLevel : 'company',
				infoId : oCurId,
				updateLevelName : oUpdateLevelName,
				ownedLevelName : oOwnedLevelName,
				manager : oManager,
				leader : oLeader
			},
			success : function(oResultStr) {
				if (oResultStr == "1") {
					// 同步修改目录
					if (oPreDepartmentName != oUpdateLevelName)
						oCurContentNode.innerHTML = oUpdateLevelName;
					oCurDepartmentInfoArray = [ oManager, oLeader,
							oUpdateLevelName, oOwnedLevelName ];
					returnToInfoThroughUpdate(oCurDepartmentInfoArray,
							"update-department-box");
					window.wxc.xcConfirm("修改成功","success");
				} else
					window.wxc.xcConfirm("修改失败","success");
			}
		});
	} else
		return;
}

function ifEmptyStringWhenUpdate(oManager, oLeader, oCurDptmentName,
		oOwnedDptmentName) {
	var str = "", flag = true;
	if (oManager == "" || oManager == null) {
		str += "部门负责人请勿为空\n";
		flag = false;
	}
	if (oLeader == "" || oLeader == null) {
		str += "分管领导请勿为空\n";
		flag = false;
	}
	if (oCurDptmentName == "" || oCurDptmentName == null) {
		str += "部门名称请勿为空\n";
		flag = false;
	}
	if (str != "")
		window.wxc.xcConfirm(str,"info",{onOK:function(){
			return flag;
		}});
	return flag;
}

// 删除部门
function deleteDepartment() {
	var oUpdateParamArray, oCurContentNode, oCurLevel, oDepartmentName, oCurDepartmentInfoArray, oTempParentNode, flag, str;
	oUpdateParamArray = getUpdateParamArray("delete");
	oCurContentNode = oUpdateParamArray[0];
	oDepartmentName = oUpdateParamArray[1];
	oCurLevel = oUpdateParamArray[2];

	switch (oCurLevel) {
	case "company":
		str = "确认删除当前部门？";
		break;
	default:
		str = "确认删除当前部门？这将会同时删除其下属部门";
		break;
	}

	window.wxc.xcConfirm(str,"confirm",{onOk:function(){ 
		// 传入后台删除部门
		$
				.ajax({
					url : 'InfoServlet',
					type : 'POST',
					data : {
						operate : 'delete',
						curLevel : oCurLevel,
						curLevelName : oDepartmentName
					},
					success : function(oResultStr) {
						if (oResultStr == "1") {
							// 注意顺序
							// 获取其上一级目录
							oTempParentNode = oCurContentNode.parentNode.parentNode.parentNode;

							// 同步把目录节点移除
							$(oCurContentNode.parentNode.parentNode).remove();

							// 如果其上一级目录在这次删除后没有下级部门了logo就变成stoplogo
							if (oTempParentNode.getElementsByTagName("ul").length == 0) {
								oTempParentNode.getElementsByTagName("a")[0].className = "stoplogo";
							}

							// 同步把部门信息置空,返回到部门基本信息
							oCurDepartmentInfoArray = [ "", "", "", "" ];
							returnToInfoThroughUpdate(oCurDepartmentInfoArray,
									"");
							showOperateButton("");
							window.wxc.xcConfirm("删除成功","success");
						} else
							window.wxc.xcConfirm("删除失败","success");
					}
				});
	}});
}

// 添加、删除、修改后回到部门基本信息子页面
function returnToInfoThroughUpdate(oCurDepartmentInfoArray, oFormBox) {
	if (oFormBox != "")
		document.getElementById(oFormBox).style.display = "none";
	document.getElementById("departmentManager").innerHTML = oCurDepartmentInfoArray[0];
	document.getElementById("leader").innerHTML = oCurDepartmentInfoArray[1];
	document.getElementById("departmentName").innerHTML = oCurDepartmentInfoArray[2];
	document.getElementById("ownedDepartmentName").innerHTML = oCurDepartmentInfoArray[3];
	document.getElementById("department-info-box").style.display = "block";
}

// 显示导入资质文件压缩包的表单
function showImportQualifyForm() {
	document.getElementById("department-info-box").style.display = "none";
	document.getElementById("update-department-box").style.display = "none";
	document.getElementById("add-department-box").style.display = "none";
	document.getElementById("import-qualify-box").style.display = "block";
}

// 提交到验证文件类型
function validateFileType(oCurFile) {
	var start, end, oFileType;
	start = oCurFile.value.lastIndexOf(".") + 1;
	end = oCurFile.value.length;
	if (start < end) {
		oFileType = oCurFile.value.substring(start, end);
	}
	if (oFileType != "zip") {
		document.getElementById("importFile-input").innerHTML = '请选择导入的压缩包：<input type="file" id="importFile" name="importFile" accept="application/zip " onchange="validateFileType(this);">';
		window.wxc.xcConfirm("请上传zip压缩包格式的文件","info");
	}
}

// ajax上传文件到后台
function uploadQualifyZip() {
	// 获取要导入资源文件的企业名
	var departmentName = document.getElementById("departmentName").innerHTML;

	$.ajaxFileUpload({
		url : 'QualifyServlet?operate=importQualify&departmentName='
				+ departmentName,
		secureuri : false,
		fileElementId : 'importFile',
		dataType : "text",
		success : function(data) {
			if (data == "1")
				window.wxc.xcConfirm("导入成功","success",{onOk:function(){
					returnToInfo();
				}});
			else
				window.wxc.xcConfirm("导入失败","success",{onOk:function(){
					returnToInfo();
				}});
		}
	});
}

// 添加部门、修改部门、导入资质文件子页面 点击返回按钮
function returnToInfo() {
	document.getElementById("department-info-box").style.display = "block";
	document.getElementById("add-department-box").style.display = "none";
	document.getElementById("update-department-box").style.display = "none";
	document.getElementById("import-qualify-box").style.display = "none";
}

// 显示代储企业资质文件子页面
function showQualifyForm() {
	// 隐藏其他窗口
	document.getElementById("qualify-box-info").style.display = "block";
	document.getElementById("treecontent-box").style.display = "none";
	document.getElementById("department-box").style.display = "none";
	// 显示代储企业名字
	var ownedUnit = document.getElementById("departmentName").innerHTML;
	document.getElementById("curCompanyName").innerHTML = "代储企业：" + ownedUnit;
	// 首次进入，初始化参数
	initParam(1);
	// welcomePage表示第一次进入
	searchThroughPage("welcomePage");
}

// 资质文件子页面 操作页码按钮实现查询
function searchThroughPage(toPage) {
	// curPageNum,pageSize,toPage,pageSum是两种查询都需要的参数
	var curPageNum = eval(document.getElementById("curPageNum").innerHTML);
	var pageSize = eval(document.getElementById("selectPageSize").value);
	var pageSum = eval(document.getElementById("pageSum").innerHTML);

	// ownedUnit不需要页面方法提供
	var ownedUnit = document.getElementById("departmentName").innerHTML;

	// 这三个是条件查询需要的
	var searchStr = document.getElementById("searchStr").value;
	var searchType = document.getElementById("searchType").value;
	var year= document.getElementById("year").value;

	showQualifySum(searchStr, searchType, year,pageSum, pageSize, ownedUnit);

	// toPage参数表示上一页，下一页。。。
	switch (toPage) {
	// 上一页
	case "prePage":
		if (curPageNum >= 2)
			curPageNum = curPageNum - 1;
		break;
	// 下一页
	case "nextPage":
		if (curPageNum < pageSum)
			curPageNum = curPageNum + 1;
		break;
	// 首页
	case "firstPage":
		curPageNum = 1;
		break;
	// 末页
	case "finalPage":
		curPageNum = pageSum;
		break;
	// 跳转到某一页
	case "turnToPage":
		var turnPage = document.getElementById("skipPageNum").value;
		if (turnPage <= 0 || turnPage > pageSum) {
			window.wxc.xcConfirm("请输入有效页面","info",{onOK:function(){
				return;
			}});
		} else
			curPageNum = turnPage;
		break;
	// 首次进入
	case "welcomePage":
		curPageNum = 1;
		pageSize = 10;
		break;
	default:
		break;
	}

	// 选择页码操作后显示当前在第几页
	document.getElementById("curPageNum").innerHTML = curPageNum;

	showQualify(curPageNum, pageSize, year,ownedUnit, searchStr, searchType);
}

// 资质文件子页面 操作查询和下拉框按钮实现查询
function searchThroughButton() {
	// 初始化参数
	initParam();

	// 获取当前方法必须的参数
	var curPageNum = eval(document.getElementById("curPageNum").innerHTML);
	var pageSize = document.getElementById("selectPageSize").value;
	var pageSum = eval(document.getElementById("pageSum").innerHTML);

	// ownedUnit不需要页面方法提供
	var ownedUnit = document.getElementById("departmentName").innerHTML;

	// 这三个是条件查询需要的
	var searchStr = document.getElementById("searchStr").value;
	var searchType = document.getElementById("searchType").value;
	var year = document.getElementById("year").value;

	showQualifySum(searchStr, searchType, year, pageSum, pageSize,
			ownedUnit);

	showQualify(curPageNum, pageSize, year, ownedUnit, searchStr,
			searchType);
}

// 首次进入子页面、每一次点击查询按钮、每一次选择下拉框都需要初始化参数
function initParam(type) {
	if (type == 1) {
		document.getElementById("searchStr").value = "请输入查询的文件名";
		document.getElementById("searchType").value = "所有类型";
		document.getElementById("year").value="请输入查询的年份";
		document.getElementById("selectPageSize").value = 10;
	}
	document.getElementById("skipPageNum").value = "";
	document.getElementById("pageSum").innerHTML = "";
	document.getElementById("curPageNum").innerHTML = 1;
}

// 显示共多少页
function showQualifySum(searchStr, searchType, year, pageSum,
		pageSize, ownedUnit) {
	// 显示当前代储企业满足条件的全部文件总数
	var sum = getLikeQulifySum(ownedUnit, searchStr, searchType, year);
	pageSum = getPageSum(sum, pageSize);
	document.getElementById("pageSum").innerHTML = pageSum;
}

// 根据参数显示符合条件资质文件
function showQualify(curPageNum, pageSize, year, ownedUnit,
		searchStr, searchType) {
	operate = "searchLikeQualify";
	// 查询满足条件的文件
	searchLikeQualify(operate, curPageNum, year, pageSize,
			ownedUnit, searchStr, searchType);
}

// 向后台发送请求 ，获取当前企业文件个数
function getQualifySum(ownedUnit) {
	var sum;
	$.ajax({
		url : 'QualifyServlet',
		type : 'POST',
		async : false,
		data : {
			operate : "getQulifySum",
			ownedUnit : ownedUnit
		},
		success : function(oQualifySum) {
			sum = oQualifySum;
		}
	});
	return sum;
}

// 向后台发送请求 ，获取当前企业符合条件的文件个数
function getLikeQulifySum(ownedUnit, searchStr, searchType, year) {
	var sum;
	$.ajax({
		url : 'QualifyServlet',
		type : 'POST',
		async : false,
		data : {
			operate : "getLikeQulifySum",
			ownedUnit : ownedUnit,
			searchStr : searchStr,
			searchType : searchType,
			year : year,
		},
		success : function(oQualifySum) {
			sum = oQualifySum;
		}
	});
	return sum;
}

// 根据总数和每页显示多少条显示页数
function getPageSum(sum, pageSize) {
	var pageSum = sum % pageSize == 0 ? (sum / pageSize) : (parseInt(sum
			/ pageSize) + 1);
	return pageSum;
}

// 向后台发送请求，获取当前页文件
function searchQualify(operate, curPageNum, pageSize, ownedUnit) {
	$.ajax({
		url : 'QualifyServlet',
		type : 'POST',
		data : {
			operate : operate,
			ownedUnit : ownedUnit,
			curPageNum : curPageNum,
			pageSize : pageSize
		},
		dataType : "json",
		success : function(oQualifyJSON) {
			createQualifyTable(oQualifyJSON, 1, curPageNum, pageSize);
		}
	});
}

// 向后台发送请求，获取当前页符合条件的文件
function searchLikeQualify(operate, curPageNum, year, pageSize,
		ownedUnit, searchStr, searchType) {
	$.ajax({
		url : 'QualifyServlet',
		type : 'POST',
		data : {
			operate : operate,
			searchStr : searchStr,
			searchType : searchType,
			year : year,
			curPageNum : curPageNum,
			pageSize : pageSize,
			ownedUnit : ownedUnit
		},
		dataType : "json",
		success : function(oQualifyJSON) {
			createQualifyTable(oQualifyJSON, 2, curPageNum, pageSize);
		}
	});
}

// 拼接资质文件表格
function createQualifyTable(oQualifyJSON, type, curPageNum, pageSize) {
	var tableIndex = (curPageNum - 1) * pageSize + 1;
	if (oQualifyJSON.length == 0) {
		$("#qualification-table-body").html("");
		window.wxc.xcConfirm("没有符合条件资质文件或没有资质文件","info");
	} else {
		var str = "";
		for ( var i = 0; i < oQualifyJSON.length; i++) {
			str += "<tr";
			if(i%2!=0)
				str+=" class='addTrColor'";
			str += ">";
			str += "<td>" + tableIndex + "</td>";
			str += "<td>" + oQualifyJSON[i].qualifyTitle + "</td>";
			str += "<td>" + oQualifyJSON[i].qualifyType + "</td>";
			str += "<td>"+oQualifyJSON[i].year+"</td>";
			str += "<td><input type=\"button\" name=\"download\" value=\"下载\" onclick=\"downloadQualify("
					+ oQualifyJSON[i].qualifyId + ");\"></td>";
			str += "</tr>";
			tableIndex++;
		}
		$("#qualification-table-body").html(str);
	}
}

// 军代室及以上版本 下载资质文件,下载不用异步
function downloadQualify(qualifyId) {
	window.wxc.xcConfirm("确认下载当前资质文件？","confirm",{onOk:function(){ 
		window.location.href = "QualifyServlet?operate=downloadQualify&qualifyId="
			+ qualifyId;
	}});
}

// 显示资质文件子页面 点击返回按钮
function returnToContentAndInfo() {
	$("#qualification-table-body").html("");
	document.getElementById("qualify-box-info").style.display = "none";
	document.getElementById("treecontent-box").style.display = "block";
	document.getElementById("department-box").style.display = "block";
}
