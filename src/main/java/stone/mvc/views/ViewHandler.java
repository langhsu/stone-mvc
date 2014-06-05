/**
 * 
 */
package stone.mvc.views;

import stone.mvc.RequestContext;

/**
 * @author langhsu
 *
 */
public interface ViewHandler {
	/**
	 * 
	 * @return
	 */
    public String getType();

    /**
     * 
     * @return
     */
    public String getSuffix();

    /**
     * 
     * @param ctx
     * @param viewPathName
     * @throws Exception
     */
    public void render(RequestContext ctx, String viewPathName) throws Exception;
}
