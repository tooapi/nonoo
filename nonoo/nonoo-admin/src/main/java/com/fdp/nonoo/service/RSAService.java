package com.fdp.nonoo.service;

import java.security.interfaces.RSAPublicKey;
import javax.servlet.http.HttpServletRequest;

public abstract interface RSAService {
	public abstract RSAPublicKey generateKey(HttpServletRequest request);

	public abstract void removePrivateKey(HttpServletRequest request);

	public abstract String decryptParameter(String password,HttpServletRequest request);
}