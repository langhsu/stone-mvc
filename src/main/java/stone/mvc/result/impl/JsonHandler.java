/**
 * 
 */
package stone.mvc.result.impl;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stone.mvc.RequestContext;
import stone.mvc.result.MimeTypeUtils;
import stone.mvc.result.ResultHandler;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

/**
 * @author langhsu
 *
 */
public class JsonHandler implements ResultHandler<JSONAware> {
	
	@Override
	public void init() {
	}
	
	@Override
	public void handle(RequestContext ctx, JSONAware result) throws Exception {
		HttpServletRequest request = ctx.getHttpServletRequest();
        HttpServletResponse response = ctx.getHttpServletResponse();

        if (result == null) {
        	result = new JSONObject();
        }

        String characterEncoding = request.getCharacterEncoding();
        response.setCharacterEncoding(characterEncoding);

        String mimetype = MimeTypeUtils.getJSON(request);
        response.setContentType(mimetype + "; charset=" + characterEncoding);

        PrintWriter out = response.getWriter();
        out.write(result.toJSONString());
        out.flush();
	}

}
