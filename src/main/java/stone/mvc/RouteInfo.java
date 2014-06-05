package stone.mvc;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 
 * @author langhsu
 *
 */
public class RouteInfo implements Serializable {
	private static final long serialVersionUID = -3351282889298677726L;

	private String path;
	
	private int pathLength;
	
	private String clazz;
	
	private String method;
	
	private Method[] methods;
	
	private String httpMethod;
	
	private Map<String, Object> params;
	
	private Class<?> interceptor;
	
	private String returnType;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Class<?> getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Class<?> interceptor) {
		this.interceptor = interceptor;
	}

	public Method[] getMethods() {
		return methods;
	}

	public void setMethods(Method[] methods) {
		this.methods = methods;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

}
