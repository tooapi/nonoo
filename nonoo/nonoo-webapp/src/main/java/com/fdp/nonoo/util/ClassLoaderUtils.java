package com.fdp.nonoo.util;

public class ClassLoaderUtils {

	public static ClassLoader getContextClassLoader() {
		ClassLoader loader = null;
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch (Throwable e) {
		}
		if (loader == null) {
			loader = ClassLoaderUtils.class.getClassLoader();
		}
		return loader;
	}

}
