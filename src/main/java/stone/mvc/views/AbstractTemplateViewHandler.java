package stone.mvc.views;

import stone.mvc.RequestContext;
import stone.utils.PathUtils;

public abstract class AbstractTemplateViewHandler implements ViewHandler {

    public abstract String getPrefix();

    @Override
    public void render(RequestContext ctx, String viewPathName) throws Exception {
        // 转换相对路径为绝对路径
        String view = PathUtils.getRelativePath(ctx.getPath(), viewPathName);

        if (view.endsWith("/")) {
            view = view.concat("index");
        }

        String prefix = getPrefix();
        if (prefix != null) {
        	if (!view.startsWith(prefix)) {
        		view = prefix + view;
            }
        }
        
        if (!view.endsWith(getSuffix())) {
        	view = view + getSuffix();
        }

        doRender(ctx, view);
    }

    protected abstract void doRender(RequestContext ctx, String viewPathName) throws Exception;

}
