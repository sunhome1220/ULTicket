/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.HashMap;
import base.CJBaseDao;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class TickDao extends CJBaseDao {

    private static TickDao instance = null;
    private static Logger log = Logger.getLogger(TickDao.class);

    private TickDao() {
    }

    public static TickDao getInstance() {
        if (instance == null) {
            instance = new TickDao();
        }
        return instance;
    }

    
    /**
     * 
     * @param argJsonObj
     * @return 
     */
    public String addTicnos(JSONObject argJsonObj) {
        String strMsg = "";
        String[] allTicNo = argJsonObj.getString("ticketnos").split(";");
        String eventid = argJsonObj.getString("eventid");
        String userid = argJsonObj.getString("USER_NM");
        log.info("addTicnos:" + userid);
        //String sql = "INSERT INTO showtick(evid,tickid,username) VALUES ('20181014',12349,'001     ')";
        String sql = "INSERT INTO showtick(evid,tickid,username) VALUES (? ,?, ?)";
        Object[] sqls = new Object[allTicNo.length];
        Object[][] objs = new Object[allTicNo.length][3];
        
        String sqlCheck = "SELECT tickid, username FROM showtick where evid=? ";
        String sqlCheckCond = "";
        for(int i=0; i<allTicNo.length; i++){
            sqlCheckCond += allTicNo[i];
            if(i<allTicNo.length-1)sqlCheckCond += ",";
            sqls[i] = sql;
            objs[i][0] = eventid;
            objs[i][1] = allTicNo[i];
            objs[i][2] = userid;            
        }
        sqlCheck += " and tickid in(" + sqlCheckCond + ")";
        
        ArrayList<HashMap> result = this.pexecuteQuery(sqlCheck, new Object[]{eventid});
        if(result.size()>0){
            strMsg = "票號重複("+ result.get(0).get("tickid") + " 已由 " + result.get(0).get("username") +" 輸入)";            
        }
        
        if(strMsg.length()==0){
            int[] batchResult = this.pexecuteBatch(sqls, objs);
            for(int j=0; j<batchResult.length; j++){
                
            }
            String sqlCount = "SELECT tickid FROM showtick where evid=? and username=?";
            int count = this.pexecuteQuery(sqlCount, new Object[]{eventid, userid}).size();
            String sqlCountTotal = "SELECT tickid FROM showtick where evid=? ";
            int countTotal = this.pexecuteQuery(sqlCountTotal, new Object[]{eventid}).size();
            strMsg = "成功新增" + batchResult.length + "筆票號資料！\n\n本場您共新增:"+count+"筆票根，總計:"+countTotal+"筆資料";
        }
        
        return strMsg;
    }
    
    
    
    public String addComment(JSONObject argJsonObj) {//create or update
        String strMsg = "";
        String[] allInterest = argJsonObj.getString("interest").split("[;,]");
        String action = argJsonObj.getString("action");//create or update
        String eventid = argJsonObj.getString("eventid");
        String tickid = argJsonObj.getString("tickid");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencecomment = argJsonObj.getString("audiencecomment");
        String comment = argJsonObj.getString("comment");
        int contactStatus = argJsonObj.getInt("contactStatus");
        int callTimes = argJsonObj.getInt("callTimes");
        String userid = argJsonObj.getString("USER_NM");

        log.info("addComment:" + userid);
        String sql = "INSERT INTO tickcomment(evid,tickid,audiencename,audiencecomment,contactStatus,"
                + "     updatetime,comment,calltimes,lastestCallernm,username,interest)  "
                + "VALUES (?,?,?,?,?,getdate(),?,?,?,?,?)";
        Object[] objs = new Object[10];
        objs[0] = eventid;
        objs[1] = tickid;
        objs[2] = audiencename;
        objs[3] = audiencecomment;
        objs[4] = contactStatus;
        
        objs[5] = comment;
        objs[6] = callTimes;
        objs[7] = userid;
        objs[8] = userid;
        objs[9] = argJsonObj.getString("interest");
        
        int result = this.pexecuteUpdate(sql, objs);        
        if(result>=0){
            strMsg = "成功新增一筆回條資料!";
        }else{
            strMsg = "異常！回條資料未成功新增!";
        }
        return strMsg;
    }
    
    public String updateComment(JSONObject argJsonObj) {
        String strMsg = "";
        String[] allInterest = argJsonObj.getString("interest").split("[;,]");        
        String eventid = argJsonObj.getString("eventid");
        String tickid = argJsonObj.getString("tickid");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencecomment = argJsonObj.getString("audiencecomment");
        String comment = argJsonObj.getString("comment");
        int contactStatus = argJsonObj.getInt("contactStatus");
        int callTimes = argJsonObj.getInt("callTimes");
        String userid = argJsonObj.getString("USER_NM");

        log.info("addComment:" + userid);
        String sql = "update tickcomment set audiencename=?,audiencecomment=?, contactStatus=?, "
                + "     updatetime=getdate(), comment=?, calltimes=?, lastestCallernm=?, username=?, interest=? "
                + " where evid=? and tickid=? ";
                
        Object[] objs = new Object[10];
        objs[0] = audiencename;
        objs[1] = audiencecomment;
        objs[2] = contactStatus;
        
        objs[3] = comment;
        objs[4] = callTimes;
        objs[5] = userid;
        objs[6] = userid;
        objs[7] = argJsonObj.getString("interest");
        objs[8] = eventid;
        objs[9] = tickid;
        
        
        int result = this.pexecuteUpdate(sql, objs);        
        if(result>=0){
            strMsg = "成功更新回條資料!";
        }else{
            strMsg = "異常！回條資料未成功更新!";
        }
        return strMsg;
    }

//    private String checkTicnoNotDuplicate(String sqlCheck) {
//        ArrayList<HashMap> result = this.pexecuteQuery(sqlCheck, new Object[]{});
//        if(result.size()>0){
//            String msg = "票號重複("+ result.get(0).get("tickid") + " 已由 " + result.get(0).get("username") +" 輸入)";
//            return msg;
//        }else{
//            return "";
//        }
//    }

    /**
     * 查詢索票狀況
     * @param argJsonObj
     * @return 
     */
    public String queryTicStatus(JSONObject argJsonObj) {
        String sql = "SELECT evid,event,count(*) FROM proctick group by evid,event";
        ArrayList<HashMap> tickResult = this.pexecuteQuery(sql, new Object[]{});
        return "";
    }
    
    //票根
    public String getShowTicCount(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        String username = argJsonObj.getString("USER_NM");
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM showtick where evid=?" 
                + " union " 
                + " SELECT '2scnt' as type, count(*) as cnt FROM showtick where evid=? and username=? "
                + " union " 
                + " SELECT '3ptCnt' as type, count(*) as cnt FROM proctick where evid=?  ";
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, evid, username, evid});
        JSONObject jo = new JSONObject();
        jo.put("totalShowCnt", result.get(0).get("cnt"));//該場所有人總輸入票根
        jo.put("showCnt", result.get(1).get("cnt"));//個人該場輸入票根
        jo.put("totalReqCnt", result.get(2).get("cnt"));//該場總索票
        return jo.toString();
    }

    //取得已輸入回條數
    public String getTicCommentCount(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        String username = argJsonObj.getString("USER_NM");
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM tickcomment where evid=? and username=? "; 
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username});
        JSONObject jo = new JSONObject();
        jo.put("countSelf", result.isEmpty()? 0: result.get(0).get("cnt"));//該場所有人總輸入票根
        return jo.toString();
    }
    
    //以票號取得該票之索票人索票的相關資訊
    

    //[[\'新竹公演\', \'南門公演\', \'板橋公演\', \'國館公演\', \'板橋公演\'],[1200, 1400,1312,1334,0,0],[1100,0,0,0,0,0]]';
    public String queryReportData(JSONObject argJsonObj) {
        String sql = "SELECT P.evid, P.event, P.pcnt, S.scnt FROM (SELECT evid,event,count(*) as pcnt "
                + "FROM proctick group by evid,event) P "
                + "left join (SELECT evid,count(*) as scnt "
                + "FROM showtick group by evid) S " 
                + "on P.evid=S.evid order by evid";
        ArrayList<HashMap> list = this.pexecuteQuery(sql, new Object[]{});
        String result = "";
        String label = "[";
        String reqNos = "[";
        String showNos = "[";
        for(int i=0; i<list.size();i++){
            label += "\'" + list.get(i).get("event") + "\'";
            if(i<list.size()-1) label += ",";
            reqNos += list.get(i).get("pcnt");
            if(i<list.size()-1) reqNos += ",";
            String scnt = list.get(i).get("scnt").toString();
            showNos += scnt.equals("")? 0: scnt;
            if(i<list.size()-1) showNos += ",";
        }
        label += "]";
        reqNos += "]";
        showNos += "]";
        
        result = "[" + label + "," + reqNos + "," + showNos + "]";
        return result;
    }
    
    public JSONArray getDataByStaffName(JSONObject jo) {
        ArrayList<HashMap> list = new ArrayList<>(); 
        
        String queryType = jo.getString("queryType");
        int evid = jo.getInt("evid");
        String sql = "SELECT * FROM proctick where evid = ? and ";
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                sql += " creator=? ";
                break;
            case "others":
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                sql += " creator=? and procman!=? ";
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                sql += " procman=? ";
                break;
            default:
                break;
        }
        sql += " order by taginc desc";
        final String userId = jo.getString("USER_ID");
        
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                list = this.pexecuteQuery(sql, new Object[]{evid, userId});
                break;
            case "others":
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                list = this.pexecuteQuery(sql, new Object[]{evid, userId, userId});
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                list = this.pexecuteQuery(sql, new Object[]{evid, userId});
                break;
            default:
                break;
        }
        JSONArray jArray = new JSONArray();
        return arrayList2JsonArray(jArray, list);
    }
    
}
