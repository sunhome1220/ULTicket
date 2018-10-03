package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import com.syscom.db.DBUtil;
import util.User;
import org.json.JSONObject;
import org.apache.log4j.Logger;

public class SqlUtil {
private static Logger log = Logger.getLogger(SqlUtil.class);
	/**
	 * SQL指令型別
	 * I:新增
	 * U:更新
	 * D:刪除
	 */
	public enum TYPE {
		I,U,D
	}
	
	/**
	 * 取得異動SQL
	 * @param tableName		更新資料表名稱	
	 * @param updateCols	更新欄位
	 * @param filterCols	更新條件欄位(Where Condition)
	 * @return				更新SQL指令
	 */
	public static String getUpdateSQL(String tableName,
			List<String> updateCols, List<String> filterCols) {
		StringBuilder sql = new StringBuilder("UPDATE ").append(tableName)
				.append(" SET ");
		sql.append(getColsStringBuilder(updateCols, "%s = ?", ", "));
		sql.append(" WHERE ");
		sql.append(getColsStringBuilder(filterCols, "%s = ?", " AND "));
		return sql.toString();
	}

	/**
	 * 取得新增資料列SQL
	 * @param tableName		新增資料表名稱
	 * @param insertCols	新增欄位
	 * @return				新增SQL指令
	 */
	public static String getInsertSQL(String tableName, List<String> insertCols) {
		StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName)
				.append(" ( ");
		sql.append(getColsStringBuilder(insertCols, "%s", ", "));
		sql.append(" ) VALUES ( ");
		sql.append(getColsStringBuilder(insertCols, "?", ", "));
		sql.append(" ) ");
		return sql.toString();
	}

	/**
	 * 取得刪除資料列SQL
	 * @param tableName		刪除資料表名稱
	 * @param filterCols	刪除條件欄位(Where Condition)
	 * @return				刪除SQL指令
	 */
	public static String getDeleteSQL(String tableName, List<String> filterCols) {
		StringBuilder sql = new StringBuilder("DELETE ").append(tableName)
				.append(" WHERE ");
		sql.append(getColsStringBuilder(filterCols, "%s = ?", " AND "));
		return sql.toString();
	}

	/**
	 * 執行新增資料
	 * @param tableName		新增資料表名稱
	 * @param insertCols	新增欄位鍵值對
	 * @return				新增列數
	 */
	public static int execInsertSQL(String tableName,
			LinkedHashMap<String, Object> insertCols) {
		return DBUtil.getInstance().pexecuteUpdate(
				getInsertSQL(tableName,
						new ArrayList<String>(insertCols.keySet())),
						new ArrayList<Object>(insertCols.values()).toArray());
	}
	
	/**
	 * 執行更新資料
	 * @param tableName		更新資料表名稱
	 * @param updateCols	更新欄位鍵值對
	 * @param filterCols	更新欄位鍵值對
	 * @return				更新列數
	 */
	public static int execUpdateSQL(String tableName,
			LinkedHashMap<String, Object> updateCols,
			LinkedHashMap<String, Object> filterCols) {
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.addAll(updateCols.values());
		parms.addAll(filterCols.values());
		return DBUtil.getInstance().pexecuteUpdate(
				getUpdateSQL(tableName,
						new ArrayList<String>(updateCols.keySet()),
						new ArrayList<String>(filterCols.keySet())),
				parms.toArray());
	}
	
	/**
	 * 執行刪除資料
	 * @param tableName		刪除資料表名稱
	 * @param filterCols	刪除欄位鍵值對
	 * @return				刪除列數
	 */
	public static int execDeleteSQL(String tableName,
			LinkedHashMap<String, Object> filterCols) {
		return DBUtil.getInstance().pexecuteUpdate(
				getDeleteSQL(tableName,
						new ArrayList<String>(filterCols.keySet())),
						new ArrayList<Object>(filterCols.values()).toArray());
	}

	/**
	 * 批次執行Update SQL指令
	 * 僅能執行update指令
	 * @param tableName		更新資料表名稱陣列	
	 * @param updateCols	更新欄位陣列
	 * @param filterCols	更新條件欄位陣列
	 * @return				各更新指令執行結果陣列
	 */
	public static int[] execUpdateBatch(String[] tableName,
			LinkedHashMap<String, Object>[] updateCols,
			LinkedHashMap<String, Object>[] filterCols) {
		
		ArrayList<String> sqls = new ArrayList<String>();
		Object[][] batchParms = new Object[tableName.length][];
		
		for(int i = 0; i < tableName.length; i++) {
			
			ArrayList<Object> parms = new ArrayList<Object>();
			
			parms.addAll(updateCols[i].values());
			parms.addAll(filterCols[i].values());
			
			sqls.add(
					getUpdateSQL(tableName[i],
								new ArrayList<String>(updateCols[i].keySet()),
								new ArrayList<String>(filterCols[i].keySet())));
			batchParms[i] = parms.toArray();
		}
		
		return DBUtil.getInstance().pexecuteBatch(sqls.toArray(), batchParms);
	}
	
	/**
	 * 批次執行Delete SQL指令
	 * 僅能執行delete指令
	 * @param tableName		刪除資料表名稱陣列	
	 * @param updateCols	刪除欄位陣列
	 * @param filterCols	刪除條件欄位陣列
	 * @return				各刪除指令執行結果陣列
	 */
	public static int[] execDeleteBatch(String[] tableName,
			LinkedHashMap<String, Object>[] filterCols) {
		
		ArrayList<String> sqls = new ArrayList<String>();
		Object[][] batchParms = new Object[tableName.length][];
		
		for(int i = 0; i < tableName.length; i++) {
			
			ArrayList<Object> parms = new ArrayList<Object>(filterCols[i].values());
			
			sqls.add(
					getDeleteSQL(tableName[i],
								new ArrayList<String>(filterCols[i].keySet())));
			batchParms[i] = parms.toArray();
		}
		
		return DBUtil.getInstance().pexecuteBatch(sqls.toArray(), batchParms);
	}
	
	/**
	 * 批次執行Insert SQL指令
	 * 僅能執行insert指令
	 * @param tableName		新增資料表名稱陣列	
	 * @param updateCols	新增欄位陣列
	 * @param filterCols	新增條件欄位陣列
	 * @return				各新增指令執行結果陣列
	 */
	public static int[] execInsertBatch(String[] tableName,
			LinkedHashMap<String, Object>[] insertCols) {
		
		ArrayList<String> sqls = new ArrayList<String>();
		Object[][] batchParms = new Object[tableName.length][];
		
		for(int i = 0; i < tableName.length; i++) {
			
			ArrayList<Object> parms = new ArrayList<Object>(insertCols[i].values());
			
			sqls.add(
					getInsertSQL(tableName[i],
								new ArrayList<String>(insertCols[i].keySet())));
			batchParms[i] = parms.toArray();
		}
		
		return DBUtil.getInstance().pexecuteBatch(sqls.toArray(), batchParms);
	}
	
	/**
	 * 批次執行SQL指令
	 * @param sqlTypes		指令型別，新增(I)、更新(U)、刪除(D)
	 * @param tableName		資料表名稱
	 * @param updateCols	更新欄位。新增指令所有欄位皆須置於此容器
	 * @param filterCols	條件欄位
	 * @return				各指令執行結果陣列
	 */
	public static int[] execBatch(TYPE[] sqlTypes, String[] tableName,
			LinkedHashMap<String, Object>[] updateCols,
			LinkedHashMap<String, Object>[] filterCols) {
		
		ArrayList<String> sqls = new ArrayList<String>();
		Object[][] batchParms = new Object[sqlTypes.length][];
		
		for(int i = 0; i < sqlTypes.length; i++) {
			ArrayList<Object> parms = new ArrayList<Object>();	
			switch(sqlTypes[i]) {
			case I:
				sqls.add(getInsertSQL(tableName[i], new ArrayList<String>(updateCols[i].keySet())));
				parms.addAll(updateCols[i].values());
				break;
			case U:
				sqls.add(getUpdateSQL(tableName[i], new ArrayList<String>(updateCols[i].keySet()), new ArrayList<String>(filterCols[i].keySet())));
				parms.addAll(updateCols[i].values());
				parms.addAll(filterCols[i].values());
				break;
			case D:
				sqls.add(getDeleteSQL(tableName[i], new ArrayList<String>(filterCols[i].keySet())));
				parms.addAll(filterCols[i].values());
				break;
			default:
				break;
			}
			batchParms[i] = parms.toArray();
		}
		return DBUtil.getInstance().pexecuteBatch(sqls.toArray(), batchParms);
	}
	
	/**
	 * 串接SQL連續條件、連續欄位
	 * 結尾自動去掉串接符號
	 * @param extractColumns	欄位名稱
	 * @param pattern			串接樣式，EX:"%s = ?"   ==>   "colA = ?"
	 * @param joinnor			串接符號
	 * @return					串接欄位字串
	 */
	public static StringBuilder getColsStringBuilder(
			List<String> extractColumns, String pattern, String joinnor) {
		StringBuilder cols = new StringBuilder();
		for (String col : extractColumns) {
			cols.append(String.format(pattern, col)).append(joinnor)
					.append(" ");
		}
		cols.delete(cols.lastIndexOf(joinnor), cols.length() - 1);
		return cols;
	}
        /**
     * 取得更新時間及人員等相關資訊
     * 
     * @param jObj 需擴充之JSONObject 
     * @param colType 需擴充之欄位類別
     * 
     * @return 已擴充完畢之JSONObject
     */
	public static JSONObject getStaticColumn(JSONObject jObj, String dbType){
        try{
        	Date current = new Date();
        	User voUser = new User();
        	voUser = (User) jObj.get("userVO");
        	
        	jObj.put(dbType + "_DTTM", new SimpleDateFormat("yyyyMMddHHmmss").format(current));
			jObj.put(dbType + "_ID", voUser.getUserId());
			jObj.put(dbType + "_NM", voUser.getUserName());
			jObj.put(dbType + "_UNIT_CD", voUser.getUnitCd());
			jObj.put(dbType + "_UNIT_NM", voUser.getUnitName());
            
        } catch(Exception e){
        	log.error(ExceptionUtil.toString(e));
        }
        
        return jObj;
    }

}
