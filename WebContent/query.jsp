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
//        (function (i, s, o, g, r, a, m) {
//            i['GoogleAnalyticsObject'] = r;
//            i[r] = i[r] || function () {
//                (i[r].q = i[r].q || []).push(arguments)
//            }, i[r].l = 1 * new Date();
//            a = s.createElement(o),
//                    m = s.getElementsByTagName(o)[0];
//            a.async = 1;
//            a.src = g;
//            m.parentNode.insertBefore(a, m)
//        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
//
//        ga('create', 'UA-62660947-1', 'auto');
//        ga('send', 'pageview');

    </script><script type="text/javascript" src="assets/js/styleswitcher.js"></script>
<style>
    .Must {
        color: #FF0000;
    }
    .loginUser{
        position: relative;
        right:0px;
/*        top: -20px;
        left: 20px;*/
/*        background-color: white;
        width: 500px;*/
    }
</style>
</head>

<body>
    <jsp:include page="menu.jsp" />    
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">查詢：</h2>
                </div>
                <div class="panel-body">
                    <section>
                        <form id="Form1">
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>組別(依筆劃排序)</label> 
                                    <select class="form-control" data-width="100px" id="teamName" name="unitLevel2">
                                        <option value="">請選擇</option>
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
                                    <label><span class=Must>*</span>伙伴姓名</label> 
                                    <input
                                        type="text" id="staffName" name="txtIDNum" class="form-control"
                                        placeholder="伙伴姓名">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>索票人姓名</label> 
                                    <input
                                        type="text" id="txtIDNum" name="txtIDNum" class="form-control"
                                        placeholder="英文請輸入大寫">
                                </div>
                            </div>                            
                        </form>
                    </section>
                </div>
                <div class="panel-footer">
                    <div align="center">
                        <button type="submit" class="btn btn-default" id="btnQry"
                                onClick="Query();">查詢</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-default" id="btnClear">清除</button>
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
    <script src="assets/js/query.js"></script>
</body>

</html>
