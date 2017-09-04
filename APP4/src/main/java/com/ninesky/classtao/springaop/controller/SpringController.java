package com.ninesky.classtao.springaop.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ninesky.classtao.springaop.vo.RequestToMethodItem;
import com.ninesky.classtao.springaop.vo.URLMapping;
import com.ninesky.common.vo.annotation.GetCache;
import com.ninesky.framework.ResponseUtils;


@Controller
@RequestMapping("/aop")
public class SpringController {
	
	@GetCache(name="您访问了aop1方法")
	@ResponseBody
	@RequestMapping(value="aop1")
	public String aop1(){
		return "AOP";
	}
	
	@GetCache(name="您访问了aop2方法")
	@ResponseBody
	@RequestMapping(value="aop2")
	public String aop2(String string) throws InterruptedException{
		Thread.sleep(1000L);
		return string;
	}
	
	
	/**
	 * 获取Spring映射
	 * **/
	@RequestMapping("url-mapping")
	public @ResponseBody Object getURLMapping() {
		List<URLMapping> list = new ArrayList<URLMapping>();
		Map<RequestMappingInfo, HandlerMethod> map2 = ContextLoaderListener.getCurrentWebApplicationContext()
				.getBean(RequestMappingHandlerMapping.class)
				.getHandlerMethods();
		for (Iterator<RequestMappingInfo> iterator = map2.keySet().iterator(); iterator
				.hasNext();) {
			RequestMappingInfo info = iterator.next();
			URLMapping m = new URLMapping();
			m.setConsumes(String.valueOf(info.getConsumesCondition()));
			m.setCustom(String.valueOf(info.getCustomCondition()));
			m.setHeaders(String.valueOf(info.getHeadersCondition()));
			m.setMethods(String.valueOf(info.getMethodsCondition()));
			m.setParams(String.valueOf(info.getParamsCondition()));
			m.setProduces(String.valueOf(info.getProducesCondition()));
			m.setUrl(info.getPatternsCondition().toString());
			HandlerMethod method = map2.get(info);
			m.setMethodName(method.getMethod().getName());
			m.setClassName(method.getBeanType().getName());
			m.setReturnType(method.getReturnType().getParameterType()
					.toString());
			MethodParameter[] parameters = method.getMethodParameters();
			List<String> list2 = new ArrayList<String>();
			for (MethodParameter methodParameter : parameters) {
				list2.add(methodParameter.getParameterType().getName());
			}
			m.setParameters(String.valueOf(list2));
			ResponseBody annotationClass = method.getMethodAnnotation(ResponseBody.class);
			if(annotationClass != null){
				m.setAnnotationName(annotationClass.toString());
			}
			list.add(m);
		}
		return ResponseUtils.sendList(list);
	}
	
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
    public @ResponseBody
	Object index(HttpServletRequest request)
    {
        ServletContext servletContext = request.getSession().getServletContext();
        if (servletContext == null)
        {
            return null;
        }

        //请求url和处理方法的映射
        List<RequestToMethodItem> requestToMethodItemList = new ArrayList<RequestToMethodItem>();
        
        //获取所有的RequestMapping
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextLoaderListener.getCurrentWebApplicationContext(),HandlerMapping.class, false, true);

        for (HandlerMapping handlerMapping : allRequestMappings.values())
        {
            //本项目只需要RequestMappingHandlerMapping中的URL映射
            if (handlerMapping instanceof RequestMappingHandlerMapping)
            {
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
                for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet())
                {
                    RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                    HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

                    RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
//                    String requestType = SetUtils.first(methodCondition.getMethods()).name();
                    String requestType = String.valueOf(methodCondition.getMethods());
                    PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
//                    String requestUrl = SetUtils.first(patternsCondition.getPatterns());
                    String requestUrl = requestMappingInfo.getPatternsCondition().toString();

                    String controllerName = mappingInfoValue.getBeanType().toString();
                    String requestMethodName = mappingInfoValue.getMethod().getName();
                    Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();
                    RequestToMethodItem item = new RequestToMethodItem(requestUrl, requestType, controllerName, requestMethodName,
methodParamTypes);

                    requestToMethodItemList.add(item);
                }
                break;
            }
        }
        return ResponseUtils.sendSuccess(requestToMethodItemList);
    }
}