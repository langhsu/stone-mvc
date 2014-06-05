package stone.utils;

import java.util.Map;

import net.sf.cglib.beans.BeanMap;

public class BeanMapUtil {
	/**
	 * javabean transform map
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertBeanToMap(Object object) {
		return BeanMap.create(object);
	}

	/**
	 * javabean transform map
	 * 
	 * @param object
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convertMapToBean(Object object, Map<String, Object> result) {
		Map map = BeanMap.create(object);
		for (String key : result.keySet()) {
			if (map.containsKey(key)
					&& !StringUtil.isNullOrEmpty(result.get(key))) {
				map.put(key, result.get(key));
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertStringMapToBean(Object object, Map<String, String> result) {
		Map map = BeanMap.create(object);
		for (String key : result.keySet()) {
			if (map.containsKey(key)
					&& !StringUtil.isNullOrEmpty(result.get(key))) {
				map.put(key, result.get(key));
			}
		}
		return map;
	}
}
