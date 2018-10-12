$(document).ready(function () {
    
    //var allTicketsNos="";
    getTickCount();
    //$("#countSelf").attr("value", 123);
    $("#btnSubmit").attr("disabled",true);
    //$("#txtBirth").datepicker();
    $("#btnSubmit").click(function(){        
        submitData();
    });
    
    $("#btnClear").click(function(){        
       $("input").val('');        
    });
    
    $("input[name='rateStar']").change(function(){        
       $("#btnSubmit").attr("disabled", false);
    });
    
    $("input[id^='tckno']").change(function(){//選擇所有的name屬性以’tckno'開頭的input元素
        var ticknoValid = ticknoIsOk(this.value); 
        if(this.value.length>0 && !ticknoValid){
            alert('請確認票號是否正確');
            this.focus();
        }
    }); 
    $("#tickid").keyup(function(){        
        if(this.value.length===5){
            //alert(this.value);
            getReqTickInfo(this.value);
        }        
    }); 
    $("input[id^='tckno']").blur(function(){//選擇所有的name屬性以’tckno'開頭的input元素
        //alert(this.value);
        var ticknoValid = ticknoIsOk(this.value); 
        if(this.value.length>0 && !ticknoValid){
            //alert('請確認票號是否正確');
            //this.focus();
        }
    }); 
    $("#tckno1").change(function(){
        var ticknoValid = ticknoIsOk($("#tckno1").val()); 
        if(ticknoValid){
            $("#btnSubmit").attr("disabled",false);
            $("#btnContinue2").attr("disabled",false);
        }else{            
            $("#btnSubmit").attr("disabled",true);
            $("#btnContinue2").attr("disabled",true);
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
function submitData() {//not yet
//    var allTicNo ="";
//    if($("#tckno1").val().length===5){allTicNo += $("#tckno1").val() + ";";}
//    if($("#tckno2").val().length===5){allTicNo += $("#tckno2").val() + ";";}
//    
//    var confirmMsg = '即將送出以下票根號碼：\n\n'+ allTicNo.replace(';','\n').replace(';','\n').replace(';','\n').replace(';','\n').replace(';','\n');
//    var count = allTicNo.split(';').length-1;
//    confirmMsg += '\n\n共'+ count +'張票根資料，請確定是否正確';
//    if(!confirm(confirmMsg)){
//        return;
//    }else{
        commit();
//    }   
}

//送出票根資料
function commit(){
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'addComment',
            eventid: $('#eventid').val(),
            tickid: $('#tickid').val(),
            audiencename: $('#audiencename').val(),
            audiencecomment: $('#audiencecomment').val(),
            interest: '1',//$('#interest').val(),
            comment: $('#comment').val(),
            callTimes: 1
        },
        async: false,
        success: function (data) {            
            alert(data.infoMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#tckid").val('');                
            }
            getTickCount();
            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            //alert('ajaxOptions=' + ajaxOptions);
//            alert(xhr.status);
//            alert(thrownError);
            alert('系統異常，請重新操作一次或通知系統管理者!');
        }
    });
}

//取得已輸入回條數量
function getTickCount(){
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getTicCommentCount',
            eventid: $('#eventid').val(),
            username: $('#username').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //var percent = eval(jo.totalShowCnt)/eval(jo.totalReqCnt) * 100 + ' ';
            $("#countSelf").text(jo.countSelf);
//            $("#countTotal").text(jo.totalShowCnt + ' (' + percent.substring(0,4) + ' %)');
//            $("#countReqTick").text(jo.totalReqCnt);
        },
        error: function (xhr, ajaxOptions, thrownError) {            
            alert('系統異常，無法取得票根資料!');
        }
    });
}

//以票號取得該票之索票人索票的相關資訊
function getReqTickInfo(){
    $("#procman").text('讀取中..');
    $("#reqName").text('讀取中..');
    $("#reqTel").text('...');
    $("#reqTickNo").text('');
    $("#showTickNo").text('');    
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getReqTickInfo',
            eventid: $('#eventid').val(),
            tickid: $('#tickid').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            
            $("#procman").text(jo.procman);
            $("#reqName").text(jo.reqName);
            $("#reqTel").text(jo.reqTel);
            $("#reqTel").attr("href", "tel:" + jo.reqTel);
            $("#reqTickNo").text(jo.reqTickNo);
            $("#showTickNo").text(jo.showTickNo);
            if(jo.reqTickNo === "0"){
                $("#btnSubmit").attr("disabled",true);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {            
            alert('系統異常，無法取得票券資料!');
        }
    });
}