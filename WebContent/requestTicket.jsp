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
    <jsp:include page="menu.jsp" />  
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 id="heaerh2" class="panel-title">索票登錄-新增&nbsp;&nbsp;&nbsp;&nbsp;</h2>                        
                </div>
                <div class="panel-body">                        
                        <form id="Form12">
                            
                            <div class="row" style="background:">
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
<!--                                    <label><span class=Must>*</span>場次</label> -->
                                    <select style="background:#baefe9" class="form-control" data-width="100px" id="eventid" name="eventid">
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
                                <div class="col-sm-6 col-xs-12">
                                    <font style="color:purple">&nbsp;&nbsp;&nbsp;本場次您已登錄門票數目：<a id="countSelf">...</a></font> (發票人是自己)<br>                                                                        
                                    <font style="color:purple">&nbsp;&nbsp;&nbsp;本場次您幫他人登錄數目：<a id="countSelf2">...</a></font> (發票人是別人)<br>                                    
                                    <br>
                                </div>                                     
                            </div>    
                            
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label title="發票人可能是自己或其他伙伴"><span class=Must></span>發票人:</label> 
                                    <div class="btn-group btn-group" role="group" aria-label="可能是自己或其他伙伴">
                                        <input type="radio" name="procmanType" id="procman1" title="發票人就是您本人" value="S" checked>登錄人本人(<%=user.getUserName()%>)
                                        <input type="radio" name="procmanType" id="procman2" value="O">其他伙伴:                                        
                                        <input type="text" size="8" id="procmanOther" style="display:none" title="看您是幫哪位伙伴登錄資料" placeholder="伙伴姓名"/>                                                                           
                                        <input type="hidden" id="loginUser" value='<%=user.getUserName()%>'/>                                                                                                                   
                                    </div>                    
                                </div>                                
                                <div class="col-sm-6 col-xs-5">                                    
                                    
                                </div>                                
                            </div>
                            
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span></span>索票資料:</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-4">                                    
                                    <input type="text" size="8" id="audiencename" maxlength="8" class="form-control" placeholder="索票人姓名">                                    
<!--                                    索票資料:<input type="tel" id="tickid" name="tickid" maxlength="5" class="form-control" placeholder="五碼票號">                                    -->
                                </div>
                                <div class="col-sm-4 col-xs-4">                                      
                                    <input type="tel" size="10" id="audiencetel" maxlength="10" class="form-control" title="索票人電話" placeholder="索票人電話">                                    
                                </div>
                                <div class="col-sm-4 col-xs-4">                                      
<!--                                    <input id="btnQuery" class="btn btn-primary"  type="button" value="查詢索票記錄">-->
                                    <input id="btnAdd" class="btn btn-success"  type="button" value="新增" style="display:none">
                                    <input id="btnMod" class="btn btn-danger"  type="button" value="修改" style="display:none">
                                </div>
                            </div>
                            <div class="row" id="divHiXX001">
                                <div class="col-sm-6 col-xs-4">                                    
                                    <input type="text" size="8" id="procaddr" maxlength="8" class="form-control" title="索票地點" placeholder="索票地點(選填)">                                    
                                </div>
                                <div class="col-sm-4 col-xs-8">
                                    <textarea id="tickmemo" name="Text1" cols="40" rows="1" placeholder="備註" class="form-control"></textarea>                                    
                                </div>
                            </div>
<!--                            <div class="row">
                                    <div class="col-sm-6 col-xs-12">
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此場次該索票人已索取門票數目：<a id="countSelf">...</a>)<br>                                    
                                    </div>                                
                            </div> -->
                            <div class="row" id="divHiXX002">
                                <div class="col-sm-4 col-xs-12">                                    
                                    <label>
                                      是否願意接受電話滿意度調查？
                                    </label>
                                    <input id="allowcontact" type="checkbox" class="btn-sm">
                                </div>
                                
<!--                                    <input type="checkbox">願意接受電話滿意度調查-->                                
                            </div>
                            <div class="row" id="divSeatType">
                                <div class="col-sm-4 col-xs-12">                                    
                                    <label>
                                      座位類別
                                    </label>
                                    <div class="btn-group btn-group" role="group">
                                        <input name="seatType" type="radio" class="btn-sm" checked>一般&nbsp;
                                        <input name="seatType" type="radio" class="btn-sm">貴賓席&nbsp;
                                        <input name="seatType" type="radio" class="btn-sm">親子區座位                                         
                                    </div>
                                </div>
                            </div>
                            <div class="row" id="divConfirmStatus">
                                <div class="col-sm-4 col-xs-12">                                    
                                    <label>
                                      確認狀態
                                    </label>
                                    <div class="btn-group btn-group" role="group">
                                        <input type="radio" name="confirmStatus" value="-1">請假&nbsp;
                                        <input type="radio" name="confirmStatus" value="0" checked>未確認&nbsp;
                                        <input type="radio" name="confirmStatus" value="1">確認出席                                        
                                    </div>
                                </div>
                            </div>
<!--                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <font style="color:purple">&nbsp;&nbsp;&nbsp;本場次此索票人索票總數：<a id="audienceEvidCnt">...</a></font><br>                                    
                                </div>
                            </div>-->
<!--                            <div class="row" id="divH12">
                                <div class="col-sm-6 col-xs-12">
                                    <font style="color:purple">&nbsp;&nbsp;&nbsp;累計總出席及索票數:<a id="showTickNo">0</a>/<a id="reqTickNo">0</a></font><br>                                    
                                </div>
                            </div>-->
                            <div class="row" id="divHiXX01">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>登錄張數: </label> 
                                    <div class="btn-group btn-group" role="group" aria-label="索票張數">
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo1" value="1" checked>１
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo2" value="2">２
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo3" value="3">３
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo4" value="4">４
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo5" value="5">５
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo6" value="6">６
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo7" value="7">７
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo8" value="8">８
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo9" value="9">９
                                        <input type="radio" name="ReqTickNo" id="ReqTickNo10" value="10">１０                                        
                                    </div>
                                </div>                                
                            </div>    
                            
                            <div class="row" id="divHiXX02">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>票號(可由系統自動連號或全部自行輸入):</label>
                                </div>
                            </div>
                            <div class="form-group row" id="divHiXX03">
                                <input type="hidden" id="originalAllTickNo">                                    
                                <div class="col-sm-4 col-xs-3">                                    
                                    <input type="tel" id="tckno1" maxlength="5" class="form-control" placeholder="票號1">                                    
                                </div>
                                <div class="col-sm-4 col-xs-3">  
                                    <input type="tel" id="tckno2" maxlength="5" class="form-control" placeholder="票號2">
                                </div>                                                            
                                <div class="col-sm-4 col-xs-3">                                    
                                    <input type="tel" id="tckno3" maxlength="5" class="form-control" placeholder="票號3">
                                </div>
                                <div class="col-sm-4 col-xs-3">   
                                    <input type="tel" id="tckno4" maxlength="5" class="form-control" placeholder="票號4">                                                                        
                                </div>        
                                <div class="col-sm-4 col-xs-3">                                    
                                    <input type="tel" id="tckno5" maxlength="5" class="form-control" placeholder="票號5">                                    
                                </div>
                                <div class="col-sm-4 col-xs-3">  
                                    <input type="tel" id="tckno6" maxlength="5" class="form-control" placeholder="票號6">
                                </div>                                                            
                                <div class="col-sm-4 col-xs-3">                                    
                                    <input type="tel" id="tckno7" maxlength="5" class="form-control" placeholder="票號7">
                                </div>
                                <div class="col-sm-4 col-xs-3">   
                                    <input type="tel" id="tckno8" maxlength="5" class="form-control" placeholder="票號8">                                                                        
                                </div>      
                                <div class="col-sm-4 col-xs-3">                                    
                                    <input type="tel" id="tckno9" maxlength="5" class="form-control" placeholder="票號9">
                                </div>
                                <div class="col-sm-4 col-xs-3">   
                                    <input type="tel" id="tckno10" maxlength="5" class="form-control" placeholder="票號10">                                                                        
                                </div>      
                                
                                <div class="col-sm-4 col-xs-6">                                         
                                    <input id="btnContinue4" class="btn btn-success"  type="button" value="自動連號">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-sm-4 col-xs-6">                                    
                                    
                                </div>
                                
                            </div>
<!--                            <div class="row"  id="divHiXX009">
                                <div class="col-sm-6 col-xs-12">                                    
                                    上次資料異動時間:<a id="updatetime"></a>
                                </div>                                
                            </div>-->
                            
                            
                        </form>
<!--                    </section>-->
                
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
<script src="assets/js/requestTicket.js"></script>

</body>

</html>
