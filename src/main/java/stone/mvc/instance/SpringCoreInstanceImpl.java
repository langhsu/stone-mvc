package stone.mvc.instance;

import stone.utils.SpringContextUtil;

public class SpringCoreInstanceImpl implements ICoreInstance {

	public Object returnclass(String classname) throws Exception {
		return SpringContextUtil.getBean(classname);
	}

	public Object returnclass(Class<?> cls) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
