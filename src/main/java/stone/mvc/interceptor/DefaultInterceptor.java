package stone.mvc.interceptor;

import java.lang.reflect.Method;

/**
 * 拦截器
 * 
 */
public interface DefaultInterceptor {
	/**
	 * 方法开始前拦截
	 * 
	 * @param cls
	 * @param routepath
	 * @param method
	 * @param params
	 * @return
	 */
	public boolean before(Class<?> cls, String routepath,
			Method method, Object... params);

	/**
	 * 方法后开始拦截
	 * 
	 * @param cls
	 * @param method
	 * @param params
	 */
	public void after(Class<?> cls, String routepath, Method method,
			Object... params);
}
