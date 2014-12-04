package com.fdp.nonoo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fdp.nonoo.entity.Member;
import com.fdp.nonoo.util.CookieUtils;

@Controller("logoutController")
public class LogoutController extends BaseController
{
  @RequestMapping(value={"/logout"}, method={RequestMethod.GET})
  public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session)
  {
	System.out.println("用户退出");
    session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
    CookieUtils.removeCookie(request, response, "username");
    return "redirect:/";
  }
}