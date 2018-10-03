package base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
//import com.syscom.db.DBUtil;
import util.DBUtil;
import util.ExceptionUtil;
import util.MessageUtil;
import util.StringUtil;

public class CJBaseDao {
    protected static Logger log = Logger.getLogger(CJBaseDao.class);
    /**
     * LIMIT_RECORD_CROWD:聚眾案件類查詢限制筆數 ，LIMIT_RECORD_NON_CROWD:刑事案件類查詢限制筆數，
     * LIMIT_RECORD_110CASE:110報案類查詢限制筆數，LIMIT_RECORD_NORMAL:其他案件類查詢限制筆數。
     * @author Barry
     *
     */
    public enum QueryLimitRecord{
	/**
	 * 聚眾案件類查詢限制筆數
	 */
	LIMIT_RECORD_CROWD("LIMIT_RECORD_CROWD"), 
	/**
	 * 刑事案件類查詢限制筆數
	 */
	LIMIT_RECORD_NON_CROWD("LIMIT_RECORD_NON_CROWD"), 
	/**
	 * 110報案類查詢限制筆數
	 */
	LIMIT_RECORD_110CASE("LIMIT_RECORD_110CASE"),
	/**
	 * 其他案件類查詢限制筆數
	 */
	LIMIT_RECORD_NORMAL("LIMIT_RECORD_NORMAL");
	private final String name;
	QueryLimitRecord(String name) {
	    this.name = name;
	}
	public String getValue() {
	    return name;
	}
    }

    public enum SqlType{
	/**
	 * 新增
	 */
	INSERT("INSERT"),
	/**
	 * 修改
	 */
	UPDATE("UPDATE"),
	/**
	 * 刪除
	 */
	DELETE("DELETE"),
	/**
	 * 查詢
	 */
	SELECT("SELECT");
	
	private final String name;
	SqlType(String name) {
	    this.name = name;
	}
	public String getValue() {
	    return name;
	}
    }
    
    /**
     * 執行查詢SQL，並且回傳使用ArrayList<HashMap>的查詢結果
     * 
     * @param sql
     *            欲查詢的SQL
     * @return 查詢結果ArrayList<HashMap>
     */
    @SuppressWarnings("rawtypes")
    protected ArrayList<HashMap> executeQuery(String sql) {
	return DBUtil.getInstance().executeQuery(sql);
    }
    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    protected ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs) {
        //log.info("SQL = " + sql);
        log.debug("SQL =" + printSql(sql, objs));
	return DBUtil.getInstance().pexecuteQuery(sql, objs);
    }
        
    /**
     * 提供與pexecuteQuery相同的功能，只是會自動檢查是否超過限制筆數，若超過會在前端網頁顯示提示訊息。
     * @param sql
     * @param objs
     * @param returnJasonObj
     * @param limitRec LIMIT_RECORD_CROWD:聚眾案件類查詢限制筆數 ，LIMIT_RECORD_NON_CROWD:刑事案件類查詢限制筆數，LIMIT_RECORD_110CASE:110報案類查詢限制筆數，LIMIT_RECORD_NORMAL:其他案件類查詢限制筆數。
     * @return
     */
    protected ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs, JSONObject returnJasonObj, QueryLimitRecord limitRec) {
	int limit = getQueryLimitRecord(limitRec);
	return pexecuteQuery(sql, objs, returnJasonObj, limit);
    }
    
    /**
     * 提供與pexecuteQuery相同的功能，只是會自動檢查是否超過限制筆數，若超過會在前端網頁顯示提示訊息。
     * @param sql
     * @param objs
     * @param returnJasonObj
     * @param limit 限制筆數
     * @return
     */
    protected ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs, JSONObject returnJasonObj, int limit) {
	StringBuilder newSql = new StringBuilder();
	String selectSql = sql;
	String orderBySql = "";
	Pattern pattern = Pattern.compile("(?i)order.+by");//找出不區分大小寫的order by
	Matcher matcher = pattern.matcher(sql);
	if(matcher.find()){//去除order by 
	    selectSql = sql.substring(0, matcher.start());
	    orderBySql = sql.substring(matcher.start());
	}
	newSql.append("SELECT TOP "+(limit+1)+" * FROM").append("\n").append("(");
	newSql.append(selectSql);
	newSql.append("\n").append(") TB");
	newSql.append("\n").append(orderBySql);
	
	ArrayList<HashMap> rows = DBUtil.getInstance().pexecuteQuery(newSql.toString(), objs);
	if(rows.size()>limit){
	    this.setOverRecordMsg(returnJasonObj, "超過查詢限制筆數:"+limit+"筆，請縮小查詢範圍！");
	    this.setDebugMsg(returnJasonObj, "總筆數為:"+rows.size()+"，超過查詢限制筆數:"+limit+"。");
	    for (int i = rows.size(); i > limit; i--) {
		rows.remove(i-1);
	    }
	}
	return rows;
    }
    
    /**
     * 從DB查出該案類的限制筆數。
     * @param limitRec
     * @return 限制筆數
     */
    protected int getQueryLimitRecord(QueryLimitRecord limitRec){
	int count = 0;
	String value = pgetData("SELECT * FROM LEDT_PARAMS WHERE LE_PARAM_KEY=?", "LE_PARAM_VALUE", new String[]{limitRec.toString()});
	try {
	    count = Integer.parseInt(value);
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
	return count;
    }
    
    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement,並且回傳ResultSet的離線物件CachedRowSet
     * 目前主要是提供給報表的DataTable使用
     * @param sql
     * @param objs
     * @return CachedRowSet ResultSet的離線物件
     */
    protected CachedRowSet pexecuteQueryRowSet(String sql, Object[] objs) throws Exception {
	return DBUtil.getInstance().pexecuteQueryRowSet(sql, objs);
    }
    
    //移除筆數限制
    protected CachedRowSet pexecuteQueryRowSet_60000(String sql, Object[] objs) throws Exception {
	return DBUtil.getInstance().pexecuteQueryRowSet_60000(sql, objs);
    }
    //移除筆數限制
    protected CachedRowSet pexecuteQueryRowSet_no_limit(String sql, Object[] objs) throws Exception {
	return DBUtil.getInstance().pexecuteQueryRowSet_no_limit(sql, objs);
    }

    /**
     * 取得所查詢SQL的第一筆列，並且取出colName所代表的值
     * 
     * @param sql
     * @param colName
     * @return
     */
    protected String getData(String sql, String colName) {
	return DBUtil.getInstance().getData(sql, colName);
    }
    /**
     * 與getData功能相同，只是改用prepareStatement
     * @param sql
     * @param colName
     * @param objs
     * @return
     */
    public String pgetData(String sql, String colName, Object[] objs) {
	return DBUtil.getInstance().pgetData(sql, colName,objs);
    }
    /**
     * 取得所查詢SQL的第一列資料
     * 
     * @param sql
     * @return sql
     */
    protected HashMap<String, String> getFirstRow(String sql) {
	return DBUtil.getInstance().getFirstRow(sql);
    }
    /**
     * 與getFirstRow功能相同，只是改用prepareStatement
     * @param sql
     * @param objs
     * @return
     */
    public HashMap<String, String> pgetFirstRow(String sql, Object[] objs) {
	return DBUtil.getInstance().pgetFirstRow(sql,objs);
    }
    /**
     * 取得所查詢SQL的第一列的colName的值
     * 
     * @param colName
     *            欄位名稱
     * @param sql
     * @return
     */
    protected String getFirstRowData(String colName, String sql) {
	return DBUtil.getInstance().getFirstRowData(colName, sql);
    }
    /**
     * 與getFirstRowData功能相同，只是改用prepareStatement
     * @param sql
     * @param objs
     * @return
     */
    public String pgetFirstRowData(String colName, String sql, Object[] objs) {
	return DBUtil.getInstance().pgetFirstRowData(colName, sql,objs);
    }
    /**
     * 判斷傳入的sql是否有資料存在
     * 
     * @param sql
     * @return
     */
    protected boolean isExist(String sql) {
	return DBUtil.getInstance().isExist(sql);
    }
    /**
     * 與isExist功能相同，只是改用prepareStatement
     * @param sql
     * @param objs
     * @return
     */
    public boolean pisExist(String sql, Object[] objs) {
	return DBUtil.getInstance().pisExist(sql,objs);
    }
    /**
     * 取的傳入SQL的總筆數
     * 
     * @param sql
     * @return 總筆數
     */
    protected int getCount(String sql) {
	return DBUtil.getInstance().getCount(sql);
    }
    /**
     * 與getCount相同功能，,只是換成prepareStatement
     * @param sql
     * @param objs
     * @return
     */
    protected int pgetCount(String sql, Object[] objs) {
	return DBUtil.getInstance().pgetCount(sql,objs);
    }
    /**
     * 執行單一SQL的update與insert條件
     * 
     * @param sql
     *            欲執行的SQL
     * @return 此SQL更新的筆數
     */
    protected int executeUpdate(String sql) {
	return DBUtil.getInstance().executeUpdate(sql);
    }
    
    /**
     * 與executeUpdate相同功能，只是換成prepareStatement
     * 
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    protected int pexecuteUpdate(String sql, Object[] objs) {
	int count = 0;
	try{
	    String sqlUpper = StringUtil.nvl(sql.toUpperCase());
	    SqlType sqlType;
	    if(sqlUpper.startsWith("UPDATE")){
		sqlType = SqlType.UPDATE;
	    }else if(sqlUpper.startsWith("DELETE")){
		sqlType = SqlType.DELETE;
	    }else if(sqlUpper.startsWith("INSERT")){
		sqlType = SqlType.INSERT;
	    }
	    count = DBUtil.getInstance().pexecuteUpdate(sql,objs);
	}catch(Exception e){
	    handleCommonException(e);
	    //ExceptionUtil.toString(e);
	}
	
	return count;
    }
    
    /**
     * 處理共通的Exception訊息
     * @param e
     */
    private void handleCommonException(Exception e){
	String msg = e.getMessage();
	JSONObject json = new JSONObject();
	if(msg.indexOf("Attempt to insert duplicate key")>-1){
	    setErrorMsg(json, "insert的key重複");
	}
	
    }
    
    /**
     * 與pexecuteUpdate相同功能，只是多回傳新增table的key
     * @param sql
     * @param objs
     * @return 0-是更新筆數,1-是新增時的key值
     */
    protected long[] pexecuteUpdateReturnKey(String sql, Object[] objs) {
	return com.syscom.db.DBUtil.getInstance().pexecuteUpdateReturnKey(sql,objs);
    }

    /**
     * 執行多個SQL的update與insert條件
     * 
     * @param sqls
     *            欲執行的SQL陣列
     * @return 此SQL陣列更新的筆數
     */
    protected int[] executeBatch(Object[] sqls) {
	return DBUtil.getInstance().executeBatch(sqls);
    }
    
    /**
     * 與executeBatch相同功能，只是換成prepareStatement
     * @param sqls
     * @param objs[][] 
     * @return
     */
    public int[] pexecuteBatch(Object[] sqls, Object[][] objs) {
	return DBUtil.getInstance().pexecuteBatch(sqls,objs);
    }
    
    /**
     * 取得標準訊息文字，訊息內容定義在messages.properties。請改用MsgString.XXXX_XXX。
     * @param key 
     * @return
     */
    public static String getMsg(String key){
	return MessageUtil.getString(key);
    }
    
    /**
     * 因為sybase的jdbc無法印出prepare statement的參數內容，所以只好自己印出來嚕。
     * @param sql
     * @param objs
     * @return
     */
    public static String printSql(String sql, Object[] objs) {
	return DBUtil.getInstance().printSql(sql, objs);
    }
    
    /**
     * 將資料庫的查詢結果轉成JSONArray
     * 
     * @param list
     * @return
     */
    public JSONArray arrayList2JsonArray(ArrayList<HashMap> list) {
	return DBUtil.arrayList2JsonArray(list);
    }
    
    /**
     * 將資料庫的查詢結果轉成JSONArray，使用傳入的JSONArray。
     * @param jArray
     * @param list
     * @return
     */
    public JSONArray arrayList2JsonArray(JSONArray jArray, ArrayList<HashMap> list) {
	return DBUtil.arrayList2JsonArray(jArray, list);
    }    

    /**
     * 設定前端網頁的訊息內容
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setReturnMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_SUCCESS_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的錯誤訊息Key，key=errorMsg。像是一般的新增,修改,刪除失敗的訊息。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setErrorMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_ERROR_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的警告訊息Key，key=warnMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setWarnMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_WARN_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的一般訊息Key，key=infoMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setInfoMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_INFO_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的訊息Key，key=overRecordMsg。表示超過查詢筆數。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setOverRecordMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_OVER_RECORD_KEY, msg);
    }
    
    /**
     * 後端程式要送到前端console顯示的訊息Key，key=debugMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setDebugMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AjaxBaseServlet.AJAX_RES_MSG_DEBUG_KEY, msg);
    }
    /**
         * 與executeUpdate相同功能，,只是換成prepareStatement
         * 
         * @param sql
         * @param objs 目前實作String;Integer;Timestamp
         * @return
         */
        public int getIdentityAfterInsert(String sql, Object[] objs) {
            return DBUtil.getInstance().getIdentityAfterInsert(sql,objs);
        }
        
}
