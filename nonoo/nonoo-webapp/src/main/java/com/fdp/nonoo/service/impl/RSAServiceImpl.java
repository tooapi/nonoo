package com.fdp.nonoo.service.impl;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fdp.nonoo.service.RSAService;
import com.fdp.nonoo.util.RSAUtils;

@Service("rsaService")
public class RSAServiceImpl implements RSAService {
	private static final String PRIVATE_KEY = "privateKey";

	@Transactional(readOnly = true)
	public RSAPublicKey generateKey(HttpServletRequest request) {
		Assert.notNull(request);
		KeyPair keyPair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		HttpSession session = request.getSession();
		session.setAttribute(PRIVATE_KEY, privateKey);
		return publicKey;
	}

	@Transactional(readOnly = true)
	public void removePrivateKey(HttpServletRequest request) {
		Assert.notNull(request);
		HttpSession localHttpSession = request.getSession();
		localHttpSession.removeAttribute(PRIVATE_KEY);
	}

	@Transactional(readOnly = true)
	public String decryptParameter(String name, HttpServletRequest request) {
		Assert.notNull(request);
		if (name != null) {
			HttpSession session = request.getSession();
			RSAPrivateKey privateKey = (RSAPrivateKey) session.getAttribute(PRIVATE_KEY);
			String str = request.getParameter(name);
			if ((privateKey != null) && (StringUtils.isNotEmpty(str)))
				return RSAUtils.decrypt(privateKey, str);
		}
		return null;
	}
}