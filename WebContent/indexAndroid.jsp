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
    //alert(u);
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if(isAndroid){
        window.location = 'indexAndroid.jsp';
    }
    else if(isiOS){
    }else{

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
				    <li>瀏覽器使用說明<br>
				        本系統適用IE8(以上)、Google Chrome、FireFox、Safari瀏覽器。
				    </li>
                                    <li>登入說明<br>
				        請先使用陳組長提供之離線版程式以取得帳號及密碼                                       
				    </li>
                                    <li>建立連結<br>
                                        目前開放功能為「票根輸入」<br>
				        首次登入即先開啟此功能，可先將連結加入主畫面以便下次使用(無需再次登入)
				    </li>
				    <li>
                                        若有任何操作問題請email至<a href="mailto:sunhome1220@gmail.com?subject=%5B%E7%84%A1%E9%99%90%E7%A5%A8%E5%8B%99%E7%AE%A1%E7%90%86-%E5%95%8F%E9%A1%8C%E5%8F%8D%E6%87%89%5D&body=%E5%95%8F%E9%A1%8C%E6%8F%8F%E8%BF%B0%3A">此處</a>
				    </li>
				    
				    </ol>
				</div>

			</div>
</div>
</section>

<!-- 以下是Footer
<div id="FatFooter">
<div class="container">
<div class="row">
<div class="accesskey">
<a href="#" title="下方網站地圖區" accesskey="M">:::</a>
</div>
<nav>
<ul>
<li>
<a href="#">機關簡介</a>
<ul>
<li>
<a href="np?ctNode=12571&CtUnit=1588&BaseDSD=7&mp=1">沿革</a>
</li>
<li>
<a href="lp?ctNode=12949&CtUnit=2708&BaseDSD=7&mp=1">首長介紹</a>
</li>
<li>
<a href="sp?xdUrl=/wSite/searchMap/searchMap.jsp&ctNode=12573&mp=1">警察機關位置查詢</a>
</li>
<li>
<a href="np?ctNode=12574&CtUnit=1745&BaseDSD=7&mp=1">警察體系</a>
</li>
<li>
<a href="np?ctNode=12575&CtUnit=1746&BaseDSD=7&mp=1">組織架構</a>
</li>
<li>
<a href="lp?ctNode=12886&CtUnit=2635&BaseDSD=7&mp=1">施政重點</a>
</li>
<li>
<a href="lp?ctNode=12577&CtUnit=2004&BaseDSD=7&mp=1">警察機關地址電話一覽</a>
</li>
<li>
<a href="np?ctNode=12578&mp=1">警察大事記</a>
</li>
<li>
<a href="lp?ctNode=12583&CtUnit=1596&BaseDSD=7&mp=1">警政工作年報</a>
</li>
<li>
<a href="lp?ctNode=12581&CtUnit=1758&BaseDSD=7&mp=1">委託研究計畫報告</a>
</li>
<li>
<a href="np?ctNode=12582&CtUnit=2576&BaseDSD=7&mp=1">中華民國警察畫刊</a>
</li>
</ul>
</li>
<li>
<a href="#">資訊公開</a>
<ul>
<li>
<a href="np?ctNode=12704&CtUnit=2519&BaseDSD=7&mp=1">政府資訊公開</a>
</li>
<li>
<a href="np?ctNode=12926&CtUnit=2688&BaseDSD=7&mp=1">OPEN DATA</a>
</li>
<li>
<a href="lp?ctNode=12551&CtUnit=1637&BaseDSD=4&mp=1">警察教育</a>
</li>
<li>
<a href="np?ctNode=12552&mp=1">警政統計</a>
</li>
<li>
<a href="lp?ctNode=12554&CtUnit=1988&BaseDSD=7&mp=1">警察法規</a>
</li>
<li>
<a href="lp?ctNode=12556&CtUnit=2438&BaseDSD=7&mp=1">出版品目錄</a>
</li>
<li>
<a href="np?ctNode=12924&mp=1">性別主流化專區</a>
</li>
<li>
<a href="np?ctNode=12951&mp=1">行政院治安會報專區</a>
</li>
</ul>
</li>
<li>
<a href="#">線上申辦</a>
<ul>
<li>
<a target="_blank" title="(另開新視窗)" href="http://www.npa.gov.tw/NPAGip/wSite/np?ctNode=11725&mp=1">警察刑事紀錄證明書(俗稱良民證)</a>
</li>
<li>
<a target="_blank" title="(另開新視窗)" href="https://nv2.npa.gov.tw/NM103-604Client/">入山案件申辦系統</a>
</li>
<li>
<a target="_blank" title="(另開新視窗)" href="https://tm2.npa.gov.tw/NM105-505Client/">交通事故資料申請及申辦案件進度查詢</a>
</li>
</ul>
</li>
<li>
<a href="#">為民服務</a>
<ul>
<li>
<a href="np?ctNode=12868&CtUnit=2638&BaseDSD=7&mp=1">警察史蹟館參觀申請書</a>
</li>
<li>
<a href="lp?ctNode=12928&CtUnit=2690&BaseDSD=7&mp=1">申辦事項及電子表單</a>
</li>
<li>
<a target="_blank" title="(另開新視窗)" href="https://nv2.npa.gov.tw/TX105-Web/view/index.jsp">計程車駕駛人服務網</a>
</li>
<li>
<a href="np?ctNode=12622&CtUnit=1579&BaseDSD=7&mp=1">110報案</a>
</li>
<li>
<a target="_blank" title="(另開新視窗)" href="https://nv2.npa.gov.tw/TrafficSearch/">測速執法點及拖吊保管場</a>
</li>
</ul>
</li>
<li>
<a href="#">溫馨關懷</a>
<ul>
<li>
<a href="np?ctNode=12623&mp=1">關老師愛心園地</a>
</li>
<li>
<a href="np?ctNode=12863&mp=1">警察優良事蹟及社團專區</a>
</li>
<li>
<a href="lp?ctNode=12642&CtUnit=1610&BaseDSD=4&mp=1">保護您小秘方</a>
</li>
<li>
<a target="_blank" title="(另開新視窗)" href="http://www.avs.org.tw/">犯罪被害人保護(連結財團法人犯罪被害人保護協會)</a>
</li>
<li>
<a href="lp?ctNode=12632&CtUnit=1820&BaseDSD=7&mp=1">影音專區</a>
</li>
<li>
<a href="sp?xdUrl=/wSite/questionnaire/questionnaire.jsp&ctNode=12633&phylum=1&mp=1">問卷調查</a>
</li>
<li>
<a href="lp?ctNode=12634&CtUnit=2334&BaseDSD=7&mp=1">活動及統計專區</a>
</li>
<li>
<a href="np?ctNode=12867&mp=1">防制酒駕專區</a>
</li>
<li>
<a href="np?ctNode=12855&mp=1">交通事故專區</a>
</li>
<li>
<a href="np?ctNode=12869&CtUnit=2639&BaseDSD=7&mp=1">各警察機關交通違規檢舉專區網頁</a>
</li>
<li>
<a href="lp?ctNode=12636&CtUnit=2025&BaseDSD=7&mp=1">交通安全宣導</a>
</li>
</ul>
</li>
<li>
<a href="#">警政單位連結</a>
<ul>
<li>
<a href="lp?ctNode=12637&CtUnit=1929&BaseDSD=4&mp=1">本署各業務單位</a>
</li>
<li>
<a href="lp?ctNode=12638&CtUnit=1686&BaseDSD=4&mp=1">警政相關單位</a>
</li>
</ul>
</li>
<li>
<a href="sp?xdUrl=/wSite/DatabaseQuery/sh.jsp&ctNode=12639&mp=1">婦幼專區</a>
</li>
<li>
<a href="#">法案預告及動態</a>
<ul>
<li>
<a href="lp?ctNode=12931&CtUnit=2693&BaseDSD=7&mp=1">草案預告</a>
</li>
<li>
<a href="lp?ctNode=12933&CtUnit=2695&BaseDSD=7&mp=1">法規命令發布</a>
</li>
</ul>
</li>
</ul>
</nav>
</div>
</div>
</div>
<footer id="footer">
<div class="container">
<div class="row">
<div class="col-sm-9 col-md-7">
<div class="Footer_link">
<div class="accesskey">
<a href="#" title="下方網站授權說明區" accesskey="B">:::</a>
</div>
<ul>
<li>
<a href="np?ctNode=12405&amp;mp=1" title="政府網站資料開放宣告">政府網站資料開放宣告</a>
</li>
<li>
<a href="np?ctNode=12404&amp;mp=1" title="隱私權保護政策">隱私權保護政策</a>
</li>
<li>
<a href="np?ctNode=12406&amp;mp=1" title="網站安全政策">網站安全政策</a>
</li>
<li>
<a href="np?ctNode=12407&amp;mp=1" title="保有及管理個人資料.pdf
            ">保有及管理個人資料</a>
</li>
</ul>
<div class="Footer_info">
<div class="Phone">總機：02-2321-9011 [本電話僅具受話功能，無法顯示來電號碼，請勿受騙]</div>
<div class="Address">
			                    本署各單位服務時間：08:30-17:30<br>
			                地址：10058 臺北市中正區忠孝東路1段7號（<a title="TgosMap" target="_blank" href="http://www.moi.gov.tw/chi/chi_about/tgos_map2.aspx?X=302726.089&amp;Y=2770889.704&amp;AddressString=10058%E8%87%BA%E5%8C%97%E5%B8%82%E4%B8%AD%E6%AD%A3%E5%8D%80%E5%BF%A0%E5%AD%9D%E6%9D%B1%E8%B7%AF%E4%B8%80%E6%AE%B57%E8%99%9F&amp;UnitName=%E5%85%A7%E6%94%BF%E9%83%A8%E8%AD%A6%E6%94%BF%E7%BD%B2+&amp;fsize=Y">位置圖</a>） </div>
<div class="Resolution">本網站設計支援IE9.0、Firefox及Chrome，最佳瀏覽解析度為1024 x 768</div>
<div class="Copyright">內政部警政署版權所有</div>
</div>
</div>
</div>
<div class="hidden-xs hidden-sm col-md-3">
<div class="qrcode">
<ul>
<li>
<a target="_nwGIP" title="內政部警政署" href="http://www.npa.gov.tw/"><img alt="內政部警政署" height="70" width="70" src="xslGip/style1/images/npa_qrcode.png"></a>
<p>警政署</p>
</li>
<li>
<a target="_nwGIP" title="警政服務(iOS版)" href="https://itunes.apple.com/tw/app/jing-zheng-fu-wu/id544121843?l=zh&mt=8"><img alt="警政服務(iOS版)" height="70" width="70" src="xslGip/style1/images/ios.png"></a>
<p>iOS版本</p>
</li>
<li>
<a target="_nwGIP" title="警政服務(Android版)" href="https://play.google.com/store/apps/details?id=tw.gov.npa.callservice&feature=search_result#?t=W251bGwsMSwxLDEsInR3Lmdvdi5ucGEuY2FsbHNlcnZpY2UiXQ"><img alt="警政服務(Android版)" height="70" width="70" src="xslGip/style1/images/android.png"></a>
<p>Android版本</p>
</li>
</ul>
<br>
<b>警政服務APP</b>
</div>
</div>
<div class="hidden-xs col-sm-3 col-md-2">
<div class="Footer_icon">
<ul>
<li class="MoiMark">
<a href="#" title="內政部網站評選優良網站"><img alt="內政部網站評選優良網站" src="xslGip/style1/images/b001.gif"></a>
</li>
<li class="Accessible_icon">
<a href="http://www.handicap-free.nat.gov.tw/Applications/Detail?category=20160911130355" target="_blank" title="無障礙A+標章(另開新視窗)"><img src="xslGip/style1/images/A_Plus.gif" alt="無障礙A+"></a>
</li>
<li class="Egov">
<a href="http://www.gov.tw/" title="連結：我的E政府(另開新視窗)" target="_blank"><img src="xslGip/style1/images/egov.png" alt="我的E政府"></a>
</li>
</ul>
</div>
<div class="Update">
<ul>
<li>更新日期：<em>2018年5月21日</em>
</li>
<li>訪客人次：<em>119705348</em>
</li>
<li>線上人數：<em>504</em>
</li>
</ul>
</div>
</div>
</div>
</div>
</footer>
-->
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
