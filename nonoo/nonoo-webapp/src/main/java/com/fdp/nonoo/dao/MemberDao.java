package com.fdp.nonoo.dao;

import java.util.List;

import com.fdp.nonoo.entity.Member;

public abstract interface MemberDao extends BaseDao<Member, Long> {
	public abstract boolean usernameExists(String username);
	
	public abstract Member findByUsername(String username);

	public abstract List<Member> findListByEmail(String email);

	public abstract boolean emailExists(String email);

}