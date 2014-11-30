package com.fdp.nonoo.admin.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdp.nonoo.common.DateEditor;
import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.common.Setting;
import com.fdp.nonoo.entity.Log;
import com.fdp.nonoo.support.freemaker.directive.FlashMessageDirective;
import com.fdp.nonoo.util.SettingUtils;
import com.fdp.nonoo.util.SpringUtils;

public class BaseController {
	protected static final String admin_error = "common/error";
	protected static final Message error = Message.error("admin.message.error",new Object[0]);
	protected static final Message success = Message.success("admin.message.success", new Object[0]);
	private static final String constraintViolations = "constraintViolations";

	@Resource(name = "validator")
	private Validator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}

	protected <T> boolean validate(T t, Class<?>... groups) {
		Set<ConstraintViolation<T>> violations = validator.validate(t, groups);
		if (violations.isEmpty()) {
			return true;
		}
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(constraintViolations, violations, 0);
		return false;
	}

	protected <T> boolean validatorValue(Class<T> beanType,
			String propertyName, Object value, Class<?>... groups) {

		Set<ConstraintViolation<T>> constrains = validator.validateValue(beanType, propertyName, value, groups);
		if (constrains.isEmpty()){
			return true;
		}
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(constraintViolations, constrains, 0);
		return false;
	}

	protected String currency(BigDecimal paramBigDecimal,
			boolean paramBoolean1, boolean paramBoolean2) {
		Setting setting = SettingUtils.get();
		String str = setting.setScale(paramBigDecimal).toString();
		if (paramBoolean1)
			str = setting.getCurrencySign() + str;
		if (paramBoolean2)
			str = str + setting.getCurrencyUnit();
		return str;
	}

	protected String getMessage(String message, Object[] params) {
		return SpringUtils.getMessage(message, params);
	}

	protected void redirect(RedirectAttributes attributes, Message message) {
		if ((attributes == null) || (message == null))
			return;
		attributes.addFlashAttribute(FlashMessageDirective.FLASH_MESSAGE_ATTRIBUTE_NAME, message);
	}

	protected void log(String message) {
		if (message == null)
			return;
		RequestAttributes requestAttributes = RequestContextHolder
				.currentRequestAttributes();
		requestAttributes.setAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME, message,
				0);
	}

}