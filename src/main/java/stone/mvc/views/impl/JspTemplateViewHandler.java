package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.exception.ResourceNotFoundException;

import stone.mvc.RequestContext;
import stone.mvc.views.AbstractTemplateViewHandler;

public class JspTemplateViewHandler extends AbstractTemplateViewHandler {
    @Override
    public String getType() {
        return "jsp";
    }

    @Override
    public String getPrefix() {
        return "jsp:";
    }

    @Override
    public String getSuffix() {
        return ".jsp";
    }

    @Override
    protected void doRender(RequestContext ctx, String viewPathName) throws Exception {
        if (ctx.getServletContext().getResource(viewPathName) == null) {
            throw new ResourceNotFoundException(viewPathName);
        }

        HttpServletRequest request = ctx.getHttpServletRequest();
        HttpServletResponse response = ctx.getHttpServletResponse();

        request.getRequestDispatcher(viewPathName).forward(request, response);
    }

}
