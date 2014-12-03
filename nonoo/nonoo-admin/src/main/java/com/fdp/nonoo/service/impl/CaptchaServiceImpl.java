package com.fdp.nonoo.service.impl;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fdp.nonoo.NonooContext;
import com.fdp.nonoo.service.CaptchaService;

@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;

	public BufferedImage buildImage(String captchaId) {
		return (BufferedImage) imageCaptchaService.getChallengeForID(captchaId);
	}

	public boolean isValid(NonooContext.CaptchaType captchaType, String captchaId,
			String captcha) {
		
		if ((captchaType == null)|| (ArrayUtils.contains(NonooContext.getCaptchatypes(), captchaType))) {
			if ((StringUtils.isNotEmpty(captchaId))&& (StringUtils.isNotEmpty(captcha))) {
				try {
					return imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;
		}
		return true;
	}
}