/**
 * 
 */
package stone.mvc.result;

import stone.mvc.RequestContext;

/**
 * @author langhsu
 *
 */
public interface ResultHandler<T> {
	void init();
	void handle(RequestContext ctx, T result) throws Exception;
}
