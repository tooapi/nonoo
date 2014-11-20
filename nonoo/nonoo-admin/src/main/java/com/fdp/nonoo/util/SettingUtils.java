package com.fdp.nonoo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

import com.fdp.nonoo.common.CommonAttributes;
import com.fdp.nonoo.common.EnumConverter;
import com.fdp.nonoo.common.Setting;

public final class SettingUtils {
	private static final CacheManager cacheManager = CacheManager.create();
	private static final BeanUtilsBean beanUtils;

	static {
		SettingUtilsBean settingUtilsBean = new SettingUtilsBean();
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		settingUtilsBean.register(localDateConverter, Date.class);
		beanUtils = new BeanUtilsBean(settingUtilsBean);
	}

	@SuppressWarnings("unchecked")
	public static Setting get() {
		Ehcache ehcache = cacheManager.getEhcache(Setting.CACHE_NAME);
		Element element = ehcache.get(Setting.CACHE_KEY);
		Setting setting = null;
		if (element != null) {
			setting = (Setting) element.getObjectValue();
		} else {

			try {
				setting = new Setting();
				File file = new ClassPathResource("/fdp.xml").getFile();
				Document document = new SAXReader().read(file);
				List<Node> nodeList = document.selectNodes("/fdp/setting");
				Iterator<Node> localIterator = nodeList.iterator();
				while (localIterator.hasNext()) {
					org.dom4j.Element temp = (org.dom4j.Element) localIterator
							.next();
					String name = temp.attributeValue("name");
					String value = temp.attributeValue("value");

					try {
						beanUtils.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					ehcache.put(new Element(Setting.CACHE_KEY, setting));

				}
			} catch (IOException e) {
				e.printStackTrace();

			} catch (DocumentException e) {
				e.printStackTrace();

			}

		}
		return setting;
	}

	@SuppressWarnings("unchecked")
	public static void set(Setting setting) {
		try {
			File file = new ClassPathResource("/fdp.xml").getFile();
			Document document = new SAXReader().read(file);
			List<Node> localList = document.selectNodes("/fdp/setting");
			Iterator<Node> localIterator = localList.iterator();
			while (localIterator.hasNext()) {
				org.dom4j.Element temp = (org.dom4j.Element) localIterator
						.next();

				String name = temp.attributeValue("name");
				String value = beanUtils.getProperty(setting, name);
				Attribute localAttribute = temp.attribute("value");
				localAttribute.setValue(value);

			}
			OutputStream output = null;
			XMLWriter writer = null;
			try {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				format.setIndent(true);
				format.setIndent("\t");
				format.setNewlines(true);
				output = new FileOutputStream(file);
				writer = new XMLWriter(output, format);
				writer.write(document);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (writer != null)
					try {
						writer.close();
					} catch (IOException localIOException4) {
					}
				IOUtils.closeQuietly(output);
			}
			Ehcache ehcache = cacheManager.getEhcache(Setting.CACHE_NAME);
			ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class SettingUtilsBean extends ConvertUtilsBean {
		public SettingUtilsBean() {

		}

		@SuppressWarnings("rawtypes")
		public String convert(Object value) {
			if (value != null) {
				Class localClass = value.getClass();
				if ((localClass.isEnum()) && (super.lookup(localClass) == null)) {
					super.register(new EnumConverter(localClass), localClass);
				} else if ((localClass.isArray())
						&& (localClass.getComponentType().isEnum())) {
					if (super.lookup(localClass) == null) {
						ArrayConverter converter = new ArrayConverter(
								localClass, new EnumConverter(
										localClass.getComponentType()), 0);
						converter.setOnlyFirstToString(false);
						super.register(converter, localClass);
					}
					Object localObject = super.lookup(localClass);
					return (String) ((Converter) localObject).convert(
							String.class, value);
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
					ArrayConverter arrayConverter = new ArrayConverter(
							targetType, new EnumConverter(
									targetType.getComponentType()), 0);
					arrayConverter.setOnlyFirstToString(false);
					super.register(arrayConverter, targetType);
				}
			return super.convert(value, targetType);
		}
	}
}