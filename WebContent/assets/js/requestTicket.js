$(document).ready(function () {
    localStorage.setItem("lastOperation", 'requestTicket.jsp');
    
    if (localStorage.teamname) {
        $("#team").val(localStorage.getItem("teamname"));
    }
    if (localStorage.eventid) {
        $("#eventid").val(localStorage.getItem("eventid"));
    }
    $('#allowcontact').bootstrapToggle({
      on: '是',
      off: '否'
    });
    //$("#btnTickNo1").click(function(){        
    $("button[id^='btnTickNo']").click(function(){        
        //alert(this.value);
        //alert(this.value);
    });
    $("#tckno4").attr("disabled", true);
    $("#tckno3").attr("disabled", true);
    $("#tckno2").attr("disabled", true);   
    $("div[id^='divHide']").hide();
    //var allTicketsNos="";
    //$("#countSelf").attr("value", 123);
    $("#btnSubmit").attr("disabled",true);
    //$("#txtBirth").datepicker();
    $("#btnSubmit").click(function(){        
        submitData();
    });
    
    $("#team").change(function(){        
        localStorage.setItem("teamname", this.value);
    });
    $("#eventid").change(function(){        
        localStorage.setItem("eventid", this.value);
    });
    $("#audiencetel").change(function(){   
        if(this.value.length>=7){
            getReqTickCount();
        }
    });
    $("#tckno1").change(function(){        
        var ticknoValid = ticknoIsOk(this.value); 
        if(ticknoValid){
            $("#btnSubmit").attr("disabled",false);
            $("#btnContinue4").attr("disabled",false);
        }else{            
            $("#btnSubmit").attr("disabled",true);
            $("#btnContinue4").attr("disabled",true);
        }
    });
    $("#eventid").change(function(){
        getReqTickCount();
    });
    $("input[name='ReqTickNo']").change(function(){
        var tickNo = eval(this.value);
        if(tickNo===4){
            $("#tckno4").attr("disabled", false);
            $("#tckno3").attr("disabled", false);
            $("#tckno2").attr("disabled", false);
        }
        if(tickNo===3){
            $("#tckno4").attr("disabled", true);
            $("#tckno3").attr("disabled", false);
            $("#tckno2").attr("disabled", false);
        }
        if(tickNo===2){
            $("#tckno4").attr("disabled", true);
            $("#tckno3").attr("disabled", true);
            $("#tckno2").attr("disabled", false);
        }
        if(tickNo===1){
            $("#tckno4").attr("disabled", true);
            $("#tckno3").attr("disabled", true);
            $("#tckno2").attr("disabled", true);           
        }
    });
    getReqTickCount();
    
    $("#btnContinue4").click(function(){        
        var tickNo = eval($("input[name='ReqTickNo']:checked").val());
        if(tickNo>=2){
            var add1 = eval($("#tckno1").val())+1;
            $("#tckno2").val(add1);  
        }        
        if(tickNo>=3){
            var add1 = eval($("#tckno2").val())+1;
            $("#tckno3").val(add1);  
        }        
        if(tickNo>=4){
            var add1 = eval($("#tckno3").val())+1;
            $("#tckno4").val(add1);  
        }                
    });
    
    $("#btnClear").click(function(){        
       //$("input").val('');        
       $("#tckno1").val('');
       $("#tckno2").val('');
       $("#tckno3").val('');
       $("#tckno4").val('');
       $("#procman").text('');
        $("#reqName").text('');
        $("#reqTel").text('');
        $("#reqTickNo").text('');
        $("#showTickNo").text('');  
        $("#btnSubmit").attr("disabled",true);
        //$("div[id^='divHide']").hide();
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
            //getReqTickInfo(this.value);
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
    
});

    
function ticknoIsOk(ticketNo){    
    //alert($("#eventid").val());
    if($("#eventid").val()==='20181014'){
        return ticketNo>=20001 && ticketNo<=25000; 
    }else if($("#eventid").val()==='20181103'){
        return ticketNo>=25001 && ticketNo<=30000; 
    }else if($("#eventid").val()==='20181125'){
        return ticketNo>=30001 && ticketNo<=35000; 
    }else if($("#eventid").val()==='20181229'){
        return ticketNo>=35001 && ticketNo<=40000; 
    }else if($("#eventid").val()==='20190101'){
        return ticketNo>=40001 && ticketNo<=45000; 
    }
    
}
function submitData() {//not yet
        commit();
//    }   
}

//送出票根資料
function commit(){
    //alert($("input[name='contactStatus']:checked").val());
    var action = 'create';
    if($("#btnSubmit").text() ==='更新資料'){
        action = 'update';
    }
    var allTickIds = $("#tckno1").val();
    if($("#tckno2").val()!=='') allTickIds += "," + $("#tckno2").val();
    if($("#tckno3").val()!=='') allTickIds += "," + $("#tckno3").val();
    if($("#tckno4").val()!=='') allTickIds += "," + $("#tckno4").val();
    
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            action: action,
            ajaxAction: 'addRequestTick',
            eventid: $('#eventid').val(),
            team: $('#team').val(),
            procaddr: $('#procaddr').val(),
            allowcontact: $('#allowcontact').prop("checked")? 1:0,
            allTickIds: allTickIds,
            audiencename: $('#audiencename').val(),
            audiencetel: $('#audiencetel').val(),
            tickmemo: $('#tickmemo').val()            
        },
        async: false,
        success: function (data) {            
            alert(data.infoMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#tckid").val('');                
            }
            getReqTickCount();
            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次或通知系統管理者!');
        }
    });
}

//取得已輸入索票登錄數量
function getReqTickCount(){
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getReqTickCount',
            eventid: $('#eventid').val(),
            audiencename: $('#audiencename').val(),
            audiencetel: $('#audiencetel').val(),
            username: $('#username').val()
        },
        async: false,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            //var percent = eval(jo.totalShowCnt)/eval(jo.totalReqCnt) * 100 + ' ';
            $("#countSelf").text(jo.countSelf);
//            $("#countTotal").text(jo.totalShowCnt + ' (' + percent.substring(0,4) + ' %)');
            $("#audienceEvidCnt").text(jo.audienceEvidCnt);
            //alert(jo.audienceTckList);
            
            var alltck = jo.audienceTckList.split(',');
            
            if(eval(jo.audienceEvidCnt)>=1){
                //alert('此回條已輸入');
                $("#btnSubmit").text('更新資料');
                $("#btnSubmit").prop('disabled', false);
            }else{
                $("#btnSubmit").text('確定新增');
                //alert('未輸入過');
            }
            $("input[name='ReqTickNo'][value=" + eval(jo.audienceEvidCnt) + "]").click();                
            if(eval(jo.audienceEvidCnt)>=4){
                alert('此觀眾已索取超過四張票！');
                $("#tckno4").val(alltck[3]);
                
            }else{
                $("div[id^='divHide0']").show();
            }
            if(eval(jo.audienceEvidCnt)>=1){
                $("#tckno1").val(alltck[0]);
            }
            if(eval(jo.audienceEvidCnt)>=2){
                $("#tckno2").val(alltck[1]);
            }
            if(eval(jo.audienceEvidCnt)>=3){
                $("#tckno3").val(alltck[2]);
            }             
        },
        error: function (xhr, ajaxOptions, thrownError) {            
            alert('系統異常，無法取得票根資料!');
        }
    });
}

//以票號取得該票之索票人索票的相關資訊
function getReqTickInfo(){
    $("div[id^='divHide']").show();
    $("#procman").text('讀取中..');
    $("#reqName").text('..');
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
            $("#contactperson").val(jo.username);            
            $("input[name='contactStatus'][value=" + jo.contactStatus + "]").prop('checked', true);
            $("#audiencecomment").val(jo.audiencecomment);
            $("#audiencename").val(jo.audiencename);
            $("#lastupdatetime").text(jo.lastupdatetime);
            if(jo.audiencename){
                //alert('此回條已輸入');
                $("#btnSubmit").text('更新資料');
                $("#btnSubmit").prop('disabled', false);
            }else{
                $("#btnSubmit").text('確定新增');
                //alert('未輸入過');
            }
            $("#comment").val(jo.comment);
            $("#interestSelected").text(jo.interest);
        },
        error: function (xhr, ajaxOptions, thrownError) {            
            alert('系統異常，無法取得票券資料!');
        }
    });
}