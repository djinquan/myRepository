/**
 * 
 */
package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

/**
 * @author djinquan
 * 2019年6月4日
 * 文件上传
 */
public class FileUploadUtil {
	
	public static Map<String,String> uploadFile(MultipartFile uf,String basePath,String dirPath,HttpServletRequest req){
		Map<String,String> map=new HashMap<String, String>();
		String fileName=uf.getOriginalFilename();
		System.out.println(uf.getName()+" "+uf.getOriginalFilename());
		if(fileName==null||fileName.trim().equals("")){
			map.put("result", "fail");
			return map;
        }
        //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
        //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
        int index = fileName.lastIndexOf(".");
        String suffix=fileName.substring(index+1).toLowerCase();
        fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
        
        String newFileName = System.currentTimeMillis()+"."+suffix;
        String fullPath =req.getServletContext().getRealPath(basePath);
		String dirTypePath = CalendarUtil.formatDate(new Date(), "yyyyMM");
		File file =  new File(fullPath+"/"+dirPath+"/"+dirTypePath+"/");
		if(!file.exists()) {
			file.mkdirs();
		}
		fullPath = fullPath+"/"+dirPath+"/"+dirTypePath + "/" + newFileName;
		String webPath = basePath+"/"+dirPath+"/"+dirTypePath+"/"+newFileName;//返回上传的路径
		
		 //获取item中的上传文件的输入流
		File newFile=new File(fullPath);
		try {
			/*newFile.createNewFile();
			FileInputStream fis=new FileInputStream(uf);
			FileOutputStream fos=new FileOutputStream(newFile);
			FileChannel fci=fis.getChannel();
			FileChannel fco=fos.getChannel();
			fci.transferTo(0, fci.size(), fco);
			Thread.sleep(10);*/
			uf.transferTo(newFile);
		}
		catch(Exception e) {
			e.printStackTrace();
			map.put("result", "fail");
			return map;
		}
		map.put("webPath", webPath);
		map.put("newFileName", newFileName);
		System.out.println("newFileName："+newFileName);
		return map;
	}

}
