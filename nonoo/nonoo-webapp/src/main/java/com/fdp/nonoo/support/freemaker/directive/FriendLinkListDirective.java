package com.fdp.nonoo.support.freemaker.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fdp.nonoo.common.Filter;
import com.fdp.nonoo.common.Order;
import com.fdp.nonoo.entity.FriendLink;
import com.fdp.nonoo.service.FriendLinkService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("friendLinkListDirective")
public class FriendLinkListDirective extends BaseDirective {
	private static final String DEFAULT_VARIABLE_NAME = "friendLinks";

	@Resource(name = "friendLinkService")
	private FriendLinkService friendLinkService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = isUseCache(env, params);
		String cacheName = getCacheName(env, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilterParameter(params, FriendLink.class,
				new String[0]);
		List<Order> orders = getOrderParameter(params, new String[0]);
		List friendLinks;
		if (bool) {
			friendLinks = this.friendLinkService.findList(count, filters,orders, cacheName);
		} else{
			friendLinks = this.friendLinkService.findList(count, filters,orders);
		}
		setVariable(DEFAULT_VARIABLE_NAME, friendLinks, env, body);
	}
}