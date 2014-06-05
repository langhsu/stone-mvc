package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;

import stone.mvc.result.MimeTypeUtils;
import stone.mvc.views.AbstractDataViewHandler;

public class JsonDataViewHandler extends AbstractDataViewHandler {

    @Override
    public String getType() {
        return "json";
    }

    @Override
    public String getMimetype(HttpServletRequest request) {
        return MimeTypeUtils.getJSON(request);
    }

}
