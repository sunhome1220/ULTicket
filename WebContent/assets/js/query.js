$(document).ready(function () {
    if (localStorage.eventid) {
        $("#eventid").val(localStorage.getItem("eventid"));
    }
    if (localStorage.queryType) {
        $("#queryType").val(localStorage.getItem("queryType"));
    }
    $("#btnClose").click(function(){
        var dialogId = "#modal-update";
        $(dialogId).hide();
    });
    $("#btnQry").click(function(){
        localStorage.setItem("eventid", $("#eventid").val());
        localStorage.setItem("queryType", $("#queryType").val());
        $("#QueryResult").jqGrid("GridUnload");
        Query();
        
     });
     Query();
});
function showButton (cellvalue, options, rowObject) {
    //return "<button type=\"button\" onclick=\"alert(options.taginc)\">修改</button>"; // 返回的html即為欄位中的樣式
    return "<button type=\"button\" onclick=\"alert("+ options +")\">修改</button>"; // 返回的html即為欄位中的樣式
}

function Query() {
    var selectedGridRowId;
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
                "票號", "發票人",  "索票人姓名", "電話", "備註", "登錄人", "登錄時間", "操作"],
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
                    editable: true,
                    edittype: 'select'
                }, {
                    name: "tickname",
                    index: "tickname",
                    align: 'center',
                    width: '10px'
                }, {
                    name: "ticktel",
                    index: "ticktel",
                    align: 'center',
                    width: '10px',
                    hidden:true
                }, {
                    name: "tickmemo",
                    index: "tickmemo",
                    align: 'center',
                    width: '50px',
                    hidden:true
                }, {
                    name: "creator",
                    index: "creator",
                    align: 'center',
                    width: '10px'
                }, {
                    name: "updatetime",
                    index: "updatetime",
                    align: 'center',
                    width: '15px'
                }, {
                    name: "test10",
                    index: "test10",
                    align: 'center',
                    width: '15px',
                    hidden: true,
                    formatter:showButton
                }],
            onCellSelect: function(rowid,e) {
                //alert("rowid=" + rowid  );
            },
            onSelectRow: function (id, status) {
                selectedGridRowId = id;
                var row = $("#QueryResult").jqGrid('getRowData', id);
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
    $("#tickid").val(row.tickid);
    $("#procman").val(row.procman);
    $("#tickname").val(row.tickname);
    $("#ticktel").val(row.ticktel);
    return;
    form = $('#modal-update').find('#UpdateMaintainForm');
    var id = $("#gridList1").jqGrid('getGridParam', "selrow");
    //var row = $("#gridList1").jqGrid('getRowData', rowId);
    $('#CJ_MAINTYPE_U').val(row.CJ_MAINTYPE);
    if (row.CJ_MAINTYPE == 'DI4') {
        document.getElementById("showLevel_U").style.display = "none";
    }
    $('#CJ_SUBTYPE_U').val(row.CJ_SUBTYPE);
    $('#CJ_ITEMNAME_U').val(row.CJ_ITEMNAME);
    $('#CJ_ITEMUNIT_U').val(row.CJ_ITEMUNIT);
    $('#CJ_REMARK_U').val(row.CJ_REMARK);
    if (row.CJ_ISACTIVE == 'Y') {
        document.getElementById("CJ_ISACTIVE_U").checked = true;
    }
    $('#CJ_ITEMID_U').val(row.CJ_ITEMID);
    $(form).find('input[name="EDIT_TYPE"]').val("update");
    $(form).find('input[name="uploadType"]').val("DrugItemImage");
//                $('#EDIT_TYPE').val("update");
//                $('#uploadType').val("DrugItemImage");
    
    $(dialogId).dialog('open');
}