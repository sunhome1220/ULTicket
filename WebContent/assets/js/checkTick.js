$(document).ready(function () {
    if($("#role").val()>='1'){//
        
    }else{
        alert('請先登入系統或申請檢核票券的權限');
        return;
    }
    if(confirm('請確認資料是否正確？\n\n場次：20181229-新北市多功能集合堂\n人數：3')){
        alert('已確認觀眾到場'); 
    }else{
        alert('取消'); 
    }
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

    if (localStorage.queryTeamName) {
        $("#teaeName").get(0).selectedIndex = 2;
        $("#teaeName").val(localStorage.getItem("queryTeamName"));
        $("#teaeName > [value=" + localStorage.getItem("queryTeamName") + "]").attr("selected", "true");
    }
    if (localStorage.queryStaffName) {
        $("#staffName").val(localStorage.getItem("queryStaffName"));
    }
    
});
