var baseInfo = {
	baseUrl : '',
	contractid : '',
	nowPage : -1,
	oTitle : document.getElementsByTagName("title")[0].innerHTML
};

// 模块模式
var table = (function() {
	var modelNum = {}, chooseModel = {};
	return {
		judChooseNum : function(model, operate) {
			if (operate == "add") {
				if (!chooseModel[model]) {
					chooseModel[model] = 0;
				}
				if (chooseModel[model] < Math.floor(modelNum[model] / 3)) {
					chooseModel[model]++;
					return true;
				} else {
					var txt = "当前勾选的该型号产品数量不能大于在库总量的1/3";
					window.wxc.xcConfirm(txt,
							window.wxc.xcConfirm.typeEnum.info);
					return false;
				}
				;
			} else {
				chooseModel[model]--;
			}
		},
		init : function(numByModel) {
			// if (numByModel == null)
			// return;
			for (var i = 0; i < numByModel.length; i++) {
				modelNum[numByModel[i]["model"]] = numByModel[i]["totalNum"];
			}
		},
		getModelNum : function() {
			return modelNum;
		},
		getChooseModel : function() {
			return chooseModel;
		},
		addEvToCheckbox : function() {
			var that = this;
			$("input.setTem").on(
					'click',
					function() {
						if (this.checked) {
							if (!that.judChooseNum(this
									.getAttribute("data-model"), "add")) {
								this.checked = false;
							}
						} else {
							that.judChooseNum(this.getAttribute("data-model"),
									"del");
							this.checked = false;
						}
					});
		}
	};
})();

getBaseUrl();
defaultLoad(baseInfo.baseUrl, 1);

// 确定当前页面的ajax请求的基本url
function getBaseUrl() {
	// var pageTitle=document.getElementsByTagName("title")[0].innerHTML;
	if (baseInfo.oTitle == "轮换出库申请") {
		baseInfo.baseUrl = "ProductQueryServlet?operate=getBorrowNextPage";
	} else if (baseInfo.oTitle == "更新出库") {
		baseInfo.baseUrl = "ProductQueryServlet?operate=getUpdateNextPage";
	}
}

// 给分页按钮添加点击事件
function addPageEv() {

	var oPagingWrap = $(".paging-wrape"), reqPage,

	totalPage = parseInt($(".paging-wrape .total span")[0].innerHTML), oA = $(".paging-wrape")[0]
			.getElementsByTagName("a"), contractid = baseInfo.contractid;

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

			getProData(baseInfo.baseUrl, reqPage, baseInfo.contractid);
		};
	}
}

// 获得数据
function getProData(baseUrl, reqPage) {
	$.ajax({
		type : 'post',
		url : baseUrl,
		data : {
			"curPageNum" : reqPage
		},
		dataType : 'json',
		success : function(data) {
			loadPro(data);
		}
	});
}

// 异步加载表格
function loadPro(tableData) {
	var oTbody = $("#fare-table tbody"), nowPage = tableData.nowPage, totalPage = tableData.totalPage;
	oTbody.eq(0).html("");
	for (var i = 0; i < tableData.items.length; i++) {
		oTbody[0].insertRow(i);
		oTbody[0].rows[i].insertCell(0);
		oTbody[0].rows[i].cells[0].innerHTML = '<input class="setTem" type="checkbox" name="checkbox_product" data-model="'
				+ tableData.items[i][2] + '">';

		for (var j = 1; j < tableData.items[0].length; j++) {
			oTbody[0].rows[i].insertCell(j);
			if(tableData.items[i][j]==null)
				oTbody[0].rows[i].cells[j].innerHTML=="";
			else
				oTbody[0].rows[i].cells[j].innerHTML = tableData.items[i][j];
		}

//		oTbody[0].rows[i].insertCell(j);
//		if (baseInfo.oTitle == "轮换出库申请") {
//			oTbody[0].rows[i].cells[j].innerHTML = '<input type="button" value="入库记录查询" \
//            onclick={window.location.href="/6905/ProductQueryServlet?operate=inoutInfo&type=inApply&productId=111&deviceNo=SCD031"}>\
//            <input type="button"  value="出库记录查询" onclick={window.location.href="/6905/ProductQueryServlet\
//            ?operate=inoutInfo&type=outApply&productId=111&deviceNo=SCD031"}>';
//		} else if (baseInfo.oTitle == "更新出库") {
//			oTbody[0].rows[i].cells[j].innerHTML = '<input type="button" value="入库记录查询" \
//            onclick={window.location.href="/6905/ProductQueryServlet?operate=inoutInfo&amp;type=inApply&amp;productId=111&amp;deviceNo=SCD029"}>\
//            <input type="button"  value="出库记录查询" onclick={window.location.href="/6905/ProductQueryServlet?operate=inoutInfo&amp;type=outApply&amp;productId=111&amp;deviceNo=SCD029"}>';
//		}

	}
	// <input type="button" value="入库记录查询"
	// onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&amp;type=inApply&amp;productId=111&amp;deviceNo=SCD029'}">
	// <input type="button" value="出库记录查询"
	// onclick="{window.location.href='/6905/ProductQueryServlet?operate=inoutInfo&amp;type=outApply&amp;productId=111&amp;deviceNo=SCD029'}">
	table.addEvToCheckbox();// 给checkbox添加事件

	for (var i = 0; i < $(".pagination a").length; i++) {
		$(".pagination a").eq(i).removeClass("active");
	}

	$(".pagination a").eq(nowPage).addClass("active");
	baseInfo.nowPage = nowPage;
}

function defaultLoad(baseUrl, reqPage) {
	var str = '<li><a href="javascript:;" aria-label="Previous">&laquo;</a></li>';
	str += '<li><a class="active" href="javascript:;">' + 1 + '</a></li>';
	$
			.ajax({
				type : 'post',
				url : baseUrl,
				data : {
					"curPageNum" : reqPage
				},
				dataType : 'json',
				success : function(data) {
					//console.log(data);
					loadPro(data);
					$(".paging-wrape .total span")[0].innerHTML = data.totalPage;
					for (var i = 0; i < data.totalPage - 1; i++) {
						str += '<li><a href="javascript:;">' + (i + 2)
								+ '</a></li>';
					}
					str += '<li><a href="javascript:;" aria-label="Next">&raquo;</a></li>';
					$(".paging-wrape .pagination")[0].innerHTML = str;
					addPageEv();
					table.init(data["numByModel"]);// 初始化table模块内部变量
					//console.log(table.getModelNum());
				}
			});
}

