<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>

<head>
<meta charset="utf-8">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="Category.Theme" content="170">
<meta name="Category.Cake" content="140">
<meta name="Category.Service" content="E10">
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
    if(isAndroid){
        window.location = 'indexAndroid.jsp';
    }
    else if(isiOS){
        //alert('ios!');
    }else{
        //alert('desktop!');
    }            
</script>
<style>
    body {
  margin: 0;
  padding: 0;
}

.frame {
  display: block;
  width: 100vw;
  height: 100vh;
  max-width: 100%;
  margin: 0;
  padding: 0;
  border: 0 none;
  box-sizing: border-box;
}
</style>
</head>
<body>
<frameset row="*">
    <iframe src="login.jsp" name="detail" class="frame"/>
</frameset>
</body>

<!--        <frame src="menu_target.jsp" scrolling="no"/>
        <frame src="index_nomenu.jsp" scrolling="no" id="detail" name="detail"/>-->
        
</html>