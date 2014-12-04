package com.fdp.nonoo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("mobileController")
@RequestMapping({ "/mobile" })
public class MobileController {

	@RequestMapping(method = { RequestMethod.GET })
	public String index(String redirectUrl, HttpServletRequest request,
			ModelMap model) {

		
		
		return "/mobile/index";
	}

}
