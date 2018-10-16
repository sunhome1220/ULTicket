/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.syscom.db.DBUtil;
import com.syscom.util.Config;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.sql.rowset.CachedRowSet;
import base.CJBaseDao;
import util.DBUtil.APPCODE_CATEGORY;
import static util.DBUtil.transCachedRowSet2ArrayList;
import util.DateUtil;
import util.ExceptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import util.SqlUtil;
import util.User;
import org.apache.commons.io.IOUtils;
import util.FTPOperation;
import util.StringUtil;

/**
 *
 * @author User
 */
public class TickDao extends CJBaseDao {

    private CommonDao comDao = CommonDao.getInstance();
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

    //新增新案件到CASE_MAIN TABLE
    public String SaveNewDrugCase(JSONObject jObj) {
        User user = (User) jObj.get("userVO");

        LinkedHashMap<String, String> logCols = new LinkedHashMap();

        StringBuilder sql = new StringBuilder();
        StringBuilder sql_CASEID = new StringBuilder();
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        ArrayList qsPara = new ArrayList();
        String last_CASEID = "";
        int success = 0;

        //找最後一筆CODENAME(受處分人編號)
        sql_CASEID.append("select top 1 CJ_CASEID from CJDT_CASE_MAIN order by CJ_CASEID desc");
        list = this.executeQuery(sql_CASEID.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR) - 1911;
        int month = cal.get(Calendar.MONTH) + 1;
        String date = "" + year + String.format("%02d", month);
        if (!list.isEmpty()) {
            String tempCompare = (list.get(0).get("CJ_CASEID").toString()).substring(2, 7);
            if (tempCompare.equals(date)) {
                int temp = Integer.parseInt(list.get(0).get("CJ_CASEID").toString().substring(7));
                String formatAns = String.format("%05d", temp + 1);
                last_CASEID = list.get(0).get("CJ_CASEID").toString().substring(0, 7) + formatAns;
            } else {
                last_CASEID = "CA" + date + "00001";
            }
        } else {
            last_CASEID = "CA" + date + "00001";
        }
        sql.append("insert into CJDT_CASE_MAIN");
        sql.append("(CJ_CASEID,CJ_CASETITLE,CJ_PERSONALID,CJ_DETECTDATE,CJ_DETECTUNITLEVEL1,CJ_DETECTUNITLEVEL2,CJ_DETECTUNIT,CJ_DETECTPERSON,"
                + "CJ_DETECTPLACE,CJ_CLIENTCOUNT,CJ_DETECTPLACETYPE,CJ_DESCRIPTION,CJ_DETECTTIME,CJ_DETECTUNIT_NPA_UNIT_CD,"
                + "CJ_CREATETIME,"
                + "CJ_CREATORID_NPA,CJ_CREATORNM,CJ_CREATORUNITCODE,CJ_CREATORCITYCODE,"
                + "CJ_OWNER_NPA,CJ_OWNERNM,CJ_OWNERUNIT,CJ_OWNERCITYCODE,"
                + "CJ_LASTMODIFIER_NPA,CJ_LASTMODIFIERNM,CJ_LASTMODIFIERUNIT,CJ_LASTMODIFIERCITYCODE,CJ_LASTUPDATETIME,CJ_OVER60REASON, CJ_CASE_STATUS,CJ_DELETE_FLAG)");
        sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?,?,?,?,?,?,?,?,getdate(),LTRIM(?),'00',0)");
        qsPara.add(last_CASEID);
        qsPara.add(jObj.getString("CASETITLE"));
        qsPara.add(jObj.getString("PERSONALID"));
        //qsPara.add(getUsDateTime(jObj.get("DETECTDATE").toString()));
        qsPara.add(jObj.getString("DETECTUNITLEVEL1"));
        qsPara.add(jObj.getString("DETECTUNITLEVEL2"));
        if ("".equals(jObj.getString("CJ_DETECTUNIT_DISP"))) {
            qsPara.add(jObj.has("DETECTUNITLEVEL3") ? jObj.getString("DETECTUNITLEVEL3") : "");
        } else {
            qsPara.add(jObj.getString("CJ_DETECTUNIT_DISP"));
        }
        qsPara.add(jObj.getString("DETECTPERSON"));
        qsPara.add(jObj.getString("DETECTPLACE"));
        qsPara.add(jObj.getString("CLIENTCOUNT"));
        qsPara.add(jObj.getString("DETECTPLACETYPE"));
        qsPara.add(jObj.getString("DESCRIPTION"));
        qsPara.add(jObj.getString("DETECTTIME"));
        //塞CJ_DETECTUNIT_NPA_UNIT_CD
        qsPara.add((jObj.has("DETECTUNITLEVEL3") && !"".equals(jObj.getString("DETECTUNITLEVEL3"))) ? jObj.getString("DETECTUNITLEVEL3") : ((jObj.has("DETECTUNITLEVEL2") && !"".equals(jObj.getString("DETECTUNITLEVEL2"))) ? jObj.getString("DETECTUNITLEVEL2") : jObj.getString("DETECTUNITLEVEL1")));
        //CJ_CREATORID_NPA
        qsPara.add(jObj.getString("USER_ID"));
        qsPara.add(jObj.getString("USER_NM"));
        qsPara.add(jObj.getString("USER_UNITCD2"));
        //qsPara.add(jObj.getString("USER_CITYCODE"));
        if (jObj.getString("USER_UNIT").indexOf("A2") > -1) {
            qsPara.add("00000");
        } else {
            qsPara.add(jObj.getString("USER_CITYCODE"));
        }
        //CJ_OWNER_NPA
        qsPara.add(jObj.getString("USER_ID"));
        qsPara.add(jObj.getString("USER_NM"));
        if (jObj.has("OWNERUNITLEVEL1") && !"".equals(jObj.getString("OWNERUNITLEVEL1"))) {
            qsPara.add(jObj.getString("OWNERUNITLEVEL1"));//專業單位存所選取的裁罰單位
        }
//        if (jObj.has("OWNERUNITLEVEL1") && !"".equals(jObj.getString("OWNERUNITLEVEL1")) && jObj.getString("OWNERUNITLEVEL1").indexOf("A2") > -1) {
//            qsPara.add(jObj.getString("OWNERUNITLEVEL1"));//專業單位存所選取的裁罰單位
//        } else {
//            if (!"".equals(jObj.getString("USER_UNITCD2")) && jObj.getString("USER_UNITCD2") != null) {
//                qsPara.add(jObj.getString("USER_UNITCD2"));
//            } else {
//                qsPara.add(jObj.getString("USER_UNITCD1"));//直接存最高的裁罰單位
//            }
//        }
//        qsPara.add(getUnitCityCode(jObj.getString("OWNERUNITLEVEL1")));
        //CJ_LASTMODIFIER_NPA
        qsPara.add(jObj.getString("USER_ID"));
        qsPara.add(jObj.getString("USER_NM"));
        qsPara.add(jObj.getString("USER_UNIT"));
        //qsPara.add(jObj.getString("USER_CITYCODE"));
        if (jObj.getString("USER_UNIT").indexOf("A2") > -1) {
            qsPara.add("00000");
        } else {
            qsPara.add(jObj.getString("USER_CITYCODE"));
        }
        qsPara.add(jObj.getString("OVER60REASON"));
        try {
            success = this.pexecuteUpdate(sql.toString(), qsPara.toArray());

//            String logSql = "SELECT * FROM CJDT_CASE_MAIN WHERE CJ_CASEID='" + last_CASEID + "' ";
//            //NPALog.logInsertFromDB(user, "毒品案件維護-新增案件", //NPALog.ModifyResult.SUCCESS, logSql);
            jObj.put("CASEID", last_CASEID);
            //查獲單位中文
            jObj.put("DETECTUNITLEVEL1_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL1")));
            jObj.put("DETECTUNITLEVEL2_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL2")));
            jObj.put("DETECTUNITLEVEL3_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL3")));
            jObj.put("DETECTUNIT_DISP", jObj.get("DETECTUNITLEVEL1_DISP").toString() + jObj.get("DETECTUNITLEVEL2_DISP").toString() + jObj.get("DETECTUNITLEVEL3_DISP").toString());
            //裁罰單位中文
            jObj.put("OWNERUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("OWNERUNITLEVEL1")));
            //查獲場所類別中文
            
            logCols.put("CASEID", "案件編號");
            logCols.put("CASETITLE", "案件名稱");
            logCols.put("PERSONALID", "單位自訂案號");
            logCols.put("DETECTUNIT_DISP", "查獲單位");
            logCols.put("DETECTPERSON", "查獲人員");
            logCols.put("CLIENTCOUNT", "查獲人數");
            logCols.put("DETECTPLACE", "查獲地點");
            logCols.put("DETECTPLACETYPE_DISP", "查獲場所類別");
            logCols.put("DETECTDATE", "查獲時間");
            logCols.put("OWNERUNIT_DISP", "裁罰單位");
            logCols.put("DESCRIPTION", "案情摘述");

            
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return "";
        }
        if (success >= 1) {
            return last_CASEID;
        } else {
            return "";
        }
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
