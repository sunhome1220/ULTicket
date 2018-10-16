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
public class DrugCaseDao extends CJBaseDao {

    private CommonDao comDao = CommonDao.getInstance();
    private static DrugCaseDao instance = null;
    private static Logger log = Logger.getLogger(DrugCaseDao.class);

    private DrugCaseDao() {
    }

    public static DrugCaseDao getInstance() {
        if (instance == null) {
            instance = new DrugCaseDao();
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


    //讀取所有案件，部分欄位JOINCJDT_APP_CODE TABLE取得資料
    public JSONArray Get_AllCase(JSONObject jObj) {
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        String sql = "SELECT top 100 taginc,evid,event,team,procman,procaddr,tickid,tickname,ticktel,tickmemo FROM proctick";
        sql += " where team=? ";
                
        ArrayList<HashMap> list = null;
        try {
            qsPara.add(jObj.get("teamNm"));
            crs = this.pexecuteQueryRowSet(sql, qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("taginc", crs.getString("taginc"));
                jObject.put("evid", crs.getString("evid"));
                jObject.put("event", crs.getString("event").replace(" ", ""));
                jObject.put("team", crs.getString("team"));
                jObject.put("procman", crs.getString("procman"));
                jObject.put("procaddr", crs.getString("procaddr"));
                jObject.put("tickid", crs.getString("tickid"));
                String tickName = crs.getString("tickname");
                if(tickName.length()>=3){
                    tickName = tickName.substring(0, 1) + "○" + tickName.substring(2);
                }
                jObject.put("tickname", tickName);
                jObject.put("ticktel", crs.getString("ticktel"));
                jObject.put("tickmemo", crs.getString("tickmemo"));
                
                //jObject.put("DETECTDATE", getTwDateTime(crs.getDate("CJ_DETECTDATE")));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;

    }


    //刪除特定CASEID之案件
    public int DeleteCase_ByCASEID(JSONObject jObj) {
        User user = (User) jObj.get("userVO");
        StringBuilder sql = new StringBuilder();
        ArrayList<String> sqls = new ArrayList();
        ArrayList<ArrayList> params = new ArrayList();
        //選取多筆時先切割字串，將切出來的多個CASEID之案件刪除
        String[] CASEIDAfter = jObj.getString("CASEID").split(",");
        int success = 0;
        //先查CASE底下的所有受處分人狀態是否皆不為'00'(不為00就不可以刪)
        int number = 0;
        for (int j = 0; j < CASEIDAfter.length; j++) {
            String query = "select count(*) cnt from CJDT_FORM_PENALTY where CJ_CASEID =? AND (CJ_STATUS !='00' ) ";
            ArrayList<HashMap> list = null;
            try {
                list = pexecuteQuery(query, new Object[]{CASEIDAfter[j]});
                for (int i = 0; i < list.size(); i++) {
                    if (list != null && list.size() > 0) {
                        number += Integer.valueOf((String) list.get(i).get("cnt"));
                    }
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
        }
        //當受處分人狀態皆不為'00'的數量為0時才可以刪，否則不能刪
        if (number != 0) {
            return success;
        } else {
            for (int i = 0; i < CASEIDAfter.length; i++) {
                sql.setLength(0);
                sql.append("INSERT INTO CJDT_BACKUP_FORM_PENALTY SELECT * FROM CJDT_FORM_PENALTY WHERE CJ_CASEID=? ");
                sql.append("UPDATE CJDT_BACKUP_FORM_PENALTY SET CJ_LASTMODIFIERNM=?,CJ_LASTUPDATETIME=getdate() WHERE CJ_CASEID=? ");
                sql.append("UPDATE CJDT_CASE_CLIENT SET CJ_DELETE_FLAG=1,CJ_LASTUPDATETIME=getdate() WHERE CJ_CASEID=? ");//20180525 改成UPDATE
                sql.append("DELETE FROM CJDT_FORM_PENALTY WHERE CJ_CASEID=? ");
                sql.append("UPDATE CJDT_CASE_MAIN SET CJ_DELETE_FLAG=1,CJ_LASTUPDATETIME=getdate() WHERE CJ_CASEID=? ");
                ArrayList<Object> qsPara = new ArrayList<Object>();
                qsPara.add(CASEIDAfter[i]);
                qsPara.add(jObj.getString("USER_NM"));
                qsPara.add(CASEIDAfter[i]);
                qsPara.add(CASEIDAfter[i]);
                qsPara.add(CASEIDAfter[i]);
                qsPara.add(CASEIDAfter[i]);
//                try {
//                    success += this.pexecuteUpdate(sql.toString(), qsPara.toArray());
//                    success++;
//                } catch (Exception e) {
//                    log.error(ExceptionUtil.toString(e));
//                }
                sqls.add(sql.toString());
                params.add(qsPara);
            }
            Object[][] objParams = new Object[params.size()][];
            for (int i = 0; i < params.size(); i++) {
                objParams[i] = params.get(i).toArray();
            }
            try {
                int[] successArr = this.pexecuteBatch(sqls.toArray(), objParams);
                for (int i = 0; i < successArr.length; i++) {
                    success += successArr[i];
                }
                if (success == sqls.toArray().length) {
                    StringBuilder otherCondition = new StringBuilder();
                    comDao.putCondition(otherCondition, jObj, "CASEID", "毒品案件ID");
                    comDao.putCondition(otherCondition, jObj, "CASETITLE", "毒品案件名稱");
                    return success;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                return 0;
            }
        }
    }

    //取得所有受處分人資料
    public JSONArray Get_AllClient(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;

        sql.append("SELECT CJ_SEQNO,CJ_NAME,CJ_PID,CJ_MOBILE,CJ_HASPIC,CJ_SCHOOLSEQ,CJ_CLIENTTYPE,CJ_PERMADDR ");
        sql.append(" FROM CJDT_CASE_CLIENT ");
        sql.append(" WHERE CJ_DELETE_FLAG <> 1 ");

        ArrayList<HashMap> list = null;
        try {

            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("SEQNO", crs.getInt("CJ_SEQNO"));
                jObject.put("NAME", crs.getString("CJ_NAME"));
                jObject.put("PID", crs.getString("CJ_PID"));
                jObject.put("MOBILE", crs.getString("CJ_MOBILE"));
                jObject.put("HASPIC", crs.getString("CJ_HASPIC"));
                jObject.put("SCHOOLSEQ", crs.getInt("CJ_SCHOOLSEQ"));
                jObject.put("CLIENTTYPE", crs.getString("CJ_CLIENTTYPE"));
                jObject.put("PERMADDR", crs.getString("CJ_PERMADDR"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
        //return arrayList2JsonArray(list);
    }

    //取得特定CASEID之受處分人資料
    public JSONArray GET_Client_ByCASEID(JSONObject jObj) {
        StringBuilder otherCondition = new StringBuilder();
        StringBuilder queryConditionId = new StringBuilder();
        StringBuilder queryConditionName = new StringBuilder();
        StringBuilder queryConditionCarNo = new StringBuilder();
        LinkedHashMap<String, String> logCols = new LinkedHashMap();

        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;

        sql.append("SELECT  CC.CJ_SEQNO,CC.CJ_NAME,CC.CJ_PID,FP.CJ_STATUS,FP.CJ_FINESTATUS,FP.CJ_WORKSHOPSTATUS,MIN(ISNULL(PS.CJ_FINESTATUS,'')) AS SFINESTATUS ");
        sql.append(" FROM CJDT_CASE_CLIENT CC "
                + " LEFT JOIN CJDT_FORM_PENALTY FP ON CC.CJ_SEQNO=FP.CJ_CLIENTSEQ AND CC.CJ_CASEID=FP.CJ_CASEID "
                + " LEFT JOIN CJDT_FORM_PENAL_SUM PS ON CC.CJ_SEQNO=PS.CJ_CLIENTSEQ AND CC.CJ_CASEID=PS.CJ_CASEID ");
        sql.append(" WHERE CC.CJ_CASEID = ? AND CC.CJ_DELETE_FLAG <> 1 "
                + "GROUP BY CC.CJ_SEQNO,CC.CJ_NAME,CC.CJ_PID,FP.CJ_STATUS,FP.CJ_FINESTATUS,FP.CJ_WORKSHOPSTATUS ORDER BY CC.CJ_SEQNO ");
        qsPara.add(jObj.getString("CASEID"));
        comDao.putCondition(otherCondition, jObj, "CASEID", "案件編號");

        ArrayList<HashMap> list = new ArrayList();
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("SEQNO", crs.getInt("CJ_SEQNO"));
                jObject.put("NAME", crs.getString("CJ_NAME"));
                jObject.put("PID", crs.getString("CJ_PID"));
                String REALSTATUS = "";
                String REALFINESTATUS = "";
                String REALWORKSHOPSTATUS = "";
                String REALSFINESTATUS = "";
                switch (StringUtil.nvl(crs.getString("CJ_STATUS"))) {
                    case "00":
                        REALSTATUS = "待裁罰";
                        break;
                    case "01":
                        REALSTATUS = "送達中";
                        break;
                    case "02":
                        REALSTATUS = "已送達";
                        break;
                    case "03":
                        REALSTATUS = "訴願中";
                        break;
                    case "04":
                        REALSTATUS = "已撤銷";
                        break;
                    case "05":
                        REALSTATUS = "已結案";
                        break;
                    case "06":
                        REALSTATUS = "已結案(無需裁罰)";
                        break;
                    case "WJ":
                        REALSTATUS = "等候判決結果";
                        break;
                    default:
                        REALSTATUS = "待裁罰";
                        break;
                }
                jObject.put("STATUS", REALSTATUS);
                switch (StringUtil.nvl(crs.getString("CJ_WORKSHOPSTATUS"))) {
                    case "01":
                        REALWORKSHOPSTATUS = "待講習";
                        break;
                    case "02":
                        REALWORKSHOPSTATUS = "訴願中";
                        break;
                    case "03":
                        REALWORKSHOPSTATUS = "待處怠金";
                        break;
                    case "04":
                        REALWORKSHOPSTATUS = "已完成";
                        break;
                    case "05":
                        REALWORKSHOPSTATUS = "已撤銷";
                    default:
                        REALWORKSHOPSTATUS = "";
                        break;
                }
                jObject.put("WORKSHOPSTATUS", REALWORKSHOPSTATUS);
                switch (StringUtil.nvl(crs.getString("CJ_FINESTATUS"))) {
                    case "00":
                        REALFINESTATUS = "待更換債權憑證";
                        break;
                    case "01":
                        REALFINESTATUS = "待繳納";
                        break;
                    case "02":
                        REALFINESTATUS = "訴願中";
                        break;
                    case "03":
                        REALFINESTATUS = "待移送行政執行處";
                        break;
                    case "04":
                        REALFINESTATUS = "行政執行處執行中";
                        break;
                    case "05":
                        REALFINESTATUS = "已繳納";
                        break;
                    case "06":
                        REALFINESTATUS = "已撤銷";
                        break;
                    case "07":
                        REALFINESTATUS = "等候下次行政執行";
                        break;
                    case "10":
                        REALFINESTATUS = "送達中";
                        break;
                    default:
                        REALFINESTATUS = "";
                        break;
                }
                jObject.put("FINESTATUS", REALFINESTATUS);
                switch (StringUtil.nvl(crs.getString("SFINESTATUS"))) {
                    case "00":
                        REALSFINESTATUS = "待更換債權憑證";
                        break;
                    case "01":
                        REALSFINESTATUS = "待繳納";
                        break;
                    case "02":
                        REALSFINESTATUS = "訴願中";
                        break;
                    case "03":
                        REALSFINESTATUS = "待移送行政執行處";
                        break;
                    case "04":
                        REALSFINESTATUS = "行政執行處執行中";
                        break;
                    case "05":
                        REALSFINESTATUS = "已繳納";
                        break;
                    case "06":
                        REALSFINESTATUS = "已撤銷";
                        break;
                    case "07":
                        REALSFINESTATUS = "等候下次行政執行";
                        break;
                    case "10":
                        REALSFINESTATUS = "送達中";
                        break;
                    default:
                        REALFINESTATUS = "";
                        break;
                }
                jObject.put("SFINESTATUS", REALSFINESTATUS);
                resultDataArray.put(jObject);
                transCachedRowSet2ArrayList(list, crs);
            }
            User user = (User) jObj.get("userVO");
            logCols.put("CJ_NAME", "受處分人姓名");
            logCols.put("CJ_PID", "受處分人身分證號");

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
        //return arrayList2JsonArray(list);
    }

    //取得特定編號之受處分人
    public JSONArray GET_Client_BySEQNO(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;

//        sql.append("SELECT *");
//        sql.append(" FROM CASE_CLIENT ");
        sql.append("SELECT SCHOOLNAME.CJ_SCHOOLNAME NEWSCHOOLNAME, ISNULL(HOUSETOWN.CJ_CODEDISP,'') NEWHOUSETOWN, ISNULL(HOUSECITY.CJ_CODEDISP,'') NEWHOUSECITY, "
                + "ISNULL(PERMTOWN.CJ_CODEDISP,'') NEWPERMTOWN, ISNULL(PERMCITY.CJ_CODEDISP,'') NEWPERMCITY, ISNULL(DRUGSOURCE.CJ_CODEDISP,'') NEWDRUGSOURCE, ISNULL(COMMITREASON.CJ_CODEDISP,'') NEWCOMMITREASON, ISNULL(OCCUPATION.CJ_CODEDISP,'') NEWOCCUPATION, "
                + "ISNULL(EDUCATION.CJ_CODEDISP,'') NEWEDUCATION,ISNULL(CLIENTTYPE.CJ_CODEDISP,'') NEWCLIENTTYPE,CC.* FROM CJDT_CASE_CLIENT CC "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='CLIENTTYPE') CLIENTTYPE ON CC.CJ_CLIENTTYPE =CLIENTTYPE.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='EDUCATION') EDUCATION ON CC.CJ_EDUCATION =EDUCATION.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='OCCUPATION') OCCUPATION ON CC.CJ_OCCUPATION =OCCUPATION.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='COMMITREASON') COMMITREASON ON CC.CJ_COMMITREASON =COMMITREASON.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='DRUGSOURCE') DRUGSOURCE ON CC.CJ_DRUGSOURCE =DRUGSOURCE.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_CITY_CODE) PERMCITY ON CC.CJ_PERMCITY = PERMCITY.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_TOWN_CODE WHERE CJ_CODEDISP NOT LIKE '＊%') PERMTOWN ON CC.CJ_PERMCITY = PERMTOWN.CJ_CITYCODE and CC.CJ_PERMTOWN = PERMTOWN.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_CITY_CODE) HOUSECITY ON CC.CJ_HOUSECITY = HOUSECITY.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_TOWN_CODE WHERE CJ_CODEDISP NOT LIKE '＊%') HOUSETOWN ON CC.CJ_HOUSECITY = HOUSETOWN.CJ_CITYCODE and CC.CJ_HOUSETOWN = HOUSETOWN.CJ_CODECODE "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_SCHOOL) SCHOOLNAME ON SCHOOLNAME.CJ_CITYCODE = CC.CJ_SCHOOLCITY AND SCHOOLNAME.CJ_SCHOOLLEVEL = CC.CJ_SCHOOLLEVEL AND SCHOOLNAME.CJ_SEQ = CC.CJ_SCHOOLSEQ ");
        sql.append(" WHERE CJ_SEQNO=? AND CJ_DELETE_FLAG <> 1");
        qsPara.add(jObj.getInt("SEQNO"));

        ArrayList<HashMap> list = null;
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("SEQNO", crs.getInt("CJ_SEQNO"));
                jObject.put("NAME", crs.getString("CJ_NAME"));
                jObject.put("PID", crs.getString("CJ_PID"));
                jObject.put("CODENAME", crs.getString("CJ_CODENAME"));
                jObject.put("CLIENTTYPE", (crs.getString("NEWCLIENTTYPE") != null) ? crs.getString("NEWCLIENTTYPE") : "");
                jObject.put("BDATE", crs.getString("CJ_BDATE"));
                jObject.put("SEX", crs.getString("CJ_SEX"));
                jObject.put("FEATURE", crs.getString("CJ_FEATURE"));
                jObject.put("OCCUPATION", (crs.getString("NEWOCCUPATION") != null) ? crs.getString("NEWOCCUPATION") : "");
                jObject.put("OCCUPATION_CODE", crs.getString("CJ_OCCUPATION"));
                jObject.put("TELNO", crs.getString("CJ_TELNO"));
                jObject.put("MOBILE", crs.getString("CJ_MOBILE"));
                jObject.put("WORKSTATUS", crs.getString("CJ_WORKSTATUS"));
                jObject.put("COMMITREASON", (crs.getString("NEWCOMMITREASON") != null) ? crs.getString("NEWCOMMITREASON") : "");
                jObject.put("COMMITREASON_CODE", crs.getString("CJ_COMMITREASON"));
                jObject.put("HASPIC", crs.getString("CJ_HASPIC"));
                jObject.put("SCHOOLCITY", crs.getString("CJ_SCHOOLCITY"));
                jObject.put("SCHOOLLEVEL", crs.getString("CJ_SCHOOLLEVEL"));
                jObject.put("SCHOOLSEQ", crs.getInt("CJ_SCHOOLSEQ"));
                jObject.put("OTHERSCHOOL", crs.getString("CJ_OTHERSCHOOL"));
                jObject.put("SCHOOLNAME", (crs.getString("NEWSCHOOLNAME") != null) ? crs.getString("NEWSCHOOLNAME") : "");
                jObject.put("DRUGSOURCE", (crs.getString("NEWDRUGSOURCE") != null) ? crs.getString("NEWDRUGSOURCE") : "");
                jObject.put("DRUGSOURCE_CODE", crs.getString("CJ_DRUGSOURCE"));
                jObject.put("CLIENTREMARK", crs.getString("CJ_CLIENTREMARK"));
                //住址要改
                jObject.put("HOUSECITY", (crs.getString("NEWHOUSECITY") != null) ? crs.getString("NEWHOUSECITY") : "");
                jObject.put("HOUSETOWN", (crs.getString("NEWHOUSETOWN") != null) ? crs.getString("NEWHOUSETOWN") : "");
                jObject.put("HOUSEADDR", crs.getString("CJ_HOUSEADDR"));
                jObject.put("PERMCITY", (crs.getString("NEWPERMCITY") != null) ? crs.getString("NEWPERMCITY") : "");
                jObject.put("PERMTOWN", (crs.getString("NEWPERMTOWN") != null) ? crs.getString("NEWPERMTOWN") : "");
                jObject.put("PERMADDR", crs.getString("CJ_PERMADDR"));
                jObject.put("CJ_MSNM", crs.getString("CJ_MSNM"));
                jObject.put("CJ_CARNO", crs.getString("CJ_CARNO"));
                jObject.put("CJ_CARTYPE", crs.getString("CJ_CARTYPE"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
        //return arrayList2JsonArray(list);
    }


    /**
     * 毒品案件查詢方法
     *
     * @deprecated
     * @see GET_CASE_ByData_New
     * @param jObj
     * @return
     */
    public JSONArray GET_CASE_ByData(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        boolean needAnd = false;
        sql.append("SELECT A.CJ_FORMID,A.CJ_CLIENTSEQ,A.CJ_STATUS,ISNULL(APP.CJ_CODEDISP,'') AS CJ_STATUS_DISPLAY,B.CJ_CASEID,B.CJ_CASETITLE,A.CJ_CREATORID_NPA,A.CJ_NAME,A.CJ_PID,A.CJ_DOCDATE "
                + "FROM CJDT_CASE_MAIN B "
                + "LEFT JOIN CJDT_FORM_PENALTY A ON A.CJ_CASEID=B.CJ_CASEID "
                + " JOIN CJDT_CASE_CLIENT CC ON A.CJ_CLIENTSEQ=CC.CJ_SEQNO AND CC.CJ_DELETE_FLAG <> 1 "
                //                + "JOIN CJDT_CASE_CLIENT C ON A.CJ_CASEID=C.CJ_CASEID "
                //                + "JOIN CJDT_CASE_DRUG D ON A.CJ_CASEID=D.CJ_CASEID "
                //                + "JOIN CJDT_FORM_PENAL_SUM E ON A.CJ_CASEID=E.CJ_CASEID "
                //                + "JOIN CJDT_FORM_RECEIPT F ON A.CJ_CASEID=F.CJ_CASEID "
                + " LEFT JOIN CJDT_APP_CODE APP ON APP.CJ_CATEGORY = 'PENALTYSTATUS' AND APP.CJ_CODECODE=A.CJ_STATUS "
                //                + "JOIN FORM_RECEIPT G ON A.CASEID=G.CASEID "    怠金收據
                + "WHERE 1=1 AND B.CJ_DELETE_FLAG <> 1 ");
        needAnd = true;
        //CASE_MAIN 條件
        if (jObj.getString("CASEID") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("B.CJ_CASEID=?");
            qsPara.add(jObj.getString("CASEID"));
            needAnd = true;
        }

        if (jObj.getString("PERSONALID") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("B.CJ_PERSONALID=?");
            qsPara.add(jObj.getString("PERSONALID"));
            needAnd = true;
        }
        if (jObj.has("FAC_CASETITLE") && jObj.getString("FAC_CASETITLE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("B.CJ_CASETITLE=? ");
            qsPara.add(jObj.getString("CASETITLE"));
            needAnd = true;
        }
        //嫌犯相關
        if (jObj.getString("CODENAME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("C.CJ_CODENAME=? ");
            qsPara.add(jObj.getString("CODENAME"));
            needAnd = true;
        }
        if (jObj.getString("CLIENTNAME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("C.CJ_NAME=? ");
            qsPara.add(jObj.getString("CLIENTNAME"));
            needAnd = true;
        }
        if (jObj.getString("PID") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("C.CJ_PID=? ");
            qsPara.add(jObj.getString("PID"));
            needAnd = true;
        }
        if (jObj.getString("CLIENTTELNO") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("( C.CJ_TELNO=? OR C.CJ_MOBILE=? )");
            qsPara.add(jObj.getString("CLIENTTELNO"));
            qsPara.add(jObj.getString("CLIENTTELNO"));
            needAnd = true;
        }
        if (jObj.getString("LICENSEPLATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("C.CJ_LICENSEPLATE=? ");
            qsPara.add(jObj.getString("LICENSEPLATE"));
            needAnd = true;
        }
        //毒品相關
        if (jObj.has("DRUGITEMID") && String.valueOf(jObj.getInt("DRUGITEMID")) != null) {//DRUGITEMID?   //CASE_DRUGITEMID?
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("D.CJ_DRUGITEMID=? ");
            qsPara.add(jObj.getInt("DRUGITEMID"));
            needAnd = true;
        }
        //時間條件
        if (jObj.getString("sCREATETIME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("sCREATETIME").substring(0, 3);
            String DataTime_MM = jObj.getString("sCREATETIME").substring(4, 6);
            String DataTime_DD = jObj.getString("sCREATETIME").substring(7, 9);
            sql.append("B.CJ_CREATETIME>=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
            needAnd = true;
        }
        if (jObj.getString("eCREATETIME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("eCREATETIME").substring(0, 3);
            String DataTime_MM = jObj.getString("eCREATETIME").substring(4, 6);
            String DataTime_DD = jObj.getString("eCREATETIME").substring(7, 9);
            sql.append("B.CJ_CREATETIME<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            needAnd = true;
        }
        if (jObj.getString("sDETECTTIME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("sDETECTTIME").substring(0, 3);
            String DataTime_MM = jObj.getString("sDETECTTIME").substring(4, 6);
            String DataTime_DD = jObj.getString("sDETECTTIME").substring(7, 9);
            sql.append("B.CJ_DETECTDATE>=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
            needAnd = true;
        }
        if (jObj.getString("eDETECTTIME") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("eDETECTTIME").substring(0, 3);
            String DataTime_MM = jObj.getString("eDETECTTIME").substring(4, 6);
            String DataTime_DD = jObj.getString("eDETECTTIME").substring(7, 9);
            sql.append("B.CJ_DETECTDATE<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            needAnd = true;
        }
        //PENALTY 條件
        if (jObj.getString("PENALTY_STATUS") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String[] DataAfter = jObj.getString("PENALTY_STATUS").split(",");
            sql.append("(");
            for (int i = 0; i < DataAfter.length; i++) {
                if (i == 0) {
                    sql.append("A.CJ_STATUS =? ");
                    qsPara.add(DataAfter[i]);
                } else {
                    sql.append("OR A.CJ_STATUS =? ");
                    qsPara.add(DataAfter[i]);
                }
            }
            sql.append(")");
            needAnd = true;
        }
        if (jObj.getString("PENALTY_sDOCDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            NumberFormat format3 = NumberFormat.getInstance();
            format3.setMinimumIntegerDigits(3);
            NumberFormat format2 = NumberFormat.getInstance();
            format2.setMinimumIntegerDigits(2);
            String DataTime_YYY = jObj.getString("PENALTY_sDOCDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_sDOCDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_sDOCDATE").substring(7, 9);
            sql.append("A.CJ_DOCDATE>=? ");
            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
            needAnd = true;
        }
        if (jObj.getString("PENALTY_eDOCDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            NumberFormat format3 = NumberFormat.getInstance();
            format3.setMinimumIntegerDigits(3);
            NumberFormat format2 = NumberFormat.getInstance();
            format2.setMinimumIntegerDigits(2);
            String DataTime_YYY = jObj.getString("PENALTY_eDOCDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_eDOCDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_eDOCDATE").substring(7, 9);
            sql.append("A.CJ_DOCDATE<=? ");
            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
            needAnd = true;
        }
        if (jObj.getString("PENALTY_DOCCODE") != null && jObj.getString("PENALTY_DOCCODE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("A.CJ_DOCCODE=? ");
            qsPara.add(jObj.getString("PENALTY_DOCCODE"));
            needAnd = true;
        }
        if (jObj.getString("PENALTY_sPENALTYDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("PENALTY_sPENALTYDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_sPENALTYDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_sPENALTYDATE").substring(7, 9);
            sql.append("B.CJ_PENALTYDATE>=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            needAnd = true;
        }
        if (jObj.getString("PENALTY_ePENALTYDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("PENALTY_ePENALTYDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_ePENALTYDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_ePENALTYDATE").substring(7, 9);
            sql.append("B.CJ_PENALTYDATE<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            needAnd = true;
        }
        if (jObj.getString("PENALTY_CREATORUNITLEVEL1") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("A.CJ_PENALTY_UNIT_CD=? ");
            qsPara.add(jObj.getString("PENALTY_CREATORUNITLEVEL1"));
            needAnd = true;
        }
        //罰鍰條件
        if (jObj.getString("FINE_STATUS") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String[] DataAfter = jObj.getString("FINE_STATUS").split(",");
            sql.append("(");
            for (int i = 0; i < DataAfter.length; i++) {
                if (i == 0) {
                    sql.append("A.CJ_FINESTATUS =? ");
                    qsPara.add(DataAfter[i]);
                } else {
                    sql.append("OR A.CJ_FINESTATUS =? ");
                    qsPara.add(DataAfter[i]);
                }
            }
            sql.append(")");
            needAnd = true;
        }
        if (jObj.getString("FINERECEIPT_sDOCDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            NumberFormat format3 = NumberFormat.getInstance();
            format3.setMinimumIntegerDigits(3);
            NumberFormat format2 = NumberFormat.getInstance();
            format2.setMinimumIntegerDigits(2);
            String DataTime_YYY = jObj.getString("FINERECEIPT_sDOCDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("FINERECEIPT_sDOCDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("FINERECEIPT_sDOCDATE").substring(7, 9);
            sql.append("F.CJ_DOCDATE>=? ");
            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
            needAnd = true;
        }
        if (jObj.getString("FINERECEIPT_sDOCDATE") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            NumberFormat format3 = NumberFormat.getInstance();
            format3.setMinimumIntegerDigits(3);
            NumberFormat format2 = NumberFormat.getInstance();
            format2.setMinimumIntegerDigits(2);
            String DataTime_YYY = jObj.getString("FINERECEIPT_sDOCDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("FINERECEIPT_sDOCDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("FINERECEIPT_sDOCDATE").substring(7, 9);
            sql.append("F.CJ_DOCDATE<=? ");
            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
            needAnd = true;
        }
        //講習條件
        if (jObj.getString("WORKSHOP_STATUS") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String[] DataAfter = jObj.getString("WORKSHOP_STATUS").split(",");
            sql.append("(");
            for (int i = 0; i < DataAfter.length; i++) {
                if (i == 0) {
                    sql.append("A.CJ_WORKSHOPSTATUS =? ");
                    qsPara.add(DataAfter[i]);
                } else {
                    sql.append("OR A.CJ_WORKSHOPSTATUS =? ");
                    qsPara.add(DataAfter[i]);
                }
            }
            sql.append(")");
            needAnd = true;
        }
        //怠金
        if (jObj.getString("SFINE_STATUS") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String[] DataAfter = jObj.getString("SFINE_STATUS").split(",");
            sql.append("(");
            for (int i = 0; i < DataAfter.length; i++) {
                if (i == 0) {
                    sql.append("E.CJ_FINESTATUS =? ");
                    qsPara.add(DataAfter[i]);
                } else {
                    sql.append("OR E.CJ_FINESTATUS =? ");
                    qsPara.add(DataAfter[i]);
                }
            }
            sql.append(")");
            needAnd = true;
        }
        //關鍵字用來查詢案情摘述
        if (jObj.getString("CASE_KEYWORD") != "") {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            //若用特殊字元需要用以下方法
            sql.append("B.CJ_DESCRIPTION LIKE ? ");
            qsPara.add("%" + jObj.getString("CASE_KEYWORD") + "%");
            needAnd = true;
        }

        sql.append(" ORDER BY CJ_CASEID DESC");
        //最後若沒有條件則全選
//        if (needAnd == false) {
//        }
//        CJDBUtil.getInstance().printSql(sql.toString(), qsPara.toArray());

        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("FORMID", crs.getString("CJ_FORMID"));
                jObject.put("CLIENTSEQ", crs.getInt("CJ_CLIENTSEQ"));
                jObject.put("CASEID", crs.getString("CJ_CASEID"));
                jObject.put("CASETITLE", crs.getString("CJ_CASETITLE"));
                jObject.put("TYPE", crs.getString("CJ_STATUS"));
                jObject.put("TYPE_DISPLAY", crs.getString("CJ_STATUS_DISPLAY"));
                jObject.put("NAME", crs.getString("CJ_NAME"));
                jObject.put("PID", crs.getString("CJ_PID"));
                jObject.put("DOCDATE", crs.getString("CJ_DOCDATE"));
                //jObject.put("PENALTYCREATOR", crs.getString("CJ_CREATORID"));CJ_CREATORID_NPA
                jObject.put("PENALTYCREATOR", crs.getString("CJ_CREATORID_NPA"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
        //return arrayList2JsonArray(list);
    }

    public JSONArray GET_CASE_ByData_New(JSONObject jObj) {
        StringBuilder otherCondition = new StringBuilder();
        StringBuilder queryConditionId = new StringBuilder();
        StringBuilder queryConditionName = new StringBuilder();
        StringBuilder queryConditionCarNo = new StringBuilder();
        LinkedHashMap<String, String> logCols = new LinkedHashMap();
        comDao.putCondition(otherCondition, jObj, "query", "查詢");

        StringBuilder sql = new StringBuilder();
        StringBuilder sqlhead1 = new StringBuilder();
        StringBuilder sqljoin = new StringBuilder();
        StringBuilder sqlcondition = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        CachedRowSet crshead = null;
        boolean needAnd = false;

//              sql為最後查詢資料SQL
        sql.append("SELECT  A.CJ_FORMID,A.CJ_CLIENTSEQ,A.CJ_STATUS,ISNULL(APP.CJ_CODEDISP,'') AS CJ_STATUS_DISPLAY,B.CJ_CASEID,B.CJ_CASETITLE,A.CJ_CREATORNM,CC.CJ_NAME,CC.CJ_PID,A.CJ_DOCDATE ");
        sql.append("FROM CJDT_CASE_MAIN B ");
        sql.append(" LEFT JOIN CJDT_CASE_CLIENT CC ON  B.CJ_CASEID = CC.CJ_CASEID AND CC.CJ_DELETE_FLAG IN (0,NULL) ");
        sql.append(" LEFT JOIN CJDT_FORM_PENALTY A ON CC.CJ_SEQNO=A.CJ_CLIENTSEQ ");
//修改成如下
//        sql.append(" LEFT JOIN (SELECT * FROM CJDT_CASE_CLIENT CC LEFT JOIN CJDT_FORM_PENALTY CA ON CC.CJ_SEQNO = CA.CJ_CLIENTSEQ) A");
//        sql.append(" ON A.CJ_CASEID = B.CJ_CASEID ");
//        if (jObj.getString("CODENAME").trim().length() > 0 || jObj.getString("CLIENTNAME").trim().length() > 0 || jObj.getString("PID").trim().length() > 0 || jObj.getString("CLIENTTELNO").trim().length() > 0 || jObj.getString("LICENSEPLATE").trim().length() > 0) {
//            sqljoin.append("AND A.CJ_CASEID=A.CJ_CASEID ");
//        }
        sql.append(" LEFT JOIN CJDT_APP_CODE APP ON APP.CJ_CATEGORY = 'PENALTYSTATUS' AND APP.CJ_CODECODE=A.CJ_STATUS ");
        if (jObj.getString("SFINE_STATUS").trim().length() > 0) {
            sql.append(" LEFT JOIN CJDT_FORM_PENAL_SUM E ON A.CJ_CLIENTSEQ=E.CJ_CLIENTSEQ  ");
        }
        if (jObj.getString("FINERECEIPT_sDOCDATE").trim().length() > 0 || jObj.getString("FINERECEIPT_eDOCDATE").trim().length() > 0) {
            sql.append(" LEFT JOIN CJDT_FORM_RECEIPT F ON A.CJ_CLIENTSEQ=F.CJ_CLIENTSEQ ");
            sql.append(" LEFT JOIN CJDT_FORM_RECEIPT_PARTS FP ON F.CJ_FORMID=FP.CJ_RECEIPTID ");
        }

        if (jObj.getString("SFINERECEIPT_sDOCDATE").trim().length() > 0 || jObj.getString("SFINERECEIPT_eDOCDATE").trim().length() > 0) {
            sql.append(" LEFT JOIN CJDT_FORM_RECEIPT F2 ON A.CJ_CLIENTSEQ=F2.CJ_CLIENTSEQ ");
            sql.append(" LEFT JOIN CJDT_FORM_RECEIPT_PARTS FP2 ON F2.CJ_FORMID=FP2.CJ_RECEIPTID ");
        }
        //sql.append(" WHERE B.CJ_CASEID in ( ");

//              sqlhead1為一開始抓取案件編號的SQL  
        //sqlhead1.append("SELECT B.CJ_CASEID ");
        //sqlhead1.append(" FROM CJDT_CASE_MAIN B ");
        //sqlhead1.append(" LEFT JOIN CJDT_FORM_PENALTY A ON A.CJ_CASEID=B.CJ_CASEID ");
        //sqlhead1.append(" LEFT JOIN CJDT_CASE_CLIENT CC ON A.CJ_CLIENTSEQ=CC.CJ_SEQNO ");
//                sqlhead1.append( " LEFT JOIN CJDT_APP_CODE APP ON APP.CJ_CATEGORY = 'PENALTYSTATUS' AND APP.CJ_CODECODE=A.CJ_STATUS " );
//              sqljoin為共用的JOIN
        //if (jObj.getString("CODENAME") != "" || jObj.getString("CLIENTNAME") != "" || jObj.getString("PID") != "" || jObj.getString("CLIENTTELNO") != "" || jObj.getString("LICENSEPLATE") != "") {
        //sqljoin.append(" LEFT JOIN CJDT_CASE_CLIENT C ON A.CJ_CASEID=C.CJ_CASEID ");
        //}
        if (jObj.has("DRUGITEMID") && jObj.getString("DRUGITEMID").trim().length() > 0) {
            sqljoin.append(" LEFT JOIN CJDT_CASE_DRUG D ON A.CJ_CASEID=D.CJ_CASEID ");
            sqljoin.append(" LEFT JOIN CJDT_CASE_DRUG_INGREDIENT DI ON D.CJ_SEQNO=DI.CJ_CASEDRUG_SN ");
        }
//        if (jObj.getString("SFINE_STATUS") != "" || jObj.getString("SFINERECEIPT_sDOCDATE") != "") {
//            sqljoin.append("LEFT JOIN CJDT_FORM_PENAL_SUM E ON A.CJ_CASEID=E.CJ_CASEID ");
//        }
//        if (jObj.getString("FINERECEIPT_sDOCDATE") != "") {
//            sqljoin.append("LEFT JOIN CJDT_FORM_RECEIPT F ON A.CJ_CASEID=F.CJ_CASEID ");
//        }
//                if(jObj.getString("DRUGITEMID") != "" )
//                    sql.append("LEFT JOIN FORM_RECEIPT G ON A.CASEID=G.CASEID " );   //怠金收據   

//              sqlcondition為條件SQL語句  
        sqlcondition.append("WHERE 1=1 AND B.CJ_DELETE_FLAG <> 1 ");
        needAnd = true;
        //CASE_MAIN 條件
        if (jObj.getString("CASEID").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_CASEID=?");
            qsPara.add(jObj.getString("CASEID"));
            comDao.putCondition(otherCondition, jObj, "CASEID", "案件編號");
            needAnd = true;
        }

        if (jObj.getString("PERSONALID").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_PERSONALID=?");
            qsPara.add(jObj.getString("PERSONALID"));
            comDao.putCondition(otherCondition, jObj, "PERSONALID", "自訂案號");
            needAnd = true;
        }
        if (jObj.has("FAC_CASETITLE") && jObj.getString("FAC_CASETITLE").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_CASETITLE=? ");
            qsPara.add(jObj.getString("CASETITLE"));
            comDao.putCondition(otherCondition, jObj, "CASETITLE", "案件名稱");
            needAnd = true;
        }
        //建立單位
        if (jObj.has("CREATORUNITLEVEL3") && jObj.getString("CREATORUNITLEVEL3").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_CREATORUNITCODE=? ");
            qsPara.add(jObj.getString("CREATORUNITLEVEL3"));
            jObj.put("CREATORUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL1")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL2")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL3")));
            comDao.putCondition(otherCondition, jObj, "CREATORUNIT_DISP", "建立單位名稱");
            needAnd = true;
        } else if (jObj.has("CREATORUNITLEVEL2") && jObj.getString("CREATORUNITLEVEL2").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_CREATORUNITCODE LIKE ? ");
            qsPara.add(jObj.getString("CREATORUNITLEVEL2").substring(0, 3) + "%");
            jObj.put("CREATORUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL1")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL2")));
            comDao.putCondition(otherCondition, jObj, "CREATORUNIT_DISP", "建立單位名稱");
            needAnd = true;
        } else if (jObj.has("CREATORUNITLEVEL1") && jObj.getString("CREATORUNITLEVEL1").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_CREATORUNITCODE LIKE ? ");
            qsPara.add(jObj.getString("CREATORUNITLEVEL1").substring(0, 2) + "%");
            jObj.put("CREATORUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("CREATORUNITLEVEL1")));
            comDao.putCondition(otherCondition, jObj, "CREATORUNIT_DISP", "建立單位名稱");
            needAnd = true;
        }
        //查獲單位
        if (jObj.has("DETECTUNITLEVEL3") && jObj.getString("DETECTUNITLEVEL3").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTUNIT=? ");
            qsPara.add(jObj.getString("DETECTUNITLEVEL3"));
            jObj.put("DETECTUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL1")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL2")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL3")));
            comDao.putCondition(otherCondition, jObj, "DETECTUNIT_DISP", "查獲單位名稱");
            needAnd = true;
        } else if (jObj.has("DETECTUNITLEVEL2") && jObj.getString("DETECTUNITLEVEL2").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTUNITLEVEL2=? ");
            qsPara.add(jObj.getString("DETECTUNITLEVEL2"));
            jObj.put("DETECTUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL1")) + SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL2")));
            comDao.putCondition(otherCondition, jObj, "DETECTUNIT_DISP", "查獲單位名稱");
            needAnd = true;
        } else if (jObj.has("DETECTUNITLEVEL1") && jObj.getString("DETECTUNITLEVEL1").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTUNITLEVEL1=? ");
            qsPara.add(jObj.getString("DETECTUNITLEVEL1"));
            jObj.put("DETECTUNIT_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("DETECTUNITLEVEL1")));
            comDao.putCondition(otherCondition, jObj, "DETECTUNIT_DISP", "查獲單位名稱");
            needAnd = true;
        }

        //查獲地點
        if (jObj.has("DETECTPLACE") && jObj.getString("DETECTPLACE").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTPLACE like ? ");
            qsPara.add(jObj.getString("DETECTCITY") + jObj.getString("DETECTTOWN") + jObj.getString("DETECTPLACE") + "%");
            comDao.putConditionSplit(otherCondition, jObj, "DETECTCITY/DETECTTOWN/DETECTPLACE", "查獲地點");
            needAnd = true;
        } else if (jObj.has("DETECTTOWN_CODE") && jObj.getString("DETECTTOWN_CODE").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTPLACE like ? ");
            qsPara.add(jObj.getString("DETECTCITY") + jObj.getString("DETECTTOWN") + "%");
            comDao.putConditionSplit(otherCondition, jObj, "DETECTCITY/DETECTTOWN", "查獲地點");
            needAnd = true;
        } else if (jObj.has("DETECTCITY_CODE") && jObj.getString("DETECTCITY_CODE").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("B.CJ_DETECTPLACE like ? ");
            qsPara.add(jObj.getString("DETECTCITY") + "%");
            comDao.putCondition(otherCondition, jObj, "DETECTCITY", "查獲地點");
            needAnd = true;
        }

//        //嫌犯相關
//        if (jObj.getString("CODENAME") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("C.CJ_CODENAME=? ");
//            qsPara.add(jObj.getString("CODENAME"));
//            needAnd = true;
//        }
//        if (jObj.getString("CLIENTNAME") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("C.CJ_NAME=? ");
//            qsPara.add(jObj.getString("CLIENTNAME"));
//            needAnd = true;
//        }
//        if (jObj.getString("PID") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("C.CJ_PID=? ");
//            qsPara.add(jObj.getString("PID"));
//            needAnd = true;
//        }
//        if (jObj.getString("CLIENTTELNO") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("( C.CJ_TELNO=? OR C.CJ_MOBILE=? )");
//            qsPara.add(jObj.getString("CLIENTTELNO"));
//            qsPara.add(jObj.getString("CLIENTTELNO"));
//            needAnd = true;
//        }
//        //車牌號碼 CJDT_CASE_CLIENT
//        if (jObj.getString("LICENSEPLATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("C.CJ_CARNO=? ");
//            qsPara.add(jObj.getString("LICENSEPLATE"));
//            needAnd = true;
//        }
        //毒品相關 查鑑驗成分
        if (jObj.getString("DRUGITEMID").trim().length() > 0) {//DRUGITEMID?   //CASE_DRUGITEMID?
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            sqlcondition.append("DI.CJ_DRUGITEMID3=? ");
            qsPara.add(Integer.parseInt(jObj.getString("DRUGITEMID")));
            jObj.put("DRUGITEMID_DISP", SharedInfoDao.getDrugItemCodeDisp(jObj.getString("DRUGITEMID")));
            comDao.putCondition(otherCondition, jObj, "DRUGITEMID_DISP", "鑑驗成分");
            needAnd = true;
        }
        //時間條件
        if (jObj.getString("sCREATETIME").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("sCREATETIME").substring(0, 3);
            String DataTime_MM = jObj.getString("sCREATETIME").substring(4, 6);
            String DataTime_DD = jObj.getString("sCREATETIME").substring(7, 9);
            sqlcondition.append("B.CJ_CREATETIME>=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
            comDao.putCondition(otherCondition, jObj, "sCREATETIME", "建立時間(起始)");
            needAnd = true;
        }
        if (jObj.getString("eCREATETIME").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("eCREATETIME").substring(0, 3);
            String DataTime_MM = jObj.getString("eCREATETIME").substring(4, 6);
            String DataTime_DD = jObj.getString("eCREATETIME").substring(7, 9);
            sqlcondition.append("B.CJ_CREATETIME<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            comDao.putCondition(otherCondition, jObj, "eCREATETIME", "建立時間(結束)");
            needAnd = true;
        }
        if (jObj.getString("sDETECTTIME").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("sDETECTTIME").substring(0, 3);
            String DataTime_MM = jObj.getString("sDETECTTIME").substring(4, 6);
            String DataTime_DD = jObj.getString("sDETECTTIME").substring(7, 9);
            sqlcondition.append("B.CJ_DETECTDATE>=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
            comDao.putCondition(otherCondition, jObj, "sDETECTTIME", "查獲時間(起始)");
            needAnd = true;
        }
        if (jObj.getString("eDETECTTIME").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("eDETECTTIME").substring(0, 3);
            String DataTime_MM = jObj.getString("eDETECTTIME").substring(4, 6);
            String DataTime_DD = jObj.getString("eDETECTTIME").substring(7, 9);
            sqlcondition.append("B.CJ_DETECTDATE<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            comDao.putCondition(otherCondition, jObj, "eDETECTTIME", "查獲時間(結束)");
            needAnd = true;
        }
//        //PENALTY 條件 
//        //處分書狀態
//        if (jObj.getString("PENALTY_STATUS") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String[] DataAfter = jObj.getString("PENALTY_STATUS").split(",");
//            sqlcondition.append("(");
//            for (int i = 0; i < DataAfter.length; i++) {
//                if (i == 0) {
//                    sqlcondition.append("A.CJ_STATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                } else {
//                    sqlcondition.append("OR A.CJ_STATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                }
//            }
//            sqlcondition.append(")");
//            needAnd = true;
//        }
//        //發文日期
//        if (jObj.getString("PENALTY_sDOCDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            NumberFormat format3 = NumberFormat.getInstance();
//            format3.setMinimumIntegerDigits(3);
//            NumberFormat format2 = NumberFormat.getInstance();
//            format2.setMinimumIntegerDigits(2);
//            String DataTime_YYY = jObj.getString("PENALTY_sDOCDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("PENALTY_sDOCDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("PENALTY_sDOCDATE").substring(7, 9);
//            sqlcondition.append("A.CJ_DOCDATE>=? ");
//            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
//            needAnd = true;
//        }
//        if (jObj.getString("PENALTY_eDOCDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            NumberFormat format3 = NumberFormat.getInstance();
//            format3.setMinimumIntegerDigits(3);
//            NumberFormat format2 = NumberFormat.getInstance();
//            format2.setMinimumIntegerDigits(2);
//            String DataTime_YYY = jObj.getString("PENALTY_eDOCDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("PENALTY_eDOCDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("PENALTY_eDOCDATE").substring(7, 9);
//            sqlcondition.append("A.CJ_DOCDATE<=? ");
//            qsPara.add(format3.format(Long.valueOf(DataTime_YYY)) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)));
//            needAnd = true;
//        }
//        //發文字號
//        if (jObj.getString("PENALTY_DOCCODE") != null && jObj.getString("PENALTY_DOCCODE").trim().length() > 0) {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("A.CJ_DOCCODE=? ");
//            qsPara.add(jObj.getString("PENALTY_DOCCODE"));
//            needAnd = true;
//        }
//        //CJDT_FORM_PENALTY 裁罰日期 A
//        if (jObj.getString("PENALTY_sPENALTYDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String DataTime_YYY = jObj.getString("PENALTY_sPENALTYDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("PENALTY_sPENALTYDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("PENALTY_sPENALTYDATE").substring(7, 9);
//            sqlcondition.append("A.CJ_PENALTYDATE>=? ");
//            qsPara.add(DataTime_YYY+DataTime_MM+DataTime_DD);
////            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
//            needAnd = true;
//        }
//        //CJDT_FORM_PENALTY 裁罰日期 A
//        if (jObj.getString("PENALTY_ePENALTYDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String DataTime_YYY = jObj.getString("PENALTY_ePENALTYDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("PENALTY_ePENALTYDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("PENALTY_ePENALTYDATE").substring(7, 9);
//            sqlcondition.append("A.CJ_PENALTYDATE<=? ");
//            qsPara.add(DataTime_YYY+DataTime_MM+DataTime_DD);
////            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
//            needAnd = true;
//        }
//        //裁罰單位
//        if (jObj.getString("PENALTY_CREATORUNITLEVEL1") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            sqlcondition.append("A.CJ_PENALTY_UNIT_CD=? ");
//            qsPara.add(jObj.getString("PENALTY_CREATORUNITLEVEL1"));
//            needAnd = true;
//        }
//        //罰鍰條件
//        if (jObj.getString("FINE_STATUS") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String[] DataAfter = jObj.getString("FINE_STATUS").split(",");
//            sqlcondition.append("(");
//            for (int i = 0; i < DataAfter.length; i++) {
//                if (i == 0) {
//                    sqlcondition.append("A.CJ_FINESTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                } else {
//                    sqlcondition.append("OR A.CJ_FINESTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                }
//            }
//            sqlcondition.append(")");
//            needAnd = true;
//        }
//        if (jObj.getString("FINERECEIPT_sDOCDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            NumberFormat format3 = NumberFormat.getInstance();
//            format3.setMinimumIntegerDigits(3);
//            NumberFormat format2 = NumberFormat.getInstance();
//            format2.setMinimumIntegerDigits(2);
//            String DataTime_YYY = jObj.getString("FINERECEIPT_sDOCDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("FINERECEIPT_sDOCDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("FINERECEIPT_sDOCDATE").substring(7, 9);
//            sqlcondition.append("F.CJ_DOCDATE>=? ");
//            qsPara.add(format3.format(Long.valueOf(DataTime_YYY) + 1911) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)) + " 00:00:00");
//            needAnd = true;
//        }
//        if (jObj.getString("FINERECEIPT_sDOCDATE") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            NumberFormat format3 = NumberFormat.getInstance();
//            format3.setMinimumIntegerDigits(3);
//            NumberFormat format2 = NumberFormat.getInstance();
//            format2.setMinimumIntegerDigits(2);
//            String DataTime_YYY = jObj.getString("FINERECEIPT_sDOCDATE").substring(0, 3);
//            String DataTime_MM = jObj.getString("FINERECEIPT_sDOCDATE").substring(4, 6);
//            String DataTime_DD = jObj.getString("FINERECEIPT_sDOCDATE").substring(7, 9);
//            sqlcondition.append("F.CJ_DOCDATE<=? ");
//            qsPara.add(format3.format(Long.valueOf(DataTime_YYY) + 1911) + format2.format(Long.valueOf(DataTime_MM)) + format2.format(Long.valueOf(DataTime_DD)) + " 23:59:59");
//            needAnd = true;
//        }
//        //講習條件
//        if (jObj.getString("WORKSHOP_STATUS") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String[] DataAfter = jObj.getString("WORKSHOP_STATUS").split(",");
//            sqlcondition.append("(");
//            for (int i = 0; i < DataAfter.length; i++) {
//                if (i == 0) {
//                    sqlcondition.append("A.CJ_WORKSHOPSTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                } else {
//                    sqlcondition.append("OR A.CJ_WORKSHOPSTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                }
//            }
//            sqlcondition.append(")");
//            needAnd = true;
//        }
//        //怠金狀態
//        if (jObj.getString("SFINE_STATUS") != "") {
//            if (needAnd == true) {
//                sqlcondition.append(" AND ");
//            }
//            String[] DataAfter = jObj.getString("SFINE_STATUS").split(",");
//            sqlcondition.append("(");
//            for (int i = 0; i < DataAfter.length; i++) {
//                if (i == 0) {
//                    sqlcondition.append("E.CJ_FINESTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                } else {
//                    sqlcondition.append("OR E.CJ_FINESTATUS =? ");
//                    qsPara.add(DataAfter[i]);
//                }
//            }
//            sqlcondition.append(")");
//            needAnd = true;
//        }

        //關鍵字用來查詢案情摘述
        if (jObj.getString("CASE_KEYWORD").trim().length() > 0) {
            if (needAnd == true) {
                sqlcondition.append(" AND ");
            }
            //若用特殊字元需要用以下方法
            sqlcondition.append("B.CJ_DESCRIPTION LIKE ? ");
            qsPara.add("%" + jObj.getString("CASE_KEYWORD") + "%");
            comDao.putCondition(otherCondition, jObj, "CASE_KEYWORD", "關鍵字");
            needAnd = true;
        }
        sqlhead1.append(sqljoin.toString());
        sqlhead1.append(sqlcondition.toString());
        sql.append(sqlhead1.toString());

        //sql.append(" )");
        //條件與人有關的要另外查
        //嫌犯相關 CASE_CLIENT CC
        if (jObj.getString("CODENAME").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("CC.CJ_CODENAME=? ");
            qsPara.add(jObj.getString("CODENAME"));
            comDao.putCondition(otherCondition, jObj, "CODENAME", "受處分人編號");
            needAnd = true;
        }
        if (jObj.getString("CLIENTNAME").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("UPPER(CC.CJ_NAME)=? ");
            qsPara.add(jObj.getString("CLIENTNAME"));
            comDao.putCondition(queryConditionName, jObj, "CLIENTNAME", "");
            needAnd = true;
        }
        if (jObj.getString("PID").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("CC.CJ_PID=? ");
            qsPara.add(jObj.getString("PID"));
            comDao.putCondition(queryConditionId, jObj, "PID", "");
            needAnd = true;
        }
        if (jObj.getString("CLIENTTELNO").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("( CC.CJ_TELNO=? OR CC.CJ_MOBILE=? )");
            qsPara.add(jObj.getString("CLIENTTELNO"));
            qsPara.add(jObj.getString("CLIENTTELNO"));
            comDao.putCondition(otherCondition, jObj, "CLIENTTELNO", "受處分人聯絡電話");
            needAnd = true;
        }
        //車牌號碼 CJDT_CASE_CLIENT
        if (jObj.getString("LICENSEPLATE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("CC.CJ_CARNO=? ");
            qsPara.add(jObj.getString("LICENSEPLATE"));
            comDao.putCondition(queryConditionCarNo, jObj, "LICENSEPLATE", "");
            needAnd = true;
        }
        //PENALTY 條件 
        //發文字
        if (jObj.getString("PENALTY_DOCCODE") != null && jObj.getString("PENALTY_DOCCODE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("A.CJ_DOCCODE=? ");
            qsPara.add(jObj.getString("PENALTY_DOCCODE"));
            comDao.putCondition(otherCondition, jObj, "PENALTY_DOCCODE", "處分書發文字");
            needAnd = true;
        }
        //發文號
        if (jObj.getString("PENALTY_DOCNO") != null && jObj.getString("PENALTY_DOCNO").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("A.CJ_DOCNO=? ");
            qsPara.add(jObj.getString("PENALTY_DOCNO"));
            comDao.putCondition(otherCondition, jObj, "PENALTY_DOCNO", "處分書發文號");
            needAnd = true;
        }
        //CJDT_FORM_PENALTY 裁罰日期 A
        if (jObj.getString("PENALTY_sPENALTYDATE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("PENALTY_sPENALTYDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_sPENALTYDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_sPENALTYDATE").substring(7, 9);
            sql.append("ISNULL(A.CJ_PENALTYDATE,'')>=? ");
            qsPara.add(DataTime_YYY + DataTime_MM + DataTime_DD);
            comDao.putCondition(otherCondition, jObj, "PENALTY_sPENALTYDATE", "裁罰日期(起始)");
//            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 00:00:00");
            needAnd = true;
        }
        //CJDT_FORM_PENALTY 裁罰日期 A
        if (jObj.getString("PENALTY_ePENALTYDATE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("PENALTY_ePENALTYDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("PENALTY_ePENALTYDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("PENALTY_ePENALTYDATE").substring(7, 9);
            sql.append("ISNULL(A.CJ_PENALTYDATE,'')<=? ");
            qsPara.add(DataTime_YYY + DataTime_MM + DataTime_DD);
            comDao.putCondition(otherCondition, jObj, "PENALTY_ePENALTYDATE", "裁罰日期(結束)");
//            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            needAnd = true;
        }
        //裁罰單位
        if (jObj.getString("PENALTY_CREATORUNITLEVEL1").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            sql.append("A.CJ_PENALTY_UNIT_CD=? ");
            qsPara.add(jObj.getString("PENALTY_CREATORUNITLEVEL1"));
            jObj.put("PENALTY_CREATORUNITLEVEL1_DISP", SharedInfoDao.getUnitCodeDisp(jObj.getString("PENALTY_CREATORUNITLEVEL1")));
            comDao.putCondition(otherCondition, jObj, "PENALTY_CREATORUNITLEVEL1_DISP", "裁罰單位");
            needAnd = true;
        }
        if (jObj.getString("SFINERECEIPT_eDOCDATE").trim().length() > 0) {
            if (needAnd == true) {
                sql.append(" AND ");
            }
            String DataTime_YYY = jObj.getString("SFINERECEIPT_eDOCDATE").substring(0, 3);
            String DataTime_MM = jObj.getString("SFINERECEIPT_eDOCDATE").substring(4, 6);
            String DataTime_DD = jObj.getString("SFINERECEIPT_eDOCDATE").substring(7, 9);
            sql.append("F2.CJ_DOCDATE<=? ");
            qsPara.add((Integer.parseInt(DataTime_YYY) + 1911) + "-" + DataTime_MM + "-" + DataTime_DD + " 23:59:59");
            comDao.putCondition(otherCondition, jObj, "SFINERECEIPT_eDOCDATE", "怠金收據填發日期(結束)");
            needAnd = true;
        }

        sql.append(" ORDER BY B.CJ_CASEID DESC,A.CJ_LASTUPDATETIME DESC");
        System.out.println(sql);
        ArrayList<HashMap> list = new ArrayList();

        //CJDBUtil.getInstance().printSql(sqlhead1.toString(), qsPara.toArray());//
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("FORMID", crs.getString("CJ_FORMID"));
                jObject.put("CLIENTSEQ", crs.getInt("CJ_CLIENTSEQ"));
                jObject.put("CASEID", crs.getString("CJ_CASEID"));
                jObject.put("CASETITLE", crs.getString("CJ_CASETITLE"));
                jObject.put("TYPE", crs.getString("CJ_STATUS"));
                jObject.put("TYPE_DISPLAY", crs.getString("CJ_STATUS_DISPLAY"));
                jObject.put("NAME", crs.getString("CJ_NAME"));
                jObject.put("PID", crs.getString("CJ_PID"));
                jObject.put("DOCDATE", crs.getString("CJ_DOCDATE"));
                //jObject.put("PENALTYCREATOR", crs.getString("CJ_CREATORID"));CJ_CREATORID_NPA
                jObject.put("PENALTYCREATOR", crs.getString("CJ_CREATORNM"));
                resultDataArray.put(jObject);
                transCachedRowSet2ArrayList(list, crs);
            }
            User user = (User) jObj.get("userVO");
            logCols.put("CJ_CASEID", "案件編號");
            logCols.put("CJ_CASETITLE", "案件名稱");
            logCols.put("CJ_NAME", "姓名");
            logCols.put("CJ_PID", "證號");
            logCols.put("CJ_DOCDATE", "處分日期");
            logCols.put("CJ_PENALTYCREATOR", "裁罰人");

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
        //return arrayList2JsonArray(list);
    }

    public boolean updateClientPhotos(JSONObject jObj, Connection conn) {
        boolean success = true;
        User user = (User) jObj.get("userVO");
        jObj = SqlUtil.getStaticColumn(jObj, "MB_UPD");
        try {
            int CJ_SEQNO = jObj.getInt("CJ_SEQNO");
            //主檔 MBDT_AMMOMAIN
            ArrayList<String> updateColName = new ArrayList<String>();
            updateColName.add("CJ_HASPIC");
            ArrayList<String> conditionColName = new ArrayList<String>();
            PreparedStatement pstmt = null;
            String sql = "";
            int index = 1;
            conditionColName.add("CJ_SEQNO");
            sql = SqlUtil.getUpdateSQL("CJDT_CASE_CLIENT", updateColName, conditionColName);
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            index = 1;

//            pstmt.setString(index++, ((jObj.has("CJ_UPD_FILENAME")) ? jObj.getJSONArray("CJ_UPD_FILENAME").get(0).toString() : ""));
//            pstmt.setInt(index++, ((jObj.has("CJ_UPD_SIZE")) ? Integer.parseInt(jObj.getJSONArray("CJ_UPD_SIZE").get(0).toString()) : 0));
            if ((jObj.has("CJ_CLIENTPIC_I") && (byte[]) jObj.get("CJ_CLIENTPIC_I_FILE") != null)
                    || (jObj.has("CJ_CLIENTPIC_U") && (byte[]) jObj.get("CJ_CLIENTPIC_U_FILE") != null)) {
                pstmt.setString(index++, "Y");
                //pstmt.setString(index++, "DrugItemPic/" + CJ_MAINTYPE + "/");
            } else {
                pstmt.setString(index++, "N");
                // pstmt.setString(index++, "");
            }
            pstmt.setInt(index++, CJ_SEQNO);
            pstmt.executeUpdate();

            pstmt.close();
            if (jObj.has("CJ_CLIENTPIC_I") && (byte[]) jObj.get("CJ_CLIENTPIC_I_FILE") != null) {
                FTPOperation ftpO = new FTPOperation(FTPOperation.SVR_TYPE.FILESVR);
                ftpO.insertFileToFTP("CaseClientPic/" + (CJ_SEQNO / 1000) + "/", String.valueOf(CJ_SEQNO), (byte[]) jObj.get("CJ_CLIENTPIC_I_FILE"));
                //ftpO.insertFileToFTP("DrugItemPic/MAINTYPE/", String.valueOf(CJ_SEQNO), (byte[]) jObj.get("CLIENT_PIC_clientInsert_FILE"));
            } else if (jObj.has("CJ_CLIENTPIC_U") && (byte[]) jObj.get("CJ_CLIENTPIC_U_FILE") != null) {
                FTPOperation ftpO = new FTPOperation(FTPOperation.SVR_TYPE.FILESVR);
                ftpO.insertFileToFTP("CaseClientPic/" + (CJ_SEQNO / 1000) + "/", String.valueOf(CJ_SEQNO), (byte[]) jObj.get("CJ_CLIENTPIC_U_FILE"));
                //ftpO.insertFileToFTP("DrugItemPic/MAINTYPE/", String.valueOf(CJ_SEQNO), (byte[]) jObj.get("CLIENT_PIC_clientInsert_FILE"));
            }
//應該從外面關            conn.commit();
            //}
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    
    /**
     *
     * @param CJ_CLIENTPIC (file path)
     * @return
     * @throws Exception
     */
    public byte[] getClientImg(String CJ_CLIENTPIC) throws Exception {
        FTPOperation ftpO = new FTPOperation(FTPOperation.SVR_TYPE.FILESVR);
        InputStream is = ftpO.downloadFromInputStream(CJ_CLIENTPIC);

        DBUtil dbUtil = DBUtil.getInstance();
        Connection conn = null;
        //byte[] image = null;
        byte[] image = IOUtils.toByteArray(is);
//		try{
//			conn = dbUtil.getConnection();
//			StringBuilder sbSql = new StringBuilder();
//			sbSql.append("select ");
//			sbSql.append(" CJ_SEAL_IMAGE");
//			sbSql.append(" from CJDT_UNIT_SEAL");
//			sbSql.append(" where CJ_UNIT_CD = ? and CJ_TYPE = ? ");
//			PreparedStatement pstmt = conn.prepareStatement(sbSql.toString());
//			pstmt.setString(1, UNIT_CD);
//                        pstmt.setString(2, TYPE);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()){
//				image = rs.getBytes("CJ_SEAL_IMAGE");
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//			throw new Exception(e);
//		}finally{
//			try{
//                            if(null!=conn){
//				conn.close();
//                            }
//			}catch(SQLException sqle){
//				sqle.printStackTrace();
//			}
//		}
        return image;
    }

    public boolean InsertCaseAttach(JSONObject jObj, Connection conn) {
        boolean success = true;
        User user = (User) jObj.get("userVO");
        LinkedHashMap<String, String> logCols = new LinkedHashMap();
        jObj = SqlUtil.getStaticColumn(jObj, "MB_UPD");
        try {
            //
            ArrayList<String> updateColName = new ArrayList<String>();
            //boolean MainNeedUpdate = false;
            updateColName.add("CJ_CASEID");
            updateColName.add("CJ_TYPE");
            updateColName.add("CJ_OTHERTYPE");
            updateColName.add("CJ_BOUNDTYPE");
            updateColName.add("CJ_BOUNDID");
            updateColName.add("CJ_TITLE");
            updateColName.add("CJ_FILENAME");
            updateColName.add("CJ_FILEDESC");
            updateColName.add("CJ_CREATETIME");
            updateColName.add("CJ_CREATORUNITCODE");
            updateColName.add("CJ_CREATORCITYCODE");
            updateColName.add("CJ_CREATORID_NPA");
            updateColName.add("CJ_CREATORNM");
            if (jObj.has("ClientBook") && !"0".equals(jObj.getString("ClientBook"))) {
                updateColName.add("CJ_CLIENTSN");
            }
            ArrayList<String> conditionColName = new ArrayList<String>();
            PreparedStatement pstmt = null;
            String sql = "";
            int index = 1;

            sql = SqlUtil.getInsertSQL("CJDT_CASE_ATTACHMENT", updateColName);
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            index = 1;
            pstmt.setString(index++, jObj.getString("CJ_CASEID"));
            pstmt.setString(index++, jObj.getString("CJ_TYPE"));

            if (jObj.has("CJ_OTHERTYPE") && !"".equals(jObj.getString("CJ_OTHERTYPE"))) {
                pstmt.setString(index++, new String(jObj.getString("CJ_OTHERTYPE").getBytes("ISO-8859-1"), "UTF-8"));
            } else {
                pstmt.setString(index++, "");
            }
            pstmt.setString(index++, "CA");
            pstmt.setInt(index++, 0);
            pstmt.setString(index++, new String(jObj.getString("CJ_TITLE").getBytes("ISO-8859-1"), "UTF-8"));
            pstmt.setString(index++, ((jObj.has("CJ_UPD_FILENAME")) ? jObj.getJSONArray("CJ_UPD_FILENAME").get(0).toString() : ""));
            pstmt.setString(index++, new String(jObj.getString("CJ_FILEDESC").getBytes("ISO-8859-1"), "UTF-8"));
            pstmt.setString(index++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            pstmt.setString(index++, user.getUnitCd());
            pstmt.setString(index++, user.getUserCity());
            pstmt.setString(index++, user.getUserId());
            pstmt.setString(index++, user.getUserName());
            if (jObj.has("ClientBook") && !"0".equals(jObj.getString("ClientBook"))) {
                pstmt.setInt(index++, jObj.getInt("ClientBook"));
            }
            int rsCnt = pstmt.executeUpdate();

            if (jObj.has("FILECONTENT_attachmentInsert") && (byte[]) jObj.get("FILECONTENT_attachmentInsert_FILE") != null) {
                ResultSet rsKey = pstmt.getGeneratedKeys();
                rsKey.next();
                int key = rsKey.getInt(1);
                rsKey.close();

                FTPOperation ftpO = new FTPOperation(FTPOperation.SVR_TYPE.FILESVR);
                ftpO.insertFileToFTP("CaseAttachFile/" + new SimpleDateFormat("yyyyMM").format(new Date()), String.valueOf(key), (byte[]) jObj.get("FILECONTENT_attachmentInsert_FILE"));
            }
            pstmt.close();
            conn.commit();

            //NPALog
            //jObj.put("CJ_TYPE_DISP", SharedInfoDao.getAppCodeDisp(APPCODE_CATEGORY.ATTACHTYPE, jObj.getString("CJ_TYPE")));
            jObj.put("CJ_TITLE_DISP", new String(jObj.getString("CJ_TITLE").getBytes("ISO-8859-1"), "UTF-8"));
            if (!jObj.has("CJ_UPD_FILENAME")) {
                jObj.put("CJ_UPD_FILENAME", "");
            }
            jObj.put("CJ_FILEDESC", new String(jObj.getString("CJ_FILEDESC").getBytes("ISO-8859-1"), "UTF-8"));
            logCols.put("CJ_CASEID", "案件編號");
            logCols.put("CJ_CASETITLE", "案件名稱");
            logCols.put("CJ_TYPE_DISP", "檔案類別");
            if (jObj.has("CJ_OTHERTYPE") && !"".equals(jObj.getString("CJ_OTHERTYPE"))) {
                jObj.put("CJ_OTHERTYPE", new String(jObj.getString("CJ_OTHERTYPE").getBytes("ISO-8859-1"), "UTF-8"));
                logCols.put("CJ_OTHERTYPE", "其他類別");
            }
            logCols.put("CJ_TITLE_DISP", "檔案標題");
            logCols.put("CJ_FILEDESC", "檔案描述");
            if (jObj.has("FILECONTENT_attachmentInsert") && (byte[]) jObj.get("FILECONTENT_attachmentInsert_FILE") != null) {
                jObj.put("CJ_UPD_FILENAME", jObj.getJSONArray("CJ_UPD_FILENAME").get(0).toString());
                logCols.put("CJ_UPD_FILENAME", "檔案名稱");
            }
            if (jObj.has("ClientBook") && !"0".equals(jObj.getString("ClientBook"))) {
                jObj.put("ClientBook_DISP", SharedInfoDao.getClientCodeDisp(jObj.getString("ClientBook")));
                logCols.put("ClientBook_DISP", "受處分人");
            }

            //NPALog.logInsert(user, jObj.getString("myurl"), ModifyResult.SUCCESS, "", "", "", "", "", "", logCols, jObj);

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    //刪除附檔上傳

    public int DeleteAttacj_BySEQNO(JSONObject jObj) {
        User user = (User) jObj.get("userVO");
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlQ = new StringBuilder();
        int success = 0;
        sql.append("DELETE FROM CJDT_CASE_ATTACHMENT WHERE CJ_SEQNO=?");
        sqlQ.append("select * from CJDT_CASE_ATTACHMENT where CJ_SEQNO=?");
        String[] SEQNOAfter = jObj.getString("SEQNO").split(",");
        for (int i = 0; i < SEQNOAfter.length; i++) {
            ArrayList<Object> qsPara = new ArrayList<Object>();
            qsPara.add(Integer.parseInt(SEQNOAfter[i]));
            try {
                ArrayList<HashMap> mapList = pexecuteQuery(sqlQ.toString(), qsPara.toArray());
                String date = mapList.get(0).get("CJ_CREATETIME").toString().substring(0, 19);
                success += this.pexecuteUpdate(sql.toString(), qsPara.toArray());

                FTPOperation ftpO = new FTPOperation(FTPOperation.SVR_TYPE.FILESVR);
                ftpO.delete("CaseAttachFile/" + date.substring(0, 4) + date.substring(5, 7), SEQNOAfter[i]);
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
        }
        //NPALog
        StringBuilder otherCondition = new StringBuilder();
        StringBuilder queryConditionId = new StringBuilder();
        StringBuilder queryConditionName = new StringBuilder();
        comDao.putCondition(otherCondition, jObj, "CASEID", "案件編號");
        comDao.putCondition(otherCondition, jObj, "CASETITLE", "案件名稱");
        comDao.putCondition(otherCondition, jObj, "SEQNO", "檔案編號");
        comDao.putCondition(otherCondition, jObj, "TITLEstr", "檔案標題");
        //NPALog.logDelete(user, jObj.getString("myurl"), ModifyResult.SUCCESS, otherCondition.toString(), queryConditionId.toString(), queryConditionName.toString(), "", "", "", null, null);
        return success;
    }

    //警局帳號會檢查受處分人是否需裁罰
    public int NeedPenalty(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        int success = 0;
        int hasitem = 0;
        //檢查持有物
        sql.append("SELECT COUNT(*) NUMBER "
                + "FROM "
                + "(SELECT DG.CJ_DRUGITEMID1,'DI1' as SAVETYPE,DG.CJ_SEQNO,DG.CJ_DRUGITEMNM1 as CJ_ITEMNAME,DG.CJ_ITEMQUALITY1 as ITEMQUALITY,DG.CJ_DRUGITEMUNIT1 as CJ_ITEMUNIT,'毒品' as ITEMTYPE,CC.CJ_NAME AS CLIENTNAME,DG.CJ_STOCKIN,' 'as CJ_SUBTYPE,CC.CJ_SEQNO AS CJ_CLIENTNO "
                + "FROM CJDT_CASE_DRUG DG "
                + "LEFT JOIN CJDT_CASE_DRUG_CLIENT CDC ON CDC.CJ_DRUGSEQ=DG.CJ_SEQNO "
                + "LEFT JOIN CJDT_CASE_CLIENT CC ON CDC.CJ_CLIENTSEQ=CC.CJ_SEQNO AND CC.CJ_DELETE_FLAG <> 1"
                + "WHERE DG.CJ_CASEID=? "
                + "UNION "
                + "SELECT DI.CJ_ITEMID AS CJ_DRUGITEMID1,'DI4' as SAVETYPE,DI.CJ_SEQNO,I.CJ_ITEMNAME,DI.CJ_ITEMQUALITY as ITEMQUALITY,I.CJ_ITEMUNIT,'吸食器' as ITEMTYPE,DI.CJ_HOLDER_NM AS CLIENTNAME,DI.CJ_STOCKIN,I.CJ_SUBTYPE ,0 AS CJ_CLIENTNO "
                + "FROM CJDT_CASE_ITEM_DEVICE DI "
                + "LEFT JOIN CJDT_DRUG_ITEM I ON DI.CJ_ITEMID=I.CJ_ITEMID "
                + "WHERE DI.CJ_ITEMTYPE='DI4' AND DI.CJ_CASEID=? "
                + "UNION "
                + "SELECT 0 as CJ_DRUGITEMID1,'DI8' as SAVETYPE,DO.CJ_SEQNO,DO.CJ_ITEMNAME,DO.CJ_ITEMQUALITY as ITEMQUALITY,DO.CJ_ITEMUNIT,'其他' as ITEMTYPE,DO.CJ_HOLDER_NM AS CLIENTNAME,DO.CJ_STOCKIN,' ' as CJ_SUBTYPE ,0 AS CJ_CLIENTNO "
                + "FROM CJDT_CASE_ITEM_OTHERS DO WHERE DO.CJ_CASEID=? ) T "
                + "WHERE T.CJ_CLIENTNO =?");
        qsPara.add(jObj.getString("CASEID"));
        qsPara.add(jObj.getString("CASEID"));
        qsPara.add(jObj.getString("CASEID"));
        qsPara.add(jObj.getInt("SEQNO"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                hasitem += crs.getInt("NUMBER");
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        //檢查鑑驗結果
        sql.setLength(0);//清除查詢串
        qsPara.clear();//清除變數
        int hasdrug = 0;
        sql.append("SELECT COUNT(*) NUMBER "
                + "FROM CJDT_CASE_DRUG_INGREDIENT DI  "
                + "JOIN  CJDT_CASE_DRUG CD ON DI.CJ_CASEDRUG_SN=CD.CJ_SEQNO  "
                + "JOIN CJDT_CASE_CLIENT CC ON CD.CJ_CASEID=CC.CJ_CASEID AND CC.CJ_DELETE_FLAG <> 1"
                + "LEFT JOIN  CJDT_DRUG_ITEM IT ON IT.CJ_ITEMID= DI.CJ_DRUGITEMID3  "
                + "LEFT JOIN (SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='DI1') DI1 ON IT.CJ_SUBTYPE =DI1.CJ_CODECODE "
                + "LEFT JOIN CJDT_DRUG_ITEM as DGIT ON DGIT.CJ_ITEMID=DI.CJ_DRUGITEMID3  WHERE CD.CJ_CASEID = ? AND CC.CJ_SEQNO=? AND DI.CJ_ITEMQUALITY4 > 0  AND IT.CJ_SUBTYPE IN ('03','04')");
        qsPara.add(jObj.getString("CASEID"));
        qsPara.add(jObj.getInt("SEQNO"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                hasdrug += crs.getInt("NUMBER");
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        //檢查尿液結果
        sql.setLength(0);//清除查詢串
        qsPara.clear();//清除變數
        int suckresult = 0;
        sql.append("SELECT COUNT(*) NUMBER "
                + "FROM CJDT_CASE_DRUG_SUCK CDS "
                + "LEFT JOIN  "
                + "CJDT_DRUG_ITEM as DGIT ON DGIT.CJ_ITEMID=CDS.CJ_ITEMID "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='DI1') DI1 ON CDS.CJ_ITEMTYPE =DI1.CJ_CODECODE "
                + " WHERE CJ_CLIENTSEQ=? AND CJ_CASEID=? ");
        qsPara.add(jObj.getInt("SEQNO"));
        qsPara.add(jObj.getString("CASEID"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                suckresult = crs.getInt("NUMBER");
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        //檢查尿液結果(陰性)
        sql.setLength(0);//清除查詢串
        qsPara.clear();//清除變數
        int suckresult_nagtive = 0;
        sql.append("SELECT COUNT(*) NUMBER "
                + "FROM CJDT_CASE_DRUG_SUCK CDS "
                + "LEFT JOIN  "
                + "CJDT_DRUG_ITEM as DGIT ON DGIT.CJ_ITEMID=CDS.CJ_ITEMID "
                + "LEFT JOIN "
                + "(SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY='DI1') DI1 ON CDS.CJ_ITEMTYPE =DI1.CJ_CODECODE "
                + " WHERE CJ_CLIENTSEQ=? AND CJ_CASEID=? AND CJ_TESTSTATUS='N'");
        qsPara.add(jObj.getInt("SEQNO"));
        qsPara.add(jObj.getString("CASEID"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                suckresult_nagtive = crs.getInt("NUMBER");
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        //未滿十四歲不罰
        sql.setLength(0);//清除查詢串
        qsPara.clear();//清除變數
        int age_under14 = 0;
        sql.append("SELECT COUNT(*) NUMBER "
                + "FROM CJDT_CASE_CLIENT  "
                + " WHERE CJ_SEQNO=? AND CJ_CASEID=? AND CJ_AGE<14 AND CJ_DELETE_FLAG <> 1");
        qsPara.add(jObj.getInt("SEQNO"));
        qsPara.add(jObj.getString("CASEID"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                age_under14 += crs.getInt("NUMBER");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        //判斷是否有持有與是否有吸食
        if (hasitem * hasdrug == 0 && suckresult == suckresult_nagtive) {//未持有也未吸食，吸食部分需要所有結果皆為陰性
            success = 1;
        }
        //判斷是否年滿十四歲
        if (age_under14 > 0) {
            success = 2;
        }
        int success_Update = 0;
        //如果未持有也未吸食則判定為不須裁罰 未滿14歲亦然
        if (success == 1 || success == 2) {
            sql.setLength(0);//清除查詢串
            qsPara.clear();//清除變數
            sql.append("UPDATE CJDT_FORM_PENALTY ");
            sql.append("SET CJ_SHOULD_PENALTY = '0' ");
            sql.append("WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=? ");
            qsPara.add(jObj.getString("CASEID"));
            qsPara.add(jObj.getInt("SEQNO"));
            try {
                success_Update = this.pexecuteUpdate(sql.toString(), qsPara.toArray());
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
        }

        return success;
    }

    //檢查受處分人的角色(持有部分)是否真的持有查扣物
    public JSONArray CHECK_drugHold(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        ArrayList man = new ArrayList();
        //找出角色為持有者或施用及持有者的受處分人
        sql.append("SELECT * FROM CJDT_CASE_CLIENT WHERE CJ_CASEID=? AND CJ_CLIENTTYPE IN ('01','03') AND CJ_DELETE_FLAG <> 1");
        qsPara.add(jObj.getString("CASEID"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                man.add(crs.getString("CJ_SEQNO"));
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        if (man.size() > 0) {
            for (int i = 0; i < man.size(); i++) {
                //檢查是否真的有查扣物
                sql.setLength(0);//清除查詢串
                qsPara.clear();//清除變數
                int hold = 0;
//                sql.append("(SELECT COUNT(*) NUM FROM CJDT_CASE_DRUG_CLIENT WHERE CJ_CLIENTSEQ=?) UNION "
//                        + "(SELECT COUNT(*) NUM FROM CJDT_CASE_ITEM_DEVICE WHERE CJ_HOLDER_SN=?) UNION "
//                        + "(SELECT COUNT(*) NUM FROM CJDT_CASE_ITEM_OTHERS WHERE CJ_HOLDER_SN=?)");   //20180213 改成只看CJDT_CASE_DRUG_CLIENT
                sql.append("(SELECT COUNT(*) NUM FROM CJDT_CASE_DRUG_CLIENT WHERE CJ_CLIENTSEQ=?)");
                qsPara.add(Integer.parseInt(man.get(i).toString()));
//                qsPara.add(Integer.parseInt(man.get(i).toString()));
//                qsPara.add(Integer.parseInt(man.get(i).toString()));
                try {
                    crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
                    while (crs.next()) {
                        hold += crs.getInt("NUM");
                    }
                } catch (Exception e) {
                    log.error(ExceptionUtil.toString(e));
                }
                jObject = new JSONObject();
                if (hold == 0) {
                    jObject.put(SharedInfoDao.getClientCodeDisp(man.get(i).toString()), "false");
                } else {
                    jObject.put(SharedInfoDao.getClientCodeDisp(man.get(i).toString()), "true");
                }
                resultDataArray.put(jObject);
            }
        }

        return resultDataArray;
    }

    //檢查受處分人的角色(持有部分)是否真的有吸食
    public JSONArray CHECK_drugSuck(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        ArrayList man = new ArrayList();
        //找出角色為施用者或施用及持有者的受處分人
        sql.append("SELECT * FROM CJDT_CASE_CLIENT WHERE CJ_CASEID=? AND CJ_CLIENTTYPE IN ('02','03') AND CJ_DELETE_FLAG <> 1 ");
        qsPara.add(jObj.getString("CASEID"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                man.add(crs.getString("CJ_SEQNO"));
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        if (man.size() > 0) {
            for (int i = 0; i < man.size(); i++) {
                //檢查是否真的有吸食
                sql.setLength(0);//清除查詢串
                qsPara.clear();//清除變數
                int suck = 0;
//                sql.append("(SELECT COUNT(*) NUM FROM CJDT_CASE_DRUG_CLIENT WHERE CJ_CLIENTSEQ=?) UNION "
//                        + "(SELECT COUNT(*) NUM FROM CJDT_CASE_ITEM_DEVICE WHERE CJ_HOLDER_SN=?) UNION "
//                        + "(SELECT COUNT(*) NUM FROM CJDT_CASE_ITEM_OTHERS WHERE CJ_HOLDER_SN=?)");   //20180213 改成只看CJDT_CASE_DRUG_CLIENT
                sql.append("(SELECT COUNT(*) NUM FROM CJDT_CASE_DRUG_SUCK WHERE CJ_CLIENTSEQ=? AND CJ_TESTSTATUS = 'Y')");
                qsPara.add(Integer.parseInt(man.get(i).toString()));
//                qsPara.add(Integer.parseInt(man.get(i).toString()));
//                qsPara.add(Integer.parseInt(man.get(i).toString()));
                try {
                    crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
                    while (crs.next()) {
                        suck += crs.getInt("NUM");
                    }
                } catch (Exception e) {
                    log.error(ExceptionUtil.toString(e));
                }
                jObject = new JSONObject();
                if (suck == 0) {
                    jObject.put(SharedInfoDao.getClientCodeDisp(man.get(i).toString()), "false");
                } else {
                    jObject.put(SharedInfoDao.getClientCodeDisp(man.get(i).toString()), "true");
                }
                resultDataArray.put(jObject);
            }
        }

        return resultDataArray;
    }


    //檢查是否需等候判決結果
    boolean checkWaitJudge(String CASEID, int CLIENTSEQNO) {
        int hasHoldOver20W = 0;
        int hasSuck1st2nd = 0;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        sql.append(" select COUNT(CJ_DRUGSEQ) NUMBER from CJDT_CASE_DRUG_CLIENT  "
                + "where (CJ_DRUGSEQ in ( "
                + "select A.CJ_DRUGSEQ from CJDT_CASE_DRUG_CLIENT A  "
                + "INNER JOIN CJDT_CASE_DRUG_INGREDIENT B ON A.CJ_DRUGSEQ = B.CJ_CASEDRUG_SN AND A.CJ_ITEMTYPE='DI1' "
                + "INNER JOIN CJDT_DRUG_ITEM C ON C.CJ_ITEMID=B.CJ_DRUGITEMID3 "
                + "WHERE B.CJ_ITEMQUALITY3 is not null and isnull(B.CJ_ITEMQUALITY3,0.000)>= ? and (C.CJ_SUBTYPE='03'OR C.CJ_SUBTYPE='04') ) "
                + " OR "
                + "CJ_DRUGSEQ in ( "
                + "select A.CJ_DRUGSEQ from CJDT_CASE_DRUG_CLIENT A  "
                + "INNER JOIN CJDT_CASE_DRUG_INGREDIENT B ON A.CJ_DRUGSEQ = B.CJ_CASEDRUG_SN AND A.CJ_ITEMTYPE='DI1' "
                + "INNER JOIN CJDT_DRUG_ITEM C ON C.CJ_ITEMID=B.CJ_DRUGITEMID3 "
                + "WHERE B.CJ_ITEMQUALITY3 is not null and isnull(B.CJ_ITEMQUALITY3,0.000)>0 and (C.CJ_SUBTYPE='01'OR C.CJ_SUBTYPE='02') ) )"
                + "AND CJ_CLIENTSEQ=? ");
        qsPara.add(Double.parseDouble(Config.getString("WaitingJudgementDrugWeightMinimum"))); //來自SYSCOM PROPERTY的數字
        qsPara.add(CLIENTSEQNO);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                hasHoldOver20W += crs.getInt("NUMBER");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        sql.setLength(0);//清除查詢串
        qsPara.clear();//清除變數
        sql.append("SELECT COUNT(*) NUMBER FROM CJDT_CASE_DRUG_SUCK WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=? AND (CJ_ITEMTYPE='01'OR CJ_ITEMTYPE='02') AND CJ_TESTSTATUS='Y' ");
        qsPara.add(CASEID);
        qsPara.add(CLIENTSEQNO);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                hasSuck1st2nd += crs.getInt("NUMBER");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        //檢查是否變為等候判決結果
        ArrayList<String> sqls = new ArrayList();
        ArrayList<ArrayList> params = new ArrayList();
        StringBuilder sql2 = new StringBuilder();
        CachedRowSet crs2 = null;
        boolean needwj = false;
        boolean needsp = false;
        int success = 0;

        if (hasHoldOver20W + hasSuck1st2nd > 0) {
            needwj = true;
        } else {
            needwj = false;
        }
        ArrayList qsPara2 = new ArrayList(); // 參數
        sql2.append("SELECT CJ_ITEMTYPE,CJ_TESTSTATUS FROM CJDT_CASE_DRUG_SUCK WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=?");
        qsPara2.add(CASEID);
        qsPara2.add(CLIENTSEQNO);
        try {
            crs2 = this.pexecuteQueryRowSet(sql2.toString(), qsPara2.toArray());
            while (crs2.next()) {
                if (crs2.getString("CJ_ITEMTYPE").equals("03") || crs2.getString("CJ_ITEMTYPE").equals("04")) {
                    if (crs2.getString("CJ_TESTSTATUS").equals("Y")) {//陽性
                        needsp = true;
                    }
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        StringBuilder sql3 = new StringBuilder();
        ArrayList<Object> qsPara3 = new ArrayList<Object>(); // 參數
        if (needwj) {
            sql3.append("UPDATE CJDT_FORM_PENALTY SET CJ_STATUS=? WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=?");
            qsPara3.add("WJ");
        } else {
            sql3.append("UPDATE CJDT_FORM_PENALTY SET CJ_STATUS=? WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=?");
            qsPara3.add("00");
        }
        qsPara3.add(CASEID);
        qsPara3.add(CLIENTSEQNO);
        sqls.add(sql3.toString());
        params.add(qsPara3);

        StringBuilder sql4 = new StringBuilder();
        ArrayList<Object> qsPara4 = new ArrayList<Object>(); // 參數
        if (needsp) {
            sql4.append("UPDATE CJDT_FORM_PENALTY SET CJ_SHOULD_PENALTY=? WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=?");
            qsPara4.add("Y");
        } else {
            sql4.append("UPDATE CJDT_FORM_PENALTY SET CJ_SHOULD_PENALTY=? WHERE CJ_CASEID=? AND CJ_CLIENTSEQ=?");
            qsPara4.add("N");
        }
        qsPara4.add(CASEID);
        qsPara4.add(CLIENTSEQNO);
        sqls.add(sql4.toString());
        params.add(qsPara4);

        Object[][] objParams = new Object[params.size()][];
        for (int i = 0; i < params.size(); i++) {
            objParams[i] = params.get(i).toArray();
        }
        try {
//            success_SaveOutcome += this.pexecuteUpdate(sql3.toString(), qsPara3.toArray());
            int[] successArr = this.pexecuteBatch(sqls.toArray(), objParams);

            for (int i = 0; i < successArr.length; i++) {
                success += successArr[i];
            }
            //System.out.println("Save1_1_1執行筆數: "+success1_1_1);
            if (success == sqls.toArray().length) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            return false;
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
        String sql = "SELECT '1pcnt' as type, count(*) as cnt FROM proctick where evid=? and username=? "; 
        ArrayList<HashMap> result = this.pexecuteQuery(sql, new Object[]{evid, username});
        JSONObject jo = new JSONObject();
        jo.put("countSelf", result.isEmpty()? 0: result.get(0).get("cnt"));//該場所有人總輸入票根
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
