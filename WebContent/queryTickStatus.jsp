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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
    <script type="text/javascript" src="assets/js/styleswitcher.js"></script>
<style>
    .Must {
        color: #FF0000;
    }
    canvas {
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
    }
</style>
</head>

<body>
    <jsp:include page="menu.jsp" />  
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">                
                <div class="panel-heading">
                    <h2 id="heaerh2" class="panel-title">索票及出席預估/統計&nbsp;&nbsp;&nbsp;&nbsp;</h2>                        
                </div>
                
<!--                <div class="row">
                    <div class="col-sm-6 col-xs-6">
                        <label><span class=Must>*</span>統計方式</label> 
                        <select class="form-control" data-width="100px" id="statType">                            
                            <option value="all">不分組總計</option>
                            <option value="self">自己組內統計</option>                            
                            <option value="person">自己發放部份(你是發票人)</option>                            
                        </select>                                
                    </div>                                
                    <div class="col-sm-6 col-xs-6">
                         <button type="submit" class="btn btn-primary" id="btnQry">查詢</button>                              
                    </div>                                
                </div>-->
                <div id="divInfo">
                    <table id="tblInfo" style="display:none;width: 98%" border="1">
                        <tr style="color:purple">
                            <td> 日期場次 </td><td style="align-content: flex-end;">座位 </td>
                            <td>登記 </td><td>伙伴<br>索票</td><td>請假</td><td>預估<br>出席 </td><td>實際<br>出席 </td>
                        </tr>
                    </table>
                </div>
               
                
            </div>
        </div>
    </section>
    
<!--    <a href="javascript:history.back()" class="scrollup"><i class="icon-left-open-big"></i>回上頁</a>-->
    <!-- Chart.js v2.4.0 -->
  
    <script src="assets/js/Chart.bundle.js"></script>
    <script src="assets/js/utils.js"></script>
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
    <script src="assets/js/utils.js"></script>
    <script src="assets/js/queryTickStatus.js"></script>
</body>

</html>
