package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.entity.Member;

public abstract interface MemberService extends BaseService<Member, Long> {

	public List<Member> findListByEmail(String email);

	public boolean usernameDisabled(String username);

	public boolean usernameExists(String username);

	public boolean emailExists(String email);

	public Member findByUsername(String username);

	public Member getCurrent();
	

}