function chooseAll() {
	var oCheckboxLeader=document.getElementById("checkbox_maintainhead");
	var oCheckbox = document.getElementsByName("checkbox_maintaininfo");
	if (oCheckboxLeader.checked == true) {
		for (var i = 0; i < oCheckbox.length; i++) {
			oCheckbox[i].checked = true;
		}
	} else {
		for (var i = 0; i < oCheckbox.length; i++) {
			oCheckbox[i].checked = false;
		}
	}
}

function arrayToJson(o) {  
    var r = [];  
    if (typeof o == "string") return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";  
    if (typeof o == "object") {  
    if (!o.sort) {  
    for (var i in o)  
    r.push(i + ":" + arrayToJson(o[i]));  
    if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {  
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



function OutMaintainInfo(){
	var oCurrentCheckBox = document.getElementsByName("checkbox_maintaininfo");
	// 先获取选中了多少的checkbox
	var oArray = new Array();
	var m = 0;
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			oArray[m] = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode
					.getElementsByTagName('td');
			var val = oCurrentTd[0].getElementsByTagName("input")[1].value;
		
			oArray[m]= val;
			m++;
			
		}
	}
	// 创建二维数组将选中的信息保存

	jQuery.ajax({
		type : "post",
		url : "Maintain?operateType=exportLog",
		data : {
			'logId' : arrayToJson(oArray)
		},
		dataType : "text",
		success : function(data) {
			
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "Maintain?operateType=download&path="+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}

