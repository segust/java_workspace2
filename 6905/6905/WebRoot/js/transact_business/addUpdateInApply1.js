var inApplyPage={};
inApplyPage.util={};
inApplyPage.util.data={};
inApplyPage.util.data.sPeInfo=[];
// inApplyPage.inf={};
inApplyPage.util.executeFn=function (){
	inApplyPage.util.addNumEvent();
	inApplyPage.util.addSubmit();
	// inApplyPage.util.initialPageBtn();
}

// 给具体机号信息分页按钮添加事件做初始化
inApplyPage.util.addNumEvent=function(){
	var inputList=document.getElementById('content').getElementsByTagName('ul')[0].getElementsByTagName('input');
	for(var i=0;i<inputList.length;i++){
		inputList[i].onblur=function(){
			inApplyPage.util.loadPageBtn();
			inApplyPage.util.setPubInfo();
		// console.log(inApplyPage.util.data);
	}
}	

}

// 加载分页按钮
inApplyPage.util.loadPageBtn=function (){
	var oTableWrap=document.getElementById('table-wrap'),
	oTableWrap_ul=oTableWrap.getElementsByTagName('ul'),
	string="",
	num=document.getElementById("content-num").value;

	if("table" in inApplyPage.util){
	}else{
		oTableClone=oTableWrap_ul[0].getElementsByTagName('table')[0].cloneNode(true);
		inApplyPage.util.table=oTableClone;
	}

	for(var i=1;i<=num;i++){
		string+="<li><a href=\"#\">第"+i+"个</a></li>";
	}
	oTableWrap_ul[0].innerHTML=string;
    // alert(oTableClone);
    var oLi=oTableWrap_ul[0].getElementsByTagName('li');
    inApplyPage.util.addTable(oLi);
    inApplyPage.util.initialPageBtn(num);

}

//向每个分页按钮中加载表格
inApplyPage.util.addTable=function(oLi){
	for(var i=0;i<oLi.length;i++){
		oLi[i].appendChild(inApplyPage.util.table.cloneNode(true));//每次都要克隆
	}
}

// 初始化分页按钮
inApplyPage.util.initialPageBtn=function (num){
	var oTableWrap=document.getElementById('table-wrap'),
	oTableWrap_ul=oTableWrap.getElementsByTagName('ul'),
	oPageBtn=oTableWrap_ul[0].getElementsByTagName('a');
	for(var i=0;i<oPageBtn.length;i++){
		oPageBtn[i].onclick=function (event){
			var eve=event||window.event;
			// alert(eve.preventDefault);
			if(eve.preventDefault){
				eve.preventDefault();
			}else{
				eve.returnValue=false;
			}
			inApplyPage.util.switchPage(eve,num);
		}	
	}
}

// 分页切换
inApplyPage.util.switchPage=function (eve,num){
	var targ=eve.target||eve.srcElement,
	targParent=targ.parentNode,
	oUl=targParent.parentNode,
	oLi=oUl.getElementsByTagName('li'),
	oLi_table=oUl.getElementsByTagName('table');
	for(var i=0;i<num;i++){
		if(oLi_table[i].className=="active"){
			oLi_table[i].className="no-active";
			oLi_table[i].getElementsByTagName('input')[0].className="no-pmnm";
			oLi_table[i].parentNode.className="no-li-bgcolor";
			oLi_table[i].parentNode.getElementsByTagName('a')[0].className="no-font-color";
			inApplyPage.util.setSpeInfo(i,oLi_table[i]);
		}
		// break;加了break为什么不行
	}
	targParent.getElementsByTagName('table')[0].getElementsByTagName('input')[0].className="pmnm";
	targParent.getElementsByTagName('table')[0].getElementsByTagName('input')[1].className="unit";
	targParent.getElementsByTagName('table')[0].getElementsByTagName('input')[2].className="pname";
	targParent.getElementsByTagName('table')[0].getElementsByTagName('input')[3].className="deviceNo";
	targParent.getElementsByTagName('table')[0].className="active";
	targParent.className="li-bgcolor";
	targ.className="font-color";

	$(function() {
		var sugestions = new Array();
		var names = new Array();
		var pmnm = document.getElementById("pmnm-value").value;
		var pname = document.getElementById("pname-value").value;
		sugestions = pmnm.replace('[','').replace(']','').split(',');
		names = pname.replace('[','').replace(']','').split(',');
		// alert(sugestions);
		$('.pmnm').combobox(sugestions);
		/*$('.unit').combobox(['1','2']);*/
		$('.pname').combobox(names);
		/*$('.deviceNo').combobox(['1','2']);*/
		});
}

// 获取公共信息
inApplyPage.util.setPubInfo=function (){
	var oPubInfoLi=document.getElementById("content").getElementsByTagName('ul')[0].getElementsByTagName('li'),
	oPubInfoList=document.getElementById("content").getElementsByTagName('ul')[0].getElementsByTagName('input'),
	
	pubInfoContainer={};
	pubInfoContainer.means=oPubInfoLi[0].getElementsByTagName('label')[1].innerHTML;
	pubInfoContainer.batch=oPubInfoList[0].value;
	pubInfoContainer.oldNum=oPubInfoList[1].value;
	pubInfoContainer.num=oPubInfoList[2].value;
	pubInfoContainer.execTime=oPubInfoLi[4].getElementsByTagName('label')[1].innerHTML;

	inApplyPage.util.data.pubInfo=pubInfoContainer;
}

inApplyPage.util.setSpeInfo=function(i,oLi_table){
	var oSpeInfoLiList=oLi_table.getElementsByTagName('input'),
	oSpeInfoSelectList=oLi_table.getElementsByTagName('select');
	sPeInfoContainer={};

	sPeInfoContainer.pmnm=oSpeInfoLiList[0].value;
	sPeInfoContainer.unit=oSpeInfoLiList[1].value;
	sPeInfoContainer.wholename=oSpeInfoLiList[2].value;
	sPeInfoContainer.dNo=oSpeInfoLiList[3].value;
	sPeInfoContainer.oldNo=oSpeInfoLiList[4].value;
	sPeInfoContainer.price=oSpeInfoLiList[5].value;
	sPeInfoContainer.oldPrice=oSpeInfoLiList[6].value;
	sPeInfoContainer.productType=oSpeInfoLiList[7].value;
	sPeInfoContainer.oldType=oSpeInfoLiList[8].value;
	sPeInfoContainer.measure=oSpeInfoLiList[9].value;
	sPeInfoContainer.manuf=oSpeInfoLiList[10].value;
	sPeInfoContainer.keeper=oSpeInfoLiList[11].value;
	sPeInfoContainer.location=oSpeInfoLiList[12].value;
	sPeInfoContainer.storageTime=oSpeInfoLiList[13].value;
	sPeInfoContainer.maintain=oSpeInfoSelectList[0].value;
	sPeInfoContainer.makeTime=oSpeInfoLiList[14].value;
	sPeInfoContainer.remark=oSpeInfoLiList[15].value;

	inApplyPage.util.data.sPeInfo[i]=sPeInfoContainer;
	console.log(inApplyPage.util.data);
}

//消除提交表格信息不完全bug
inApplyPage.util.checkTable=function(){
	var oTableWrap=document.getElementById('table-wrap').getElementsByTagName('table'),
	speInfoContainer={};
	for(var i=0;i<oTableWrap.length;i++){
		if(oTableWrap[i].className=="active"){
			var oTable_input=oTableWrap[i].getElementsByTagName('input'),
			oTable_select=oTableWrap[i].getElementsByTagName('select');

//			speInfoContainer.wholename=oTable_input[0].value;
//			speInfoContainer.unit=oTable_select[0].value;
//			speInfoContainer.dNo=oTable_input[1].value;
//			speInfoContainer.pmnm=oTable_input[2].value;
//			speInfoContainer.price=oTable_input[3].value;
//			speInfoContainer.productType=oTable_select[1].value;
//			speInfoContainer.oldType=oTable_select[2].value;
//			speInfoContainer.oldPrice=oTable_input[4].value;
//			speInfoContainer.measure=oTable_input[5].value;
//			speInfoContainer.manuf=oTable_input[6].value;
//			speInfoContainer.keeper=oTable_input[7].value;
//			speInfoContainer.location=oTable_input[8].value;
//			speInfoContainer.storageTime=oTable_input[9].value;
//
//			speInfoContainer.maintain=oTable_select[3].value;
//			speInfoContainer.makeTime=oTable_input[10].value;
//			speInfoContainer.remark=oTable_input[11].value;
			//start
			speInfoContainer.pmnm=oTable_input[0].value;
			speInfoContainer.unit=oTable_input[1].value;
			speInfoContainer.wholename=oTable_input[2].value;
			speInfoContainer.dNo=oTable_input[3].value;
			speInfoContainer.oldNo=oTable_input[4].value;
			speInfoContainer.price=oTable_input[5].value;
			speInfoContainer.oldPrice=oTable_input[6].value;
			speInfoContainer.productType=oTable_input[7].value;
			speInfoContainer.oldType=oTable_input[8].value;
			speInfoContainer.measure=oTable_input[9].value;
			speInfoContainer.manuf=oTable_input[10].value;
			speInfoContainer.keeper=oTable_input[11].value;
			speInfoContainer.location=oTable_input[12].value;
			speInfoContainer.storageTime=oTable_input[13].value;
			speInfoContainer.maintain=oTable_select[0].value;
			speInfoContainer.makeTime=oTable_input[14].value;
			speInfoContainer.remark=oTable_input[15].value;
			inApplyPage.util.data.sPeInfo[i]=speInfoContainer;
			console.log(inApplyPage.util.data);
		}
	}

}
// 给提交按钮添加事件
inApplyPage.util.addSubmit=function(){
	
	var oInput=document.getElementById('submit-form').getElementsByTagName('input');
	//alert(oInput[0].parentNode);
	oInput[0].parentNode.onsubmit=function(){
		inApplyPage.util.checkTable();       //之前此句错误，导致return false 没有执行到；
		jsonText=JSON.stringify(inApplyPage.util.data);
		oInput[0].value=jsonText;
		console.log(oInput[0].value);
		//alert(oInput[0].value);
			// return false;
			if(inApplyPage.util.data.sPeInfo.length==0){
			return false;
			}
		}
	}

function  findByNo(event) {
	var deviceNo = event.value;
	$.ajax({
		type : "post",
		url : "ProductHandleServlet?operate=findByNo&deviceNo="+deviceNo+"&isIn="+1,
		dataType : "text",
		success : function(data) {
			if(data != "") {
				var product = eval("("+data+")");
				if(product.price != undefined) {
					var price = document.getElementById("oldprice");
					price.setAttribute("value", product.price);
				}
				if(product.productModel != undefined) {
					var model = document.getElementById("oldmodel");
					model.setAttribute("value", product.productModel);
				}
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}