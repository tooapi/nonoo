package com.fdp.nonoo.controller;

import java.awt.image.BufferedImage;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdp.nonoo.service.CaptchaService;
import com.fdp.nonoo.service.RSAService;

@Controller("commonController")
@RequestMapping({ "/common" })
public class CommonController {

	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	@Resource(name = "rsaService")
	private RSAService rsaService;

	@RequestMapping({ "/resource_not_found" })
	public String resourceNotFound() {
		return "/common/resource_not_found";
	}

	@RequestMapping({ "/error" })
	public String error(HttpServletRequest request) {

		return "/common/error";
	}

	@RequestMapping(value = { "/public_key" }, method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> map = new HashMap<String, String>();
		map.put("modulus",Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		map.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return map;
	}

	@RequestMapping(value = { "/captcha" }, method = { RequestMethod.GET })
	public void captcha(String captchaId, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(captchaId))
			captchaId = request.getSession().getId();
		String str1 = new StringBuffer().append("yB").append("-").append("der")
				.append("ewoP").reverse().toString();
		String str2 = new StringBuffer().append("ten").append(".")
				.append("xxp").append("ohs").reverse().toString();
		response.addHeader(str1, str2);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/png");
		ServletOutputStream output = null;
		try {
			output = response.getOutputStream();
			BufferedImage image = captchaService.buildImage(captchaId);
			ImageIO.write(image, "png", output);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}