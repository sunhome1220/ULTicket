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
import org.json.JSONObject;
import util.DBUtil;
import util.DateUtil;

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
        String eventid = argJsonObj.getString("eventid");
        String event = getEventName(eventid);//argJsonObj.getString("event");
        String teamname = argJsonObj.getString("team");
        String procman = argJsonObj.getString("procman");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencetel = argJsonObj.getString("audiencetel");
        String procaddr = argJsonObj.getString("procaddr");
        String tickmemo = argJsonObj.getString("tickmemo");
        int allowcontact = argJsonObj.getInt("allowcontact");//滿意度調查
        int seatType = argJsonObj.getInt("seatType");//座位類別
        int friendType = argJsonObj.getInt("friendType");//是否為伙伴親友(0:一般觀眾，1:伙伴親友)
        int confirmStatus = argJsonObj.getInt("confirmStatus");//確認
        String userid = argJsonObj.getString("USER_NM");

        String checkSql = "select tickid,tickname from proctick where evid = ? and tickid in("+strAllTickIds+")";
        final ArrayList<HashMap> list = this.pexecuteQuery(checkSql, new Object[]{eventid});
        if(list.size()>0){
            String msg = "";
            for(HashMap m: list){
                msg += "\n票號:" + m.get("tickid").toString();
                msg += "(" + m.get("tickname").toString()+")";
            }
            return "票號重複!請重選票號!\n" + msg;
        }
        
        log.info("addReqTick:" + userid);
        String sql = "INSERT INTO proctick (evid,event,team,procman,procaddr, "
                + "tickid,tickname,ticktel,tickmemo,createtime,allowcontact,creator,seatType,confirmStatus,friendType)   "
                + "VALUES (?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?)";
        
        Object[] objs = new Object[14];
        objs[0] = eventid;
        objs[1] = event;
        objs[2] = teamname;
        objs[3] = procman;
        objs[4] = procaddr;
        
        //objs[5] = allTickIds;
        objs[6] = audiencename;
        objs[7] = audiencetel;
        objs[8] = tickmemo;
        objs[9] = allowcontact;
        objs[10] = userid;
        objs[11] = seatType;
        objs[12] = confirmStatus;
        objs[13] = friendType;
        
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
    
    public String addReqTickDataAudi(JSONObject arg) {//20181029 for 觀眾索票
        String strMsg = "";
        final String strAllTickIds = arg.getString("allTickIds");
        String[] allTickIds = strAllTickIds.split("[,]");
        String eventid = arg.getString("eventid");
        String event = getEventName(eventid);//argJsonObj.getString("event");
        String teamname = arg.getString("team");
        String procman = arg.getString("procman");
        String audiencename = arg.getString("audiencename");
        String audiencetel = arg.getString("audiencetel");
        String procaddr = arg.has("procaddr")?arg.getString("procaddr"):"";
        String tickmemo = arg.has("tickmemo")?arg.getString("tickmemo"):"";
        int allowcontact = arg.getInt("allowcontact");
        String userid = arg.getString("USER_NM");

        String checkSql = "select tickid,tickname from proctick where evid = ? and tickid in("+strAllTickIds+")";
        final ArrayList<HashMap> list = this.pexecuteQuery(checkSql, new Object[]{eventid});
        if(list.size()>0){
            String msg = "";
            for(HashMap m: list){
                msg += "\n票號:" + m.get("tickid").toString();
                msg += "(" + m.get("tickname").toString()+")";
            }
            return "票號重複!請重選票號!\n" + msg;
        }
        
        log.info("addReqTick:" + userid);
        String sql = "INSERT INTO proctick (evid,event,team,procman,procaddr, "
                + "tickid,tickname,ticktel,tickmemo,updatetime,allowcontact,creator)   "
                + "VALUES (?,?,?,?,?,?,?,?,?,getdate(),?,?)";
        
        Object[] objs = new Object[11];
        objs[0] = eventid;
        objs[1] = event;
        objs[2] = teamname;
        objs[3] = procman;
        objs[4] = procaddr;
        
        //objs[5] = allTickIds;
        objs[6] = audiencename;
        objs[7] = audiencetel;
        objs[8] = tickmemo;
        objs[9] = allowcontact;
        objs[10] = userid;
        
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
    
    public String delReqTickData(JSONObject argJsonObj) {
        String strMsg = "";
        final String strAllTickIds = argJsonObj.getString("allTickIds");
        String[] allTickIds = strAllTickIds.split("[,]");
        String eventid = argJsonObj.getString("eventid");
        String event = getEventName(eventid);
        String teamname = argJsonObj.getString("team");
        String audiencename = argJsonObj.getString("audiencename");
        String audiencetel = argJsonObj.getString("audiencetel");
        String procaddr = argJsonObj.getString("procaddr");
        String tickmemo = argJsonObj.getString("tickmemo");
        int allowcontact = argJsonObj.getInt("allowcontact");
        String userid = argJsonObj.getString("USER_NM");

        log.info("delReqTick:" + userid);
        String sql = "delete proctick where evid=? and tickid in(?) and tickname=? and ticktel=? ";
                
        
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
        String eventid = argJsonObj.getString("eventid");
        String allTickIds = argJsonObj.getString("allTickIds");
        String oldAllTckIds = argJsonObj.getString("originalAllTickNo");
        if(oldAllTckIds.endsWith(","))oldAllTckIds=oldAllTckIds.substring(0,oldAllTckIds.length()-1);
        if(oldAllTckIds.split(",").length==0 || oldAllTckIds.split(",").length>4){
            //TODO:只能是數字+逗號
            //return "";
        }
        String audiencename = argJsonObj.getString("audiencename");
        String audiencetel = argJsonObj.getString("audiencetel");
        String procaddr = argJsonObj.getString("procaddr");
        String tickmemo = argJsonObj.getString("tickmemo");
        int allowcontact = argJsonObj.getInt("allowcontact");
        String userid = argJsonObj.getString("USER_NM");

        if(allTickIds.split(",").length > oldAllTckIds.split(",").length){
            //增加票券數
            String addTickIds = allTickIds.replace(oldAllTckIds+",", "");
            argJsonObj.put("allTickIds", addTickIds);
            strMsg += addReqTickData(argJsonObj) + ";";
        }else if(allTickIds.split(",").length < oldAllTckIds.split(",").length){
            //減少
            //strMsg += delReqTickData(argJsonObj) + ";";
            strMsg += "暫不開放刪除功能!" + ";";
        }
        log.info("updateReqTick:" + userid);
        String sql = "update proctick set procman=?,procaddr=?, tickname=?, ticktel=?,tickmemo=?, "
                + "     updatetime=getdate(), allowcontact=? "
                + " where evid=? and tickid in ("+oldAllTckIds+") ";//select tickid from proctick where evid
                
        Object[] objs = new Object[7];
        objs[0] = userid;//audiencename;
        objs[1] = procaddr;
        objs[2] = audiencename;        
        objs[3] = audiencetel;        
        objs[4] = tickmemo;
        objs[5] = allowcontact;
        objs[6] = eventid;
        //objs[7] = oldAllTckIds;
        
        int result = this.pexecuteUpdate(sql, objs);        
        if(result>=0){
            strMsg += "成功更新索票資料!";//(索票數:"+ result +")";
            if(result!=allTickIds.split("[,]").length){
                
            }
        }else{
            strMsg += "異常！索票資料未成功更新!";
        }
        return strMsg;
    }
    
    //針對一筆update
    public String updateReqTickDataOne(JSONObject argJsonObj) {
        String strMsg = "";
        int taginc = argJsonObj.getInt("taginc");
        String eventid = argJsonObj.getString("eventid");
        String procman = argJsonObj.getString("procman");//發票人
        String tickname = argJsonObj.getString("tickname");//索票人
        String ticktel = argJsonObj.getString("ticktel");
        int allowcontact = argJsonObj.getInt("allowcontact");//滿意度調查
        int seatType = argJsonObj.getInt("seatType");//
        int friendType = argJsonObj.getInt("friendType");//
        int confirmStatus = argJsonObj.getInt("confirmStatus");//
        String procaddr = argJsonObj.getString("procaddr");//索票地
        String tickmemo = argJsonObj.getString("tickmemo");//備註
        
        String userid = argJsonObj.getString("USER_NM");
        boolean updateOtherTick = argJsonObj.has("updateOtherTick") && argJsonObj.getString("updateOtherTick").equals("true");
        
        String oldTickname ="";
        String oldTicktel ="";
        if(updateOtherTick){
            String sqlQueryOldData = "select evid, tickname, ticktel from proctick where taginc = ?";
            final ArrayList<HashMap> listOld = this.pexecuteQuery(sqlQueryOldData, new Object[]{taginc});
            oldTickname = ((HashMap)listOld.get(0)).get("tickname").toString();
            oldTicktel = ((HashMap)listOld.get(0)).get("ticktel").toString();
        }
        
        log.info("updateReqTick:" + userid);
        String sql = "update proctick set procman=?,procaddr=?, tickname=?, ticktel=?,tickmemo=?, "
                + "     updatetime=getdate(), lastUpdater=?, allowcontact=? , seatType=?, friendType=?, confirmStatus=?";
        //if(!updateOtherTick){
        sql += " where taginc = ? ";//一般狀況，只更新一筆
        if(updateOtherTick){
            sql += " or taginc in "//更新同場其他同姓名同電話的索票資料
                + "(select taginc from proctick where trim(evid)+trim(tickname)+trim(ticktel) = '"+ eventid+oldTickname+oldTicktel +"')";
        }
                
        Object[] objs = new Object[11];
        objs[0] = procman;//audiencename;
        objs[1] = procaddr;
        objs[2] = tickname;        
        objs[3] = ticktel;        
        objs[4] = tickmemo;
        objs[5] = userid;
        objs[6] = allowcontact;        
        objs[7] = seatType;
        objs[8] = friendType;
        objs[9] = confirmStatus;
        objs[10] = taginc;
        //objs[7] = oldAllTckIds;
        
        JSONObject jo = new JSONObject();
        int result = this.pexecuteUpdate(sql, objs);        
        if(result>=1){
            strMsg += "成功更新 "+result+" 筆索票資料!";//(索票數:"+ result +")";            
            jo.put("lastUpdater", userid);
            jo.put("updatetime", DateUtil.getDateTime());
        }else{
            strMsg += "異常！索票資料未成功更新!";
        }
        jo.put("resultMsg", strMsg);
        return jo.toString();
    }
    
    //刪除一筆
    public String deleteReqTickDataOne(JSONObject argJsonObj) {
        String strMsg = "";
        int taginc = argJsonObj.getInt("taginc");

        log.info("deleteReqTick:" + taginc);
        String sql = "delete proctick where taginc = ? ";
                
        Object[] objs = new Object[1];
        objs[0] = taginc;
        
        JSONObject jo = new JSONObject();
        int result = this.pexecuteUpdate(sql, objs);        
        if(result>=1){
            strMsg += "成功刪除索票資料!";//(索票數:"+ result +")";                        
            jo.put("updatetime", DateUtil.getDateTime());
        }else{
            strMsg += "異常！索票資料未刪除成功!";
        }
        jo.put("resultMsg", strMsg);
        return jo.toString();
    }
    
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
//        String audiencename = argJsonObj.getString("audiencename");
//        String audiencetel = argJsonObj.getString("audiencetel");
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM proctick where evid=? and (procman=? ) "; 
        sql += " union ";
        sql += "SELECT '3pcnt' as type, count(*) as cnt FROM proctick where evid=? and (creator=? and procman!=?)"; 
//        sql += " union ";
//        sql += " SELECT '2audienceEvidCnt' as type, count(*) as cnt FROM proctick where evid=? and tickname=? and ticktel=?  ";
        //ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username, evid, username, username, evid, audiencename,audiencetel});
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username, evid, username, username});
        
//        String sql2 = " SELECT tickid,procaddr,tickmemo,allowcontact,updatetime FROM proctick where evid=? and tickname=? and ticktel=? order by tickid ";
//        ArrayList<HashMap> result2 = this.pexecuteQuery(sql2, new Object[]{evid, audiencename,audiencetel});
//        String allTickid = "";
//        for(HashMap o:result2){
//            allTickid += o.get("tickid").toString() + ",";
//        }
//        String addr="", memo="", allowcontact="",updatetime="";
//        if(result2.size()>0){
//            addr = result2.get(0).get("procaddr").toString();
//            memo = result2.get(0).get("tickmemo").toString();
//            updatetime = result2.get(0).get("updatetime").toString();
//            allowcontact = result2.get(0).get("allowcontact")==null?"0":result2.get(0).get("allowcontact").toString();
//        }
        
        JSONObject jo = new JSONObject();
        jo.put("countSelf", result.isEmpty()? 0: result.get(0).get("cnt"));//該場登入者總輸入票
        jo.put("countSelf2", result.isEmpty()? 0: result.get(1).get("cnt"));//該場登入者幫他人登錄總數
//        jo.put("audienceEvidCnt", result.isEmpty()? 0: result.get(1).get("cnt"));//該場該索票人已索票數
//        jo.put("audienceTckList", allTickid);//該場該索票人已索票清單
//        jo.put("procaddr", addr);//索票地點
//        jo.put("tickmemo", memo);//索票備註
//        jo.put("updatetime", updatetime);//updatetime
//        jo.put("allowcontact", allowcontact);//索票備註
        return jo.toString();
    }
    
    
    //以票號取得該票之索票人索票的相關資訊
    public String getReqTickInfo(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        int tickid = argJsonObj.getInt("tickid");
        String username = argJsonObj.getString("USER_NM");
        
        //由票根號查出某人索票資料
        String sql = "SELECT '1proctickCount' as type, tickname, procman, ticktel, team, count(*) as cnt FROM proctick "
                        + "where tickname+ticktel in(" 
                        + "SELECT tickname+ticktel FROM proctick where evid=? and tickid=?)"
                        + " group by tickname, procman, ticktel, team "; 
                sql += " union "
                        + "SELECT '2showtickCount' as type, '', '','','', count(*) as cnt FROM showtick where tickid in(" 
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
        String procTel = proCnt.equals("0")? "-": result.get(0).get("ticktel").toString();
        
        final String dataTeam = result.get(0).get("team").toString();
        //jo.put("team", dataTeam);//20181111 視權限決定查不查得到
        if(argJsonObj.getInt("ROLE")<5 && !argJsonObj.getString("USER_TEAM").equals(dataTeam)){
            if(reqAudienceName.length()>=2){
                reqAudienceName = reqAudienceName.substring(0, 1) + "○" + reqAudienceName.substring(2);
            }
            if(procTel.length()>6){
                procTel = procTel.substring(0, 6) + "**"+"-"+ dataTeam;
            }
        }                
        jo.put("reqName", reqAudienceName);//
        jo.put("reqTel", procTel);//
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
            jo.put("age", result.get(0).get("age").toString());
            jo.put("satisfaction", result.get(0).get("satisfaction").toString());
            jo.put("interest", result.get(0).get("interest").toString());
            jo.put("lastupdatetime", result.get(0).get("updatetime").toString());
        }else{
            
        }
        return jo.toString();
    }
    
    //以票號取得該票之索票人電話，並判斷是否已有人聯絡過
    public String getContactInfo(JSONObject argJsonObj) {
        int evid = argJsonObj.getInt("eventid");
        int tickid = argJsonObj.getInt("tickid");
        
        //由票根號查出某人索票資料
        String sql = "SELECT * FROM tickcomment "
                + "where ticktel in (select ticktel from tickcomment where evid=? and tickid=?) "
                + " and contactStatus<>0"; 
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, tickid});
        JSONObject jo = new JSONObject();
        final int contactCnt = result.size();
        jo.put("contactCnt", contactCnt);//
        String contactLog = "";
        if(contactCnt>0){//曾經有人聯絡過
            for(HashMap m: result){
                contactLog += "\n" + getContactStatusStr( m.get("contactStatus").toString()) 
                        + "\t" + m.get("lastestCallernm").toString() 
                        + "於" + m.get("updatetime").toString().substring(0,16) + " 聯絡";
            }            
        }else{
            contactLog = "查無任何聯絡記錄(也可能是聯絡過但沒記錄)";
        }
        jo.put("log", contactLog);
        return jo.toString();
    }

    String getContactStatusStr(String status){
        switch (status) {
            case "1":
                return "有上課意願";
            case "2":
                return "有興趣但暫無時間";
            case "3":
                return "電話錯誤或空號";
            case "4":
                return "完全沒興趣";
            default:
                return "";
        }
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
            return "板橋(1125)";
        else if(eventid.equals("20181229"))
            return "板橋(1229)";
        else if(eventid.equals("20190101"))
            return "國館公演";
        else
            return "";
    }
    
    /**
     * 取得索票出席統計資料
     * @return 
     */
    public JSONObject getTickStatInfo() {
        JSONObject jo = new JSONObject();
        JSONObject jots = new JSONObject();
        JSONObject jot = new JSONObject();
                
        String sql = "update perform set perform.confirmCnt=p.reqcnt from perform left join(SELECT evid,confirmStatus,count(*) as reqcnt FROM proctick group by evid,confirmStatus having confirmStatus =1) p on perform.evid=p.evid";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});
        sql = "update perform set perform.showcnt=s.showcnt from perform left join(SELECT evid,count(*) as showcnt FROM showtick group by evid) s on perform.evid=s.evid ";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});
        sql = "update perform set perform.reqcnt=p.reqcnt from perform left join(SELECT evid,count(*) as reqcnt FROM proctick group by evid) p on perform.evid=p.evid ";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});
        sql = "update perform set perform.cancelCnt=p.reqcnt from perform left join(SELECT evid,confirmStatus,count(*) as reqcnt FROM proctick group by evid,confirmStatus having confirmStatus =-1) p on perform.evid=p.evid";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});
        sql = "update perform set perform.friendCnt=p.reqcnt from perform left join(SELECT evid,friendType,count(*) as reqcnt FROM proctick group by evid,friendType having friendType =1) p on perform.evid=p.evid";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});        
        sql = "update perform set perform.showcntNotProc=p.showcntNotProc, updatetime=getdate() from perform left join(SELECT evid,count(*) as showcntNotProc FROM showtick  where tickid not in (select tickid from proctick) group by evid) p on perform.evid=p.evid";
        DBUtil.getInstance().pexecuteUpdate(sql, new Object[]{});        
        
        sql = "SELECT * FROM perform where enabled = 1";
//        sql = "SELECT * FROM perform where evid >= 20181103";
        sql += "order by evid";
        ArrayList qsPara = new ArrayList();    
        boolean passed = false;        
        String msg = "";
        String msg2 = "";
        try{              
            ArrayList list = DBUtil.getInstance().pexecuteQuery(sql, qsPara.toArray());
            if(list.size() > 0){//有資料
                for(Object o: list){                    
                    HashMap m = (HashMap)o;
                    String evid = m.get("evid").toString();
                    int reqcnt = m.get("reqcnt").toString().equals("")?0:Integer.parseInt(m.get("reqcnt").toString());
                    int confirmCnt = m.get("confirmCnt").toString().equals("")?0:Integer.parseInt(m.get("confirmCnt").toString());
                    int cancelCnt = m.get("cancelCnt").toString().equals("")?0:Integer.parseInt(m.get("cancelCnt").toString());
                    int friendCnt = m.get("friendCnt").toString().equals("")?0:Integer.parseInt(m.get("friendCnt").toString());
                    int showCnt = m.get("showcnt").toString().equals("")?0:Integer.parseInt(m.get("showcnt").toString());
                    int showcntNotProc = m.get("showcntNotProc").toString().equals("")?0:Integer.parseInt(m.get("showcntNotProc").toString());
                    jot.put("reqcnt", reqcnt);
                    jot.put("confirmCnt", confirmCnt);
                    jot.put("cancelCnt", cancelCnt);
                    jot.put("friendCnt", friendCnt);
                    jot.put("showCnt", showCnt);
                    jot.put("showcntNotProc", showcntNotProc);
                    jots.append(evid, jot);
                    int predictShowNo = (int)(friendCnt * 0.9 + (reqcnt - friendCnt - cancelCnt) *0.4);
                    String seatSize = m.get("seatsize").toString();
                    int predictShowPect = (int)(((float)predictShowNo/Integer.parseInt(seatSize)) * 100);
                    int realShowPect = (int)(((float)showCnt/Integer.parseInt(seatSize)) * 100);
                    
                    //msg += evid + "-" + m.get("event").toString() + "-"
                    msg += m.get("event").toString() + "-"
                            + "座位:" + seatSize
                            + ",登記:" + reqcnt + "\n"
                            //+ ",確認將出席:" + m.get("confirmCnt").toString()
                            + "伙伴或親友:" + friendCnt + ""
                            + ",請假:" + cancelCnt + "\n"
                            + "預估出席:" + predictShowNo + "";
                    msg2 += "<tr><td >"+ evid.substring(0,8) + "<br>"
                            + "" + m.get("event").toString() + "</td>"
                            + "<td>" + ((seatSize.length()<4)?" ":"") + seatSize + "</td>"
                            + "<td>" + reqcnt + "</td>"
                            //+ ",確認將出席:" + m.get("confirmCnt").toString()
                            + "<td>" + friendCnt + "</td>"
                            + "<td>" + cancelCnt + "</td>"
                            + "<td>" + predictShowNo + "<br>("+ predictShowPect +"%)" + "</td>";
                    if(showCnt > 0){        
                        msg += ",實際出席:" + showCnt;
                        msg2 += "<td>" + showcntNotProc + "</td>";
                        msg2 += "<td>" + showCnt + "<br>("+ realShowPect +"%)</td>";
                    }else{
                        msg2 += "<td>N/A</td>";
                        msg2 += "<td>N/A</td>";
                    }
                    msg2 += "</tr>";
                    msg += "\n\n";
                }
                msg += "預估出席數=人才伙伴索票*0.9+ 一般民眾索票*0.4";
                msg += "";
                //HashMap data = (HashMap)list.get(0);                
            }            
        }catch(Exception e){
            
        }
        jo.put("data", jots);        
        jo.put("msg", msg);        
        jo.put("msgTable", msg2);        
        return jo;
    }
}
