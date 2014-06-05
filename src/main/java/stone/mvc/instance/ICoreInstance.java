package stone.mvc.instance;

public interface ICoreInstance {
	/**
	 * 
	 * @param classname
	 * @return
	 * @throws Exception
	 */
	public Object returnclass(String classname) throws Exception;

	/**
	 * 
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public Object returnclass(Class<?> cls) throws Exception;
}
