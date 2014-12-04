package com.fdp.nonoo.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fdp.nonoo.common.Principal;
import com.fdp.nonoo.entity.Member;
import com.fdp.nonoo.service.MemberService;

public class MemberInterceptor extends HandlerInterceptorAdapter {
	private static final String PREFIX = "/redirect:";
	private static final String URL_REDIRECT = "redirectUrl";
	private static final String CURRENT_MEMBER = "member";
	private static final String DEFAULT_LOGIN_URL = "/login.jhtml";
	private String loginUrl = DEFAULT_LOGIN_URL;

	@Value("${url_escaping_charset}")
	private String charset;

	@Resource(name = "memberService")
	private MemberService memberService;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws UnsupportedEncodingException, IOException {
		HttpSession session = request.getSession();
		Principal principal = (Principal) session
				.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (principal != null)
			return true;
		String str1 = request.getHeader("X-Requested-With");
		if ((str1 != null) && (str1.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String str2 = (request.getQueryString() != null) ? request
					.getRequestURI() + "?" + request.getQueryString() : request
					.getRequestURI();
			response.sendRedirect(request.getContextPath() + loginUrl + "?"+ URL_REDIRECT + "=" + URLEncoder.encode(str2, charset));
		} else {
			response.sendRedirect(request.getContextPath() + loginUrl);
		}
		return false;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if (modelAndView == null)
			return;
		String str = modelAndView.getViewName();
		if (StringUtils.startsWith(str, PREFIX))
			return;
		modelAndView.addObject(CURRENT_MEMBER, memberService.getCurrent());
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}