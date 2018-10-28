<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>

<head>
    <meta charset="utf-8">
    <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">           
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="Category.Theme" content="170">
    <meta name="Category.Cake" content="140">
    <meta name="Category.Service" content="E10">
    <title>無限會員管理系統</title>
    <link rel="stylesheet" href="assets/plugins/bootstrap-3.3.6/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/plugins/bootstrap-3.3.6/dist/css/bootstrap-theme.min.css">	
    <link rel="stylesheet" href="assets/css/megamenu.css">
    <link rel="stylesheet" href="assets/plugins/slick/slick.css">
    <link rel="stylesheet" href="assets/plugins/slick/slick-theme.css">
    <link rel="stylesheet" href="assets/css/slick-slide.css">
    <link rel="stylesheet" href="assets/plugins/fontello/css/fontello.css">
    <link rel="stylesheet" href="assets/css/customize.css">
    <link rel="stylesheet" href="assets/css/design.css">
    <link rel="stylesheet" href="assets/plugins/grid/grid-Microsoft.CUF.css">
    <link rel="stylesheet" href="assets/plugins/grid/grid-mobile.css">
    <link rel="stylesheet" href="assets/plugins/jquery-ui-1.12.1.custom/jquery-ui.min.css">
    <script>
    </script><script type="text/javascript" src="assets/js/styleswitcher.js"></script>
<style>
    .Must {
        color: #FF0000;
    }
</style>
</head>

<body>
    <jsp:include page="menu.jsp" />  
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">觀眾索票連結：<span class=Must>規劃設計中</span></h2>
                </div>
                <div class="panel-body">
                    <section>
                        <form id="Form1">
                            <div class="row" style="background:">
                                <div class="col-sm-6 col-xs-6">
<!--                                    <label><span class=Must>*</span>組別(依筆劃排序)</label> -->
                                    <select class="form-control" data-width="100px" id="team" name="team">
                                        <option value="">請選擇組別(依筆劃排序)</option>                                        
                                        <option value="弘恩">弘恩</option>
                                        <option value="永樂">永樂</option>
                                        <option value="合歡">合歡</option>
                                        <option value="恆德">恆德</option>
                                        <option value="博愛">博愛</option>
                                        <option value="復興">復興</option>
                                        <option value="藍天">藍天</option>   
                                    </select>                                
                                </div>                                                                                             
                            </div>
                            
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label></label> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
<!--                                    <label><span class=Must>*</span>場次</label> -->
                                    <select class="form-control" data-width="100px" id="eventid" name="eventid">
                                        <option value="">請選擇演出場次</option>
<!--                                        <option value="20181014" selected>10/14-新竹公演(票號:20001~25000)</option>-->
                                        <option value="20181103">11/03-南門公演(票號:25001~30000)</option>
                                        <option value="20181125">11/25-板橋公演(票號:30001~35000)</option>
                                        <option value="20181229">12/29-板橋公演(票號:35001~40000)</option>
                                        <option value="20190101">01/01-國館公演(票號:40001~45000)</option>
                                </select>                                
                                </div>                                    
                            </div>
                            <div class="row">                                
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno1" maxlength="5" class="form-control" placeholder="票號1">                                    
                                </div>                                
                            </div>
                            <div class="row">                                
                                <div class="col-sm-4 col-xs-6">  
                                    <input type="tel" id="tckno2" maxlength="5" class="form-control" placeholder="票號2">
                                </div>                                                                                            
                            </div>
                            <div class="row">                                
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno3" maxlength="5" class="form-control" placeholder="票號3">
                                </div>                                
                            </div>
                            <div class="row">                                
                                <div class="col-sm-4 col-xs-6">   
                                    <input type="tel" id="tckno4" maxlength="5" class="form-control" placeholder="票號4">                                                                        
                                </div>                                        
                            </div>
                        </form>
                        <div align="center">
                            <input type="button" class="btn btn-primary" id="btnShowQRCode" value="產生觀眾索票QRCode"/>                     
                        </div>
                    </section>
                </div>
                <div class="panel-footer">
                    <img id='qrcode' src='#' alt="QRCode" style="display:none"/>
<!--                    <div align="center" id="qrcode" style="display:none" >
                        <div align="center">
                                    <img src="assets/img/reqticQRCode.png" alt="Smiley face" height="70%" width="70%">
                                </div>
                    </div>-->
                    
                </div>
            </div>
            <div>
                <br>
                <div class="jqGrid">
                    <table id="QueryResult" style="display: none"></table>
                </div>
                <div id="QueryResultpagger"></div>
            </div>
        </div>
    </section>

    <a href="#" class="scrollup"><i class="icon-up-open"></i></a>
    <script src="assets/js/jquery-1.11.2.js"></script>
    <script src="assets/plugins/bootstrap-3.3.6/dist/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.lazyload.min.js"></script>
    <script src="assets/plugins/slick/slick.js"></script>
    <script src="assets/js/bootstrap.offcanvas.js"></script>
    <script src="assets/plugins/fullcalendar-2.3.1/lib/moment.min.js"></script>
    <script src="assets/plugins/fullcalendar-2.3.1/fullcalendar.min.js"></script>
    <script src="assets/js/jquery.scrollUp.js"></script>
    <script src="assets/js/customize_scripts.js"></script>
    <script src="assets/plugins/grid/jquery.jqGrid.src.js"></script>
    <script src="assets/plugins/grid/grid.locale-zh.js"></script>
    <script src="assets/plugins/grid/Microsoft.Common.js"></script>
    <script src="assets/plugins/grid/Microsoft.jqGrid.js"></script>
    <script src="assets/plugins/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
    <script src="assets/js/datepicker-zh-TW.js"></script>
    <script src="assets/js/audienceReqTic.js"></script>
<!--    <script src="assets/js/query.js"></script>-->
</body>

</html>
