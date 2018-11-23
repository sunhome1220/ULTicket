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
            alert('請先輸入您的電話，電話僅作為演出前的確認及資料識別之用，若您不願接受滿意度調查，志工在演出後將不會致電詢問');
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
    var allTickIds = $("#tickno").text();
    
    var procman = $('#procman').text();
    var confirmMsg = '請確認以下資料是否正確\n發票人:'+ procman
            +'\n索票人:'+ $('#audiencename').val()
            +'\n索票人電話:'+$('#audiencetel').val();
            //+'\n登錄票數:'+ newTickIdsCount;
//    if(!confirm(confirmMsg)){
//        return;
//    }
    //alert('功能開發測試中');
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
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次或通知系統管理者!');
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