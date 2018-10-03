/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.syscom.db.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;
import base.CJBaseDao;
import static util.DBUtil.transCachedRowSet2ArrayList;
import util.ExceptionUtil;
import util.FixedCachedRowSetImpl;
import util.User;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class SharedInfoDao extends CJBaseDao {

    private static SharedInfoDao instance = null;
    private static Logger log = Logger.getLogger(SharedInfoDao.class);
    public static HashMap AppcodeMap = new HashMap(226);
    public static HashMap CityMap = new HashMap();
    public static HashMap TownMap = new HashMap();
    public static HashMap UnitMap = new HashMap();
    public static HashMap DrugMap = new HashMap();
    public static HashMap DrugItemMap = new HashMap();
    public static HashMap DrugIOthertemMap = new HashMap();
    public static HashMap SchoolMap = new HashMap();
    public static HashMap FactoryMap = new HashMap();
    public static HashMap ClientMap = new HashMap();
    public static HashMap ExSentenceMap = new HashMap();
    public static HashMap NonPoliceMap = new HashMap();
    public static HashMap ItemTypeMap = new HashMap();
    public static HashMap FileRuleMap = new HashMap();
    public static HashMap WorkshopClassMap = new HashMap();
    public static HashMap PENALTYSTATUSMap = new HashMap();
    public static HashMap FINESTATUSMap = new HashMap();
    public static HashMap WORKSHOPSTATUSMap = new HashMap();
    public static HashMap CityAndUnitCD1Map = new HashMap();

    private CommonDao comDao = CommonDao.getInstance();

    private SharedInfoDao() {
    }

    public static SharedInfoDao getInstance() {
        if (instance == null) {
            instance = new SharedInfoDao();
        }
        return instance;
    }

    /**
     *
     */
    public static void initAppCodeDisp() {
        AppcodeMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CATEGORY, CJ_CODECODE, CJ_CODEDISP FROM CJDT_APP_CODE");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            AppcodeMap.put(m.get("CJ_CATEGORY").toString() + "-" + m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     * CITY 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initCityCodeDisp() {
        CityMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE, CJ_CODEDISP FROM CJDT_CITY_CODE");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            CityMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getCityCodeDisp(String code) {
        if (CityMap.isEmpty()) {
            initCityCodeDisp();
        }
        String dispNm = "";
        if (code != null && CityMap.containsKey(code)) {
            dispNm = CityMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * TOWN先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initTownCodeDisp() {
        TownMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE, CJ_CODEDISP FROM CJDT_TOWN_CODE");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            TownMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getTownCodeDisp(String code) {
        if (TownMap.isEmpty()) {
            initTownCodeDisp();
        }
        String dispNm = "";
        if (code != null && TownMap.containsKey(code)) {
            dispNm = TownMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * UNIT先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initUnitCodeDisp() {
        UnitMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT where E0_DELETE_FLAG<>1 ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            UnitMap.put(m.get("E0_UNIT_CD").toString(), m.get("E0_UNIT_S_NM").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getUnitCodeDisp(String code) {
        if (UnitMap.isEmpty()) {
            initUnitCodeDisp();
        }
        String dispNm = "";
        if (code != null && UnitMap.containsKey(code)) {
            dispNm = UnitMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initDrugCodeDisp() {
        DrugMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SEQNO,CJ_DRUGITEMNM1 FROM CJDT_CASE_DRUG ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            DrugMap.put(m.get("CJ_SEQNO").toString(), m.get("CJ_DRUGITEMNM1").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getDrugCodeDisp(String code) {
        if (DrugMap.isEmpty()) {
            initDrugCodeDisp();
        }
        String dispNm = "";
        if (code != null && DrugMap.containsKey(code)) {
            dispNm = DrugMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initDrugItemCodeDisp() {
        DrugItemMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_ITEMID,CJ_ITEMNAME FROM CJDT_DRUG_ITEM ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            DrugItemMap.put(m.get("CJ_ITEMID").toString(), m.get("CJ_ITEMNAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getDrugItemCodeDisp(String code) {
        if (DrugItemMap.isEmpty()) {
            initDrugItemCodeDisp();
        }
        String dispNm = "";
        if (code != null && DrugItemMap.containsKey(code)) {
            dispNm = DrugItemMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initDrugOtherItemCodeDisp() {
        DrugIOthertemMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SEQNO,CJ_ITEMNAME FROM CJDT_CASE_ITEM_OTHERS ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            DrugIOthertemMap.put(m.get("CJ_SEQNO").toString(), m.get("CJ_ITEMNAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getDrugIOthertemCodeDisp(String code) {
        if (DrugIOthertemMap.isEmpty()) {
            initDrugOtherItemCodeDisp();
        }
        String dispNm = "";
        if (code != null && DrugIOthertemMap.containsKey(code)) {
            dispNm = DrugIOthertemMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initSchoolCodeDisp() {
        SchoolMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SEQ, CJ_SCHOOLNAME FROM CJDT_SCHOOL ");// WHERE CJ_ISACTIVE='Y'");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            SchoolMap.put(m.get("CJ_SEQ").toString(), m.get("CJ_SCHOOLNAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getSchoolCodeDisp(String code) {
        if (SchoolMap.isEmpty()) {
            initSchoolCodeDisp();
        }
        String dispNm = "";
        if (code != null && SchoolMap.containsKey(code)) {
            dispNm = SchoolMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initFactoryCodeDisp() {
        FactoryMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_FACTORYID, CJ_FACTORYNAME FROM CJDT_DRUG_FACTORY WHERE CJ_ISACTIVE='Y'");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            FactoryMap.put(m.get("CJ_FACTORYID").toString(), m.get("CJ_FACTORYNAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getFactoryCodeDisp(String code) {
        if (FactoryMap.isEmpty()) {
            initFactoryCodeDisp();
        }
        String dispNm = "";
        if (code != null && FactoryMap.containsKey(code)) {
            dispNm = FactoryMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initClientCodeDisp() {
        ClientMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SEQNO, CJ_NAME FROM CJDT_CASE_CLIENT");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            ClientMap.put(m.get("CJ_SEQNO").toString(), m.get("CJ_NAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getClientCodeDisp(String code) {
        if (ClientMap.isEmpty()) {
            initClientCodeDisp();
        }
        String dispNm = "";
        if (code != null && ClientMap.containsKey(code)) {
            dispNm = ClientMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initExSentenceDisp() {
        ExSentenceMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SENTENCE_ID, CJ_SENTENCE FROM CJDT_EXAMPLE_SENTENCE");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            ExSentenceMap.put(m.get("CJ_SENTENCE_ID").toString(), m.get("CJ_SENTENCE").toString());
        }
    }

    /**
     *
     */
    public static void initItemTypeCodeDisp() {
        ItemTypeMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE, CJ_CODEDISP FROM CJDT_APP_CODE WHERE CJ_CATEGORY='DRUGITEM' AND CJ_REMARK='DF' AND CJ_DISABLED='N' ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            ItemTypeMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getExSentenceDisp(String code) {
        if (ExSentenceMap.isEmpty()) {
            initExSentenceDisp();
        }
        String dispNm = "";
        if (code != null && ExSentenceMap.containsKey(code)) {
            dispNm = ExSentenceMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     */
    public static void initNonPoliceDisp() {
        NonPoliceMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_UNIT_CD,CJ_UNIT_NM FROM CJDT_OTHER_UNITS");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            NonPoliceMap.put(m.get("CJ_UNIT_CD").toString(), m.get("CJ_UNIT_NM").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getNonPoliceDisp(String code) {
        if (NonPoliceMap.isEmpty()) {
            initNonPoliceDisp();
        }
        String dispNm = "";
        if (code != null && NonPoliceMap.containsKey(code)) {
            dispNm = NonPoliceMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getItemTypeDisp(String code) {
        if (ItemTypeMap.isEmpty()) {
            initItemTypeCodeDisp();
        }
        String dispNm = "";
        if (code != null && ItemTypeMap.containsKey(code)) {
            dispNm = ItemTypeMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * <<<<<<< .mine
     * CITY 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     * =======
     *  FileRule 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     * >>>>>>> .r4480
     */
    public static void initFileRuleDisp() {
        FileRuleMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SEQNO,CJ_TYPE_NM FROM CJDT_FACTORY_ATTACH_RULE");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            FileRuleMap.put(m.get("CJ_SEQNO").toString(), m.get("CJ_TYPE_NM").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getFileRuleDisp(String code) {
        if (FileRuleMap.isEmpty()) {
            initFileRuleDisp();
        }
        String dispNm = "";
        if (code != null && FileRuleMap.containsKey(code)) {
            dispNm = FileRuleMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * WorkshopClass 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initWorkshopClassDisp() {
        WorkshopClassMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_SN,CJ_LOCATION,CJ_DATE,CJ_TIME_S,CJ_HOURS,CJ_CLS_NAME FROM CJDT_CLASS_INFO");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            WorkshopClassMap.put(m.get("CJ_SN").toString(), m.get("CJ_LOCATION").toString()
                    + "-民國" + (m.get("CJ_DATE").toString()).substring(0, 3) + "年" + (m.get("CJ_DATE").toString()).substring(3, 5) + "月" + (m.get("CJ_DATE").toString()).substring(5, 7) + "日"
                    + (m.get("CJ_TIME_S").toString().substring(0, 2)) + "時" + (m.get("CJ_TIME_S").toString().substring(2, 4)) + "分" + " 時數:"
                    + " 時數:" + (m.get("CJ_HOURS").toString()) + " 主題:" + m.get("CJ_CLS_NAME").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getWorkshopClassDisp(String code) {
        if (WorkshopClassMap.isEmpty()) {
            initWorkshopClassDisp();
        }
        String dispNm = "";
        if (code != null && WorkshopClassMap.containsKey(code)) {
            dispNm = WorkshopClassMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * PENALTYSTATUS 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initPENALTYSTATUSDisp() {
        PENALTYSTATUSMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE,CJ_CODEDISP FROM CJDT_APP_CODE WHERE CJ_CATEGORY='PENALTYSTATUS'");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            PENALTYSTATUSMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getPENALTYSTATUSDisp(String code) {
        if (PENALTYSTATUSMap.isEmpty()) {
            initPENALTYSTATUSDisp();
        }
        String dispNm = "";
        if (code != null && PENALTYSTATUSMap.containsKey(code)) {
            dispNm = PENALTYSTATUSMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * FINESTATUS 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initFINESTATUSDisp() {
        FINESTATUSMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE,CJ_CODEDISP FROM CJDT_APP_CODE WHERE CJ_CATEGORY='FINESTATUS'");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            FINESTATUSMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getFINESTATUSDisp(String code) {
        if (FINESTATUSMap.isEmpty()) {
            initFINESTATUSDisp();
        }
        String dispNm = "";
        if (code != null && FINESTATUSMap.containsKey(code)) {
            dispNm = FINESTATUSMap.get(code).toString();
        }
        return dispNm;
    }

    /**
     * WORKSHOPSTATUS 先撈一次資料到HashTable，避免每次儲存時都需要耗時重撈一次
     */
    public static void initWORKSHOPSTATUSDisp() {
        WORKSHOPSTATUSMap.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT CJ_CODECODE,CJ_CODEDISP FROM CJDT_APP_CODE WHERE CJ_CATEGORY='WORKSHOPSTATUS'");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap m : list) {
            WORKSHOPSTATUSMap.put(m.get("CJ_CODECODE").toString(), m.get("CJ_CODEDISP").toString());
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getWORKSHOPSTATUSDisp(String code) {
        if (WORKSHOPSTATUSMap.isEmpty()) {
            initWORKSHOPSTATUSDisp();
        }
        String dispNm = "";
        if (code != null && WORKSHOPSTATUSMap.containsKey(code)) {
            dispNm = WORKSHOPSTATUSMap.get(code).toString();
        }
        return dispNm;
    }

    public static void initCityAndUnitCD1() {
        CityAndUnitCD1Map.clear();
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * FROM CJDT_CITY_CODE order by cast(CJ_REMARK as int) ");
        ArrayList<HashMap> list = DBUtil.getInstance().pexecuteQuery(sql.toString(), qsPara.toArray());
        String[] UnitCD1list = {
            "中央",
            "基隆市警察局",
            "臺北市政府警察局",
            "新北市政府警察局",
            "桃園市政府警察局",
            "新竹市警察局",
            "新竹縣政府警察局",
            "苗栗縣警察局",
            "臺中市政府警察局",
            "彰化縣警察局",
            "南投縣政府警察局",
            "雲林縣警察局",
            "嘉義市政府警察局",
            "嘉義縣警察局",
            "臺南市政府警察局",
            "高雄市政府警察局",
            "屏東縣政府警察局",
            "宜蘭縣政府警察局",
            "花蓮縣警察局",
            "臺東縣警察局",
            "澎湖縣政府警察局",
            "金門縣警察局",
            "連江縣警察局"
            };
        for (int i=0;i<23;i++) {
            CityAndUnitCD1Map.put(list.get(i).get("CJ_CODECODE").toString(),UnitCD1list[i]);
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static String getCityAndUnitCD1(String code) {
        if (CityAndUnitCD1Map.isEmpty()) {
            initCityAndUnitCD1();
        }
        String dispNm = "";
        if (code != null && CityAndUnitCD1Map.containsKey(code)) {
            dispNm = CityAndUnitCD1Map.get(code).toString();
        }
        return dispNm;
    }

    //從DB抓城市名與代碼
    public JSONArray GET_ALLCITY(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        sql.append("SELECT * FROM CJDT_CITY_CODE order by cast(CJ_REMARK as int) ");
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("CJ_CODECODE", crs.getString("CJ_CODECODE"));
                jObject.put("CJ_CODEDISP", crs.getString("CJ_CODEDISP"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
    }

    //從DB抓鄉鎮名與代碼
    public JSONArray GET_ALLTOWN(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        sql.append("SELECT * FROM CJDT_TOWN_CODE WHERE CJ_CITYCODE=? and CJ_CODEDISP NOT LIKE '＊%' ");
        qsPara.add(jObj.getString("CJ_CITYCODE"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("CJ_CODECODE", crs.getString("CJ_CODECODE"));
                jObject.put("CJ_CODEDISP", crs.getString("CJ_CODEDISP"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
    }
    //從DB抓SCHOOL

    public JSONArray GET_SCHOOL(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        sql.append("SELECT CJ_SEQ,CJ_SCHOOLNAME FROM CJDT_SCHOOL WHERE CJ_CITYCODE=? AND CJ_SCHOOLLEVEL=?");
        qsPara.add(jObj.getString("CJ_CITYCODE"));
        qsPara.add(jObj.getString("CJ_SCHOOLLEVEL"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("CJ_SEQ", crs.getInt("CJ_SEQ"));
                jObject.put("CJ_SCHOOLNAME", crs.getString("CJ_SCHOOLNAME"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
    }

    //從DB抓SCHOOLLEVEL
    public JSONArray GET_SCHOOLLEVEL(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        CachedRowSet crs = null;
        sql.append("SELECT * FROM CJDT_APP_CODE WHERE CJ_CATEGORY=?");
        qsPara.add(jObj.getString("CJ_CATEGORY"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("CJ_SCHOOLLEVEL", crs.getString("CJ_CODECODE"));
                jObject.put("CJ_SCHOOLLEVELNAME", crs.getString("CJ_CODEDISP"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
    }

    // region Query
    public JSONArray getSubCode(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();

        sql.append("SELECT ")
                .append(" CJ_CATEGORY, CJ_CODECODE, CJ_CODEDISP, CJ_REMARK, CJ_DISABLED,CJ_ORDERNO ")
                .append(" FROM CJDT_APP_CODE ")
                .append(" WHERE CJ_CATEGORY = ? ");
        if (jObj.has("CJ_CATEGORY") && !"".equals(jObj.getString("CJ_CATEGORY"))) {
            qsPara.add(jObj.getString("CJ_CATEGORY"));
        }
        if (jObj.has("CJ_MAINTYPE") && !"".equals(jObj.getString("CJ_MAINTYPE"))) {
            qsPara.add(jObj.getString("CJ_MAINTYPE"));
            sql.append(" and (CJ_CODECODE='DI1' or CJ_CODECODE='DI4')");
        }
        if (jObj.has("CJ_SUBTYPE") && !"".equals(jObj.getString("CJ_SUBTYPE"))) {
            qsPara.add(jObj.getString("CJ_SUBTYPE"));
        }
        sql.append(" ORDER BY CJ_CODECODE ");

        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());
        for (HashMap map : mapList) {
            map.put("CJ_DISABLED_DISPLAY", ("Y".equals(map.get("CJ_DISABLED").toString()) ? "是" : "否"));
        }

        return arrayList2JsonArray(mapList);
    }

    /**
     * 取得NPA unit cd
     *
     * @param cibUnitCd
     * @return
     */
    public String getNpaUnitCd(String cibUnitCd) {
        if (cibUnitCd.startsWith("A") || cibUnitCd.startsWith("B")) {
            return cibUnitCd;
        }
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        String userNm = "";
        CachedRowSet crs = null;
        sql.append("SELECT CJ_NPA_UNIT_CD FROM CJDT_UNITS WHERE CJ_UNITCODE = ?");
        qsPara.add(cibUnitCd);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                userNm = crs.getString("CJ_NPA_UNIT_CD");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        log.debug("get CJ_NPA_UNIT_CD:" + userNm);
        return userNm;
    }

    /**
     * 取得NPA unit cd
     *
     * @param cibUnitCd
     * @return
     */
    public String getNpaUnitNm(String cibUnitCd) {
        if (cibUnitCd.startsWith("A") || cibUnitCd.startsWith("B")) {
            return cibUnitCd;
        }
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        String unitNm = "";
        CachedRowSet crs = null;
        sql.append("SELECT CJ_UNITNAME FROM CJDT_UNITS WHERE CJ_UNITCODE = ?");
        qsPara.add(cibUnitCd);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                unitNm = crs.getString("CJ_UNITNAME");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        log.debug("get CJ_UNITNAME:" + unitNm);
        return unitNm;
    }

    /**
     * 取得使用者姓名(好像無用武之地了)
     *
     * @param cibUserId
     * @return
     */
    public String getUserNm(int cibUserId) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        String userNm = "";
        CachedRowSet crs = null;
        sql.append("SELECT CJ_USERNAME FROM CJDT_USERS WHERE CJ_USERID= ?");
        qsPara.add(cibUserId);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                userNm = crs.getString("CJ_USERNAME");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        log.debug("get userNm:" + userNm);
        return userNm;
    }

    //teststart   
    // 永續層操作
    // region 查詢 Model Start
    // ----------------------------------------
    /**
     * 取得符合條件資料之CD及NAME.
     *
     * @param level 1-警政署 2-警局 3-分局 4-派出所
     * @param level2CODE
     * @param level3CODE
     * @return JSONArray.
     */
//    public JSONArray getUnitByLevel(final String level, final String level2CODE, final String level3CODE) {
//        StringBuilder sql = new StringBuilder();
//        ArrayList qsPara = new ArrayList();
//
//        sql.append("SELECT CJ_UNIT_CD, CJ_UNIT_S_NM FROM CJDT_E0_NPAUNIT "
//                + "WHERE CJ_DELETE_FLAG <> 1 and CJ_IS_SHOW  > 0 ");
//
//        if (null != level) {
//            switch (level) {
//                case "2":
//                    sql.append(" AND CJ_DEPT_CD = CJ_UNIT_CD ");
//                    sql.append(" AND CJ_UNIT_FLAG NOT IN ('91','92') ");
//                    break;
//                case "3":
//                    sql.append(" AND (CJ_UNIT_LEVEL IN ('1','3') or (CJ_UNIT_LEVEL = '4' and CJ_UNIT_FLAG = '77'))");
//                    sql.append(" AND CJ_UNIT_FLAG NOT IN ('90','92') ");
//                    break;
//                case "4":
//                    sql.append(" AND CJ_UNIT_LEVEL IN ('1','4') ");
//                    sql.append(" AND CJ_UNIT_FLAG NOT IN ('77','90','91') ");
//                    break;
//            }
//        }
//
//        if (nvl(level2CODE).trim().length() > 0) {
//            sql.append(" AND CJ_DEPT_CD=? ");
//            qsPara.add(level2CODE);
//        }
//
//        if (nvl(level3CODE).trim().length() > 0) {
//            sql.append(" AND CJ_BRANCH_CD=? ");
//            qsPara.add(level3CODE);
//        }
//
//        sql.append(" ORDER BY CJ_IS_SHOW,CJ_UNIT_CD");
//
//        ArrayList<HashMap> list = pexecuteQuery(sql.toString(), qsPara.toArray());
//
//        return arrayList2JsonArray(list);
//    }
    /**
     * 取得NPA unit cd
     *
     * @param cibUnitCd
     * @return
     */
    public String getBidUnitCd(String npaUnitCd) {

        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        String BidCD = "";
        CachedRowSet crs = null;
        sql.append("SELECT CJ_UNITCODE FROM CJDT_UNITS WHERE CJ_NPA_UNIT_CD = ?");
        qsPara.add(npaUnitCd);
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                BidCD = crs.getString("CJ_UNITCODE");
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        log.debug("get CJ_UNITCODE:" + BidCD);
        return BidCD;
    }
    //從DB抓鄉鎮名與代碼

    public JSONArray getDrugDropDown(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList qsPara = new ArrayList();
        JSONObject jObject = new JSONObject();
        JSONArray resultDataArray = new JSONArray();
        String MainType = jObj.getString("MAINTYPE");

        CachedRowSet crs = null;
        sql.append("SELECT * FROM CJDT_TOWN_CODE WHERE CJ_CITYCODE=? and CJ_CODEDISP NOT LIKE '＊%' ");
        qsPara.add(jObj.getString("CJ_CITYCODE"));
        try {
            crs = this.pexecuteQueryRowSet(sql.toString(), qsPara.toArray());
            while (crs.next()) {
                jObject = new JSONObject();
                jObject.put("CJ_CODECODE", crs.getString("CJ_CODECODE"));
                jObject.put("CJ_CODEDISP", crs.getString("CJ_CODEDISP"));
                resultDataArray.put(jObject);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return resultDataArray;
    }

    public JSONArray getAttachType(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'ATTACHTYPE' AND CJ_DISABLED ='N' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getAttachType_Factory(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_FACTORY_ATTACH_RULE  ")
                .append(" WHERE CJ_NOTEMPTY ='Y' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getClientType(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'CLIENTTYPE' AND CJ_DISABLED ='N' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getCommitReason(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'COMMITREASON' AND CJ_DISABLED ='N' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getDrugSource(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'DRUGSOURCE' AND CJ_DISABLED ='N' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getOccupation(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'OCCUPATION'  ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getDetectPlaceType(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'DETECTPLACETYPE' AND CJ_DISABLED ='N' ");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }

    public JSONArray getJusticeUnit(JSONObject jObj) {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> qsPara = new ArrayList<Object>();
        sql.append("SELECT * ")
                .append(" FROM CJDT_APP_CODE  ")
                .append(" WHERE CJ_CATEGORY = 'REFERUNIT'  ORDER BY CJ_ORDERNO");
        ArrayList<HashMap> mapList = this.pexecuteQuery(sql.toString(), qsPara.toArray());

        return arrayList2JsonArray(mapList);
    }
    
    public static String getUnitCityCode(String unit) {
        String userCityCode = "";
        if (unit.substring(0, 2).equals("AD")) {
            userCityCode = "65000";
        } else if (unit.substring(0, 2).equals("AW")) {
            userCityCode = "63000";
        } else if (unit.substring(0, 2).equals("AE")) {
            //60008?
            userCityCode = "68000";
        } else if (unit.substring(0, 2).equals("BG")) {
            userCityCode = "10004";
        } else if (unit.substring(0, 2).equals("BF")) {
            userCityCode = "10018";
        } else if (unit.substring(0, 2).equals("BH")) {
            userCityCode = "10005";
        } else if (unit.substring(0, 2).equals("AB")) {
            userCityCode = "66000";
        } else if (unit.substring(0, 2).equals("BJ")) {
            userCityCode = "10007";
        } else if (unit.substring(0, 2).equals("BL")) {
            userCityCode = "10009";
        } else if (unit.substring(0, 2).equals("BN")) {
            userCityCode = "10010";
        } else if (unit.substring(0, 2).equals("BM")) {
            userCityCode = "10020";
        } else if (unit.substring(0, 2).equals("AC")) {
            userCityCode = "67000";
        } else if (unit.substring(0, 2).equals("AV")) {
            userCityCode = "64000";
        } else if (unit.substring(0, 2).equals("BQ")) {
            userCityCode = "10013";
        } else if (unit.substring(0, 2).equals("BR")) {
            userCityCode = "10014";
        } else if (unit.substring(0, 2).equals("BS")) {
            userCityCode = "10015";
        } else if (unit.substring(0, 2).equals("BT")) {
            userCityCode = "10002";
        } else if (unit.substring(0, 2).equals("BU")) {
            userCityCode = "10016";
        } else if (unit.substring(0, 2).equals("BY")) {
            userCityCode = "09020";
        } else if (unit.substring(0, 2).equals("BZ")) {
            userCityCode = "09007";
        } else if (unit.substring(0, 2).equals("BK")) {
            userCityCode = "10008";
        } else if (unit.substring(0, 2).equals("BA")) {
            userCityCode = "10017";
        }

        return userCityCode;
    }
}
