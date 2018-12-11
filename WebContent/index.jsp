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
    <title>無限票務管理系統</title>
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
    <link rel="stylesheet" href="assets/plugins/grid/grid-Microsoft.CUF.css">
    <link rel="stylesheet" href="assets/plugins/grid/grid-mobile.css">
    <script type="text/javascript">
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isAndroid) {
            //window.location = 'indexAndroid.jsp';
        } else if (isiOS) {
            //alert('ios!');
        } else {
            //alert('desktop!');
        }
    </script>
</head>

<body>
    <jsp:include page="menu.jsp" />  
    <section id="MainContent">
        <div class="container" style="padding-left: 0px;padding-right: 0px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 class="panel-title">說明:</h2>
                </div>
                <div class="panel-body">
                    <ol>
                        <li>登入說明<br>
                            若無帳號請先註冊，註冊驗證碼請詢問各組組長(目前是固定的)<br>
                            <!--                                        測試階段若無帳號可先以測試帳號密碼<B>tester/tester</B>登入，正式上線(10/14)時會清除測試資料<br>-->
                        </li>
                        <li>功能選單<br>                                      
                            若使用手機操作，選單位於畫面左上角<a>(≡)</a>
                        </li>
                        <!--                                    <li>建立連結<br>
                                                                目前完成功能為「<a href="reply.jsp">票根輸入</a>」、「<a href="queryTickStatus.jsp">出席狀況</a>」及
                                                                「<a href="replyComment.jsp">回條輸入</a>」，
                                                                請放心<B>自行輸入資料</B>，若需刪除錯誤或是測試的資料請再聯絡我們，
                                                                有任何功能上的建議也請盡量提出，謝謝！
                                                            </li>-->

                        <!--				    <li>
                                                                我們將基於以下目的而使用您的個人資料：<br>                                        
                                                                通知您演出及上課的相關資訊和確認；                                        
                                                                透過您同意收取的通訊地址寄送票券或相關課程資訊；                                        
                                                            </li>
                                                            <li>
                                                                個人資料安全性：<br>
                                                                您在本網站上進行任何操作所提供的資訊將採用SSL加密協定以保護個人識別資料(建置中)。
                                                            </li>
                                                            <li>
                                                                您的隱私權選擇：<br>
                                                                如果您不希望我們以任何方式取得或使用您的個人資料，則請勿提供資料給我們，或您如果已經提供個人資料並且希望取消、更新或存取您的個人資料時，請寄送電子郵件通知我們。<a href="mailto:sunhome1220@gmail.com?subject=%5B%E7%84%A1%E9%99%90%E7%A5%A8%E5%8B%99%E7%AE%A1%E7%90%86-%E5%88%AA%E9%99%A4%E5%80%8B%E8%B3%87%5D&body=%E5%95%8F%E9%A1%8C%E6%8F%8F%E8%BF%B0%3A">刪除個人資料</a>。
                                                            </li>-->
                        <!--				    <li>
                                                                意見回饋<br>
                                                                若有任何操作問題或發現任何問題請寄送電子郵件至<a href="mailto:sunhome1220@gmail.com?subject=%5B%E7%84%A1%E9%99%90%E7%A5%A8%E5%8B%99%E7%AE%A1%E7%90%86-%E5%95%8F%E9%A1%8C%E5%8F%8D%E6%87%89%5D&body=%E5%95%8F%E9%A1%8C%E6%8F%8F%E8%BF%B0%3A">sunhome1220@gmail.com</a>
                                                            </li>				    -->
                        <li>
                            要修改之前登錄的資料<br>
                            請在<a href="query.jsp">查詢</a>後按下要修改的票號」                            
                        </li>
                        <li>
                            版本異動說明<br>
                            <a>2018/08/22</a>:「索票登錄」增加<a>「發票人」</a>欄位<br>發票人預設為登入者本人，
                            若幫其他伙伴輸入資料，發票人欄位請輸入伙伴的姓名。<br>                            
                            <a>2018/08/25</a>:將「索票登錄」的新增功能獨立出來成為「<a href="requestTicket.jsp">索票登錄(簡易)</a>」
<!--                            <br>功能較單純較易上手，若需查詢或修改請用「<a href="requestTicketMod.jsp">索票登錄</a>」               -->
                            <br>
                            <a>2018/10/26</a>:增加<a>註冊</a>功能<br>驗證碼請詢問各組組長(有個簡單規則)                                               
                            <br>
                            <a>2018/10/27</a>:修正Android手機無法使用的問題                                               
                            <br>
                            <a>2018/10/28</a>:新增「<a href="audienceRecTicQRCode.jsp">產生Qrcode</a>」功能(demo)。                                               
                            <br>
                            <a>2018/10/29</a>:新增「<a href="query.jsp">查詢</a>」功能，可查自己的索票記錄(包含自己新增或是別人幫忙新增者)
                            <br>
                            <a>2018/10/30</a>:票券輸入後會保留上次輸入的「發票人」及「地點」內容，使用者不需重覆輸入
                            <br>
                            <a>2018/10/31</a>:索票資料加上「座位類別」及「確認狀態」，以利後續統計及宣傳之策略；「<a href="query.jsp">查詢</a>」功能的結果加上可編輯功能。                            
                            <br>
                            <a>2018/11/01</a>:加入權限，區分組長、伙伴等權限。
                            <br>
                            <a>2018/11/03</a>:加上輸入票根的防錯機制。
                            <br>
                            <a>2018/11/09</a>:將「回條輸入」缺少的功能補齊。增加連線逾時的處理。
                            <br>
                            <a>2018/11/13</a>:增加組長可下載全組資料的功能;密碼改以SHA256加密；修改查詢表格的換頁圖示。
                            <br>
                            <a>2018/11/15</a>:在<a href="queryTickStatus.jsp">索票/出席狀況</a>增加出席人數預估功能。
                            <br>
                            <a>2018/11/26</a>:調整查詢結果的國館票號排序問題。
                            <br>
                            <a>2018/11/30</a>:新增回條輸入的欄位(年齡+滿意度)，若票根未輸入到但有回條，會再更新出席狀態成為「出席」。
                            <br>
                            <a>2018/12/11</a>:新增由觀眾自行輸入回條的功能。
                            <br>
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
    <script src="assets/js/datepicker-zh-TW.js"></script>
    <script src="assets/js/index.js"></script>
</body>

</html>
