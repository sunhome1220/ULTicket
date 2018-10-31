$(document).ready(function () {
    var eventid = getUrlParameter('evid');
    var procman = getUrlParameter('procman');
    var tickno = getUrlParameter('tickno');
    if(procman){
        $("#eventid").val(eventid);
        $("#procman").text(procman);
        $("#tickno").text(tickno);
        //alert('觀眾索票');
        //$("div[id^=").hide();
    }else{
        localStorage.setItem("lastOperation", 'requestTicket.jsp');
        if (localStorage.teamName) {
            //alert(localStorage.teamName);
            $("#team").val(localStorage.getItem("teamName"));
        }
        if (localStorage.eventid) {
            $("#eventid").val(localStorage.getItem("eventid"));
        }
        if (localStorage.procaddr) {
            $("#procaddr").val(localStorage.getItem("procaddr"));            
        }
        if (localStorage.procmanOther) {
            $("#procmanOther").val(localStorage.getItem("procmanOther"));
        }        
    }
    
    
    var jo;
//    if (localStorage.teamname) {
//        $("#team").val(localStorage.getItem("teamname"));
//    }
    
    $('#allowcontact').bootstrapToggle({
      on: '是',
      off: '否'
    });
    //$("#btnTickNo1").click(function(){        
    $("button[id^='btnTickNo']").click(function(){        
        //alert(this.value);
        //alert(this.value);
    });
    $("input[id^=tckno]").attr("disabled", true);
    $("#tckno1").attr("disabled", false);
    
    $("div[id^='divHide']").hide();
    $("#btnSubmit").attr("disabled",true);
    $("#btnSubmit").click(function(){        
        submitData();
    });
    
    $("#team").change(function(){        
        localStorage.setItem("teamname", this.value);
    });
    $("#eventid").change(function(){        
        localStorage.setItem("eventid", this.value);
    });    
    if($("#eventid").val()!==''){
        getReqTickCount();        
    }
    $("input[name='procmanType']").change(function(){        
        if(this.value==='S'){
            $("#procmanOther").hide();
            //$("#procman").val($('#loginUser').val());
        }else if(this.value==='O'){
            $("#procmanOther").show();
            //$("#procman").val($("#procmanOther").val());//$('#loginUser').val()
        }
        
    });
    $("#btnQuery").click(function(){           
        if($('#eventid').val()===''){
            alert('請輸入場次');
        }else if($('#audiencename').val()===''){
            alert('請先輸入索票人姓名');
            $('#audiencename').focus();
        }else if($('#audiencetel').val()===''){
            alert('請先輸入索票人電話!');
            $('#audiencetel').focus();
        }else{
            getReqTickCount();
        }       
    });
    
//    $("#audiencetel").change(function(){   
//        if(this.value.length>=7){
//            getReqTickCount();
//        }
//    });
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
        for (i = 2; i <= tickNo; i++) {
            $("#tckno"+i).attr("disabled", false);            
        }
        for (i = tickNo+1; i <= 10; i++) {
            $("#tckno"+i).attr("disabled", true); 
            $("#tckno"+i).val('');
        }        
    });
    //getReqTickCount();
    
    $("#btnContinue4").click(function(){        
        var tickNo = eval($("input[name='ReqTickNo']:checked").val());
        var i;
        for (i = 1; i < tickNo; i++) {
            var add1 = eval($("#tckno"+i).val())+1;
            $("#tckno"+(i+1)).val(add1);  
        }                         
    });
    
    $("#btnClear").click(function(){        
        $("input[id^=tckno]").val('');
        $("#audiencename").val('');
        $("#audiencetel").val('');
        $("#procaddr").val('');
        $("#tickmemo").val('');
        $("#btnSubmit").attr("disabled",true);        
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

//送出票根資料
function submitData(){
    //alert($("#procmanOther").val()+"-"+$("#procaddr").val());
    localStorage.setItem("procmanOther",$("#procmanOther").val());
    localStorage.setItem("procaddr",$("#procaddr").val());
    //alert($("input[name='contactStatus']:checked").val());
    var action = 'create';
    if($("#btnSubmit").text() ==='更新資料'){
        action = 'update';
    }
    var allTickIds = $("#tckno1").val();
    if($("#tckno2").val()!=='') allTickIds += "," + $("#tckno2").val();
    if($("#tckno3").val()!=='') allTickIds += "," + $("#tckno3").val();
    if($("#tckno4").val()!=='') allTickIds += "," + $("#tckno4").val();
    if($("#tckno5").val()!=='') allTickIds += "," + $("#tckno5").val();
    if($("#tckno6").val()!=='') allTickIds += "," + $("#tckno6").val();
    if($("#tckno7").val()!=='') allTickIds += "," + $("#tckno7").val();
    if($("#tckno8").val()!=='') allTickIds += "," + $("#tckno8").val();
    if($("#tckno9").val()!=='') allTickIds += "," + $("#tckno9").val();
    if($("#tckno10").val()!=='') allTickIds += "," + $("#tckno10").val();
    
    var newTickIdsCount = allTickIds.split(',').length;
    var oldTickIdsCount = $("#originalAllTickNo").val().split(',').length;
    var procman = "";//20181022 發票人

    var procmanType =  $('input[name=procmanType]:checked').val();
    if(procmanType==='S'){
        procman = $('#loginUser').val();
    }else if(procmanType==='O'){
        procman = $('#procmanOther').val();
    }
    var confirmMsg = '請確認以下資料是否正確\n發票人:'+ procman
            +'\n索票人:'+ $('#audiencename').val()
            +'\n索票人電話:'+$('#audiencetel').val()
            +'\n登錄票數:'+ newTickIdsCount;
    
    if(!confirm(confirmMsg)){
        return;
    }
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            action: action,
            ajaxAction: 'addRequestTick',
            eventid: $('#eventid').val(),
            team: $('#team').val(),
            procman: procman,
            procaddr: $('#procaddr').val(),
            allowcontact: $('#allowcontact').prop("checked")? 1:0,
            seatType: $("input[name='seatType']:checked").val(),            
            confirmStatus: $("input[name='confirmStatus']:checked").val(),  
            allTickIds: allTickIds,
            originalAllTickNo: $("#originalAllTickNo").val(),
            audiencename: $('#audiencename').val(),
            audiencetel: $('#audiencetel').val(),
            tickmemo: $('#tickmemo').val()            
        },
        async: false,
        success: function (data) {            
            alert(data.infoMsg);
            if(data.infoMsg.indexOf('成功')>=0){
                $("#tckid").val('');                
                getReqTickCount();
            }            
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
            jo = JSON.parse(data.infoMsg);
            $("#countSelf").text(jo.countSelf);
            $("#countSelf2").text(jo.countSelf2);//幫他人登錄數
            //$("#countSelf2").prop("title", "票號:11111");
            
            $("#audienceEvidCnt").text(jo.audienceEvidCnt);
            //alert(jo.audienceTckList);
            $("#originalAllTickNo").val(jo.audienceTckList);
            var alltck = jo.audienceTckList.split(',');
            if(alltck.length>=5){
                $("#procaddr").val(jo.procaddr);
                $("#tickmemo").val(jo.tickmemo);
                $("#updatetime").text(jo.updatetime);
                if(jo.allowcontact==='1'){//
                    $('#allowcontact').prop("checked",true);
                    //$('#allowcontact').click();//????????????
                }else{
                    $('#allowcontact').prop("checked",false);
                }
            }else{
                //alert('tick is null');
            }
            
            
            var confirmMsg = '此索票人已登記'+jo.audienceEvidCnt+'張票，是否要繼續登錄更多票號資料？\n若要修改資料請按:取消)';
            if((eval(jo.audienceEvidCnt)>=1 && eval(jo.audienceEvidCnt)<=9) ||
                    (eval(jo.audienceEvidCnt)>=10 && !confirm(confirmMsg))){
//                    $("#btnAdd").show();
//                    $("#btnMod").show();
//                    $("#btnQuery").hide();

                //alert('此回條已輸入');
                if(eval(jo.audienceEvidCnt)<10){
                    alert('此索票人已登記'+jo.audienceEvidCnt+'張票');
                }
                $("#btnSubmit").text('更新資料');
                $("#btnSubmit").prop('disabled', false);
                $("input[name='ReqTickNo'][value=" + eval(jo.audienceEvidCnt) + "]").click();     
                if(eval(jo.audienceEvidCnt)>=1){
                    $("#tckno1").val(alltck[0]);
                }
                if(eval(jo.audienceEvidCnt)>=2){
                    $("#tckno2").val(alltck[1]);
                }
                if(eval(jo.audienceEvidCnt)>=3){
                    $("#tckno3").val(alltck[2]);
                }             
                if(eval(jo.audienceEvidCnt)>=4){
                    $("#tckno4").val(alltck[3]);
                }             
                if(eval(jo.audienceEvidCnt)>=5){
                    $("#tckno5").val(alltck[4]);
                }             
                if(eval(jo.audienceEvidCnt)>=6){
                    $("#tckno6").val(alltck[5]);
                }             
                if(eval(jo.audienceEvidCnt)>=7){
                    $("#tckno7").val(alltck[6]);
                }             
                if(eval(jo.audienceEvidCnt)>=8){
                    $("#tckno8").val(alltck[7]);
                }             
                if(eval(jo.audienceEvidCnt)>=9){
                    $("#tckno9").val(alltck[8]);
                }             
                if(eval(jo.audienceEvidCnt)>=10){
                    $("#tckno10").val(alltck[9]);
                }             
                
            }else{
                if(eval(jo.audienceEvidCnt)===0 && $('#audiencename').val()!==''){
                    alert('尚無任何索票登錄資料');
                }
                $("input[id^='tckno']").val('');
                $("#btnSubmit").text('確定新增');
                //alert('未輸入過');
//                $("#procaddr").val('');
//                $("#tickmemo").val('');
//                $("#updatetime").val('');
                
            }
            
//            if(eval(jo.audienceEvidCnt)>=4){
//                //alert('此觀眾已索取超過四張票！');
//                //$("#tckno4").val(alltck[3]);                
//            }else{
                $("div[id^='divHide0']").show();
//            }
            
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

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};