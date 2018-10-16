$(document).ready(function () {
    //autologin();
    //$("#txtBirth").datepicker();
    //txtUserNum
    if (localStorage.loginUserId) {
        $("#txtUserNum").val(localStorage.getItem("loginUserId"));
    }
});
function login() {
    localStorage.setItem("loginUserId", $("#txtUserNum").val());
    $.ajax({
        url: 'AuthServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'login',
            uid: $('#txtUserNum').val(),
            pwd: $('#txtPwd').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //if(data.infoMsg.indexOf('登入成功')>=0){
            if(jo.msg.indexOf('登入成功')>=0){
                if(localStorage.lastOperation){
                    $(location).attr('href', localStorage.getItem("lastOperation"));
                }
                //window.location.href="reply.jsp";
                localStorage.setItem("loginCode", jo.loginCode);                
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
            pwd: $('#txtPwd').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //if(data.infoMsg.indexOf('登入成功')>=0){
            if(jo.msg.indexOf('登入成功')>=0){
                window.location.href="reply.jsp";
                localStorage.setItem("loginCode", jo.loginCode);                
            }else{
                localStorage.removeItem("loginCode");
                alert(data.infoMsg);
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