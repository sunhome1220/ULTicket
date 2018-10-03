$(document).ready(function () {
    var allTicketsNos="";
    //$("#txtBirth").datepicker();
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
    $("#btnContinue5").click(function(){        
        if($("#tckno4").val()===''){$("#btnContinue4").click();}
          var add1 = eval($("#tckno4").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno5").val(add1);  
    });
    $("#btnContinue6").click(function(){        
        if($("#tckno5").val()===''){$("#btnContinue5").click();}
        var add1 = eval($("#tckno5").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno6").val(add1);  
    });
    $("#tckno1").change(function(){
        if($("#tckno1").val().length===5){
            $("#btnContinue2").attr("disabled",false);
            $("#btnContinue3").attr("disabled",false);
            $("#btnContinue4").attr("disabled",false);
            $("#btnContinue5").attr("disabled",false);
            $("#btnContinue6").attr("disabled",false);
        }else{
            $("#btnContinue2").attr("disabled",true);
            $("#btnContinue3").attr("disabled",true);
            $("#btnContinue4").attr("disabled",true);
            $("#btnContinue5").attr("disabled",true);
            $("#btnContinue6").attr("disabled",true);
        }
        //allTicketsNos
        //alert($("#tckno1").val());
    });
});
function addAllReceipts() {//not yet
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