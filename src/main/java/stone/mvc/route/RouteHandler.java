/**
 * 
 */
package stone.mvc.route;

import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ClassUtils;

import stone.exception.ErrorCode;
import stone.exception.StoneWebException;
import stone.mvc.RequestContext;
import stone.mvc.RouteInfo;
import stone.mvc.annotation.FormParam;
import stone.mvc.annotation.RouteParam;
import stone.mvc.instance.DefaultCoreInstanceImpl;
import stone.mvc.instance.ICoreInstance;
import stone.mvc.instance.SpringCoreInstanceImpl;
import stone.mvc.interceptor.DefaultInterceptor;
import stone.utils.BeanMapUtil;
import stone.utils.HttpUtil;
import stone.utils.StringUtil;

/**
 * @author langhsu
 *
 */
public class RouteHandler {
	
	public Object methodInvoke(RouteInfo routeInfo, boolean isSupportSpring) throws Exception {
		RequestContext context = RequestContext.getRequestContext();
		HttpServletRequest request = context.getHttpServletRequest();
		String method = request.getMethod();
		String charset = request.getCharacterEncoding();
		Map<String, String> multipartParams = null;
		Map<String, String> putParams = null;
		Object obj = null;
		try {
			boolean isMultipartRequest = HttpUtil.isMultipartRequest(request);
			if (isMultipartRequest) {
				multipartParams = HttpUtil.getMultipartParams(request, charset);
			}
			if ("put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
				if (isMultipartRequest) {
					putParams = multipartParams;
				} else {
					putParams = getPutParams(request, charset);
				}
			}
			Class<?> clazz = HttpUtil.getClassByClassLoader(HttpUtil.getClassLoader(), routeInfo.getClazz());
			
			Method[] mds = routeInfo.getMethods();
			
			for (int i = 0; i < mds.length; i++) {
				Method md = mds[i];
				if (routeInfo.getMethod().equals(md.getName())) {
					Class<?>[] mdTypes = md.getParameterTypes();
					Annotation[][] annoParams = md.getParameterAnnotations();
					Object[] objs = new Object[annoParams.length];
					for (int j = 0; j < annoParams.length; j++) {
						Annotation annotation = annoParams[j][0];
						if ("FormParam".equals(annotation.annotationType().getSimpleName())) {
							FormParam param = (FormParam) annotation;
							String pmType = mdTypes[j].getName();
							Map<String, String> params = new HashMap<String, String>();
							if (isMultipartRequest) {
								params.putAll(multipartParams);
							}
							if ("put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
								params = putParams;
							} else {
								Enumeration<?> enumeration = request.getParameterNames();
								while (enumeration.hasMoreElements()) {
									String key = (String) enumeration.nextElement();
									params.put(key, request.getParameter(key));
								}
							}
							if (ClassUtils.isAssignable(mdTypes[j], Map.class)) {
								objs[j] = params;
							} else if (mdTypes[j].isArray()) {
								Object[] object = new Object[params.keySet()
										.size()];
								int k = 0;
								for (String key : params.keySet()) {
									object[k] = params.get(key);
									k++;
								}
								objs[j] = object;
							} else if (ClassUtils.isAssignable(mdTypes[j], Collection.class)) {
								List<String> result = new LinkedList<String>();
								for (String key : params.keySet()) {
									result.add(params.get(key));
								}
								objs[j] = result;
							} else if (mdTypes[j].isPrimitive()
									|| mdTypes[j].getName().startsWith("java.")) {
								if (params.containsKey(param.value())) {
									objs[j] = HttpUtil.createObjectByParamType(pmType,
													params.get(param.value()) + "");
								}
							} else {// javabean
								objs[j] = mdTypes[j].newInstance();
								BeanMapUtil.convertStringMapToBean(objs[j], params);
							}
						} else if ("RouteParam".equals(annotation
								.annotationType().getSimpleName())) {
							RouteParam param = (RouteParam) annotation;
							String pmType = mdTypes[j].getName();
							if (ClassUtils.isAssignable(mdTypes[j], Map.class)) {
								objs[j] = routeInfo.getParams();
							} else if (mdTypes[j].isArray()) {
								Map<String, Object> params = routeInfo.getParams();
								Object[] object = new Object[params.keySet()
										.size()];
								int k = 0;
								for (String key : params.keySet()) {
									object[k] = params.get(key);
									k++;
								}
								objs[j] = object;
							} else if (ClassUtils.isAssignable(mdTypes[j],
									Collection.class)) {
								Map<String, Object> params = routeInfo.getParams();
								List<Object> result = new LinkedList<Object>();
								for (String key : params.keySet()) {
									result.add(params.get(key));
								}
								objs[j] = result;
								// throw new
								// Exception("RouteParam时，java 基本类型不能作为参数"+pmType);
							} else if (mdTypes[j].isPrimitive()
									|| mdTypes[j].getName().startsWith("java.")) {
								Map<String, Object> params = routeInfo.getParams();
								if (params.containsKey(param.value())) {
									objs[j] = HttpUtil
											.createObjectByParamType(
													pmType,
													routeInfo.getParams().get(
															param.value())
															+ "");
								}
							} else {// javabean
								objs[j] = mdTypes[j].newInstance();
								BeanMapUtil.convertMapToBean(objs[j],
										routeInfo.getParams());
							}
						}
					}
					Object o = null;
					if (!StringUtil.isNullOrEmpty(isSupportSpring)
							&& isSupportSpring) {
						try {
							ICoreInstance coreInstance = new SpringCoreInstanceImpl();
							String clsname = clazz.getSimpleName()
									.substring(0, 1).toLowerCase()
									+ clazz.getSimpleName().substring(1);
							o = coreInstance.returnclass(clsname);
						} catch (Exception e) {
							throw StoneWebException.unchecked(e,
									ErrorCode.ClassNotFound);
						}
					} else {
						try {
							ICoreInstance coreInstance = new DefaultCoreInstanceImpl();
							o = coreInstance.returnclass(clazz);
						} catch (Exception e) {
							throw StoneWebException.unchecked(e,
									ErrorCode.ClassNotFound);
						}
					}
					Boolean flag = true;
					Class<?> interceptorclass = routeInfo.getInterceptor();
					if (!StringUtil.isNullOrEmpty(interceptorclass)
							&& interceptorclass.newInstance() instanceof DefaultInterceptor) {
						DefaultInterceptor defaultInterceptot = (DefaultInterceptor) interceptorclass.newInstance();
						if (objs.length > 0) {
							flag = defaultInterceptot.before(clazz, routeInfo.getPath(), md, objs);
						} else {
							flag = defaultInterceptot.before(clazz, routeInfo.getPath(), md);
						}
					}// 开始前拦截
					if (flag) {// 通过
						try {
							md.setAccessible(true);
							if (objs.length > 0) {
								obj = md.invoke(o, objs);// 执行
							} else {
								obj = md.invoke(o);
							}
						} catch (InvocationTargetException e) {
							throw StoneWebException.unchecked(e, ErrorCode.InvocationTargetException);
						} catch (Throwable e) {
							throw StoneWebException.unchecked(e);
						}

					}
					if (!StringUtil.isNullOrEmpty(interceptorclass)
							&& interceptorclass.newInstance() instanceof DefaultInterceptor) {
						DefaultInterceptor defaultInterceptot = (DefaultInterceptor) interceptorclass.newInstance();
						if (objs.length > 0) {
							defaultInterceptot.after(clazz, routeInfo.getPath(), md, objs);
						} else {
							defaultInterceptot.after(clazz, routeInfo.getPath(), md);
						}
					}// 方法后拦截
					break;
				}
			}
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.MethodInvokeException);
		}
		return obj;
	}
	
	private Map<String, String> getPutParams(HttpServletRequest request, String charset) {
		InputStreamReader in = null;
		try {
			// request.getParameterMap()
			in = new InputStreamReader(request.getInputStream(), charset);
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
		}
		String urlParam = HttpUtil.getServletInputStream(in);
		return HttpUtil.getUrlParamByKey(urlParam);
	}
}
