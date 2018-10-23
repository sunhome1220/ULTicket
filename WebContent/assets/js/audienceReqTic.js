$(document).ready(function () {
    $("#qrcode").hide();
    $("#btnShowQRCode").click(function(){
        $("#qrcode").show();
     });
    //autologin();
    //$("#txtBirth").datepicker();
    if (localStorage.queryTeamName) {
        $("#teaeName").get(0).selectedIndex = 2;
        $("#teaeName").val(localStorage.getItem("queryTeamName"));
        $("#teaeName > [value=" + localStorage.getItem("queryTeamName") + "]").attr("selected", "true");
    }
    if (localStorage.queryStaffName) {
        $("#staffName").val(localStorage.getItem("queryStaffName"));
    }
    
});

function QueryTicStatus() {
    
    $("#QueryResult").show();
    $("#QueryResult").jqGrid(
        {
            url: 'QueryServlet',
            mtype: 'POST',
            datatype: "json",
            postData: {
                ajaxAction: 'queryTicStatus',
                //staffName: $('#staffName').val(),
                teamNm: $('#teamName').val()
            },
            pager: "#QueryResultpagger",
            rowList: [10, 20, 30],
            colNames: ["ID", "場編", "場次", "組別", "發票地",
                "發票人", "票號", "姓名", "電話", "備註", "操作"],
            colModel: [{
                    name: "taginc",
                    index: "taginc",
                    align: 'center',
                    hidden:true
                }, {
                    name: "evid",
                    index: "evid",
                    align: 'center',
                    width: '60px'
                }, {
                    name: "event",
                    index: "event",
                    align: 'center',
                    width: '60px'
                }, {
                    name: "team",
                    index: "team",
                    align: 'center',
                    width: '40px'
                }, {
                    name: "test4",
                    index: "procman",
                    align: 'center',
                    width: '40px'
                }, {
                    name: "test5",
                    index: "procaddr",
                    align: 'center',
                    width: '40px'
                }, {
                    name: "tickid",
                    index: "tickid",
                    align: 'center',
                    width: '20px'
                }, {
                    name: "tickname",
                    index: "tickname",
                    align: 'center',
                    width: '30px'
                }, {
                    name: "test8",
                    index: "ticktel",
                    align: 'center',
                    width: '40px'
                }, {
                    name: "test9",
                    index: "tickmemo",
                    align: 'center',
                    width: '50px'
                }, {
                    name: "test10",
                    index: "test10",
                    align: 'center',
                    width: '50px',
                    hidden: true
                }]
        });
    $("#QueryResult").setGridWidth($(window).width() * 0.8);
}