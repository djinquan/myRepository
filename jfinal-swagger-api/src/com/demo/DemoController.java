package com.demo;

import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.swagger.annotation.ActionApi;
import com.jfinal.swagger.annotation.Api;

@Api(remark="DemoController，通过sort将其顺序调整到最前", sort=3)
public class DemoController extends Controller {

	@ActionApi(remark="DemoController首页Action")
	public void index() {
		renderJson(Kv.by("demo", "Hello JFinal!"));
	}
	
	@ActionApi(remark="输出文本")
	public void text() {
		renderText("JFinal Awesome!");
	}
}
