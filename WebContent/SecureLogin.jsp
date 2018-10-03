<%@page import="dao.AuthDao"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	import="util.User,com.syscom.db.DBUtil,com.syscom.util.StringUtils,java.util.*,util.*"%>
<% 
String id = request.getParameter("id");
if(id!=null && id.length()>0){
	User user = new User();
	String ip = request.getRemoteAddr();
	if(ip.equals("0:0:0:0:0:0:0:1")){
		ip="127.0.0.1";
	}
	user.setUserIp(ip);
	AuthDao dao = AuthDao.getInstance();
	
        String unitCd = request.getParameter("unitCd");
	String userId = request.getParameter("TM_USER_ID");
        String userNm = request.getParameter("TM_USER_NM");
	String roles = request.getParameter("roles");
        if(id.equals("custom")){
            roles = request.getParameter("selectRoles");
        }
        System.out.println("unitCd:"+unitCd+" userId:"+userId+" userNm:"+userNm+" roles:"+roles);
	if(id.equals("custom")){//自訂單位登入
		user.setUserId(userId);
		user.setUserName(userNm);
		user.setIdNo("A123456789");//身分證
		/*user.setUnitCd1("A1000");
		user.setUnitCd1Name("警政署");
		user.setUnitCd2("A1E01");
		user.setUnitCd2Name("警政署資訊室");
		user.setUnitCd3("A1E41");
		user.setUnitCd3Name("警政署資訊室資料作業科");*/
		user.setUnitCd(unitCd);//使用者的單位代碼
		//user.setUnitName("警政署資訊室資料作業科");//後面會撈出單位名稱
		//user.setScopeUnitCd("");
		//user.setScopeUnitLevel("");
		//user.setOwnRole(new String[]{"LE200001"});
		dao.setUser3LevelUnit(user);
		//user.setOwnRole(new String[]{roles});
		if(roles.contains(",")){
		    user.setOwnRole(roles.split(","));
		}else{
		    user.setOwnRole(new String[]{roles});
		}
	}else if(id.equals("ABCD6001")){//署系統管理人員
		user.setUserId(id);
		user.setUserName("署資訊承辦");
		user.setIdNo("P123456789");//身分證
		user.setUnitCd1("A1000");
		user.setUnitCd1Name("警政署");
		user.setUnitCd2("A1E01");
		user.setUnitCd2Name("警政署資訊室");
		user.setUnitCd3("A1E41");
		user.setUnitCd3Name("警政署資訊室前瞻應用科");
		user.setUnitCd("A1E41");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000006"});
	}else if(id.equals("ABCD5001")){//刑事局
		user.setUserId(id);
		user.setUserName("刑事局承辦");
		user.setIdNo("Q123456789");//身分證
		user.setUnitCd1("A2200");
		user.setUnitCd1Name("刑事局");
		user.setUnitCd2("A22Q1");
		user.setUnitCd2Name("刑事局偵一大隊");//TODO:刑事局的承辦人單位會是？
		user.setUnitCd3("");
		user.setUnitCd3Name("");
		user.setUnitCd("A22Q1");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000005"});
        }else if(id.equals("ABCD4001")){//刑事局鑑識
		user.setUserId(id);
		user.setUserName("刑事局鑑識");
		user.setIdNo("R123456789");//身分證
		user.setUnitCd1("A2200");
		user.setUnitCd1Name("刑事局");
		user.setUnitCd2("A22E1");
		user.setUnitCd2Name("刑事局鑑識科");
		user.setUnitCd3("");
		user.setUnitCd3Name("");
		user.setUnitCd("A22E1");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000004"});
        }else if(id.equals("ABCD3001")){//北市承辦人測試-刑大
		user.setUserId(id);
		user.setUserName("北市警測");
		user.setIdNo("R123456789");//身分證
		user.setUnitCd1("AW000");
		user.setUnitCd1Name("北市政府警察局");
		user.setUnitCd2("AWZ00");
		user.setUnitCd2Name("台北市政府警察局刑事大隊");
		user.setUnitCd3("AWZI1");
		user.setUnitCd3Name("北市刑大偵查組");
		user.setUnitCd("AWZI1");
		user.setOwnRole(new String[]{"CJ000003"});
        }
        else if(id.equals("ABCD3002")){//新北市承辦人-刑大偵查組
		user.setUserId(id);
		user.setUserName("新北警測");
		user.setIdNo("R123456789");//身分證                
		user.setUnitCd1("AD000");
		user.setUnitCd1Name("新北市政府警察局");
		user.setUnitCd2("ADZ00");
		user.setUnitCd2Name("新北市政府警察局刑事大隊");
		user.setUnitCd3("ADZI1");
		user.setUnitCd3Name("新北刑大偵查組");
		user.setUnitCd("ADZ00");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000003"});
        }
        else if(id.equals("ABCD2001")){//北市鑑識
		user.setUserId(id);
		user.setUserName("北市鑑識");
		user.setIdNo("S123456789");//身分證
		user.setUnitCd1("AW000");
		user.setUnitCd1Name("台北市政府警察局");
		user.setUnitCd2("AW0T1");
		user.setUnitCd2Name("台北市政府警察局鑑識中心");
		user.setUnitCd3("");
		user.setUnitCd3Name("");
		user.setUnitCd("AW0T1");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000002"});
        }		
        else if(id.equals("ABCD2002")){//新北鑑識
		user.setUserId(id);
		user.setUserName("新北鑑識");
		user.setIdNo("S123456789");//身分證
		user.setUnitCd1("AD000");
		user.setUnitCd1Name("台北市政府警察局");
		user.setUnitCd2("AD0T1");
		user.setUnitCd2Name("台北市政府警察局鑑識中心");
		user.setUnitCd3("");
		user.setUnitCd3Name("");
		user.setUnitCd("AD0T1");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000002"});
        }		
        else if(id.equals("ABCD1001")){//北市中山分局偵查隊
		user.setUserId(id);
		user.setUserName("中山偵查測");
		user.setIdNo("S123456789");//身分證
		user.setUnitCd1("AW000");
		user.setUnitCd1Name("台北市政府警察局");
		user.setUnitCd2("AW100");
		user.setUnitCd2Name("台北市政府警察局中山分局");
		user.setUnitCd3("AW141");
		user.setUnitCd3Name("北市中山分局偵查隊");
		user.setUnitCd("AW141");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000001"});
        }		
        else if(id.equals("ABCD1002")){//北市大安分局偵查隊
		user.setUserId(id);
		user.setUserName("大安偵查測");
		user.setIdNo("S123456789");//身分證
		user.setUnitCd1("AW000");
		user.setUnitCd1Name("台北市政府警察局");
		user.setUnitCd2("AW200");
		user.setUnitCd2Name("台北市政府警察局大安分局");
		user.setUnitCd3("AW241");
		user.setUnitCd3Name("北市大安分局偵查隊");
		user.setUnitCd("AW241");//使用者的單位代碼
		user.setUnitName("北市大安分局偵查隊");
		user.setOwnRole(new String[]{"CJ000001"});
        }		
        else if(id.equals("ABCD1003")){//新北板橋分局偵查隊
		user.setUserId(id);
		user.setUserName("板橋偵查測");
		user.setIdNo("S123456789");//身分證
		user.setUnitCd1("AD000");
		user.setUnitCd1Name("新北市政府警察局");
		user.setUnitCd2("AD600");
		user.setUnitCd2Name("新北市板橋分局偵查隊");
		user.setUnitCd3("AD641");
		user.setUnitCd3Name("新北市板橋分局偵查隊");
		user.setUnitCd("AD641");//使用者的單位代碼
		user.setOwnRole(new String[]{"CJ000001"});
        }		
	user.setLoginTime(DateUtil.get14DateFormat(new Date()));//警署的Log機制所必需的資料
        
        String role_permission = "";
        String userRoles = user.getOwnRoleString();
        if(userRoles.contains("CJ000002") ||
                userRoles.contains("CJ000001")){
            role_permission = "3";
        } else if(userRoles.contains("CJ000003") ||
                userRoles.contains("CJ000005")                
                ){
            role_permission = "2";
        } else {
            role_permission = "1";
        }
        user.setRolePermission(role_permission);
	
	dao.setUser3LevelUnit(user);
//	dao.setUserFuncList(user);
//	dao.setUserScopeUnit(user);	
	
	session.setAttribute("user", user);
	System.out.println("getOwnRoleString = "+user.getOwnRoleString());
	response.sendRedirect("MainFrameset.jsp");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系統登入</title>
<style type="text/css">
table {
	border-collapse: collapse;
	border: 1px solid #E0810D;
}

table td {
	border: 1px solid #E0810D;
	font-size: 13px;
}
</style>
<script language="JavaScript">

function login(id){    
    if(id === "custom"){
        var unitCd = "";
        if(document.getElementById("TM_PR_UNIT_CD").value !== ""){
            unitCd = document.getElementById("TM_PR_UNIT_CD").value;
        } else if(document.getElementById("TM_PR_B_UNIT_CD").value !== ""){
            unitCd = document.getElementById("TM_PR_B_UNIT_CD").value;
        } else if(document.getElementById("TM_PR_D_UNIT_CD").value !== ""){
            unitCd = document.getElementById("TM_PR_D_UNIT_CD").value;
        }
        loginForm.unitCd.value=unitCd;
        if(unitCd === ""){
            alert("請選擇單位");
            return;
        }
        
        if(document.getElementById("TM_USER_ID").value === ""){
            alert("請輸入帳號");
            return;
        }
        if(document.getElementById("TM_USER_NM").value === ""){
            alert("請輸入姓名");
            return;
        }

        var roles = document.getElementsByName("roles");
        var role = "";
        for(var i=0;i<roles.length;i++){
            if(roles[i].checked){
                role += ","+roles[i].value;
            }
        }
        if(role.length > 0){
            role = role.substring(1);
        }
        loginForm.selectRoles.value=role;
        if(role === ""){
            alert("請勾選角色");
            return;
        }
    }
	loginForm.id.value=id;
	loginForm.submit();
}
</script>
</head>
<body>
	請選擇需測試登入的角色:
	<p />
	<form name="loginForm" method="post" action="">
		<input type="hidden" name="id" value="" />
                <input type="hidden" name="unitCd" value="" />
                <input type="hidden" name="selectRoles" value="" />
		<table width="100%" cellspacing="3" cellpadding="4" border="1"
			rules="all">
			<tr><td class="labelField">組別(依筆劃排序)：</td>
                            <td class="dataField">
                                <select class="selectpicker" data-width="100px" id="TM_PR_D_UNIT_CD" name="unitLevel2">
                                    <option value="">請選擇</option>
                                    <option value="永樂">永樂</option>
                                    <option value="藍天">藍天</option>
                                    <option value="復興">復興</option>
                                </select>                                 
                            </td>
			</tr>
                        <tr><td class="labelField">登入帳號：</td>
                            <td class="dataField">
                                <input type="text" class="form-control" id="TM_USER_ID" name="TM_USER_ID" placeholder="請輸入帳號" value="" maxlength="8"/>
                            </td>
			</tr>
                        <tr><td class="labelField">登入姓名：</td>
                            <td class="dataField">
                                <input type="text" class="form-control" id="TM_USER_NM" name="TM_USER_NM" placeholder="請輸入您的全名" value=""/>
                            </td>
			</tr>
                        <tr><td colspan="2" class="labelField">
                                <input type="button" name="" value="登入"
					onclick="login('custom')" />
                            </td>
			</tr>                    
                </table>
                
	</form>

        <jsp:include page="Footer.jsp" />
        <script src="assets/js/SecureLogin.js"></script>
	<script src="assets/js/ddlProcess.js"></script>
	<script src="assets/js/formProcess.js"></script>
</body>
</html>
