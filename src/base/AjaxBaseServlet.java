package base;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.ExceptionUtil;
import util.User;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import com.syscom.util.StringUtils;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;

/**
 * 之後可以改成不要用Servlet而是用像struts一樣的Action,就可以定義全域變數而不需要一直透過參數傳遞
 * 
 * @author Barry
 * 
 */
public abstract class AjaxBaseServlet extends BaseServlet {
    /**
     * Ajax從前端傳到後端時所指定的動作，key=ajaxAction。
     */
    public static final String AJAX_REQ_ACTION_KEY = "ajaxAction";

    /**
     * Ajax傳回到前端時預設使用的成功訊息Key，key=successMsg。
     */
    public static final String AJAX_RES_MSG_SUCCESS_KEY = "successMsg";
    /**
     * Ajax傳回到前端時預設使用的錯誤訊息Key，key=errorMsg。像是一般的新增,修改,刪除失敗的訊息。
     */
    public static final String AJAX_RES_MSG_ERROR_KEY = "errorMsg";
    /**
     * Ajax傳回到前端時預設使用的警告訊息Key，key=warnMsg。
     */
    public static final String AJAX_RES_MSG_WARN_KEY = "warnMsg";
    /**
     * Ajax傳回到前端時預設使用的一般訊息Key，key=infoMsg。
     */
    public static final String AJAX_RES_MSG_INFO_KEY = "infoMsg";
    /**
     * Ajax傳回到前端時預設使用的訊息Key，key=overRecordMsg。表示超過查詢筆數。
     */
    public static final String AJAX_RES_MSG_OVER_RECORD_KEY = "overRecordMsg";
    /**
     * Ajax傳回到前端時預設使用的錯誤訊息Key，key=exceptionMsg。
     */
    public static final String AJAX_RES_MSG_EXCEPTION_KEY = "exceptionMsg";
    /**
     * 後端程式要送到前端console顯示的訊息Key，key=debugMsg。
     */
    public static final String AJAX_RES_MSG_DEBUG_KEY = "debugMsg";
    /**
     * Ajax傳回到前端時預設使用的訊息物件名稱，key=msgData。
     */
    // public static final String AJAX_RES_MSG_OBJECT_KEY = "msgData";

    /**
     * Ajax傳回到jqGrid時填入欄位資料預設的Key，key=rows。
     */
    public static final String AJAX_RES_JQGRID_DATA_KEY = "rows";
    /**
     * Ajax傳回到前端時填入表單資料的Key，key=formData。
     */
    public static final String AJAX_RES_FORM_DATA_KEY = "formData";


    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
	JSONObject returnJasonObj = new JSONObject();
	//JSONArray jArray = null;
	JSONObject argJsonObj = new JSONObject();
	JSONObject msgJsonObj = new JSONObject();
        JSONArray argJsonArray = new JSONArray();
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	if (!isMultipart){
		// 將傳入的參數傳成json
		String para;
		Enumeration<String> paras = request.getParameterNames();
                if(null!=request.getParameter("isArray") && "Y".equals(request.getParameter("isArray"))){
                    convertToJsonArray(argJsonArray,request);
                }else{
                    while (paras.hasMoreElements()) {
                      para = paras.nextElement();
                      //argJsonObj.put(para, StringUtils.escapeHtml(request.getParameter(para)));
                      argJsonObj.put(para, StringUtils.encode(request.getParameter(para)));
                    }
                }
	}

	try {
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");
	   // executeAjax(request, response, session, user, argJsonObj, returnJasonObj);
           log.info("user in session = "+user);
	    if("Y".equals(request.getParameter("isArray"))){
	    	executeAjax(request, response, session, user, argJsonArray, returnJasonObj);
	    }else{
	    	executeAjax(request, response, session, user, argJsonObj, returnJasonObj);
	    }
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	    // msgJsonObj.put("exceptionMsg", ExceptionUtil.toString(e));//
	    // 存放列外的錯誤訊息
	    // msgJsonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, e.getMessage());//
	    // 存放列外的錯誤訊息
	    //returnJasonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, "系統發生錯誤:"+e.getMessage()+"\n詳細內容:"+ExceptionUtil.toString(e));
	    setExceptionMsg(returnJasonObj, e);
	} finally {
	    // returnJasonObj.put(AJAX_RES_MSG_OBJECT_KEY, msgJsonObj);
	    String accept = request.getHeader("Accept");
	    if (accept.contains("application/json")) {// 判斷Browser是否支援json
		response.setContentType("application/json");
	    } else {
		response.setContentType("text/html");// 設成text/html ie7才不會有問題
	    }
                response.setCharacterEncoding("UTF-8");
        if (argJsonObj.has("ajaxAction")){
                String strAjaxAction = argJsonObj.getString("ajaxAction");
            if (strAjaxAction.length()>=3 && strAjaxAction.substring(0,3).equals("TM0")){
                
            }else{
//                response.getWriter().write(returnJasonObj.toString());
//                response.getWriter().flush();
                ServletOutputStream out = response.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
                writer.write(returnJasonObj.toString());
                writer.flush();
//                writer.close();
//                response.getOutputStream().write(returnJasonObj.toString().getBytes("UTF-8"));
//                response.getOutputStream().flush();
            }
        }else{
//                response.getWriter().write(returnJasonObj.toString());
//                response.getWriter().flush();
                ServletOutputStream out = response.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
                writer.write(returnJasonObj.toString());
                writer.flush();
//                writer.close();
//                response.getOutputStream().write(returnJasonObj.toString().getBytes("UTF-8"));
//                response.getOutputStream().flush();
            }
	}

    }

    /**
     * 設定前端網頁的jqGrid資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setJqGridData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put(AJAX_RES_JQGRID_DATA_KEY, data);// rows存放jqGrid的資料
    }

    /**
     * 設定前端網頁的表單資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setFormData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put(AJAX_RES_FORM_DATA_KEY, data);// formData存放form表單的資料
    }

    /**
     * 設定前端網頁的訊息內容
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setReturnMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_SUCCESS_KEY, msg);//TODO:確定都是叫"儲存成功"嗎???
    }
    
    /**
     * Ajax傳回到前端時預設使用的錯誤訊息Key，key=errorMsg。像是一般的新增,修改,刪除失敗的訊息。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setErrorMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_ERROR_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的警告訊息Key，key=warnMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setWarnMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_WARN_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的一般訊息Key，key=infoMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setInfoMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_INFO_KEY, msg);
    }
    
    /**
     * Ajax傳回到前端時預設使用的訊息Key，key=overRecordMsg。表示超過查詢筆數。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setOverRecordMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_OVER_RECORD_KEY, msg);
    }
    
    /**
     * 後端程式要送到前端console顯示的訊息Key，key=debugMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setDebugMsg(JSONObject returnJasonObj, Object msg) {
	returnJasonObj.put(AJAX_RES_MSG_DEBUG_KEY, msg);
    }
    
    /**
     * 後端程式要送到前端顯示的訊息，key=exceptionMsg。
     * 
     * @param returnJasonObj
     * @param msg
     */
    public void setExceptionMsg(JSONObject returnJasonObj, Exception e) {
	returnJasonObj.put(AJAX_RES_MSG_EXCEPTION_KEY, "系統發生錯誤:"+e.getMessage()+"\n詳細內容:\n"+ExceptionUtil.toString(e));
    }
    /**
     * 設定前端網頁的DDL資料
     * 
     * @param returnJasonObj
     * @param data
     */
    public void setDDLData(JSONObject returnJasonObj, Object data) {
	returnJasonObj.put("result", data);// rows存放jqGrid的資料
    }
    /**
     * 請implements此method，盡量不要catch exception，若有需要則請在throws出來exception。
     * 程式的寫法可參考npa.le2.control.CrowdActivityReportServlet此範例程式
     * 
     * @param request
     *            原來的HttpServletRequest物件
     * @param response
     *            原來的HttpServletResponse物件
     * @param argJsonObj
     *            前端所傳送過來的參數請透過此jason物件取得，盡量不要使用request.getParameter
     * @param msgJsonObj
     *            存放要傳送到前端的成功與錯誤訊息
     * @param returnJasonObj
     *            存放前端畫面所需要使用的jqGrid與表單資料
     * @throws Exception
     */
    protected abstract void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
	    JSONObject argJsonObj, JSONObject returnJasonObj) throws Exception;
    
        /*
     * Implement this method when needed
     * @param request
     *            原來的HttpServletRequest物件
     * @param response
     *            原來的HttpServletResponse物件
     * @param argJsonObj
     *            前端所傳送過來的參數請透過此jason物件取得，盡量不要使用request.getParameter
     * @param msgJsonObj
     *            存放要傳送到前端的成功與錯誤訊息
     * @param returnJasonObj
     *            存放前端畫面所需要使用的jqGrid與表單資料
     * @throws Exception
     */
    protected  void executeAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user,
    	    JSONArray argJsonArray, JSONObject returnJasonObj) throws Exception
    {
    }
        /*
     * Using this method, you must be sure your data is in array mode
     */
    private void convertToJsonArray(JSONArray argJsonArray, HttpServletRequest request){
    	List<String> paraNameList = new ArrayList<String>();
    	Enumeration<String> paras = request.getParameterNames();
    	int num = 0;
    	while(paras.hasMoreElements()){
    		String paraName = paras.nextElement().toString();
    		if(paraName.contains("[")){
    			String rName = paraName.substring(0, paraName.indexOf("["));
    			int rNum = Integer.parseInt(paraName.substring(paraName.indexOf("[")+1, paraName.indexOf("]")));
    			if(!paraNameList.contains(rName)){
    				paraNameList.add(rName);
    			}else{
    				if(num<rNum){
    					num = rNum;
    				}
    			}
    		}
    	}
    	log.info("request array num = "+num);
    	for(int i=0;i<num+1;i++){
    		JSONObject jsonObject = new JSONObject();
    		for(int j=0;j<paraNameList.size();j++){
    			jsonObject.put(paraNameList.get(j), request.getParameter(paraNameList.get(j)+"["+i+"]"));
    		}
    		argJsonArray.put(jsonObject);
    	}
    }
}
