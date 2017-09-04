package com.ninesky.classtao.swagger.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ninesky.classtao.swagger.vo.MethodTypeVO;
import com.ninesky.classtao.swagger.vo.MethodVO;
import com.ninesky.classtao.swagger.vo.SwaggerInfoVO;
import com.ninesky.classtao.swagger.vo.SwaggerVO;
import com.ninesky.classtao.swagger.vo.TagVO;
import com.ninesky.framework.ResponseUtils;


@RestController
@RequestMapping("/swagger")
public class swaggerController {
	
	@RequestMapping(value="test")
	public Object aop1(){
		HashMap<String,Object> map = new HashMap<String,Object>();
		HashMap<String,Object> info = new HashMap<String,Object>();
		HashMap<String,Object> concat = new HashMap<String,Object>();
		concat.put("name", "chentianhui@classtao.com");
		HashMap<String,Object> license = new HashMap<String,Object>();
		info.put("description", "API接口文档管理");	
		info.put("version", "V3.8.0");	
		info.put("title", "课道平台API管理");	
		info.put("termsOfService", "www.baidu.com");	
		info.put("contact", concat);	
		info.put("license", license);
		
		map.put("host", "localhost:8080");
		map.put("basePath", "/yay-apidoc");
		List<HashMap<String,Object>> tags = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> tag = new HashMap<String,Object>();
		tag.put("name", "user");
		tag.put("description", "测试描素Tag");
		tags.add(tag);
		map.put("tags", tags);
		
		HashMap<String,Object> paths = new HashMap<String,Object>();
		HashMap<String,Object> get = new HashMap<String,Object>();
		HashMap<String,Object> getDetail = new HashMap<String,Object>();
		List<String> tags2 = new ArrayList<String>();
		tags2.add("user");
		getDetail.put("tags", tags2);
		getDetail.put("summary", "Logs user into the system");
		getDetail.put("operationId", "loginUserUsingGET");
		
		List<String> consumes = new ArrayList<String>();
		consumes.add("application/json");
		getDetail.put("consumes", consumes);
		getDetail.put("produces", consumes);
		
		List<HashMap<String,Object>> parameters = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> param1 = new HashMap<String,Object>();
		param1.put("name", "username");
		param1.put("in", "query");
		param1.put("description", "username字段描述");
		param1.put("required", true);
		param1.put("type", "string");
		parameters.add(param1);
		HashMap<String,Object> param2 = new HashMap<String,Object>();
		param2.put("name", "password");
		param2.put("in", "query");
		param2.put("description", "password的字段描述");
		param2.put("required", true);
		param2.put("type", "string");
		parameters.add(param2);
		getDetail.put("parameters", parameters);
		
		
		HashMap<String,Object> responses = new HashMap<String,Object>();
		HashMap<String,Object> response200 = new HashMap<String,Object>();
		response200.put("description", "OK");
		HashMap<String,Object> schema = new HashMap<String,Object>();
		schema.put("type", "string");
		response200.put("description", "username字段描述");
		response200.put("schema", schema);
		
		HashMap<String,Object> response400 = new HashMap<String,Object>();
		response400.put("description", "Invalid username/password supplied");
		
		HashMap<String,Object> response401 = new HashMap<String,Object>();
		response401.put("description", "Forbidden");
		
		HashMap<String,Object> response403 = new HashMap<String,Object>();
		response403.put("description", "Not Found");

		responses.put("200", response200);
		responses.put("400", response400);
		responses.put("401", response401);
		responses.put("403", response403);
		getDetail.put("responses", responses);
		
		
		get.put("get", getDetail);
		paths.put("/api/user/login", get);
		
		
//		map.put("info", info);
		map.put("paths", paths);
		map.put("swagger", "2.0");
		
		
		
		return map;
	}
	
	@RequestMapping(value="test2")
	public Object aop2(HttpServletRequest request){
		SwaggerVO swagger = new SwaggerVO();
		swagger.setBasePath("/lezhi");
		swagger.setHost("localhost:8080");
		HashMap<String,Object> paths = new HashMap<String,Object>();
		swagger.setPaths(paths);
		swagger.setSwagger("2.0");
		SwaggerInfoVO info = new SwaggerInfoVO();
		swagger.setInfo(info);
		List<TagVO> tags = new ArrayList<TagVO>();
		TagVO tag = new TagVO();
		tag.setName("user");
		tag.setDescription("TagVO的描述字段");
		tags.add(tag);
		swagger.setTags(tags);
		
		//这里添加URL-PATH到paths
		List<MethodVO> MethodVOList = getURLPathList(request);
		for (MethodVO item:MethodVOList) {
			MethodTypeVO mt = new MethodTypeVO();
			System.out.println(item.requestType);
			if ("[GET]".equals(item.requestType))
				mt.setGet(item);
			if ("[]".equals(item.requestType))
				mt.setGet(item);
			paths.put(item.getRequestUrl().substring(1, item.getRequestUrl().length()-1), mt);
		}
		return swagger;
	}
	
	private List<MethodVO>  getURLPathList(HttpServletRequest request)
    {
        ServletContext servletContext = request.getSession().getServletContext();
        if (servletContext == null)
        {
            return null;
        }

        //请求url和处理方法的映射
        List<MethodVO> MethodVOList = new ArrayList<MethodVO>();
        
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
                    MethodVO item = new MethodVO(requestUrl, requestType, controllerName, requestMethodName,methodParamTypes);
                    MethodVOList.add(item);
                    break;
                }
                break;
            }
        }
        return MethodVOList;
    }
	
}