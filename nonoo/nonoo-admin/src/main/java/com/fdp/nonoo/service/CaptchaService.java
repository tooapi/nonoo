package com.fdp.nonoo.service;

import java.awt.image.BufferedImage;

import com.fdp.nonoo.NonooContext;

public abstract interface CaptchaService {
	public abstract BufferedImage buildImage(String imagePath);

	public abstract boolean isValid(NonooContext.CaptchaType captchaType,String captchaId, String captcha);
}