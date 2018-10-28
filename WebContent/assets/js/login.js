$(document).ready(function () {
    //autologin();
    //$("#txtBirth").datepicker();
    //txtUserNum
    if (localStorage.loginUserId) {
        $("#txtUserNum").val(localStorage.getItem("loginUserId"));
    }
    if (localStorage.loginUserPwd) {
        $("#txtPwd").val(localStorage.getItem("loginUserPwd"));
    }
    $("#btnRegister").click(function(){
        if($("#btnRegister").text()!=='確定註冊'){
            $("div[id^='divHide']").show();
            $("#btnRegister").text('確定註冊');
            $("#btnLogin").hide();
            $("#btnClear").hide();
            $("#btnCancel").show();
        }else{
            if($('#txtUserNum').val()===''){
                alert('請輸入帳號');
            }else if($('#txtPwd').val()===''){
                alert('請輸入密碼');
            }else if($('#team').val()===''){
                alert('請選擇組別');
            }else{
                register();
            }            
        }
    });
    $("#btnCancel").click(function(){
        $("div[id^='divHide']").hide();
        $("#btnRegister").text('註冊');
        $("#btnLogin").show();
        $("#btnClear").show();
        $("#btnCancel").hide();
    });
    $("#btnClear").click(function(){
        $("input").val('');        
    });
});
function login() {
    var deviceType="";
    var u = navigator.userAgent;    
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if(isAndroid){
        //window.location = 'indexAndroid.jsp';
        deviceType = "Android";
    }
    else if(isiOS){
        deviceType = "iOS";
    }else if(u.indexOf('Windows') > -1){
        deviceType = "Windows";
    }else{
        deviceType = "Others";
    }  
    
    localStorage.setItem("loginUserId", $("#txtUserNum").val());
    localStorage.setItem("loginUserPwd", $("#txtPwd").val());
    if($("#txtUserNum").val()===''){
        alert('請輸入您的帳號');
        return;
    }
    if($("#txtPwd").val()===''){
        alert('請輸入您的密碼');
        return;
    }
    $.ajax({
        url: 'AuthServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'login',
            uid: $('#txtUserNum').val(),
            deviceType: deviceType ,
            pwd: $('#txtPwd').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //if(data.infoMsg.indexOf('登入成功')>=0){
            if(jo.msg.indexOf('登入成功')>=0){
                if(localStorage.lastOperation){
                    $(location).attr('href', localStorage.getItem("lastOperation"));
                }else{
                    $(location).attr('href', "index.jsp");
                }
                //window.location.href="reply.jsp";
                localStorage.setItem("loginCode", jo.loginCode);                
                localStorage.setItem("teamName", jo.teamName);                
            }else{
                localStorage.removeItem("loginCode");
                var jo2 = JSON.parse(data.infoMsg);
                alert(jo2.msg);
            }
            
            //$.unblockUI();
//            $.alert.open({
//                type: 'info',
//                content: '登入成功',
//                callback: function () {
//                    //window.opener.location.reload(); //將原畫面重新整理
//                    //window.close();
//                }
//            });
        },
        error: function (xhr, ajaxOptions, thrownError) {
            //alert('ajaxOptions=' + ajaxOptions);
            alert(xhr.status);
            alert(thrownError);
        }
    });
}
function register() {
    localStorage.setItem("loginUserId", $("#txtUserNum").val());
    $.ajax({
        url: 'AuthServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'register',
            uid: $('#txtUserNum').val(),
            pwd: $('#txtPwd').val(),
            team: $('#team').val(),
            authCode: $('#authCode').val(),
            tel: $('#tel').val(),
            email: $('#email').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //if(data.infoMsg.indexOf('登入成功')>=0){
            if(jo.msg.indexOf('註冊成功')>=0){
                alert(jo.msg);
                window.location.href="index.jsp";
                localStorage.setItem("loginCode", jo.loginCode);                
            }else{
                localStorage.removeItem("loginCode");
                alert(jo.msg);
            }
            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            //alert('ajaxOptions=' + ajaxOptions);
            alert(xhr.status);
            alert(thrownError);
        }
    });
}
function autologin() {
    if(localStorage.loginCode){
        $.ajax({
            url: 'AuthServlet',
            method: 'POST',
            dataType: 'json',
            data: {
                ajaxAction: 'autologin',
                uid: localStorage.getItem("loginUserId"),
                loginCode: localStorage.getItem("loginCode")
            },
            async: false,
            success: function (data) {
                var jo = JSON.parse(data.infoMsg);
                if(jo.msg.indexOf('登入成功')>=0){
                    window.location.href="reply.jsp";                    
                }else{
                    localStorage.removeItem("loginCode");
                    alert(data.infoMsg);
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert('ajaxOptions=' + ajaxOptions);
                alert(xhr.status);
                alert(thrownError);
            }
        });    
    }
    
}