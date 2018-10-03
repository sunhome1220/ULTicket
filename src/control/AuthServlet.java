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
import java.util.ArrayList;
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
        
        switch (argJsonObj.getString("ajaxAction")) {            
            case "login":  
                String viewerURL = "reply.jsp";//票根輸入TODO:改成讀個別user之設定首頁
                user = new User();
                user.setUserId("TEST001");
                String userId = argJsonObj.getString("uid");
                user.setUserName(userId);
                String pwd = argJsonObj.getString("pwd");
                if(authCheck(userId, pwd)){
                    user.setLoginStatus(1);
                    result = "登入成功";
                    session.setAttribute("user", user);
                    //res.sendRedirect(viewerURL);			    
                }else{
                    user.setLoginStatus(0);
                    result = "登入失敗，無註冊資料或密碼錯誤";
                }                
                this.setInfoMsg(returnJasonObj, result);
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
}
