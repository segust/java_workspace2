function addinput() {
	var btn = document.getElementById("upload");
	btn.disabled = false;
	btn.className = "upload-btn";

	var div = document.getElementById("file");
	div.className = 'file-btn-box';

	var input = document.createElement("input");
	input.type = "file";
	input.name = "filename";

	var del = document.createElement("input");
	del.type = "button";
	del.className = 'del-btn';
	del.value = "x";
	del.onclick = function d() {
		this.parentNode.parentNode.removeChild(this.parentNode);
	};

	var innerdiv = document.createElement("div");

	innerdiv.appendChild(input);
	innerdiv.appendChild(del);

	div.appendChild(innerdiv);
}

function checkUpload() {
	if (document.files.filename.value == "") {
		window.wxc.xcConfirm("请检查要上传的文件是否添加!", "warning");
		return false;
	}
	for ( var i = 0; i < document.files.filename.length; i++) {
		if (document.files.filename[i].value == "") {
			window.wxc.xcConfirm("请检查要上传的文件是否全部添加!", "warning");
			return false;
		}
	}
}

function judgeFile() {
	var f = document.getElementById("up1").value;
	if (f == "") {
		window.wxc.xcConfirm("请检查要上传的文件是否添加!", "warning");
		return false;
	} else {
		document.getElementById("fileForm").submit();
	}
}

function deleteAttach(url) {
	window.wxc.xcConfirm("确认删除吗？", "confirm", {
		onOk : function() {
			window.location.href = url;
		}
	});
	//	 var flag = window.confirm("确认删除吗？"); if (flag) { window.location.href =
	//	 url; }
}
