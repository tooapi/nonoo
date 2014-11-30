package com.fdp.nonoo.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fdp.nonoo.common.CommonAttributes;
import com.fdp.nonoo.common.EnumConverter;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

public final class FreemarkerUtils {
	private static final ConvertUtilsBean convertUtilsBean = new CustomerConvertUtilsBean();

	static {
		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(dateConverter, Date.class);
	}

	public static String process(String template, Map<String, ?> model) {
		Configuration configuration = null;
		ApplicationContext context = SpringUtils.getApplicationContext();
		if (context != null) {
			FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer) SpringUtils.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
			if (freeMarkerConfigurer != null){
				configuration = freeMarkerConfigurer.getConfiguration();
			}
		}
		return process(template, model, configuration);
	}

	public static String process(String template, Map<String, ?> model,
			Configuration configuration) {
		if (template == null)
			return null;
		if (configuration == null)
			configuration = new Configuration();
		StringWriter writer = new StringWriter();
		try {
			new Template("template", new StringReader(template), configuration)
					.process(model, writer);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getParameter(String name, Class<T> type,
			Map<String, TemplateModel> params) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(type);
		Assert.notNull(params);
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		Object object = DeepUnwrap.unwrap(model);
		return (T) convertUtilsBean.convert(object, type);

	}

	public static TemplateModel getVariable(String name, Environment env)
			throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		return env.getVariable(name);
	}

	public static void setVariable(String name, Object value, Environment env)
			throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		if (value instanceof TemplateModel)
			env.setVariable(name, (TemplateModel) value);
		else
			env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
	}

	public static void setVariables(Map<String, Object> variables,
			Environment env) throws TemplateModelException {
		Assert.notNull(variables);
		Assert.notNull(env);
		Iterator<Entry<String, Object>> iterator = variables.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> localEntry = (Map.Entry<String, Object>) iterator
					.next();
			String str = (String) localEntry.getKey();
			Object object = localEntry.getValue();
			if (object instanceof TemplateModel)
				env.setVariable(str, (TemplateModel) object);
			else
				env.setVariable(str, ObjectWrapper.BEANS_WRAPPER.wrap(object));
		}
	}

	public static class CustomerConvertUtilsBean extends ConvertUtilsBean {
		public String convert(Object value) {
			if (value != null) {
				Class<? extends Object> classz = value.getClass();
				if ((classz.isEnum()) && (super.lookup(classz) == null)) {
					super.register(new EnumConverter(classz), classz);
				} else if ((classz.isArray())
						&& (classz.getComponentType().isEnum())) {

					if (super.lookup(classz) == null) {
						ArrayConverter converter = new ArrayConverter(classz,
								new EnumConverter(classz.getComponentType()), 0);
						converter.setOnlyFirstToString(false);
						super.register(converter, classz);
					}
					return (String) super.lookup(classz).convert(String.class,
							value);
				}
			}
			return super.convert(value);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(String value, Class clazz) {
			if ((clazz.isEnum()) && (super.lookup(clazz) == null))
				super.register(new EnumConverter(clazz), clazz);
			return super.convert(value, clazz);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(String[] values, Class clazz) {
			if ((clazz.isArray()) && (clazz.getComponentType().isEnum())
					&& (super.lookup(clazz.getComponentType()) == null))
				super.register(new EnumConverter(clazz.getComponentType()),
						clazz.getComponentType());
			return super.convert(values, clazz);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(Object value, Class targetType) {
			if (super.lookup(targetType) == null)
				if (targetType.isEnum()) {
					super.register(new EnumConverter(targetType), targetType);
				} else if ((targetType.isArray())
						&& (targetType.getComponentType().isEnum())) {
					ArrayConverter converter = new ArrayConverter(targetType,
							new EnumConverter(targetType.getComponentType()), 0);
					converter.setOnlyFirstToString(false);
					super.register(converter, targetType);
				}
			return super.convert(value, targetType);
		}

	}
}