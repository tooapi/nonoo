package com.fdp.nonoo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fdp.nonoo.util.CookieUtils;

public class ListInterceptor extends HandlerInterceptorAdapter {
	private static final String redirect = "redirect:";
	private static final String listQuery = "listQuery";

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if ((modelAndView == null) || (!(modelAndView.isReference())))
			return;
		String view = modelAndView.getViewName();
		if (!(StringUtils.startsWith(view, redirect)))
			return;
		String list = CookieUtils.getCookie(request, listQuery);
		if (!(StringUtils.isNotEmpty(list)))
			return;
		if (StringUtils.startsWith(list, "?"))
			list = list.substring(1);
		if (StringUtils.contains(view, "?"))
			modelAndView.setViewName(view + "&" + list);
		else
			modelAndView.setViewName(view + "?" + list);
		CookieUtils.removeCookie(request, response, listQuery);
	}
}