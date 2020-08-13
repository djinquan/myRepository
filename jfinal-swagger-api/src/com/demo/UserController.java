package com.demo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.swagger.annotation.ActionApi;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.Param;

@Api(tag="/aaa", remark="UserController，排序到末尾", sort=30)
public class UserController extends Controller {

	@ActionApi(remark="DemoController首页Action", sort=1)
	public void index() {
		renderJson(Kv.by("user", "Hello JFinal!"));
	}
	
	@ActionApi(remark="输出文本", sort=2)
	@Param(name="text", remark="文本内容")
	public void text() {
		renderText("输出接收文本：<br>" + get("text"));
	}
}
