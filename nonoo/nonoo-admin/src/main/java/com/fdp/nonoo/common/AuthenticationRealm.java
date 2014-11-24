package com.fdp.nonoo.common;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.fdp.nonoo.entity.Admin;
import com.fdp.nonoo.service.AdminService;
import com.fdp.nonoo.service.CaptchaService;
import com.fdp.nonoo.util.SettingUtils;


public class AuthenticationRealm extends AuthorizingRealm {
	@Resource(name = "captchaService")
	private CaptchaService captchaService;

	@Resource(name = "adminService")
	private AdminService adminService;

	protected AuthenticationInfo doGetAuthenticationInfo(
			org.apache.shiro.authc.AuthenticationToken token)
			throws AuthenticationException {
		AuthenticationToken authToken = (AuthenticationToken) token;
		String username = authToken.getUsername();
		String password = new String(authToken.getPassword());
		String captchaId = authToken.getCaptchaId();
		String captcha = authToken.getCaptcha();
		String hostIp = authToken.getHost();
		if (!captchaService.isValid(Setting.CaptchaType.adminLogin, captchaId,captcha)) {
			throw new UnsupportedTokenException();
		}

		if ((username != null) && (password != null)) {

			Admin admin = adminService.findByUsername(username);
			if (admin == null) {
				throw new UnknownAccountException();
			}
			if (!admin.getIsEnabled()) {
				throw new DisabledAccountException();
			}
			Setting setting = SettingUtils.get();
			int i;
			if (admin.getIsLocked()) {
				if (ArrayUtils.contains(setting.getAccountLockTypes(),
						Setting.AccountLockType.admin)) {
					i = setting.getAccountLockTime();
					if (i == 0) {
						throw new LockedAccountException();
					}
					Date lockDate = admin.getLockedDate();
					Date avliableDate = DateUtils.addMinutes(lockDate, i);
					if (new Date().after(avliableDate)) {
						admin.setLoginFailureCount(0);
						admin.setIsLocked((false));
						admin.setLockedDate(null);
						adminService.update(admin);

					}
					throw new LockedAccountException();
				}
				admin.setLoginFailureCount(0);
				admin.setIsLocked(false);
				admin.setLockedDate(null);
				adminService.update(admin);
			}
			if (!password.equals(admin.getPassword())) {
				i = admin.getLoginFailureCount() + 1;
				if (i >= setting.getAccountLockCount()) {
					admin.setIsLocked(true);
					admin.setLockedDate(new Date());
				}
				admin.setLoginFailureCount(i);
				adminService.update(admin);
				throw new IncorrectCredentialsException();
			}
			admin.setLoginIp(hostIp);
			admin.setLoginDate(new Date());
			admin.setLoginFailureCount(0);
			adminService.update(admin);
			return new SimpleAuthenticationInfo(new Principal(admin.getId(),
					username), password, getName());
		} else {
			throw new UnknownAccountException();
		}
	}

	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Principal principal = (Principal) principals.fromRealm(getName()).iterator().next();
		if (principal != null) {
			List<String> authorityList = adminService.findAuthorities(principal
					.getId());
			if (authorityList != null) {
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
				authorizationInfo.addStringPermissions(authorityList);
				return authorizationInfo;
			}
		}
		return null;
	}

}
