/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import base.AjaxBaseServlet;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import util.User;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import util.DBUtil;

/**
 *
 * @author User
 */
@WebServlet("/AuthServlet")
public class AuthServlet extends AjaxBaseServlet {

    Logger log = Logger.getLogger(AuthServlet.class);

    @Override

    protected void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
            JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
        argJsonObj.put("userVO", user);
        HttpServletResponse res = (HttpServletResponse) response;
        
        String result = "";
        JSONObject jo = new JSONObject();
        String viewerURL = "reply.jsp";//票根輸入TODO:改成讀個別user之設定首頁
        String userId = argJsonObj.getString("uid");
        String pwd = "";
        final String autoLoginCheckStr = "2018pwd";
                                
        switch (argJsonObj.getString("ajaxAction")) {            
            case "register":  //註冊
                user = new User();
                user.setUserId(userId);//20181004 目前帳號與姓名一樣，沒特別設欄位
                user.setUserName(userId);
                String username = argJsonObj.getString("uid");
                pwd = argJsonObj.getString("pwd");
                String team = argJsonObj.getString("team");
                String tel = argJsonObj.getString("tel");
                String email = argJsonObj.getString("email");
                String authCode = argJsonObj.getString("authCode");
                if(!authCode.equals("無限" + team)){
                    result = "驗證碼錯誤";
                    jo.put("status", false);                    
                    jo.put("msg", result);
                }else{
                    if(isAccountUnique(userId)){
                        result = createAccount(userId,pwd, userId, team, authCode, tel, email);
                        user.setLoginStatus(1);
                        //result = "新增帳號成功";
                        jo.put("status", true);
                        jo.put("msg", result);
                        //jo.put("loginCode", digestPasswordSHA256(userId+pwd));
                        session.setAttribute("user", user);
                        //res.sendRedirect(viewerURL);			    
                    }else{
                        user.setLoginStatus(0);
                        jo.put("status", false);
                        result = "抱歉，該帳號已被註冊，請另選帳號";
                        jo.put("msg", result);
                    }         
                }
                //jo.toString();
                this.setInfoMsg(returnJasonObj, jo.toString());
                break;            
            case "login":  
                log.info("login:" + argJsonObj.toString());
                user = new User();
                user.setUserId(userId);//20181004 目前帳號與姓名一樣，沒特別設欄位
                user.setUserName(userId);
                pwd = argJsonObj.getString("pwd");
                String deviceType = argJsonObj.getString("deviceType");
                
                JSONObject joR = authCheck2(userId, pwd, deviceType);
                //if(authCheck(userId, pwd)){
                if(joR.getBoolean("passed")==true){
                    user.setLoginStatus(1);
                    result = "登入成功";//+ digestPasswordSHA256(userId + pwd);
                    jo.put("status", true);                    
                    jo.put("teamName", joR.getString("teamName"));                    
                    user.setTeam(joR.getString("teamName"));
                    user.setRole(joR.getInt("role"));
                    jo.put("loginCode", digestPasswordSHA256(userId + autoLoginCheckStr));
                    jo.put("redirectUrl", "reply.jsp");
                    session.setAttribute("user", user);                    
                    //res.sendRedirect(viewerURL);			    
                }else if(joR.getString("result").equals("NoPwd")){
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    jo.put("NoPwd", true);
                    result = "密碼已清除或尚未設定!\n\n請輸入新密碼並再次按下「登入」";
                    
                }else if(joR.getString("result").equals("WrongPwd")){
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "密碼累計錯誤"+ joR.getInt("wrongPwdTimes") +"次，請確認後重新登入!";
                }else if(joR.getString("result").equals("WrongPwdOver5")){
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "密碼累計錯誤超過5次，因資安考量帳號已鎖定!\n請聯絡系統管理員";
                }else{
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "查無此帳號，請先註冊";
                }             
                jo.put("msg", result);
                jo.toString();
                this.setInfoMsg(returnJasonObj, jo.toString());
                break;            
            case "autologin":  
                user = new User();
                user.setUserId(userId);//20181004 目前帳號與姓名一樣，沒特別設欄位
                user.setUserName(userId);
                String loginCode = argJsonObj.getString("loginCode");
                if(digestPasswordSHA256(userId + autoLoginCheckStr).equals(loginCode)){                    
                    user.setLoginStatus(1);
                    result = "登入成功";//+ digestPasswordSHA256(userId + pwd);
                    jo.put("status", true);                    
                    jo.put("loginCode", digestPasswordSHA256(userId + autoLoginCheckStr));
                    jo.put("redirectUrl", "reply.jsp");                    
                    session.setAttribute("user", user);                    
                }else{
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "登入失敗，無註冊資料或密碼錯誤";
                }             
                jo.put("msg", result);
                jo.toString();
                this.setInfoMsg(returnJasonObj, jo.toString());
                break;            
        }

    }

    private boolean authCheck(String userId, String pwd) {
        String sql = "SELECT * FROM emppass where empno = ? and emppass= ? ";
        ArrayList qsPara = new ArrayList();    
        boolean passed = false;
        String teamName = "";
        try{
            qsPara.add(userId);            
            qsPara.add(pwd);            
            //passed = DBUtil.getInstance().pisExist(sql, qsPara.toArray());
            ArrayList list = DBUtil.getInstance().pexecuteQuery(sql, qsPara.toArray());
            passed = list.size()>0;
            if(passed){
                sql = "update emppass set logintime = getdate() where empno = ? ";
                DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{userId});
                HashMap m = (HashMap)list.get(0);
                teamName = m.get("teamname").toString();                
            }
        }catch(Exception e){
            
        }
        return passed;
    }
    
    private JSONObject authCheck2(String userId, String pwd, String deviceType) {
        JSONObject jo = new JSONObject();
        String sql = "SELECT * FROM emppass where empno = ? ";//and emppass= ? ";
        ArrayList qsPara = new ArrayList();    
        boolean passed = false;
        String teamName = "";
        String role = "";
        String result = "";
        try{
            qsPara.add(userId);            
            //qsPara.add(pwd);            
            //passed = DBUtil.getInstance().pisExist(sql, qsPara.toArray());
            ArrayList list = DBUtil.getInstance().pexecuteQuery(sql, qsPara.toArray());
            if(list.size() > 0){//有帳號
                final String pwdSHA = digestPasswordSHA256(pwd);
                HashMap data = (HashMap)list.get(0);
                passed = data.get("emppass").equals(pwd) || data.get("emppassSHA").equals(pwdSHA);
                final String wrongTimesStr = data.get("wrongPwdTimes").toString();
                int wrongPwdTimes = wrongTimesStr.equals("")? 0 : Integer.parseInt(wrongTimesStr);                
                if(wrongPwdTimes >=5){
                    result = "WrongPwdOver5";
                    passed = false;
                }else if(data.get("emppass").toString().trim().length()==0 && data.get("emppassSHA").toString().trim().length()==0){
                    if(!pwd.equals("")){//重設密碼
                        sql = "update emppass set emppassSHA = ? where empno = ? ";                        
                        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{pwdSHA, userId});
                    }else{
                        result = "NoPwd";
                        passed = false;
                    }
                    
                }else if(passed){
                    sql = "update emppass set logintime = getdate() where empno = ? ";
                    DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{userId});
                    if(data.get("emppassSHA").toString().trim().equals("")){
                        sql = "update emppass set emppassSHA = ? where empno = ? ";                        
                        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{pwdSHA, userId});
                    }                    
                    HashMap m = (HashMap)list.get(0);
                    teamName = m.get("teamname").toString();                
                    role = m.get("role").toString();          
                    wrongPwdTimes = 0;//歸0
                }else{
                    wrongPwdTimes = wrongPwdTimes + 1;                    
                    result = "WrongPwd";    
                    jo.put("wrongPwdTimes", wrongPwdTimes);
                } 
                sql = "update emppass set wrongPwdTimes = ? where empno = ? ";
                DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{wrongPwdTimes, userId});
            }else{
                result = "IdNotFound";
            }            
        }catch(Exception e){
            
        }
        jo.put("passed", passed);
        jo.put("teamName", teamName);
        jo.put("role", role);
        jo.put("result", result);
        return jo;
    }

    private boolean isAccountUnique(String userId) {
        String sql = "SELECT * FROM emppass where empno = ? ";
        ArrayList qsPara = new ArrayList();    
        boolean unique = false;
        try{
            qsPara.add(userId);                  
            unique = !DBUtil.getInstance().pisExist(sql, qsPara.toArray());
        }catch(Exception e){
            
        }
        return unique;
    }

    private String createAccount(String userId, String pwd, String userNm, String team, String authCode, String tel, String email) {
        String result = "註冊失敗(" + userId +")";
        
        String sql = "INSERT INTO emppass(empno, emppass,empname,role,updatetime,teamname,email,tel,emppassSHA,agreeRuleDate) "
            + "VALUES (? ,? ,?, 0, getdate(), ?, ?, ?, ?, getdate())";
        ArrayList qsPara = new ArrayList();    
        try{
            qsPara.add(userId);                  
            qsPara.add(pwd);                  
            qsPara.add(userNm);                  
            qsPara.add(team);                  
            qsPara.add(email);                  
            qsPara.add(tel);                  
            qsPara.add(digestPasswordSHA256(pwd));                              
            int cnt = DBUtil.getInstance().pexecuteUpdate(sql, qsPara.toArray());
            if(cnt==1){
                result = "註冊成功, 帳號:" + userId +"";
            }
        }catch(Exception e){
            result = "註冊失敗(" + userId +")";
        }        
        return result;
    }
    	/**
	 * 使用MD5做加密
	 * 
	 * @param password
	 * @return
	 */
	public static String digestPassword(String password) {
		String digest = null;

		try {
			// use message digest to encrypt password and the algorithm is MD5
			MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
			md.update(password.getBytes());
			byte[] byteDigest = md.digest();
			StringBuffer strBuffer = new StringBuffer(byteDigest.length * 2);
			// convert to hex
			for (int i = 0; i < byteDigest.length; i++) {
				strBuffer.append(convertDigit(byteDigest[i] >> 4));
				strBuffer.append(convertDigit(byteDigest[i] & 0xf));
			}

			digest = strBuffer.toString();
		} catch (Exception e) {
			//log.error(ExceptionUtil.toString(e));
		}
		return digest;
	}

        private static char convertDigit(int i) {
		i &= 0xf;
		if (i >= 10)
			return (char) ((i - 10) + 97);
		else
			return (char) (i + 48);
	}
	/**
	 * 使用SHA256做加密，會比MD5更加安全。警署弱點掃描程式要求。
	 * 
	 * @param password
	 * @return
	 */
	public static String digestPasswordSHA256(String password) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());

			byte[] byteData = md.digest();
			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			digest = sb.toString();
		} catch (Exception e) {
			//log.error(ExceptionUtil.toString(e));
		}
		return digest;
	}

}
