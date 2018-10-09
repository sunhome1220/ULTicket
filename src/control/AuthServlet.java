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
import util.User;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import util.DBUtil;
import util.ExceptionUtil;

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
                pwd = argJsonObj.getString("pwd");
                String username = argJsonObj.getString("username");
                String teamnm = argJsonObj.getString("teamnm");
                if(isAccountUnique(userId)){
                    createAccount(userId, username, teamnm, pwd);
                    user.setLoginStatus(1);
                    result = "登入成功";
                    jo.put("status", true);
                    jo.put("msg", result);
                    jo.put("loginCode", digestPasswordSHA256(userId+pwd));
                    session.setAttribute("user", user);
                    //res.sendRedirect(viewerURL);			    
                }else{
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "抱歉，該帳號已被註冊，請另選帳號";
                    jo.put("msg", result);
                }                
                jo.toString();
                this.setInfoMsg(returnJasonObj, result);
                break;            
            case "login":  
                user = new User();
                user.setUserId(userId);//20181004 目前帳號與姓名一樣，沒特別設欄位
                user.setUserName(userId);
                pwd = argJsonObj.getString("pwd");
                if(authCheck(userId, pwd)){
                    user.setLoginStatus(1);
                    result = "登入成功";//+ digestPasswordSHA256(userId + pwd);
                    jo.put("status", true);                    
                    jo.put("loginCode", digestPasswordSHA256(userId + autoLoginCheckStr));
                    jo.put("redirectUrl", "reply.jsp");
                    session.setAttribute("user", user);
                    //res.sendRedirect(viewerURL);			    
                }else{
                    user.setLoginStatus(0);
                    jo.put("status", false);
                    result = "登入失敗，無註冊資料或密碼錯誤";
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
        try{
            qsPara.add(userId);            
            qsPara.add(pwd);            
            passed = DBUtil.getInstance().pisExist(sql, qsPara.toArray());
        }catch(Exception e){
            
        }
        return passed;
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

    private void createAccount(String userId, String userNm, String team, String pwd) {
        String sql = "INSERT INTO showtick(empno, emppass) VALUES (? ,? ,?)";
        ArrayList qsPara = new ArrayList();    
        boolean success = false;
        try{
            qsPara.add(userId);                  
            success = !DBUtil.getInstance().pisExist(sql, qsPara.toArray());
        }catch(Exception e){
            
        }
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
