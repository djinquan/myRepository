/**
 * 
 */
package com.example.demo.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Contant;
import com.example.demo.config.UtilConfig;
import com.example.demo.config.EhcacheUtil.EhcacheUtil;
import com.example.demo.model.User;
import com.example.demo.services.UserService;
import com.example.demo.util.FileUploadUtil;

/**
 * @author djinquan
 * 2020年1月13日
 * 
 */
@Controller
public class TestController {
	
	private static final String EXD_PATH = "userPhoto";
	private static final String BASE_DIR = "/static/upload";
	
	// 当前环境配置名称
    @Value("${profile.name}") //读取当前环境配置名称
    private String profileName;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UtilConfig uc;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest req,String name) {
		//System.out.println(servletContext.getRealPath(""));//当前路径
		/*HttpSession session=req.getSession();
		session.setAttribute("user", "你好");*/
		for(int i=76635;i<10000000;i++) {
			User user=new User();
			user.setId(i);
			user.setName("djq"+i);
			user.setSex(i%2);
			user.setAge(i%100);
			userService.save(user);
			System.out.println("剩余："+(10000000-i));
		}
		//你好
		return "hello1111";
	}
	
	@RequestMapping("/allUser")
	@ResponseBody
	public String AllUser(HttpServletRequest req) {
		List<User> list=userService.getAllUser();
		StringBuffer sb=new StringBuffer();
		for (User user : list) {
			sb.append(user.getName()).append(" ");
		}
		//你好
		return sb.toString();
	}
	
	@RequestMapping("/jsp/hello")
	public String helloJSP(HttpServletRequest req,String name) {
		//System.out.println(servletContext.getRealPath(""));//当前路径
		throw new RuntimeException("hello");
		/*System.out.println(name);
		HttpSession session=req.getSession();
		session.setAttribute("user", "你好");
		return "hello";*/
	}
	
	
	@RequestMapping("/jsp/user")
	public String userJSP(Model mv,String name) {
		System.out.println("当前环境:"+profileName);
		List<User> list=userService.getAllUser();
		mv.addAttribute("list", list);
		//你好
		return "user/user";
	}
	
	@RequestMapping("/putUser")
	@ResponseBody
	public User putUser(Long id) {
		User user=userService.getById(id);
		//EhcacheUtil.getInstance().putValue("user1", user);
		redisTemplate.opsForValue().set(id+"", user);
		return user;
	}
	
	@RequestMapping("/getUser")
	@ResponseBody
	public User getUser(Long id) {
		//User user=(User) EhcacheUtil.getInstance().getKey("user1");
		User user=(User) redisTemplate.opsForValue().get(id+"");
		return user;
	}
	
	@RequestMapping("/user/fileUpload")
	public String fileUpload(Model mv,String name) {
		//你好
		return "user/fileUpload";
	}
	
	@RequestMapping("/user/fileUploadSave")
	@ResponseBody
	public String fileUploadSave(MultipartFile file,HttpServletRequest req) {
		//String path=upload(file, EXD_PATH, req.getSession().getServletContext().getRealPath(BASE_DIR));
		String path=FileUploadUtil.uploadFile(file, BASE_DIR, EXD_PATH, req).get("webPath");
		System.out.println("path:"+path);
		return "user/fileUpload";
	}
	
	@RequestMapping("/redirect")
	public ModelAndView redirect(ModelAndView mv) {
		System.out.println(Contant.chassisId+" "+Contant.typeId);
		mv.addObject("a", "a")
		.addObject("b", "b")
		.setViewName("redirect:toRedirect");
		return mv;
	}
	
	@RequestMapping("/toRedirect")
	public String toRedirect(String a,String b) {
		System.out.println("a:"+a+" b:"+b);
		return "toRedirect";
	}
	
	private String upload(MultipartFile file, String path, String fileLocation) {
        String fileFinishName = null;
        try {
            // 如果目录不存在则创建
            File uploadDir = new File(fileLocation);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            //获取源文件名称
            String fileName = file.getOriginalFilename();
            fileFinishName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
            //上传文件到指定目录下
            File uploadFile = new File(uploadDir + uploadDir.separator + fileFinishName);
            file.transferTo(uploadFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return path + "/" + fileFinishName;
    }
	
	
	@RequestMapping("/save")
	@ResponseBody
	public User save() {
		User user=new User();
		user.setId(3);
		user.setName("djqT");
		user.setAge(13);
		user.setSex(2);
		userService.save(user);
		return user;
	}

}
