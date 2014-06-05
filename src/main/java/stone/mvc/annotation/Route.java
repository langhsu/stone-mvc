package stone.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import stone.mvc.interceptor.DefaultInterceptorImpl;

/**
 * 地址路由类 注解类
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Route {
	String value() default "";
	
	HttpMethod[] method() default { HttpMethod.GET, HttpMethod.POST };
	
	ResultType resultType() default ResultType.STRING;
	
	Class<?> cls() default DefaultInterceptorImpl.class;// 拦截类

}
