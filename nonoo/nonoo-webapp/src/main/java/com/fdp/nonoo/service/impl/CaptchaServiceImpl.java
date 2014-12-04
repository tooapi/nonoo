package com.fdp.nonoo.service.impl;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fdp.nonoo.service.CaptchaService;

@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;

	public BufferedImage buildImage(String captchaId) {
		return (BufferedImage) imageCaptchaService.getChallengeForID(captchaId);
	}

	public boolean isValid(String captchaId, String captcha) {

		return true;
	}

}