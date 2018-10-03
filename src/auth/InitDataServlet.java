package auth;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import base.BaseServlet;

import com.syscom.db.DBUtil;

/**
 * Servlet implementation class InitData
 */
public class InitDataServlet extends BaseServlet {
    protected static Logger log = Logger.getLogger(InitDataServlet.class);
    private static final long serialVersionUID = 1L;
    //private Scheduler scheduler;
    /**
     * @see BaseServlet#BaseServlet()
     */
    public InitDataServlet() {
	super();	
    }
    @Override
    public void init() throws ServletException {
	super.init();
	DBUtil db = new DBUtil();
	ServletContext ctx = this.getServletContext();

    }
    @Override
    public void destroy() {
	
    }
    
    /**
     * 執行查詢SQL，並且回傳使用ArrayList<HashMap>的查詢結果
     * 
     * @param sql
     *            欲查詢的SQL
     * @return 查詢結果ArrayList<HashMap>
     */
    protected ArrayList<HashMap> executeQuery(String sql) {
	DBUtil db = new DBUtil();
	ArrayList<HashMap> list = db.executeQuery(sql);

	return list;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
	String string = req.getRequestURI();
    }

}
