$(document).ready(function () {
    genQrcode();
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

    function genQrcode(){
        var evid = '20181229';
        var tickno = '35001,35002,35999';
        
        var serverNmPort = 'http://ult.nctu.me';
        //serverNmPort = 'http://localhost:8081';//for local test
        var text = serverNmPort + '/tick/checkTick.jsp?evid='+evid+'%26tickno='+tickno;
        
        $("#qrCodeUrl").val(text);
        $("#qrCodeUrl").text(text);
        
        $("#qrcode").attr("src","http://chart.apis.google.com/chart?cht=qr&chl="+ text +"&chs=300x300");
        $("#qrcode").show();
     }
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
