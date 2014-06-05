package stone.mvc.dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import stone.mvc.RequestContext;
import stone.mvc.RouteInfo;
import stone.mvc.annotation.HttpMethod;
import stone.mvc.result.ResultHandler;
import stone.mvc.result.ResultHandlerResolver;
import stone.mvc.route.RouteHandler;
import stone.mvc.route.Router;
import stone.mvc.route.SimpleRouter;
import stone.utils.WebUtil;

/**
 * 
 * @author langhsu
 *
 */
public class DispatcherFilter implements Filter {
	private static final Logger log = Logger.getLogger(DispatcherFilter.class);
	
	private static final String INIT_ACTION_PACKAGES = "actionPackages";
	
	private static final String INIT_COMTROLLER_FILTERS = "controllerFilters";
	
	private static final String INIT_VIEW_RENDER = "viewRender";
	
	private static final String INIT_SUPPORT_SPRING = "isSupportSpring";
	
	private String actionPackages = ""; // 扫描路径

	private String controllerFilters = ""; // Controller 过滤

	private static Boolean isSupportSpring;

	private Router router;
	
	private ResultHandlerResolver resultHandler;
	
	private FilterConfig filterConfig;
	private ServletContext servletContext;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		log.info("[info] init framework starting...");
		
		filterConfig = config;
		
		actionPackages = config.getInitParameter(INIT_ACTION_PACKAGES);
		controllerFilters = config.getInitParameter(INIT_COMTROLLER_FILTERS);
		
		isSupportSpring = Boolean.parseBoolean(config.getInitParameter(INIT_SUPPORT_SPRING));
		
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
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// cast
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        
        String path = req.getServletPath();
        String method = req.getMethod().toUpperCase();
        
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
				
				return;
			}
		} catch (Exception e) {
			handleActionMethodException(req, res, e);
		} finally {
			RequestContext.cleaup();
		}
		
		// 调用其它Filter
        filterChain.doFilter(req, res);
	}

	
	@Override
	public void destroy() {
	}
	
	protected final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext);
	}
	
	/**
	 * 1. Ajax 发送 403 错误
	 * 2. Other 重定向到首页
	 * @param q Request
	 * @param p Response
	 * @param e Exception
	 */
	private void handleActionMethodException(HttpServletRequest q, HttpServletResponse p, Exception e) {
		if (WebUtil.isAJAX(q)) {
			WebUtil.sendError(403, p, e.getMessage());
			q.setAttribute("javax.servlet.error.exception", e);
		} else {
			WebUtil.redirectRequest(q.getContextPath() + "/", p);
		}
	}
}