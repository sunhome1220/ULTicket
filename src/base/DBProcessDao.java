package base;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import util.DBUtil;
import util.MessageUtil;

public class DBProcessDao {
    
    protected static Logger log = Logger.getLogger(DBProcessDao.class);

    /**********************************************
     * 
     * Barry
     * 
     **********************************************/
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
     * 執行查詢SQL，並且回傳使用ArrayList<HashMap>的查詢結果
     * 
     * @param sql
     *            欲查詢的SQL
     * @return 查詢結果ArrayList<HashMap>
     */
    @SuppressWarnings("rawtypes")
    protected ArrayList<HashMap> executeQueryByDB01(String sql) {
	return DBUtil.getInstance().executeQueryByDB01(sql);
    }
    
    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    protected ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs) {
	return DBUtil.getInstance().pexecuteQuery(sql, objs);
    }
    
    protected ArrayList<HashMap> pexecuteQueryByLabel(String sql, Object[] objs) {
	return DBUtil.getInstance().pexecuteQueryByLabel(sql, objs);
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
     * 與executeUpdate相同功能，,只是換成prepareStatement
     * 
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    protected int pexecuteUpdate(String sql, Object[] objs) {
	return DBUtil.getInstance().pexecuteUpdate(sql,objs);
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
     * 與executeBatch相同功能，,只是換成prepareStatement
     * @param sqls
     * @param objs[][] 
     * @return
     */
    public int[] pexecuteBatch(Object[] sqls, Object[][] objs) {
	return DBUtil.getInstance().pexecuteBatch(sqls,objs);
    }
    
    /**
     * 新增一筆資料並回傳Identity值
     */
    public int getIdentityAfterInsert(String sql, Object[] objs) {
    	return DBUtil.getInstance().getIdentityAfterInsert(sql, objs);
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
     * 將資料庫的查詢結果轉成JSONArray
     * 
     * @param list
     * @return
     */
    public JSONArray arrayList2JsonArray(ArrayList<HashMap> list) {
	return DBUtil.arrayList2JsonArray(list);
    }
    
    public JSONArray arrayList2JsonArrayByKeyValue(HashMap<String,ArrayList<String>> list){
        return DBUtil.arrayList2JsonArrayByKeyValue(list);
    }
    
    public ResultSet executeQuerySet(String sql,Object[] objs){
        return DBUtil.getInstance().excuteQuerySet(sql,objs);
    }
}
