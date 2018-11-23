package util;

import java.io.Serializable;

public class User implements Serializable{

    /**
     * @return the role
     */
    public int getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * @return the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * @return the loginStatus
     */
    public int getLoginStatus() {
        return loginStatus;
    }

    /**
     * @param loginStatus the loginStatus to set
     */
    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
    private static final long serialVersionUID = 1L;
    private String userId;
    private String team;
    private int role;
    private String unitCd1;
    private String unitCd2;
    private String unitCd3;
    private String unitCd1Name;
    private String unitCd2Name;
    private String unitCd3Name;
    private String unitCdFullName;
    private String password;
    private String userName;
    private String userCity;
    private String lastLogin;
    private String userState;
    private String idNo;
    private String email;
    private String policePhone;
    private String autophoneArea;
    private String autophone;
    private String autophoneExtension;
    private String authorizedRole;
    private String areaCd;
    private String areaToGis;
    private String areaToGoogle;
    private String areaToTpeProxy;
    private String areaToTaoProxy;
    private String areaToNtpProxy;
    private String areaToKelProxy;
    private String areaToHscProxy;
    private String areaToTccProxy;
    private String areaToChcProxy;
    private String areaToNtcProxy;
    private String areaToTncProxy;
    private String areaToKaoProxy;
    private String createUser;
    private String createDate;
    private String modifyUser;
    private String modifyDate;
    private int loginStatus;//0(未登入)/1(已登入)
    
    /**
     * 使用者的IP
     */
    private String userIp = "";//預設空白這樣點選訓練版才不會錯誤
    /**
     * 使用者最後一階的單位代碼
     */
    private String unitCd;
    /**
     * 使用者最後一階的單位名稱
     */
    private String unitName;
    /**
     * 所擁有的權限
     */
    private String[] ownFun;
    /**
     * 所擁有的角色
     */
    private String[] ownRole;
    /**
     * 本user所代表的單位，過濾資料用。如:AW000 臺北市政府警察局
     */
    private String scopeUnitCd = "";
    /**
     * 本user所代表的層次，過濾資料用。如:LE_UNIT_CD1
     */
    private String scopeUnitLevel = "";
    /**
     * 本user所代表的層次，過濾資料用。如:1
     */
    private String scopeUnitLevelNum = "";
    /**
     * F003	治安狀況摘要表輸入
     */
    private boolean isPublicSecurityAbstractKeyin = false;
    /**
     * F004	重大案件審核
     */
    private boolean isMajorCaseVerify = false;
    /**
     * F005	重大案件結案
     */
    private boolean isMajorCaseClose = false;
    /**
     * F006	重大刑案審核
     */
    private boolean isMajorCriminalCaseVerify = false;
    /**
     * F007	重大刑案結案
     */
    private boolean isMajorCriminalCaseClose = false;
    /**
     * F008	案件清單可刪除案件
     */
    private boolean isCaseDel = false;

    /**
     * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
     */
    private String unitLevel = "";
    /**
     * E0DT_NPAUNIT的E0_UNIT_FLAG單位旗標
     */
    private String unitFlag = "";
    /**
     * user登入系統時間
     */
    private String loginTime = "";
/**
	 * 使用者的權限 1：只能看自己 2：能看自己和下屬 3：三層全可被控制
	 */
	private boolean isAllowUse;
        
/**
    * 使用者的權限 1：只能看自己 2：能看自己和下屬 3：三層全可被控制
    */
    private String rolePermission;      
        
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getUnitCd1() {
	return unitCd1;
    }

    public void setUnitCd1(String unitCd1) {
	this.unitCd1 = unitCd1;
    }

    public String getUnitCd2() {
	return unitCd2;
    }

    public void setUnitCd2(String unitCd2) {
	this.unitCd2 = unitCd2;
    }

    public String getUnitCd3() {
	return unitCd3;
    }

    public void setUnitCd3(String unitCd3) {
	this.unitCd3 = unitCd3;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getUserName() {
	return userName;
    }
    
    public String getUserCity() {
	return userCity;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }
    
    public void setUserCity(String userCity) {
	this.userCity = userCity;
    }

    public String getLastLogin() {
	return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
	this.lastLogin = lastLogin;
    }

    public String getUserState() {
	return userState;
    }

    public void setUserState(String userState) {
	this.userState = userState;
    }

    public String getIdNo() {
	return idNo;
    }

    public void setIdNo(String idNo) {
	this.idNo = idNo;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPolicePhone() {
	return policePhone;
    }

    public void setPolicePhone(String policePhone) {
	this.policePhone = policePhone;
    }

    public String getAutophoneArea() {
	return autophoneArea;
    }

    public void setAutophoneArea(String autophoneArea) {
	this.autophoneArea = autophoneArea;
    }

    public String getAutophone() {
	return autophone;
    }

    public void setAutophone(String autophone) {
	this.autophone = autophone;
    }

    public String getAutophoneExtension() {
	return autophoneExtension;
    }

    public void setAutophoneExtension(String autophoneExtension) {
	this.autophoneExtension = autophoneExtension;
    }

    public String getAuthorizedRole() {
	return authorizedRole;
    }

    public void setAuthorizedRole(String authorizedRole) {
	this.authorizedRole = authorizedRole;
    }

    public String getUnitCd1Name() {
	return unitCd1Name;
    }
    
    public String getUnitCdFullName() {
	return unitCdFullName;
    }
    
    public void setUnitCdFullName(String unitCdFullName) {
	this.unitCdFullName = unitCdFullName;
    }
    
    public void setUnitCd1Name(String unitCd1Name) {
	this.unitCd1Name = unitCd1Name;
    }
    

    public String getUnitCd2Name() {
	return unitCd2Name;
    }

    public void setUnitCd2Name(String unitCd2Name) {
	this.unitCd2Name = unitCd2Name;
    }

    public String getUnitCd3Name() {
	return unitCd3Name;
    }

    public void setUnitCd3Name(String unitCd3Name) {
	this.unitCd3Name = unitCd3Name;
    }

    public String getAreaCd() {
	return areaCd;
    }

    public void setAreaCd(String areaCd) {
	this.areaCd = areaCd;
    }

    public String[] getOwnFun() {
        return ownFun;
    }

    public void setOwnFun(String[] ownFun) {
        this.ownFun = ownFun;
    }

    public String getAreaToGis() {
    	return areaToGis;
    }
    
    public void setAreaToGis(String areaToGis) {
    	this.areaToGis = areaToGis;
    }
    
    public String getAreaToGoogle() {
    	return areaToGoogle;
    }
    
    public void setAreaToGoogle(String areaToGoogle) {
    	this.areaToGoogle = areaToGoogle;
    }
    
    /*
    private String areaToTpeProxy;
    private String areaToTaoProxy;
    private String areaToNtpProxy;
    private String areaToKelProxy;
    private String areaToHscProxy;
    */
    public String getAreaToTpeProxy() {
    	return areaToTpeProxy;
    }
    
    public void setAreaToTpeProxy(String areaToTpeProxy) {
    	this.areaToTpeProxy = areaToTpeProxy;
    }
    
    public String getAreaToTaoProxy() {
    	return areaToTaoProxy;
    }
    
    public void setAreaToTaoProxy(String areaToTaoProxy) {
    	this.areaToTaoProxy = areaToTaoProxy;
    }
    
    public String getAreaToNtpProxy() {
    	return areaToNtpProxy;
    }
    
    public void setAreaToNtpProxy(String areaToNtpProxy) {
    	this.areaToNtpProxy = areaToNtpProxy;
    }
    
    public String getAreaToKelProxy() {
    	return areaToKelProxy;
    }
    
    public void setAreaToKelProxy(String areaToKelProxy) {
    	this.areaToKelProxy = areaToKelProxy;
    }
    
    public String getAreaToHscProxy() {
    	return areaToHscProxy;
    }
    
    public void setAreaToHscProxy(String areaToHscProxy) {
    	this.areaToHscProxy = areaToHscProxy;
    }

    /**
     * 取得角色
     * @return 
     */
    public String[] getOwnRole() {
        return ownRole;
    }
    
    /**
     * 取得權限最大的角色
     * 三四級毒品最大的為CJ000006，最小的為CJ000001
     * @return 
     */
    public String getMaxOwnRole() {
        String maxRole = "";
        for(String r:ownRole){
            if(r.compareTo(maxRole) > 0){
                maxRole = r;
            }
        }
        return maxRole;
    }

    public void setOwnRole(String[] ownRole) {
        this.ownRole = ownRole;
    }

    public String getUserIp() {
	return userIp;
    }

    public void setUserIp(String userIp) {
	this.userIp = userIp;
    }

    /**
     * @return the unitCd
     */
    public String getUnitCd() {
	return unitCd;
    }

    /**
     * @param unitCd the unitCd to set
     */
    public void setUnitCd(String unitCd) {
	this.unitCd = unitCd;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
	return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }
    
    public String getOwnRoleString() {
	String s = "";
	for(String e : ownRole){
	    s += ","+e;
	}
	s = s.substring(1);
        return s;
    }

    /**
     * @return 本user所代表的層次，過濾資料用。如:LE_UNIT_CD1
     */
    public String getScopeUnitLevel() {
	return scopeUnitLevel;
    }

    /**
     * @param 本user所代表的層次，過濾資料用。如:LE_UNIT_CD1
     */
    public void setScopeUnitLevel(String scopeUnitLevel) {
	this.scopeUnitLevel = scopeUnitLevel;
    }

    /**
     * @return 本user所代表的單位，過濾資料用。如:AW000 臺北市政府警察局
     */
    public String getScopeUnitCd() {
	return scopeUnitCd;
    }

    /**
     * @param 本user所代表的單位，過濾資料用。如:AW000 臺北市政府警察局
     */
    public void setScopeUnitCd(String scopeUnitCd) {
	this.scopeUnitCd = scopeUnitCd;
    }

    /**
     * @return 將scopeUnitLevel與scopeUnitCd組合成sql的條件，如:LE_UNIT_CD1='AW000'，若是空字串表示不做限制。
     */
    public String getScopeUnitSql() {
	String tmp = "";
	if(scopeUnitLevel.length()>0 && scopeUnitCd.length()>0){
	    tmp = scopeUnitLevel + "='" + scopeUnitCd + "'";
	}
	return tmp;
    }

    /**
     * @return F003	治安狀況摘要表輸入
     */
    public boolean isPublicSecurityAbstractKeyin() {
	return isPublicSecurityAbstractKeyin;
    }

    /**
     * @param F003	治安狀況摘要表輸入
     */
    public void setPublicSecurityAbstractKeyin(boolean isPublicSecurityAbstractKeyin) {
	this.isPublicSecurityAbstractKeyin = isPublicSecurityAbstractKeyin;
    }

    /**
     * @return F004	重大案件審核
     */
    public boolean isMajorCaseVerify() {
	return isMajorCaseVerify;
    }

    /**
     * @param F004	重大案件審核
     */
    public void setMajorCaseVerify(boolean isMajorCaseVerify) {
	this.isMajorCaseVerify = isMajorCaseVerify;
    }

    /**
     * @return F005	重大案件結案
     */
    public boolean isMajorCaseClose() {
	return isMajorCaseClose;
    }

    /**
     * @param F005	重大案件結案
     */
    public void setMajorCaseClose(boolean isMajorCaseClose) {
	this.isMajorCaseClose = isMajorCaseClose;
    }

    /**
     * @return F006	重大刑案審核
     */
    public boolean isMajorCriminalCaseVerify() {
	return isMajorCriminalCaseVerify;
    }

    /**
     * @param F006	重大刑案審核
     */
    public void setMajorCriminalCaseVerify(boolean isMajorCriminalCaseVerify) {
	this.isMajorCriminalCaseVerify = isMajorCriminalCaseVerify;
    }

    /**
     * @return F007	重大刑案結案
     */
    public boolean isMajorCriminalCaseClose() {
	return isMajorCriminalCaseClose;
    }

    /**
     * @param F007	重大刑案結案
     */
    public void setMajorCriminalCaseClose(boolean isMajorCriminalCaseClose) {
	this.isMajorCriminalCaseClose = isMajorCriminalCaseClose;
    }

    /**
     * @return F008	案件清單可刪除案件
     */
    public boolean isCaseDel() {
	return isCaseDel;
    }

    /**
     * @param F008	案件清單可刪除案件
     */
    public void setCaseDel(boolean isCaseDel) {
	this.isCaseDel = isCaseDel;
    }

    /**
     * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
     * @return the unitLevel
     */
    public String getUnitLevel() {
	return unitLevel;
    }

    /**
     * E0DT_NPAUNIT的E0_UNIT_LEVEL單位層級
     * @param unitLevel the unitLevel to set
     */
    public void setUnitLevel(String unitLevel) {
	this.unitLevel = unitLevel;
    }

    /**
     * E0DT_NPAUNIT的E0_UNIT_FLAG單位旗標
     * @return the unitFlag
     */
    public String getUnitFlag() {
	return unitFlag;
    }

    /**
     * E0DT_NPAUNIT的E0_UNIT_FLAG單位旗標
     * @param unitFlag the unitFlag to set
     */
    public void setUnitFlag(String unitFlag) {
	this.unitFlag = unitFlag;
    }
    

    /**
     * @return the areaToTccProxy
     */
    public String getAreaToTccProxy() {
	return areaToTccProxy;
    }

    /**
     * @param areaToTccProxy the areaToTccProxy to set
     */
    public void setAreaToTccProxy(String areaToTccProxy) {
	this.areaToTccProxy = areaToTccProxy;
    }

    /**
     * @return the areaToChcProxy
     */
    public String getAreaToChcProxy() {
	return areaToChcProxy;
    }

    /**
     * @param areaToChcProxy the areaToChcProxy to set
     */
    public void setAreaToChcProxy(String areaToChcProxy) {
	this.areaToChcProxy = areaToChcProxy;
    }

    /**
     * @return the areaToNtcProxy
     */
    public String getAreaToNtcProxy() {
	return areaToNtcProxy;
    }

    /**
     * @param areaToNtcProxy the areaToNtcProxy to set
     */
    public void setAreaToNtcProxy(String areaToNtcProxy) {
	this.areaToNtcProxy = areaToNtcProxy;
    }
    /**
     * @return the areaToTncProxy
     */
    public String getAreaToTncProxy() {
	return areaToTncProxy;
    }

    /**
     * @param areaToTncProxy the areaToTncProxy to set
     */
    public void setAreaToTncProxy(String areaToTncProxy) {
	this.areaToTncProxy = areaToTncProxy;
    }
    /**
     * @return the areaToKaoProxy
     */
    public String getAreaToKaoProxy() {
	return areaToKaoProxy;
    }

    /**
     * @param areaToKaoProxy the areaToKaoProxy to set
     */
    public void setAreaToKaoProxy(String areaToKaoProxy) {
	this.areaToKaoProxy = areaToKaoProxy;
    }
    /**
     * @return the loginTime
     */
    public String getLoginTime() {
	return loginTime;
    }

    /**
     * @param loginTime the loginTime to set
     */
    public void setLoginTime(String loginTime) {
	this.loginTime = loginTime;
    }

    /**
     * @return the scopeUnitLevelNum
     */
    public String getScopeUnitLevelNum() {
	return scopeUnitLevelNum;
    }

    /**
     * @param scopeUnitLevelNum the scopeUnitLevelNum to set
     */
    public void setScopeUnitLevelNum(String scopeUnitLevelNum) {
	this.scopeUnitLevelNum = scopeUnitLevelNum;
    }
    public void setIsAllowUse(boolean isAllowUse) {
		this.isAllowUse = isAllowUse;
	}

	public boolean getIsAllowUse() {
		return isAllowUse;
	}
        
        public String getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(String rolePermission) {
		this.rolePermission = rolePermission;
	}

}
