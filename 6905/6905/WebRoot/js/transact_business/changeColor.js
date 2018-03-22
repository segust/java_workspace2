function changeColor(){
	var type=document.getElementById("type").innerHTML;
	var tr=document.getElementById("content").getElementsByTagName("tr");
	for(var i=1;i<tr.length;i++) {
		if(type=="updateOut"||type=="turnOut"){
			var td=tr[i].getElementsByTagName("td")[12];
			if(td.innerHTML>0)
		    	td.style.color="red";
		}else if(type=="allotOut"){
			var td=tr[i].getElementsByTagName("td")[15];
			if(td.innerHTML>0)
				td.style.color="red";
		}
	}
}
//changeColor();