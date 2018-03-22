var sum, num;
addPagingEv();
var tableData = new Array();// todo

getTableData();
function getTableData() {
	$.ajax({
		type : 'get',
		url : 'ApplyHandleServlet?operate=translate_get&random='
				+ Math.random(),
		dataType : 'json',
		success : function(data) {
			tableData = data;
			if (tableData != null) {
				if (tableData["totalPage"] != 0)
					defualtLoad();
			}
		}
	});
}
function defualtLoad() {
	var oTbody = document.getElementById('fare-table').getElementsByTagName(
			"tbody")[0], data = tableData.items["1"], oPageWrape = document
			.getElementById("paging-wrape"), oUl = oPageWrape
			.getElementsByTagName("ul"), oA = oPageWrape
			.getElementsByTagName("a"), str = "";
	page = tableData["totalPage"], pageDom = document
			.getElementById('total-page');

	for (var i = 0; i < page; i++) {
		str += '<li><a href="javascript:;">' + (i + 1) + '</a></li>';
	}

	oUl[0].innerHTML = str;
	pageDom.innerHTML = "总共" + page + "页";

	num = data.length;
	sum = 0;
	var oldNum = document.getElementById("oldnum"), oldPrice = document
			.getElementById("oldprice");
	oldNum.value = num;
	for (var i = 0; i < data.length; i++) {
		oTbody.insertRow(i);
		oTbody.rows[i].insertCell(0);
		oTbody.rows[i].cells[0].innerHTML = '<a onclick="deleteCur(this)"><img src="img/transact_business/delete.png"></a>';
		for (var j = 0; j < 16; j++) {
			oTbody.rows[i].insertCell(j + 1);
			oTbody.rows[i].cells[j + 1].innerHTML = data[i][j];
			if (j == 5) {
				sum += parseInt(data[i][j], 10);
			}
		}
	}
	oldPrice.value = sum;
	addPagingEv();
}

function addPagingEv() {
	var oA = document.getElementById("paging-wrape").getElementsByTagName("a"), nextPage;
	for (var i = 0; i < oA.length; i++) {
		oA[i].onclick = function(event) {
			var ev = event || window.event, target = ev.target || ev.srcElement;
			nextPage = target.innerHTML;
			loadTable(nextPage);
			return false;
		};
	}

}

function loadTable(page) {

	var oTbody = document.getElementById('fare-table').getElementsByTagName(
			"tbody")[0], data = tableData.items[page];
	oTbody.innerHTML = "";
	for (var i = 0; i < data.length; i++) {

		oTbody.insertRow(i);
		oTbody.rows[i].insertCell(0);
		oTbody.rows[i].cells[0].innerHTML = '<a onclick="deleteCur(this)"><img src="img/transact_business/delete.png"></a>';
		for (var j = 0; j < 16; j++) {
			oTbody.rows[i].insertCell(j + 1);
			oTbody.rows[i].cells[j + 1].innerHTML = data[i][j];
		}
	}
}

function deleteCur(event) {
	var oTr = event.parentNode.parentNode;
	var tds = oTr.getElementsByTagName('td');
	for (var i = 0; i < tds.length; i++) {
		if (i == 6) {
			var devide = parseInt(tds[i].innerHTML, 10), oldPrice = document
					.getElementById("oldprice");
			oldPrice.value = parseInt(oldPrice.value, 10) - devide;
		} else if (i == 7) {
			var devide = parseInt(tds[i].innerHTML, 10), oldNum = document
					.getElementById("oldnum");
			oldNum.value = parseInt(oldNum.value, 10) - devide;
		}
	}
	var oTbody = oTr.parentNode;
	oTbody.removeChild(oTr);
}