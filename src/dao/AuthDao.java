package dao;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import com.syscom.util.StringUtils;

import base.CJBaseDao;
import util.ExceptionUtil;
import util.StringUtil;
import util.User;

public class AuthDao extends CJBaseDao {

    private static AuthDao instance = null;
    private static Logger log = Logger.getLogger(AuthDao.class);

    private AuthDao() {
    }

    public static AuthDao getInstance() {
        if (instance == null) {
            instance = new AuthDao();
        }
        return instance;
    }

    /**
     * 檢查登入帳密是否正確以及取得相關資料
     *
     * @param id
     * @param pwd
     * @param session
     * @return
     */
    /**
     * 檢查登入帳密是否正確以及取得相關資料
     *
     * @param id
     * @param pwd
     * @param session
     * @param isCheckUser 是否要檢查帳號密是否正確
     * @return 錯誤訊息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String chkUser(String id, String pwd, HttpSession session, boolean isCheckUser) {
        String msg = "";
        try {
            
            boolean validBln = true;
            if (isCheckUser) {
                
            }
            log.debug("isCheckUser=" + isCheckUser);
            log.debug("validBln=" + validBln);
            if (validBln) { // 密碼正確
                String uid = id; // 使用者帳號
                String cn = ""; // 使用者姓名
                String npa_pid = ""; // 使用者證號(員編?)
                String npa_title = ""; // 使用者職稱
                String npa_org_id = ""; // 使用者單位
                String npa_org_nm = "";// 使用者名稱
                String npa_tdt = ""; // 登入時間
                String userIP = ""; // 使用者登入IP

            } else { // 密碼不正確
                log.debug("帳號或密碼錯誤，請重新輸入!");
                msg = "帳號或密碼錯誤，請重新輸入!";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ExceptionUtil.toString(ex));
            msg = "登入失敗，發生例外錯誤：\\n" + ex.getMessage() + "---" + ExceptionUtil.toString(ex);
        }

        return msg;
    }

    /**
     * 設定User的ScopeUnitCd與setScopeUnitLevel，但是必須先設定三層警局單位與UnitCd
     *
     * @param user
     */
    public void setUserScopeUnit(User user) {
        String scopeUnitCd = "";
        String scopeUnitLevel = "";
        String scopeUnitLevelNum = "";
        String unitLevel = user.getUnitLevel();
        String unitFlag = user.getUnitFlag();
        String unitNm = user.getUnitName();
        log.debug("單位層級:" + unitLevel + "---單位旗標:" + unitFlag + "---unitNm:" + unitNm);
        boolean isCountryCrime = false;//是否為縣市刑大
        if (user.getUnitCd().charAt(2) == 'Z') {
            isCountryCrime = true;
        }

        // 一般的條件判斷
        if (user.getUnitCd1().equals("A1000")// 警政署則是空白
                //|| user.getUnitCd().equals("A22Y1")// 刑事局偵防中心
                //|| user.getUnitCd().startsWith("A22")//新規則，只要是刑事局底下的單位都為相同的最高權限 104/07/3 因為刑事局偵查科查不到全國重大刑案案況表裡的資料
                //|| user.getUnitCd().equals("AWZ00")// 北市警局刑事大隊
                ) {
            scopeUnitLevel = "";
            scopeUnitCd = "";
            scopeUnitLevelNum = "";

        } else if ((unitLevel.equals("2") && unitFlag.equals("10"))// 縣市警察局
                || (unitLevel.equals("2") && unitFlag.equals("50"))// 專業單位 總隊局
                //|| user.getUnitCd().equals("AWZ00")// 北市警局刑事大隊
                || (unitLevel.equals("3") && unitFlag.equals("21"))// 縣市警察局 科室課/中心 [用於保安業務人員]
                || (unitLevel.equals("3") && unitFlag.equals("22"))// 縣市警察局 大隊/隊 [婦幼隊、捷運隊] 104/5/20
                || (unitLevel.equals("3") && unitFlag.equals("60") && (unitNm.indexOf("刑事警察大隊") > -1 || unitNm.indexOf("刑事警察隊") > -1 || unitNm.indexOf("保安警察隊") > -1 || unitNm.indexOf("保安警察大隊") > -1 || unitNm.indexOf("安全檢查") > -1))// 專業單位 大隊/中隊/隊/分隊/段/分局  104/5/20
                || (unitLevel.equals("3") && unitFlag.equals("61"))// 縣市警察局 大隊/隊 104/5/20
                || (unitLevel.equals("4") && unitFlag.equals("32"))// 大隊 隊/中隊/分隊 104/5/28
                || (unitLevel.equals("4") && unitFlag.equals("33"))// 大隊 組室 104/5/28
                || isCountryCrime//縣市刑大
                ) {
            scopeUnitLevel = "LE_UNIT_CD1";
            scopeUnitCd = user.getUnitCd1();
            scopeUnitLevelNum = "1";

        } else if ((unitLevel.equals("3") && unitFlag.equals("20")) // 縣市分局
                || (unitLevel.equals("3") && unitFlag.equals("60"))// 專業單位 大隊/中隊/隊/分隊/段/分局  104/5/20
                || (unitLevel.equals("4") && unitFlag.equals("77"))// 專業單位   // 中隊 // (港務警察局)
                || (unitLevel.equals("4") && unitFlag.equals("31"))// 縣市分局 組室/中心/隊 [因為士林偵查隊的關係移上來]
                ) {
            scopeUnitLevel = "CJ_UNIT_CD2";
            scopeUnitCd = user.getUnitCd2();
            scopeUnitLevelNum = "2";

        } else if ((unitLevel.equals("4") && unitFlag.equals("30")) // 分駐所/派出所/駐在所/檢查所
                || (unitLevel.equals("4") && unitFlag.equals("37"))// 檢查哨
                || (unitLevel.equals("4") && unitFlag.equals("71"))// 專業單位 組室
                || (unitLevel.equals("4") && unitFlag.equals("70"))// 專業單位
                // 派出所/分駐所/隊/中隊/分隊/小隊
                ) {
            scopeUnitLevel = "CJ_UNIT_CD3";
            scopeUnitCd = StringUtil.nvl(user.getUnitCd3()).length() > 0 ? user.getUnitCd3() : user.getUnitCd();
            scopeUnitLevelNum = "3";

        } else {
            log.debug("未定義的單位層級與單位旗標");
            scopeUnitLevel = "";
            scopeUnitCd = "";
            scopeUnitLevelNum = "";

        }

        // 針對勤指中心處理,往上升一級
        boolean isExist = pisExist("SELECT * FROM ABDB..E0DT_NPAUNIT WHERE (E0_UNIT_NM LIKE '%勤%指%中心' or E0_UNIT_NM LIKE '%指%中心[一二]') AND E0_UNIT_CD = ?",
                new Object[]{user.getUnitCd()});
        if (isExist && !isCountryCrime) {//排除縣市刑大下的勤指
            if (unitLevel.equals("3")) {
                scopeUnitLevel = "CJ_UNIT_CD1";
                scopeUnitCd = user.getUnitCd1();
                scopeUnitLevelNum = "1";
            } else if (unitLevel.equals("4")) {
                scopeUnitLevel = "CJ_UNIT_CD2";
                scopeUnitCd = user.getUnitCd2();
                scopeUnitLevelNum = "2";
            }
        }

        log.debug("scopeUnitLevel:" + scopeUnitLevel + "---scopeUnitCd:" + scopeUnitCd + "---scopeUnitLevelNum:" + scopeUnitLevelNum);
        user.setScopeUnitLevel(scopeUnitLevel);
        user.setScopeUnitCd(scopeUnitCd);
        user.setScopeUnitLevelNum(scopeUnitLevelNum);

    }

    /**
     * 設定User所有角色所能使用的功能清單(OwnFun)，但是必須先設定OwnRole
     *
     * @param user
     */
    public void setUserFuncList(User user) {
        String[] roles = user.getOwnRole();
        // 列出該角色所能使用的功能清單
        String roleSql = "";

        if (roles.length > 0) {
            int idx = 0;
            for (String role : roles) {
                log.debug("role=" + role);
                idx++;
                if (idx == 1) {
                    roleSql += "(LE_ROLE = '" + role + "'";
                } else {
                    roleSql += " OR LE_ROLE = '" + role + "'";
                }
            }
            roleSql += ")";
        }
        log.debug("roleSql=" + roleSql);
        String sql = "SELECT DISTINCT(CJ_FUNC) FROM CJDT_FUNC_ROLE FUNC_ROLE "
                + "INNER JOIN CJDT_FUNC FUNC ON FUNC_ROLE.CJ_FUNC = FUNC.CJ_FUNC_ID AND FUNC.CJ_ENABLED='Y' "
                + "WHERE " + roleSql + " AND CJ_ENABLE='Y'";
        ArrayList<HashMap> list = executeQuery(sql);
        String func = "";
        if (list.size() > 0) {
            String[] funcs = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                func = StringUtils.nvl(list.get(i).get("CJ_FUNC"));
                funcs[i] = func;
                if (func.equals("F003")) {// 治安狀況摘要表輸入
                    user.setPublicSecurityAbstractKeyin(true);
                } else if (func.equals("F004")) {// 重大案件審核
                    user.setMajorCaseVerify(true);
                } else if (func.equals("F005")) {// 重大案件結案
                    user.setMajorCaseClose(true);
                } else if (func.equals("F006")) {// 重大刑案審核
                    user.setMajorCriminalCaseVerify(true);
                } else if (func.equals("F007")) {// 重大刑案結案
                    user.setMajorCriminalCaseClose(true);
                } else if (func.equals("F008")) {// 案件清單可刪除案件
                    user.setCaseDel(true);
                }

            }
            user.setOwnFun(funcs);
        }
    }

    /**
     * 設定User的三層警局單位(UnitCd1,UnitCd2,UnitCd3)與單位層級(unitLevel)與單位旗標(unitFlag)，
     * 但是必須先設定UnitCd
     *
     * @param user
     * @param unitCd 使用者所隸屬的單位
     */
    public void setUser3LevelUnit(User user) {
        // 查取出三階單位DB
        String unitCd = user.getUnitCd();
//        String sql = "SELECT CJ_UNIT_NM,CJ_UNIT_S_NM, CJ_UNIT_LEVEL, CJ_DEPT_CD, CJ_BRANCH_CD, CJ_UNIT_FLAG  FROM CJDT_E0_NPAUNIT WHERE CJ_UNIT_CD= ?";
        String sql = "SELECT E0_UNIT_NM,E0_UNIT_S_NM, E0_UNIT_LEVEL, E0_DEPT_CD, E0_BRANCH_CD, E0_UNIT_FLAG  FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_CD= ?";
        
        HashMap row = pgetFirstRow(sql, new Object[]{unitCd});
        String unitCd1 = "";
        String unitNm1 = "";
        String unitCd2 = "";
        String unitNm2 = "";
        String unitCd3 = "";
        String unitNm3 = "";
        String unitName = "";// 單位全名
        String unitFlag = "";
        String unitLevel = "";

        String unitFullNm = ""; //new 單位完整全名
        if (row != null) {
            String depCd = StringUtils.nvl(row.get("E0_DEPT_CD"));
            String branchCd = StringUtils.nvl(row.get("E0_BRANCH_CD"));
            unitLevel = StringUtils.nvl(row.get("E0_UNIT_LEVEL"));
            unitFlag = StringUtils.nvl(row.get("E0_UNIT_FLAG"));
            if (depCd.equalsIgnoreCase(unitCd)) {// 表示只有第一階
                unitCd1 = unitCd;
                unitNm1 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{unitCd});
                unitFullNm = pgetData(sql, "E0_UNIT_NM", new Object[]{unitCd});
            } else if (branchCd.length() == 0) {// 表示只有第二階
                unitCd1 = depCd;
                unitNm1 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{depCd});
                unitCd2 = unitCd;
                unitNm2 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{unitCd});
                unitFullNm = pgetData(sql, "E0_UNIT_NM", new Object[]{unitCd});
            } else {// 三階都有
                unitCd1 = depCd;
                unitNm1 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{depCd});
                unitCd2 = branchCd;
                unitNm2 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{branchCd});
                unitCd3 = unitCd;
                unitNm3 = pgetData(sql, "E0_UNIT_S_NM", new Object[]{unitCd});
                unitFullNm = pgetData(sql, "E0_UNIT_NM", new Object[]{unitCd});
            }
            unitName = StringUtils.nvl(row.get("E0_UNIT_S_NM"));//單位名稱
        }
        user.setUnitName(unitName);//給自訂單位登入用
        user.setUnitCdFullName(unitFullNm); // new 塞單位完整全名
        user.setUnitCd1(unitCd1);
        user.setUnitCd2(unitCd2);
        user.setUnitCd3(unitCd3);
        user.setUnitCd1Name(unitNm1);
        user.setUnitCd2Name(unitNm2);
        user.setUnitCd3Name(unitNm3);
        user.setUnitFlag(unitFlag);
        user.setUnitLevel(unitLevel);

        //取得user的citycode
        String UserCity = getUserCityCode(unitCd1);
        user.setUserCity(UserCity);
//	log.debug("userId:"+user.getUserId()+"; userName:"+user.getUserName());
//	log.debug("unitCd:"+unitCd+"; unitCd1:"+unitCd1+"; unitCd2:"+unitCd2+"; unitCd3:"+unitCd3);
//	log.debug("unitName:"+unitName+"; unitNm1:"+unitNm1+"; unitNm2:"+unitNm2+"; unitNm3:"+unitNm3);
//	log.debug("unitLevel:"+unitLevel+"; unitFlag:"+unitFlag); 
    }

    /**
     * 設定User的權限層級
     *
     * @param user UserVO
     */
    public void setUserRolePermission(User user) {
        // 查取出三階單位DB
        String sql = "SELECT MAX(CJ_PERMISSION) AS CJ_PERMISSION FROM CJDT_ROLE WHERE CJ_ENABLED='Y' AND "
                + "CJ_ROLE_ID IN ('" + user.getOwnRoleString().replaceAll(",", "','") + "')";

        String rolePermission = getData(sql, "CJ_PERMISSION");
        //log.debug("AuthDao rolePermission=" + rolePermission);

        user.setRolePermission(rolePermission);

    }

    /**
     * 設定User的是否有入山系統使用權限
     *
     * @param User UserVO
     */
    public void setIsAllowUse(User user) {
        // 查取出三階單位DB
        String sql = "SELECT CJ_IS_SHOW FROM CJDT_E0_NPAUNIT WHERE CJ_UNIT_CD=? ";

        String CJ_IS_SHOW = pgetData(sql, "CJ_IS_SHOW", new Object[]{user.getUnitCd()});

        user.setIsAllowUse("1".equals(CJ_IS_SHOW) ? true : false);

    }

    public String getUserCityCode(String unit) {
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
