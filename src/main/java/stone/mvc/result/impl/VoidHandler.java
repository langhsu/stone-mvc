/**
 * 
 */
package stone.mvc.result.impl;

import stone.mvc.RequestContext;
import stone.mvc.result.ResultHandler;

/**
 * @author langhsu
 *
 */
public class VoidHandler implements ResultHandler<Void> {

	@Override
	public void init() {
	}
	
	@Override
	public void handle(RequestContext ctx, Void result) throws Exception {
		
	}

}
