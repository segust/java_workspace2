// 温度管理控制查找条件不能为空后跳转
function searchTemperatureByCondition() {
	var startTemp = document.getElementById("startTemp").value;
	var endTemp = document.getElementById("endTemp").value;
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;

	// 输入框验证
	var str = "", flag = true;
	if (startTemp == "" && endTemp != "") {
		str += "起始标准温度为空\n";
		if (isNaN(endTemp)) {
			str += "结束标准温度不为数字\n";
		}
		flag = false;
	}

	if (startTemp != "" && endTemp == "") {
		str += "结束标准温度为空\n";
		if (isNaN(startTemp)) {
			str += "起始标准温度不为数字\n";
		}
		flag = false;
	}

	if (startTemp != "" && endTemp != "") {
		if (isNaN(endTemp)) {
			str += "结束标准温度不为数字\n";
			flag = false;
		}
		if (isNaN(startTemp)) {
			str += "起始标准温度不为数字\n";
			flag = false;
		}
	}

	if (Number(startTemp) > Number(endTemp)) {
		str += "起始标准温度大于结束标准温度\n";
		flag = false;
	}

	if (!flag) {
		alert(str);
		return;
	}

	// var oTempNum = getCountByDateCondition();
	/*
	 * if (startTemp == "" && endTemp == "" && startDate == "" && endDate == "")
	 * window.location.href = "TemperatureServlet?pageSize=10"; else if
	 * (startTemp != "" && endTemp == "") alert("结束温度不可为空"); else if (startTemp == "" &&
	 * endTemp != "") alert("起始温度不可为空"); else if (startDate != "" && endDate ==
	 * "") alert("结束时间段不可为空"); else if (startDate == "" && endDate != "")
	 * alert("起始时间段不可为空"); else if (startTemp > endTemp) alert("起始温度大于结束温度");
	 * else if (startDate > endDate) alert("起始时间大于结束时间"); else
	 * window.location.href =
	 * "TemperatureServlet?operate=getQualifyTemperature&startTemp=" + startTemp +
	 * "&endTemp=" + endTemp + "&startDate=" + startDate + "&endDate=" +
	 * endDate;
	 */

	if (startDate == "" && endDate == "")
		window.location.href = "TemperatureServlet?pageSize=10&startTemp="
				+ startTemp + "&endTemp=" + endTemp;
	else if (startDate != "" && endDate == "")
		alert("结束时间段不可为空");
	else if (startDate == "" && endDate != "")
		alert("起始时间段不可为空");
	else if (startDate > endDate)
		alert("起始时间大于结束时间");
	else
		window.location.href = "TemperatureServlet?operate=getQualifyTemperature&startTemp="
				+ startTemp
				+ "&endTemp="
				+ endTemp
				+ "&startDate="
				+ startDate
				+ "&endDate=" + endDate;
}

// 湿度管理控制查找条件不能为空后跳转
function searchHumidityByCondition() {
	var startHumidity = document.getElementById("startHumidity").value;
	var endHumidity = document.getElementById("endHumidity").value;
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;

	// 输入框验证
	var str = "", flag = true;
	if (startHumidity == "" && endHumidity != "") {
		str += "起始标准湿度为空\n";
		if (isNaN(endHumidity)) {
			str += "结束标准湿度不为数字\n";
		}
		flag = false;
	}

	if (startHumidity != "" && endHumidity == "") {
		str += "结束标准湿度为空\n";
		if (isNaN(startHumidity)) {
			str += "起始标准湿度不为数字\n";
		}
		flag = false;
	}

	if (startHumidity != "" && endHumidity != "") {
		if (isNaN(endHumidity)) {
			str += "结束标准湿度不为数字\n";
			flag = false;
		}
		if (isNaN(startHumidity)) {
			str += "起始标准湿度不为数字\n";
			flag = false;
		}
	}

	if (Number(startHumidity) > Number(endHumidity)) {
		str += "起始标准湿度大于结束标准湿度\n";
		flag = false;
	}

	if (!flag) {
		alert(str);
		return;
	}

	/*
	 * if (startHumidity == "" && endHumidity == "" && startDate == "" &&
	 * endDate == "") window.location.href = "HumidityServlet?pageSize=10"; else
	 * if (startHumidity != "" && endHumidity == "") alert("结束湿度不可为空"); else if
	 * (startHumidity == "" && endHumidity != "") alert("起始湿度不可为空"); else if
	 * (startDate != "" && endDate == "") alert("结束时间段不可为空"); else if (startDate == "" &&
	 * endDate != "") alert("起始时间段不可为空"); else if (startHumidity > endHumidity)
	 * alert("起始湿度大于结束湿度"); else if (startDate > endDate) alert("起始时间大于结束时间");
	 * else window.location.href =
	 * "HumidityServlet?operate=getQualifyHumidity&startHumidity=" +
	 * startHumidity + "&endHumidity=" + endHumidity + "&startDate=" + startDate +
	 * "&endDate=" + endDate;
	 */

	if (startDate == "" && endDate == "")
		window.location.href = "HumidityServlet?pageSize=10&startHumidity="
				+ startHumidity + "&endHumidity=" + endHumidity;
	else if (startDate != "" && endDate == "")
		alert("结束时间段不可为空");
	else if (startDate == "" && endDate != "")
		alert("起始时间段不可为空");
	else if (startDate > endDate)
		alert("起始时间大于结束时间");
	else
		window.location.href = "HumidityServlet?operate=getQualifyHumidity&startHumidity="
				+ startHumidity
				+ "&endHumidity="
				+ endHumidity
				+ "&startDate="
				+ startDate + "&endDate=" + endDate;
}

/* 温度highchart图表 */

// $(function () {
// $('tem-chart').highcharts({
// chart: {
// type: 'line'
// },
// title: {
// text: 'Temperature and Time'
// },
// yAxis: {
// title: {
// text: 'Temperature (°C)'
// }
// },
// tooltip: {
// enabled: false,
// formatter: function() {
// return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'°C';
// }
// },
// plotOptions: {
// line: {
// dataLabels: {
// enabled: true
// }
// }
// },
// series: [{
// name: 'Tokyo',
// data: [7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
// }]
// });
// });
function showTemChart(json) {
	console.log(1);
	var data = mj.tools.toJson(json);
	var xset = [], yset = [];

	for (var i = 0; i < data.length; i++) {
		xset.push(data[i].date);
		yset.push(data[i].temperature);
	}

	$(function() {
		$('#tem-chart-container').highcharts({
			chart : {
				type : 'line',
				zoomType : 'x'
			},

			title : {
				text : '库房温度折线图'
			},
			xAxis : {
				categories : xset,
				labels : {
					enabled : false
				},
				title : {
					text : ''
				}
			},
			yAxis : {
				title : {
					text : '温度 (°C)'
				}
			},
			tooltip : {
				formatter : function() {
					return 'x:' + this.x + '<br/>' + 'y:' + this.y + '°C'
							+ '<br/>';
				}
			},
			legend : {
				enabled : false
			},
			credits : {
				enabled : false
			},
			plotOptions : {
				line : {
					dataLabels : {
						enabled : true
					}
				}
			},
			series : [{

						data : yset
					}]
		}, function(chart) {
			var oForm = document.getElementsByTagName('form'), oFormInput = oForm[0]
					.getElementsByTagName('input');
			console.log(oFormInput[0].value);
			// 获得序列列表
			var seriesList = chart.series;
			// 遍历每一条序列的每一个数据点
			for (var i = 0; i < seriesList.length; i++) {
				var pointList = seriesList[i].points;
				// 遍历当前序列的每一个数据点
				for (var j = 0; j < pointList.length; j++) {
					// 判断数据点的y值是否大于警戒值
					if (pointList[j].y < oFormInput[0].value
							|| pointList[j].y > oFormInput[1].value) {
						chart.series[i].points[j].update({
									color : "red"
								});
					}
				}
			}
		});
	});
}

function showHumChart(json) {
	console.log(2);
	var data = mj.tools.toJson(json);
	var xset = [], yset = [];

	for (var i = 0; i < data.length; i++) {
		xset.push(data[i].date);
		yset.push(data[i].humidity);
	}
	$(function() {

		$('#humi-chart-container').highcharts({
					chart : {

						type : 'line'
					},
					title : {
						text : '库房湿度折线图'
					},
					xAxis : {
						categories : xset,
						labels : {
							enabled : false
						},
						title : {
							text : ''
						}
					},
					yAxis : {
						title : {
							text : '湿度 (%rh)'
						}
					},
					tooltip : {
						formatter : function() {
							return 'x:' + this.x + '<br/>' + 'y:' + this.y
									+ '<br/>';
						}
					},
					legend : {
						enabled : false
					},
					credits : {
						enabled : false
					},
					plotOptions : {
						line : {
							dataLabels : {
								enabled : true
							}
						}
					},
					series : [{

								data : yset
							}]
				}, function(chart) {
					var oForm = document.getElementsByTagName('form'), oFormInput = oForm[0]
							.getElementsByTagName('input');
					// 获得序列列表
					var seriesList = chart.series;
					// 遍历每一条序列的每一个数据点
					for (var i = 0; i < seriesList.length; i++) {
						var pointList = seriesList[i].points;
						// 遍历当前序列的每一个数据点
						for (var j = 0; j < pointList.length; j++) {
							// 判断数据点的y值是否大于警戒值
							if (pointList[j].y < oFormInput[0].value
									|| pointList[j].y > oFormInput[1].value) {
								chart.series[i].points[j].update({
											color : "red"
										});
							}
						}
					}
				});
	});
}

Highcharts.setOptions({
			colors : ['#009966']
		});

var mj = {};
mj.app = {};
mj.tools = {};

mj.app.loadChart = function() {
	var oForm = document.getElementsByTagName('form'), oFormInput = oForm[0]
			.getElementsByTagName('input');
	if (document.getElementById('tem-chart-container')) {

		if (oFormInput[2].value == "" && oFormInput[3].value == "") {
			showChart();
			mj.tools
					.ajax(
							"http://localhost:8080/6905/TemperatureServlet?operate=getAllJSONTemperature",
							showTemChart);
		} else {
			var startTemp = oFormInput[0].value, endTemp = oFormInput[1].value, startDate = oFormInput[3].value, endDate = oFormInput[4].value;
			showChart();
			mj.tools
					.ajax(
							"http://localhost:8080/6905/TemperatureServlet?operate=getAllQualifyTemperature&startTemp="
									+ "&startDate="
									+ startDate
									+ "&endDate="
									+ endDate, showTemChart);
		}
	} else if (document.getElementById('humi-chart-container')) {

		if (oFormInput[2].value == "" && oFormInput[3].value == "") {
			showChart();
			mj.tools
					.ajax(
							"http://localhost:8080/6905/HumidityServlet?operate=getAllJSONHumidity",
							showHumChart);
		} else {
			var startHumi = oFormInput[0].value, endHumi = oFormInput[1].value, startDate = oFormInput[3].value, endDate = oFormInput[4].value;
			showChart();

			mj.tools
					.ajax(
							"http://localhost:8080/6905/HumidityServlet?operate=getAllQualifyHumidity&startHumidity="
									+ "&startDate="
									+ startDate
									+ "&endDate="
									+ endDate, showHumChart);
		}
	}

}

mj.app.addOnclick = function() {

	var oShowChart = document.getElementById('showChart');
	oShowChart.onclick = function() {
		mj.app.loadChart();
	};

};

function showChart() {
	var oTable = document.getElementById('table-box'), oChart, oRightChilds = document
			.getElementById('right').childNodes, oPageBox;
	mj.tools.pageBoxIndex = -1;
	for (var i = 0; i < oRightChilds.length; i++) {
		if (oRightChilds[i].nodeType == 1
				&& oRightChilds[i].className == "page-box") {
			oPageBox = oRightChilds[i];
			mj.tools.pageBoxIndex = i;
			break;
		}
	}
	if (document.getElementById('tem-chart-container')) {
		oChart = document.getElementById('tem-chart-container');
	} else {
		oChart = document.getElementById('humi-chart-container');
	}
	oTable.style.display = "none";
	oPageBox.style.display = "none";
	oChart.style.display = "block";
}

function showTable() {
	var oTable = document.getElementById('table-box'), oChart;
	if (document.getElementById('tem-chart-container')) {
		oChart = document.getElementById('tem-chart-container');
	} else {
		oChart = document.getElementById('humi-chart-container');
	}

	oTable.style.display = "block";
	oPageBox[mj.tools.pageBoxIndex].style.display = "block";
	oChart.style.display = "none";
}

// 发送ajax的函数（gai）
mj.tools.ajax = function(url, callback, fail, flag) {
	// alert(url);
	var xhr = mj.tools.createXHR();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status >= 200 && xhr.status < 300 || xhr.status == 304) {
				callback(xhr.responseText);
			} else {
				if (fail) {
					fail(xhr.status);
				}
			}
		}
	};
	xhr.open("get", url, true);
	xhr.send(null);
};
/* 返回json解析后的js对象 */
mj.tools.toJson = function(jsontext) {
	var obj = null;
	if (JSON instanceof Object) {
		obj = JSON.parse(jsontext);

	} else {
		obj = eval(jsontext);

	}
	return obj;
};

/* 兼容不同浏览器的xhr */
mj.tools.createXHR = function() {
	if (typeof XMLHttpRequest != "undefined") {
		createXHR = function() {
			return new XMLHttpRequest();
		};
	} else if (typeof ActiveXObject != "undefined") {
		createXHR = function() {
			if (typeof arguments.callee.activeXString != "string") {
				var versions = ["MSXML2.XMLHttp.6.0", "MSXML.XMLHttp.3.0",
						"MSXML2.XMLHttp"], i, len;
				for (i = 0, len = versions.length; i < len; i++) {
					try {
						new ActiveXObject(versions[i]);
						arguments.callee.activeXString = versions[i];
						break;
					} catch (ex) {
					}
				}
			}
			return new ActiveXObject(arguments.callee.activeXString);
		};
	} else {
		createXHR = function() {
			throw new Error("No XMLHttpRequest obj");
		};
	}
	return createXHR();
};

var lhs = {};
lhs.app = {};

/* 根据当前温度或湿度是否超出标准显示黑色或红色 */
lhs.app.showUnusualRecords = function() {
	var oMinNum, oMaxNum, oMinNumstr, oMaxNumStr, oRecordTable, oRecordRow, oRecord;

	if (document.getElementById("temper-table")) {
		oMinNumstr = document.getElementById("startTemp").value;
		oMaxNumStr = document.getElementById("endTemp").value;
		oRecordTable = document.getElementById("temper-table");
	} else if (document.getElementById("humidity-table")) {
		oMinNumstr = document.getElementById("startHumidity").value;
		oMaxNumStr = document.getElementById("endHumidity").value;
		oRecordTable = document.getElementById("humidity-table");
	}
	oMinNum = Number(oMinNumstr);
	oMaxNum = Number(oMaxNumStr);

	oRecordRow = oRecordTable.getElementsByTagName("tr");
	if (oMinNumstr != "" && oMaxNumStr != "") {
		for (var i = 0; i < oRecordRow.length; i++) {
			oRecord = Number(oRecordRow[i].getElementsByTagName("td")[0].innerHTML);
			if (oRecord >= oMaxNum + 1 || oRecord <= oMinNum - 1) {
				oRecordRow[i].style.color = "red";
				var oInputArray = oRecordRow[i].getElementsByTagName("input");
				var oTempLength = oInputArray.length;
				for (var j = 0; j < oTempLength; j++) {
					oInputArray[j].style.color = "red";
				}
			}
		}
	}
}

// 获取满足条件的记录数，不满足条件的记录数
lhs.app.showCountByCondition = function() {
	var oStartTemp, oStartTempValue, oEndTemp, oEndTempValue, oStartHumdity, oStartHumdityValue, oEndHumdity, oEndHumdityValue, oStartDate, oEndDate, oNum;
	oStartTemp = document.getElementById("startTemp");
	oEndTemp = document.getElementById("endTemp");
	oStartDate = document.getElementById("startDate").value;
	oEndDate = document.getElementById("endDate").value;
	oStartHumdity = document.getElementById("startHumidity");
	oEndHumidity = document.getElementById("endHumidity");

	if (oStartTemp != null) {
		oStartTempValue = oStartTemp.value;
		oEndTempValue = oEndTemp.value;
		$.ajax({
					url : 'TemperatureServlet',
					type : 'POST',
					data : {
						operate : 'getTotalQualifyRecords',
						startTemp : oStartTempValue,
						endTemp : oEndTempValue,
						startDate : oStartDate,
						endDate : oEndDate
					},
					// async:false,
					success : function(oData) {
						oNum = oData.split(";")
						document.getElementById("matchNum").innerHTML = oNum[0];
						document.getElementById("unmatchNum").innerHTML = oNum[1];
					}
				});
	} else {
		oStartHumdityValue = oStartHumdity.value;
		oEndHumdityValue = oEndHumidity.value;
		$.ajax({
					url : 'HumidityServlet',
					type : 'POST',
					data : {
						operate : 'getTotalQualifyRecords',
						startHumdity : oStartHumdityValue,
						endHumidity : oEndHumdityValue,
						startDate : oStartDate,
						endDate : oEndDate
					},
					// async:false,
					success : function(oData) {
						oNum = oData.split(";")
						document.getElementById("matchNum").innerHTML = oNum[0];
						document.getElementById("unmatchNum").innerHTML = oNum[1];
					}
				});
	}

}

//// 获取满足条件的记录数，不满足条件的湿度记录数
//lhs.app.showCountByHumiCondition = function() {
//	var startHumdiity = document.getElementById("startHumidity").value;
//	var endHumidity = document.getElementById("endHumidity").value;
//	var startDate = document.getElementById("startDate").value;
//	var endDate = document.getElementById("endDate").value;
//	var oTempNum;
//
//}

// 显示修改记录
function showUpdateRecord(event, oRecordId, oRecordType) {
	var oOldTempNode, oUpdateTempNode, oUpdateNum, oUpdateDate, oUpdatePosition, oOldNum, oOldDate, oOldPosition, oNumDifStr, oDateDifStr, oPositionDifStr, oTypeStr, str;
	var oOldRecordArray = new Array();
	switch (oRecordType) {
		case "temperature" :
			oTypeStr = "温度";
			break;
		case "humidity" :
			oTypeStr = "湿度";
			break;
		default :
			break;
	}

	// 从页面获取修改后的记录
	oUpdateTempNode = event.parentNode.parentNode.getElementsByTagName("td");
	oUpdateNum = oUpdateTempNode[0].innerHTML;
	oUpdateDate = oUpdateTempNode[1].innerHTML;
	oUpdatePosition = oUpdateTempNode[4].innerHTML;

	// 从后台获取原始记录
	oOldRecordArray = getOldRecord(oRecordId, oRecordType);
	oOldNum = oOldRecordArray[0];
	oOldDate = oOldRecordArray[1];
	oOldPosition = oOldRecordArray[2];

	str = "style='color:red'"
	if (oUpdateNum != oOldNum)
		oNumDifStr = str;
	if (oUpdateDate != oOldDate)
		oDateDifStr = str;
	if (oUpdatePosition != oOldPosition)
		oPositionDifStr = str;

	// 将记录拼接到页面显示
	oUpdateTempNode = document.getElementById("updateRecord");
	str = "<h4>修改记录</h4>";
	str += "<p>" + oTypeStr + "：<span " + oNumDifStr + ">" + oUpdateNum
			+ "</span></p>";
	str += "<p>" + oTypeStr + "记录时间：<span " + oDateDifStr + ">" + oUpdateDate
			+ "</span></p>";
	str += "<p>位置：<span " + oPositionDifStr + ">" + oUpdatePosition
			+ "</span></p>";
	oUpdateTempNode.innerHTML = str;

	oOldTempNode = document.getElementById("oldRecord");
	str = "<h4>原始记录</h4>";
	str += "<p>" + oTypeStr + "：<span " + oNumDifStr + ">" + oOldNum
			+ "</span></p>";
	str += "<p>" + oTypeStr + "记录时间：<span " + oDateDifStr + ">" + oOldDate
			+ "</span></p>";
	str += "<p>位置：<span " + oPositionDifStr + ">" + oOldPosition
			+ "</span></p>";
	oOldTempNode.innerHTML = str;

	// 显示到修改原因子页面
	document.getElementById("search-condition-box").style.display = "none";
	document.getElementById("search-result-box").style.display = "none";
	document.getElementById("page-box").style.display = "none";
	document.getElementById("search-update-box").style.display = "block";

}

// 异步获取后台原始记录
function getOldRecord(oRecordId, oRecordType) {
	var oOldNum, oOldDate, oOldPosition;
	var oOldRecordArray = new Array();
	$.ajax({
				url : 'TemperatureServlet',
				type : 'POST',
				data : {
					operate : 'searchOldRecord',
					recordId : oRecordId,
					recordType : oRecordType
				},
				async : false,
				dataType : "json",
				success : function(oRecordJSON) {
					if (oRecordJSON.length == 1) {
						oOldNum = oRecordJSON[0].recordNum;
						oOldDate = oRecordJSON[0].curRecordDate;
						oOldPosition = oRecordJSON[0].position;
						oOldRecordArray = [oOldNum, oOldDate, oOldPosition];
					} else {
						alert("获取原始记录失败");
					}
				}
			});
	return oOldRecordArray;
}

// 回到温度或湿度页面
function returnToRecords() {
	document.getElementById("search-condition-box").style.display = "block";
	document.getElementById("search-result-box").style.display = "block";
	document.getElementById("page-box").style.display = "block";
	document.getElementById("search-update-box").style.display = "none";
}

// 点击清空查询条件
function clearSearchCondition(oType) {
	document.getElementById("endDate").value = "";
	document.getElementById("startDate").value = "";
}
