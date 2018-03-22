$(function(){
    
    var checkInfo={},
        discernAttr="",
        $success=$("div.add-success");
        // 事件代理
    $(".btn-wrap,.add-success,.all-record-wrap").on("click","a",function(ev){
        $this=$(this);
        discernAttr=$this.attr("data-fn");
        
        if(discernAttr=="add-record"){

            checkInfo={
            	"unit":$("#unit").val(),
            	"date":$("#date").val(),
            	"site":$("#site").val(),
            	"item":$("#item").val(),
            	"suggest":$("#suggest").val(),
            	"feedback":$("#feedback").val(),
            	"remark":$("#remark").val()
            };
            
            // 如果添加成功
            if(addRecord(checkInfo)){
                $(".check-info-wrap,.all-record-wrap,.btn-wrap").hide();
                $(".add-success").show();
            }
        }else if(discernAttr=="add-again"){
            $(".all-record-wrap,.add-success").hide();
            $(".check-info-wrap,.btn-wrap").show();
        }else if(discernAttr=="view-record"){
        	$.ajax({
                type:"get",
                url:"/6905/InspectServlet?operate=inspectQuery",
                dataType:'json',
                success:function(resData){
                    if(resData.length>0&&resData[0]!=null){
                        $(".tips.error-tips").hide();
                        $(".check-info-wrap,.btn-wrap").hide();
                        $success.hide();
                        $(".all-record-wrap").show();
                        // console.log(resData);
                        loadAllRecord(resData);
                    }else{
                        $(".tips.error-tips").show();
                    }
                     
                }
            });
        }
      
    });


// 向后台保存检查记录
    function addRecord(data){
    	var flag=false;
        $.ajax({
            type:"post",
            url:"/6905/InspectServlet",
            data:{
                data:JSON.stringify(data),
                operate:"inspect"
            },
            dataType:'JSON',
            async:false,
            success:function(res){
                if(res.status){
                    flag=true;
                }else{
                	flag=false;
                }
            }
        });
        return flag;
    }

// 加载所有检查记录
    function loadAllRecord(resData){
       
        var oTbody=$(".all-record-wrap tbody")[0];
        for(var i=0;i<resData.length;i++){
            oTbody.insertRow(i);
            for(var j=0;j<resData[0].length;j++){
                oTbody.rows[i].insertCell(j);
                oTbody.rows[i].cells[j].innerHTML=resData[i][j];
            }
            
        }
    }




});


