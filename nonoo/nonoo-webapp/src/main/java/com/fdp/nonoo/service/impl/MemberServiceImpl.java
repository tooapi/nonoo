package com.fdp.nonoo.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fdp.nonoo.common.Principal;
import com.fdp.nonoo.dao.MemberDao;
import com.fdp.nonoo.entity.Member;
import com.fdp.nonoo.service.MemberService;

@Service("memberService")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements
		MemberService {

	@Resource(name = "memberDao")
	private MemberDao memberDao;

	@Resource(name = "memberDao")
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}

	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		RequestAttributes attributes = RequestContextHolder
				.currentRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) attributes)
					.getRequest();
			Principal principal = (Principal) request.getSession()
					.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null)
				return this.memberDao.find(principal.getId());
		}
		return null;
	}
}
