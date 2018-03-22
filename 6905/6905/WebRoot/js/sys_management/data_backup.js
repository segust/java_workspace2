function outData() {
	jQuery.ajax({
		type : "post",
		url : "SystemManagementServlet?operate=doDataBackup",
		success : function(data) {
			if (data == 0) {
				alert("导出失败!");
			} else {
				window.location.href = "SystemManagementServlet?operate=download&absolutePath="
						+ data;
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}