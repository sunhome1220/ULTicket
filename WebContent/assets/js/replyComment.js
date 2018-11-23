$(document).ready(function () {
//    $("a[id^=href]").click(function(){
//        window.location.href=this.id.replace('href_','')+'.jsp';
//    });
    localStorage.setItem("lastOperation", 'replyComment.jsp');
//    $("div[id^='divHide']").hide();
    //var allTicketsNos="";
    getTickCount();
    //$("#countSelf").attr("value", 123);
    $("#btnSubmit").attr("disabled", true);
    //$("#txtBirth").datepicker();
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
        $("#reqTel").text('');
        $("#reqTickNo").text('');
        $("#showTickNo").text('');
        $("div[id^='divHide']").hide();
    });

    $("input[name='contactStatus']").change(function () {
        //alert(this.value);
        $("#btnSubmit").attr("disabled", false);
    });

//    $("input[id^='tckno']").change(function(){//選擇所有的name屬性以’tckno'開頭的input元素
//        var ticknoValid = ticknoIsOk(this.value); 
//        if(this.value.length>0 && !ticknoValid){
//            alert('請確認票號是否正確');
//            this.focus();
//        }
//    }); 
//    $("#tickid").keyup(function(){        
//        if(this.value.length===5 && ticknoIsOk($("#tickid").val())){
//            getReqTickInfo(this.value);
//        }        
//    }); 
    $("#btnQuery").click(function () {
        if (ticknoIsOk($("#tickid").val())) {
            getReqTickInfo(this.value);
            getContactInfo(this.value);
            //return;
        } else {
            alert('票號輸入錯誤');
            return;
        }
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
    $("#tickid").change(function () {
        var ticknoValid = ticknoIsOk($("#tickid").val());
        if (ticknoValid) {
            $("#btnSubmit").attr("disabled", false);
            //$("#btnContinue2").attr("disabled",false);
        } else {
            $("#btnSubmit").attr("disabled", true);
            //$("#btnContinue2").attr("disabled",true);
        }
        //allTicketsNos
        //alert($("#tickid").val());
    });
    $("#eventid").change(function () {
        getTickCount();
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
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            action: action,
            ajaxAction: 'addComment',
            eventid: $('#eventid').val(),
            tickid: $('#tickid').val(),
            audiencename: $('#audiencename').val(),
            ticktel: $('#reqTel').text(),
            audiencecomment: $('#audiencecomment').val(),
            interest: $('#interestSelected').val(),
            comment: $('#comment').val(),
            contactStatus: $("input[name='contactStatus']:checked").val(),
            callTimes: 1

        },
        async: true,
        success: function (data) {
            alert(data.infoMsg);
            if (data.infoMsg.indexOf('成功') >= 0) {
                $("#tckid").val('');
            }
            getTickCount();

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

//取得已輸入回條數量
function getTickCount() {
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getTicCommentCount',
            eventid: $('#eventid').val(),
            statType: '', //$('#eventid').val(),
            username: $('#username').val()
        },
        async: false,
        success: function (data) {
            try {
                var jo = JSON.parse(data.infoMsg);
                //var percent = eval(jo.totalShowCnt)/eval(jo.totalReqCnt) * 100 + ' ';
                $("#countSelf").text(jo.countSelf);
//            $("#countTotal").text(jo.totalShowCnt + ' (' + percent.substring(0,4) + ' %)');
//            $("#countReqTick").text(jo.totalReqCnt);
            } catch (e) {
                alert('getTicCommentCount err! ' + e);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status === 901) {
                alert('連線逾時，請重新登入!');
                window.location.href = 'login.jsp';
            } else {
                alert('系統異常，無法取得票根資料!');
            }
        }
    });
}

//以票號取得該票之索票人索票的相關資訊
function getReqTickInfo() {
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
        async: true,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            $("#procman").text(jo.procman);
            $("#reqName").text(jo.reqName);
            $("#reqTel").text(jo.reqTel);
            $("#reqTel").attr("href", "tel:" + jo.reqTel);
            $("#reqTickNo").text(jo.reqTickNo);
            $("#showTickNo").text(jo.showTickNo);
            if (jo.reqTickNo === "0") {
                $("#btnSubmit").attr("disabled", true);
            }
            $("#contactperson").val(jo.username);
            $("input[name='contactStatus'][value=" + jo.contactStatus + "]").prop('checked', true);
            $("#audiencecomment").val(jo.audiencecomment);
            $("#audiencename").val(jo.audiencename);
            $("#lastupdatetime").text(jo.lastupdatetime);
            if (jo.audiencename) {
                //alert('此回條已輸入');
                $("#btnSubmit").text('更新資料');
                $("#btnSubmit").prop('disabled', false);
            } else {
                $("#btnSubmit").text('確定新增');
                //alert('未輸入過');
            }
            $("#comment").val(jo.comment);
            $("#interestSelected").text(jo.interest);
            $("input[name=interest][value=0]").prop("checked", false);
            var interests = jo.interest.split(',');
            for (i = 0; i < interests.length; i++) {
                //alert(interests[i]);
                $("input[name=interest][value=0]").click();
            }
            return;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status === 901) {
                alert('連線逾時，請重新登入!');
                window.location.href = 'login.jsp';
            } else {
                alert('系統異常，無法取得票券資料!');
            }
        }
    });
}

//以票號取得該票之索票人是否打過電話了
function getContactInfo() {
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getContactInfo',
            eventid: $('#eventid').val(),
            tickid: $('#tickid').val()
        },
        async: true,
        success: function (data) {
            var jo = JSON.parse(data.infoMsg);
            alert(jo.log);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status === 901) {
                alert('連線逾時，請重新登入!');
                window.location.href = 'login.jsp';
            } else {
                alert('系統異常，無法取得電話聯絡資料!');
            }
        }
    });
}