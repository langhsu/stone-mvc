package stone.mvc.result;

import javax.servlet.http.HttpServletRequest;

public class MimeTypeUtils {

    // IE 10 以下的版本不支持 application/json
    public static String getJSON(HttpServletRequest request) {
        return isOldIEBrowser(request, 10) ? "text/html" : "application/json";
    }

    // IE 9  以下的版本不支持 application/javscript
    public static String getJavaScript(HttpServletRequest request) {
        return isOldIEBrowser(request, 9) ? "text/html" : "application/javascript";
    }

    private static boolean isOldIEBrowser(HttpServletRequest request, int expectedVersion) {
        try {
            String agent = request.getHeader("user-agent");
            int ipos = agent.indexOf("MSIE");
            if (ipos > 0) {
                ipos = ipos + 4;
                int jpos = agent.indexOf(';', ipos);
                String version = agent.substring(ipos, jpos);
                return Float.parseFloat(version) < expectedVersion;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
