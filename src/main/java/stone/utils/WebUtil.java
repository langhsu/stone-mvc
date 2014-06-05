package stone.utils;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {

	// 将数据以纯文本格式写入响应中
	public static void writeText(HttpServletResponse response, Object data) {
		try {
			// 设置响应头
			response.setContentType("text/plain"); // 指定内容类型为纯文本格式
			response.setCharacterEncoding("UTF-8"); // 防止中文乱码

			// 向响应中写入数据
			PrintWriter writer = response.getWriter();
			writer.write(data + ""); // 转为字符串
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取上传文件路径
	public static String getUploadFilePath(HttpServletRequest request, String relativePath) {
		// 获取绝对路径
		String filePath = "";

		// 创建文件
		// FileUtil.createFile(filePath);

		return filePath;
	}

	// 转发请求
	public static void forwordRequest(String path, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 重定向请求
	public static void redirectRequest(String path, HttpServletResponse response) {
		try {
			response.sendRedirect(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 发送错误代码
	public static void sendError(int code, HttpServletResponse response,
			String message) {
		try {
			response.sendError(code, message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 判断是否为 AJAX 请求
	public static boolean isAJAX(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null;
	}
}
