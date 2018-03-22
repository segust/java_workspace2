//让一个元素变为向x或y轴固定，type为不固定后该元素的position的值
function fixFloatDir(className, type) {
	var nodes = $('.' + className + '');
	var width = 0, height = 0;
	var Left = 0, Top = 0;
	Left = document.documentElement.scrollLeft || document.body.scrollLeft;
	Top = document.documentElement.scrollTop || document.body.scrollTop;
	if (Left > 0 && Top == 0) {
		for ( var i = 0; i < nodes.length; i++) {
			nodes[i].style.position = 'fixed';
			nodes[i].style.left = 0;
			nodes[i].style.top = 0;
		}
	} else {
		for ( var i = 0; i < nodes.length; i++) {
			width = nodes[i].offseWidth;
			height = nodes[i].offseHeight;
			nodes[i].style.position = '' + type + '';
			nodes[i].style.top = 0;
			nodes[i].style.left = Left + 'px';
			nodes[i].style.width = width - Left + 'px';
		}
	}
}

/*
 * function tabCursor(tabClass){ var tables=$('.'+tabClass+''),tr=null; for(var
 * i=0;i<tables.length;i++){ tr=tables[i].rows; for(var j=0;j<tr.length;j++){
 * tr[j].style.cursor="pointer"; } } }
 */
/*
 * $(document).ready(function(){ $("#re_new").click(function(){
 * $("#whole").show(); $("#renew_basedata").show(); }); });
 * $(document).ready(function(){ $("#save_base").click(function(){
 * $("#whole").hide(); $("#renew_basedata").hide(); }); }
 */
/*function selectoption() {
	var firstoption = document.getElementById('firstoption');
	var secondoption = document.getElementById('secondoption');
	var thirdoption = document.getElementById('thirdoption');
	if (firstoption.innerHTML == "三月") {
		secondoption.innerHTML = "六个月";
		thirdoption.innerHTML = "一年";
	} else if (firstoption.innerHTML == "六个月") {
		secondoption.innerHTML = "三个月";
		thirdoption.innerHTML = "一年";
	} else if (firstoption.innerHTML == "一年") {
		secondoption.innerHTML = "三个月";
		thirdoption.innerHTML = "六个月";
	}
}*/

//日志管理 鼠标点击查看更多和消失更多
function showMoreRemark(targ){
	var oLogRemark=targ.parentNode.getElementsByTagName("input")[0].value;
	$("#show-logremark-box").html(oLogRemark);
	$("#show-logremark-box").slideToggle("slow");
}

