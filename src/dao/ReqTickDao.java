/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import base.CJBaseDao;
import util.ExceptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import util.User;

/**
 *
 * @author User
 */
public class ReqTickDao extends CJBaseDao {

    private CommonDao comDao = CommonDao.getInstance();
    private static ReqTickDao instance = null;
    private static Logger log = Logger.getLogger(ReqTickDao.class);

    private ReqTickDao() {
    }

    public static ReqTickDao getInstance() {
        if (instance == null) {
            instance = new ReqTickDao();
        }
        return instance;
    }

    
    public String addReqTickData(JSONObject argJsonObj) {//create or update
        String strMsg = "";
        final String strAllTickIds = argJsonObj.getString("allTickIds");
        String[] allTickIds = strAllTickIds.split("[,]");
        //String action = argJsonObj.getString("action");//create or update
        String eventid = argJsonObj.getString("eventid");
        String event = getEventName(eventid);//argJsonObj.getString("event");
        String teamname = argJsonObj.getString("team");
        //String tickid = argJsonObj.getString("tickid");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencetel = argJsonObj.getString("audiencetel");
        String procaddr = argJsonObj.getString("procaddr");
        String tickmemo = argJsonObj.getString("tickmemo");
        int allowcontact = argJsonObj.getInt("allowcontact");
        String userid = argJsonObj.getString("USER_NM");

        String checkSql = "select * from proctick where evid = ? and tickid in(?)";
        final ArrayList<HashMap> list = this.pexecuteQuery(checkSql, new Object[]{eventid, strAllTickIds});
        if(list.size()>0){
            return "票號重複!請重選票號";
        }
        
        log.info("addReqTick:" + userid);
        String sql = "INSERT INTO proctick (evid,event,team,procman,procaddr, "
                + "tickid,tickname,ticktel,tickmemo,updatetime,allowcontact)   "
                + "VALUES (?,?,?,?,?,?,?,?,?,getdate(),?)";
        
        Object[] objs = new Object[10];
        objs[0] = eventid;
        objs[1] = event;
        objs[2] = teamname;
        objs[3] = userid;
        objs[4] = procaddr;
        
        //objs[5] = allTickIds;
        objs[6] = audiencename;
        objs[7] = audiencetel;
        objs[8] = tickmemo;
        objs[9] = allowcontact;
        
        int result = 0;
        for (String tickId : allTickIds) {
            objs[5] = tickId;
            result += this.pexecuteUpdate(sql, objs);        
        }
        if(result>=0){
            strMsg = "成功新增"+result+"筆索票登錄資料!";
        }else{
            strMsg = "異常！索票登錄資料未成功新增!";
        }
        return strMsg;
    }
    
    public String updateReqTickData(JSONObject argJsonObj) {
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
    
    
    //取得已輸入索票數
    public String getReqTickCount(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        String username = argJsonObj.getString("USER_NM");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencetel = argJsonObj.getString("audiencetel");
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM proctick where evid=? and procman=? "; 
        sql += " union ";
        sql += " SELECT '2audienceEvidCnt' as type, count(*) as cnt FROM proctick where evid=? and tickname=? and ticktel=?  ";
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username, evid, audiencename,audiencetel});
        
        String sql2 = " SELECT tickid FROM proctick where evid=? and tickname=? and ticktel=? order by tickid ";
        ArrayList<HashMap> result2 = this.pexecuteQuery(sql2, new Object[]{evid, audiencename,audiencetel});
        String allTickid = "";
        for(HashMap o:result2){
            allTickid += o.get("tickid").toString() + ",";
        }
        String addr="", memo="";
        if(result.size()>0){
            addr = result2.get(0).get("procaddr").toString();
            memo = result2.get(0).get("tickmemo").toString();
        }
        
        JSONObject jo = new JSONObject();
        jo.put("countSelf", result.isEmpty()? 0: result.get(0).get("cnt"));//該場登入者總輸入票根
        jo.put("audienceEvidCnt", result.isEmpty()? 0: result.get(1).get("cnt"));//該場該索票人已索票數
        jo.put("audienceTckList", allTickid);//該場該索票人已索票清單
        jo.put("procaddr", addr);//索票地點
        jo.put("tickmemo", memo);//索票備註
        return jo.toString();
    }
    
    
    //以票號取得該票之索票人索票的相關資訊
    public String getReqTickInfo(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        int tickid = argJsonObj.getInt("tickid");
        String username = argJsonObj.getString("USER_NM");
        
        //由票根號查出某人索票資料
        String sql = "SELECT '1proctickCount' as type, tickname, procman, ticktel, count(*) as cnt FROM proctick "
                        + "where tickname+ticktel in(" 
                        + "SELECT tickname+ticktel FROM proctick where evid=? and tickid=?)"
                        + " group by tickname, procman, ticktel "; 
                sql += " union "
                        + "SELECT '2showtickCount' as type, '', '','', count(*) as cnt FROM showtick where tickid in(" 
                        + "SELECT tickid FROM proctick where tickname+ticktel in(" 
                        + "SELECT tickname+ticktel FROM proctick where evid=? and tickid=?))"; 
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, tickid, evid, tickid});
        JSONObject jo = new JSONObject();
        final String proCnt = result.get(0).get("cnt").toString();
        jo.put("reqTickNo", proCnt);//
        jo.put("procman", proCnt.equals("0")? "查無索票紀錄!": result.get(0).get("procman"));//
        String reqAudienceName = proCnt.equals("0")? "-": result.get(0).get("tickname").toString();
        if(reqAudienceName.length()>=2){
            //reqAudienceName = reqAudienceName.substring(0, 1) + "○" + reqAudienceName.substring(2);
        }
        jo.put("reqName", reqAudienceName);//
        jo.put("reqTel", proCnt.equals("0")? "-": result.get(0).get("ticktel").toString());//
        jo.put("showTickNo", proCnt.equals("0")? 0: result.get(result.size()-1).get("cnt"));//
        
        String sqlComment = "SELECT * FROM tickcomment where tickid =?"; 
        result = this.pexecuteQuery(sqlComment, new Object[]{tickid});
        if(result.size()>0){
            jo.put("audiencename", result.get(0).get("audiencename").toString());
            jo.put("audiencecomment", result.get(0).get("audiencecomment").toString());
            jo.put("contactStatus", result.get(0).get("contactStatus").toString());
            jo.put("comment", result.get(0).get("comment").toString());
            jo.put("calltimes", result.get(0).get("calltimes").toString());
            jo.put("username", result.get(0).get("username").toString());
            jo.put("interest", result.get(0).get("interest").toString());
            jo.put("lastupdatetime", result.get(0).get("updatetime").toString());
        }else{
            
        }
        return jo.toString();
    }

    
    /**
     * TODO:之後有獨立table後再改寫
     * @param eventid
     * @return 
     */
    private String getEventName(String eventid) {
        if(eventid.equals("20181103"))
            return "南門公演";
        else if(eventid.equals("20181125"))
            return "板橋公演(11月)";
        else if(eventid.equals("20181229"))
            return "板橋公演(12月)";
        else if(eventid.equals("20190101"))
            return "國館公演";
        else
            return "";
    }
}
