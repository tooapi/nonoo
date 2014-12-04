package com.fdp.nonoo;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContext;

@Component("applicationContext")
@Lazy(false)
public class ApplicationContext implements DisposableBean,
		ApplicationContextAware, ServletContextAware {

	private static final String LOCALE_RESOLVER = "localeResolver";

	private static final String THEME_RESOLVER = "themeResolver";

	private static org.springframework.context.ApplicationContext context;

	private static ServletContext servletContext;


	public void setApplicationContext(
			org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;

	}


	public void setServletContext(ServletContext appServletContext) {
		servletContext = appServletContext;

	}

	public void destroy() {
		context = null;
	}

	public static org.springframework.context.ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String name) {
		Assert.hasText(name);
		return context.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return context.getBean(name, type);
	}

	public static String getMessage(String code, Object[] args) {
		LocaleResolver localeResolver = (LocaleResolver) getBean(
				LOCALE_RESOLVER, LocaleResolver.class);
		RequestContext requestContext = ((RequestContext) servletContext.getAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT"));
		if (requestContext == null) {
			servletContext.setAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT",requestContext);
		}
	
		Locale locale = localeResolver.resolveLocale(null);
		System.out.println("locale is :::::::" + locale);
		return context.getMessage(code, args, locale);
	}

	public static String getCurrentTheme(String theme) {
		ThemeResolver themeResolver = (ThemeResolver) getBean(THEME_RESOLVER,ThemeResolver.class);
		themeResolver.resolveThemeName(null);

		return null;
	}

}
