package com.fdp.nonoo.support.freemaker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.fdp.nonoo.common.Message;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("flashMessageDirective")
public class FlashMessageDirective extends BaseDirective {
	public static final String FLASH_MESSAGE_ATTRIBUTE_NAME = FlashMessageDirective.class.getName() + ".FLASH_MESSAGE";

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws IOException, TemplateException {
		RequestAttributes requestAttributes = RequestContextHolder
				.currentRequestAttributes();
		if (requestAttributes == null) {
			return;
		}
		Message message = (Message) requestAttributes.getAttribute(FLASH_MESSAGE_ATTRIBUTE_NAME, 0);
		if (body != null) {
			setVariable("flashMessage", message, env, body);
		} else {
			if (message == null)
				return;
			Writer writer = env.getOut();
			writer.write("$.message(\"" + message.getType() + "\", \""+ message.getContent() + "\");");
		}
	}
}