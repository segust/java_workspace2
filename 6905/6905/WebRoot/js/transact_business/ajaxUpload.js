// 新增合同 判断所写字段是否符合要求
function submitContractValidate() {
	var contractid = document.getElementById("contractid").value, signdate = document
			.getElementById("signdate").value, totalnumber = document
			.getElementById("totalnumber").value, contractprice = document
			.getElementById("contractprice").value, result = "", flag = true;
	if (contractid == "") {
		result += "合同编号为空\n";
		flag = false;
	}
	if (signdate == "") {
		result += "签订日期为空\n";
		flag = false;
	}
	if (isNaN(totalnumber)) {
		result += "订购数量不是数字类型\n";
		flag = false;
	}
	if (totalnumber <= 0 || totalnumber >= 2147483648) {
		if (totalnumber == "")
			result += "订购数量为空\n";
		else if (totalnumber < 0)
			result += "订购数量为负\n";
		else if (totalnumber >= 2147483648)
			result += "订购数量大于可存储长度\n";
		else if (totalnumber == 0)
			result += "订购数量为0\n";
		flag = false;
	}
	if (isNaN(contractprice)) {
		result += "合同金额不是数字类型\n";
		flag = false;
	}
	if (contractprice <= 0 || contractprice > 18446744073709551615) {
		if (contractprice == "")
			result += "合同金额为空\n";
		else if (contractprice < 0)
			result += "合同金额为负\n";
		else if (contractprice > 18446744073709551615)
			result += "合同金额大于可存储长度\n";
		else if (contractprice == 0)
			result += "合同金额为0\n";
		flag = false;
	}
	if (!flag)
		window.wxc.xcConfirm(result,"info");
	return flag;
}

function uploadForm() {
	if (submitContractValidate()) {
		var contractid = document.getElementById("contractid").value;
		var totalnumber = document.getElementById("totalnumber").value;
		var contractprice = document.getElementById("contractprice").value;
		var jds = document.getElementById("jds").value;
		var signdate = document.getElementById("signdate").value;
		var buyer = document.getElementById("buyer").value;

		$.ajaxFileUpload({
					url : 'ContractHandleServlet?operate=ajaxUpload&contractid='
							+ contractid
							+ '&totalnumber='
							+ totalnumber
							+ '&contractprice='
							+ contractprice
							+ '&jds='
							+ jds
							+ '&signdate=' + signdate + '&buyer=' + buyer,
					secureuri : false,
					fileElementId : 'path',
					dataType : 'text',
					success : function(data, status) {
						if (data == "1") {
							window.wxc
							.xcConfirm(
									"新增合同成功！",
									"success",
									{
										onOk : function() {
											window.location.href = "ContractHandleServlet?operate=querycontract";
										}
									});
						} else if (data == "0") {
							window.wxc.xcConfirm("新增合同失败","info");
						}
					},
					error : function(data, status, e) {
						window.wxc.xcConfirm(e,"error");
					}
				});
	}
}

function updateContract() {
	if (submitContractValidate()) {
		var contractid = document.getElementById("contractid").value, totalnumber = document
				.getElementById("totalnumber").value, contractprice = document
				.getElementById("contractprice").value, jds = document
				.getElementById("jds").value, signdate = document
				.getElementById("signdate").value, buyer = document
				.getElementById("buyer").value;

		$.ajax({
					url : 'ContractHandleServlet',
					type : 'POST',
					data : {
						operate : 'updateform',
						contractid : contractid,
						totalnumber : totalnumber,
						contractprice : contractprice,
						jds : jds,
						signdate : signdate,
						buyer : buyer
					},
					dataType : 'text',
					success : function(data, status) {
						if (data == "1") {
							window.wxc
							.xcConfirm(
									"修改合同成功！",
									"success",
									{
										onOk : function() {
											window.location.href = "ContractHandleServlet?operate=querycontract";
										}
									});
						} else if (data == "0") {
							window.wxc.xcConfirm("修改合同失败","info");
						}
					},
					error : function(data, status, e) {
						window.wxc.xcConfirm(e,"error");
					}
				});
	}
}

/*function uploadOutList() {
	var msg = document.getElementById("msg");
	msg.innerHTML = "正在导入...";
	$.ajaxFileUpload({
		url : 'OutWarehouseServlet?operate=importOutlist',
		secureuri : false,
		fileElementId : 'fileToUpload',
		dataType : 'text',
		success : function(data, status) {
			if (data == "1") {
				msg.innerHTML = "导入成功";
			} else if (data == "0") {
				msg.innerHTML = "导入失败";
			}
		},
		error : function(data, status, e) {
			alert(e);
		}
	});
}
*/
/*function uploadJDJOutList() {
	var msg = document.getElementById("msg");
	msg.innerHTML = "正在导入...";
	$.ajaxFileUpload({
		url : 'OutWarehouseServlet?operate=importOutlistFile',
		secureuri : false,
		fileElementId : 'fileToUpload',
		dataType : 'text',
		success : function(data, status) {
			if (data == "1") {
				msg.innerHTML = "导入成功";
			} else if (data == "0") {
				msg.innerHTML = "导入失败";
			}
		},
		error : function(data, status, e) {
			alert(e);
		}
	});
}
*/
/*function uploadjdjBackup(isUp) {
	var msg = document.getElementById("msg");
	var applyType = document.getElementById("applyType").value;
	msg.innerHTML = "正在导入...";
	var path = "";
	if (isUp == "1") {
		path = "InWarehouseServlet?operate=importBackupUpFiles&applyType="
				+ applyType;
	} else if (isUp == "0") {
		path = "InWarehouseServlet?operate=importBackupDownFiles&applyType="
				+ applyTpye;
	}
	$.ajaxFileUpload({
		url : path,
		secureuri : false,
		fileElementId : 'fileToUpload',
		dataType : 'text',
		success : function(data, status) {
			if (data == "1") {
				msg.innerHTML = "导入成功";
			} else if (data == "0") {
				msg.innerHTML = "导入失败";
			}
		},
		error : function(data, status, e) {
			alert(e);
		}
	});
}*/

function iscIdExist(event) {
	var contractid = event.value;
	$.ajax({
		url : 'ContractHandleServlet',
		type : 'POST',
		data : {
			operate : 'isExistcId',
			contractid : contractid
		},
		dataType : 'text',
		success : function(data, status) {
			if (data == "1") {
				window.wxc.xcConfirm("合同编号已存在，请勿重复添加！","info");
				event.value="";
				//event.focus();
				//event.style="backgroud-color:red;";
			}
		},
		error : function(data, status, e) {
			window.wxc.xcConfirm("发生错误，请及时联系管理员！","warning");
		}
	});
}

//验证上传后文件是否是pdf,改到addattachment.jsp上传
//function validateContractFile(targ){
//	var oFileName=targ.value,
//		oStartIndex=oFileName.lastIndexOf(".")+1,
//		oEndIndex=oFileName.length,
//		oFileType=oFileName.substring(oStartIndex,oEndIndex);
//	if(oFileType!="pdf"){
//		targ.value="";
//		alert("请上传pdf文件");
//	}
//	else if(document.getElementById("upfile")!=null){
//		var oUpfile=document.getElementById("upfile");
//		oUpfile.value=targ.value;
//	}
//}
