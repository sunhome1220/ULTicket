$(document).ready(function () {
    //$("#txtBirth").datepicker();
});
function login() {//not yet
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
            alert(data.infoMsg);
            window.location.href="reply.jsp";
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