package stone.mvc.views.impl;

import javax.servlet.http.HttpServletRequest;

import stone.mvc.views.AbstractDataViewHandler;

public class XmlDataViewHandler extends AbstractDataViewHandler {

    @Override
    public String getType() {
        return "xml";
    }

    @Override
    public String getMimetype(HttpServletRequest request) {
        return "text/xml";
    }

}
