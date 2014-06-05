/**
 * 
 */
package stone.mvc;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author langhsu
 * 
 */
public class RequestContext implements Serializable {
	private static final long serialVersionUID = 6845732790452856925L;

	private static ThreadLocal<RequestContext> actionContext = new ThreadLocal<RequestContext>();

    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    private String path;
    
    //TODO:
    //private ValueStack values;
    
    public ServletContext getServletContext() {
        return context;
    }
    
    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

    public HttpSession getHttpSession() {
        return request.getSession();
    }

    public static RequestContext getRequestContext() {
        return actionContext.get();
    }

    public static void setRequestContext(ServletContext context, HttpServletRequest request, HttpServletResponse response, String path) {
    	RequestContext rc = new RequestContext();
    	rc.context = context;
    	rc.request = request;
    	rc.response = response;
    	rc.path = path;
        actionContext.set(rc);
    }

    public static void cleaup() {
    	actionContext.remove();
    }

	public String getPath() {
		return path;
	}

}
