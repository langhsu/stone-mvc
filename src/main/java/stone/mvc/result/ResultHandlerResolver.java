package stone.mvc.result;

import java.util.IdentityHashMap;
import java.util.Map;

import stone.mvc.result.impl.JsonHandler;
import stone.mvc.result.impl.StringHandler;
import stone.mvc.result.impl.VoidHandler;

import com.alibaba.fastjson.JSONAware;

public class ResultHandlerResolver {
	private final Map<Class<?>, ResultHandler<?>> mapping = new IdentityHashMap<Class<?>, ResultHandler<?>>();
	
	public void init() throws InstantiationException, IllegalAccessException {
		register(Void.TYPE, VoidHandler.class);
		register(String.class, StringHandler.class);
		register(JSONAware.class, JsonHandler.class);
	}
	
	public void register(Class<?> resultClass, Class<?> resultHandlerClass) throws InstantiationException, IllegalAccessException {
		if (ResultHandler.class.isAssignableFrom(resultHandlerClass)) {
			ResultHandler<?> resultHandler = (ResultHandler<?>) resultHandlerClass.newInstance();
			resultHandler.init();
			mapping.put(resultClass, resultHandler);
		}
	}

	public boolean supported(Class<?> resultClass) {
		boolean ret = false;
		if (mapping.containsKey(resultClass)) {
			ret = true;
		} else if (JSONAware.class.isAssignableFrom(resultClass)) {
			ret = true;
		}
		return ret;
	}

	public ResultHandler<?> lookup(Class<?> resultClass) {
		ResultHandler<?> result = mapping.get(resultClass);
		if (result == null) {
			if (JSONAware.class.isAssignableFrom(resultClass)) {
				result = mapping.get(JSONAware.class);
			} else {
				throw new IllegalStateException("Unsupported result class: " + resultClass.getName());
			}
		}
		return result;
	}
}
