package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;

import stone.mvc.views.AbstractDataViewHandler;

public class HtmlDataViewHandler extends AbstractDataViewHandler {

    @Override
    public String getType() {
        return "html";
    }

    @Override
    public String getMimetype(HttpServletRequest request) {
        return "text/html";
    }

}
