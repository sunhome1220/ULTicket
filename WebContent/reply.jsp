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
<!--    <link href="assets/css/custom-lite.css" rel="stylesheet" type="text/css" />-->
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
                    <h2 id="heaerh2" class="panel-title">票根輸入-20181014新竹公演-陳測試</h2>    
                    
<!--                    <h2 id="heaerh22" class="panel-title">此帳號本場輸入數:10</h2>-->
<!--                    <h2 id="heaerh23" class="panel-title">本場總輸入票根數:sp;1002</h2>-->
                </div>
                <div class="panel-body">
                    
                        <form id="Form12">
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>票號</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno1" name="tckno1" maxlength="5" class="form-control" placeholder="票號1">                                    
                                </div>
                                <div class="col-sm-4 col-xs-6">  
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno2" name="tckno2" maxlength="5" class="form-control" placeholder="票號2">
                                </div>
                                <div class="col-sm-4 col-xs-6">                                                                        
                                    <input id="btnContinue2" disabled type="button" value="連號">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno3" name="tckno2" maxlength="5" class="form-control" placeholder="票號3">
                                </div>
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input id="btnContinue3" disabled type="button" value="連號">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno4" name="tckno2" maxlength="5" class="form-control" placeholder="票號4">
                                </div>
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input id="btnContinue4" disabled type="button" value="連號">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno5" name="tckno2" maxlength="5" class="form-control" placeholder="票號5">
                                </div>
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input id="btnContinue5" disabled type="button" value="連號">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tckno6" name="tckno2" maxlength="5" class="form-control" placeholder="票號6">
                                </div>
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input id="btnContinue6" disabled type="button" value="連號">
                                </div>
                            </div>
<!--                            <div class="row">
                                <input type="tel" id="tckno5" name="tckno5" maxlength="5" class="form-control" placeholder="票號5">
                                <input type="tel" id="tckno6" name="tckno6" maxlength="5" class="form-control" placeholder="票號6">
                                <input type="tel" id="tckno7" name="tckno7" maxlength="5" class="form-control" placeholder="票號7">
                                <input type="tel" id="tckno8" name="tckno8" maxlength="5" class="form-control" placeholder="票號8">
                                    <input type="text" id="tckno9" name="tckno9" maxlength="5" class="form-control" placeholder="票號9">
                                <input type="text" id="tckno10" name="tckno10" maxlength="5" class="form-control" placeholder="票號10">
                            </div>-->

                        </form>
                    </section>
                </div>
                <div class="panel-footer">                    
                    <div align="center">
                        <button class="btn green btn-sm" id="btnQry">
                            <i class="fa fa-search"></i>&nbsp;確認輸入</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="reset" class="btn btn-default" id="btnClear">清除</button>
                    </div>
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
<!--<script src="assets/plugins/bootstrap-3.3.6/dist/js/bootstrap.min.js"></script>-->
<script src="assets/plugins/bootstrap/js/bootstrap.min.js"></script>
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
<script src="assets/js/reply.js"></script>
</body>

</html>
