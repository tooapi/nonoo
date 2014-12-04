package com.fdp.nonoo.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fdp.nonoo.util.CookieUtils;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static final String DEFAULT_COOKIE_TOKEAN = "token";

	private static final String DEFAULT_MISSING_TOKEN = "Bad or missing token!";

	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws IOException {
		String token = CookieUtils.getCookie(request, DEFAULT_COOKIE_TOKEAN);
		if (request.getMethod().equalsIgnoreCase("POST")) {
			String head = request.getHeader("X-Requested-With");
			if ((head != null) && (head.equalsIgnoreCase("XMLHttpRequest"))) {
				if ((token != null)&& (token.equals(request.getHeader(DEFAULT_COOKIE_TOKEAN)))){
					return true;
				}else{
				response.addHeader("tokenStatus", "accessDenied");
				}
			} else if ((token != null)&& (token.equals(request.getParameter(DEFAULT_COOKIE_TOKEAN)))) {
				return true;
			}
			if (token == null) {
				token = UUID.randomUUID().toString();
				CookieUtils.addCookie(request, response, DEFAULT_COOKIE_TOKEAN,token);
			}
			response.sendError(403, DEFAULT_MISSING_TOKEN);
			return false;
		}
		if (token == null) {
			token = UUID.randomUUID().toString();
			CookieUtils.addCookie(request, response, DEFAULT_COOKIE_TOKEAN,token);
		}
		request.setAttribute(DEFAULT_COOKIE_TOKEAN, token);
		return true;
	}
}