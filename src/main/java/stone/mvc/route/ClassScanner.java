package stone.mvc.route;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import stone.exception.ErrorCode;
import stone.exception.StoneWebException;

/**
 * 扫描 Package(包括jar)下的class文件
 * 
 * @author langhsu
 *
 */
public class ClassScanner {
	
	/**
	 * 排除内部类， true： 排除
	 */
	private boolean excludeInner = true;
	
	/**
	 * 过滤规则列表 如果是null或者空，即全部符合不过滤
	 */
	private List<String> classFilters = null;
	
	public ClassScanner(Boolean excludeInner, List<String> classFilters) {
		this.excludeInner = excludeInner;
		this.classFilters = classFilters;
	}
	
	/**
	 * 扫描包
	 * 
	 * @param rootPackage
	 * @param recursive
	 * @return
	 */
	public Set<Class<?>> findPkgClassAll(String rootPackage, boolean recursive) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String pckName = rootPackage;
		if (pckName.endsWith(".")) {
			pckName = pckName.substring(0, pckName.lastIndexOf('.'));
		}
		String pck2path = pckName.replace('.', '/');

		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(pck2path);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol) || "vfs".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					scanPackageClassesByFile(classes, pckName, filePath, recursive);
				} else if ("jar".equals(protocol)) {
					scanPackageClassesByJar(pckName, url, recursive, classes);
				}
			}
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
		}

		return classes;
	}

	/**
	 * 以jar的方式扫描包下的所有Class文件
	 * 
	 * @param rootPackage
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private void scanPackageClassesByJar(String rootPackage, URL url, final boolean recursive, Set<Class<?>> classes) {
		String pckName = rootPackage;
		String pck2path = pckName.replace('.', '/');
		JarFile jar;
		try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (!name.startsWith(pck2path) || entry.isDirectory()) {
					continue;
				}

				// 判断是否递归搜索子包
				if (!recursive && name.lastIndexOf('/') != pck2path.length()) {
					continue;
				}
				// 判断是否过滤 inner class
				if (this.excludeInner && name.indexOf('$') != -1) {
					continue;
				}
				String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
				
				// 判定是否符合过滤条件
				if (this.filterClassName(classSimpleName)) {
					String className = name.replace('/', '.');
					className = className.substring(0, className.length() - 6);
					try {
						classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
					} catch (ClassNotFoundException e) {
						throw StoneWebException.unchecked(e, ErrorCode.ClassNotFound);
					}
				}
			}
		} catch (IOException e) {
			throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
		}
	}

	/**
	 * 以文件的方式扫描包下的所有Class文件
	 * 
	 * @param classes
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 */
	private void scanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, boolean recursive) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		final boolean fileRecursive = recursive;
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义文件过滤规则
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return fileRecursive;
				}
				String filename = file.getName();
				if (excludeInner && filename.indexOf('$') != -1) {
					return false;
				}
				return filterClassName(filename);
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				scanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));

				} catch (ClassNotFoundException e) {
					throw StoneWebException.unchecked(e, ErrorCode.IOE_Exception);
				}
			}
		}
	}

	/**
	 * 根据过滤规则判断类名
	 * 
	 * @param className
	 * @return
	 */
	private boolean filterClassName(String className) {
		if (!className.endsWith(".class")) {
			return false;
		}
		if (null == this.classFilters || this.classFilters.isEmpty()) {
			return true;
		}
		String tmpName = className.substring(0, className.length() - 6);
		boolean flag = false;
		for (String str : classFilters) {
			String tmpreg = "^" + str.replace("*", ".*") + "$";
			Pattern p = Pattern.compile(tmpreg);
			if (p.matcher(tmpName).find()) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean isExcludeInner() {
		return excludeInner;
	}

	public void setExcludeInner(boolean excludeInner) {
		this.excludeInner = excludeInner;
	}

	public List<String> getClassFilters() {
		return classFilters;
	}

	public void setClassFilters(List<String> classFilters) {
		this.classFilters = classFilters;
	}
	
}
