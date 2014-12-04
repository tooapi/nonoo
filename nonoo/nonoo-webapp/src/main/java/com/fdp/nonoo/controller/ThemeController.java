package com.fdp.nonoo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ThemeResolver;

@Controller("themeController")
@RequestMapping({ "/theme" })
public class ThemeController {

	@Resource(name = "themeResolver")
	private ThemeResolver themeResolver;

	@RequestMapping("/change")
	public void change(HttpServletRequest request,
			HttpServletResponse response, String theme) {
		System.out.println("current theme is "+ themeResolver.resolveThemeName(request));
		themeResolver.setThemeName(request, response, theme);
		System.out.println("current theme change to "+ themeResolver.resolveThemeName(request));
	}

}
