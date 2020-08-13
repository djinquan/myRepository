/**
 * 
 */
package com.example.demo.config.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author djinquan
 * 2020年3月9日
 * 
 */
@ControllerAdvice
public class ExceptionAdvice {
	public static final String ERROR_VIEW = "error";

		@ExceptionHandler(value = Exception.class)
		public Object errorHandler(HttpServletRequest request, HttpServletResponse resp, Exception e) throws Exception {
			e.printStackTrace();
			if (isAjax(request)) {
				Map<String,Object> map=new HashMap<>();
				map.put("result", "fail");
				map.put("msg",e.getMessage());
				return map;
			} else {

				ModelAndView mav = new ModelAndView();
				mav.addObject("e", e);
				mav.addObject("url", request.getRequestURL());
				System.out.println("-----------------------------------------------");
				System.out.println(e.getMessage());
				System.out.println(request.getRequestURL());
				mav.setViewName(ERROR_VIEW);
				return mav;
			}
		}

		public static boolean isAjax(HttpServletRequest request) {

			return (request.getHeaders("X-Requested-With") != null
					&& "XMLHttpReqeust".equals(request.getHeaders("X-Requested-With").toString()));
		}

}
