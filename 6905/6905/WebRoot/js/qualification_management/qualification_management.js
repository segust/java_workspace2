// 根据选择的文件自动获取文件名在input text中显示
function showTitle(thisFile) {
	var qualifyTitle = document.getElementById("qualifyTitle");
	// 获取文件名并显示
	var t1 = thisFile.value.lastIndexOf("\\");
	var t2 = thisFile.value.lastIndexOf(".");
	if (t1 < t2 && t1 < thisFile.value.length) {
		qualifyTitle.value = thisFile.value.substring(t1 + 1, t2);
	}
}

// 点击资质文件的下载按钮的函数
function downloadQualify(qualifyId) {
	window.wxc.xcConfirm("确认下载当前资质文件？","confirm",{onOk:function(){ 
		window.location.href = "QualifyServlet?operate=downloadQualify&qualifyId="
			+ qualifyId;
	}});
}

//复选框全选，全不选
function chooseAll(){
	var oCheckboxHead,oCheckboxBody,oFlag;
	oCheckboxHead=document.getElementById("checkboxhead");
	oCheckboxBody=document.getElementsByName("checkboxbody");
	if(oCheckboxHead.checked)
		oFlag=true;
	else
		oFlag=false;
	for (var i = 0; i < oCheckboxBody.length; i++) {
		oCheckboxBody[i].checked=oFlag;
	}
}

//导出选择的资质文件
function exportChooseQualify(){
	turnToPageWithCheckedbox('QualifyServlet?operate=exportQualify','checkboxbody');
}


