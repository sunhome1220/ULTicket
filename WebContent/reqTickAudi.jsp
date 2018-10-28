<%@page import="util.User"%>
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
    <meta name="apple-mobile-web-app-capable" content="yes">
    <title>無限票務</title>
    <link rel="apple-touch-icon" href="assets/img/logo.png">
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
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!--    <link href="assets/css/custom-lite.css" rel="stylesheet" type="text/css" />-->
    <script>

    </script><script type="text/javascript" src="assets/js/styleswitcher.js"></script>
    <style>
        .Must {
            color: #FF0000;
        }
    </style>
    <%
    User user = (User) session.getAttribute("user");        
    %>
</head>

<body>
    <div class="container">            
            <div style="margin-left:-15px;margin-right:-15px">                    
                <nav class="navbar navbar-default " role="navigation">                    
                    <div class="container" style="padding-left: 0px;padding-right: 0px;">                        
                        <div class="navbar-header">                            
                            <div class="NAME">
                                無限創意表演團                                
                            </div>
                            
                            <!--   <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#offcanvas-right"><span class="sr-only">Toggle navigation</span><i class="icon-down-open"></i></button>-->
                        </div>
                        
                        <div class="collapse navbar-collapse Topmenu" id="offcanvas-left" role="navigation" style="padding-left: 0px;padding-right: 0px;">
                            <div id="MENU" class="menu">
                                <ul>
                                    <li>
                                        <a href="index.jsp" >說明</a>
                                    </li>                                    
                                </ul>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 id="heaerh2" class="panel-title">一般民眾索票登錄&nbsp;&nbsp;&nbsp;&nbsp;</h2>                        
                </div>
                <div class="panel-body">                        
                        <form id="Form12">
                            
                            <div class="row" style="display:none">
                                <div class="col-sm-6 col-xs-6">
<!--                                    <label><span class=Must>*</span>組別(依筆劃排序)</label> -->
                                    <select style="background:#baefe9" class="form-control" data-width="100px" id="team" name="team">
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
                                    <label><span class=Must>*</span>場次</label>
                                    <select class="form-control" data-width="100px" id="eventid" name="eventid">
                                        <option value="">請選擇演出場次</option>
<!--                                        <option value="20181014" selected>10/14-新竹公演(票號:20001~25000)</option>-->
                                        <option value="20181103">【餐桌上的記憶】-11/03-南門公演(票號:25001~30000)</option>
                                        <option value="20181125">【餐桌上的記憶】-11/25-板橋公演(票號:30001~35000)</option>
                                        <option value="20181229">【餐桌上的記憶】-12/29-板橋公演(票號:35001~40000)</option>
                                        <option value="20190101">【餐桌上的記憶】-01/01-國館公演(票號:40001~45000)</option>
                                </select>                                
                                </div>                                
                            </div>
                            
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <br>
                                    <label title="發票人就是傳此連結給您的人"><span class=Must></span>發票人姓名:</label> 
                                        <a id="procman"></a> (就是剛剛提供QRCode給您的服務志工)                                        
                                    </div>                    
                            </div>                                
                                
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must></span>索票人資料:</label>
                                </div>
                            </div>
                            <div class="row">
                                
                                <div class="col-sm-4 col-xs-4">                                    
                                    <input type="text" size="8" id="audiencename" maxlength="8" class="form-control" placeholder="您的大名">                                    
<!--                                    索票資料:<input type="tel" id="tickid" name="tickid" maxlength="5" class="form-control" placeholder="五碼票號">                                    -->
                                </div>
                                <div class="col-sm-4 col-xs-4">                                      
                                    <input type="tel" size="10" id="audiencetel" maxlength="10" class="form-control" title="索票人電話" placeholder="您的聯絡電話">                                    
                                </div>
                                <div class="col-sm-4 col-xs-10">                                      
                                    <input type="email" size="10" id="audienceEmail" maxlength="10" class="form-control" title="索票人email" placeholder="(選填)您的email">                                    
                                </div>
                            </div>
                            <div class="row" id="divHiXX002">
                                <div class="col-sm-4 col-xs-12">                                    
                                    <label>
                                      是否願意接受電話滿意度調查？
                                    </label>
                                    <input id="allowcontact" type="checkbox" class="btn-sm">
                                </div>                                
                            </div>
                            <div class="row" id="divHiXX02">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must></span>索取票號:</label>
                                    <a id="tickno"></a>
                                </div>
                            </div>
                            
                        </form>
                
                <div class="panel-footer" >                    
                    <div align="center">
                        <button class="btn btn-primary" style="" id="btnSubmit">確認輸入</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="reset" class="btn btn-default" id="btnClear">清除重填</button>
                    </div>
                </div>
            
            <div>
                <br>
                <div class="jqGrid">
                    <table id="QueryResult" style="display: none"></table>
                </div>
                <div id="QueryResultpagger"></div>
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
<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<script src="assets/js/reqTickAudi.js"></script>

</body>

</html>
