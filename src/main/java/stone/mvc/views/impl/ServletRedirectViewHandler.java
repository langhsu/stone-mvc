package stone.mvc.views.impl;

import java.io.IOException;

import stone.mvc.RequestContext;
import stone.mvc.views.ViewHandler;
import stone.utils.PathUtils;

public class ServletRedirectViewHandler implements ViewHandler {

    @Override
    public String getType() {
        return "redirect";
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public void render(RequestContext ctx, String viewPathName) throws IOException {
        // 转换相对路径为绝对路径
        viewPathName = PathUtils.getRelativePath(ctx.getPath(), viewPathName);

        if (viewPathName.charAt(0) == '/') {
            // 添加 context path
            viewPathName = ctx.getHttpServletRequest().getContextPath() + viewPathName;
        }
        ctx.getHttpServletResponse().sendRedirect(viewPathName);
    }
}
