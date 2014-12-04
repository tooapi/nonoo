package com.fdp.nonoo.controller;

import com.fdp.nonoo.common.Message;

public class BaseController {
	protected static final String front_error = "/front/common/error";
	protected static final Message error = Message.error("front.message.error");
	protected static final Message success = Message.success("front.message.success");
}