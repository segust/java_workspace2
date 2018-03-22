// 增加产品 判断所写字段是否符合要求
function submitProductValidate() {
	
	var productprice = document.getElementById("productprice").value;
	var productcount = document.getElementById("productcount").value;
	var productname = document.getElementById("productname").value;
	var productunit = document.getElementById("productunit").value;
	var resultStr = "";
	var reg = new RegExp("^[0-9]*$");
	var flag = true;
	if(productname == "" && productunit == "") {
		resultStr += "整机名称或产品名称不能同时为空！";
		flag = false;
	}
	if (!reg.test(productprice)) {
		resultStr += "产品单价不是数字类型\n";
		flag = false;
	}
	if (!reg.test(productcount)) {
		resultStr += "产品数量不是数字类型\n";
		flag = false;
	}
	if (productprice <= 0 || productcount > 18446744073709551615) {
		if (productprice == 0)
			resultStr += "产品单价为0\n";
		else if (productprice < 0)
			resultStr += "产品单价为负\n";
		else if (productprice > 18446744073709551615)
			resultStr += "产品单价大于可存储长度\n";
		else if (productprice == "")
			resultStr += "产品单价为空\n";
		flag = false;
	}
	if (productcount <= 0 || productcount >= 2147483648) {
		if (productcount == 0)
			resultStr += "数量为0\n";
		else if (productcount < 0)
			resultStr += "数量为负\n";
		else if (productcount >= 2147483648)
			resultStr += "数量大于可存储长度\n";
		else if (productcount == "")
			resultStr += "数量为空\n";
		flag = false;
	}
	if (resultStr != "")
		//alert(resultStr);
		window.wxc.xcConfirm(resultStr,"warning");
	return flag;
}

var objs = new Array();
var index = 0;
//记录添加的产品信息
function addProduct() {
	if (submitProductValidate()) {
		var contractid = document.getElementById("contractid").innerHTML;
		var name = document.getElementById("productname").value;
		var model = document.getElementById("productmodel").value;
		var unit = document.getElementById("productunit").value;
		var measure = document.getElementById("measureunit").value;
		var price = document.getElementById("productprice").value;
		var num = document.getElementById("productcount").value;
		var dtime = document.getElementById("deliverytime").value;
		var manuf = document.getElementById("manufacturer").value;
		var keeper = document.getElementById("keeper").value;
		var sign = document.getElementById("signtime").innerHTML;
		var buyer = document.getElementById("buyer").innerHTML;
		//var dNo = document.getElementById("deviceNo").value;
		var dNo = '无';
		var pmnm = document.getElementById("PMNM").value;

		var obj = new createProduct(contractid, name, model, unit, measure,
				price, num, dtime, manuf, keeper, sign, buyer,dNo,pmnm);
		var flag ="1";
		/*if(objs.length == 0) {
		}else {
			for(var i=0;i<objs.length;i++) {
				if(objs[i].deviceNo == obj.deviceNo) {
					flag = "0";
					break;
				}
			}
		}*/
			if(flag == "1") {
				objs.push(obj);
				var list = document.getElementById("addprolist");
				var msg = document.getElementById("start-msg");
				var submit = document.getElementById("submit");
				msg.setAttribute("style", "display:none;");
				submit.setAttribute("style", "display:block;");
				var span = document.createElement("p");
				var namelist = obj.productName;
				if(obj.productName == "") {
					namelist = obj.productUnit;
				}
				span.setAttribute("style", "margin-top:10px;");
				span.innerHTML = "新增产品:产品名称&nbsp;<a href='javascript:void(0)' onclick='setPro(objs,"
						+ index
						+ ")'>"
						+ namelist
						+ "</a>&nbsp;&nbsp;<input class='search-btn' type='button' value='删除' onclick='delcur(this,"
						+ index + ")'/><br/>";
				list.appendChild(span);
				$("#ok").css("display","none");
				$("#error").css("display","none");
				index++;
			}else if(flag == "0") {
				//alert("输入了重复机号");
				window.wxc.xcConfirm("输入了重复机号","warning");
			}
		}
}

function setPro(objs, index) {

	var curObj = objs[index];
	var contractid = document.getElementById("cid-view");
	var name = document.getElementById("name-view");
	var model = document.getElementById("model-view");
	var unit = document.getElementById("unit-view");
	var measure = document.getElementById("measure-view");
	var price = document.getElementById("price-view");
	var num = document.getElementById("num-view");
	var dtime = document.getElementById("time-view");
	var manuf = document.getElementById("manu-view");
	var keeper = document.getElementById("keeper-view");
	var sign = document.getElementById("sign-view");
	var buyer = document.getElementById("buyer-view");
	var dNo = document.getElementById("device-view");
	var pmnm = document.getElementById("pmnm-view");
	$(contractid).html(curObj.contractId);
	$(name).html(curObj.productName);
	$(model).html(curObj.productModel);
	$(unit).html(curObj.productUnit);
	$(measure).html(curObj.measureUnit);
	$(price).html(curObj.price);
	$(num).html(curObj.productCount);
	$(dtime).html(curObj.deliveryTime);
	$(manuf).html(curObj.manufacturer);
	$(keeper).html(curObj.keeper);
	$(sign).html(curObj.signTime);
	$(buyer).html(curObj.buyer);
	$(dNo).html(curObj.deviceNo);
	$(pmnm).html(curObj.PMNM);
	$(document).ready(function() {
				$("#view-table").slideToggle("slow");
				$("#cover-table").slideToggle("slow");
			});
}
function hide() {
	$(document).ready(function() {
				$("#view-table").slideToggle("slow");
				$("#cover-table").slideToggle("slow");
			});
}

function delcur(event, ind) {
	var p = event.parentNode;
	var list = document.getElementById("addprolist");
	if (list.getElementsByTagName("p").length == 2) {
		var msg = document.getElementById("start-msg");
		var submit = document.getElementById("submit");
		msg.setAttribute("style", "display:block;");
		submit.setAttribute("style", "display:none;");
	}
	list.removeChild(p);
	index--;
	objs.splice(ind, 1);
}
function createProduct(contractid, name, model, unit, measure, price, num,
		dtime, manuf, keeper,  sign, buyer,dNo,pmnm) {
	this.contractId = contractid;
	this.productName = name;
	this.productModel = model;
	this.productUnit = unit;
	this.measureUnit = measure;
	this.price = price;
	this.productCount = num;
	this.signTime = sign;
	this.deliveryTime = dtime;
	this.manufacturer = manuf;
	this.keeper = keeper;
	// date类型的需要转换
	this.buyer = buyer;
	this.deviceNo = dNo;
	this.PMNM = pmnm;
	return this;
}


// 清空form选择
function clearForm(id) {
	var formObj = document.getElementById(id);
	if (formObj == undefined) {
		return;
	}
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].type == "text"
				&& formObj.elements[i].name != "contractid"
				&& formObj.elements[i].name != "signtime"
				&& formObj.elements[i].name != "buyer") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "password") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "radio") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "checkbox") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "select-one") {
			formObj.elements[i].options[0].selected = true;
		} else if (formObj.elements[i].type == "select-multiple") {
			for (var j = 0; j < formObj.elements[i].options.length; j++) {
				formObj.elements[i].options[j].selected = false;
			}
		} else if (formObj.elements[i].type == "file") {
			// formObj.elements[i].select();
			// document.selection.clear();
			// for IE, Opera, Safari, Chrome
			var file = formObj.elements[i];
			if (file.outerHTML) {
				file.outerHTML = file.outerHTML;
			} else {
				file.value = ""; // FF(包括3.5)
			}
		} else if (formObj.elements[i].type == "textarea") {
			formObj.elements[i].value = "";
		}
	}
}

function upObjs() {
	$.ajax({
		type : "post",
		url : "ProductHandleServlet?operate=addproduct",
		data : {
			'data' : arrayToJson(objs)
		},
		dataType : "text",
		success : function(data) {
			if (data == "1") {
				//alert("添加成功");
				window.wxc.xcConfirm("添加成功","success",{onOk:function(){ 
					window.location.href = "ProductHandleServlet?operate=gotoproduct&contractid="
						+ objs[0].contractId;
				}});
			} else if (data == "0") {
				window.wxc.xcConfirm("添加失败","warning");
			}

		},
		error : function() {
			window.wxc.xcConfirm("请求失败","warning");
		}
	});
}

function arrayToJson(o) {
	var r = [];
	if (typeof o == "string")
		return "\""
				+ o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n")
						.replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for (var i in o)
				r.push(i + ":" + arrayToJson(o[i]));
			if (!!document.all
					&& !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/
							.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
			}
			r = "{" + r.join() + "}";
		} else {
			for (var i = 0; i < o.length; i++) {
				r.push(arrayToJson(o[i]));
			}
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
}

// 判断输入的产品编号的数字串之差是否等于产品数量
function isEqualProductCount() {
	var productCode1 = document.getElementById("productCode1").value;
	var productCode2 = document.getElementById("productCode2").value;
	var num = document.getElementById("num").value;
	var reg = new RegExp(/\d+/);
	var startCode = reg.exec(productCode1);
	var endCode = reg.exec(productCode2);
	var testNum = endCode - startCode + 1;
	var str = "";
	var flag = true;
	if (num == "") {
		str += "输入数量为空";
		flag = false;
	}
	if (testNum < 0) {
		str += "开始编号大于结束编号";
		flag = false;
	} else if (testNum != num) {
		str += "编号范围和所输数量不一致";
		flag = false;
	}
	if (str != "")
		window.wxc.xcConfirm(str,"warning");
	return flag;
}

function checkDeviceNo(event){
	var val = event.value;
	if(val == "") {
		//alert("必须填写机号");
		window.wxc.xcConfirm("必须填写机号","warning");
	}else {
		$.ajax({
			type : "post",
			url : "ProductHandleServlet?operate=checkDeviceNo&dNo="+val,
			dataType : "text",
			success : function(data) {
				if (data == "1") {
					$("#ok").css("display","block");
				} else if (data == "0") {
					$("#deviceNo").css("color","red");
					var device = document.getElementById("deviceNo");
					device.focus();
					$("#error").css("display","block");
				}
			},
			error : function() {
				window.wxc.xcConfirm("请求失败","warning");
			}
		});
	}
}





// defaultLoad(1,$("#contract-table tbody")[0].rows[0].cells[0].innerHTML);



// // 给分页按钮添加点击事件
// function addPageEv(){

//     var oPagingWrap=$(".paging-wrape"),
//         reqPage,
//         nowPage,
//         totalPage=parseInt($(".paging-wrape .total span")[0].innerHTML),
//         oA=$(".paging-wrape")[0].getElementsByTagName("a"),
//         contractid=$("#contract-table tbody")[0].rows[0].cells[0].innerHTML;
       
//     for(var i=0;i<$(".pagination a").length;i++){
//         if($(".pagination a")[i].className=="active"){
//             nowPage=parseInt($(".pagination a")[i].innerHTML);
//         }

//     }

    
//     for(var i=0;i<oA.length;i++){
        
//         oA[i].onclick=function(event){
            
//             var ev=event||window.event;
//                 targ=ev.target||ev.srcElement;

//         	if(targ.getAttribute("aria-label")=="Previous"){
//         		if(nowPage>1){
//         			reqPage=nowPage-1;
//         		}else{
//         			reqPage=nowPage;
                
//         		}
                
//         	}else if(targ.getAttribute("aria-label")=="Next"){
//                 if(nowPage<totalPage){

//         			reqPage=nowPage+1;
        			
//         		}else{
//         			reqPage=nowPage;
//         		}
//         	}else if(targ.innerHTML=="确定"){
//         		var inputPage=parseInt($(".go-page .page_Input")[0].value);
     
//         		if(inputPage>totalPage||inputPage<1){
//                     reqPage=nowPage;
//         		}else{
//         			reqPage=inputPage;
//         		}
               
//         	}else{
//         		reqPage=this.innerHTML;
//         	}


        	
//         	getProData(reqPage,contractid);
//         };


//     }
// }

// // 获得数据
// function getProData(reqPage,contractid){
  
//     $.ajax({
//         type:'get',
//         url:'ProductHandleServlet?operate=getNextPage&curPageNum='+reqPage+"&contractid="+contractid,
//         dataType:'json',
//         success:function(data){
//             console.log(JSON.stringify(data));
            
//             loadPro(data);
//         }
//     });
// }




// // 异步加载表格
// function loadPro(tableData){
//     var oTbody=$("#contract-table tbody")[0],
//         nowPage=tableData.nowPage,
//         totalPage=tableData.totalPage;
//         oTbody.innerHTML="";
//     for(var i=0;i<tableData.items.length;i++){
//         oTbody.insertRow(i);
//         for(var j=0;j<11;j++){
//             oTbody.rows[i].insertCell(j);
//             oTbody.rows[i].cells[j].innerHTML=tableData.items[i][j];
//         }
    
//     }

//     for(var i=0;i<$(".pagination a").length;i++){
//         $(".pagination a").eq(i).removeClass("active");
//     }

//     $(".pagination a").eq(nowPage).addClass("active");

// }


// function defaultLoad(reqPage,contractid){
//     var str='<li><a href="javascript:;" aria-label="Previous">&laquo;</a></li>';
//         str+='<li><a class="active" href="javascript:;">'+1+'</a></li>';
//     $.ajax({
//         type:'get',
//         url:'ProductHandleServlet?operate=getNextPage&curPageNum='+reqPage+"&contractid="+contractid,
//         dataType:'json',
//         success:function(data){
//             console.log(JSON.stringify(data));
//             $(".paging-wrape .total span")[0].innerHTML=data.totalPage;
//             for(var i=0;i<data.totalPage-1;i++){
//                 str+='<li><a href="javascript:;">'+(i+2)+'</a></li>';
//             }
            
//             str+='<li><a href="javascript:;" aria-label="Next">&raquo;</a></li>';
//             $(".paging-wrape .pagination")[0].innerHTML=str;
//             addPageEv();
//         }

//     });


    
// }