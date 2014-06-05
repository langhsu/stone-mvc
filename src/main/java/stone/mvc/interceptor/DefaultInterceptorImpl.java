package stone.mvc.interceptor;

import java.lang.reflect.Method;

public class DefaultInterceptorImpl implements DefaultInterceptor {

	public boolean before(Class<?> cls, String routepath,
			Method method, Object... params) {
		// FIXME: do something!
		return true;
	}

	public void after(Class<?> cls, String routepath, Method method, Object... params) {
		// FIXME: do something!
	}

}
