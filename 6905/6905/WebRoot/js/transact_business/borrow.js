/**
 * 用于轮换管理
 */
function showInput() {
	var select = document.getElementById("productunit");
	lastIndex = select.selectedIndex;
	var midValue = select.options[lastIndex].value;
	if(midValue == "other") {
		var input = document.getElementById("other-input");
		input.setAttribute("style", "display:block;");
	}else {
		var input = document.getElementById("other-input");
		input.setAttribute("style", "display:none;");
	}
}