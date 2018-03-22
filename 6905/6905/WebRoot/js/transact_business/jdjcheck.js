function allcheck(type,check) {
	var checkIndex = 7;
	var oUnitIndex = 6;
	if(type =="GXCK" || type=="LHCK") {
		checkIndex = 8;
		oUnitIndex = 7;
	}
	var table = document.getElementById("mytable");
	var trs = table.getElementsByTagName("tr");
	var ownedUnit = "";
	var array = new Array();
	for(var i=1;i<trs.length;i++) {
		var o = new Array();
		var tds = trs[i].getElementsByTagName("td");
		for(var j=0;j<tds.length;j++) {
			if(j==0){
				var id = tds[j].getElementsByTagName("input")[1].value;
				ownedUnit = tds[j].getElementsByTagName("input")[2].value;
				o.push(id);
			}else if(j == oUnitIndex) {
				ownedUnit = tds[j].innerHTML;
			}else if(j==checkIndex){
				o.push(check);
				tds[j].innerHTML = check;
			}
		}
		array.push(o);
	}
	synDatabase(type,check,array,ownedUnit,1);
}

function checkPass(type,check) {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	var flag = false;
	var array = new Array();
	var ownedUnit = "";
	var checkIndex = 7;
	var oUnitIndex = 6;
	if(type =="GXCK" || type=="LHCK") {
		checkIndex = 8;
		oUnitIndex = 7;
	}
	// 先获取选中了多少的checkbox
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			flag = true;
			var o = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode.getElementsByTagName("td");
			for (var j = 0; j < oCurrentTd.length; j++) {
				if(j==0){
					var id = oCurrentTd[j].getElementsByTagName("input")[1].value;
					ownedUnit = oCurrentTd[j].getElementsByTagName("input")[2].value;
					o.push(id);
				}else if(j == oUnitIndex) {
					ownedUnit = oCurrentTd[j].innerHTML;
				}else if (j == checkIndex) {
					oCurrentTd[j].innerHTML=check;
					o.push(check);
				}
			}
			array.push(o);
		}
	}
	if(flag){
		synDatabase(type,check,array,ownedUnit,0);
	}else {
		var msg = document.getElementById("checkmsg");
		msg.innerHTML="您还没有选中数据！";
		msg.setAttribute("style", "color: red;width: 200px;margin-left: auto;margin-right: auto;");
	}
}
//获取前端数据并传到后台
//isOneClick同军代室
function synDatabase(type,check,array,ownedUnit,isOneClick) {
	jQuery.ajax({
		type : "post",
		url : "InWarehouseServlet?operate=checkFileInJDJ",
		data : {
			'data' : arrayToJson(array),
			'applyType':type,
			'ownedUnit':ownedUnit
		},
		dataType : "text",
		success : function(data) {
			if (data == "保存成功！") {
				var msg = document.getElementById("checkmsg");
				if(isOneClick == 1) {
					$(msg).html("一键" + check + "完成");
				}else if(isOneClick ==0) {
					$(msg).html(check + "完成");
				}
				msg.setAttribute("style", "color: blue;width: 200px;margin-left: auto;margin-right: auto;");
				var username = document.getElementsByName("user");
				for(var i=0;i<username.length;i++) {
					username[i].setAttribute("style","display:block"); 
				}
				var out = document.getElementById("outTable");
				out.removeAttribute("disabled");
				out.setAttribute("style", "width: 100px;");
			}else {
				var msg = document.getElementById("checkmsg");
				if(isOneClick == 1) {
					$(msg).html("一键" + check + "未完成");
				}else if(isOneClick ==0) {
					$(msg).html(check + "未完成");
				}
				msg.setAttribute("style", "color: red;width: 200px;margin-left: auto;margin-right: auto;");
			}

		},
		error : function() {
			alert("请求失败");
		}
	});
}

function downloadAll(type) {
	var oCurrentCheckBox = document.getElementsByName("mycheck");
	var flag = false;
	var ownedUnit="";
	var array = new Array();
	// 先获取选中了多少的checkbox
	for (var i = 0; i < oCurrentCheckBox.length; i++) {
		if (oCurrentCheckBox[i].checked) {
			flag = true;
			var o = new Array();
			var oCurrentTd = oCurrentCheckBox[i].parentNode.parentNode.getElementsByTagName("td");
			for (var j = 0; j < 7; j++) {
				if(j==0){
					var id = oCurrentTd[j].getElementsByTagName("input")[1].value;
					o.push(id);
				}else if(j==6) {
					ownedUnit = oCurrentTd[j].innerHTML;
				}
			}
			array.push(o);
		}
	}
	var exportType = "none";
	
	if(type == "RK"){
		exportType = "newIn";
	}else if(type == "LHRK") {
		exportType = "circleIn";
	}else if(type == "LHCK") {
		exportType = "circleOut";
	}else if(type == "GXRK") {
		exportType = "renewIn";
	}else if(type == "GXCK") {
		exportType = "renewOut";
	}
	if(flag){
		jQuery.ajax({
			type : "post",
			url : "InWarehouseServlet?operate=exportSingleForm",
			data : {
				'outData' : arrayToJson(array),
				'exportType':exportType,
				'ownedUnit':ownedUnit
			},
			dataType : "text",
			success : function(data) {
				if(data != "") {
					window.location.href = "InWarehouseServlet?operate=download&absolutePath="
						+ data+"&exportType="+exportType;
					var msg = document.getElementById("checkmsg");
					msg.innerHTML="导出成功！";
					msg.setAttribute("style", "color: blue;width: 200px;margin-left: auto;margin-right: auto;");
				}
			},
			error : function() {
			alert("请求失败");
			}
		});
	}else {
		var msg = document.getElementById("checkmsg");
		msg.innerHTML="您还没有选中数据！";
		msg.setAttribute("style", "color: red;width: 200px;margin-left: auto;margin-right: auto;");
	}
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
function getAll() {
	var checks = document.getElementsByName("mycheck");
	for (var i = 0; i < checks.length; i++) {
		if (checks[i].checked == false) {
			checks[i].checked = true;
		} else if (checks[i].checked == true) {
			checks[i].checked = false;
		}
	}
}
function synUpdateDatabase(type) {
	var table = document.getElementById("mytable");
	var trs = table.getElementsByTagName("tr");
	var array = new Array();
	for(var i=1;i<trs.length;i++) {
		var o = new Array();
		var tds = trs[i].getElementsByTagName("td");
		for(var j=0;j<tds.length;j++) {
			if(j==0){
				var id = tds[j].getElementsByTagName("input")[1].value;
				o.push(id);
			}else if(j==19){
				var user = tds[j].getElementsByTagName("span")[0].innerHTML;
				o.push(user);
			}
			else {
				o.push(tds[j].innerHTML);
			}
		}
		array.push(o);
	}
	jQuery.ajax({
		type : "post",
		url : "InWarehouseServlet?operate=checkFileSynchroInJDS&applyType="+type,
		data : {
			'data' : arrayToJson(array)
		},
		dataType : "text",
		success : function(data) {
			var msg = document.getElementById("checkmsg");
			if (data == "保存成功！") {
				msg.innerHTML="同步数据库成功，请导出！";
				msg.setAttribute("style", "color: blue;width: 200px;margin-left: auto;margin-right: auto;");
				var out = document.getElementById("outTable");
				out.removeAttribute("disabled");
				out.setAttribute("style", "width: 100px;");
			}

		},
		error : function() {
			alert("请求失败");
		}
	});
}

function detail_Apply(event){
	var tr = event.parentNode.parentNode;
	var tds = tr.getElementsByTagName("td");
	for(var i=0;i<3;i++){
    if(i==0){
    	var  id=tds[i].getElementsByTagName("input")[1].value;
    }
    if(i==2){
    	var  type=tds[i].innerHTML;
    }
	}
  //把获取到的数据传给后台
	if(type=="新入库"||type=="轮换入库"||type=="更新入库"){
	jQuery.ajax({
		type:"post",
		url:"ApplyHandleServlet?operate=searchByInId&inId="+id,
	   success:function(data){
				if(type=="新入库"){
					createnewIndetail(data);
				}else if(type=="轮换入库"){
					createborrowIndetail(data);
				}else if(type=="更新入库"){
					createupdatedetail(data);
				}
				}
	});	
	}else if(type=="轮换出库"||type=="更新出库"){
		jQuery.ajax({
			type:"post",
			url:"ApplyHandleServlet?operate=searchByOutId&outId="+id,
		   success:function(data){
					if(type=="轮换出库"){
						createborrowOutdetail(data);
					}else if(type=="更新出库"){
						createupdatedetail(data);
					}
					}
		});	
	}
	//把获取到的数据传给后台
	//jQuery.ajax({
	//});	
	if(type=="新入库"){
		$(document).ready(function(){
			$("#whole").show();
			$("#new_In").show();
			});
		$(document).ready(function(){
			  $("#sure_new_In").click(function(){
			  $("#whole").hide();
			  $("#new_In").hide();
			  $("#new_Table").html("");
			  });
			  });
	}
	else if (type=="轮换入库"){
		$(document).ready(function(){
			$("#whole").show();
			$("#borrow_In").show();
			});
		$(document).ready(function(){
			  $("#sure_borrow_In").click(function(){
			  $("#whole").hide();
			  $("#borrow_In").hide();
			  $("#borrowin_Table").html("");
			  });
			  });
	}
    else if (type=="轮换出库"){
    	$(document).ready(function(){
			$("#whole").show();
			$("#borrow_Out").show();
			});
		$(document).ready(function(){
			  $("#sure_borrow_Out").click(function(){
			  $("#whole").hide();
			  $("#borrow_Out").hide();
			  $("#borrowout_Table").html("");
			  });
			  });
	}
    else if (type=="更新入库"){
    	$(document).ready(function(){
			$("#whole").show();
			$("#update").show();
			});
		$(document).ready(function(){
			  $("#sure_update").click(function(){
			  $("#whole").hide();
			  $("#update").hide();
			  $("#update_table").html("");
			  });
			  });
	}
   else if (type=="更新出库"){
	   $(document).ready(function(){
			$("#whole").show();
			$("#update").show();
			});
		$(document).ready(function(){
			  $("#sure_update").click(function(){
			  $("#whole").hide();
			  $("#update").hide();
			  $("#update_table").html("");
			  });
			  });
}
}
function createnewIndetail(provinces){
	 var t=document.getElementById("new_Table");     //获取Table
	//通过eval() 函数可以将JSON字符串转化为对象 
	 var trs=  t.getElementsByTagName("tr");//获取行 
	 //for(var j=0;j<trs.length;j++){
		 //t.deleteRow();
	 //}
	 var detail = eval(provinces); 
	    if(detail.length>0){ 
	    	
	        for (var i = 0; i < detail.length; i++) { 
	            //tr 
	                    //if(i%11==0){ 
	                        var r = t.insertRow(t.rows.length);//创建新的行 
	                     //} 
	                    //td  
	          for(var j=0;j<18;j++){
	        	  var c = r.insertCell(); //创建新的列 
	        	  if(j==0){
		            c.innerHTML = detail[i].contractid; 
		            }
	        	  if(j==1){
			            c.innerHTML = detail[i].productname; 
			            }
	        	  if(j==2){
			            c.innerHTML = detail[i].productmodel; 
			            }
	        	  if(j==3){
			            c.innerHTML = detail[i].deviceNo; 
			            }
	        	  if(j==4){
			            c.innerHTML = detail[i].wholeName; 
			            }
	        	  if(j==5){
	        		  c.innerHTML = detail[i].productunit; 
			            }
	        	  if(j==6){
			            c.innerHTML = detail[i].measureunit; 
			            }
	        	  if(j==7){
			            c.innerHTML = detail[i].productprice; 
			            }
	        	  if(j==8){
			            c.innerHTML = detail[i].pmnm; 
			            }
	        	  if(j==9){
			            c.innerHTML = detail[i].manuf; 
			            }
	        	  if(j==10){
			            c.innerHTML = detail[i].keeper; 
			            }
	        	  if(j==11){
			            c.innerHTML = detail[i].location; 
			            }
	        	  if(j==12){
			            c.innerHTML = detail[i].maintainCycle; 
			            }
	        	  if(j==13){
			            c.innerHTML = detail[i].produced; 
			            }
	        	  if(j==14){
			            c.innerHTML = detail[i].productcode; 
			            }
	        	  if(j==15){
			            c.innerHTML = detail[i].status; 
			            }
	        	  if(j==16){
			            c.innerHTML = detail[i].storage; 
			            }
	        	  if(j==17){
			            c.innerHTML = detail[i].remark; 
			            }
	          }
	            } 
	    }else{ 
	        var r = t.insertRow(); 
	        var c = r.insertCell(); 
	        c.innerHTML="null"; 
	    }
	    //alert(document.getElementById("new_Table"));
	    //document.getElementById("new_Table").appendChild(t); 
}
function createborrowIndetail(provinces){
	 var t=document.getElementById("borrowin_Table");     //获取Table
	//通过eval() 函数可以将JSON字符串转化为对象 
	 var trs=  t.getElementsByTagName("tr");//获取行 
	 for(var j=0;j<trs.length;j++){
		 t.deleteRow();
	 }
	 var detail = eval(provinces); 
	    if(detail.length>0){ 
	    	
	        for (var i = 0; i < detail.length; i++) { 
	            //tr 
	                    //if(i%11==0){ 
	                        var r = t.insertRow(t.rows.length);//创建新的行 
	                     //} 
	                    //td  
	          for(var j=0;j<13;j++){
	        	  var c = r.insertCell(); //创建新的列 
	        	  if(j==0){
			            c.innerHTML = detail[i].wholeName; 
			            }
		        	  if(j==1){
				            c.innerHTML = detail[i].productunit; 
				            }
		        	  if(j==2){
				            c.innerHTML = detail[i].deviceNo; 
				            }
		        	  if(j==3){
				            c.innerHTML = detail[i].pmnm; 
				            }
		        	  if(j==4){
				            c.innerHTML = detail[i].measureunit; 
				            }
		        	  if(j==5){
		        		  c.innerHTML = detail[i].manuf; 
				            }
		        	  if(j==6){
				            c.innerHTML = detail[i].keeper; 
				            }
		        	  if(j==7){
				            c.innerHTML = detail[i].productprice; 
				            }
		        	  if(j==8){
				            c.innerHTML = detail[i].location; 
				            }
		        	  if(j==9){
				            c.innerHTML = detail[i].produced; 
				            }
		        	  if(j==10){
				            c.innerHTML = detail[i].maintainCycle; 
				            }
		        	  if(j==11){
				            c.innerHTML = detail[i].status; 
				            }
		        	  if(j==12){
				            c.innerHTML = detail[i].remark; 
				            }
	          }
	            } 
	    }else{ 
	        var r = t.insertRow(); 
	        var c = r.insertCell(); 
	        c.innerHTML="null"; 
	    } 
	    //document.getElementById('borrowout_Table').appendChild(t); 
}


function createborrowOutdetail(provinces){
	 var t=document.getElementById("borrowout_Table");     //获取Table
	//通过eval() 函数可以将JSON字符串转化为对象 
	 var trs=  t.getElementsByTagName("tr");//获取行 
	 //for(var j=0;j<trs.length;j++){
		 //t.deleteRow();
	//}
	 var detail = eval("("+provinces+")"); 
	 
	    if(detail.length>0){ 
	    	
	        for (var i = 0; i < detail.length; i++) { 
	         
	        	
	                    //if(i%11==0){ 
	                        var r = t.insertRow(t.rows.length);//创建新的行
	                       
	                     //} 
	                    //td  
	          for(var j=0;j<13;j++){
	        	  var c = r.insertCell(); //创建新的列 
	        	  if(j==0){
	        		  
			            c.innerHTML = detail[i].wholeName; 
			            
			            }
		        	  if(j==1){
				            c.innerHTML = detail[i].productunit; 
				           
				            }
		        	  if(j==2){
				            c.innerHTML = detail[i].deviceNo; 
				            
				            }
		        	  if(j==3){
				            c.innerHTML = detail[i].pmnm; 
				           
				            }
		        	  if(j==4){
				            c.innerHTML = detail[i].measureunit;
				            
				            }
		        	  if(j==5){
		        		  c.innerHTML = detail[i].manuf; 
		        		  
				            }
		        	  if(j==6){
				            c.innerHTML = detail[i].keeper; 
				            
				            }
		        	  if(j==7){
				            c.innerHTML = detail[i].productprice; 
				           
				            }
		        	  if(j==8){
				            c.innerHTML = detail[i].location;
				            
				            }
		        	  if(j==9){
				            c.innerHTML = detail[i].produced; 
				            
				            }
		        	  if(j==10){
				            c.innerHTML = detail[i].maintainCycle; 
				           
				            }
		        	  if(j==11){
				            c.innerHTML = detail[i].status; 
				           
				            }
		        	  if(j==12){
				            c.innerHTML = detail[i].remark; 
				         
				            }
	          }
	            } 
	    }else{ 
	        var r = t.insertRow(); 
	        var c = r.insertCell(); 
	        c.innerHTML="null"; 
	    } 
	   // document.getElementById('borrowin_Table').appendChild(t); 
}
function createupdatedetail(provinces){
	 var t=document.getElementById("update_table");     //获取Table
	//通过eval() 函数可以将JSON字符串转化为对象 
	 var trs=  t.getElementsByTagName("tr");//获取行 
	 //for(var j=0;j<trs.length;j++){
		 //t.deleteRow();
	// }
	 var detail = eval(provinces); 
	    if(detail.length>0){ 
	    	
	        for (var i = 0; i < detail.length; i++) { 
	            //tr 
	                    //if(i%11==0){ 
	                        var r = t.insertRow(t.rows.length);//创建新的行 
	                     //} 
	                    //td  
	          for(var j=0;j<15;j++){
	        	  var c = r.insertCell(); //创建新的列 
	        	  if(j==0){
		            c.innerHTML = detail[i].wholeName; 
		            }
	        	  if(j==1){
			            c.innerHTML = detail[i].productunit; 
			            }
	        	  if(j==2){
			            c.innerHTML = detail[i].deviceNo; 
			            }
	        	  if(j==3){
			            c.innerHTML = detail[i].productprice; 
			            }
	        	  if(j==4){
			            c.innerHTML = detail[i].oldPrice; 
			            }
	        	  if(j==5){
	        		  c.innerHTML = detail[i].measureunit; 
			            }
	        	  if(j==6){
			            c.innerHTML = detail[i].manuf; 
			            }
	        	  if(j==7){
			            c.innerHTML = detail[i].keeper; 
			            }
	        	  if(j==8){
			            c.innerHTML = detail[i].pmnm; 
			            }
	        	  if(j==9){
			            c.innerHTML = detail[i].location; 
			            }
	        	  if(j==10){
			            c.innerHTML = detail[i].storage; 
			            }
	        	  if(j==11){
			            c.innerHTML = detail[i].maintainCycle; 
			            }
	        	  if(j==12){
			            c.innerHTML = detail[i].status; 
			            }
	        	  if(j==13){
			            c.innerHTML = detail[i].produced; 
			            }
	        	  if(j==14){
			            c.innerHTML = detail[i].remark; 
			            }
	          }
	            } 
	    }else{ 
	        var r = t.insertRow(); 
	        var c = r.insertCell(); 
	        c.innerHTML="null"; 
	    } 
	    //document.getElementById('update_table').appendChild(t); 
}
function createupdatedetail(provinces){
	 var t=document.getElementById("update_table");     //获取Table
	//通过eval() 函数可以将JSON字符串转化为对象 
	 var trs=  t.getElementsByTagName("tr");//获取行 
	 for(var j=0;j<trs.length;j++){
		 t.deleteRow();
	 }
	 var detail = eval(provinces); 
	    if(detail.length>0){ 
	    	
	        for (var i = 0; i < detail.length; i++) { 
	            //tr 
	                    //if(i%11==0){ 
	                        var r = t.insertRow(t.rows.length);//创建新的行 
	                     //} 
	                    //td  
	          for(var j=0;j<15;j++){
	        	  var c = r.insertCell(); //创建新的列 
	        	  if(j==0){
		            c.innerHTML = detail[i].wholeName; 
		            }
	        	  if(j==1){
			            c.innerHTML = detail[i].productunit; 
			            }
	        	  if(j==2){
			            c.innerHTML = detail[i].deviceNo; 
			            }
	        	  if(j==3){
			            c.innerHTML = detail[i].productprice; 
			            }
	        	  if(j==4){
			            c.innerHTML = detail[i].oldPrice; 
			            }
	        	  if(j==5){
	        		  c.innerHTML = detail[i].measureunit; 
			            }
	        	  if(j==6){
			            c.innerHTML = detail[i].manuf; 
			            }
	        	  if(j==7){
			            c.innerHTML = detail[i].keeper; 
			            }
	        	  if(j==8){
			            c.innerHTML = detail[i].pmnm; 
			            }
	        	  if(j==9){
			            c.innerHTML = detail[i].location; 
			            }
	        	  if(j==10){
			            c.innerHTML = detail[i].storage; 
			            }
	        	  if(j==11){
			            c.innerHTML = detail[i].maintainCycle; 
			            }
	        	  if(j==12){
			            c.innerHTML = detail[i].status; 
			            }
	        	  if(j==13){
			            c.innerHTML = detail[i].produced; 
			            }
	        	  if(j==14){
			            c.innerHTML = detail[i].remark; 
			            }
	          }
	            } 
	    }else{ 
	        var r = t.insertRow(); 
	        var c = r.insertCell(); 
	        c.innerHTML="null"; 
	    } 
	    //document.getElementById('update_table').appendChild(t); 
}