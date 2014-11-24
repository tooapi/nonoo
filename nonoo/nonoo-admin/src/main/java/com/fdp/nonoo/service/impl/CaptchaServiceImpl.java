package com.fdp.nonoo.service.impl;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fdp.nonoo.common.Setting;
import com.fdp.nonoo.service.CaptchaService;
import com.fdp.nonoo.util.SettingUtils;

@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;

	public BufferedImage buildImage(String captchaId) {
		return (BufferedImage) this.imageCaptchaService
				.getChallengeForID(captchaId);
	}

	public boolean isValid(Setting.CaptchaType captchaType, String captchaId,
			String captcha) {
		Setting setting = SettingUtils.get();
		if ((captchaType == null)
				|| (ArrayUtils.contains(setting.getCaptchaTypes(), captchaType))) {
			if ((StringUtils.isNotEmpty(captchaId))
					&& (StringUtils.isNotEmpty(captcha))) {
				try {
					return this.imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
				} catch (Exception e) {
					return false;
				}
			}
			return false;
		}
		return true;
	}
}