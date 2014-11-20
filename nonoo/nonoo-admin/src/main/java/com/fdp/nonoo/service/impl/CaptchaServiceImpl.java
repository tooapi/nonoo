package com.fdp.nonoo.service.impl;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.fdp.nonoo.common.Setting.CaptchaType;
import com.fdp.nonoo.service.CaptchaService;

@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

	public BufferedImage buildImage(String imagePath) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(CaptchaType captchaType, String captchaId,
			String captcha) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}