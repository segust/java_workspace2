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
    //these variables are placed here so that they are shared between different times of setting up comboboxes

    //keyCode data from http://unixpapa.com/js/key.html
    var keys = { up: 38, down: 40, enter: 13, tab: 9, esc: 27 };
    var hideTimer;
    var showing = false;
    var suggestionsKey = 'combobox_suggestions';
    var optionsContainer;

    $.fn.combobox = function(suggestions, config) {
        config = $.extend({ imageUrl: 'dropdown.gif' }, config);
        
        if(!optionsContainer) {
					optionsContainer = $('<ul id="comboboxDropdown" />').appendTo($('body'));
		    //if there is jquery.bgiframe plugin, use it to fix ie6 select/z-index bug.
		    //search "z-index ie6 select bug" for more infomation
		    if ($.fn.bgiframe)
		        optionsContainer.bgiframe();
				}
				
        $(this).each(function(i) {
            var $$ = this;
            var textBox = $($$);

            var oldSuggestions = $.data($$, suggestionsKey);
            $.data($$, suggestionsKey, suggestions);
            
            //exit if already initialized
            if (oldSuggestions)
                return;

            //turn off browser auto complete feature for textbox
            //keydown to process Up,Down,Enter,Tab,Esc
            //keyup to see if text changed
            textBox.attr('autocomplete', 'off').focus(function() { show(''); }).blur(blur).keydown(keydown).keyup(keyup);

            var container = textBox.wrap('<div class="combobox" />').parent();

            var additionalHeight = $.browser.msie ? 5 : 3;
            var button = $('<img class="button" src="img/transact_business/dropdown.gif" />').insertAfter(textBox).css({ height: textBox.height() + additionalHeight }).click(function() {
                textBox.focus();
            });
            textBox.width(textBox.width() - button.width());

            //keep the original value of textbox so we can recove it if use presses esc
            var oriValue;
            function show(filter) {
                if (hideTimer) {
                    window.clearTimeout(hideTimer);
                    hideTimer = 0;
                }
                oriValue = textBox.val();
                hide();

                //generate the options (li inside ul)
                var html = '';
                var suggestions = $.data($$, suggestionsKey);
                for (var k in suggestions) {
                    var v = suggestions[k];
                    if ((!filter) || (filter && v.toLowerCase().indexOf(filter.toLowerCase()) >= 0)) {
                        html += '<li>' + v + '</li>';
                    }
                }

                //position and size of the options UI
                var loc = { left: textBox.offset().left, top: textBox.offset().top + textBox.height() + 3, width: textBox.width() + button.width() }
                optionsContainer.html(html).css(loc);

                //decide which option is currently selected
                selIndex = 0;
                var found = false;
                var options = optionsContainer.children('li').each(function(i) {
                    if (found) return;
                    if ($(this).text().toLowerCase() == oriValue.toLowerCase()) {
                        $(this).addClass('selected');
                        selIndex = i;
                        found = true;
                    }
                });
                //if there is no items matched, hide the empty select list, so user can show options with down key
                if (!options.size()) {
                    hide();
                    return;
                }
                if (!found)
                    options.eq(0).addClass('selected');

                //mouse hover to change the highlight option, clicking to select it
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
                //showing all the options
                    optionsContainer.slideDown();
                else
                //showing filtered options, happens when textbox.value changed, should not flick
                    optionsContainer.show();
                showing = true;
            }

            var selIndex;
            function keydown(evt) {
                switch (evt.keyCode) {
                    case keys.esc:
                        hide();
                        textBox.val(oriValue);
                        //fixes esc twice clears the textbox value bug in ie
                        evt.preventDefault();
                        return;
                    case keys.enter:
                        choose();
                        //don't submit the form
                        //lyt
                        var id = $(textBox).attr('id');
                        if(id.toLowerCase() == "deviceno") {
                        	 goForInfo(val);
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
                    var options = optionsContainer.children('li');
                    var n = options.size();
                    if (!n)
                        return;
                    selIndex++;

                    if (selIndex > n - 1)
                        selIndex = 0;

                    var v = options.eq(selIndex);
                    if (v.size()) {
                        options.each(function() { $(this).removeClass('selected') });
                        v.addClass('selected');
                    }
                } else {
                    show('');
                }
            }

            function goup() {
                if (showing) {
                    var options = optionsContainer.children('li');
                    var n = options.size();
                    if (!n)
                        return;
                    selIndex--;

                    if (selIndex < 0)
                        selIndex = n - 1;

                    var v = options.eq(selIndex);
                    if (v.size()) {
                        options.each(function() { $(this).removeClass('selected') });
                        v.addClass('selected');
                    }
                } else {
                    show('');
                }
            }

            function choose() {
                if (showing) {
                    var v = $('li', optionsContainer).eq(selIndex);
                    if (v.size()) {
                        textBox.val(v.text());
                        oldVal = v.text();
                        hide();
                    }
                }
            }

            function hide() {
                if (showing) {
                    optionsContainer.hide().children('li').each(function() { $(this).remove(); });
                    showing = false;
                }
            }

            function blur() {
                //if there's no setTimeout, when clicking option li,
                //textBox.blur comes first, so hide is called, and the ul.select is removed
                //so li.click won't fire
                if (!hideTimer) {
                    hideTimer = window.setTimeout(hide, 300);
                }
                var v = $('li', optionsContainer).eq(selIndex);
                var id = $(textBox).attr('id');
                if(id.toLowerCase() == "deviceno") {
                     if (v.size()) {
                         textBox.val(v.text());
                         oldVal = v.text();
                         //靠上下键盘触发离开
                         goForInfo(oldVal);
                         hide();
                     }
                }
            }
        });
    };
})(jQuery);

function  goForInfo(deviceNo) {
	$.ajax({
		type : "post",
		url : "ProductHandleServlet?operate=findByNoPre&deviceNo="+deviceNo,
		dataType : "text",
		success : function(data) {
			if(data != "") {
				var product = eval("("+data+")");
				if(product.productName != undefined) {
					var name = document.getElementById("productname");
					name.setAttribute("value", product.productName);
				}
				if(product.productModel != undefined) {
					var model = document.getElementById("productmodel");
					model.setAttribute("value", product.productModel);
				}
				if(product.unitName != undefined) {
					var unit = document.getElementById("productunit");
					unit.setAttribute("value", product.unitName);
				}
				if(product.pmnm != undefined) {
					var pmnm = document.getElementById("pmnm");
					pmnm.setAttribute("value", product.pmnm);
				}
				if(product.price != undefined) {
					var price = document.getElementById("price");
					price.setAttribute("value", product.price);
				}
				if(product.measure != undefined) {
					var measure = document.getElementById("measure");
					measure.setAttribute("value", product.measure);
				}
				if(product.manuf != undefined) {
					var manufacturer = document.getElementById("manufacturer");
					manufacturer.setAttribute("value", product.manuf);
				}
				if(product.keeper != undefined) {
					var keeper = document.getElementById("keeper");
					keeper.setAttribute("value", product.keeper);
				}
				if(product.productName != undefined) {
					var wname = document.getElementById("wholename");
					wname.setAttribute("value", product.productName);
				}
			}
		},
		error : function() {
			alert("请求失败");
		}
	});
}