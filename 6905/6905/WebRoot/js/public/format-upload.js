$("#upfile").val("");
function format(){
    
    var flag=true,
    fileObject=$('input[type="file"]'),
    filepath=fileObject.val(),
        // type=fileObject.attr("accept").slice(1,fileObject.attr("accept").length);
        type=fileObject.attr("accept").toLowerCase().replace(/\./g,"").split(",");
        
        addIndexOf();
        if(filepath==undefined||$.trim(filepath)==""){

            $("p.error-info").text("请选择上传文件！"); 
            flag=false;
        }else{
            
            var fileArr=filepath.split("\\"),
            fileTArr=fileArr[fileArr.length-1].toLowerCase().split("."),
            filetype=fileTArr[fileTArr.length-1];
            
            console.log(type+"="+filetype);

            if(type.join('').indexOf(filetype)){ 
                $("p.error-info").text("上传文件必须为"+type+"文件！"); 
                $("#upfile").val("");
                flag=false; 
            }else{ 
                $("p.error-info").text(""); 
            }
        }
        console.log(flag); 
        return flag;
    }

    function addIndexOf(){
        if (!Array.prototype.indexOf)
        {
          Array.prototype.indexOf = function(elt /*, from*/)
          {
            var len = this.length >>> 0;

            var from = Number(arguments[1]) || 0;
            from = (from < 0)
            ? Math.ceil(from)
            : Math.floor(from);
            if (from < 0)
              from += len;
          for (; from < len; from++)
          {
              if (from in this &&
                  this[from] === elt)
                return from;
        }
        return -1;
    };
}
}
