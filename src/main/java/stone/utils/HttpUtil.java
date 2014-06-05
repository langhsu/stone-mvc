package stone.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.portable.UnknownException;

import stone.exception.ErrorCode;
import stone.exception.StoneWebException;

/**
 * 
 * @author langhsu
 *
 */
public class HttpUtil {
	
	/**
	 * 根据 key 取出 Map 里对应的 value
	 * 
	 * @param maps
	 * @param key
	 * @return
	 */
	public static String getMapValue(List<ConcurrentHashMap<String, String>> maps, String key) {
		String val = null;
		for (Map<String, String> m : maps) {
			String v = m.get(key);
			if (StringUtil.isNotEmpty(v)) {
				val = v;
				break;
			}
		}
		return val;
	}

	/**
	 * 返回当前Request请求IP
	 * 
	 * @param req
	 * @return
	 */
	public static String getRealIp(HttpServletRequest req) {
		String lastLoginIP = req.getHeader("x-forwarded-for");
		if (StringUtil.isEmpty(lastLoginIP) || "unknown".equalsIgnoreCase(lastLoginIP)) {
			lastLoginIP = req.getHeader("Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(lastLoginIP) || "unknown".equalsIgnoreCase(lastLoginIP)) {
			lastLoginIP = req.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtil.isEmpty(lastLoginIP) || "unknown".equalsIgnoreCase(lastLoginIP)) {
			lastLoginIP = req.getRemoteAddr();
		}
		return lastLoginIP;
	}

	/**
	 * 返回 HttpSession
	 * 
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession(true);
	}

	/**
	 * 将一个对象放入Session
	 * 
	 * @param sessionName
	 * @param obj
	 * @param request
	 * @return
	 */
	public static boolean putSession(HttpServletRequest request, String sessionName, Object obj) {
		boolean boo = false;
		try {
			HttpSession session = getSession(request);
			session.setAttribute(sessionName, obj);
			boo = true;
		} catch (Exception e) {
			boo = false;
		}
		return boo;
	}

	/**
	 * 设置P3P，以完成跨域请求
	 * 
	 * @param response
	 */
	public static void setResponseHeaderP3P(HttpServletResponse response) {
		response.setHeader("P3P",
				"CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
	}

	/**
	 * 页面跳转
	 * 
	 * @param request
	 * @param response
	 * @param path
	 * @param isRequestDispatcher
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirectPage(HttpServletRequest request,
			HttpServletResponse response, String path,
			boolean isRequestDispatcher) throws IOException, ServletException {
		if (isRequestDispatcher) {
			request.getRequestDispatcher(path).forward(request, response);
		} else {
			response.sendRedirect(path);
		}
	}

	/**
	 * 返回JSON结果
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultJsonToString(HttpServletResponse response, String str) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
			// logger.error("返回JSON结果失败。");
			// System.out.println();
		}
	}

	/**
	 * 返回Html结果
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultHTMLToString(HttpServletResponse response, String str) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
			// System.out.println("返回HTML结果失败。");
		}
	}

	/**
	 * 返回XML结果
	 * 
	 * @param response
	 * @param str
	 */
	public static void resultXMLToString(HttpServletResponse response,
			String str) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.setContentType("text/xml; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(str);
			out.close();
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
			// System.out.println("返回HTML结果失败。");
		}
	}

	/**
	 * 将字符串进行URLEncoder编码
	 * 
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str) {
		String encodeStr = null;
		try {
			encodeStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw StoneWebException.unchecked(e,
					ErrorCode.UnsupportedEncoding);
		}
		return encodeStr;
	}

	/**
	 * 将字符串进行URLDecoder解码
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecoder(String str) {
		String decoderStr = null;
		try {
			decoderStr = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw StoneWebException.unchecked(e,
					ErrorCode.UnsupportedEncoding);
		}
		return decoderStr;
	}

	/**
	 * 将字符串进行URLDecoder解码并转换成UTF-8编码
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecoder(String str, String charset) {
		String decoderStr = null;
		try {
			decoderStr = new String(URLDecoder.decode(str, charset).getBytes(
					"ISO-8859-1"), charset);
		} catch (UnsupportedEncodingException e) {
			throw StoneWebException.unchecked(e, ErrorCode.UnsupportedEncoding);
		}
		return decoderStr;
	}

	
	/**
	 * 
	 * <br/>
	 * Description:获取当前日期
	 * 
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = date.format(d);
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:返回国际化时间，含时区
	 * 
	 * 
	 * @return
	 */
	public static String getCurrentInternationalizationDate() {
		String str = null;
		try {
			Date d = new Date();
			SimpleDateFormat date = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
			str = date.format(d);
		} catch (Exception e) {
			System.out.println("转换日期异常！");
		}
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:获取当前日期,返回指定日期格式
	 * 
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String getCurrentDate(String dataFormat) {
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat(dataFormat);
		String str = date.format(d);
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:根据格转式转换时间
	 * 
	 * 
	 * @param date
	 * @param dataFormat
	 * @return
	 */
	public static String conversionDate(Date date, String dataFormat) {
		SimpleDateFormat d = new SimpleDateFormat(dataFormat);
		String str = d.format(date);
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:格式化日期时间，返回String类型
	 * 
	 * 
	 * @param dateTime
	 * @param dataFormat
	 * @return
	 */
	public static String conversionDateReturnString(String dateTime,
			String dataFormat) {
		SimpleDateFormat dateTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat(dataFormat);
		String str = null;
		try {
			str = date.format(dateTemp.parse(dateTime));
		} catch (ParseException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}

		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:long转Date
	 * 
	 * 
	 * @param longDate
	 * @return
	 */
	public static Date longToDate(long longDate) {
		return new Date(longDate);
	}

	/**
	 * 
	 * <br/>
	 * Description:long转String
	 * 
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static String longToString(String dateStr, String dateFormat) {
		String str = null;
		try {
			str = conversionDate(longToDate(Long.parseLong(dateStr)),
					dateFormat);
		} catch (Exception e) {
			throw StoneWebException
					.unchecked(e, ErrorCode.Conversion_ERROR);

		}
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:获取当前日期返回Date类型
	 * 
	 * 
	 * @return
	 */
	public static Date getCurrentDateReturnDate() {
		Date strDate = new Date();
		return strDate;
	}

	/**
	 * 
	 * <br/>
	 * Description:格式化日期时间，返回Date类型
	 * 
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date conversionDateReturnDate(String dateTime) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date strDate = null;
		try {
			strDate = date.parse(dateTime);
		} catch (ParseException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>
	 * Description:格式化日期时间，返回Date类型(输入方式月日年时分秒)
	 * 
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date conversionDateReturnDateInput_Mdy(String dateTime) {
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date strDate = null;
		try {
			strDate = date.parse(dateTime);
		} catch (ParseException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>
	 * Description:转换国际化时间
	 * 
	 * 
	 * @param dateTime
	 *            2010-06-01T20:00:00+08:00
	 * @return
	 */
	public static Date conversionDateToDateByInternationalization(
			String dateTime) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date strDate = null;
		try {
			strDate = date.parse(dateTime);
		} catch (ParseException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}
		return strDate;
	}

	/**
	 * 
	 * <br/>
	 * Description:转换国际化时间
	 * 
	 * 
	 * @param dateTime
	 *            2010-06-01T20:00:00+08:00
	 * @param dateFormat
	 *            日常日期格式 例如：yyyy-MM-dd'T'HH:mm:ss+hh:mm
	 * @return
	 */
	public static String conversionDateToStringByInternationalization(
			String dateTime, String dateFormat) {
		SimpleDateFormat dateTemp = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		String str = null;
		try {
			str = date.format(dateTemp.parse(dateTime));
		} catch (ParseException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:计算未来几钟后的时间
	 * 
	 * 
	 * @param minutes
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes, String outDataFormat) {
		String dateTime = null;
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd  HH:mm:ss G E D F w W a E F");
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		Date date = new Date();
		long Time = (date.getTime() / 1000) + 60 * minutes;
		date.setTime(Time * 1000);
		dateTime = formatter.format(date);
		return dateTime;
	}

	/**
	 * 
	 * <br/>
	 * Description:计算未来几钟后的时间
	 * 
	 * 
	 * @param minutes
	 * @param inputDateTime
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes, String inputDateTime,
			String outDataFormat) {
		String dateTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		Date date = conversionDateReturnDate(inputDateTime);
		long Time = (date.getTime() / 1000) + 60 * minutes;
		date.setTime(Time * 1000);
		dateTime = formatter.format(date);
		return dateTime;
	}

	/**
	 * 
	 * <br/>
	 * Description:计算未来几钟后的时间
	 * 
	 * 
	 * @param minutes
	 * @param inputDateTime
	 * @param outDataFormat
	 * @return
	 */
	public static String generateFutureTime(int minutes, Date inputDateTime,
			String outDataFormat) {
		String dateTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat(outDataFormat);
		long Time = (inputDateTime.getTime() / 1000) + 60 * minutes;
		inputDateTime.setTime(Time * 1000);
		dateTime = formatter.format(inputDateTime);
		return dateTime;
	}

	/**
	 * 
	 * <br/>
	 * Description:根据传入时间返回国际化时间，含时区
	 * 
	 * 
	 * @param d
	 * @return
	 */
	public static String getInternationalizationDate(Date d) {
		String str = null;
		try {
			SimpleDateFormat date = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
			str = date.format(d);
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.ParseException);
		}
		return str;
	}

	/**
	 * 
	 * <br/>
	 * Description:删除多余的空格和回车
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteTabsAndEnter(String str) {
		String s = null;

		Pattern p = Pattern.compile("\\s*|\t|\r|\n");

		Matcher m = p.matcher(str);

		String after = m.replaceAll("");

		s = after;

		return s;
	}

	/**
	 * 
	 * <br/>
	 * Description:Map 排序
	 * 
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static LinkedHashMap<String, Integer> sortMap(
			Map<String, Integer> map) {

		LinkedHashMap<String, Integer> tmp = new LinkedHashMap<String, Integer>();

		Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);

		List arrayList = new ArrayList(map.entrySet());
		Collections.sort(arrayList, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map.Entry obj1 = (Map.Entry) o1;
				Map.Entry obj2 = (Map.Entry) o2;
				return ((Integer) obj2.getValue()).compareTo((Integer) obj1
						.getValue());
			}
		});

		int v = arrayList.size();

		for (int i = 0; i < v; i++) {
			String strs[] = arrayList.get(i).toString().split("=");
			tmp.put(strs[0], Integer.parseInt(strs[1]));
		}

		return tmp;
	}

	/**
	 * 
	 * <br/>
	 * Description:返回当前项目编译路径
	 * 
	 * 
	 * @return
	 */
	public String getProjectClassesPath() {
		return this.getClass().getResource("/").getPath();
	}

	/**
	 * 
	 * <br/>
	 * Description:返回当前项目编译路径
	 * 
	 * @author Eric
	 * @return
	 */
	public static String getClassesPath() {
		HttpUtil utils = new HttpUtil();
		String str = null;
		try {
			str = urlDecoder(utils.getProjectClassesPath());
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.NotFoundPath);
		}
		return str;
	}

	

	/**
	 * 
	 * <br/>
	 * Description:判断请求是否为Ajax请求
	 * 
	 * 
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest req) {
		boolean boo = false;
		String header = req.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header)) {
			boo = true;
		}
		return boo;
	}

	/**
	 * 返回一个ClassLoader
	 * 
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 根据classLoader和类名称返回一个类
	 * 
	 * 
	 * @param classLoader
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByClassLoader(ClassLoader classLoader,
			String className) {
		Class<?> clazz = null;
		try {
			if (null != className && null != classLoader) {
				clazz = classLoader.loadClass(className);
			}
		} catch (ClassNotFoundException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ClassNotFound);
		}
		return clazz;
	}

	/**
	 * 根据classForName和类名称返回一个类
	 * 
	 * 
	 * @param classLoader
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByClassForName(String className) {
		Class<?> clazz = null;
		try {
			if (null != className) {
				clazz = Class.forName(className);
			}
		} catch (ClassNotFoundException e) {
			throw StoneWebException.unchecked(e, ErrorCode.ClassNotFound);
		}
		return clazz;
	}

	/**
	 * 删除字符串两端的大括号
	 * 
	 * @param val
	 * @return
	 */
	public static String deleteBigBrackets(String val) {
		String v = val;
		String begin = v.substring(0, 1);
		String end = v.substring(v.length() - 1, v.length());
		if (begin.equalsIgnoreCase("{") && end.equalsIgnoreCase("}")) {
			v = v.substring(1, v.length() - 1);
		}
		return v;
	}

	/**
	 * 给字符串包裹一个大括号
	 * 
	 * 
	 * @param val
	 * @return
	 */
	public static String addBigBrackets(String val) {

		return "{" + val + "}";
	}

	/**
	 * 删除字符串尾部是否包含 “/”
	 * 
	 * @param val
	 * @return
	 */
	public static String deleteRightBar(String val) {
		boolean boo = checkRightBar(val);

		String s = val;

		if (boo) {
			while (checkRightBar(s)) {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	/**
	 * 判断字符串尾部是否包含 “/”
	 * 
	 * @param val
	 * @return
	 */
	public static boolean checkRightBar(String val) {
		boolean boo = false;
		if (val.length() > 1) {
			String s = val.substring(val.length() - 1, val.length());

			if ("/".equalsIgnoreCase(s)) {
				boo = true;
			}
		}

		return boo;
	}

	/**
	 * 根据传入的类型返回对应数据的类型--反射工具专用
	 * 
	 * 
	 * @param pmType
	 * @param val
	 * @return
	 * @throws ConversionTypeException
	 */
	public static Object createObjectByParamType(String pmType, String val) throws StoneWebException {

		Object obj = null;

		try {
			if (pmType.equals("java.lang.String")) {
				obj = String.valueOf(val);
			} else if (pmType.equals("java.util.Date")) {
				obj = String.valueOf(HttpUtil.conversionDateReturnDate(val));
			} else if (pmType.equals("java.lang.Boolean")) {
				obj = new Boolean(val);
			} else if (pmType.equals("boolean")) {
				obj = new Boolean(val);
			} else if (pmType.equals("java.lang.Integer")) {
				obj = Integer.parseInt(val);
			} else if (pmType.equals("int")) {
				obj = Integer.parseInt(val);
			} else if (pmType.equals("java.lang.Long")) {
				obj = Long.parseLong(val);
			} else if (pmType.equals("java.lang.Double")) {
				obj = Double.parseDouble(val);
			} else if (pmType.equals("java.lang.Byte")) {
				obj = Byte.parseByte(val);
			} else if (pmType.equals("java.lang.Short")) {
				obj = Short.parseShort(val);
			} else if (pmType.equals("java.lang.Float")) {
				obj = Float.parseFloat(val);
			} else if (pmType.equals("java.math.BigDecimal")) {
				obj = new BigDecimal(val);
			} else if (pmType.equals("java.math.BigInteger")) {
				obj = new BigInteger(val);
			} else if (pmType.equals("java.io.File")) {
				obj = new File(val);
			}
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.Conversion_ERROR);
		}

		return obj;
	}

	/**
	 * 根据转入类型返回对应的类型名称
	 * 
	 * @param pmType
	 * @return
	 */
	public static String getTypeNameByParamType(String pmType) {
		String str = null;

		if (pmType.equals("class java.lang.String")) {
			str = "string";
		} else if (pmType.equals("class java.util.Date")) {
			str = "date";
		} else if (pmType.equals("class java.lang.Boolean")) {
			str = "boolean";
		} else if (pmType.equals("boolean")) {
			str = "boolean";
		} else if (pmType.equals("class java.lang.Integer")) {
			str = "integer";
		} else if (pmType.equals("int")) {
			str = "int";
		} else if (pmType.equals("class java.lang.Long")) {
			str = "long";
		} else if (pmType.equals("class java.lang.Double")) {
			str = "double";
		} else if (pmType.equals("class java.lang.Byte")) {
			str = "byte";
		} else if (pmType.equals("class java.lang.Short")) {
			str = "short";
		} else if (pmType.equals("class java.lang.Float")) {
			str = "float";
		} else if (pmType.equals("class java.math.BigDecimal")) {
			str = "bigDecimal";
		} else if (pmType.equals("class java.math.BigInteger")) {
			str = "bigInteger";
		} else if (pmType.equals("class java.io.File")) {
			str = "file";
		} else if (pmType.equals("interface java.util.Map")
				|| pmType.equals("class java.util.TreeMap")
				|| pmType.equals("class java.util.HashMap")
				|| pmType.equals("class java.util.EnumMap")
				|| pmType.equals("class java.util.LinkHashMap")) {
			str = "map";
		} else if (pmType.equals("interface java.util.List")
				|| pmType.equals("class java.util.ArrayList")
				|| pmType.equals("class java.util.LinkedList")) {
			str = "list";
		} else if (pmType.equals("interface java.util.Set")
				|| pmType.equals("class java.util.TreeSet")
				|| pmType.equals("class java.util.HashSet")
				|| pmType.equals("class java.util.BitSet")
				|| pmType.equals("class java.util.EnumSet")
				|| pmType.equals("class java.util.LinkedHashSet")
				|| pmType.equals("class java.util.TreeSet")) {
			str = "set";
		} else if (pmType.equals("java.util.Vector")) {
			str = "vector";
		}

		return str;
	}

	/**
	 * 读取 ServletInputStream
	 * 
	 * @param request
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getServletInputStream(InputStreamReader in) {

		int BUFFER_SIZE = 4096;

		StringWriter out = new StringWriter();

		try {
			// int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				// byteCount += bytesRead;
			}
			out.flush();
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				throw StoneWebException.unchecked(ex, ErrorCode.IOE_Exception);

			}
			try {
				out.close();
			} catch (IOException ex) {
				throw StoneWebException.unchecked(ex, ErrorCode.IOE_Exception);

			}
		}

		return out.toString();
	}

	/**
	 * 根据URl地址和Key返回对应值
	 * 
	 * @param val
	 * @param key
	 * @return
	 */
	public static Map<String, String> getUrlParamByKey(String val) {
		Map<String, String> map = null;
		if (null != val && !"".equalsIgnoreCase(val)) {
			map = new HashMap<String, String>();

			String[] maps = val.split("&");

			if (maps.length != 0) {
				for (String m : maps) {
					String[] km = m.split("=");
					map.put(km[0], km[1]);
				}
			} else {
				String[] km = val.split("=");
				map.put(km[0], km[1]);
			}
		}

		return map;
	}

	public static boolean isMultipartRequest(HttpServletRequest request) {
		boolean boo = false;
		if (request.getContentType() == null
				|| request.getContentType().equals("null")
				|| request.getContentType().equals("")) {
			return boo;
		}
		String[] cts = request.getContentType().split(";");

		String contentType = cts[0];

		int ct = contentType.toLowerCase().indexOf("multipart/".toLowerCase());

		if (ct != -1) {
			boo = true;
		}

		return boo;
	}

	/**
	 * 获取文件上传信息
	 * 
	 * @param request
	 * @param charset
	 * @return
	 * @throws UploadFileException
	 * @throws UnknownException
	 */
	public static Map<String, String> getMultipartParams(
			HttpServletRequest request, String charset) throws StoneWebException {
		return UploadFileUtil.getMultipartParams(request, charset);
	}

}
