<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	import="util.*,base.BaseActionSupport"%>

<script>try {
        console.log('p3 start footer.jsp loadtime=' + (new Date() - startTime));
    } catch (e) {
    }</script>
<script src="assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="assets/plugins/bootstrap/js/bootstrap-lite.js"
	type="text/javascript"></script>
<script src="assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.js"
	type="text/javascript"></script>
<!--<script src="assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>-->
<!--  mega menu -->
<script type='text/javascript'
	src='assets/plugins/jquery-mega-drop-down-menu/js/jquery.hoverIntent.minified.js'></script>
<script type='text/javascript'
	src='assets/plugins/jquery-mega-drop-down-menu/js/jquery.dcmegamenu.1.3.3.js'></script>
<!-- validation -->
<script
	src="assets/plugins/jquery-validation/dist/jquery.validate.min.js"></script>
<!--	Noty	-->
<script src="assets/plugins/noty/jquery.noty.js"></script>
<script src="assets/plugins/noty/layouts/bottom.js"
	type="text/javascript"></script>
<script src="assets/plugins/noty/layouts/bottomCenter.js"
	type="text/javascript"></script>
<script src="assets/plugins/noty/themes/default.js"
	type="text/javascript"></script>
<!-- smartalert -->
<script src="assets/plugins/smartalert/js/alert.js"></script>
<!-- jqGrid -->
<script src="assets/plugins/jquery-jqgrid/i18n/grid.locale-tw.js"
	type="text/javascript"></script>
<script src="assets/plugins/jquery-jqgrid/jquery.jqGrid.min.js"></script>
<!-- datePicker -->
<script src="assets/plugins/WdatePicker/WdatePicker.js"
	type="text/javascript"></script>
<!-- Ajax Form -->
<script src="assets/plugins/jquery.form.js"></script>
<!-- fileDownload -->
<script src="assets/plugins/jquery.fileDownload.js"></script>
<!-- Upload -->
<script src="assets/plugins/jquery.ajaxfileupload.js"></script>
<!-- fileDownload -->
<script src="assets/plugins/jquery.blockui.min.js"></script>
<!-- JsonReturn -->
<script src="assets/plugins/jquery.serializeJSON.js"></script>
<!-- selectSearch -->
<script src="assets/plugins/select2.full.js"></script>
<!--dateFormat-->
<script src="assets/plugins/date.format.js"></script>
<!-- countdown -->
<!-- 啟用倒數計時功能時請 unmark -->
<!--<script src="assets/plugins/lwtCountdown/js/jquery.lwtCountdown-1.0.js"></script>-->
<!-- 自定義的共用js -->
<script src="assets/js/Footer.js"></script>
<!--修正IE的select和tooltip的問題-->
<script src="assets/plugins/jquery.tooltipFix.js"></script>
<script>
//	log('帳號所擁有的角色ownRole:'+ownRole);
jQuery(document).ready(function () {
//Metronic.init(); // init metronic core components
//Layout.init(); // init current layout
// Here we disable the hover rows option globally for IE8
var isIE8 = !!navigator.userAgent.match(/MSIE 8.0/) || navigator.userAgent.match(/MSIE 7.0/);
if (isIE8) {
    jQuery.extend(jQuery.jgrid.defaults, {hoverrows: false});
}
// initialize mega menu
$('#mega-menu-user').dcMegaMenu({
    rowItems: '1',
    speed: 'fast',
    effect: 'fade'
});
$('#mega-menu-4').dcMegaMenu({
    rowItems: '10',
    speed: 'fast',
    effect: 'fade'
});

//alert錯誤,警示,告知訊息
    <%
            String errorMsg = StringUtil.nvl(request
                    .getAttribute(BaseActionSupport.ERROR_MESSAGE));
            String warnMsg = StringUtil.nvl(request
                    .getAttribute(BaseActionSupport.WARN_MESSAGE));
            String infoMsg = StringUtil.nvl(request
                            .getAttribute(BaseActionSupport.INFO_MESSAGE));
            if (!errorMsg.equals("")) {%>
$.alert.open({
            type: 'error',
    content: '<%=errorMsg%>'
});
hasError = true;
    <%}%>
    <%if (!warnMsg.equals("")) {%>
$.alert.open({
            type: 'warning',
    content: '<%=warnMsg%>'
});
    <%}%>
    <%if (!infoMsg.equals("")) {%>
$.alert.open({
            type: 'info',
    content: '<%=infoMsg%>'
});
    <%}%>
//override jquery ui dialog widget _focusTabable method for IE8 using dialog with WdatePicker
$.widget("ui.dialog", $.ui.dialog, {
    _focusTabbable: function (ul, item) {
        return;
    }
});
});

//$(window).load(function () {
//var heartbeat = function () {
//    if (heart) {
//        clearInterval(heart);
//    }
//    var heart = setInterval(function () {
//        $.ajax({
//            url: 'HeartBeatServlet',
//            type: 'post',
//            datatype: 'json',
//            data: {
//                ajaxAction: 'heartbeat'
//            },
//            success: function () {
//            },
//            error: function () {
//            }
//        });
//    }, 1000 * 60 * 3);//每3分鐘執行一次
//};
//
//if (window.parent.mainFrameset && window.parent.mainFrameset.cols != '0,*') {
//    heartbeat();
//} else if (window.top.mainFrameset && window.top.mainFrameset.cols != '0,*') {
//    heartbeat();
//} else {
//    log('mainFrameset跑去那兒了~~');
//}
//});
</script>

<div id="alertDiv" style="font-size: 15px"></div>
<script>try {
        console.log('p4 end of footer.jsp loadtime=' + (new Date() - startTime));
    } catch (e) {
    }</script>
