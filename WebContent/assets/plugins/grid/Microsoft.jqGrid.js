
//定義jqGrid的預設值及Function Event
$(function () {
    $.extend($.jgrid.defaults, {
        //==========屬性處理================
        multiselect: false,//不多選
        rowNum: 10,
        rowList: [10, 20, 30],
        toppager: false,
        viewrecords: true,
        rownumbers: true,
        autowidth: true,
        altRows: true,
        altclass: 'jqgrow2',
        height: "100%",//高度隨筆數而定
        rownumWidth: 30,//RowNum寬度 default:25 http://goo.gl/y8BqR
        //==========Event處理===============
        //錯誤處理
        loadError: Microsoft.UI.Grid.loadError,
        serializeGridData: Microsoft.UI.Grid.serializeGridData,
        loadComplete: Microsoft.UI.Grid.loadComplete,
        gridComplete: gridComplete
    });
})

//完成後的處理
function gridComplete(mythis) {
    if (mythis == null) { mythis = this; };
    var mygrid = $(mythis);
    //讓每個row下的cell 都有colname(供RWD使用)
    var cm = mygrid.jqGrid("getGridParam", "colNames");
    var trs = mygrid.find('tr');
    $.each(trs, function (tri, tritem) {
        var tds = $(tritem).find('td');
        if ($(tritem).hasClass("jqgfirstrow"))//排除grid第一筆訂寬度用的
            return;
        $.each(tds, function (tdi, tditem) {
            var mytitle = cm[tdi].replace("<br/>", "\n");
            $(tditem).attr('mycolname', mytitle);

            //讓RWD時的Header可以自動調高度！//註：因加html至Data欄裡面！所以要小心
            var dirtyContent = "";
            var mytitle2 = cm[tdi].replace("<br/>", "");
            if (mytitle2.Blength() > mytitle2.length && mytitle2.Blength() > 10) {
                for (var ii = 1; ii < (mytitle2.Blength() / 10) ; ii++)
                    dirtyContent += "\n";
                $(tditem).append(dirtyContent);
            }
            //讓RWD時的Header可以自動調高度！

            if (cm[tdi].indexOf("<input") >= 0) {
                $(tditem).attr('mycolname', "");
            }
        });
    });
    //讓它再執行一次Resize(可移除body出現scrollbar的問題)
    jqGridResize();
    //若資料是空值則Header出現x-overflow:auto
    var hdivgrid = mygrid.parent().parent().parent().find(".ui-jqgrid-hdiv");//找到header的Div
    if (mythis.p.reccount === 0) {
        hdivgrid.addClass("ui-jqgrid-hdivScrollX");
    }
    else {
        hdivgrid.removeClass("ui-jqgrid-hdivScrollX");
    }
}

//讓grid它重新調整寬度(電腦版)
var myresizeid;
$(function () {
    //因受到sideMenu的縮放影響導至它width算不準，所以要Delay 300 ms再處理grid
    $(window).bind('resize',
        function () {
            clearTimeout(myresizeid);
            myresizeid = setTimeout(jqGridResize, 300);
        });
})

function jqGridResize() {
    var grids = $(".ui-jqgrid-btable,.ui-pg-table,.ui-jqgrid-htable");
    $.each(grids, function (i, item) {
        //排外不自動resize
        if (//$(item).parents(".dialogpage").length > 0 ||
            $(item).parents(".ExcludeGridResize").length > 0) {

        }
            //在Modal視窗內的grid
        else if ($(item).parents(".modal-body:last").length > 0) {
            var nowwidth = $(item).parents(".modal-body:last").width();//因它有2個modal-body,要選最後一個
            if (nowwidth > 0) {
                $(item).jqGrid('setGridWidth', nowwidth - 1);
                $(".ui-jqgrid-btable").width(nowwidth - 2);//jqgrid body再減1才不會有Srollbar
            }
        }
        else {
            //最大寬度取決於div contentBlock
            //因pc.css有定義
            //( in screen and (max-width: 1200px) and (min-width: 533px))=980 所有要限制grid大小
            //( in screen and (max-width: 1500px) and (min-width: 1200px))=1200
            //screen and (min-width: 1501px) =1500
            var nowwidth = $(window).width(); //$(item).width();
            var contentwidth =$(window).width()-50;// $(".body-content").width();
            var bodywidth = $(window).width()-50;//$(".panel-body").width();
            var gridwidth = $(item).jqGrid('getGridParam', 'width'); // get current width
            if (bodywidth != null && bodywidth < contentwidth)
                contentwidth = bodywidth;
            //contentwidth = contentwidth == null ? $("#contentBlock").width() : contentwidth;
            //若contentwidth比較小，則採用它的
            nowwidth = (contentwidth <= nowwidth) ? contentwidth : nowwidth;
            nowwidth = (nowwidth <= 980) ? nowwidth :
                (nowwidth > 980 && nowwidth < 1200) ? contentwidth :
                (nowwidth > 1200 && nowwidth < 1500) ? contentwidth : contentwidth
            ;
            if (nowwidth <= 533)
                nowwidth = nowwidth - 20;//在RWD下要減20才可以
            $(item).jqGrid('setGridWidth', nowwidth - 1);
            if (nowwidth <= 533) {
                $(".ui-jqgrid-btable").width(nowwidth - 2);//jqgrid body再減1才不會有Srollbar //note非手機格式下避免自調整width
            }
            console.log(nowwidth + "," + contentwidth);
        }
    });
}