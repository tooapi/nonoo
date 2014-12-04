package com.fdp.nonoo.service;

import java.security.interfaces.RSAPublicKey;
import javax.servlet.http.HttpServletRequest;

public abstract interface RSAService
{
  public abstract RSAPublicKey generateKey(HttpServletRequest requst);

  public abstract void removePrivateKey(HttpServletRequest paramHttpServletRequest);

  public abstract String decryptParameter(String paramString, HttpServletRequest paramHttpServletRequest);
}