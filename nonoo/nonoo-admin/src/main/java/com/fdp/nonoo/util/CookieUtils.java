package com.fdp.nonoo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.fdp.nonoo.NonooContext;

public final class CookieUtils {
	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge, String path, String domain, Boolean secure) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie cookie = new Cookie(name, value);
			if (maxAge != null)
				cookie.setMaxAge(maxAge);
			if (StringUtils.isNotEmpty(path))
				cookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				cookie.setDomain(domain);
			if (secure != null)
				cookie.setSecure(secure);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge) {
		
		addCookie(request, response, name, value, maxAge,NonooContext.getCookiepath(), NonooContext.getCookiedomain(),null);
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
	
		addCookie(request, response, name, value, null,NonooContext.getCookiepath(), NonooContext.getCookiedomain(),null);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Assert.hasText(name);
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			try {
				name = URLEncoder.encode(name, "UTF-8");
				for (Cookie cookie : cookies){
					if (name.equals(cookie.getName())){
						return URLDecoder.decode(cookie.getValue(),"UTF-8");
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String path,
			String domain) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path))
				cookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				cookie.setDomain(domain);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		
		removeCookie(request, response, name, NonooContext.getCookiepath(),NonooContext.getCookiedomain());
	}
}