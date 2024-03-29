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

</head>

<body>    
    <section id="MainContent">
        <nav class="navbar navbar-default megamenu" role="navigation">  
            <div class="container" style="padding-left: 0px;padding-right: 0px;"> 
                <div class="navbar-header">                            
                    <button type="button" class="navbar-toggle pull-left" data-toggle="collapse" data-target="#offcanvas-left"><span class="sr-only">Toggle navigation</span><i class="icon-menu"></i></button>
                    <div class="Logo">
                        <img id='top_img' alt="無限創意表演團" src="assets/img/logo4.png"  width="80"  height="80">
                    </div>                            
                    <div class="NAME">
                        無限創意表演團                                
                    </div>

                    <!--   <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#offcanvas-right"><span class="sr-only">Toggle navigation</span><i class="icon-down-open"></i></button>-->
                </div>
            </div>
        </nav>
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 id="heaerh2" class="panel-title">回條資料輸入&nbsp;&nbsp;&nbsp;&nbsp;</h2>  
                    請提供您寶貴的意見,使我們下次演出更進步,謝謝。                    
                </div>
                <div class="panel-body">                        
                    <form id="Form12">
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">
                                <label>場次</label> 
                                <select class="form-control" data-width="100px" id="eventid" name="eventid">
                                    <option value="20181014" selected>10/14-新竹公演(票號:20001~25000)</option>
                                    <option value="20181103">11/03-南門公演(票號:25001~30000)</option>
                                    <option value="20181125">11/25-板橋公演(票號:30001~35000)</option>
                                    <option value="20181229">12/29-板橋公演(票號:35001~40000)</option>
                                    <option value="20190101">01/01-國館公演(兌換券:1~5000)</option>
                                </select>                                
                            </div>

                        </div>                                                       
                        <div class="row">
                            <div class="col-sm-4 col-xs-6">                                    
                                <input type="tel" id="tickid" name="tickid" maxlength="5" class="form-control" placeholder="門票號碼">                                
                            </div>
<!--                            <div class="col-sm-4 col-xs-6">                                    
                                <a>若票券遺失可不填</a>
                            </div>-->
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">
                                <label><span class=Must>*</span>個人資料:</label>
                            </div>
                        </div>                            
                        <div class="row">
                            <div class="col-sm-4 col-xs-6">                                    
                                <input type="text" id="audiencename" name="audiencename" maxlength="12" class="form-control" placeholder="您的姓名">                                    
                            </div>
                            <div class="col-sm-4 col-xs-4">                                    
                                <input type="tel" id="age" name="age" maxlength="3" class="form-control" placeholder="年齡(選填)">                                    
                            </div>
                        </div>                                                       
                        <div class="row">
                            <div class="col-sm-4 col-xs-6">                                    
                                <input type="tel" id="reqTel" maxlength="12" class="form-control" placeholder="聯絡電話(選填)">                                    
                            </div>
<!--                            <div class="col-sm-4 col-xs-6">                                    
                                <input type="tel" id="reqTel"  maxlength="12" class="form-control" placeholder="聯絡電話-市話(選填)">                                    
                            </div>-->
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">
                                <label><span class=Must>*</span>滿意度:</label>
                                <input name="satisfaction" type="radio" value="2">非常滿意
                                <input name="satisfaction" type="radio" value="1" checked>滿意
                                <input name="satisfaction" type="radio" value="0">不滿意                                    
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">                                    
                                <textarea id="audiencecomment" name="Text1" cols="40" rows="1" placeholder="感想或建議(選填)" class="form-control"></textarea>                                    
                            </div>
                            <div class="col-sm-4 col-xs-6">   
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">
                                <label><span></span>有興趣課程:</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-xs-12">                                                                        
                                <input name="interest" type="checkbox" value="0">心靈課程
                                <input name="interest" type="checkbox" value="1">和太鼓
                                <input name="interest" type="checkbox" value="2">節奏樂團<br>
                                <input name="interest" type="checkbox" value="3">陶笛
                                <input name="interest" type="checkbox" value="4">益智桌遊
                                <input name="interest" type="checkbox" value="5">兒童美術美勞<br>
                                <input name="interest" type="checkbox" value="6">女性有氧瑜珈
                                <input name="interest" type="checkbox" value="7">流行舞蹈
                                <input name="interest" type="checkbox" value="8">桌球                                    
                                <input name="interest" type="checkbox" value="9">籃球<br>
                                <input name="interest" type="checkbox" value="A">毛線編織班
                                <input name="interest" type="checkbox" value="B">表演課
                                <input name="interest" type="checkbox" value="C">蔬食烹飪料理
                                <input id="interestSelected" type="hidden" value=""/>
                                <!--                                    <input type="range" id="rateStar" name="tckno2" min="1" max="3">-->
                            </div>
                            <div class="col-sm-4 col-xs-6">   
                            </div>
                        </div>
                    </form><br>
                    <!--                    </section>-->

                    <div class="panel-footer">                    
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
                    <script src="assets/js/utils.js"></script>
                    <script src="assets/js/replyCommentAudi.js"></script>
                    </body>

                    </html>
