$(document).ready(function () {
//    $('#allowcontact').bootstrapToggle({
//      on: '是',
//      off: '否'
//    });
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
});
function showButton (cellvalue, options, rowObject) {
    //return "<button type=\"button\" onclick=\"alert(options.taginc)\">修改</button>"; // 返回的html即為欄位中的樣式
    return "<button type=\"button\" onclick=\"alert("+ options +")\">修改</button>"; // 返回的html即為欄位中的樣式
}
function allowcontactFormat(cellvalue, options, rowObject) {
    return cellvalue==='0'?"不同意":"同意";
}
function seatTypeFormat(cellvalue, options, rowObject) {
    return cellvalue==='1'?"一般"
    :cellvalue==='2'?"<font color='purple'>貴賓</font>"
    :cellvalue==='3'?"<font color='pink'><b>親子</b></font>":"";
}
function confirmStatusFormat(cellvalue, options, rowObject) {
    return cellvalue==='-1'?"<font color='red'>請假</font>"
    :cellvalue==='0'?"未確認"
    :cellvalue==='1'?"<font color='green'><b>確認出席</b></font>":"";
}

function updateReqTickData(){
    var confirmMsg = '請確認是否要修改資料';
    if(!confirm(confirmMsg)){
        return;
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
            confirmStatus: $("input[name='confirmStatus']:checked").val(),  
            tickname: $('#tickname').val(),
            ticktel: $('#ticktel').val(),
            tickmemo: $('#tickmemo').val()            
        },
        async: false,
        success: function (data) { 
            var jo = JSON.parse(data.infoMsg);
            //alert(data.infoMsg);
            alert(jo.resultMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#lastUpdater").val(jo.lastUpdater);                
                $("#updatetime").val(jo.updatetime);                
                $("#btnQry").click();                 
            }            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次或通知系統管理者!');
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

function Query() {
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
                evid: $('#eventid').val()
            },
            rownumbers: true, //是否顯示行號
            sortable: true,
            loadonce: true,
            //multiselect: true,
            //pgbuttons: true,
            //pginput: true,
            pager: "#QueryResultpagger",
            rowList: [10, 20, 50],
            colNames: ["ID", "場編", "場次", "組別", "發票地",
                "票號", "發票人",  "索票人", "電話", "備註", "資料建立者", "建立時間", "操作", 
                "滿意度調查", "最後修改者", "座位類別", "確認狀態", "異動時間"],
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
                    name: "procaddr",
                    index: "procaddr",
                    align: 'center',
                    width: '10px',
                    hidden:true
                }, {
                    name: "tickid",
                    index: "tickid",
                    align: 'center',
                    width: '8px'
                }, {
                    name: "procman",
                    index: "procman",
                    align: 'center',
                    width: '10px' ,
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
                    name: "tickmemo",
                    index: "tickmemo",
                    align: 'left',
                    width: '15px',
                    hidden: !bigScreen
                }, {
                    name: "creator",
                    index: "creator",
                    align: 'center',
                    width: '10px',
                    hidden: !bigScreen
                }, {
                    name: "createtime",
                    index: "createtime",
                    width: '20px',
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
                    width: '12px',
                    formatter: allowcontactFormat,
                    hidden: !bigScreen
                }, {
                    name: "lastUpdater",
                    index: "lastUpdater",
                    align: 'center',
                    width: '10px',
                    hidden: !bigScreen
                }, {
                    name: "seatType",
                    index: "seatType",
                    align: 'center',
                    width: '8px',
                    formatter: seatTypeFormat,
                    hidden: !bigScreen
                }, {
                    name: "confirmStatus",
                    index: "confirmStatus",
                    width: '8px',
                    formatter: confirmStatusFormat,
                    hidden: false
                }, {
                    name: "updatetime",
                    index: "updatetime",
                    align: 'center',
                    width: '20px',
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
    
    var seatTypeValue = row.seatType==='一般'?"1":row.seatType==='貴賓'?"2":"3";//先用formatter轉成中文了，要再轉回來數值
    var confirmStatusValue = row.confirmStatus==='請假'?"-1":row.confirmStatus==='未確認'?"0":"1";
    var allowcontactValue = row.allowcontact==='不同意'?"0":"1";
    
//    $('#allowcontact').prop("checked",true);
//    $('#allowcontact').click();        
    //alert(allowcontactValue);
    $("input[name='seatType'][value="+ seatTypeValue +"]").click();
    $("input[name='confirmStatus'][value="+confirmStatusValue+"]").click();
    //$("input[name='allowcontact'][value="+allowcontactValue+"]").prop("checked",true);
    //$("input[name='allowcontact'][value="+allowcontactValue+"]").click();
      
    if(allowcontactValue==='1'){//
        //alert('checked');
        //$('#allowcontact').prop("checked",true);
        $('#allowcontact').click();                
    }else{
        $('#allowcontact').prop("checked",false);
    }
    
    return; 
    $(dialogId).dialog('open');
}