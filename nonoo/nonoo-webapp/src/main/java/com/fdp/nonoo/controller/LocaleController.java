package com.fdp.nonoo.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

@Controller("localeController")
@RequestMapping("/locale")
public class LocaleController {

	@Resource(name = "localeResolver")
	private LocaleResolver localeResolver;

	@RequestMapping("/change")
	public String change(String locale, HttpServletRequest request,
			HttpServletResponse response) {
		Locale local = new Locale(locale);
		System.out.println("current local is "+local.getDisplayLanguage());
		localeResolver.setLocale(request, response, local);
		return "redirect:/demo/index.do";
	}

}
