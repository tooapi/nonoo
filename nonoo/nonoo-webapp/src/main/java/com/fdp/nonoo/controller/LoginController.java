package com.fdp.nonoo.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@Controller("loginController")
@RequestMapping({ "/login" })
public class LoginController extends BaseController {

	@Resource(name = "memberService")
	private MemberService memberService;

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
		return "/login/index";
	}

	@RequestMapping(value = { "/submit" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message submit(String username, String password,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(password))) {
			return Message.error("请输入用户名和密码");
		}
		Member member = null;
		if (username.contains("@")) {
			List<Member> users = memberService.findListByEmail(username);
			if (users.isEmpty()) {
				return Message.error("用户不存在或者密码错误");
			}
			if (users.size() == 1) {
				member = users.get(0);
			}
		} else {

			member = memberService.findByUsername(username);
		}

		if (member == null)
			return Message.error("front.login.unknownAccount");
		if (!(member.getIsEnabled()))
			return Message.error("front.login.disabledAccount");

		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		memberService.update(member);
		session.invalidate();
		session = request.getSession();
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
		CookieUtils.addCookie(request, response, "username",member.getUsername());

		return success;
	}
}
