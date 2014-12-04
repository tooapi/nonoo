package com.fdp.nonoo.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fdp.nonoo.service.MailService;

@Controller("indexController")
@RequestMapping({ "/" })
public class IndexController {

	@Resource(name = "mailService")
	MailService mailService;

	@RequestMapping({ "/index" })
	public String index(HttpServletRequest request,HttpServletResponse response) {
		
		
		

		return "/index";
	}

	@RequestMapping({ "/test" })
	public String test() {

	

		return "/test";
	}
	
	

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam MultipartFile[] myfiles,
			HttpServletRequest request) throws IOException {
		for (MultipartFile myfile : myfiles) {
			if (myfile.isEmpty()) {
				System.out.println("文件未上传");
			} else {
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件原名: " + myfile.getOriginalFilename());
				System.out.println("========================================");
				// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
				String realPath = request.getSession().getServletContext()
						.getRealPath("/WEB-INF/upload");
				// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
				FileUtils.copyInputStreamToFile(myfile.getInputStream(),
						new File(realPath, myfile.getOriginalFilename()));
			}
		}
		return "upload";
	}

}
