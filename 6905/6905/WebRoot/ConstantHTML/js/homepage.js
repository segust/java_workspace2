window.onload=function(){
	 setTopWidth();
	 setLeftHeight();
	 setRightWidth();
	 sayHello();
	//changeColor();
	if(typeof(mj)!="undefined"){
		mj.app.addOnclick();
	} 
	if(typeof(lhs)!="undefined"){
		lhs.app.showUnusualRecords();
		lhs.app.showCountByCondition();
	}
	if(typeof(inApplyPage)!="undefined"){
		inApplyPage.util.executeFn();
	}
	if(typeof(yg)!="undefined"){ 
		yg.app.aa();
	} 
	
	addTopInfoEvent();
	if(typeof(welcomeBox)!="undefined"){
		setTimeout("welcomeBox()",500);
	}
	if(typeof(allFn9831)!="undefined"){
		allFn9831();
	}
	 setInterval("checkInfo()",10000);
}

 window.onresize=function(){
 	setTopWidth();
 	setLeftHeight();
 	setRightWidth();
 	sayHello();
 }

function setTopWidth(){
	var topInfo=$('#top')[0];
	var width=document.documentElement.clientWidth || document.body.clientWidth;
	if(width>1215){
		topInfo.style.width=width+'px';	
	}else{
		topInfo.style.width=1216+'px';	
	}	
}

//设置左边和右边盒子高度
function setLeftHeight(){
	var leftdiv=document.getElementById('left');
	var rightdiv=document.getElementById('right');
	var height=document.documentElement.clientHeight|| document.body.clientHeight;
	if(height>350){
		leftdiv.style.height=height-90+'px';
		rightdiv.style.height=height-90+'px';
	}else{
		leftdiv.style.height=350+'px';
		rightdiv.style.height=height-90+'px';
	}

}


//设置右边盒子的宽度
function setRightWidth(){
	var width=document.documentElement.clientWidth || document.body.clientWidth;
	var rightdiv=document.getElementById('right');
	rightdiv.style.width=width-145+"px";
}

//根据当前时间显示上午好、下午好、晚上好
function sayHello(){
	var nowdate=new Date();
	var hour=nowdate.getHours();

	if(hour>=8&&hour<=12)
		document.getElementById("hello").innerHTML="上午好";
	else if(hour>=13&&hour<=17)
		document.getElementById("hello").innerHTML="下午好";
	else if(hour>=18||hour<=7)
		document.getElementById("hello").innerHTML="晚上好";
}

//退出事件，向后台发送请求
function userLoginOut(basePath){
	$.ajax({
		url:"UserLoginOutServlet",
		type:"POST",
		success:function(data){
			if(data=="1")
				window.location.href=basePath+"jsp/login/login.html";
		}
	});
}

// 给top消息链接添加事件
function addTopInfoEvent(){
	// var topInfo=;
	document.getElementById('top-info-btn-link').onclick=function(){		
		swicth();
	}
}

function swicth(){
	var infoWrap=document.getElementById('top-info-wrap');
	// status=infoWrap.getAttribute('data-status');
	$("#top-info-wrap").slideToggle();	
}

function checkInfo(){
	$.ajax(
	{
		type:"post",
		url:"AlertNewMaintain",
		dataType:"json",
		success:function(data){
			if(data.protect.num != 0||data.update.num != 0){

				var oTopInfoWrap= document.getElementById("top-info-wrap"),
				    oUl=oTopInfoWrap.getElementsByTagName("ul"),
				    oNum1=oUl[0].getElementsByTagName('span')[0],
				    oNum2=oUl[1].getElementsByTagName('span')[0],
                    oA1=oUl[0].getElementsByTagName('a')[0],
                    oA2=oUl[1].getElementsByTagName('a')[0];

            
				oNum1.innerHTML=data.protect.num;
		        if(data.protect.num==0){
                    oA1.className="disabled";
		        }else{
		        	oA1.className="not-disabled";
		        }
				oA1.href=data.protect.link;
                
                oNum2.innerHTML=data.update.num;
                if(data.update.num==0){
                    oA2.className="disabled";
		        }else{
		        	oA2.className="not-disabled";
		        }
				oA2.href=data.update.num;
				
				// var infoWrap=document.getElementById('top-info-wrap'),
				// status=infoWrap.getAttribute('data-status');
				$("#top-info-wrap").slideDown("1000");
			}
		}
	});
}


//用来判断时间
function judgeInputTime(text){
	var flag=false;
	var  Time=document.getElementById(text).value;
	if(Time==""){
		flag=true;
	}else{	   
	   var     newPar=/^(-|\+)?\d+$/ ;        //用来判断是不是整数 
	   var   b=newPar.test(Time);
	   if(b==false) {
	   	alert("输入的 ' "+Time+" '非法，请重新输入 ！ ");
	   	document.getElementById(text).value=""; 
	   	flag=false; 
	   } else {//是整数的情况下，判断他是否大于0
	   	if(Time>0){
	   		flag=true;
	   	} 
		   else{//若不大于0，则错误
		   	alert("输入的 ' "+Time+" '非法，请重新输入 ！ ");
		   	document.getElementById(text).value=""; 
		   	flag=false; 
		   }
		}  
	}
	return   flag;
}


