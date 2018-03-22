// 显示添加设备记录子页面
function showAddDeviceForm() {
	document.getElementById("device-search-box").style.display = "none";
	document.getElementById("device-info-box").style.display = "none";
	document.getElementById("page-box").style.display = "none";
	document.getElementById("add-device-box").style.display = "block";
	document.getElementById("update-device-box").style.display = "none";
}

// 添加一台设备记录
function addDevice() {
	var oInputArray;
	oInputArray = document.getElementById("add-device-box")
			.getElementsByTagName("input");

	$.ajax({
		url : 'DeviceServlet',
		type : 'POST',
		data : {
			operate : 'addDevice',
			deviceName : oInputArray[0].value,
			deviceNo : oInputArray[1].value,
			position : oInputArray[2].value,
			deviceInTime : oInputArray[3].value,
			status : oInputArray[4].value,
			repairTime : ""
		},
		success : function(oResultFlag) {
			if (oResultFlag == "1") {
				window.wxc.xcConfirm("添加成功","success");
			} else
				window.wxc.xcConfirm("添加失败","error");
		}
	});
	searchThroughButton();
	returnToInfo();
}

// 显示修改设备记录子页面
function showUpdateDeviceForm(event, oDeviceId) {
	// 获取原有的设备信息
	var oTdArray, oInputArray, oSelect;
	oTdArray = event.parentNode.parentNode.getElementsByTagName("td");

	// 显示原有设备信息
	oInputArray = document.getElementById("update-device-box")
			.getElementsByTagName("input");
	oInputArray[0].value = oTdArray[0].innerHTML;
	oInputArray[1].value = oTdArray[1].innerHTML;
	oInputArray[2].value = oTdArray[2].innerHTML;
	oInputArray[3].value = oTdArray[3].innerHTML;
	document.getElementById("updatestatus").value = oTdArray[4].innerHTML;
	document.getElementById("updateDeviceId").value = oDeviceId;

	document.getElementById("device-search-box").style.display = "none";
	document.getElementById("device-info-box").style.display = "none";
	document.getElementById("page-box").style.display = "none";
	document.getElementById("add-device-box").style.display = "none";
	document.getElementById("update-device-box").style.display = "block";
}

// 修改设备信息
function updateDevice() {
	var oInputArray, oUpdateDeviceId, oSelectStatus;
	oInputArray = document.getElementById("update-device-box")
			.getElementsByTagName("input");
	oUpdateDeviceId = document.getElementById("updateDeviceId").value;
	oSelectStatus = document.getElementById("updatestatus").value;

	$.ajax({
		url : 'DeviceServlet',
		type : 'POST',
		data : {
			operate : 'updateDevice',
			deviceId : oUpdateDeviceId,
			deviceName : oInputArray[0].value,
			deviceNo : oInputArray[1].value,
			position : oInputArray[2].value,
			deviceInTime : oInputArray[3].value,
			status : oSelectStatus
		},
		success : function(oResultFlag) {
			if (oResultFlag == "1")
				window.wxc.xcConfirm("修改成功","success");
			else
				window.wxc.xcConfirm("修改失败","error");
		}
	});
	searchThroughButton();
	returnToInfo();
}

// 删除一条设备记录
// function deleteDevice(event, oDeviceId) {
// var oDelflag = window.confirm("确定删除该条设备信息？");
// if (oDelflag) {
// $.ajax({
// url : 'DeviceServlet',
// type : 'POST',
// data : {
// operate : 'deleteOneDevice',
// deviceId : oDeviceId
// },
// success : function(oResultFlag) {
// if (oResultFlag == "1") {
// alert("删除成功");
// event.parentNode.parentNode.remove();
// } else
// alert("删除失败");
// }
// });
// }
// }

// 显示某一设备维修信息的盒子
function showCurDeviceRepairInfoForm(oDeviceId) {
	document.getElementById("device-content-box").style.display = "none";
	document.getElementById("repair-content-box").style.display = "block";

	var str = "";
	$
			.ajax({
				url : 'RepairServlet',
				type : 'POST',
				data : {
					operate : 'getCurDeviceRepairInfo',
					deviceId : oDeviceId,
					curPageNum : 1,
					pageSize : 10
				},
				dataType : "json",
				success : function(oResultJSON) {
					for ( var i = 0; i < oResultJSON.length; i++) {
						str += "<tr";
						if (i % 2 != 0)
							str += " class='addTrColor'";
						str += ">";
						str += "<td>" + oResultJSON[i].repairMan + "</td>";
						str += "<td>" + oResultJSON[i].repairTime + "</td>";
						str += "<td>" + oResultJSON[i].repairReason + "</td>";
						str += "<td><input type=\"button\" value=\"修改维修记录\" onclick=\"function("
								+ oResultJSON[i].repairId + ");\"></td>";
						str += "</tr>";
					}
					$("#repair-info-tbody").html(str);
				}
			});
}

// 显示 添加设备维修记录 的盒子
function showRepairDeviceForm(event, oRepairDeviceId) {
	// 获取原有的设备信息
	var oTdArray, oInputArray;
	oTdArray = event.parentNode.parentNode.getElementsByTagName("td");

	// 显示原有设备信息
	oInputArray = document.getElementById("add-repair-box")
			.getElementsByTagName("input");
	oInputArray[1].value = oTdArray[0].innerHTML;
	oInputArray[2].value = oTdArray[1].innerHTML;

	document.getElementById("repairDeviceId").value = oRepairDeviceId;

	document.getElementById("device-search-box").style.display = "none";
	document.getElementById("device-info-box").style.display = "none";
	document.getElementById("page-box").style.display = "none";
	document.getElementById("add-repair-box").style.display = "block";
}

// 重置设备维修信息
function resetRepairInfo(event) {
	oInputArray = event.parentNode.parentNode.getElementsByTagName("input");
	oInputArray[0].value = "";
	oInputArray[3].value = "";
	oInputArray[4].value = "";
}

// 添加设备维修记录
function addRepairRecord(referer) {
	var oRepairDeviceId, oInputArray;
	oRepairDeviceId = document.getElementById("repairDeviceId").value;
	oInputArray = document.getElementById("add-repair-box")
			.getElementsByTagName("input");
	$.ajax({
		url : 'RepairServlet',
		type : 'POST',
		data : {
			operate : 'addRepairInfo',
			repairMan : oInputArray[0].value,
			deviceName : oInputArray[1].value,
			deviceNo : oInputArray[2].value,
			repairTime : oInputArray[3].value,
			repairReason : oInputArray[4].value,
			deviceId : oRepairDeviceId
		},
		success : function(oResultFlag) {
			if (oResultFlag == "1") {
				window.wxc.xcConfirm("添加成功","success");
			} else
				window.wxc.xcConfirm("添加失败","error");
		}
	});
	window.location.href = referer;
}

// 从添加设备。。。返回设备记录页面
function returnToInfo() {
	document.getElementById("device-content-box").style.display = "block";
	document.getElementById("device-search-box").style.display = "block";
	document.getElementById("device-info-box").style.display = "block";
	document.getElementById("page-box").style.display = "block";
	document.getElementById("add-device-box").style.display = "none";
	document.getElementById("update-device-box").style.display = "none";
	document.getElementById("add-repair-box").style.display = "none";
}

// 从查看设备维修信息返回设备记录页面
function returnToInfoFromRepair() {
	document.getElementById("device-content-box").style.display = "block";
	document.getElementById("repair-content-box").style.display = "none";
}

// 获取前台输入条件查询
function searchThroughButton(operate) {
	var oDeviceStartTime, oDeviceEndTime, oDeviceType, oDeviceSum, oPageSum, oDeviceStatus,oCurPageNum,oPageSize;
	oDeviceStartTime = document.getElementById("deviceStartTime").value;
	oDeviceEndTime = document.getElementById("deviceEndTime").value;
	oDeviceType = document.getElementById("searchDeviceType").value;
	oPageSize = document.getElementById("selectPageSize").value;
	oDeviceStatus = document.getElementById("chooseStatus").value;
	oCurPageNum=document.getElementById("curPageNum").innerHTML;

	oDeviceSum = getDeviceSumByCondition(oDeviceStartTime, oDeviceEndTime,
			oDeviceType, oDeviceStatus);

	oPageSum = getPageSum(oDeviceSum, oPageSize);

	$("#pageSum").html(oPageSum);
	// document.getElementById("pageSum").innerHTML = oPageSum;
	
	switch (operate) {
	case 'nextPage':
		oCurPageNum=(oCurPageNum==oPageSum)?oPageSum:oCurPageNum+1;
		break;
	case 'prePage':
		oCurPageNum=(oCurPageNum==1)?1:oCurPageNum-1;
		break;
	case 'firstPage':
		oCurPageNum=1;
		break;
	case 'lastPage':
		oCurPageNum=oPageSum;
		break;
	case 'skipPage':
		var oSkipPageNum=Number(document.getElementById("skipPageNum").value);
		if(oSkipPageNum<=0||isNaN(oSkipPageNum)){
			window.wxc.xcConfirm("请输入有效页面","error");
	 		return;
		}
	 	else if(oSkipPageNum>oPageSum){
	 		window.wxc.xcConfirm("您输入的页面大于总页数","error");
	 		return;
	 	}
	 	else{
	 		oCurPageNum=oSkipPageNum;
	 	}
		break;
	default:
		break;
	}
	
	// 异步提交到后台
	$
			.ajax({
				url : 'DeviceServlet',
				type : 'POST',
				data : {
					operate : 'searchDeviceInfoByCondition',
					deviceStartTime : oDeviceStartTime,
					deviceEndTime : oDeviceEndTime,
					deviceType : oDeviceType,
					deviceStatus : oDeviceStatus,
					curPageNum : oCurPageNum-1,
					pageSize : oPageSize
				},
				dataType : "json",
				success : function(oResultJSON) {
					// 拼接到页面显示
					var str = "", tempstr = "";
					for ( var i = 0; i < oResultJSON.length; i++) {
						tempstr = "";
						if (i % 2 != 0)
							str += "<tr class='addTrColor'>";
						else
							str += "<tr>";
						str += "<td>" + oResultJSON[i].deviceName + "</td>";
						str += "<td>" + oResultJSON[i].deviceNo + "</td>";
						str += "<td>" + oResultJSON[i].position + "</td>";
						str += "<td>" + oResultJSON[i].deviceInTime + "</td>";
						str += "<td>" + oResultJSON[i].status + "</td>";
						if (oResultJSON[i].repairTime == ""
								|| oResultJSON[i].repairTime == null) {
							// str += "<td>无</td>";
						} else {
							// str += "<td>" + oResultJSON[i].repairTime +
							// "</td>";
							tempstr = "&nbsp;<input type=\"button\" value=\"查看维修记录\" onclick=\"showCurDeviceRepairInfoForm("
									+ oResultJSON[i].deviceId + ");\"/>";
						}
						str += "<td>";
						// str += "<input type=\"button\" value=\"删除设备\"
						// onclick=\"deleteDevice(this,"
						// + oResultJSON[i].deviceId + ");\"/>&nbsp;";
						str += "<input type=\"button\" value=\"修改设备信息\" onclick=\"showUpdateDeviceForm(this,"
								+ oResultJSON[i].deviceId + ");\"/>&nbsp;";
						if (oResultJSON[i].status != "报废")
							str += "<input type=\"button\" value=\"添加维修记录\" onclick=\"showRepairDeviceForm(this,"
									+ oResultJSON[i].deviceId + ");\"/>";
						if (tempstr != "" && tempstr != null)
							str += tempstr;
						str += "</td>";
						str += "</tr>";
					}
					$("#device-table-body").html(str);
				}
			});
}

// 获取按条件查询的设备总数
function getDeviceSumByCondition(oDeviceStartTime, oDeviceEndTime, oDeviceType,
		oDeviceStatus) {
	var sum;
	$.ajax({
		url : 'DeviceServlet',
		type : 'POST',
		data : {
			operate : 'getDeviceSumByCondition',
			deviceStartTime : oDeviceStartTime,
			deviceEndTime : oDeviceEndTime,
			deviceType : oDeviceType,
			deviceStatus : oDeviceStatus
		},
		async : false,
		// dataType:"json",
		success : function(oCurDeviceSum) {
			sum = oCurDeviceSum;
		}
	});
	return sum;
}

// 清空查询条件
function clearCondition() {
	document.getElementById("deviceStartTime").value = "";
	document.getElementById("deviceEndTime").value = "";
	document.getElementById("searchDeviceType").value = "所有设备";
	document.getElementById("chooseStatus").value = "全部状态";
}

// 计算分多少页
function getPageSum(sum, pageSize) {
	var pageSum = sum % pageSize == 0 ? (sum / pageSize) : (parseInt(sum
			/ pageSize) + 1);
	return pageSum;
}
