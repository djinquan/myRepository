package com.jfinal.swagger.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.core.Action;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.swagger.annotation.ActionApi;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.Item;
import com.jfinal.swagger.annotation.Param;
import com.jfinal.swagger.annotation.Params;
import com.jfinal.swagger.annotation.Responses;
import com.jfinal.swagger.annotation.SecurityApi;
import com.jfinal.swagger.config.SwaggerConfig;
import com.jfinal.swagger.enums.InType;
import com.jfinal.template.Engine;

/**
 * Swagger UI Controller
 */
public class SwaggerController extends Controller {
	
	// 请求方法
	private final String HTTP_METHOD = "get,post,head,put,delete";
	private final String DEFAULT_STYLE = "layui";
	private final Set<String> STYLE_SET = new HashSet<String>(Arrays.asList("layui,default".split(",")));
	private static final String RESOURCE_BASE_PATH = "../../swagger";
	private static Engine engine = Engine.use().setBaseTemplatePath(RESOURCE_BASE_PATH);
	
	static {
		engine.setToClassPathSourceFactory();
	}

	/**
	 * api页面
	 */
    public void index() {
    	String style = getPara(0, DEFAULT_STYLE);
    	style = style == null || !STYLE_SET.contains(style.trim()) ? DEFAULT_STYLE : style.trim();
    	renderHtml(engine.getTemplate(style + "/index.html").renderToString(null));
    }

    /**
     * 获取所有api接口
     */
    public void api() {
    	// 所有接口类tag描述信息
    	List<Kv> tags = new ArrayList<>();
    	// 所有接口路径
        Map<String, Map<String, Kv>> paths = new LinkedHashMap<>();
        
    	// 扫描所有API类Action注解
    	Map<Class<? extends Controller>, List<Action>> classMap = scanAllApiAction();
        classMap.keySet().forEach(clazz -> {
        	List<Action> actions = classMap.get(clazz);
        	actions.forEach(action -> {
        		Method method = action.getMethod();
        		ActionApi ApiAction = method.getAnnotation(ActionApi.class);
        		String httpMethod = ApiAction.httpMethod();
        		if (httpMethod == null || "".equals(httpMethod.trim())) {
        			// 请求方法：HEAD：请求页面的首部、GET：查看, POST：创建, PUT：更新, DELETE：删除
        			httpMethod = HTTP_METHOD;
        		}
        		
        		Map<String, Kv> methodApiMap = new HashMap<>();
        		String[] httpMethods = httpMethod.split(",");// 支持多个请求方法
        		for (String methodItem : httpMethods) {
        			methodItem = methodItem.trim().toLowerCase();
        			if (!Arrays.asList(HTTP_METHOD.split(",")).contains(methodItem)) {
        				continue;
        			}
        			// 获取参数注解信息
        			List<Param> params = new ArrayList<>();
        			if (method.isAnnotationPresent(Params.class)) {
        				params.addAll(Arrays.asList(method.getAnnotation(Params.class).value()));
        			}
        			if (method.isAnnotationPresent(Param.class)) {
        				// Java8新特性：支持多注解
        				Param[] paramArray = method.getAnnotationsByType(Param.class);
        				params.addAll(Arrays.asList(paramArray));
        			}
        			
        			// 构建参数列表(包含全局参数)
        			List<Kv> paramList = new ArrayList<>(SwaggerConfig.getGlobalParamList());
        			params.forEach(i -> {
        				// 注意：swaggerUI 使用Java的关键字default作为默认值,此处将defaultValue转换为default
        				Kv kv = Kv.by("name", i.name()).set("description", i.remark()).set("required", i.required())
        						.set("type", i.dataType()).set("format", i.format()).set("default", i.defaultValue());
        				if ("file".equals(i.dataType())) {
        					kv.set("in", InType.FORM_DATA.getValue());
        				} else {
        					kv.set("in", i.in().getValue());
        				}
        				kv.set("schema", i.schema());// 
        				kv.set("items", "");//{type:"string",enum:["a","b","c"],default:""}
        				kv.set("collectionFormat", i.collectionFormat());
        				paramList.add(kv);
        			});
        			// 每个action注解信息
        			Kv actionKv = Kv.by("parameters", paramList)
        					.set("operationId", method.getName())
        					.set("tags", toSet(notBlank(ApiAction.tag()) ? ApiAction.tag() : actions.get(0).getControllerKey()))
        					.set("description", ApiAction.remark())
        					.set("summary", ApiAction.summary())
        					.set("consumes", toSet("application/json" + "," + ApiAction.consumes()))
        					.set("produces", toSet(ApiAction.consumes() + "," + "application/json"));
        			
        			// response
        			Kv responseKv = Kv.by("200", Kv.by("description", "Success"))
        					.set("400", Kv.by("description", "Invalid ID supplied"))
        					.set("403", Kv.by("description", "Forbiden"))
        					.set("404", Kv.by("description", "Not found"))
        					.set("405", Kv.by("description", "Validation exception"))
        					.set("500", Kv.by("description", "Interneral error"));
        			if (method.isAnnotationPresent(Responses.class)) {
        				Responses response = method.getAnnotation(Responses.class);
        				responseKv.set(response.key(), Kv.by("description", response.remark()));
        				Kv itemKv = Kv.create();
        				for (Item i : response.schemaItems()) {
        					itemKv.set(i.key(), i.value());
        				}
        				responseKv.set("schema", Kv.by("type", response.schemaType()).set("items", itemKv));
        				actionKv.set("responses", responseKv);
        			}
        			
        			// security
        			if (method.isAnnotationPresent(SecurityApi.class)) {
        				// Java8新特性：支持多注解
        				SecurityApi[] securityApis = method.getAnnotationsByType(SecurityApi.class);
        				List<Kv> securitys = new ArrayList<>();
        				for (SecurityApi item : securityApis) {
        					Kv security = Kv.by(item.key(), toSet(item.value()));
        					securitys.add(security);
        				}
        				actionKv.set("security", securitys);
        			}
        			
        			methodApiMap.put(methodItem, actionKv);
        		}
        		
        		paths.put(notBlank(ApiAction.url()) ? ApiAction.url() : action.getActionKey(), methodApiMap);
        	});
        	
        	Api api = clazz.getAnnotation(Api.class);
        	Kv tag = Kv.by("name", actions.get(0).getControllerKey()).set("description", api.remark() + " (" + clazz.getSimpleName() + ")");
        	if (notBlank(api.outerUrl()) || notBlank(api.outerRemark())) {
        		tag.set("externalDocs", Kv.by("description", api.outerRemark()).set("url", api.outerUrl()));
        	}
        	tags.add(tag);
        });
        
        // 获取host
        String host = getRequest().getServerName();
        if (this.getRequest().getServerPort() != 80) {
        	host += ":" + getRequest().getServerPort();
        }
        Okv allApi = Okv.by("swagger", "2.0")
        		.set("info", SwaggerConfig.getApiInfo())
        		.set("host", host)
        		.set("basePath", "")
        		.set("tags", tags)
        		.set("schemes", SwaggerConfig.getScheme())// 传输协议Scheme：HTTP、HTTPS
        		.set("paths", paths)
        		.set("securityDefinitions", SwaggerConfig.getSecurityDefinition())
        		.set("definitions", SwaggerConfig.getDefinition())
        		.set("externalDocs", SwaggerConfig.getExternalDocs());
        renderJson(allApi);
    }
    
    /**
     * 扫描所有API类Action注解
     * @return
     */
    private static Map<Class<? extends Controller>, List<Action>> scanAllApiAction() {
    	Map<Class<? extends Controller>, List<Action>> apiMap = new HashMap<>();
    	// 获取遍历所有action
        JFinal.me().getAllActionKeys().forEach(actionKey -> {
        	Action action = JFinal.me().getAction(actionKey, new String[1]);
    		Class<? extends Controller> controller = action.getControllerClass();
    		
    		if (apiMap.containsKey(controller)) {
    			if (action.getMethod().isAnnotationPresent(ActionApi.class)) {
    				List<Action> actions = apiMap.get(controller);
    				if (!actions.contains(action)) {
    					actions.add(action);
            			apiMap.put(controller, actions);
    				}
    			}
    		} else {
    			if (controller.isAnnotationPresent(Api.class)) {
    				if (action.getMethod().isAnnotationPresent(ActionApi.class)) {
    					List<Action> actions = new ArrayList<>();
    					actions.add(action);
        				apiMap.put(controller, actions);
    				}
        		}
    		}
        });
        List<Class<? extends Controller>> list = new ArrayList<>(apiMap.keySet());
        list.sort((Class<? extends Controller> clazz1, Class<? extends Controller> clazz2) -> clazz1.getAnnotation(Api.class).sort() - clazz2.getAnnotation(Api.class).sort());
        
        Map<Class<? extends Controller>, List<Action>> result = new LinkedHashMap<>();
        list.forEach(i -> {
        	List<Action> actions = apiMap.get(i);
        	actions.sort((Action action1, Action action2) -> action1.getMethod().getAnnotation(ActionApi.class).sort() - action2.getMethod().getAnnotation(ActionApi.class).sort());
        	result.put(i, actions);
        });
        return result;
    }
    
    /**
     * 字符串转set集合
     * @param value
     * @return
     */
    private static Set<String> toSet(String value) {
    	Set<String> result = new LinkedHashSet<>();
    	if (value != null) {
    		String[] values = value.split(",");
    		for (String item : values) {
    			if (notBlank(item)) {
    				result.add(item.trim());
    			}
    		}
    	}
    	return result;
    }
    /**
     * 判断字符串非空
     * @param value
     * @return
     */
    private static boolean notBlank(String value) {
    	return value != null && !"".equals(value.trim());
    }
}