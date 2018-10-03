package dao;

import static util.StringUtil.join;
import static util.StringUtil.nvl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import base.CJBaseDao;
import util.DateUtil;
import util.ExceptionUtil;
import util.User;

public class CommonDao extends CJBaseDao
{
	private static CommonDao instance = null;
	private static Logger log = Logger.getLogger(CommonDao.class);

	private CommonDao()
	{
	}

	public static CommonDao getInstance()
	{
		if (instance == null)
		{
			instance = new CommonDao();
		}
		return instance;
	}

	/**
	 * 查詢縣市資料
	 * 
	 * @return
	 */
	public JSONArray queryCity(JSONObject jObj)
	{
            System.out.println("queryCity...");
		JSONArray returnArray = new JSONArray();
		// String sql = "SELECT * FROM ABDB..E0DT_CITY ORDER BY E0_CITY_CD";
		String sql = "select TM_CITY_CD, TM_CITY_NM from TMDT_E0_CITY where substring(TM_CITY_NM,1,1) <> '＊' ORDER BY TM_CITY_CD ";
		returnArray = arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
		// 加入舊縣市代碼
		// if(jObj.has("oldCity") && jObj.getBoolean("oldCity")){
		// sql =
		// "select E0_CITY_CD, E0_CITY_NM, E0_CITY_CD AS ORDERS from ABDB..E0DT_CITY where substring(E0_CITY_NM,1,1) = '＊' "
		// + "AND E0_CITY_CD <> '99999' ORDER BY 3 ";
		// returnArray = arrayList2JsonArray(returnArray, pexecuteQuery(sql, new
		// Object[] {}));
		// }
                System.out.println("returnArray:"+returnArray.toString());
		return returnArray;
	}

	public JSONArray queryCityWithOld(JSONObject jObj)
	{
		JSONArray returnArray = new JSONArray();
		// String sql = "SELECT * FROM ABDB..E0DT_CITY ORDER BY E0_CITY_CD";
		String sql = "select E0_CITY_CD, E0_CITY_NM, E0_COMMON_SORT AS ORDERS from E0DT_CITY_SORT "
				+ "where E0_CITY_CD <> '99999' AND E0_COMMON_SORT IS NOT NULL ORDER BY 3 ";
		returnArray = arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
		return returnArray;
	}

	/**
	 * 查詢縣市資料(依據使用者的管轄轄區作限制),增加一個參數(otherParam)來判斷是否要將"全部"選項改為"請選擇縣市"選項
	 * 
	 * @return
	 */
	public JSONArray queryCityByUnitCd(JSONObject obj, User user)
	{
		JSONArray otherSelect = new JSONArray();
		JSONArray returnArray = new JSONArray();
		if (user.getScopeUnitCd().equals("") || user.getUnitCd().startsWith("A2"))
		{// 警署或刑事局則加入"全部"選項 , 專業單位加入請選擇縣市
			HashMap newSelect = new HashMap();
			newSelect.put("E0_CITY_CD", "");
			if (obj.getString("otherParam").equals(""))
			{
				newSelect.put("E0_CITY_NM", "全部");
			} else
			{
				newSelect.put("E0_CITY_NM", "請選擇縣市");
			}
			newSelect.put("ORDERS", "");
			otherSelect.put(newSelect);
		}
		// String sql = "SELECT * FROM ABDB..E0DT_CITY ORDER BY E0_CITY_CD";
		String sql = "select E0_CITY_CD, E0_CITY_NM, E0_COMMON_SORT AS ORDERS from E0DT_CITY_SORT where substring(E0_CITY_NM,1,1) <> '＊' AND E0_COMMON_SORT IS NOT NULL "
				+ "AND E0_CITY_CD <> '99999' ";

		String subSql = "";
		ArrayList param = new ArrayList();
		// 依據使用者的管轄轄區作限制
		if (user.getUnitCd().startsWith("A2") && !user.getScopeUnitCd().equals(""))
		{// 航空警察局 或 基隆、台中、高雄、花蓮港務警察局 等專業單位
			// sql +=
			// " AND E0_CITY_CD = ? UNION SELECT '99999' AS E0_CITY_CD, '其　他' AS E0_CITY_NM, '00' AS ORDERS "
			// ;
			// param.add(user.getUnitCd1());
		} else if (!user.getScopeUnitCd().equals(""))
		{// 一般單位[自己的 data scope]
			sql += " AND E0_CITY_CD = ? ";
			subSql += " AND E0_CITY_NM LIKE ? ";
			param.add(getAreaUnitByUnitCd(user.getUnitCd1()));
		}
		sql += " ORDER BY 3 ";
		if (param.size() > 0)
		{
			returnArray = arrayList2JsonArray(otherSelect, pexecuteQuery(sql, param.toArray()));
		} else
		{
			returnArray = arrayList2JsonArray(otherSelect, pexecuteQuery(sql, null));
		}
		// if(obj.getBoolean("oldCity")){//將新舊縣市做合併
		// sql =
		// "select E0_CITY_CD, E0_CITY_NM, E0_CITY_CD AS ORDERS from ABDB..E0DT_CITY where substring(E0_CITY_NM,1,1) = '＊' AND E0_CITY_CD <> '99999' "
		// + subSql + " ORDER BY 3 ";
		// String areaName =
		// returnArray.getJSONObject(0).getString("E0_CITY_NM").substring(0,2);
		// if(areaName.equals("臺北")){
		// areaName = "臺北市";
		// }else if(areaName.equals("新北")){
		// areaName = "臺北縣";
		// }
		// if(param.size()>0){
		// returnArray = arrayList2JsonArray(returnArray, pexecuteQuery(sql, new
		// Object[]{"＊"+areaName+"%"}));
		// }else{
		// returnArray = arrayList2JsonArray(returnArray, pexecuteQuery(sql,
		// null));
		// }
		// }
		return returnArray;
	}

	/**
	 * 查詢轄區資料顯示縣市中文(依據使用者的管轄轄區作限制)
	 * 
	 * @return
	 */
	public JSONArray queryUnitCdByUserCd(JSONObject obj, User user)
	{
		JSONArray otherSelect = new JSONArray();
		JSONArray returnArray = new JSONArray();
		if (user.getScopeUnitCd().equals(""))
		{// 警署或刑事局則加入"全部"選項
			HashMap newSelect = new HashMap();
			newSelect.put("E0_UNIT_CD", "");
			newSelect.put("E0_UNIT_NM", "全部");
			newSelect.put("E0_UNIT_SORT", "0000");
			otherSelect.put(newSelect);
		}
		// String sql = "SELECT * FROM ABDB..E0DT_CITY ORDER BY E0_CITY_CD";
		/*
		 * String sql =
		 * "select E0_CITY_CD, E0_CITY_NM, E0_CITY_CD AS ORDERS from ABDB..E0DT_CITY where substring(E0_CITY_NM,1,1) <> '＊' "
		 * + "AND E0_CITY_CD <> '99999' " ;
		 */
		String sql = "SELECT E0_UNIT_CD, E0_CITY_NM AS E0_UNIT_NM, E0_COMMON_SORT AS E0_UNIT_SORT FROM E0DT_CITY_SORT WHERE substring(E0_CITY_NM,1,1) <> '＊' AND E0_COMMON_SORT IS NOT NULL   ";
		String subSql = "";
		ArrayList param = new ArrayList();
		// 依據使用者的管轄轄區作限制
		if (user.getUnitCd().startsWith("A23") || user.getUnitCd().startsWith("A2H") ||
				user.getUnitCd().startsWith("A2I") || user.getUnitCd().startsWith("A2J") ||
				user.getUnitCd().startsWith("A2K"))
		{// 航空警察局 或 基隆、台中、高雄、花蓮港務警察局
			sql += " AND E0_UNIT_CD = ? UNION SELECT '99999' AS E0_UNIT_CD, '其　他' AS E0_UNIT_NM, '9999' AS E0_UNIT_SORT ";
			subSql += " AND E0_UNIT_CD = ? ";
			param.add(user.getUnitCd1());
		} else if (!user.getScopeUnitCd().equals(""))
		{// 一般單位[自己的 data scope]
			sql += " AND E0_UNIT_CD = ? ";
			subSql += " AND E0_UNIT_CD = ? ";
			param.add(user.getUnitCd1());
		} else
		{
			sql += " UNION SELECT '99999' AS E0_UNIT_CD, '其　他' AS E0_UNIT_NM, '9999' AS E0_UNIT_SORT ";
		}
		sql += " ORDER BY E0_UNIT_SORT ";
		if (param.size() > 0)
		{
			returnArray = arrayList2JsonArray(otherSelect, pexecuteQuery(sql, param.toArray()));
		} else
		{
			returnArray = arrayList2JsonArray(otherSelect, pexecuteQuery(sql, null));
		}

		String[] getBothUnitCd = getUnitAllCode(user.getUnitCd1());
		if (obj.getBoolean("oldCity"))
		{// 將新舊縣市做合併
			sql = "SELECT E0_UNIT_CD, SUBSTRING(E0_UNIT_NM,1,4) AS E0_UNIT_NM, E0_UNIT_SORT FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG IN ('10','1') AND E0_DELETE_FLAG = 1 "
					+ subSql + "ORDER BY 1 ";// E0_DEPT_CD IN ('AE000','BE000')
												// AND
			if (param.size() > 0)
			{
				returnArray = arrayList2JsonArray(returnArray, pexecuteQuery(sql, new Object[] { getBothUnitCd[0] }));
			} else
			{
				returnArray = arrayList2JsonArray(returnArray, pexecuteQuery(sql, null));
			}
		}
		return returnArray;
	}

	/**
	 * 由警局代碼(縣市警局)取得對應的縣市名稱
	 **/
	public String getAreaUnitByUnitCd(String unitCd1)
	{
		String sql = ""
				+ " SELECT "
				+ "     E0_CITY_CD, "
				+ "     E0_CITY_NM, "
				+ "     E0_UNIT_CD, "
				+ "     CASE WHEN E0_CITY_CD='99999' THEN '00' ELSE E0_CITY_CD END AS ORDERS "
				+ " FROM ABDB..E0DT_CITY "
				+ " LEFT JOIN( "
				+ "     SELECT SUBSTRING(E0_UNIT_NM, 1, 3) AS E0_UNIT_NM, E0_UNIT_CD "
				+ "     FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_DELETE_FLAG=0 "
				+ " ) A ON A.E0_UNIT_NM = E0_CITY_NM "
				+ " WHERE SUBSTRING(E0_CITY_NM, 1, 1) <> '＊' "
				+ " AND E0_UNIT_CD = ? ";
		return this.pgetData(sql, "E0_CITY_CD", new Object[] { unitCd1 });
	}

	/**
	 * 由縣市代碼取得對應的警局代碼
	 **/
	public String getUnitCdByAreaUnit(String cityCd)
	{
		String sql = ""
				+ " SELECT "
				+ "     E0_CITY_CD, "
				+ "     E0_CITY_NM, "
				+ "     E0_UNIT_CD, "
				+ "     CASE WHEN E0_CITY_CD='99999' THEN '00' ELSE E0_CITY_CD END AS ORDERS "
				+ " FROM ABDB..E0DT_CITY "
				+ " LEFT JOIN( "
				+ "     SELECT SUBSTRING(E0_UNIT_NM, 1, 3) AS E0_UNIT_NM, E0_UNIT_CD "
				+ "     FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_DELETE_FLAG=0 "
				+ " ) A ON A.E0_UNIT_NM = E0_CITY_NM "
				+ " WHERE SUBSTRING(E0_CITY_NM, 1, 1) <> '＊' "
				+ " AND E0_CITY_CD = ? ";
		return this.pgetData(sql, "E0_UNIT_CD", new Object[] { cityCd });
	}

	/**
	 * 依縣市代碼查詢鄉鎮區資料 縣市如果是 「其　他」，第二階鄉鎮區的選項提供一個「其他」（沒有空格），代號99999999
	 * 
	 * @param cityId
	 *            縣市代碼
	 * @return
	 */
	public JSONArray queryArea(JSONObject cityJObj)
	{
		JSONArray jArray = new JSONArray();
		String cityId = cityJObj.getString("city");
		if (cityId.equals("99999"))
		{// 縣市如果是
			// 「其　他」，第二階鄉鎮區的選項提供一個「其他」（沒有空格），代號99999999
			jArray = new JSONArray();
			JSONObject jObj = new JSONObject();
			ArrayList list = new ArrayList();
			jObj.put("E0_TOWN_ALIAS", "其他");
			jObj.put("E0_TOWN_CD", "99999999");
			jArray.put(jObj);
		} else
		{
			String sql = "";
			// 放入舊鄉鎮區代碼
			sql = "SELECT E0_TOWN_CD, E0_TOWN_ALIAS, E0_TOWN_CD AS ORDERS ,E0_CITY_CD FROM ABDB..E0DT_TOWN WHERE "
					+ "substring(E0_TOWN_FULLNAME,5,3) = ? ";
			String oldCityName = cityJObj.getString("cityName");
			if (oldCityName.indexOf("＊") != -1)
			{// 舊
				oldCityName = oldCityName.substring(1, 4);
				if (oldCityName.equals("新北市"))
				{
					oldCityName = "臺北縣";
				}
				String oldCityCd = pgetData(sql, "E0_CITY_CD", new Object[] { oldCityName });
				sql = "SELECT E0_TOWN_CD, E0_TOWN_ALIAS, E0_TOWN_CD AS ORDERS FROM ABDB..E0DT_TOWN WHERE E0_CITY_CD=? AND substring(E0_TOWN_FULLNAME,1,1) = '＊' "
						+ "AND E0_TOWN_CD <> '99999999' AND SUBSTRING(E0_TOWN_ALIAS,2,1) != '' "
						+ "UNION SELECT '99999999' AS E0_TOWN_CD, '其　他' AS E0_TOWN_ALIAS, '00' AS ORDERS "
						+ "ORDER BY 3 DESC ";
				jArray = arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { oldCityCd }));
			} else
			{// 新
				sql = "SELECT E0_TOWN_CD, E0_TOWN_ALIAS, E0_TOWN_CD AS ORDERS , E0_TOWN_FULLNAME "
						+ "FROM ABDB..E0DT_TOWN WHERE E0_CITY_CD= ? AND substring(E0_TOWN_FULLNAME,1,1) <> '＊' AND E0_TOWN_CD <> '99999999' "
						+ "ORDER BY 3 DESC ";
				jArray = arrayList2JsonArray(pexecuteQuery(sql, new Object[] { cityId }));
			}
		}
		return jArray;
	}

	/**
	 * 依鄉鎮區代碼查詢村里資料
	 * 
	 * @param areaId
	 *            鄉鎮區代碼
	 * @return
	 */
	public JSONArray queryVillage(JSONObject areaJObj)
	{
		JSONArray jArray = new JSONArray();
		String areaId = areaJObj.getString("area");
		if (areaId.equals("99999999"))
		{// 鄉鎮區如果是「其　他」，第三階鄉鎮區的選項提供一個「其他」（沒有空格），代號99999999999
			jArray = new JSONArray();
			JSONObject jObj = new JSONObject();
			jObj.put("E0_VILLAGE_S_NM", "其他");
			jObj.put("E0_VILLAGE_CD", "99999999999");// 11碼
			jArray.put(jObj);
		} else
		{
			String subSql = " OR SUBSTRING(E0_TOWN_FULLNAME,5,2) = ? ";

			String areaNewName = areaJObj.getString("areaName"), areaOldName = areaJObj.getString("areaName");
			String getCityName = areaJObj.getString("cityName");
			String getCityOldName = "";

			if (getCityName.indexOf("＊") == -1)
			{
				getCityName = getCityName.substring(0, 2);
				getCityOldName = getCityName.substring(0, 2);
				/*
				 * if(getCityName.equals("新北")){
				 * getCityOldName = "臺北縣";
				 * subSql = " OR SUBSTRING(E0_TOWN_FULLNAME,5,3) = ? ";
				 * }
				 */
			} else
			{
				getCityName = getCityName.substring(1, 3);
				getCityOldName = getCityName;
			}

			String sql = "", bothAreaCd = "";
			if (areaNewName.indexOf("＊") == -1)
			{// 查詢新鄉鎮區areacd
				areaNewName = areaNewName.substring(0, 2);
				sql = "SELECT E0_TOWN_CD FROM ABDB..E0DT_TOWN WHERE "
						+ "(SUBSTRING(E0_TOWN_ALIAS,1,2) = ? ) "
						+ "AND (SUBSTRING(E0_TOWN_FULLNAME,1,2) = ? "
						+ "OR SUBSTRING(E0_TOWN_FULLNAME,4,2) = ? ) "
						+ "ORDER BY E0_TOWN_CD DESC ";
				bothAreaCd = pgetFirstRowData("E0_TOWN_CD", sql, new Object[] { areaNewName, getCityName, getCityName });
			} else
			{// 查詢舊鄉鎮區areacd
				areaOldName = areaOldName.substring(1, 3);
				sql = "SELECT E0_TOWN_CD FROM ABDB..E0DT_TOWN WHERE "
						+ "SUBSTRING(E0_TOWN_ALIAS,2,2) = ?  "
						+ "AND SUBSTRING(E0_TOWN_FULLNAME,5,2) = ? "
						+ "ORDER BY E0_TOWN_CD DESC ";
				bothAreaCd = pgetFirstRowData("E0_TOWN_CD", sql, new Object[] { areaOldName, getCityOldName });
			}
			sql = "SELECT * FROM ABDB..E0DT_VILLAGE WHERE E0_TOWN_CD = ? ORDER BY E0_VILLAGE_CD ";
			jArray = arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { bothAreaCd }));
		}
		return jArray;
	}

	/**
	 * 查詢縣市新舊代碼資料 傳入『新縣市』代碼，回傳"新"與"舊"縣市代碼 傳入『舊縣市』代碼，只回傳"舊"縣市代碼
	 * 
	 * @param city_cd縣市代碼
	 * @param city_nm縣市中文
	 * @return
	 */
	public List<String> getCityAllCode(String city_cd, String cd_nm)
	{
		StringBuilder sql = new StringBuilder();
		ArrayList args = new ArrayList();
		sql.append("\n").append(" SELECT ");
		sql.append("\n").append(" E0_CITY_CD ");
		sql.append("\n").append(" FROM ABDB..E0DT_CITY  ");
		sql.append("\n").append(" WHERE  ");
		sql.append("\n")
				.append("  str_replace(str_replace(str_replace(str_replace(str_replace(ltrim(rtrim(str_replace(str_replace(ISNULL(E0_CITY_NM,''),'＊',''),' ',''))) ,'台' , '臺'), '臺北縣','新北市') ,'臺中縣','臺中市') ,'臺南縣','臺南市') ,'高雄縣','高雄市')    ");
		sql.append("\n").append(" IN ( ");
		sql.append("\n").append("         SELECT  ");
		sql.append("\n")
				.append("         (CASE WHEN E0_CITY_NM LIKE '＊%' THEN  E0_CITY_NM ELSE str_replace(str_replace(str_replace(str_replace(str_replace(ltrim(rtrim(str_replace(str_replace(ISNULL(E0_CITY_NM,''),'＊',''),' ',''))) ,'台' , '臺'), '臺北縣','新北市') ,'臺中縣','臺中市') ,'臺南縣','臺南市') ,'高雄縣','高雄市') END) ");
		sql.append("\n").append("         FROM ABDB..E0DT_CITY  ");
		sql.append("\n").append("         WHERE 1=1  ");
		if (nvl(city_cd).length() > 0)
		{
			sql.append("\n").append("          AND E0_CITY_CD = ? ");
			args.add(city_cd);
		}
		if (nvl(cd_nm).length() > 0)
		{
			sql.append("\n").append("          AND E0_CITY_NM LIKE '%' +?+ '%' ");
			args.add(cd_nm);
		}
		sql.append("\n").append(" ) ");

		List<String> ary_code = new ArrayList<String>();
		ArrayList<HashMap> list = pexecuteQuery(sql.toString(), args.toArray());
		for (HashMap o : list)
		{
			ary_code.add(nvl(o.get("E0_CITY_CD")));
		}

		return ary_code;
	}

	/**
	 * 查詢機關新舊代碼資料 傳入『新機關』代碼，回傳"新"與"舊"機關代碼 傳入『舊機關』代碼，只回傳"舊"機關代碼
	 * 
	 * @param unit_cd機關代碼
	 * @return
	 */
	public String[] getUnitAllCode(String unit_cd)
	{
		if (nvl(unit_cd).length() <= 0)
			return new String[] {};
		StringBuilder sql = new StringBuilder();
		ArrayList args = new ArrayList();
		sql.append("\n").append("  SELECT OLD_UNIT_CD FROM LEDV_UNITMAP ");
		sql.append("\n").append("  WHERE NEW_UNIT_CD = ?  ");
		args.add(unit_cd);

		List<String> ary_code = new ArrayList<String>();
		ArrayList<HashMap> list = pexecuteQuery(sql.toString(), args.toArray());
		for (HashMap o : list)
		{
			if (nvl(o.get("OLD_UNIT_CD")).length() <= 0)
				continue;
			ary_code.add(nvl(o.get("OLD_UNIT_CD")));
		}
		ary_code.add(unit_cd);

		String[] ary = new String[ary_code.size()];
		ary_code.toArray(ary);

		return ary;
	}

	
	// 專給治安事故摘要表 
	public String getUnit_bySummary(User user)
	{
		String unit_tmp = "";
		if (user.getOwnRoleString().indexOf("LE200001") >= 0 || user.getUnitCd1().equals("A2200"))
			unit_tmp = "A1101";
		else
			unit_tmp = user.getUnitCd();
		return "'" + join(getUnitAllCode(unit_tmp), "','") + "'";
	}

	/**
	 * 查詢警署的第一級單位，要顯示警政署、縣市警察局、專業警察單位、五都舊警察局。
	 * 
	 * @return
	 */
	public JSONArray queryUnit1(User user)
	{
		/*
		 * String sql = "SELECT * FROM ABDB..E0DT_NPAUNIT "
		 * +" WHERE E0_UNIT_CD=E0_DEPT_CD" +" AND E0_DELETE_FLAG=0"
		 * +" ORDER BY E0_UNIT_CD";
		 */
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE "
				+ " ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10')"
				+ " OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_DELETE_FLAG=0 AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00'))"
				+ " OR (E0_UNIT_FLAG='90')"
				// +
				// " OR (E0_UNIT_CD IN ('D000','B000','I000','C000','O000','V000','P000','A2X00'))"
				+ ")"
				+ getQueryUnit1ScopeSql(user)
				+ " ORDER BY E0_UNIT_SORT, E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
	}

	private String getQueryUnit1ScopeSql(User user)
	{
		String retScopeSql = "";
		String scopeSql = "AND E0_UNIT_CD IN ( "
				+ "  SELECT * FROM ( "
				+ "      SELECT OLD_UNIT_CD AS A FROM LEDV_UNITMAP  "
				+ "      WHERE NEW_UNIT_CD='%s' "
				+ "      UNION  "
				+ "      SELECT DISTINCT NEW_UNIT_CD FROM LEDV_UNITMAP "
				+ "      WHERE NEW_UNIT_CD='%s' "
				+ "   ) UNITMAP "
				+ " )";
		if (user.getScopeUnitSql().length() != 0)
		{
			retScopeSql = String.format(scopeSql, user.getUnitCd1(), user.getUnitCd1());
		}
		return retScopeSql;
	}

	/**
	 * 查詢警署的第二級單位
	 * 
	 * @param deptCd
	 *            第一級單位
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray queryUnit2(String deptCd, User user)
	{
		/*
		 * String sql = "SELECT * FROM ABDB..E0DT_NPAUNIT " +
		 * " WHERE E0_DEPT_CD=? AND (E0_BRANCH_CD='' OR E0_BRANCH_CD=E0_DEPT_CD) AND E0_UNIT_CD<>E0_DEPT_CD"
		 * +" AND E0_DELETE_FLAG=0" +" ORDER BY E0_UNIT_CD";
		 */
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2') ORDER BY E0_UNIT_CD";
		ArrayList<HashMap> unit2 = pexecuteQuery(sql, new Object[] { deptCd });
		if (user.getScopeUnitLevelNum().length() > 0 && Integer.parseInt(user.getScopeUnitLevelNum()) >= 2)
		{
			for (int i = unit2.size() - 1; i >= 0; i--)
			{
				if (!unit2.get(i).get("E0_UNIT_CD").toString().equals(user.getScopeUnitCd()))
				{
					unit2.remove(i);
				}
			}
		}
		return arrayList2JsonArray(unit2);
	}

	/**
	 * 查詢警署的第三級單位
	 * 
	 * @param branchCd
	 *            第二級單位
	 * @return
	 */
	public JSONArray queryUnit3(String branchCd, User user)
	{
		/*
		 * String sql =
		 * "SELECT E0_UNIT_CD, E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD='AW000' AND E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2') ORDER BY E0_UNIT_CD"
		 * +" WHERE E0_BRANCH_CD=? AND E0_UNIT_CD<>E0_BRANCH_CD"
		 * +" AND E0_DELETE_FLAG=0" +" ORDER BY E0_UNIT_CD";
		 */
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_BRANCH_CD=? AND E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG IN ('30','32','70','77') ORDER BY E0_UNIT_CD";
		ArrayList<HashMap> unit3 = pexecuteQuery(sql, new Object[] { branchCd });
		if (user.getScopeUnitLevelNum().length() > 0 && Integer.parseInt(user.getScopeUnitLevelNum()) >= 3)
		{
			for (int i = unit3.size() - 1; i >= 0; i--)
			{
				if (!unit3.get(i).get("E0_UNIT_CD").toString().equals(user.getScopeUnitCd()))
				{
					unit3.remove(i);
				}
			}
		}
		return arrayList2JsonArray(unit3);
	}

	/**
	 * 查詢警署的第一級單位，警政署及刑事局人員可以選全部（警政署、縣市警察局、專業警察單位，不含舊警察局）。
	 * 
	 * @return
	 */
	public JSONArray queryNewUnit1(JSONObject obj)
	{
		String subsql = "";
		if (obj.getString("reportType").equals("noA1000"))
		{// 去掉警政署選項
			subsql = " AND E0_UNIT_CD <> 'A1000' ";
		}
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE "
				+ " ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10')"
				+ " OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00'))"
				+ " OR (E0_UNIT_FLAG='90')) AND E0_DELETE_FLAG=0 " + subsql
				+ " ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
	}

	/**
	 * 查詢轄區的第一級單位，警政署及刑事局人員可以選全部（警政署、縣市警察局、專業警察單位，不含舊警察局）。[有限制腳色選擇範圍]
	 * 
	 * @return
	 */
	public JSONArray queryLimitRoleNewUnit1(String reportType, User user)
	{
		String a1000Sql = "";
		if (!reportType.equals("noA1000"))
		{
			a1000Sql = " OR (E0_UNIT_FLAG='90') ";
		}
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE "
				+ " ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10')"
				+ " OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00'))"
				+ a1000Sql + " ) AND E0_DELETE_FLAG=0 ";

		ArrayList param = new ArrayList();
		ArrayList list = new ArrayList();
		if (!user.getScopeUnitCd().equals(""))
		{// 非警署及刑事局單位要限制其選取範圍
			sql += " AND E0_UNIT_CD = ? ";
			param.add(user.getUnitCd1());
		}
		sql += " ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
		if (param.size() > 0)
		{
			list = pexecuteQuery(sql, param.toArray());
		} else
		{
			list = pexecuteQuery(sql, new Object[] {});
		}
		return arrayList2JsonArray(list);
	}

	/**
	 * 查詢警署的第一級單位，警政署及刑事局人員可以選全部（警政署、縣市警察局、專業警察單位，包含舊警察局）。
	 * 
	 * @return
	 */
	public JSONArray queryNewUnit1WithOldUnit(JSONObject obj)
	{
		String subsql = "";
		if (obj.getString("reportType").equals("noA1000"))
		{// 去掉警政署選項
			subsql = " AND E0_UNIT_CD <> 'A1000' ";
		}
		String sql =
				" SELECT E0_UNIT_CD, E0_UNIT_S_NM, E0_UNIT_SORT FROM ABDB..E0DT_NPAUNIT WHERE  ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10') OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00')) OR (E0_UNIT_FLAG='90')) AND E0_DELETE_FLAG=0 "
						+ subsql
						+ " UNION ALL "
						+ " SELECT E0_UNIT_CD, E0_UNIT_S_NM, 'a'+E0_UNIT_SORT as E0_UNIT_SORT FROM ABDB..E0DT_NPAUNIT WHERE  ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10') OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00')) OR (E0_UNIT_FLAG='90')) AND E0_DELETE_FLAG=1  "
						+ subsql
						+ " UNION ALL"
						+ " SELECT E0_UNIT_CD, E0_UNIT_S_NM, 'a'+E0_UNIT_SORT as E0_UNIT_SORT FROM ABDB..E0DT_NPAUNIT WHERE  ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='1') OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='4' AND E0_UNIT_CD NOT IN ('2700','2800','2L00','2M00','2O00','2U00')) OR (E0_UNIT_FLAG='90')) AND E0_DELETE_FLAG=1 "
						+ subsql
						+ " ORDER BY E0_UNIT_SORT,E0_UNIT_CD";

		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
	}

	/**
	 * 查詢轄區的第二級單位
	 * 
	 * @param deptCd
	 *            第一級單位
	 * @return
	 */
	public JSONArray queryNewUnit2(String deptCd)
	{
		// String sql =
		// "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2') AND E0_DELETE_FLAG = 0 ORDER BY E0_UNIT_CD";
		// 港警中隊
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ((E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2'))  OR (E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='77')) AND E0_DELETE_FLAG = 0 ORDER BY E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] { deptCd }));
	}

	/**
	 * 查詢轄區的第二級單位，包含舊單位
	 * 
	 * @param deptCd
	 *            第一級單位
	 * @return
	 */
	public JSONArray queryNewUnit2WithOldUnit(String deptCd)
	{
		// String sql =
		// "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2') AND E0_DELETE_FLAG = 0 ORDER BY E0_UNIT_CD";
		// 港警中隊
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ((E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('20','22','60','2'))  OR (E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='77')) ORDER BY E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] { deptCd }));
	}

	/**
	 * 查詢轄區的第三級單位
	 * 
	 * @param branchCd
	 *            第二級單位
	 * @return
	 */
	public JSONArray queryNewUnit3(String branchCd)
	{
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_BRANCH_CD=? AND E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG IN ('30','32','70','77') AND E0_UNIT_CD <> E0_BRANCH_CD AND E0_DELETE_FLAG = 0 ORDER BY E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] { branchCd }));
	}

	/**
	 * 查詢轄區的第三級單位，包含舊單位
	 * 
	 * @param branchCd
	 *            第二級單位
	 * @return
	 */
	public JSONArray queryNewUnit3WithOldUnit(String branchCd)
	{
		String sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_BRANCH_CD=? AND E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG IN ('30','32','70','77') AND E0_UNIT_CD <> E0_BRANCH_CD  ORDER BY E0_UNIT_CD";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] { branchCd }));
	}

	/**
	 * 查詢報導來源檔
	 * 
	 * @return
	 */
	public JSONArray queryReportSouce()
	{
		String sql = "SELECT   LE_CODE_VALUE as LE_ID , LE_CODE_TEXT AS LE_NAME  FROM LEDT_SYSCODE " + " WHERE LE_CODE_VAR  =  'MEDIA_SOURCE' "
				+ " ORDER BY LE_CODE_VALUE   ";
		return arrayList2JsonArray(pexecuteQuery(sql, new Object[] {}));
	}

	public void testInsert(InputStream is)
	{
		try
		{
			String sql = "INSERT INTO LEDT_CASE_PIC VALUES(?,?,?,?)";

			this.pexecuteUpdate(sql, new Object[] { 2, "09311002819", "test.png", is });
		} catch (Exception e)
		{
			log.error(ExceptionUtil.toString(e));
		}

	}

	/**
	 * 查詢通報單位(不是轄區)的第一級單位
	 * 
	 * @return
	 */
	public JSONArray queryNotifyUnit1(User user)
	{
		JSONObject jObj;
		JSONArray jArray = new JSONArray();
		// 登入者若是警政署勤務指揮中心[A1101]就顯示
		if (user.getUnitCd1().equals("A1101"))
		{// 不知這樣判斷是否正確
			
		}
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "1");
		jObj.put("E0_UNIT_S_NM", "警政署勤務中心");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "all");
		jObj.put("E0_UNIT_S_NM", "全國勤務中心");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "4");
		jObj.put("E0_UNIT_S_NM", "直轄市警察局勤指");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "5");
		jObj.put("E0_UNIT_S_NM", "縣市警察局勤指");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "6");
		jObj.put("E0_UNIT_S_NM", "署屬各警察機關勤指");
		if (user.getUnitCd2().equals("A1101"))
		{// 不知這樣判斷是否正確
			jObj.put("E0_UNIT_CD", "A");
			jObj.put("E0_UNIT_S_NM", "署內機關");
		}
		jArray.put(jObj);
		// 各縣市警察局
		/*
		 * String sql =
		 * "SELECT E0_UNIT_CD,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG='10' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD "
		 * ;
		 * arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
		 * //各專業單位第一階都不擋放警局後面
		 * sql =
		 * "SELECT E0_UNIT_CD+'-' E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_DELETE_FLAG=0 AND E0_UNIT_CD NOT IN ('A2700','A2800') ORDER BY E0_UNIT_SORT "
		 * ;
		 * ArrayList<HashMap> list = pexecuteQuery(sql, null);
		 * arrayList2JsonArray(jArray, list);
		 */
		//
		String sql = "SELECT (CASE E0_UNIT_FLAG WHEN '50' THEN E0_UNIT_CD+'-' ELSE E0_UNIT_CD END) AS E0_UNIT_CD,"
				+ " E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE "
				+ " ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10')"
				+ " OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00'))"
				+ " ) AND E0_DELETE_FLAG=0 "
				+ " ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
		arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
		return jArray;
	}

	/**
	 * 查詢通報單位(不是轄區)的第二級單位
	 * 
	 * @param deptCd
	 *            第一級單位
	 * @return
	 */
	public JSONArray queryNotifyUnit2(String notifyUnit1, String notifyUnit2)
	{
		JSONArray jArray = new JSONArray();
		String sql;
		log.debug("notifyUnit1.length()=" + notifyUnit1.length());
		if (notifyUnit1.length() == 5)
		{// 各縣市警局
			if (notifyUnit2.equals("0"))
			{// 選擇各縣市警局
				JSONArray jArrayUnit2 = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("E0_UNIT_CD", "all");
				obj.put("E0_UNIT_S_NM", "各單位勤指");
				jArray.put(obj);
				obj = new JSONObject();
				obj.put("E0_UNIT_CD", "allBranch");
				obj.put("E0_UNIT_S_NM", "警局各科室");
				jArray.put(obj);
				// 縣市警局的大隊與隊
				sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG='22' AND E0_DELETE_FLAG=0 ORDER BY LEN(E0_UNIT_S_NM) DESC, E0_UNIT_CD DESC";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				// 縣市警局的分局
				sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG='20' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				// 縣市警局的科室中心
				sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG='21' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));

			} else
			{// 選擇分局
				if (notifyUnit2.equals("all"))
				{// 各單位勤指
					sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD = ?  AND (E0_UNIT_NM LIKE '%指%中心' or E0_UNIT_NM LIKE '%指%中心[一二]') AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				} else if (notifyUnit2.equals("allBranch"))
				{// 警局各科室
					sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG='21' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				} else
				{//
					// sql =
					// "SELECT E0_UNIT_CD,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_BRANCH_CD=? AND E0_UNIT_FLAG!='20' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
					sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT "
							+ " WHERE ( (E0_BRANCH_CD=? AND E0_UNIT_FLAG!='20' OR E0_UNIT_CD=? )"// --分局底下單位
							+ " OR (E0_UNIT_CD=? AND E0_UNIT_FLAG='21')  )"// --科室單位
							+ " AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2, notifyUnit2, notifyUnit2 }));
					// 縣市警局第三階若沒有資料就塞本身單位
					if (jArray.length() == 0)
					{
						JSONObject obj = new JSONObject();
						obj.put("E0_UNIT_CD", notifyUnit2);
						obj.put("E0_UNIT_S_NM", queryUnitShortName(notifyUnit2));
						jArray.put(obj);
					}
				}

			}
		} else if (notifyUnit1.length() == 6)
		{// 專業單位多一個-變6碼來跟縣市警局區別
			notifyUnit1 = notifyUnit1.substring(0, notifyUnit1.length() - 1);
			if (notifyUnit2.equals("0"))
			{// 選擇專業單位
				// log.debug("notifyUnit1="+notifyUnit1);

				// 各港警總隊加上各中隊的選項;
				// A2I00-台中港警總隊;A2H00-基隆港警總隊;A2J00-高雄港警總隊;A2K00-花蓮港警總隊
				if (notifyUnit1.equalsIgnoreCase("A2I00") || notifyUnit1.equalsIgnoreCase("A2H00")
						|| notifyUnit1.equalsIgnoreCase("A2J00")
						|| notifyUnit1.equalsIgnoreCase("A2K00"))
				{
					JSONObject obj = new JSONObject();
					obj.put("E0_UNIT_CD", "all");
					obj.put("E0_UNIT_S_NM", "各中隊");
					jArray.put(obj);
				}

				// 各保安總隊加上各大隊的選項; A2900 保一總隊;A2A00 保二總隊;A2B00 保三總隊;A2C00
				// 保四總隊;A2D00 保五總隊;A2E00 保六總隊;A2F00 保七總隊
				if (notifyUnit1.equalsIgnoreCase("A2900") || notifyUnit1.equalsIgnoreCase("A2A00")
						|| notifyUnit1.equalsIgnoreCase("A2B00")
						|| notifyUnit1.equalsIgnoreCase("A2C00") || notifyUnit1.equalsIgnoreCase("A2D00")
						|| notifyUnit1.equalsIgnoreCase("A2E00")
						|| notifyUnit1.equalsIgnoreCase("A2F00"))
				{
					JSONObject obj = new JSONObject();
					obj.put("E0_UNIT_CD", "all");
					obj.put("E0_UNIT_S_NM", "各大隊");
					jArray.put(obj);
				}

				// 國道公路警察局加上各大隊的選項;A2400-國道公路警察局
				if (notifyUnit1.equalsIgnoreCase("A2400"))
				{
					JSONObject obj = new JSONObject();
					obj.put("E0_UNIT_CD", "all");
					obj.put("E0_UNIT_S_NM", "各大隊");
					jArray.put(obj);
				}

				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND (E0_UNIT_LEVEL='3'  OR (E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='77')) AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));

			} else
			{// 選擇專業單位第二個項目
				if (notifyUnit2.equals("all"))
				{// 各中隊
					// 各港警總隊加上各中隊的選項
					if (notifyUnit1.equalsIgnoreCase("A2I00") || notifyUnit1.equalsIgnoreCase("A2H00")
							|| notifyUnit1.equalsIgnoreCase("A2J00")
							|| notifyUnit1.equalsIgnoreCase("A2K00"))
					{
						sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT "
								+ " WHERE E0_DEPT_CD=? AND  (E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='77')"
								+ " AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
						arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
					}
					// 各保安總隊加上各大隊的選項
					if (notifyUnit1.equalsIgnoreCase("A2900") || notifyUnit1.equalsIgnoreCase("A2A00")
							|| notifyUnit1.equalsIgnoreCase("A2B00") || notifyUnit1.equalsIgnoreCase("A2C00")
							|| notifyUnit1.equalsIgnoreCase("A2D00") || notifyUnit1.equalsIgnoreCase("A2E00")
							|| notifyUnit1.equalsIgnoreCase("A2F00"))
					{
						sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT "
								+ " WHERE E0_DEPT_CD=? AND E0_UNIT_LEVEL='3'  AND E0_UNIT_FLAG='60' "
								+ " AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
						arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
					}
					// 國道公路警察局加上各大隊的選項;A2400-國道公路警察局
					if (notifyUnit1.equalsIgnoreCase("A2400"))
					{
						sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT "
								+ " WHERE E0_DEPT_CD=? AND E0_UNIT_LEVEL='3'  AND E0_UNIT_FLAG='60' "
								+ " AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
						arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit1 }));
					}

				} else
				{
					ArrayList<HashMap> list;
					// 各保安大隊 與國道警察
					if (notifyUnit1.equalsIgnoreCase("A2900") || notifyUnit1.equalsIgnoreCase("A2A00")
							|| notifyUnit1.equalsIgnoreCase("A2B00") || notifyUnit1.equalsIgnoreCase("A2C00")
							|| notifyUnit1.equalsIgnoreCase("A2D00") || notifyUnit1.equalsIgnoreCase("A2E00")
							|| notifyUnit1.equalsIgnoreCase("A2F00")
							|| notifyUnit1.equalsIgnoreCase("A2400")
							|| notifyUnit1.equalsIgnoreCase("A2300") // 航警局
					)
					{
						// --列出大隊下的各中隊包含大隊本身
						sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT "
								+ " WHERE ((E0_BRANCH_CD=? AND E0_UNIT_LEVEL='4') OR E0_UNIT_CD=? ) "
								+ " AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
						list = pexecuteQuery(sql, new Object[] { notifyUnit2, notifyUnit2 });
					} else
					{
						sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE (E0_BRANCH_CD=? AND E0_UNIT_LEVEL='4') OR E0_UNIT_CD=? AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
						list = pexecuteQuery(sql, new Object[] { notifyUnit2, notifyUnit2 });
					}

					// 專業單位第二階若沒有資料就塞本身單位
					if (list.size() > 0)
					{
						arrayList2JsonArray(jArray, list);
					} else
					{
						JSONObject obj = new JSONObject();
						obj.put("E0_UNIT_CD", notifyUnit2);
						obj.put("E0_UNIT_S_NM", queryUnitShortName(notifyUnit2));
						jArray.put(obj);
					}
				}

			}
		} else if (notifyUnit1.equals("4"))
		{// 直轄市警察局
			if (notifyUnit2.equals("0"))
			{// 選直轄市警察局
				// 列出直轄市五個勤指中心
				// sql =
				// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD IN ('AB000','AD000','AV000','AW000','AC000','AE000') AND E0_UNIT_LEVEL='3' AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				// arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				// 因為沒有E0_UNIT_SORT所以改成分成兩階對撈資料
				// 改成先撈出直轄字排序在各自取出其勤指
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD IN ('AW000','AB000','AD000','AV000','AC000','AE000') AND E0_UNIT_LEVEL='2' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT";
				ArrayList<HashMap> list = pexecuteQuery(sql, null);
				for (HashMap<String, String> r : list)
				{
					sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD ='" + r.get("E0_UNIT_CD")
							+ "' AND E0_UNIT_LEVEL='3' AND E0_DELETE_FLAG=0 AND E0_UNIT_NM LIKE '%指%中心' ORDER BY E0_UNIT_SORT";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				}

			}
		} else if (notifyUnit1.equals("5"))
		{// 縣市警察局
			if (notifyUnit2.equals("0"))
			{// 非直轄市勤指
				// sql =
				// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD NOT IN ('AB000','AD000','AV000','AW000','AC000','AE000') AND E0_UNIT_FLAG='21' AND E0_UNIT_LEVEL='3' AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				// arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				// 因為沒有E0_UNIT_SORT所以改成分成兩階對撈資料
				// 改成先撈出直轄字排序在各自取出其勤指
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD NOT IN ('AW000','AB000','AD000','AV000','AC000','AE000') AND E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT";
				ArrayList<HashMap> list = pexecuteQuery(sql, null);
				for (HashMap<String, String> r : list)
				{
					sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD ='" + r.get("E0_UNIT_CD")
							+ "' AND E0_UNIT_LEVEL='3' AND E0_DELETE_FLAG=0 AND E0_UNIT_NM LIKE '%指%中心' ORDER BY E0_UNIT_SORT";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				}
			}
		} else if (notifyUnit1.equals("6"))
		{// 署屬各警察機關勤指
			if (notifyUnit2.equals("0"))
			{// 選署屬各警察機關勤指
				// sql =
				// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND (E0_UNIT_FLAG='50') AND E0_DELETE_FLAG=0 AND E0_UNIT_CD NOT IN ('A2700','A2800') ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				// gary說署屬各警察機關勤指只顯示第一階的勤指中心就好了
				// sql =
				// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE  ( ( E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('60','61') )  ) AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				// arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				// 因為沒有E0_UNIT_SORT所以改成分成兩階對撈資料
				// 改成先撈出署屬專業單位排序在各自取出其勤指
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT";
				ArrayList<HashMap> list = pexecuteQuery(sql, null);
				for (HashMap<String, String> r : list)
				{
					sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD ='" + r.get("E0_UNIT_CD")
							+ "' AND E0_UNIT_LEVEL='3' AND E0_DELETE_FLAG=0 AND E0_UNIT_NM LIKE '%指%中心' ORDER BY E0_UNIT_SORT";
					arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
				}
			} else
			{// 選擇署屬各警察機關的各個專業單位的勤指,這邊用不到了
				// sql =
				// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ( ( E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('60','61') ) OR ( E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG IN ('77','70','71') ) ) AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				// arrayList2JsonArray(jArray, pexecuteQuery(sql, new
				// Object[] { notifyUnit2 }));
			}
		} else if (notifyUnit1.equals("A")){
			if (notifyUnit2.equals("0")){
				//署內機關
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD = 'A1000'  AND E0_UNIT_FLAG = '91' AND E0_DELETE_FLAG=0 AND E0_UNIT_SORT <>'' AND E0_UNIT_SORT <>'0' ORDER BY E0_UNIT_SORT";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			}
		} else if (notifyUnit1.equalsIgnoreCase("all"))
		{// 單位一選全國勤務中心(all)時
			// sql =
			// "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG IN ('21','61') AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
			// arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			// 因為沒有E0_UNIT_SORT所以改成分成兩階對撈資料
			// 改成先撈出各主管單位排序在各自取出其勤指
			sql = "SELECT E0_UNIT_CD, E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE "
					+ " ((E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10')"
					+ " OR (E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00','A2U00'))"
					+ " ) AND E0_DELETE_FLAG=0 "
					+ " ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
			ArrayList<HashMap> list = pexecuteQuery(sql, null);
			for (HashMap<String, String> r : list)
			{
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD ='" + r.get("E0_UNIT_CD")
						+ "' AND E0_UNIT_LEVEL='3' AND E0_DELETE_FLAG=0 AND E0_UNIT_NM LIKE '%指%中心' ORDER BY E0_UNIT_SORT";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			}
		}
		return jArray;
	}

	// 舊的寫法
	public JSONArray queryNotifyUnit1Old(User user)
	{
		JSONObject jObj;
		JSONArray jArray = new JSONArray();
		/*
		 * jObj = new JSONObject(); jObj.put("E0_UNIT_CD", "0");
		 * jObj.put("E0_UNIT_S_NM", "單位一"); jArray.put(jObj);
		 */
		// 登入者若是警政署勤務指揮中心[A1101]就顯示
		if (user.getUnitCd1().equals("A1101"))
		{// 不知這樣判斷是否正確

		}
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "1");
		jObj.put("E0_UNIT_S_NM", "警政署勤務中心");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "all");
		jObj.put("E0_UNIT_S_NM", "全國勤務中心");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "4");
		jObj.put("E0_UNIT_S_NM", "直轄市警察局勤指");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "5");
		jObj.put("E0_UNIT_S_NM", "縣市警察局勤指");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "6");
		jObj.put("E0_UNIT_S_NM", "署屬各警察機關勤指");
		jArray.put(jObj);
		jObj = new JSONObject();
		jObj.put("E0_UNIT_CD", "2");
		jObj.put("E0_UNIT_S_NM", "專業單位");
		jArray.put(jObj);
		String sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG='10' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD ";
		arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
		return jArray;
	}

	// 舊的寫法
	public JSONArray queryNotifyUnit2Old(String notifyUnit1, String notifyUnit2)
	{
		JSONObject jObj;
		JSONArray jArray = new JSONArray();
		/*
		 * jObj = new JSONObject(); jObj.put("E0_UNIT_CD", "0");
		 * jObj.put("E0_UNIT_S_NM", "單位二"); jArray.put(jObj);
		 */
		String sql;
		if (notifyUnit1.length() == 5)
		{// 各縣市警局
			if (notifyUnit2.equals("0"))
			{// 選擇各縣市警局
				// 單位二的分局
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG='20' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				JSONArray jArrayUnit2 = arrayList2JsonArray(pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				// 各縣市警局的隸屬單位
				sql = "SELECT E0_UNIT_CD,E0_UNIT_S_NM,E0_FULL_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG IN ('21','22') AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				JSONArray jArrayFrom = arrayList2JsonArray(pexecuteQuery(sql, new Object[] { notifyUnit1 }));
				jArray.put(jArrayUnit2);// 單位二
				jArray.put(jArrayFrom);// 通報單位
			} else
			{// 選擇分局
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_BRANCH_CD=? AND E0_UNIT_FLAG!='20' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2 }));
			}

		} else if (notifyUnit1.equals("2"))
		{// 選專業單位時
			if (notifyUnit2.equals("0"))
			{// 選專業單位
				// sql =
				// "SELECT E0_UNIT_CD,E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG='50' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				sql = "SELECT E0_UNIT_CD, E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='50' AND E0_DELETE_FLAG=0 AND E0_UNIT_CD NOT IN ('A2700','A2800','A2L00','A2M00','A2O00') ORDER BY E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			} else
			{// 選擇單位二的各個專業單位
				// sql =
				// "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM,E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG!='50' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND E0_UNIT_FLAG!='50' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2 }));
			}
		} else if (notifyUnit1.equals("4"))
		{// 直轄市警察局
			if (notifyUnit2.equals("0"))
			{// 選直轄市警察局
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10' AND E0_UNIT_CD LIKE 'A%' ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			} else
			{// 選擇直轄市警察局的各個專業單位
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ((E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='31') OR (E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG='21')) AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2 }));
			}
		} else if (notifyUnit1.equals("5"))
		{// 縣市警察局
			if (notifyUnit2.equals("0"))
			{// 選縣市警察局
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND E0_UNIT_FLAG='10' AND E0_UNIT_CD LIKE 'B%' ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			} else
			{// 選擇縣市警察局的各個專業單位
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ((E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='31') OR (E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG='21')) AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2 }));
			}
		} else if (notifyUnit1.equals("6"))
		{// 署屬各警察機關
			if (notifyUnit2.equals("0"))
			{// 選署屬各警察機關
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_LEVEL='2' AND (E0_UNIT_FLAG='10' OR E0_UNIT_FLAG='50') AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, null));
			} else
			{// 選擇署屬各警察機關的各個專業單位
				sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM,E0_UNIT_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_DEPT_CD=? AND ((E0_UNIT_LEVEL='4' AND E0_UNIT_FLAG='31') OR (E0_UNIT_LEVEL='3' AND E0_UNIT_FLAG IN ('21','61'))) AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
				arrayList2JsonArray(jArray, pexecuteQuery(sql, new Object[] { notifyUnit2 }));
			}

		} else if (notifyUnit1.equalsIgnoreCase("all"))
		{// 單位一選全國勤務中心(all)時
			sql = "SELECT E0_UNIT_CD,E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_FLAG IN ('21','61') AND E0_UNIT_NM LIKE '%指%中心' AND E0_DELETE_FLAG=0 ORDER BY E0_UNIT_SORT,E0_UNIT_CD";
			arrayList2JsonArray(jArray, pexecuteQuery(sql, null));

		}
		/*
		 * String sql = "SELECT * FROM ABDB..E0DT_NPAUNIT " +
		 * " WHERE E0_DEPT_CD=? AND (E0_BRANCH_CD='' OR E0_BRANCH_CD=E0_DEPT_CD) AND E0_UNIT_CD<>E0_DEPT_CD"
		 * +" AND E0_DELETE_FLAG=0" +" ORDER BY E0_UNIT_CD"; ArrayList<HashMap>
		 * list = pexecuteQuery(sql, null); for (HashMap<String, String> e :
		 * list) { jObj = new JSONObject(); for (String col : e.keySet()) {
		 * jObj.put(col, e.get(col)); } jArray.put(jObj); }
		 */
		return jArray;
	}

	/**
	 * 紀錄回報時間等相關資料
	 * 
	 * @param user
	 * @param unit
	 * @param caseNo
	 * @param pkId
	 * @return
	 */
	public int reportTime(User user, String unit, String caseNo, String pkId)
	{
		HashMap r = this.pgetFirstRow("SELECT * FROM LEDT_BROADCAST WHERE LE_ID=?", new Object[] { Long.parseLong(pkId) });
		boolean isFirstRec = false;// 是否為第一位接報人
		if (r != null && nvl(r.get("LE_RECEIPT_ACCOUNT")).length() == 0)
		{
			isFirstRec = true;
		}
		int count = 0;
		String sysTime = DateUtil.getSystemTimeDB();
		String sql;
		if (isFirstRec)
		{// 第一位接報人
			sql = "UPDATE LEDT_BROADCAST SET LE_RECEIPT_ACCOUNT=?,LE_RECEIPT_NAME=?,LE_RECEIPT_UNIT=?,LE_RECEIPT_UNITNAME=?,LE_RECEIPT_IP=?,LE_RECEIPT_TIME=? "
					+ " ,LE_USER_ID=?,LE_USER_NAME=?,LE_USER_IP=?,LE_USER_UNIT_NM=?,LE_USER_UNIT_CD=?,LE_LOG_TIME=?,LE_USER_UNIT_CD1=?"
					+ " ,LE_RECEIPT_TIME1=?,LE_RECEIPT_ACCOUNT1=?,LE_RECEIPT_NAME1=?" + " WHERE LE_ID=?";
			count = this.pexecuteUpdate(sql,
					new Object[] { user.getUserId(), user.getUserName(), user.getUnitCd(), user.getUnitName(), user.getUserId(), sysTime, user.getUserId(), user.getUserName(),
							user.getUserIp(), user.getUnitName(), user.getUnitCd(), DateUtil.getSystemTimeDB(), user.getUnitCd1(), sysTime, user.getUserId(), user.getUserName(),
							Long.parseLong(pkId) });
		} else
		{// 非第一位接報人
			sql = "UPDATE LEDT_BROADCAST SET " + " LE_USER_ID=?,LE_USER_NAME=?,LE_USER_IP=?,LE_USER_UNIT_NM=?,LE_USER_UNIT_CD=?,LE_LOG_TIME=?,LE_USER_UNIT_CD1=?"
					+ " ,LE_RECEIPT_TIME1=?,LE_RECEIPT_ACCOUNT1=?,LE_RECEIPT_NAME1=?" + " WHERE LE_ID=?";
			count = this.pexecuteUpdate(sql,
					new Object[] { user.getUserId(), user.getUserName(), user.getUserIp(), user.getUnitName(), user.getUnitCd(), DateUtil.getSystemTimeDB(), user.getUnitCd1(),
							sysTime, user.getUserId(), user.getUserName(), Long.parseLong(pkId) });
		}
		sql = "INSERT INTO LEDT_BROADCAST_ALL(LE_CASE_NO,LE_ANN_TYPE,LE_ANN_RECEIPT,LE_RECEIPT_TYPE,LE_RECEIPT_TIME,LE_RECEIPT_ACCOUNT,LE_RECEIPT_NAME,LE_RECEIPT_UNIT,LE_RECEIPT_UNITNAME,LE_RECEIPT_IP) VALUES(?,?,?,?,?,?,?,?,?,?)";
		count += this.pexecuteUpdate(
				sql,
				new Object[] { caseNo, nvl(r.get("LE_ANN_TYPE")), unit, "0", sysTime, user.getUserId(), user.getUserName(), user.getUnitCd(), user.getUnitName(),
						user.getUserIp() });

		return count;
	}

	/**
	 * 查詢單位的代碼
	 * 
	 * @param unitName
	 *            單位中文名稱
	 * @return
	 */
	public String queryUnitCD(String unitName)
	{
		String returnStr = "";
		// A1101 警政署勤務中心
		if (unitName.equals("警政署勤務中心"))
		{
			returnStr = "A1101";
		} else
		{
			String sql = "SELECT E0_UNIT_CD FROM ABDB..E0DT_NPAUNIT WHERE (E0_FULL_S_NM = ? OR E0_UNIT_S_NM = ?)";
			returnStr = pgetData(sql, "E0_UNIT_CD", new Object[] { unitName, unitName });
		}
		return returnStr;
	}

	/**
	 * 查詢單位的中文
	 * 
	 * @param unitCD
	 *            單位代碼
	 * @return
	 */
	public String queryUnitName(String unitCD)
	{
		String returnStr = "";
		// A1101 警政署勤務中心
		if (unitCD.equals("A1101"))
		{
			returnStr = "警政署勤務指揮中心";
		} else
		{
			String sql = "SELECT E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_CD = ? ";
			returnStr = pgetData(sql, "E0_FULL_S_NM", new Object[] { unitCD });
		}
		return returnStr;
	}

	/**
	 * 查詢單位的中文全名
	 * 
	 * @param unitCD
	 *            單位代碼
	 * @return
	 */
	public String queryFullUnitName(String unitCD)
	{
		String returnStr = "";
		// A1101 警政署勤務中心
		if (unitCD.equals("A1101"))
		{
			returnStr = "警政署勤務指揮中心";
		} else
		{
			String sql = "SELECT E0_UNIT_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_CD = ? ";
			returnStr = pgetData(sql, "E0_UNIT_NM", new Object[] { unitCD });
		}
		return returnStr;
	}

	/**
	 * 查詢單位的中文縮寫
	 * 
	 * @param unitCD
	 *            單位代碼
	 * @return
	 */
	public String queryUnitShortName(String unitCD)
	{
		String returnStr = "";
		// A1101 警政署勤務中心
		if (unitCD.equals("A1101"))
		{
			returnStr = "警政署勤務中心";
		} else
		{
			String sql = "SELECT E0_FULL_S_NM FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_CD = ? ";
			returnStr = pgetData(sql, "E0_FULL_S_NM", new Object[] { unitCD });
		}
		return returnStr;
	}

	/**
	 * 
	 * @param str
	 *            組成查詢條件的StringBuilder
	 * @param obj
	 *            存放欄位的JSONObject
	 * @param colName
	 *            欄位英文名稱
	 * @param colName_TW
	 *            欄位中文名稱
	 * @return
	 */
	public String putCondition(StringBuilder str, JSONObject obj, String colName, String colName_TW)
	{
		String returnStr = "";
		if (obj.has(colName))
		{
			if (obj.getString(colName).length() > 0 && !obj.getString(colName).equals("null") && !obj.getString(colName).equals("-1"))
			{
				if (colName.equals("appUseType"))
				{
					if (obj.getString(colName).equals("600"))
					{
						returnStr = obj.getString("appUseCustom");
					} else
					{
						returnStr = queryAppUseName(obj.getString("appUseSubType"));
					}
				} else if (colName.equals("appUseType2"))
				{
					if (obj.getString(colName).equals("600"))
					{
						returnStr = obj.getString("appUseCustom2");
					} else
					{
						returnStr = queryAppUseName(obj.getString("appUseSubType2"));
					}
				} else
				{
					str.append("&" + colName_TW + "=" + obj.getString(colName));
				}
			}
		}
		return returnStr;
	}
        
        
                    /**
	 * 
	 * @param str
	 *            組成查詢條件的StringBuilder
	 * @param obj
	 *            存放欄位的JSONObject
	 * @param colName
	 *            欄位英文名稱(用"/"來分割)
	 * @param colName_TW
	 *            欄位中文名稱
	 * @return
	 */
	   public String putConditionSplit(StringBuilder str, JSONObject obj, String colName, String colName_TW) {
                            String returnStr = "";
                            String[] array = colName.split("/");
                            for (int i = 0; i < array.length; i++) {
                                if (obj.has(array[i])) {
                                    if (obj.getString(array[i]).length() > 0 && !obj.getString(array[i]).equals("null") && !obj.getString(array[i]).equals("-1")) {
                                        if (array[i].equals("appUseType")) {
                                            if (obj.getString(array[i]).equals("600")) {
                                                returnStr = obj.getString("appUseCustom");
                                            } else {
                                                returnStr = queryAppUseName(obj.getString("appUseSubType"));
                                            }
                                        } else if (array[i].equals("appUseType2")) {
                                            if (obj.getString(array[i]).equals("600")) {
                                                returnStr = obj.getString("appUseCustom2");
                                            } else {
                                                returnStr = queryAppUseName(obj.getString("appUseSubType2"));
                                            }
                                        } else {
                                            if(i==0){
                                                str.append("&" + colName_TW + "=" + obj.getString(array[i]));
                                            }else{
                                                str.append(obj.getString(array[i]));
                                            }
                                        }
                                   }
                                }
                            }

                            return returnStr;
                        }

	/**
	 * 查詢委託查詢的用途中文名稱
	 * 
	 * @return
	 */
	public String queryAppUseName(String appUnitCd)
	{
		String returnStr = "";
		String sql = "SELECT E0_USENAME FROM ABDB..E0DT_APPUSE WHERE E0_USECODE = ? ";
		returnStr = pgetData(sql, "E0_USENAME", new Object[] { appUnitCd });
		return returnStr;
	}
 	/**
	 * 2017/08/22
	 * chiayi
	 * 權限管理
	 *           
	 */       
        public JSONArray getRoleInfoBySystemType(String MB_SYSTEMTYPE, User user){
		String sql = "SELECT A.* FROM MBDT_ROLE A INNER JOIN MBDT_ROLE_SYSTEMTYPE B ON A.MB_ID=B.MB_ROLE_ID AND A.MB_ENABLED='Y' AND B.MB_SYSTEMTYPE=? ORDER BY MB_ORDER";
		Object[] objs = new Object[]{MB_SYSTEMTYPE};
		ArrayList<HashMap> mapList = pexecuteQuery(sql, objs);
		String usql = "SELECT E0_UNIT_LEVEL FROM ABDB..E0DT_NPAUNIT WHERE E0_UNIT_CD=?";
		Object[] objsA = new Object[]{user.getUnitCd()};
		String unitLevel = this.pgetData(usql, "E0_UNIT_LEVEL", objsA);
		ArrayList<HashMap> nmapList = new ArrayList<HashMap>();
		for(HashMap map : mapList){
			if(Integer.parseInt(map.get("MB_UNIT_LEVEL").toString())>=Integer.parseInt(unitLevel)){
				nmapList.add(map);
			}
		}
		return this.arrayList2JsonArray(nmapList);
	}
        /**
	 * 2017/08/22
	 * chiayi
	 * 權限管理
	 *           
	 */  
        public JSONArray getFuncRoleMappingBySystemType(String MB_SYSTEMTYPE, User user){
//		String rolesql = "SELECT A.* FROM MBDT_ROLE A INNER JOIN MBDT_ROLE_SYSTEMTYPE B ON A.MB_ID=B.MB_ROLE_ID AND A.MB_ENABLED='Y' AND B.MB_SYSTEMTYPE=? ORDER BY MB_ORDER";
		Object[] objs = new Object[]{MB_SYSTEMTYPE};
//		JSONArray roleArray = arrayList2JsonArray(pexecuteQuery(rolesql, objs));
		JSONArray roleArray = getRoleInfoBySystemType(MB_SYSTEMTYPE, user);
		StringBuilder funcsql = new StringBuilder();
		funcsql.append("SELECT A.MB_FUNC_ID AS FUNC_GROUP_ID,isnull(A.MB_ORDER,'') AS FUNC_GROUP_NO, A.MB_FUNC_NM AS FUNC_GROUP_NM,isnull(A.MB_FUNC_URL,'') AS FUNC_GROUP_URL,");
		funcsql.append("A.MB_ORDER||'.'||B.MB_ORDER AS FUNC_NO,B.MB_FUNC_NM,B.MB_FUNC_ID FROM (");
		funcsql.append("SELECT A.MB_SYSTEMTYPE_CODE,A.MB_SYSTEMTYPE_CNAME,A.MB_SYSTEMTYPE_ID,B.MB_FUNC_ID,B.MB_ORDER,");
		funcsql.append("B.MB_FUNC_NM,B.MB_FUNC_GROUP,B.MB_FUNC_URL FROM MBDT_SYSTEMTYPE A INNER JOIN ");
		funcsql.append("MBDT_FUNC B ON A.MB_SYSTEMTYPE=B.MB_SYSTEMTYPE AND A.MB_SYSTEMTYPE=?");
		funcsql.append(") A ");
		funcsql.append("LEFT JOIN (SELECT A.* FROM MBDT_FUNC A, MBDT_FUNC B WHERE ");
		funcsql.append("A.MB_FUNC_GROUP=B.MB_FUNC_ID AND A.MB_ENABLED='Y') B ON ");
		funcsql.append("A.MB_FUNC_ID = B.MB_FUNC_GROUP ORDER BY FUNC_GROUP_NO,CONVERT(INT,B.MB_ORDER)");
		ArrayList<HashMap> mapList = pexecuteQuery(funcsql.toString(), objs);
		JSONArray funcArray = new JSONArray();
		//for dealing with level2 functions
		List<String> groupIdList = new ArrayList<String>();
		for(HashMap map:mapList){
			if(!"".equals(map.get("FUNC_GROUP_URL"))){
				JSONObject groupObject = new JSONObject();
				groupObject.put("FUNC_NO", map.get("FUNC_GROUP_NO").toString());
				groupObject.put("MB_FUNC_NM", map.get("FUNC_GROUP_NM").toString());
				groupObject.put("MB_FUNC_ID", map.get("FUNC_GROUP_ID").toString());
				funcArray.put(groupObject);
				groupIdList.add(map.get("FUNC_GROUP_ID").toString());
			}
			if(!"".equals(map.get("MB_FUNC_NM").toString())){
				JSONObject groupObject = new JSONObject();
				groupObject.put("FUNC_NO", map.get("FUNC_NO").toString());
				groupObject.put("MB_FUNC_NM", map.get("MB_FUNC_NM").toString());
				groupObject.put("MB_FUNC_ID", map.get("MB_FUNC_ID").toString());
				funcArray.put(groupObject);
			}
		}
		JSONArray frMappingArray = new JSONArray();
		for(int j=0;j<funcArray.length();j++){
			for(int i=0;i<roleArray.length();i++){
				StringBuilder frsql = new StringBuilder();
				frsql.append("SELECT A.MB_ID,A.MB_FUNC,A.MB_ROLE,A.MB_ENABLE,A.MB_SYSTEMTYPE,A.MB_PERMISSION,");
				frsql.append("B.THREE_ORDER||'.'||B.MB_FUNC_NM AS MB_FUNC_NM FROM MBDT_FUNC_ROLE A ");
				frsql.append("INNER JOIN (");
				frsql.append("SELECT A.MB_ORDER||'.'||B.MB_ORDER AS THREE_ORDER,B.MB_FUNC_NM,B.MB_FUNC_ID FROM (");
				frsql.append("SELECT A.MB_SYSTEMTYPE_CODE,A.MB_SYSTEMTYPE_CNAME,A.MB_SYSTEMTYPE_ID,B.MB_FUNC_ID,B.MB_ORDER,");
				frsql.append("B.MB_FUNC_NM,B.MB_FUNC_GROUP,B.MB_FUNC_URL FROM MBDT_SYSTEMTYPE A INNER JOIN ");
				frsql.append("MBDT_FUNC B ON A.MB_SYSTEMTYPE=B.MB_SYSTEMTYPE AND A.MB_SYSTEMTYPE=?");
				frsql.append(") A ");
				frsql.append("LEFT JOIN (SELECT A.* FROM MBDT_FUNC A, MBDT_FUNC B WHERE ");
				frsql.append("A.MB_FUNC_GROUP=B.MB_FUNC_ID AND A.MB_ENABLED='Y') B ON ");
				frsql.append("A.MB_FUNC_ID = B.MB_FUNC_GROUP ) B ON A.MB_FUNC=B.MB_FUNC_ID AND B.MB_FUNC_ID=? ");
				frsql.append("INNER JOIN MBDT_ROLE C ON CONVERT(INT,A.MB_ROLE)=C.MB_ID AND C.MB_ID=?");
				String MB_FUNC_ID = funcArray.getJSONObject(j).getString("MB_FUNC_ID");
				Integer ROLE_ID = Integer.valueOf(roleArray.getJSONObject(i).getString("MB_ID"));
				Object[] frObjs = new Object[]{MB_SYSTEMTYPE, MB_FUNC_ID, ROLE_ID};
				ArrayList<HashMap> frMapList = pexecuteQuery(frsql.toString(), frObjs);
				JSONObject frObject = new JSONObject();
				if(null==frMapList || frMapList.size()==0){
					frObject.put("MB_ID", "");
					frObject.put("MB_FUNC", MB_FUNC_ID);
					frObject.put("MB_ROLE", String.valueOf(ROLE_ID));
					frObject.put("MB_ENABLE", "N");
					frObject.put("MB_SYSTEMTYPE", MB_SYSTEMTYPE);
					frObject.put("MB_PERMISSION", String.valueOf(roleArray.getJSONObject(i).getInt("MB_PERMISSION")));
					frObject.put("MB_FUNC_NM", funcArray.getJSONObject(j).getString("FUNC_NO")+" "+funcArray.getJSONObject(j).getString("MB_FUNC_NM"));
				}else{
					frObject.put("MB_ID", frMapList.get(0).get("MB_ID"));
					frObject.put("MB_FUNC", frMapList.get(0).get("MB_FUNC"));
					frObject.put("MB_ROLE", frMapList.get(0).get("MB_ROLE"));
					frObject.put("MB_ENABLE", frMapList.get(0).get("MB_ENABLE"));
					frObject.put("MB_SYSTEMTYPE", frMapList.get(0).get("MB_SYSTEMTYPE"));
					frObject.put("MB_PERMISSION", frMapList.get(0).get("MB_PERMISSION"));
					frObject.put("MB_FUNC_NM", funcArray.getJSONObject(j).getString("FUNC_NO")+" "+funcArray.getJSONObject(j).getString("MB_FUNC_NM"));
				}
				frMappingArray.put(frMappingArray.length(), frObject);
			}
			
		}
		return frMappingArray;
	}
        
        
}
