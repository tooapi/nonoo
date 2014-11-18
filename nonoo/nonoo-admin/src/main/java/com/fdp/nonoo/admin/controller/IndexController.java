package com.fdp.nonoo.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("indexController")
public class IndexController {

	@RequestMapping({ "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response) {

		return "/index";
	}

	@RequestMapping({ "/test" })
	public String test() {

		return "/test";
	}

}
