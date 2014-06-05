package stone.utils;

public class StringUtil {
	
	public static boolean isNullOrEmpty(Object obj) {
		return (obj == null) || obj.toString().trim().length() <= 0;
	}
	
	public static boolean isEmpty (String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isNotEmpty (String str) {
		return !isEmpty(str);
	}
	
	public static String toString(Object paramObject) {
		if (paramObject == null)
			return "null";
		return paramObject.toString();
	}
}
