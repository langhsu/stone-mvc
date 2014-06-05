/**
 * 
 */
package stone.mvc.route;

import java.util.concurrent.ConcurrentLinkedQueue;

import stone.mvc.RouteInfo;
import stone.mvc.annotation.HttpMethod;

/**
 * @author langhsu
 *
 */
public interface Router {
	/**
	 * 
	 * @param packages
	 * @param controllerFilters
	 * @return
	 */
	ConcurrentLinkedQueue<RouteInfo> findController(String packages, String controllerFilters);
	
	/**
	 * 
	 * @param path
	 * @param method
	 * @return
	 */
	RouteInfo lookup(String path, HttpMethod method);
	
	/**
	 * 
	 * @return
	 */
	ConcurrentLinkedQueue<RouteInfo> getRoutes();
}
