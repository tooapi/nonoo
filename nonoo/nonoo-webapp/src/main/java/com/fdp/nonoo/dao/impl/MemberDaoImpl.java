package com.fdp.nonoo.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.MemberDao;
import com.fdp.nonoo.entity.Member;

@Repository("memberDao")
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements MemberDao {
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String sql = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long count = (Long) entityManager.createQuery(sql, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("username", username).getSingleResult();
		return count > 0;
	}
	
	public boolean emailExists(String email) {
		if (email == null)
			return false;
		String str = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = entityManager.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getSingleResult();
		return count > 0;
	}


	public Member findByUsername(String username) {
		if (username == null)
			return null;
		try {
			String sql = "select members from Member members where lower(members.username) = lower(:username)";
			return (Member) entityManager.createQuery(sql, Member.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Member> findListByEmail(String email) {

		if (email == null) {
			return Collections.emptyList();
		}
		String sql = "select members from Member members where lower(members.email) = lower(:email)";
		return this.entityManager.createQuery(sql, Member.class)
				.setFlushMode(FlushModeType.COMMIT)
				.setParameter("email", email).getResultList();

	}

}