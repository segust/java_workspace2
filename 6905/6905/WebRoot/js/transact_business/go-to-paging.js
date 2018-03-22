var baseInfo={
    baseUrl:'',
    contractid:'',
    nowPage:-1,
};
getBaseUrl();
defaultLoad(baseInfo.baseUrl,1);
addSearchEv();

// 确定当前页面的ajax请求的基本url
function getBaseUrl(){
    var pageTitle=document.getElementsByTagName("title")[0].innerHTML;
        if(pageTitle=="轮换入库申请管理"){
            baseInfo.baseUrl="BorrowServlet?operate=getNextPage";
        }else if(pageTitle=="更新入库申请管理"){
            baseInfo.baseUrl="UpdateServlet?operate=getNextPage";
        }
}

// 给分页按钮添加点击事件
function addPageEv(){

    var oPagingWrap=$(".paging-wrape"),
        reqPage,
        totalPage=parseInt($(".paging-wrape .total span")[0].innerHTML),
        oA=$(".paging-wrape")[0].getElementsByTagName("a"),
        contractid=baseInfo.contractid;
        
    for(var i=0;i<$(".pagination a").length;i++){
        if($(".pagination a")[i].className=="active"){
            baseInfo.nowPage=parseInt($(".pagination a")[i].innerHTML);
        }
    }

    for(var i=0;i<oA.length;i++){
        
        oA[i].onclick=function(event){
            
            var ev=event||window.event;
                targ=ev.target||ev.srcElement;
        	if(targ.getAttribute("aria-label")=="Previous"){
        		if(baseInfo.nowPage>1){
        			reqPage=baseInfo.nowPage-1;
        		}else{
        			reqPage=baseInfo.nowPage;
        		}
                
        	}else if(targ.getAttribute("aria-label")=="Next"){
                if(baseInfo.nowPage<totalPage){

        			reqPage=baseInfo.nowPage+1;
        			
        		}else{
        			reqPage=baseInfo.nowPage;
        		} 
        	}else if(targ.innerHTML=="确定"){
        		var inputPage=parseInt($(".go-page .page_Input")[0].value);
     
        		if(inputPage>totalPage||inputPage<1){
                    reqPage=baseInfo.nowPage;
        		}else{
        			reqPage=inputPage;
        		}
               
        	}else{
        		reqPage=this.innerHTML;
        	}

        	getProData(baseInfo.baseUrl,reqPage,baseInfo.contractid);
        };
    }
}
// 给查询按钮添加点击事件
function addSearchEv(){
    var oSearch=$(".toptitle-op .in-apply .submit-btn")[0];
        oSearch.onclick=function(){
            // alert(baseInfo.baseUrl);
            getProData(baseInfo.baseUrl,1);
        };
}

// 获得数据
function getProData(baseUrl,reqPage){
    var condition=$(".toptitle-op .in-apply input");
    console.log(reqPage);
    $.ajax({
        type:'post',
        url:baseUrl,
        data:{
            "curPageNum":reqPage,
            "operateTime":condition.eq(0).val(),
            "productModel":condition.eq(1).val()
        },
        dataType:'json',
        success:function(data){
            console.log(JSON.stringify(data));
            loadPro(data);
        }
    });
}


// 异步加载表格
function loadPro(tableData){
    var oTbody=$("#fare-table tbody"),
        nowPage=tableData.nowPage,
        totalPage=tableData.totalPage;
        oTbody.eq(0).html("");
    for(var i=0;i<tableData.items.length;i++){
        $("#fare-table tbody")[0].insertRow(i);
        oTbody[0].rows[i].insertCell(0);
        oTbody[0].rows[i].cells[0].innerHTML='<input type="radio" class="setTem" name="selectPro" onclick="radioSelect(this)"><input type="hidden" value="'+tableData.items[i][0]+'">';
        for(var j=1;j<tableData.items[0].length;j++){
            oTbody[0].rows[i].insertCell(j);
            oTbody[0].rows[i].cells[j].innerHTML=tableData.items[i][j];
        }
    
    }

    for(var i=0;i<$(".pagination a").length;i++){
        $(".pagination a").eq(i).removeClass("active");
    }
    
    $(".pagination a").eq(nowPage).addClass("active");
    baseInfo.nowPage=nowPage;
}


function defaultLoad(baseUrl,reqPage){
    var str='<li><a href="javascript:;" aria-label="Previous">&laquo;</a></li>';
        str+='<li><a class="active" href="javascript:;">'+1+'</a></li>';

    $.ajax({
        type:'post',
        url:baseUrl,
        data:{
            "curPageNum":reqPage,

        },
        dataType:"json",
        success:function(data){
            console.log(data);
            console.log(data.totalPage);
            $(".paging-wrape .total span")[0].innerHTML=data.totalPage;
            for(var i=0;i<data.totalPage-1;i++){
                str+='<li><a href="javascript:;">'+(i+2)+'</a></li>';
            }
            
            str+='<li><a href="javascript:;" aria-label="Next">&raquo;</a></li>';
            $(".paging-wrape .pagination")[0].innerHTML=str;
            addPageEv();
        }
    });
}