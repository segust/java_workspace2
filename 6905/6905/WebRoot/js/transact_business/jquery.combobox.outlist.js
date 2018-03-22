// Transform a textbox into a combobox, so users can enter text or select offered text.
// deerchao@gmail.com   2009-3-16
// 2009-3-18
//		Suggestion matching is now case-insensitive
//		The second time setting up combobox is working now
//		Fixed the IE relative z-index bug by avoiding relative positioning
// usage:
//--------------Html----------------
// <input class="combo" />
//--------------Script-------------
// jQuery('.combo').combobox(['option1', 'option2', 'option3'], {imageUrl : '/images/dropdown.gif'});

(function($) {
	// these variables are placed here so that they are shared between different
	// times of setting up comboboxes

	// keyCode data from http://unixpapa.com/js/key.html
	var keys = {
		up : 38,
		down : 40,
		enter : 13,
		tab : 9,
		esc : 27
	};
	var hideTimer;
	var showing = false;
	var suggestionsKey = 'combobox_suggestions';
	var optionsContainer;

	$.fn.combobox = function(suggestions, config) {
		config = $.extend({
			imageUrl : 'dropdown.gif'
		}, config);

		if (!optionsContainer) {
			optionsContainer = $('<ul id="comboboxDropdown" />').appendTo(
					$('body'));
			// if there is jquery.bgiframe plugin, use it to fix ie6
			// select/z-index bug.
			// search "z-index ie6 select bug" for more infomation
			if ($.fn.bgiframe)
				optionsContainer.bgiframe();
		}

		$(this)
				.each(
						function(i) {
							var $$ = this;
							var textBox = $($$);

							var oldSuggestions = $.data($$, suggestionsKey);
							$.data($$, suggestionsKey, suggestions);

							// exit if already initialized
							if (oldSuggestions)
								return;

							// turn off browser auto complete feature for
							// textbox
							// keydown to process Up,Down,Enter,Tab,Esc
							// keyup to see if text changed
							textBox.attr('autocomplete', 'off').focus(
									function() {
										show('');
									}).blur(blur).keydown(keydown).keyup(keyup);

							var container = textBox.wrap(
									'<div class="combobox" />').parent();

							var additionalHeight = $.browser.msie ? 5 : 3;
							var button = $(
									'<img class="button" src="img/transact_business/dropdown.gif" />')
									.insertAfter(textBox).css(
											{
												height : textBox.height()
														+ additionalHeight
											}).click(function() {
										textBox.focus();
									});
							textBox.width(textBox.width() - button.width());

							// keep the original value of textbox so we can
							// recove it if use presses esc
							var oriValue;
							function show(filter) {
								if (hideTimer) {
									window.clearTimeout(hideTimer);
									hideTimer = 0;
								}
								oriValue = textBox.val();
								hide();

								// generate the options (li inside ul)
								var html = '';
								var suggestions = $.data($$, suggestionsKey);
								for ( var k in suggestions) {
									var v = suggestions[k];
									if ((!filter)
											|| (filter && v
													.toLowerCase()
													.indexOf(
															filter
																	.toLowerCase()) >= 0)) {
										html += '<li>' + v + '</li>';
									}
								}

								// position and size of the options UI
								var loc = {
									left : textBox.offset().left,
									top : textBox.offset().top
											+ textBox.height() + 3,
									width : textBox.width() + button.width()
								}
								optionsContainer.html(html).css(loc);

								// decide which option is currently selected
								selIndex = 0;
								var found = false;
								var options = optionsContainer
										.children('li')
										.each(
												function(i) {
													if (found)
														return;
													if ($(this).text()
															.toLowerCase() == oriValue
															.toLowerCase()) {
														$(this).addClass(
																'selected');
														selIndex = i;
														found = true;
													}
												});
								// if there is no items matched, hide the empty
								// select list, so user can show options with
								// down key
								if (!options.size()) {
									hide();
									return;
								}
								if (!found)
									options.eq(0).addClass('selected');

								// mouse hover to change the highlight option,
								// clicking to select it
								options.click(function() {
									textBox.val($(this).text());
								}).hover(function() {
									options.each(function() {
										$(this).removeClass('selected');
									});
									$(this).addClass('selected');
									selIndex = options.index(this);
								});

								if (!filter)
									// showing all the options
									optionsContainer.slideDown();
								else
									// showing filtered options, happens when
									// textbox.value changed, should not flick
									optionsContainer.show();
								showing = true;
							}

							var selIndex;
							function keydown(evt) {
								switch (evt.keyCode) {
								case keys.esc:
									hide();
									textBox.val(oriValue);
									// fixes esc twice clears the textbox value
									// bug in ie
									evt.preventDefault();
									return;
								case keys.enter:
									choose();
									// don't submit the form
									// lyt
									var id = $(textBox).attr('id');
									var clazz = $(textBox).attr('class');
									var oTr = this.parentNode.parentNode.parentNode;
									if (clazz == "pmnm") {
										goForInfo(val, this);
									} else if (clazz == "model") {
										var pmnm = oTr
												.getElementsByTagName('td')[1]
												.getElementsByTagName('input')[0].value;
										goForMeasure(pmnm, val, this);
									} else if (clazz == "model") {
										var pmnm = oTr
												.getElementsByTagName('td')[1]
												.getElementsByTagName('input')[0].value;
										var model = oTr
												.getElementsByTagName('td')[2]
												.getElementsByTagName('input')[0].value;
										goForCount(pmnm, model, val, this);
									}
									evt.preventDefault();
									return;
								case keys.tab:
									choose();
									return;
								case keys.up:
									goup();
									return;
								case keys.down:
									godown();
									return;
								}
							}

							var oldVal = '';
							function keyup(evt) {
								var v = $(this).val();
								if (v != oldVal) {
									show(oldVal = v);
								}
							}

							function godown() {
								if (showing) {
									var options = optionsContainer
											.children('li');
									var n = options.size();
									if (!n)
										return;
									selIndex++;

									if (selIndex > n - 1)
										selIndex = 0;

									var v = options.eq(selIndex);
									if (v.size()) {
										options.each(function() {
											$(this).removeClass('selected')
										});
										v.addClass('selected');
									}
								} else {
									show('');
								}
							}

							function goup() {
								if (showing) {
									var options = optionsContainer
											.children('li');
									var n = options.size();
									if (!n)
										return;
									selIndex--;

									if (selIndex < 0)
										selIndex = n - 1;
									var v = options.eq(selIndex);
									if (v.size()) {
										options.each(function() {
											$(this).removeClass('selected')
										});
										v.addClass('selected');
									}
								} else {
									show('');
								}
							}

							function choose() {
								if (showing) {
									var v = $('li', optionsContainer).eq(
											selIndex);
									if (v.size()) {
										textBox.val(v.text());
										oldVal = v.text();
										hide();
									}
								}
							}

							function hide() {
								if (showing) {
									optionsContainer.hide().children('li')
											.each(function() {
												$(this).remove();
											});
									showing = false;
								}
							}

							function blur() {
								// if there's no setTimeout, when clicking
								// option li,
								// textBox.blur comes first, so hide is called,
								// and the ul.select is removed
								// so li.click won't fire
								if (!hideTimer) {
									hideTimer = window.setTimeout(hide, 300);
								}
								var v = $('li', optionsContainer).eq(selIndex);
								var id = $(textBox).attr('id');
								var clazz = $(textBox).attr('class');
								var oTr = this.parentNode.parentNode.parentNode;
								if (clazz == "pmnm") {
									if (v.size()) {
										textBox.val(v.text());
										oldVal = v.text();
										// 靠上下键盘触发离开
										goForInfo(oldVal, this);
										hide();
									}
								} else if (clazz == "model") {
									if (v.size()) {
										textBox.val(v.text());
										oldVal = v.text();
										// 靠上下键盘触发离开
										var pmnm = oTr
												.getElementsByTagName('td')[1]
												.getElementsByTagName('input')[0].value;
										goForMeasure(pmnm, oldVal, this);
										hide();
									}
								} else if (clazz == "measure") {
									if (v.size()) {
										textBox.val(v.text());
										oldVal = v.text();
										// 靠上下键盘触发离开
										var pmnm = oTr
												.getElementsByTagName('td')[1]
												.getElementsByTagName('input')[0].value;
										var model = oTr
												.getElementsByTagName('td')[2]
												.getElementsByTagName('input')[0].value;
										goForCount(pmnm, model, oldVal, this);
										hide();
									}
								}
							}
						});
	};
})(jQuery);

function goForCount(pmnm, model, measure, event) {
	var oTr = event.parentNode.parentNode.parentNode;
	var tds = oTr.getElementsByTagName("td");
	var keeper = document.getElementById("keeper").value;
	$
			.ajax({
				type : "post",
				url : "ProductHandleServlet?operate=goForCount&keeper="
						+ keeper + "&pmnm=" + pmnm + "&model=" + model
						+ "&measure=" + measure,
				dataType : "text",
				success : function(data) {
					if (data != "") {
						var product = eval("(" + data + ")");
						if (product.price != undefined) {
							tds[8].getElementsByTagName("input")[0].value = product.price;
						}
						if (product.count != undefined) {
							tds[6].getElementsByTagName("input")[0].value = product.count;
							tds[6].getElementsByTagName("input")[1].value = product.count;
							tds[9].getElementsByTagName("input")[0].value = parseInt(product.count)
									* parseInt(product.price);
						}
					}
				},
				error : function() {
					alert("请求失败");
				}
			});
}

// 根据品名代码查询产品型号
function goForInfo(pmnm, event) {
	var oTr = event.parentNode.parentNode.parentNode;
	var tds = oTr.getElementsByTagName("td");
	var keeper = document.getElementById("keeper").value;
	$
			.ajax({
				type : "post",
				url : "ProductHandleServlet?operate=goForPmnmAndKeeper&keeper="
						+ keeper + "&pmnm=" + pmnm,
				dataType : "text",
				success : function(data) {
					if (data != "") {
						var array = eval("(" + data + ")");
						var suggestion = "[";
						for (var i = 0; i < array.length; i++) {
							suggestion += array[i] + ",";
						}
						if (array.length > 0) {
							suggestion = suggestion.substring(0,
									suggestion.length - 1);
						} else {

						}
						suggestion += "]";
						$('.model').combobox(
								suggestion.replace('[', '').replace(']', '')
										.split(','));
					}
				},
				error : function() {
					alert("请求失败");
				}
			});
}

function goForMeasure(pmnm, model, event) {
	var oTr = event.parentNode.parentNode.parentNode;
	var tds = oTr.getElementsByTagName("td");
	var keeper = document.getElementById("keeper").value;
	$
			.ajax({
				type : "post",
				url : "ProductHandleServlet?operate=goForMeasure&keeper="
						+ keeper + "&pmnm=" + pmnm + "&model=" + model,
				dataType : "text",
				success : function(data) {
					if (data != "") {
						var array = eval("(" + data + ")");
						var suggestion = "[";
						for (var i = 0; i < array.length; i++) {
							suggestion += array[i] + ",";
						}
						if (array.length > 0) {
							suggestion = suggestion.substring(0,
									suggestion.length - 1);
						} else {

						}
						suggestion += "]";
						$('.measure').combobox(
								suggestion.replace('[', '').replace(']', '')
										.split(','));
					}
				},
				error : function() {
					alert("请求失败");
				}
			});
}

var lyt = {};
lyt.vars = {}, lyt.vars.myIds = new Array();
var idIndex = 0;
var maxNum;
//发料单的勾选
function selectToOut(event) {
	var oTr = event.parentNode.parentNode;
	var tds = oTr.getElementsByTagName('td');
	var table = document.getElementById("list-content");
	var botonTrs = table.getElementsByTagName('tr');

	if (event.checked) {
		var array = new Array();
		var j = 0;
		for (var i = 0; i < tds.length; i++) {
			if (i == 0) {
				array[j++] = tds[i].getElementsByTagName('input')[1].value;
			} else if (i == 1 || i == 3 || i == 4 || i == 7 || i == 8 || i == 9) {
				array[j++] = tds[i].innerHTML;
			}
		}
		array[j++] = tds[2].innerHTML;
		lyt.vars.myIds[idIndex++] = array[0];
		var lastTr = botonTrs[botonTrs.length - 1];
		var targetTds = lastTr.getElementsByTagName('td');

		if (lastTr.getElementsByTagName('td')[0].innerHTML != "1") {
			addNewRow();
			botonTrs = table.getElementsByTagName('tr');
			lastTr = botonTrs[botonTrs.length - 1];
			targetTds = lastTr.getElementsByTagName('td');
		} else if (lastTr.getElementsByTagName('td')[0].innerHTML == "1"
				&& lastTr.getElementsByTagName('td')[1]
						.getElementsByTagName('input')[1].value != "") {
			addNewRow();
			botonTrs = table.getElementsByTagName('tr');
			lastTr = botonTrs[botonTrs.length - 1];
			targetTds = lastTr.getElementsByTagName('td');
		}

		// array[0]:Ids,[1]:外键序号,[2]:pmnm,[3]:model,[4]:单价,[5]:计量单位,[6]:数量[7]产品名称
		// tds[0]:fkId 用序号对应,[1]:品名内码,[2]:model,[3]:单位,[6]:数量,[8]:单价,[9]:金额,[10]:隐藏一个Input放ids
		
		// targetTds[0].getElementsByTagName('input')[0].value = array[1];
		/*for(var k=0;k<array.length;k++){
			alert("k:"+k+": "+array[k]);
		}*/
		targetTds[1].getElementsByTagName('input')[0].value = array[1];
		targetTds[1].getElementsByTagName('input')[1].value = array[2];
		targetTds[2].getElementsByTagName('input')[0].value = array[7]+"+"+array[3];
		targetTds[3].getElementsByTagName('input')[0].value = array[5];
		targetTds[6].getElementsByTagName('input')[0].value = array[6];
		targetTds[6].getElementsByTagName('input')[1].value = array[6];
		targetTds[8].getElementsByTagName('input')[0].value = array[4];
		targetTds[9].getElementsByTagName('input')[0].value = parseInt(
				array[4], 10)
				* parseInt(array[6], 10);
		targetTds[10].getElementsByTagName('input')[0].value = array[0];

	} else {
		var fk = tds[1].innerHTML;
		for (var i = 1; i < botonTrs.length; i++) {
			var oFkVal = botonTrs[i].getElementsByTagName("td")[1]
					.getElementsByTagName("input")[0].value;
			if (fk == oFkVal) {
				if (botonTrs.length == 2) {
					var oFirstTds = botonTrs[i].getElementsByTagName("td");
					for (var j = 0; j < oFirstTds.length; j++) {
						var oInput0=oFirstTds[j].getElementsByTagName("input")[0],
							oInput1=oFirstTds[j].getElementsByTagName("input")[1];
						switch (j) {
						case 0:
							break;
						case 1:
							oInput0.value = "";
							oInput1.value = "";
							break;
						case 6:
							oInput0.value = "";
							oInput1.value = "";
							//$(oInput1).style="";
							//oFirstTds[j].getElementsByTagName("input")[1].style="";
							break;
						case 10:
							oInput0.value = "";
							oInput1.value = "";
							break;
						default:
							oInput0.value = "";
							break;
						}
					}
				} else {
					$(botonTrs[i]).remove();
					sortOrderId();
					break;
				}
			}
		}
	}
}

//发料单序号重排
function sortOrderId(){
	var oTable = document.getElementById("list-content"),
		  oTrs=oTable.getElementsByTagName("tr");
	for (var i = 1; i < oTrs.length; i++) {
		var oTd=oTrs[i].getElementsByTagName("td")[0];
		$(oTd).html(i);
	}
}

/*
 * 新版0823 添加一行
 */
function addNewRow() {
	var oTable = document.getElementById("list-content");
	var trs = oTable.getElementsByTagName("tr");
	var oOrder = trs[trs.length - 1].getElementsByTagName("td")[0].innerHTML;
	var pre = trs[trs.length - 1].getElementsByTagName("td")[6]
			.getElementsByTagName("input")[1];
	pre.blur(validNum(pre));
	// var oOrder = 1;
	oOrder = parseInt(oOrder) + 1;
	if (oOrder > 18) {
		alert("增加行数不得超过18行");
	} else {
		var oTr = document.createElement('tr');
		var oFragment = document.createDocumentFragment();
		for (var i = 0; i < 11; i++) {
			var oTd = document.createElement('td');
			if (i == 0) {
				oTd.innerHTML = oOrder;
			} else {
				var oInput = document.createElement('input');
				oInput.type = "text";
				if (i == 1) {
					// var sugestions =
					// document.getElementById("pmnm-value").value;
					// oInput.setAttribute("class","pmnm");
					var oHidden1 = document.createElement('input');
					oHidden1.type = "hidden";
					oInput.disabled = "true";
					oTd.appendChild(oHidden1);
				} else if (i == 2 || i == 3 || i == 8 || i == 9) {
					// var models =
					// document.getElementById("model-value").value;
					// oInput.setAttribute("class","model");
					oInput.disabled = "true";
				} else if (i == 6 || i == 10) {
					var oHidden = document.createElement('input');
					oHidden.type = "hidden";
					oTd.appendChild(oHidden);
					// 给oInput增加离开事件
					// oInput.setAttribute("onblur","validNum(this);");
				}
				oInput
						.setAttribute(
								"style",
								"border:none;width: 85px;height:20px;text-align:center;border-bottom-color: black;border-bottom: 1px solid;");
				oTd.appendChild(oInput);
			}
			oFragment.appendChild(oTd);
		}
		var oSpan = document.createElement('span');
		oTr.appendChild(oFragment);
		oTr.appendChild(oSpan);
		oTable.appendChild(oTr);
		// $('.pmnm').combobox(sugestions.replace('[','').replace(']','').split(','));
		/*
		 * $('.model').combobox(models.replace('[','').replace(']','').split(','));
		 * $('.model').combobox(models.replace('[','').replace(']','').split(','));
		 */
	}
}

function isExist(botonTrs, array) {
	var i;
	var flag = false;
	for (i = 1; i < botonTrs.length; i++) {
		var oTds = botonTrs[i].getElementsByTagName('td');
		if (oTds[1].getElementsByTagName('input')[1].value == array[1]
				&& oTds[2].getElementsByTagName('input')[0].value == array[2]) {
			flag = true;
			break;
		}
	}
	if (flag) {
		return i;
	} else {
		return -1;
	}
}

function validNum(event) {
	// 输入数量
	var num = parseInt(event.value);
	// 实际数量
	var oTd = event.parentNode;
	var oTr = oTd.parentNode;
	var real = parseInt(oTd.getElementsByTagName('input')[0].value);
	var price = parseInt(oTr.getElementsByTagName('td')[8]
			.getElementsByTagName('input')[0].value);
	var ids = oTr.getElementsByTagName('td')[10].getElementsByTagName('input')[0].value;
	var view = oTd.getElementsByTagName('input')[1];
	if (num <= real && num > 0) {
		view.setAttribute("style",
				"background: url('img/ok.jpg') no-repeat scroll right center;");
		oTr.getElementsByTagName('td')[9].getElementsByTagName('input')[0].value = num
				* price;
		/*
		 * var changeIds=ids.split(","); changeIds = changeIds.slice(0,num);
		 * oTr.getElementsByTagName('td')[10]
		 * .getElementsByTagName('input')[0].value = changeIds.join(",")
		 */
	} else {
		alert("输入数大于最大允许出库数：" + real + "或者输入非法数");
		view.value = real;
		oTr.getElementsByTagName('td')[9].getElementsByTagName('input')[0].value = parseInt(
				real, 10)
				* price;
		/*
		 * view.setAttribute("style", "background: url('img/error.jpg')
		 * no-repeat scroll right center;");
		 */
	}
}