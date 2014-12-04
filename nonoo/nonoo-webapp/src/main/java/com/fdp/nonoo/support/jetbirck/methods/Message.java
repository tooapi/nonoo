package com.fdp.nonoo.support.jetbirck.methods;

import com.fdp.nonoo.ApplicationContext;

public class Message {

	public static String get(String message) {

		String msg = ApplicationContext.getMessage(message, null);
		System.out.println(msg);
		return msg;
	}

}
