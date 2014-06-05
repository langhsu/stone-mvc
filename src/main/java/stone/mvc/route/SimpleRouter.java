package stone.mvc.route;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import stone.mvc.RouteInfo;
import stone.mvc.annotation.HttpMethod;
import stone.mvc.annotation.Route;
import stone.utils.HttpUtil;

/**
 * Router
 * 
 * @author langhsu
 *
 */
public class SimpleRouter implements Router {
	private ConcurrentLinkedQueue<RouteInfo> routes;
	
	// group by Http method
	private final RouteMatcher[] matchers = new RouteMatcher[HttpMethod.values().length];
	
	@Override
	public ConcurrentLinkedQueue<RouteInfo> findController(String packages, String controllerFilters) {
		routes = new ConcurrentLinkedQueue<RouteInfo>();
		List<String> classFilters = new ArrayList<String>();
		classFilters.add(controllerFilters);
		
		ClassScanner scan = new ClassScanner(true, classFilters);
		
		// Recursive scanning
		Set<Class<?>> clazzList = scan.findPkgClassAll(packages, true);
		
		for (Class<?> clazz : clazzList) {
			Method[] methods = clazz.getMethods();
			
			// 循环获取所有的方法(非静态)
			for (Method method : methods) {
				if (Modifier.isStatic(method.getModifiers())) {
					continue;
				}
				
				if (method.isAnnotationPresent(Route.class)) {
					RouteInfo routeInfo = new RouteInfo();
					Route ann = method.getAnnotation(Route.class);
					
					if (null == ann) {
						continue;
					}
					
					//TODO:
					//Class<?> ret = TypeResolverUtils.getRawType(method.getGenericReturnType(), clazz);
					
					routeInfo.setClazz(clazz.getName());
					routeInfo.setMethods(clazz.getMethods());
					routeInfo.setMethod(method.getName());

					Route pathClazz = null;
					if (clazz.isAnnotationPresent(Route.class)) {
						pathClazz = clazz.getAnnotation(Route.class);
						String path = ((null != pathClazz) ? HttpUtil.deleteRightBar(pathClazz.value()) : "")
								+ HttpUtil.deleteRightBar(ann.value());
						routeInfo.setPath(path.trim());
						routeInfo.setPathLength(path.split("/").length);
						routeInfo.setInterceptor(pathClazz.cls());
					}
					
					HttpMethod[] httpMethods = ann.method();
					
					if (httpMethods.length > 0) {
						routeInfo.setHttpMethod(httpMethods.toString());
						routeInfo.setReturnType(ann.resultType().toString());
					}
					
					for (HttpMethod hm : httpMethods) {
						RouteMatcher matcher = matchers[hm.getIndex()];
						if (matcher == null) {
							matcher = new RouteMatcher(hm);
							matchers[hm.getIndex()] = matcher;
						}
						matcher.register(routeInfo);
					}
					routes.add(routeInfo);
				}
			}
		}
		return routes;
	}
	
	@Override
	public RouteInfo lookup(String path, HttpMethod method) {
		RouteMatcher matcher = matchers[method.getIndex()];
        if (matcher != null) {
            return matcher.lookup(path, method);
        }
		return null;
	}

	public ConcurrentLinkedQueue<RouteInfo> getRoutes() {
		return routes;
	}

}
