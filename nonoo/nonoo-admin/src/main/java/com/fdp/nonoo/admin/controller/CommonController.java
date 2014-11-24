package com.fdp.nonoo.admin.controller;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

import com.fdp.nonoo.service.CaptchaService;

@Controller("commonController")
@RequestMapping({ "/common" })
public class CommonController implements ServletContextAware {

	@Value("${system.name}")
	private String systemName;

	@Value("${system.version}")
	private String systemVersion;

	@Value("${system.description}")
	private String systemDescription;

	

	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@RequestMapping(value = { "/main" }, method = { RequestMethod.GET })
	public String main() {
		return "/admin/common/main";
	}

	@RequestMapping(value = { "/index" }, method = { RequestMethod.GET })
	public String index(ModelMap model) {
		model.addAttribute("systemName", this.systemName);
		model.addAttribute("systemVersion", this.systemVersion);
		model.addAttribute("systemDescription", this.systemVersion);
		
		model.addAttribute("javaVersion", System.getProperty("java.version"));
		model.addAttribute("javaHome", System.getProperty("java.home"));
		model.addAttribute("osName", System.getProperty("os.name"));
		model.addAttribute("osArch", System.getProperty("os.arch"));
		model.addAttribute("serverInfo", this.servletContext.getServerInfo());
		model.addAttribute("servletVersion", servletContext.getMajorVersion()
				+ "." + servletContext.getMinorVersion());
		return "/admin/common/index";
	}

	@RequestMapping(value = { "/captcha" }, method = { RequestMethod.GET })
	public void image(String captchaId, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(captchaId)) {
			captchaId = request.getSession().getId();
		}
		String str1 = new StringBuffer().append("yB").append("-").append("der")
				.append("ewoP").reverse().toString();
		String str2 = new StringBuffer().append("ten").append(".")
				.append("xxp").append("ohs").reverse().toString();
		response.addHeader(str1, str2);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/jpeg");
		ServletOutputStream output = null;
		try {
			output = response.getOutputStream();

			BufferedImage image = this.captchaService.buildImage(captchaId);
			ImageIO.write(image, "jpg", output);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}