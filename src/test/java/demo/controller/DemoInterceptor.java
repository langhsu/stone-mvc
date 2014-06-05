package demo.controller;

import java.lang.reflect.Method;

import stone.mvc.interceptor.DefaultInterceptor;

public class DemoInterceptor implements DefaultInterceptor{


	public boolean before(Class<?> cls,String routepath, Method method, Object... params) {
		// TODO Auto-generated method stub
		return true;
	}

	public void after(Class<?> cls,String routepath, Method method, Object... params) {
		// TODO Auto-generated method stub
		
	}

}
