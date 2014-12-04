package com.fdp.nonoo.controller.member;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fdp.nonoo.common.Pageable;
import com.fdp.nonoo.common.Setting;
import com.fdp.nonoo.service.MemberService;
import com.fdp.nonoo.util.SettingUtils;

@Controller("memberController")
@RequestMapping({ "/member" })
public class MemberController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", memberService.findPage(pageable));
		model.addAttribute("abc", memberService.findPage(pageable).getContent());
		return "/member/list";
	}

	@RequestMapping(value = { "/index" }, method = { RequestMethod.GET })
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
		return "/member/index";
	}

}