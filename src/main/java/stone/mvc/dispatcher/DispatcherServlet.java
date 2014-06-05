package stone.mvc.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stone.StoneVersion;
import stone.mvc.RequestContext;
import stone.mvc.RouteInfo;
import stone.mvc.annotation.HttpMethod;
import stone.mvc.result.ResultHandler;
import stone.mvc.result.ResultHandlerResolver;
import stone.mvc.route.RouteHandler;
import stone.mvc.route.Router;
import stone.mvc.route.SimpleRouter;

/**
 * 
 * @author langhsu
 * 
 */
public class DispatcherServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7764509477404273011L;
	
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
	
	// web.xml 配置信息
	private static final String INIT_ROUTE_PACKAGES = "routePackages";
	
	private static final String INIT_COMTROLLER_FILTERS = "controllerFilters";
	
	private static final String INIT_VIEW_RENDER = "viewRender";
	
	private static final String INIT_SUPPORT_SPRING = "isSupportSpring";
	
	// Defined
	private String actionPackages = ""; // 扫描路径

	private String controllerFilters = ""; // Controller 过滤

	private static Boolean isSupportSpring;

	private Router router;
	
	private ResultHandlerResolver resultHandler;
	
	@Override
	public void init() throws ServletException {
		log.info("[info] init framework starting...");
		
		log.info("[info] java.version = {}", System.getProperty("java.version"));
        log.info("[info] stone.version = {}", StoneVersion.getVersion());
        
		actionPackages = getInitParameter(INIT_ROUTE_PACKAGES);
		controllerFilters = getInitParameter(INIT_COMTROLLER_FILTERS);
		
		isSupportSpring = Boolean.parseBoolean(getInitParameter(INIT_SUPPORT_SPRING));
		
		router = new SimpleRouter();
		router.findController(actionPackages, controllerFilters);
		
		resultHandler = new ResultHandlerResolver();
		try {
			resultHandler.init();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		log.info("[info] init framework completed");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String method = req.getMethod().toUpperCase();
		String path = req.getServletPath();
		
		RequestContext.setRequestContext(getServletContext(), req, res, path);

		Object result = "";
		
		RequestContext context = RequestContext.getRequestContext();
		try {
			HttpMethod httpMethod = HttpMethod.valueOf(method);
			
			RouteInfo routeInfo = router.lookup(path, httpMethod);

			if (null != routeInfo) {
				RouteHandler handler = new RouteHandler();
				result = handler.methodInvoke(routeInfo, isSupportSpring);
				
				resultHandler.supported(result.getClass());
				
				ResultHandler rh = resultHandler.lookup(result.getClass());
				rh.handle(context, result);
			} else {
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				throw new ServletException(String.format("The requested URL %s was not found on this server.",
						path));
			}
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			RequestContext.cleaup();
		}

	}

	@Override
	public void destroy() {
	}
	
}
