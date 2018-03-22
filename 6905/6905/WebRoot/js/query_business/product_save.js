addLinkEvent();

function addLinkEvent(){

	var oLink=document.getElementById("relative-link").getElementsByTagName("a");

    for(var i=0;i<oLink.length;i++){

	    oLink[i].onclick=function(){
	    	
	    	var jsonData=savaData();
	    	$.ajax({
	    		
	    		type:"post",
	    		url:"ApplyHandleServlet?operate=justForTranslate",
	    		dataType:"json",
	    		data:{
	    			data:jsonData
	    		},
	    		async:false,
	    		success:function(data){
	    			
	    		}
	    	});
            // return false;
	    };
    }

}

function savaData(){
	
	var oTable=document.getElementById("fare-table"),
	oTbody=oTable.getElementsByTagName("tbody")[0],

	oTr=oTbody.getElementsByTagName("tr"),
	count=0,
	data={
		"totalPage":1,
		"items":{}
	};
	for(var x=0;x<oTr.length;x++){

		var td=oTr[x].getElementsByTagName("td");

		if(td[0].getElementsByTagName('input')[0].checked){
			count=count+1;
		}

	}

	page=Math.ceil(count/10);

	data["totalPage"]=page;
	var strPage=toString(page);
    var i=0;
	for(var k=1;k<page+1;k++){

		data["items"][k]= [];
		var count2=0;
	
		for(;i<oTr.length;i++){
           
			var td=oTr[i].getElementsByTagName("td");
			if(td[0].getElementsByTagName('input')[0].checked){

				data["items"][k][count2]=[];
				for(var j=1;j<td.length-1;j++){
					data["items"][k][count2].push(td[j].innerHTML);
				}
				if(count2==9){
                    i++;
                    break;
				}
				count2++;
			}

        
		}

	}

	var jsonData=JSON.stringify(data);
    
	return jsonData;
}