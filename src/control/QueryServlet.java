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
import javax.sql.rowset.CachedRowSet;
import base.AjaxBaseServlet;
import dao.DrugCaseDao;
import java.util.ArrayList;
import util.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBUtil;

/**
 *
 * @author User
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends AjaxBaseServlet {

    Logger log = Logger.getLogger(QueryServlet.class);

    @Override

    protected void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
            JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception {
        argJsonObj.put("userVO", user);
        
        JSONObject jObject;
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs1 = null;
        boolean result = false;
        HttpSession tmpsession = null;
        String msg = "";
        //從SESSION找到USER的ID 先當 ACCOUNT 暫用，正式做時SESSION ID即為該ID
//        User newuser = (User) session.getAttribute("user");
//        String UserAccount = newuser.getUserId();
//        argJsonObj.put("ACCOUNT", UserAccount);
//        forumDao GET_ID = forumDao.getInstance();
//        resultDataArray = GET_ID.GET_ID(argJsonObj);
//        String UserId = "" + resultDataArray.getJSONObject(0).getInt("USERID");
        argJsonObj.put("USER_ID", user.getUserId());
        argJsonObj.put("USER_NM", user.getUserName());//20171012
        argJsonObj.put("USER_UNIT", user.getUnitCd());
        argJsonObj.put("USER_CITYCODE", user.getUserCity());
        //讀講習選單、繳納方式、注意事項用
        argJsonObj.put("USER_UNITCD1", user.getUnitCd1());
        argJsonObj.put("USER_UNITCD2", user.getUnitCd2());
        if (argJsonObj.has("CASEID")) {
            request.getSession().setAttribute("caseId", argJsonObj.getString("CASEID"));
            log.debug("save caseId to session: " + argJsonObj.getString("CASEID"));
        } else {
            if (request.getSession().getAttribute("caseId") != null) {
                log.debug("get caseId from session: " + request.getSession().getAttribute("caseId").toString());
                argJsonObj.put("CASEID", request.getSession().getAttribute("caseId").toString());
            }
        }
        
        DrugCaseDao dao = DrugCaseDao.getInstance();
        String success = "";
        
        switch (argJsonObj.getString("ajaxAction")) {                        
            case "addRecipts":  
                msg = dao.addTicnos(argJsonObj);
                this.setInfoMsg(returnJasonObj, msg);
                break;
            case "getShowTicCount":  
                msg = dao.getShowTicCount(argJsonObj);
                this.setInfoMsg(returnJasonObj, msg);
                break;
            case "queryTicStatus":  
                msg = dao.queryTicStatus(argJsonObj);
                this.setInfoMsg(returnJasonObj, msg);
                break;
            case "getReportData":  
                msg = dao.queryReportData(argJsonObj);
                this.setInfoMsg(returnJasonObj, msg);
                break;
            case "getDataByStaffName":  
                resultDataArray = dao.Get_AllCase(argJsonObj);
                msg = "" + success;
                this.setJqGridData(returnJasonObj, resultDataArray);
                break;
            case "doSaveNewCase"://儲存新案件
                success = dao.SaveNewDrugCase(argJsonObj);
                msg = "" + success;
                this.setDDLData(returnJasonObj, msg);

                break;
            case "doDeleteCase":
                DrugCaseDao DeleteCase_ByCASEID = DrugCaseDao.getInstance();
                int success_DELETE = DeleteCase_ByCASEID.DeleteCase_ByCASEID(argJsonObj);
//                if(success_DELETE>0){
//                    this.setReturnMsg(returnJasonObj, MsgString.DELETE_SUCCESS);
//                }else{
//                    this.setReturnMsg(returnJasonObj, MsgString.DELETE_FAIL);
//                }

                this.setDDLData(returnJasonObj, success_DELETE);
//                msg = "" + success_DELETE;
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
