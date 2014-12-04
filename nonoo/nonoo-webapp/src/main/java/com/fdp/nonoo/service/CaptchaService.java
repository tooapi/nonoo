package com.fdp.nonoo.service;

import java.awt.image.BufferedImage;



public abstract interface CaptchaService
{
  public abstract BufferedImage buildImage(String captchaId);

  public abstract boolean isValid( String captchaId, String captcha);
}