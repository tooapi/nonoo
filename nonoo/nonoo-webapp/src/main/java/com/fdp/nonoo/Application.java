package com.fdp.nonoo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.fdp.nonoo.util.ClassLoaderUtils;
import com.fdp.nonoo.util.WordUtils;

public class Application {

	private static final Pattern PARAMETER_PATTERN = Pattern
			.compile("\\$\\{([^}]*)\\}");

	private static final String DEFAULT_CONFIG_FILE = "fdp.properties";

	private static final String SYSTEM_NAME = "system.name";

	private static final String PROJECT_NAME = "project.name";
	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_PORT = "mail.smtp.port";
	private static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
	private static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

	private static String systemName;
	private static String projectName;

	private static String mailSmtpHost;

	private static Integer mailSmtpPort;
	private static String  mailSmtpUsername;
	private static String  mailSmtpPassword;

	static {

		Properties config = new Properties();
		config.setProperty(SYSTEM_NAME, "noo");
		config.setProperty(PROJECT_NAME, "noo");
		config.setProperty(MAIL_SMTP_HOST, "");
		config.setProperty(MAIL_SMTP_PORT, "");
		config.setProperty(MAIL_SMTP_USERNAME, "");
		config.setProperty(MAIL_SMTP_PASSWORD, "");
		loadClasspath(DEFAULT_CONFIG_FILE);

	}

	private static void loadClasspath(String classpath) {
		if (classpath.startsWith("/")) {
			classpath = classpath.substring(1);
		}
		InputStream is = ClassLoaderUtils.getContextClassLoader()
				.getResourceAsStream(classpath);
		load(is, classpath);
	}

	private static void load(InputStream is, String name) {
		if (is == null) {
			throw new IllegalStateException("InputStream not found: " + name);
		}
		load(is);
	}

	private static void load(InputStream is) {
		if (is == null) {

		}

		try {
			Properties config = new Properties();
			config.load(is);
			load(config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static void load(Properties config) {
		System.out.println("loading properties");
		Field[] fields;
		if (config == null) {

		}
		fields = Application.class.getDeclaredFields();
		for (Field field : fields) {
			String name = WordUtils.toPropertyName(field.getName());
			System.out.println(name);
			String value = config.getProperty(name);
			if (value == null)
				continue;
			setFieldValue(field, value);
			System.out.println(value);
		}
		
		System.out.println("end");

	}

	@SuppressWarnings("unchecked")
	private static void setFieldValue(Field field, String value) {
		if (field == null || Modifier.isFinal(field.getModifiers()))
			return;
		field.setAccessible(true);
		Class<?> type = field.getType();
		try {
			if (List.class.isAssignableFrom(type)) {
				List<Object> values = (List<Object>) field
						.get(Application.class);
				if (values == null) {
					values = new ArrayList<Object>();
					field.set(Application.class, values);
				}

				type = String.class;

				Type genericType = field.getGenericType();
				if (genericType instanceof ParameterizedType) {
					Type[] actualTypeArgs = ((ParameterizedType) genericType)
							.getActualTypeArguments();
					if (actualTypeArgs != null && actualTypeArgs.length > 0) {
						Type actualType = actualTypeArgs[0];
						if ((actualType instanceof Class)) {
							type = (Class<?>) actualType;
						} else if (actualType instanceof ParameterizedType) {
							type = (Class<?>) ((ParameterizedType) actualType)
									.getRawType();
						}
					}
				}
				if (value == null)
					return;

				for (String val : split(field, value)) {
					val = val.trim();
					if (val.length() > 0) {
						values.add(cast(val, type));
					}
				}
			} else {
				if (value != null) {
					value = value.trim();
				}

				field.set(Application.class, cast(value, type));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected static String[] split(Field field, String value) {
		return value.split(",");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object cast(String value, Class<?> type) {
		if (value == null) {
			return value;
		}
		value = replaceValue(value);

		if (String.class.equals(type)) {
			return value;
		}
		if (value == null || value.length() == 0) {
			if (Boolean.TYPE.equals(type)) {
				return Boolean.FALSE;
			} else if (Integer.TYPE.equals(type)) {
				return Integer.valueOf(0);
			} else if (Long.TYPE.equals(type)) {
				return Long.valueOf(value);
			} else if (Float.TYPE.equals(type)) {
				return Float.valueOf(0);
			} else if (Double.TYPE.equals(type)) {
				return Double.valueOf(0);
			}
			return null;
		}
		if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
			value = value.toLowerCase();
			return "true".equals(value) || "1".equals(value);
		} else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
			return Integer.valueOf(value);
		} else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
			return Long.valueOf(value);
		} else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
			return Float.valueOf(value);
		} else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
			return Double.valueOf(value);
		} else if (type.isEnum()) {
			return Enum.valueOf((Class<Enum>) type, value);
		}
		return value;
	}

	private static String replaceValue(String value) {
		if (value == null) {
			return null;
		}
		if (value.indexOf('$') >= 0) {
			Matcher matcher = PARAMETER_PATTERN.matcher(value);
			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				String val = matcher.group(1);
				val = System.getProperty(val);
				if (val == null) {
					val = "";
				}
				matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
			}
			matcher.appendTail(sb);
			value = sb.toString();
		}
		return value;
	}

	public static void main(String args[]) {

		System.out.println(Application.getSystemName());
		System.out.println(Application.getSystemName());
		System.out.println(Application.getSystemName());
		System.out.println(Application.getSystemName());

	}

	public static void setSystemName(String systemName) {
		Application.systemName = systemName;
	}

	public static String getProjectName() {
		return projectName;
	}

	public static void setProjectName(String projectName) {
		Application.projectName = projectName;
	}

	public static String getSystemName() {

		return systemName;
	}

	public static String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public static void setMailSmtpHost(String mailSmtpHost) {
		Application.mailSmtpHost = mailSmtpHost;
	}

	public static Integer getMailSmtpPort() {
		return mailSmtpPort;
	}

	public static void setMailSmtpPort(Integer mailSmtpPort) {
		Application.mailSmtpPort = mailSmtpPort;
	}

	public static String getMailSmtpUsername() {
		return mailSmtpUsername;
	}

	public static void setMailSmtpUsername(String mailSmtpUsername) {
		Application.mailSmtpUsername = mailSmtpUsername;
	}

	public static String getMailSmtpPassword() {
		return mailSmtpPassword;
	}

	public static void setMailSmtppassword(String mailSmtpPassword) {
		Application.mailSmtpPassword = mailSmtpPassword;
	}

}
