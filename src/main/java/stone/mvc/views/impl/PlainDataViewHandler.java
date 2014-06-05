package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;

import stone.mvc.views.AbstractDataViewHandler;

public class PlainDataViewHandler extends AbstractDataViewHandler {

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public String getMimetype(HttpServletRequest request) {
        return "text/plain";
    }

}
