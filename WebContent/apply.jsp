<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>

<head>
    <meta charset="utf-8">
    <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">           
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="Category.Theme" content="170">
    <meta name="Category.Cake" content="140">
    <meta name="Category.Service" content="E10">
    <title>會員註冊2</title>
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
    <link rel="stylesheet" href="assets/plugins/jquery-ui-1.12.1.custom/jquery-ui.min.css">
    <script>               
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r;
            i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
            a = s.createElement(o),
                    m = s.getElementsByTagName(o)[0];
            a.async = 1;
            a.src = g;
            m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

        ga('create', 'UA-62660947-1', 'auto');
        ga('send', 'pageview');

    </script><script type="text/javascript" src="assets/js/styleswitcher.js"></script>
<styleSwitcher cat="font"></styleSwitcher>
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
                    <h2 class="panel-title">門票申請:</h2>
                </div>
                <div class="panel-body">
                    <section>
                        <form id="Form1" name="reg" action="apply.jsp" method=post>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>領票方式</label>
                                    <select id="place" class="form-control">
                                        <option>上課地點自取</option>
                                        <option>電子票券</option>                                                                            
                                    </select>


                                </div>
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>索取張數</label> 
                                    <select id="ticketsNumber" class="form-control">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>										
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label for="country"><span class=Must>*</span>索取地點</label>

                                    <select id="select_police" class="form-control">
                                        <option>新北中和總部</option>
                                        <option>北市南門國中</option>
                                        <option>桃園龍安國小</option>
                                        <option>台中健行國小</option>
                                        <option>高雄</option>										
                                    </select>

                                </div>								
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>姓名</label> 
                                    <input type="text" id="audienceName" name="ApChName" class="form-control">
                                </div>								
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label>居住地區</label> 
                                    <select id="born" name="country"
                                            class="form-control">
                                        <option>台北市</option>
                                        <option>新北縣</option>
                                        <option>台中市</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>年齡</label> 
                                    <select id="born" name="country"
                                            class="form-control">
                                        <option>未滿18歲</option>
                                        <option>19~30</option>
                                        <option>31~40</option>
                                        <option>41~50</option>
                                        <option>51~60</option>
                                        <option>60以上</option>
                                    </select>
                                </div>
                                <div class="col-sm-6 col-xs-12">
                                    <label for="born">性別<br></label> <br> <input
                                        type="radio" id="male" name="sex" checked> <label
                                        for="male">男性</label> <input type="radio" id="female"
                                        name="sex"> <label for="female">女性</label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <label>通訊地址</label> <input type="text" id="address2"
                                                               name="address2" class="form-control">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-xs-12">
                                    <label><span class=Must>*</span>聯絡電話</label> <input type="text"
                                                                                        id="telno" name="telno" class="form-control">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <label>電子郵件</label> <input type="text" id="email" name="email"
                                                               class="form-control">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <label><span class=Must>*</span>備註說明</label> <input type="text"
                                                                                        id="address2" name="address2"
                                                                                        value="" class="form-control">
                                </div>
                            </div>

                            <br>
                            <div class="row">

                                <div class="col-sm-12">
                                    <div align="center">
                                        <button id="btnSubmit" type="submit" class="btn btn-default">送出</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </section>
                </div>
                <div class="panel-heading">
                    <h2 class="panel-title">注意事項：</h2>
                </div>
                <div class="panel-body">
                    <ol>
                        <li>由於每場演出的座位有限，工作人員會<br> If							
                        </li>
                    </ol>
                </div>
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
    <script src="assets/js/datepicker-zh-TW.js"></script> <!--西元日期-->
    <!--<script src="assets/js/datepicker-zh-TW1.js"></script> <!--民國日期-->
    <script src="assets/js/utils.js"></script>
    <script src="assets/js/apply.js"></script>
</body>

</html>
