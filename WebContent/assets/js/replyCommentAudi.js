$(document).ready(function () {
    var eventid = getUrlParameter('evid');
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
        $("input").val('');
        $("#procman").text('');
        $("#reqName").text('');
//        $("#reqTel").text('');
        $("#reqTel").val('');
        $("#reqTickNo").text('');
        $("#showTickNo").text('');
        //$("div[id^='divHide']").hide();
    });

    
    $("input[id^='tckno']").blur(function () {//選擇所有的name屬性以’tckno'開頭的input元素
        //alert(this.value);
        var ticknoValid = ticknoIsOk(this.value);
        if (this.value.length > 0 && !ticknoValid) {
            //alert('請確認票號是否正確');
            //this.focus();
        }
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
    var action = 'create';
    if ($("#btnSubmit").text() === '更新資料') {
        action = 'update';
    }
    if($('#tickid').val()===''){
        alert('請輸入門票號碼');
        $('#tickid').focus();
        return;
    }
    if(!ticknoIsOk($('#tickid').val())){
        alert('請確認票號是否正確');
        $('#tickid').focus();
        return;
    }    
    if($('#audiencename').val()===''){
        alert('請輸入您的姓名');
        $('#audiencename').focus();
        return;
    }
    if($('#reqTel').val()===''){
        alert('請輸入您的聯絡電話號碼');
        $('#reqTel').focus();
        return;
    }
    if($("input[name='satisfaction']:checked").val()===''){
        alert('請輸入滿意度狀況');
        return;
    }
    if($('#interestSelected').val()===''){
        if(!confirm('尚未勾選有興趣課程，若要增加課程項目請按取消')){
            return;
        }
    }else{
//        if(!confirm('是否確定要送出意見回條資料？')){
//            return;
//        }
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
            age: $('#age').val()
        },
        async: true,
        success: function (data) {
            if(data.infoMsg){
                alert(data.infoMsg);
                if (data.infoMsg.indexOf('成功') >= 0) {
                    $("input").val('');
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