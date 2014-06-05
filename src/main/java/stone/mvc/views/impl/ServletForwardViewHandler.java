package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stone.mvc.RequestContext;
import stone.mvc.views.ViewHandler;
import stone.utils.PathUtils;

public class ServletForwardViewHandler implements ViewHandler {

    @Override
    public String getType() {
        return "forward";
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public void render(RequestContext ctx, String viewPathName) throws Exception {
        HttpServletRequest request = ctx.getHttpServletRequest();
        HttpServletResponse response = ctx.getHttpServletResponse();

        // 转换相对路径为绝对路径
        viewPathName = PathUtils.getRelativePath(ctx.getPath(), viewPathName);
        request.getRequestDispatcher(viewPathName).forward(request, response);
    }

}
