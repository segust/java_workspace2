//页面跳转将已选择的id和当前页未选的id传到后台
function turnToPageWithCheckedbox(url,checkboxBodyName){
	var oCheckedId,oUnCheckedId,oCheckboxBody,oCheckedIdInput;
	oCheckedIdInput=document.getElementById("checkedIdStr");
	oUnCheckedIdInput=document.getElementById("unCheckedIdStr");
	//获取已选择的id
	oCheckedId=$.trim(oCheckedIdInput.value);
	oUnCheckedId=$.trim(oUnCheckedIdInput.value);

	if(oCheckedId!=""&&oCheckedId!=null)
		oCheckedId+=",";
	
	//获取当前页选择的id
	oCheckboxBody=document.getElementsByName(checkboxBodyName);
	for (var i = 0; i < oCheckboxBody.length; i++) {
		if(oCheckboxBody[i].checked){
			oCheckedId+=oCheckboxBody[i].value;
			oCheckedId+=",";
		}
		else{
			oUnCheckedId+=oCheckboxBody[i].value;
			oUnCheckedId+=",";
		}
	}
	oCheckedIdInput.value=oCheckedId;
	oUnCheckedIdInput.value=oUnCheckedId;
	
	//alert(oCheckedId);
	if(oCheckedId==""){
		alert("请勾选导出内容再进行导出");
		return;
	}
	
	document.forms.checkedIdForm.action=url;
	document.forms.checkedIdForm.submit();
}
