var rowNum = 20;
$(document).ready(function () {
    $("a[id^=href]").click(function(){
        window.location.href=this.id.replace('href_','')+'.jsp';
    });
//    $('#allowcontact').bootstrapToggle({
//      on: '是',
//      off: '否'
//    });            
    if($("#role").val()==='2'){//組長
        $('#queryType').append('<option value="team">4.組內所有資料</option>');
        $('#divQueryName').show();
        $('#export').prop('disabled', false);
        rowNum = 5000;
    }
    if($("#role").val()==='5'){//管理者
        $('#queryType').append('<option value="team">4.組內所有資料</option>');
        $('#queryType').append('<option value="all">5.不分組所有資料</option>');
        $('#divQueryName').show();
        $('#export').prop('disabled', false);
        rowNum = 5000;
    }
    if (localStorage.eventid) {
        $("#eventid").val(localStorage.getItem("eventid"));
    }
    if (localStorage.queryType) {
        $("#queryType").val(localStorage.getItem("queryType"));
    }
    $("#btnSave").click(function(){
        updateReqTickData();
        //$(dialogId).hide();
    });
    $("#btnDelete").click(function(){
        deleteReqTickData();
        //$(dialogId).hide();
    });
    $("#btnClose").click(function(){
        var dialogId = "#modal-update";
        $(dialogId).hide();
    });
    $("#allowcontact").click(function(){
        //alert($('#allowcontact').prop("checked")? 1:0);
    });
    $("#btnQry").click(function(){
        localStorage.setItem("eventid", $("#eventid").val());
        localStorage.setItem("queryType", $("#queryType").val());
        $("#QueryResult").jqGrid("GridUnload");
        Query();
        
     });
     //Query();
    $("#export").on("click", function(){    
        if($("#QueryResult").css('display')==='none'){
            alert('請先查詢再匯出');
            //Query();
            return;
        }
        $("#load_QueryResult").html(''); 
        $("#lui_QueryResult").html(''); 
        $("#QueryResult_rn").html(''); 
        var paggerHtml = $("#QueryResultpagger").html();
        $("#QueryResultpagger").html(''); 
        var blob = new Blob([document.getElementById('jqGridTable').innerHTML], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
        });
        $("#QueryResultpagger").html(paggerHtml); 
        var strFile = "索票資料-"+ $("#eventid").val()  +".xls";
        saveAs(blob, strFile);
        return false;
    
//            $("#QueryResult").jqGrid("exportToExcel",{
//            includeLabels : true,
//            includeGroupHeader : true,
//            includeFooter: true,
//            fileName : "export-"+'audience'+".xlsx",
//            maxlength : 80 // maxlength for visible string data
//        });
    }); 
});
function showButton (cellvalue, options, rowObject) {
    //return "<button type=\"button\" onclick=\"alert(options.taginc)\">修改</button>"; // 返回的html即為欄位中的樣式
    return "<button type=\"button\" onclick=\"alert("+ options +")\">修改</button>"; // 返回的html即為欄位中的樣式
}
function allowcontactFormat(cellvalue, options, rowObject) {
    return cellvalue==='0'?"不同意":"同意";
}
function tickidFormat(cellvalue, options, rowObject) {
    return cellvalue<10?("000"+cellvalue)
    :cellvalue<100?("00"+cellvalue)
    :cellvalue<1000?("0"+cellvalue)
    :cellvalue;
}
function seatTypeFormat(cellvalue, options, rowObject) {
    return cellvalue==='1'?"一般"
    :cellvalue==='2'?"<font color='purple'>貴賓</font>"
    :cellvalue==='3'?"<font color='pink'><b>親子</b></font>":"";
}
function friendTypeFormat(cellvalue, options, rowObject) {
    return cellvalue==='0'?"一般民眾":"伙伴或親友";
}
function confirmStatusFormat(cellvalue, options, rowObject) {
    return cellvalue==='-1'?"<font color='red'>請假</font>"
    :cellvalue==='0'?"未確認"
    :cellvalue==='1'?"<font color='green'>會出席</font>":"";
}
function presentStatusFormat(cellvalue, options, rowObject) {
    return cellvalue==='1'?"<font color='red'>V</font>":"";    
}

function updateReqTickData(){
    var confirmMsg = '請確認是否要修改資料';
    if(!confirm(confirmMsg)){
        return;
    }    
    var updateOtherTick = "false";
    //if(confirm('此場次同一索票人尚有其他'+ sameTicknameCnt-1 +'張索票資料，資料是否要一併修改？')){
    if(confirm('是否要一併更新此場次同一索票人的其他張索票資料？(依姓名+手機判斷)')){
        updateOtherTick = "true";        
    }    
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'updateRequestTick',
            taginc: $('#taginc').val(),
            eventid: $('#eventid').val(),
            procman: $('#procman').val(), 
            procaddr: $('#procaddr').val(),
            allowcontact: $('#allowcontact').prop("checked")? 1:0,
            seatType: $("input[name='seatType']:checked").val(),            
            friendType: $("input[name='friendType']:checked").val(),            
            confirmStatus: $("input[name='confirmStatus']:checked").val(),  
            tickname: $('#tickname').val(),
            ticktel: $('#ticktel').val(),
            tickmemo: $('#tickmemo').val(),            
            updateOtherTick: updateOtherTick            
        },
        async: false,
        success: function (data) { 
            var jo = JSON.parse(data.infoMsg);
            //alert(data.infoMsg);
            alert(jo.resultMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#lastUpdater").val(jo.lastUpdater);                
                $("#updatetime").val(jo.updatetime);                
                var dialogId = "#modal-update";
                $(dialogId).hide();
                $("#btnQry").click();                 
            }            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次、通知系統管理者，或是關閉畫面再重新操作!');
        }
    });
}

function deleteReqTickData(){
    var confirmMsg = '請確認是否要刪除資料？';
    if(!confirm(confirmMsg)){
        return;
    }    
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'deleteRequestTick',
            taginc: $('#taginc').val()
        },
        async: false,
        success: function (data) { 
            var jo = JSON.parse(data.infoMsg);
            alert(jo.resultMsg);
            if(data.infoMsg.indexOf('成功')>=0){  
                var dialogId = "#modal-update";
                $(dialogId).hide();
                $("#btnQry").click();                 
            }            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次或通知系統管理者!');
        }
    });
}

function getRequestTickData(){//TODO:取得確認狀態統計資料
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getConfirmStatData',
            statType:$("#statType").val(),
            queryType: $('#queryType').val(),
            confirmStatus: $('#confirmStatusType').val(),
            evid: $('#eventid').val()
            //eventid: $('#eventid').val(),            
        },
        async: false,
        success: function (data) {            
            alert(data.infoMsg);            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('取得確認狀態統計資料異常!');
        }
    });
    //return eval(reportData);
}

function Query() {     
    //getRequestTickData();
    var selectedGridRowId;
    //alert($(window).width());//980
    var bigScreen = false;
    if($(window).width()>=980){
        bigScreen = true;
    }
    if($('#eventid').val()===''){
        alert('請先選擇場次');return;
    }
    $("#QueryResult").show();
    $("#QueryResult").jqGrid(
        {
            url: 'QueryServlet',
            mtype: 'POST',
            datatype: "json",
            postData: {
                ajaxAction: 'getDataByStaffName',
                queryType: $('#queryType').val(),
                confirmStatus: $('#confirmStatusType').val(),
                evid: $('#eventid').val(),
                tickname: $('#queryName').val()//                
            },
            rownumbers: true, //是否顯示行號
            sortable: true,
            loadonce: true,
            //multiselect: true,
            pgbuttons: true,
            //pginput: true,
            pager: "#QueryResultpagger",
            rowNum: rowNum,
            rowList: [20, 50, 100, 200, 500, 1000, 5000],
            //rowList: [5000, 1000, 500, 50, 10],
            
            colNames: ["ID", "場編", "場次", "組別", 
                "票號", "發票人",  "索票人", "電話", "發票地", "備註", "建立者", "建立時間", "操作", 
                "電話調查", "修改者", "座位別", "觀眾類別", "確認狀態", "出席", "異動時間"],
            colModel: [{
                    name: "taginc",
                    index: "taginc",
                    align: 'center',
                    hidden:true
                }, {
                    name: "evid",
                    index: "evid",
                    align: 'center',
                    width: '60px',
                    hidden:true
                }, {
                    name: "event",
                    index: "event",
                    align: 'center',
                    width: '10px',
                    hidden:true
                }, {
                    name: "team",
                    index: "team",
                    align: 'center',
                    width: '8px',
                    hidden:true
                }, {               
                
                    name: "tickid",
                    index: "tickid",
                    align: 'center',
                    sorttype: "number",
                    formatter: tickidFormat,
                    width: '6px'
                }, {
                    name: "procman",
                    index: "procman",
                    align: 'center',
                    width: '7px' ,
                    edittype: 'select'
                }, {
                    name: "tickname",
                    index: "tickname",
                    align: 'center',
                    width: '10px'
                }, {
                    name: "ticktel",
                    index: "ticktel",
                    align: 'left',
                    width: '10px',
                    hidden: !bigScreen
                }, {    
                    name: "procaddr",
                    index: "procaddr",
                    align: 'left',
                    width: '9px',
                    hidden: !bigScreen
                }, {
                    name: "tickmemo",
                    index: "tickmemo",
                    align: 'left',
                    width: '8px',
                    hidden: !bigScreen
                }, {
                    name: "creator",
                    index: "creator",
                    align: 'center',
                    width: '7px',
                    hidden: !bigScreen
                }, {
                    name: "createtime",
                    index: "createtime",
                    width: '15px',
                    align: 'center',
                    formatter: 'date',
                    formatoptions: { srcformat: "ISO8601Long", newformat: "Y/m/d h:i" },
                    hidden: !bigScreen
                }, {
                    name: "test10",
                    index: "test10",
                    align: 'center',
                    width: '15px',
                    hidden: true,
                    formatter:showButton
                }, {
                    name: "allowcontact",
                    index: "allowcontact",
                    align: 'center',
                    width: '8px',
                    formatter: allowcontactFormat,
                    hidden: !bigScreen
                }, {
                    name: "lastUpdater",
                    index: "lastUpdater",
                    align: 'center',
                    width: '7px',
                    hidden: !bigScreen
                }, {
                    name: "seatType",
                    index: "seatType",
                    align: 'center',
                    width: '8px',
                    formatter: seatTypeFormat,
                    hidden: !bigScreen
                }, {
                    name: "friendType",
                    index: "friendType",
                    align: 'center',
                    width: '8px',
                    formatter: friendTypeFormat,
                    hidden: !bigScreen
                }, {
                    name: "confirmStatus",
                    index: "confirmStatus",
                    width: '10px',
                    align: 'center',
                    formatter: confirmStatusFormat,
                    hidden: false
                }, {
                    name: "presentStatus",
                    index: "presentStatus",
                    width: '6px',
                    align: 'center',
                    formatter: presentStatusFormat,
                    hidden: false
                }, {
                    name: "updatetime",
                    index: "updatetime",
                    align: 'center',
                    width: '15px',
                    formatter: 'date',
                    formatoptions: { srcformat: "ISO8601Long", newformat: "Y/m/d h:i" },
                    hidden: !bigScreen
                
                }],
            onCellSelect: function(rowid,e) {
                //alert("rowid=" + rowid  );
            },
            onSelectRow: function (id, status) {
                selectedGridRowId = id;
                //var row = $("#QueryResult").jqGrid('getRowData', id);
                showUpdateDialog(id);
                //alert(row.taginc);
            }
        }
                );
    $("#QueryResult").setGridWidth($(window).width() * 0.7);        
}

//跳出編輯視窗
function showUpdateDialog(rowId) {
    //alert(rowId);
    var dialogId = "#modal-update";
    $(dialogId).show();
    //$(dialogId).dialog('open');
    
    var row = $("#QueryResult").jqGrid('getRowData', rowId);
    $("#taginc").val(row.taginc);
    $("#event").val(row.event);
    $("#tickid").val(row.tickid);
    $("#procman").val(row.procman);
    $("#tickname").val(row.tickname);
    $("#ticktel").val(row.ticktel);
    $("#tickmemo").val(row.tickmemo);
    $("#procaddr").val(row.procaddr);
    $("#creator").val(row.creator);
    $("#updatetime").val(row.updatetime);
    $("#createtime").val(row.createtime);
    $("#lastUpdater").val(row.lastUpdater);
    
    //alert(row.seatType +'-' + row.seatType.indexOf('貴賓'));
    var seatTypeValue = row.seatType==='一般'?"1":row.seatType.indexOf('貴賓')>=0?"2":"3";//先用formatter轉成中文了，要再轉回來數值
    var friendTypeValue = row.friendType.indexOf('友')>=0?"1":"0";//先用formatter轉成中文了，要再轉回來數值
    var confirmStatusValue = row.confirmStatus.indexOf('請假')>=0?"-1":row.confirmStatus==='未確認'?"0":"1";
    var allowcontactValue = row.allowcontact==='不同意'?"0":"1";
    
    $("input[name='seatType'][value="+ seatTypeValue +"]").click();
    $("input[name='friendType'][value="+ friendTypeValue +"]").click();
    //alert('seatTypeValue=' + seatTypeValue);
    //alert('friendTypeValue=' + friendTypeValue);
    //return;
    $("input[name='confirmStatus'][value="+confirmStatusValue+"]").click();
      
    if(allowcontactValue==='1'){//
        $('#allowcontact').click();                
    }else{
        $('#allowcontact').prop("checked",false);
    }    
    return; 
    //$(dialogId).dialog('open');
}