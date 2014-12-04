package com.fdp.nonoo.controller;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.common.Principal;
import com.fdp.nonoo.common.Setting;
import com.fdp.nonoo.entity.Member;
import com.fdp.nonoo.service.MemberService;
import com.fdp.nonoo.util.CookieUtils;
import com.fdp.nonoo.util.SettingUtils;

@Controller("registerController")
@RequestMapping({ "/register" })
public class RegisterController {

	@Resource(name = "memberService")
	private MemberService memberService;

	@RequestMapping(value = { "/check_username" }, method = { RequestMethod.GET })
	@ResponseBody
	public boolean checkUsername(String username) {

		if (StringUtils.isEmpty(username))
			return false;
		return ((!(memberService.usernameDisabled(username))) && (!(memberService
				.usernameExists(username))));

	}

	@RequestMapping(value = { "/check_email" }, method = { RequestMethod.GET })
	@ResponseBody
	public boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email))
			return false;
		return !(memberService.emailExists(email));
	}

	@RequestMapping(method = { RequestMethod.GET })
	public String index(String redirectUrl, HttpServletRequest request,
			ModelMap model) {
		Setting setting = SettingUtils.get();
		if ((redirectUrl != null)
				&& (!(redirectUrl.equalsIgnoreCase(setting.getSiteUrl())))
				&& (!(redirectUrl.startsWith(request.getContextPath() + "/")))
				&& (!(redirectUrl.startsWith(setting.getSiteUrl() + "/"))))
			redirectUrl = null;
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/register/index";
	}

	@RequestMapping(value = { "/submit" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message submit(String captchaId, String password, String username,
			String email, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		if ((memberService.usernameDisabled(username))
				|| (memberService.usernameExists(username)))
			return Message.error("front.register.disabledExist");

		Member member = new Member();
		member.setUsername(username.toLowerCase());
		member.setPassword(DigestUtils.md5Hex(password));
		member.setEmail(email);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());

		memberService.save(member);
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(
				member.getId(), member.getUsername()));
		CookieUtils.addCookie(request, response, "username", member.getUsername());
		return Message.success("front.register.success");

	}

}
