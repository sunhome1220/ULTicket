package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONArray;
//import org.json.JSONObject;

public class DBUtil {

    public static Logger log = Logger.getLogger(DBUtil.class);
    private static DBUtil instance = null;
    public int connectType = 1;//1-從DataSource,2-從設定檔
    final int WARN_COST_TIME = 300;//查詢超過此時間(ms)者要警告
    
    public static DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    public static DBUtil getInstance(int connectType) {
        instance = getInstance();
        instance.connectType = connectType;
        return instance;
    }

    public final static int MAX_QUERY_ROWS = 5;//最大查詢筆數default:200

    /**
     * 執行查詢SQL，並且回傳使用ArrayList<HashMap>的查詢結果
     *
     * @param sql 欲查詢的SQL
     * @return 查詢結果ArrayList<HashMap>
     */
    @SuppressWarnings("rawtypes")
    public ArrayList<HashMap> executeQuery(String sql) {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        //HashMap<String, Object> row;
        Connection conn = null;
        Statement stmt = null;
        long s = System.currentTimeMillis();	
        ResultSet rs = null;
        try {
            conn = getConnection();            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            long costTime = System.currentTimeMillis() - s;
            if(costTime > WARN_COST_TIME){
                log.warn("SQL=" + sql);                
                log.warn("[耗時]:" + costTime + " ms");
            }
            transformResultSet2ArrayList(list, rs);

        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return list;
    }
    

    /**
     * 執行查詢SQL，並且回傳使用ArrayList<HashMap>的查詢結果
     *
     * @param sql 欲查詢的SQL
     * @return 查詢結果ArrayList<HashMap>
     */
    @SuppressWarnings("rawtypes")
    public ArrayList<HashMap> executeQueryByDB01(String sql) {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        //HashMap<String, Object> row;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        log.info("SQL7=" + sql);
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            transformResultSet2ArrayList(list, rs);

        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 將查詢回來的ResultSet資料轉成ArrayList包HashMap<String, Object>
     *
     * @param list
     * @param rs
     * @throws SQLException
     */
    private void transformResultSet2ArrayList(ArrayList<HashMap> list, ResultSet rs) throws SQLException {
        LinkedHashMap<String, Object> row;
        ResultSetMetaData rsMetaData = rs.getMetaData();
        while (rs.next()) {
            row = new LinkedHashMap<String, Object>();
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                if (rsMetaData.getColumnType(i) == Types.BLOB) {// 針對binary的資料作處理
                    //row.put(rsMetaData.getColumnName(i), rs.getBinaryStream(i));//用這個也是可以,不過取出值時就要轉成InputStream而不是Blob
                    row.put(rsMetaData.getColumnName(i), rs.getBlob(i));
                } else {
                    //Candy modify 為了盡量使用sybase jdbc driver, 所以程式盡量寫成jtds與jdbc都能用
                    //若使用reMetaData.getCoumnName,在sql command內若欄位有設alias時, sybase jdbc driver只能得到未改alias前的欄位名而出錯
                    //所以要用reMetaData.getColumnLabel. 這個jtds和sybase jdbc driver都可以用
                    //row.put(rsMetaData.getColumnName(i), StringUtil.nvl(rs.getString(rsMetaData.getColumnName(i))));
                    row.put(rsMetaData.getColumnName(i), StringUtil.nvl(rs.getString(rsMetaData.getColumnLabel(i))));
                }
            }
            list.add(row);
        }
    }

    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement
     *
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return
     */
    public ArrayList<HashMap> pexecuteQuery(String sql, Object[] objs) {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        HashMap<String, String> row;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long s = System.currentTimeMillis();	
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);
            rs = pstmt.executeQuery();
            long costTime = System.currentTimeMillis() - s;            
            if(costTime > WARN_COST_TIME){
                log.warn("pSQL=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }
            transformResultSet2ArrayList(list, rs);

        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return list;
    }
    
    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement
     *
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return ResultSetMetaData
     */
    public ResultSetMetaData pexecuteQuery_ResultSet(String sql, Object[] objs) {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        HashMap<String, String> row;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsMetaData = null;
        long s = System.currentTimeMillis();	
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);
            rs = pstmt.executeQuery();
            long costTime = System.currentTimeMillis() - s;            
            if(costTime > WARN_COST_TIME){
                log.warn("pSQL=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }
            rsMetaData = rs.getMetaData();
        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return rsMetaData;
    }

    private void transformResultSet2ArrayListByLabel(ArrayList<HashMap> list, ResultSet rs) throws SQLException {
        LinkedHashMap<String, Object> row;
        ResultSetMetaData rsMetaData = rs.getMetaData();
        while (rs.next()) {
            row = new LinkedHashMap<String, Object>();
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                if (rsMetaData.getColumnType(i) == Types.BLOB) {
                    row.put(rsMetaData.getColumnLabel(i), rs.getBlob(i));
                } else {
                    row.put(rsMetaData.getColumnLabel(i), StringUtil.nvl(rs.getString(rsMetaData.getColumnLabel(i))));
                }
            }
            list.add(row);
        }
    }

//    public ArrayList<HashMap> pexecuteQueryByLabel(String sql, Object[] objs) {
//        ArrayList<HashMap> list = new ArrayList<HashMap>();
//        HashMap<String, String> row;
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        // log.info("SQL=" + sql);
//        try {
//            conn = getConnection();
//            pstmt = conn.prepareStatement(sql);
//            setPstmtParameter(objs, pstmt);
//            log.info("pSQL=" + printSql(sql, objs));
//            rs = pstmt.executeQuery();
//            transformResultSet2ArrayListByLabel(list, rs);
//
//        } catch (Exception e) {
//            // log.error(e.getMessage(), e.getCause());
//            log.error(ExceptionUtil.toString(e));
//            e.printStackTrace();
//        } finally {
//            try {
//                rs.close();
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                log.error(ExceptionUtil.toString(e));
//                // e.printStackTrace();
//            }
//        }
//        return list;
//    }

    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement,並且回傳ResultSet的離線物件CachedRowSet
     * 目前主要是提供給報表的DataTable使用
     *
     * @param sql
     * @param objs
     * @return CachedRowSet ResultSet的離線物件
     */
    public CachedRowSet pexecuteQueryRowSet(String sql, Object[] objs) throws Exception{
        CachedRowSet crs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long s = System.currentTimeMillis();	
        try {
            conn = getConnection();
            crs = new FixedCachedRowSetImpl();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);            
            pstmt.setMaxRows(MAX_QUERY_ROWS);            
            rs = pstmt.executeQuery();

            long costTime = System.currentTimeMillis() - s;
            
            if(costTime > WARN_COST_TIME){
                log.warn("pSQL2=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }else{
                log.debug("pSQL2=" + printSql(sql, objs));                
                log.debug("[耗時]:" + costTime + " ms");
            }
            crs.populate(rs);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
        }
        return crs;
    }
    public CachedRowSet pexecuteQueryRowSet_60000(String sql, Object[] objs) throws Exception{
        CachedRowSet crs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long s = System.currentTimeMillis();	
        // log.info("SQL=" + sql);
        try {
            conn = getConnection();
            crs = new FixedCachedRowSetImpl();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);            
            pstmt.setMaxRows(60001);
            rs = pstmt.executeQuery();

            long costTime = System.currentTimeMillis() - s;
            if(costTime > WARN_COST_TIME){
                log.warn("pSQL2=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }else{
                log.debug("pSQL2=" + printSql(sql, objs));                
                log.debug("[耗時]:" + costTime + " ms");
            }
//            if(rs != null && rs.next()){
//
//            }
            // This loads the data into a CachedResultSet. The connection can be closed after this line.
            crs.populate(rs);

        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return crs;
    }

    /**
     * 提供與executeQuery相同的功能,只是換成prepareStatement,並且回傳ResultSet的離線物件CachedRowSet, 並移除筆數限制
     * 目前主要是提供給報表的DataTable使用
     *
     * @param sql
     * @param objs
     * @return CachedRowSet ResultSet的離線物件
     */
     public CachedRowSet pexecuteQueryRowSet_no_limit(String sql, Object[] objs) throws Exception{
        CachedRowSet crs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long s = System.currentTimeMillis();	
         log.info("SQL=" + sql);
        try {
            conn = getConnection();
            crs = new FixedCachedRowSetImpl();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);            
            //pstmt.setMaxRows(MAX_QUERY_ROWS);
            rs = pstmt.executeQuery();

            long costTime = System.currentTimeMillis() - s;
            if(costTime > WARN_COST_TIME){
                log.warn("pSQL2=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }
//            if(rs != null && rs.next()){
//
//            }
            // This loads the data into a CachedResultSet. The connection can be closed after this line.
            crs.populate(rs);

        } catch (Exception e) {
            // log.error(e.getMessage(), e.getCause());
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return crs;
    }
//    public CachedRowSet pexecuteQueryRowSetBy110(String sql, Object[] objs) {
//        CachedRowSet crs = null;
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        // log.info("SQL=" + sql);
//        try {
//            conn = get110CaseConnection();
//            crs = new FixedCachedRowSetImpl();
//            pstmt = conn.prepareStatement(sql);
//            setPstmtParameter(objs, pstmt);
//            log.info("pSQL=" + printSql(sql, objs));
//            rs = pstmt.executeQuery();
//
////                if(rs != null && rs.next()){
//            //
////                }
//            // This loads the data into a CachedResultSet. The connection can be closed after this line.
//            crs.populate(rs);
//
//        } catch (Exception e) {
//            // log.error(e.getMessage(), e.getCause());
//            log.error(ExceptionUtil.toString(e));
//            e.printStackTrace();
//        } finally {
//            try {
//                rs.close();
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                log.error(ExceptionUtil.toString(e));
//                // e.printStackTrace();
//            }
//        }
//        return crs;
//    }

//    public CachedRowSet pexecuteQueryRowSetByDB01(String sql, Object[] objs) {
//        CachedRowSet crs = null;
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        // log.info("SQL=" + sql);
//        try {
//            conn = getDB01CaseConnection();
//            crs = new FixedCachedRowSetImpl();
//            pstmt = conn.prepareStatement(sql);
//            setPstmtParameter(objs, pstmt);
//            log.info("pSQL=" + printSql(sql, objs));
//            rs = pstmt.executeQuery();
//
////                if(rs != null && rs.next()){
//            //
////                }
//            // This loads the data into a CachedResultSet. The connection can be closed after this line.
//            crs.populate(rs);
//
//        } catch (Exception e) {
//            // log.error(e.getMessage(), e.getCause());
//            log.error(ExceptionUtil.toString(e));
//            e.printStackTrace();
//        } finally {
//            try {
//                rs.close();
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                log.error(ExceptionUtil.toString(e));
//                // e.printStackTrace();
//            }
//        }
//        return crs;
//    }

    /**
     * 取得所查詢SQL的第一筆列，並且取出colName所代表的值
     *
     * @param sql
     * @param colName
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getData(String sql, String colName) {
        String rtnString = null;
        ArrayList<HashMap> list = executeQuery(sql);
        if (list.size() > 0) {
            HashMap<String, String> row = list.get(0);
            rtnString = row.get(colName);
        }
        return rtnString;
    }

    /**
     * 與getData功能相同，只是改用prepareStatement
     *
     * @param sql
     * @param colName
     * @param objs
     * @return
     */
    public String pgetData(String sql, String colName, Object[] objs) {
        String rtnString = null;
        ArrayList<HashMap> list = pexecuteQuery(sql, objs);
        if (list.size() > 0) {
            HashMap<String, String> row = list.get(0);
            rtnString = row.get(colName);
        }
        return rtnString;
    }

    /**
     * 取得所查詢SQL的第一列資料
     *
     * @param sql
     * @return 若查無資料則回傳null
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public HashMap<String, String> getFirstRow(String sql) {
        HashMap<String, String> rtn = null;
        ArrayList<HashMap> list = executeQuery(sql);
        if (list.size() > 0) {
            rtn = list.get(0);
        }
        return rtn;
    }

    /**
     * 與getFirstRow功能相同，只是改用prepareStatement
     *
     * @param sql
     * @param objs
     * @return
     */
    public HashMap<String, String> pgetFirstRow(String sql, Object[] objs) {
        HashMap<String, String> rtn = null;
        ArrayList<HashMap> list = pexecuteQuery(sql, objs);
        if (list.size() > 0) {
            rtn = list.get(0);
        }
        return rtn;
    }

    /**
     * 取得所查詢SQL的第一列的colName的值
     *
     * @param colName 欄位名稱
     * @param sql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String getFirstRowData(String colName, String sql) {
        String rtn = null;
        ArrayList<HashMap> list = executeQuery(sql);
        if (list.size() > 0) {
            HashMap row = list.get(0);
            rtn = StringUtil.nvl(row.get(colName));
        }
        return rtn;
    }

    /**
     * 與getFirstRowData功能相同，只是改用prepareStatement
     *
     * @param sql
     * @param objs
     * @return
     */
    public String pgetFirstRowData(String colName, String sql, Object[] objs) {
        String rtn = null;
        ArrayList<HashMap> list = pexecuteQuery(sql, objs);
        if (list.size() > 0) {
            HashMap row = list.get(0);
            rtn = StringUtil.nvl(row.get(colName));
        }
        return rtn;
    }

    /**
     * 判斷傳入的sql是否有資料存在
     *
     * @param sql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean isExist(String sql) {
        boolean rtn = false;
        ArrayList<HashMap> list = executeQuery(sql);
        if (list.size() > 0) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * 與isExist功能相同，只是改用prepareStatement
     *
     * @param sql
     * @param objs
     * @return
     */
    public boolean pisExist(String sql, Object[] objs) {
        boolean rtn = false;
        ArrayList<HashMap> list = pexecuteQuery(sql, objs);
        if (list.size() > 0) {
            rtn = true;
        }
        return rtn;
    }

    /**
     * 取的傳入SQL的總筆數
     *
     * @param sql
     * @return 總筆數
     */
    public int getCount(String sql) {
        ArrayList<HashMap> list = executeQuery(sql);
        return list.size();
        /*
	 * Connection conn = null; Statement stmt = null; ResultSet rs = null;
	 * log.info("Count SQL=" + sql); try { conn = getConnection(); stmt =
	 * conn.createStatement(); rs = stmt.executeQuery(sql); while
	 * (rs.next()) { count++; }
	 * 
	 * } catch (Exception e) {
	 * log.error(ExceptionUtil.toString(e));
	 * e.printStackTrace(); } finally { try { rs.close(); if (stmt != null)
	 * stmt.close(); conn.close(); } catch (Exception e) {
	 * log.error(ExceptionUtil.toString(e)); //
	 * e.printStackTrace(); } } return count;
         */
    }

    /**
     * 與getCount功能相同，只是改用prepareStatement
     *
     * @param sql
     * @param objs
     * @return 總筆數
     */
    public int pgetCount(String sql, Object[] objs) {
        ArrayList<HashMap> list = pexecuteQuery(sql, objs);
        return list.size();
        /*
	 * Connection conn = null; Statement stmt = null; ResultSet rs = null;
	 * log.info("Count SQL=" + sql); try { conn = getConnection(); stmt =
	 * conn.createStatement(); rs = stmt.executeQuery(sql); while
	 * (rs.next()) { count++; }
	 * 
	 * } catch (Exception e) {
	 * log.error(ExceptionUtil.toString(e));
	 * e.printStackTrace(); } finally { try { rs.close(); if (stmt != null)
	 * stmt.close(); conn.close(); } catch (Exception e) {
	 * log.error(ExceptionUtil.toString(e)); //
	 * e.printStackTrace(); } } return count;
         */
    }

    /**
     * 執行單一SQL的update與insert條件
     *
     * @param sql 欲執行的SQL
     * @return 此SQL更新的筆數
     */
    public int executeUpdate(String sql) {
        int resultCount = 0;
        Connection conn = null;
        Statement stmt = null;
        log.info("SQL3=" + sql);
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            resultCount = stmt.executeUpdate(sql);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            // e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                e.printStackTrace();
            }
        }
        return resultCount;
    }

    /**
     * 與executeUpdate相同功能，,只是換成prepareStatement
     *
     * @param sql
     * @param objs 目前實作String;Integer;Timestamp
     * @return 資料更新的筆數
     */
    public int pexecuteUpdate(String sql, Object[] objs) {
        int resultCount = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        long s = System.currentTimeMillis();	
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);
            
            resultCount = pstmt.executeUpdate();
            long costTime = System.currentTimeMillis() - s;
            if(costTime > WARN_COST_TIME){
                log.warn("pSQLUpd=" + printSql(sql, objs));                
                log.warn("[耗時]:" + costTime + " ms");
            }else{
                //log.debug("pSQL4=" + printSql(sql, objs));//
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            // e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                e.printStackTrace();
            }
        }
        return resultCount;
    }

    /**
     * 設定PreparedStatement的參數
     *
     * @param objs
     * @param pstmt
     * @throws SQLException
     * @throws IOException
     */
    private void setPstmtParameter(Object[] objs, PreparedStatement pstmt) throws SQLException, IOException {
        if (objs != null) {
            int idx = 1;
            for (Object o : objs) {
                if (o instanceof String) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.VARCHAR);
                    } else {
                        pstmt.setString(idx++, o.toString());
                    }

                } else if (o instanceof Integer) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.INTEGER);
                    } else {
                        pstmt.setInt(idx++, (Integer) o);
                    }

                } else if (o instanceof Timestamp) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.TIMESTAMP);
                    } else {
                        pstmt.setTimestamp(idx++, (Timestamp) o);
                    }

                } else if (o instanceof Float) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.FLOAT);
                    } else {
                        pstmt.setFloat(idx++, (Float) o);
                    }

                } else if (o instanceof Double) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.DOUBLE);
                    } else {
                        pstmt.setDouble(idx++, (Double) o);
                    }

                } else if (o instanceof BigDecimal) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.DECIMAL);
                    } else {
                        pstmt.setBigDecimal(idx++, (BigDecimal) o);
                    }

                } else if (o instanceof Blob) {
                    if (o == null) {
                        pstmt.setNull(idx++, Types.BLOB);
                    } else {
                        pstmt.setBlob(idx++, (Blob) o);
                    }

                } else if (o instanceof InputStream) {// 上傳檔案用
                    pstmt.setBinaryStream(idx++, (InputStream) o, ((InputStream) o).available());// 24648
                    // pstmt.setBlob(idx++, (InputStream) o, ((InputStream)
                    // o).available());//會丟出AbstractMethodError
                }
            }
        }
    }

    /**
     * 執行多個SQL的update與insert條件
     *
     * @param sqls 欲執行的SQL陣列
     * @return 此SQL陣列更新的筆數
     */
    public int[] executeBatch(Object[] sqls) {
        int[] resultCount = new int[sqls.length];
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            log.info("準備執行批次SQL:");
            for (int i = 0; i < sqls.length; i++) {
                log.info((i + 1) + ". SQL=" + sqls[i]);
                resultCount[i] = stmt.executeUpdate(sqls[i].toString());
                log.info((i + 1) + ". 更新筆數=" + resultCount[i]);
                }
            conn.commit();
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException sqle) {
                log.error(ExceptionUtil.toString(sqle));
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return resultCount;
    }

    /**
     * 與executeBatch相同功能，,只是換成prepareStatement
     *
     * @param sqls
     * @param objs [][]
     * @return
     */
    public int[] pexecuteBatch(Object[] sqls, Object[][] objs) {
        int[] resultCount = new int[sqls.length];
        Connection conn = null;
        // Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            log.info("準備執行批次pSQL:");
            for (int i = 0; i < sqls.length; i++) {
                // resultCount[i] = stmt.executeUpdate(sqls[i].toString());
                pstmt = conn.prepareStatement(sqls[i].toString());
                setPstmtParameter(objs[i], pstmt);
                log.info((i + 1) + ". pSQL=" + printSql(sqls[i].toString(), objs[i]));
                resultCount[i] = pstmt.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException sqle) {
                log.error(ExceptionUtil.toString(sqle));
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return resultCount;
    }

    // 取得資料庫連結
    public Connection getConnection() {
        Connection conn = null;
        if (connectType == 1) {// 1-從DataSource
            try {
                InitialContext cxt = new InitialContext();
                DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/InfiniteDB");
                conn = ds.getConnection();                
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            }
        } else if (connectType == 2) {// 2-從設定檔
            String postfix = "";//NPAUtil.getPropertiesPostfix();
            try {
                String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//Config.getString("DRIVER");
                String dbUrl = "jdbc:sqlserver://220.135.113.164:1578;database=un888";//Config.getString("URL" + postfix);
                String dbUsername = "sa";//Config.getString("USERNAME" + postfix);
                String dbPassword = "Matrix3626";//Config.getString("PASSWORD" + postfix);
                //Class.forName("net.sourceforge.jtds.jdbc.Driver");
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Class.forName(dbDriver);
                //jdbc:sqlserver://220.135.113.164:1578;database=un888
                //conn = DriverManager.getConnection("jdbc:jtds:sybase://172.16.201.41:5000/LEDB;charset=UTF-8", "sa", "syscom1");
                conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return conn;
    }
    
    // 取得 CJDB連結
//    public Connection getCJConnection() {
//        Connection conn = null;
//	    try {
//                Class.forName("com.sybase.jdbc4.jdbc.SybDriver");
//			conn = DriverManager.getConnection(
//					"jdbc:jtds:sybase://172.16.220.47:5000/CJDB;charset=UTF8", "AIU001", "AIU001");
//} catch (Exception e) {
//		log.error(ExceptionUtil.toString(e));
//	    }
//        try {
//            InitialContext cxt = new InitialContext();
//            DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/CJDB");
//            conn = ds.getConnection();
//        } catch (Exception e) {
//            log.error(ExceptionUtil.toString(e));
//        }
//        return conn;
//    }
    // 取得110CASE資料庫連結
//    public Connection get110CaseConnection() {
//        Connection conn = null;
//        try {
//            String dbDriver = NpaConfig.getString("DRIVER");
//            String dbUrl = NpaConfig.getString("URLBY110CASE");
//            String dbUsername = NpaConfig.getString("USERNAME");
//            String dbPassword = NpaConfig.getString("PASSWORD");
//            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Class.forName(dbDriver);
//            //conn = DriverManager.getConnection("jdbc:jtds:sybase://172.16.201.41:5000/LEDB;charset=UTF-8", "sa", "syscom1");
//            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }

    /**
     * 因為sybase的jdbc無法印出prepare statement的參數內容，所以只好自己印出來嚕。
     *
     * @param sql
     * @param objs
     * @return
     */
    public static String printSql(String sql, Object[] objs) {
        String tmp = sql;
        if (objs != null) {
            String value = "";
            try {
                for (Object o : objs) {
                    if (o instanceof String || o instanceof Timestamp) {
                        value = "'" + o + "'";
                    } else if (o instanceof InputStream) {
                        value = "InputStream";
                    } else if (o == null) {
                        value = "null";
                    } else {
                        value = o.toString();
                    }
                    tmp = tmp.substring(0, tmp.indexOf("?")) + value + tmp.substring(tmp.indexOf("?") + 1, tmp.length());
                }

            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
            } finally {

            }
        }
        return tmp;
    }

    /**
     * 將資料庫的查詢結果轉成JSONArray
     *
     * @param list
     * @return
     */
    public static JSONArray arrayList2JsonArray(ArrayList<HashMap> list) {
        JSONArray jArray = new JSONArray();
        return arrayList2JsonArray(jArray, list);
    }

    /**
     * 將資料庫的查詢結果轉成JSONArray，使用傳入的JSONArray。
     *
     * @param jArray
     * @param list
     * @return
     */
    public static JSONArray arrayList2JsonArray(JSONArray jArray, ArrayList<HashMap> list) {
        JSONObject jObj;
        for (HashMap<String, String> e : list) {
            jObj = new JSONObject();
            for (String col : e.keySet()) {
                jObj.put(col, e.get(col));
            }
            jArray.put(jObj);
        }
        return jArray;
    }

    public static JSONArray arrayList2JsonArrayByKeyValue(HashMap<String, ArrayList<String>> list) {
        JSONArray jArray = new JSONArray();
        return arrayList2JsonArrayByKeyValue(jArray, list);
    }

    public static JSONArray arrayList2JsonArrayByKeyValue(JSONArray jArray, HashMap<String, ArrayList<String>> list) {
        jArray.put(list);
        return jArray;
    }

    /**
     * 將Clob轉成String
     *
     * @param c
     * @return 字串
     * @throws SQLException
     */
    public static String clobToString(Clob c) throws SQLException {
        String s = "";
        s = c.getSubString(1, (int) c.length());
        return s;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(printSql("select * from let where a=? and b=? and e=?", new Object[]{"c", new Integer(10), "a"}));
        DBUtil db = DBUtil.getInstance(2);
        //塞入照片
        InputStream is = new FileInputStream(new File("D:\\2013-07-05_115533.png"));
        String sql = "INSERT INTO LETB05 VALUES(?,?,?,?)";
        db.pexecuteUpdate(sql, new Object[]{2, "09311002819", "test.png", is});

        //取出照片
        ArrayList<HashMap> list = db.executeQuery("SELECT * FROM LETB05 WHERE CASE_NO='09311002819' AND CASE_PIC_NO=2");
        for (HashMap e : list) {
            Object o = e.get("CASE_PIC");
            Blob b = (Blob) o;
            //InputStream is = (InputStream)o;
            //log.debug(is.available());
            log.debug(b.getBinaryStream().available());
            FileOutputStream fos = new FileOutputStream("d:\\" + e.get("CONTENT"));
            BufferedInputStream bis = new BufferedInputStream(b.getBinaryStream());
            byte[] buffer = new byte[10240];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            bis.close();
            fos.close();
        }

    }

    /**
     * 新增一筆資料並回傳Identity值
     *
     * @param sql
     * @param objs
     * @return
     */
    public int getIdentityAfterInsert(String sql, Object[] objs) {
        int returnValue = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            //Candy note: 下面這行指令在sybase jdbc driver是不work的. 理由如下:
            //Creates a default PreparedStatement object that has the capability to retrieve auto-generated keys. The given constant tells the driver whether it should make auto-generated keys available for retrieval. This parameter is ignored if the SQL statement is not an INSERT statement.
            //Note: This method is optimized for handling parametric SQL statements that benefit from precompilation. If the driver supports precompilation, the method prepareStatement will send the statement to the database for precompilation. Some drivers may not support precompilation. In this case, the statement may not be sent to the database until the PreparedStatement object is executed. 
            //因此, 若使用sybase jdbc driver而要達到這個功能, 比較好的作法是用store procedure 先insert以取得insert進去的identity 值傳回後, 再用update的方式把其他欄位補上去.
            /*create procedure sp_add_NVDT_APL_BASIC
                    @p_IDNO char(40),
                    @p_NV_SEQ_NO int output
               as 
                   BEGIN 
                     insert into NVDT_APL_BASIC( NV_APL_IDNO ) VALUES(@p_IDNO )
                     select @p_NV_SEQ_NO = @@identity
                   END
             */
            //否則, 照原來下面的寫法, 只能用jtds driver了
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int idx = 1;
            for (Object o : objs) {
                if (o instanceof String) {
                    pstmt.setString(idx++, o.toString());
                } else if (o instanceof Integer) {
                    pstmt.setInt(idx++, (Integer) o);
                } else if (o instanceof Timestamp) {
                    pstmt.setTimestamp(idx++, (Timestamp) o);
                } else if (o instanceof Double) {
                    pstmt.setDouble(idx++, (Double) o);
                }
            }
            log.info("pSQL=" + printSql(sql, objs));

            int affectRows = pstmt.executeUpdate();

            if (affectRows != 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    returnValue = generatedKeys.getInt(1);
                }
            }

        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                conn.close();
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    public ResultSet excuteQuerySet(String sql, Object[] objs) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // log.info("SQL=" + sql);
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtParameter(objs, pstmt);
            rs = pstmt.executeQuery();
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(ExceptionUtil.toString(e));
                // e.printStackTrace();
            }
        }
        return rs;
    }
    public ArrayList<HashMap> pexecuteQueryByLabel(String sql, Object[] objs) {
	ArrayList<HashMap> list = new ArrayList<HashMap>();
	HashMap<String, String> row;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	// log.info("SQL=" + sql);
	try {
	    conn = getConnection();
	    pstmt = conn.prepareStatement(sql);
	    setPstmtParameter(objs, pstmt);
	    log.info("pSQL=" + printSql(sql, objs));	    
	    rs = pstmt.executeQuery();
	    transformResultSet2ArrayListByLabel(list, rs);

	} catch (Exception e) {
	    // log.error(e.getMessage(), e.getCause());
	    log.error(ExceptionUtil.toString(e));
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		if (pstmt != null)
		    pstmt.close();
		if (conn != null)
		    conn.close();
	    } catch (Exception e) {
		log.error(ExceptionUtil.toString(e));
		// e.printStackTrace();
	    }
	}
	return list;
    }
    
//    /**
//     * 提供與executeQuery相同的功能,只是換成prepareStatement
//     *
//     * @param sql
//     * @param objs 目前實作String;Integer;Timestamp
//     * @return
//     */
//    public ArrayList<HashMap> pexecuteQueryByAXDB(String sql, Object[] objs) {
//        ArrayList<HashMap> list = new ArrayList<HashMap>();
//        HashMap<String, String> row;
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        long s = System.currentTimeMillis();	
//        try {
//            conn = getAXDBCaseConnection();
//            pstmt = conn.prepareStatement(sql);
//            setPstmtParameter(objs, pstmt);
//            rs = pstmt.executeQuery();
//            long costTime = System.currentTimeMillis() - s;            
//            if(costTime > WARN_COST_TIME){
//                log.warn("pSQL=" + printSql(sql, objs));                
//                log.warn("[耗時]:" + costTime + " ms");
//            }
//            transformResultSet2ArrayList(list, rs);
//
//        } catch (Exception e) {
//            // log.error(e.getMessage(), e.getCause());
//            log.error(ExceptionUtil.toString(e));
//            e.printStackTrace();
//        } finally {
//            try {
//                rs.close();
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                log.error(ExceptionUtil.toString(e));
//                // e.printStackTrace();
//            }
//        }
//        return list;
//    }
    
    
//    // 取得AXDB資料庫連結
//    public Connection getAXDBCaseConnection() {
//    	Connection conn = null;
//	    try {
//                String postfix = NPAUtil.getPropertiesPostfix();
//		String dbDriver = Config.getString("DRIVER");
//		String dbUrl = Config.getString("URLBYAXDB" + postfix);
//		String dbUsername = Config.getString("USERNAMEBYAXDB" + postfix);
//		String dbPassword = Config.getString("PASSWORDBYAXDB" + postfix);
//		//Class.forName("net.sourceforge.jtds.jdbc.Driver");
//		Class.forName(dbDriver);
//		//conn = DriverManager.getConnection("jdbc:jtds:sybase://172.16.201.41:5000/LEDB;charset=UTF-8", "sa", "syscom1");
//		conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//	    } catch (Exception e) {
//		e.printStackTrace();
//	    }
//	return conn;
//    }


    public enum APPCODE_CATEGORY{
        CLIENTTYPE,
        EDUCATION,
        OCCUPATION,
        COMMITREASON,
        DRUGSOURCE,
        PENALTYSTATUS,
        FINESTATUS,
        WORKSHOPSTATUS,
        SCHOOLLEVEL,
        DETECTPLACETYPE,
        FACTORYSTATUS,
        DRUGITEM,
        DI1,
        ATTACHTYPE,
        DELIVERWAY,
        EX_SENTENCE,

    }
    /*
    CachedRowSet to ArrayList<HashMap>
    
    */
    public static void transCachedRowSet2ArrayList(ArrayList<HashMap> list, CachedRowSet rs) throws SQLException {
        LinkedHashMap<String, Object> row;
        ResultSetMetaData rsMetaData = rs.getMetaData();

        row = new LinkedHashMap<String, Object>();
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
            if (rsMetaData.getColumnType(i) == Types.BLOB) {
                row.put(rsMetaData.getColumnLabel(i), rs.getBlob(i));
            } else {
                row.put(rsMetaData.getColumnLabel(i), StringUtil.nvl(rs.getString(rsMetaData.getColumnLabel(i))));
            }
        }
        if (list == null) {
            list.set(0, row);
        } else {

            list.add(row);
        }

    }
}
