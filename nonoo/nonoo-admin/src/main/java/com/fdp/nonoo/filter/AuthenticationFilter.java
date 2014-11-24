package com.fdp.nonoo.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.fdp.nonoo.common.AuthenticationToken;
import com.fdp.nonoo.service.RSAService;

public class AuthenticationFilter extends FormAuthenticationFilter {

	private String enPassword = "password";
	private String captchaId = "captchaId";
	private String captcha = "captcha";

	private String isRememberMe = "isRememberMe";

	@Resource(name = "rsaService")
	private RSAService rsaService;

	protected org.apache.shiro.authc.AuthenticationToken createToken(
			ServletRequest servletRequest, ServletResponse servletResponse) {
		String username = getUsername(servletRequest);
		String password = getPassword(servletRequest);
		String captchaId = getCaptchaId(servletRequest);
		String captcha = getCaptcha(servletRequest);
		boolean isRememberMe = isRememberMe(servletRequest);
		String host = getHost(servletRequest);
		return new AuthenticationToken(username, password, captchaId, captcha,isRememberMe, host);
	}

	protected boolean onAccessDenied(ServletRequest servletRequest,
			ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String str = request.getHeader("X-Requested-With");
		if ((str != null) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		return super.onAccessDenied(request, response);
	}

	protected boolean onLoginSuccess(
			org.apache.shiro.authc.AuthenticationToken token, Subject subject,
			ServletRequest servletRequest, ServletResponse servletResponse)
			throws Exception {
		Session session = subject.getSession();
		Map<Object, Object> attributeKeyMap = new HashMap<Object, Object>();
		Collection<Object> attributeKeys = session.getAttributeKeys();
		Iterator<Object> iterator = attributeKeys.iterator();
		Object attributeKey;
		while (iterator.hasNext()) {
			attributeKey = iterator.next();
			attributeKeyMap.put(attributeKey,session.getAttribute(attributeKey));
		}
		session.stop();
		session = subject.getSession();
		for (Map.Entry<Object, Object> entry : attributeKeyMap.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}
		return super.onLoginSuccess(token, subject, servletRequest,
				servletResponse);
	}

	protected String getPassword(ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String password = rsaService.decryptParameter(enPassword, request);
		rsaService.removePrivateKey(request);
		return password;
	}

	protected String getCaptchaId(ServletRequest servletRequest) {
		String id = WebUtils.getCleanParam(servletRequest, captchaId);
		if (id == null) {
			id = ((HttpServletRequest) servletRequest).getSession().getId();
		}
		return id;
	}

	protected String getCaptcha(ServletRequest servletRequest) {
		return WebUtils.getCleanParam(servletRequest, captcha);
	}

	public String getEnPasswordParam() {
		return this.enPassword;
	}

	public void setEnPasswordParam(String enPasswordParam) {
		this.enPassword = enPasswordParam;
	}

	public String getCaptchaIdParam() {
		return this.captchaId;
	}

	public void setCaptchaIdParam(String captchaIdParam) {

		this.captchaId = captchaIdParam;
	}

	public String getCaptchaParam() {
		
		return this.captcha;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captcha = captchaParam;
	}

	public void setRememberMeParam(String rememberMeParam) {
        this.isRememberMe=rememberMeParam;
	}
	
	public String  getRememberMeParam(){
		  
		return this.isRememberMe;
	}
}