package stone.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.UnknownException;

import stone.exception.ErrorCode;
import stone.exception.StoneWebException;

public class UploadFileUtil {
	/**
	 * 
	 * <br/>
	 * Description:获取文件上传信息
	 * 
	 * 
	 * @param request
	 * @param charset
	 * @return
	 * @throws UploadFileException
	 * @throws UnknownException
	 */
	public static Map<String, String> getMultipartParams(
			HttpServletRequest request, String charset) throws StoneWebException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			request.setCharacterEncoding(charset); // 设置编码

			// 获得磁盘文件条目工厂
			org.apache.commons.fileupload.disk.DiskFileItemFactory factory = new org.apache.commons.fileupload.disk.DiskFileItemFactory();
			// 获取文件需要上传到的路径
			// String path = request.getServletContext().getRealPath("/upload");
			String path = null;
			if (isWindowsOS()) {
				path = "C://Windows//Temp";
			} else {
				path = "/tmp";
			}
			// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
			// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
			/**
			 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以
			 * .tem 格式的 然后再将其真正写到 对应目录的硬盘上
			 */
			factory.setRepository(new File(path));
			// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
			factory.setSizeThreshold(1024 * 1024);

			// 高水平的API文件上传处理
			org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(
					factory);

			// 可以上传多个文件
			List<org.apache.commons.fileupload.FileItem> list = (List<org.apache.commons.fileupload.FileItem>) upload
					.parseRequest(request);

			for (org.apache.commons.fileupload.FileItem item : list) {
				// 获取表单的属性名字
				String name = item.getFieldName();

				// 如果获取的 表单信息是普通的 文本 信息
				if (item.isFormField()) {
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					String value = item.getString(charset);
					map.put(name, value);
				}
				// 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
				else {
					/**
					 * 以下三步，主要获取 上传文件的名字
					 */
					// 获取路径名
					String value = item.getName();
					// 索引到最后一个反斜杠
					int start = value.lastIndexOf("\\");
					// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
					String filename = value.substring(start + 1);

					map.put(name, path + "/" + filename);

					// 真正写到磁盘上
					// 它抛出的异常 用exception 捕捉

					item.write(new File(path, filename));// 第三方提供的

					// 手动写的
					// OutputStream out = new FileOutputStream(new
					// File(path,filename));
					//
					// InputStream in = item.getInputStream();
					//
					// int length = 0;
					// byte[] buf = new byte[1024];
					//
					// System.out.println("获取上传文件的总共的容量：" + item.getSize());
					//
					// // in.read(buf) 每次读到的数据存放在 buf 数组中
					// while((length = in.read(buf)) != - 1){
					// // 在 buf 数组中 取出数据 写到 （输出流）磁盘上
					// out.write(buf,0,length);
					//
					// }
					//
					// in.close();
					// out.close();
				}
			}

		} catch (org.apache.commons.fileupload.FileUploadException e) {
			throw StoneWebException.unchecked(e,
					ErrorCode.UploadFile_Error);
		} catch (Exception e) {
			throw StoneWebException.unchecked(e, ErrorCode.Unknown_ERROR);
		}
		return map;
	}

	/**
	 * 
	 * <br/>
	 * Description:判断操作系统是否是windows
	 * 
	 * 
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean boo = false;
		try {
			Properties sp = System.getProperties();

			String osName = sp.getProperty("os.name");

			int osInt = osName.toLowerCase().indexOf("win");

			if (osInt != -1) {
				boo = true;
			}
		} catch (Exception e) {
			System.out.println("获取操作系统名称异常。");
		}

		return boo;
	}
}
