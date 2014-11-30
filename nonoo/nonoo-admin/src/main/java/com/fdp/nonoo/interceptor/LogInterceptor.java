package com.fdp.nonoo.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fdp.nonoo.common.LogConfig;
import com.fdp.nonoo.entity.Log;
import com.fdp.nonoo.service.AdminService;
import com.fdp.nonoo.service.LogConfigService;
import com.fdp.nonoo.service.LogService;

public class LogInterceptor extends HandlerInterceptorAdapter {
	private static final String[] DEFAULT_IGNORE_PARAMETERS = { "password",
			"rePassword", "currentPassword" };
	private static AntPathMatcher antPathMatcher = new AntPathMatcher();
	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Resource(name = "logConfigService")
	private LogConfigService logConfigService;

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "adminService")
	private AdminService adminService;

	@SuppressWarnings("unchecked")
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		List<LogConfig> configList = logConfigService.getAll();
		if (configList == null)
			return;
		String path = request.getServletPath();
		Iterator<LogConfig> iterator = configList.iterator();
		while (iterator.hasNext()) {
			LogConfig logConfig = (LogConfig) iterator.next();
			if (!antPathMatcher.match(logConfig.getUrlPattern(), path))
				continue;
			String cunrrentUserName = adminService.getCurrentUsername();
			String operation = logConfig.getOperation();
			String operator = cunrrentUserName;
			String content = (String) request
					.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
			String ip = request.getRemoteAddr();
			request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
			StringBuffer stringBuffer = new StringBuffer();

			Map<String, String[]> map = request.getParameterMap();

			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				if (ArrayUtils.contains(ignoreParameters, entry.getKey()))
					continue;
				String[] values = (String[]) entry.getValue();
				if (values == null)
					continue;
				for (String value : values)
					stringBuffer.append(entry.getKey() + " = " + value + "\n");
			}

			Log log = new Log();
			log.setOperation(operation);
			log.setOperator(operator);
			log.setContent(content);
			log.setParameter(stringBuffer.toString());
			log.setIp(ip);
			logService.save(log);
			return;
		}
	}

	public String[] getIgnoreParameters() {
		return this.ignoreParameters;
	}

	public void setIgnoreParameters(String[] ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}
}