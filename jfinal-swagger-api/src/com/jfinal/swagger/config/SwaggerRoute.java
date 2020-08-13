package com.jfinal.swagger.config;

import com.jfinal.config.Routes;
import com.jfinal.swagger.controller.SwaggerController;

/**
 * swagger 路由
 */
public class SwaggerRoute extends Routes {

    @Override
    public void config() {
        add("/swagger", SwaggerController.class);
    }
}
