package com.jfinal.swagger.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.kit.Kv;
import com.jfinal.swagger.enums.InType;

/**
 * Swagger UI自定义接口信息配置类
 *
 */
public class SwaggerConfig {

	// 全局参数(如:token)
	private final static List<Kv> globalParamList = new ArrayList<>();
	private final static Kv apiInfo = initApiInfo();
	private final static List<Kv> securityDefinition = new ArrayList<>();
	private final static Set<String> scheme = new LinkedHashSet<>(Arrays.asList("http", "https"));
	private final static Kv definition = Kv.create();
	private final static Kv externalDocs = Kv.by("description", "了解更多信息").set("url", "https://swagger.io/");
	
	/**
	 * 添加全局参数(如:token)
	 * @param name 参数名称
	 * @param remark 参数说明
	 * @param dataType 参数数据类型
	 * @param defaultValue 参数默认值
	 * @param required 参数是否必需
	 * @param format 参数值格式
	 * @param in 请求参数传输类型
	 */
	public static void addGlobalParam(String name, String remark, String dataType, String defaultValue, boolean required, String format, InType in) {
		Kv kv = Kv.by("name", name).set("description", remark)
				.set("type", dataType)
				.set("default", defaultValue)
				.set("required", required)
				.set("format", format);
		if (in == null) {
			kv.set("in", InType.QUERY.getValue());
		} else {
			for (InType item : InType.values()) {
				if (item == in) {
					kv.set("in", item.getValue());
					break;
				}
			}
		}
		globalParamList.add(kv);
	}
	
	/**
	 * 设置API基本信息
	 * @param remark API说明
	 * @param version 版本
	 * @param title 标题
	 * @param termsOfService 服务信息
	 * @param contact 联系信息(如：{"email" : "apiteam@swagger.io"})
	 * @param license 许可信息(如：{"name" : "Apache 2.0", "url" : "http://www.apache.org/licenses/LICENSE-2.0.html"})
	 */
	public static void setApiInfo(String remark, String version, String title, String termsOfService, Map<String, String> contact, Map<String, String> license) {
		apiInfo.set("description", remark).set("version", version).set("title", title).set("termsOfService", termsOfService);
		if (contact != null) {
			apiInfo.set("contact", contact);
		}
		if (license != null) {
			apiInfo.set("license", license);
		}
	}
	
	/**
	 * 添加安全认证信息
	 * @param authType 认证类型(如：oauth2)
	 * @param authorizationUrl 认证地址(如：https://localhost/oauth/authorize)
	 * @param flow (如：implicit)
	 * @param scopes 作用域(具体参考swagger ui相关规范)
	 * @param apiKeyType 认证apiKey类型(具体参考swagger ui相关规范)
	 * @param apiKeyName 认证apiKey名称(具体参考swagger ui相关规范)
	 * @param inType 请求参数传输类型
	 */
    public static void addSecurityDefinition(String authType, String authorizationUrl, String flow, Map<String, String> scopes, String apiKeyType, String apiKeyName, InType in) {
    	Kv auth_keyKv = Kv.by("type", authType).set("authorizationUrl", authorizationUrl)
    			.set("flow", "implicit").set("scopes", scopes == null ? new HashMap<>() : scopes);
    	Kv api_keyKv = Kv.by("type", apiKeyType).set("name", apiKeyName);
    	if (in == null) {
    		api_keyKv.set("in", InType.QUERY.getValue());
		} else {
			for (InType item : InType.values()) {
				if (item == in) {
					api_keyKv.set("in", item.getValue());
					break;
				}
			}
		}
    	securityDefinition.add(Kv.by("my_auth_key", auth_keyKv).set("api_key", api_keyKv));
    }
    
    /**
     * 设置传输协议
     * @param schemes 传输协议(可选值：http、https、ws、wss)
     */
    public static void setSchemes(String... schemes) {
    	List<String> list = Arrays.asList("http", "https", "ws", "wss");
    	if (scheme != null && schemes.length > 0) {
    		boolean isClear = false;
    		for (String item : schemes) {
    			if (list.contains(item)) {
        			if (!isClear) {
        				scheme.clear();
        				isClear = true;
        			}
        			scheme.add(item);
        		}
    		}
    	}
    }
    
    /**
     * 添加对象定义信息
     * @param definitionKey 对象名称(KEY)
     * @param definitionInfo 对象信息(具体参考swagger ui相关规范)
     */
    public static void addDefinition(String definitionKey, Map<String, Object> definitionInfo) {
    	definition.set("definitionKey", definitionInfo);
    }
    
    /**
     * 设置了解更多信息
     * @param text
     * @param url
     */
    public static void setExternalDocs(String text, String url) {
    	externalDocs.set("description", (text == null || "".equals(text.trim())) ? "了解更多信息" : text)
    	.set("url", url);
    }
	
	/**
	 * 获取全局参数列表
	 */
	public static List<Kv> getGlobalParamList() {
		return globalParamList;
	}
	/**
	 * 获取API基本信息
	 */
	public static Kv getApiInfo() {
		return apiInfo;
	}
	/**
	 * 获取安全认证信息定义
	 */
	public static List<Kv> getSecurityDefinition() {
		return securityDefinition;
	}
	/**
	 * 获取传输协议
	 */
	public static Set<String> getScheme() {
		return scheme;
	}
	/**
	 * 获取对象定义
	 */
	public static Kv getDefinition() {
		return definition;
	}
	/**
	 * 获取了解更多信息
	 */
	public static Kv getExternalDocs() {
		return externalDocs;
	}
	
	/**
	 * 初始化接口总描述信息
	 */
    private static Kv initApiInfo() {
    	return Kv.by("description", "swagger-ui在线API文档")
    	.set("version", "1.0.0")
    	.set("title", "swagger-ui在线API文档")
    	.set("termsOfService", "http://swagger.io/terms/")
    	.set("contact", Kv.by("email", "apiteam@swagger.io"))
    	.set("license", Kv.by("name", "Apache 2.0").set("url", "http://www.apache.org/licenses/LICENSE-2.0.html"));
    }
}
