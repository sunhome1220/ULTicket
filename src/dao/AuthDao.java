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
}
