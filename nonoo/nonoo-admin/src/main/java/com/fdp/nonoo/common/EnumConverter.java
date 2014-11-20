package com.fdp.nonoo.common;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter extends AbstractConverter {
	private final Class<?> enumClass;

	public EnumConverter(Class<?> enumClass) {
		this(enumClass, null);
	}

	public EnumConverter(Class<?> enumClass, Object defaultValue) {
		super(defaultValue);
		this.enumClass = enumClass;
	}

	protected Class<?> getDefaultType() {
		return this.enumClass;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object convertToType(Class type, Object value) {
		String val = value.toString().trim();
		return Enum.valueOf(type, val);
	}

	protected String convertToString(Object value) {
		return value.toString();
	}
}