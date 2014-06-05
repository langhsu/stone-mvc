package demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import stone.mvc.RequestContext;
import stone.mvc.annotation.FormParam;
import stone.mvc.annotation.HttpMethod;
import stone.mvc.annotation.ResultType;
import stone.mvc.annotation.Route;
import stone.mvc.annotation.RouteParam;
import stone.utils.BeanMapUtil;
import stone.utils.HttpUtil;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

@Route("/demo")
public class SamplesController {

	/**
	 * base/demo/demo.html
	 * 
	 * @throws Exception
	 */
	@Route(value = "/demo.html")
	public String demo() throws Exception {
		return "redirect:/index.jsp";
	}

	/**
	 * base/demo/demo2/demo20.html </p>
	 * 
	 * @param demo2
	 * @throws Exception
	 */
	@Route(value = "/{demo2}/demo20.html")
	public void demo20(@RouteParam("demo2") Map<String, Object> demo2)
			throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultJsonToString(response, demo2.get("demo2") + "");
	}

	/**
	 * 
	 * base/demo/demo3/demo21.html
	 * 
	 * @param demo2
	 * @throws Exception
	 */
	@Route(value = "/{demo2}/demo21.html", resultType = ResultType.JSON)
	public void demo21(@RouteParam("demo2") String demo2) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultJsonToString(response, demo2);
	}

	/**
	 * base/demo/demo3/demo22.html
	 * 
	 * @param demo2
	 * @throws Exception
	 */
	@Route(value = "/{demo1}/demo22.html", resultType = ResultType.JSON)
	public JSONAware demo22(@RouteParam("demo2") DemoEntity demo2) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ket", demo2.getDemo1());
		return new JSONObject(map);
	}

	/**
	 * base/demo/demo99.html?demo=3
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo99.html")
	public void demo99(@FormParam("demo") String demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultJsonToString(response, demo);
	}

	/**
	 * base/demo/demo993.html?demo=3
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo993.html")
	public void demo993(@FormParam("demo") Object[] demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultJsonToString(response, demo.length + "");
	}

	/**
	 * base/demo/demo12/sa/demo6.html</p>
	 * 
	 * @param demo4
	 * @throws Exception
	 */
	@Route(value = "/demo12/{demo}/{demo1}.html", cls = DemoInterceptor.class)
	public void demo12(@RouteParam("demo") Map<String, Object> demo)
			throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.get("demo1") + "");
	}

	/**
	 * 
	 * base/demo/demo4/sa/demo6.html</p>
	 * 
	 * @param demo4
	 * @throws Exception
	 */
	@Route(value = "/demo4/{demo}/{demo1}.html", cls = DemoInterceptor.class)
	public void demo4(@RouteParam("demo") DemoEntity demoEntity)
			throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demoEntity.getDemo());
	}

	/**
	 * base/demo/demo14/sa/demo16.html</p>
	 * 
	 * @param demo4
	 * @throws Exception
	 */
	@Route(value = "/demo14/{demo}/{demo1}.html", cls = DemoInterceptor.class)
	public void demo14(@RouteParam("demo") String demo,
			@RouteParam("demo1") String demo1) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo);
	}

	/**
	 * base/demo/demo17/sa/demo16.html</p>
	 * 
	 * @param demo4
	 * @throws Exception
	 */
	@Route(value = "/demo17/{demo}/{demo1}.html", cls = DemoInterceptor.class)
	public void demo17(@RouteParam("demo") List<String> demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.size() + "");
	}

	/**
	 * base/demo/demo18/sa/demo16.html</p>
	 * 
	 * @param demo4
	 * @throws Exception
	 */
	@Route(value = "/demo18/{demo}/{demo1}.html", cls = DemoInterceptor.class)
	public void demo18(@RouteParam("demo") Object[] demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.length + "");
	}

	/**
	 * base/demo/demo7.html?demo=3&demo1=4 </p>
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo7.html")
	public void demo7(@FormParam("demo") Map<String, Object> demo)
			throws Exception {
		DemoEntity demoEntity = new DemoEntity();
		BeanMapUtil.convertMapToBean(demoEntity, demo);
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demoEntity.getDemo());
		// return demo.get("demo")+"";
	}

	/**
	 * base/demo/demo10.html?demo=10 </p>
	 * 
	 * @param demo
	 * @return
	 * @throws Exception
	 */
	@Route(value = "/demo10.html")
	public String demo10(@FormParam("demo") String demo) throws Exception {
		// HttpServletResponse
		// response=RequestContext.getRequestContext().getHttpServletResponse();
		return "/index.jsp";
	}

	/**
	 * base/demo/demo110.html?demo=10 </p>
	 * 
	 * @param demo
	 * @return
	 * @throws Exception
	 */
	@Route(value = "/demo110.html", method = {HttpMethod.POST})
	public String demo110(@FormParam("demo") String demo) throws Exception {
		// HttpServletResponse
		// response=RequestContext.getRequestContext().getHttpServletResponse();
		return "/index.jsp";
	}

	/**
	 * base/demo/demo33.html?demo=3 </p>
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo33.html")
	public void demo33(@FormParam("demo") String demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo);
	}

	/**
	 * base/demo/demo331.html?demo=3 </p>
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo331.html")
	public void demo331(@FormParam("demo") Map<String, Object> demo)
			throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.get("demo") + "");
	}

	/**
	 * base/demo/demo332.html?demo=3 </p>
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo332.html")
	public void demo332(@FormParam("demo") DemoEntity demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.getDemo());
	}

	/**
	 * base/demo/demo333.html?demo=3 </p>
	 * 
	 * @param demo
	 * @throws Exception
	 */
	@Route(value = "/demo333.html")
	public void demo333(@FormParam("demo") Object[] demo) throws Exception {
		HttpServletResponse response = RequestContext.getRequestContext()
				.getHttpServletResponse();
		HttpUtil.resultHTMLToString(response, demo.length + "");
	}
}
