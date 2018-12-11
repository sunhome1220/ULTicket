package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.User;

import org.apache.log4j.Logger;

/**
 * 本Filter專門用來檢查client端的ajax是否有登入系統
 */

public class AjaxSessionExpirationFilter implements Filter {
    protected static Logger log = Logger.getLogger(AjaxSessionExpirationFilter.class);
    protected FilterConfig filterConfig = null;
    protected boolean isEnabled = true;
    private int customSessionExpiredErrorCode = 901;//not login/ timeout

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

	customSessionExpiredErrorCode = Integer
		.parseInt(filterConfig.getInitParameter("customSessionExpiredErrorCode"));
    }

    @Override
    public void destroy() {
	this.filterConfig = null;
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
     
	if (isEnabled) {
	    HttpSession session = ((HttpServletRequest) request).getSession();
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
		String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
		String requestURI = ((HttpServletRequest) request).getRequestURI();
//                System.out.println("requestURI:"+requestURI);
                boolean isSecureLogin = false;
                String header = ((HttpServletRequest) request).getHeader("referer");
                if(header != null 
                        && (header.contains("SecureLogin.jsp") || header.contains("Audi.jsp"))
                        ){
                    isSecureLogin = true;
                }
		// 需排除首頁會做的ajax
		// SysParamServlet,ChangeUnitServlet,UserApplyServlet是不需要登入就可以使用的ajax
		if ("XMLHttpRequest".equals(ajaxHeader) && !requestURI.contains("SysParamServlet") && !isSecureLogin
			&& !requestURI.contains("ChangeUnitServlet") 
			&& !requestURI.contains("AuthServlet") 
                        && !requestURI.contains("UserApplyServlet") 
                        && !requestURI.contains("ForgetPwdServlet")) {
		    //NPAUtil.showRequestInfo((HttpServletRequest)request);
		    log.info("偵測到Ajax呼叫,但沒有登入系統因此送出error code{" + customSessionExpiredErrorCode + "}");
		    HttpServletResponse resp = (HttpServletResponse) response;
		    resp.sendError(this.customSessionExpiredErrorCode);
                    //resp.sendRedirect("login.jsp");
		} else {
		    chain.doFilter(request, response);
		}
	    } else {
		chain.doFilter(request, response);
	    }

	} else {
	    chain.doFilter(request, response);
	}
    }

}
