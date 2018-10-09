$(document).ready(function () {
    var allTicketsNos="";
    getTickCount();
    //$("#countSelf").attr("value", 123);
    
    $("#btnSubmit").attr("disabled",true);
    //$("#txtBirth").datepicker();
    $("#btnSubmit").click(function(){        
        addAllReceipts();
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
    $("#btnContinue5").click(function(){        
        if($("#tckno4").val()===''){$("#btnContinue4").click();}
        var add1 = eval($("#tckno4").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno5").val(add1);  
        return;
    });
    $("#btnClear").click(function(){        
       $("input[id^='tckno']").val('');        
    });
    $("#btnContinue6").click(function(){        
        if($("#tckno5").val()===''){$("#btnContinue5").click();}
        var add1 = eval($("#tckno5").val())+1;
        if(add1<10000) add1 = "0" + add1;
        $("#tckno6").val(add1);  
    });
 
    $("input[id^='tckno']").change(function(){
        //alert(this.value);
        var ticknoValid = ticknoIsOk(this.value); 
        if(this.value.length>0 && !ticknoValid){
            alert('請確認票號是否正確');
            this.focus();
        }
    }); //選擇所有的name屬性以’J'開頭的input元素
    $("#tckno1").change(function(){
        var ticknoValid = ticknoIsOk($("#tckno1").val()); 
        if(ticknoValid){
            $("#btnSubmit").attr("disabled",false);
            $("#btnContinue2").attr("disabled",false);
            $("#btnContinue3").attr("disabled",false);
            $("#btnContinue4").attr("disabled",false);
            $("#btnContinue5").attr("disabled",false);
            $("#btnContinue6").attr("disabled",false);
        }else{
            if($("#tckno1").val().length>0 && !ticknoValid){
                alert('請確認票號是否正確');
                $("#tckno1").focus();
            }
            $("#btnSubmit").attr("disabled",true);
            $("#btnContinue2").attr("disabled",true);
            $("#btnContinue3").attr("disabled",true);
            $("#btnContinue4").attr("disabled",true);
            $("#btnContinue5").attr("disabled",true);
            $("#btnContinue6").attr("disabled",true);
        }
        //allTicketsNos
        //alert($("#tckno1").val());
    });
    $("#eventid").change(function(){
        getTickCount();
    });
});

    
function ticknoIsOk(ticketNo){
    if($("#eventid").val()==='20181014'){
        return ticketNo>=20001 && ticketNo<=25000; 
    }else if($("#eventid").val()==='20181103'){
        return ticketNo>=25001 && ticketNo<=30000; 
    }else if($("#eventid").val()==='20181125'){
        return ticketNo>=30001 && ticketNo<=35000; 
    }else if($("#eventid").val()==='20181129'){
        return ticketNo>=35001 && ticketNo<=40000; 
    }
    
}
function addAllReceipts() {//not yet
    var allTicNo ="";
    if($("#tckno1").val().length===5){allTicNo += $("#tckno1").val() + ";";}
    if($("#tckno2").val().length===5){allTicNo += $("#tckno2").val() + ";";}
    if($("#tckno3").val().length===5){allTicNo += $("#tckno3").val() + ";";}
    if($("#tckno4").val().length===5){allTicNo += $("#tckno4").val() + ";";}
    if($("#tckno5").val().length===5){allTicNo += $("#tckno5").val() + ";";}
    if($("#tckno6").val().length===5){allTicNo += $("#tckno6").val() + ";";}
//    $.alert.open({
//        type: 'info',
//        content: '即將送出，請確認以下票根號碼是否正確\\\n '+ allTicNo.replace(';','\\\n'),
//        callback: function () {
//            commit();
//            //window.opener.location.reload(); //將原畫面重新整理
//            //window.close();
//        }
//    });
    var confirmMsg = '即將送出以下票根號碼：\n\n'+ allTicNo.replace(';','\n').replace(';','\n').replace(';','\n').replace(';','\n').replace(';','\n');
    var count = allTicNo.split(';').length-1;
    confirmMsg += '\n\n共'+ count +'張票根資料，請確定是否正確';
    if(!confirm(confirmMsg)){
        return;
    }else{
        commit(allTicNo);
    }   
}

//送出票根資料
function commit(allTicNo){
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'addRecipts',
            eventid: $('#eventid').val(),
            ticketnos: allTicNo
        },
        async: false,
        success: function (data) {
            alert(data.infoMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#tckno1").val('');
                $("#tckno2").val('');
                $("#tckno3").val('');
                $("#tckno4").val('');
                $("#tckno5").val('');
                $("#tckno6").val('');
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
//            alert(xhr.status);
//            alert(thrownError);
            alert('系統異常，請重新操作一次或通知系統管理者!');
        }
    });
}
//取得已輸入票根數量
function getTickCount(){
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getShowTicCount',
            eventid: $('#eventid').val(),
            username: $('#username').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            var percent = eval(jo.totalShowCnt)/eval(jo.totalReqCnt) * 100 + ' ';
            $("#countSelf").text(jo.showCnt);
            $("#countTotal").text(jo.totalShowCnt + ' (' + percent.substring(0,4) + ' %)');
            $("#countReqTick").text(jo.totalReqCnt);
        },
        error: function (xhr, ajaxOptions, thrownError) {            
            alert('系統異常，無法取得票根資料!');
        }
    });
}