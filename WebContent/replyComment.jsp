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
    <jsp:include page="menu.jsp" />  
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 id="heaerh2" class="panel-title">回條資料輸入&nbsp;&nbsp;&nbsp;&nbsp;</h2>  
                    觀眾觀賞完離場後繳交的意見回條
                </div>
                <div class="panel-body">                        
                        <form id="Form12">
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>場次</label> 
                                    <select class="form-control" data-width="100px" id="eventid" name="eventid">
                                        <option value="">請選擇</option>
                                        <option value="20181014" selected>10/14-新竹公演(票號:20001~25000)</option>
                                        <option value="20181103">11/03-南門公演(票號:25001~30000)</option>
                                        <option value="20181125">11/25-板橋公演(票號:30001~35000)</option>
                                        <option value="20181229">12/29-板橋公演(票號:35001~40000)</option>
                                        <option value="20190101">01/01-國館公演(兌換券:1~5000)</option>
                                </select>                                
                                </div>
                                
                            </div>
                            <div class="row">
                                    <div class="col-sm-6 col-xs-12">
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此場次您已輸入回條數目：<a id="countSelf">...</a><br>                                    
                                    </div>
                                <br>
                            </div>     
                            <br>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>索票資料/意見回條:</label>
                                </div>
                            </div>                            
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    <input type="tel" id="tickid" name="tickid" maxlength="5" class="form-control" placeholder="意見回條的票號">                                    
                                </div>
                                <div class="col-sm-4 col-xs-6">   
                                    <input type="button" class="btn btn-info" id="btnQuery" value="查詢票號"/>
                                </div>
                            </div>
                            <div class="row" id="divHide1" style="display:none">
                                <div class="col-sm-4 col-xs-6">
                                    發票人: <a id="procman"></a><br>                                    
<!--                                    電　話: <a id="reqTel" href="tel:"></a><br>                                                                      -->
                                    電　話: <input style="border-color: gray" type="tel" size="12" id="reqTel" maxlength="21" class="" placeholder="電話"><br>                                                                      
                                </div>
                                <div class="col-sm-4 col-xs-6">
                                    索票人: <a id="reqName"></a><br>
                                    出席/索票數: <a id="showTickNo"></a>/<a id="reqTickNo"></a><br>                                    
                                </div>
                            </div>
                            <div class="row" id="divHide2" style="display:none">
<!--                                <div class="col-sm-4 col-xs-6">                                    
                                    聯絡人　:<input type="text" size="10" id="contactperson" maxlength="10" class="" placeholder="">                                    
                                </div>-->
                                <div class="col-sm-4 col-xs-6">                                    
                                    聯絡人: <input style="border-color: gray" type="text" size="12" id="contactperson" maxlength="10" class="" placeholder="聯絡人姓名">                                    
                                </div>
                                
                                <div class="col-sm-4 col-xs-6">                                                                        
                                    <a style="color:#42c8f4" id="sameAsProcman">同發票人</a>&nbsp;&nbsp;
                                    <a style="color:#f441e2" id="sameAsMe">就是我</a>
                                </div>
                            </div>                            
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
<!--                                    <label><span></span>回條資料:</label>-->
                                </div>
                            </div>                            
                            <div class="row" id="divHide3" style="display:none">
                                <div class="col-sm-4 col-xs-6">                                    
                                    觀　眾: <input type="text" id="audiencename" style="border-color: gray" size="12" maxlength="10" class="" placeholder="觀眾姓名">
                                </div>                                
                                <div class="col-sm-4 col-xs-6">                                                                        
                                    <a style="color:#42c8g1" id="sameAsReqMan">就是索票人本人</a>&nbsp;&nbsp;                                    
                                </div>                                
                            </div>
                            <div class="row" id="divHide32" style="display:none">
                                <div class="col-sm-4 col-xs-6">                                    
                                    年　齡: <input type="tel" id="age" style="border-color: gray" size="12" maxlength="2" class="" placeholder="">
                                </div>                                
                                <div class="col-sm-4 col-xs-6">                                                                                                            
                                </div>                                
                            </div>
                            <div class="row" id="divHide4" style="display:none">
                                <div class="col-sm-6 col-xs-12">                                    
                                    <textarea id="audiencecomment" name="Text1" cols="40" rows="1" placeholder="觀眾評論備註" class="form-control"></textarea>                                    
                                </div>
                                <div class="col-sm-4 col-xs-6">   
                                </div>
                            </div>
                            <div class="row" id="divHide51" style="display:none">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>滿意度:</label>
                                    <input name="satisfaction" type="radio" value="2">非常滿意
                                    <input name="satisfaction" type="radio" value="1">滿意
                                    <input name="satisfaction" type="radio" value="0">不滿意                                    
                                </div>
                            </div>
                            <div class="row" id="divHide61" style="display:none">
                                <div class="col-sm-6 col-xs-12">     
                                    
                                </div>                                
                            </div>
                            <div class="row" id="divHide5" style="display:none">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>聯絡狀況:</label>
                                </div>
                            </div>
                            <div class="row" id="divHide6" style="display:none">
                                <div class="col-sm-6 col-xs-12">     
                                    <input name="contactStatus" type="radio" value="0">尚未聯絡
                                    <input name="contactStatus" type="radio" value="1">有上課意願
                                    <input name="contactStatus" type="radio" value="2">有興趣但暫無時間<br>
                                    <input name="contactStatus" type="radio" value="3">電話錯誤或空號
                                    <input name="contactStatus" type="radio" value="4">完全沒興趣
<!--                                    <input type="range" id="rateStar" name="tckno2" min="1" max="3">-->
                                </div>
                                <div class="col-sm-4 col-xs-6">   
                                </div>
                            </div>
                            <div class="row" id="divHide32" style="display:none">
                                <div class="col-sm-6 col-xs-12">                                    
                                    上次資料更新/聯絡時間:<a id="lastupdatetime"></a>
                                </div>                                
                            </div>
                            
                            <div class="row" id="divHide7" style="display:none">
                                <div class="col-sm-6 col-xs-12">                                    
                                    <textarea id="comment" name="Text1" cols="40" rows="1" placeholder="備註" class="form-control"></textarea>                                    
                                </div>
                                <div class="col-sm-4 col-xs-6">   
                                </div>
                            </div>                            
                            <div class="row" id="divHide8" style="display:none">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>有興趣課程:</label>
                                </div>
                            </div>
                            <div class="row" id="divHide9" style="display:none">
                                <div class="col-sm-6 col-xs-12">                                                                        
                                    <input name="interest" type="checkbox" value="0">心靈課程
                                    <input name="interest" type="checkbox" value="1">和太鼓
                                    <input name="interest" type="checkbox" value="2">節奏樂團<br>
                                    <input name="interest" type="checkbox" value="3">陶笛
                                    <input name="interest" type="checkbox" value="4">桌遊
                                    <input name="interest" type="checkbox" value="5">兒童美勞<br>
                                    <input name="interest" type="checkbox" value="6">女性有氧瑜珈
                                    <input name="interest" type="checkbox" value="7">流行舞蹈
                                    <input name="interest" type="checkbox" value="8">桌球                                    
                                    <input name="interest" type="checkbox" value="9">籃球<br>
                                    <input name="interest" type="checkbox" value="A">手工藝
                                    <input name="interest" type="checkbox" value="B">表演課
                                    <input name="interest" type="checkbox" value="C">烹飪料理
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
<script src="assets/js/replyComment.js"></script>
</body>

</html>
