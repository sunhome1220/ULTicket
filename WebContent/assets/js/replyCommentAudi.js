$(document).ready(function () {
    var d = new Date();  
    var month = d.getMonth()+1;
    var day = d.getDate();
    var strTodayDate = d.getFullYear() + '' +
        (month<10 ? '0' : '') + month + '' +
        (day<10 ? '0' : '') + day;
    
    var eventid = getUrlParameter('evid');
    if(eventid !== strTodayDate){
        if(!confirm('今天並非是演出日(' + eventid + ')，是否確定要新增資料？')){
            return;
        }
    }
    $("#eventid").val(eventid);
    $("#eventid").attr("disabled", true);
    
    //localStorage.setItem("lastOperation", 'replyCommentAudi.jsp');
    $("#btnSubmit").click(function () {
        submitData();
    });
    $("#sameAsProcman").click(function () {
        $("#contactperson").val($("#procman").text());
    });
    $("#sameAsMe").click(function () {
        $("#contactperson").val($("#username").text().split(',')[0]);
    });
    $("#sameAsReqMan").click(function () {
        $("#audiencename").val($("#reqName").text());
    });
    $("#btnClear").click(function () {
//        $("#tickid").val('');
//        $("#audiencename").val('');
//        $("#age").val('');
//        $("#reqTel").val('');
//        $("#audiencecomment").val('');        
        location.reload();
    });

    
    $("input[name=interest]").change(function () {
        var oldValue = $("#interestSelected").val();
        if (this.checked) {
            $("#interestSelected").val(oldValue + this.value + ",");
        } else {
            $("#interestSelected").val(oldValue.replace(this.value + ",", ""));
        }
    });    
});


function ticknoIsOk(ticketNo) {
    if ($("#eventid").val() === '20181014') {
        return ticketNo >= 20001 && ticketNo <= 25000;
    } else if ($("#eventid").val() === '20181103') {
        return ticketNo >= 25001 && ticketNo <= 30000;
    } else if ($("#eventid").val() === '20181125') {
        return ticketNo >= 30001 && ticketNo <= 35000;
    } else if ($("#eventid").val() === '20181229') {
        return ticketNo >= 35001 && ticketNo <= 40000;
    } else if ($("#eventid").val() === '20190101') {
        return ticketNo >= 1 && ticketNo <= 5000;
    }
}
function submitData() {//送出票根資料
    var localStoreId = 'CommentSubmitCnt';// + $('#eventid').val();
    var limited = 10;
    if(localStorage.getItem(localStoreId)){
        if(eval(localStorage.getItem(localStoreId)) > limited){
            alert('很抱歉，此裝置已填寫回條超過上限次數(' + limited +')。\n\n若您仍需繼續填寫,請先登入系統，謝謝');
            return;
        }
    }
    var confirmMsg = "";
    var action = 'create';
    if ($("#btnSubmit").text() === '更新資料') {
        action = 'update';
    }      
    if($('#audiencename').val().length <= 1 || (!isNaN($('#audiencename').val()))){
        alert('請確認姓名是否輸入正確');
        confirmMsg += '請確認姓名是否輸入正確\n';
        //$('#audiencename').focus();
        $('#audiencename').select();
        return;
    }
    if($('#age').val()!=='' && (isNaN($('#age').val()) || $('#age').val() < 1 || $('#age').val() > 140)){
        alert('請輸入正確的年齡(1~140)');
        confirmMsg += '年齡資料輸入錯誤\n';
        $('#age').select();
        return;
    }
//    if($('#reqTel').val()===''){
//        alert('請輸入您的聯絡電話號碼');
//        $('#reqTel').focus();
//        return;
//    }
    //alert('==='+$("input[name='satisfaction']:checked").val()+'===');
    if(!$("input[name='satisfaction']:checked") || $("input[name='satisfaction']:checked").val()===''){
        alert('請輸入滿意度狀況');
        return;
    }
    if($('#tickid').val()!=='' && !ticknoIsOk($('#tickid').val())){
        alert('請確認票號是否正確，若忘記號碼或票券遺失請留空白即可');        
        $('#tickid').focus();
        return;
    }
    if($('#tickid').val()===''){
        confirmMsg += '票號為您所持門票上5碼數字，若忘記號碼或票券遺失請留空白即可\n\n';
    }
    if($('#interestSelected').val()===''){
        confirmMsg += '尚未勾選有興趣課程\n';
//        if(!confirm(confirmMsg)){
//            return;
//        }
    }else{
        if($('#reqTel').val()===''){
            confirmMsg += '您有勾選課程但未輸入聯絡電話\n\n您的資料僅作為課程及演出資訊的通知，不作他用，請安心填寫\n';
//            if(!confirm(confirmMsg)){
//                return;
//            }
        }
    }
    if(confirmMsg!=='' && !confirm(confirmMsg + '是否確認送出資料？')){
        return;
    }else if(confirmMsg==='' && !confirm('是否確認送出資料？')){
        return;        
    }
    
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            action: action,
            ajaxAction: 'addCommentAudi',
            eventid: $('#eventid').val(),
            tickid: $('#tickid').val(),
            audiencename: $('#audiencename').val(),
            ticktel: $('#reqTel').val(),
            audiencecomment: $('#audiencecomment').val(),
            interest: $('#interestSelected').val(),
            satisfaction: $("input[name='satisfaction']:checked").val(),
            age: $('#age').val(),
            submitCnt: eval(localStorage.getItem(localStoreId)) + 1
        },
        async: true,
        success: function (data) {
            //localStorage.setItem('committedCnt',0);
            if(data.infoMsg){
                if(localStorage.getItem(localStoreId)){
                    localStorage.setItem(localStoreId, eval(localStorage.getItem(localStoreId))+1);
                }else{
                    localStorage.setItem(localStoreId, 1);
                }
                alert(data.infoMsg);
                if (data.infoMsg.indexOf('成功') >= 0) {                    
                }            
            }else{
                alert(data.exceptionMsg);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            //alert('ajaxOptions=' + ajaxOptions);
//            alert(xhr.status);
//            alert(thrownError);
            if (xhr.status === 901) {
                alert('連線逾時，請重新登入!');
                window.location.href = 'login.jsp';
            } else {
                alert('系統異常，請重新操作一次或通知系統管理者!');
            }
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

function ticknoIsOk(ticketNo) {
    //alert(ticketNo);
    if ($("#eventid").val() === '20181014') {
        return ticketNo >= 20001 && ticketNo <= 25000;
    } else if ($("#eventid").val() === '20181103') {
        return ticketNo >= 25001 && ticketNo <= 30000;
    } else if ($("#eventid").val() === '20181125') {
        return ticketNo >= 30001 && ticketNo <= 35000;
    } else if ($("#eventid").val() === '20181229') {
        return ticketNo >= 35001 && ticketNo <= 40000;
    } else if ($("#eventid").val() === '20190101') {
        return ticketNo >= 1 && ticketNo <= 5000;
    }
}