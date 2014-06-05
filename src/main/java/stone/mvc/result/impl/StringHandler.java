/**
 * 
 */
package stone.mvc.result.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import stone.exception.StoneWebException;
import stone.mvc.RequestContext;
import stone.mvc.result.ResultHandler;
import stone.mvc.views.ViewHandler;
import stone.mvc.views.ViewHandlerResolver;

/**
 * @author langhsu
 * 
 */
public class StringHandler implements ResultHandler<String> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ViewHandlerResolver viewHandlerResolver;

	private String defaultType = "jsp";

	private ViewHandler defaultHandler;

	@Override
	public void init() {
		viewHandlerResolver = new ViewHandlerResolver();
		try {
			viewHandlerResolver.init();

			defaultHandler = viewHandlerResolver.lookup(defaultType);
			if (defaultHandler == null) {
				throw new IllegalStateException(
						"Cannot find the default view resolver: " + defaultType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void handle(RequestContext ctx, String result) throws Exception {
		ViewHandler viewHandler = null;

		String url;
		if (result == null) {
			url = ctx.getPath();
		} else {
			int ipos = result.indexOf(':');
			if (ipos > 0) {
				// 根据 URL 前缀查找 view
				String type = result.substring(0, ipos);
				url = result.substring(ipos + 1);
				viewHandler = viewHandlerResolver.lookup(type);
				if (viewHandler == null) {
					throw new StoneWebException(
							"Can't find view resolver for path: " + result);
				}
			} else {
				url = result;
			}
		}

		if (viewHandler == null) {
			// 根据后缀名查找 view
			String suffix = getFileExtension(result);
			if (suffix != null) {
				viewHandler = viewHandlerResolver.lookup(suffix);
				if (viewHandler == null) {
					throw new StoneWebException("Can't find view resolver for path: " + result);
				}
			}
		}

		if (viewHandler == null) {
			// 使用默认配置 view
			viewHandler = defaultHandler;
		}

		viewHandler.render(ctx, url);
	}

	public static String getFileExtension(final String path) {
		if (path == null) {
			return null;
		}
		int extIndex = path.lastIndexOf('.');
		if (extIndex == -1) {
			return null;
		}
		return path.substring(extIndex + 1);
	}
}
