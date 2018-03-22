var baseInfo = {
	baseUrl : '',
	nowPage : -1,
};

addChooseFirmEv();

$(".paging-wrape").addClass("hide");

$(".fare-table").addClass("hide");
// 给分页按钮添加点击事件
function addPageEv() {

	var oPagingWrap = $(".paging-wrape"), reqPage,

	totalPage = parseInt($(".paging-wrape .total span")[0].innerHTML), oA = $(".paging-wrape")[0]
			.getElementsByTagName("a");

	for (var i = 0; i < $(".pagination a").length; i++) {
		if ($(".pagination a")[i].className == "active") {
			baseInfo.nowPage = parseInt($(".pagination a")[i].innerHTML);
		}

	}

	for (var i = 0; i < oA.length; i++) {

		oA[i].onclick = function(event) {

			var ev = event || window.event;
			targ = ev.target || ev.srcElement;

			if (targ.getAttribute("aria-label") == "Previous") {
				if (baseInfo.nowPage > 1) {
					reqPage = baseInfo.nowPage - 1;
				} else {
					reqPage = baseInfo.nowPage;

				}

			} else if (targ.getAttribute("aria-label") == "Next") {
				if (baseInfo.nowPage < totalPage) {

					reqPage = baseInfo.nowPage + 1;

				} else {
					reqPage = baseInfo.nowPage;
				}
			} else if (targ.innerHTML == "确定") {
				var inputPage = parseInt($(".go-page .page_Input")[0].value);

				if (inputPage > totalPage || inputPage < 1) {
					reqPage = baseInfo.nowPage;
				} else {
					reqPage = inputPage;
				}

			} else {
				reqPage = this.innerHTML;
			}

			getProData(baseInfo.baseUrl, reqPage);
		};
	}
}

// 获得数据
function getProData(baseUrl, reqPage) {
	$.ajax({
		type : 'post',
		url : baseUrl,
		data : {
			keeper : $('#ownedUnit option:selected').text(),
			curPageNum : reqPage
		},
		dataType : 'json',
		success : function(data) {
			// console.log(JSON.stringify(data));
			loadPro(data);
		}
	});
}

// 异步加载表格
function loadPro(tableData) {
	var oTbody = $("#fare-table tbody"), nowPage = tableData.nowPage, totalPage = tableData.totalPage;
	oTbody.eq(0).html("");
	for (var i = 0; i < tableData.items.length; i++) {
		$("#fare-table tbody")[0].insertRow(i);
		oTbody[0].rows[i].insertCell(0);
		oTbody[0].rows[i].cells[0].innerHTML = '<input type="checkbox" onclick="selectToOut(this);"><input type="hidden" value="'
				+ tableData.items[i][0] + '">';
		for (var j = 1; j < 14; j++) {
			oTbody[0].rows[i].insertCell(j);
			oTbody[0].rows[i].cells[j].innerHTML = tableData.items[i][j];
		}
	}

	for (var i = 0; i < $(".pagination a").length; i++) {
		$(".pagination a").eq(i).removeClass("active");
	}

	$(".pagination a").eq(nowPage).addClass("active");
	baseInfo.nowPage = nowPage;

	clearOutData();
}

// 在选择企业后清空下面已选择的数据
function clearOutData() {
	var table = document.getElementById("list-content"), botonTrs = table
			.getElementsByTagName('tr');

	for (var i = 1; i < botonTrs.length; i++) {
		var oFirstTds = botonTrs[i].getElementsByTagName("td");
		if (i == 1) {
			for (var j = 0; j < oFirstTds.length; j++) {
				var oInput0 = oFirstTds[j].getElementsByTagName("input")[0], oInput1 = oFirstTds[j]
						.getElementsByTagName("input")[1];
				switch (j) {
				case 0:
					break;
				case 1:
					oInput0.value = "";
					oInput1.value = "";
					break;
				case 6:
					oInput0.value = "";
					oInput1.value = "";
					// $(oInput1).style="";
					// oFirstTds[j].getElementsByTagName("input")[1].style="";
					break;
				case 10:
					oInput0.value = "";
					oInput1.value = "";
					break;
				default:
					oInput0.value = "";
					break;
				}
			}
		} else {
			$(botonTrs[i]).remove();
			sortOrderId();
		}
	}
}

// 发料单序号重排
function sortOrderId() {
	var oTable = document.getElementById("list-content"), oTrs = oTable
			.getElementsByTagName("tr");
	for (var i = 1; i < oTrs.length; i++) {
		var oTd = oTrs[i].getElementsByTagName("td")[0];
		$(oTd).html(i);
	}
}

// 给选择代储企业添加onchange事件
function addChooseFirmEv() {
	document.getElementById("ownedUnit").onchange = goforkeeper;
}

// 根据选择的代储企业加载相应数据
function goforkeeper() {
	var keeper = $('#ownedUnit option:selected').text(), // 选中的文本
	url = "OutWarehouseServlet?operate=goForkeeper", str = '<li><a href="javascript:;" aria-label="Previous">&laquo;</a></li>';
	str += '<li><a class="active" href="javascript:;">' + 1 + '</a></li>';
	baseInfo.baseUrl = "OutWarehouseServlet?operate=goForkeeper";
	$
			.ajax({
				type : "post",
				url : url,
				data : {
					keeper : keeper
				},
				dataType : "json",
				success : function(data) {

					$(".paging-wrape .total span")[0].innerHTML = data.totalPage;
					for (var i = 0; i < data.totalPage - 1; i++) {
						str += '<li><a href="javascript:;">' + (i + 2)
								+ '</a></li>';
					}
					str += '<li><a href="javascript:;" aria-label="Next">&raquo;</a></li>';
					$(".paging-wrape .pagination")[0].innerHTML = str;
					// url="OutWarehouseServlet?operate=goForkeeper&keeper="+keeper;
					$(".paging-wrape").removeClass("hide");
					$(".fare-table").removeClass("hide");
					getProData(url, 1);

					addPageEv();
				},
				error : function() {
					alert("请求失败");
				}
			});
}