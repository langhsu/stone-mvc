/**
 * 
 */
package stone.mvc.views;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import stone.mvc.views.impl.HtmlDataViewHandler;
import stone.mvc.views.impl.JsonDataViewHandler;
import stone.mvc.views.impl.JspTemplateViewHandler;
import stone.mvc.views.impl.PlainDataViewHandler;
import stone.mvc.views.impl.ServletForwardViewHandler;
import stone.mvc.views.impl.ServletRedirectViewHandler;
import stone.mvc.views.impl.XmlDataViewHandler;


/**
 * @author langhsu
 *
 */
public class ViewHandlerResolver {
	private final Map<String, ViewHandler> mapping = new HashMap<String, ViewHandler>();
	
	public void init() throws InstantiationException, IllegalAccessException {
		register(ServletForwardViewHandler.class);
        register(ServletRedirectViewHandler.class);
        register(PlainDataViewHandler.class);
        register(HtmlDataViewHandler.class);
        register(XmlDataViewHandler.class);
        register(JsonDataViewHandler.class);
        register(JspTemplateViewHandler.class);
    }
	
	public void register(Class<?> viewHandlerClass) throws InstantiationException, IllegalAccessException {
        if (ViewHandler.class.isAssignableFrom(viewHandlerClass)) {
        	ViewHandler viewHandler = (ViewHandler) viewHandlerClass.newInstance();

            mapping.put(viewHandler.getType(), viewHandler);
            String suffix = viewHandler.getSuffix();
            if (suffix != null) {
                suffix = StringUtils.removeStart(suffix, ".");
                mapping.put(suffix, viewHandler);
            }
        }
    }

    public ViewHandler lookup(String type) {
        return mapping.get(type);
    }
}
