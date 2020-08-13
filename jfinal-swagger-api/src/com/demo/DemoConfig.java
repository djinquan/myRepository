package com.demo;




import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.swagger.config.SwaggerConfig;
import com.jfinal.swagger.config.SwaggerRoute;
import com.jfinal.swagger.controller.SwaggerController;
import com.jfinal.swagger.enums.InType;
import com.jfinal.template.Engine;

/**
 * 项目全局配置
 */
public class DemoConfig extends JFinalConfig {

	
	/**
	 * 配置常量
	 * @param me
	 */
	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);// 是否开发模式
		me.setInjectDependency(true);// 开启Inject依赖注入
		me.setDenyAccessJsp(false);//4.5版后需要这个设置才可以直接访问JSP
	}

	/**
	 * 配置模板引擎
	 * @param me
	 */
	@Override
	public void configEngine(Engine me) {
		me.setDevMode(true);
		//me.setBaseTemplatePath("views");
		//me.setToClassPathSourceFactory();
	}

	/**
	 * 配置访问路由
	 * @param me
	 */
	@Override
	public void configRoute(Routes me) {
		// me.add(new ProjectRoute());
		// 配置项目访问路由
		me.add("/", IndexController.class);
		me.add("user", UserController.class);
		me.add("demo", DemoController.class);
		
		// 配置Swagger UI访问路由
		me.add(new SwaggerRoute());
	}

	/**
	 * 配置处理器
	 * @param me
	 */
	@Override
	public void configHandler(Handlers me) {
		
	}

	/**
	 * 配置拦截器
	 * @param me
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	/**
	 * 配置插件(注意：依赖于数据库的插件尽量放在后面，避免数据库未初始化就进行调用)
	 * @param me
	 */
	@Override
	public void configPlugin(Plugins me) {
		
	}

	/**
	 * JFinal启动完成后的操作
	 */
	@Override
	public void onStart() {
		SwaggerConfig.addGlobalParam("x-access-token", "token令牌", "string", "jfinal-awesome-token", true, "", InType.HEADER);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " - 服务启动完成！");
	}

	/**
	 * JFinal停止前的操作
	 */
	@Override
	public void onStop() {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " - 服务即将停止！");
	}
}