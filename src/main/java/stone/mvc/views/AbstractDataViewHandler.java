package stone.mvc.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stone.mvc.RequestContext;

/**
 * 
 * @author langhsu
 *
 */
public abstract class AbstractDataViewHandler implements ViewHandler {

    @Override
    public String getSuffix() {
        return null;
    }

    public abstract String getMimetype(HttpServletRequest request);

    @Override
    public void render(RequestContext ctx, String value) throws IOException {
        HttpServletRequest request = ctx.getHttpServletRequest();
        HttpServletResponse response = ctx.getHttpServletResponse();

        String characterEncoding = request.getCharacterEncoding();
        response.setCharacterEncoding(characterEncoding);
        response.setContentType(getMimetype(request) + "; charset=" + characterEncoding);

        PrintWriter out = response.getWriter();
        out.write(value);
        out.flush();
    }

}
