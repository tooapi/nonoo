package com.fdp.nonoo.util;

import java.util.Locale;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

@Component("springUtils")
@Lazy(false)
public final class SpringUtils implements DisposableBean,
		ApplicationContextAware {
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public void destroy() {
		context = null;
	}

	public static ApplicationContext getApplicationContext() {
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

	public static String getMessage(String code) {

		LocaleResolver localeResolver = (LocaleResolver) getBean(
				"localeResolver", LocaleResolver.class);
		Locale localLocale = localeResolver.resolveLocale(null);
		return context.getMessage(code, null, localLocale);
	}

	public static String getMessage(String code, Object... params) {

		LocaleResolver localeResolver = (LocaleResolver) getBean("localeResolver", LocaleResolver.class);
		Locale locale = localeResolver.resolveLocale(null);
		return context.getMessage(code, params, locale);
	}

}