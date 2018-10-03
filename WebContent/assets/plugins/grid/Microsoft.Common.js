/*************************************************
Summary：列出以下定義的Function
Accordion.Model(active)             //物件
Microsoft.SelectItem(hidden,display)   //物件
Microsoft.Common.WindowOpen(url, openName)
Microsoft.Common.ClearClick(conId)             //清元件下的輸入資料
Microsoft.Common.Ajax(postUrl, params, callback)                   //原func:POSTToServer
Microsoft.Common.Ajax(postUrl, params, callback, errorcallback)  //原func:POSTToServer
Microsoft.Common.Ajax(postUrl, params, callback, errorcallback,useMask)  //原func:POSTToServer
Microsoft.Common.AjaxSync(postUrl, params) //有回傳值
Microsoft.Common.AjaxSync(postUrl, params, okcallback)//有回傳值
Microsoft.Common.AjaxSync(postUrl, params, okcallback, errorcallback)//有回傳值
Microsoft.Common.AjaxSync(postUrl, params, okcallback, errorcallback,useMask)//有回傳值
Microsoft.Common.SkipJsonNull(json)
String.Blength()
Microsoft.UI.Init.PostBackWithMask()
Microsoft.UI.Init.TabUniqueId(fId)
Microsoft.UI.Init.TabUniqueId()
Microsoft.UI.Init.DatePicker()
Microsoft.UI.Init.DatePicker.PadDateFormat(dateText, fmt)
Microsoft.UI.Init.GridReload()
Microsoft.UI.Grid.serializeGridData(data)
Microsoft.UI.Grid.loadComplete(data)
Microsoft.UI.Grid.gridExport(sender)
Microsoft.UI.Grid.loadError(xhr, status, error)
*************************************************/

/*************************************************\
將clsss套用autoCaseUpper的自動轉大寫
\*************************************************/
$(document).ready(function () {
    var funcAutoCaseUpper = function (elem) {
        elem.value = elem.value.toUpperCase();
    };
    $("input.autoCaseUpper:text, textarea.autoCaseUpper").change(function () {
        funcAutoCaseUpper(this);
    }).each(function (i, n) {
        funcAutoCaseUpper(n);
    });
});


/*************************************************
// addMethod - By John Resig (MIT Licensed)
// http://ejohn.org/blog/javascript-method-overloading/
// 加入Extend Method，且達到overloading功能
*************************************************/
function addMethod(object, name, fn) {
    var old = object[name];
    object[name] = function () {
        if (fn.length == arguments.length)
            return fn.apply(this, arguments);
        else if (typeof old == 'function')
            return old.apply(this, arguments);
    };
}

//*************************************************
//*************************************************
//  Microsoft Accordion Function 
//*************************************************
//*************************************************
var Accordion = function () { };
/*************************************************
擴充：增加 Accordion.Model 函式(物件)
*************************************************/
addMethod(Accordion, "Model", function (active) {
    this.openActive = active;
});

//*************************************************
//*************************************************
//  Microsoft Common Function 
//*************************************************
//*************************************************
var Microsoft = function () { };
$.extend(Microsoft, {
    Common: function () { }
});
$.extend(Microsoft.Common, {
    Hello: function (name) {
        alert("hello" + name);
    }
});
/*************************************************
擴充：增加 Microsoft.SelectItem(hidden, display) 函式(物件)
*************************************************/
addMethod(Microsoft, "SelectItem", function (hidden, display) {
    this.hidden = hidden;
    this.display = display;
});
/*************************************************
擴充：增加 Microsoft.Common.WindowOpen 函式
*************************************************/
addMethod(Microsoft.Common, "WindowOpen", function (url, openName) {
    //處理openName不合法字串//空白
    openName = openName.replace(/\s+/g, '').replace(/\-/g, '');//移 空白 及 -
    window.open(url, openName);
});
/*************************************************
擴充：增加 Microsoft.Common.ClearClick 函式
清除指定的Control ID的輸入資訊
*************************************************/
addMethod(Microsoft.Common, "ClearClick", function (conId) {
    $('#' + conId).find('input[type=text]').val('');
    $('#' + conId).find('input[type=radio]').attr('checked', false);
    $('#' + conId).find('input[type=checkbox]').attr('checked', false);
    $('#' + conId).find('select').val("");
});
/*************************************************
擴充：增加 Microsoft.Common.Ajax 函式
postUrl,    //執行POST URL00097FA000558
params,     //參數物件(JSON object)
callback,   //成功之callback function
return      //無
範例:
Microsoft.Common.Ajax(url, { id: ids[0] },function(data){});
*************************************************/
addMethod(Microsoft.Common, "Ajax", function (postUrl, params, callback) {
    Microsoft.Common.Ajax(postUrl, params, callback, null, true);
});
addMethod(Microsoft.Common, "Ajax", function (postUrl, params, callback, errorcallback) {
    Microsoft.Common.Ajax(postUrl, params, callback, errorcallback, true);
});
/*************************************************
擴充：增加 Microsoft.Common.Ajax 函式
postUrl,    //執行POST URL
params,     //參數物件(JSON object)
callback,   //成功之callback function
errorcallback, //失敗之callback function
useMask     //是否使用遮罩 [true/false]=[是/否]
return      //無
*************************************************/
addMethod(Microsoft.Common, "Ajax", function (postUrl, params, callback, errorcallback, useMask) {
    if ($.type(params) != "string") {
        if (params == null) {
            params = {};
        }
        if ((params.tabUniqueId == 'undefined') || (params.tabUniqueId == null) || params.tabUniqueId == '') {
            params.tabUniqueId = $('#tabUniqueId').val();
        }
        params.usemask = useMask;
    }
    //10/25 移至 jquery.unobtrusive-ajax-MicrosoftExtend.js
    //$.ajaxSettings.traditional = true;//處理%5B%5D的問題 http://goo.gl/dnkUj0  update:加這會有錯
    $.ajax({
        type: 'post',
        url: postUrl,
        data: params,
        datatype: 'json',
        beforeSend: function (xhr, settings) {
            //settings.data = settings.data.replace(/%5D%5B(.+?)%5D=/g, "%5D.$1=");//將XXX[0][QQQ]轉為XXX[0].QQQ
        },
        success: function (data, status, xhr) {
            //$(document).ajaxStop($.unblockUI);//除遮罩
            //if (data != null && data.Redirect) {
            //    window.location.href = data.Redirect;
            //    return;
            //}
            //var overlimitrow = xhr.getResponseHeader("OverLimitRow");//超過限資料筆數
            //var showmessage = xhr.getResponseHeader("AppendMessage");//顯示訊息
            //if (overlimitrow != null) { appendMessage(overlimitrow); }
            //if (showmessage != null) { appendMessage(showmessage); }
            if (callback != null) { callback(data); }
        },
        error: function (xhr, status, data) {
            Microsoft.Common.ErrorHandle(xhr, status, data, errorcallback);
        }
    });
});

/*************************************************
擴充：增加 Microsoft.Common.AjaxSync 函式
功能:同步取到呼叫後的結果
postUrl,    //執行POST URL
params,     //參數物件(JSON object)
callback,   //成功之callback function
return      //呼叫ajax的結果
範例:
var result = Microsoft.Common.AjaxSync(url, { id: ids[0] });
*************************************************/
addMethod(Microsoft.Common, "AjaxSync", function (postUrl, params) {
    return Microsoft.Common.AjaxSync(postUrl, params, null, null, true);
});
addMethod(Microsoft.Common, "AjaxSync", function (postUrl, params, okcallback) {
    return Microsoft.Common.AjaxSync(postUrl, params, okcallback, null, true);
});
addMethod(Microsoft.Common, "AjaxSync", function (postUrl, params, okcallback, errorcallback) {
    return Microsoft.Common.AjaxSync(postUrl, params, okcallback, errorcallback, true);
});
addMethod(Microsoft.Common, "AjaxSync", function (postUrl, params, okcallback, errorcallback, useMask) {
    var result;

    if ($.type(params) != "string") {
        if (params == null) {
            params = {};
        }
        if ((params.tabUniqueId == 'undefined') || (params.tabUniqueId == null) || params.tabUniqueId == '') {
            params.tabUniqueId = $('#tabUniqueId').val();
        }
        params.usemask = useMask;
    }
    //$.ajaxSettings.traditional = true;//處理%5B%5D的問題 http://goo.gl/dnkUj0     update:加這會有錯
    $.ajax({
        type: 'post',
        data: params,
        url: postUrl,
        async: false,
        beforeSend: function (xhr, settings) {
            //settings.data = settings.data.replace(/%5D%5B(.+?)%5D=/g, "%5D.$1=");//將XXX[0][QQQ]轉為XXX[0].QQQ
        },
        success: function (data, status, xhr) {
            result = data;
            if (okcallback != null) { okcallback(data); }
        },
        error: function (xhr, status, data) {
            Microsoft.Common.ErrorHandle(xhr, status, data, errorcallback);
        }
    });
    return result;
});

/*************************************************
擴充：增加 Microsoft.Common.ErrorHandle 函式
功能:Ajax的錯誤處理
xhr:
status:
data:
callback:
範例:
var result = Microsoft.Common.ErrorHandle(xhr, status, data,errorcallback);
*************************************************/
addMethod(Microsoft.Common, "ErrorHandle", function (xhr, status, data, errorcallback) {
    $(document).ajaxStop($.unblockUI);//除遮罩
    var showmessage = xhr.getResponseHeader("AppendMessage");//顯示訊息
    if (showmessage != null) {
        if ($.isFunction(window.appendMessage)) {
            appendMessage(showmessage);//加上訊息於表頭
        }
    }
    if (errorcallback != null) { errorcallback(data); }
});
/*************************************************
擴充：增加 Microsoft.Common.SkipJsonNull 函式
json        //為JSON物件或JSON格式的字串
return      //json物件
*************************************************/
addMethod(Microsoft.Common, "SkipJsonNull", function (json) {
    var parseString = json;
    if (typeof (json) == "object") {
        parseString = JSON.stringify(json);
    }
    var result = JSON.parse(parseString, function (key, value) { if (value != null) { return value; } });
    return result;
});

/*************************************************
擴充：增加 Microsoft.Common.ParseJsonNull 函式
json        //為JSON物件或JSON格式的字串
return      //json物件
*************************************************/
addMethod(Microsoft.Common, "ParseJsonNull", function (json) {
    var parseString = json;
    if (typeof (json) == "object") {
        parseString = JSON.stringify(json);
    }
    var result = JSON.parse(parseString, function (key, value) { if (value != null) { return value; } else { return ""; } });
    return result;
});

/*************************************************
擴充：增加 String.Blength 函式
功能：string.Blength() 傳回字串的byte長度 (判斷出中英文)
範例：
var str = "小弟是Y2J";
//中文字(6) + 英數字(3) = 總共(9)
alert("byte數:"+str.Blength());
*************************************************/
String.prototype.Blength = function () {
    var arr = this.match(/[^\x00-\xff]/ig);
    return arr == null ? this.length : this.length + arr.length;
}



//*************************************************
//擴充：增加 Microsoft.UI 函式
//*************************************************
$.extend(Microsoft, {
    UI: {}
});
//*************************************************
//擴充：增加 Microsoft.UI.Init 函式
//*************************************************
$.extend(Microsoft.UI, {
    Init: {}
});
//*************************************************
//擴充：增加 Microsoft.UI.DatePicker 函式
//*************************************************
$.extend(Microsoft.UI.Init, {
    DatePicker: {}
});
//*************************************************
//擴充：增加 Microsoft.UI.Grid 函式
//*************************************************
$.extend(Microsoft.UI, {
    Grid: {}
});
/*************************************************
擴充：增加 Microsoft.UI.Init.TabUniqueId 函式
複製 hidden TabUniqueId 到各個 form 內
*************************************************/
addMethod(Microsoft.UI.Init, "TabUniqueId", function () {
    Microsoft.UI.Init.TabUniqueId(null);
});
addMethod(Microsoft.UI.Init, "TabUniqueId", function (fId) {
    if ($('#tabUniqueId').exists()) {
        var sel = 'form';
        if (fId !== 'undefined' && fId != null && fId != '') {
            sel = 'form#' + fId;
        }
        $(sel).each(function () {
            if (!$(this).find('input[name=tabUniqueId]').exists()) {
                $('<input>').attr({
                    type: 'hidden',
                    id: 'tabUniqueId',
                    name: 'tabUniqueId',
                    value: $('#tabUniqueId').val()
                }).appendTo($(this));
            }
        });
    }
});
/*************************************************
擴充：增加 Microsoft.UI.Init.PostBackWithMask 函式
form PostBack 或是用 location.href change URL 時, 增加遮罩顯示 busy 功能
*************************************************/
addMethod(Microsoft.UI.Init, "PostBackWithMask", function () {
    $(document).on("submit", "form", function (event) {
        var loading2 = $('#pageLoading');
        if (loading2.length > 0) {
            $.blockUI({
                message: loading2,
                css:
                    {
                        border: 'none',
                        backgroundColor: 'none'
                    },
                overlayCSS: { opacity: '0' }
            });
        }
    });
    $(window).on('beforeunload', function (event) {
        var loading2 = $('#pageLoading');
        if (loading2.length > 0) {
            $.blockUI({
                message: loading2,
                css:
                    {
                        border: 'none',
                        backgroundColor: 'none'
                    },
                overlayCSS: { opacity: '0' }
            });
            setTimeout(hideMask, document.readyState ? 10 : 1000);
        }
    });
    var hideMask = document.readyState ? function () {
        setTimeout(function () {
            if (document.readyState != 'loading') {
                $.unblockUI();
            }
        }, 300);
    } : function () {
        $.unblockUI();
    }
});
/*************************************************
擴充：增加 Microsoft.UI.Init.DatePicker 函式
初使日期元件
*************************************************/
addMethod(Microsoft.UI.Init, "DatePicker", function () {
    $('.Wdate').each(function () {
        if ($(this).attr('type') === 'text') {
            var fmt = $(this).attr('dateFmt');
            var maxdate = $(this).attr('maxDate');
            var mindate = $(this).attr('minDate');
            var disPop = ($(this).attr('disablePop') === "true");
            var onpicked = $(this).attr('onpicked');
            var myvel = $(this).attr('vel');
            if (typeof (fmt) === 'undefined' || fmt === null || fmt === '') {
                fmt = 'yyyy/MM/dd'
            }
            var dateParams = { dateFmt: fmt, disablePop: disPop, vel: myvel, realDateFmt: "yyyy/MM/dd" };
            var dateParams2 = { dateFmt: fmt, disablePop: true, vel: myvel, realDateFmt: "yyyy/MM/dd" };
            //補上日期區間限制設定
            if (typeof (maxdate) != 'undefined' && maxdate != null && maxdate != '') {
                dateParams.maxDate = maxdate;
                dateParams2.maxDate = maxdate;
            }
            if (typeof (mindate) != 'undefined' && mindate != null && mindate != '') {
                dateParams.minDate = mindate;
                dateParams2.minDate = mindate;
            }
            //觸發事件
            if (typeof (onpicked) != 'undefined' && onpicked != null && onpicked != '') {
                dateParams.onpicked = onpicked;
                dateParams2.onpicked = onpicked;
            }
            $(this).on('click', function () {
                WdatePicker(dateParams);
            });
            $(this).on('focus', function () {
                WdatePicker(dateParams2);
            });
        }
    });
});

/*************************************************
擴充：增加 Microsoft.UI.Init.DatePicker.PadDateFormat 函式
日期文字補上分隔字元, 並檢查格式是否合法
*************************************************/
addMethod(Microsoft.UI.Init.DatePicker, "PadDateFormat", function (a) {
    return Microsoft.UI.Init.DatePicker.PadDateFormat(a, 'yyy/MM/dd');
});

addMethod(Microsoft.UI.Init.DatePicker, "PadDateFormat", function (a, fmt) {
    if ((a == null) || (a == undefined) || (a == '')) {
        return "";
    }
    var rValue = '';
    var re1 = new RegExp('/', 'g');
    var re2 = new RegExp('-', 'g');
    var n = a.replace(re1, '').replace(re2, '');
    if ((fmt == null) || (fmt == undefined) || (fmt == '')) {
        fmt = 'yyy/MM/dd';
    }
    if (fmt == 'yyy/MM/dd') {
        if (n.length == 7) {
            rValue = n.substr(0, 3) + '/' + n.substr(3, 2) + '/' + n.substr(5, 2);
        }
        if (n.length == 6) {
            rValue = '0' + n.substr(0, 2) + '/' + n.substr(2, 2) + '/' + n.substr(4, 2);
        }
        var y = parseInt(rValue.substr(0, 3), 10) + 1911;
        var m = parseInt(rValue.substr(4, 2), 10) - 1;
        var d = parseInt(rValue.substr(7, 2), 10);
        var x = new Date(y, m, d);
        if (isNaN(x.getTime())) {
            return '';
        }
        if (y == x.getFullYear() && m == x.getMonth() && d == x.getDate()) {
            return rValue;
        }
        return '';
    }
    else if (fmt == 'yyyy/MM/dd') {
        if (n.length == 8) {
            rValue = n.substr(0, 4) + '/' + n.substr(4, 2) + '/' + n.substr(6, 2);
            var y = parseInt(n.substr(0, 4), 10);
            var m = parseInt(n.substr(4, 2), 10) - 1;
            var d = parseInt(n.substr(6, 2), 10);
            var x = new Date(y, m, d);
            if (isNaN(x.getTime())) {
                return '';
            }
            if (y == x.getFullYear() && m == x.getMonth() && d == x.getDate()) {
                return rValue;
            }
        }
        return '';
    }
    return rValue;
});

/*************************************************
擴充：增加 Microsoft.UI.Init.GridReload 函式
本頁面是明細視窗, 重新查詢前一頁面
*************************************************/
addMethod(Microsoft.UI.Init, "GridReload", function () {
    if ($("input[id='gridReload'][type='hidden']").length > 0) {
        var openerClass = typeof (window.opener);
        if (openerClass == 'object') {
            var o = $("input[id*='Query'][value*='查詢']:visible", window.opener.document);
            if (o.length <= 0) {
                openerClass = typeof (window.opener.opener);
                if (openerClass == 'object') {
                    o = $("input[id*='Query'][value*='查詢']:visible", window.opener.opener.document);
                    if (o.length <= 0) {
                        openerClass = typeof (window.opener.opener.opener);
                        if (openerClass == 'object') {
                            o = $("input[id*='Query'][value*='查詢']:visible", window.opener.opener.opener.document);
                        }
                    }
                }
            }
            if (o.length > 0) {
                $(o[0]).trigger('click');
            }
        }
    }
});
/*************************************************
擴充：增加 Microsoft.UI.Grid.serializeGridData 函式
jqGrid Binding 資料之前, 先將 tabUniqueId 加進 postData, 否則後端 catch 會抓不到資料
*************************************************/
addMethod(Microsoft.UI.Grid, "serializeGridData", function (data) {
    var newPostData = $.extend({}, data, {
        tabUniqueId: $('#tabUniqueId').val(),
        exportType: "csv",
        modelName: '',
        dataColNames: '',
        displayColNames: ''
    });
    return newPostData;
});
/*************************************************
擴充：增加 Microsoft.UI.Grid.loadComplete 函式
jqGrid Binding 資料完成後, 將後續 jqGrid 功能上須要的資料暫存到 hidden
*************************************************/
addMethod(Microsoft.UI.Grid, "loadComplete", function (data) {
    //處理Timeout的問題
    if (data != null && data.Redirect) {
        window.location.href = data.Redirect;
    }
    var pName = $(this).attr('id') + '_expName';
    if (!$('#' + pName).exists()) {
        $('<input>').attr({
            type: 'hidden',
            id: pName,
            name: pName
        }).appendTo('#hValueBar');
    }
    if ((typeof (data.exportParam) != 'undefined') && (data.exportParam != null)) {
        $('#' + pName).val(data.exportParam);
    }
});
/*************************************************
擴充：增加 Microsoft.UI.Grid."gridExport" 函式
jqGrid 匯出資料成 csv 檔
*************************************************/
addMethod(Microsoft.UI.Grid, "gridExport", function (sender) {
    var grid = this;
    var reccount = $(grid).getGridParam('reccount');
    if (typeof (reccount) === 'undefined' || reccount === null || reccount === 0) {
        grid = sender;
        reccount = $(grid).getGridParam('reccount');
        if (typeof (reccount) === 'undefined' || reccount === null || reccount === 0) {
            return;
        }
    }
    var dName = new Array();
    var rName = new Array();
    var fName = $(grid).getGridParam("colNames").slice(0);
    $.each($(grid).getGridParam("colModel"), function (index, data) {
        // 第一欄.行號.不匯出
        if (index == 0) {
        }
            // 隱藏的欄位/不匯出
        else if (data.hidden == true) {
        }
            // 若是欄位名稱的內容是 html(Ex:checkbox選取欄位).不匯出.
        else if (/<[a-z][\s\S]*>/i.test(fName[index]) == true) {
        }
        else {
            rName.push((data.symbol == null ? "" : data.symbol) + data.name);
            dName.push(fName[index]);
        }
    });
    var pName = '#' + $(grid).attr('id') + '_expName';
    pData = Microsoft.UI.Grid.serializeGridData($(grid).getGridParam('postData'));
    pData.modelName = $(pName).val().split(':')[0];
    pData.saveKeyName = $(pName).val().split(':')[1];
    pData.dataColNames = rName.join(',');
    pData.displayColNames = dName.join(',');
    $(grid).setGridParam({ postData: pData });
    $(grid).excelExport({ url: ROOTURL + 'Assistant/export' });
    setTimeout(function () { $.unblockUI(); }, 1000);//20141023-因Grid匯出時的blockUI無法關除，故於這裡為Grid匯出時特別處理

});
/*************************************************
擴充：增加 Microsoft.UI.Grid.loadError 函式
針對grid的錯誤做統一的處理
*************************************************/
addMethod(Microsoft.UI.Grid, "loadError", function (xhr, status, error) {
    var msgArray = [];
    var showmessage = xhr.getResponseHeader("AppendMessage");//顯示訊息
    if (showmessage == null) { showmessage = ""; }
    if (showmessage != "") {
        msgArray.push(showmessage);
        if ($.isFunction(window.appendMessage)) {
            appendMessage(msgArray);//加上訊息於表頭
        }
    }
    $(document).ajaxStop($.unblockUI);
});
//*************************************************
//  把form資料序列化為json物件，以原本的.serializeArray()來改寫
//  $("form").serializeJson();
//*************************************************
(function ($) {
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
    $.fn.exists = function () {
        return this.length !== 0;
    }
})(jQuery);