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

@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {
	private static final String privateKey = "privateKey";

	@Transactional(readOnly = true)
	public RSAPublicKey generateKey(HttpServletRequest request) {
		Assert.notNull(request);
		KeyPair keypair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keypair
				.getPublic();
		RSAPrivateKey localRSAPrivateKey = (RSAPrivateKey) keypair
				.getPrivate();
		HttpSession localHttpSession = request.getSession();
		localHttpSession.setAttribute(privateKey, localRSAPrivateKey);
		return publicKey;
	}

	@Transactional(readOnly = true)
	public void removePrivateKey(HttpServletRequest request) {
		Assert.notNull(request);
		HttpSession localHttpSession = request.getSession();
		localHttpSession.removeAttribute(privateKey);
	}

	@Transactional(readOnly = true)
	public String decryptParameter(String password, HttpServletRequest request) {
		Assert.notNull(request);
		if (password != null) {
			HttpSession session = request.getSession();
			RSAPrivateKey key = (RSAPrivateKey) session
					.getAttribute(privateKey);
			String pass = request.getParameter(password);
			if ((key != null) && (StringUtils.isNotEmpty(pass)))
				return RSAUtils.decrypt(key, pass);
		}
		return null;
	}
}