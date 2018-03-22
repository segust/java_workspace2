changeColor();

// 隔行变色
function changeColor(){
	if(document.getElementsByTagName("tr")){
		var tr=document.getElementsByTagName("tr");
		for(var i=1;i<tr.length;i++){
			if(i%2==0){
				tr[i].className="addTrColor";
			}else{
				// tr[i].style.backgroundColor="white";
			}
		}
	}
}