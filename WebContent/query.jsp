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
            position: fixed;
            right:0px;
            /*        top: -20px;
                    left: 20px;*/
            /*        background-color: white;
                    width: 500px;*/
        }.npaDialog{
            position: relative;
            right:0px;
            top: 20px;
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
                            <!--                            <div class="row">
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
                                                        </div>-->
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>查詢類別</label> 
                                    <select class="form-control" data-width="100px" id="queryType">
                                        <option value="">請選擇</option>
                                        <option value="self" selected="">1.自己已登錄票券(登錄人是自己,含(2))</option>
                                        <option value="others">2.幫別人登錄票券(登錄人是自己，但發票人是別人)</option>
                                        <option value="selfProc">3.自己已索取票券(發票人是自己)</option>                                                                              
                                    </select>                                 
                                </div>
                            </div>
                            <!--                            <div class="row">
                                                            <div class="col-sm-6 col-xs-12">
                                                                <label><span class=Must>*</span>伙伴姓名</label> 
                                                                <input
                                                                    type="text" id="staffName" name="txtIDNum" class="form-control"
                                                                    placeholder="伙伴姓名">
                                                            </div>
                                                        </div>-->
                            <!--                            <div class="row">
                                                            <div class="col-sm-6 col-xs-12">
                                                                <label><span class=Must></span>登錄人姓名(只能查詢自己登錄的資料)</label> 
                                                                <input
                                                                    type="text" id="creator" name="creator" class="form-control"
                                                                    placeholder="發票人姓名">
                                                            </div>
                                                        </div>                            -->
                            <!--                            <div class="row">
                                                            <div class="col-sm-6 col-xs-12">
                                                                <label><span class=Must></span>發票人姓名(只能查詢自己登錄的資料)</label> 
                                                                <input
                                                                    type="text" id="procman" name="procman" class="form-control"
                                                                    placeholder="發票人姓名">
                                                            </div>
                                                        </div>                            -->
                        </form><br>
                        <div align="center">
                            <button type="submit" class="btn btn-default" id="btnQry">查詢</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="submit" class="btn btn-default" id="btnClear">清除</button>
                        </div>
                        <br>

                    </section>
                </div>
                <div class="panel-footer">

                </div>
            </div>
            <div>

            </div>
        </div>
        <div id="modal-update" class="npaDialog" style="display: none;width: 80%">
        <div class="modal-body">
            <form id="UpdateMaintainForm" enctype="multipart/form-data" method='post' action='CJDT_UploadServlet' target="_self">
                <table border="0" width="100%" cellpadding="0" cellspacing="0"
                       class="keyinTableGray">

                    
                    <tr>
                        <td class="labelField" width="100">票號：</td>
                        <td class="dataField">
                            <input id="tickid" type="text" class="form-control" size="20"></td>
                    </tr>                                    
                    <tr>
                        <td class="labelField" width="100">發票人：</td>
                        <td class="dataField ">
                            <input id="procman" type="text" class="form-control" size="20"></td>
                    </tr>
                    <tr>
                        <td class="labelField" width="100">索票人：</td>
                        <td class="dataField ">
                            <input id="tickname" type="text" class="form-control" size="20"></td>
                    </tr>
                    <tr>
                        <td class="labelField" width="100">索票人電話：</td>
                        <td class="dataField ">
                            <input id="ticktel"  type="text" class="form-control" size="20"></td>
                    </tr>                    
                </table>
            </form>
            <div class="modal-footer">
                <button id="QS_SAVE_UPDATE" name="QS_SAVE_UPDATE"
                        class="btn green-seagreen btn-info" type="button">
                    <i class="fa fa-save"></i>&nbsp;儲存
                </button>
                &nbsp;                                
                <button id="btnClose" name="QS_CLOSE_UPDATE"
                        class="btn purple btn-default" type="button">
                    <i class="fa fa-times"></i>&nbsp;關閉視窗
                </button>
            </div>
        </div>
    </div> 
    </section>
    <div class="jqGrid">
        <table id="QueryResult" style="display: none"></table>
    </div>
    <div id="QueryResultpagger"></div>
    
                       
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
