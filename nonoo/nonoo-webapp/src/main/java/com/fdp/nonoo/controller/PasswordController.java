package com.fdp.nonoo.controller;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.common.Setting;
import com.fdp.nonoo.entity.Member;
import com.fdp.nonoo.entity.SafeKey;
import com.fdp.nonoo.service.CaptchaService;
import com.fdp.nonoo.service.MailService;
import com.fdp.nonoo.service.MemberService;
import com.fdp.nonoo.util.SettingUtils;

@Controller("passwordController")
@RequestMapping({ "/password" })
public class PasswordController extends BaseController {

	@Resource(name = "memberService")
	private MemberService memberService;

	@Resource(name = "mailService")
	private MailService mailService;

	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	@RequestMapping(value = { "/find" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message find(String captchaId, String captcha, String username,
			String email) {
		if (!(this.captchaService.isValid(captchaId, captcha)))
			return Message.error("shop.captcha.invalid");
		if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(email)))
			return Message.error("shop.common.invalid");
		Member member = this.memberService.findByUsername(username);
		if (member == null)
			return Message.error("shop.password.memberNotExist");
		if (!(member.getEmail().equalsIgnoreCase(email)))
			return Message.error("shop.password.invalidEmail");
		Setting setting = SettingUtils.get();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(UUID.randomUUID().toString()+ DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
		safeKey.setExpire((setting.getSafeKeyExpiryTime().intValue() != 0) ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime().intValue()) : null);
		member.setSafeKey(safeKey);
		memberService.update(member);
		mailService.sendFindPasswordMail(member.getEmail(),member.getUsername(), safeKey);
		return Message.success("shop.password.mailSuccess");
	}

}
