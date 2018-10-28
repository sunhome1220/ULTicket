$(document).ready(function () {
    $('#allowcontact').bootstrapToggle({
      on: '是',
      off: '否'
    });
    var eventid = getUrlParameter('evid');
    var procman = getUrlParameter('procman');
    var tickno = getUrlParameter('tickno');
    if(procman){
        $("#eventid").val(eventid);
        $("#eventid").attr("disabled", true);
        $("#procman").text(procman);
        $("#tickno").text(tickno);        
    }
    
    var jo;
    
    //$("#btnTickNo1").click(function(){        
    $("button[id^='btnTickNo']").click(function(){        
        //alert(this.value);
        //alert(this.value);
    });
    $("input[id^=tckno]").attr("disabled", true);
    $("#tckno1").attr("disabled", false);
//    $("#btnAdd").hide();
//    $("#btnMod").hide();
    
    //$("#btnSubmit").attr("disabled",true);
    //$("#txtBirth").datepicker();
    $("#btnSubmit").click(function(){     
        if($('#audiencename').val()===''){
            alert('請輸入您的姓名');
            $('#audiencename').focus();
            return;
        }else if($('#audiencetel').val()===''){
            alert('請先輸入您的電話，電話僅作為資料識別之用，若您不願接受滿意度調查，志工將不會去電詢問');
            $('#audiencetel').focus();
            return;
        }
        if(confirm('請確認資料是否正確\n票號:'+ tickno+'\n索票人姓名:'+ $("#audiencename").val())){
            submitData();
        }
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
    
    $("#btnClear").click(function(){        
        $("#audiencename").val('');
        $("#audiencetel").val('');
        $("#audienceEmail").val('');
        $("#btnSubmit").attr("disabled",true);        
    });    
});

//送出票根資料
function submitData(){
    //alert($("input[name='contactStatus']:checked").val());
    var action = 'create';
    var allTickIds = $("#tckno1").val();
    
    var procman = $('#procman').text();
    var confirmMsg = '請確認以下資料是否正確\n發票人:'+ procman
            +'\n索票人:'+ $('#audiencename').val()
            +'\n索票人電話:'+$('#audiencetel').val();
            //+'\n登錄票數:'+ newTickIdsCount;
    if(!confirm(confirmMsg)){
        return;
    }
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            action: action,
            ajaxAction: 'addRequestTickAudi',
            eventid: $('#eventid').val(),
            team: $('#team').val(),
            procman: procman,
            procaddr: $('#procaddr').val(),
            allowcontact: $('#allowcontact').prop("checked")? 1:0,
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
            $("#countSelf2").prop("title", "票號:11111");
            
            $("#audienceEvidCnt").text(jo.audienceEvidCnt);
            //alert(jo.audienceTckList);
            $("#originalAllTickNo").val(jo.audienceTckList);
            var alltck = jo.audienceTckList.split(',');
            $("#procaddr").val(jo.procaddr);
            $("#tickmemo").val(jo.tickmemo);
            $("#updatetime").text(jo.updatetime);
            if(jo.allowcontact==='1'){//
                $('#allowcontact').prop("checked",true);
                $('#allowcontact').click();
            }else{
                $('#allowcontact').prop("checked",false);
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
//                $("#btnAdd").show();
//                $("#btnMod").hide();
//                $("#btnQuery").hide();
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