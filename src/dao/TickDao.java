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
import util.DateUtil;

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
    
    
    //意見回條
    public String addComment(JSONObject argJsonObj) {//create or update
        String strMsg = "";
        String[] allInterest = argJsonObj.getString("interest").split("[;,]");
        String action = argJsonObj.getString("action");//create or update
        String eventid = argJsonObj.getString("eventid");
        String tickid = argJsonObj.getString("tickid");
        String audiencename = argJsonObj.getString("audiencename");
        String ticktel = argJsonObj.getString("ticktel");
        String audiencecomment = argJsonObj.getString("audiencecomment");
        String comment = argJsonObj.getString("comment");
        int contactStatus = argJsonObj.getInt("contactStatus");
        int callTimes = argJsonObj.getInt("callTimes");
        String userid = argJsonObj.getString("USER_NM");

        log.info("addComment:" + userid);
        String sql = "INSERT INTO tickcomment(evid,tickid,audiencename,audiencecomment,contactStatus,"
                + "     updatetime,comment,calltimes,lastestCallernm,username,interest,ticktel)  "
                + "VALUES (?,?,?,?,?,getdate(),?,?,?,?,?,?)";
        Object[] objs = new Object[11];
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
        objs[10] = ticktel;
        
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
        //String[] allInterest = argJsonObj.getString("interest").split("[;,]");        
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
        String statType = argJsonObj.getString("statType");
        if(statType.equals("self")){
            //只查自己組
        }
        String username = argJsonObj.getString("USER_NM");
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM tickcomment where evid=? and username=? "; 
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username});
        JSONObject jo = new JSONObject();
        jo.put("countSelf", result.isEmpty()? 0: result.get(0).get("cnt"));//該場所有人總輸入票根
        return jo.toString();
    }
    
    //以票號取得該票之索票人索票的相關資訊
    

    //[[\'新竹公演\', \'南門公演\', \'板橋公演\', \'國館公演\', \'板橋公演\'],[1200, 1400,1312,1334,0,0],[1100,0,0,0,0,0]]';
    public String queryReportData(JSONObject argJo) {
        String today = DateUtil.getTodayDate();
        String updateSql = "UPDATE proctick SET presentStatus= 1 "
                + "where (presentStatus is null or presentStatus=0) "
                + "and tickid in(select tickid from showtick where evid>="+ today +")";
        this.pexecuteUpdate(updateSql, new Object[]{});
        //updateSql = "update  proctick set event='南門公演' where event = '南門公 演' ";
        this.pexecuteUpdate(updateSql, new Object[]{});
        
        ArrayList<HashMap> list = new ArrayList();
        String sql = "";
        String statType = argJo.getString("statType");
        if(statType.equals("self")){
            sql = "SELECT P.evid, P.event,P.team, P.pcnt, C.cofirmCnt,S.scnt "
                + "FROM (SELECT evid,event,team,count(*) as pcnt "
                    + " FROM proctick group by evid,event,team having team=?) P "
                    + "left join (SELECT evid,event,team,count(*) as cofirmCnt,confirmStatus "
                    + "FROM proctick group by evid,event,team,confirmStatus having confirmStatus=1) C "
                    + " on P.evid=C.evid and P.team=C.team "
                    + "left join (SELECT evid,event,team,count(*) as scnt,presentStatus "
                    + "FROM proctick group by evid,event,team,presentStatus having presentStatus=1) S "
                    + "on P.evid=S.evid and P.team=S.team ";
            list = this.pexecuteQuery(sql, new Object[]{argJo.getString("USER_TEAM")});
        }else if(statType.equals("person")){//只統計自己是發票人的部份
            sql = "SELECT P.evid, P.event,P.procman, P.pcnt, C.cofirmCnt,S.scnt "
                + "FROM (SELECT evid,event,procman,count(*) as pcnt "
                    + " FROM proctick group by evid,event,procman having procman=?) P "
                    + "left join (SELECT evid,event,procman,count(*) as cofirmCnt,confirmStatus "
                    + "FROM proctick group by evid,event,procman,confirmStatus having confirmStatus=1) C "
                    + " on P.evid=C.evid and P.procman=C.procman "
                    + "left join (SELECT evid,event,procman,count(*) as scnt,presentStatus "
                    + "FROM proctick group by evid,event,procman,presentStatus having presentStatus=1) S "
                    + "on P.evid=S.evid and P.procman=S.procman ";
            list = this.pexecuteQuery(sql, new Object[]{argJo.getString("USER_ID")});
       
        }else{
            sql = "SELECT P.evid, P.event, P.pcnt, S.scnt,C.cofirmCnt "
                + "FROM "
                + "(SELECT evid,event,count(*) as pcnt FROM proctick group by evid,event) P "
                + "left join (SELECT evid,event,count(*) as cofirmCnt,confirmStatus "
                + "FROM proctick group by evid,event,confirmStatus having confirmStatus=1) C on P.evid=C.evid "
                + "left join (SELECT evid,count(*) as scnt "
                + "FROM showtick group by evid) S " 
                + "on P.evid=S.evid order by evid";
                list = this.pexecuteQuery(sql, new Object[]{});
        }
        
        String result = "";
        String label = "";
        String reqNos = "";
        String showNos = "";
        String confirmNos = "";
        for(int i=0; i<list.size();i++){
            label += "\'" + list.get(i).get("event") + "\'";
            if(i<list.size()-1) label += ",";
            reqNos += list.get(i).get("pcnt");
            if(i<list.size()-1) reqNos += ",";
            String scnt = list.get(i).get("scnt").toString();
            showNos += scnt.equals("")? 0: scnt;
            if(i<list.size()-1) showNos += ",";
            String ccnt = list.get(i).get("cofirmCnt").toString();
            confirmNos += ccnt.equals("")? 0: ccnt;
            if(i<list.size()-1) confirmNos += ",";
        }
//        label += "]";
//        reqNos += "]";
//        showNos += "]";
//        confirmNos += "";
        
        result = "[[" + label + "],[" + reqNos + "],[" + showNos + "],[" + confirmNos + "]]";
        return result;
    }
    
    /**
     * 
     * @param jo
     * @return 
     */
    public JSONArray getDataByStaffName(JSONObject jo) {
        ArrayList<HashMap> list = new ArrayList<>(); 
        //ArrayList<HashMap> listG = new ArrayList<>(); 
        
        String queryType = jo.getString("queryType");        
        if(queryType.equals("team") && jo.getInt("ROLE")<2){//0: 一般();2:組長 5:系統管理者
            return null;//防高手破解前端script
        }
        if(queryType.equals("all") && jo.getInt("ROLE")<5){
            return null;
        }
        String team = jo.getString("USER_TEAM");
        String confirmStatus = jo.getString("confirmStatus");
        String tickname = jo.getString("tickname");//索票人姓名
        //String confirmStatusLike = confirmStatus +"%";
        
        int evid = jo.getInt("evid");
        String sql = "SELECT * FROM proctick where evid = ? ";
        String sqlG = "SELECT confirmStatus, count(*) FROM proctick where evid = ? ";
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                sql += " and creator=? ";
                sqlG += " and creator=? ";
                break;
            case "others":
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                sql += " and creator=? and procman!=? ";
                sqlG += " and creator=? and procman!=? ";
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                sql += " and procman=? ";
                sqlG += " and procman=? ";
                break;
            case "team":
                //自己所屬組之總計                
                sql += " and team=? ";
                sqlG += " and team=? ";
                break;
            case "all":
                //all
                break;
            default:
                break;
        }        
        //sqlG += " group by confirmStatus ";
        final String userId = jo.getString("USER_ID");
        Object[] para3 = new Object[]{evid, userId};
        Object[] para4 = new Object[]{evid, userId, userId};
        Object[] paraT = new Object[]{evid, team};//組
        Object[] paraA = new Object[]{evid};//all
        
        if(!confirmStatus.equals("")){
            sql += " and confirmStatus = ? ";
            //sql += "and confirmStatus = ? ";
            para3 = new Object[]{evid, userId, confirmStatus};
            para4 = new Object[]{evid, userId, userId, confirmStatus};
            paraT = new Object[]{evid, team, confirmStatus};
            paraA = new Object[]{evid, confirmStatus};
        }        
        if(!tickname.trim().equals("")){
            sql += " and tickname like ? ";
        }
        sql += " order by taginc desc";
        
        Object[] newPara = null;
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                list = this.pexecuteQuery(sql, para3);
                //listG = this.pexecuteQuery(sqlG, para3);
                break;
            case "others":        
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                list = this.pexecuteQuery(sql, para4);
                //listG = this.pexecuteQuery(sqlG, para4);
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                list = this.pexecuteQuery(sql, para3);
                //listG = this.pexecuteQuery(sqlG, para3);
                break;
            case "team":
                //整組統計
                newPara = paraT;
                if(!tickname.trim().equals("")){
                    newPara = new Object[paraT.length+1];
                    for(int i=0;i<paraT.length;i++){
                        newPara[i] = paraT[i];
                    }
                    newPara[paraT.length] = tickname + "%";
                }
                list = this.pexecuteQuery(sql, newPara);                
                break;
            default:
                newPara = paraA;
                if(!tickname.trim().equals("")){
                    newPara = new Object[paraA.length+1];
                    for(int i=0;i<paraA.length;i++){
                        newPara[i] = paraA[i];
                    }
                    newPara[paraA.length] = tickname + "%";
                }
                list = this.pexecuteQuery(sql, newPara);
                
                break;
        }
        JSONArray jArray = new JSONArray();
        return arrayList2JsonArray(jArray, list);
    }
    
    /**
     * 
     * @param jo
     * @return 
     */
    public JSONArray getConfirmStatData(JSONObject jo) {
        ArrayList<HashMap> listG = new ArrayList<>(); 
        
        String queryType = jo.getString("queryType");        
        if(queryType.equals("team") && jo.getInt("ROLE")<2){//0: 一般();2:組長 5:系統管理者
            return null;//防高手破解前端script
        }
        if(queryType.equals("all") && jo.getInt("ROLE")<5){
            return null;
        }
        String team = jo.getString("USER_TEAM");
        String confirmStatus = jo.getString("confirmStatus");
        //String confirmStatusLike = confirmStatus +"%";
        
        int evid = jo.getInt("evid");
        String sqlG = "SELECT confirmStatus, count(*) FROM proctick where evid = ? ";
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                sqlG += " and creator=? ";
                break;
            case "others":
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                sqlG += " and creator=? and procman!=? ";
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                sqlG += " and procman=? ";
                break;
            case "team":
                //自己所屬組之總計                
                sqlG += " and team=? ";
                break;
            default:
                break;
        }
        sqlG += " group by confirmStatus ";
        final String userId = jo.getString("USER_ID");
        Object[] para3 = new Object[]{evid, userId};
        Object[] para4 = new Object[]{evid, userId, userId};
        Object[] paraT = new Object[]{evid, team};//組
        Object[] paraA = new Object[]{evid};//all
        
        switch (queryType) {
            case "self":
                //自己已登錄票券(登錄人是自己,含(2)
                listG = this.pexecuteQuery(sqlG, para3);
                break;
            case "others":        
                //幫別人登錄票券(登錄人是自己，但發票人是別人)
                listG = this.pexecuteQuery(sqlG, para4);
                break;
            case "selfProc":
                //自己已索取票券(發票人是自己)
                listG = this.pexecuteQuery(sqlG, para3);
                break;
            case "team":
                //整組統計
                listG = this.pexecuteQuery(sqlG, paraT);
                break;
            default://全團
                listG = this.pexecuteQuery(sqlG, paraA);
                break;
        }
        JSONArray jArray = new JSONArray();
        return arrayList2JsonArray(jArray, listG);
    }
    
}
