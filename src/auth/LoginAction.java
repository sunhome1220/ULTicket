package auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.Action;

import base.BaseActionSupport;
import dao.AuthDao;

public class LoginAction extends BaseActionSupport {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pwd;
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	    throws Exception {
	String rtn = Action.SUCCESS;
	if(nvl(id).length()>0){
	    id=id.toUpperCase();
	}
	if (!nvl(id).equals("") && !nvl(pwd).equals("")) {

	    // 取得 IP ============================	    
	    String ip = request.getHeader("X-Forwarded-For");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		ip = request.getHeader("Proxy-Client-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		ip = request.getHeader("WL-Proxy-Client-IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		ip = request.getHeader("HTTP_CLIENT_IP");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");

	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		ip = request.getRemoteAddr();
	    
	    log.debug("X-Forwarded-For : "+request.getHeader("X-Forwarded-For"));
	    log.debug("Proxy-Client-IP : "+request.getHeader("Proxy-Client-IP"));
	    log.debug("WL-Proxy-Client-IP : "+request.getHeader("WL-Proxy-Client-IP"));
	    log.debug("HTTP_CLIENT_IP : "+request.getHeader("HTTP_CLIENT_IP"));
	    log.debug("HTTP_X_FORWARDED_FOR : "+request.getHeader("HTTP_X_FORWARDED_FOR"));
	    log.debug("ip : "+ip);
	    // ==================================

	    AuthDao auth = AuthDao.getInstance();
	    request.setAttribute("id", id);
	    String msg = auth.chkUser(id, pwd, session, true);
	    //String msg = "發生錯誤!!";
	    if (msg.length() == 0) {
		session.setAttribute("isLogin", "Y");// 這樣才會顯示SpotMap.jsp
	    } else {// msg="reset";
		    // setErrorMsg(request, "帳號或密碼錯誤!!");
		setErrorMsg(request, msg);
		rtn = Action.ERROR;
	    }
	} else {
	    setWarnMsg(request, "帳號或密碼請勿空白!!");
	    rtn = Action.ERROR;
	}

	return rtn;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getPwd() {
	return pwd;
    }

    public void setPwd(String pwd) {
	this.pwd = pwd;
    }

}
