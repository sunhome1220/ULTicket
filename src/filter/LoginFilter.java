package filter;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import util.ExceptionUtil;
import util.User;
import org.apache.log4j.Logger;
import util.DBUtil;

/**
 * Servlet Filter implementation class LoginFilter
 */

public class LoginFilter implements Filter {
    protected static Logger log = Logger.getLogger(LoginFilter.class);
    protected FilterConfig filterConfig = null;
    protected boolean isEnabled = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.filterConfig = filterConfig;
	String value = filterConfig.getInitParameter("isEnabled");
	if (value == null) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("true")) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("yes")) {
	    this.isEnabled = true;
	} else if (value.equalsIgnoreCase("y")) {
	    this.isEnabled = true;
	} else {
	    this.isEnabled = false;
	}
    }

    @Override
    public void destroy() {
	this.filterConfig = null;
    }

    @SuppressWarnings({ "unused" })
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	HttpServletResponse res = (HttpServletResponse) response;
	HttpSession session = req.getSession();
	String requestURI = req.getRequestURI();
	if(session.isNew()){
            // 填入預設的Session資料
            // 取得 IP ============================
            String ip = req.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = req.getHeader("Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = req.getHeader("WL-Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = req.getHeader("HTTP_CLIENT_IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = req.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = req.getRemoteAddr();   
            User user = (User) session.getAttribute("user");
			// 是否登入
            if (user != null) {       
                //addLoginHistory(session.getId(), user.getUserId(), user.getUnitCd(), ip);
            }
	}
	
	if (isEnabled) {
	    String pageName = null;
	    String dirName = null;
	    boolean isJSP = false;
	    boolean isIndex = false;
	    boolean isException = false;
	    if (requestURI.endsWith(".jsp")) {
		isJSP = true;
		if (
                       requestURI.contains("index.jsp")
                    || requestURI.contains("SecureLogin.jsp")				
                    || requestURI.contains("AuthServlet")				
                    || requestURI.contains("SessionTimeout.jsp")
                    || requestURI.contains("DocumentModeNotSupport.jsp") 
                    || requestURI.contains("BrowserNotSupport.jsp")
                    || requestURI.contains("CaseRealtimeImport.jsp") 
                    || requestURI.contains("Detect.jsp") 
                    || requestURI.contains("login.jsp")
                    || requestURI.contains("reqTickAudi.jsp")//20181028觀眾索票，不需登入
                    || requestURI.contains("app.jsp")
                    || requestURI.contains("app2.jsp")
                    || requestURI.contains("dbConnTest.jsp") 
                    || requestURI.contains("ServerTest.jsp") 
                    || requestURI.contains("version.jsp") 
                    || requestURI.contains("MainFrameset.jsp")	
                    || requestURI.contains("NoSSOLogin.jsp")
                        )
		    isIndex = true;
		if (requestURI.contains("exception.jsp"))
		    isException = true;
		if (!isIndex && !isException) {// 非index and 非exception頁面才檢查
		    try {
			// 從URI取得request的頁面名稱
			int lastIndex = requestURI.lastIndexOf("/");
			if (lastIndex != requestURI.length() - 1) {
			    pageName = requestURI.substring(lastIndex + 1);
			    int last2Index = requestURI.lastIndexOf("/", lastIndex - 1);
			    if (last2Index != -1) {
				dirName = requestURI.substring(last2Index + 1, lastIndex);
			    }
			}
			User user = (User) session.getAttribute("user");
			// 是否登入
			if (user != null) {                 
                            if(user.getLoginStatus()==0){
                                String ip2 = getClientIp(req);
                                //addLoginHistory(session.getId(), user.getUserId(), user.getUnitCd(), ip2); 
                                user.setLoginStatus(1);
                                session.setAttribute("user", user);
                            }
			    chain.doFilter(request, response);// 登入過就繼續
			} else {
			    String viewerURL = "login.jsp";
                            String clientIp = getClientIp(req);
//                            //TODO test
//                                user = new User();
////                                user.setUserId("TEST001");
//                                user.setLoginStatus(0);
//                                session.setAttribute("user", user);
//                                //TODO test
                            if(clientIp.equals("127.0.0.1") || clientIp.equals("0:0:0:0:0:0:0:1")){
                                
                                viewerURL = "login.jsp";//是local
                                //viewerURL = "app.jsp";//是local
                            }else{
                                log.debug("client not local!");
                            }
			    res.sendRedirect(viewerURL);			    
			}
		    } catch (Exception e) {
			log.error(ExceptionUtil.toString(e));
		    }
		} else {// 不需要經過login的頁面
		    chain.doFilter(request, response);
		}
	    } else {// 不需要經過login的頁面
		chain.doFilter(request, response);
	    }

	} else {
	    chain.doFilter(request, response);
	}

    }
    private String getClientIp(HttpServletRequest req) {
        String clientIp;
        // 填入預設的Session資料
        // 取得 IP ============================
        clientIp = req.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp))
            clientIp = req.getHeader("Proxy-Client-IP");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp))
            clientIp = req.getHeader("WL-Proxy-Client-IP");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp))
            clientIp = req.getHeader("HTTP_CLIENT_IP");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp))
            clientIp = req.getHeader("HTTP_X_FORWARDED_FOR");
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp))
            clientIp = req.getRemoteAddr();
        return clientIp;
    }

//    private void addLoginHistory(String id, String userId, String unitCd, String ip) {
//        String sql = "INSERT INTO CJDT_LOGIN_HISTORY(SESSIONID, USERID, CJ_UNITCODE, REMOTEADDR, LOGINTIME, LOGOUTTIME) "
//                + "VALUES(?, ?, ?, ?, GETDATE(), DATEADD(MI,1,GETDATE()))";
//        DBUtil.getInstance().pexecuteUpdate(sql,new Object[]{id, userId, unitCd, ip});        
//    }
}
