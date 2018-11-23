$(document).ready(function () {
    $("a[id^=href]").click(function(){
        window.location.href=this.id.replace('href_','')+'.jsp';
    });
    //$("#qrcode").hide();
    if (localStorage.teamName) {
        $("#team").val(localStorage.getItem("teamName"));
    }
    if (localStorage.eventid) {
        $("#eventid").val(localStorage.getItem("eventid"));
    }
    $("#eventid").change(function(){        
        localStorage.setItem("eventid", this.value);
    });
    $("#btnContinue2").click(function(){        
        var add1 = eval($("#tckno1").val())+1;
        if(add1<10000) add1 = '0' + add1;
        $("#tckno2").val(add1);  
    });
    $("#btnContinue3").click(function(){        
        if($("#tckno2").val()===''){$("#btnContinue2").click();}
        var add1 = eval($("#tckno2").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno3").val(add1);  
    });
    $("#btnContinue4").click(function(){        
        if($("#tckno3").val()===''){$("#btnContinue3").click();}
        var add1 = eval($("#tckno3").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno4").val(add1);  
    });
    $("#btnCopyUrl").click(function(){        
        //copyUrl();
        $("#qrCodeUrl").select();
        document.execCommand("copy");
        alert('已複製至剪貼簿，可直接以mail或line傳給朋友');
    });
    $("#btnShowQRCode").click(function(){
        var evid = $("#eventid").val();
        var tickno = '';
        if($("#tckno1").val()!==''){
            tickno = $("#tckno1").val();
        }
        if($("#tckno2").val()!==''){
            tickno += ','+ $("#tckno2").val();
        }
        if($("#tckno3").val()!==''){
            tickno += ','+ $("#tckno3").val();
        }
        if($("#tckno4").val()!==''){
            tickno += ','+ $("#tckno4").val();
        }
        if(tickno===''){
            alert('請至少輸入一張票號');return;
        }
        var staffId = encodeURIComponent($("#username").text().replace(',您好',''));//%E7%8E%8B%E6%B8%AC%E8%A9%A6
        var team = encodeURIComponent($("#team").val());
        
        var serverNmPort = 'http://ult.nctu.me';
        //serverNmPort = 'http://localhost:8081';//for test
        var text = serverNmPort + '/tick/reqTickAudi.jsp?evid='+evid+'%26tickno='+tickno+'%26procman='+staffId+'%26team='+team;
        
        $("#qrCodeUrl").val(text);
        $("#qrCodeUrl").text(text);
        $("#qrcode").attr("src","http://chart.apis.google.com/chart?cht=qr&chl="+ text +"&chs=300x300");
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