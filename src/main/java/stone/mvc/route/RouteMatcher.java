/**
 * 
 */
package stone.mvc.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import stone.mvc.RouteInfo;
import stone.mvc.annotation.HttpMethod;
import stone.utils.HttpUtil;

/**
 * Route Matcher
 * 
 * @author langhsu
 *
 */
public class RouteMatcher {
	private ConcurrentLinkedQueue<RouteInfo> routes;
	private HttpMethod method;
	
	public RouteMatcher (HttpMethod method){
		this.method = method;
		this.routes = new ConcurrentLinkedQueue<RouteInfo>();
	}
	
	public void register(RouteInfo route) {
		routes.add(route);
	}
	
	public RouteInfo lookup(String path, HttpMethod method) {
		String url = HttpUtil.deleteRightBar(path);
		String[] urlSegments = url.split("/");
		int segLen = urlSegments.length;
		for (RouteInfo route : routes) {
			String routePath = route.getPath();
			if (!routePath.contains("{") && !routePath.contains("}")) {
				if (routePath.equals(url)) {// 没有URL作为参数匹配
					return route;
				}
			} else {
				if (routePath.contains("{") && routePath.contains("}") && route.getPathLength() == segLen) {
					
					String[] mUrls = HttpUtil.deleteRightBar(routePath).split("/");
					Map<String, Object> params = new HashMap<String, Object>();
					List<String> results = new ArrayList<String>();
					
					for (int i = 0; i < mUrls.length; i++) {
						if (!mUrls[i].equals(urlSegments[i])) {
							
							// {demo}.html
							if (mUrls[i].contains(".") && mUrls[i].contains("{") && mUrls[i].contains("}")) {
								String value = urlSegments[i].substring(0, urlSegments[i].indexOf("."));
								String key = mUrls[i].substring(mUrls[i].indexOf("{") + 1, mUrls[i].indexOf("}"));
								params.put(key, value);
							} else if (!mUrls[i].contains(".") && mUrls[i].contains("{") && mUrls[i].contains("}")) {
								params.put(mUrls[i].substring(1,
										mUrls[i].length() - 1), HttpUtil.deleteBigBrackets(urlSegments[i]));
							}

						} else {
							results.add(mUrls[i]);
						}
					}
					if (results.size() + params.keySet().size() == mUrls.length) {
						route.setParams(params);
						return route;
					}
				}
			}
		}
		return null;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}
	
}
